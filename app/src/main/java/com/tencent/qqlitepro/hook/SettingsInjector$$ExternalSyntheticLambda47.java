package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda47 implements java.lang.Runnable {
    public final /* synthetic */ long f$0;
    public final /* synthetic */ long f$1;
    public final /* synthetic */ android.widget.ProgressBar f$2;
    public final /* synthetic */ android.widget.TextView f$3;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda47(long j, long j2, android.widget.ProgressBar progressBar, android.widget.TextView textView) {
        this.f$0 = j;
        this.f$1 = j2;
        this.f$2 = progressBar;
        this.f$3 = textView;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.SettingsInjector.lambda$startAudioDownload$107(this.f$0, this.f$1, this.f$2, this.f$3);
    }
}
