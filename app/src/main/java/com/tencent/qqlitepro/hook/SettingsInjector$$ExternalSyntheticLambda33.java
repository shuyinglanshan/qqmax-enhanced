package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda33 implements java.lang.Runnable {
    public final /* synthetic */ java.lang.String f$0;
    public final /* synthetic */ java.lang.String f$1;
    public final /* synthetic */ android.widget.EditText f$2;
    public final /* synthetic */ android.widget.TextView f$3;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda33(java.lang.String str, java.lang.String str2, android.widget.EditText editText, android.widget.TextView textView) {
        this.f$0 = str;
        this.f$1 = str2;
        this.f$2 = editText;
        this.f$3 = textView;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.SettingsInjector.lambda$buildSummary$54(this.f$0, this.f$1, this.f$2, this.f$3);
    }
}
