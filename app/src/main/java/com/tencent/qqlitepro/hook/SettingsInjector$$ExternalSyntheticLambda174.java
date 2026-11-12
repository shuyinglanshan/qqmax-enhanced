package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda174 implements java.lang.Runnable {
    public final /* synthetic */ boolean f$0;
    public final /* synthetic */ java.io.File f$1;
    public final /* synthetic */ android.content.Context f$2;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda174(boolean z, java.io.File file, android.content.Context context) {
        this.f$0 = z;
        this.f$1 = file;
        this.f$2 = context;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.SettingsInjector.lambda$handleMailPicked$178(this.f$0, this.f$1, this.f$2);
    }
}
