package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda140 implements java.lang.Runnable {
    public final /* synthetic */ java.io.File f$0;
    public final /* synthetic */ java.lang.String f$1;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda140(java.io.File file, java.lang.String str) {
        this.f$0 = file;
        this.f$1 = str;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.SettingsInjector.lambda$handleFilePicked$95(this.f$0, this.f$1);
    }
}
