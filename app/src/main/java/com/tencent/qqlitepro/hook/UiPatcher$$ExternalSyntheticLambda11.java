package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class UiPatcher$$ExternalSyntheticLambda11 implements java.lang.Runnable {
    public final /* synthetic */ com.tencent.qqlitepro.hook.UiPatcher.FileInfoUI f$0;
    public final /* synthetic */ android.os.Handler f$1;
    public final /* synthetic */ android.widget.ProgressBar f$2;
    public final /* synthetic */ android.widget.TextView f$3;
    public final /* synthetic */ android.widget.TextView f$4;
    public final /* synthetic */ android.app.Dialog f$5;
    public final /* synthetic */ boolean[] f$6;

    public /* synthetic */ UiPatcher$$ExternalSyntheticLambda11(com.tencent.qqlitepro.hook.UiPatcher.FileInfoUI fileInfoUI, android.os.Handler handler, android.widget.ProgressBar progressBar, android.widget.TextView textView, android.widget.TextView textView2, android.app.Dialog dialog, boolean[] zArr) {
        this.f$0 = fileInfoUI;
        this.f$1 = handler;
        this.f$2 = progressBar;
        this.f$3 = textView;
        this.f$4 = textView2;
        this.f$5 = dialog;
        this.f$6 = zArr;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.UiPatcher.lambda$showFakeFileInfoDialog$19(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6);
    }
}
