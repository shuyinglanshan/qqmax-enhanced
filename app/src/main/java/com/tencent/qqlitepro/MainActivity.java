package com.tencent.qqlitepro;

/* JADX INFO: loaded from: classes.dex */
public class MainActivity extends android.app.Activity {
    private static final int QR_RES = com.tencent.qqlitepro.R.drawable.group_qr;
    private static final java.lang.String QR_SAVE_NAME = "official_group_qrcode.jpg";
    private static final java.lang.String QR_URL = "https://yuju.99kpk.top:81/pan/1610/qrcode_1790274464464.jpg";
    /*
     * 状态刷新间隔。
     * 因为有变化检测（见 refreshStatus），状态不变时几乎零开销，
     * 所以这里用 800ms 保证激活/注入状态能"实时"反映，而不会带来重绘负担。
     */
    private static final long REFRESH_MS = 800;
    /* 状态缓存：用于跳过无变化的重绘 */
    private boolean mStatusInited;
    private int mLastFw = -1;
    private boolean mLastAct;
    private boolean mLastInj;
    private java.lang.String mLastHint;
    private android.widget.TextView mActVal;
    private android.widget.TextView mFwVal;
    private android.widget.TextView mHint;
    private android.widget.TextView mInjVal;
    private float mScale = 1.0f;
    private android.graphics.Bitmap mQrBitmap = null;
    private final android.os.Handler mHandler = new android.os.Handler(android.os.Looper.getMainLooper());
    private final java.lang.Runnable mRefresh = new com.tencent.qqlitepro.MainActivity.AnonymousClass1();

    private float sp(float f) {
        return f * this.mScale;
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.MainActivity$1, reason: invalid class name */
    class AnonymousClass1 implements java.lang.Runnable {
        AnonymousClass1() {
        }

        @Override // java.lang.Runnable
        public void run() {
            com.tencent.qqlitepro.MainActivity.this.refreshStatus();
            com.tencent.qqlitepro.MainActivity.this.mHandler.postDelayed(this, com.tencent.qqlitepro.MainActivity.REFRESH_MS);
        }
    }

    @Override // android.app.Activity
    protected void onResume() {
        super.onResume();
        this.mHandler.removeCallbacks(this.mRefresh);
        this.mHandler.post(this.mRefresh);
    }

    @Override // android.app.Activity
    protected void onPause() {
        super.onPause();
        this.mHandler.removeCallbacks(this.mRefresh);
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        this.mHandler.removeCallbacks(this.mRefresh);
        super.onDestroy();
    }

    @Override // android.app.Activity
    protected void onCreate(android.os.Bundle bundle) {
        super.onCreate(bundle);
        this.mScale = computeScale();
        android.widget.ScrollView scrollView = new android.widget.ScrollView(this);
        scrollView.setBackgroundColor(-14671580);
        android.widget.LinearLayout linearLayout = new android.widget.LinearLayout(this);
        linearLayout.setOrientation(1);
        linearLayout.setGravity(49);
        linearLayout.setPadding(dp(20.0f), dp(20.0f), dp(20.0f), dp(20.0f));
        scrollView.addView(linearLayout, new android.widget.FrameLayout.LayoutParams(-1, -2));
        android.graphics.drawable.GradientDrawable gradientDrawable = new android.graphics.drawable.GradientDrawable();
        gradientDrawable.setColor(-13816010);
        gradientDrawable.setCornerRadius(dp(14.0f));
        scrollView.setBackground(gradientDrawable);
        linearLayout.addView(tv("QQMax增强模块", 23.0f, -1, 17), lp());
        linearLayout.addView(tv("模块状态", 16.0f, -12878593, 8388611), lp());
        android.widget.TextView textViewStatusValue = statusValue();
        this.mFwVal = textViewStatusValue;
        android.view.View viewStatusRow = statusRow("运行框架", textViewStatusValue);
        linearLayout.addView(viewStatusRow, lp());
        android.widget.TextView textViewStatusValue2 = statusValue();
        this.mActVal = textViewStatusValue2;
        android.view.View viewStatusRow2 = statusRow("激活状态", textViewStatusValue2);
        linearLayout.addView(viewStatusRow2, lp());
        android.widget.TextView textViewStatusValue3 = statusValue();
        this.mInjVal = textViewStatusValue3;
        android.view.View viewStatusRow3 = statusRow("注入状态", textViewStatusValue3);
        linearLayout.addView(viewStatusRow3, lp());
        viewStatusRow.setOnClickListener(new com.tencent.qqlitepro.MainActivity$$ExternalSyntheticLambda2(this));
        viewStatusRow2.setOnClickListener(new com.tencent.qqlitepro.MainActivity$$ExternalSyntheticLambda3(this));
        viewStatusRow3.setOnClickListener(new com.tencent.qqlitepro.MainActivity$$ExternalSyntheticLambda4(this));
        android.widget.TextView textViewTv = tv("", 13.0f, -1739917, 8388611);
        this.mHint = textViewTv;
        linearLayout.addView(textViewTv, lp());
        refreshStatus();
        linearLayout.addView(tv("所有功能均在 QQMax → 设置 →【增强模块设置】中配置", 16.0f, -6491966, 8388611), lp());
        android.widget.TextView textViewTv2 = tv("功能一览：\n· 防撤回：强制阻止撤回（红边角标）/ 原文还原\n· 群公告全量显示、翻译接口替换\n· 自定义后缀、定时消息\n· 关键词回复 / 自动回复（支持 AI）\n· AI 总结、URL 转语音发送\n· 任意格式文件发送（伪图片/伪语音/分片）\n· 下载管理、JS 插件扩展\n· 本地 HTTP API、QQ 动态定时点赞/评论\n\n使用步骤：\n1. 在 LSPosed / EdXp 中启用本模块；\n2. 勾选作用域：com.tencent.qqlite；\n3. 加入官方群聊 801524531\n    （未加入时不会注入任何功能）；\n4. 重启目标应用生效；\n5. 打开 QQMax → 设置 →【增强模块设置】\n    配置各功能；\n6. 详见【关于模块】页。", 14.0f, -2236963, 8388611);
        textViewTv2.setLineSpacing(0.0f, 1.3f);
        linearLayout.addView(textViewTv2, lp());
        android.widget.TextView textView = new android.widget.TextView(this);
        textView.setText("\n版本:v1.2.1\n作者:疏影阑珊\n官方群聊:801524531\n\n");
        textView.setTextSize(sp(15.0f));
        textView.setTextColor(-1052689);
        textView.setGravity(17);
        linearLayout.addView(textView, lp());
        android.widget.TextView textView2 = new android.widget.TextView(this);
        textView2.setText("Made by TraeWork");
        textView2.setTextSize(sp(11.0f));
        textView2.setTextColor(-10177034);
        textView2.setGravity(17);
        linearLayout.addView(textView2, lp());
        android.view.View viewTv = tv("扫码加入官方群聊", 15.0f, -12878593, 17);
        android.widget.LinearLayout.LayoutParams layoutParamsLp = lp();
        layoutParamsLp.topMargin = dp(16.0f);
        linearLayout.addView(viewTv, layoutParamsLp);
        android.widget.ImageView imageView = new android.widget.ImageView(this);
        imageView.setScaleType(android.widget.ImageView.ScaleType.FIT_CENTER);
        imageView.setAdjustViewBounds(true);
        android.widget.LinearLayout.LayoutParams layoutParams = new android.widget.LinearLayout.LayoutParams(dp(140.0f), dp(140.0f));
        layoutParams.gravity = 1;
        layoutParams.topMargin = dp(8.0f);
        imageView.setLayoutParams(layoutParams);
        imageView.setOnClickListener(new com.tencent.qqlitepro.MainActivity$$ExternalSyntheticLambda5(this));
        linearLayout.addView(imageView);
        android.widget.TextView textViewTv3 = tv("二维码加载中…", 12.0f, -6643546, 17);
        linearLayout.addView(textViewTv3, lp());
        loadQrCode(imageView, textViewTv3);
        setContentView(scrollView);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$0(android.view.View view) {
        refreshStatus();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$1(android.view.View view) {
        refreshStatus();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$2(android.view.View view) {
        refreshStatus();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$3(android.view.View view) {
        showQrFullscreen();
    }

    private void loadQrCode(android.widget.ImageView imageView, android.widget.TextView textView) {
        android.graphics.Bitmap bitmapDecodeResource = android.graphics.BitmapFactory.decodeResource(getResources(), QR_RES);
        if (bitmapDecodeResource != null) {
            this.mQrBitmap = bitmapDecodeResource;
            imageView.setImageBitmap(bitmapDecodeResource);
            textView.setText("点击二维码可查看大图（右滑退出）");
        } else {
            java.lang.Thread thread = new java.lang.Thread(new com.tencent.qqlitepro.MainActivity$$ExternalSyntheticLambda0(this, textView, imageView), "antirecall-qrcode");
            thread.setDaemon(true);
            thread.start();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadQrCode$5(android.widget.TextView textView, android.widget.ImageView imageView) {
        android.graphics.Bitmap bitmapDecodeFile = null;
        try {
            java.lang.String strDownload = com.tencent.qqlitepro.util.Net.download(this, QR_URL, QR_SAVE_NAME);
            if (strDownload != null) {
                bitmapDecodeFile = android.graphics.BitmapFactory.decodeFile(strDownload);
            }
        } catch (java.lang.Throwable unused) {
        }
        runOnUiThread(new com.tencent.qqlitepro.MainActivity$$ExternalSyntheticLambda1(this, bitmapDecodeFile, textView, imageView));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadQrCode$4(android.graphics.Bitmap bitmap, android.widget.TextView textView, android.widget.ImageView imageView) {
        if (bitmap == null) {
            textView.setText("二维码加载失败，请前往官方群聊 801524531");
            return;
        }
        this.mQrBitmap = bitmap;
        imageView.setImageBitmap(bitmap);
        textView.setText("点击二维码可查看大图（右滑退出）");
    }

    private void showQrFullscreen() {
        if (this.mQrBitmap == null) {
            return;
        }
        android.app.Dialog dialog = new android.app.Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        android.widget.FrameLayout frameLayout = new android.widget.FrameLayout(this);
        frameLayout.setBackgroundColor(-16777216);
        android.widget.ImageView imageView = new android.widget.ImageView(this);
        imageView.setImageBitmap(this.mQrBitmap);
        imageView.setScaleType(android.widget.ImageView.ScaleType.FIT_CENTER);
        android.util.DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int iMin = (int) (java.lang.Math.min(displayMetrics.widthPixels, displayMetrics.heightPixels) * 0.9f);
        frameLayout.addView(imageView, new android.widget.FrameLayout.LayoutParams(iMin, iMin, 17));
        android.widget.TextView textView = new android.widget.TextView(this);
        textView.setText("右滑退出");
        textView.setTextSize(sp(12.0f));
        textView.setTextColor(-6643546);
        textView.setGravity(17);
        android.widget.FrameLayout.LayoutParams layoutParams = new android.widget.FrameLayout.LayoutParams(-1, -2, 80);
        layoutParams.bottomMargin = dp(24.0f);
        frameLayout.addView(textView, layoutParams);
        frameLayout.setOnTouchListener(new com.tencent.qqlitepro.MainActivity.AnonymousClass3(new android.view.GestureDetector(this, new com.tencent.qqlitepro.MainActivity.AnonymousClass2(dialog, dp(50.0f), dp(120.0f)))));
        dialog.setContentView(frameLayout);
        dialog.show();
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.MainActivity$2, reason: invalid class name */
    class AnonymousClass2 extends android.view.GestureDetector.SimpleOnGestureListener {
        final /* synthetic */ android.app.Dialog val$dialog;
        final /* synthetic */ int val$flingMin;
        final /* synthetic */ int val$scrollMin;

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public boolean onDown(android.view.MotionEvent motionEvent) {
            return true;
        }

        AnonymousClass2(android.app.Dialog dialog, int i, int i2) {
            this.val$dialog = dialog;
            this.val$flingMin = i;
            this.val$scrollMin = i2;
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public boolean onFling(android.view.MotionEvent motionEvent, android.view.MotionEvent motionEvent2, float f, float f2) {
            return com.tencent.qqlitepro.MainActivity.this.tryDismiss(this.val$dialog, motionEvent, motionEvent2, f, this.val$flingMin);
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public boolean onScroll(android.view.MotionEvent motionEvent, android.view.MotionEvent motionEvent2, float f, float f2) {
            return com.tencent.qqlitepro.MainActivity.this.tryDismiss(this.val$dialog, motionEvent, motionEvent2, f, this.val$scrollMin);
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.MainActivity$3, reason: invalid class name */
    class AnonymousClass3 implements android.view.View.OnTouchListener {
        final /* synthetic */ android.view.GestureDetector val$detector;

        AnonymousClass3(android.view.GestureDetector gestureDetector) {
            this.val$detector = gestureDetector;
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(android.view.View view, android.view.MotionEvent motionEvent) {
            this.val$detector.onTouchEvent(motionEvent);
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean tryDismiss(android.app.Dialog dialog, android.view.MotionEvent motionEvent, android.view.MotionEvent motionEvent2, float f, int i) {
        if (motionEvent != null && motionEvent2 != null) {
            float x = motionEvent2.getX() - motionEvent.getX();
            float fAbs = java.lang.Math.abs(motionEvent2.getY() - motionEvent.getY());
            if (x > i && x > fAbs) {
                dialog.dismiss();
                return true;
            }
        }
        return false;
    }

    private android.widget.TextView tv(java.lang.String str, float f, int i, int i2) {
        android.widget.TextView textView = new android.widget.TextView(this);
        textView.setText(str);
        textView.setTextSize(sp(f));
        textView.setTextColor(i);
        textView.setGravity(i2);
        textView.setLineSpacing(0.0f, 1.25f);
        return textView;
    }

    private android.widget.TextView statusValue() {
        android.widget.TextView textView = new android.widget.TextView(this);
        textView.setTextSize(sp(15.0f));
        textView.setGravity(8388613);
        return textView;
    }

    private android.widget.LinearLayout statusRow(java.lang.String str, android.widget.TextView textView) {
        android.widget.LinearLayout linearLayout = new android.widget.LinearLayout(this);
        linearLayout.setOrientation(0);
        linearLayout.setGravity(16);
        android.widget.TextView textView2 = new android.widget.TextView(this);
        textView2.setText(str);
        textView2.setTextSize(sp(15.0f));
        textView2.setTextColor(-1052689);
        textView2.setGravity(8388611);
        textView2.setLayoutParams(new android.widget.LinearLayout.LayoutParams(0, -2, 1.0f));
        linearLayout.addView(textView2);
        linearLayout.addView(textView);
        return linearLayout;
    }

    /* JADX INFO: Access modifiers changed from: private */
    private void refreshStatus() {
        int iFramework = com.tencent.qqlitepro.util.Status.framework(this);
        boolean zActivated = com.tencent.qqlitepro.util.Status.activated(this);
        boolean zInjected = com.tencent.qqlitepro.util.Status.injected(this);
        /*
         * 卡顿修复：状态值几乎不变，但 setText/setTextColor 每次都会触发 requestLayout+invalidate。
         * 加变化检测后，状态稳定时刷新几乎零成本（也顺带避免闪烁）。
         */
        if (!this.mStatusInited || this.mLastFw != iFramework) {
            this.mLastFw = iFramework;
            this.mFwVal.setText(com.tencent.qqlitepro.util.Status.frameworkName(iFramework));
            this.mFwVal.setTextColor((iFramework == 0 || iFramework == 1) ? -1739917 : -11751600);
        }
        if (!this.mStatusInited || this.mLastAct != zActivated) {
            this.mLastAct = zActivated;
            this.mActVal.setText(zActivated ? "已激活" : "未激活");
            this.mActVal.setTextColor(zActivated ? -11751600 : -1739917);
        }
        if (!this.mStatusInited || this.mLastInj != zInjected) {
            this.mLastInj = zInjected;
            this.mInjVal.setText(zInjected ? "已注入" : "未注入");
            this.mInjVal.setTextColor(zInjected ? -11751600 : -1739917);
        }
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        if (zActivated && zInjected) {
            sb.append("模块已在 QQMax 中生效");
        } else if (zActivated) {
            // 激活 = 框架已启用本模块；宿主没运行不代表没激活
            sb.append("模块已激活（框架已启用），启动 QQMax 后即可生效");
        } else {
            sb.append("未激活：");
            sb.append(com.tencent.qqlitepro.util.Status.activationHint(iFramework));
        }
        java.lang.String strAndroid81Hint = com.tencent.qqlitepro.util.Status.android81Hint();
        if (strAndroid81Hint.length() > 0) {
            sb.append('\n');
            sb.append(strAndroid81Hint);
        }
        java.lang.String hintText = sb.toString();
        int hintColor = zActivated ? -11751600 : -1739917;
        if (!this.mStatusInited || !hintText.equals(this.mLastHint)) {
            this.mLastHint = hintText;
            this.mHint.setText(hintText);
            this.mHint.setTextColor(hintColor);
        }
        this.mStatusInited = true;
    }

    private android.widget.LinearLayout.LayoutParams lp() {
        android.widget.LinearLayout.LayoutParams layoutParams = new android.widget.LinearLayout.LayoutParams(-1, -2);
        layoutParams.topMargin = dp(10.0f);
        return layoutParams;
    }

    private float computeScale() {
        try {
            android.util.DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            float fMin = ((java.lang.Math.min(displayMetrics.widthPixels, displayMetrics.heightPixels) / java.lang.Math.min(1080.0f, 2340.0f)) + 0.2f) * 0.9f;
            if (fMin < 0.7f) {
                fMin = 0.7f;
            }
            if (fMin > 1.35f) {
                return 1.35f;
            }
            return fMin;
        } catch (java.lang.Throwable unused) {
            return 1.0f;
        }
    }

    private int dp(float f) {
        return java.lang.Math.round(f * getResources().getDisplayMetrics().density);
    }
}
