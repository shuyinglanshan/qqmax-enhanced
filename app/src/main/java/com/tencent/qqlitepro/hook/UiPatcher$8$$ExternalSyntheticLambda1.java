package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class UiPatcher$8$$ExternalSyntheticLambda1 implements java.lang.Runnable {
    public final /* synthetic */ java.lang.String f$0;

    public /* synthetic */ UiPatcher$8$$ExternalSyntheticLambda1(java.lang.String str) {
        this.f$0 = str;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.UiPatcher.openEditByMsgId(this.f$0);
    }
}
