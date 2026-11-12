package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class UiPatcher$$ExternalSyntheticLambda21 implements android.view.View.OnClickListener {
    public final /* synthetic */ android.widget.EditText f$0;
    public final /* synthetic */ java.lang.String f$1;
    public final /* synthetic */ android.view.View f$2;
    public final /* synthetic */ android.app.Dialog f$3;

    public /* synthetic */ UiPatcher$$ExternalSyntheticLambda21(android.widget.EditText editText, java.lang.String str, android.view.View view, android.app.Dialog dialog) {
        this.f$0 = editText;
        this.f$1 = str;
        this.f$2 = view;
        this.f$3 = dialog;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(android.view.View view) {
        com.tencent.qqlitepro.hook.UiPatcher.lambda$showEditDialog2$6(this.f$0, this.f$1, this.f$2, this.f$3, view);
    }
}
