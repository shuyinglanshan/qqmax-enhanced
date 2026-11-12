package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda63 implements java.lang.Runnable {
    public final /* synthetic */ int[] f$0;
    public final /* synthetic */ android.widget.TextView f$1;
    public final /* synthetic */ java.util.ArrayList f$2;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda63(int[] iArr, android.widget.TextView textView, java.util.ArrayList arrayList) {
        this.f$0 = iArr;
        this.f$1 = textView;
        this.f$2 = arrayList;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.SettingsInjector.lambda$buildFileSend$90(this.f$0, this.f$1, this.f$2);
    }
}
