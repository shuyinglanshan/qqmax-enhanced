package com.tencent.qqlitepro.cache;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class MsgCache$$ExternalSyntheticLambda2 implements java.lang.Runnable {
    public final /* synthetic */ com.tencent.qqlitepro.cache.MsgCache f$0;

    public /* synthetic */ MsgCache$$ExternalSyntheticLambda2(com.tencent.qqlitepro.cache.MsgCache msgCache) {
        this.f$0 = msgCache;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.persist();
    }
}
