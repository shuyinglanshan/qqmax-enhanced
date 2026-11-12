package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class UiPatcher$$ExternalSyntheticLambda7 implements android.content.DialogInterface.OnDismissListener {
    public final /* synthetic */ java.lang.String f$0;

    public /* synthetic */ UiPatcher$$ExternalSyntheticLambda7(java.lang.String str) {
        this.f$0 = str;
    }

    @Override // android.content.DialogInterface.OnDismissListener
    public final void onDismiss(android.content.DialogInterface dialogInterface) {
        com.tencent.qqlitepro.hook.UiPatcher.lambda$showFakeFileInfoDialog$23(this.f$0, dialogInterface);
    }
}
