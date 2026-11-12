package com.tencent.qqlitepro.hook;

/* JADX INFO: loaded from: classes.dex */
public final class AddFriendVerifyHook {
    private static final java.lang.String CLS_COLLECT = "com.tencent.qqnt.watch.add.result.QQSearchFriendFragment$onViewCreated$1$2$invokeSuspend$$inlined$collect$1";
    private static final java.lang.String CLS_FRAGMENT = "com.tencent.qqnt.watch.add.result.QQSearchFriendFragment";
    private static final int COLOR_BG_CARD = -14803424;
    private static final int COLOR_BG_CARD_DEEP = -15592942;
    private static final int COLOR_INPUT_BG = -14277077;
    private static final int COLOR_PRIMARY = -12878593;
    private static final int COLOR_SECONDARY = -12961220;
    private static final int COLOR_TEXT_SUB = -6642255;
    private static final int COLOR_TEXT_WHITE = -1;
    private static final android.os.Handler MAIN = new android.os.Handler(android.os.Looper.getMainLooper());
    private static volatile boolean sInstalled = false;

    public static boolean isInstalled() {
        return sInstalled;
    }

    private AddFriendVerifyHook() {
    }

    public static void patch(java.lang.ClassLoader classLoader) {
        if (sInstalled) {
            return;
        }
        try {
            java.lang.Class clsFindClassIfExists = de.robv.android.xposed.XposedHelpers.findClassIfExists(CLS_COLLECT, classLoader);
            if (clsFindClassIfExists == null) {
                com.tencent.qqlitepro.util.Log.i("AddFriendVerify: 未找到 collect 类（可能尚未加载），稍后重试");
                return;
            }
            boolean z = false;
            for (java.lang.reflect.Method method : clsFindClassIfExists.getDeclaredMethods()) {
                if ("emit".equals(method.getName())) {
                    method.setAccessible(true);
                    de.robv.android.xposed.XposedBridge.hookMethod(method, new com.tencent.qqlitepro.hook.AddFriendVerifyHook.AnonymousClass1(classLoader));
                    z = true;
                }
            }
            if (z) {
                sInstalled = true;
                com.tencent.qqlitepro.util.Log.i("AddFriendVerify: hook 成功 com.tencent.qqnt.watch.add.result.QQSearchFriendFragment$onViewCreated$1$2$invokeSuspend$$inlined$collect$1");
            } else {
                com.tencent.qqlitepro.util.Log.w("AddFriendVerify: 未找到 emit 方法（稍后重试）");
            }
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.e("AddFriendVerify patch: " + th);
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.AddFriendVerifyHook$1, reason: invalid class name */
    static class AnonymousClass1 extends de.robv.android.xposed.XC_MethodHook {
        final /* synthetic */ java.lang.ClassLoader val$cl;

        AnonymousClass1(java.lang.ClassLoader classLoader) {
            this.val$cl = classLoader;
        }

        protected void beforeHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            try {
                com.tencent.qqlitepro.hook.AddFriendVerifyHook.handleEmit(methodHookParam, this.val$cl);
            } catch (java.lang.Throwable th) {
                com.tencent.qqlitepro.util.Log.w("AddFriendVerify handleEmit: " + th);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void handleEmit(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam, java.lang.ClassLoader classLoader) throws java.lang.Throwable {
        java.lang.Object obj = (methodHookParam.args == null || methodHookParam.args.length <= 0) ? null : methodHookParam.args[0];
        if (obj == null) {
            return;
        }
        try {
            int intField = de.robv.android.xposed.XposedHelpers.getIntField(obj, "addFriendSetting");
            if (intField == 3 || intField == 4) {
                methodHookParam.setResult((java.lang.Object) null);
                long longField = de.robv.android.xposed.XposedHelpers.getLongField(obj, "buddyUin");
                java.lang.Object objectField = de.robv.android.xposed.XposedHelpers.getObjectField(obj, "question");
                java.util.ArrayList arrayList = new java.util.ArrayList();
                if (objectField instanceof java.util.List) {
                    for (java.lang.Object obj2 : (java.util.List) objectField) {
                        if (obj2 != null) {
                            arrayList.add(java.lang.String.valueOf(obj2));
                        }
                    }
                }
                MAIN.post(new com.tencent.qqlitepro.hook.AddFriendVerifyHook$$ExternalSyntheticLambda2(longField, arrayList));
            }
        } catch (java.lang.Throwable unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void showDialog(long j, java.util.List<java.lang.String> list) {
        try {
            android.app.Activity activityCurrentActivity = com.tencent.qqlitepro.plugin.PluginEngine.currentActivity();
            if (activityCurrentActivity == null) {
                com.tencent.qqlitepro.util.Log.w("AddFriendVerify: 无前台 Activity");
                return;
            }
            android.app.Dialog dialog = new android.app.Dialog(activityCurrentActivity);
            dialog.requestWindowFeature(1);
            android.view.Window window = dialog.getWindow();
            if (window != null) {
                window.setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(0));
                window.addFlags(2);
                window.setDimAmount(0.6f);
            }
            int i = (int) (activityCurrentActivity.getResources().getDisplayMetrics().widthPixels * 0.86f);
            int i2 = (int) (activityCurrentActivity.getResources().getDisplayMetrics().heightPixels * 0.8f);
            android.widget.LinearLayout linearLayout = new android.widget.LinearLayout(activityCurrentActivity);
            linearLayout.setOrientation(1);
            linearLayout.setBackground(roundRect(activityCurrentActivity, COLOR_BG_CARD, dp(activityCurrentActivity, 20.0f)));
            int iDp = dp(activityCurrentActivity, 20.0f);
            linearLayout.setPadding(iDp, dp(activityCurrentActivity, 16.0f), iDp, dp(activityCurrentActivity, 14.0f));
            linearLayout.setGravity(1);
            linearLayout.setLayoutParams(new android.view.ViewGroup.LayoutParams(-1, -1));
            android.widget.TextView textView = new android.widget.TextView(activityCurrentActivity);
            textView.setText("好友验证");
            textView.setTextColor(-1);
            textView.setTextSize(22.0f);
            textView.setTypeface(android.graphics.Typeface.DEFAULT_BOLD);
            textView.setGravity(17);
            textView.setPadding(0, 0, 0, dp(activityCurrentActivity, 2.0f));
            linearLayout.addView(textView, new android.widget.LinearLayout.LayoutParams(-1, -2));
            android.view.View view = new android.view.View(activityCurrentActivity);
            view.setBackgroundColor(COLOR_PRIMARY);
            android.widget.LinearLayout.LayoutParams layoutParams = new android.widget.LinearLayout.LayoutParams(dp(activityCurrentActivity, 40.0f), dp(activityCurrentActivity, 3.0f));
            layoutParams.topMargin = dp(activityCurrentActivity, 4.0f);
            layoutParams.bottomMargin = dp(activityCurrentActivity, 12.0f);
            layoutParams.gravity = 1;
            linearLayout.addView(view, layoutParams);
            java.lang.StringBuilder sb = new java.lang.StringBuilder("对方需要回答验证问题才能添加为好友：\n\n");
            if (list != null && !list.isEmpty()) {
                int i3 = 0;
                while (i3 < list.size()) {
                    int i4 = i3 + 1;
                    sb.append(i4);
                    sb.append(". ");
                    sb.append(list.get(i3));
                    sb.append('\n');
                    i3 = i4;
                }
            } else {
                sb.append("（对方未提供具体问题，可直接填写答案）\n");
            }
            android.widget.TextView textView2 = new android.widget.TextView(activityCurrentActivity);
            textView2.setText(sb.toString().trim());
            textView2.setTextColor(COLOR_TEXT_SUB);
            textView2.setTextSize(15.0f);
            textView2.setLineSpacing(dp(activityCurrentActivity, 4.0f), 1.0f);
            android.widget.EditText editText = new android.widget.EditText(activityCurrentActivity);
            editText.setHint("请输入验证答案");
            editText.setHintTextColor(-10855840);
            editText.setTextColor(-1);
            editText.setTextSize(16.0f);
            editText.setSingleLine(true);
            editText.setBackground(roundRect(activityCurrentActivity, COLOR_INPUT_BG, dp(activityCurrentActivity, 10.0f)));
            int iDp2 = dp(activityCurrentActivity, 14.0f);
            editText.setPadding(iDp2, dp(activityCurrentActivity, 8.0f), iDp2, dp(activityCurrentActivity, 8.0f));
            android.widget.LinearLayout linearLayout2 = new android.widget.LinearLayout(activityCurrentActivity);
            linearLayout2.setOrientation(1);
            linearLayout2.addView(textView2, new android.widget.LinearLayout.LayoutParams(-1, -2));
            android.widget.LinearLayout.LayoutParams layoutParams2 = new android.widget.LinearLayout.LayoutParams(-1, -2);
            layoutParams2.topMargin = dp(activityCurrentActivity, 12.0f);
            linearLayout2.addView(editText, layoutParams2);
            android.widget.ScrollView scrollView = new android.widget.ScrollView(activityCurrentActivity);
            scrollView.addView(linearLayout2, new android.view.ViewGroup.LayoutParams(-1, -2));
            android.widget.LinearLayout.LayoutParams layoutParams3 = new android.widget.LinearLayout.LayoutParams(-1, 0, 1.0f);
            layoutParams3.bottomMargin = dp(activityCurrentActivity, 12.0f);
            linearLayout.addView(scrollView, layoutParams3);
            android.widget.LinearLayout linearLayout3 = new android.widget.LinearLayout(activityCurrentActivity);
            linearLayout3.setOrientation(0);
            linearLayout3.setGravity(16);
            linearLayout.addView(linearLayout3, new android.widget.LinearLayout.LayoutParams(-1, -2));
            android.widget.TextView textViewFullWidthButton = fullWidthButton(activityCurrentActivity, "取消", COLOR_SECONDARY);
            textViewFullWidthButton.setOnClickListener(new com.tencent.qqlitepro.hook.AddFriendVerifyHook$$ExternalSyntheticLambda0(dialog));
            android.widget.LinearLayout.LayoutParams layoutParams4 = new android.widget.LinearLayout.LayoutParams(0, dp(activityCurrentActivity, 40.0f), 1.0f);
            layoutParams4.rightMargin = dp(activityCurrentActivity, 6.0f);
            linearLayout3.addView(textViewFullWidthButton, layoutParams4);
            android.widget.TextView textViewFullWidthButton2 = fullWidthButton(activityCurrentActivity, "发送", COLOR_PRIMARY);
            textViewFullWidthButton2.setOnClickListener(new com.tencent.qqlitepro.hook.AddFriendVerifyHook$$ExternalSyntheticLambda1(editText, activityCurrentActivity, j, dialog));
            android.widget.LinearLayout.LayoutParams layoutParams5 = new android.widget.LinearLayout.LayoutParams(0, dp(activityCurrentActivity, 40.0f), 1.0f);
            layoutParams5.leftMargin = dp(activityCurrentActivity, 6.0f);
            linearLayout3.addView(textViewFullWidthButton2, layoutParams5);
            linearLayout.measure(android.view.View.MeasureSpec.makeMeasureSpec(i, 1073741824), android.view.View.MeasureSpec.makeMeasureSpec(i2, Integer.MIN_VALUE));
            int iMin = java.lang.Math.min(linearLayout.getMeasuredHeight(), i2);
            dialog.setContentView(linearLayout);
            if (window != null) {
                android.view.WindowManager.LayoutParams attributes = window.getAttributes();
                attributes.width = i;
                attributes.height = iMin;
                attributes.gravity = 17;
                window.setAttributes(attributes);
            }
            dialog.setCancelable(true);
            dialog.show();
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("AddFriendVerify dialog: " + th);
        }
    }

    private static android.widget.TextView fullWidthButton(android.app.Activity activity, java.lang.String str, int i) {
        android.widget.TextView textView = new android.widget.TextView(activity);
        textView.setText(str);
        textView.setTextColor(-1);
        textView.setTextSize(16.0f);
        textView.setTypeface(android.graphics.Typeface.DEFAULT_BOLD);
        textView.setGravity(17);
        textView.setBackground(roundRect(activity, i, dp(activity, 22.0f)));
        textView.setClickable(true);
        return textView;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void onSend(android.app.Activity activity, long j, java.lang.String str, android.app.Dialog dialog) {
        if (android.text.TextUtils.isEmpty(str)) {
            toast("答案不能为空");
            return;
        }
        com.tencent.qqlitepro.util.Log.i("AddFriendVerify: 已记录验证答案 uin=" + j + ", 交由原生流程发送");
        dialog.dismiss();
    }

    private static android.graphics.drawable.GradientDrawable roundRect(android.app.Activity activity, int i, float f) {
        android.graphics.drawable.GradientDrawable gradientDrawable = new android.graphics.drawable.GradientDrawable();
        gradientDrawable.setShape(0);
        gradientDrawable.setColor(i);
        gradientDrawable.setCornerRadius(f);
        return gradientDrawable;
    }

    private static int dp(android.app.Activity activity, float f) {
        return java.lang.Math.round(f * activity.getResources().getDisplayMetrics().density);
    }

    private static void toast(java.lang.String str) {
        try {
            android.app.Activity activityCurrentActivity = com.tencent.qqlitepro.plugin.PluginEngine.currentActivity();
            if (activityCurrentActivity != null) {
                android.widget.Toast.makeText(activityCurrentActivity, str, 0).show();
            }
        } catch (java.lang.Throwable unused) {
        }
    }
}
