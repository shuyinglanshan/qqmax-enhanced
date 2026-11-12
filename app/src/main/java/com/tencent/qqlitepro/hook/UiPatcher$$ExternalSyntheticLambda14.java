package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class UiPatcher$$ExternalSyntheticLambda14 implements java.lang.Runnable {
    public final /* synthetic */ android.view.View f$0;
    public final /* synthetic */ java.lang.String f$1;
    public final /* synthetic */ java.util.List f$2;

    public /* synthetic */ UiPatcher$$ExternalSyntheticLambda14(android.view.View view, java.lang.String str, java.util.List list) {
        this.f$0 = view;
        this.f$1 = str;
        this.f$2 = list;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.UiPatcher.lambda$injectFileDetailRow$32(this.f$0, this.f$1, this.f$2);
    }
}
