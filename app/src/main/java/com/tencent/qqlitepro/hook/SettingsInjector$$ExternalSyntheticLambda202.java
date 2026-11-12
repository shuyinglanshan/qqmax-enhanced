package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda202 implements java.util.concurrent.Callable {
    public final /* synthetic */ int f$0;
    public final /* synthetic */ java.util.List f$1;
    public final /* synthetic */ boolean f$2;
    public final /* synthetic */ android.os.Handler f$3;
    public final /* synthetic */ java.lang.Object f$4;
    public final /* synthetic */ int f$5;
    public final /* synthetic */ java.lang.String f$6;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda202(int i, java.util.List list, boolean z, android.os.Handler handler, java.lang.Object obj, int i2, java.lang.String str) {
        this.f$0 = i;
        this.f$1 = list;
        this.f$2 = z;
        this.f$3 = handler;
        this.f$4 = obj;
        this.f$5 = i2;
        this.f$6 = str;
    }

    @Override // java.util.concurrent.Callable
    public final java.lang.Object call() throws java.lang.Exception {
        return com.tencent.qqlitepro.hook.SettingsInjector.lambda$sendFilesToSession$101(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6);
    }
}
