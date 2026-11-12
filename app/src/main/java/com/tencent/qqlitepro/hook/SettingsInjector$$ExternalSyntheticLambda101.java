package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda101 implements java.lang.Runnable {
    public final /* synthetic */ android.content.Context f$0;
    public final /* synthetic */ android.widget.LinearLayout f$1;
    public final /* synthetic */ android.widget.TextView f$2;
    public final /* synthetic */ android.widget.TextView f$3;
    public final /* synthetic */ java.util.List f$4;
    public final /* synthetic */ boolean[] f$5;
    public final /* synthetic */ boolean f$6;
    public final /* synthetic */ java.util.Set f$7;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda101(android.content.Context context, android.widget.LinearLayout linearLayout, android.widget.TextView textView, android.widget.TextView textView2, java.util.List list, boolean[] zArr, boolean z, java.util.Set set) {
        this.f$0 = context;
        this.f$1 = linearLayout;
        this.f$2 = textView;
        this.f$3 = textView2;
        this.f$4 = list;
        this.f$5 = zArr;
        this.f$6 = z;
        this.f$7 = set;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.SettingsInjector.loadSessionsAsync(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7);
    }
}
