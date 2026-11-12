package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class AvatarHook$$ExternalSyntheticLambda1 implements android.content.DialogInterface.OnClickListener {
    public final /* synthetic */ boolean f$0;
    public final /* synthetic */ android.content.Context f$1;
    public final /* synthetic */ java.lang.String f$2;
    public final /* synthetic */ java.lang.String f$3;

    public /* synthetic */ AvatarHook$$ExternalSyntheticLambda1(boolean z, android.content.Context context, java.lang.String str, java.lang.String str2) {
        this.f$0 = z;
        this.f$1 = context;
        this.f$2 = str;
        this.f$3 = str2;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(android.content.DialogInterface dialogInterface, int i) {
        com.tencent.qqlitepro.hook.AvatarHook.lambda$showSaveDialog$0(this.f$0, this.f$1, this.f$2, this.f$3, dialogInterface, i);
    }
}
