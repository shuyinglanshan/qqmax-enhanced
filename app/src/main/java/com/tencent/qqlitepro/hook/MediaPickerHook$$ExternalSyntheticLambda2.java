package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class MediaPickerHook$$ExternalSyntheticLambda2 implements android.view.View.OnClickListener {
    public final /* synthetic */ java.lang.Object f$0;

    public /* synthetic */ MediaPickerHook$$ExternalSyntheticLambda2(java.lang.Object obj) {
        this.f$0 = obj;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(android.view.View view) {
        com.tencent.qqlitepro.hook.MediaPickerHook.requestAddFriend(this.f$0);
    }
}
