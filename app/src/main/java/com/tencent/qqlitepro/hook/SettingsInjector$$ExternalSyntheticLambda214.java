package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda214 implements android.view.View.OnClickListener {
    public final /* synthetic */ long f$0;
    public final /* synthetic */ java.lang.String f$1;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda214(long j, java.lang.String str) {
        this.f$0 = j;
        this.f$1 = str;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(android.view.View view) {
        com.tencent.qqlitepro.hook.SettingsInjector.openMailHtmlInBrowser(view.getContext(), this.f$0, this.f$1);
    }
}
