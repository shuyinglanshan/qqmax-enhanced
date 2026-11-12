package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda44 implements java.lang.Runnable {
    public final /* synthetic */ java.lang.String f$0;
    public final /* synthetic */ java.lang.Throwable f$1;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda44(java.lang.String str, java.lang.Throwable th) {
        this.f$0 = str;
        this.f$1 = th;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.SettingsInjector.appendLog(com.tencent.qqlitepro.hook.SettingsInjector.sVoiceLog, "[发送] -> " + this.f$0 + " 失败：" + this.f$1);
    }
}
