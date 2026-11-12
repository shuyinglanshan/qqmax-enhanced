package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda112 implements java.lang.Runnable {
    public final /* synthetic */ java.lang.Runnable f$0;
    public final /* synthetic */ android.app.Dialog[] f$1;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda112(java.lang.Runnable runnable, android.app.Dialog[] dialogArr) {
        this.f$0 = runnable;
        this.f$1 = dialogArr;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.SettingsInjector.lambda$showLegacySessionPicker$5(this.f$0, this.f$1);
    }
}
