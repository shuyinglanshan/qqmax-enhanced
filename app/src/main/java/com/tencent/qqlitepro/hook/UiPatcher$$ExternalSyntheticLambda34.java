package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class UiPatcher$$ExternalSyntheticLambda34 implements java.lang.Runnable {
    public final /* synthetic */ android.view.View f$0;
    public final /* synthetic */ java.lang.String f$1;
    public final /* synthetic */ com.tencent.qqlitepro.hook.UiPatcher.FakeDetect f$2;

    public /* synthetic */ UiPatcher$$ExternalSyntheticLambda34(android.view.View view, java.lang.String str, com.tencent.qqlitepro.hook.UiPatcher.FakeDetect fakeDetect) {
        this.f$0 = view;
        this.f$1 = str;
        this.f$2 = fakeDetect;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.UiPatcher.lambda$recheckFake$30(this.f$0, this.f$1, this.f$2);
    }
}
