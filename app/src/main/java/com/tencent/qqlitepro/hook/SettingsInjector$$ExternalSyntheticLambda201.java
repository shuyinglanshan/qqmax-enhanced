package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda201 implements java.lang.Runnable {
    public final /* synthetic */ android.content.Context f$0;
    public final /* synthetic */ java.lang.Throwable f$1;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda201(android.content.Context context, java.lang.Throwable th) {
        this.f$0 = context;
        this.f$1 = th;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.SettingsInjector.lambda$sendFilesToSession$100(this.f$0, this.f$1);
    }
}
