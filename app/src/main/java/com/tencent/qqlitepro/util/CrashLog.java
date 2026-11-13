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
 *  /sdcard/QQMax增强/crash/crash_YYYYMMDD.log
 *
 * 特性：
 *  - 每条记录独立时间戳（yyyy-MM-dd HH:mm:ss.SSS），一眼区分
 *  - 完全异步，崩溃时同步 flush（崩溃后没机会再异步写）
 *  - 自带体积上限（单文件 4MB，超出滚动为 .1）
 *  - 绝不抛异常（自身任何失败都静默，避免二次崩溃）
 */
public final class CrashLog {

    private static final String DIR_NAME = "QQMax增强/crash";
    private static final String PREFIX = "crash_";
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
            /* 写一条启动标记：验证文件可写，也让用户能确认模块已生效 */
            try {
                StringBuilder boot = new StringBuilder();
                boot.append(now()).append("  [启动] 模块已加载  process=")
                        .append(processName()).append("  version=")
                        .append(versionName()).append('\n');
                appendSync(boot.toString());
            } catch (Throwable ignored) {
            }
            Log.i("崩溃日志: 全局异常捕获已安装 -> " + crashFilePath());
        } catch (Throwable t) {
            Log.w("崩溃日志: 安装失败 " + t);
        }
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
            appendSync(sb.toString());
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
        appendSync(sb.toString());
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

    /** 崩溃路径是"最后机会"，同步写，不用队列 */
    private static void appendSync(String text) {
        synchronized (LOCK) {
            java.io.FileOutputStream fos = null;
            try {
                java.io.File f = crashFile();
                if (f == null) {
                    return;
                }
                /* 超过上限：滚动 */
                if (f.exists() && f.length() > MAX_BYTES) {
                    java.io.File old = new java.io.File(f.getParentFile(), f.getName() + ".1");
                    try {
                        if (old.exists()) {
                            old.delete();
                        }
                        f.renameTo(old);
                    } catch (Throwable ignored) {
                    }
                }
                fos = new java.io.FileOutputStream(f, true);
                fos.write(text.getBytes("UTF-8"));
                fos.flush();
            } catch (Throwable ignored) {
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

    /** 崩溃日志文件（按天）；失败时回退到应用私有目录 */
    private static java.io.File crashFile() {
        try {
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
            java.text.SimpleDateFormat dsf =
                    new java.text.SimpleDateFormat("yyyyMMdd", java.util.Locale.US);
            return new java.io.File(dir, PREFIX + dsf.format(new java.util.Date()) + ".log");
        } catch (Throwable ignored) {
            return null;
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
                if (one != null && one.isFile() && one.getName().startsWith(PREFIX)) {
                    out.add(one);
                }
            }
            java.util.Collections.sort(out, new java.util.Comparator<java.io.File>() {
                @Override
                public int compare(java.io.File a, java.io.File b) {
                    return Long.compare(b.lastModified(), a.lastModified());
                }
            });
        } catch (Throwable ignored) {
        }
        return out;
    }
}
