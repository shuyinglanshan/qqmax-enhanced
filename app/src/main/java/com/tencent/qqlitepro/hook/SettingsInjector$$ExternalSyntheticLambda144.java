package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda144 implements java.lang.Runnable {
    public final /* synthetic */ android.widget.LinearLayout f$0;
    public final /* synthetic */ android.widget.CompoundButton[] f$1;
    public final /* synthetic */ android.content.Context f$2;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda144(android.widget.LinearLayout linearLayout, android.widget.CompoundButton[] compoundButtonArr, android.content.Context context) {
        this.f$0 = linearLayout;
        this.f$1 = compoundButtonArr;
        this.f$2 = context;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.SettingsInjector.lambda$buildKeywordReply$42(this.f$0, this.f$1, this.f$2);
    }
}
