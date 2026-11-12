package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$RenderModeRow$$ExternalSyntheticLambda0 implements android.view.View.OnClickListener {
    public final /* synthetic */ com.tencent.qqlitepro.hook.SettingsInjector.RenderModeRow f$0;
    public final /* synthetic */ int[] f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ android.content.Context f$3;
    public final /* synthetic */ java.lang.Runnable f$4;
    public final /* synthetic */ android.widget.TextView f$5;
    public final /* synthetic */ android.graphics.drawable.GradientDrawable f$6;

    public /* synthetic */ SettingsInjector$RenderModeRow$$ExternalSyntheticLambda0(com.tencent.qqlitepro.hook.SettingsInjector.RenderModeRow renderModeRow, int[] iArr, int i, android.content.Context context, java.lang.Runnable runnable, android.widget.TextView textView, android.graphics.drawable.GradientDrawable gradientDrawable) {
        this.f$0 = renderModeRow;
        this.f$1 = iArr;
        this.f$2 = i;
        this.f$3 = context;
        this.f$4 = runnable;
        this.f$5 = textView;
        this.f$6 = gradientDrawable;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(android.view.View view) {
        this.f$0.lambda$new$1(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, view);
    }
}
