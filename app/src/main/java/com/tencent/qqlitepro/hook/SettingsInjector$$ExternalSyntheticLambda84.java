package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda84 implements android.widget.CompoundButton.OnCheckedChangeListener {
    public final /* synthetic */ android.widget.LinearLayout f$0;
    public final /* synthetic */ android.view.View f$1;
    public final /* synthetic */ android.content.Context f$2;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda84(android.widget.LinearLayout linearLayout, android.view.View view, android.content.Context context) {
        this.f$0 = linearLayout;
        this.f$1 = view;
        this.f$2 = context;
    }

    @Override // android.widget.CompoundButton.OnCheckedChangeListener
    public final void onCheckedChanged(android.widget.CompoundButton compoundButton, boolean z) {
        com.tencent.qqlitepro.hook.SettingsInjector.lambda$buildFang$28(this.f$0, this.f$1, this.f$2, compoundButton, z);
    }
}
