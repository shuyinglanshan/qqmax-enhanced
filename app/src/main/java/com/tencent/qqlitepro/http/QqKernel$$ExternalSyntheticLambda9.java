package com.tencent.qqlitepro.http;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class QqKernel$$ExternalSyntheticLambda9 implements java.lang.reflect.InvocationHandler {
    public final /* synthetic */ int[] f$0;
    public final /* synthetic */ java.util.concurrent.CountDownLatch f$1;

    public /* synthetic */ QqKernel$$ExternalSyntheticLambda9(int[] iArr, java.util.concurrent.CountDownLatch countDownLatch) {
        this.f$0 = iArr;
        this.f$1 = countDownLatch;
    }

    @Override // java.lang.reflect.InvocationHandler
    public final java.lang.Object invoke(java.lang.Object obj, java.lang.reflect.Method method, java.lang.Object[] objArr) throws java.lang.Throwable {
        return com.tencent.qqlitepro.http.QqKernel.lambda$fetchGroupNotices$11(this.f$0, this.f$1, obj, method, objArr);
    }
}
