package com.tencent.qqlitepro;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class MainHook$$ExternalSyntheticLambda1 implements java.lang.Runnable {
    public final /* synthetic */ android.content.Context f$0;
    public final /* synthetic */ java.lang.String f$1;

    public /* synthetic */ MainHook$$ExternalSyntheticLambda1(android.content.Context context, java.lang.String str) {
        this.f$0 = context;
        this.f$1 = str;
    }

    @Override // java.lang.Runnable
    public final void run() {
        android.widget.Toast.makeText(this.f$0, this.f$1, 1).show();
    }
}
