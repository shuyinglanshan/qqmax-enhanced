package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda203 implements java.lang.Runnable {
    public final /* synthetic */ int f$0;
    public final /* synthetic */ java.util.List f$1;
    public final /* synthetic */ double f$2;
    public final /* synthetic */ android.content.Context f$3;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda203(int i, java.util.List list, double d, android.content.Context context) {
        this.f$0 = i;
        this.f$1 = list;
        this.f$2 = d;
        this.f$3 = context;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.SettingsInjector.lambda$sendFilesToSession$102(this.f$0, this.f$1, this.f$2, this.f$3);
    }
}
