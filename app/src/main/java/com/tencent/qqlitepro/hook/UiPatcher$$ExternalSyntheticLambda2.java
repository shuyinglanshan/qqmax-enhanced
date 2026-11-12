package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class UiPatcher$$ExternalSyntheticLambda2 implements java.lang.Runnable {
    public final /* synthetic */ android.view.ViewGroup f$0;
    public final /* synthetic */ java.util.List f$1;

    public /* synthetic */ UiPatcher$$ExternalSyntheticLambda2(android.view.ViewGroup viewGroup, java.util.List list) {
        this.f$0 = viewGroup;
        this.f$1 = list;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.UiPatcher.lambda$injectFileDetailRow$31(this.f$0, this.f$1);
    }
}
