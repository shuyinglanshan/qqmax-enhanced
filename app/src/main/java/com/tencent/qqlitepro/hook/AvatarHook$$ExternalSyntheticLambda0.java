package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class AvatarHook$$ExternalSyntheticLambda0 implements java.lang.Runnable {
    public final /* synthetic */ android.content.Context f$0;
    public final /* synthetic */ java.lang.String f$1;
    public final /* synthetic */ java.lang.String f$2;
    public final /* synthetic */ boolean f$3;
    public final /* synthetic */ java.lang.String f$4;
    public final /* synthetic */ java.lang.String f$5;

    public /* synthetic */ AvatarHook$$ExternalSyntheticLambda0(android.content.Context context, java.lang.String str, java.lang.String str2, boolean z, java.lang.String str3, java.lang.String str4) {
        this.f$0 = context;
        this.f$1 = str;
        this.f$2 = str2;
        this.f$3 = z;
        this.f$4 = str3;
        this.f$5 = str4;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.AvatarHook.lambda$showSaveDialog$1(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
    }
}
