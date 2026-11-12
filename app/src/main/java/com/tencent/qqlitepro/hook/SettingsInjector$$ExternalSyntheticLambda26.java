package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda26 implements android.content.DialogInterface.OnClickListener {
    public final /* synthetic */ android.content.Context f$0;
    public final /* synthetic */ java.lang.String f$1;
    public final /* synthetic */ java.lang.String f$2;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda26(android.content.Context context, java.lang.String str, java.lang.String str2) {
        this.f$0 = context;
        this.f$1 = str;
        this.f$2 = str2;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(android.content.DialogInterface dialogInterface, int i) {
        com.tencent.qqlitepro.hook.SettingsInjector.downloadMailAtt(this.f$0, this.f$1, this.f$2);
    }
}
