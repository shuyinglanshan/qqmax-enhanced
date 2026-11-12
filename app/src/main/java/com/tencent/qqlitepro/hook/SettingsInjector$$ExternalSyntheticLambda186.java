package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda186 implements java.lang.Runnable {
    public final /* synthetic */ java.lang.String f$0;
    public final /* synthetic */ java.lang.String f$1;
    public final /* synthetic */ android.widget.ProgressBar f$2;
    public final /* synthetic */ android.widget.TextView f$3;
    public final /* synthetic */ android.app.AlertDialog f$4;
    public final /* synthetic */ android.content.Context f$5;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda186(java.lang.String str, java.lang.String str2, android.widget.ProgressBar progressBar, android.widget.TextView textView, android.app.AlertDialog alertDialog, android.content.Context context) {
        this.f$0 = str;
        this.f$1 = str2;
        this.f$2 = progressBar;
        this.f$3 = textView;
        this.f$4 = alertDialog;
        this.f$5 = context;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.SettingsInjector.lambda$downloadAndUpdate$73(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
    }
}
