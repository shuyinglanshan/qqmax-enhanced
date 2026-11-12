package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda225 implements java.lang.Runnable {
    public final /* synthetic */ android.content.Context f$0;
    public final /* synthetic */ android.widget.LinearLayout f$1;
    public final /* synthetic */ android.widget.TextView f$2;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda225(android.content.Context context, android.widget.LinearLayout linearLayout, android.widget.TextView textView) {
        this.f$0 = context;
        this.f$1 = linearLayout;
        this.f$2 = textView;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.SettingsInjector.renderAudioList(this.f$0, this.f$1, this.f$2);
    }
}
