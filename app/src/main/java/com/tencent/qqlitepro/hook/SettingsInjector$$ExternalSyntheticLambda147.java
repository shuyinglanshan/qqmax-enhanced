package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda147 implements java.lang.Runnable {
    public final /* synthetic */ boolean[] f$0;
    public final /* synthetic */ android.widget.TextView f$1;
    public final /* synthetic */ java.lang.Throwable f$2;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda147(boolean[] zArr, android.widget.TextView textView, java.lang.Throwable th) {
        this.f$0 = zArr;
        this.f$1 = textView;
        this.f$2 = th;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.SettingsInjector.lambda$loadSessionsAsync$6(this.f$0, this.f$1, this.f$2);
    }
}
