package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class UiPatcher$$ExternalSyntheticLambda18 implements java.lang.Runnable {
    public final /* synthetic */ java.lang.String f$0;
    public final /* synthetic */ java.lang.String f$1;
    public final /* synthetic */ android.view.View f$2;
    public final /* synthetic */ java.lang.Object f$3;
    public final /* synthetic */ int f$4;

    public /* synthetic */ UiPatcher$$ExternalSyntheticLambda18(java.lang.String str, java.lang.String str2, android.view.View view, java.lang.Object obj, int i) {
        this.f$0 = str;
        this.f$1 = str2;
        this.f$2 = view;
        this.f$3 = obj;
        this.f$4 = i;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.UiPatcher.lambda$retryTipIfMissing$3(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4);
    }
}
