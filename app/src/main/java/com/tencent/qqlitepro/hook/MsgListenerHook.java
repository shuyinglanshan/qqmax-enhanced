package com.tencent.qqlitepro.hook;

/* JADX INFO: loaded from: classes.dex */
public final class MsgListenerHook {
    private static final long AI_REPLY_COOLDOWN_MS = 15000;
    private static final long DISPATCH_DEDUP_MS = 300000;
    private static final long REPLY_COOLDOWN_MS = 3000;
    private static final long REPLY_DEDUP_MS = 600000;
    private static final java.lang.String SELF_AT_SENTINEL = "\u0000@self\u0000";
    private static final long SELF_DEDUP_MS = 120000;
    private static volatile java.lang.String sSelfUid;
    private static final java.util.Map<java.lang.String, java.lang.Long> sSentIds = new java.util.LinkedHashMap();
    private static final java.util.Map<java.lang.String, java.lang.Long> sLastReplyAt = new java.util.LinkedHashMap();
    static volatile long sRecvCount = 0;
    private static final java.util.Map<java.lang.String, java.lang.Long> sDispatchedIds = new java.util.LinkedHashMap();
    private static final java.util.Map<java.lang.String, java.lang.Long> sRepliedIds = new java.util.LinkedHashMap();
    private static final java.util.Map<java.lang.String, java.lang.Long> sAiCooldown = new java.util.LinkedHashMap();
    private static final java.lang.String[] CANDIDATES = {"com.tencent.watch.aio_impl.kernel.adapter.WatchAIOFetchService$registerMsgListener$2", "com.tencent.watch.aio_impl.kernel.adapter.WatchAIOFetchService$registerMsgListener$1", "com.tencent.watch.aio_impl.kernel.adapter.WatchAIOFetchService$registerMsgListener$0", "com.tencent.qqlite.aio_impl.kernel.adapter.WatchAIOFetchService$registerMsgListener$2", "com.tencent.app.aio_impl.kernel.adapter.WatchAIOFetchService$registerMsgListener$2"};
    private static final com.google.gson.Gson GSON = new com.google.gson.GsonBuilder().disableHtmlEscaping().create();

    public static java.lang.String selfUid() {
        return sSelfUid;
    }

    private MsgListenerHook() {
    }

    public static java.lang.Class<?> findListener(java.lang.ClassLoader classLoader) {
        for (java.lang.String str : CANDIDATES) {
            try {
                java.lang.Class<?> cls = java.lang.Class.forName(str, false, classLoader);
                if (cls != null) {
                    com.tencent.qqlitepro.util.Log.i("命中监听器类: " + str);
                    return cls;
                }
            } catch (java.lang.Throwable unused) {
            }
        }
        java.lang.Class<?> clsScanLoaded = scanLoaded(classLoader, "registerMsgListener");
        if (clsScanLoaded != null) {
            com.tencent.qqlitepro.util.Log.i("模糊扫描命中: " + clsScanLoaded.getName());
        }
        return clsScanLoaded;
    }

    private static java.lang.Class<?> scanLoaded(java.lang.ClassLoader classLoader, java.lang.String str) {
        try {
            for (java.lang.Object obj : (java.lang.Object[]) de.robv.android.xposed.XposedBridge.class.getMethod("getLoadedClasses", new java.lang.Class[0]).invoke(null, new java.lang.Object[0])) {
                java.lang.String strValueOf = java.lang.String.valueOf(obj);
                if (strValueOf.contains(str) && strValueOf.contains("WatchAIOFetchService")) {
                    try {
                        java.lang.Class<?> cls = java.lang.Class.forName(strValueOf, false, classLoader);
                        if (cls != null) {
                            return cls;
                        }
                    } catch (java.lang.Throwable unused) {
                        continue;
                    }
                }
            }
        } catch (java.lang.Throwable unused2) {
        }
        return null;
    }

    public static void hook(java.lang.Class<?> cls) {
        if (cls == null) {
            com.tencent.qqlitepro.util.Log.e("监听器类为 null，跳过 hook");
            return;
        }
        com.tencent.qqlitepro.hook.MsgListenerHook.AnonymousClass1 anonymousClass1 = new com.tencent.qqlitepro.hook.MsgListenerHook.AnonymousClass1();
        com.tencent.qqlitepro.hook.MsgListenerHook.AnonymousClass2 anonymousClass2 = new com.tencent.qqlitepro.hook.MsgListenerHook.AnonymousClass2();
        com.tencent.qqlitepro.hook.MsgListenerHook.AnonymousClass3 anonymousClass3 = new com.tencent.qqlitepro.hook.MsgListenerHook.AnonymousClass3();
        int i = 0;
        for (java.lang.reflect.Method method : cls.getDeclaredMethods()) {
            java.lang.String name = method.getName();
            try {
                if (name.equals("onRecvMsg") || name.equals("onMsgInfoListUpdate") || name.equals("onMsgInfoListAdd")) {
                    de.robv.android.xposed.XposedBridge.hookMethod(method, anonymousClass2);
                } else if (name.equals("onAddSendMsg")) {
                    de.robv.android.xposed.XposedBridge.hookMethod(method, anonymousClass3);
                } else {
                    if (isRecallSignal(name)) {
                        de.robv.android.xposed.XposedBridge.hookMethod(method, anonymousClass1);
                    }
                }
                i++;
            } catch (java.lang.Throwable th) {
                com.tencent.qqlitepro.util.Log.w("hook 方法失败 " + method.getName() + ": " + th);
            }
        }
        com.tencent.qqlitepro.util.Log.i("消息监听 hook 完成，共命中 " + i + " 个方法 -> " + cls.getName());
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.MsgListenerHook$1, reason: invalid class name */
    static class AnonymousClass1 extends de.robv.android.xposed.XC_MethodHook {
        AnonymousClass1() {
        }

        protected void beforeHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            try {
                com.tencent.qqlitepro.hook.MsgListenerHook.callbackOnMsgDelete(methodHookParam);
                com.tencent.qqlitepro.hook.MsgListenerHook.applyBlockMode(methodHookParam);
            } catch (java.lang.Throwable th) {
                com.tencent.qqlitepro.util.Log.w("onMsgDelete 处理异常: " + th);
            }
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.MsgListenerHook$2, reason: invalid class name */
    static class AnonymousClass2 extends de.robv.android.xposed.XC_MethodHook {
        AnonymousClass2() {
        }

        protected void beforeHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            try {
                com.tencent.qqlitepro.hook.MsgListenerHook.callbackCacheList(methodHookParam, 0);
            } catch (java.lang.Throwable th) {
                com.tencent.qqlitepro.util.Log.w("消息列表缓存异常: " + th);
            }
            com.tencent.qqlitepro.hook.MsgListenerHook.maybeReplyAll(methodHookParam, 0);
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.MsgListenerHook$3, reason: invalid class name */
    static class AnonymousClass3 extends de.robv.android.xposed.XC_MethodHook {
        AnonymousClass3() {
        }

        protected void beforeHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            try {
                com.tencent.qqlitepro.hook.MsgListenerHook.callbackAddSendMsg(methodHookParam);
            } catch (java.lang.Throwable th) {
                com.tencent.qqlitepro.util.Log.w("onAddSendMsg 处理异常: " + th);
            }
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.MsgListenerHook$4, reason: invalid class name */
    static class AnonymousClass4 extends de.robv.android.xposed.XC_MethodHook {
        AnonymousClass4() {
        }

        protected void beforeHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            try {
                com.tencent.qqlitepro.hook.MsgListenerHook.callbackCacheList(methodHookParam, 0);
            } catch (java.lang.Throwable th) {
                com.tencent.qqlitepro.util.Log.w("全局消息缓存异常: " + th);
            }
            com.tencent.qqlitepro.hook.MsgListenerHook.maybeReplyAll(methodHookParam, 0);
        }
    }

    public static void hookGlobal(java.lang.ClassLoader classLoader) {
        try {
            java.lang.Class<?> cls = java.lang.Class.forName("com.tencent.qqnt.msg.MsgService$init$1", false, classLoader);
            com.tencent.qqlitepro.hook.MsgListenerHook.AnonymousClass4 anonymousClass4 = new com.tencent.qqlitepro.hook.MsgListenerHook.AnonymousClass4();
            com.tencent.qqlitepro.hook.MsgListenerHook.AnonymousClass5 anonymousClass5 = new com.tencent.qqlitepro.hook.MsgListenerHook.AnonymousClass5();
            com.tencent.qqlitepro.hook.MsgListenerHook.AnonymousClass6 anonymousClass6 = new com.tencent.qqlitepro.hook.MsgListenerHook.AnonymousClass6();
            int i = 0;
            for (java.lang.reflect.Method method : cls.getDeclaredMethods()) {
                java.lang.String name = method.getName();
                try {
                    if (name.equals("onRecvMsg") || name.equals("onMsgInfoListUpdate") || name.equals("onMsgInfoListAdd")) {
                        de.robv.android.xposed.XposedBridge.hookMethod(method, anonymousClass4);
                    } else if (name.equals("onAddSendMsg")) {
                        de.robv.android.xposed.XposedBridge.hookMethod(method, anonymousClass5);
                    } else {
                        if (name.equals("onMsgDelete")) {
                            de.robv.android.xposed.XposedBridge.hookMethod(method, anonymousClass6);
                        }
                    }
                    i++;
                } catch (java.lang.Throwable th) {
                    com.tencent.qqlitepro.util.Log.w("hook 全局方法 " + method.getName() + " 失败: " + th);
                }
            }
            com.tencent.qqlitepro.util.Log.i("全局消息回调 hook 完成，共命中 " + i + " 个方法 -> com.tencent.qqnt.msg.MsgService$init$1");
        } catch (java.lang.Throwable th2) {
            com.tencent.qqlitepro.util.Log.w("hook 全局接收器失败: " + th2 + "（不影响当前会话 hook）");
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.MsgListenerHook$5, reason: invalid class name */
    static class AnonymousClass5 extends de.robv.android.xposed.XC_MethodHook {
        AnonymousClass5() {
        }

        protected void beforeHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            try {
                com.tencent.qqlitepro.hook.MsgListenerHook.callbackAddSendMsg(methodHookParam);
            } catch (java.lang.Throwable th) {
                com.tencent.qqlitepro.util.Log.w("全局发送缓存异常: " + th);
            }
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.MsgListenerHook$6, reason: invalid class name */
    static class AnonymousClass6 extends de.robv.android.xposed.XC_MethodHook {
        AnonymousClass6() {
        }

        protected void beforeHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            try {
                com.tencent.qqlitepro.hook.MsgListenerHook.callbackOnMsgDelete(methodHookParam);
                com.tencent.qqlitepro.hook.MsgListenerHook.applyBlockMode(methodHookParam);
            } catch (java.lang.Throwable th) {
                com.tencent.qqlitepro.util.Log.w("全局撤回/删除异常: " + th);
            }
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.MsgListenerHook$7, reason: invalid class name */
    static class AnonymousClass7 extends de.robv.android.xposed.XC_MethodHook {
        AnonymousClass7() {
        }

        protected void beforeHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            java.util.List list;
            java.lang.Object fieldValue;
            java.lang.String strFieldString;
            try {
                if (methodHookParam.args == null) {
                    return;
                }
                java.lang.Object[] objArr = methodHookParam.args;
                int length = objArr.length;
                int i = 0;
                while (true) {
                    if (i >= length) {
                        list = null;
                        break;
                    }
                    java.lang.Object obj = objArr[i];
                    if (obj instanceof java.util.List) {
                        list = (java.util.List) obj;
                        break;
                    }
                    i++;
                }
                if (list == null) {
                    return;
                }
                java.lang.StringBuilder sb = new java.lang.StringBuilder();
                for (java.lang.Object obj2 : list) {
                    if (obj2 != null && (fieldValue = com.tencent.qqlitepro.hook.MsgListenerHook.getFieldValue(obj2, "textElement")) != null && (strFieldString = com.tencent.qqlitepro.hook.MsgListenerHook.fieldString(fieldValue, "content")) != null && !strFieldString.isEmpty()) {
                        sb.append(strFieldString);
                    }
                }
                /* 取本次发送的目标会话 peerUid（用于按白名单匹配多后缀规则） */
                java.lang.String peerUidArg = com.tencent.qqlitepro.hook.MsgListenerHook
                        .extractPeerUid(methodHookParam.args);
                com.tencent.qqlitepro.hook.MsgListenerHook
                        .applyCatgirlSuffix(list, sb.toString(), peerUidArg);
            } catch (java.lang.Throwable th) {
                com.tencent.qqlitepro.util.Log.w("发送拦截处理异常: " + th);
            }
        }
    }

    public static void hookSendSuffix(java.lang.ClassLoader classLoader) {
        try {
            java.lang.Class<?> cls = java.lang.Class.forName("com.tencent.qqnt.msg.api.impl.MsgServiceImpl", false, classLoader);
            com.tencent.qqlitepro.hook.MsgListenerHook.AnonymousClass7 anonymousClass7 = new com.tencent.qqlitepro.hook.MsgListenerHook.AnonymousClass7();
            int i = 0;
            for (java.lang.reflect.Method method : cls.getDeclaredMethods()) {
                if ("sendMsg".equals(method.getName())) {
                    for (java.lang.Class<?> cls2 : method.getParameterTypes()) {
                        if (cls2 == java.util.ArrayList.class) {
                            de.robv.android.xposed.XposedBridge.hookMethod(method, anonymousClass7);
                            i++;
                            break;
                        }
                    }
                }
            }
            com.tencent.qqlitepro.util.Log.i("发送拦截 hook 已注入 MsgServiceImpl.sendMsg，共命中 " + i + " 个方法");
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("发送拦截 hook 注入失败: " + th);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void applyCatgirlSuffix(java.util.List<java.lang.Object> list, java.lang.String str) {
        applyCatgirlSuffix(list, str, null);
    }

    /** 从发送参数里提取目标会话 peerUid */
    public static java.lang.String extractPeerUid(java.lang.Object[] args) {
        if (args == null) {
            return null;
        }
        for (java.lang.Object a : args) {
            if (a == null) {
                continue;
            }
            String cn = a.getClass().getName();
            if (!cn.contains("Contact") && !cn.contains("Peer")) {
                continue;
            }
            try {
                java.lang.Object uid = getFieldValue(a, "peerUid");
                if (uid != null && !String.valueOf(uid).isEmpty()) {
                    return String.valueOf(uid);
                }
            } catch (Throwable ignored) {}
            try {
                java.lang.Object chatType = getFieldValue(a, "chatType");
                java.lang.Object peerUin = getFieldValue(a, "peerUin");
                if (chatType != null && peerUin != null) {
                    return String.valueOf(peerUin);
                }
            } catch (Throwable ignored) {}
        }
        return null;
    }

    /**
     * 自定义后缀（支持多条规则，每条规则独立白名单）。
     * 命中规则：白名单为空=全部会话；否则只有 peerUid 在白名单内才追加。
     */
    public static void applyCatgirlSuffix(java.util.List<java.lang.Object> list, java.lang.String str,
                                         java.lang.String peerUid) {
        java.lang.String strCatgirlSuffix;
        java.lang.Object fieldValue;
        try {
            android.content.Context contextAppContext = appContext();
            if (contextAppContext == null
                    || !com.tencent.qqlitepro.util.Config.catgirlEnabled(contextAppContext)
                    || str == null || str.isEmpty()) {
                return;
            }
            /* 白名单判定：空白名单=全部会话；否则只有名单内会话才追加 */
            if (!com.tencent.qqlitepro.util.Config.catgirlPeerAllowed(contextAppContext, peerUid)) {
                return;
            }
            strCatgirlSuffix = com.tencent.qqlitepro.util.Config.catgirlSuffix(contextAppContext);
            if (strCatgirlSuffix == null || strCatgirlSuffix.isEmpty()) {
                return;
            }
            if (true) {
                java.lang.Object obj = null;
                for (java.lang.Object obj2 : list) {
                    if (obj2 != null && (fieldValue = getFieldValue(obj2, "textElement")) != null) {
                        obj = fieldValue;
                    }
                }
                if (obj == null) {
                    return;
                }
                java.lang.String strFieldString = fieldString(obj, "content");
                if (strFieldString == null) {
                    strFieldString = "";
                }
                de.robv.android.xposed.XposedHelpers.setObjectField(obj, "content", strFieldString + strCatgirlSuffix);
                com.tencent.qqlitepro.util.Log.i("自定义后缀: 已在发送消息末尾追加“" + strCatgirlSuffix + "”");
            }
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("自定义后缀处理异常: " + th);
        }
    }

    private static boolean isRecallSignal(java.lang.String str) {
        if (str.startsWith("is") || str.startsWith("get")) {
            return false;
        }
        return str.contains("Delete") || str.contains("Recall");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void callbackOnMsgDelete(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
        int i = 0;
        try {
            java.lang.StringBuilder sb = new java.lang.StringBuilder("!! 触发撤回/删除回调: ");
            sb.append(methodHookParam.method == null ? "<?" : methodHookParam.method.getName());
            sb.append("  argsLen=");
            sb.append(methodHookParam.args == null ? 0 : methodHookParam.args.length);
            com.tencent.qqlitepro.util.Log.i(sb.toString());
        } catch (java.lang.Throwable unused) {
        }
        if (antiRecallEnabled()) {
            com.tencent.qqlitepro.cache.MsgCache msgCache = com.tencent.qqlitepro.cache.MsgCache.get();
            java.lang.String str = null;
            if (methodHookParam.args != null) {
                java.lang.Object[] objArr = methodHookParam.args;
                int length = objArr.length;
                java.lang.String str2 = null;
                int i2 = 0;
                while (i < length) {
                    java.lang.Object obj = objArr[i];
                    if (obj != null) {
                        if ((obj instanceof java.lang.String) && str2 == null) {
                            java.lang.String str3 = (java.lang.String) obj;
                            if (!str3.isEmpty() && str3.length() < 30) {
                                str2 = str3;
                            }
                        }
                        java.util.Iterator<?> it = asIterable(obj).iterator();
                        while (it.hasNext()) {
                            java.lang.String strIdString = idString(it.next());
                            if (strIdString != null) {
                                if (msgCache.isSelfMessage(strIdString)) {
                                    com.tencent.qqlitepro.util.Log.i("删除/撤回跳过自身消息(不描边): msgId=" + strIdString);
                                } else {
                                    msgCache.markRecalled(strIdString);
                                    com.tencent.qqlitepro.util.Log.i("捕获撤回/删除: msgId=" + strIdString + " peer=" + str2);
                                    i2 = 1;
                                }
                            }
                        }
                    }
                    i++;
                }
                i = i2;
                str = str2;
            }
            if (i != 0) {
                try {
                    com.tencent.qqlitepro.hook.UiPatcher.onMessagesRecalled();
                } catch (java.lang.Throwable unused2) {
                }
            }
            if (i == 0) {
                com.tencent.qqlitepro.util.Log.i("撤回/删除回调未解析出 msgId（peer=" + str + "）");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void applyBlockMode(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
        try {
            if (antiRecallEnabled() && com.tencent.qqlitepro.util.Config.mode(appContext()) == 1) {
                java.lang.reflect.Method method = methodHookParam.method instanceof java.lang.reflect.Method ? (java.lang.reflect.Method) methodHookParam.method : null;
                if (method != null && method.getReturnType() == java.lang.Void.TYPE) {
                    methodHookParam.setResult((java.lang.Object) null);
                    com.tencent.qqlitepro.util.Log.i("强制阻止撤回：拦截 onMsgDelete，消息保留（红边待渲染）");
                } else {
                    java.lang.StringBuilder sb = new java.lang.StringBuilder("强制阻止撤回：跳过非 void 回调 ");
                    sb.append(method == null ? "<?>" : method.getName());
                    com.tencent.qqlitepro.util.Log.i(sb.toString());
                }
            }
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("强制阻止撤回失败: " + th);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void callbackCacheList(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam, int i) {
        if (antiRecallEnabled()) {
            java.lang.Object objArg = arg(methodHookParam, i);
            com.tencent.qqlitepro.cache.MsgCache msgCache = com.tencent.qqlitepro.cache.MsgCache.get();
            int i2 = 0;
            boolean z = true;
            try {
                if (com.tencent.qqlitepro.util.Config.mode(appContext()) != 1) {
                    z = false;
                }
            } catch (java.lang.Throwable unused) {
            }
            int i3 = 0;
            int i4 = 0;
            for (java.lang.Object obj : asIterable(objArg)) {
                if (obj != null) {
                    try {
                        if (msgCache.isRecallRecord(obj)) {
                            if (z) {
                                if (msgCache.restoreInto(obj)) {
                                    i2++;
                                }
                            } else {
                                msgCache.feedTip(fieldString(obj, "msgId"));
                                i4++;
                            }
                        } else if (doCache(msgCache, obj, null)) {
                            i3++;
                            sRecvCount++;
                            dumpRaw(obj);
                            collectFakeParts(obj);
                        }
                    } catch (java.lang.Throwable th) {
                        com.tencent.qqlitepro.util.Log.w("列表项处理异常: " + th);
                    }
                }
            }
            if (i2 > 0) {
                com.tencent.qqlitepro.util.Log.i("本次还原被撤回消息原文 " + i2 + " 条");
                try {
                    com.tencent.qqlitepro.hook.UiPatcher.onMessagesRecalled();
                } catch (java.lang.Throwable unused2) {
                }
            }
            if (i4 > 0) {
                com.tencent.qqlitepro.util.Log.i("本次向灰条投喂原文 " + i4 + " 条");
            }
            if (i3 > 0) {
                com.tencent.qqlitepro.util.Log.i("消息列表缓存成功 " + i3 + " 条");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void callbackAddSendMsg(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
        java.lang.Object objArg = arg(methodHookParam, 0);
        if (doCache(com.tencent.qqlitepro.cache.MsgCache.get(), objArg, null)) {
            com.tencent.qqlitepro.util.Log.i("发送/同步消息已缓存: msgId=" + fieldString(objArg, "msgId"));
            dumpRaw(objArg);
        }
        collectFakeParts(objArg);
        try {
            java.lang.String strFieldString = fieldString(objArg, "msgId");
            if (strFieldString == null || strFieldString.isEmpty()) {
                strFieldString = java.lang.String.valueOf(java.lang.System.nanoTime());
            }
            java.util.Map<java.lang.String, java.lang.Long> map = sSentIds;
            synchronized (map) {
                try {
                    map.put(strFieldString, java.lang.Long.valueOf(java.lang.System.currentTimeMillis()));
                } catch (java.lang.Throwable th) {
                    throw th;
                }
            }
        } catch (java.lang.Throwable unused) {
        }
        try {
            java.lang.String strFieldString2 = fieldString(objArg, "senderUid");
            if (strFieldString2 == null || strFieldString2.isEmpty()) {
                return;
            }
            sSelfUid = strFieldString2;
        } catch (java.lang.Throwable unused2) {
        }
    }

    private static void dumpRaw(java.lang.Object obj) {
        try {
            java.lang.String strFieldString = fieldString(obj, "msgId");
            java.lang.String strFieldString2 = fieldString(obj, "peerUid");
            int iFieldInt = fieldInt(obj, "chatType", 2);
            java.lang.String json = GSON.toJson(obj);
            if (com.tencent.qqlitepro.cache.RawMsgStore.offer(strFieldString, strFieldString2, iFieldInt, json)) {
                com.tencent.qqlitepro.util.Log.i("接收消息原始JSON: " + json);
            }
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("消息 raw JSON 序列化失败: " + th);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void maybeReplyAll(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam, int i) {
        try {
            for (java.lang.Object obj : asIterable(arg(methodHookParam, i))) {
                if (obj != null) {
                    try {
                        maybeReply(obj);
                    } catch (java.lang.Throwable th) {
                        com.tencent.qqlitepro.util.Log.w("回复判断异常: " + th);
                    }
                }
            }
        } catch (java.lang.Throwable th2) {
            com.tencent.qqlitepro.util.Log.w("回复批处理异常: " + th2);
        }
    }

    private static boolean isSelfEcho(java.lang.Object obj) {
        if (obj == null) {
            return false;
        }
        java.lang.String strFieldString = fieldString(obj, "msgId");
        if (strFieldString != null && !strFieldString.isEmpty()) {
            java.util.Map<java.lang.String, java.lang.Long> map = sSentIds;
            synchronized (map) {
                boolean zContainsKey = map.containsKey(strFieldString);
                trimMap(map, SELF_DEDUP_MS);
                if (zContainsKey) {
                    return true;
                }
            }
        }
        java.lang.String strFieldString2 = fieldString(obj, "senderUid");
        java.lang.String str = sSelfUid;
        if (strFieldString2 != null && !strFieldString2.isEmpty()) {
            if (str != null && !str.isEmpty() && str.equals(strFieldString2)) {
                return true;
            }
            try {
                if (com.tencent.qqlitepro.http.QqKernel.isSelfTarget(strFieldString2)) {
                    return true;
                }
            } catch (java.lang.Throwable unused) {
            }
        }
        java.lang.String strFieldString3 = fieldString(obj, "senderUin");
        if (strFieldString3 == null || strFieldString3.isEmpty()) {
            strFieldString3 = fieldString(obj, "sendUin");
        }
        if (strFieldString3 != null && !strFieldString3.isEmpty()) {
            try {
                if (com.tencent.qqlitepro.http.QqKernel.isSelfTarget(strFieldString3)) {
                    return true;
                }
            } catch (java.lang.Throwable unused2) {
            }
        }
        return false;
    }

    /* JADX WARN: Code duplicated, block: B:64:0x00ed  */
    private static void maybeReply(java.lang.Object obj) {
        android.content.Context contextAppContext;
        java.lang.String strKeywordReplyContent;
        java.lang.String str;
        java.lang.String strAutoReplyContent;
        boolean zAutoReplyAt;
        if (obj == null || isSelfEcho(obj) || (contextAppContext = appContext()) == null) {
            return;
        }
        try {
            if (com.tencent.qqlitepro.cache.MsgCache.get().isRecallRecord(obj)) {
                return;
            }
        } catch (java.lang.Throwable unused) {
        }
        dispatchToPlugins(obj);
        java.lang.String strFieldString = fieldString(obj, "msgId");
        java.lang.String strFieldString2 = fieldString(obj, "senderUid");
        if ((strFieldString2 == null || strFieldString2.isEmpty()) && ((strFieldString2 = fieldString(obj, "senderUin")) == null || strFieldString2.isEmpty())) {
            strFieldString2 = fieldString(obj, "sendUin");
        }
        java.lang.String str2 = strFieldString2;
        boolean zKeywordReplyEnabled = com.tencent.qqlitepro.util.Config.keywordReplyEnabled(contextAppContext);
        boolean zAutoReplyEnabled = com.tencent.qqlitepro.util.Config.autoReplyEnabled(contextAppContext);
        if (zKeywordReplyEnabled || zAutoReplyEnabled) {
            java.lang.String strFieldString3 = fieldString(obj, "peerUid");
            int iFieldInt = fieldInt(obj, "chatType", 2);
            if (strFieldString3 == null || strFieldString3.isEmpty()) {
                return;
            }
            java.lang.String strExtractText = extractText(obj);
            boolean[] zArr = {false};
            if (!zKeywordReplyEnabled || !isWhitelisted(com.tencent.qqlitepro.util.Config.keywordReplyPeers(contextAppContext), iFieldInt, strFieldString3) || strExtractText == null || strExtractText.isEmpty()) {
                strKeywordReplyContent = null;
                str = null;
            } else {
                com.tencent.qqlitepro.hook.MsgListenerHook.KeywordMatch keywordMatchMatchKeywordRules = matchKeywordRules(strExtractText, com.tencent.qqlitepro.util.Config.keywordRules(contextAppContext));
                if (keywordMatchMatchKeywordRules != null) {
                    if (keywordMatchMatchKeywordRules.ai) {
                        if (keywordMatchMatchKeywordRules.at) {
                            if (!isAtMe(obj)) {
                                return;
                            }
                        } else if (atOthersOnly(obj)) {
                            return;
                        }
                        handleKeywordAi(contextAppContext, iFieldInt, strFieldString3, str2, aiPromptContent(strExtractText, keywordMatchMatchKeywordRules.keyword), keywordMatchMatchKeywordRules.keyword, keywordMatchMatchKeywordRules.replyAt, fieldString(obj, "senderNick"));
                        return;
                    }
                    strKeywordReplyContent = keywordMatchMatchKeywordRules.reply;
                    str = "关键词回复";
                    zArr[0] = keywordMatchMatchKeywordRules.replyAt;
                } else {
                    java.lang.String strKeywordReplyKeywords = com.tencent.qqlitepro.util.Config.keywordReplyKeywords(contextAppContext);
                    if (strKeywordReplyKeywords == null || strKeywordReplyKeywords.trim().isEmpty() || !matchKeyword(strExtractText, strKeywordReplyKeywords)) {
                        strKeywordReplyContent = null;
                        str = null;
                    } else {
                        strKeywordReplyContent = com.tencent.qqlitepro.util.Config.keywordReplyContent(contextAppContext);
                        str = "关键词回复";
                    }
                }
            }
            if (strKeywordReplyContent == null && zAutoReplyEnabled && iFieldInt == 1 && isWhitelisted(com.tencent.qqlitepro.util.Config.autoReplyPeers(contextAppContext), iFieldInt, strFieldString3)) {
                strAutoReplyContent = com.tencent.qqlitepro.util.Config.autoReplyContent(contextAppContext);
                str = "自动回复";
            } else {
                strAutoReplyContent = strKeywordReplyContent;
            }
            if (strAutoReplyContent == null || strAutoReplyContent.trim().isEmpty()) {
                return;
            }
            if ("自动回复".equals(str)) {
                zAutoReplyAt = com.tencent.qqlitepro.util.Config.autoReplyAt(contextAppContext);
            } else {
                zAutoReplyAt = "关键词回复".equals(str) && zArr[0];
            }
            java.lang.String str3 = iFieldInt + ":" + strFieldString3;
            long jCurrentTimeMillis = java.lang.System.currentTimeMillis();
            java.util.Map<java.lang.String, java.lang.Long> map = sLastReplyAt;
            synchronized (map) {
                java.lang.Long l = map.get(str3);
                if (l == null || jCurrentTimeMillis - l.longValue() >= REPLY_COOLDOWN_MS) {
                    map.put(str3, java.lang.Long.valueOf(jCurrentTimeMillis));
                    trimMap(map, SELF_DEDUP_MS);
                    if (strFieldString == null || strFieldString.isEmpty()) {
                        strFieldString = str3 + "|" + strExtractText;
                    }
                    java.util.Map<java.lang.String, java.lang.Long> map2 = sRepliedIds;
                    synchronized (map2) {
                        if (map2.containsKey(strFieldString)) {
                            return;
                        }
                        map2.put(strFieldString, java.lang.Long.valueOf(jCurrentTimeMillis));
                        trimMap(map2, REPLY_DEDUP_MS);
                        java.lang.StringBuilder sb = new java.lang.StringBuilder();
                        sb.append(str);
                        sb.append("触发: ");
                        sb.append(strFieldString3);
                        sb.append(" -> ");
                        sb.append(strAutoReplyContent);
                        sb.append(zAutoReplyAt ? " (回复并艾特)" : "");
                        com.tencent.qqlitepro.util.Log.i(sb.toString());
                        java.lang.Thread thread = new java.lang.Thread(new com.tencent.qqlitepro.hook.MsgListenerHook$$ExternalSyntheticLambda1(str2, strAutoReplyContent, zAutoReplyAt, fieldString(obj, "senderNick"), iFieldInt, strFieldString3), "auto-reply-send");
                        thread.setDaemon(true);
                        thread.start();
                    }
                }
            }
        }
    }

    static /* synthetic */ void lambda$maybeReply$0(java.lang.String str, java.lang.String str2, boolean z, java.lang.String str3, int i, java.lang.String str4) {
        if (str != null) {
            try {
                if (!str.isEmpty() && com.tencent.qqlitepro.http.QqKernel.isSelfTarget(str)) {
                    str = null;
                }
            } catch (java.lang.Throwable unused) {
                return;
            }
        }
        try {
            com.tencent.qqlitepro.auto.AutoTaskScheduler.sendElements(appContext(), i, str4,
                    com.tencent.qqlitepro.http.QqKernel.buildElementsWithAt(str2, z, str, str3));
        } catch (java.lang.Throwable th) {
            // 原实现此处不记录日志
        }
    }

    private static boolean isWhitelisted(java.lang.String str, int i, java.lang.String str2) {
        if (str != null && !str.trim().isEmpty()) {
            java.lang.String str3 = i + "|" + str2;
            for (java.lang.String str4 : str.split("\n")) {
                java.lang.String strTrim = str4.trim();
                if (!strTrim.isEmpty()) {
                    java.lang.String[] strArrSplit = strTrim.split("\\|", 3);
                    if (strArrSplit.length >= 2) {
                        if ((i + "|" + strArrSplit[1].trim()).equals(str3)) {
                            return true;
                        }
                    } else {
                        continue;
                    }
                }
            }
        }
        return false;
    }

    private static java.lang.String extractText(java.lang.Object obj) {
        java.util.List list;
        java.lang.Object fieldValue;
        java.lang.String strFieldString;
        try {
            java.lang.Object fieldValue2 = getFieldValue(obj, "elements");
            if (fieldValue2 instanceof java.util.List) {
                list = (java.util.List) fieldValue2;
            } else if (fieldValue2 == null || !fieldValue2.getClass().isArray()) {
                list = null;
            } else {
                java.util.ArrayList arrayList = new java.util.ArrayList();
                int length = java.lang.reflect.Array.getLength(fieldValue2);
                for (int i = 0; i < length; i++) {
                    arrayList.add(java.lang.reflect.Array.get(fieldValue2, i));
                }
                list = arrayList;
            }
            if (list == null) {
                return null;
            }
            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            for (java.lang.Object obj2 : list) {
                if (obj2 != null && (fieldValue = getFieldValue(obj2, "textElement")) != null && (strFieldString = fieldString(fieldValue, "content")) != null && !strFieldString.isEmpty()) {
                    sb.append(strFieldString);
                }
            }
            if (sb.length() == 0) {
                return null;
            }
            return sb.toString();
        } catch (java.lang.Throwable unused) {
            return null;
        }
    }

    private static void dispatchToPlugins(java.lang.Object obj) {
        try {
            if (com.tencent.qqlitepro.plugin.PluginEngine.hasMessageCallbacks()) {
                java.lang.String strFieldString = fieldString(obj, "msgId");
                if (strFieldString != null && !strFieldString.isEmpty()) {
                    java.util.Map<java.lang.String, java.lang.Long> map = sDispatchedIds;
                    synchronized (map) {
                        boolean zContainsKey = map.containsKey(strFieldString);
                        if (!zContainsKey) {
                            map.put(strFieldString, java.lang.Long.valueOf(java.lang.System.currentTimeMillis()));
                        }
                        trimMap(map, DISPATCH_DEDUP_MS);
                        if (zContainsKey) {
                            return;
                        }
                    }
                }
                java.lang.String strFieldString2 = fieldString(obj, "peerUid");
                if (strFieldString2 == null) {
                    strFieldString2 = "";
                }
                int iFieldInt = fieldInt(obj, "chatType", 2);
                java.lang.String strFieldString3 = fieldString(obj, "senderUid");
                java.lang.String strFieldString4 = fieldString(obj, "senderUin");
                if (strFieldString4 == null || strFieldString4.isEmpty()) {
                    strFieldString4 = fieldString(obj, "sendUin");
                }
                com.tencent.qqlitepro.plugin.PluginEngine.dispatchIncomingMessage("{\"msgId\":" + jsStr(strFieldString) + ",\"peerUid\":" + jsStr(strFieldString2) + ",\"chatType\":" + iFieldInt + ",\"senderUid\":" + jsStr(strFieldString3) + ",\"senderUin\":" + jsStr(strFieldString4) + ",\"senderNick\":" + jsStr(fieldString(obj, "senderNick")) + ",\"text\":" + jsStr(extractText(obj)) + "}");
            }
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("插件消息分发异常: " + th);
        }
    }

    private static java.lang.String jsStr(java.lang.String str) {
        if (str == null) {
            return "null";
        }
        java.lang.StringBuilder sb = new java.lang.StringBuilder(str.length() + 16);
        sb.append('\"');
        for (int i = 0; i < str.length(); i++) {
            char cCharAt = str.charAt(i);
            if (cCharAt == '\t') {
                sb.append("\\t");
            } else if (cCharAt == '\n') {
                sb.append("\\n");
            } else if (cCharAt == '\r') {
                sb.append("\\r");
            } else if (cCharAt == '\"') {
                sb.append("\\\"");
            } else if (cCharAt == '\\') {
                sb.append("\\\\");
            } else if (cCharAt < ' ') {
                sb.append("\\u");
                sb.append(java.lang.String.format("%04x", java.lang.Integer.valueOf(cCharAt)));
            } else {
                sb.append(cCharAt);
            }
        }
        sb.append('\"');
        return sb.toString();
    }

    private static boolean matchKeyword(java.lang.String str, java.lang.String str2) {
        if (str != null && str2 != null) {
            try {
                java.lang.String lowerCase = str.toLowerCase();
                for (java.lang.String str3 : str2.split(",")) {
                    java.lang.String strTrim = str3.trim();
                    if (!strTrim.isEmpty() && (java.util.regex.Pattern.compile(java.util.regex.Pattern.quote(strTrim), 2).matcher(str).find() || lowerCase.contains(strTrim.toLowerCase()))) {
                        return true;
                    }
                }
            } catch (java.lang.Throwable unused) {
            }
        }
        return false;
    }

    private static final class KeywordMatch {
        final boolean ai;
        final boolean at;
        final java.lang.String keyword;
        final java.lang.String reply;
        final boolean replyAt;

        KeywordMatch(java.lang.String str, java.lang.String str2, boolean z, boolean z2, boolean z3) {
            this.keyword = str;
            this.reply = str2;
            this.ai = z;
            this.at = z2;
            this.replyAt = z3;
        }
    }

    private static com.tencent.qqlitepro.hook.MsgListenerHook.KeywordMatch matchKeywordRules(java.lang.String str, java.lang.String str2) {
        java.lang.String strTrim;
        boolean zEquals = false;
        boolean zEquals2 = false;
        boolean zEquals3 = false;
        if (str != null && str2 != null) {
            java.lang.String lowerCase = str.toLowerCase();
            for (java.lang.String str3 : str2.split("\n")) {
                java.lang.String strTrim2 = str3.trim();
                if (!strTrim2.isEmpty()) {
                    java.lang.String[] strArrSplit = strTrim2.split("\\|");
                    if (strArrSplit.length < 2) {
                        continue;
                    } else {
                        java.lang.String strUnescapeField = com.tencent.qqlitepro.util.Config.unescapeField(strArrSplit[0].trim());
                        if (!strUnescapeField.isEmpty() && lowerCase.contains(strUnescapeField.toLowerCase())) {
                            if (strArrSplit.length == 2) {
                                strTrim = strArrSplit[1].trim();
                                zEquals3 = false;
                            } else {
                                if (strArrSplit.length == 3) {
                                    strTrim = strArrSplit[1].trim();
                                    zEquals3 = "1".equals(strArrSplit[2].trim());
                                } else if (strArrSplit.length == 4) {
                                    strTrim = strArrSplit[1].trim();
                                    zEquals3 = "1".equals(strArrSplit[2].trim());
                                    zEquals2 = "1".equals(strArrSplit[3].trim());
                                    zEquals = false;
                                } else {
                                    java.lang.StringBuilder sb = new java.lang.StringBuilder(strArrSplit[1]);
                                    for (int i = 2; i < strArrSplit.length - 3; i++) {
                                        sb.append('|');
                                        sb.append(strArrSplit[i]);
                                    }
                                    strTrim = sb.toString().trim();
                                    boolean zEquals4 = "1".equals(strArrSplit[strArrSplit.length - 3].trim());
                                    boolean zEquals5 = "1".equals(strArrSplit[strArrSplit.length - 2].trim());
                                    zEquals = "1".equals(strArrSplit[strArrSplit.length - 1].trim());
                                    zEquals2 = zEquals5;
                                    zEquals3 = zEquals4;
                                }
                            }
                            return new com.tencent.qqlitepro.hook.MsgListenerHook.KeywordMatch(strUnescapeField, com.tencent.qqlitepro.util.Config.unescapeField(strTrim), zEquals3, zEquals2, zEquals);
                        }
                    }
                }
            }
        }
        return null;
    }

    private static java.util.Set<java.lang.String> collectAtTargets(java.lang.Object obj) {
        java.util.HashSet hashSet = new java.util.HashSet();
        try {
            java.lang.Object fieldValue = getFieldValue(obj, "elements");
            if (!(fieldValue instanceof java.util.List)) {
                return hashSet;
            }
            for (java.lang.Object obj2 : (java.util.List) fieldValue) {
                if (obj2 != null) {
                    java.lang.Object fieldValue2 = getFieldValue(obj2, "atElement");
                    if (fieldValue2 != null) {
                        java.lang.String strFieldString = fieldString(fieldValue2, "ownUid");
                        if (strFieldString != null && !strFieldString.isEmpty()) {
                            hashSet.add(SELF_AT_SENTINEL);
                        }
                        java.lang.Object fieldValue3 = getFieldValue(fieldValue2, "uidList");
                        if (fieldValue3 instanceof java.util.List) {
                            for (java.lang.Object obj3 : (java.util.List) fieldValue3) {
                                if (obj3 != null) {
                                    java.lang.String strValueOf = java.lang.String.valueOf(obj3);
                                    if (!strValueOf.isEmpty()) {
                                        hashSet.add(strValueOf);
                                    }
                                }
                            }
                        }
                    }
                    java.lang.Object fieldValue4 = getFieldValue(obj2, "textElement");
                    if (fieldValue4 != null) {
                        java.lang.String strFieldString2 = fieldString(fieldValue4, "atNtUid");
                        if (strFieldString2 != null && !strFieldString2.isEmpty()) {
                            hashSet.add(strFieldString2);
                        }
                        java.lang.Object fieldValue5 = getFieldValue(fieldValue4, "atUid");
                        if (fieldValue5 != null) {
                            java.lang.String strValueOf2 = java.lang.String.valueOf(fieldValue5);
                            if (!strValueOf2.isEmpty()) {
                                hashSet.add(strValueOf2);
                            }
                        }
                        java.lang.Object fieldValue6 = getFieldValue(fieldValue4, "atTinyId");
                        if (fieldValue6 != null) {
                            java.lang.String strValueOf3 = java.lang.String.valueOf(fieldValue6);
                            if (!strValueOf3.isEmpty()) {
                                hashSet.add(strValueOf3);
                            }
                        }
                        java.lang.String strFieldString3 = fieldString(fieldValue4, "content");
                        if (strFieldString3 != null && !strFieldString3.isEmpty()) {
                            collectAtFromMarker(strFieldString3, hashSet);
                        }
                        java.lang.Object fieldValue7 = getFieldValue(fieldValue4, "atUidList");
                        if (fieldValue7 instanceof java.util.List) {
                            for (java.lang.Object obj4 : (java.util.List) fieldValue7) {
                                if (obj4 != null) {
                                    java.lang.String strValueOf4 = java.lang.String.valueOf(obj4);
                                    if (!strValueOf4.isEmpty()) {
                                        hashSet.add(strValueOf4);
                                    }
                                }
                            }
                        }
                        java.lang.Object fieldValue8 = getFieldValue(fieldValue4, "atUids");
                        if (fieldValue8 instanceof java.util.List) {
                            for (java.lang.Object obj5 : (java.util.List) fieldValue8) {
                                if (obj5 != null) {
                                    java.lang.String strValueOf5 = java.lang.String.valueOf(obj5);
                                    if (!strValueOf5.isEmpty()) {
                                        hashSet.add(strValueOf5);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (java.lang.Throwable unused) {
        }
        return hashSet;
    }

    private static void collectAtFromMarker(java.lang.String str, java.util.Set<java.lang.String> set) {
        int iIndexOf;
        int i = 0;
        while (i < str.length() && (iIndexOf = str.indexOf("ꡦATꡦ", i)) >= 0) {
            try {
                i = iIndexOf + 4;
                int iIndexOf2 = str.indexOf(43116, i);
                if (iIndexOf2 > i) {
                    java.lang.String strTrim = str.substring(i, iIndexOf2).trim();
                    if (!strTrim.isEmpty()) {
                        set.add(strTrim);
                    }
                    i = iIndexOf2 + 1;
                }
            } catch (java.lang.Throwable unused) {
                return;
            }
        }
    }

    private static boolean isAtMe(java.lang.Object obj) {
        java.util.Set<java.lang.String> setCollectAtTargets = collectAtTargets(obj);
        if (setCollectAtTargets.contains(SELF_AT_SENTINEL)) {
            return true;
        }
        java.util.Iterator<java.lang.String> it = setCollectAtTargets.iterator();
        while (it.hasNext()) {
            if (com.tencent.qqlitepro.http.QqKernel.isSelfTarget(it.next())) {
                return true;
            }
        }
        java.lang.String str = sSelfUid;
        if (str != null && !str.isEmpty()) {
            return setCollectAtTargets.contains(str);
        }
        return !setCollectAtTargets.isEmpty();
    }

    private static boolean atOthersOnly(java.lang.Object obj) {
        java.util.Set<java.lang.String> setCollectAtTargets = collectAtTargets(obj);
        if (setCollectAtTargets.isEmpty() || setCollectAtTargets.contains(SELF_AT_SENTINEL)) {
            return false;
        }
        java.util.Iterator<java.lang.String> it = setCollectAtTargets.iterator();
        while (it.hasNext()) {
            if (com.tencent.qqlitepro.http.QqKernel.isSelfTarget(it.next())) {
                return false;
            }
        }
        java.lang.String str = sSelfUid;
        if (str != null && !str.isEmpty()) {
            java.util.Iterator<java.lang.String> it2 = setCollectAtTargets.iterator();
            while (it2.hasNext()) {
                if (!str.equals(it2.next())) {
                    return true;
                }
            }
        }
        return false;
    }

    private static java.lang.String aiPromptContent(java.lang.String str, java.lang.String str2) {
        java.lang.String strTrim = str2 == null ? "" : str2.trim();
        if (str == null || str.isEmpty()) {
            return strTrim;
        }
        java.lang.String lowerCase = str.toLowerCase();
        java.lang.String lowerCase2 = strTrim.toLowerCase();
        if (lowerCase.equals(lowerCase2)) {
            return strTrim;
        }
        if (strTrim.isEmpty() || !lowerCase.startsWith(lowerCase2)) {
            return str;
        }
        java.lang.String strTrim2 = str.substring(strTrim.length()).trim();
        return strTrim2.isEmpty() ? strTrim : strTrim2;
    }

    private static void handleKeywordAi(android.content.Context context, int i, java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.String str4, boolean z, java.lang.String str5) {
        if (context != null) {
            com.tencent.qqlitepro.util.Config.reloadForce(context);
        }
        java.lang.String strKeywordAiUrl = com.tencent.qqlitepro.util.Config.keywordAiUrl(context);
        if (strKeywordAiUrl == null || strKeywordAiUrl.trim().isEmpty()) {
            return;
        }
        java.lang.String strKeywordAiApiKey = com.tencent.qqlitepro.util.Config.keywordAiApiKey(context);
        java.lang.String strKeywordAiModel = com.tencent.qqlitepro.util.Config.keywordAiModel(context);
        java.lang.String strKeywordAiPersona = com.tencent.qqlitepro.util.Config.keywordAiPersona(context);
        java.lang.String str6 = (str2 == null || str2.isEmpty()) ? str : str2;
        java.util.Map<java.lang.String, java.lang.Long> map = sAiCooldown;
        synchronized (map) {
            java.lang.Long l = map.get(str6);
            long jCurrentTimeMillis = java.lang.System.currentTimeMillis();
            if (l != null && jCurrentTimeMillis - l.longValue() < AI_REPLY_COOLDOWN_MS) {
                com.tencent.qqlitepro.util.Log.i("关键词[AI]同用户冷却中: " + str6);
                return;
            }
            map.put(str6, java.lang.Long.valueOf(jCurrentTimeMillis));
            trimMap(map, 60000L);
            com.tencent.qqlitepro.util.Log.i("关键词[AI]触发: " + str + " kw=" + str4);
            java.lang.Thread thread = new java.lang.Thread(new com.tencent.qqlitepro.hook.MsgListenerHook$$ExternalSyntheticLambda0(strKeywordAiUrl, strKeywordAiApiKey, strKeywordAiModel, strKeywordAiPersona, str3, str4, str2, z, str5, i, str), "keyword-ai-reply");
            thread.setDaemon(true);
            thread.start();
        }
    }

    static /* synthetic */ void lambda$handleKeywordAi$1(java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.String str4, java.lang.String str5, java.lang.String str6, java.lang.String str7, boolean z, java.lang.String str8, int i, java.lang.String str9) {
        try {
            java.lang.String strKeywordReply = com.tencent.qqlitepro.hook.AiSummaryHook.keywordReply(str, str2, str3, str4, str5);
            if (strKeywordReply != null && !strKeywordReply.trim().isEmpty()) {
                if (str7 != null && !str7.isEmpty() && com.tencent.qqlitepro.http.QqKernel.isSelfTarget(str7)) {
                    str7 = null;
                }
                com.tencent.qqlitepro.auto.AutoTaskScheduler.sendElements(appContext(), i, str9, com.tencent.qqlitepro.http.QqKernel.buildElementsWithAt(strKeywordReply.trim(), z, str7, str8));
                return;
            }
            com.tencent.qqlitepro.util.Log.w("关键词[AI]未生成回复 kw=" + str6);
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("关键词[AI]发送异常: " + th);
        }
    }

    private static void trimMap(java.util.Map<java.lang.String, java.lang.Long> map, long j) {
        long jCurrentTimeMillis = java.lang.System.currentTimeMillis();
        java.util.Iterator<java.util.Map.Entry<java.lang.String, java.lang.Long>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            if (jCurrentTimeMillis - it.next().getValue().longValue() > j) {
                it.remove();
            }
        }
    }

    private static int fieldInt(java.lang.Object obj, java.lang.String str, int i) {
        java.lang.Object fieldValue = getFieldValue(obj, str);
        return fieldValue instanceof java.lang.Number ? ((java.lang.Number) fieldValue).intValue() : i;
    }

    private static boolean doCache(com.tencent.qqlitepro.cache.MsgCache msgCache, java.lang.Object obj, java.lang.String str) {
        java.lang.String strFieldString;
        java.util.List<?> listResolveElements;
        if (!antiRecallEnabled() || obj == null || (strFieldString = fieldString(obj, "msgId")) == null || (listResolveElements = resolveElements(obj)) == null || listResolveElements.isEmpty()) {
            return false;
        }
        if (str == null) {
            str = fieldString(obj, "peerUid");
        }
        return msgCache.cache(appContext(), strFieldString, str, fieldString(obj, "senderUid"), obj, listResolveElements);
    }

    private static java.lang.Object arg(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam, int i) {
        if (methodHookParam == null || methodHookParam.args == null || i >= methodHookParam.args.length || i < 0) {
            return null;
        }
        return methodHookParam.args[i];
    }

    private static java.lang.Iterable<?> asIterable(java.lang.Object obj) {
        if (obj == null) {
            return new java.util.ArrayList(0);
        }
        if (obj instanceof java.lang.Iterable) {
            return (java.lang.Iterable) obj;
        }
        if (obj.getClass().isArray()) {
            java.util.ArrayList arrayList = new java.util.ArrayList();
            int length = java.lang.reflect.Array.getLength(obj);
            for (int i = 0; i < length; i++) {
                arrayList.add(java.lang.reflect.Array.get(obj, i));
            }
            return arrayList;
        }
        java.util.ArrayList arrayList2 = new java.util.ArrayList(1);
        arrayList2.add(obj);
        return arrayList2;
    }

    private static java.lang.String idString(java.lang.Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof java.lang.CharSequence) {
            return obj.toString();
        }
        if ((obj instanceof java.lang.Number) || (obj instanceof java.lang.Boolean)) {
            return obj.toString();
        }
        return fieldString(obj, "msgId");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.lang.String fieldString(java.lang.Object obj, java.lang.String str) {
        java.lang.Object fieldValue;
        if (obj == null || (fieldValue = getFieldValue(obj, str)) == null) {
            return null;
        }
        return java.lang.String.valueOf(fieldValue);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.lang.Object getFieldValue(java.lang.Object obj, java.lang.String str) {
        for (java.lang.Class<?> superclass = obj.getClass(); superclass != null && superclass != java.lang.Object.class; superclass = superclass.getSuperclass()) {
            try {
                java.lang.reflect.Field declaredField = superclass.getDeclaredField(str);
                declaredField.setAccessible(true);
                return declaredField.get(obj);
            } catch (java.lang.Throwable unused) {
            }
        }
        try {
            java.lang.reflect.Method method = obj.getClass().getMethod("get" + java.lang.Character.toUpperCase(str.charAt(0)) + str.substring(1), new java.lang.Class[0]);
            method.setAccessible(true);
            return method.invoke(obj, new java.lang.Object[0]);
        } catch (java.lang.Throwable unused2) {
            return null;
        }
    }

    private static java.util.List<?> resolveElements(java.lang.Object obj) {
        java.lang.Object fieldValue = getFieldValue(obj, "elements");
        if (fieldValue instanceof java.util.List) {
            return (java.util.List) fieldValue;
        }
        if (fieldValue == null || !fieldValue.getClass().isArray()) {
            return null;
        }
        java.util.ArrayList arrayList = new java.util.ArrayList();
        int length = java.lang.reflect.Array.getLength(fieldValue);
        for (int i = 0; i < length; i++) {
            arrayList.add(java.lang.reflect.Array.get(fieldValue, i));
        }
        return arrayList;
    }

    private static void collectFakeParts(java.lang.Object obj) {
        com.tencent.qqlitepro.http.FakeImage.OriginalInfo originalInfo;
        java.lang.String strPngByMd5;
        com.tencent.qqlitepro.http.FakeVoice.OriginalInfo originalInfo2;
        try {
            java.util.List<?> listResolveElements = resolveElements(obj);
            if (listResolveElements != null && !listResolveElements.isEmpty()) {
                for (java.lang.Object obj2 : listResolveElements) {
                    if (obj2 != null) {
                        java.lang.Object fieldValue = getFieldValue(obj2, "pttElement");
                        if (fieldValue != null) {
                            java.lang.Object fieldValue2 = getFieldValue(fieldValue, "filePath");
                            if ((fieldValue2 instanceof java.lang.String) && !((java.lang.String) fieldValue2).isEmpty()) {
                                java.lang.String str = (java.lang.String) fieldValue2;
                                if (com.tencent.qqlitepro.http.FakeVoice.isFakeVoice(str) && (originalInfo2 = com.tencent.qqlitepro.http.FakeVoice.parseOriginalInfo(str)) != null && originalInfo2.shard) {
                                    int iCollectedParts = com.tencent.qqlitepro.http.FakeVoice.collectedParts(originalInfo2);
                                    com.tencent.qqlitepro.http.FakeVoice.collectPart(str);
                                    int iCollectedParts2 = com.tencent.qqlitepro.http.FakeVoice.collectedParts(originalInfo2);
                                    if (iCollectedParts2 != iCollectedParts) {
                                        com.tencent.qqlitepro.util.Log.i("伪语音: 自动归集分片 " + iCollectedParts2 + "/" + originalInfo2.count + " (" + originalInfo2.name + ")");
                                    }
                                }
                            }
                        }
                        java.lang.Object fieldValue3 = getFieldValue(obj2, "picElement");
                        if (fieldValue3 != null) {
                            java.lang.String[] strArr = {"sourcePath", "filePath", "thumbPath"};
                            java.util.ArrayList<java.lang.String> arrayList = new java.util.ArrayList();
                            for (int i = 0; i < 3; i++) {
                                java.lang.Object fieldValue4 = getFieldValue(fieldValue3, strArr[i]);
                                if ((fieldValue4 instanceof java.lang.String) && !((java.lang.String) fieldValue4).isEmpty()) {
                                    arrayList.add((java.lang.String) fieldValue4);
                                }
                            }
                            java.lang.Object fieldValue5 = getFieldValue(fieldValue3, "md5HexStr");
                            if ((fieldValue5 instanceof java.lang.String) && !((java.lang.String) fieldValue5).isEmpty() && (strPngByMd5 = com.tencent.qqlitepro.http.FakeImage.pngByMd5((java.lang.String) fieldValue5)) != null) {
                                arrayList.add(strPngByMd5);
                            }
                            for (java.lang.String str2 : arrayList) {
                                if (str2 != null && !str2.isEmpty()) {
                                    try {
                                        if (com.tencent.qqlitepro.http.FakeImage.isFakeImage(str2) && (originalInfo = com.tencent.qqlitepro.http.FakeImage.parseOriginalInfo(str2)) != null && originalInfo.shard) {
                                            int iCollectedParts3 = com.tencent.qqlitepro.http.FakeImage.collectedParts(originalInfo);
                                            com.tencent.qqlitepro.http.FakeImage.collectPart(str2);
                                            int iCollectedParts4 = com.tencent.qqlitepro.http.FakeImage.collectedParts(originalInfo);
                                            if (iCollectedParts4 != iCollectedParts3) {
                                                com.tencent.qqlitepro.util.Log.i("伪图片: 自动归集分片 " + iCollectedParts4 + "/" + originalInfo.count + " (" + originalInfo.name + ")");
                                            }
                                        }
                                    } catch (java.lang.Throwable unused) {
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("伪语音自动归集异常: " + th);
        }
    }

    private static android.content.Context appContext() {
        try {
            java.lang.reflect.Method method = java.lang.Class.forName("android.app.ActivityThread").getMethod("currentApplication", new java.lang.Class[0]);
            method.setAccessible(true);
            return (android.content.Context) method.invoke(null, new java.lang.Object[0]);
        } catch (java.lang.Throwable unused) {
            return null;
        }
    }

    private static boolean antiRecallEnabled() {
        try {
            return com.tencent.qqlitepro.util.Config.enabled(appContext());
        } catch (java.lang.Throwable unused) {
            return true;
        }
    }
}
