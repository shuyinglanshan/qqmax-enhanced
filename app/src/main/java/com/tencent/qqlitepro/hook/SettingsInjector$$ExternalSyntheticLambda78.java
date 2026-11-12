package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda78 implements android.widget.CompoundButton.OnCheckedChangeListener {
    public final /* synthetic */ android.content.Context f$0;
    public final /* synthetic */ com.tencent.qqlitepro.plugin.PluginEngine.Feature f$1;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda78(android.content.Context context, com.tencent.qqlitepro.plugin.PluginEngine.Feature feature) {
        this.f$0 = context;
        this.f$1 = feature;
    }

    @Override // android.widget.CompoundButton.OnCheckedChangeListener
    public final void onCheckedChanged(android.widget.CompoundButton compoundButton, boolean z) {
        com.tencent.qqlitepro.plugin.PluginEngine.setFeatureEnabled(this.f$0, this.f$1, z);
    }
}
