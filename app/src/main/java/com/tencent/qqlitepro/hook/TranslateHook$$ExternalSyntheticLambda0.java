package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class TranslateHook$$ExternalSyntheticLambda0 implements java.lang.Runnable {
    public final /* synthetic */ java.lang.String f$0;
    public final /* synthetic */ java.lang.Object f$1;

    public /* synthetic */ TranslateHook$$ExternalSyntheticLambda0(java.lang.String str, java.lang.Object obj) {
        this.f$0 = str;
        this.f$1 = obj;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.TranslateHook.invokeCallback(this.f$1, com.tencent.qqlitepro.hook.TranslateHook.httpTranslate(this.f$0));
    }
}
