package com.tencent.qqlitepro;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class MainActivity$$ExternalSyntheticLambda0 implements java.lang.Runnable {
    public final /* synthetic */ com.tencent.qqlitepro.MainActivity f$0;
    public final /* synthetic */ android.widget.TextView f$1;
    public final /* synthetic */ android.widget.ImageView f$2;

    public /* synthetic */ MainActivity$$ExternalSyntheticLambda0(com.tencent.qqlitepro.MainActivity mainActivity, android.widget.TextView textView, android.widget.ImageView imageView) {
        this.f$0 = mainActivity;
        this.f$1 = textView;
        this.f$2 = imageView;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$loadQrCode$5(this.f$1, this.f$2);
    }
}
