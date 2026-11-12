package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class NativePickerBridge$$ExternalSyntheticLambda1 implements java.lang.Runnable {
    public final /* synthetic */ long f$0;
    public final /* synthetic */ java.lang.Object f$1;
    public final /* synthetic */ long f$2;

    public /* synthetic */ NativePickerBridge$$ExternalSyntheticLambda1(long j, java.lang.Object obj, long j2) {
        this.f$0 = j;
        this.f$1 = obj;
        this.f$2 = j2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.NativePickerBridge.lambda$scheduleAdapterRefresh$1(this.f$0, this.f$1, this.f$2);
    }
}
