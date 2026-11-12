package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class UiPatcher$$ExternalSyntheticLambda25 implements java.lang.Runnable {
    public final /* synthetic */ java.io.File f$0;
    public final /* synthetic */ android.widget.ProgressBar f$1;
    public final /* synthetic */ android.widget.TextView f$2;
    public final /* synthetic */ android.widget.TextView f$3;
    public final /* synthetic */ android.app.Dialog f$4;
    public final /* synthetic */ boolean[] f$5;

    public /* synthetic */ UiPatcher$$ExternalSyntheticLambda25(java.io.File file, android.widget.ProgressBar progressBar, android.widget.TextView textView, android.widget.TextView textView2, android.app.Dialog dialog, boolean[] zArr) {
        this.f$0 = file;
        this.f$1 = progressBar;
        this.f$2 = textView;
        this.f$3 = textView2;
        this.f$4 = dialog;
        this.f$5 = zArr;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.UiPatcher.lambda$showFakeFileInfoDialog$18(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
    }
}
