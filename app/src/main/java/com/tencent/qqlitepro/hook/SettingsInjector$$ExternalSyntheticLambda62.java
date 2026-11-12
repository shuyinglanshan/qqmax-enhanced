package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda62 implements java.lang.Runnable {
    public final /* synthetic */ android.content.Context f$0;
    public final /* synthetic */ android.widget.TextView f$1;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda62(android.content.Context context, android.widget.TextView textView) {
        this.f$0 = context;
        this.f$1 = textView;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.SettingsInjector.lambda$buildFileSend$89(this.f$0, this.f$1);
    }
}
