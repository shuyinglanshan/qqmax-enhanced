package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda154 implements android.view.View.OnClickListener {
    public final /* synthetic */ android.widget.TextView f$0;
    public final /* synthetic */ android.graphics.drawable.GradientDrawable f$1;
    public final /* synthetic */ java.lang.Runnable f$2;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda154(android.widget.TextView textView, android.graphics.drawable.GradientDrawable gradientDrawable, java.lang.Runnable runnable) {
        this.f$0 = textView;
        this.f$1 = gradientDrawable;
        this.f$2 = runnable;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(android.view.View view) {
        com.tencent.qqlitepro.hook.SettingsInjector.lambda$saveButton$217(this.f$0, this.f$1, this.f$2, view);
    }
}
