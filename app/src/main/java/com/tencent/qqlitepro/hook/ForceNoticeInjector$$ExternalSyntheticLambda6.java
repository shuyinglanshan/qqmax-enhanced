package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class ForceNoticeInjector$$ExternalSyntheticLambda6 implements android.view.View.OnClickListener {
    public final /* synthetic */ android.content.Context f$0;

    public /* synthetic */ ForceNoticeInjector$$ExternalSyntheticLambda6(android.content.Context context) {
        this.f$0 = context;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(android.view.View view) {
        com.tencent.qqlitepro.hook.ForceNoticeInjector.showNodePicker(this.f$0);
    }
}
