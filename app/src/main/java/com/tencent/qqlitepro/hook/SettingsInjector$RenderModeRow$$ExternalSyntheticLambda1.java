package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$RenderModeRow$$ExternalSyntheticLambda1 implements java.lang.Runnable {
    public final /* synthetic */ com.tencent.qqlitepro.hook.SettingsInjector.RenderModeRow f$0;
    public final /* synthetic */ android.graphics.drawable.GradientDrawable f$1;

    public /* synthetic */ SettingsInjector$RenderModeRow$$ExternalSyntheticLambda1(com.tencent.qqlitepro.hook.SettingsInjector.RenderModeRow renderModeRow, android.graphics.drawable.GradientDrawable gradientDrawable) {
        this.f$0 = renderModeRow;
        this.f$1 = gradientDrawable;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$new$0(this.f$1);
    }
}
