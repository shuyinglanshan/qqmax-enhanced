package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda75 implements java.lang.Runnable {
    public final /* synthetic */ com.tencent.qqlitepro.mail.QqMailManager.SendResult f$0;
    public final /* synthetic */ android.content.Context f$1;
    public final /* synthetic */ android.view.ViewGroup f$2;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda75(com.tencent.qqlitepro.mail.QqMailManager.SendResult sendResult, android.content.Context context, android.view.ViewGroup viewGroup) {
        this.f$0 = sendResult;
        this.f$1 = context;
        this.f$2 = viewGroup;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.SettingsInjector.lambda$buildMailSend$170(this.f$0, this.f$1, this.f$2);
    }
}
