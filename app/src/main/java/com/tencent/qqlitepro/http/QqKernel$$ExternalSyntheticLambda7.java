package com.tencent.qqlitepro.http;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class QqKernel$$ExternalSyntheticLambda7 implements java.lang.reflect.InvocationHandler {
    public final /* synthetic */ com.tencent.qqlitepro.http.QqKernel.BulletinWaiter f$0;

    public /* synthetic */ QqKernel$$ExternalSyntheticLambda7(com.tencent.qqlitepro.http.QqKernel.BulletinWaiter bulletinWaiter) {
        this.f$0 = bulletinWaiter;
    }

    @Override // java.lang.reflect.InvocationHandler
    public final java.lang.Object invoke(java.lang.Object obj, java.lang.reflect.Method method, java.lang.Object[] objArr) throws java.lang.Throwable {
        return com.tencent.qqlitepro.http.QqKernel.lambda$fullRecordsViaKernel$12(this.f$0, obj, method, objArr);
    }
}
