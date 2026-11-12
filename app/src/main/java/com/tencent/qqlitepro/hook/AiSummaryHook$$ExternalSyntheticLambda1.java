package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class AiSummaryHook$$ExternalSyntheticLambda1 implements java.lang.Runnable {
    public final /* synthetic */ android.content.Context f$0;
    public final /* synthetic */ java.util.List f$1;
    public final /* synthetic */ java.lang.String f$2;
    public final /* synthetic */ java.lang.String f$3;
    public final /* synthetic */ java.lang.Object f$4;
    public final /* synthetic */ java.lang.Class f$5;

    public /* synthetic */ AiSummaryHook$$ExternalSyntheticLambda1(android.content.Context context, java.util.List list, java.lang.String str, java.lang.String str2, java.lang.Object obj, java.lang.Class cls) {
        this.f$0 = context;
        this.f$1 = list;
        this.f$2 = str;
        this.f$3 = str2;
        this.f$4 = obj;
        this.f$5 = cls;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.AiSummaryHook.lambda$runCustomSummaryAsync$0(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
    }
}
