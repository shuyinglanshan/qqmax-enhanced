package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda215 implements java.lang.Runnable {
    public final /* synthetic */ java.lang.String f$0;
    public final /* synthetic */ java.io.File f$1;
    public final /* synthetic */ android.os.Handler f$2;
    public final /* synthetic */ android.widget.ProgressBar f$3;
    public final /* synthetic */ android.widget.TextView f$4;
    public final /* synthetic */ android.content.Context f$5;
    public final /* synthetic */ java.lang.Runnable f$6;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda215(java.lang.String str, java.io.File file, android.os.Handler handler, android.widget.ProgressBar progressBar, android.widget.TextView textView, android.content.Context context, java.lang.Runnable runnable) {
        this.f$0 = str;
        this.f$1 = file;
        this.f$2 = handler;
        this.f$3 = progressBar;
        this.f$4 = textView;
        this.f$5 = context;
        this.f$6 = runnable;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.SettingsInjector.lambda$startAudioDownload$111(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6);
    }
}
