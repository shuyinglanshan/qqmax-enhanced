package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda93 implements java.lang.Runnable {
    public final /* synthetic */ android.widget.CompoundButton[] f$0;
    public final /* synthetic */ android.widget.EditText f$1;
    public final /* synthetic */ android.widget.CompoundButton[] f$2;
    public final /* synthetic */ android.content.Context f$3;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda93(android.widget.CompoundButton[] compoundButtonArr, android.widget.EditText editText, android.widget.CompoundButton[] compoundButtonArr2, android.content.Context context) {
        this.f$0 = compoundButtonArr;
        this.f$1 = editText;
        this.f$2 = compoundButtonArr2;
        this.f$3 = context;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.SettingsInjector.lambda$buildAutoReply$49(this.f$0, this.f$1, this.f$2, this.f$3);
    }
}
