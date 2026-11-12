package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda164 implements java.lang.Runnable {
    public final /* synthetic */ android.content.Context f$0;
    public final /* synthetic */ boolean f$1;
    public final /* synthetic */ boolean f$2;
    public final /* synthetic */ android.os.Handler f$3;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda164(android.content.Context context, boolean z, boolean z2, android.os.Handler handler) {
        this.f$0 = context;
        this.f$1 = z;
        this.f$2 = z2;
        this.f$3 = handler;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.SettingsInjector.lambda$qqDynamicRunLatest$133(this.f$0, this.f$1, this.f$2, this.f$3);
    }
}
