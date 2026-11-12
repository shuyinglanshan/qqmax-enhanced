package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda130 implements android.content.DialogInterface.OnClickListener {
    public final /* synthetic */ android.content.Context f$0;
    public final /* synthetic */ java.io.File f$1;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda130(android.content.Context context, java.io.File file) {
        this.f$0 = context;
        this.f$1 = file;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(android.content.DialogInterface dialogInterface, int i) {
        com.tencent.qqlitepro.hook.SettingsInjector.importPluginFile(this.f$0, this.f$1);
    }
}
