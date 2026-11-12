package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda180 implements java.lang.reflect.InvocationHandler {
    public final /* synthetic */ int f$0;
    public final /* synthetic */ java.lang.Object f$1;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda180(int i, java.lang.Object obj) {
        this.f$0 = i;
        this.f$1 = obj;
    }

    @Override // java.lang.reflect.InvocationHandler
    public final java.lang.Object invoke(java.lang.Object obj, java.lang.reflect.Method method, java.lang.Object[] objArr) throws java.lang.Throwable {
        return com.tencent.qqlitepro.hook.SettingsInjector.lambda$makeCategory$19(this.f$0, this.f$1, obj, method, objArr);
    }
}
