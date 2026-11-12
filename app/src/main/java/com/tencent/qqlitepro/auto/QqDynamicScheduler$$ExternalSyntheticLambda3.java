package com.tencent.qqlitepro.auto;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class QqDynamicScheduler$$ExternalSyntheticLambda3 implements java.lang.Runnable {
    public final /* synthetic */ boolean[] f$0;

    public /* synthetic */ QqDynamicScheduler$$ExternalSyntheticLambda3(boolean[] zArr) {
        this.f$0 = zArr;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.auto.QqDynamicScheduler.lambda$triggerNativeRefresh$1(this.f$0);
    }
}
