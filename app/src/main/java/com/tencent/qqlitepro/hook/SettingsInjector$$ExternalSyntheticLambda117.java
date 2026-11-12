package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda117 implements java.lang.Runnable {
    public final /* synthetic */ android.graphics.Bitmap f$0;
    public final /* synthetic */ android.widget.ImageView f$1;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda117(android.graphics.Bitmap bitmap, android.widget.ImageView imageView) {
        this.f$0 = bitmap;
        this.f$1 = imageView;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.SettingsInjector.lambda$loadAvatar$11(this.f$0, this.f$1);
    }
}
