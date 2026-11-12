package com.tencent.qqlitepro.http;

/* JADX INFO: loaded from: classes.dex */
public final class FakeImage {
    public static final java.lang.String BASE_DIR = "/sdcard/QQMax增强";
    public static final java.lang.String DOWNLOAD_DIR = "/sdcard/QQMax增强/downloads";
    private static final java.lang.String MD5_MAP_FILE = "/sdcard/QQMax增强/tmp/.img_md5.map";
    public static final java.lang.String PARTS_DIR = "/sdcard/QQMax增强/downloads/.parts";
    public static final long SHARD_BYTES = 20971520;
    private static final java.lang.String SHARD_PATH_FILE = "/sdcard/QQMax增强/tmp/.shard_paths_img.map";
    private static final java.lang.String TMP_DIR = "/sdcard/QQMax增强/tmp";

    /* 路径修复：同 FakeVoice，内部统一走 Storage 解析出的可写目录 */
    private static android.content.Context ctx() {
        return com.tencent.qqlitepro.cache.MsgCache.appContext();
    }

    private static java.io.File dirDownloads(android.content.Context c) {
        return com.tencent.qqlitepro.util.Storage.dir(c, "downloads");
    }

    private static java.io.File dirParts(android.content.Context c) {
        return com.tencent.qqlitepro.util.Storage.dir(c, "downloads/.parts");
    }

    private static java.io.File dirTmp(android.content.Context c) {
        return com.tencent.qqlitepro.util.Storage.dir(c, "tmp");
    }

    private static java.io.File fileShardMap(android.content.Context c) {
        return new java.io.File(dirTmp(c), ".shard_paths_img.map");
    }

    private static java.io.File fileMd5Map(android.content.Context c) {
        return new java.io.File(dirTmp(c), ".img_md5.map");
    }
    private static final byte[] MAGIC = {81, 81, 76, 77, 73, 77, 71};
    private static final byte[] PNG_SIGNATURE = {-119, 80, 78, 71, 13, 10, 26, 10};
    private static final java.util.concurrent.ConcurrentHashMap<java.lang.String, java.lang.String> sMd5ToPng = new java.util.concurrent.ConcurrentHashMap<>();
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

    public static final class Wrapped {
        public int count;
        public java.lang.String md5;
        public java.lang.String originalName;
        public java.io.File pngFile;
        public boolean shard;
    }

    private static byte[] intToU16Be(int i) {
        return new byte[]{(byte) ((i >> 8) & org.mozilla.classfile.ByteCode.IMPDEP2), (byte) (i & org.mozilla.classfile.ByteCode.IMPDEP2)};
    }

    public static void registerPngByMd5(java.lang.String str, java.lang.String str2) {
        if (str == null || str.isEmpty() || str2 == null) {
            return;
        }
        java.lang.String lowerCase = str.toLowerCase(java.util.Locale.US);
        sMd5ToPng.put(lowerCase, str2);
        try {
            java.io.File file = fileMd5Map(ctx());
            java.io.File parentFile = file.getParentFile();
            if (parentFile != null && !parentFile.exists()) {
                parentFile.mkdirs();
            }
            java.util.LinkedHashSet linkedHashSet = new java.util.LinkedHashSet();
            if (file.exists()) {
                for (java.lang.String str3 : java.nio.file.Files.readAllLines(file.toPath())) {
                    if (!(str3.indexOf(124) > 0 ? str3.substring(0, str3.indexOf(124)) : str3).trim().equalsIgnoreCase(lowerCase)) {
                        linkedHashSet.add(str3);
                    }
                }
            }
            linkedHashSet.add(lowerCase + "|" + str2);
            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            java.util.Iterator it = linkedHashSet.iterator();
            while (it.hasNext()) {
                sb.append((java.lang.String) it.next());
                sb.append('\n');
            }
            java.io.FileOutputStream fileOutputStream = new java.io.FileOutputStream(file, false);
            try {
                fileOutputStream.write(sb.toString().getBytes("UTF-8"));
                fileOutputStream.close();
            } catch (java.lang.Throwable th) {
                try {
                    fileOutputStream.close();
                } catch (java.lang.Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        } catch (java.lang.Throwable unused) {
        }
    }

    public static void loadPersistedMap() {
        try {
            java.io.File file = fileMd5Map(ctx());
            if (file.exists() && file.isFile()) {
                for (java.lang.String str : java.nio.file.Files.readAllLines(file.toPath())) {
                    int iIndexOf = str.indexOf(124);
                    if (iIndexOf > 0) {
                        java.lang.String strTrim = str.substring(0, iIndexOf).trim();
                        java.lang.String strTrim2 = str.substring(iIndexOf + 1).trim();
                        if (!strTrim.isEmpty() && !strTrim2.isEmpty()) {
                            java.io.File file2 = new java.io.File(strTrim2);
                            if (file2.exists() && file2.isFile()) {
                                sMd5ToPng.putIfAbsent(strTrim.toLowerCase(java.util.Locale.US), strTrim2);
                            }
                        }
                    }
                }
            }
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("加载伪图片md5映射失败: " + th);
        }
    }

    private static void persistRemove(java.lang.String str) {
        if (str != null) {
            sMd5ToPng.remove(str.toLowerCase(java.util.Locale.US));
        }
    }

    public static java.lang.String pngByMd5(java.lang.String str) {
        if (str != null && !str.isEmpty()) {
            java.lang.String lowerCase = str.toLowerCase(java.util.Locale.US);
            java.lang.String str2 = sMd5ToPng.get(lowerCase);
            if (str2 != null) {
                return str2;
            }
            try {
                java.io.File file = fileMd5Map(ctx());
                if (file.exists()) {
                    java.util.Scanner scanner = new java.util.Scanner(file);
                    while (scanner.hasNextLine()) {
                        java.lang.String strNextLine = scanner.nextLine();
                        int iIndexOf = strNextLine.indexOf(124);
                        if (iIndexOf > 0 && strNextLine.substring(0, iIndexOf).trim().equalsIgnoreCase(lowerCase)) {
                            java.lang.String strTrim = strNextLine.substring(iIndexOf + 1).trim();
                            if (!strTrim.isEmpty() && new java.io.File(strTrim).exists()) {
                                sMd5ToPng.putIfAbsent(lowerCase, strTrim);
                                return strTrim;
                            }
                        }
                    }
                }
            } catch (java.lang.Throwable unused) {
            }
        }
        return null;
    }

    public static java.lang.String formatSize(long j) {
        return com.tencent.qqlitepro.http.FakeVoice.formatSize(j);
    }

    private FakeImage() {
    }

    public static com.tencent.qqlitepro.http.FakeImage.Wrapped wrapToFakeImage(java.lang.String str) {
        com.tencent.qqlitepro.http.FakeImage.Wrapped wrapped = new com.tencent.qqlitepro.http.FakeImage.Wrapped();
        java.io.File file = new java.io.File(str);
        if (file.exists() && file.isFile()) {
            try {
                wrapped.originalName = file.getName();
                wrapped.md5 = md5Hex(file);
                wrapped.count = 1;
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
                java.io.File file3 = new java.io.File(file2, "qqlpi_" + java.lang.System.currentTimeMillis() + "_" + java.lang.Math.abs(wrapped.md5.hashCode()) + ".png");
                byte[] bArrBuildImagePrefix = buildImagePrefix();
                java.io.FileOutputStream fileOutputStream = new java.io.FileOutputStream(file3);
                try {
                    java.io.FileInputStream fileInputStream = new java.io.FileInputStream(file);
                    try {
                        fileOutputStream.write(bArrBuildImagePrefix);
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
                                wrapped.pngFile = file3;
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
                com.tencent.qqlitepro.util.Log.w("封装伪图片失败: " + th5);
            }
        }
        return null;
    }

    public static boolean isFakeImage(java.lang.String str) {
        return parseOriginalInfo(str) != null;
    }

    public static com.tencent.qqlitepro.http.FakeImage.OriginalInfo parseOriginalInfo(java.lang.String str) {
        if (str == null) {
            return null;
        }
        try {
            java.io.File file = new java.io.File(str);
            if (file.exists() && file.isFile()) {
                java.io.RandomAccessFile randomAccessFile = new java.io.RandomAccessFile(file, "r");
                try {
                    if (randomAccessFile.length() < 8) {
                        randomAccessFile.close();
                        return null;
                    }
                    byte[] bArr = new byte[8];
                    randomAccessFile.readFully(bArr);
                    if (!java.util.Arrays.equals(bArr, PNG_SIGNATURE)) {
                        randomAccessFile.close();
                        return null;
                    }
                    long jPngPayloadOffset = pngPayloadOffset(randomAccessFile);
                    if (jPngPayloadOffset < 0) {
                        randomAccessFile.close();
                        return null;
                    }
                    byte[] bArr2 = MAGIC;
                    if (((long) bArr2.length) + jPngPayloadOffset + 10 > randomAccessFile.length()) {
                        randomAccessFile.close();
                        return null;
                    }
                    byte[] bArr3 = new byte[bArr2.length];
                    randomAccessFile.seek(jPngPayloadOffset);
                    randomAccessFile.readFully(bArr3);
                    if (!java.util.Arrays.equals(bArr3, bArr2)) {
                        randomAccessFile.close();
                        return null;
                    }
                    byte[] bArr4 = new byte[2];
                    randomAccessFile.readFully(bArr4);
                    int i = ((bArr4[0] & org.mozilla.classfile.ByteCode.IMPDEP2) << 8) | (bArr4[1] & org.mozilla.classfile.ByteCode.IMPDEP2);
                    if (i > 0 && i <= 65535) {
                        long j = jPngPayloadOffset + 9 + ((long) i);
                        if (j <= randomAccessFile.length()) {
                            byte[] bArr5 = new byte[i];
                            randomAccessFile.readFully(bArr5);
                            org.json.JSONObject jSONObject = new org.json.JSONObject(new java.lang.String(bArr5, "UTF-8"));
                            com.tencent.qqlitepro.http.FakeImage.OriginalInfo originalInfo = new com.tencent.qqlitepro.http.FakeImage.OriginalInfo();
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

    public static java.io.File restoreFromFakeImage(java.lang.String str) {
        return restoreFromFakeImage(str, dirDownloads(ctx()).getAbsolutePath(), true);
    }

    public static java.io.File restoreFromFakeImage(java.lang.String str, java.lang.String str2, boolean z) {
        com.tencent.qqlitepro.http.FakeImage.OriginalInfo originalInfo = parseOriginalInfo(str);
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
                                com.tencent.qqlitepro.util.Log.w("伪图片还原MD5不一致: want=" + originalInfo.md5 + " got=" + strMd5Hex);
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
                com.tencent.qqlitepro.util.Log.w("还原伪图片失败: " + th5);
            }
        }
        return null;
    }

    public static java.util.List<com.tencent.qqlitepro.http.FakeImage.Wrapped> prepareFakeImages(java.lang.String str, boolean z) {
        java.util.ArrayList arrayList = new java.util.ArrayList();
        java.io.File file = new java.io.File(str);
        if (file.exists() && file.isFile()) {
            long length = file.length();
            java.lang.String strMd5Hex = md5Hex(file);
            if (!z || length <= SHARD_BYTES) {
                com.tencent.qqlitepro.http.FakeImage.Wrapped wrappedWrapToFakeImage = wrapToFakeImage(str);
                if (wrappedWrapToFakeImage != null) {
                    wrappedWrapToFakeImage.md5 = strMd5Hex;
                    registerCreated(wrappedWrapToFakeImage);
                    arrayList.add(wrappedWrapToFakeImage);
                }
            } else {
                int i = (int) ((20971519 + length) / SHARD_BYTES);
                for (int i2 = 0; i2 < i; i2++) {
                    com.tencent.qqlitepro.http.FakeImage.Wrapped wrappedWrapToFakeImageShard = wrapToFakeImageShard(str, i2, i, strMd5Hex, length);
                    if (wrappedWrapToFakeImageShard != null) {
                        registerCreated(wrappedWrapToFakeImageShard);
                        arrayList.add(wrappedWrapToFakeImageShard);
                    }
                }
                return arrayList;
            }
        }
        return arrayList;
    }

    private static void registerCreated(com.tencent.qqlitepro.http.FakeImage.Wrapped wrapped) {
        try {
            java.io.File file = wrapped.pngFile;
            if (file != null && file.exists() && file.isFile()) {
                registerPngByMd5(md5Hex(file), file.getAbsolutePath());
            }
        } catch (java.lang.Throwable unused) {
        }
    }

    private static com.tencent.qqlitepro.http.FakeImage.Wrapped wrapToFakeImageShard(java.lang.String str, int i, int i2, java.lang.String str2, long j) {
        int i3;
        com.tencent.qqlitepro.http.FakeImage.Wrapped wrapped = new com.tencent.qqlitepro.http.FakeImage.Wrapped();
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
            java.io.File file3 = new java.io.File(file2, "qqlpis_" + java.lang.System.currentTimeMillis() + "_" + i + "_" + java.lang.Math.abs(str2.hashCode()) + ".png");
            byte[] bArrBuildImagePrefix = buildImagePrefix();
            java.io.FileOutputStream fileOutputStream = new java.io.FileOutputStream(file3);
            try {
                java.io.FileInputStream fileInputStream = new java.io.FileInputStream(file);
                try {
                    fileOutputStream.write(bArrBuildImagePrefix);
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
                    wrapped.pngFile = file3;
                    return wrapped;
                } catch (java.lang.Throwable th) {
                    try {
                        fileInputStream.close();
                        throw th;
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                        throw th;
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
            com.tencent.qqlitepro.util.Log.w("封装分片伪图片失败: " + th5);
            return null;
        }
    }

    public static boolean isShard(java.lang.String str) {
        com.tencent.qqlitepro.http.FakeImage.OriginalInfo originalInfo = parseOriginalInfo(str);
        return originalInfo != null && originalInfo.shard;
    }

    private static long expectedPartSize(com.tencent.qqlitepro.http.FakeImage.OriginalInfo originalInfo, int i) {
        if (originalInfo.size <= 0) {
            return -1L;
        }
        long j = ((long) i) * SHARD_BYTES;
        if (j >= originalInfo.size) {
            return -1L;
        }
        return java.lang.Math.min(SHARD_BYTES, originalInfo.size - j);
    }

    private static boolean isPartValid(com.tencent.qqlitepro.http.FakeImage.OriginalInfo originalInfo, int i, java.io.File file) {
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

    public static int collectedParts(com.tencent.qqlitepro.http.FakeImage.OriginalInfo originalInfo) {
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
        com.tencent.qqlitepro.http.FakeImage.OriginalInfo originalInfo = parseOriginalInfo(str);
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
            com.tencent.qqlitepro.util.Log.w("归集图片分片失败: " + th5);
        }
    }

    private static void recordShardPath(com.tencent.qqlitepro.http.FakeImage.OriginalInfo originalInfo, java.lang.String str) {
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
                        } catch (java.lang.Throwable th) {
                            try {
                                fileOutputStream.close();
                            } catch (java.lang.Throwable th2) {
                                th.addSuppressed(th2);
                            }
                            throw th;
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
        synchronized (com.tencent.qqlitepro.http.FakeImage.class) {
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

    public static int autoCollectAllParts(com.tencent.qqlitepro.http.FakeImage.OriginalInfo originalInfo) {
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
                            com.tencent.qqlitepro.http.FakeImage.OriginalInfo originalInfo2 = parseOriginalInfo(file2.getAbsolutePath());
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

    public static java.io.File mergeParts(com.tencent.qqlitepro.http.FakeImage.OriginalInfo originalInfo) {
        if (originalInfo == null || originalInfo.md5 == null) {
            return null;
        }
        return com.tencent.qqlitepro.http.FakeVoice.mergePartsWithProgress(originalInfo.md5, originalInfo.name, originalInfo.count, originalInfo.size, null);
    }

    public static java.io.File restoreWithProgress(java.lang.String str, com.tencent.qqlitepro.http.FakeVoice.RestoreProgress restoreProgress) {
        return restoreWithProgress(str, dirDownloads(ctx()).getAbsolutePath(), restoreProgress);
    }

    public static java.io.File restoreWithProgress(java.lang.String str, java.lang.String str2, com.tencent.qqlitepro.http.FakeVoice.RestoreProgress restoreProgress) {
        java.io.FileOutputStream fileOutputStream;
        int iAutoCollectAllParts;
        com.tencent.qqlitepro.http.FakeImage.OriginalInfo originalInfo = parseOriginalInfo(str);
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
                    return com.tencent.qqlitepro.http.FakeVoice.mergePartsWithProgress(originalInfo.md5, originalInfo.name, originalInfo.count, originalInfo.size, str2, restoreProgress);
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
                    com.tencent.qqlitepro.util.Log.w("伪图片还原MD5不一致: want=" + originalInfo.md5 + " got=" + actualMd5);
                }
            }
            if (restoreProgress != null) {
                restoreProgress.onProgress(100, "还原完成", j2, j2);
            }
            return file2;
        } catch (java.lang.Throwable th7) {
            com.tencent.qqlitepro.util.Log.w("带进度还原伪图片失败: " + th7);
            return null;
        }
    }

    private static long pngPayloadOffset(java.io.RandomAccessFile randomAccessFile) throws java.lang.Exception {
        long j = 8;
        do {
            try {
                byte[] bArr = new byte[8];
                randomAccessFile.seek(j);
                randomAccessFile.readFully(bArr);
                int i = ((bArr[2] & 255) << 8) | ((bArr[0] & 255) << 24) | ((bArr[1] & 255) << 16) | (bArr[3] & 255);
                if (i < 0) {
                    return -1L;
                }
                java.lang.String str = new java.lang.String(bArr, 4, 4, "ISO-8859-1");
                j = j + 8 + ((long) i) + 4;
                if (j > randomAccessFile.length()) {
                    return -1L;
                }
                if ("IEND".equals(str)) {
                    return j;
                }
            } catch (java.lang.Throwable unused) {
                return -1L;
            }
        } while (j < randomAccessFile.length());
        return -1L;
    }

    static byte[] buildImagePrefix() {
        return buildMinimalPng(416, 468, 0, 0, 0, 0);
    }

    static byte[] buildMinimalPng(int i, int i2, int i3, int i4, int i5, int i6) {
        try {
            java.io.ByteArrayOutputStream byteArrayOutputStream = new java.io.ByteArrayOutputStream(128);
            byteArrayOutputStream.write(PNG_SIGNATURE);
            java.io.ByteArrayOutputStream byteArrayOutputStream2 = new java.io.ByteArrayOutputStream(16);
            writeInt(byteArrayOutputStream2, i);
            writeInt(byteArrayOutputStream2, i2);
            byteArrayOutputStream2.write(8);
            byteArrayOutputStream2.write(6);
            byteArrayOutputStream2.write(0);
            byteArrayOutputStream2.write(0);
            byteArrayOutputStream2.write(0);
            writeChunk(byteArrayOutputStream, "IHDR", byteArrayOutputStream2.toByteArray());
            java.io.ByteArrayOutputStream byteArrayOutputStream3 = new java.io.ByteArrayOutputStream();
            int i7 = (i * 4) + 1;
            byte[] bArr = new byte[i7];
            bArr[0] = 0;
            for (int i8 = 0; i8 < i; i8++) {
                int i9 = i8 * 4;
                bArr[i9 + 1] = (byte) i3;
                bArr[i9 + 2] = (byte) i4;
                bArr[i9 + 3] = (byte) i5;
                bArr[i9 + 4] = (byte) i6;
            }
            for (int i10 = 0; i10 < i2; i10++) {
                byteArrayOutputStream3.write(bArr, 0, i7);
            }
            writeChunk(byteArrayOutputStream, "IDAT", zlib(byteArrayOutputStream3.toByteArray()));
            writeChunk(byteArrayOutputStream, "IEND", new byte[0]);
            return byteArrayOutputStream.toByteArray();
        } catch (java.lang.Throwable unused) {
            return new byte[]{-119, 80, 78, 71, 13, 10, 26, 10, 0, 0, 0, 13, 73, 72, 68, 82, 0, 0, 0, 1, 0, 0, 0, 1, 8, 6, 0, 0, 0, 31, 21, -60, -119, 0, 0, 0, 13, 73, 68, 65, 84, 8, -41, 99, -8, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -32, 0, 58, -60, 26, 0, 0, 0, 0, 73, 69, 78, 68, -82, 66, 96, -126};
        }
    }

    private static void writeInt(java.io.ByteArrayOutputStream byteArrayOutputStream, int i) {
        byteArrayOutputStream.write((i >> 24) & org.mozilla.classfile.ByteCode.IMPDEP2);
        byteArrayOutputStream.write((i >> 16) & org.mozilla.classfile.ByteCode.IMPDEP2);
        byteArrayOutputStream.write((i >> 8) & org.mozilla.classfile.ByteCode.IMPDEP2);
        byteArrayOutputStream.write(i & org.mozilla.classfile.ByteCode.IMPDEP2);
    }

    private static void writeChunk(java.io.ByteArrayOutputStream byteArrayOutputStream, java.lang.String str, byte[] bArr) {
        try {
            byte[] bytes = str.getBytes("UTF-8");
            int length = bArr.length;
            byteArrayOutputStream.write((length >> 24) & org.mozilla.classfile.ByteCode.IMPDEP2);
            byteArrayOutputStream.write((length >> 16) & org.mozilla.classfile.ByteCode.IMPDEP2);
            byteArrayOutputStream.write((length >> 8) & org.mozilla.classfile.ByteCode.IMPDEP2);
            byteArrayOutputStream.write(length & org.mozilla.classfile.ByteCode.IMPDEP2);
            byteArrayOutputStream.write(bytes);
            byteArrayOutputStream.write(bArr);
            java.util.zip.CRC32 crc32 = new java.util.zip.CRC32();
            crc32.update(bytes);
            crc32.update(bArr);
            long value = crc32.getValue();
            byteArrayOutputStream.write(((int) (value >> 24)) & org.mozilla.classfile.ByteCode.IMPDEP2);
            byteArrayOutputStream.write(((int) (value >> 16)) & org.mozilla.classfile.ByteCode.IMPDEP2);
            byteArrayOutputStream.write(((int) (value >> 8)) & org.mozilla.classfile.ByteCode.IMPDEP2);
            byteArrayOutputStream.write(((int) value) & org.mozilla.classfile.ByteCode.IMPDEP2);
        } catch (java.lang.Throwable unused) {
        }
    }

    private static byte[] zlib(byte[] bArr) {
        java.util.zip.Deflater deflater = new java.util.zip.Deflater(9, false);
        deflater.setInput(bArr);
        deflater.finish();
        java.io.ByteArrayOutputStream byteArrayOutputStream = new java.io.ByteArrayOutputStream();
        byte[] bArr2 = new byte[1024];
        while (!deflater.finished()) {
            byteArrayOutputStream.write(bArr2, 0, deflater.deflate(bArr2));
        }
        deflater.end();
        return byteArrayOutputStream.toByteArray();
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
            } catch (java.lang.Throwable thRethrow2) {
                try {
                    fileInputStream.close();
                } catch (java.lang.Throwable th2) {
                    thRethrow2.addSuppressed(th2);
                }
                throw thRethrow2;
            }
        } catch (java.lang.Throwable unused) {
            return "";
        }
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
            } catch (java.lang.Throwable thRethrow2) {
                try {
                    randomAccessFile.close();
                } catch (java.lang.Throwable th2) {
                    thRethrow2.addSuppressed(th2);
                }
                throw thRethrow2;
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
