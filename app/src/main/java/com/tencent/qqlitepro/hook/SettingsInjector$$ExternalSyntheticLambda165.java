package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda165 implements java.lang.Runnable {
    public final /* synthetic */ android.content.Context f$0;
    public final /* synthetic */ boolean f$1;
    public final /* synthetic */ java.lang.String f$2;
    public final /* synthetic */ android.view.ViewGroup f$3;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda165(android.content.Context context, boolean z, java.lang.String str, android.view.ViewGroup viewGroup) {
        this.f$0 = context;
        this.f$1 = z;
        this.f$2 = str;
        this.f$3 = viewGroup;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.SettingsInjector.lambda$buildKeywordReply$40(this.f$0, this.f$1, this.f$2, this.f$3);
    }
}
