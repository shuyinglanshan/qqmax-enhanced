package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class UiPatcher$$ExternalSyntheticLambda5 implements android.view.View.OnClickListener {
    public final /* synthetic */ boolean[] f$0;
    public final /* synthetic */ android.widget.TextView f$1;
    public final /* synthetic */ android.widget.ProgressBar f$2;
    public final /* synthetic */ android.widget.TextView f$3;
    public final /* synthetic */ com.tencent.qqlitepro.hook.UiPatcher.FileInfoUI f$4;
    public final /* synthetic */ android.os.Handler f$5;
    public final /* synthetic */ android.app.Dialog f$6;

    public /* synthetic */ UiPatcher$$ExternalSyntheticLambda5(boolean[] zArr, android.widget.TextView textView, android.widget.ProgressBar progressBar, android.widget.TextView textView2, com.tencent.qqlitepro.hook.UiPatcher.FileInfoUI fileInfoUI, android.os.Handler handler, android.app.Dialog dialog) {
        this.f$0 = zArr;
        this.f$1 = textView;
        this.f$2 = progressBar;
        this.f$3 = textView2;
        this.f$4 = fileInfoUI;
        this.f$5 = handler;
        this.f$6 = dialog;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(android.view.View view) {
        com.tencent.qqlitepro.hook.UiPatcher.lambda$showFakeFileInfoDialog$20(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, view);
    }
}
