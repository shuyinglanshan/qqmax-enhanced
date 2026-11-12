package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda206 implements android.view.View.OnClickListener {
    public final /* synthetic */ java.lang.String f$0;
    public final /* synthetic */ android.view.ViewGroup f$1;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda206(java.lang.String str, android.view.ViewGroup viewGroup) {
        this.f$0 = str;
        this.f$1 = viewGroup;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(android.view.View view) {
        com.tencent.qqlitepro.hook.SettingsInjector.lambda$fillNoticeNodeList$67(this.f$0, this.f$1, view);
    }
}
