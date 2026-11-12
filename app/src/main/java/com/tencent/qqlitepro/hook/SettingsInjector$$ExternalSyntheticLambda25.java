package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda25 implements java.lang.Runnable {
    public final /* synthetic */ android.content.Context f$0;
    public final /* synthetic */ java.lang.String f$1;
    public final /* synthetic */ java.lang.String f$2;
    public final /* synthetic */ android.os.Handler f$3;
    public final /* synthetic */ android.view.ViewGroup f$4;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda25(android.content.Context context, java.lang.String str, java.lang.String str2, android.os.Handler handler, android.view.ViewGroup viewGroup) {
        this.f$0 = context;
        this.f$1 = str;
        this.f$2 = str2;
        this.f$3 = handler;
        this.f$4 = viewGroup;
    }

    @Override // java.lang.Runnable
    public final void run() {
        android.content.Context context = this.f$0;
        this.f$3.post(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda40(com.tencent.qqlitepro.mail.QqMailManager.verify(context, this.f$1, this.f$2), context, this.f$4));
    }
}
