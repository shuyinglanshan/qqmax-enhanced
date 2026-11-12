package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class ForceNoticeInjector$$ExternalSyntheticLambda15 implements java.lang.Runnable {
    public final /* synthetic */ java.lang.String f$0;
    public final /* synthetic */ android.app.Dialog f$1;

    public /* synthetic */ ForceNoticeInjector$$ExternalSyntheticLambda15(java.lang.String str, android.app.Dialog dialog) {
        this.f$0 = str;
        this.f$1 = dialog;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.ForceNoticeInjector.lambda$show$5(this.f$0, this.f$1);
    }
}
