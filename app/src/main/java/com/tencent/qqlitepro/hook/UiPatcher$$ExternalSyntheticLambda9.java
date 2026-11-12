package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class UiPatcher$$ExternalSyntheticLambda9 implements java.lang.Runnable {
    public final /* synthetic */ java.lang.Object f$0;
    public final /* synthetic */ android.view.View f$1;
    public final /* synthetic */ java.lang.Object f$2;
    public final /* synthetic */ java.lang.String f$3;
    public final /* synthetic */ java.lang.Object f$4;

    public /* synthetic */ UiPatcher$$ExternalSyntheticLambda9(java.lang.Object obj, android.view.View view, java.lang.Object obj2, java.lang.String str, java.lang.Object obj3) {
        this.f$0 = obj;
        this.f$1 = view;
        this.f$2 = obj2;
        this.f$3 = str;
        this.f$4 = obj3;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.UiPatcher.lambda$ensureReceiverMedia$27(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4);
    }
}
