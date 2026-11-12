package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda149 implements java.lang.Runnable {
    public final /* synthetic */ android.app.Dialog f$0;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda149(android.app.Dialog dialog) {
        this.f$0 = dialog;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.dismiss();
    }
}
