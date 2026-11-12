package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda23 implements java.lang.Runnable {
    public final /* synthetic */ android.widget.TextView f$0;
    public final /* synthetic */ android.content.Context f$1;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda23(android.widget.TextView textView, android.content.Context context) {
        this.f$0 = textView;
        this.f$1 = context;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.SettingsInjector.lambda$buildFileSend$88(this.f$0, this.f$1);
    }
}
