package com.tencent.qqlitepro.http;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class HttpApiServer$$ExternalSyntheticLambda1 implements java.lang.Runnable {
    public final /* synthetic */ java.net.Socket f$0;

    public /* synthetic */ HttpApiServer$$ExternalSyntheticLambda1(java.net.Socket socket) {
        this.f$0 = socket;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.tencent.qqlitepro.http.HttpApiServer.handle(this.f$0);
    }
}
