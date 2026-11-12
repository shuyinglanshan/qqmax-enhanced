package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda114 implements android.view.View.OnClickListener {
    public final /* synthetic */ android.widget.LinearLayout f$0;
    public final /* synthetic */ android.widget.LinearLayout f$1;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda114(android.widget.LinearLayout linearLayout, android.widget.LinearLayout linearLayout2) {
        this.f$0 = linearLayout;
        this.f$1 = linearLayout2;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(android.view.View view) {
        com.tencent.qqlitepro.hook.SettingsInjector.lambda$addKeywordRuleRow$45(this.f$0, this.f$1, view);
    }
}
