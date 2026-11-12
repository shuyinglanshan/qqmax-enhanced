package com.tencent.qqlitepro.hook;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class SettingsInjector$$ExternalSyntheticLambda176 implements java.lang.Runnable {
    public final /* synthetic */ android.content.Context f$0;
    public final /* synthetic */ android.os.Handler f$1;
    public final /* synthetic */ java.lang.String f$2;
    public final /* synthetic */ java.util.List f$3;
    public final /* synthetic */ long f$4;

    public /* synthetic */ SettingsInjector$$ExternalSyntheticLambda176(android.content.Context context, android.os.Handler handler, java.lang.String str, java.util.List list, long j) {
        this.f$0 = context;
        this.f$1 = handler;
        this.f$2 = str;
        this.f$3 = list;
        this.f$4 = j;
    }

    @Override // java.lang.Runnable
    public final void run() {
        try {
            com.tencent.qqlitepro.hook.SettingsInjector.lambda$sendFilesToSession$103(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4);
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("发送文件线程异常: " + th);
        }
    }
}
