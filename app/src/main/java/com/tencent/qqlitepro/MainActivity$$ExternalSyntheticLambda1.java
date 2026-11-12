package com.tencent.qqlitepro;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class MainActivity$$ExternalSyntheticLambda1 implements java.lang.Runnable {
    public final /* synthetic */ com.tencent.qqlitepro.MainActivity f$0;
    public final /* synthetic */ android.graphics.Bitmap f$1;
    public final /* synthetic */ android.widget.TextView f$2;
    public final /* synthetic */ android.widget.ImageView f$3;

    public /* synthetic */ MainActivity$$ExternalSyntheticLambda1(com.tencent.qqlitepro.MainActivity mainActivity, android.graphics.Bitmap bitmap, android.widget.TextView textView, android.widget.ImageView imageView) {
        this.f$0 = mainActivity;
        this.f$1 = bitmap;
        this.f$2 = textView;
        this.f$3 = imageView;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$loadQrCode$4(this.f$1, this.f$2, this.f$3);
    }
}
