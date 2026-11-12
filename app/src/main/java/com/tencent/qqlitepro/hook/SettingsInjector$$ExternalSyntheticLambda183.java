package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda183 implements java.lang.Runnable {
    public final /* synthetic */ android.widget.LinearLayout f$0;
    public final /* synthetic */ java.util.List f$1;
    public final /* synthetic */ android.content.Context f$2;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda183(android.widget.LinearLayout linearLayout, java.util.List list, android.content.Context context) {
        this.f$0 = linearLayout;
        this.f$1 = list;
        this.f$2 = context;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.SettingsInjector.addSettingRow(this.f$0, this.f$1, "", "新设置项", "", false, this.f$2);
    }
}
