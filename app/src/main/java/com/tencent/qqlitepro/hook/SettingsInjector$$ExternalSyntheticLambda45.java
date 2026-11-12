package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda45 implements java.lang.Runnable {
    public final /* synthetic */ int f$0;
    public final /* synthetic */ java.util.List f$1;
    public final /* synthetic */ android.content.Context f$2;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda45(int i, java.util.List list, android.content.Context context) {
        this.f$0 = i;
        this.f$1 = list;
        this.f$2 = context;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.SettingsInjector.lambda$sendVoiceFiles$118(this.f$0, this.f$1, this.f$2);
    }
}
