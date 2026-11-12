package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda187 implements java.lang.Runnable {
    public final /* synthetic */ long f$0;
    public final /* synthetic */ android.widget.ProgressBar f$1;
    public final /* synthetic */ long f$2;
    public final /* synthetic */ android.widget.TextView f$3;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda187(long j, android.widget.ProgressBar progressBar, long j2, android.widget.TextView textView) {
        this.f$0 = j;
        this.f$1 = progressBar;
        this.f$2 = j2;
        this.f$3 = textView;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.SettingsInjector.lambda$downloadAndUpdate$70(this.f$0, this.f$1, this.f$2, this.f$3);
    }
}
