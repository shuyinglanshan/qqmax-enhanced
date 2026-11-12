package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda18 implements java.lang.Runnable {
    public final /* synthetic */ android.widget.EditText f$0;
    public final /* synthetic */ android.content.Context f$1;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda18(android.widget.EditText editText, android.content.Context context) {
        this.f$0 = editText;
        this.f$1 = context;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.SettingsInjector.lambda$buildQqDynamic$125(this.f$0, this.f$1);
    }
}
