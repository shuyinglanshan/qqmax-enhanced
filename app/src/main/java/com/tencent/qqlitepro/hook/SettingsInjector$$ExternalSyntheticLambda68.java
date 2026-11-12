package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda68 implements android.view.View.OnClickListener {
    public final /* synthetic */ java.lang.Runnable f$0;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda68(java.lang.Runnable runnable) {
        this.f$0 = runnable;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(android.view.View view) {
        this.f$0.run();
    }
}
