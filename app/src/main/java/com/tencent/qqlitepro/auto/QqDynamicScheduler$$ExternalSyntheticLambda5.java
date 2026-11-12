package com.tencent.qqlitepro.auto;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class QqDynamicScheduler$$ExternalSyntheticLambda5 implements java.util.Comparator {
    @Override // java.util.Comparator
    public final int compare(java.lang.Object obj, java.lang.Object obj2) {
        return java.lang.Long.compare(((com.tencent.qqlitepro.auto.QqDynamicScheduler.FeedRecord) obj2).timeMs, ((com.tencent.qqlitepro.auto.QqDynamicScheduler.FeedRecord) obj).timeMs);
    }
}
