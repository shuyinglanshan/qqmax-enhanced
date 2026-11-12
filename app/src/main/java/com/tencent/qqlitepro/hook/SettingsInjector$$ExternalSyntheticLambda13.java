package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda13 implements android.view.View.OnClickListener {
    public final /* synthetic */ android.widget.TextView f$0;
    public final /* synthetic */ android.graphics.drawable.GradientDrawable f$1;
    public final /* synthetic */ android.content.Context f$2;
    public final /* synthetic */ java.lang.Runnable f$3;
    public final /* synthetic */ android.view.ViewGroup f$4;
    public final /* synthetic */ int f$5;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda13(android.widget.TextView textView, android.graphics.drawable.GradientDrawable gradientDrawable, android.content.Context context, java.lang.Runnable runnable, android.view.ViewGroup viewGroup, int i) {
        this.f$0 = textView;
        this.f$1 = gradientDrawable;
        this.f$2 = context;
        this.f$3 = runnable;
        this.f$4 = viewGroup;
        this.f$5 = i;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(android.view.View view) {
        com.tencent.qqlitepro.hook.SettingsInjector.lambda$addResetButton$220(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, view);
    }
}
