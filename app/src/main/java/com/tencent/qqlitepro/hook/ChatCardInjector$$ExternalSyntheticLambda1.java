package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class ChatCardInjector$$ExternalSyntheticLambda1 implements java.lang.Runnable {
    public final /* synthetic */ java.lang.Object f$0;

    public /* synthetic */ ChatCardInjector$$ExternalSyntheticLambda1(java.lang.Object obj) {
        this.f$0 = obj;
    }

    @Override // java.lang.Runnable
    public final void run() {
        de.robv.android.xposed.XposedHelpers.callMethod(this.f$0, "notifyDataSetChanged", new java.lang.Object[0]);
    }
}
