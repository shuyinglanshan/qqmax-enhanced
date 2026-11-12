package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda179 implements java.lang.reflect.InvocationHandler {
    public final /* synthetic */ android.app.Activity f$0;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda179(android.app.Activity activity) {
        this.f$0 = activity;
    }

    @Override // java.lang.reflect.InvocationHandler
    public final java.lang.Object invoke(java.lang.Object obj, java.lang.reflect.Method method, java.lang.Object[] objArr) throws java.lang.Throwable {
        return com.tencent.qqlitepro.hook.SettingsInjector.lambda$registerBackInvoke$22(this.f$0, obj, method, objArr);
    }
}
