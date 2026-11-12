package com.tencent.qqlitepro.plugin;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class PluginAPI$$ExternalSyntheticLambda0 implements java.util.concurrent.Callable {
    public final /* synthetic */ com.tencent.qqlitepro.plugin.PluginAPI f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ java.util.List f$2;
    public final /* synthetic */ android.os.Handler f$3;
    public final /* synthetic */ boolean f$4;
    public final /* synthetic */ java.lang.Object f$5;
    public final /* synthetic */ int f$6;

    public /* synthetic */ PluginAPI$$ExternalSyntheticLambda0(com.tencent.qqlitepro.plugin.PluginAPI pluginAPI, int i, java.util.List list, android.os.Handler handler, boolean z, java.lang.Object obj, int i2) {
        this.f$0 = pluginAPI;
        this.f$1 = i;
        this.f$2 = list;
        this.f$3 = handler;
        this.f$4 = z;
        this.f$5 = obj;
        this.f$6 = i2;
    }

    @Override // java.util.concurrent.Callable
    public final java.lang.Object call() throws java.lang.Exception {
        return this.f$0.lambda$sendFile$0(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6);
    }
}
