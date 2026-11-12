package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$10$$ExternalSyntheticLambda1 implements java.lang.Runnable {
    public final /* synthetic */ android.content.Context f$0;
    public final /* synthetic */ android.os.Handler f$1;
    public final /* synthetic */ boolean[] f$2;
    public final /* synthetic */ int f$3;
    public final /* synthetic */ android.view.ViewGroup f$4;

    public /* synthetic */ SettingsInjector$10$$ExternalSyntheticLambda1(android.content.Context context, android.os.Handler handler, boolean[] zArr, int i, android.view.ViewGroup viewGroup) {
        this.f$0 = context;
        this.f$1 = handler;
        this.f$2 = zArr;
        this.f$3 = i;
        this.f$4 = viewGroup;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.SettingsInjector.AnonymousClass10.lambda$run$1(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4);
    }
}
