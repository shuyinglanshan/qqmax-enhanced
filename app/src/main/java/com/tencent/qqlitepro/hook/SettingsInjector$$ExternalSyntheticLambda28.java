package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda28 implements java.lang.Runnable {
    public final /* synthetic */ boolean f$0;
    public final /* synthetic */ android.os.Handler f$1;
    public final /* synthetic */ boolean[] f$2;
    public final /* synthetic */ android.widget.TextView f$3;
    public final /* synthetic */ java.util.List f$4;
    public final /* synthetic */ java.util.Set f$5;
    public final /* synthetic */ android.widget.TextView f$6;
    public final /* synthetic */ android.widget.LinearLayout f$7;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda28(boolean z, android.os.Handler handler, boolean[] zArr, android.widget.TextView textView, java.util.List list, java.util.Set set, android.widget.TextView textView2, android.widget.LinearLayout linearLayout) {
        this.f$0 = z;
        this.f$1 = handler;
        this.f$2 = zArr;
        this.f$3 = textView;
        this.f$4 = list;
        this.f$5 = set;
        this.f$6 = textView2;
        this.f$7 = linearLayout;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.SettingsInjector.lambda$loadSessionsAsync$8(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7);
    }
}
