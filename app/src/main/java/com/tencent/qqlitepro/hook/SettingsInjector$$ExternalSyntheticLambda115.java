package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda115 implements com.tencent.qqlitepro.hook.SettingsInjector.ThConsumer {
    public final /* synthetic */ android.view.View[] f$0;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda115(android.view.View[] viewArr) {
        this.f$0 = viewArr;
    }

    @Override // com.tencent.qqlitepro.hook.SettingsInjector.ThConsumer
    public final void accept(boolean z) {
        com.tencent.qqlitepro.hook.SettingsInjector.lambda$addKeywordRuleRow$46(this.f$0, z);
    }
}
