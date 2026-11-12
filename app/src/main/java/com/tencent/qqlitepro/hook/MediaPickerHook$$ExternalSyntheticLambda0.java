package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class MediaPickerHook$$ExternalSyntheticLambda0 implements java.lang.Runnable {
    public final /* synthetic */ java.lang.String f$0;
    public final /* synthetic */ java.lang.String f$1;
    public final /* synthetic */ android.content.Context f$2;

    public /* synthetic */ MediaPickerHook$$ExternalSyntheticLambda0(java.lang.String str, java.lang.String str2, android.content.Context context) {
        this.f$0 = str;
        this.f$1 = str2;
        this.f$2 = context;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.MediaPickerHook.lambda$requestAddFriend$2(this.f$0, this.f$1, this.f$2);
    }
}
