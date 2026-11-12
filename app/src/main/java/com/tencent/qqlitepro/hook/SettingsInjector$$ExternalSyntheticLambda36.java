package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda36 implements java.lang.Runnable {
    public final /* synthetic */ android.content.Context f$0;
    public final /* synthetic */ long f$1;
    public final /* synthetic */ android.os.Handler f$2;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda36(android.content.Context context, long j, android.os.Handler handler) {
        this.f$0 = context;
        this.f$1 = j;
        this.f$2 = handler;
    }

    @Override // java.lang.Runnable
    public final void run() {
        android.content.Context context = this.f$0;
        this.f$2.post(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda128(com.tencent.qqlitepro.mail.QqMailManager.upgradeEntry(context, this.f$1), context));
    }
}
