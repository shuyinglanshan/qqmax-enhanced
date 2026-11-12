package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda209 implements java.lang.Runnable {
    public final /* synthetic */ android.app.AlertDialog f$0;
    public final /* synthetic */ android.content.Context f$1;
    public final /* synthetic */ java.lang.Throwable f$2;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda209(android.app.AlertDialog alertDialog, android.content.Context context, java.lang.Throwable th) {
        this.f$0 = alertDialog;
        this.f$1 = context;
        this.f$2 = th;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.SettingsInjector.lambda$downloadPluginForNotice$79(this.f$0, this.f$1, this.f$2);
    }
}
