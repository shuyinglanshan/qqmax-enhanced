package com.tencent.qqlitepro.hook;

/* JADX INFO: loaded from: classes.dex */
public final class NativePickerBridge {
    private static final java.lang.String ADAPTER_CLS = "com.tencent.qqnt.watch.fs.rv.FriendSelectAdapter";
    private static final java.lang.String COMPAT_ACT = "com.tencent.biz.richframework.activity.CompatPublicActivity";
    private static final java.lang.String EXTRA_PICK = "qqlp_native_pick";
    private static final java.lang.String FRAG_DEST_CLS = "androidx.navigation.fragment.FragmentNavigator$Destination";
    private static final java.lang.String FRAG_NAV_CLS = "androidx.navigation.fragment.FragmentNavigator";
    private static final java.lang.String FRIEND_SELECT_FRAG = "com.tencent.qqnt.watch.fs.FriendSelectFragment";
    private static final java.lang.String FROM_GROUP_FRAG = "com.tencent.qqnt.watch.fs.SelectFromGroupFragment";
    private static final java.lang.String GRAPH_NAV_CLS = "androidx.navigation.NavGraphNavigator";
    private static final java.lang.String GROUP_ADAPTER_CLS = "com.tencent.qqnt.watch.fs.rv.GroupSelectAdapter";
    private static final java.lang.String GROUP_MEMBER_FRAG = "com.tencent.qqnt.watch.fs.GroupMemberSelectFragment";
    private static final java.lang.String ITEM_CLS = "com.tencent.qqnt.watch.fs.rv.FriendSelectItem";
    private static final java.lang.String LIFECYCLE_OWNER_CLS = "androidx.lifecycle.LifecycleOwner";
    private static final java.lang.String LOADING_FRAG_CLS = "com.tencent.qqnt.watch.ui.componet.tips.LoadingFragment";
    private static final java.lang.String NAV_GRAPH_CLS = "androidx.navigation.NavGraph";
    private static final java.lang.String NAV_HOST_CLS = "androidx.navigation.fragment.NavHostFragment";
    private static final java.lang.String NAV_NAVIGATOR_CLS = "androidx.navigation.Navigator";
    private static final java.lang.String RECYCLER_VIEW_CLS = "androidx.recyclerview.widget.RecyclerView";
    private static final java.lang.String RESULT_LISTENER_CLS = "androidx.fragment.app.FragmentResultListener";
    private static java.lang.ClassLoader sCl;
    private static volatile com.tencent.qqlitepro.hook.NativePickerBridge.Pending sPending;
    private static final java.util.Map<java.lang.String, java.lang.Integer> sIdCache = new java.util.HashMap();
    private static final java.util.Set<java.lang.Object> sScrolledToTopOnce = java.util.Collections.newSetFromMap(new java.util.IdentityHashMap());
    private static final java.util.Set<java.lang.String> sHookedSubmitLists = new java.util.HashSet();
    private static final java.util.Map<java.lang.Object, java.lang.Long> sRefreshGens = new java.util.IdentityHashMap();
    private static long sRefreshSeq = 0;
    private static final java.util.Set<java.lang.Object> sInitialDataDone = java.util.Collections.newSetFromMap(new java.util.IdentityHashMap());

    private static int clampMax(int i) {
        if (i < 0 || i > 100000) {
            return 0;
        }
        return i;
    }

    private static int clampSize(int i) {
        if (i < 0 || i > 1000) {
            return 0;
        }
        return i;
    }

    private static final class Pending {
        boolean awaitResume;
        java.lang.Runnable confirm;
        boolean confirmDone;
        boolean finished;
        java.lang.Object friendFragment;
        android.app.Activity hostAct;
        boolean hostReady;
        java.util.Map<java.lang.String, java.lang.String> lines;
        java.util.Set<java.lang.String> preKeys;
        java.util.Set<java.lang.String> preUins;
        boolean privateOnly;
        java.lang.String requestKey;
        final java.util.Set<java.lang.String> subKeys;
        boolean uinMode;



        private Pending() {
            this.subKeys = new java.util.HashSet();
        }
    }

    private NativePickerBridge() {
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.NativePickerBridge$1, reason: invalid class name */
    static class AnonymousClass1 extends de.robv.android.xposed.XC_MethodHook {
        AnonymousClass1() {
        }

        protected void afterHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            com.tencent.qqlitepro.hook.NativePickerBridge.onCompatCreate(methodHookParam);
        }
    }

    public static void init(java.lang.ClassLoader classLoader) {
        sCl = classLoader;
        try {
            de.robv.android.xposed.XposedHelpers.findAndHookMethod(COMPAT_ACT, classLoader, "onCreate", new java.lang.Object[]{android.os.Bundle.class, new com.tencent.qqlitepro.hook.NativePickerBridge.AnonymousClass1()});
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("原生选择器: CompatPublicActivity hook 失败: " + th);
        }
        try {
            java.lang.Class clsFindClass = de.robv.android.xposed.XposedHelpers.findClass(FRIEND_SELECT_FRAG, classLoader);
            de.robv.android.xposed.XposedBridge.hookAllMethods(clsFindClass, "onCreate", new com.tencent.qqlitepro.hook.NativePickerBridge.AnonymousClass2());
            de.robv.android.xposed.XposedBridge.hookAllMethods(clsFindClass, "Y", new com.tencent.qqlitepro.hook.NativePickerBridge.AnonymousClass3());
            de.robv.android.xposed.XposedBridge.hookAllMethods(clsFindClass, "b0", new com.tencent.qqlitepro.hook.NativePickerBridge.AnonymousClass4());
        } catch (java.lang.Throwable th2) {
            com.tencent.qqlitepro.util.Log.w("原生选择器: FriendSelectFragment hook 失败: " + th2);
        }
        try {
            int i = 0;
            for (java.lang.reflect.Method method : de.robv.android.xposed.XposedHelpers.findClass(FRIEND_SELECT_FRAG, classLoader).getDeclaredMethods()) {
                if ("c0".equals(method.getName())) {
                    java.lang.Class<?>[] parameterTypes = method.getParameterTypes();
                    if (parameterTypes.length == 1 && java.util.List.class.isAssignableFrom(parameterTypes[0])) {
                        de.robv.android.xposed.XposedBridge.hookMethod(method, new com.tencent.qqlitepro.hook.NativePickerBridge.AnonymousClass5());
                        i++;
                    }
                }
            }
            com.tencent.qqlitepro.util.Log.i("原生选择器: 已 hook c0 确认兜底 " + i + " 个方法");
        } catch (java.lang.Throwable th3) {
            com.tencent.qqlitepro.util.Log.w("原生选择器: c0 兜底 hook 失败: " + th3);
        }
        hookPickerAdapter(ADAPTER_CLS, classLoader);
        hookPickerAdapter(GROUP_ADAPTER_CLS, classLoader);
        try {
            de.robv.android.xposed.XposedBridge.hookAllMethods(de.robv.android.xposed.XposedHelpers.findClass(GROUP_MEMBER_FRAG, classLoader), "Y", new com.tencent.qqlitepro.hook.NativePickerBridge.AnonymousClass6());
        } catch (java.lang.Throwable th4) {
            com.tencent.qqlitepro.util.Log.w("原生选择器: GroupMemberSelectFragment hook 失败: " + th4);
        }
        try {
            de.robv.android.xposed.XposedBridge.hookAllMethods(de.robv.android.xposed.XposedHelpers.findClass(FROM_GROUP_FRAG, classLoader), "Y", new com.tencent.qqlitepro.hook.NativePickerBridge.AnonymousClass7());
        } catch (java.lang.Throwable th5) {
            com.tencent.qqlitepro.util.Log.w("原生选择器: SelectFromGroupFragment hook 失败: " + th5);
        }
        try {
            de.robv.android.xposed.XposedHelpers.findAndHookMethod(android.app.Activity.class, "finish", new java.lang.Object[]{new com.tencent.qqlitepro.hook.NativePickerBridge.AnonymousClass8()});
        } catch (java.lang.Throwable th6) {
            com.tencent.qqlitepro.util.Log.w("原生选择器: finish 拦截 hook 失败: " + th6);
        }
        try {
            de.robv.android.xposed.XposedHelpers.findAndHookMethod(android.app.Activity.class, "onResume", new java.lang.Object[]{new com.tencent.qqlitepro.hook.NativePickerBridge.AnonymousClass9()});
        } catch (java.lang.Throwable th7) {
            com.tencent.qqlitepro.util.Log.w("原生选择器: onResume hook 失败: " + th7);
        }
        try {
            de.robv.android.xposed.XposedHelpers.findAndHookMethod(android.app.Activity.class, "onDestroy", new java.lang.Object[]{new com.tencent.qqlitepro.hook.NativePickerBridge.AnonymousClass10()});
        } catch (java.lang.Throwable th8) {
            com.tencent.qqlitepro.util.Log.w("原生选择器: onDestroy 清理 hook 失败: " + th8);
        }
        try {
            java.lang.Class clsFindClass2 = de.robv.android.xposed.XposedHelpers.findClass("com.tencent.qqnt.watch.ui.componet.tips.TipsUtils", classLoader);
            de.robv.android.xposed.XposedBridge.hookAllMethods(clsFindClass2, "g", new com.tencent.qqlitepro.hook.NativePickerBridge.AnonymousClass11());
            de.robv.android.xposed.XposedBridge.hookAllMethods(clsFindClass2, "b", new com.tencent.qqlitepro.hook.NativePickerBridge.AnonymousClass12());
        } catch (java.lang.Throwable th9) {
            com.tencent.qqlitepro.util.Log.w("原生选择器: TipsUtils hook 失败: " + th9);
        }
        com.tencent.qqlitepro.util.Log.i("原生会话选择器桥接已初始化");
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.NativePickerBridge$2, reason: invalid class name */
    static class AnonymousClass2 extends de.robv.android.xposed.XC_MethodHook {
        AnonymousClass2() {
        }

        protected void afterHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            com.tencent.qqlitepro.hook.NativePickerBridge.onFriendCreate(methodHookParam, false);
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.NativePickerBridge$3, reason: invalid class name */
    static class AnonymousClass3 extends de.robv.android.xposed.XC_MethodHook {
        AnonymousClass3() {
        }

        protected void beforeHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            com.tencent.qqlitepro.hook.NativePickerBridge.onFriendCreate(methodHookParam, true);
            com.tencent.qqlitepro.hook.NativePickerBridge.schedulePreselectionReapply(methodHookParam.thisObject);
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.NativePickerBridge$4, reason: invalid class name */
    static class AnonymousClass4 extends de.robv.android.xposed.XC_MethodHook {
        AnonymousClass4() {
        }

        protected void afterHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            android.os.Bundle bundle;
            com.tencent.qqlitepro.hook.NativePickerBridge.Pending pending = com.tencent.qqlitepro.hook.NativePickerBridge.sPending;
            if (pending == null || methodHookParam.thisObject != pending.friendFragment || (bundle = (android.os.Bundle) methodHookParam.getResult()) == null) {
                return;
            }
            pending.subKeys.add(bundle.getString("REQUEST_KEY"));
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.NativePickerBridge$5, reason: invalid class name */
    static class AnonymousClass5 extends de.robv.android.xposed.XC_MethodHook {
        AnonymousClass5() {
        }

        protected void beforeHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            try {
                com.tencent.qqlitepro.hook.NativePickerBridge.onConfirmCapture(methodHookParam.thisObject, methodHookParam.args.length > 0 ? methodHookParam.args[0] : null);
            } catch (java.lang.Throwable th) {
                com.tencent.qqlitepro.util.Log.w("原生选择器: c0 兜底异常: " + th);
            }
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.NativePickerBridge$6, reason: invalid class name */
    static class AnonymousClass6 extends de.robv.android.xposed.XC_MethodHook {
        AnonymousClass6() {
        }

        protected void beforeHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            com.tencent.qqlitepro.hook.NativePickerBridge.onGroupMemberFix(methodHookParam);
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.NativePickerBridge$7, reason: invalid class name */
    static class AnonymousClass7 extends de.robv.android.xposed.XC_MethodHook {
        AnonymousClass7() {
        }

        protected void beforeHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            com.tencent.qqlitepro.hook.NativePickerBridge.onGroupListFix(methodHookParam);
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.NativePickerBridge$8, reason: invalid class name */
    static class AnonymousClass8 extends de.robv.android.xposed.XC_MethodHook {
        AnonymousClass8() {
        }

        protected void beforeHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            com.tencent.qqlitepro.hook.NativePickerBridge.Pending pending = com.tencent.qqlitepro.hook.NativePickerBridge.sPending;
            if (pending == null || pending.finished || pending.hostReady || !(methodHookParam.thisObject instanceof android.app.Activity)) {
                return;
            }
            android.app.Activity activity = (android.app.Activity) methodHookParam.thisObject;
            if (com.tencent.qqlitepro.hook.NativePickerBridge.COMPAT_ACT.equals(activity.getClass().getName())) {
                android.content.Intent intent = activity.getIntent();
                java.lang.String stringExtra = intent == null ? null : intent.getStringExtra(com.tencent.qqlitepro.hook.NativePickerBridge.EXTRA_PICK);
                if (stringExtra == null || !stringExtra.equals(pending.requestKey)) {
                    return;
                }
                methodHookParam.setResult((java.lang.Object) null);
                com.tencent.qqlitepro.util.Log.i("原生选择器: 已拦截宿主 onCreate 自结束");
            }
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.NativePickerBridge$9, reason: invalid class name */
    static class AnonymousClass9 extends de.robv.android.xposed.XC_MethodHook {
        AnonymousClass9() {
        }

        protected void afterHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            com.tencent.qqlitepro.hook.NativePickerBridge.Pending pending = com.tencent.qqlitepro.hook.NativePickerBridge.sPending;
            if (pending == null || !pending.awaitResume || pending.confirmDone || com.tencent.qqlitepro.hook.NativePickerBridge.COMPAT_ACT.equals(methodHookParam.thisObject.getClass().getName())) {
                return;
            }
            pending.awaitResume = false;
            java.lang.Runnable runnable = pending.confirm;
            com.tencent.qqlitepro.hook.NativePickerBridge.sPending = null;
            if (runnable != null) {
                com.tencent.qqlitepro.util.Log.i("原生选择器: 执行确认回调");
                try {
                    runnable.run();
                    return;
                } catch (java.lang.Throwable th) {
                    com.tencent.qqlitepro.util.Log.w("原生选择器: 确认回调异常: " + th);
                    return;
                }
            }
            com.tencent.qqlitepro.util.Log.i("原生选择器: 确认回调为空，跳过");
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.NativePickerBridge$10, reason: invalid class name */
    static class AnonymousClass10 extends de.robv.android.xposed.XC_MethodHook {
        AnonymousClass10() {
        }

        protected void afterHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            com.tencent.qqlitepro.hook.NativePickerBridge.Pending pending = com.tencent.qqlitepro.hook.NativePickerBridge.sPending;
            if (pending == null || pending.awaitResume || pending.finished || !(methodHookParam.thisObject instanceof android.app.Activity)) {
                return;
            }
            android.app.Activity activity = (android.app.Activity) methodHookParam.thisObject;
            if (com.tencent.qqlitepro.hook.NativePickerBridge.COMPAT_ACT.equals(activity.getClass().getName())) {
                android.content.Intent intent = activity.getIntent();
                java.lang.String stringExtra = intent == null ? null : intent.getStringExtra(com.tencent.qqlitepro.hook.NativePickerBridge.EXTRA_PICK);
                if (stringExtra == null || !stringExtra.equals(pending.requestKey)) {
                    return;
                }
                com.tencent.qqlitepro.hook.NativePickerBridge.sPending = null;
                com.tencent.qqlitepro.util.Log.i("原生选择器: 选择页销毁，放弃本次请求");
            }
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.NativePickerBridge$11, reason: invalid class name */
    static class AnonymousClass11 extends de.robv.android.xposed.XC_MethodHook {
        AnonymousClass11() {
        }

        protected void beforeHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            if (com.tencent.qqlitepro.hook.NativePickerBridge.isOurFlowFragment(methodHookParam.args.length > 1 ? methodHookParam.args[1] : null)) {
                com.tencent.qqlitepro.util.Log.i("原生选择器: 已跳过 showLoading 导航");
                methodHookParam.setResult((java.lang.Object) null);
            }
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.NativePickerBridge$12, reason: invalid class name */
    static class AnonymousClass12 extends de.robv.android.xposed.XC_MethodHook {
        AnonymousClass12() {
        }

        protected void beforeHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            java.lang.Object obj = methodHookParam.args.length > 1 ? methodHookParam.args[1] : null;
            if (obj != null) {
                if (com.tencent.qqlitepro.hook.NativePickerBridge.isOurFlowFragment(methodHookParam.args.length > 0 ? methodHookParam.args[0] : null)) {
                    com.tencent.qqlitepro.util.Log.i("原生选择器: hideLoading → 绕过协程，主线程执行列表回调");
                    new android.os.Handler(android.os.Looper.getMainLooper()).post(new com.tencent.qqlitepro.hook.NativePickerBridge$12$$ExternalSyntheticLambda0(obj));
                    methodHookParam.setResult((java.lang.Object) null);
                }
            }
        }

        static /* synthetic */ void lambda$beforeHookedMethod$0(java.lang.Object obj) {
            try {
                de.robv.android.xposed.XposedHelpers.callMethod(obj, "invoke", new java.lang.Object[0]);
            } catch (java.lang.Throwable th) {
                com.tencent.qqlitepro.util.Log.w("原生选择器: 列表回调执行异常: " + th);
            }
        }
    }

    public static boolean start(android.content.Context context, boolean z, java.util.Set<java.lang.String> set, java.util.Set<java.lang.String> set2, java.util.Map<java.lang.String, java.lang.String> map, java.lang.Runnable runnable) {
        if (context == null) {
            return false;
        }
        if (sPending != null) {
            com.tencent.qqlitepro.util.Log.w("原生选择器尚未关闭，忽略本次请求");
            return false;
        }
        com.tencent.qqlitepro.hook.NativePickerBridge.Pending pending = new com.tencent.qqlitepro.hook.NativePickerBridge.Pending();
        pending.requestKey = java.util.UUID.randomUUID().toString();
        pending.privateOnly = z;
        pending.confirm = runnable;
        pending.uinMode = set != null;
        pending.preUins = set;
        pending.preKeys = set2;
        if (map == null) {
            map = new java.util.LinkedHashMap<>();
        }
        pending.lines = map;
        android.content.Intent intent = new android.content.Intent();
        intent.setClassName(context, COMPAT_ACT);
        intent.putExtra(EXTRA_PICK, pending.requestKey);
        intent.addFlags(805306368);
        sPending = pending;
        try {
            context.startActivity(intent);
            return true;
        } catch (java.lang.Throwable th) {
            sPending = null;
            com.tencent.qqlitepro.util.Log.w("打开原生选择器失败: " + th);
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void onCompatCreate(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
        android.app.Activity activity = (android.app.Activity) methodHookParam.thisObject;
        android.content.Intent intent = activity.getIntent();
        java.lang.String stringExtra = intent == null ? null : intent.getStringExtra(EXTRA_PICK);
        com.tencent.qqlitepro.hook.NativePickerBridge.Pending pending = sPending;
        if (stringExtra == null || pending == null || !stringExtra.equals(pending.requestKey)) {
            return;
        }
        pending.hostAct = activity;
        try {
            java.lang.Object objCallMethod = de.robv.android.xposed.XposedHelpers.callMethod(activity, "getSupportFragmentManager", new java.lang.Object[0]);
            java.lang.Object objectField = de.robv.android.xposed.XposedHelpers.getObjectField(activity, "g");
            if (objectField != null) {
                java.lang.Object objCallMethod2 = de.robv.android.xposed.XposedHelpers.callMethod(objCallMethod, "beginTransaction", new java.lang.Object[0]);
                de.robv.android.xposed.XposedHelpers.callMethod(objCallMethod2, "remove", new java.lang.Object[]{objectField});
                de.robv.android.xposed.XposedHelpers.callMethod(objCallMethod2, "commitNow", new java.lang.Object[0]);
            }
            java.lang.Object objNewInstance = de.robv.android.xposed.XposedHelpers.findClass(NAV_HOST_CLS, sCl).getDeclaredConstructor(new java.lang.Class[0]).newInstance(new java.lang.Object[0]);
            int iMustId = mustId(activity, "nav_host_fragment");
            android.view.View viewFindViewById = activity.findViewById(mustId(activity, "frag_container"));
            if (viewFindViewById == null) {
                throw new java.lang.IllegalStateException("未找到容器视图 frag_container");
            }
            viewFindViewById.setId(iMustId);
            java.lang.Object objCallMethod3 = de.robv.android.xposed.XposedHelpers.callMethod(objCallMethod, "beginTransaction", new java.lang.Object[0]);
            de.robv.android.xposed.XposedHelpers.callMethod(objCallMethod3, "add", new java.lang.Object[]{java.lang.Integer.valueOf(iMustId), objNewInstance, "qqlp_nav"});
            de.robv.android.xposed.XposedHelpers.callMethod(objCallMethod3, "commitNow", new java.lang.Object[0]);
            java.lang.Object objectField2 = de.robv.android.xposed.XposedHelpers.getObjectField(objNewInstance, "b");
            if (objectField2 == null) {
                throw new java.lang.IllegalStateException("NavHostFragment.b(controller) 为空");
            }
            registerResultListener(objNewInstance, objectField2, pending);
            buildAndNavigate(objectField2, pending, activity);
            pending.hostReady = true;
            com.tencent.qqlitepro.util.Log.i("原生选择器: 承载成功 privateOnly=" + pending.privateOnly);
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("原生选择器承载失败: " + th);
            sPending = null;
            try {
                activity.finish();
            } catch (java.lang.Throwable unused) {
            }
        }
    }

    private static void registerResultListener(java.lang.Object obj, java.lang.Object obj2, com.tencent.qqlitepro.hook.NativePickerBridge.Pending pending) throws java.lang.Exception {
        java.lang.reflect.Method method;
        java.lang.Object objCallMethod = de.robv.android.xposed.XposedHelpers.callMethod(obj, "getChildFragmentManager", new java.lang.Object[0]);
        java.lang.Class clsFindClass = de.robv.android.xposed.XposedHelpers.findClass(RESULT_LISTENER_CLS, sCl);
        java.lang.Object objNewProxyInstance = java.lang.reflect.Proxy.newProxyInstance(sCl, new java.lang.Class[]{clsFindClass}, new com.tencent.qqlitepro.hook.NativePickerBridge$$ExternalSyntheticLambda0(obj));
        java.lang.Class<?> clsFindClass2 = de.robv.android.xposed.XposedHelpers.findClass(LIFECYCLE_OWNER_CLS, sCl);
        java.lang.reflect.Method[] methods = objCallMethod.getClass().getMethods();
        int length = methods.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                method = null;
                break;
            }
            method = methods[i];
            java.lang.Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length == 3 && parameterTypes[0] == java.lang.String.class && parameterTypes[1] == clsFindClass2 && clsFindClass.isAssignableFrom(parameterTypes[2])) {
                break;
            } else {
                i++;
            }
        }
        if (method == null) {
            throw new java.lang.IllegalStateException("未找到 FragmentManager.setFragmentResultListener");
        }
        method.invoke(objCallMethod, pending.requestKey, obj, objNewProxyInstance);
    }

    static /* synthetic */ java.lang.Object lambda$registerResultListener$0(java.lang.Object obj, java.lang.Object obj2, java.lang.reflect.Method method, java.lang.Object[] objArr) throws java.lang.Throwable {
        if (objArr == null || objArr.length != 2) {
            return null;
        }
        java.lang.Object obj3 = objArr[0];
        if (!(obj3 instanceof java.lang.String)) {
            return null;
        }
        java.lang.Object obj4 = objArr[1];
        if (!(obj4 instanceof android.os.Bundle)) {
            return null;
        }
        onFragmentResult(obj, (java.lang.String) obj3, (android.os.Bundle) obj4);
        return null;
    }

    private static void buildAndNavigate(java.lang.Object obj, com.tencent.qqlitepro.hook.NativePickerBridge.Pending pending, android.app.Activity activity) throws java.lang.Exception {
        java.lang.Object objectField = de.robv.android.xposed.XposedHelpers.getObjectField(obj, "k");
        java.lang.Object objFindNavigatorByType = findNavigatorByType(objectField, FRAG_NAV_CLS);
        java.lang.Object objFindNavigatorByType2 = findNavigatorByType(objectField, GRAPH_NAV_CLS);
        java.lang.Class<?> clsFindClass = de.robv.android.xposed.XposedHelpers.findClass(NAV_NAVIGATOR_CLS, sCl);
        java.lang.Class clsFindClass2 = de.robv.android.xposed.XposedHelpers.findClass(FRAG_DEST_CLS, sCl);
        java.lang.Object objNewInstance = de.robv.android.xposed.XposedHelpers.findClass(NAV_GRAPH_CLS, sCl).getDeclaredConstructor(clsFindClass).newInstance(objFindNavigatorByType2);
        int iMustId = mustId(activity, "friendSelectFragment");
        java.lang.Object[][] objArr = {new java.lang.Object[]{java.lang.Integer.valueOf(iMustId), FRIEND_SELECT_FRAG, "FriendSelectFragment"}, new java.lang.Object[]{java.lang.Integer.valueOf(mustId(activity, "loadingFragment")), LOADING_FRAG_CLS, "LoadingFragment"}, new java.lang.Object[]{java.lang.Integer.valueOf(mustId(activity, "SelectFromGroupFragment")), FROM_GROUP_FRAG, "SelectFromGroupFragment"}, new java.lang.Object[]{java.lang.Integer.valueOf(mustId(activity, "groupMemberSelectFragment")), GROUP_MEMBER_FRAG, "GroupMemberSelectFragment"}};
        int i = 0;
        for (int i2 = 4; i < i2; i2 = 4) {
            java.lang.Object[] objArr2 = objArr[i];
            java.lang.Object objNewInstance2 = clsFindClass2.getDeclaredConstructor(clsFindClass).newInstance(objFindNavigatorByType);
            de.robv.android.xposed.XposedHelpers.setIntField(objNewInstance2, "d", ((java.lang.Integer) objArr2[0]).intValue());
            de.robv.android.xposed.XposedHelpers.setObjectField(objNewInstance2, "j", objArr2[1]);
            de.robv.android.xposed.XposedHelpers.setObjectField(objNewInstance2, "f", objArr2[2]);
            de.robv.android.xposed.XposedHelpers.callMethod(objNewInstance, "g", new java.lang.Object[]{objNewInstance2});
            i++;
        }
        de.robv.android.xposed.XposedHelpers.setIntField(objNewInstance, "k", iMustId);
        de.robv.android.xposed.XposedHelpers.setIntField(objNewInstance, "d", 2113929217);
        de.robv.android.xposed.XposedHelpers.setObjectField(obj, "d", objNewInstance);
        java.lang.Object objCallMethod = de.robv.android.xposed.XposedHelpers.callMethod(objNewInstance, "h", new java.lang.Object[]{java.lang.Integer.valueOf(iMustId)});
        if (objCallMethod == null) {
            throw new java.lang.IllegalStateException("起点目的地未注册: friendSelectFragment");
        }
        de.robv.android.xposed.XposedHelpers.callMethod(obj, "h", new java.lang.Object[]{objCallMethod, argsBundle(pending), null, null});
    }

    private static android.os.Bundle argsBundle(com.tencent.qqlitepro.hook.NativePickerBridge.Pending pending) {
        android.os.Bundle bundle = new android.os.Bundle();
        bundle.putString("REQUEST_KEY", pending.requestKey);
        bundle.putStringArrayList("FORCE_SELECT_LIST", new java.util.ArrayList<>());
        bundle.putString("KEY_BUTTON_NAME", "确认");
        bundle.putInt("CAN_CONFIRM_SIZE", 0);
        bundle.putInt("KEY_BUTTON_ICON_RES", 0);
        bundle.putInt("KEY_MAX_SIZE", 0);
        java.util.ArrayList<java.lang.String> arrayList = new java.util.ArrayList<>();
        try {
            java.lang.String strSelfUid = com.tencent.qqlitepro.http.QqKernel.selfUid();
            if (strSelfUid != null && !strSelfUid.isEmpty()) {
                arrayList.add(strSelfUid);
            }
        } catch (java.lang.Throwable unused) {
        }
        bundle.putStringArrayList("KEY_IGNORE_LIST", arrayList);
        bundle.putBoolean("KEY_IS_SUPPORT_FROM_GROUP", false);
        bundle.putBoolean("KEY_IS_SUPPORT_SELECT_GROUP", !pending.privateOnly);
        return bundle;
    }

    private static java.lang.Object findNavigatorByType(java.lang.Object obj, java.lang.String str) throws java.lang.Exception {
        java.lang.Class clsFindClass = de.robv.android.xposed.XposedHelpers.findClass(str, sCl);
        for (java.lang.reflect.Field field : obj.getClass().getDeclaredFields()) {
            if (!java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                try {
                    field.setAccessible(true);
                    java.lang.Object obj2 = field.get(obj);
                    if (obj2 instanceof java.util.Map) {
                        for (java.lang.Object obj3 : ((java.util.Map) obj2).values()) {
                            if (clsFindClass.isInstance(obj3)) {
                                return obj3;
                            }
                        }
                    } else if (obj2 instanceof java.lang.Iterable) {
                        for (java.lang.Object obj4 : (java.lang.Iterable) obj2) {
                            if (clsFindClass.isInstance(obj4)) {
                                return obj4;
                            }
                        }
                    } else if (clsFindClass.isInstance(obj2)) {
                        return obj2;
                    }
                } catch (java.lang.Throwable unused) {
                    continue;
                }
            }
        }
        throw new java.lang.IllegalStateException("未找到 navigator: " + str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void onFriendCreate(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam, boolean z) {
        com.tencent.qqlitepro.hook.NativePickerBridge.Pending pending = sPending;
        if (pending == null) {
            return;
        }
        java.lang.Object obj = methodHookParam.thisObject;
        try {
            android.os.Bundle bundle = (android.os.Bundle) de.robv.android.xposed.XposedHelpers.callMethod(obj, "requireArguments", new java.lang.Object[0]);
            java.lang.String string = bundle.getString("REQUEST_KEY");
            if (string != null && string.equals(pending.requestKey)) {
                if (!z) {
                    pending.friendFragment = obj;
                }
                de.robv.android.xposed.XposedHelpers.setIntField(obj, "i", bundle.getInt("CAN_CONFIRM_SIZE", 0));
            }
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("原生选择器: FriendSelect 处理异常: " + th);
        }
    }

    private static void hookPickerAdapter(java.lang.String str, java.lang.ClassLoader classLoader) {
        try {
            int i = 0;
            for (java.lang.reflect.Method method : de.robv.android.xposed.XposedHelpers.findClass(str, classLoader).getMethods()) {
                if ("submitList".equals(method.getName())) {
                    java.lang.Class<?>[] parameterTypes = method.getParameterTypes();
                    if (parameterTypes.length >= 1 && parameterTypes.length <= 2) {
                        i++;
                        if (sHookedSubmitLists.add(method.toGenericString())) {
                            de.robv.android.xposed.XposedBridge.hookMethod(method, new com.tencent.qqlitepro.hook.NativePickerBridge.AnonymousClass13());
                            com.tencent.qqlitepro.util.Log.i("原生选择器: submitList hook 生效 " + method.toGenericString());
                        }
                    }
                }
            }
            if (i == 0) {
                com.tencent.qqlitepro.util.Log.w("原生选择器: submitList 未找到(" + str + ")");
            }
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("原生选择器: submitList hook 失败(" + str + "): " + th);
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.NativePickerBridge$13, reason: invalid class name */
    static class AnonymousClass13 extends de.robv.android.xposed.XC_MethodHook {
        AnonymousClass13() {
        }

        protected void beforeHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            if (com.tencent.qqlitepro.hook.NativePickerBridge.isPickerAdapter(methodHookParam.thisObject)) {
                com.tencent.qqlitepro.hook.NativePickerBridge.bumpRefreshGen(methodHookParam.thisObject);
                if (android.os.Looper.myLooper() == android.os.Looper.getMainLooper()) {
                    return;
                }
                java.lang.Object obj = methodHookParam.thisObject;
                java.lang.reflect.Method method = (java.lang.reflect.Method) methodHookParam.method;
                java.lang.Object[] objArr = methodHookParam.args == null ? new java.lang.Object[0] : (java.lang.Object[]) methodHookParam.args.clone();
                method.setAccessible(true);
                new android.os.Handler(android.os.Looper.getMainLooper()).post(new com.tencent.qqlitepro.hook.NativePickerBridge$13$$ExternalSyntheticLambda0(method, obj, objArr));
                methodHookParam.setResult((java.lang.Object) null);
            }
        }

        static /* synthetic */ void lambda$beforeHookedMethod$0(java.lang.reflect.Method method, java.lang.Object obj, java.lang.Object[] objArr) {
            try {
                method.invoke(obj, objArr);
            } catch (java.lang.Throwable th) {
                com.tencent.qqlitepro.util.Log.w("原生选择器: submitList 主线程重投异常: " + th);
            }
        }

        protected void afterHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            if (com.tencent.qqlitepro.hook.NativePickerBridge.isPickerAdapter(methodHookParam.thisObject) && android.os.Looper.myLooper() == android.os.Looper.getMainLooper() && com.tencent.qqlitepro.hook.NativePickerBridge.markInitialDataIfNeeded(methodHookParam.thisObject, methodHookParam.args)) {
                com.tencent.qqlitepro.hook.NativePickerBridge.onSubmitList(methodHookParam);
                com.tencent.qqlitepro.hook.NativePickerBridge.scheduleAdapterRefresh(methodHookParam.thisObject);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isPickerAdapter(java.lang.Object obj) {
        if (obj == null) {
            return false;
        }
        return obj.getClass().getName().startsWith("com.tencent.qqnt.watch.fs.rv.");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean markInitialDataIfNeeded(java.lang.Object obj, java.lang.Object[] objArr) {
        if (obj != null && objArr != null && objArr.length >= 1) {
            java.lang.Object obj2 = objArr[0];
            if ((obj2 instanceof java.util.List) && !((java.util.List) obj2).isEmpty()) {
                return sInitialDataDone.add(obj);
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void bumpRefreshGen(java.lang.Object obj) {
        if (obj == null) {
            return;
        }
        long j = sRefreshSeq;
        sRefreshSeq = 1 + j;
        java.util.Map<java.lang.Object, java.lang.Long> map = sRefreshGens;
        synchronized (map) {
            map.put(obj, java.lang.Long.valueOf(j));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void scheduleAdapterRefresh(java.lang.Object obj) {
        long j = sRefreshSeq;
        sRefreshSeq = 1 + j;
        java.util.Map<java.lang.Object, java.lang.Long> map = sRefreshGens;
        synchronized (map) {
            map.put(obj, java.lang.Long.valueOf(j));
        }
        android.os.Handler handler = new android.os.Handler(android.os.Looper.getMainLooper());
        long[] jArr = {0, 350, 1500};
        for (int i = 0; i < 3; i++) {
            long j2 = jArr[i];
            handler.postDelayed(new com.tencent.qqlitepro.hook.NativePickerBridge$$ExternalSyntheticLambda1(j, obj, j2), j2);
        }
    }

    static /* synthetic */ void lambda$scheduleAdapterRefresh$1(long j, java.lang.Object obj, long j2) {
        java.lang.Object objCallMethod;
        java.util.Map<java.lang.Object, java.lang.Long> map = sRefreshGens;
        synchronized (map) {
            if (java.lang.Long.valueOf(j).equals(map.get(obj))) {
                try {
                    android.view.View viewFindRecyclerView = findRecyclerView(obj);
                    if (viewFindRecyclerView != null && (objCallMethod = de.robv.android.xposed.XposedHelpers.callMethod(viewFindRecyclerView, "getAdapter", new java.lang.Object[0])) != null && objCallMethod == obj) {
                        de.robv.android.xposed.XposedHelpers.callMethod(objCallMethod, "notifyDataSetChanged", new java.lang.Object[0]);
                        de.robv.android.xposed.XposedHelpers.callMethod(viewFindRecyclerView, "requestLayout", new java.lang.Object[0]);
                        viewFindRecyclerView.invalidate();
                        if (j2 == 0 && ((java.lang.Integer) de.robv.android.xposed.XposedHelpers.callMethod(objCallMethod, "getItemCount", new java.lang.Object[0])).intValue() > 0 && sScrolledToTopOnce.add(obj)) {
                            de.robv.android.xposed.XposedHelpers.callMethod(viewFindRecyclerView, "scrollToPosition", new java.lang.Object[]{0});
                        }
                    }
                } catch (java.lang.Throwable unused) {
                }
            }
        }
    }

    private static android.view.View findRecyclerView(java.lang.Object obj) {
        try {
            java.lang.Class clsFindClass = de.robv.android.xposed.XposedHelpers.findClass(RECYCLER_VIEW_CLS, sCl);
            try {
                java.lang.Object objectField = de.robv.android.xposed.XposedHelpers.getObjectField(de.robv.android.xposed.XposedHelpers.getObjectField(obj, "mObservable"), "mObservers");
                if (objectField instanceof java.util.List) {
                    for (java.lang.Object obj2 : (java.util.List) objectField) {
                        if (obj2 != null) {
                            for (java.lang.reflect.Field field : obj2.getClass().getDeclaredFields()) {
                                field.setAccessible(true);
                                java.lang.Object obj3 = field.get(obj2);
                                if (clsFindClass.isInstance(obj3)) {
                                    return (android.view.View) obj3;
                                }
                            }
                        }
                    }
                }
            } catch (java.lang.Throwable unused) {
            }
            com.tencent.qqlitepro.hook.NativePickerBridge.Pending pending = sPending;
            if (pending != null && pending.hostAct != null) {
                return scanForRv(pending.hostAct.getWindow().getDecorView(), clsFindClass, obj);
            }
        } catch (java.lang.Throwable unused2) {
        }
        return null;
    }

    private static android.view.View scanForRv(android.view.View view, java.lang.Class<?> cls, java.lang.Object obj) {
        if (cls.isInstance(view)) {
            try {
                if (de.robv.android.xposed.XposedHelpers.callMethod(view, "getAdapter", new java.lang.Object[0]) == obj) {
                    return view;
                }
            } catch (java.lang.Throwable unused) {
            }
        }
        if (!(view instanceof android.view.ViewGroup)) {
            return null;
        }
        android.view.ViewGroup viewGroup = (android.view.ViewGroup) view;
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            android.view.View viewScanForRv = scanForRv(viewGroup.getChildAt(i), cls, obj);
            if (viewScanForRv != null) {
                return viewScanForRv;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:53:0x0102  */
    public static void onSubmitList(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
        java.lang.String str;
        com.tencent.qqlitepro.hook.NativePickerBridge.Pending pending = sPending;
        if (pending == null || pending.friendFragment == null || methodHookParam.thisObject != de.robv.android.xposed.XposedHelpers.getObjectField(pending.friendFragment, "p") || methodHookParam.args.length < 1 || !(methodHookParam.args[0] instanceof java.util.List)) {
            return;
        }
        try {
            java.util.List list = (java.util.List) methodHookParam.args[0];
            java.util.HashSet hashSet = (java.util.HashSet) de.robv.android.xposed.XposedHelpers.getObjectField(pending.friendFragment, "g");
            java.lang.Class clsFindClass = de.robv.android.xposed.XposedHelpers.findClass(ITEM_CLS, sCl);
            if (pending.uinMode) {
                for (java.lang.Object obj : list) {
                    java.lang.Object objectField = de.robv.android.xposed.XposedHelpers.getObjectField(obj, "b");
                    if (objectField != null && pending.preUins.contains(java.lang.String.valueOf(objectField))) {
                        de.robv.android.xposed.XposedHelpers.setBooleanField(obj, "d", true);
                        hashSet.add(obj);
                    }
                }
            } else {
                for (java.lang.Object obj2 : list) {
                    java.lang.Object objectField2 = de.robv.android.xposed.XposedHelpers.getObjectField(obj2, "a");
                    if (objectField2 != null) {
                        if (pending.preKeys.contains("1:" + objectField2)) {
                            de.robv.android.xposed.XposedHelpers.setBooleanField(obj2, "d", true);
                            hashSet.add(obj2);
                        }
                    }
                }
                for (java.lang.String str2 : pending.preKeys) {
                    if (str2.startsWith("2:")) {
                        java.lang.String strSubstring = str2.substring(2);
                        java.util.Iterator it = hashSet.iterator();
                        do {
                            if (!it.hasNext()) {
                                java.lang.String str3 = pending.lines.get(str2);
                                if (str3 != null) {
                                    java.lang.String[] strArrSplit = str3.split("\\|", 3);
                                    if (strArrSplit.length < 3 || strArrSplit[2].isEmpty()) {
                                        str = strSubstring;
                                    } else {
                                        str = strArrSplit[2];
                                    }
                                } else {
                                    str = strSubstring;
                                }
                                java.lang.Object objNewGroupSelectItem = newGroupSelectItem(clsFindClass, strSubstring, str);
                                hashSet.add(objNewGroupSelectItem);
                                java.util.Iterator it2 = list.iterator();
                                while (true) {
                                    if (it2.hasNext()) {
                                        java.lang.Object next = it2.next();
                                        if (de.robv.android.xposed.XposedHelpers.getBooleanField(next, "f") && strSubstring.equals(java.lang.String.valueOf(de.robv.android.xposed.XposedHelpers.getObjectField(next, "a")))) {
                                            break;
                                        }
                                    } else {
                                        list.add(objNewGroupSelectItem);
                                        break;
                                    }
                                }
                            }
                        } while (!strSubstring.equals(java.lang.String.valueOf(de.robv.android.xposed.XposedHelpers.getObjectField(it.next(), "a"))));
                    }
                }
            }
            int i = ((android.os.Bundle) de.robv.android.xposed.XposedHelpers.callMethod(pending.friendFragment, "requireArguments", new java.lang.Object[0])).getInt("CAN_CONFIRM_SIZE", 0);
            de.robv.android.xposed.XposedHelpers.setIntField(pending.friendFragment, "i", i);
            boolean z = hashSet.size() >= i;
            de.robv.android.xposed.XposedHelpers.setBooleanField(pending.friendFragment, "h", z);
            java.lang.Object objectField3 = de.robv.android.xposed.XposedHelpers.getObjectField(pending.friendFragment, "f");
            de.robv.android.xposed.XposedHelpers.callMethod(objectField3, "clearAnimation", new java.lang.Object[0]);
            de.robv.android.xposed.XposedHelpers.callMethod(objectField3, "setAlpha", new java.lang.Object[]{java.lang.Float.valueOf(1.0f)});
            java.lang.Object[] objArr = new java.lang.Object[1];
            objArr[0] = java.lang.Integer.valueOf(z ? 0 : 8);
            de.robv.android.xposed.XposedHelpers.callMethod(objectField3, "setVisibility", objArr);
            com.tencent.qqlitepro.util.Log.i("原生选择器: submitList size=" + list.size() + " 已选=" + hashSet.size());
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("原生选择器: 预选处理异常: " + th);
        }
    }

    private static java.lang.Object newGroupSelectItem(java.lang.Class<?> cls, java.lang.String str, java.lang.String str2) throws java.lang.Exception {
        java.lang.reflect.Constructor<?> declaredConstructor = cls.getDeclaredConstructor(java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.TYPE, java.lang.Boolean.TYPE, java.lang.Boolean.TYPE);
        java.lang.Object[] objArr = new java.lang.Object[6];
        objArr[0] = str;
        objArr[1] = "0";
        if (str2 != null && !str2.isEmpty()) {
            str = str2;
        }
        objArr[2] = str;
        objArr[3] = true;
        objArr[4] = false;
        objArr[5] = true;
        return declaredConstructor.newInstance(objArr);
    }

    static void schedulePreselectionReapply(java.lang.Object obj) {
        android.os.Handler handler = new android.os.Handler(android.os.Looper.getMainLooper());
        long[] jArr = {300, 900};
        for (int i = 0; i < 2; i++) {
            handler.postDelayed(new com.tencent.qqlitepro.hook.NativePickerBridge$$ExternalSyntheticLambda2(obj), jArr[i]);
        }
    }

    static /* synthetic */ void lambda$schedulePreselectionReapply$2(java.lang.Object obj) {
        try {
            reapplyPreselection(obj);
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("原生选择器: 预选兜底异常: " + th);
        }
    }

    /* JADX WARN: Code duplicated, block: B:31:0x00aa  */
    /* JADX WARN: Code duplicated, block: B:33:0x00b2  */
    /* JADX WARN: Code duplicated, block: B:53:0x0107  */
    /* JADX WARN: Code duplicated, block: B:59:0x012f A[PHI: r9
      0x012f: PHI (r9v2 boolean) = (r9v1 boolean), (r9v4 boolean) binds: [B:37:0x00bd, B:57:0x0119] A[DONT_GENERATE, DONT_INLINE]] */
    private static void reapplyPreselection(java.lang.Object obj) throws java.lang.Exception {
        int i;
        java.lang.String str;
        com.tencent.qqlitepro.hook.NativePickerBridge.Pending pending = sPending;
        if (pending == null || pending.finished || obj != pending.friendFragment) {
            return;
        }
        java.lang.Object objectField = de.robv.android.xposed.XposedHelpers.getObjectField(obj, "p");
        java.util.List list = (java.util.List) de.robv.android.xposed.XposedHelpers.callMethod(objectField, "getCurrentList", new java.lang.Object[0]);
        if (list == null) {
            return;
        }
        java.util.HashSet hashSet = (java.util.HashSet) de.robv.android.xposed.XposedHelpers.getObjectField(obj, "g");
        java.lang.Class clsFindClass = de.robv.android.xposed.XposedHelpers.findClass(ITEM_CLS, sCl);
        java.util.HashSet hashSet2 = new java.util.HashSet();
        java.util.Iterator it = list.iterator();
        boolean z = false;
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            java.lang.Object next = it.next();
            java.lang.String strValueOf = java.lang.String.valueOf(de.robv.android.xposed.XposedHelpers.getObjectField(next, "a"));
            boolean booleanField = de.robv.android.xposed.XposedHelpers.getBooleanField(next, "f");
            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            sb.append(booleanField ? "2:" : "1:");
            sb.append(strValueOf);
            hashSet2.add(sb.toString());
            if (pending.uinMode) {
                if (!booleanField && pending.preUins.contains(java.lang.String.valueOf(de.robv.android.xposed.XposedHelpers.getObjectField(next, "b")))) {
                    if (!de.robv.android.xposed.XposedHelpers.getBooleanField(next, "d")) {
                        de.robv.android.xposed.XposedHelpers.setBooleanField(next, "d", true);
                        z = true;
                    }
                    hashSet.add(next);
                }
            } else {
                java.util.Set<java.lang.String> set = pending.preKeys;
                java.lang.StringBuilder sb2 = new java.lang.StringBuilder();
                sb2.append(booleanField ? "2:" : "1:");
                sb2.append(strValueOf);
                if (set.contains(sb2.toString())) {
                    if (!de.robv.android.xposed.XposedHelpers.getBooleanField(next, "d")) {
                        de.robv.android.xposed.XposedHelpers.setBooleanField(next, "d", true);
                        z = true;
                    }
                    hashSet.add(next);
                }
            }
        }
        if (pending.uinMode) {
            i = 0;
        } else {
            java.util.ArrayList arrayList = new java.util.ArrayList();
            for (java.lang.String str2 : pending.preKeys) {
                if (str2.startsWith("2:") && !hashSet2.contains(str2)) {
                    java.lang.String strSubstring = str2.substring(2);
                    java.lang.String str3 = pending.lines.get(str2);
                    if (str3 != null) {
                        java.lang.String[] strArrSplit = str3.split("\\|", 3);
                        if (strArrSplit.length < 3 || strArrSplit[2].isEmpty()) {
                            str = strSubstring;
                        } else {
                            str = strArrSplit[2];
                        }
                    } else {
                        str = strSubstring;
                    }
                    java.lang.Object objNewGroupSelectItem = newGroupSelectItem(clsFindClass, strSubstring, str);
                    arrayList.add(objNewGroupSelectItem);
                    hashSet.add(objNewGroupSelectItem);
                    z = true;
                }
            }
            if (arrayList.isEmpty()) {
                i = 0;
            } else {
                java.util.ArrayList arrayList2 = new java.util.ArrayList(list);
                arrayList2.addAll(arrayList);
                i = 0;
                de.robv.android.xposed.XposedHelpers.callMethod(objectField, "submitList", new java.lang.Object[]{arrayList2});
            }
        }
        if (z) {
            de.robv.android.xposed.XposedHelpers.callMethod(objectField, "notifyDataSetChanged", new java.lang.Object[i]);
        }
        int i2 = ((android.os.Bundle) de.robv.android.xposed.XposedHelpers.callMethod(obj, "requireArguments", new java.lang.Object[i])).getInt("CAN_CONFIRM_SIZE", i);
        de.robv.android.xposed.XposedHelpers.setIntField(obj, "i", i2);
        boolean z2 = hashSet.size() >= i2;
        de.robv.android.xposed.XposedHelpers.setBooleanField(obj, "h", z2);
        java.lang.Object objectField2 = de.robv.android.xposed.XposedHelpers.getObjectField(obj, "f");
        if (objectField2 instanceof android.view.View) {
            ((android.view.View) objectField2).clearAnimation();
            de.robv.android.xposed.XposedHelpers.callMethod(objectField2, "setAlpha", new java.lang.Object[]{java.lang.Float.valueOf(1.0f)});
            java.lang.Object[] objArr = new java.lang.Object[1];
            objArr[0] = java.lang.Integer.valueOf(z2 ? 0 : 8);
            de.robv.android.xposed.XposedHelpers.callMethod(objectField2, "setVisibility", objArr);
        }
        if (z) {
            com.tencent.qqlitepro.util.Log.i("原生选择器: 预选兜底已重刷 已选=" + hashSet.size());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void onGroupMemberFix(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
        if (sPending == null) {
            return;
        }
        java.lang.Object obj = methodHookParam.thisObject;
        if (isOurFlowFragment(obj)) {
            try {
                android.os.Bundle bundle = (android.os.Bundle) de.robv.android.xposed.XposedHelpers.callMethod(obj, "requireArguments", new java.lang.Object[0]);
                de.robv.android.xposed.XposedHelpers.setIntField(obj, "j", clampSize(bundle.getInt("CAN_CONFIRM_SIZE", 0)));
                de.robv.android.xposed.XposedHelpers.setIntField(obj, "k", clampMax(bundle.getInt("KEY_MAX_SIZE", 0)));
            } catch (java.lang.Throwable unused) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void onGroupListFix(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
        if (sPending == null) {
            return;
        }
        java.lang.Object obj = methodHookParam.thisObject;
        if (isOurFlowFragment(obj)) {
            try {
                de.robv.android.xposed.XposedHelpers.setIntField(obj, "i", clampSize(((android.os.Bundle) de.robv.android.xposed.XposedHelpers.callMethod(obj, "requireArguments", new java.lang.Object[0])).getInt("CAN_CONFIRM_SIZE", 0)));
            } catch (java.lang.Throwable unused) {
            }
        }
    }

    private static void onFragmentResult(java.lang.Object obj, java.lang.String str, android.os.Bundle bundle) {
        com.tencent.qqlitepro.hook.NativePickerBridge.Pending pending = sPending;
        if (pending == null || !str.equals(pending.requestKey) || pending.finished) {
            return;
        }
        pending.finished = true;
        boolean z = bundle.getBoolean("RESULT_IS_CANCEL", true);
        java.util.ArrayList parcelableArrayList = bundle.getParcelableArrayList("RESULT_LIST");
        if (!z) {
            java.util.ArrayList arrayList = new java.util.ArrayList();
            if (parcelableArrayList != null) {
                for (java.lang.Object obj2 : parcelableArrayList) {
                    java.lang.String strValueOf = java.lang.String.valueOf(de.robv.android.xposed.XposedHelpers.getObjectField(obj2, "b"));
                    java.lang.String strValueOf2 = java.lang.String.valueOf(de.robv.android.xposed.XposedHelpers.getObjectField(obj2, "c"));
                    java.lang.String strValueOf3 = java.lang.String.valueOf(de.robv.android.xposed.XposedHelpers.getObjectField(obj2, "d"));
                    java.lang.String[] strArr = new java.lang.String[4];
                    strArr[0] = de.robv.android.xposed.XposedHelpers.getBooleanField(obj2, "e") ? "2" : "1";
                    strArr[1] = strValueOf;
                    strArr[2] = strValueOf3;
                    strArr[3] = strValueOf2;
                    arrayList.add(strArr);
                }
            }
            com.tencent.qqlitepro.util.Log.i("原生选择器: 结果监听捕获 " + arrayList.size() + " 个会话");
            consumeResultAndConfirm(pending, arrayList);
        }
        finishPickerActivity(pending, obj);
        if (z) {
            sPending = null;
        }
    }

    private static void consumeResultAndConfirm(com.tencent.qqlitepro.hook.NativePickerBridge.Pending pending, java.util.List<java.lang.String[]> list) {
        try {
            com.tencent.qqlitepro.hook.SettingsInjector.acceptNativeResult(list);
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("原生选择器: 结果回填异常: " + th);
        }
        pending.confirmDone = true;
        java.lang.Runnable runnable = pending.confirm;
        sPending = null;
        if (runnable != null) {
            new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(new com.tencent.qqlitepro.hook.NativePickerBridge$$ExternalSyntheticLambda4(runnable), 300L);
        }
    }

    static /* synthetic */ void lambda$consumeResultAndConfirm$3(java.lang.Runnable runnable) {
        com.tencent.qqlitepro.util.Log.i("原生选择器: 执行确认回调(post)");
        try {
            runnable.run();
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("原生选择器: 确认回调异常(post): " + th);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void onConfirmCapture(java.lang.Object obj, java.lang.Object obj2) {
        com.tencent.qqlitepro.hook.NativePickerBridge.Pending pending = sPending;
        if (pending == null || pending.finished || obj == null || !isOurFlowFragment(obj)) {
            return;
        }
        pending.finished = true;
        java.util.ArrayList arrayList = new java.util.ArrayList();
        if (obj2 instanceof java.util.Collection) {
            for (java.lang.Object obj3 : (java.util.Collection) obj2) {
                if (obj3 != null) {
                    try {
                        java.lang.String strValueOf = java.lang.String.valueOf(de.robv.android.xposed.XposedHelpers.getObjectField(obj3, "b"));
                        java.lang.String strValueOf2 = java.lang.String.valueOf(de.robv.android.xposed.XposedHelpers.getObjectField(obj3, "c"));
                        java.lang.String strValueOf3 = java.lang.String.valueOf(de.robv.android.xposed.XposedHelpers.getObjectField(obj3, "d"));
                        java.lang.String[] strArr = new java.lang.String[4];
                        strArr[0] = de.robv.android.xposed.XposedHelpers.getBooleanField(obj3, "e") ? "2" : "1";
                        strArr[1] = strValueOf;
                        strArr[2] = strValueOf3;
                        strArr[3] = strValueOf2;
                        arrayList.add(strArr);
                    } catch (java.lang.Throwable unused) {
                    }
                }
            }
        }
        com.tencent.qqlitepro.util.Log.i("原生选择器: c0 兜底捕获 " + arrayList.size() + " 个会话");
        consumeResultAndConfirm(pending, arrayList);
        finishPickerActivity(pending, obj);
    }

    private static void finishPickerActivity(com.tencent.qqlitepro.hook.NativePickerBridge.Pending pending, java.lang.Object obj) {
        android.app.Activity activity = null;
        if (obj != null) {
            try {
                java.lang.Object objCallMethod = de.robv.android.xposed.XposedHelpers.callMethod(obj, "requireActivity", new java.lang.Object[0]);
                if (objCallMethod instanceof android.app.Activity) {
                    activity = (android.app.Activity) objCallMethod;
                }
            } catch (java.lang.Throwable unused) {
            }
        }
        if (activity == null && pending != null) {
            try {
                if (pending.hostAct != null) {
                    activity = pending.hostAct;
                }
            } catch (java.lang.Throwable unused2) {
                return;
            }
        }
        if (activity == null) {
            return;
        }
        new android.os.Handler(android.os.Looper.getMainLooper()).post(new com.tencent.qqlitepro.hook.NativePickerBridge$$ExternalSyntheticLambda3(activity));
    }

    static /* synthetic */ void lambda$finishPickerActivity$4(android.app.Activity activity) {
        try {
            if (activity.isFinishing()) {
                return;
            }
            activity.finish();
        } catch (java.lang.Throwable unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isOurFlowFragment(java.lang.Object obj) {
        com.tencent.qqlitepro.hook.NativePickerBridge.Pending pending = sPending;
        if (pending == null || pending.finished || obj == null) {
            return false;
        }
        try {
            java.lang.Object objCallMethod = de.robv.android.xposed.XposedHelpers.callMethod(obj, "requireActivity", new java.lang.Object[0]);
            android.content.Intent intent = objCallMethod instanceof android.app.Activity ? ((android.app.Activity) objCallMethod).getIntent() : null;
            return intent != null && intent.hasExtra(EXTRA_PICK);
        } catch (java.lang.Throwable unused) {
            return false;
        }
    }

    private static int mustId(android.app.Activity activity, java.lang.String str) {
        int iIdOrZero = idOrZero(str);
        if (iIdOrZero == 0 && activity != null) {
            iIdOrZero = activity.getResources().getIdentifier(str, "id", activity.getPackageName());
        }
        if (iIdOrZero != 0) {
            return iIdOrZero;
        }
        throw new java.lang.IllegalStateException("缺少宿主资源 id: " + str);
    }

    private static int idOrZero(java.lang.String str) {
        int staticIntField;
        java.lang.Integer num = sIdCache.get(str);
        if (num != null) {
            return num.intValue();
        }
        try {
            staticIntField = de.robv.android.xposed.XposedHelpers.getStaticIntField(de.robv.android.xposed.XposedHelpers.findClass("com.tencent.qqlite.R$id", sCl), str);
        } catch (java.lang.Throwable unused) {
            staticIntField = 0;
        }
        sIdCache.put(str, java.lang.Integer.valueOf(staticIntField));
        return staticIntField;
    }

}
