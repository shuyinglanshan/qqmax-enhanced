package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda129 implements java.lang.Runnable {
    public final /* synthetic */ android.app.Activity f$0;
    public final /* synthetic */ android.net.Uri f$1;
    public final /* synthetic */ java.io.File f$2;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda129(android.app.Activity activity, android.net.Uri uri, java.io.File file) {
        this.f$0 = activity;
        this.f$1 = uri;
        this.f$2 = file;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.SettingsInjector.lambda$handleFilePicked$97(this.f$0, this.f$1, this.f$2);
    }
}
