package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda30 implements java.lang.Runnable {
    public final /* synthetic */ android.content.Context f$0;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda30(android.content.Context context) {
        this.f$0 = context;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.SettingsInjector.openModuleSettings(this.f$0);
    }
}
