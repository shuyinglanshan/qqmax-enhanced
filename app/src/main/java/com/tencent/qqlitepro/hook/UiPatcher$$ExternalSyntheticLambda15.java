package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class UiPatcher$$ExternalSyntheticLambda15 implements java.lang.Runnable {
    public final /* synthetic */ boolean f$0;
    public final /* synthetic */ java.lang.String f$1;

    public /* synthetic */ UiPatcher$$ExternalSyntheticLambda15(boolean z, java.lang.String str) {
        this.f$0 = z;
        this.f$1 = str;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.UiPatcher.lambda$scheduleShardCollect$24(this.f$0, this.f$1);
    }
}
