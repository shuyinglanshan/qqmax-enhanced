package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class MediaPickerHook$$ExternalSyntheticLambda6 implements java.lang.Runnable {
    public final /* synthetic */ android.content.Context f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ java.lang.String f$2;
    public final /* synthetic */ java.util.List f$3;

    public /* synthetic */ MediaPickerHook$$ExternalSyntheticLambda6(android.content.Context context, int i, java.lang.String str, java.util.List list) {
        this.f$0 = context;
        this.f$1 = i;
        this.f$2 = str;
        this.f$3 = list;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.MediaPickerHook.resolveAndSend(this.f$0, this.f$1, this.f$2, this.f$3);
    }
}
