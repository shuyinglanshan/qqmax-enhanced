package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$4$$ExternalSyntheticLambda0 implements java.lang.Runnable {
    public final /* synthetic */ android.app.Activity f$0;

    public /* synthetic */ SettingsInjector$4$$ExternalSyntheticLambda0(android.app.Activity activity) {
        this.f$0 = activity;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.SettingsInjector.showModuleCategory(this.f$0);
    }
}
