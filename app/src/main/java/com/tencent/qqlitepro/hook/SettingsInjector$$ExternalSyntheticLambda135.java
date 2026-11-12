package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda135 implements java.lang.Runnable {
    public final /* synthetic */ android.widget.EditText f$0;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda135(android.widget.EditText editText) {
        this.f$0 = editText;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.SettingsInjector.insertAtToken(this.f$0, com.tencent.qqlitepro.http.QqKernel.AT_ALL_TOKEN);
    }
}
