package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda226 implements java.lang.Runnable {
    public final /* synthetic */ android.content.Context f$0;
    public final /* synthetic */ android.widget.EditText f$1;
    public final /* synthetic */ android.widget.TextView f$2;
    public final /* synthetic */ android.widget.ProgressBar f$3;
    public final /* synthetic */ java.lang.Runnable f$4;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda226(android.content.Context context, android.widget.EditText editText, android.widget.TextView textView, android.widget.ProgressBar progressBar, java.lang.Runnable runnable) {
        this.f$0 = context;
        this.f$1 = editText;
        this.f$2 = textView;
        this.f$3 = progressBar;
        this.f$4 = runnable;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.SettingsInjector.startAudioDownload(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4);
    }
}
