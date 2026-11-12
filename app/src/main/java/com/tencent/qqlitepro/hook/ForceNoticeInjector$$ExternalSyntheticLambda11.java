package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class ForceNoticeInjector$$ExternalSyntheticLambda11 implements android.view.View.OnClickListener {
    public final /* synthetic */ java.lang.String f$0;
    public final /* synthetic */ android.app.Dialog f$1;

    public /* synthetic */ ForceNoticeInjector$$ExternalSyntheticLambda11(java.lang.String str, android.app.Dialog dialog) {
        this.f$0 = str;
        this.f$1 = dialog;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(android.view.View view) {
        com.tencent.qqlitepro.hook.ForceNoticeInjector.lambda$fillNodeList$9(this.f$0, this.f$1, view);
    }
}
