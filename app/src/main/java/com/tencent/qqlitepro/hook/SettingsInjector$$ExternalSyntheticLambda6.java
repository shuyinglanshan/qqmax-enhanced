package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda6 implements java.lang.Runnable {
    public final /* synthetic */ boolean f$0;
    public final /* synthetic */ android.content.Context f$1;
    public final /* synthetic */ android.os.Handler f$2;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda6(boolean z, android.content.Context context, android.os.Handler handler) {
        this.f$0 = z;
        this.f$1 = context;
        this.f$2 = handler;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.SettingsInjector.lambda$qqDynamicRun$131(this.f$0, this.f$1, this.f$2);
    }
}
