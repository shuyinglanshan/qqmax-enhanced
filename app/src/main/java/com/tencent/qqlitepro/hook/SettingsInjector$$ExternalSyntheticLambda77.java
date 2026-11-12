package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda77 implements android.view.View.OnClickListener {
    public final /* synthetic */ boolean f$0;
    public final /* synthetic */ android.content.Context f$1;
    public final /* synthetic */ com.tencent.qqlitepro.plugin.PluginEngine.Feature f$2;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda77(boolean z, android.content.Context context, com.tencent.qqlitepro.plugin.PluginEngine.Feature feature) {
        this.f$0 = z;
        this.f$1 = context;
        this.f$2 = feature;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(android.view.View view) {
        com.tencent.qqlitepro.hook.SettingsInjector.lambda$featureCardRow$182(this.f$0, this.f$1, this.f$2, view);
    }
}
