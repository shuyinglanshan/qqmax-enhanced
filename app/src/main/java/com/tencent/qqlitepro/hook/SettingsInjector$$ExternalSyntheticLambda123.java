package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda123 implements java.lang.Runnable {
    public final /* synthetic */ android.content.Context f$0;
    public final /* synthetic */ android.widget.LinearLayout f$1;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda123(android.content.Context context, android.widget.LinearLayout linearLayout) {
        this.f$0 = context;
        this.f$1 = linearLayout;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.SettingsInjector.addKeywordRuleRow(this.f$0, this.f$1, "", "", false, false, false);
    }
}
