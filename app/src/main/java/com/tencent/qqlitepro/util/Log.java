package com.tencent.qqlitepro.util;

/* JADX INFO: loaded from: classes.dex */
public final class Log {
    private static final java.lang.String FALLBACK_PKG = "com.tencent.qqlite";
    private static final java.lang.String FILE_NAME = "anti_recall.log";
    private static final long MAX_FILE_BYTES = 8388608;
    private static final java.lang.String TAG = "[AntiRecall]";
    private static java.io.File sFile;
    private static java.io.OutputStream sFileOut;
    private static long sNextOpenRetryAt;
    private static long sWritten;
    private static volatile java.lang.reflect.Method sXposedLog;
    private static volatile long sXposedLogRetryAt;
    private static final java.lang.Object FILE_LOCK = new java.lang.Object();
    private static final java.lang.ThreadLocal<java.text.SimpleDateFormat> TS = new com.tencent.qqlitepro.util.Log.AnonymousClass1();

    /* JADX INFO: renamed from: com.tencent.qqlitepro.util.Log$1, reason: invalid class name */
    static class AnonymousClass1 extends java.lang.ThreadLocal<java.text.SimpleDateFormat> {
        AnonymousClass1() {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // java.lang.ThreadLocal
        public java.text.SimpleDateFormat initialValue() {
            return new java.text.SimpleDateFormat("HH:mm:ss.SSS", java.util.Locale.getDefault());
        }
    }

    private Log() {
    }

    private static java.lang.String tag() {
        java.lang.String str = "";
        try {
            str = java.lang.System.getProperty("antirecall.tag", "") + " ";
        } catch (java.lang.Throwable unused) {
        }
        return str + TAG;
    }

    public static void v(java.lang.String str) {
        xlog("  V " + str);
    }

    public static void i(java.lang.String str) {
        xlog("  I " + str);
    }

    public static void w(java.lang.String str) {
        xlog("  W " + str);
    }

    public static void e(java.lang.String str) {
        xlog("  E " + str);
    }

    public static void e(java.lang.String str, java.lang.Throwable th) {
        xlog("  E " + str + "  cause=" + th);
    }

    public static void dumpMethod(java.lang.Object obj, java.lang.String str) {
        if (obj == null) {
            xlog("  DUMP " + str + " -> null");
            return;
        }
        try {
            java.lang.reflect.Method[] declaredMethods = obj.getClass().getDeclaredMethods();
            java.lang.StringBuilder sb = new java.lang.StringBuilder("  DUMP " + str + " (class=" + obj.getClass().getName() + ") :");
            for (java.lang.reflect.Method method : declaredMethods) {
                sb.append('\n');
                sb.append("    ");
                sb.append(method.getName());
                sb.append("(");
                sb.append(com.tencent.qqlitepro.util.Log$$ExternalSyntheticBackport0.m(",", stringify(method.getParameterTypes())));
                sb.append(")");
            }
            xlog(sb.toString());
        } catch (java.lang.Throwable th) {
            xlog("  DUMP " + str + " failed " + th);
        }
    }

    public static void dumpFields(java.lang.Object obj, java.lang.String str) {
        if (obj == null) {
            xlog("  FIELDS " + str + " -> null");
            return;
        }
        try {
            java.lang.Class<?> cls = obj.getClass();
            java.lang.StringBuilder sb = new java.lang.StringBuilder("  FIELDS " + str + " (class=" + cls.getName() + ") :");
            for (java.lang.reflect.Field field : cls.getDeclaredFields()) {
                try {
                    field.setAccessible(true);
                    java.lang.Object obj2 = field.get(obj);
                    sb.append('\n');
                    sb.append("    ");
                    sb.append(field.getName());
                    sb.append(" = ");
                    sb.append(java.lang.String.valueOf(obj2));
                } catch (java.lang.Throwable unused) {
                }
            }
            xlog(sb.toString());
        } catch (java.lang.Throwable th) {
            xlog("  FIELDS " + str + " failed " + th);
        }
    }

    public static void dumpClass(java.lang.String str) {
        try {
            java.lang.Class<?> cls = java.lang.Class.forName(str);
            java.lang.StringBuilder sb = new java.lang.StringBuilder("  CLASS " + str + " ->");
            for (java.lang.reflect.Method method : cls.getDeclaredMethods()) {
                sb.append('\n');
                sb.append("    ");
                sb.append(method.getName());
                sb.append("(");
                sb.append(com.tencent.qqlitepro.util.Log$$ExternalSyntheticBackport0.m(",", stringify(method.getParameterTypes())));
                sb.append(")");
            }
            xlog(sb.toString());
        } catch (java.lang.Throwable th) {
            xlog("  CLASS " + str + " not found: " + th);
        }
    }

    private static java.lang.String[] stringify(java.lang.Class<?>[] clsArr) {
        java.lang.String[] strArr = new java.lang.String[clsArr.length];
        for (int i = 0; i < clsArr.length; i++) {
            strArr[i] = clsArr[i].getSimpleName();
        }
        return strArr;
    }

    public static java.lang.String logFilePath() {
        java.lang.String absolutePath;
        synchronized (FILE_LOCK) {
            java.io.File file = sFile;
            absolutePath = file == null ? "(未启用)" : file.getAbsolutePath();
        }
        return absolutePath;
    }

    private static void xlog(java.lang.String str) {
        enqueue(str);
        try {
            android.util.Log.i("AntiRecall", tag() + str);
        } catch (java.lang.Throwable unused) {
        }
        java.lang.reflect.Method method = sXposedLog;
        if (method == null && java.lang.System.currentTimeMillis() >= sXposedLogRetryAt) {
            try {
                method = java.lang.Class.forName("de.robv.android.xposed.XposedBridge").getMethod("log", java.lang.String.class);
                sXposedLog = method;
            } catch (java.lang.Throwable unused2) {
                sXposedLogRetryAt = java.lang.System.currentTimeMillis() + 30000;
            }
        }
        if (method != null) {
            try {
                method.invoke(null, tag() + str);
            } catch (java.lang.Throwable unused3) {
            }
        }
    }

    /*
     * 日志写入改为异步，避免阻塞主线程。
     *
     * 原实现：调用线程（常常是 UI 线程）直接 open/write/flush，
     * 日志一多就会造成明显卡顿（尤其 hook 回调里高频打日志时）。
     * 现在：调用线程只做一次入队（O(1)，无锁竞争用 ConcurrentLinkedQueue），
     * 由单独的低优先级守护线程批量落盘并定期 flush。
     */
    private static final java.util.concurrent.ConcurrentLinkedQueue<byte[]> QUEUE =
            new java.util.concurrent.ConcurrentLinkedQueue<>();
    private static final int QUEUE_MAX = 4000;
    private static volatile boolean sWriterStarted = false;

    private static void ensureWriter() {
        if (sWriterStarted) {
            return;
        }
        synchronized (Log.class) {
            if (sWriterStarted) {
                return;
            }
            sWriterStarted = true;
            java.lang.Thread t = new java.lang.Thread(new java.lang.Runnable() {
                @Override
                public void run() {
                    writerLoop();
                }
            }, "qqmax-log-writer");
            t.setDaemon(true);
            // 略低优先级，尽量不抢 UI 线程
            try {
                t.setPriority(java.lang.Thread.MIN_PRIORITY);
            } catch (java.lang.Throwable ignored) {
            }
            t.start();
        }
    }

    private static void writerLoop() {
        while (true) {
            try {
                byte[] first = QUEUE.poll();
                if (first == null) {
                    java.lang.Thread.sleep(120L);
                    continue;
                }
                synchronized (FILE_LOCK) {
                    java.io.OutputStream out = openIfNeeded();
                    if (out == null) {
                        continue;
                    }
                    out.write(first);
                    long batchBytes = first.length;
                    // 批量消费，减少 write/flush 次数
                    byte[] next;
                    int n = 0;
                    while (n++ < 256 && (next = QUEUE.poll()) != null) {
                        out.write(next);
                        batchBytes += next.length;
                    }
                    out.flush();
                    sWritten += batchBytes;
                    if (sWritten > 8388608) {
                        rotateLocked();
                    }
                }
            } catch (java.lang.InterruptedException ignored) {
            } catch (java.lang.Throwable ignored) {
            }
        }
    }

    private static void appendToFile(java.lang.String str) {
        try {
            java.lang.StringBuilder sb = new java.lang.StringBuilder(str.length() + 40);
            sb.append(TS.get().format(new java.util.Date()));
            sb.append(" [");
            sb.append(java.lang.Thread.currentThread().getId());
            sb.append("] ");
            sb.append(str);
            sb.append('\n');
            byte[] bytes = sb.toString().getBytes("UTF-8");
            synchronized (FILE_LOCK) {
                java.io.OutputStream outputStreamOpenIfNeeded = openIfNeeded();
                if (outputStreamOpenIfNeeded == null) {
                    return;
                }
                outputStreamOpenIfNeeded.write(bytes);
                outputStreamOpenIfNeeded.flush();
                long length = sWritten + ((long) bytes.length);
                sWritten = length;
                if (length > 8388608) {
                    rotateLocked();
                }
            }
        } catch (java.lang.Throwable unused) {
        }
    }

    /** 仅入队，绝不做 IO —— 保证主线程零阻塞 */
    private static void enqueue(java.lang.String str) {
        try {
            if (QUEUE.size() >= QUEUE_MAX) {
                return;   // 队列积压时丢弃新日志，优先保性能
            }
            java.lang.StringBuilder sb = new java.lang.StringBuilder(str.length() + 40);
            sb.append(TS.get().format(new java.util.Date()));
            sb.append(" [");
            sb.append(java.lang.Thread.currentThread().getId());
            sb.append("] ");
            sb.append(str);
            sb.append('\n');
            QUEUE.offer(sb.toString().getBytes("UTF-8"));
            ensureWriter();
        } catch (java.lang.Throwable ignored) {
        }
    }

    private static java.io.OutputStream openIfNeeded() {
        java.io.OutputStream outputStream = sFileOut;
        if (outputStream != null) {
            return outputStream;
        }
        long jCurrentTimeMillis = java.lang.System.currentTimeMillis();
        if (jCurrentTimeMillis < sNextOpenRetryAt) {
            return null;
        }
        for (java.io.File file : candidates()) {
            try {
                java.io.File parentFile = file.getParentFile();
                if (parentFile == null || parentFile.exists() || parentFile.mkdirs()) {
                    java.io.FileOutputStream fileOutputStream = new java.io.FileOutputStream(file, true);
                    sFile = file;
                    sFileOut = fileOutputStream;
                    sWritten = file.length();
                    android.util.Log.i("AntiRecall", "[AntiRecall] 日志文件已启用：" + file.getAbsolutePath());
                    return fileOutputStream;
                }
            } catch (java.lang.Throwable unused) {
            }
        }
        sNextOpenRetryAt = jCurrentTimeMillis + 10000;
        return null;
    }

    private static java.util.List<java.io.File> candidates() {
        java.lang.String packageName;
        java.util.ArrayList arrayList = new java.util.ArrayList();
        /* 首选：/sdcard/QQMax增强/logs/qqmax_YYYYMMDD.log（便于用户直接取日志） */
        try {
            java.io.File ext = android.os.Environment.getExternalStorageDirectory();
            if (ext != null) {
                java.io.File dir = new java.io.File(ext, "QQMax增强/logs");
                if (dir.exists() || dir.mkdirs()) {
                    java.text.SimpleDateFormat dsf =
                            new java.text.SimpleDateFormat("yyyyMMdd", java.util.Locale.US);
                    arrayList.add(new java.io.File(dir, "qqmax_" + dsf.format(new java.util.Date()) + ".log"));
                }
            }
        } catch (java.lang.Throwable ignored) {
        }
        android.content.Context contextAppContext = appContext();
        java.lang.String str = null;
        if (contextAppContext != null) {
            try {
                packageName = contextAppContext.getPackageName();
            } catch (java.lang.Throwable unused) {
                packageName = null;
            }
            try {
                java.io.File externalFilesDir = contextAppContext.getExternalFilesDir(null);
                if (externalFilesDir != null) {
                    arrayList.add(new java.io.File(externalFilesDir, FILE_NAME));
                }
            } catch (java.lang.Throwable unused2) {
            }
            str = packageName;
        }
        if (str == null || str.isEmpty()) {
            str = FALLBACK_PKG;
        }
        try {
            java.io.File externalStorageDirectory = android.os.Environment.getExternalStorageDirectory();
            if (externalStorageDirectory != null) {
                arrayList.add(new java.io.File(externalStorageDirectory, "Android/data/" + str + "/files/anti_recall.log"));
                arrayList.add(new java.io.File(externalStorageDirectory, FILE_NAME));
            }
        } catch (java.lang.Throwable unused3) {
        }
        if (contextAppContext != null) {
            try {
                java.io.File filesDir = contextAppContext.getFilesDir();
                if (filesDir != null) {
                    arrayList.add(new java.io.File(filesDir, FILE_NAME));
                }
            } catch (java.lang.Throwable unused4) {
            }
        }
        return arrayList;
    }

    private static void rotateLocked() {
        try {
            java.io.OutputStream outputStream = sFileOut;
            if (outputStream != null) {
                outputStream.close();
            }
        } catch (java.lang.Throwable unused) {
        }
        sFileOut = null;
        try {
            if (sFile != null) {
                java.io.File file = new java.io.File(sFile.getAbsolutePath() + ".1");
                if (file.exists()) {
                    file.delete();
                }
                sFile.renameTo(file);
            }
        } catch (java.lang.Throwable unused2) {
        }
        sWritten = 0L;
    }

    private static android.content.Context appContext() {
        try {
            java.lang.reflect.Method method = java.lang.Class.forName("android.app.ActivityThread").getMethod("currentApplication", new java.lang.Class[0]);
            method.setAccessible(true);
            return (android.content.Context) method.invoke(null, new java.lang.Object[0]);
        } catch (java.lang.Throwable unused) {
            return null;
        }
    }
}
