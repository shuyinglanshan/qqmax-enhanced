package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class ForceNoticeInjector$$ExternalSyntheticLambda4 implements java.lang.Runnable {
    public final /* synthetic */ android.content.Context f$0;
    public final /* synthetic */ java.lang.String f$1;
    public final /* synthetic */ android.app.Dialog f$2;

    public /* synthetic */ ForceNoticeInjector$$ExternalSyntheticLambda4(android.content.Context context, java.lang.String str, android.app.Dialog dialog) {
        this.f$0 = context;
        this.f$1 = str;
        this.f$2 = dialog;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.ForceNoticeInjector.lambda$show$6(this.f$0, this.f$1, this.f$2);
    }
}
