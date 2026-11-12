package com.tencent.qqlitepro.http;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class QqKernel$$ExternalSyntheticLambda20 implements java.lang.reflect.InvocationHandler {
    public final /* synthetic */ java.lang.Object[] f$0;
    public final /* synthetic */ java.util.concurrent.CountDownLatch f$1;

    public /* synthetic */ QqKernel$$ExternalSyntheticLambda20(java.lang.Object[] objArr, java.util.concurrent.CountDownLatch countDownLatch) {
        this.f$0 = objArr;
        this.f$1 = countDownLatch;
    }

    @Override // java.lang.reflect.InvocationHandler
    public final java.lang.Object invoke(java.lang.Object obj, java.lang.reflect.Method method, java.lang.Object[] objArr) throws java.lang.Throwable {
        return com.tencent.qqlitepro.http.QqKernel.lambda$getSessionsViaGetInfos$22(this.f$0, this.f$1, obj, method, objArr);
    }
}
