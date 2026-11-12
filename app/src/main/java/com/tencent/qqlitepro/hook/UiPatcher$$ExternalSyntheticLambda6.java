package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class UiPatcher$$ExternalSyntheticLambda6 implements android.view.View.OnClickListener {
    public final /* synthetic */ boolean[] f$0;
    public final /* synthetic */ com.tencent.qqlitepro.hook.UiPatcher.FileInfoUI f$1;
    public final /* synthetic */ android.widget.ProgressBar f$2;
    public final /* synthetic */ android.widget.TextView f$3;
    public final /* synthetic */ android.widget.TextView f$4;
    public final /* synthetic */ android.view.View.OnClickListener f$5;

    public /* synthetic */ UiPatcher$$ExternalSyntheticLambda6(boolean[] zArr, com.tencent.qqlitepro.hook.UiPatcher.FileInfoUI fileInfoUI, android.widget.ProgressBar progressBar, android.widget.TextView textView, android.widget.TextView textView2, android.view.View.OnClickListener onClickListener) {
        this.f$0 = zArr;
        this.f$1 = fileInfoUI;
        this.f$2 = progressBar;
        this.f$3 = textView;
        this.f$4 = textView2;
        this.f$5 = onClickListener;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(android.view.View view) {
        com.tencent.qqlitepro.hook.UiPatcher.lambda$showFakeFileInfoDialog$22(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, view);
    }
}
