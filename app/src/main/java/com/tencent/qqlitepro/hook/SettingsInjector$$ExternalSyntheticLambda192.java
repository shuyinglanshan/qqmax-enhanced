package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda192 implements java.lang.Runnable {
    public final /* synthetic */ android.widget.TextView f$0;
    public final /* synthetic */ java.lang.String f$1;
    public final /* synthetic */ android.view.ViewGroup f$2;
    public final /* synthetic */ android.content.Context f$3;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda192(android.widget.TextView textView, java.lang.String str, android.view.ViewGroup viewGroup, android.content.Context context) {
        this.f$0 = textView;
        this.f$1 = str;
        this.f$2 = viewGroup;
        this.f$3 = context;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.SettingsInjector.lambda$buildNotice$59(this.f$0, this.f$1, this.f$2, this.f$3);
    }
}
