package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda4 implements android.view.View.OnClickListener {
    public final /* synthetic */ android.content.Context f$0;
    public final /* synthetic */ android.widget.TextView f$1;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda4(android.content.Context context, android.widget.TextView textView) {
        this.f$0 = context;
        this.f$1 = textView;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(android.view.View view) {
        android.content.Context context = this.f$0;
        android.widget.TextView textView = this.f$1;
        com.tencent.qqlitepro.hook.SettingsInjector.copyTextToClipboard(context, textView == null ? "" : java.lang.String.valueOf(textView.getText()));
    }
}
