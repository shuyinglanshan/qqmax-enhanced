package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda157 implements com.tencent.qqlitepro.hook.SettingsInjector.ThConsumer {
    public final /* synthetic */ android.content.Context f$0;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda157(android.content.Context context) {
        this.f$0 = context;
    }

    @Override // com.tencent.qqlitepro.hook.SettingsInjector.ThConsumer
    public final void accept(boolean z) {
        com.tencent.qqlitepro.util.Config.setAutoTaskEnabled(this.f$0, z);
    }
}
