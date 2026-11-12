package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda90 implements android.widget.TextView.OnEditorActionListener {
    public final /* synthetic */ android.widget.LinearLayout f$0;
    public final /* synthetic */ android.widget.TextView f$1;
    public final /* synthetic */ java.util.List f$2;
    public final /* synthetic */ android.widget.EditText f$3;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda90(android.widget.LinearLayout linearLayout, android.widget.TextView textView, java.util.List list, android.widget.EditText editText) {
        this.f$0 = linearLayout;
        this.f$1 = textView;
        this.f$2 = list;
        this.f$3 = editText;
    }

    @Override // android.widget.TextView.OnEditorActionListener
    public final boolean onEditorAction(android.widget.TextView textView, int i, android.view.KeyEvent keyEvent) {
        return com.tencent.qqlitepro.hook.SettingsInjector.lambda$showLegacySessionPicker$3(this.f$0, this.f$1, this.f$2, this.f$3, textView, i, keyEvent);
    }
}
