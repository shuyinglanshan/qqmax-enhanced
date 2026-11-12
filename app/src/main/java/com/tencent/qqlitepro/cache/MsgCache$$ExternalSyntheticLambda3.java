package com.tencent.qqlitepro.cache;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class MsgCache$$ExternalSyntheticLambda3 implements java.util.Comparator {
    @Override // java.util.Comparator
    public final int compare(java.lang.Object obj, java.lang.Object obj2) {
        return java.lang.Long.compare(((com.tencent.qqlitepro.cache.CachedMsg) obj).timestamp, ((com.tencent.qqlitepro.cache.CachedMsg) obj2).timestamp);
    }
}
