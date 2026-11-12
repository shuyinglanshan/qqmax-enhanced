package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda43 implements java.lang.Runnable {
    public final /* synthetic */ java.lang.String f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ java.lang.String f$2;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda43(java.lang.String str, int i, java.lang.String str2) {
        this.f$0 = str;
        this.f$1 = i;
        this.f$2 = str2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.SettingsInjector.appendLog(com.tencent.qqlitepro.hook.SettingsInjector.sVoiceLog, "[发送] -> " + this.f$0 + "：code=" + this.f$1 + " " + this.f$2);
    }
}
