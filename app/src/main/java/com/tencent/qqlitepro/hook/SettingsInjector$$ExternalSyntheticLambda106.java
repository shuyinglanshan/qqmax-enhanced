package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda106 implements android.widget.CompoundButton.OnCheckedChangeListener {
    public final /* synthetic */ android.content.Context f$0;
    public final /* synthetic */ java.io.File f$1;
    public final /* synthetic */ android.widget.TextView f$2;
    public final /* synthetic */ com.tencent.qqlitepro.hook.SettingsInjector.PluginInfo f$3;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda106(android.content.Context context, java.io.File file, android.widget.TextView textView, com.tencent.qqlitepro.hook.SettingsInjector.PluginInfo pluginInfo) {
        this.f$0 = context;
        this.f$1 = file;
        this.f$2 = textView;
        this.f$3 = pluginInfo;
    }

    @Override // android.widget.CompoundButton.OnCheckedChangeListener
    public final void onCheckedChanged(android.widget.CompoundButton compoundButton, boolean z) {
        com.tencent.qqlitepro.hook.SettingsInjector.lambda$pluginCardRow$190(this.f$0, this.f$1, this.f$2, this.f$3, compoundButton, z);
    }
}
