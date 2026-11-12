package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda142 implements android.view.View.OnClickListener {
    public final /* synthetic */ com.google.gson.JsonObject f$0;
    public final /* synthetic */ android.view.ViewGroup f$1;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda142(com.google.gson.JsonObject jsonObject, android.view.ViewGroup viewGroup) {
        this.f$0 = jsonObject;
        this.f$1 = viewGroup;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(android.view.View view) {
        com.tencent.qqlitepro.hook.SettingsInjector.lambda$renderNoticeCards$74(this.f$0, this.f$1, view);
    }
}
