package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda49 implements java.lang.Runnable {
    public final /* synthetic */ android.widget.TextView f$0;
    public final /* synthetic */ java.lang.String f$1;
    public final /* synthetic */ android.widget.ProgressBar f$2;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda49(android.widget.TextView textView, java.lang.String str, android.widget.ProgressBar progressBar) {
        this.f$0 = textView;
        this.f$1 = str;
        this.f$2 = progressBar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.SettingsInjector.lambda$startAudioDownload$110(this.f$0, this.f$1, this.f$2);
    }
}
