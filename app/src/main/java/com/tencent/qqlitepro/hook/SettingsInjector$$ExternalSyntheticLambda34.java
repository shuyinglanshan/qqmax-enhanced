package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda34 implements android.view.View.OnClickListener {
    public final /* synthetic */ android.content.Context f$0;
    public final /* synthetic */ java.lang.String f$1;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda34(android.content.Context context, java.lang.String str) {
        this.f$0 = context;
        this.f$1 = str;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(android.view.View view) {
        com.tencent.qqlitepro.hook.SettingsInjector.lambda$renderAudioList$113(this.f$0, this.f$1, view);
    }
}
