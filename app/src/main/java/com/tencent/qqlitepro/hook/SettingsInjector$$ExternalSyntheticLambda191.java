package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda191 implements java.lang.Runnable {
    public final /* synthetic */ android.widget.TextView f$0;
    public final /* synthetic */ java.util.List f$1;
    public final /* synthetic */ android.content.Context f$2;
    public final /* synthetic */ android.view.ViewGroup f$3;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda191(android.widget.TextView textView, java.util.List list, android.content.Context context, android.view.ViewGroup viewGroup) {
        this.f$0 = textView;
        this.f$1 = list;
        this.f$2 = context;
        this.f$3 = viewGroup;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.hook.SettingsInjector.lambda$buildNotice$57(this.f$0, this.f$1, this.f$2, this.f$3);
    }
}
