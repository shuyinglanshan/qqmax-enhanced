package com.tencent.qqlitepro.http;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class QqKernel$$ExternalSyntheticLambda5 implements java.lang.reflect.InvocationHandler {
    public final /* synthetic */ java.lang.String f$0;

    public /* synthetic */ QqKernel$$ExternalSyntheticLambda5(java.lang.String str) {
        this.f$0 = str;
    }

    @Override // java.lang.reflect.InvocationHandler
    public final java.lang.Object invoke(java.lang.Object obj, java.lang.reflect.Method method, java.lang.Object[] objArr) throws java.lang.Throwable {
        return com.tencent.qqlitepro.http.QqKernel.lambda$loggingOperateCallback$26(this.f$0, obj, method, objArr);
    }
}
