package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda54 implements com.tencent.qqlitepro.hook.SettingsInjector.SelectableTextView.Listener {
    public final /* synthetic */ android.widget.TextView f$0;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda54(android.widget.TextView textView) {
        this.f$0 = textView;
    }

    @Override // com.tencent.qqlitepro.hook.SettingsInjector.SelectableTextView.Listener
    public final void onSelectionChanged(int i, int i2) {
        com.tencent.qqlitepro.hook.SettingsInjector.lambda$showTextCopyDialog$15(this.f$0, i, i2);
    }
}
