package com.tencent.qqlitepro.hook;

/* JADX INFO: loaded from: classes.dex */
public class ForceNoticeInjector {
    private static final java.lang.String CONFIG_URL = "https://devfile.vip/preview/5bbpqi7q/config.json";
    private static final int COUNTDOWN = 5;
    private static final int MATCH = -1;
    private static final java.lang.String TAG = "ForceNotice";
    private static final int WRAP = -2;
    private static volatile android.app.Dialog sDialog = null;
    private static volatile boolean sFetched = false;
    private static volatile boolean sFetchedOk = false;
    private static volatile boolean sFetching = false;
    private static final java.util.List<com.google.gson.JsonObject> sAllForce = new java.util.ArrayList();
    private static final java.util.Set<java.lang.String> sConfirmedShown = java.util.concurrent.ConcurrentHashMap.newKeySet();
    private static volatile boolean sPatched = false;
    private static volatile java.lang.ref.WeakReference<android.app.Activity> sLastAct = new java.lang.ref.WeakReference<>(null);
    private static volatile java.util.List<com.google.gson.JsonObject> sNodeList = null;
    private static volatile java.lang.String sNodeUrl = null;

    static /* synthetic */ boolean lambda$show$4(android.content.DialogInterface dialogInterface, int i, android.view.KeyEvent keyEvent) {
        return i == 4;
    }

    private ForceNoticeInjector() {
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.ForceNoticeInjector$1, reason: invalid class name */
    static class AnonymousClass1 extends de.robv.android.xposed.XC_MethodHook {
        AnonymousClass1() {
        }

        protected void afterHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            try {
                if (methodHookParam.thisObject instanceof android.app.Activity) {
                    com.tencent.qqlitepro.hook.ForceNoticeInjector.maybeShow((android.app.Activity) methodHookParam.thisObject);
                }
            } catch (java.lang.Throwable unused) {
            }
        }
    }

    public static void patch(java.lang.ClassLoader classLoader) {
        if (sPatched) {
            return;
        }
        sPatched = true;
        try {
            de.robv.android.xposed.XposedHelpers.findAndHookMethod(android.app.Activity.class, "onResume", new java.lang.Object[]{new com.tencent.qqlitepro.hook.ForceNoticeInjector.AnonymousClass1()});
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.e("ForceNotice onResume hook 失败: " + th);
        }
        try {
            de.robv.android.xposed.XposedHelpers.findAndHookMethod(android.app.Activity.class, "onStart", new java.lang.Object[]{new com.tencent.qqlitepro.hook.ForceNoticeInjector.AnonymousClass2()});
        } catch (java.lang.Throwable th2) {
            com.tencent.qqlitepro.util.Log.e("ForceNotice onStart hook 失败: " + th2);
        }
        try {
            de.robv.android.xposed.XposedHelpers.findAndHookMethod(android.app.Activity.class, "onBackPressed", new java.lang.Object[]{new com.tencent.qqlitepro.hook.ForceNoticeInjector.AnonymousClass3()});
        } catch (java.lang.Throwable th3) {
            com.tencent.qqlitepro.util.Log.e("ForceNotice onBackPressed hook 失败: " + th3);
        }
        com.tencent.qqlitepro.util.Log.i("ForceNotice hook 已注册");
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.ForceNoticeInjector$2, reason: invalid class name */
    static class AnonymousClass2 extends de.robv.android.xposed.XC_MethodHook {
        AnonymousClass2() {
        }

        protected void afterHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            try {
                if (methodHookParam.thisObject instanceof android.app.Activity) {
                    com.tencent.qqlitepro.hook.ForceNoticeInjector.maybeShow((android.app.Activity) methodHookParam.thisObject);
                }
            } catch (java.lang.Throwable unused) {
            }
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.ForceNoticeInjector$3, reason: invalid class name */
    static class AnonymousClass3 extends de.robv.android.xposed.XC_MethodHook {
        AnonymousClass3() {
        }

        protected void beforeHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            if (com.tencent.qqlitepro.hook.ForceNoticeInjector.isShowing()) {
                methodHookParam.setResult((java.lang.Object) null);
            }
        }
    }

    public static void triggerForCurrentActivity() {
        new android.os.Handler(android.os.Looper.getMainLooper()).post(new com.tencent.qqlitepro.hook.ForceNoticeInjector$$ExternalSyntheticLambda8());
    }

    static /* synthetic */ void lambda$triggerForCurrentActivity$0() {
        try {
            android.app.Activity activityForegroundActivity = foregroundActivity();
            if (activityForegroundActivity != null) {
                maybeShow(activityForegroundActivity);
            }
        } catch (java.lang.Throwable unused) {
        }
    }

    private static android.app.Activity foregroundActivity() {
        android.app.Activity activity = null;
        try {
            java.lang.Object objInvoke = java.lang.Class.forName("android.app.ActivityThread").getMethod("currentActivityThread", new java.lang.Class[0]).invoke(null, new java.lang.Object[0]);
            java.lang.Object objectField = objInvoke == null ? null : de.robv.android.xposed.XposedHelpers.getObjectField(objInvoke, "mActivities");
            if (!(objectField instanceof java.util.Map)) {
                return null;
            }
            for (java.lang.Object obj : ((java.util.Map) objectField).values()) {
                try {
                    java.lang.Object objectField2 = de.robv.android.xposed.XposedHelpers.getObjectField(obj, "activity");
                    if (objectField2 instanceof android.app.Activity) {
                        android.app.Activity activity2 = (android.app.Activity) objectField2;
                        if (!activity2.isFinishing() && !activity2.isDestroyed()) {
                            if (activity == null) {
                                activity = activity2;
                            }
                            if (java.lang.Boolean.TRUE.equals(de.robv.android.xposed.XposedHelpers.getObjectField(obj, "mResumed"))) {
                                return activity2;
                            }
                            java.lang.Object objectField3 = de.robv.android.xposed.XposedHelpers.getObjectField(obj, "mLifecycleState");
                            if ((objectField3 instanceof java.lang.Integer) && ((java.lang.Integer) objectField3).intValue() == 3) {
                                return activity2;
                            }
                        }
                    }
                } catch (java.lang.Throwable unused) {
                }
            }
        } catch (java.lang.Throwable unused2) {
        }
        return activity;
    }

    static boolean isShowing() {
        android.app.Dialog dialog = sDialog;
        return dialog != null && dialog.isShowing();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void maybeShow(android.app.Activity activity) {
        if (activity != null) {
            sLastAct = new java.lang.ref.WeakReference<>(activity);
        }
        if (isShowing()) {
            return;
        }
        if (sFetched && sFetchedOk) {
            showNext(activity);
        } else {
            if (sFetching) {
                return;
            }
            sFetching = true;
            new java.lang.Thread(new com.tencent.qqlitepro.hook.ForceNoticeInjector$$ExternalSyntheticLambda13(), "force-notice-fetch").start();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void fetch() {
        android.os.Handler handler;
        com.tencent.qqlitepro.hook.ForceNoticeInjector$$ExternalSyntheticLambda9 forceNoticeInjector$$ExternalSyntheticLambda9;
        try {
            java.lang.String strResolveNoticeUrl = resolveNoticeUrl();
            if (strResolveNoticeUrl != null && !strResolveNoticeUrl.isEmpty()) {
                java.util.List<com.google.gson.JsonObject> forceNotices = parseForceNotices(fetchJson(strResolveNoticeUrl));
                java.util.List<com.google.gson.JsonObject> list = sAllForce;
                synchronized (list) {
                    list.clear();
                    list.addAll(forceNotices);
                }
                sFetchedOk = true;
                com.tencent.qqlitepro.util.Log.i("ForceNotice 拉取到强制公告 " + forceNotices.size() + " 条（节点 " + strResolveNoticeUrl + "）");
                sFetched = true;
                sFetching = false;
                android.app.Activity activity = sLastAct.get();
                if (activity != null) {
                    handler = new android.os.Handler(android.os.Looper.getMainLooper());
                    forceNoticeInjector$$ExternalSyntheticLambda9 = new com.tencent.qqlitepro.hook.ForceNoticeInjector$$ExternalSyntheticLambda9(activity);
                    handler.post(forceNoticeInjector$$ExternalSyntheticLambda9);
                    return;
                }
                return;
            }
            sFetchedOk = false;
            com.tencent.qqlitepro.util.Log.w("ForceNotice 无可用公告节点，跳过拉取");
            sFetched = true;
            sFetching = false;
            android.app.Activity activity2 = sLastAct.get();
            if (activity2 != null) {
                new android.os.Handler(android.os.Looper.getMainLooper()).post(new com.tencent.qqlitepro.hook.ForceNoticeInjector$$ExternalSyntheticLambda9(activity2));
            }
        } catch (java.lang.Throwable th) {
            try {
                sFetchedOk = false;
                com.tencent.qqlitepro.util.Log.w("ForceNotice 强制公告拉取失败: " + th);
                sFetched = true;
                sFetching = false;
                android.app.Activity activity3 = sLastAct.get();
                if (activity3 == null) {
                    return;
                }
                handler = new android.os.Handler(android.os.Looper.getMainLooper());
                forceNoticeInjector$$ExternalSyntheticLambda9 = new com.tencent.qqlitepro.hook.ForceNoticeInjector$$ExternalSyntheticLambda9(activity3);
            } catch (java.lang.Throwable th2) {
                sFetched = true;
                sFetching = false;
                android.app.Activity activity4 = sLastAct.get();
                if (activity4 != null) {
                    new android.os.Handler(android.os.Looper.getMainLooper()).post(new com.tencent.qqlitepro.hook.ForceNoticeInjector$$ExternalSyntheticLambda9(activity4));
                }
                throw th2;
            }
        }
    }

    static /* synthetic */ void lambda$fetch$1(android.app.Activity activity) {
        try {
            if (isShowing() || !sFetchedOk) {
                return;
            }
            showNext(activity);
        } catch (java.lang.Throwable unused) {
        }
    }

    private static java.lang.String resolveNoticeUrl() {
        java.lang.String asString;
        java.lang.String str = sNodeUrl;
        if (str != null && !str.isEmpty()) {
            return str;
        }
        try {
            java.util.List<com.google.gson.JsonObject> listFetchNodeConfig = fetchNodeConfig();
            for (com.google.gson.JsonObject jsonObject : listFetchNodeConfig) {
                if (isDefaultNode(jsonObject)) {
                    try {
                        asString = jsonObject.get("url").getAsString();
                    } catch (java.lang.Throwable unused) {
                        asString = null;
                    }
                    if (asString != null && !asString.isEmpty()) {
                        sNodeUrl = asString;
                        return asString;
                    }
                }
            }
            if (!listFetchNodeConfig.isEmpty()) {
                java.lang.String strPrims = prims(listFetchNodeConfig.get(0), "url");
                if (!strPrims.isEmpty()) {
                    sNodeUrl = strPrims;
                    return strPrims;
                }
            }
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("ForceNotice 解析公告节点失败: " + th);
        }
        return null;
    }

    private static java.util.List<com.google.gson.JsonObject> fetchNodeConfig() throws java.lang.Exception {
        java.util.List<com.google.gson.JsonObject> list = sNodeList;
        if (list != null) {
            return list;
        }
        com.google.gson.JsonObject asJsonObject = com.google.gson.JsonParser.parseString(fetchJson(CONFIG_URL)).getAsJsonObject();
        java.util.ArrayList arrayList = new java.util.ArrayList();
        com.google.gson.JsonElement jsonElement = asJsonObject.get("configs");
        if (jsonElement != null && jsonElement.isJsonArray()) {
            for (com.google.gson.JsonElement jsonElement2 : jsonElement.getAsJsonArray()) {
                if (jsonElement2 != null && jsonElement2.isJsonObject()) {
                    arrayList.add(jsonElement2.getAsJsonObject());
                }
            }
        }
        arrayList.sort(new com.tencent.qqlitepro.hook.ForceNoticeInjector$$ExternalSyntheticLambda10());
        sNodeList = arrayList;
        return arrayList;
    }

    static int nodeId(com.google.gson.JsonObject jsonObject) {
        try {
            if (jsonObject.has("id") && jsonObject.get("id").isJsonPrimitive()) {
                return jsonObject.get("id").getAsInt();
            }
            return 0;
        } catch (java.lang.Throwable unused) {
            return 0;
        }
    }

    private static boolean isDefaultNode(com.google.gson.JsonObject jsonObject) {
        try {
            return jsonObject.has("is_default") && jsonObject.get("is_default").getAsBoolean();
        } catch (java.lang.Throwable unused) {
            return false;
        }
    }

    private static java.lang.String effectiveNodeUrl(java.util.List<com.google.gson.JsonObject> list) {
        java.lang.String str = sNodeUrl;
        if (str != null && !str.isEmpty()) {
            return str;
        }
        if (list == null) {
            return null;
        }
        for (com.google.gson.JsonObject jsonObject : list) {
            if (isDefaultNode(jsonObject)) {
                java.lang.String strPrims = prims(jsonObject, "url");
                if (!strPrims.isEmpty()) {
                    return strPrims;
                }
            }
        }
        if (list.isEmpty()) {
            return null;
        }
        java.lang.String strPrims2 = prims(list.get(0), "url");
        if (strPrims2.isEmpty()) {
            return null;
        }
        return strPrims2;
    }

    private static void selectNode(java.lang.String str) {
        if (str == null || str.isEmpty()) {
            str = null;
        }
        sNodeUrl = str;
    }

    private static java.lang.String fetchJson(java.lang.String str) throws java.lang.Exception {
        java.io.BufferedReader bufferedReader;
        java.net.HttpURLConnection httpURLConnection = (java.net.HttpURLConnection) new java.net.URL(str).openConnection();
        try {
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setReadTimeout(15000);
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode >= 200 && responseCode < 300) {
                bufferedReader = new java.io.BufferedReader(new java.io.InputStreamReader(httpURLConnection.getInputStream(), java.nio.charset.StandardCharsets.UTF_8));
            } else {
                bufferedReader = new java.io.BufferedReader(new java.io.InputStreamReader(httpURLConnection.getErrorStream(), java.nio.charset.StandardCharsets.UTF_8));
            }
            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            while (true) {
                java.lang.String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                sb.append(line);
                sb.append('\n');
            }
            bufferedReader.close();
            if (responseCode < 200 || responseCode >= 300) {
                throw new java.lang.RuntimeException("HTTP " + responseCode);
            }
            java.lang.String string = sb.toString();
            httpURLConnection.disconnect();
            return string;
        } catch (java.lang.Throwable th) {
            httpURLConnection.disconnect();
            throw th;
        }
    }

    private static java.util.List<com.google.gson.JsonObject> parseForceNotices(java.lang.String str) throws java.lang.Exception {
        java.lang.String asString;
        int asInt;
        com.google.gson.JsonObject asJsonObject = com.google.gson.JsonParser.parseString(str).getAsJsonObject();
        if (asJsonObject.has("code") && (asInt = asJsonObject.get("code").getAsInt()) != 200) {
            throw new java.lang.RuntimeException("code=" + asInt);
        }
        if (asJsonObject.has("msg") && ((asString = asJsonObject.get("msg").getAsString()) == null || !"ok".equals(asString.trim()))) {
            throw new java.lang.RuntimeException("msg=" + asString);
        }
        java.util.ArrayList arrayList = new java.util.ArrayList();
        com.google.gson.JsonElement jsonElement = asJsonObject.get("data");
        if (jsonElement != null && jsonElement.isJsonArray()) {
            for (com.google.gson.JsonElement jsonElement2 : jsonElement.getAsJsonArray()) {
                if (jsonElement2 != null && jsonElement2.isJsonObject()) {
                    com.google.gson.JsonObject asJsonObject2 = jsonElement2.getAsJsonObject();
                    if ("read".equals(prims(asJsonObject2, "type"))) {
                        arrayList.add(asJsonObject2);
                    }
                }
            }
        } else if (jsonElement != null && jsonElement.isJsonObject()) {
            com.google.gson.JsonObject asJsonObject3 = jsonElement.getAsJsonObject();
            if ("read".equals(prims(asJsonObject3, "type"))) {
                arrayList.add(asJsonObject3);
            }
        }
        arrayList.sort(new com.tencent.qqlitepro.hook.ForceNoticeInjector$$ExternalSyntheticLambda7());
        return arrayList;
    }

    static int noticeId(com.google.gson.JsonObject jsonObject) {
        try {
            if (jsonObject.has("id") && jsonObject.get("id").isJsonPrimitive()) {
                return jsonObject.get("id").getAsInt();
            }
            return 0;
        } catch (java.lang.Throwable unused) {
            return 0;
        }
    }

    private static void showNext(android.app.Activity activity) {
        com.google.gson.JsonObject next;
        java.util.List<com.google.gson.JsonObject> list = sAllForce;
        synchronized (list) {
            java.util.Iterator<com.google.gson.JsonObject> it = list.iterator();
            do {
                if (!it.hasNext()) {
                    next = null;
                    break;
                }
                next = it.next();
            } while (!shouldShow(activity, next));
        }
        if (next != null) {
            show(activity, next);
        }
    }

    private static boolean shouldShow(android.app.Activity activity, com.google.gson.JsonObject jsonObject) {
        java.lang.String strNoticeIdStr = noticeIdStr(jsonObject);
        if (strNoticeIdStr.isEmpty() || sConfirmedShown.contains(strNoticeIdStr)) {
            return false;
        }
        android.content.Context applicationContext = activity != null ? activity.getApplicationContext() : com.tencent.qqlitepro.mail.QqMailManager.appContext();
        return (applicationContext == null || com.tencent.qqlitepro.util.Config.isForceNoticeRead(applicationContext, strNoticeIdStr)) ? false : true;
    }

    private static void show(android.app.Activity activity, com.google.gson.JsonObject jsonObject) {
        if (activity == null) {
            return;
        }
        java.lang.String strNoticeIdStr = noticeIdStr(jsonObject);
        com.tencent.qqlitepro.util.Log.i("ForceNotice 显示强制公告 id=" + strNoticeIdStr + " title=" + prims(jsonObject, "title"));
        android.app.Dialog dialog = new android.app.Dialog(activity);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnKeyListener(new com.tencent.qqlitepro.hook.ForceNoticeInjector$$ExternalSyntheticLambda14());
        android.view.Window window = dialog.getWindow();
        if (window != null) {
            window.requestFeature(1);
            window.setBackgroundDrawable(new android.graphics.drawable.GradientDrawable());
            window.getDecorView().setBackgroundColor(-15592942);
            window.setLayout(-1, -1);
        }
        android.widget.TextView[] textViewArr = new android.widget.TextView[2];
        android.view.View rootNotice = build(activity, jsonObject, textViewArr);
        /* 每次弹窗前重置倒计时标志，避免上次的 true 残留导致刚弹出就能滑走 */
        sCountdownDone = false;
        /* 包一层代理，负责右滑手势判定（不影响子 View 滚动与点击） */
        android.view.View wrapped = wrapSwipeToDismiss(rootNotice, dialog);
        dialog.setContentView(wrapped);
        dialog.show();
        sDialog = dialog;
        startCountdown(textViewArr, new java.lang.Runnable[]{new com.tencent.qqlitepro.hook.ForceNoticeInjector$$ExternalSyntheticLambda15(strNoticeIdStr, dialog), new com.tencent.qqlitepro.hook.ForceNoticeInjector$$ExternalSyntheticLambda4(activity, strNoticeIdStr, dialog)}, 8);
    }

    /**
     * 倒计时结束后允许右滑关闭公告。
     *
     * 关键：不能用 setOnTouchListener —— 根布局的子 View（ScrollView/按钮）
     * 会先消费事件，根布局收不到 MOVE，导致右滑永远失效。
     * 改为包一层 FrameLayout 并覆写 dispatchTouchEvent，在分发前先判断手势，
     * 命中右滑就直接关闭；未命中再交给子 View，不影响滚动与点击。
     */
    private static android.view.View wrapSwipeToDismiss(android.view.View root,
                                                        final android.app.Dialog dialog) {
        try {
            final android.content.Context ctx = root.getContext();
            final float[] startX = new float[1];
            final float[] startY = new float[1];
            final boolean[] tracking = new boolean[1];

            android.widget.FrameLayout wrap = new android.widget.FrameLayout(ctx) {
                @Override
                public boolean dispatchTouchEvent(android.view.MotionEvent event) {
                    try {
                        switch (event.getActionMasked()) {
                            case android.view.MotionEvent.ACTION_DOWN:
                                startX[0] = event.getX();
                                startY[0] = event.getY();
                                tracking[0] = true;
                                break;
                            case android.view.MotionEvent.ACTION_MOVE:
                                if (tracking[0] && sCountdownDone) {
                                    float dx = event.getX() - startX[0];
                                    float dy = Math.abs(event.getY() - startY[0]);
                                    /* 右滑 60dp 且纵向偏移 < 80dp：关闭公告 */
                                    if (dx > dp(ctx, 60.0f) && dy < dp(ctx, 80.0f)) {
                                        tracking[0] = false;
                                        try {
                                            dialog.dismiss();
                                        } catch (Throwable ignored) {
                                        }
                                        return true;
                                    }
                                }
                                break;
                            case android.view.MotionEvent.ACTION_UP:
                            case android.view.MotionEvent.ACTION_CANCEL:
                                tracking[0] = false;
                                break;
                            default:
                                break;
                        }
                    } catch (Throwable ignored) {
                    }
                    return super.dispatchTouchEvent(event);
                }
            };
            /* root 原本有自己的 LayoutParams，移入 wrap 时用铺满即可 */
            wrap.addView(root, new android.widget.FrameLayout.LayoutParams(-1, -1));
            return wrap;
        } catch (Throwable t) {
            com.tencent.qqlitepro.util.Log.w("公告右滑代理安装失败: " + t);
        }
        return root;
    }

    /* 倒计时结束标志（供右滑判断） */
    private static volatile boolean sCountdownDone;
    private static volatile boolean[] sSwipeDismissFlag;

    static /* synthetic */ void lambda$show$5(java.lang.String str, android.app.Dialog dialog) {
        sConfirmedShown.add(str);
        dialog.dismiss();
    }

    static /* synthetic */ void lambda$show$6(android.content.Context context, java.lang.String str, android.app.Dialog dialog) {
        android.content.Context applicationContext = context.getApplicationContext();
        if (applicationContext != null) {
            com.tencent.qqlitepro.util.Config.markForceNoticeRead(applicationContext, str);
        }
        sConfirmedShown.add(str);
        dialog.dismiss();
    }

    private static void startCountdown(android.widget.TextView[] textViewArr, java.lang.Runnable[] runnableArr, int i) {
        int[] iArr = {i};
        java.lang.String[] strArr = new java.lang.String[textViewArr.length];
        java.lang.Runnable[] runnableArr2 = new java.lang.Runnable[textViewArr.length];
        for (int i2 = 0; i2 < textViewArr.length; i2++) {
            strArr[i2] = baseLabel(textViewArr[i2]);
            runnableArr2[i2] = runnableArr[i2];
            textViewArr[i2].setEnabled(false);
            textViewArr[i2].setText(applyCounter(strArr[i2], iArr[0]));
            textViewArr[i2].setOnClickListener(null);
        }
        android.os.Handler handler = new android.os.Handler(android.os.Looper.getMainLooper());
        java.lang.Runnable[] runnableArr3 = new java.lang.Runnable[1];
        runnableArr3[0] = new com.tencent.qqlitepro.hook.ForceNoticeInjector.AnonymousClass4(textViewArr, iArr, strArr, runnableArr2, handler, runnableArr3);
        for (android.widget.TextView textView : textViewArr) {
            textView.addOnAttachStateChangeListener(new com.tencent.qqlitepro.hook.ForceNoticeInjector.AnonymousClass5(handler, runnableArr3));
        }
        handler.removeCallbacks(runnableArr3[0]);
        handler.postDelayed(runnableArr3[0], 1000L);
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.ForceNoticeInjector$4, reason: invalid class name */
    static class AnonymousClass4 implements java.lang.Runnable {
        final /* synthetic */ java.lang.String[] val$bases;
        final /* synthetic */ android.widget.TextView[] val$btns;
        final /* synthetic */ android.os.Handler val$h;
        final /* synthetic */ java.lang.Runnable[] val$hold;
        final /* synthetic */ java.lang.Runnable[] val$onClickArr;
        final /* synthetic */ int[] val$remain;

        AnonymousClass4(android.widget.TextView[] textViewArr, int[] iArr, java.lang.String[] strArr, java.lang.Runnable[] runnableArr, android.os.Handler handler, java.lang.Runnable[] runnableArr2) {
            this.val$btns = textViewArr;
            this.val$remain = iArr;
            this.val$bases = strArr;
            this.val$onClickArr = runnableArr;
            this.val$h = handler;
            this.val$hold = runnableArr2;
        }

        @Override // java.lang.Runnable
        public void run() {
            int i = 0;
            if (this.val$btns[0].getWindowToken() == null) {
                return;
            }
            int[] iArr = this.val$remain;
            int i2 = iArr[0] - 1;
            iArr[0] = i2;
            if (i2 > 0) {
                for (int i3 = 0; i3 < this.val$btns.length; i3++) {
                    java.lang.String strApplyCounter = com.tencent.qqlitepro.hook.ForceNoticeInjector.applyCounter(this.val$bases[i3], this.val$remain[0]);
                    if (!strApplyCounter.contentEquals(this.val$btns[i3].getText())) {
                        this.val$btns[i3].setText(strApplyCounter);
                    }
                }
                this.val$h.postDelayed(this.val$hold[0], 1000L);
                return;
            }
            while (true) {
                android.widget.TextView[] textViewArr = this.val$btns;
                if (i >= textViewArr.length) {
                    /* 倒计时结束：允许右滑退出 */
                    com.tencent.qqlitepro.hook.ForceNoticeInjector.sCountdownDone = true;
                    if (com.tencent.qqlitepro.hook.ForceNoticeInjector.sSwipeDismissFlag != null) {
                        com.tencent.qqlitepro.hook.ForceNoticeInjector.sSwipeDismissFlag[0] = true;
                    }
                    return;
                }
                textViewArr[i].setText(this.val$bases[i]);
                this.val$btns[i].setEnabled(true);
                this.val$btns[i].setOnClickListener(new com.tencent.qqlitepro.hook.ForceNoticeInjector$4$$ExternalSyntheticLambda0(this.val$onClickArr[i]));
                i++;
            }
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.ForceNoticeInjector$5, reason: invalid class name */
    static class AnonymousClass5 implements android.view.View.OnAttachStateChangeListener {
        final /* synthetic */ android.os.Handler val$h;
        final /* synthetic */ java.lang.Runnable[] val$hold;

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewAttachedToWindow(android.view.View view) {
        }

        AnonymousClass5(android.os.Handler handler, java.lang.Runnable[] runnableArr) {
            this.val$h = handler;
            this.val$hold = runnableArr;
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewDetachedFromWindow(android.view.View view) {
            this.val$h.removeCallbacks(this.val$hold[0]);
        }
    }

    private static java.lang.String baseLabel(android.widget.TextView textView) {
        java.lang.String string = textView.getText().toString();
        int iIndexOf = string.indexOf(" (");
        return iIndexOf >= 0 ? string.substring(0, iIndexOf) : string;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.lang.String applyCounter(java.lang.String str, int i) {
        return str + " (" + i + ")";
    }

    private static android.view.View build(android.content.Context context, com.google.gson.JsonObject jsonObject, android.widget.TextView[] textViewArr) {
        /*
         * 开屏公告自适应：
         * 读取设备真实屏幕尺寸（含 contentRect 排除状态栏/导航栏），
         * 按比例分配标题/正文/按钮区，保证任何分辨率下按钮都不被挤出屏幕。
         */
        android.graphics.Rect rect = new android.graphics.Rect();
        if (context instanceof android.app.Activity) {
            ((android.app.Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        } else {
            android.util.DisplayMetrics dm = context.getResources().getDisplayMetrics();
            rect.set(0, 0, dm.widthPixels, dm.heightPixels);
        }
        int screenW = rect.width() > 0 ? rect.width() : context.getResources().getDisplayMetrics().widthPixels;
        int screenH = rect.height() > 0 ? rect.height() : context.getResources().getDisplayMetrics().heightPixels;
        /* 边距随屏宽自适应：最小 12dp，最大 24dp（屏宽 3% 为基准） */
        int padH = Math.max(dp(context, 12.0f), Math.min(dp(context, 24.0f), (int) (screenW * 0.03f)));
        int padV = Math.max(dp(context, 10.0f), Math.min(dp(context, 20.0f), (int) (screenH * 0.02f)));
        /* 正文高度占屏 55%（保证按钮区至少能放下两个按钮） */
        int contentMinH = (int) (screenH * 0.55f);
        /* 标题字号随屏宽自适应：小屏 17sp，大屏 22sp */
        float titleSize = Math.max(17.0f, Math.min(22.0f, screenW / 22.0f));
        /* 按钮高度固定 44dp 以上，保证可点 */
        int btnMinH = Math.max(dp(context, 44.0f), (int) (screenH * 0.06f));

        android.widget.LinearLayout linearLayout = new android.widget.LinearLayout(context);
        linearLayout.setOrientation(1);
        linearLayout.setGravity(1);
        linearLayout.setPadding(padH, padV, padH, padV);
        linearLayout.setBackgroundColor(-15592942);
        android.widget.FrameLayout frameLayout = new android.widget.FrameLayout(context);
        android.widget.TextView textViewText = text(context, nonEmpty(prims(jsonObject, "title"), "（标题）"), -1, titleSize, 1);
        textViewText.setGravity(17);
        frameLayout.addView(textViewText, new android.widget.FrameLayout.LayoutParams(-1, -2, 17));
        /* 需求：删除右上角三条杠（nodeSwitcher）按钮，避免遮挡标题 */
        linearLayout.addView(frameLayout, m(-1, -2, 0, 0, 0, dp(context, 8.0f)));
        android.widget.TextView textViewText2 = text(context, "发布时间：" + nonEmpty(prims(jsonObject, "time"), "（时间未知）"), -6709078, 13.0f, 0);
        textViewText2.setGravity(17);
        linearLayout.addView(textViewText2, m(-1, -2, 0, 0, 0, dp(context, 16.0f)));
        android.widget.ScrollView scrollView = new android.widget.ScrollView(context);
        scrollView.setLayoutParams(new android.widget.LinearLayout.LayoutParams(-1, 0, 1.0f));
        scrollView.setMinimumHeight(contentMinH);
        float bodySize = Math.max(13.0f, Math.min(16.0f, screenW / 26.0f));
        android.widget.TextView textViewText3 = text(context, nonEmpty(prims(jsonObject, "content"), "（无正文）"), -1512723, bodySize, 0);
        textViewText3.setLineSpacing(dp(context, 4.0f), 1.0f);
        textViewText3.setPadding(dp(context, 8.0f), dp(context, 16.0f), dp(context, 8.0f), dp(context, 16.0f));
        scrollView.addView(textViewText3, new android.widget.FrameLayout.LayoutParams(-1, -2));
        linearLayout.addView(scrollView, new android.widget.LinearLayout.LayoutParams(-1, 0, 1.0f));
        android.widget.LinearLayout linearLayout2 = new android.widget.LinearLayout(context);
        linearLayout2.setOrientation(0);
        linearLayout2.setGravity(17);
        android.widget.LinearLayout.LayoutParams layoutParams2 = new android.widget.LinearLayout.LayoutParams(-1, btnMinH);
        layoutParams2.topMargin = dp(context, 12.0f);
        linearLayout.addView(linearLayout2, layoutParams2);
        android.widget.TextView textViewButton = button(context, "确认");
        android.widget.TextView textViewButton2 = button(context, "不再提醒");
        textViewButton.setMinHeight(btnMinH - dp(context, 8.0f));
        textViewButton2.setMinHeight(btnMinH - dp(context, 8.0f));
        textViewButton.setGravity(17);
        textViewButton2.setGravity(17);
        linearLayout2.addView(textViewButton, m(0, -1, 0, 0, dp(context, 6.0f), 0, 1.0f));
        linearLayout2.addView(textViewButton2, m(0, -1, dp(context, 6.0f), 0, 0, 0, 1.0f));
        textViewArr[0] = textViewButton;
        textViewArr[1] = textViewButton2;
        return linearLayout;
    }

    private static android.widget.TextView button(android.content.Context context, java.lang.String str) {
        android.widget.TextView textViewText = text(context, str, -1, 14.0f, 1);
        android.graphics.drawable.GradientDrawable gradientDrawable = new android.graphics.drawable.GradientDrawable();
        gradientDrawable.setShape(0);
        gradientDrawable.setColor(-10395295);
        gradientDrawable.setCornerRadius(dp(context, 10.0f));
        textViewText.setBackground(gradientDrawable);
        textViewText.setGravity(17);
        textViewText.setMaxLines(1);
        textViewText.setIncludeFontPadding(true);
        try {
            textViewText.setAutoSizeTextTypeUniformWithConfiguration(8, 14, 1, 2);
            textViewText.setAutoSizeTextTypeWithDefaults(1);
        } catch (java.lang.Throwable unused) {
        }
        textViewText.setPadding(dp(context, 6.0f), dp(context, 14.0f), dp(context, 6.0f), dp(context, 14.0f));
        return textViewText;
    }

    private static android.view.View nodeSwitcher(android.content.Context context) {
        android.widget.LinearLayout linearLayout = new android.widget.LinearLayout(context);
        linearLayout.setOrientation(1);
        linearLayout.setGravity(17);
        linearLayout.setClickable(true);
        linearLayout.setFocusable(true);
        int iDp = dp(context, 20.0f);
        int iMax = java.lang.Math.max(2, dp(context, 2.5f));
        int iDp2 = dp(context, 4.0f);
        for (int i = 0; i < 3; i++) {
            android.view.View view = new android.view.View(context);
            android.graphics.drawable.GradientDrawable gradientDrawable = new android.graphics.drawable.GradientDrawable();
            gradientDrawable.setShape(0);
            gradientDrawable.setColor(-1);
            gradientDrawable.setCornerRadius(iMax);
            view.setBackground(gradientDrawable);
            android.widget.LinearLayout.LayoutParams layoutParams = new android.widget.LinearLayout.LayoutParams(iDp, iMax);
            if (i > 0) {
                layoutParams.topMargin = iDp2;
            }
            linearLayout.addView(view, layoutParams);
        }
        linearLayout.setOnClickListener(new com.tencent.qqlitepro.hook.ForceNoticeInjector$$ExternalSyntheticLambda6(context));
        return linearLayout;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void showNodePicker(android.content.Context context) {
        if (context == null) {
            return;
        }
        android.app.Dialog dialog = new android.app.Dialog(context);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        android.view.Window window = dialog.getWindow();
        if (window != null) {
            window.requestFeature(1);
            window.setBackgroundDrawable(new android.graphics.drawable.GradientDrawable());
            window.setLayout(dp(context, 300.0f), -2);
            window.getDecorView().setBackgroundColor(-14803424);
        }
        android.widget.LinearLayout linearLayout = new android.widget.LinearLayout(context);
        linearLayout.setOrientation(1);
        linearLayout.setPadding(dp(context, 16.0f), dp(context, 18.0f), dp(context, 16.0f), dp(context, 14.0f));
        linearLayout.addView(text(context, "公告节点", -1, 17.0f, 1), new android.widget.LinearLayout.LayoutParams(-1, -2));
        android.widget.FrameLayout frameLayout = new android.widget.FrameLayout(context);
        frameLayout.setPadding(0, dp(context, 10.0f), 0, 0);
        android.widget.TextView textViewText = text(context, "加载中…", -6643546, 14.0f, 0);
        textViewText.setGravity(17);
        textViewText.setPadding(0, dp(context, 20.0f), 0, dp(context, 20.0f));
        frameLayout.addView(textViewText, new android.widget.FrameLayout.LayoutParams(-1, -2));
        linearLayout.addView(frameLayout, new android.widget.FrameLayout.LayoutParams(-1, -2));
        dialog.setContentView(linearLayout);
        dialog.show();
        new java.lang.Thread(new com.tencent.qqlitepro.hook.ForceNoticeInjector$$ExternalSyntheticLambda5(context, dialog, frameLayout, textViewText), "force-notice-node").start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void fillNodeList(android.content.Context context, android.app.Dialog dialog, android.widget.FrameLayout frameLayout, android.widget.TextView textView) {
        java.util.List<com.google.gson.JsonObject> listFetchNodeConfig;
        java.lang.String str;
        try {
            str = null;
            listFetchNodeConfig = fetchNodeConfig();
        } catch (java.lang.Throwable th) {
            listFetchNodeConfig = null;
            str = "节点加载失败：" + th.getMessage();
        }
        new android.os.Handler(android.os.Looper.getMainLooper()).post(new com.tencent.qqlitepro.hook.ForceNoticeInjector$$ExternalSyntheticLambda12(frameLayout, str, textView, listFetchNodeConfig, context, dialog));
    }

    static /* synthetic */ void lambda$fillNodeList$10(android.widget.FrameLayout frameLayout, java.lang.String str, android.widget.TextView textView, java.util.List list, android.content.Context context, android.app.Dialog dialog) {
        try {
            frameLayout.removeAllViews();
            if (str != null) {
                textView.setText(str);
                frameLayout.addView(textView, new android.widget.FrameLayout.LayoutParams(-1, -2));
                return;
            }
            try {
                if (list != null && !list.isEmpty()) {
                    java.lang.String strEffectiveNodeUrl = effectiveNodeUrl(list);
                    android.widget.ScrollView scrollView = new android.widget.ScrollView(context);
                    android.widget.LinearLayout linearLayout = new android.widget.LinearLayout(context);
                    linearLayout.setOrientation(1);
                    java.util.Iterator it = list.iterator();
                    while (it.hasNext()) {
                        com.google.gson.JsonObject jsonObject = (com.google.gson.JsonObject) it.next();
                        java.lang.String strNonEmpty = nonEmpty(prims(jsonObject, com.sun.mail.imap.IMAPStore.ID_NAME), "节点");
                        java.lang.String strPrims = prims(jsonObject, "desc");
                        java.lang.String strPrims2 = prims(jsonObject, "url");
                        android.widget.LinearLayout linearLayout2 = new android.widget.LinearLayout(context);
                        linearLayout2.setOrientation(1);
                        android.graphics.drawable.GradientDrawable gradientDrawable = new android.graphics.drawable.GradientDrawable();
                        gradientDrawable.setShape(0);
                        gradientDrawable.setColor(-14013129);
                        gradientDrawable.setCornerRadius(dp(context, 10.0f));
                        linearLayout2.setBackground(gradientDrawable);
                        linearLayout2.setPadding(dp(context, 12.0f), dp(context, 10.0f), dp(context, 12.0f), dp(context, 10.0f));
                        if (strPrims2 != null && !strPrims2.isEmpty() && strPrims2.equals(strEffectiveNodeUrl)) {
                            strNonEmpty = strNonEmpty + "（已选择）";
                        }
                        linearLayout2.addView(text(context, strNonEmpty, -1, 14.0f, 1), new android.widget.LinearLayout.LayoutParams(-1, -2));
                        if (strPrims != null && !strPrims.isEmpty()) {
                            android.widget.TextView textViewText = text(context, strPrims, -6643546, 12.0f, 0);
                            textViewText.setPadding(0, dp(context, 2.0f), 0, 0);
                            linearLayout2.addView(textViewText, new android.widget.LinearLayout.LayoutParams(-1, -2));
                        }
                        linearLayout.addView(linearLayout2, m(-1, -2, 0, 0, 0, dp(context, 10.0f)));
                        linearLayout2.setClickable(true);
                        linearLayout2.setOnClickListener(new com.tencent.qqlitepro.hook.ForceNoticeInjector$$ExternalSyntheticLambda11(strPrims2, dialog));
                    }
                    scrollView.addView(linearLayout, new android.widget.FrameLayout.LayoutParams(-1, -2));
                    frameLayout.addView(scrollView, new android.widget.FrameLayout.LayoutParams(-1, dp(context, 300.0f)));
                    return;
                }
                textView.setText("暂无可用节点");
                frameLayout.addView(textView, new android.widget.FrameLayout.LayoutParams(-1, -2));
            } catch (java.lang.Throwable th) {
                java.lang.Throwable thRethrow = th;
                com.tencent.qqlitepro.util.Log.w("ForceNotice 节点列表渲染失败: " + thRethrow);
                try {
                    dialog.dismiss();
                } catch (java.lang.Throwable unused) {
                }
            }
        } catch (java.lang.Throwable th2) {
            java.lang.Throwable thRethrow2 = th2;
        }
    }

    static /* synthetic */ void lambda$fillNodeList$9(java.lang.String str, android.app.Dialog dialog, android.view.View view) {
        if (str != null && !str.isEmpty()) {
            selectNode(str);
        }
        dialog.dismiss();
        refetchAndShow();
    }

    private static void refetchAndShow() {
        sFetched = false;
        sFetchedOk = false;
        sFetching = false;
        new java.lang.Thread(new com.tencent.qqlitepro.hook.ForceNoticeInjector$$ExternalSyntheticLambda13(), "force-notice-refetch").start();
    }

    private static java.lang.String prims(com.google.gson.JsonObject jsonObject, java.lang.String str) {
        if (jsonObject == null) {
            return "";
        }
        try {
            return (jsonObject.has(str) && jsonObject.get(str).isJsonPrimitive()) ? jsonObject.get(str).getAsString() : "";
        } catch (java.lang.Throwable unused) {
            return "";
        }
    }

    private static java.lang.String nonEmpty(java.lang.String str, java.lang.String str2) {
        return (str == null || str.isEmpty()) ? str2 : str;
    }

    private static java.lang.String noticeIdStr(com.google.gson.JsonObject jsonObject) {
        try {
            return (jsonObject.has("id") && jsonObject.get("id").isJsonPrimitive()) ? jsonObject.get("id").getAsString() : "";
        } catch (java.lang.Throwable unused) {
            return "";
        }
    }

    private static int dp(android.content.Context context, float f) {
        return (int) ((context.getResources().getDisplayMetrics().density * f) + 0.5f);
    }

    private static android.widget.TextView text(android.content.Context context, java.lang.String str, int i, float f, int i2) {
        android.widget.TextView textView = new android.widget.TextView(context);
        textView.setText(str);
        textView.setTextColor(i);
        textView.setTextSize(f);
        textView.setTypeface(android.graphics.Typeface.DEFAULT, i2);
        return textView;
    }

    private static android.widget.LinearLayout.LayoutParams m(int i, int i2, int i3, int i4, int i5, int i6) {
        android.widget.LinearLayout.LayoutParams layoutParams = new android.widget.LinearLayout.LayoutParams(i, i2);
        layoutParams.leftMargin = i3;
        layoutParams.topMargin = i4;
        layoutParams.rightMargin = i5;
        layoutParams.bottomMargin = i6;
        return layoutParams;
    }

    private static android.widget.LinearLayout.LayoutParams m(int i, int i2, int i3, int i4, int i5, int i6, float f) {
        android.widget.LinearLayout.LayoutParams layoutParamsM = m(i, i2, i3, i4, i5, i6);
        layoutParamsM.weight = f;
        return layoutParamsM;
    }
}
