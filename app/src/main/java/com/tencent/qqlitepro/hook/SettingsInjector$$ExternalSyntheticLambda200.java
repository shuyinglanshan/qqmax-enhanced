package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda200 implements java.lang.Runnable {
    public final /* synthetic */ android.content.Context f$0;
    public final /* synthetic */ boolean f$1;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda200(android.content.Context context, boolean z) {
        this.f$0 = context;
        this.f$1 = z;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.SettingsInjector.lambda$sendFilesToSession$99(this.f$0, this.f$1);
    }
}
