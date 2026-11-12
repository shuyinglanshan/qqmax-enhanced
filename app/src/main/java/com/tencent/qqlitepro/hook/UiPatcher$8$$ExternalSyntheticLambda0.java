package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class UiPatcher$8$$ExternalSyntheticLambda0 implements java.lang.Runnable {
    public final /* synthetic */ android.view.View f$0;
    public final /* synthetic */ java.lang.String f$1;

    public /* synthetic */ UiPatcher$8$$ExternalSyntheticLambda0(android.view.View view, java.lang.String str) {
        this.f$0 = view;
        this.f$1 = str;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.UiPatcher.AnonymousClass8.lambda$afterHookedMethod$1(this.f$0, this.f$1);
    }
}
