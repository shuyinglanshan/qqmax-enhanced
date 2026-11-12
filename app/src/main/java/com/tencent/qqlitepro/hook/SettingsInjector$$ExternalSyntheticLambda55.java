package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda55 implements java.lang.Runnable {
    public final /* synthetic */ com.tencent.qqlitepro.hook.SettingsInjector.SelectableTextView f$0;
    public final /* synthetic */ java.lang.String f$1;
    public final /* synthetic */ android.content.Context f$2;
    public final /* synthetic */ android.app.Dialog f$3;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda55(com.tencent.qqlitepro.hook.SettingsInjector.SelectableTextView selectableTextView, java.lang.String str, android.content.Context context, android.app.Dialog dialog) {
        this.f$0 = selectableTextView;
        this.f$1 = str;
        this.f$2 = context;
        this.f$3 = dialog;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.SettingsInjector.lambda$showTextCopyDialog$16(this.f$0, this.f$1, this.f$2, this.f$3);
    }
}
