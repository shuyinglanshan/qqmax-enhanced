package com.tencent.qqlitepro.auto;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class QqDynamicScheduler$$ExternalSyntheticLambda1 implements java.lang.Runnable {
    public final /* synthetic */ java.lang.Object f$0;
    public final /* synthetic */ java.lang.Object f$1;
    public final /* synthetic */ boolean[] f$2;
    public final /* synthetic */ java.lang.Throwable[] f$3;

    public /* synthetic */ QqDynamicScheduler$$ExternalSyntheticLambda1(java.lang.Object obj, java.lang.Object obj2, boolean[] zArr, java.lang.Throwable[] thArr) {
        this.f$0 = obj;
        this.f$1 = obj2;
        this.f$2 = zArr;
        this.f$3 = thArr;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.auto.QqDynamicScheduler.lambda$safeLike$3(this.f$0, this.f$1, this.f$2, this.f$3);
    }
}
