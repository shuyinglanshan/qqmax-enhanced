package com.tencent.qqlitepro.util;

/**
 * 宿主崩溃 / 异常日志收集。
 *
 * 覆盖范围：
 *  1. 未捕获异常（Thread.setDefaultUncaughtExceptionHandler）—— 宿主所有线程
 *  2. 主线程 Looper 的未捕获异常（Looper.loop 抛出的）
 *  3. 手动上报的受检异常（catch 里调用 {@link #record}）
 *  4. 关键生命周期事件（用于还原崩溃前的操作路径）
 *
 * 落盘位置（与模块日志同目录，按天分文件，便于定位）：
 *  /sdcard/QQMax增强/crash/crash.log（单文件滚动，内容去重）
 *
 * 特性：
 *  - 每条记录独立时间戳（yyyy-MM-dd HH:mm:ss.SSS），一眼区分
 *  - 完全异步，崩溃时同步 flush（崩溃后没机会再异步写）
 *  - 自带体积上限（单文件 4MB，超出滚动为 .1）
 *  - 绝不抛异常（自身任何失败都静默，避免二次崩溃）
 */
public final class CrashLog {

    private static final String DIR_NAME = "QQMax增强/crash";
    /** 统一日志文件名（单文件滚动，不再每次启动新建） */
    private static final String FILE = "crash.log";
    private static final long MAX_BYTES = 4 * 1024 * 1024L;

    /** 崩溃前最近的操作轨迹（环形缓冲，最多 40 条） */
    private static final int BREADCRUMB_MAX = 40;
    private static final String[] BREADCRUMBS = new String[BREADCRUMB_MAX];
    private static volatile int sBreadcrumbIdx = 0;

    private static final Object LOCK = new Object();
    private static volatile boolean sInstalled = false;
    private static volatile java.lang.Thread.UncaughtExceptionHandler sPrevHandler = null;

    private static final ThreadLocal<java.text.SimpleDateFormat> TS_FULL =
            new ThreadLocal<java.text.SimpleDateFormat>() {
                @Override
                protected java.text.SimpleDateFormat initialValue() {
                    return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS",
                            java.util.Locale.US);
                }
            };

    private CrashLog() {
    }

    /* ===================== 安装 ===================== */

    /**
     * 安装全局崩溃捕获。可重复调用（幂等）。
     * 会在宿主原有 handler 之前记录，然后继续交给原 handler，
     * 不改变宿主的崩溃行为（该弹崩溃页仍会弹）。
     */
    public static void install() {
        if (sInstalled) {
            return;
        }
        sInstalled = true;
        try {
            sPrevHandler = Thread.getDefaultUncaughtExceptionHandler();
            Thread.setDefaultUncaughtExceptionHandler(
                    new java.lang.Thread.UncaughtExceptionHandler() {
                        @Override
                        public void uncaughtException(java.lang.Thread t, java.lang.Throwable e) {
                            try {
                                /* 宿主闪退：独立格式 + 原始错误 */
                                hostCrash(e, t);
                            } catch (Throwable ignored) {
                            }
                            try {
                                writeCrash("未捕获异常", t, e);
                            } catch (Throwable ignored) {
                            }
                            /* 交还给宿主原生 handler，保持原有行为 */
                            java.lang.Thread.UncaughtExceptionHandler prev = sPrevHandler;
                            if (prev != null) {
                                prev.uncaughtException(t, e);
                            }
                        }
                    });
            /*
             * 启动标记：只在"模块版本或进程"变化时写一次。
             * 之前每次启动都写一条 90 多字节的冗余行，日志被刷满没意义。
             */
            try {
                String sig = "boot|" + processName() + "|" + versionName();
                StringBuilder boot = new StringBuilder();
                boot.append(now()).append("  [启动] 模块已加载  process=")
                        .append(processName()).append("  version=")
                        .append(versionName()).append('\n');
                appendDedup(sig, boot.toString());
            } catch (Throwable ignored) {
            }
            /*
             * 兜底：hook 主线程 Looper 的 messageLogging（部分 ROM 的崩溃会绕过
             * default handler）。这里不替换日志器，只在主线程发生 FATAL 时补记一条。
             * 真正的堆栈仍由 default handler 记录，两者互补。
             */
            try {
                installMainLooperWatchdog();
            } catch (Throwable ignored) {
            }
            Log.i("崩溃日志: 全局异常捕获已安装 -> " + crashFilePath());
        } catch (Throwable t) {
            Log.w("崩溃日志: 安装失败 " + t);
        }
    }

    /**
     * 主线程看门狗：每 5 秒打一条心跳轨迹。
     * 若进程在主线程死锁/ANR 后被杀，日志里能看到"最后一次心跳"时间，
     * 从而定位卡死时段（这是纯 crash handler 抓不到的场景）。
     */
    private static void installMainLooperWatchdog() {
        final android.os.Handler h = new android.os.Handler(android.os.Looper.getMainLooper());
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    breadcrumb("主线程心跳");
                } catch (Throwable ignored) {
                }
                h.postDelayed(this, 30000L);
            }
        }, 30000L);
    }

    /** 记录一条操作轨迹（崩溃时一并输出，便于还原现场） */
    public static void breadcrumb(String what) {
        try {
            if (what == null) {
                return;
            }
            synchronized (BREADCRUMBS) {
                BREADCRUMBS[sBreadcrumbIdx % BREADCRUMB_MAX] =
                        now() + "  " + what;
                sBreadcrumbIdx++;
            }
        } catch (Throwable ignored) {
        }
    }

    /* ===================== 手动记录 ===================== */

    /**
     * 模块功能异常日志（带原始报错，便于定位）。
     *
     * 与 {@link #record} 的区别：这里明确标记来源为「模块」，并强制附带
     * 原始异常类名 + message + 完整堆栈 + 调用位置，方便排查模块自身 bug。
     *
     * @param feature 功能名（如 "反馈页" / "点赞列表" / "消息统计"）
     */
    public static void moduleError(String feature, Throwable e) {
        try {
            StringBuilder sb = new StringBuilder(2048);
            sb.append(now()).append("  [模块异常] ").append(feature).append('\n');
            sb.append("  原始错误 : ").append(rawOf(e)).append('\n');
            sb.append("  调用位置 : ").append(callerOf()).append('\n');
            sb.append("  进程     : ").append(processName())
                    .append("   版本: ").append(versionName()).append('\n');
            if (e != null) {
                sb.append("---------- 原始堆栈 ----------\n");
                sb.append(stackOf(e));
            }
            sb.append('\n');
            String sig = "mod|" + feature + "|" + rawOf(e) + "|" + firstFrame(e);
            appendDedup(sig, sb.toString());
        } catch (Throwable ignored) {
        }
    }

    /**
     * 宿主闪退日志（模块观测到的宿主进程异常退出）。
     *
     * 两个来源：
     *  1) CrashLog 的 uncaught handler（宿主未捕获异常）
     *  2) MainHook 里检测到的 ActivityThread 崩溃
     * 这里统一格式化，并保留原始 Throwable。
     */
    public static void hostCrash(Throwable e, java.lang.Thread t) {
        try {
            StringBuilder sb = new StringBuilder(4096);
            sb.append("\n========== 宿主闪退 ").append(now()).append(" ==========\n");
            sb.append("原始错误 : ").append(rawOf(e)).append('\n');
            sb.append("线程     : ").append(t == null ? "?" : t.getName())
                    .append(" (id=").append(t == null ? -1 : t.getId()).append(")\n");
            sb.append("进程     : ").append(processName()).append('\n');
            sb.append("版本     : ").append(versionName()).append('\n');
            sb.append("设备     : ").append(android.os.Build.MANUFACTURER).append(' ')
                    .append(android.os.Build.MODEL).append(" / Android ")
                    .append(android.os.Build.VERSION.RELEASE)
                    .append(" (API ").append(android.os.Build.VERSION.SDK_INT).append(")\n");
            /* 崩溃前 20 条轨迹 */
            sb.append("---------- 操作轨迹 ----------\n");
            try {
                synchronized (BREADCRUMBS) {
                    int total = sBreadcrumbIdx;
                    int start = Math.max(0, total - 20);
                    for (int i = start; i < total; i++) {
                        String b = BREADCRUMBS[i % BREADCRUMB_MAX];
                        if (b != null) {
                            sb.append("  ").append(b).append('\n');
                        }
                    }
                }
            } catch (Throwable ignored) {
            }
            sb.append("---------- 原始堆栈 ----------\n");
            if (e != null) {
                sb.append(stackOf(e));
            }
            sb.append("========== END ").append(now()).append(" ==========\n\n");
            String sig = "hostcrash|" + rawOf(e) + "|" + firstFrame(e);
            appendDedup(sig, sb.toString());
        } catch (Throwable ignored) {
        }
    }

    /** 异常的一句话原文：类名: message */
    private static String rawOf(Throwable e) {
        try {
            if (e == null) {
                return "null";
            }
            String msg = e.getMessage();
            String cause = "";
            Throwable c = e.getCause();
            if (c != null && c != e) {
                cause = "  <- caused by " + c.getClass().getName()
                        + (c.getMessage() == null ? "" : ": " + c.getMessage());
            }
            return e.getClass().getName() + (msg == null ? "" : ": " + msg) + cause;
        } catch (Throwable ignored) {
            return String.valueOf(e);
        }
    }

    /** 当前调用位置（模块内第一个非 CrashLog 的栈帧） */
    private static String callerOf() {
        try {
            StackTraceElement[] st = new Throwable().getStackTrace();
            for (StackTraceElement el : st) {
                String cn = el.getClassName();
                if (cn.contains("CrashLog")) {
                    continue;
                }
                return cn + "." + el.getMethodName()
                        + "(" + el.getFileName() + ":" + el.getLineNumber() + ")";
            }
        } catch (Throwable ignored) {
        }
        return "?";
    }

    /** 记录一个被捕获的异常（非崩溃，但值得留痕） */
    public static void record(String scene, Throwable e) {
        try {
            StringBuilder sb = new StringBuilder(1024);
            sb.append(now()).append("  [").append(scene).append("]  ")
                    .append(e == null ? "null" : e.getClass().getName())
                    .append(": ").append(e == null ? "" : String.valueOf(e.getMessage()))
                    .append('\n');
            if (e != null) {
                sb.append(stackOf(e));
            }
            /* 同一场景 + 同一异常 + 同一首帧 → 只记一次 */
            String sig = "exc|" + scene + "|"
                    + (e == null ? "null" : e.getClass().getName() + "|" + e.getMessage())
                    + "|" + firstFrame(e);
            appendDedup(sig, sb.toString());
        } catch (Throwable ignored) {
        }
    }

    /* ===================== 内部 ===================== */

    private static void writeCrash(String kind, java.lang.Thread t, java.lang.Throwable e) {
        StringBuilder sb = new StringBuilder(8192);
        sb.append("\n");
        sb.append("========== CRASH ").append(now()).append(" ==========\n");
        sb.append("类型   : ").append(kind).append('\n');
        sb.append("线程   : ").append(t == null ? "?" : t.getName())
                .append(" (id=").append(t == null ? -1 : t.getId()).append(")\n");
        sb.append("异常   : ").append(e == null ? "null" : e.getClass().getName())
                .append('\n');
        sb.append("消息   : ").append(e == null ? "" : String.valueOf(e.getMessage()))
                .append('\n');
        sb.append("进程   : ").append(processName()).append('\n');
        sb.append("版本   : ").append(versionName()).append('\n');
        sb.append("设备   : ").append(android.os.Build.MANUFACTURER).append(' ')
                .append(android.os.Build.MODEL).append(" / Android ")
                .append(android.os.Build.VERSION.RELEASE)
                .append(" (API ").append(android.os.Build.VERSION.SDK_INT).append(")\n");
        sb.append("---------- 崩溃前操作轨迹 ----------\n");
        try {
            synchronized (BREADCRUMBS) {
                int total = sBreadcrumbIdx;
                int start = Math.max(0, total - BREADCRUMB_MAX);
                for (int i = start; i < total; i++) {
                    String b = BREADCRUMBS[i % BREADCRUMB_MAX];
                    if (b != null) {
                        sb.append("  ").append(b).append('\n');
                    }
                }
            }
        } catch (Throwable ignored) {
        }
        sb.append("---------- 堆栈 ----------\n");
        if (e != null) {
            sb.append(stackOf(e));
        }
        sb.append("========== END ").append(now()).append(" ==========\n\n");
        /* 同一崩溃（异常类型 + 消息 + 首个业务栈帧）只记录一次 */
        String sig = "crash|" + kind + "|"
                + (e == null ? "null" : e.getClass().getName() + "|" + e.getMessage())
                + "|" + firstFrame(e);
        appendDedup(sig, sb.toString());
    }

    /** 取第一个栈帧（用于判定"同一处崩溃"） */
    private static String firstFrame(Throwable e) {
        try {
            if (e == null) {
                return "-";
            }
            StackTraceElement[] st = e.getStackTrace();
            if (st != null && st.length > 0) {
                return String.valueOf(st[0]);
            }
        } catch (Throwable ignored) {
        }
        return "-";
    }

    private static String stackOf(Throwable e) {
        try {
            java.io.StringWriter sw = new java.io.StringWriter();
            java.io.PrintWriter pw = new java.io.PrintWriter(sw);
            e.printStackTrace(pw);
            pw.flush();
            return sw.toString();
        } catch (Throwable ignored) {
            return String.valueOf(e);
        }
    }

    private static String now() {
        try {
            return TS_FULL.get().format(new java.util.Date());
        } catch (Throwable ignored) {
            return String.valueOf(System.currentTimeMillis());
        }
    }

    private static String processName() {
        /* 最可靠：直接读 /proc/self/cmdline（无需权限） */
        try {
            java.io.BufferedReader br = new java.io.BufferedReader(
                    new java.io.FileReader("/proc/self/cmdline"));
            String line = br.readLine();
            br.close();
            if (line != null) {
                int z = line.indexOf('\u0000');
                if (z > 0) {
                    line = line.substring(0, z);
                }
                line = line.trim();
                if (!line.isEmpty()) {
                    return line;
                }
            }
        } catch (Throwable ignored) {
        }
        try {
            android.content.Context ctx = ctxOrNull();
            if (ctx != null) {
                int pid = android.os.Process.myPid();
                android.app.ActivityManager am = (android.app.ActivityManager)
                        ctx.getSystemService(android.content.Context.ACTIVITY_SERVICE);
                if (am != null) {
                    java.util.List<android.app.ActivityManager.RunningAppProcessInfo> list =
                            am.getRunningAppProcesses();
                    if (list != null) {
                        for (android.app.ActivityManager.RunningAppProcessInfo p : list) {
                            if (p != null && p.pid == pid && p.processName != null) {
                                return p.processName;
                            }
                        }
                    }
                }
            }
        } catch (Throwable ignored) {
        }
        return "?";
    }

    private static String versionName() {
        try {
            return com.tencent.qqlitepro.BuildConfig.VERSION_NAME;
        } catch (Throwable ignored) {
            return "?";
        }
    }

    private static android.content.Context ctxOrNull() {
        try {
            return com.tencent.qqlitepro.util.Ctx.get();
        } catch (Throwable ignored) {
        }
        return null;
    }

    /* ============ 内容去重 ============ */

    /** 已写入内容指纹（避免重复内容反复落盘） */
    private static final java.util.Set<String> sWrittenHash =
            java.util.Collections.newSetFromMap(new java.util.concurrent.ConcurrentHashMap<>());
    /** 指纹集合上限 */
    private static final int WRITTEN_HASH_MAX = 200;

    private static String hashOf(String text) {
        try {
            java.security.MessageDigest md =
                    java.security.MessageDigest.getInstance("SHA-256");
            byte[] d = md.digest(text.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder(32);
            for (int i = 0; i < 16; i++) {
                sb.append(String.format("%02x", d[i]));
            }
            return sb.toString();
        } catch (Throwable t) {
            return String.valueOf(text.hashCode());
        }
    }

    /**
     * 去重写入：完全相同的内容只写一次。
     *
     * @param sig  业务指纹（如 "boot|主进程|1.2.3"、"crash|类名|首帧栈帧"）。
     *             相同 sig 视为同一事件，不再重复写。
     * @param text 要写入的完整文本
     * @return true = 实际写入了
     */
    private static boolean appendDedup(String sig, String text) {
        try {
            String key = hashOf(sig == null ? text : sig);
            if (!sWrittenHash.add(key)) {
                return false;   /* 已写过，跳过 */
            }
            if (sWrittenHash.size() > WRITTEN_HASH_MAX) {
                sWrittenHash.clear();
                sWrittenHash.add(key);
            }
            appendSync(text);
            return true;
        } catch (Throwable t) {
            try {
                appendSync(text);
            } catch (Throwable ignored) {
            }
            return true;
        }
    }

    /** 崩溃路径是"最后机会"，同步写，不用队列；失败重试 3 次（含重建目录） */
    private static void appendSync(String text) {
        synchronized (LOCK) {
            byte[] bytes;
            try {
                bytes = text.getBytes("UTF-8");
            } catch (Throwable t) {
                bytes = text.getBytes();
            }
            /* 第一次写；失败则重建目录后重试，最多 3 轮 */
            for (int attempt = 0; attempt < 3; attempt++) {
                java.io.FileOutputStream fos = null;
                try {
                    java.io.File f = crashFile();
                    if (f == null) {
                        /* 目录可能被清理：强制重建一次 */
                        sForceRebuildDir = true;
                        continue;
                    }
                    if (f.exists() && f.length() > MAX_BYTES) {
                        java.io.File old = new java.io.File(f.getParentFile(),
                                f.getName() + ".1");
                        try {
                            if (old.exists()) {
                                old.delete();
                            }
                            f.renameTo(old);
                        } catch (Throwable ignored) {
                        }
                    }
                    fos = new java.io.FileOutputStream(f, true);
                    fos.write(bytes);
                    fos.flush();
                    /* 关键：崩溃后进程随时被杀，必须 fsync 落盘 */
                    try {
                        fos.getFD().sync();
                    } catch (Throwable ignored) {
                    }
                    return;   /* 成功 */
                } catch (Throwable t) {
                    if (attempt == 2) {
                        return;   /* 彻底失败，静默 */
                    }
                    try {
                        Thread.sleep(20L);
                    } catch (Throwable ignored) {
                    }
                } finally {
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (Throwable ignored) {
                        }
                    }
                }
            }
        }
    }

    /** 标记需要重建目录 */
    private static volatile boolean sForceRebuildDir = false;

    /** 崩溃日志文件（按天）；失败时回退到应用私有目录 */
    private static java.io.File crashFile() {
        try {
            if (sForceRebuildDir) {
                sForceRebuildDir = false;
                try {
                    java.io.File ext = android.os.Environment.getExternalStorageDirectory();
                    if (ext != null) {
                        java.io.File d = new java.io.File(ext, DIR_NAME);
                        if (!d.exists()) {
                            d.mkdirs();
                        }
                    }
                } catch (Throwable ignored) {
                }
            }
            java.io.File dir = null;
            try {
                java.io.File ext = android.os.Environment.getExternalStorageDirectory();
                if (ext != null) {
                    dir = new java.io.File(ext, DIR_NAME);
                    if (!dir.exists() && !dir.mkdirs()) {
                        dir = null;
                    }
                }
            } catch (Throwable ignored) {
            }
            if (dir == null) {
                android.content.Context ctx = ctxOrNull();
                if (ctx != null && ctx.getExternalFilesDir(null) != null) {
                    dir = new java.io.File(ctx.getExternalFilesDir(null), "crash");
                    if (!dir.exists() && !dir.mkdirs()) {
                        dir = null;
                    }
                }
            }
            if (dir == null) {
                return null;
            }
            /*
             * 单一滚动文件：crash.log
             * 不再"每次进程启动新建一份"，避免产生大量无意义小文件。
             * 超过 MAX_BYTES 时滚动为 crash.log.1（只保留上一份）。
             */
            return new java.io.File(dir, FILE);
        } catch (Throwable ignored) {
            return null;
        }
    }

    /* ============ 异常条目解析（供反馈页列出可上报的错误） ============ */

    /** 从崩溃日志里解析出的一条异常 */
    public static class Entry {
        /** 去重用的稳定键（异常类型 + 消息 + 首帧） */
        public String key;
        /** 一行展示文本（如 "java.lang.NullPointerException: xxx"） */
        public String title;
        /** 来源：模块异常 / 宿主闪退 / 未捕获异常 */
        public String kind;
        /** 该条目的完整原文（含堆栈、轨迹、设备信息） */
        public String raw;
        /** 发生时间文本 */
        public String time;
    }

    /**
     * 读取崩溃日志文件并解析成条目列表。
     * 完全相同的异常（key 相同）只保留一条（保留最早出现的完整原文）。
     *
     * @return 永不为 null；文件不存在时为空列表
     */
    public static java.util.List<Entry> readEntries() {
        java.util.ArrayList<Entry> out = new java.util.ArrayList<>();
        java.util.Map<String, Entry> seen = new java.util.LinkedHashMap<>();
        try {
            java.io.File f = crashFile();
            if (f == null || !f.exists() || f.length() <= 0) {
                return out;
            }
            String all = readTextFile(f);
            if (all == null || all.isEmpty()) {
                return out;
            }
            /* 按分隔符切块：模块异常单行开头 / 宿主闪退 & 未捕获异常用 ===== 包裹 */
            String[] lines = all.split("\n");
            StringBuilder cur = new StringBuilder();
            String curKey = null;
            String curTitle = null;
            String curKind = null;
            String curTime = null;

            for (String line : lines) {
                boolean isStart =
                        line.contains("[模块异常]")
                        || line.contains("========== 宿主闪退")
                        || line.contains("========== CRASH")
                        || line.contains("[启动]")
                        || line.contains("[异常]");
                boolean isEnd = line.contains("========== END");

                if (isStart) {
                    /* 收尾上一条 */
                    if (curKey != null) {
                        addEntry(seen, cur);
                    }
                    cur.setLength(0);
                    curKey = null;
                    curTitle = null;
                    curKind = null;
                    curTime = extractTime(line);
                }
                if (cur.length() > 0 || isStart) {
                    cur.append(line).append('\n');
                }
                if (isStart) {
                    if (line.contains("[模块异常]")) {
                        curKind = "模块异常";
                        int i = line.indexOf("[模块异常]");
                        curTitle = line.substring(i + 6).trim();
                    } else if (line.contains("宿主闪退")) {
                        curKind = "宿主闪退";
                    } else if (line.contains("========== CRASH")) {
                        curKind = "未捕获异常";
                    } else if (line.contains("[启动]")) {
                        curKind = "启动";
                        curTitle = "模块启动";
                    } else if (line.contains("[异常]")) {
                        curKind = "异常";
                    }
                }
                /* 从内容里提取异常标题（第一处 "xxxException"/"Error" 行） */
                if (curTitle == null && curKind != null && !"启动".equals(curKind)) {
                    String t = pickThrowableLine(line);
                    if (t != null) {
                        curTitle = t;
                    }
                }
                if (isEnd) {
                    curKey = buildKey(curKind, curTitle, cur.toString());
                    if (curKey != null) {
                        addEntry(seen, cur);
                    }
                    cur.setLength(0);
                    curKey = null;
                    curTitle = null;
                }
            }
            /* 收尾最后一条（模块异常没有 END 标记） */
            if (cur.length() > 0) {
                if (curTitle == null) {
                    curTitle = pickThrowableLine(cur.toString());
                }
                if (curTitle != null) {
                    addEntry(seen, cur);
                }
            }
            out.addAll(seen.values());

            /*
             * 兜底：文件里没有可识别条目（例如只有旧格式内容），
             * 但确实存在日志时，提供一条"整个文件"的条目，
             * 保证用户在反馈页始终有东西可选、不会空手而归。
             */
            if (out.isEmpty() && all != null && !all.trim().isEmpty()) {
                Entry e = new Entry();
                e.key = "raw|" + hashOf(all);
                e.kind = "原始日志";
                e.title = firstMeaningfulLine(all);
                if (e.title == null) {
                    e.title = "崩溃日志全文";
                }
                e.raw = all;
                e.time = extractTime(all);
                out.add(e);
            }
        } catch (Throwable ignored) {
        }
        return out;
    }

    /** 收尾/入库：把当前块整理成 Entry */
    private static void addEntry(java.util.Map<String, Entry> seen, StringBuilder cur) {
        try {
            String raw = cur.toString();
            if (raw.trim().isEmpty()) {
                return;
            }
            String title = pickThrowableLine(raw);
            if (title == null) {
                title = firstMeaningfulLine(raw);
            }
            if (title == null) {
                return;
            }
            String kind = raw.contains("[模块异常]") ? "模块异常"
                    : raw.contains("宿主闪退") ? "宿主闪退"
                    : raw.contains("========== CRASH") ? "未捕获异常" : "其它";
            String key = kind + "|" + title;
            if (seen.containsKey(key)) {
                return;   /* 完全相同的问题只留一条 */
            }
            Entry e = new Entry();
            e.key = key;
            e.title = title;
            e.kind = kind;
            e.raw = raw;
            e.time = extractTime(raw);
            seen.put(key, e);
        } catch (Throwable ignored) {
        }
    }

    private static String buildKey(String kind, String title, String raw) {
        try {
            String t = title != null ? title : pickThrowableLine(raw);
            if (t == null) {
                return null;
            }
            return (kind == null ? "?" : kind) + "|" + t;
        } catch (Throwable ignored) {
            return null;
        }
    }

    /** 从一段文本里挑出异常标题行（类名: 消息） */
    private static String pickThrowableLine(String text) {
        try {
            String[] ls = text.split("\n");
            for (String l : ls) {
                String t = l.trim();
                /* 形如 java.lang.XxxException: msg 或 com.x.YyyError */
                if (t.startsWith("原始错误")) {
                    int c = t.indexOf(':');
                    if (c > 0) {
                        String v = t.substring(c + 1).trim();
                        if (!v.isEmpty()) {
                            return v;
                        }
                    }
                }
                if (t.matches("^[a-zA-Z_$][\\w$]*(\\.[a-zA-Z_$][\\w$]*)+"
                        + "(Exception|Error|Throwable).*")) {
                    return t;
                }
            }
        } catch (Throwable ignored) {
        }
        return null;
    }

    private static String firstMeaningfulLine(String text) {
        try {
            String[] ls = text.split("\n");
            for (String l : ls) {
                String t = l.trim();
                if (!t.isEmpty() && !t.startsWith("=")) {
                    return t.length() > 120 ? t.substring(0, 120) + "…" : t;
                }
            }
        } catch (Throwable ignored) {
        }
        return null;
    }

    private static String extractTime(String text) {
        try {
            java.util.regex.Matcher m = java.util.regex.Pattern.compile(
                    "(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{3})")
                    .matcher(text);
            if (m.find()) {
                return m.group(1);
            }
        } catch (Throwable ignored) {
        }
        return "";
    }

    private static String readTextFile(java.io.File f) {
        java.io.FileInputStream fis = null;
        try {
            fis = new java.io.FileInputStream(f);
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream();
            byte[] buf = new byte[8192];
            int n;
            while ((n = fis.read(buf)) > 0) {
                bos.write(buf, 0, n);
                if (bos.size() > 4 * 1024 * 1024) {
                    break;
                }
            }
            return new String(bos.toByteArray(), "UTF-8");
        } catch (Throwable ignored) {
            return null;
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (Throwable ignored) {
                }
            }
        }
    }

    /**
     * 写出一个"只含指定条目"的临时日志文件（供上传）。
     * 文件名格式：qqmax_<13位毫秒时间戳>.txt
     *
     * @return 临时文件；失败返回 null
     */
    public static java.io.File writeTempEntryFile(Entry e) {
        try {
            if (e == null || e.raw == null || e.raw.isEmpty()) {
                return null;
            }
            android.content.Context ctx = ctxOrNull();
            java.io.File dir = null;
            try {
                if (ctx != null && ctx.getExternalFilesDir(null) != null) {
                    dir = new java.io.File(ctx.getExternalFilesDir(null), "tmp");
                }
            } catch (Throwable ignored) {
            }
            if (dir == null) {
                try {
                    dir = new java.io.File(android.os.Environment.getExternalStorageDirectory(),
                            DIR_NAME + "/tmp");
                } catch (Throwable ignored) {
                }
            }
            if (dir == null) {
                return null;
            }
            if (!dir.exists() && !dir.mkdirs()) {
                return null;
            }
            long ts = System.currentTimeMillis();
            java.io.File out = new java.io.File(dir, "qqmax_" + ts + ".txt");
            java.io.FileOutputStream fos = new java.io.FileOutputStream(out, false);
            fos.write(e.raw.getBytes("UTF-8"));
            fos.flush();
            try {
                fos.getFD().sync();
            } catch (Throwable ignored) {
            }
            fos.close();
            return out;
        } catch (Throwable ignored) {
            return null;
        }
    }

    /** 上传成功后清除临时文件 */
    public static void deleteTempFile(java.io.File f) {
        try {
            if (f != null && f.exists()) {
                f.delete();
            }
        } catch (Throwable ignored) {
        }
    }

    /** 供设置页展示崩溃日志路径 */
    public static String crashFilePath() {
        try {
            java.io.File f = crashFile();
            return f == null ? "(不可用)" : f.getAbsolutePath();
        } catch (Throwable ignored) {
            return "(不可用)";
        }
    }

    /** 列出所有崩溃日志文件（新到旧） */
    public static java.util.List<java.io.File> listFiles() {
        java.util.ArrayList<java.io.File> out = new java.util.ArrayList<java.io.File>();
        try {
            java.io.File f = crashFile();
            if (f == null) {
                return out;
            }
            java.io.File dir = f.getParentFile();
            if (dir == null || !dir.exists()) {
                return out;
            }
            java.io.File[] all = dir.listFiles();
            if (all == null) {
                return out;
            }
            for (java.io.File one : all) {
                if (one != null && one.isFile()
                        && one.getName().startsWith(FILE.replace(".log", ""))) {
                    out.add(one);
                }
            }
            java.util.Collections.sort(out, new java.util.Comparator<java.io.File>() {
                @Override
                public int compare(java.io.File a, java.io.File b) {
                    return Long.compare(b.lastModified(), a.lastModified());
                }
            });
            /* 带时间戳后文件数会增多：超过 30 份时删除最旧的，避免无限堆积 */
            if (out.size() > 3) {
                for (int i = 3; i < out.size(); i++) {
                    try {
                        out.get(i).delete();
                    } catch (Throwable ignored) {
                    }
                }
                while (out.size() > 3) {
                    out.remove(out.size() - 1);
                }
            }
        } catch (Throwable ignored) {
        }
        return out;
    }
}
