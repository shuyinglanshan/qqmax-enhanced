package com.tencent.qqlitepro;

/* JADX INFO: loaded from: classes.dex */
public class MainHook implements de.robv.android.xposed.IXposedHookLoadPackage {
    private static final long GATE_CHECK_INTERVAL_MS = 3000;
    private static final long GATE_KERNEL_WAIT_MS = 60000;
    private static final long HOST_READY_WAIT_MS = 60000;
    public static final java.lang.String NOT_IN_GROUP_TIP = "您尚未加入官方群聊，请前往模块界面扫码进群后重启应用";
    private static final java.lang.String TARGET_APP = "com.tencent.qqlite";
    private static final java.util.concurrent.atomic.AtomicBoolean sBaseInstalled = new java.util.concurrent.atomic.AtomicBoolean(false);
    private static final java.util.concurrent.atomic.AtomicBoolean sSettingsInstalled = new java.util.concurrent.atomic.AtomicBoolean(false);
    private static final java.util.concurrent.atomic.AtomicBoolean sGateHooked = new java.util.concurrent.atomic.AtomicBoolean(false);
    private static final java.util.concurrent.atomic.AtomicBoolean sGateChecking = new java.util.concurrent.atomic.AtomicBoolean(false);
    private static volatile boolean sGroupPassed = false;
    private static final java.util.concurrent.atomic.AtomicBoolean sStartupToastShown = new java.util.concurrent.atomic.AtomicBoolean(false);
    private static final java.util.concurrent.atomic.AtomicBoolean sUpdateToastShown = new java.util.concurrent.atomic.AtomicBoolean(false);
    private static volatile long sLastGateCheckAt = 0;
    private static volatile boolean sHostAppReady = false;
    private static volatile boolean sFirstActivityResumed = false;
    private static volatile java.lang.ClassLoader sHostCl = null;
    private static volatile boolean sStatusReportedByHook = false;

    private android.content.Context getAppCtxForToast() {
        try {
            return com.tencent.qqlitepro.util.Ctx.get();
        } catch (java.lang.Throwable ignored) {
            return null;
        }
    }

    public static boolean isGroupPassed() {
        return sGroupPassed;
    }

    /** 模块设置里点"刷新"时调用：立即重检官方群并尝试注入 */
    private static volatile de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam sLpp;
    private static volatile MainHook sInstance;
    private static final java.util.concurrent.atomic.AtomicBoolean sManualChecking = new java.util.concurrent.atomic.AtomicBoolean(false);

    /**
     * 手动重检官方群（设置界面"刷新"按钮）。
     * 与自动重检的区别：不受 3 秒节流转圈限制，忽略缓存，立即执行。
     */
    public static void recheckGroupNow() {
        final de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam lpp = sLpp;
        if (lpp == null) {
            com.tencent.qqlitepro.util.Log.w("手动重检：宿主参数未就绪，请稍后再试");
            return;
        }
        if (!sManualChecking.compareAndSet(false, true)) {
            com.tencent.qqlitepro.util.Log.i("手动重检：上一轮仍在进行，忽略本次");
            return;
        }
        sLastGateCheckAt = 0L;
        new java.lang.Thread(new java.lang.Runnable() {
            @Override
            public void run() {
                try {
                    com.tencent.qqlitepro.util.Log.i("手动重检：开始检测官方群");
                    if (!awaitHostReady(20000L)) {
                        com.tencent.qqlitepro.util.Log.w("手动重检：宿主未就绪");
                        return;
                    }
                    java.lang.Boolean st = com.tencent.qqlitepro.http.QqKernel.queryOfficialGroupState(20000L);
                    if (java.lang.Boolean.TRUE.equals(st)) {
                        if (sGroupPassed) {
                            com.tencent.qqlitepro.util.Log.i("手动重检：已在群中且此前已注入，无需重复");
                            return;
                        }
                        com.tencent.qqlitepro.util.Log.i("手动重检：检测到已加入官方群，立即注入");
                        MainHook inst = sInstance;
                        if (inst != null) {
                            inst.onGatePassed(lpp);
                        }
                    } else if (java.lang.Boolean.FALSE.equals(st)) {
                        com.tencent.qqlitepro.util.Log.i("手动重检：确认未加入官方群");
                        sStartupToastShown.set(false);
                        MainHook inst2 = sInstance;
                        if (inst2 != null) {
                            inst2.toastGateResult(NOT_IN_GROUP_TIP);
                        }
                    } else {
                        com.tencent.qqlitepro.util.Log.w("手动重检：本轮无法判定，请稍后重试");
                        MainHook inst3 = sInstance;
                        if (inst3 != null) {
                            inst3.toastGateResult("暂时无法确认，请稍后再点刷新");
                        }
                    }
                } catch (java.lang.Throwable th) {
                    com.tencent.qqlitepro.util.Log.w("手动重检异常: " + th);
                } finally {
                    sManualChecking.set(false);
                }
            }
        }, "antirecall-manual-gate").start();
    }

    public void handleLoadPackage(de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam loadPackageParam) throws java.lang.Throwable {
        sLpp = loadPackageParam;
        sInstance = this;
        java.lang.Object objInvoke;
        if (!TARGET_APP.equals(loadPackageParam.packageName)) {
            com.tencent.qqlitepro.util.Log.v("跳过非目标包: " + loadPackageParam.packageName);
            return;
        }
        java.lang.String str = loadPackageParam.processName;
        if (str != null && str.endsWith(":MSF")) {
            try {
                com.tencent.qqlitepro.hook.RiskControlBypassHook.patchMsfProcess(loadPackageParam);
            } catch (java.lang.Throwable th) {
                com.tencent.qqlitepro.util.Log.e("MSF 进程风控 hook 失败: " + th);
            }
            try {
                com.tencent.qqlitepro.hook.MsfKeepAliveHook.patchMsfProcess(loadPackageParam);
                return;
            } catch (java.lang.Throwable th2) {
                com.tencent.qqlitepro.util.Log.e("MSF 进程保活 hook 失败: " + th2);
                return;
            }
        }
        if (str != null && !"com.tencent.mobileqq".equals(str) && !TARGET_APP.equals(str)) {
            com.tencent.qqlitepro.util.Log.v("跳过非主进程: " + str);
            return;
        }
        sHostCl = loadPackageParam.classLoader;
        com.tencent.qqlitepro.util.Log.i("==== AntiRecall 模块已注入 ====");
        try {
            com.tencent.qqlitepro.util.Log.i("运行环境：Android " + android.os.Build.VERSION.RELEASE + "（SDK_INT=" + android.os.Build.VERSION.SDK_INT + "） 机型=" + android.os.Build.MODEL + " 厂商=" + android.os.Build.MANUFACTURER + " 进程=" + str + " 是安卓8.1=" + com.tencent.qqlitepro.util.Status.isAndroid81());
        } catch (java.lang.Throwable unused) {
        }
        try {
            try {
                objInvoke = java.lang.Class.forName("de.robv.android.xposed.XposedBridge").getMethod("getXposedVersion", new java.lang.Class[0]).invoke(null, new java.lang.Object[0]);
            } catch (java.lang.Throwable unused2) {
                objInvoke = null;
            }
            com.tencent.qqlitepro.util.Log.i("框架可用：XposedBridge=OK version=" + objInvoke);
        } catch (java.lang.Throwable th3) {
            com.tencent.qqlitepro.util.Log.i("XposedBridge 未暴露给 Class.forName（LSPosed 正常现象）: " + th3);
        }
        if (sBaseInstalled.compareAndSet(false, true)) {
            com.tencent.qqlitepro.util.Status.markActivatedInjected();
            hookApplicationForStatus(loadPackageParam);
        }
        if (sSettingsInstalled.compareAndSet(false, true)) {
            try {
                com.tencent.qqlitepro.hook.SettingsInjector.patch(loadPackageParam.classLoader);
            } catch (java.lang.Throwable th4) {
                com.tencent.qqlitepro.util.Log.e("QQMax 设置入口注入失败: " + th4);
            }
        }
        try {
            com.tencent.qqlitepro.http.QqKernel.setClassLoader(loadPackageParam.classLoader);
        } catch (java.lang.Throwable th5) {
            com.tencent.qqlitepro.util.Log.w("设置宿主类加载器失败: " + th5);
        }
        if (!sGroupPassed) {
            startGroupGate(loadPackageParam);
            return;
        }
        try {
            com.tencent.qqlitepro.hook.RiskControlBypassHook.patch(loadPackageParam);
        } catch (java.lang.Throwable th6) {
            com.tencent.qqlitepro.util.Log.e("风控绕过 hook 失败: " + th6);
        }
        try {
            com.tencent.qqlitepro.hook.MsfKeepAliveHook.patchMainProcess(loadPackageParam);
        } catch (java.lang.Throwable th7) {
            com.tencent.qqlitepro.util.Log.e("推送保活 hook 注入失败: " + th7);
        }
        loadCacheWhenReady();
        try {
            com.tencent.qqlitepro.hook.MsgListenerHook.hook(com.tencent.qqlitepro.hook.MsgListenerHook.findListener(loadPackageParam.classLoader));
        } catch (java.lang.Throwable th8) {
            com.tencent.qqlitepro.util.Log.e("消息监听 hook 失败: " + th8);
        }
        try {
            com.tencent.qqlitepro.hook.MsgListenerHook.hookGlobal(loadPackageParam.classLoader);
        } catch (java.lang.Throwable th9) {
            com.tencent.qqlitepro.util.Log.e("全局消息 hook 失败: " + th9);
        }
        try {
            com.tencent.qqlitepro.hook.UiPatcher.patch(loadPackageParam.classLoader);
        } catch (java.lang.Throwable th10) {
            com.tencent.qqlitepro.util.Log.e("UI hook 失败: " + th10);
        }
        try {
            com.tencent.qqlitepro.hook.UiPatcher.patchGrayTipCell(loadPackageParam.classLoader);
        } catch (java.lang.Throwable th11) {
            com.tencent.qqlitepro.util.Log.e("灰条 hook 失败: " + th11);
        }
        try {
            com.tencent.qqlitepro.hook.UiPatcher.patchMsgFactory(loadPackageParam.classLoader);
        } catch (java.lang.Throwable th12) {
            com.tencent.qqlitepro.util.Log.e("消息 item 工厂 hook 失败: " + th12);
        }
        try {
            com.tencent.qqlitepro.hook.UiPatcher.patchBaseCell(loadPackageParam.classLoader);
        } catch (java.lang.Throwable th13) {
            com.tencent.qqlitepro.util.Log.e("BaseWatchItemCell 红边/角标 hook 失败: " + th13);
        }
        try {
            com.tencent.qqlitepro.hook.UiPatcher.patchLongPressMenu(loadPackageParam.classLoader);
        } catch (java.lang.Throwable th14) {
            com.tencent.qqlitepro.util.Log.e("长按菜单编辑注入失败: " + th14);
        }
        try {
            com.tencent.qqlitepro.hook.UiPatcher.patchPttWaveView(loadPackageParam.classLoader);
        } catch (java.lang.Throwable th15) {
            com.tencent.qqlitepro.util.Log.e("语音波形防崩 hook 失败: " + th15);
        }
        try {
            com.tencent.qqlitepro.hook.ChatCardInjector.patch(loadPackageParam.classLoader);
        } catch (java.lang.Throwable th16) {
            com.tencent.qqlitepro.util.Log.e("QQ 邮箱会话卡片注入失败: " + th16);
        }
        try {
            com.tencent.qqlitepro.hook.MediaPickerHook.patch(loadPackageParam.classLoader);
            com.tencent.qqlitepro.util.Log.i("系统媒体选择器 hook 已注册");
        } catch (java.lang.Throwable th17) {
            com.tencent.qqlitepro.util.Log.e("系统媒体选择器 hook 注册失败: " + th17);
        }
        try {
            com.tencent.qqlitepro.hook.SettingsInjector.patchSelectableText(loadPackageParam.classLoader);
        } catch (java.lang.Throwable th18) {
            com.tencent.qqlitepro.util.Log.e("可选文本修复 hook 注册失败: " + th18);
        }
        try {
            com.tencent.qqlitepro.mail.QqMailManager.ensurePolling(null);
        } catch (java.lang.Throwable th19) {
            com.tencent.qqlitepro.util.Log.e("QQ 邮箱轮询启动失败: " + th19);
        }
        try {
            com.tencent.qqlitepro.hook.ForceNoticeInjector.patch(loadPackageParam.classLoader);
        } catch (java.lang.Throwable th20) {
            com.tencent.qqlitepro.util.Log.e("强制公告注入失败: " + th20);
        }
        try {
            com.tencent.qqlitepro.hook.AiSummaryHook.patchSummarizeApi(loadPackageParam.classLoader);
        } catch (java.lang.Throwable th21) {
            com.tencent.qqlitepro.util.Log.e("原生总结 API 自定义模型 hook 失败: " + th21);
        }
        try {
            com.tencent.qqlitepro.hook.TranslateHook.patch(loadPackageParam.classLoader);
        } catch (java.lang.Throwable th22) {
            com.tencent.qqlitepro.util.Log.e("翻译接口替换 hook 失败: " + th22);
        }
        try {
            com.tencent.qqlitepro.http.QqKernel.init(loadPackageParam.classLoader);
            com.tencent.qqlitepro.http.HttpApiServer.start();
        } catch (java.lang.Throwable th23) {
            com.tencent.qqlitepro.util.Log.e("HTTP API 服务器启动失败: " + th23);
        }
        try {
            com.tencent.qqlitepro.http.QqKernel.registerGlobalBulletinListener();
        } catch (java.lang.Throwable th24) {
            com.tencent.qqlitepro.util.Log.e("全局公告监听注册失败: " + th24);
        }
        try {
            com.tencent.qqlitepro.http.QqKernel.installTempChatForceShowHook();
        } catch (java.lang.Throwable th25) {
            com.tencent.qqlitepro.util.Log.e("临时会话强制显示 hook 安装失败: " + th25);
        }
        try {
            com.tencent.qqlitepro.http.FakeImage.loadPersistedMap();
        } catch (java.lang.Throwable th26) {
            com.tencent.qqlitepro.util.Log.e("加载伪图片md5映射失败: " + th26);
        }
        try {
            com.tencent.qqlitepro.hook.MsgListenerHook.hookSendSuffix(loadPackageParam.classLoader);
        } catch (java.lang.Throwable th27) {
            com.tencent.qqlitepro.util.Log.e("发送拦截 hook 注入失败: " + th27);
        }
        try {
            com.tencent.qqlitepro.auto.AutoTaskScheduler.start();
        } catch (java.lang.Throwable th28) {
            com.tencent.qqlitepro.util.Log.e("定时消息调度器启动失败: " + th28);
        }
        try {
            com.tencent.qqlitepro.hook.QzoneDynamicHook.patch(loadPackageParam.classLoader);
            com.tencent.qqlitepro.auto.QqDynamicScheduler.init(loadPackageParam.classLoader);
            com.tencent.qqlitepro.auto.QqDynamicScheduler.start();
        } catch (java.lang.Throwable th29) {
            com.tencent.qqlitepro.util.Log.e("QQ动态调度器启动失败: " + th29);
        }
        try {
            de.robv.android.xposed.XposedHelpers.findAndHookMethod(android.app.Activity.class, "onResume", new java.lang.Object[]{new com.tencent.qqlitepro.MainHook.AnonymousClass1()});
            de.robv.android.xposed.XposedHelpers.findAndHookMethod(android.app.Activity.class, "onPause", new java.lang.Object[]{new com.tencent.qqlitepro.MainHook.AnonymousClass2()});
            com.tencent.qqlitepro.util.Log.i("已 hook 前台 Activity 追踪(onResume/onPause)");
        } catch (java.lang.Throwable th30) {
            com.tencent.qqlitepro.util.Log.e("前台 Activity 追踪 hook 失败: " + th30);
        }
        try {
            com.tencent.qqlitepro.hook.AvatarHook.patch(loadPackageParam.classLoader);
            com.tencent.qqlitepro.util.Log.i("头像增强 hook 已注册");
        } catch (java.lang.Throwable th31) {
            com.tencent.qqlitepro.util.Log.e("头像增强 hook 注册失败: " + th31);
        }
        try {
            com.tencent.qqlitepro.hook.AddFriendVerifyHook.patch(loadPackageParam.classLoader);
            com.tencent.qqlitepro.util.Log.i("添加好友验证问答 hook 已注册");
        } catch (java.lang.Throwable th32) {
            com.tencent.qqlitepro.util.Log.e("添加好友验证问答 hook 注册失败: " + th32);
        }
        com.tencent.qqlitepro.util.Log.i("==== AntiRecall 模块初始化完成 ====");
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.MainHook$1, reason: invalid class name */
    class AnonymousClass1 extends de.robv.android.xposed.XC_MethodHook {
        AnonymousClass1() {
        }

        protected void afterHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            try {
                if (methodHookParam.thisObject instanceof android.app.Activity) {
                    com.tencent.qqlitepro.plugin.PluginEngine.setCurrentActivity((android.app.Activity) methodHookParam.thisObject);
                }
            } catch (java.lang.Throwable unused) {
            }
            try {
                com.tencent.qqlitepro.MainHook.this.retryLateInstalls();
            } catch (java.lang.Throwable unused2) {
            }
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.MainHook$2, reason: invalid class name */
    class AnonymousClass2 extends de.robv.android.xposed.XC_MethodHook {
        AnonymousClass2() {
        }

        protected void afterHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            try {
                if (com.tencent.qqlitepro.plugin.PluginEngine.currentActivity() == methodHookParam.thisObject) {
                    com.tencent.qqlitepro.plugin.PluginEngine.setCurrentActivity(null);
                }
            } catch (java.lang.Throwable unused) {
            }
        }
    }

    private void startGroupGate(de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam loadPackageParam) {
        if (sGateHooked.compareAndSet(false, true)) {
            try {
                de.robv.android.xposed.XposedHelpers.findAndHookMethod(android.app.Activity.class, "onResume", new java.lang.Object[]{new com.tencent.qqlitepro.MainHook.AnonymousClass3(loadPackageParam)});
                com.tencent.qqlitepro.util.Log.i("已 hook 官方群门禁重校验(onResume)");
            } catch (java.lang.Throwable th) {
                sFirstActivityResumed = true;
                com.tencent.qqlitepro.util.Log.e("官方群门禁 onResume hook 失败（退化为仅等 Application.onCreate）: " + th);
            }
        }
        checkGroupAndInstall(loadPackageParam);
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.MainHook$3, reason: invalid class name */
    class AnonymousClass3 extends de.robv.android.xposed.XC_MethodHook {
        final /* synthetic */ de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam val$lpparam;

        AnonymousClass3(de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam loadPackageParam) {
            this.val$lpparam = loadPackageParam;
        }

        protected void afterHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            try {
                com.tencent.qqlitepro.MainHook.sFirstActivityResumed = true;
                if (com.tencent.qqlitepro.MainHook.sGroupPassed) {
                    return;
                }
                com.tencent.qqlitepro.MainHook.this.checkGroupAndInstall(this.val$lpparam);
            } catch (java.lang.Throwable th) {
                com.tencent.qqlitepro.util.Log.w("官方群门禁重校验异常: " + th);
            }
        }
    }

    private void catchUpLateInject() {
        try {
            com.tencent.qqlitepro.hook.ForceNoticeInjector.triggerForCurrentActivity();
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("强制公告晚注入补齐失败: " + th);
        }
        retryGlobalBulletinListener(0);
        try {
            new android.os.Handler(android.os.Looper.getMainLooper()).post(new com.tencent.qqlitepro.MainHook.AnonymousClass4());
        } catch (java.lang.Throwable th2) {
            com.tencent.qqlitepro.util.Log.w("会话列表晚注入补齐刷新失败: " + th2);
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.MainHook$4, reason: invalid class name */
    class AnonymousClass4 implements java.lang.Runnable {
        AnonymousClass4() {
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                com.tencent.qqlitepro.hook.ChatCardInjector.refreshChatList();
            } catch (java.lang.Throwable unused) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    private void retryLateInstalls() {
        java.lang.ClassLoader classLoader = sHostCl;
        if (classLoader == null) {
            return;
        }
        try {
            if (!com.tencent.qqlitepro.hook.MediaPickerHook.isInstalled()) {
                com.tencent.qqlitepro.hook.MediaPickerHook.patch(classLoader);
            }
        } catch (java.lang.Throwable unused) {
        }
        try {
            if (!com.tencent.qqlitepro.hook.TranslateHook.isInstalled()) {
                com.tencent.qqlitepro.hook.TranslateHook.patch(classLoader);
            }
        } catch (java.lang.Throwable unused2) {
        }
        try {
            if (!com.tencent.qqlitepro.hook.AiSummaryHook.isInstalled()) {
                com.tencent.qqlitepro.hook.AiSummaryHook.patchSummarizeApi(classLoader);
            }
        } catch (java.lang.Throwable unused3) {
        }
        try {
            if (!com.tencent.qqlitepro.hook.AvatarHook.isInstalled()) {
                com.tencent.qqlitepro.hook.AvatarHook.patch(classLoader);
            }
        } catch (java.lang.Throwable unused4) {
        }
        try {
            if (!com.tencent.qqlitepro.hook.AddFriendVerifyHook.isInstalled()) {
                com.tencent.qqlitepro.hook.AddFriendVerifyHook.patch(classLoader);
            }
        } catch (java.lang.Throwable unused5) {
        }
        try {
            if (!com.tencent.qqlitepro.hook.QzoneDynamicHook.isInstalled()) {
                com.tencent.qqlitepro.hook.QzoneDynamicHook.patch(classLoader);
            }
        } catch (java.lang.Throwable unused6) {
        }
        try {
            if (com.tencent.qqlitepro.http.QqKernel.isGlobalBulletinListenerReady()) {
                return;
            }
            com.tencent.qqlitepro.http.QqKernel.registerGlobalBulletinListener();
        } catch (java.lang.Throwable unused7) {
        }
    }

    private void retryGlobalBulletinListener(int i) {
        try {
            if (com.tencent.qqlitepro.http.QqKernel.isGlobalBulletinListenerReady()) {
                return;
            }
            com.tencent.qqlitepro.http.QqKernel.registerGlobalBulletinListener();
            if (com.tencent.qqlitepro.http.QqKernel.isGlobalBulletinListenerReady()) {
                com.tencent.qqlitepro.util.Log.i("群公告数量限制 hook 已在第 " + (i + 1) + " 次尝试中安装成功");
                return;
            }
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("群公告数量限制 hook 重试异常: " + th);
        }
        if (i >= 7) {
            return;
        }
        try {
            new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(new com.tencent.qqlitepro.MainHook$$ExternalSyntheticLambda2(this, i), 1500L);
        } catch (java.lang.Throwable unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$retryGlobalBulletinListener$0(int i) {
        retryGlobalBulletinListener(i + 1);
    }

    private void toastGateResult(java.lang.String str) {
        if (sStartupToastShown.compareAndSet(false, true)) {
            java.lang.Thread thread = new java.lang.Thread(new com.tencent.qqlitepro.MainHook$$ExternalSyntheticLambda5(str), "antirecall-gate-toast");
            thread.setDaemon(true);
            thread.start();
        }
    }

    static /* synthetic */ void lambda$toastGateResult$2(java.lang.String str) {
        for (int i = 0; i < 100; i++) {
            android.content.Context contextAppContext = com.tencent.qqlitepro.cache.MsgCache.appContext();
            if (contextAppContext != null) {
                if (contextAppContext.getApplicationContext() != null) {
                    contextAppContext = contextAppContext.getApplicationContext();
                }
                new android.os.Handler(android.os.Looper.getMainLooper()).post(new com.tencent.qqlitepro.MainHook$$ExternalSyntheticLambda4(contextAppContext, str));
                return;
            }
            try {
                java.lang.Thread.sleep(100L);
            } catch (java.lang.InterruptedException unused) {
                return;
            }
        }
        com.tencent.qqlitepro.util.Log.w("门禁提示 Toast 未弹出（Application Context 未就绪）: " + str);
    }

    static /* synthetic */ void lambda$toastGateResult$1(android.content.Context context, java.lang.String str) {
        try {
            android.widget.Toast.makeText(context, str, 1).show();
        } catch (java.lang.Throwable unused) {
        }
        toastUpdateNoticeIfOutdated(context);
    }

    private static void toastUpdateNoticeIfOutdated(android.content.Context context) {
        if (context == null || !sUpdateToastShown.compareAndSet(false, true)) {
            return;
        }
        java.lang.Thread thread = new java.lang.Thread(new com.tencent.qqlitepro.MainHook$$ExternalSyntheticLambda8(context), "antirecall-update-toast");
        thread.setDaemon(true);
        thread.start();
    }

    static /* synthetic */ void lambda$toastUpdateNoticeIfOutdated$4(android.content.Context context) {
        java.lang.String strFindNewerUpdateVersion = com.tencent.qqlitepro.hook.SettingsInjector.findNewerUpdateVersion(context);
        if (strFindNewerUpdateVersion == null || strFindNewerUpdateVersion.isEmpty()) {
            com.tencent.qqlitepro.util.Log.i("未发现高于已安装版本的版本更新公告，不提示更新");
            return;
        }
        java.lang.String str = "检测到新版本 " + strFindNewerUpdateVersion + "，请前往「增强模块设置 → 官方公告 → 版本更新日志」查看更新";
        com.tencent.qqlitepro.util.Log.i("版本更新提示: " + str);
        new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(new com.tencent.qqlitepro.MainHook$$ExternalSyntheticLambda1(context, str), 4000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkGroupAndInstall(de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam loadPackageParam) {
        if (sGroupPassed) {
            return;
        }
        long jCurrentTimeMillis = java.lang.System.currentTimeMillis();
        if (jCurrentTimeMillis - sLastGateCheckAt >= GATE_CHECK_INTERVAL_MS && sGateChecking.compareAndSet(false, true)) {
            sLastGateCheckAt = jCurrentTimeMillis;
            java.lang.Thread thread = new java.lang.Thread(new com.tencent.qqlitepro.MainHook$$ExternalSyntheticLambda0(this, loadPackageParam), "antirecall-group-gate");
            thread.setDaemon(true);
            thread.start();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkGroupAndInstall$5(de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam loadPackageParam) {
        try {
            if (!awaitHostReady(60000L)) {
                com.tencent.qqlitepro.util.Log.w("宿主初始化未完成，本轮门禁校验跳过（不判定结果，等下一次 onResume 重校验）");
                sLastGateCheckAt = 0L;
                sGateChecking.set(false);
                return;
            }
            java.lang.Boolean boolQueryOfficialGroupState = com.tencent.qqlitepro.http.QqKernel.queryOfficialGroupState(60000L);
            if (java.lang.Boolean.TRUE.equals(boolQueryOfficialGroupState)) {
                onGatePassed(loadPackageParam);
            } else if (java.lang.Boolean.FALSE.equals(boolQueryOfficialGroupState)) {
                com.tencent.qqlitepro.util.Log.i("官方群门禁未通过：未检测到官方群会话，仅保留设置入口，不注入任何功能 hook");
                recheckBeforeNotInGroupToast(loadPackageParam);
            } else {
                com.tencent.qqlitepro.util.Log.w("官方群门禁本轮无法判定（内核数据未就绪），等下一次 onResume 重校验");
                sLastGateCheckAt = 0L;
            }
        } catch (java.lang.Throwable th) {
            try {
                com.tencent.qqlitepro.util.Log.w("官方群门禁校验异常（不判定结果）: " + th);
                sLastGateCheckAt = 0L;
            } finally {
                sGateChecking.set(false);
            }
        }
    }

    private static boolean awaitHostReady(long j) {
        long jCurrentTimeMillis = java.lang.System.currentTimeMillis() + java.lang.Math.max(0L, j);
        while (true) {
            if (sHostAppReady && sFirstActivityResumed) {
                return true;
            }
            if (java.lang.System.currentTimeMillis() >= jCurrentTimeMillis) {
                return false;
            }
            try {
                java.lang.Thread.sleep(100L);
            } catch (java.lang.InterruptedException unused) {
                return false;
            }
        }
    }

    private void onGatePassed(de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam loadPackageParam) {
        sGroupPassed = true;
        com.tencent.qqlitepro.util.Log.i("官方群门禁通过：已加入官方群，立即注入全部功能");
        /*
         * 「已加入官方群聊」提示受开关控制：
         * 用户加入群后通常不需要每次都弹这个提示，可在基础设置里关闭。
         */
        try {
            if (com.tencent.qqlitepro.util.Config.groupToastEnabled(sInstance != null ? sInstance.getAppCtxForToast() : null)) {
                toastGateResult("已加入官方群聊，模块功能已生效");
            } else {
                com.tencent.qqlitepro.util.Log.i("官方群提示已关闭，跳过 toast");
            }
        } catch (java.lang.Throwable ignored) {
            toastGateResult("已加入官方群聊，模块功能已生效");
        }
        try {
            handleLoadPackage(loadPackageParam);
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.e("官方群门禁通过后注入失败: " + th);
        }
        catchUpLateInject();
    }

    private void recheckBeforeNotInGroupToast(de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam loadPackageParam) {
        if (sStartupToastShown.get()) {
            return;
        }
        java.lang.Thread thread = new java.lang.Thread(new com.tencent.qqlitepro.MainHook$$ExternalSyntheticLambda3(this, loadPackageParam), "antirecall-gate-recheck");
        thread.setDaemon(true);
        thread.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$recheckBeforeNotInGroupToast$6(de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam loadPackageParam) {
        try {
            java.lang.Thread.sleep(GATE_CHECK_INTERVAL_MS);
            if (sGroupPassed || sStartupToastShown.get()) {
                return;
            }
            java.lang.Boolean boolQueryOfficialGroupState = null;
            try {
                if (awaitHostReady(60000L)) {
                    boolQueryOfficialGroupState = com.tencent.qqlitepro.http.QqKernel.queryOfficialGroupState(10000L);
                } else {
                    com.tencent.qqlitepro.util.Log.w("宿主初始化未完成，本轮门禁复核跳过（不判定结果）");
                }
            } catch (java.lang.Throwable th) {
                com.tencent.qqlitepro.util.Log.w("官方群门禁复核异常（不判定结果）: " + th);
            }
            if (java.lang.Boolean.TRUE.equals(boolQueryOfficialGroupState)) {
                onGatePassed(loadPackageParam);
            } else if (java.lang.Boolean.FALSE.equals(boolQueryOfficialGroupState)) {
                com.tencent.qqlitepro.util.Log.i("官方群门禁复核确认未加入：提示用户未加入官方群");
                toastGateResult(NOT_IN_GROUP_TIP);
            } else {
                com.tencent.qqlitepro.util.Log.w("官方群门禁复核无法判定（内核数据未就绪），不提示未加入，等下一次 onResume");
                sLastGateCheckAt = 0L;
            }
        } catch (java.lang.InterruptedException unused) {
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.MainHook$5, reason: invalid class name */
    class AnonymousClass5 extends de.robv.android.xposed.XC_MethodHook {
        AnonymousClass5() {
        }

        protected void afterHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            android.content.Context context;
            try {
                if (methodHookParam.method == null || !"attachBaseContext".equals(methodHookParam.method.getName()) || (methodHookParam.thisObject instanceof android.app.Application)) {
                    if (methodHookParam.method != null && "onCreate".equals(methodHookParam.method.getName())) {
                        com.tencent.qqlitepro.MainHook.sHostAppReady = true;
                    }
                    if (methodHookParam.thisObject instanceof android.content.Context) {
                        context = (android.content.Context) methodHookParam.thisObject;
                    } else {
                        context = (methodHookParam.args == null || methodHookParam.args.length <= 0 || !(methodHookParam.args[0] instanceof android.content.Context)) ? null : (android.content.Context) methodHookParam.args[0];
                    }
                    if (context != null) {
                        com.tencent.qqlitepro.util.Status.reportInjected(context);
                        com.tencent.qqlitepro.MainHook.sStatusReportedByHook = true;
                    }
                }
            } catch (java.lang.Throwable th) {
                com.tencent.qqlitepro.util.Log.w("Application 状态上报异常: " + th);
            }
        }
    }

    private void hookApplicationForStatus(de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam loadPackageParam) {
        com.tencent.qqlitepro.MainHook.AnonymousClass5 anonymousClass5 = new com.tencent.qqlitepro.MainHook.AnonymousClass5();
        try {
            de.robv.android.xposed.XposedHelpers.findAndHookMethod(android.content.ContextWrapper.class, "attachBaseContext", new java.lang.Object[]{android.content.Context.class, anonymousClass5});
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("hook ContextWrapper.attachBaseContext 失败: " + th);
        }
        try {
            de.robv.android.xposed.XposedHelpers.findAndHookMethod(android.app.Application.class, "onCreate", new java.lang.Object[]{anonymousClass5});
        } catch (java.lang.Throwable th2) {
            com.tencent.qqlitepro.util.Log.w("hook Application.onCreate 失败: " + th2);
        }
        reportStatusFallback();
    }

    private void reportStatusFallback() {
        java.lang.Thread thread = new java.lang.Thread(new com.tencent.qqlitepro.MainHook$$ExternalSyntheticLambda7(), "antirecall-status-fallback");
        thread.setDaemon(true);
        thread.start();
    }

    static /* synthetic */ void lambda$reportStatusFallback$7() {
        for (int i = 0; i < 120; i++) {
            try {
                java.lang.Thread.sleep(500L);
                if (sStatusReportedByHook) {
                    return;
                }
                android.content.Context contextAppContext = com.tencent.qqlitepro.cache.MsgCache.appContext();
                if (contextAppContext != null) {
                    com.tencent.qqlitepro.util.Status.reportInjected(contextAppContext);
                    com.tencent.qqlitepro.util.Log.i("注入状态兜底上报成功（Application hook 未生效，改为轮询 currentApplication）");
                    return;
                }
            } catch (java.lang.InterruptedException unused) {
                return;
            }
        }
        com.tencent.qqlitepro.util.Log.w("注入状态兜底上报超时：60s 内未取到 Application Context");
    }

    private void loadCacheWhenReady() {
        android.content.Context contextAppContext = com.tencent.qqlitepro.cache.MsgCache.appContext();
        if (contextAppContext != null) {
            applyLoad(contextAppContext);
            return;
        }
        java.lang.Thread thread = new java.lang.Thread(new com.tencent.qqlitepro.MainHook$$ExternalSyntheticLambda6(this), "antirecall-cache-load");
        thread.setDaemon(true);
        thread.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadCacheWhenReady$8() {
        for (int i = 0; i < 600; i++) {
            try {
                java.lang.Thread.sleep(100L);
                android.content.Context contextAppContext = com.tencent.qqlitepro.cache.MsgCache.appContext();
                if (contextAppContext != null) {
                    applyLoad(contextAppContext);
                    return;
                }
            } catch (java.lang.InterruptedException unused) {
                return;
            }
        }
    }

    private void applyLoad(android.content.Context context) {
        try {
            com.tencent.qqlitepro.cache.MsgCache.get().load(context);
            com.tencent.qqlitepro.util.Log.i("已加载历史撤回缓存");
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("加载缓存失败: " + th);
        }
        try {
            com.tencent.qqlitepro.plugin.PluginEngine.ensureLoaded(context);
        } catch (java.lang.Throwable th2) {
            com.tencent.qqlitepro.util.Log.w("插件引擎初始化失败: " + th2);
        }
        com.tencent.qqlitepro.util.Status.reportInjected(context);
    }
}
