package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda116 implements android.view.View.OnClickListener {
    public final /* synthetic */ java.lang.String f$0;
    public final /* synthetic */ android.widget.TextView f$1;
    public final /* synthetic */ android.widget.TextView f$2;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda116(java.lang.String str, android.widget.TextView textView, android.widget.TextView textView2) {
        this.f$0 = str;
        this.f$1 = textView;
        this.f$2 = textView2;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(android.view.View view) {
        com.tencent.qqlitepro.hook.SettingsInjector.lambda$sessionRow$10(this.f$0, this.f$1, this.f$2, view);
    }
}
