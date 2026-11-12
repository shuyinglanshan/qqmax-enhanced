package com.tencent.qqlitepro.plugin;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class PluginEngine$$ExternalSyntheticLambda0 implements java.lang.Runnable {
    public final /* synthetic */ com.tencent.qqlitepro.plugin.PluginEngine.Feature f$0;
    public final /* synthetic */ com.tencent.qqlitepro.plugin.PluginEngine.Plugin f$1;
    public final /* synthetic */ java.lang.Runnable f$2;

    public /* synthetic */ PluginEngine$$ExternalSyntheticLambda0(com.tencent.qqlitepro.plugin.PluginEngine.Feature feature, com.tencent.qqlitepro.plugin.PluginEngine.Plugin plugin, java.lang.Runnable runnable) {
        this.f$0 = feature;
        this.f$1 = plugin;
        this.f$2 = runnable;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.plugin.PluginEngine.lambda$runFeature$4(this.f$0, this.f$1, this.f$2);
    }
}
