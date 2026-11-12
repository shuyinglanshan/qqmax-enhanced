package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class MsgListenerHook$$ExternalSyntheticLambda1 implements java.lang.Runnable {
    public final /* synthetic */ java.lang.String f$0;
    public final /* synthetic */ java.lang.String f$1;
    public final /* synthetic */ boolean f$2;
    public final /* synthetic */ java.lang.String f$3;
    public final /* synthetic */ int f$4;
    public final /* synthetic */ java.lang.String f$5;

    public /* synthetic */ MsgListenerHook$$ExternalSyntheticLambda1(java.lang.String str, java.lang.String str2, boolean z, java.lang.String str3, int i, java.lang.String str4) {
        this.f$0 = str;
        this.f$1 = str2;
        this.f$2 = z;
        this.f$3 = str3;
        this.f$4 = i;
        this.f$5 = str4;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.MsgListenerHook.lambda$maybeReply$0(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
    }
}
