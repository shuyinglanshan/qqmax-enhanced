package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda145 implements java.lang.Runnable {
    public final /* synthetic */ android.content.Context f$0;
    public final /* synthetic */ android.widget.EditText f$1;
    public final /* synthetic */ android.widget.EditText f$2;
    public final /* synthetic */ android.widget.EditText f$3;
    public final /* synthetic */ android.widget.EditText f$4;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda145(android.content.Context context, android.widget.EditText editText, android.widget.EditText editText2, android.widget.EditText editText3, android.widget.EditText editText4) {
        this.f$0 = context;
        this.f$1 = editText;
        this.f$2 = editText2;
        this.f$3 = editText3;
        this.f$4 = editText4;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.SettingsInjector.lambda$buildKeywordReply$43(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4);
    }
}
