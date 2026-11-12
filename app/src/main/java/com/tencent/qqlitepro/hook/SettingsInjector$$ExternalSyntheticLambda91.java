package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda91 implements java.lang.Runnable {
    public final /* synthetic */ java.io.File f$0;
    public final /* synthetic */ android.os.Handler f$1;
    public final /* synthetic */ android.content.Context f$2;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda91(java.io.File file, android.os.Handler handler, android.content.Context context) {
        this.f$0 = file;
        this.f$1 = handler;
        this.f$2 = context;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.SettingsInjector.lambda$importPluginFile$85(this.f$0, this.f$1, this.f$2);
    }
}
