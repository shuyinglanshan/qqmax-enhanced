package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class ForceNoticeInjector$$ExternalSyntheticLambda5 implements java.lang.Runnable {
    public final /* synthetic */ android.content.Context f$0;
    public final /* synthetic */ android.app.Dialog f$1;
    public final /* synthetic */ android.widget.FrameLayout f$2;
    public final /* synthetic */ android.widget.TextView f$3;

    public /* synthetic */ ForceNoticeInjector$$ExternalSyntheticLambda5(android.content.Context context, android.app.Dialog dialog, android.widget.FrameLayout frameLayout, android.widget.TextView textView) {
        this.f$0 = context;
        this.f$1 = dialog;
        this.f$2 = frameLayout;
        this.f$3 = textView;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.ForceNoticeInjector.fillNodeList(this.f$0, this.f$1, this.f$2, this.f$3);
    }
}
