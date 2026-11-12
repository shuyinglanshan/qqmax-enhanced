package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda216 implements java.lang.Runnable {
    public final /* synthetic */ android.content.Context f$0;
    public final /* synthetic */ java.io.File f$1;
    public final /* synthetic */ com.tencent.qqlitepro.hook.SettingsInjector.PluginInfo f$2;
    public final /* synthetic */ android.app.Dialog f$3;
    public final /* synthetic */ java.lang.Runnable f$4;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda216(android.content.Context context, java.io.File file, com.tencent.qqlitepro.hook.SettingsInjector.PluginInfo pluginInfo, android.app.Dialog dialog, java.lang.Runnable runnable) {
        this.f$0 = context;
        this.f$1 = file;
        this.f$2 = pluginInfo;
        this.f$3 = dialog;
        this.f$4 = runnable;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.SettingsInjector.lambda$confirmDeletePlugin$195(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4);
    }
}
