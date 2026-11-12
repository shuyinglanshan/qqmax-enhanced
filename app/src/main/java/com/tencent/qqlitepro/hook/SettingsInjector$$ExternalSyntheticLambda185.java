package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda185 implements java.lang.Runnable {
    public final /* synthetic */ java.lang.Object[] f$0;
    public final /* synthetic */ java.lang.Throwable[] f$1;
    public final /* synthetic */ android.os.Handler f$2;
    public final /* synthetic */ android.content.Context f$3;
    public final /* synthetic */ java.util.List f$4;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda185(java.lang.Object[] objArr, java.lang.Throwable[] thArr, android.os.Handler handler, android.content.Context context, java.util.List list) {
        this.f$0 = objArr;
        this.f$1 = thArr;
        this.f$2 = handler;
        this.f$3 = context;
        this.f$4 = list;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.SettingsInjector.lambda$sendVoiceFiles$119(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4);
    }
}
