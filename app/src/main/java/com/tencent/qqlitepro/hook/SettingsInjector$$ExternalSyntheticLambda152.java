package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda152 implements java.lang.Runnable {
    public final /* synthetic */ android.widget.EditText f$0;
    public final /* synthetic */ android.widget.EditText f$1;
    public final /* synthetic */ android.widget.TextView f$2;
    public final /* synthetic */ android.content.Context f$3;
    public final /* synthetic */ android.widget.EditText f$4;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda152(android.widget.EditText editText, android.widget.EditText editText2, android.widget.TextView textView, android.content.Context context, android.widget.EditText editText3) {
        this.f$0 = editText;
        this.f$1 = editText2;
        this.f$2 = textView;
        this.f$3 = context;
        this.f$4 = editText3;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.SettingsInjector.lambda$buildSummary$55(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4);
    }
}
