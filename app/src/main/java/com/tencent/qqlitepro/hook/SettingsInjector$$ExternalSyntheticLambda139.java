package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda139 implements android.widget.CompoundButton.OnCheckedChangeListener {
    public final /* synthetic */ com.tencent.qqlitepro.hook.SettingsInjector.ThConsumer f$0;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda139(com.tencent.qqlitepro.hook.SettingsInjector.ThConsumer thConsumer) {
        this.f$0 = thConsumer;
    }

    @Override // android.widget.CompoundButton.OnCheckedChangeListener
    public final void onCheckedChanged(android.widget.CompoundButton compoundButton, boolean z) {
        this.f$0.accept(z);
    }
}
