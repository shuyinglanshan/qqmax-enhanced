package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda107 implements android.view.View.OnClickListener {
    public final /* synthetic */ android.content.Context f$0;
    public final /* synthetic */ java.io.File f$1;
    public final /* synthetic */ com.tencent.qqlitepro.hook.SettingsInjector.PluginInfo f$2;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda107(android.content.Context context, java.io.File file, com.tencent.qqlitepro.hook.SettingsInjector.PluginInfo pluginInfo) {
        this.f$0 = context;
        this.f$1 = file;
        this.f$2 = pluginInfo;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(android.view.View view) {
        com.tencent.qqlitepro.hook.SettingsInjector.pluginDetailDialog(this.f$0, this.f$1, this.f$2);
    }
}
