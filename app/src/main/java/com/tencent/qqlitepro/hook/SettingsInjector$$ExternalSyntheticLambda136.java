package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda136 implements java.lang.Runnable {
    public final /* synthetic */ android.content.Context f$0;
    public final /* synthetic */ android.os.Handler f$1;
    public final /* synthetic */ android.view.ViewGroup f$2;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda136(android.content.Context context, android.os.Handler handler, android.view.ViewGroup viewGroup) {
        this.f$0 = context;
        this.f$1 = handler;
        this.f$2 = viewGroup;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.SettingsInjector.lambda$buildMailInbox$146(this.f$0, this.f$1, this.f$2);
    }
}
