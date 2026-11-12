package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda197 implements android.view.View.OnClickListener {
    public final /* synthetic */ android.widget.LinearLayout f$0;
    public final /* synthetic */ android.widget.LinearLayout f$1;
    public final /* synthetic */ java.util.List f$2;
    public final /* synthetic */ android.widget.EditText f$3;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda197(android.widget.LinearLayout linearLayout, android.widget.LinearLayout linearLayout2, java.util.List list, android.widget.EditText editText) {
        this.f$0 = linearLayout;
        this.f$1 = linearLayout2;
        this.f$2 = list;
        this.f$3 = editText;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(android.view.View view) {
        com.tencent.qqlitepro.hook.SettingsInjector.lambda$addSettingRow$199(this.f$0, this.f$1, this.f$2, this.f$3, view);
    }
}
