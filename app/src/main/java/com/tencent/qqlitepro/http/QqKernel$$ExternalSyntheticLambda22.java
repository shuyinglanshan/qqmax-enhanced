package com.tencent.qqlitepro.http;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class QqKernel$$ExternalSyntheticLambda22 implements java.lang.Runnable {
    public final /* synthetic */ java.lang.reflect.Method f$0;
    public final /* synthetic */ java.lang.Object f$1;
    public final /* synthetic */ java.lang.Object[] f$2;
    public final /* synthetic */ java.util.concurrent.CountDownLatch f$3;

    public /* synthetic */ QqKernel$$ExternalSyntheticLambda22(java.lang.reflect.Method method, java.lang.Object obj, java.lang.Object[] objArr, java.util.concurrent.CountDownLatch countDownLatch) {
        this.f$0 = method;
        this.f$1 = obj;
        this.f$2 = objArr;
        this.f$3 = countDownLatch;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.http.QqKernel.lambda$awaitOperate$28(this.f$0, this.f$1, this.f$2, this.f$3);
    }
}
