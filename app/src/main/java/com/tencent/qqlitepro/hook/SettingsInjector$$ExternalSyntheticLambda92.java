package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda92 implements java.lang.Runnable {
    public final /* synthetic */ android.widget.EditText f$0;
    public final /* synthetic */ android.widget.CompoundButton[] f$1;
    public final /* synthetic */ android.widget.CompoundButton[] f$2;
    public final /* synthetic */ android.content.Context f$3;
    public final /* synthetic */ android.view.ViewGroup f$4;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda92(android.widget.EditText editText, android.widget.CompoundButton[] compoundButtonArr, android.widget.CompoundButton[] compoundButtonArr2, android.content.Context context, android.view.ViewGroup viewGroup) {
        this.f$0 = editText;
        this.f$1 = compoundButtonArr;
        this.f$2 = compoundButtonArr2;
        this.f$3 = context;
        this.f$4 = viewGroup;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.SettingsInjector.lambda$buildAutoReply$48(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4);
    }
}
