package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda173 implements java.lang.Runnable {
    public final /* synthetic */ android.content.Context f$0;
    public final /* synthetic */ java.lang.String f$1;
    public final /* synthetic */ java.lang.String f$2;
    public final /* synthetic */ java.lang.String f$3;
    public final /* synthetic */ boolean f$4;
    public final /* synthetic */ java.util.List f$5;
    public final /* synthetic */ android.os.Handler f$6;
    public final /* synthetic */ android.view.ViewGroup f$7;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda173(android.content.Context context, java.lang.String str, java.lang.String str2, java.lang.String str3, boolean z, java.util.List list, android.os.Handler handler, android.view.ViewGroup viewGroup) {
        this.f$0 = context;
        this.f$1 = str;
        this.f$2 = str2;
        this.f$3 = str3;
        this.f$4 = z;
        this.f$5 = list;
        this.f$6 = handler;
        this.f$7 = viewGroup;
    }

    @Override // java.lang.Runnable
    public final void run() {
        android.content.Context context = this.f$0;
        this.f$6.post(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda75(com.tencent.qqlitepro.mail.QqMailManager.send(context, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5), context, this.f$7));
    }
}
