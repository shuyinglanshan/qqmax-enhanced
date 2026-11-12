package com.tencent.qqlitepro.http;

/* JADX INFO: loaded from: classes.dex */
public final class FakeVoice {
    public static final java.lang.String BASE_DIR = "/sdcard/QQMax增强";
    public static final java.lang.String DOWNLOAD_DIR = "/sdcard/QQMax增强/downloads";
    public static final java.lang.String PARTS_DIR = "/sdcard/QQMax增强/downloads/.parts";
    public static final long SHARD_BYTES = 8388608;
    private static final java.lang.String SHARD_PATH_FILE = "/sdcard/QQMax增强/tmp/.shard_paths_voi.map";
    private static final java.lang.String TMP_DIR = "/sdcard/QQMax增强/tmp";
    /*
     * 路径修复：原实现把目录写死成 /sdcard/QQMax增强/...。
     * Android 10+ 分区存储下应用对该路径无写权限，mkdirs 失败后
     * FileOutputStream 会抛 ENOENT，表现为"未找到路径"。
     * 这里保留原常量（兼容外部引用），但内部统一走 Storage 解析出的可写目录。
     */
    private static android.content.Context ctx() {
        return com.tencent.qqlitepro.cache.MsgCache.appContext();
    }

    private static java.io.File dirBase(android.content.Context ctx) {
        return com.tencent.qqlitepro.util.Storage.dir(ctx, "");
    }

    private static java.io.File dirDownloads(android.content.Context ctx) {
        return com.tencent.qqlitepro.util.Storage.dir(ctx, "downloads");
    }

    private static java.io.File dirParts(android.content.Context ctx) {
        return com.tencent.qqlitepro.util.Storage.dir(ctx, "downloads/.parts");
    }

    private static java.io.File dirTmp(android.content.Context ctx) {
        return com.tencent.qqlitepro.util.Storage.dir(ctx, "tmp");
    }

    private static java.io.File fileShardMap(android.content.Context ctx) {
        return new java.io.File(dirTmp(ctx), ".shard_paths_voi.map");
    }

    private static final byte[] MAGIC = {81, 81, 76, 77, 70, 65, 75, 69};
    private static final java.util.concurrent.ConcurrentHashMap<java.lang.String, java.util.concurrent.ConcurrentHashMap<java.lang.Integer, java.lang.String>> sShardPaths = new java.util.concurrent.ConcurrentHashMap<>();
    private static volatile boolean sShardPathsLoaded = false;

    public static final class OriginalInfo {
        public int count;
        public int index;
        public java.lang.String md5;
        public java.lang.String name;
        public long payloadOffset;
        public boolean shard;
        public java.lang.String shardMd5;
        public long shardSize;
        public long size;
        public long time;
    }

    public interface RestoreProgress {
        void onProgress(int i, java.lang.String str, long j, long j2);
    }

    public static final class Wrapped {
        public java.io.File amrFile;
        public int count;
        public java.lang.String md5;
        public java.lang.String originalName;
        public boolean shard;
    }

    private static byte[] intToU16Be(int i) {
        return new byte[]{(byte) ((i >> 8) & org.mozilla.classfile.ByteCode.IMPDEP2), (byte) (i & org.mozilla.classfile.ByteCode.IMPDEP2)};
    }

    public static java.lang.String formatSize(long j) {
        if (j < 1024) {
            return j + " B";
        }
        double d = j / 1024.0d;
        if (d < 1024.0d) {
            return java.lang.String.format(java.util.Locale.US, "%.1f KB", java.lang.Double.valueOf(d));
        }
        double d2 = d / 1024.0d;
        return d2 < 1024.0d ? java.lang.String.format(java.util.Locale.US, "%.1f MB", java.lang.Double.valueOf(d2)) : java.lang.String.format(java.util.Locale.US, "%.2f GB", java.lang.Double.valueOf(d2 / 1024.0d));
    }

    public static java.lang.String extensionOf(java.lang.String str) {
        int iLastIndexOf;
        if (str == null || (iLastIndexOf = str.lastIndexOf(46)) < 0 || iLastIndexOf == str.length() - 1) {
            return "未知";
        }
        java.lang.String strSubstring = str.substring(iLastIndexOf + 1);
        return strSubstring.isEmpty() ? "未知" : strSubstring.toUpperCase(java.util.Locale.US);
    }

    public static java.lang.String formatTime(long j) {
        if (j <= 0) {
            return "未知";
        }
        try {
            return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.US).format(new java.util.Date(j));
        } catch (java.lang.Throwable unused) {
            return "未知";
        }
    }

    private FakeVoice() {
    }

    public static com.tencent.qqlitepro.http.FakeVoice.Wrapped wrapToFakeVoice(java.lang.String str) {
        com.tencent.qqlitepro.http.FakeVoice.Wrapped wrapped = new com.tencent.qqlitepro.http.FakeVoice.Wrapped();
        java.io.File file = new java.io.File(str);
        if (file.exists() && file.isFile()) {
            try {
                wrapped.originalName = file.getName();
                wrapped.md5 = md5Hex(file);
                org.json.JSONObject jSONObject = new org.json.JSONObject();
                jSONObject.put("n", wrapped.originalName);
                jSONObject.put("m", wrapped.md5);
                jSONObject.put("s", file.length());
                jSONObject.put("t", java.lang.System.currentTimeMillis());
                byte[] bytes = jSONObject.toString().getBytes("UTF-8");
                byte[] bArrIntToU16Be = intToU16Be(bytes.length);
                java.io.File file2 = dirTmp(ctx());
                if (!file2.exists()) {
                    file2.mkdirs();
                }
                java.io.File file3 = new java.io.File(file2, "qqlm_" + java.lang.System.currentTimeMillis() + "_" + java.lang.Math.abs(wrapped.md5.hashCode()) + ".amr");
                java.io.FileOutputStream fileOutputStream = new java.io.FileOutputStream(file3);
                try {
                    java.io.FileInputStream fileInputStream = new java.io.FileInputStream(file);
                    try {
                        fileOutputStream.write(MAGIC);
                        fileOutputStream.write(bArrIntToU16Be);
                        fileOutputStream.write(bytes);
                        byte[] bArr = new byte[org.mozilla.javascript.Parser.ARGC_LIMIT];
                        while (true) {
                            int i = fileInputStream.read(bArr);
                            if (i > 0) {
                                fileOutputStream.write(bArr, 0, i);
                            } else {
                                fileOutputStream.flush();
                                fileInputStream.close();
                                fileOutputStream.close();
                                wrapped.amrFile = file3;
                                return wrapped;
                            }
                        }
                    } catch (java.lang.Throwable th2) {
                        try {
                            fileInputStream.close();
                        } catch (java.lang.Throwable th3) {
                            th2.addSuppressed(th3);
                        }
                        throw th2;
                    }
                } catch (java.lang.Throwable th4) {
                    fileOutputStream.close();
                    throw th4;
                }
            } catch (java.lang.Throwable th5) {
                com.tencent.qqlitepro.util.Log.w("封装伪语音失败: " + th5);
            }
        }
        return null;
    }

    public static boolean isFakeVoice(java.lang.String str) {
        return parseOriginalInfo(str) != null;
    }

    public static com.tencent.qqlitepro.http.FakeVoice.OriginalInfo parseOriginalInfo(java.lang.String str) {
        if (str == null) {
            return null;
        }
        try {
            java.io.File file = new java.io.File(str);
            if (file.exists() && file.isFile()) {
                java.io.RandomAccessFile randomAccessFile = new java.io.RandomAccessFile(file, "r");
                try {
                    if (randomAccessFile.length() >= 10) {
                        byte[] bArr = new byte[8];
                        randomAccessFile.readFully(bArr);
                        if (java.util.Arrays.equals(bArr, MAGIC)) {
                            byte[] bArr2 = new byte[2];
                            randomAccessFile.readFully(bArr2);
                            int i = ((bArr2[0] & org.mozilla.classfile.ByteCode.IMPDEP2) << 8) | (bArr2[1] & org.mozilla.classfile.ByteCode.IMPDEP2);
                            if (i > 0 && i <= 65535) {
                                long j = i + 10;
                                if (j <= randomAccessFile.length()) {
                                    byte[] bArr3 = new byte[i];
                                    randomAccessFile.readFully(bArr3);
                                    org.json.JSONObject jSONObject = new org.json.JSONObject(new java.lang.String(bArr3, "UTF-8"));
                                    com.tencent.qqlitepro.http.FakeVoice.OriginalInfo originalInfo = new com.tencent.qqlitepro.http.FakeVoice.OriginalInfo();
                                    originalInfo.name = jSONObject.optString("n", null);
                                    originalInfo.md5 = jSONObject.optString("m", null);
                                    originalInfo.size = jSONObject.optLong("s", 0L);
                                    originalInfo.time = jSONObject.optLong("t", 0L);
                                    originalInfo.payloadOffset = j;
                                    originalInfo.shard = jSONObject.optInt("sh", 0) == 1;
                                    originalInfo.index = jSONObject.optInt("i", 0);
                                    originalInfo.count = jSONObject.optInt("c", 1);
                                    originalInfo.shardSize = jSONObject.optLong("fs", 0L);
                                    originalInfo.shardMd5 = jSONObject.optString("fm", null);
                                    if (originalInfo.name == null || originalInfo.name.isEmpty()) {
                                        randomAccessFile.close();
                                        return null;
                                    }
                                    randomAccessFile.close();
                                    return originalInfo;
                                }
                            }
                            randomAccessFile.close();
                            return null;
                        }
                        randomAccessFile.close();
                        return null;
                    }
                    randomAccessFile.close();
                    return null;
                } catch (java.lang.Throwable th) {
                    try {
                        randomAccessFile.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                    throw th;
                }
            }
        } catch (java.lang.Throwable unused) {
        }
        return null;
    }

    public static java.io.File restoreFromFakeVoice(java.lang.String str) {
        return restoreFromFakeVoice(str, dirDownloads(ctx()).getAbsolutePath(), true);
    }

    public static java.io.File restoreFromFakeVoice(java.lang.String str, java.lang.String str2, boolean z) {
        com.tencent.qqlitepro.http.FakeVoice.OriginalInfo originalInfo = parseOriginalInfo(str);
        if (originalInfo != null && originalInfo.payloadOffset >= 10) {
            java.io.File file = new java.io.File(str2);
            if (!file.exists() && !file.mkdirs()) {
                return null;
            }
            java.io.File file2 = new java.io.File(file, originalInfo.name);
            try {
                java.io.FileInputStream fileInputStream = new java.io.FileInputStream(str);
                try {
                    java.io.FileOutputStream fileOutputStream = new java.io.FileOutputStream(file2);
                    try {
                        fileInputStream.skip(originalInfo.payloadOffset);
                        byte[] bArr = new byte[org.mozilla.javascript.Parser.ARGC_LIMIT];
                        while (true) {
                            int i = fileInputStream.read(bArr);
                            if (i <= 0) {
                                break;
                            }
                            fileOutputStream.write(bArr, 0, i);
                        }
                        fileOutputStream.flush();
                        fileOutputStream.close();
                        fileInputStream.close();
                        if (z && originalInfo.md5 != null && !originalInfo.md5.isEmpty()) {
                            java.lang.String strMd5Hex = md5Hex(file2);
                            if (!originalInfo.md5.equalsIgnoreCase(strMd5Hex)) {
                                com.tencent.qqlitepro.util.Log.w("伪语音还原MD5不一致: want=" + originalInfo.md5 + " got=" + strMd5Hex);
                            }
                        }
                        return file2;
                    } catch (java.lang.Throwable th2) {
                        try {
                            fileOutputStream.close();
                        } catch (java.lang.Throwable th3) {
                            th2.addSuppressed(th3);
                        }
                        throw th2;
                    }
                } catch (java.lang.Throwable th4) {
                    fileInputStream.close();
                    throw th4;
                }
            } catch (java.lang.Throwable th5) {
                com.tencent.qqlitepro.util.Log.w("还原伪语音失败: " + th5);
            }
        }
        return null;
    }

    public static java.io.File restoreWithProgress(java.lang.String str, com.tencent.qqlitepro.http.FakeVoice.RestoreProgress restoreProgress) {
        return restoreWithProgress(str, dirDownloads(ctx()).getAbsolutePath(), restoreProgress);
    }

    public static java.io.File restoreWithProgress(java.lang.String str, java.lang.String str2, com.tencent.qqlitepro.http.FakeVoice.RestoreProgress restoreProgress) {
        java.io.FileOutputStream fileOutputStream;
        int iAutoCollectAllParts;
        com.tencent.qqlitepro.http.FakeVoice.OriginalInfo originalInfo = parseOriginalInfo(str);
        if (originalInfo == null) {
            return null;
        }
        java.io.File file = new java.io.File(str2);
        if (!file.exists() && !file.mkdirs()) {
            return null;
        }
        java.io.File file2 = new java.io.File(file, originalInfo.name);
        try {
            if (originalInfo.shard && originalInfo.count > 1) {
                collectPart(str);
                if (collectedParts(originalInfo) >= originalInfo.count || (iAutoCollectAllParts = autoCollectAllParts(originalInfo)) >= originalInfo.count) {
                    return mergePartsWithProgress(originalInfo.md5, originalInfo.name, originalInfo.count, originalInfo.size, str2, restoreProgress);
                }
                if (restoreProgress != null) {
                    restoreProgress.onProgress(0, "已归集 " + iAutoCollectAllParts + "/" + originalInfo.count + " 分片；请将其余分片消息滑动到可见（触发下载）后，长按任一分片重试", 0L, originalInfo.size);
                }
                return null;
            }
            long j = originalInfo.payloadOffset;
            long length = new java.io.File(str).length() - j;
            long j2 = length > 0 ? length : 0L;
            if (restoreProgress != null) {
                restoreProgress.onProgress(0, "正在还原文件", 0L, j2);
            }
            java.io.FileInputStream fileInputStream = new java.io.FileInputStream(str);
            java.io.FileOutputStream fileOutputStream2 = new java.io.FileOutputStream(file2);
            try {
                fileInputStream.skip(j);
                byte[] buffer = new byte[org.mozilla.javascript.Parser.ARGC_LIMIT];
                long copied = 0;
                int read;
                while ((read = fileInputStream.read(buffer)) > 0) {
                    fileOutputStream2.write(buffer, 0, read);
                    copied += read;
                    if (restoreProgress != null && j2 > 0) {
                        int percent = (int) ((100 * copied) / j2);
                        if (percent > 100) {
                            percent = 100;
                        }
                        try {
                            restoreProgress.onProgress(percent, "正在还原文件", copied, j2);
                        } catch (java.lang.Throwable unused) {
                        }
                    }
                }
                fileOutputStream2.flush();
            } finally {
                try {
                    fileOutputStream2.close();
                } catch (java.lang.Throwable unused2) {
                }
                try {
                    fileInputStream.close();
                } catch (java.lang.Throwable unused3) {
                }
            }
            if (restoreProgress != null) {
                restoreProgress.onProgress(92, "正在校验文件完整性", 0L, 0L);
            }
            if (originalInfo.md5 != null && !originalInfo.md5.isEmpty()) {
                java.lang.String actualMd5 = md5Hex(file2);
                if (!originalInfo.md5.equalsIgnoreCase(actualMd5)) {
                    com.tencent.qqlitepro.util.Log.w("伪语音还原MD5不一致: want=" + originalInfo.md5 + " got=" + actualMd5);
                }
            }
            if (restoreProgress != null) {
                restoreProgress.onProgress(100, "还原完成", j2, j2);
            }
            return file2;
        } catch (java.lang.Throwable th7) {
            com.tencent.qqlitepro.util.Log.w("带进度还原伪语音失败: " + th7);
            return null;
        }
    }

    public static java.io.File mergePartsWithProgress(java.lang.String str, java.lang.String str2, int i, long j, com.tencent.qqlitepro.http.FakeVoice.RestoreProgress restoreProgress) {
        return mergePartsWithProgress(str, str2, i, j, dirDownloads(ctx()).getAbsolutePath(), restoreProgress);
    }

    public static java.io.File mergePartsWithProgress(java.lang.String str, java.lang.String str2, int i, long j, java.lang.String str3, com.tencent.qqlitepro.http.FakeVoice.RestoreProgress restoreProgress) {
        long j2;
        boolean zEqualsIgnoreCase;
        java.io.FileOutputStream fileOutputStream;
        java.io.FileInputStream fileInputStream;
        java.io.File file = null;
        if (i <= 1 || str == null) {
            return null;
        }
        try {
            java.io.File file2 = new java.io.File(dirParts(ctx()), str);
            java.io.File file3 = new java.io.File(str3);
            if (!file3.exists() && !file3.mkdirs()) {
                return null;
            }
            java.io.File file4 = new java.io.File(file3, str2);
            if (j <= 0) {
                long length = 0;
                for (int i2 = 0; i2 < i; i2++) {
                    java.io.File file5 = new java.io.File(file2, i2 + ".part");
                    if (file5.exists()) {
                        length += file5.length();
                    }
                }
                j2 = length;
            } else {
                j2 = j;
            }
            if (restoreProgress != null) {
                restoreProgress.onProgress(0, "开始合并 " + i + " 分片", 0L, j2);
            }
            java.io.FileOutputStream fileOutputStream2 = new java.io.FileOutputStream(file4);
            long j3 = 0;
            try {
                for (int partIndex = 0; partIndex < i; partIndex++) {
                    java.io.File partFile = new java.io.File(file2, partIndex + ".part");
                    if (!partFile.exists()) {
                        continue;
                    }
                    java.io.FileInputStream partIn = new java.io.FileInputStream(partFile);
                    try {
                        byte[] buf = new byte[org.mozilla.javascript.Parser.ARGC_LIMIT];
                        int n;
                        while ((n = partIn.read(buf)) > 0) {
                            fileOutputStream2.write(buf, 0, n);
                            j3 += n;
                            if (restoreProgress != null && j2 > 0) {
                                int percent = (int) ((100 * j3) / j2);
                                if (percent > 100) {
                                    percent = 100;
                                }
                                try {
                                    restoreProgress.onProgress(percent, "合并分片 " + (partIndex + 1) + "/" + i, j3, j2);
                                } catch (java.lang.Throwable unused) {
                                }
                            }
                        }
                    } finally {
                        try {
                            partIn.close();
                        } catch (java.lang.Throwable unused2) {
                        }
                    }
                }
                fileOutputStream2.flush();
            } finally {
                try {
                    fileOutputStream2.close();
                } catch (java.lang.Throwable unused3) {
                }
            }
            boolean zSizeOk = j2 <= 0 || file4.length() == j2;
            boolean zMd5Ok = false;
            if (str == null || str.isEmpty()) {
                zMd5Ok = true;
            } else {
                java.lang.String strMd5Hex = md5Hex(file4);
                zMd5Ok = str.equalsIgnoreCase(strMd5Hex);
                if (!zMd5Ok) {
                    com.tencent.qqlitepro.util.Log.w("分片合并MD5不一致: want=" + str + " got=" + strMd5Hex);
                }
            }
            if (zSizeOk && zMd5Ok) {
                if (restoreProgress != null) {
                    restoreProgress.onProgress(100, "合并完成", j3, j2);
                }
                deleteRecursively(file2);
                if (file4.exists()) {
                    return file4;
                }
                return null;
            }
            file4.delete();
            if (restoreProgress == null) {
                return null;
            }
            restoreProgress.onProgress(0, "合并结果校验失败，分片已保留，可稍后重试", 0L, j2);
            return null;
        } catch (java.lang.Throwable th9) {
            com.tencent.qqlitepro.util.Log.w("带进度合并分片失败: " + th9);
            return null;
        }
    }

    private static java.lang.String md5Hex(java.io.File file) {
        try {
            java.io.FileInputStream fileInputStream = new java.io.FileInputStream(file);
            try {
                java.security.MessageDigest messageDigest = java.security.MessageDigest.getInstance("MD5");
                byte[] bArr = new byte[org.mozilla.javascript.Parser.ARGC_LIMIT];
                while (true) {
                    int i = fileInputStream.read(bArr);
                    if (i > 0) {
                        messageDigest.update(bArr, 0, i);
                    } else {
                        java.lang.String hex = toHex(messageDigest.digest());
                        fileInputStream.close();
                        return hex;
                    }
                    return "";
                }
            } catch (java.lang.Throwable thRethrow5) {
                try {
                    fileInputStream.close();
                } catch (java.lang.Throwable th2) {
                    thRethrow5.addSuppressed(th2);
                }
                throw thRethrow5;
            }
        } catch (java.lang.Throwable unused) {
            return "";
        }
    }

    public static java.util.List<com.tencent.qqlitepro.http.FakeVoice.Wrapped> prepareFakeVoices(java.lang.String str, boolean z) {
        java.util.ArrayList arrayList = new java.util.ArrayList();
        java.io.File file = new java.io.File(str);
        if (file.exists() && file.isFile()) {
            long length = file.length();
            java.lang.String strMd5Hex = md5Hex(file);
            if (!z || length <= SHARD_BYTES) {
                com.tencent.qqlitepro.http.FakeVoice.Wrapped wrappedWrapToFakeVoice = wrapToFakeVoice(str);
                if (wrappedWrapToFakeVoice != null) {
                    wrappedWrapToFakeVoice.md5 = strMd5Hex;
                    arrayList.add(wrappedWrapToFakeVoice);
                }
            } else {
                int i = (int) ((8388607 + length) / SHARD_BYTES);
                for (int i2 = 0; i2 < i; i2++) {
                    com.tencent.qqlitepro.http.FakeVoice.Wrapped wrappedWrapToFakeVoiceShard = wrapToFakeVoiceShard(str, i2, i, strMd5Hex, length);
                    if (wrappedWrapToFakeVoiceShard != null) {
                        arrayList.add(wrappedWrapToFakeVoiceShard);
                    }
                }
                return arrayList;
            }
        }
        return arrayList;
    }

    private static com.tencent.qqlitepro.http.FakeVoice.Wrapped wrapToFakeVoiceShard(java.lang.String str, int i, int i2, java.lang.String str2, long j) {
        int i3;
        com.tencent.qqlitepro.http.FakeVoice.Wrapped wrapped = new com.tencent.qqlitepro.http.FakeVoice.Wrapped();
        java.io.File file = new java.io.File(str);
        long length = file.length();
        long j2 = ((long) i) * SHARD_BYTES;
        if (j2 >= length) {
            return null;
        }
        long jMin = java.lang.Math.min(SHARD_BYTES, length - j2);
        try {
            wrapped.originalName = file.getName();
            wrapped.md5 = str2;
            wrapped.shard = true;
            wrapped.count = i2;
            java.lang.String strMd5HexRange = md5HexRange(file, j2, jMin);
            org.json.JSONObject jSONObject = new org.json.JSONObject();
            jSONObject.put("n", wrapped.originalName);
            jSONObject.put("m", str2);
            jSONObject.put("s", j);
            jSONObject.put("t", java.lang.System.currentTimeMillis());
            jSONObject.put("sh", 1);
            jSONObject.put("i", i);
            jSONObject.put("c", i2);
            jSONObject.put("fs", jMin);
            jSONObject.put("fm", strMd5HexRange);
            byte[] bytes = jSONObject.toString().getBytes("UTF-8");
            byte[] bArrIntToU16Be = intToU16Be(bytes.length);
            java.io.File file2 = dirTmp(ctx());
            if (!file2.exists()) {
                file2.mkdirs();
            }
            java.io.File file3 = new java.io.File(file2, "qqlms_" + java.lang.System.currentTimeMillis() + "_" + i + "_" + java.lang.Math.abs(str2.hashCode()) + ".amr");
            java.io.FileOutputStream fileOutputStream = new java.io.FileOutputStream(file3);
            try {
                java.io.FileInputStream fileInputStream = new java.io.FileInputStream(file);
                try {
                    fileOutputStream.write(MAGIC);
                    fileOutputStream.write(bArrIntToU16Be);
                    fileOutputStream.write(bytes);
                    fileInputStream.skip(j2);
                    byte[] bArr = new byte[org.mozilla.javascript.Parser.ARGC_LIMIT];
                    long j3 = jMin;
                    while (j3 > 0 && (i3 = fileInputStream.read(bArr, 0, (int) java.lang.Math.min(org.mozilla.javascript.Parser.ARGC_LIMIT, j3))) > 0) {
                        fileOutputStream.write(bArr, 0, i3);
                        j3 -= (long) i3;
                    }
                    fileOutputStream.flush();
                    fileInputStream.close();
                    fileOutputStream.close();
                    wrapped.amrFile = file3;
                    return wrapped;
                } catch (java.lang.Throwable thRethrow5) {
                    try {
                        fileInputStream.close();
                        throw thRethrow5;
                    } catch (java.lang.Throwable th2) {
                        thRethrow5.addSuppressed(th2);
                        throw thRethrow5;
                    }
                }
            } catch (java.lang.Throwable th3) {
                try {
                    fileOutputStream.close();
                    throw th3;
                } catch (java.lang.Throwable th4) {
                    th3.addSuppressed(th4);
                    throw th3;
                }
            }
        } catch (java.lang.Throwable th5) {
            com.tencent.qqlitepro.util.Log.w("封装分片伪语音失败: " + th5);
            return null;
        }
    }

    public static boolean isShard(java.lang.String str) {
        com.tencent.qqlitepro.http.FakeVoice.OriginalInfo originalInfo = parseOriginalInfo(str);
        return originalInfo != null && originalInfo.shard;
    }

    private static long expectedPartSize(com.tencent.qqlitepro.http.FakeVoice.OriginalInfo originalInfo, int i) {
        if (originalInfo.size <= 0) {
            return -1L;
        }
        long j = ((long) i) * SHARD_BYTES;
        if (j >= originalInfo.size) {
            return -1L;
        }
        return java.lang.Math.min(SHARD_BYTES, originalInfo.size - j);
    }

    private static boolean isPartValid(com.tencent.qqlitepro.http.FakeVoice.OriginalInfo originalInfo, int i, java.io.File file) {
        try {
            if (file.exists() && file.isFile()) {
                long jExpectedPartSize = expectedPartSize(originalInfo, i);
                if (jExpectedPartSize > 0) {
                    if (file.length() != jExpectedPartSize) {
                        return false;
                    }
                } else if (i != originalInfo.index || originalInfo.shardSize <= 0) {
                    if (file.length() <= 0) {
                        return false;
                    }
                } else if (file.length() != originalInfo.shardSize) {
                    return false;
                }
                if (i != originalInfo.index || originalInfo.shardMd5 == null || originalInfo.shardMd5.isEmpty()) {
                    return true;
                }
                return originalInfo.shardMd5.equalsIgnoreCase(md5Hex(file));
            }
        } catch (java.lang.Throwable unused) {
        }
        return false;
    }

    public static int collectedParts(com.tencent.qqlitepro.http.FakeVoice.OriginalInfo originalInfo) {
        if (originalInfo == null || !originalInfo.shard) {
            return 0;
        }
        java.io.File file = new java.io.File(dirParts(ctx()), originalInfo.md5 == null ? "" : originalInfo.md5);
        if (!file.isDirectory()) {
            return 0;
        }
        int i = 0;
        for (int i2 = 0; i2 < originalInfo.count; i2++) {
            if (isPartValid(originalInfo, i2, new java.io.File(file, i2 + ".part"))) {
                i++;
            }
        }
        return i;
    }

    public static void collectPart(java.lang.String str) {
        com.tencent.qqlitepro.http.FakeVoice.OriginalInfo originalInfo = parseOriginalInfo(str);
        if (originalInfo == null || !originalInfo.shard || originalInfo.md5 == null) {
            return;
        }
        recordShardPath(originalInfo, str);
        try {
            java.io.File file = new java.io.File(dirParts(ctx()), originalInfo.md5);
            if (file.exists() || file.mkdirs()) {
                java.io.File file2 = new java.io.File(file, originalInfo.index + ".part");
                if (file2.exists() && isPartValid(originalInfo, originalInfo.index, file2)) {
                    return;
                }
                java.io.FileInputStream fileInputStream = new java.io.FileInputStream(str);
                try {
                    java.io.FileOutputStream fileOutputStream = new java.io.FileOutputStream(file2);
                    try {
                        fileInputStream.skip(originalInfo.payloadOffset);
                        byte[] bArr = new byte[org.mozilla.javascript.Parser.ARGC_LIMIT];
                        while (true) {
                            int i = fileInputStream.read(bArr);
                            if (i <= 0) {
                                break;
                            } else {
                                fileOutputStream.write(bArr, 0, i);
                            }
                        }
                        fileOutputStream.flush();
                        fileOutputStream.close();
                        fileInputStream.close();
                        if (isPartValid(originalInfo, originalInfo.index, file2)) {
                            return;
                        }
                        file2.delete();
                    } catch (java.lang.Throwable th2) {
                        try {
                            fileOutputStream.close();
                        } catch (java.lang.Throwable th3) {
                            th2.addSuppressed(th3);
                        }
                        throw th2;
                    }
                } catch (java.lang.Throwable th4) {
                    fileInputStream.close();
                    throw th4;
                }
            }
        } catch (java.lang.Throwable th5) {
            com.tencent.qqlitepro.util.Log.w("归集分片失败: " + th5);
        }
    }

    private static void recordShardPath(com.tencent.qqlitepro.http.FakeVoice.OriginalInfo originalInfo, java.lang.String str) {
        java.util.concurrent.ConcurrentHashMap<java.lang.Integer, java.lang.String> concurrentHashMapPutIfAbsent;
        if (originalInfo != null) {
            try {
                if (originalInfo.shard && originalInfo.md5 != null && !originalInfo.md5.isEmpty() && str != null && !str.isEmpty()) {
                    loadShardPathsOnce();
                    java.lang.String lowerCase = originalInfo.md5.toLowerCase(java.util.Locale.US);
                    java.util.concurrent.ConcurrentHashMap<java.lang.String, java.util.concurrent.ConcurrentHashMap<java.lang.Integer, java.lang.String>> concurrentHashMap = sShardPaths;
                    java.util.concurrent.ConcurrentHashMap<java.lang.Integer, java.lang.String> concurrentHashMap2 = concurrentHashMap.get(lowerCase);
                    if (concurrentHashMap2 == null && (concurrentHashMapPutIfAbsent = concurrentHashMap.putIfAbsent(lowerCase, (concurrentHashMap2 = new java.util.concurrent.ConcurrentHashMap<>()))) != null) {
                        concurrentHashMap2 = concurrentHashMapPutIfAbsent;
                    }
                    java.lang.String strPut = concurrentHashMap2.put(java.lang.Integer.valueOf(originalInfo.index), str);
                    if (strPut == null || !strPut.equals(str)) {
                        java.io.File file = fileShardMap(ctx());
                        java.io.File parentFile = file.getParentFile();
                        if (parentFile != null && !parentFile.exists()) {
                            parentFile.mkdirs();
                        }
                        java.io.FileOutputStream fileOutputStream = new java.io.FileOutputStream(file, true);
                        try {
                            fileOutputStream.write((lowerCase + "|" + originalInfo.index + "|" + str + "\n").getBytes("UTF-8"));
                            fileOutputStream.close();
                        } catch (java.lang.Throwable thRethrow5) {
                            try {
                                fileOutputStream.close();
                            } catch (java.lang.Throwable th2) {
                                thRethrow5.addSuppressed(th2);
                            }
                            throw thRethrow5;
                        }
                    }
                }
            } catch (java.lang.Throwable unused) {
            }
        }
    }

    private static void loadShardPathsOnce() {
        if (sShardPathsLoaded) {
            return;
        }
        synchronized (com.tencent.qqlitepro.http.FakeVoice.class) {
            if (sShardPathsLoaded) {
                return;
            }
            try {
                java.io.File file = fileShardMap(ctx());
                if (file.exists() && file.isFile()) {
                    for (java.lang.String str : java.nio.file.Files.readAllLines(file.toPath())) {
                        int iIndexOf = str.indexOf(124);
                        int iIndexOf2 = iIndexOf >= 0 ? str.indexOf(124, iIndexOf + 1) : -1;
                        if (iIndexOf > 0 && iIndexOf2 > iIndexOf) {
                            java.lang.String strTrim = str.substring(0, iIndexOf).trim();
                            java.lang.String strTrim2 = str.substring(iIndexOf2 + 1).trim();
                            int i = java.lang.Integer.parseInt(str.substring(iIndexOf + 1, iIndexOf2).trim());
                            if (!strTrim.isEmpty() && !strTrim2.isEmpty() && i >= 0) {
                                java.util.concurrent.ConcurrentHashMap<java.lang.Integer, java.lang.String> concurrentHashMap = new java.util.concurrent.ConcurrentHashMap<>();
                                concurrentHashMap.put(java.lang.Integer.valueOf(i), strTrim2);
                                java.util.concurrent.ConcurrentHashMap<java.lang.Integer, java.lang.String> concurrentHashMapPutIfAbsent = sShardPaths.putIfAbsent(strTrim, concurrentHashMap);
                                if (concurrentHashMapPutIfAbsent != null) {
                                    concurrentHashMapPutIfAbsent.put(java.lang.Integer.valueOf(i), strTrim2);
                                }
                            }
                        }
                    }
                }
            } catch (java.lang.Throwable unused) {
            }
            sShardPathsLoaded = true;
        }
    }

    public static int autoCollectAllParts(com.tencent.qqlitepro.http.FakeVoice.OriginalInfo originalInfo) {
        java.io.File[] fileArrListFiles;
        if (originalInfo == null || !originalInfo.shard || originalInfo.md5 == null) {
            return 0;
        }
        loadShardPathsOnce();
        try {
            java.io.File file = dirTmp(ctx());
            if (file.isDirectory() && (fileArrListFiles = file.listFiles()) != null) {
                for (java.io.File file2 : fileArrListFiles) {
                    if (file2.isFile() && file2.length() > 0) {
                        try {
                            com.tencent.qqlitepro.http.FakeVoice.OriginalInfo originalInfo2 = parseOriginalInfo(file2.getAbsolutePath());
                            if (originalInfo2 != null && originalInfo2.shard && originalInfo.md5.equalsIgnoreCase(originalInfo2.md5)) {
                                collectPart(file2.getAbsolutePath());
                            }
                        } catch (java.lang.Throwable unused) {
                        }
                    }
                }
            }
        } catch (java.lang.Throwable unused2) {
        }
        try {
            java.util.concurrent.ConcurrentHashMap<java.lang.Integer, java.lang.String> concurrentHashMap = sShardPaths.get(originalInfo.md5.toLowerCase(java.util.Locale.US));
            if (concurrentHashMap != null) {
                for (java.lang.String str : (java.lang.String[]) concurrentHashMap.values().toArray(new java.lang.String[0])) {
                    try {
                        collectPart(str);
                    } catch (java.lang.Throwable unused3) {
                    }
                }
            }
        } catch (java.lang.Throwable unused4) {
        }
        return collectedParts(originalInfo);
    }

    public static java.io.File mergeParts(com.tencent.qqlitepro.http.FakeVoice.OriginalInfo originalInfo) {
        if (originalInfo == null || !originalInfo.shard || originalInfo.count <= 1 || originalInfo.md5 == null || collectedParts(originalInfo) < originalInfo.count) {
            return null;
        }
        try {
            java.io.File file = new java.io.File(dirParts(ctx()), originalInfo.md5);
            java.io.File file2 = dirDownloads(ctx());
            if (!file2.exists() && !file2.mkdirs()) {
                return null;
            }
            java.io.File file3 = new java.io.File(file2, originalInfo.name);
            java.io.FileOutputStream fileOutputStream = new java.io.FileOutputStream(file3);
            for (int i = 0; i < originalInfo.count; i++) {
                try {
                    java.io.File file4 = new java.io.File(file, i + ".part");
                    if (file4.exists()) {
                        java.io.FileInputStream fileInputStream = new java.io.FileInputStream(file4);
                        try {
                            byte[] bArr = new byte[org.mozilla.javascript.Parser.ARGC_LIMIT];
                            while (true) {
                                int i2 = fileInputStream.read(bArr);
                                if (i2 <= 0) {
                                    break;
                                }
                                fileOutputStream.write(bArr, 0, i2);
                            }
                            fileInputStream.close();
                        } catch (java.lang.Throwable th2) {
                            try {
                                fileInputStream.close();
                            } catch (java.lang.Throwable th3) {
                                th2.addSuppressed(th3);
                            }
                            throw th2;
                        }
                    } else {
                        fileOutputStream.close();
                        return null;
                    }
                } catch (java.lang.Throwable th4) {
                    com.tencent.qqlitepro.util.Log.w("合并还原分片失败: " + th4);
                    return null;
                }
            }
            fileOutputStream.flush();
            fileOutputStream.close();
            deleteRecursively(file);
            if (file3.exists()) {
                return file3;
            }
            return null;
        } catch (java.lang.Throwable th5) {
            com.tencent.qqlitepro.util.Log.w("合并还原分片失败: " + th5);
            return null;
        }
    }

    private static void deleteRecursively(java.io.File file) {
        java.io.File[] fileArrListFiles;
        if (file == null || !file.exists()) {
            return;
        }
        if (file.isDirectory() && (fileArrListFiles = file.listFiles()) != null) {
            for (java.io.File file2 : fileArrListFiles) {
                deleteRecursively(file2);
            }
        }
        file.delete();
    }

    private static java.lang.String md5HexRange(java.io.File file, long j, long j2) {
        int i;
        try {
            java.io.RandomAccessFile randomAccessFile = new java.io.RandomAccessFile(file, "r");
            try {
                randomAccessFile.seek(j);
                java.security.MessageDigest messageDigest = java.security.MessageDigest.getInstance("MD5");
                byte[] bArr = new byte[org.mozilla.javascript.Parser.ARGC_LIMIT];
                while (j2 > 0 && (i = randomAccessFile.read(bArr, 0, (int) java.lang.Math.min(org.mozilla.javascript.Parser.ARGC_LIMIT, j2))) > 0) {
                    messageDigest.update(bArr, 0, i);
                    j2 -= (long) i;
                }
                java.lang.String hex = toHex(messageDigest.digest());
                randomAccessFile.close();
                return hex;
            } catch (java.lang.Throwable thRethrow5) {
                try {
                    randomAccessFile.close();
                } catch (java.lang.Throwable th2) {
                    thRethrow5.addSuppressed(th2);
                }
                throw thRethrow5;
            }
        } catch (java.lang.Throwable unused) {
            return "";
        }
    }

    private static java.lang.String toHex(byte[] bArr) {
        if (bArr == null) {
            return "";
        }
        java.lang.StringBuilder sb = new java.lang.StringBuilder(bArr.length * 2);
        for (byte b : bArr) {
            sb.append(java.lang.Character.forDigit((b >> 4) & 15, 16));
            sb.append(java.lang.Character.forDigit(b & 15, 16));
        }
        return sb.toString();
    }
}
