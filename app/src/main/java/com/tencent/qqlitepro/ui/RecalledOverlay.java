package com.tencent.qqlitepro.ui;

/* JADX INFO: loaded from: classes.dex */
public final class RecalledOverlay {
    private static final int RED = android.graphics.Color.rgb(230, 54, 57);

    private RecalledOverlay() {
    }

    public static void show(android.content.Context context, java.lang.String str) {
        if (context == null || str == null) {
            return;
        }
        new android.os.Handler(android.os.Looper.getMainLooper()).post(new com.tencent.qqlitepro.ui.RecalledOverlay$$ExternalSyntheticLambda0(context, str));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void toast(android.content.Context context, java.lang.String str) {
        android.widget.FrameLayout frameLayout = new android.widget.FrameLayout(context);
        android.graphics.drawable.GradientDrawable gradientDrawable = new android.graphics.drawable.GradientDrawable();
        gradientDrawable.setColor(-419430401);
        gradientDrawable.setCornerRadius(dp(context, 8.0f));
        int iDp = dp(context, 2.0f);
        int i = RED;
        gradientDrawable.setStroke(iDp, i);
        frameLayout.setBackground(gradientDrawable);
        frameLayout.setPadding(dp(context, 12.0f), dp(context, 8.0f), dp(context, 12.0f), dp(context, 8.0f));
        android.widget.TextView textView = new android.widget.TextView(context);
        textView.setText("对方撤回了一条消息：\n" + str);
        textView.setTextColor(android.graphics.Color.parseColor("#c62828"));
        textView.setTextSize(14.0f);
        textView.setLineSpacing(0.0f, 1.1f);
        textView.setMaxLines(8);
        frameLayout.addView(textView, new android.widget.FrameLayout.LayoutParams(-2, -2));
        android.widget.TextView textView2 = new android.widget.TextView(context);
        textView2.setText("已撤回");
        textView2.setTextColor(-1);
        textView2.setTextSize(8.0f);
        textView2.setBackgroundColor(i);
        textView2.setPadding(dp(context, 6.0f), dp(context, 1.0f), dp(context, 6.0f), dp(context, 1.0f));
        android.widget.FrameLayout.LayoutParams layoutParams = new android.widget.FrameLayout.LayoutParams(-2, -2, 8388661);
        layoutParams.setMargins(0, 0, 0, 0);
        frameLayout.addView(textView2, layoutParams);
        frameLayout.measure(0, 0);
        frameLayout.getMeasuredWidth();
        android.widget.Toast toast = new android.widget.Toast(context);
        toast.setDuration(1);
        toast.setGravity(49, 0, dp(context, 48.0f));
        android.widget.LinearLayout linearLayout = new android.widget.LinearLayout(context);
        linearLayout.setPadding(dp(context, 8.0f), 0, dp(context, 8.0f), 0);
        linearLayout.addView(frameLayout);
        toast.setView(linearLayout);
        toast.show();
    }

    private static int dp(android.content.Context context, float f) {
        return java.lang.Math.round(f * context.getResources().getDisplayMetrics().density);
    }
}
