package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda109 implements java.lang.Runnable {
    public final /* synthetic */ android.content.Context f$0;
    public final /* synthetic */ android.net.Uri f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ android.os.Handler f$3;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda109(android.content.Context context, android.net.Uri uri, int i, android.os.Handler handler) {
        this.f$0 = context;
        this.f$1 = uri;
        this.f$2 = i;
        this.f$3 = handler;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.SettingsInjector.lambda$handleMailPicked$180(this.f$0, this.f$1, this.f$2, this.f$3);
    }
}
