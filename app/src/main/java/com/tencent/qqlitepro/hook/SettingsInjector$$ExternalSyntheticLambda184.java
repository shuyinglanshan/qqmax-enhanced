package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda184 implements java.lang.Runnable {
    public final /* synthetic */ java.lang.Object[] f$0;
    public final /* synthetic */ java.lang.String f$1;
    public final /* synthetic */ java.lang.Throwable[] f$2;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda184(java.lang.Object[] objArr, java.lang.String str, java.lang.Throwable[] thArr) {
        this.f$0 = objArr;
        this.f$1 = str;
        this.f$2 = thArr;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.SettingsInjector.lambda$sendVoiceFiles$114(this.f$0, this.f$1, this.f$2);
    }
}
