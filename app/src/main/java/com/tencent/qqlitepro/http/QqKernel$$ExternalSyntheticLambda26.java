package com.tencent.qqlitepro.http;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class QqKernel$$ExternalSyntheticLambda26 implements java.lang.Runnable {
    public final /* synthetic */ long f$0;
    public final /* synthetic */ java.util.concurrent.CountDownLatch f$1;

    public /* synthetic */ QqKernel$$ExternalSyntheticLambda26(long j, java.util.concurrent.CountDownLatch countDownLatch) {
        this.f$0 = j;
        this.f$1 = countDownLatch;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.http.QqKernel.lambda$scheduleAsyncBulletinDeliver$14(this.f$0, this.f$1);
    }
}
