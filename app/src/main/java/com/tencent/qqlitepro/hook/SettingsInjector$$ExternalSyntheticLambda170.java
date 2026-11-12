package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda170 implements android.view.View.OnClickListener {
    public final /* synthetic */ android.view.ViewGroup f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda170(android.view.ViewGroup viewGroup, int i) {
        this.f$0 = viewGroup;
        this.f$1 = i;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(android.view.View view) {
        com.tencent.qqlitepro.hook.SettingsInjector.openPage(this.f$0, this.f$1);
    }
}
