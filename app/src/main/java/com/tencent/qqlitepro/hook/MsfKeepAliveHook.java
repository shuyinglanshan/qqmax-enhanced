package com.tencent.qqlitepro.hook;

/* JADX INFO: loaded from: classes.dex */
public final class MsfKeepAliveHook {
    private static final java.lang.String CHANNEL_ID = "WearQQ";
    private static final java.lang.String CLS_MANUFACTURER_ROUTER = "com.tencent.qqnt.manufacturer.func.ManufacturerRouter";
    private static final java.lang.String CLS_MSF_ALIVE = "com.tencent.mobileqq.msf.service.i";
    private static final java.lang.String CLS_MSF_SERVICE = "com.tencent.mobileqq.msf.service.MsfService";
    private static final java.lang.String CLS_THIRD_PUSH_API = "com.tencent.qqnt.manufacturer.func.api.IThirdPushApi";
    private static final java.lang.String EXTRA_FG = "qqlitepro_fg";
    private static final java.lang.String PKG = "com.tencent.qqlite";
    private static volatile android.os.PowerManager.WakeLock sWakeLock;

    private MsfKeepAliveHook() {
    }

    public static void patchMsfProcess(de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam loadPackageParam) {
        java.lang.ClassLoader classLoader = loadPackageParam.classLoader;
        hookJobEnabled(classLoader);
        hookJobCancel(classLoader);
        hookMsfServiceOnStartCommand(classLoader);
        com.tencent.qqlitepro.util.Log.i("[保活] MSF 省电保活 hook 已安装（:MSF 进程，仅推送时临时前台）");
    }

    public static void patchMainProcess(de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam loadPackageParam) {
        hookThirdPushApi(loadPackageParam.classLoader);
    }

    private static void hookJobEnabled(java.lang.ClassLoader classLoader) {
        try {
            de.robv.android.xposed.XposedHelpers.findAndHookMethod(CLS_MSF_ALIVE, classLoader, "d", new java.lang.Object[]{de.robv.android.xposed.XC_MethodReplacement.returnConstant(java.lang.Boolean.TRUE)});
            com.tencent.qqlitepro.util.Log.i("[保活] 已强制启用 MSF JobScheduler 保活开关");
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("[保活] hook i.d() 失败: " + th);
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.MsfKeepAliveHook$1, reason: invalid class name */
    static class AnonymousClass1 extends de.robv.android.xposed.XC_MethodHook {
        AnonymousClass1() {
        }

        protected void beforeHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            methodHookParam.setResult((java.lang.Object) null);
        }
    }

    private static void hookJobCancel(java.lang.ClassLoader classLoader) {
        try {
            de.robv.android.xposed.XposedHelpers.findAndHookMethod(CLS_MSF_ALIVE, classLoader, "g", new java.lang.Object[]{new com.tencent.qqlitepro.hook.MsfKeepAliveHook.AnonymousClass1()});
            com.tencent.qqlitepro.util.Log.i("[保活] 已拦截 MSF 保活任务的取消");
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("[保活] hook i.g() 失败: " + th);
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.MsfKeepAliveHook$2, reason: invalid class name */
    static class AnonymousClass2 extends de.robv.android.xposed.XC_MethodHook {
        AnonymousClass2() {
        }

        protected void beforeHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            android.content.Intent intent = (android.content.Intent) methodHookParam.args[0];
            if (intent == null || !intent.hasExtra(com.tencent.qqlitepro.hook.MsfKeepAliveHook.EXTRA_FG)) {
                return;
            }
            com.tencent.qqlitepro.hook.MsfKeepAliveHook.applyMsfForeground((android.content.Context) methodHookParam.thisObject, intent.getBooleanExtra(com.tencent.qqlitepro.hook.MsfKeepAliveHook.EXTRA_FG, false));
        }
    }

    private static void hookMsfServiceOnStartCommand(java.lang.ClassLoader classLoader) {
        try {
            de.robv.android.xposed.XposedHelpers.findAndHookMethod(CLS_MSF_SERVICE, classLoader, "onStartCommand", new java.lang.Object[]{android.content.Intent.class, java.lang.Integer.TYPE, java.lang.Integer.TYPE, new com.tencent.qqlitepro.hook.MsfKeepAliveHook.AnonymousClass2()});
            com.tencent.qqlitepro.util.Log.i("[保活] 已 hook MsfService.onStartCommand（按需临时前台）");
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("[保活] hook MsfService.onStartCommand 失败: " + th);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void applyMsfForeground(android.content.Context context, boolean z) {
        if (context == null) {
            return;
        }
        try {
            if (z) {
                de.robv.android.xposed.XposedHelpers.callMethod(context, "startForeground", new java.lang.Object[]{1, buildPushNotification(context)});
                com.tencent.qqlitepro.util.Log.i("[保活] MSF 已临时进入前台（正在处理推送）");
            } else {
                de.robv.android.xposed.XposedHelpers.callMethod(context, "stopForeground", new java.lang.Object[]{true});
                com.tencent.qqlitepro.util.Log.i("[保活] MSF 已退出前台（推送处理完毕，恢复省电）");
            }
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("[保活] 切换 MSF 前台状态失败: " + th);
        }
    }

    private static android.app.Notification buildPushNotification(android.content.Context context) {
        android.app.Notification.Builder builder;
        android.app.NotificationManager notificationManager;
        if (android.os.Build.VERSION.SDK_INT >= 26 && (notificationManager = (android.app.NotificationManager) context.getSystemService("notification")) != null && notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
            android.app.NotificationChannel notificationChannelM = new android.app.NotificationChannel(CHANNEL_ID, "QQMax 消息推送", 2);
            notificationChannelM.enableLights(false);
            notificationChannelM.enableVibration(false);
            notificationManager.createNotificationChannel(notificationChannelM);
        }
        if (android.os.Build.VERSION.SDK_INT >= 26) {
            builder = new android.app.Notification.Builder(context, CHANNEL_ID);
        } else {
            builder = new android.app.Notification.Builder(context);
        }
        int i = context.getApplicationInfo().icon;
        android.app.Notification.Builder contentText = builder.setContentTitle("QQMax").setContentText("正在后台接收消息…");
        if (i == 0) {
            i = android.R.drawable.sym_def_app_icon;
        }
        contentText.setSmallIcon(i).setOngoing(true);
        return builder.build();
    }

    private static void hookThirdPushApi(java.lang.ClassLoader classLoader) {
        try {
            java.lang.Class clsFindClassIfExists = de.robv.android.xposed.XposedHelpers.findClassIfExists(CLS_MANUFACTURER_ROUTER, classLoader);
            java.lang.Class clsFindClassIfExists2 = de.robv.android.xposed.XposedHelpers.findClassIfExists(CLS_THIRD_PUSH_API, classLoader);
            if (clsFindClassIfExists != null && clsFindClassIfExists2 != null) {
                de.robv.android.xposed.XposedHelpers.findAndHookMethod(clsFindClassIfExists, "a", new java.lang.Object[]{java.lang.Class.class, new com.tencent.qqlitepro.hook.MsfKeepAliveHook.AnonymousClass3(clsFindClassIfExists2, buildThirdPushImpl(classLoader, clsFindClassIfExists2))});
                com.tencent.qqlitepro.util.Log.i("[保活] 已接管 IThirdPushApi（真实前台服务/WakeLock）");
                return;
            }
            com.tencent.qqlitepro.util.Log.w("[保活] 未找到 ManufacturerRouter / IThirdPushApi，跳过推送接管");
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("[保活] hook ManufacturerRouter.a 失败: " + th);
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.MsfKeepAliveHook$3, reason: invalid class name */
    static class AnonymousClass3 extends de.robv.android.xposed.XC_MethodHook {
        final /* synthetic */ java.lang.Class val$apiCls;
        final /* synthetic */ java.lang.Object val$realImpl;

        AnonymousClass3(java.lang.Class cls, java.lang.Object obj) {
            this.val$apiCls = cls;
            this.val$realImpl = obj;
        }

        protected void beforeHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            if (methodHookParam.args[0] == this.val$apiCls) {
                methodHookParam.setResult(this.val$realImpl);
            }
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.MsfKeepAliveHook$4, reason: invalid class name */
    static class AnonymousClass4 implements java.lang.reflect.InvocationHandler {
        final /* synthetic */ java.lang.ClassLoader val$cl;

        AnonymousClass4(java.lang.ClassLoader classLoader) {
            this.val$cl = classLoader;
        }

        @Override // java.lang.reflect.InvocationHandler
        public java.lang.Object invoke(java.lang.Object obj, java.lang.reflect.Method method, java.lang.Object[] objArr) {
            java.lang.String name = method.getName();
            try {
                if ("startForegroundService".equals(name)) {
                    com.tencent.qqlitepro.hook.MsfKeepAliveHook.requestMsfForeground(this.val$cl, true);
                    return null;
                }
                if ("stopForegroundService".equals(name)) {
                    com.tencent.qqlitepro.hook.MsfKeepAliveHook.requestMsfForeground(this.val$cl, false);
                    return null;
                }
                if ("acquireWakeLock".equals(name)) {
                    com.tencent.qqlitepro.hook.MsfKeepAliveHook.acquireWakeLock(this.val$cl);
                    return null;
                }
                if ("releaseWakeLock".equals(name)) {
                    com.tencent.qqlitepro.hook.MsfKeepAliveHook.releaseWakeLock();
                    return null;
                }
                if ("getIsServiceExist".equals(name)) {
                    return java.lang.Boolean.TRUE;
                }
                if ("getPushType".equals(name)) {
                    return 0;
                }
                if ("registerThirdPush".equals(name)) {
                    return null;
                }
                return com.tencent.qqlitepro.hook.MsfKeepAliveHook.defaultValue(method.getReturnType());
            } catch (java.lang.Throwable th) {
                com.tencent.qqlitepro.util.Log.w("[保活] IThirdPushApi." + name + " 异常: " + th);
            }
            return com.tencent.qqlitepro.hook.MsfKeepAliveHook.defaultValue(method.getReturnType());
        }
    }

    private static java.lang.Object buildThirdPushImpl(java.lang.ClassLoader classLoader, java.lang.Class<?> cls) {
        return java.lang.reflect.Proxy.newProxyInstance(classLoader, new java.lang.Class[]{cls}, new com.tencent.qqlitepro.hook.MsfKeepAliveHook.AnonymousClass4(classLoader));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void requestMsfForeground(java.lang.ClassLoader classLoader, boolean z) {
        android.content.Context contextHostContext = hostContext(classLoader);
        if (contextHostContext == null) {
            com.tencent.qqlitepro.util.Log.w("[保活] 取不到宿主 Context，无法下发 MSF 前台控制");
            return;
        }
        android.content.Intent intent = new android.content.Intent();
        intent.setComponent(new android.content.ComponentName(PKG, CLS_MSF_SERVICE));
        intent.putExtra(EXTRA_FG, z);
        try {
            contextHostContext.startService(intent);
            com.tencent.qqlitepro.util.Log.i("[保活] 已下发 MSF 临时前台=" + z);
        } catch (java.lang.Throwable th) {
            if (z && android.os.Build.VERSION.SDK_INT >= 26) {
                try {
                    contextHostContext.startForegroundService(intent);
                    com.tencent.qqlitepro.util.Log.i("[保活] startService 受限，改 startForegroundService 下发前台=true");
                    return;
                } catch (java.lang.Throwable th2) {
                    com.tencent.qqlitepro.util.Log.w("[保活] 下发 MSF 前台控制失败: " + th2);
                    return;
                }
            }
            com.tencent.qqlitepro.util.Log.w("[保活] 下发 MSF 前台控制失败: " + th);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void acquireWakeLock(java.lang.ClassLoader classLoader) {
        android.content.Context contextHostContext = hostContext(classLoader);
        if (contextHostContext == null) {
            return;
        }
        try {
            if (sWakeLock == null) {
                android.os.PowerManager powerManager = (android.os.PowerManager) contextHostContext.getSystemService("power");
                if (powerManager == null) {
                    return;
                }
                sWakeLock = powerManager.newWakeLock(1, "qqlitepro:push");
                sWakeLock.setReferenceCounted(false);
            }
            if (sWakeLock.isHeld()) {
                return;
            }
            sWakeLock.acquire(300000L);
            com.tencent.qqlitepro.util.Log.i("[保活] 已获取推送 WakeLock（5 分钟）");
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("[保活] 获取 WakeLock 失败: " + th);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    private static void releaseWakeLock() {
        try {
            android.os.PowerManager.WakeLock wakeLock = sWakeLock;
            if (wakeLock == null || !wakeLock.isHeld()) {
                return;
            }
            wakeLock.release();
            com.tencent.qqlitepro.util.Log.i("[保活] 已释放推送 WakeLock");
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("[保活] 释放 WakeLock 失败: " + th);
        }
    }

    private static android.content.Context hostContext(java.lang.ClassLoader classLoader) {
        try {
            java.lang.Object objInvoke = java.lang.Class.forName("android.app.ActivityThread").getMethod("currentApplication", new java.lang.Class[0]).invoke(null, new java.lang.Object[0]);
            if (objInvoke instanceof android.content.Context) {
                return (android.content.Context) objInvoke;
            }
        } catch (java.lang.Throwable unused) {
        }
        try {
            java.lang.Class clsFindClassIfExists = de.robv.android.xposed.XposedHelpers.findClassIfExists("mqq.app.MobileQQ", classLoader);
            if (clsFindClassIfExists != null) {
                java.lang.Object staticObjectField = de.robv.android.xposed.XposedHelpers.getStaticObjectField(clsFindClassIfExists, "sMobileQQ");
                if (staticObjectField instanceof android.content.Context) {
                    return (android.content.Context) staticObjectField;
                }
            }
        } catch (java.lang.Throwable unused2) {
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.lang.Object defaultValue(java.lang.Class<?> cls) {
        if (cls == java.lang.Void.TYPE) {
            return null;
        }
        if (cls == java.lang.Boolean.TYPE) {
            return java.lang.Boolean.FALSE;
        }
        if (cls == java.lang.Integer.TYPE) {
            return 0;
        }
        if (cls == java.lang.Long.TYPE) {
            return 0L;
        }
        if (cls == java.lang.Short.TYPE) {
            return (short) 0;
        }
        if (cls == java.lang.Byte.TYPE) {
            return (byte) 0;
        }
        if (cls == java.lang.Character.TYPE) {
            return (char) 0;
        }
        if (cls == java.lang.Float.TYPE) {
            return java.lang.Float.valueOf(0.0f);
        }
        if (cls == java.lang.Double.TYPE) {
            return java.lang.Double.valueOf(0.0d);
        }
        return null;
    }
}
