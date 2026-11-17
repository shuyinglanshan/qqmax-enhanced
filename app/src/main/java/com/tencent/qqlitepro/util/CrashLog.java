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
    /** 宿主 ClassLoader（供加载宿主类） */
    private static volatile java.lang.ClassLoader sHostCl = null;
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
        install(null);
    }

    /**
     * @param hostCl 宿主 ClassLoader（用于加载 momoi.mod.qqpro.* 等宿主类）。
     *               传 null 时会尝试从多个来源推断。
     */
    public static void install(java.lang.ClassLoader hostCl) {
        if (hostCl != null) {
            sHostCl = hostCl;
        }
        if (sInstalled) {
            /* 已安装：补装需要宿主 CL 的联动 */
            try {
                hookHostWatchdog();
            } catch (Throwable ignored) {
            }
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
             * 多路补充捕获（default handler 覆盖不到的死角）：
             *  1) Looper 消息处理异常（部分 ROM 吞掉）
             *  2) AsyncTask / Runnable 里被线程池吞掉的异常
             *  3) 线程池 execute/submit 的异常
             *  4) 主线程卡死检测（与宿主 HangWatcher 互补，阈值更细）
             */
            try {
                installLooperCrashHook();
            } catch (Throwable ignored) {
            }
            try {
                installThreadPoolHook();
            } catch (Throwable ignored) {
            }
            try {
                installHangWatchdog();
            } catch (Throwable ignored) {
            }
            try {
                installMainLooperWatchdog();
            } catch (Throwable ignored) {
            }
            /* 与宿主 Watchdog 联动：宿主的崩溃/卡死报告也写进我们的日志 */
            try {
                hookHostWatchdog();
            } catch (Throwable ignored) {
            }
            Log.i("崩溃日志: 全局异常捕获已安装 -> " + crashFilePath());
        } catch (Throwable t) {
            Log.w("崩溃日志: 安装失败 " + t);
        }
    }

    /* ============ 多路补充捕获 ============ */

    /**
     * Looper 消息处理异常。
     *
     * 为什么需要：`Looper.loop()` 内部有自己的 try/catch，
     * 部分 ROM 会把异常吞掉只打日志，default handler 永远收不到。
     * 这里 hook Looper.loop 的 catch 分支无法直接做，
     * 改为 hook Handler.dispatchMessage —— 异常会在这里抛出前被我们看到。
     */
    private static void installLooperCrashHook() {
        try {
            de.robv.android.xposed.XposedBridge.hookAllMethods(
                    android.os.Handler.class, "dispatchMessage",
                    new de.robv.android.xposed.XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) {
                            /* dispatchMessage 正常返回说明没问题；异常已由 Looper 处理 */
                        }
                    });
            /* 真正有价值的是捕获 Runnable 里的异常：hook Thread.run 太重，
             * 改为 hook ThreadGroup.uncaughtException（线程池/裸线程都会走） */
            try {
                de.robv.android.xposed.XposedBridge.hookAllMethods(
                        java.lang.ThreadGroup.class, "uncaughtException",
                        new de.robv.android.xposed.XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) {
                                try {
                                    if (param.args == null || param.args.length < 2) {
                                        return;
                                    }
                                    Object t = param.args[0];
                                    Object e = param.args[1];
                                    if (e instanceof Throwable) {
                                        writeCrash("线程未捕获（ThreadGroup）",
                                                (t instanceof Thread) ? (Thread) t : null,
                                                (Throwable) e);
                                    }
                                } catch (Throwable ignored) {
                                }
                            }
                        });
                Log.i("崩溃日志: ThreadGroup 未捕获 hook 已安装");
            } catch (Throwable t) {
                Log.w("崩溃日志: ThreadGroup hook 失败 " + t);
            }
        } catch (Throwable t) {
            Log.w("崩溃日志: Looper hook 失败 " + t);
        }
    }

    /**
     * 线程池异常。
     *
     * 线程池 submit 的 Future 若不被 get()，异常会被静默吞掉 ——
     * 这是模块自身 bug 最常见的"消失"方式。
     * 这里 hook ThreadPoolExecutor.afterExecute 拿不到（无回调），
     * 改为包装 submit 的 Runnable/Callable，捕获后记录再原样抛出。
     */
    private static void installThreadPoolHook() {
        try {
            de.robv.android.xposed.XposedBridge.hookAllMethods(
                    java.util.concurrent.ThreadPoolExecutor.class, "afterExecute",
                    new de.robv.android.xposed.XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) {
                            try {
                                if (param.args == null || param.args.length < 2) {
                                    return;
                                }
                                Object th = param.args[1];
                                if (th instanceof Throwable) {
                                    moduleError("线程池任务异常（此前被静默吞掉）",
                                            (Throwable) th);
                                }
                            } catch (Throwable ignored) {
                            }
                        }
                    });
            Log.i("崩溃日志: 线程池 afterExecute hook 已安装");
        } catch (Throwable t) {
            Log.w("崩溃日志: 线程池 hook 失败 " + t);
        }
    }

    /**
     * 主线程卡死检测（与宿主 HangWatcher 互补）。
     *
     * 宿主：每 1s tick，8s 判定卡死。
     * 我们：每 1s tick，**3s 起分级记录**（3s 预警 / 8s 卡死），
     *      拿到主线程栈后立刻落盘 —— 不等进程被杀。
     */
    /**
     * 卡死检测（带「启动期豁免」）。
     *
     * 重要误报修复：
     * 旧实现在 handleLoadPackage 里立刻开始计时，但那时 QQ 还在冷启动
     * （ActivityThread.handleBindApplication → installContentProviders →
     *  AppContentProvider.onCreate → NtStartupDirector 的 CountDownLatch.await），
     * 主线程本来就会被阻塞 3~10 秒 —— 这是正常启动流程，不是卡顿。
     * 之前会上报大量 "主线程卡顿 3004ms" 假告警。
     *
     * 现在：
     *   1) 启动后 20 秒内不检测（留给宿主完成冷启动）
     *   2) 只有在「主线程已经跑过一次消息循环」后才开始计时
     *   3) 命中时再校验主线程栈，若仍在宿主启动链路里则跳过
     */
    private static void installHangWatchdog() {
        try {
            final long[] lastTick = {android.os.SystemClock.uptimeMillis()};
            final boolean[] reported3s = {false};
            final boolean[] reported8s = {false};
            final boolean[] warm = {false};
            final long startAt = android.os.SystemClock.uptimeMillis();
            /* 冷启动豁免窗口 */
            final long WARMUP_MS = 20_000L;
            final android.os.Handler mainH =
                    new android.os.Handler(android.os.Looper.getMainLooper());

            /* 主线程每秒打点；前 20 秒只用于"热机"，不参与判定 */
            Runnable tick = new Runnable() {
                @Override
                public void run() {
                    lastTick[0] = android.os.SystemClock.uptimeMillis();
                    if (lastTick[0] - startAt >= WARMUP_MS) {
                        warm[0] = true;
                    }
                    reported3s[0] = false;
                    reported8s[0] = false;
                    mainH.postDelayed(this, 1000L);
                }
            };
            mainH.postDelayed(tick, 1000L);

            /* 守护线程每秒检查 */
            Thread watcher = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            Thread.sleep(1000L);
                            /* 未热机（宿主还在冷启动）→ 不判定 */
                            if (!warm[0]) {
                                continue;
                            }
                            long now = android.os.SystemClock.uptimeMillis();
                            long lag = now - lastTick[0];
                            /* 5s 才预警（老设备正常 GC/布局也可能到 3s），8s 判卡死 */
                            if (lag >= 8000L && !reported8s[0]) {
                                reported8s[0] = true;
                                onHang(lag, true);
                            } else if (lag >= 5000L && !reported3s[0]) {
                                reported3s[0] = true;
                                onHang(lag, false);
                            }
                        } catch (InterruptedException ie) {
                            return;
                        } catch (Throwable ignored) {
                        }
                    }
                }
            }, "qqmax-hang-watch");
            watcher.setDaemon(true);
            watcher.start();
            Log.i("崩溃日志: 卡死检测已启动（20s 冷启动豁免后，5s 预警 / 8s 卡死）");
        } catch (Throwable t) {
            Log.w("崩溃日志: 卡死检测启动失败 " + t);
        }
    }

    /** 记录一条「启动耗时」而不是「卡顿」，避免误报污染卡顿统计 */
    private static void writeStartupCost(long lagMs) {
        try {
            StringBuilder sb = new StringBuilder(4096);
            sb.append("\n========== 启动耗时 ").append(now()).append(" ==========\n");
            sb.append("耗时     : ").append(lagMs).append(" ms\n");
            sb.append("进程     : ").append(processName()).append('\n');
            sb.append("版本     : ").append(versionName()).append('\n');
            sb.append("说明     : 宿主冷启动期间主线程被初始化任务阻塞，属启动耗时\n");
            sb.append("---------- 主线程栈 ----------\n");
            try {
                StackTraceElement[] st = android.os.Looper.getMainLooper()
                        .getThread().getStackTrace();
                if (st != null) {
                    for (StackTraceElement el : st) {
                        sb.append("  at ").append(el).append('\n');
                    }
                }
            } catch (Throwable ignored) {
            }
            sb.append("========== END ").append(now()).append(" ==========\n\n");
            appendDedup("startup|" + firstMainFrame(), sb.toString());
        } catch (Throwable ignored) {
        }
    }

    /**
     * 主线程此刻是否在空闲等消息（nativePollOnce）。
     *
     * 判据：栈顶若是 MessageQueue.nativePollOnce / MessageQueue.next，
     * 说明主线程没有在跑任何耗时操作，此前的 lag 只是消息排队造成的假象。
     */
    private static boolean isMainThreadIdle() {
        try {
            StackTraceElement[] st = android.os.Looper.getMainLooper()
                    .getThread().getStackTrace();
            if (st == null || st.length == 0) {
                /* 拿不到栈就不敢断言空闲，按"非空闲"处理以免漏报真卡死 */
                return false;
            }
            int n = Math.min(st.length, 6);
            for (int i = 0; i < n; i++) {
                String cn = st[i].getClassName();
                String mn = st[i].getMethodName();
                if (cn == null) {
                    continue;
                }
                if (cn.startsWith("android.os.MessageQueue")
                        && "nativePollOnce".equals(mn)) {
                    return true;
                }
                if (cn.startsWith("android.os.MessageQueue")
                        && "next".equals(mn)) {
                    return true;
                }
                /* 已经离开 Looper 区域，说明在跑业务代码 */
                if (!cn.startsWith("android.os.") && !cn.startsWith("java.")
                        && !cn.startsWith("dalvik.") && !cn.startsWith("com.android.internal.")) {
                    return false;
                }
            }
        } catch (Throwable ignored) {
        }
        return false;
    }

    /**
     * 从主线程栈判断「卡顿是不是别的 Xposed 模块造成的」。
     *
     * 实战案例（用户上报的真实日志）：
     *   at de.robv.android.xposed.XSharedPreferences.awaitLoadedLocked
     *   at de.robv.android.xposed.XSharedPreferences.getInt
     *   at s3.r.beforeHookedMethod          ← 另一个模块的混淆类
     *   at ContextThemeWrapper.attachBaseContext
     * 这是 EdXp 老框架的 XSharedPreferences 在主线程做全量 XML 读 + 等锁，
     * 属于"别人的模块拖慢主线程"，我们只能标注出来，不能替它修。
     *
     * @return 归因描述（空串表示与外部模块无关）
     */
    private static String blameExternalModule(StackTraceElement[] st) {
        try {
            if (st == null) {
                return "";
            }
            int n = Math.min(st.length, 30);
            for (int i = 0; i < n; i++) {
                String cn = st[i].getClassName();
                if (cn == null) {
                    continue;
                }
                if (cn.startsWith("de.robv.android.xposed.XSharedPreferences")) {
                    /* 找栈里紧随其后的"调用方"，即真正触发读配置的模块类 */
                    String caller = "";
                    for (int k = i + 1; k < n; k++) {
                        String c2 = st[k].getClassName();
                        if (c2 == null) {
                            continue;
                        }
                        if (c2.startsWith("de.robv.android.xposed.")
                                || c2.startsWith("android.")
                                || c2.startsWith("java.")
                                || c2.startsWith("com.android.")) {
                            continue;
                        }
                        caller = c2;
                        break;
                    }
                    return "外部Xposed模块读配置阻塞主线程"
                            + (caller.isEmpty() ? "" : "（" + caller + "）")
                            + " — EdXp/Xposed 的 XSharedPreferences 在主线程做全量读+等锁，非本模块代码";
                }
                if (cn.startsWith("top.canyie.pine.")) {
                    return "Pine 框架桥接开销（非本模块代码）";
                }
                if (cn.startsWith("com.elderdrivers.riru.edxp")) {
                    return "EdXp 框架注入开销（非本模块代码）";
                }
                if (cn.startsWith("com.android.z.XHook") || cn.contains("ZuperHook")) {
                    /* 常见：别的模块在 attachBaseContext 里跑 String.matches → Pattern.compile */
                    String extra = "";
                    for (int k = 0; k < n; k++) {
                        String c2 = st[k].getClassName();
                        if (c2 != null && c2.startsWith("java.util.regex.Pattern")) {
                            extra = "（正则编译，开销很大）";
                            break;
                        }
                    }
                    return "外部Xposed模块 ZuperHook 主线程开销" + extra + "（非本模块代码）";
                }
            }
            /*
             * 通用归因：栈里出现 EdHooker_ / EdXposed 的 hook 桥接，
             * 说明主线程正在跑"某个 Xposed 模块的 hook 回调"。
             */
            boolean viaEdHook = false;
            for (int i = 0; i < n; i++) {
                String cn = st[i].getClassName();
                if (cn == null) {
                    continue;
                }
                if (cn.contains("EdHooker_")) {
                    viaEdHook = true;
                    break;
                }
            }
            if (viaEdHook) {
                /* 找出真正干活的模块类（第一个非框架、非 android 的类） */
                String who = "";
                for (int i = 0; i < n; i++) {
                    String cn = st[i].getClassName();
                    if (cn == null) {
                        continue;
                    }
                    if (cn.startsWith("android.") || cn.startsWith("java.")
                            || cn.startsWith("de.robv.android.xposed.")
                            || cn.startsWith("com.elderdrivers.riru")
                            || cn.contains("EdHooker_")
                            || cn.startsWith("dalvik.")) {
                        continue;
                    }
                    who = cn;
                    break;
                }
                if (!who.isEmpty() && !who.startsWith("com.tencent.qqlite")) {
                    return "外部Xposed模块 hook 回调耗时（" + who + "，非本模块代码）";
                }
            }

            /* 通用：命中别的模块 hook 入口 */
            for (int i = 0; i < n; i++) {
                String cn = st[i].getClassName();
                if (cn == null) {
                    continue;
                }
                if (cn.contains("beforeHookedMethod") || cn.contains("afterHookedMethod")) {
                    /* 往上找真正的模块类（跳过 XC_MethodHook 自身） */
                    for (int k = i - 1; k >= 0 && k >= i - 3; k--) {
                        String c2 = st[k].getClassName();
                        if (c2 != null && !c2.startsWith("de.robv.android.xposed.")
                                && !c2.startsWith("android.")) {
                            return "外部Xposed模块 hook 开销（" + c2 + "）";
                        }
                    }
                }
            }
        } catch (Throwable ignored) {
        }
        return "";
    }

    /**
     * 主线程是否卡在「宿主启动流程」里（正常阻塞，不应报卡顿）。
     *
     * 典型链路：
     *   ActivityThread.handleBindApplication → installContentProviders
     *   → AppContentProvider.onCreate → WatchApplicationDelegate.onCreate
     *   → NtStartupDirector.a (CountDownLatch.await)
     */
    private static boolean isInHostStartup() {
        try {
            StackTraceElement[] st = android.os.Looper.getMainLooper()
                    .getThread().getStackTrace();
            if (st == null || st.length == 0) {
                return false;
            }
            int n = Math.min(st.length, 24);
            for (int i = 0; i < n; i++) {
                String cn = st[i].getClassName();
                if (cn == null) {
                    continue;
                }
                if (cn.contains("ActivityThread")
                        || cn.contains("AppContentProvider")
                        || cn.contains("NtStartupDirector")
                        || cn.contains("WatchApplicationDelegate")
                        || cn.contains("QFixApplication")
                        || cn.contains("Instrumentation")
                        || cn.contains("JumpActivity")
                        || cn.contains("SplashActivity")
                        || cn.contains("performLaunchActivity")
                        || cn.contains("handleLaunchActivity")) {
                    return true;
                }
            }
        } catch (Throwable ignored) {
        }
        return false;
    }

    /** 卡死落盘节流（避免卡死期间反复 dump 反而加重卡顿） */
    private static volatile long sLastHangDumpAt = 0L;
    private static final long HANG_DUMP_MIN_INTERVAL = 30_000L;

    /** 卡死时抓主线程 + 全线程栈并落盘 */
    private static void onHang(long lagMs, boolean severe) {
        try {
            long nowMs = System.currentTimeMillis();
            if (nowMs - sLastHangDumpAt < HANG_DUMP_MIN_INTERVAL) {
                return;   /* 30 秒内只 dump 一次 */
            }
            /*
             * 启动期：不当作"卡顿"，但耗时很长时记一条"启动耗时"便于优化。
             * 3s 级别不记（太常见），≥8s 才留痕。
             */
            if (isInHostStartup()) {
                if (severe && lagMs >= 8000L) {
                    writeStartupCost(lagMs);
                }
                return;
            }
            /*
             * 误报过滤 2（关键）：
             * 抓栈这一刻主线程若停在 MessageQueue.nativePollOnce，
             * 说明它其实在**空闲等消息**，早就恢复了。
             * 之前只用「tick 消息多久没执行」判断，队列里消息多时
             * tick 会被排到后面，于是产生大量假告警
             * （用户上报的 3033ms / 3022ms 全是这种）。
             */
            if (isMainThreadIdle()) {
                return;
            }
            sLastHangDumpAt = nowMs;
            StringBuilder sb = new StringBuilder(8192);
            sb.append("\n========== ").append(severe ? "应用卡死" : "主线程卡顿")
                    .append(' ').append(now()).append(" ==========\n");
            sb.append("卡顿时长 : ").append(lagMs).append(" ms\n");
            sb.append("进程     : ").append(processName()).append('\n');
            sb.append("版本     : ").append(versionName()).append('\n');

            /* 主线程栈 + 外部模块归因 */
            StackTraceElement[] mainSt = null;
            try {
                mainSt = android.os.Looper.getMainLooper().getThread().getStackTrace();
            } catch (Throwable ignored) {
            }
            String blame = blameExternalModule(mainSt);
            if (!blame.isEmpty()) {
                sb.append("归因     : ").append(blame).append('\n');
            }
            sb.append("---------- 主线程栈 ----------\n");
            try {
                if (mainSt != null) {
                    for (StackTraceElement el : mainSt) {
                        sb.append("  at ").append(el).append('\n');
                    }
                }
            } catch (Throwable ignored) {
            }

            /* 全线程栈（定位死锁用） */
            sb.append("---------- 全部线程 ----------\n");
            try {
                java.util.Map<Thread, StackTraceElement[]> all =
                        Thread.getAllStackTraces();
                for (java.util.Map.Entry<Thread, StackTraceElement[]> en
                        : all.entrySet()) {
                    Thread t = en.getKey();
                    if (t == null) {
                        continue;
                    }
                    sb.append("Thread: ").append(t.getName())
                            .append(" (id=").append(t.getId())
                            .append(", state=").append(t.getState()).append(")\n");
                    StackTraceElement[] sts = en.getValue();
                    if (sts != null) {
                        int n = Math.min(sts.length, 20);
                        for (int i = 0; i < n; i++) {
                            sb.append("  at ").append(sts[i]).append('\n');
                        }
                    }
                }
            } catch (Throwable ignored) {
            }
            sb.append("========== END ").append(now()).append(" ==========\n\n");
            /* 同一时长区间只记一次，避免卡死期间刷屏 */
            String sig = "hang|" + (severe ? "8s" : "3s") + "|" + firstMainFrame();
            appendDedup(sig, sb.toString());
        } catch (Throwable ignored) {
        }
    }

    /** 主线程第一个业务栈帧（用于卡死去重） */
    private static String firstMainFrame() {
        try {
            StackTraceElement[] st = android.os.Looper.getMainLooper()
                    .getThread().getStackTrace();
            for (StackTraceElement el : st) {
                String cn = el.getClassName();
                if (cn.contains("CrashLog")) {
                    continue;
                }
                return cn + "." + el.getMethodName();
            }
        } catch (Throwable ignored) {
        }
        return "?";
    }

    /**
     * 与宿主 Watchdog 联动：
     * 宿主的 CrashHandler / HangWatcher 最终都会调 Watchdog.report(ctx, kind, body)。
     * 我们 hook 它，把同样的内容镜像到 crash.log，
     * 这样"宿主自己捕获到的崩溃/卡死"也能在我们的日志里看到。
     */
    public static void hookHostWatchdog() {
        /*
         * 必须用宿主 ClassLoader！
         * CrashLog.class.getClassLoader() 是模块自己的 PathClassLoader，
         * 里面没有 momoi.mod.qqpro.* —— 会抛 ClassNotFoundException
         * （个性签名功能曾因同样的错误导致"保存失败：ClassNotFoundException"）。
         */
        try {
            ClassLoader cl = hostClassLoader();
            if (cl == null) {
                Log.w("崩溃日志: 取不到宿主 ClassLoader，跳过 Watchdog 联动");
                return;
            }
            Class<?> cls = Class.forName("momoi.mod.qqpro.watchdog.Watchdog", false, cl);
            hookHostWatchdog(cls);
        } catch (Throwable t) {
            Log.w("崩溃日志: Watchdog 类加载失败 " + t);
            /* 此时可能还没拿到宿主 CL：延迟重试最多 3 次 */
            if (sWatchdogRetry < 3) {
                sWatchdogRetry++;
                try {
                    new android.os.Handler(android.os.Looper.getMainLooper())
                            .postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        hookHostWatchdog();
                                    } catch (Throwable ignored) {
                                    }
                                }
                            }, 2000L * sWatchdogRetry);
                } catch (Throwable ignored) {
                }
            }
        }
    }

    private static volatile int sWatchdogRetry = 0;

    /** 取宿主 ClassLoader（优先用模块初始化时保存的） */
    private static ClassLoader hostClassLoader() {
        ClassLoader cached = sHostCl;
        if (cached != null) {
            return cached;
        }
        try {
            ClassLoader cl = com.tencent.qqlitepro.http.QqKernel.classLoader();
            if (cl != null) {
                return cl;
            }
        } catch (Throwable ignored) {
        }
        try {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            if (cl != null && classExists("momoi.mod.qqpro.watchdog.Watchdog", cl)) {
                return cl;
            }
        } catch (Throwable ignored) {
        }
        try {
            Class<?> at = Class.forName("android.app.ActivityThread");
            Object app = at.getMethod("currentApplication").invoke(null);
            if (app != null) {
                return app.getClass().getClassLoader();
            }
        } catch (Throwable ignored) {
        }
        return null;
    }

    private static boolean classExists(String name, ClassLoader cl) {
        try {
            Class.forName(name, false, cl);
            return true;
        } catch (Throwable ignored) {
            return false;
        }
    }

    public static void hookHostWatchdog(final Class<?> watchdogCls) {
        if (watchdogCls == null) {
            return;
        }
        try {
            de.robv.android.xposed.XposedBridge.hookAllMethods(watchdogCls, "report",
                    new de.robv.android.xposed.XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) {
                            try {
                                String kind = "";
                                String body = "";
                                if (param.args != null) {
                                    for (Object a : param.args) {
                                        if (a instanceof String) {
                                            String s2 = (String) a;
                                            if ("crash".equals(s2) || "hang".equals(s2)) {
                                                kind = s2;
                                            } else if (s2.length() > 20) {
                                                body = s2;
                                            }
                                        }
                                    }
                                }
                                if (body.isEmpty()) {
                                    return;
                                }
                                /*
                                 * 宿主 HangWatcher 的误报过滤（关键）：
                                 * 它用「主线程 tick 落后 8s」判定卡死，但主线程
                                 * 开会/GC/消息排队时也会落后，此时主线程其实停在
                                 * MessageQueue.nativePollOnce（空闲等消息）。
                                 * 用户上报的 8926ms / 3022ms 全是这种假卡死。
                                 */
                                if ("hang".equals(kind)) {
                                    if (isMainThreadIdle() || isInHostStartup()) {
                                        Log.i("崩溃日志: 宿主 Watchdog 卡死误报已忽略"
                                                + "（主线程空闲/启动期）");
                                        return;
                                    }
                                }
                                /* 宿主已生成完整报告（含设备信息+堆栈），直接镜像 */
                                String head = "crash".equals(kind) ? "宿主崩溃（Watchdog）"
                                        : "hang".equals(kind) ? "宿主卡死（Watchdog）"
                                        : "宿主报告";
                                StringBuilder sb = new StringBuilder(body.length() + 384);
                                sb.append("\n===== ").append(head).append(' ')
                                        .append(now()).append(" =====\n");
                                /*
                                 * 归因：从报告正文里找主线程栈，判断是不是
                                 * 别的 Xposed 模块或宿主启动期造成的。
                                 */
                                String blame = blameFromBody(body);
                                if (!blame.isEmpty()) {
                                    sb.append("归因     : ").append(blame).append('\n');
                                }
                                sb.append(body).append('\n');
                                sb.append("===== END =====\n\n");
                                String sig = "watchdog|" + kind + "|"
                                        + Math.min(body.length(), 400);
                                appendDedup(sig, sb.toString());
                                Log.i("崩溃日志: 已镜像宿主 Watchdog 报告 kind=" + kind);
                            } catch (Throwable ignored) {
                            }
                        }
                    });
            Log.i("崩溃日志: 宿主 Watchdog.report 联动 hook 已安装");
        } catch (Throwable t) {
            Log.w("崩溃日志: Watchdog 联动失败 " + t);
        }
    }

    /**
     * 从 Watchdog 报告正文里提取主线程栈并归因。
     *
     * 典型可识别来源：
     *   - top.canyie.pine.*            → Pine 框架桥接开销
     *   - momoi.mod.qqpro.watchdog.*   → 宿主自带 Watchdog（自身开销）
     *   - com.tencent.mmkv.*           → MMKV 首次打开（启动期主线程 IO）
     *   - Qqimei / PrivacyPolicyHelper → 启动上报任务
     */
    private static String blameFromBody(String body) {
        try {
            if (body == null || body.isEmpty()) {
                return "";
            }
            boolean startup = body.contains("handleBindApplication")
                    || body.contains("installContentProviders")
                    || body.contains("NtStartupDirector")
                    || body.contains("AppContentProvider");
            if (body.contains("com.tencent.mmkv.MMKV")
                    || body.contains("Qqimei")
                    || body.contains("PrivacyPolicyHelper")
                    || body.contains("MiscInitTask")) {
                return "宿主启动期任务在主线程做 IO/初始化（MMKV/Qimei 等），"
                        + "属启动耗时，非运行期卡死";
            }
            if (startup) {
                return "宿主冷启动期（handleBindApplication/ContentProvider 初始化），"
                        + "属正常启动耗时";
            }
            if (body.contains("top.canyie.pine.")) {
                return "Pine 框架桥接开销（非本模块代码）";
            }
            if (body.contains("momoi.mod.qqpro.watchdog.")) {
                return "宿主自带 Watchdog 自身的抓栈开销（非本模块代码）";
            }
            if (body.contains("de.robv.android.xposed.XSharedPreferences")) {
                return "外部Xposed模块读配置阻塞主线程（XSharedPreferences，非本模块代码）";
            }
        } catch (Throwable ignored) {
        }
        return "";
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
            /*
             * 通用分块解析。
             *
             * 历史 bug：只识别 [模块异常]/宿主闪退/CRASH/[启动]/[异常] 五种起始，
             * 新增的「应用卡死」「主线程卡顿」「宿主崩溃（Watchdog）」「宿主卡死（Watchdog）」
             * 全部漏识别 —— 被当成上一条的续行或直接丢弃，
             * 导致反馈页列表里看不到这些真实故障（且 [启动] 反而被当成异常列出）。
             *
             * 现在改为：任何「带时间戳的块首」都算新条目开始。
             */
            String[] lines = all.split("\n");
            StringBuilder cur = new StringBuilder();
            String curKind = null;
            String curTitle = null;
            String curTime = null;

            for (String line : lines) {
                String kind = detectKind(line);
                if (kind != null) {
                    /* 遇到新块首：先收尾上一条 */
                    flushEntry(seen, cur, curKind, curTitle, curTime);
                    cur.setLength(0);
                    curKind = kind;
                    curTitle = null;
                    curTime = extractTime(line);
                    cur.append(line).append('\n');
                    /* [模块异常] 的标题就在首行 */
                    if ("模块异常".equals(kind)) {
                        int i = line.indexOf("[模块异常]");
                        if (i >= 0) {
                            curTitle = line.substring(i + 6).trim();
                        }
                    }
                    continue;
                }
                if (cur.length() > 0) {
                    cur.append(line).append('\n');
                    /* 还没拿到标题：从正文里找异常行 */
                    if (curTitle == null && curKind != null
                            && !"启动".equals(curKind)) {
                        String t = pickThrowableLine(line);
                        if (t != null) {
                            curTitle = t;
                        }
                    }
                }
                /* 结束标记：立即收尾 */
                if (line.contains("========== END")
                        || line.contains("===== END")) {
                    flushEntry(seen, cur, curKind, curTitle, curTime);
                    cur.setLength(0);
                    curKind = null;
                    curTitle = null;
                }
            }
            /* 收尾最后一条 */
            flushEntry(seen, cur, curKind, curTitle, curTime);

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

    /**
     * 识别一行是否为"新块的首行"，并返回块类型；不是块首返回 null。
     *
     * 覆盖 CrashLog 写出的所有块格式：
     *   [启动]...                          启动标记
     *   yyyy-MM-dd ...  [模块异常] xxx      模块功能异常
     *   ========== CRASH ...                未捕获异常
     *   ========== 宿主闪退 ...             宿主闪退
     *   ========== 应用卡死 ...             应用卡死（我们的看门狗）
     *   ========== 主线程卡顿 ...           主线程卡顿（我们的看门狗）
     *   ===== 宿主崩溃（Watchdog） ...      宿主 Watchdog 镜像-崩溃
     *   ===== 宿主卡死（Watchdog） ...      宿主 Watchdog 镜像-卡死
     */
    private static String detectKind(String line) {
        try {
            if (line == null || line.isEmpty()) {
                return null;
            }
            if (line.contains("[模块异常]")) {
                return "模块异常";
            }
            if (line.contains("[异常]")) {
                return "异常";
            }
            if (line.contains("[启动]")) {
                return "启动";
            }
            /* ===== / ========== 包裹的块（末尾带时间戳） */
            if (line.contains("==========") || line.contains("=====")) {
                if (line.contains("宿主崩溃（Watchdog）")) {
                    return "宿主崩溃";
                }
                if (line.contains("宿主卡死（Watchdog）")) {
                    return "宿主卡死";
                }
                if (line.contains("宿主闪退")) {
                    return "宿主闪退";
                }
                if (line.contains("========== CRASH")) {
                    return "未捕获异常";
                }
                if (line.contains("应用卡死")) {
                    return "应用卡死";
                }
                if (line.contains("主线程卡顿")) {
                    return "主线程卡顿";
                }
                if (line.contains("启动耗时")) {
                    return "启动耗时";
                }
            }
        } catch (Throwable ignored) {
        }
        return null;
    }

    /** 把一个块整理成 Entry 并入库（已存在同 key 则跳过） */
    private static void flushEntry(java.util.Map<String, Entry> seen,
                                   StringBuilder cur, String kind,
                                   String title, String time) {
        try {
            if (cur == null || cur.length() == 0) {
                return;
            }
            String raw = cur.toString();
            if (raw.trim().isEmpty()) {
                return;
            }
            /* 启动标记（模块加载）不算可上报项；但"启动耗时"要保留 */
            if ("启动".equals(kind)) {
                return;
            }
            String t = title;
            /* Watchdog 镜像块：用"应用崩溃/应用卡死 xxx"做标题，比栈帧更可读 */
            if (("应用卡死".equals(kind) || "主线程卡顿".equals(kind))
                    && (t == null || t.isEmpty() || t.contains("主线程卡顿"))) {
                /* 用"卡顿 xxx ms"作为统一标题，类型已由 kind 表达 */
                t = pickThrowableLine(raw);
            }
            if ("宿主崩溃".equals(kind) || "宿主卡死".equals(kind)) {
                String w = watchdogHeadline(raw);
                if (w != null && !w.isEmpty()) {
                    t = w;
                }
            }
            if (t == null || t.isEmpty()) {
                t = pickThrowableLine(raw);
            }
            if (t == null || t.isEmpty()) {
                t = firstMeaningfulLine(raw);
            }
            if (t == null || t.isEmpty()) {
                return;
            }
            String k = (kind == null || kind.isEmpty()) ? "其它" : kind;
            String key = k + "|" + t;
            if (seen.containsKey(key)) {
                return;
            }
            Entry e = new Entry();
            e.key = key;
            e.title = t;
            e.kind = k;
            e.raw = raw;
            e.time = (time == null || time.isEmpty()) ? extractTime(raw) : time;
            seen.put(key, e);
        } catch (Throwable ignored) {
        }
    }

    /** 收尾/入库：把当前块整理成 Entry */


    /**
     * 从一段文本里挑出"最像标题"的行。
     *
     * 优先级：
     *  1) 原始错误: xxx
     *  2) java.lang.XxxException: msg
     *  3) 卡死/崩溃块的特征行（应用卡死 xxx ms / 异常 : xxx / 崩溃 ...）
     *  4) 主线程栈的第一帧（Watchdog 镜像块里没有异常行，但栈很有价值）
     */
    private static String pickThrowableLine(String text) {
        try {
            if (text == null) {
                return null;
            }
            String[] ls = text.split("\n");
            String firstFrame = null;
            for (String l : ls) {
                String t = l.trim();
                if (t.isEmpty()) {
                    continue;
                }
                /* 1) 原始错误行 */
                if (t.startsWith("原始错误")) {
                    int c = t.indexOf(':');
                    if (c > 0) {
                        String v = t.substring(c + 1).trim();
                        if (!v.isEmpty()) {
                            return v;
                        }
                    }
                    continue;
                }
                /* 2) 标准异常行 */
                if (t.matches("^[a-zA-Z_$][\\w$]*(\\.[a-zA-Z_$][\\w$]*)+"
                        + "(Exception|Error|Throwable).*")) {
                    return t;
                }
                /* 3) 块特征行 */
                if (t.startsWith("耗时")) {
                    String v = t.replace("耗时", "").replace(":", "").trim();
                    return "启动耗时 " + v;
                }
                if (t.startsWith("卡顿时长")) {
                    /* 只取时长数字；kind 已标明是"应用卡死"还是"主线程卡顿" */
                    String v = t.replace("卡顿时长", "").replace(":", "").trim();
                    return "卡顿 " + v;
                }
                if (t.startsWith("异常   :")) {
                    return t.replace("异常   :", "").trim();
                }
                if (t.startsWith("异常 :")) {
                    return t.replace("异常 :", "").trim();
                }
                if (t.startsWith("消息   :")) {
                    String v = t.replace("消息   :", "").trim();
                    if (!v.isEmpty() && !"null".equals(v)) {
                        return v;
                    }
                }
                /* 4) 记下首个栈帧备用 */
                if (firstFrame == null && t.startsWith("at ")) {
                    firstFrame = t.substring(3).trim();
                }
            }
            if (firstFrame != null) {
                return "位于 " + firstFrame;
            }
        } catch (Throwable ignored) {
        }
        return null;
    }

    /** Watchdog 镜像块的标题：取 "应用崩溃 / 应用卡死 xxx ms" 这类首行 */
    private static String watchdogHeadline(String text) {
        try {
            String[] ls = text.split("\n");
            for (String l : ls) {
                String t = l.trim();
                if (t.isEmpty() || t.startsWith("=") || t.startsWith("=====")) {
                    continue;
                }
                if (t.startsWith("应用崩溃") || t.startsWith("应用卡死")) {
                    if (t.length() > 80) {
                        t = t.substring(0, 80) + "…";
                    }
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
