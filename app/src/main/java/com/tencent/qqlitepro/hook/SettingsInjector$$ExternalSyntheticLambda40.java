package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda40 implements java.lang.Runnable {
    public final /* synthetic */ com.tencent.qqlitepro.mail.QqMailManager.VerifyResult f$0;
    public final /* synthetic */ android.content.Context f$1;
    public final /* synthetic */ android.view.ViewGroup f$2;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda40(com.tencent.qqlitepro.mail.QqMailManager.VerifyResult verifyResult, android.content.Context context, android.view.ViewGroup viewGroup) {
        this.f$0 = verifyResult;
        this.f$1 = context;
        this.f$2 = viewGroup;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.SettingsInjector.lambda$buildMailSetting$141(this.f$0, this.f$1, this.f$2);
    }
}
