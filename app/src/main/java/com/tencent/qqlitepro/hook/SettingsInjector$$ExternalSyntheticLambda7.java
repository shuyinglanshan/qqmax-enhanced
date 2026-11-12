package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda7 implements android.content.DialogInterface.OnClickListener {
    public final /* synthetic */ java.io.File f$0;
    public final /* synthetic */ android.content.Context f$1;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda7(java.io.File file, android.content.Context context) {
        this.f$0 = file;
        this.f$1 = context;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(android.content.DialogInterface dialogInterface, int i) {
        com.tencent.qqlitepro.hook.SettingsInjector.lambda$deleteDownloadFile$214(this.f$0, this.f$1, dialogInterface, i);
    }
}
