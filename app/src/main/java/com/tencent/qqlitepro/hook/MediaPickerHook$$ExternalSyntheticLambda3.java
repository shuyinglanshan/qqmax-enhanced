package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class MediaPickerHook$$ExternalSyntheticLambda3 implements java.lang.Runnable {
    public final /* synthetic */ java.util.List f$0;
    public final /* synthetic */ java.lang.Object f$1;
    public final /* synthetic */ android.content.Context f$2;

    public /* synthetic */ MediaPickerHook$$ExternalSyntheticLambda3(java.util.List list, java.lang.Object obj, android.content.Context context) {
        this.f$0 = list;
        this.f$1 = obj;
        this.f$2 = context;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.MediaPickerHook.lambda$onPicked$5(this.f$0, this.f$1, this.f$2);
    }
}
