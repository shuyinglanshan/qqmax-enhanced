package com.tencent.qqlitepro;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class MainHook$$ExternalSyntheticLambda0 implements java.lang.Runnable {
    public final /* synthetic */ com.tencent.qqlitepro.MainHook f$0;
    public final /* synthetic */ de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam f$1;

    public /* synthetic */ MainHook$$ExternalSyntheticLambda0(com.tencent.qqlitepro.MainHook mainHook, de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam loadPackageParam) {
        this.f$0 = mainHook;
        this.f$1 = loadPackageParam;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$checkGroupAndInstall$5(this.f$1);
    }
}
