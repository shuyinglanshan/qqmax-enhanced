package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda213 implements android.content.DialogInterface.OnClickListener {
    public final /* synthetic */ java.lang.Runnable f$0;
    public final /* synthetic */ android.content.Context f$1;
    public final /* synthetic */ android.view.ViewGroup f$2;
    public final /* synthetic */ int f$3;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda213(java.lang.Runnable runnable, android.content.Context context, android.view.ViewGroup viewGroup, int i) {
        this.f$0 = runnable;
        this.f$1 = context;
        this.f$2 = viewGroup;
        this.f$3 = i;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(android.content.DialogInterface dialogInterface, int i) {
        com.tencent.qqlitepro.hook.SettingsInjector.lambda$addResetButton$219(this.f$0, this.f$1, this.f$2, this.f$3, dialogInterface, i);
    }
}
