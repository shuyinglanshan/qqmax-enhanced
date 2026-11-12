package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class NativePickerBridge$13$$ExternalSyntheticLambda0 implements java.lang.Runnable {
    public final /* synthetic */ java.lang.reflect.Method f$0;
    public final /* synthetic */ java.lang.Object f$1;
    public final /* synthetic */ java.lang.Object[] f$2;

    public /* synthetic */ NativePickerBridge$13$$ExternalSyntheticLambda0(java.lang.reflect.Method method, java.lang.Object obj, java.lang.Object[] objArr) {
        this.f$0 = method;
        this.f$1 = obj;
        this.f$2 = objArr;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.NativePickerBridge.AnonymousClass13.lambda$beforeHookedMethod$0(this.f$0, this.f$1, this.f$2);
    }
}
