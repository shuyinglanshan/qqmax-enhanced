package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda32 implements java.lang.Runnable {
    public final /* synthetic */ android.widget.FrameLayout f$0;
    public final /* synthetic */ java.lang.String f$1;
    public final /* synthetic */ android.widget.TextView f$2;
    public final /* synthetic */ android.content.Context f$3;
    public final /* synthetic */ android.view.ViewGroup f$4;
    public final /* synthetic */ java.util.List f$5;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda32(android.widget.FrameLayout frameLayout, java.lang.String str, android.widget.TextView textView, android.content.Context context, android.view.ViewGroup viewGroup, java.util.List list) {
        this.f$0 = frameLayout;
        this.f$1 = str;
        this.f$2 = textView;
        this.f$3 = context;
        this.f$4 = viewGroup;
        this.f$5 = list;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.SettingsInjector.lambda$fillNoticeNodeList$68(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
    }
}
