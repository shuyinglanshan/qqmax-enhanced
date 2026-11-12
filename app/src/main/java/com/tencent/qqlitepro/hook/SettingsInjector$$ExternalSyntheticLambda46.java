package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda46 implements java.lang.Runnable {
    public final /* synthetic */ java.lang.Throwable f$0;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda46(java.lang.Throwable th) {
        this.f$0 = th;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.SettingsInjector.appendLog(com.tencent.qqlitepro.hook.SettingsInjector.sPluginLog, "[导入] 失败: " + this.f$0);
    }
}
