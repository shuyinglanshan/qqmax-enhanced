package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda194 implements java.lang.Runnable {
    public final /* synthetic */ java.util.List f$0;
    public final /* synthetic */ java.io.File f$1;
    public final /* synthetic */ com.tencent.qqlitepro.hook.SettingsInjector.PluginInfo f$2;
    public final /* synthetic */ android.content.Context f$3;
    public final /* synthetic */ android.app.Dialog f$4;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda194(java.util.List list, java.io.File file, com.tencent.qqlitepro.hook.SettingsInjector.PluginInfo pluginInfo, android.content.Context context, android.app.Dialog dialog) {
        this.f$0 = list;
        this.f$1 = file;
        this.f$2 = pluginInfo;
        this.f$3 = context;
        this.f$4 = dialog;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.SettingsInjector.lambda$pluginSettingsStructured$198(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4);
    }
}
