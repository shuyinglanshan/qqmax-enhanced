package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda124 implements java.lang.Runnable {
    public final /* synthetic */ android.widget.TextView f$0;
    public final /* synthetic */ android.content.Context f$1;
    public final /* synthetic */ android.view.ViewGroup f$2;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda124(android.widget.TextView textView, android.content.Context context, android.view.ViewGroup viewGroup) {
        this.f$0 = textView;
        this.f$1 = context;
        this.f$2 = viewGroup;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.SettingsInjector.lambda$buildNotice$60(this.f$0, this.f$1, this.f$2);
    }
}
