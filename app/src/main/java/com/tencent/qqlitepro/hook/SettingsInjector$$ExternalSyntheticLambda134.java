package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda134 implements java.lang.Runnable {
    public final /* synthetic */ android.widget.LinearLayout f$0;
    public final /* synthetic */ android.widget.CompoundButton[] f$1;
    public final /* synthetic */ android.content.Context f$2;
    public final /* synthetic */ android.view.ViewGroup f$3;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda134(android.widget.LinearLayout linearLayout, android.widget.CompoundButton[] compoundButtonArr, android.content.Context context, android.view.ViewGroup viewGroup) {
        this.f$0 = linearLayout;
        this.f$1 = compoundButtonArr;
        this.f$2 = context;
        this.f$3 = viewGroup;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.SettingsInjector.lambda$buildKeywordReply$41(this.f$0, this.f$1, this.f$2, this.f$3);
    }
}
