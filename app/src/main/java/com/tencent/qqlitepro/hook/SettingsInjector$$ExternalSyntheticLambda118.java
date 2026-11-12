package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda118 implements java.lang.Runnable {
    public final /* synthetic */ java.lang.String f$0;
    public final /* synthetic */ android.widget.ImageView f$1;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda118(java.lang.String str, android.widget.ImageView imageView) {
        this.f$0 = str;
        this.f$1 = imageView;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.SettingsInjector.lambda$loadAvatar$12(this.f$0, this.f$1);
    }
}
