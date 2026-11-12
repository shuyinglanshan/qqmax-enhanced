package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class AddFriendVerifyHook$$ExternalSyntheticLambda1 implements android.view.View.OnClickListener {
    public final /* synthetic */ android.widget.EditText f$0;
    public final /* synthetic */ android.app.Activity f$1;
    public final /* synthetic */ long f$2;
    public final /* synthetic */ android.app.Dialog f$3;

    public /* synthetic */ AddFriendVerifyHook$$ExternalSyntheticLambda1(android.widget.EditText editText, android.app.Activity activity, long j, android.app.Dialog dialog) {
        this.f$0 = editText;
        this.f$1 = activity;
        this.f$2 = j;
        this.f$3 = dialog;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(android.view.View view) {
        android.widget.EditText editText = this.f$0;
        com.tencent.qqlitepro.hook.AddFriendVerifyHook.onSend(this.f$1, this.f$2, editText.getText() == null ? "" : editText.getText().toString().trim(), this.f$3);
    }
}
