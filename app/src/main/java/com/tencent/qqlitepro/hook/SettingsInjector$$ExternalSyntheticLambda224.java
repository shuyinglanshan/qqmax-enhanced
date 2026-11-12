package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda224 implements java.lang.Runnable {
    public final /* synthetic */ boolean f$0;
    public final /* synthetic */ java.lang.Object f$1;
    public final /* synthetic */ java.lang.Object[] f$2;
    public final /* synthetic */ java.lang.Throwable[] f$3;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda224(boolean z, java.lang.Object obj, java.lang.Object[] objArr, java.lang.Throwable[] thArr) {
        this.f$0 = z;
        this.f$1 = obj;
        this.f$2 = objArr;
        this.f$3 = thArr;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.SettingsInjector.lambda$buildFakeElement$104(this.f$0, this.f$1, this.f$2, this.f$3);
    }
}
