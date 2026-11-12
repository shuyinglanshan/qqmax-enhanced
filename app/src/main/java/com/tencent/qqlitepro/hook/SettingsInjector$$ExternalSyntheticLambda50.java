package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda50 implements java.lang.Runnable {
    public final /* synthetic */ android.widget.EditText f$0;
    public final /* synthetic */ android.widget.EditText f$1;
    public final /* synthetic */ android.widget.EditText f$2;
    public final /* synthetic */ boolean f$3;
    public final /* synthetic */ java.io.File f$4;
    public final /* synthetic */ android.view.ViewGroup f$5;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda50(android.widget.EditText editText, android.widget.EditText editText2, android.widget.EditText editText3, boolean z, java.io.File file, android.view.ViewGroup viewGroup) {
        this.f$0 = editText;
        this.f$1 = editText2;
        this.f$2 = editText3;
        this.f$3 = z;
        this.f$4 = file;
        this.f$5 = viewGroup;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.SettingsInjector.lambda$mailPickedRow$177(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
    }
}
