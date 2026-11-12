package com.tencent.qqlitepro.util;

/* JADX INFO: loaded from: classes.dex */
public final class Status {
    public static final java.lang.String AUTHORITY = "com.tencent.qqlitepro.status";
    public static final int FW_UNKNOWN = 0;
    public static final int FW_NONE = 1;
    public static final int FW_LSPOSED = 2;
    public static final int FW_EDXPOSED = 3;
    public static final int FW_TAICHI = 4;
    /* 新增：细分框架类型，便于准确展示 */
    public static final int FW_VIRTUAL_XPOSED = 5;
    public static final int FW_KSU_NEXT = 6;
    public static final int FW_APATCH = 7;
    public static final int FW_XPOSED_COMPAT = 8;
    private static final long HEARTBEAT_MS = 2000;
    private static final java.lang.String PKG_EDXPOSED_NEW = "org.meowcat.edxposed.manager";
    private static final java.lang.String PKG_EDXPOSED_OLD = "de.robv.android.xposed.installer";
    private static final java.lang.String PKG_EDXPOSED_SOLOHSU = "com.solohsu.android.edxp.manager";
    private static final java.lang.String PKG_HOST = "com.tencent.qqlite";
    private static final java.lang.String PKG_LSPOSED = "org.lsposed.manager";
    private static final java.lang.String PKG_SELF = "com.tencent.qqlitepro";
    private static final java.lang.String PKG_TAICHI = "me.weishu.exp";
    private static final java.lang.String PKG_VIRTUAL_XPOSED = "io.va.exposed";
    private static final android.os.Binder TOKEN = new android.os.Binder();
    private static final android.net.Uri URI = android.net.Uri.parse("content://com.tencent.qqlitepro.status");
    private static volatile boolean sActivated = false;
    private static volatile boolean sInjected = false;
    private static volatile int sInProcessFw = -1;
    private static volatile boolean sReporterStarted = false;

    public static boolean activatedInProcess() {
        return sActivated;
    }

    public static java.lang.String activationHint(int i) {
        if (i == 1) {
            return "未检测到 Xposed 框架，请先安装并激活 LSPosed 或 EdXp（安卓 8.1 建议用 EdXp/EdXposed）";
        }
        if (i == 2) {
            return "在 LSPosed 中启用本模块，勾选作用域 com.tencent.qqlite，再重启 QQMax";
        }
        if (i != 3) {
            return i != 4 ? "在 Xposed 管理器中启用本模块并勾选作用域 com.tencent.qqlite，然后重启 QQMax" : "在太极中启用本模块（需 Xposed 兼容模式），再重启 QQMax";
        }
        return "在 EdXp 管理器中启用「QQMax增强模块」，勾选作用域 com.tencent.qqlite，再重启 QQMax（部分机型需重启手机让框架重新加载模块列表）";
    }

    public static java.lang.String frameworkName(int i) {
        switch (i) {
            case FW_NONE:            return "未检测到框架";
            case FW_LSPOSED:         return "LSPosed";
            case FW_EDXPOSED:        return "EdXp / EdXposed";
            case FW_TAICHI:          return "太极";
            case FW_VIRTUAL_XPOSED:  return "VirtualXposed";
            case FW_KSU_NEXT:        return "KernelSU-Next";
            case FW_APATCH:          return "APatch";
            case FW_XPOSED_COMPAT:   return "Xposed 兼容框架";
            default:                 return "未知";
        }
    }

    public static boolean injectedInProcess() {
        return sInjected;
    }

    public static boolean isAndroid81() {
        return android.os.Build.VERSION.SDK_INT == 27;
    }

    public static void markActivatedInjected() {
        sActivated = true;
        sInjected = true;
    }

    private Status() {
    }

    public static void reportInjected(android.content.Context context) {
        sActivated = true;
        sInjected = true;
        // 关键：把宿主 Context 交给 Ctx 统一托管。
        // 临时会话置顶/免打扰等所有依赖 SharedPreferences 的功能都从这里获益，
        // 避免 ActivityThread 反射在早期回调返回 null 导致设置写不进去。
        try {
            com.tencent.qqlitepro.util.Ctx.set(context);
        } catch (java.lang.Throwable ignored) {
        }
        if (context == null || sReporterStarted) {
            return;
        }
        synchronized (com.tencent.qqlitepro.util.Status.class) {
            if (sReporterStarted) {
                return;
            }
            sReporterStarted = true;
            java.lang.Thread thread = new java.lang.Thread(new com.tencent.qqlitepro.util.Status$$ExternalSyntheticLambda0(context), "antirecall-status-heartbeat");
            thread.setDaemon(true);
            thread.start();
        }
    }

    static /* synthetic */ void lambda$reportInjected$0(android.content.Context context) {
        int i = 0;
        boolean z = false;
        while (!java.lang.Thread.currentThread().isInterrupted()) {
            long j = HEARTBEAT_MS;
            try {
                android.os.Bundle bundle = new android.os.Bundle();
                bundle.putBinder("token", TOKEN);
                bundle.putInt("fw", detectFrameworkInProcess());
                android.os.Bundle bundleCall = context.getContentResolver().call(URI, "report_injected", (java.lang.String) null, bundle);
                /*
                 * 心跳成功即代表框架里模块确实处于启用状态。
                 * 注意：这里的 context 是宿主 Context，直接 getSharedPreferences 会把记录
                 * 写进宿主包目录，模块自己读不到。所以由 StatusProvider（模块进程）负责落盘。
                 */
                if (bundleCall == null || !bundleCall.getBoolean("injected", false)) {
                    i++;
                    if (i == 60 && !z) {
                        com.tencent.qqlitepro.util.Log.w("注入状态心跳上报持续失败（ContentProvider 不可达），将继续低频重试");
                        z = true;
                    }
                    if (i <= 60) {
                        j = 1000;
                    }
                } else {
                    if (i > 0) {
                        com.tencent.qqlitepro.util.Log.i("注入状态心跳已恢复");
                    }
                    i = 0;
                }
            } catch (java.lang.Throwable unused) {
            }
            try {
                java.lang.Thread.sleep(j);
            } catch (java.lang.InterruptedException unused2) {
                return;
            }
        }
    }

    public static void markInjected(android.content.Context context) {
        reportInjected(context);
    }

    public static boolean injected(android.content.Context context) {
        /*
         * 状态实时更新修复：
         * 旧逻辑 "sInjected && !isSelfProcess(context)" 会短路返回本进程缓存。
         * 模块界面进程的包名同样是 com.tencent.qqlitepro，isSelfProcess 恒为 true，
         * 于是永远跳过 ContentProvider 查询 —— 宿主注入成功后界面仍显示"未注入"，
         * 必须杀进程重启才能看到变化。
         * 正确做法：优先以宿主进程的心跳（ContentProvider + Binder 存活）为准，
         * 仅当查询失败时才回退到本进程标志。
         */
        if (context == null) {
            return sInjected;
        }
        try {
            android.os.Bundle bundleCall = context.getContentResolver()
                    .call(URI, "get_status", (java.lang.String) null, (android.os.Bundle) null);
            if (bundleCall != null) {
                return bundleCall.getBoolean("injected", false);
            }
        } catch (java.lang.Throwable unused) {
        }
        // ContentProvider 不可用时（例如宿主未安装/未启动）才回退
        return sInjected && isSelfProcess(context);
    }

    /**
     * 模块是否"已激活"。
     *
     * 语义澄清（重要）：激活指<b>框架已启用本模块</b>，与宿主应用当前是否运行无关。
     * 旧实现直接 return injected(context)，而 injected 依赖宿主进程心跳，
     * 于是宿主一关闭界面就显示"未激活"，这是错的 —— 框架里模块明明还是开着的。
     *
     * 现在的判定顺序：
     *   1) 宿主正在运行且已注入 -> 必然激活（最可靠）
     *   2) 宿主未运行 -> 只要有 Xposed 框架存在，且本模块曾在框架中生效过
     *      （有注入历史记录），就认为仍然激活
     *   3) 否则未激活
     */
    public static boolean activated(android.content.Context context) {
        // 1) 宿主正在运行且已注入：必然激活
        if (injected(context)) {
            return true;
        }
        // 2) 宿主未运行：框架存在 + 曾经成功注入过，即视为"仍处于激活状态"
        try {
            int fw = framework(context);
            if (fw == FW_NONE || fw == FW_UNKNOWN) {
                return false;
            }
            return hasEverInjected(context);
        } catch (java.lang.Throwable unused) {
            return false;
        }
    }

    /* 记录"模块曾经成功注入过宿主"，用于宿主未运行时仍能正确显示激活状态 */
    private static final java.lang.String PREF = "qqlitepro_status";
    private static final java.lang.String KEY_EVER_INJECTED = "ever_injected";

    /** 标记曾经注入成功（由宿主进程心跳首次成功时写入） */
    public static void markEverInjected(android.content.Context context) {
        try {
            if (context == null) {
                return;
            }
            context.getSharedPreferences(PREF, 0).edit()
                    .putBoolean(KEY_EVER_INJECTED, true).apply();
        } catch (java.lang.Throwable ignored) {
        }
    }

    /** 是否曾经成功注入过（宿主未运行时也能读，因为写在模块自己的 SP 里） */
    public static boolean hasEverInjected(android.content.Context context) {
        try {
            if (context == null) {
                return sActivated;
            }
            return context.getSharedPreferences(PREF, 0).getBoolean(KEY_EVER_INJECTED, false);
        } catch (java.lang.Throwable unused) {
            return sActivated;
        }
    }

    private static boolean isSelfProcess(android.content.Context context) {
        if (context == null) {
            return false;
        }
        try {
            return "com.tencent.qqlitepro".equals(context.getPackageName());
        } catch (java.lang.Throwable unused) {
            return false;
        }
    }

    public static int framework(android.content.Context context) {
        if (context == null) {
            return 0;
        }
        int iProbeFrameworkByPackage = probeFrameworkByPackage(context);
        if (iProbeFrameworkByPackage != 0) {
            return iProbeFrameworkByPackage;
        }
        int iReportedFramework = reportedFramework(context);
        if (iReportedFramework != 0 && iReportedFramework != 1) {
            return iReportedFramework;
        }
        int iDetectFrameworkInProcess = detectFrameworkInProcess();
        return iDetectFrameworkInProcess != 0 ? iDetectFrameworkInProcess : installed(context, PKG_HOST) ? 1 : 0;
    }

    private static int probeFrameworkByPackage(android.content.Context context) {
        if (installed(context, PKG_LSPOSED)) {
            return 2;
        }
        if (installed(context, PKG_EDXPOSED_NEW) || installed(context, PKG_EDXPOSED_OLD) || installed(context, PKG_EDXPOSED_SOLOHSU)) {
            return 3;
        }
        return (installed(context, PKG_TAICHI) || installed(context, PKG_VIRTUAL_XPOSED)) ? 4 : 0;
    }

    private static int reportedFramework(android.content.Context context) {
        try {
            android.os.Bundle bundleCall = context.getContentResolver().call(URI, "get_status", (java.lang.String) null, (android.os.Bundle) null);
            if (bundleCall == null) {
                return 0;
            }
            return bundleCall.getInt("fw", 0);
        } catch (java.lang.Throwable unused) {
            return 0;
        }
    }

    /**
     * 在当前进程内（即已注入宿主进程时）判定实际生效的 Xposed 框架。
     *
     * 修复"框架识别不准"：
     * 旧实现只区分 LSPosed / EdXposed / 太极三类，且依赖少数几个类名；
     * LSPosed 不同版本、KernelSU/APatch 下的 LSPosed、VirtualXposed、
     * 以及纯 Xposed 兼容层都会落到"其它"，界面上显示成"未知"。
     * 这里按"框架特征类 -> 类型"做多级判定，覆盖主流实现。
     */
    public static int detectFrameworkInProcess() {
        int cached = sInProcessFw;
        if (cached != -1) {
            return cached;
        }
        int fw = detectFrameworkUncached();
        sInProcessFw = fw;
        return fw;
    }

    private static int detectFrameworkUncached() {
        // 太极（先判，因为它也带 XposedBridge）
        if (classPresent("me.weishu.exp.TaichiBridge")
                || classPresent("me.weishu.taichi.TaichiBridge")
                || classPresent("me.weishu.exp.ExpBridge")) {
            return FW_TAICHI;
        }
        // VirtualXposed / 虚拟容器
        if (classPresent("io.va.exposed.VirtualXposed")
                || classPresent("io.va.exposed.FakeShadowContext")
                || classPresent("com.lody.virtual.client.core.VirtualCore")) {
            return FW_VIRTUAL_XPOSED;
        }
        // LSPosed（含 KernelSU / APatch 下的 Zygisk 版本）
        if (classPresent("org.lsposed.lspd.core.Main")
                || classPresent("org.lsposed.lspd.core.Entry")
                || classPresent("org.lsposed.lspd.impl.LSPosedBridge")
                || classPresent("org.lsposed.lspd.nativebridge.NativeBridge")
                || classPresent("org.lsposed.lspd.nativebridge.HookBridge")
                || classPresent("org.lsposed.lspd.models.Module")
                || classPresent("org.lsposed.lspd.config.LSPApplicationServiceClient")
                // Zygisk 版在目标进程暴露的核心类
                || classPresent("org.lsposed.zygisk.LSPlant")
                || classPresent("org.lsposed.lspd.zygisk.LSPlant")) {
            return FW_LSPOSED;
        }
        // EdXposed / EdXp
        if (classPresent("de.robv.android.xposed.XposedBridge")) {
            if (classPresent("org.meowcat.edxposed.manager.XposedApp")
                    || classPresent("com.elderdrivers.riru.edxp.config.EdXpConfigGlobal")
                    || classPresent("com.elderdrivers.riru.edxp._hooker.impl.HandleBindApp")
                    || classPresent("de.robv.android.xposed.XposedInit")) {
                return FW_EDXPOSED;
            }
            // 其它兼容 XposedBridge 的实现（如 KSU 自编译 LSPosed、Xposed 兼容层）
            return FW_XPOSED_COMPAT;
        }
        return 0;
    }

    private static boolean classPresent(java.lang.String str) {
        try {
            java.lang.Class.forName(str, false, com.tencent.qqlitepro.util.Status.class.getClassLoader());
            return true;
        } catch (java.lang.Throwable unused) {
            return false;
        }
    }

    public static java.lang.String android81Hint() {
        return !isAndroid81() ? "" : "安卓 8.1 提示：请使用支持 8.1 的 EdXp（EdXposed 0.5.x 及以上）或 LSPosed；启用后务必重启 QQMax，必要时重启手机。";
    }

    private static boolean installed(android.content.Context context, java.lang.String str) {
        try {
            context.getPackageManager().getPackageInfo(str, 0);
            return true;
        } catch (java.lang.Throwable unused) {
            return false;
        }
    }
}
