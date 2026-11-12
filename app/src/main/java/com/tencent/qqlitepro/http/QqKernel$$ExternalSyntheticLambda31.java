package com.tencent.qqlitepro.http;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class QqKernel$$ExternalSyntheticLambda31 implements java.lang.reflect.InvocationHandler {
    public final /* synthetic */ int[] f$0;
    public final /* synthetic */ java.lang.String[] f$1;
    public final /* synthetic */ java.util.concurrent.CountDownLatch f$2;

    public /* synthetic */ QqKernel$$ExternalSyntheticLambda31(int[] iArr, java.lang.String[] strArr, java.util.concurrent.CountDownLatch countDownLatch) {
        this.f$0 = iArr;
        this.f$1 = strArr;
        this.f$2 = countDownLatch;
    }

    @Override // java.lang.reflect.InvocationHandler
    public final java.lang.Object invoke(java.lang.Object obj, java.lang.reflect.Method method, java.lang.Object[] objArr) throws java.lang.Throwable {
        return com.tencent.qqlitepro.http.QqKernel.lambda$setGroupShutUp$3(this.f$0, this.f$1, this.f$2, obj, method, objArr);
    }
}
