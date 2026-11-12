package com.tencent.qqlitepro;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class MainHook$$ExternalSyntheticLambda2 implements java.lang.Runnable {
    public final /* synthetic */ com.tencent.qqlitepro.MainHook f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ MainHook$$ExternalSyntheticLambda2(com.tencent.qqlitepro.MainHook mainHook, int i) {
        this.f$0 = mainHook;
        this.f$1 = i;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$retryGlobalBulletinListener$0(this.f$1);
    }
}
