package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class UiPatcher$$ExternalSyntheticLambda27 implements java.lang.Runnable {
    public final /* synthetic */ android.widget.ProgressBar f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ android.widget.TextView f$2;
    public final /* synthetic */ java.lang.String f$3;

    public /* synthetic */ UiPatcher$$ExternalSyntheticLambda27(android.widget.ProgressBar progressBar, int i, android.widget.TextView textView, java.lang.String str) {
        this.f$0 = progressBar;
        this.f$1 = i;
        this.f$2 = textView;
        this.f$3 = str;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.UiPatcher.lambda$showFakeFileInfoDialog$14(this.f$0, this.f$1, this.f$2, this.f$3);
    }
}
