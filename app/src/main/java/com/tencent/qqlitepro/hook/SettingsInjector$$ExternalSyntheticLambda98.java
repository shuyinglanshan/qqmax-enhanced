package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda98 implements java.lang.Runnable {
    public final /* synthetic */ android.widget.EditText f$0;
    public final /* synthetic */ android.widget.EditText f$1;
    public final /* synthetic */ android.widget.EditText[] f$2;
    public final /* synthetic */ android.view.ViewGroup f$3;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda98(android.widget.EditText editText, android.widget.EditText editText2, android.widget.EditText[] editTextArr, android.view.ViewGroup viewGroup) {
        this.f$0 = editText;
        this.f$1 = editText2;
        this.f$2 = editTextArr;
        this.f$3 = viewGroup;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.SettingsInjector.lambda$buildMailSend$166(this.f$0, this.f$1, this.f$2, this.f$3);
    }
}
