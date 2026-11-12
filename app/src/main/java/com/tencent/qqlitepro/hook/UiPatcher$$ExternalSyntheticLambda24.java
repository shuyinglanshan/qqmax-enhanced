package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class UiPatcher$$ExternalSyntheticLambda24 implements java.lang.Runnable {
    public final /* synthetic */ android.widget.ProgressBar f$0;
    public final /* synthetic */ android.widget.TextView f$1;
    public final /* synthetic */ java.lang.Throwable f$2;

    public /* synthetic */ UiPatcher$$ExternalSyntheticLambda24(android.widget.ProgressBar progressBar, android.widget.TextView textView, java.lang.Throwable th) {
        this.f$0 = progressBar;
        this.f$1 = textView;
        this.f$2 = th;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.UiPatcher.lambda$showFakeFileInfoDialog$16(this.f$0, this.f$1, this.f$2);
    }
}
