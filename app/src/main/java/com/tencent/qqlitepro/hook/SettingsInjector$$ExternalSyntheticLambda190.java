package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda190 implements java.lang.Runnable {
    public final /* synthetic */ android.content.Context f$0;
    public final /* synthetic */ java.lang.String f$1;
    public final /* synthetic */ java.lang.String f$2;
    public final /* synthetic */ android.os.Handler f$3;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda190(android.content.Context context, java.lang.String str, java.lang.String str2, android.os.Handler handler) {
        this.f$0 = context;
        this.f$1 = str;
        this.f$2 = str2;
        this.f$3 = handler;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.SettingsInjector.lambda$downloadMailAtt$165(this.f$0, this.f$1, this.f$2, this.f$3);
    }
}
