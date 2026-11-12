package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class MediaPickerHook$$ExternalSyntheticLambda7 implements java.lang.Runnable {
    public final /* synthetic */ java.util.List f$0;
    public final /* synthetic */ android.content.Context f$1;
    public final /* synthetic */ java.lang.Object f$2;

    public /* synthetic */ MediaPickerHook$$ExternalSyntheticLambda7(java.util.List list, android.content.Context context, java.lang.Object obj) {
        this.f$0 = list;
        this.f$1 = context;
        this.f$2 = obj;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.MediaPickerHook.lambda$onPicked$6(this.f$0, this.f$1, this.f$2);
    }
}
