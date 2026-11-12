package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda70 implements android.view.View.OnClickListener {
    public final /* synthetic */ int f$0;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda70(int i) {
        this.f$0 = i;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(android.view.View view) {
        com.tencent.qqlitepro.hook.SettingsInjector.setModeAndRefresh(view.getParent() instanceof android.view.ViewGroup ? (android.view.ViewGroup) view.getParent() : null, this.f$0);
    }
}
