package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda48 implements java.lang.Runnable {
    public final /* synthetic */ android.widget.ProgressBar f$0;
    public final /* synthetic */ android.widget.TextView f$1;
    public final /* synthetic */ java.lang.String f$2;
    public final /* synthetic */ java.lang.String f$3;
    public final /* synthetic */ android.content.Context f$4;
    public final /* synthetic */ java.lang.Runnable f$5;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda48(android.widget.ProgressBar progressBar, android.widget.TextView textView, java.lang.String str, java.lang.String str2, android.content.Context context, java.lang.Runnable runnable) {
        this.f$0 = progressBar;
        this.f$1 = textView;
        this.f$2 = str;
        this.f$3 = str2;
        this.f$4 = context;
        this.f$5 = runnable;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.SettingsInjector.lambda$startAudioDownload$109(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
    }
}
