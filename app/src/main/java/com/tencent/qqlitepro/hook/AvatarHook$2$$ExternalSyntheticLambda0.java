package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class AvatarHook$2$$ExternalSyntheticLambda0 implements android.view.View.OnLongClickListener {
    public final /* synthetic */ android.widget.ImageView f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ java.lang.String f$2;
    public final /* synthetic */ java.lang.String f$3;

    public /* synthetic */ AvatarHook$2$$ExternalSyntheticLambda0(android.widget.ImageView imageView, int i, java.lang.String str, java.lang.String str2) {
        this.f$0 = imageView;
        this.f$1 = i;
        this.f$2 = str;
        this.f$3 = str2;
    }

    @Override // android.view.View.OnLongClickListener
    public final boolean onLongClick(android.view.View view) {
        return com.tencent.qqlitepro.hook.AvatarHook.AnonymousClass2.lambda$afterHookedMethod$0(this.f$0, this.f$1, this.f$2, this.f$3, view);
    }
}
