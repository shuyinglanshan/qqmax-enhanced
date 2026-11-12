package com.tencent.qqlitepro.auto;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class QqDynamicScheduler$$ExternalSyntheticLambda6 implements java.lang.Runnable {
    public final /* synthetic */ java.lang.Runnable f$0;
    public final /* synthetic */ java.util.concurrent.CountDownLatch f$1;

    public /* synthetic */ QqDynamicScheduler$$ExternalSyntheticLambda6(java.lang.Runnable runnable, java.util.concurrent.CountDownLatch countDownLatch) {
        this.f$0 = runnable;
        this.f$1 = countDownLatch;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.auto.QqDynamicScheduler.lambda$runOnMain$5(this.f$0, this.f$1);
    }
}
