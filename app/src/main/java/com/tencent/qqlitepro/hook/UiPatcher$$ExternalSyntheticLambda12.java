package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class UiPatcher$$ExternalSyntheticLambda12 implements android.view.View.OnClickListener {
    public final /* synthetic */ java.io.File f$0;
    public final /* synthetic */ android.widget.TextView f$1;
    public final /* synthetic */ android.widget.TextView f$2;
    public final /* synthetic */ android.view.View.OnClickListener f$3;

    public /* synthetic */ UiPatcher$$ExternalSyntheticLambda12(java.io.File file, android.widget.TextView textView, android.widget.TextView textView2, android.view.View.OnClickListener onClickListener) {
        this.f$0 = file;
        this.f$1 = textView;
        this.f$2 = textView2;
        this.f$3 = onClickListener;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(android.view.View view) {
        com.tencent.qqlitepro.hook.UiPatcher.lambda$showFakeFileInfoDialog$21(this.f$0, this.f$1, this.f$2, this.f$3, view);
    }
}
