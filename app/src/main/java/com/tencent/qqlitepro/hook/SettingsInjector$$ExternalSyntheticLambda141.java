package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda141 implements java.lang.Runnable {
    public final /* synthetic */ java.lang.String f$0;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda141(java.lang.String str) {
        this.f$0 = str;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.SettingsInjector.appendLog(com.tencent.qqlitepro.hook.SettingsInjector.sFileLog, "[选择] 失败：" + this.f$0);
    }
}
