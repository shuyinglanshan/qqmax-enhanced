package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda167 implements java.lang.Runnable {
    public final /* synthetic */ java.lang.String f$0;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda167(java.lang.String str) {
        this.f$0 = str;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.SettingsInjector.sQqDynamicLog.append(this.f$0 + "\n");
    }
}
