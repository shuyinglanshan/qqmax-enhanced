package com.tencent.qqlitepro;

/* JADX INFO: loaded from: classes.dex */
public class StatusProvider extends android.content.ContentProvider {
    private static volatile int sReportedFw = -1;
    private static volatile android.os.IBinder sToken;
    private static volatile boolean sTokenAlive;
    private final android.os.IBinder.DeathRecipient deathRecipient = new com.tencent.qqlitepro.StatusProvider.AnonymousClass1();

    @Override // android.content.ContentProvider
    public java.lang.String getType(android.net.Uri uri) {
        return "text/plain";
    }

    @Override // android.content.ContentProvider
    public boolean onCreate() {
        return true;
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.StatusProvider$1, reason: invalid class name */
    class AnonymousClass1 implements android.os.IBinder.DeathRecipient {
        AnonymousClass1() {
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            synchronized (com.tencent.qqlitepro.StatusProvider.class) {
                com.tencent.qqlitepro.StatusProvider.sToken = null;
                com.tencent.qqlitepro.StatusProvider.sTokenAlive = false;
                com.tencent.qqlitepro.StatusProvider.sReportedFw = -1;
            }
            android.util.Log.i("AntiRecall", "[AntiRecall] 宿主进程已退出，注入状态立即置为未注入");
        }
    }

    @Override // android.content.ContentProvider
    public android.os.Bundle call(java.lang.String str, java.lang.String str2, android.os.Bundle bundle) {
        int i;
        android.os.Bundle bundle2 = new android.os.Bundle();
        if ("report_injected".equals(str)) {
            bindToken(bundle == null ? null : bundle.getBinder("token"));
            if (bundle != null && (i = bundle.getInt("fw", -1)) >= 0) {
                sReportedFw = i;
            }
            boolean alive = sToken != null && sTokenAlive;
            bundle2.putBoolean("injected", alive);
            /*
             * 由模块进程自己落盘"曾经注入成功"，供宿主未运行时仍能显示"已激活"。
             * 不能在宿主进程写：那里的 getSharedPreferences 指向宿主包目录，模块读不到。
             */
            if (alive) {
                try {
                    android.content.Context ctx = getContext();
                    if (ctx != null) {
                        com.tencent.qqlitepro.util.Status.markEverInjected(ctx);
                    }
                } catch (java.lang.Throwable ignored) {
                }
            }
        } else if ("get_status".equals(str)) {
            boolean z = sToken != null && sTokenAlive;
            bundle2.putBoolean("injected", z);
            bundle2.putInt("fw", z ? sReportedFw : -1);
        }
        return bundle2;
    }

    private void bindToken(android.os.IBinder iBinder) {
        if (iBinder == null) {
            return;
        }
        synchronized (com.tencent.qqlitepro.StatusProvider.class) {
            if (iBinder == sToken) {
                return;
            }
            android.os.IBinder iBinder2 = sToken;
            sToken = iBinder;
            if (iBinder2 != null) {
                try {
                    iBinder2.unlinkToDeath(this.deathRecipient, 0);
                } catch (java.lang.Throwable unused) {
                }
            }
            try {
                iBinder.linkToDeath(this.deathRecipient, 0);
                sTokenAlive = true;
            } catch (java.lang.Throwable unused2) {
                sTokenAlive = false;
            }
        }
    }

    @Override // android.content.ContentProvider
    public android.database.Cursor query(android.net.Uri uri, java.lang.String[] strArr, java.lang.String str, java.lang.String[] strArr2, java.lang.String str2) {
        throw new java.lang.UnsupportedOperationException("unsupported");
    }

    @Override // android.content.ContentProvider
    public android.net.Uri insert(android.net.Uri uri, android.content.ContentValues contentValues) {
        throw new java.lang.UnsupportedOperationException("unsupported");
    }

    @Override // android.content.ContentProvider
    public int delete(android.net.Uri uri, java.lang.String str, java.lang.String[] strArr) {
        throw new java.lang.UnsupportedOperationException("unsupported");
    }

    @Override // android.content.ContentProvider
    public int update(android.net.Uri uri, android.content.ContentValues contentValues, java.lang.String str, java.lang.String[] strArr) {
        throw new java.lang.UnsupportedOperationException("unsupported");
    }
}
