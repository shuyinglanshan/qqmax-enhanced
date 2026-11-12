package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class ForceNoticeInjector$$ExternalSyntheticLambda7 implements java.util.Comparator {
    @Override // java.util.Comparator
    public final int compare(java.lang.Object obj, java.lang.Object obj2) {
        return java.lang.Integer.compare(com.tencent.qqlitepro.hook.ForceNoticeInjector.noticeId((com.google.gson.JsonObject) obj2), com.tencent.qqlitepro.hook.ForceNoticeInjector.noticeId((com.google.gson.JsonObject) obj));
    }
}
