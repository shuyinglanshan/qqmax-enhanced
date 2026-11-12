package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class AddFriendVerifyHook$$ExternalSyntheticLambda2 implements java.lang.Runnable {
    public final /* synthetic */ long f$0;
    public final /* synthetic */ java.util.List f$1;

    public /* synthetic */ AddFriendVerifyHook$$ExternalSyntheticLambda2(long j, java.util.List list) {
        this.f$0 = j;
        this.f$1 = list;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.AddFriendVerifyHook.showDialog(this.f$0, this.f$1);
    }
}
