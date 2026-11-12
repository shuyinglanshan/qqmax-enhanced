package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class MediaPickerHook$$ExternalSyntheticLambda1 implements java.lang.reflect.InvocationHandler {
    public final /* synthetic */ java.lang.Object f$0;

    public /* synthetic */ MediaPickerHook$$ExternalSyntheticLambda1(java.lang.Object obj) {
        this.f$0 = obj;
    }

    @Override // java.lang.reflect.InvocationHandler
    public final java.lang.Object invoke(java.lang.Object obj, java.lang.reflect.Method method, java.lang.Object[] objArr) throws java.lang.Throwable {
        return com.tencent.qqlitepro.hook.MediaPickerHook.lambda$attachCells$0(this.f$0, obj, method, objArr);
    }
}
