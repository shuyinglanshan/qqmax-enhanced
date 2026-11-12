package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda56 implements java.lang.Runnable {
    public final /* synthetic */ com.tencent.qqlitepro.hook.SettingsInjector.SelectableTextView f$0;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda56(com.tencent.qqlitepro.hook.SettingsInjector.SelectableTextView selectableTextView) {
        this.f$0 = selectableTextView;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.selectAll();
    }
}
