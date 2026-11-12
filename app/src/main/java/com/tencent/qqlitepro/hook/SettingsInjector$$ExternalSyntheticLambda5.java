package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda5 implements java.lang.Runnable {
    public final /* synthetic */ android.widget.TextView f$0;
    public final /* synthetic */ android.graphics.drawable.GradientDrawable f$1;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda5(android.widget.TextView textView, android.graphics.drawable.GradientDrawable gradientDrawable) {
        this.f$0 = textView;
        this.f$1 = gradientDrawable;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.setBackground(this.f$1);
    }
}
