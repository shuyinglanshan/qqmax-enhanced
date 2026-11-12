package com.tencent.qqlitepro.util;

/* JADX INFO: loaded from: classes.dex */
public final class Net {
    private Net() {
    }

    public static java.io.File downloadDir(android.content.Context context) {
        java.io.File filesDir = context != null ? context.getFilesDir() : null;
        if (filesDir == null) {
            filesDir = new java.io.File(".");
        }
        java.io.File file = new java.io.File(filesDir, "qqlite_downloads");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static java.lang.String download(android.content.Context context, java.lang.String str, java.lang.String str2) {
        java.net.HttpURLConnection httpURLConnection;
        java.lang.String strFileNameOf;
        if (str == null || str.trim().isEmpty()) {
            return null;
        }
        try {
            java.lang.String strTrim = str.trim();
            if (!strTrim.startsWith("http://") && !strTrim.startsWith("https://")) {
                strTrim = "http://" + strTrim;
            }
            if (str2 == null || str2.trim().isEmpty()) {
                strFileNameOf = fileNameOf(strTrim);
            } else {
                strFileNameOf = str2.trim();
            }
            java.lang.String strFileNameOf2 = fileNameOf(strFileNameOf);
            if (strFileNameOf2.isEmpty()) {
                strFileNameOf2 = "download_" + java.lang.System.currentTimeMillis();
            }
            httpURLConnection = (java.net.HttpURLConnection) new java.net.URL(strTrim).openConnection();
            try {
                httpURLConnection.setConnectTimeout(10000);
                httpURLConnection.setReadTimeout(30000);
                httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android) QQMax-AntiRecall");
                httpURLConnection.setInstanceFollowRedirects(true);
                int responseCode = httpURLConnection.getResponseCode();
                if (responseCode != 200) {
                    com.tencent.qqlitepro.util.Log.w("下载失败 HTTP " + responseCode + " : " + strTrim);
                    if (httpURLConnection != null) {
                        try {
                            httpURLConnection.disconnect();
                        } catch (java.lang.Throwable unused) {
                        }
                    }
                    return null;
                }
                java.io.File file = new java.io.File(downloadDir(context), strFileNameOf2);
                java.io.InputStream inputStream = httpURLConnection.getInputStream();
                try {
                    java.io.FileOutputStream fileOutputStream = new java.io.FileOutputStream(file, false);
                    try {
                        byte[] bArr = new byte[8192];
                        while (true) {
                            int i = inputStream.read(bArr);
                            if (i > 0) {
                                fileOutputStream.write(bArr, 0, i);
                            } else {
                                break;
                            }
                        }
                        fileOutputStream.close();
                        try {
                            inputStream.close();
                        } catch (java.lang.Throwable unused4) {
                        }
                        if (!file.exists() || file.length() <= 0) {
                            if (httpURLConnection != null) {
                                try {
                                    httpURLConnection.disconnect();
                                } catch (java.lang.Throwable unused5) {
                                }
                            }
                            return null;
                        }
                        com.tencent.qqlitepro.util.Log.i("已下载: " + strTrim + " -> " + file.getAbsolutePath());
                        java.lang.String absolutePath = file.getAbsolutePath();
                        if (httpURLConnection != null) {
                            try {
                                httpURLConnection.disconnect();
                            } catch (java.lang.Throwable unused6) {
                            }
                        }
                        return absolutePath;
                    } catch (java.lang.Throwable th) {
                        try {
                            fileOutputStream.close();
                        } catch (java.lang.Throwable unused7) {
                        }
                        throw th;
                    }
                } catch (java.lang.Throwable th2) {
                    inputStream.close();
                    throw th2;
                }
            } catch (java.lang.Throwable th3) {
                java.lang.Throwable thRethrow = th3;
                try {
                    com.tencent.qqlitepro.util.Log.w("下载异常: " + thRethrow);
                    return null;
                } finally {
                    if (httpURLConnection != null) {
                        try {
                            httpURLConnection.disconnect();
                        } catch (java.lang.Throwable unused8) {
                        }
                    }
                }
            }
        } catch (java.lang.Throwable th4) {
            com.tencent.qqlitepro.util.Log.w("下载异常: " + th4);
            return null;
        }
    }

    private static java.lang.String fileNameOf(java.lang.String str) {
        if (str == null) {
            return "";
        }
        int iIndexOf = str.indexOf(63);
        if (iIndexOf >= 0) {
            str = str.substring(0, iIndexOf);
        }
        int iLastIndexOf = str.lastIndexOf(47);
        return iLastIndexOf < 0 ? str : str.substring(iLastIndexOf + 1);
    }
}
