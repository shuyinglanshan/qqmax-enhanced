package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class UiPatcher$$ExternalSyntheticLambda35 implements java.lang.Runnable {
    public final /* synthetic */ android.view.View f$0;
    public final /* synthetic */ java.lang.Object f$1;
    public final /* synthetic */ java.lang.String f$2;

    public /* synthetic */ UiPatcher$$ExternalSyntheticLambda35(android.view.View view, java.lang.Object obj, java.lang.String str) {
        this.f$0 = view;
        this.f$1 = obj;
        this.f$2 = str;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.UiPatcher.recheckFake(this.f$0, this.f$1, this.f$2);
    }
}
