package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda158 implements java.lang.Runnable {
    public final /* synthetic */ android.content.Context f$0;
    public final /* synthetic */ android.view.ViewGroup f$1;
    public final /* synthetic */ android.widget.LinearLayout f$2;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda158(android.content.Context context, android.view.ViewGroup viewGroup, android.widget.LinearLayout linearLayout) {
        this.f$0 = context;
        this.f$1 = viewGroup;
        this.f$2 = linearLayout;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.SettingsInjector.addAutoTaskRuleRow(this.f$0, this.f$1, this.f$2, "", "", "", false);
    }
}
