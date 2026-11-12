package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda132 implements java.lang.Runnable {
    public final /* synthetic */ android.widget.TextView f$0;
    public final /* synthetic */ java.lang.String f$1;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda132(android.widget.TextView textView, java.lang.String str) {
        this.f$0 = textView;
        this.f$1 = str;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.SettingsInjector.lambda$appendLog$121(this.f$0, this.f$1);
    }
}
