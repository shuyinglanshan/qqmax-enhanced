package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class UiPatcher$$ExternalSyntheticLambda17 implements java.lang.Runnable {
    public final /* synthetic */ java.lang.Runnable f$0;

    public /* synthetic */ UiPatcher$$ExternalSyntheticLambda17(java.lang.Runnable runnable) {
        this.f$0 = runnable;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.UiPatcher.SHARD_COLLECT_EXEC.execute(this.f$0);
    }
}
