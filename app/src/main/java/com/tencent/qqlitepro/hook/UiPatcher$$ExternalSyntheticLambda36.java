package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class UiPatcher$$ExternalSyntheticLambda36 implements android.content.DialogInterface.OnClickListener {
    public final /* synthetic */ android.view.View f$0;
    public final /* synthetic */ java.lang.String f$1;

    public /* synthetic */ UiPatcher$$ExternalSyntheticLambda36(android.view.View view, java.lang.String str) {
        this.f$0 = view;
        this.f$1 = str;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(android.content.DialogInterface dialogInterface, int i) {
        com.tencent.qqlitepro.hook.UiPatcher.showEditDialog(this.f$0, this.f$1);
    }
}
