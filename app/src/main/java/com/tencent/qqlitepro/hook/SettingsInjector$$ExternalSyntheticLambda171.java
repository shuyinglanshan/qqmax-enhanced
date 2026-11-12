package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda171 implements java.lang.Runnable {
    public final /* synthetic */ java.io.File f$0;
    public final /* synthetic */ java.lang.String f$1;
    public final /* synthetic */ android.widget.ProgressBar f$2;
    public final /* synthetic */ android.widget.TextView f$3;
    public final /* synthetic */ android.app.AlertDialog f$4;
    public final /* synthetic */ android.content.Context f$5;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda171(java.io.File file, java.lang.String str, android.widget.ProgressBar progressBar, android.widget.TextView textView, android.app.AlertDialog alertDialog, android.content.Context context) {
        this.f$0 = file;
        this.f$1 = str;
        this.f$2 = progressBar;
        this.f$3 = textView;
        this.f$4 = alertDialog;
        this.f$5 = context;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.SettingsInjector.lambda$downloadPluginForNotice$80(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
    }
}
