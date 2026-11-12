package com.tencent.qqlitepro.http;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class QqKernel$$ExternalSyntheticLambda13 implements java.lang.Runnable {
    public final /* synthetic */ long f$0;
    public final /* synthetic */ java.util.ArrayList f$1;

    public /* synthetic */ QqKernel$$ExternalSyntheticLambda13(long j, java.util.ArrayList arrayList) {
        this.f$0 = j;
        this.f$1 = arrayList;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.http.QqKernel.lambda$scheduleAsyncBulletinDeliver$13(this.f$0, this.f$1);
    }
}
