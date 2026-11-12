package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class UiPatcher$$ExternalSyntheticLambda23 implements com.tencent.qqlitepro.http.FakeVoice.RestoreProgress {
    public final /* synthetic */ android.os.Handler f$0;
    public final /* synthetic */ android.widget.ProgressBar f$1;
    public final /* synthetic */ android.widget.TextView f$2;

    public /* synthetic */ UiPatcher$$ExternalSyntheticLambda23(android.os.Handler handler, android.widget.ProgressBar progressBar, android.widget.TextView textView) {
        this.f$0 = handler;
        this.f$1 = progressBar;
        this.f$2 = textView;
    }

    @Override // com.tencent.qqlitepro.http.FakeVoice.RestoreProgress
    public final void onProgress(int i, java.lang.String str, long j, long j2) {
        this.f$0.post(new com.tencent.qqlitepro.hook.UiPatcher$$ExternalSyntheticLambda27(this.f$1, i, this.f$2, str));
    }
}
