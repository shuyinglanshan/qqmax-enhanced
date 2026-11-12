package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda220 implements java.lang.Runnable {
    public final /* synthetic */ android.content.Context f$0;
    public final /* synthetic */ java.lang.String f$1;
    public final /* synthetic */ java.lang.String f$2;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda220(android.content.Context context, java.lang.String str, java.lang.String str2) {
        this.f$0 = context;
        this.f$1 = str;
        this.f$2 = str2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.SettingsInjector.viewMailImage(this.f$0, this.f$1, this.f$2);
    }
}
