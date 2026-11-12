package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda148 implements java.lang.Runnable {
    public final /* synthetic */ boolean[] f$0;
    public final /* synthetic */ java.util.List f$1;
    public final /* synthetic */ java.util.List f$2;
    public final /* synthetic */ java.util.Set f$3;
    public final /* synthetic */ android.widget.TextView f$4;
    public final /* synthetic */ android.widget.TextView f$5;
    public final /* synthetic */ android.widget.LinearLayout f$6;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda148(boolean[] zArr, java.util.List list, java.util.List list2, java.util.Set set, android.widget.TextView textView, android.widget.TextView textView2, android.widget.LinearLayout linearLayout) {
        this.f$0 = zArr;
        this.f$1 = list;
        this.f$2 = list2;
        this.f$3 = set;
        this.f$4 = textView;
        this.f$5 = textView2;
        this.f$6 = linearLayout;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.SettingsInjector.lambda$loadSessionsAsync$7(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6);
    }
}
