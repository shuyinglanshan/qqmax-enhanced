package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda9 implements java.lang.Runnable {
    public final /* synthetic */ android.content.Context f$0;
    public final /* synthetic */ android.widget.FrameLayout f$1;
    public final /* synthetic */ android.widget.TextView f$2;
    public final /* synthetic */ android.view.ViewGroup f$3;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda9(android.content.Context context, android.widget.FrameLayout frameLayout, android.widget.TextView textView, android.view.ViewGroup viewGroup) {
        this.f$0 = context;
        this.f$1 = frameLayout;
        this.f$2 = textView;
        this.f$3 = viewGroup;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.SettingsInjector.fillNoticeNodeList(this.f$0, this.f$1, this.f$2, this.f$3);
    }
}
