package com.tencent.qqlitepro.hook;

/* JADX INFO: loaded from: classes.dex */
public final class TranslateHook {
    private static final java.lang.String API_BASE = "https://api.corexwear.com/fy2/index.php?msg=";
    private static final java.lang.String YDAO_URL = "https://m.youdao.com/translate?type=AUTO&inputtext=";
    private static volatile boolean sInstalled = false;

    public static boolean isInstalled() {
        return sInstalled;
    }

    private TranslateHook() {
    }

    public static void patch(java.lang.ClassLoader classLoader) {
        java.lang.reflect.Method method;
        if (sInstalled) {
            return;
        }
        try {
            int i = 0;
            java.lang.reflect.Method[] declaredMethods = java.lang.Class.forName("momoi.mod.qqpro.hook.translate.Translator", false, classLoader).getDeclaredMethods();
            int length = declaredMethods.length;
            while (true) {
                if (i >= length) {
                    method = null;
                    break;
                }
                method = declaredMethods[i];
                if (method.getName().equals("translate") && com.tencent.qqlitepro.util.Log$$ExternalSyntheticBackport0.m(method) == 3) {
                    break;
                } else {
                    i++;
                }
            }
            if (method == null) {
                com.tencent.qqlitepro.util.Log.w("未找到 Translator.translate 目标方法（翻译替换未生效，稍后重试）");
                return;
            }
            de.robv.android.xposed.XposedBridge.hookMethod(method, new com.tencent.qqlitepro.hook.TranslateHook.AnonymousClass1());
            sInstalled = true;
            com.tencent.qqlitepro.util.Log.i("已 hook 内置翻译接口 Translator.translate（替换为自定义网关）");
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("hook Translator.translate 失败（稍后重试）: " + th);
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.TranslateHook$1, reason: invalid class name */
    static class AnonymousClass1 extends de.robv.android.xposed.XC_MethodHook {
        AnonymousClass1() {
        }

        protected void beforeHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            try {
                android.content.Context contextAppContext = com.tencent.qqlitepro.hook.TranslateHook.appContext();
                if (contextAppContext != null && com.tencent.qqlitepro.util.Config.translateEnabled(contextAppContext)) {
                    java.lang.String str = (methodHookParam.args == null || methodHookParam.args.length < 1 || !(methodHookParam.args[0] instanceof java.lang.String)) ? null : (java.lang.String) methodHookParam.args[0];
                    java.lang.Object obj = (methodHookParam.args == null || methodHookParam.args.length < 3) ? null : methodHookParam.args[2];
                    if (str != null && !str.trim().isEmpty() && obj != null) {
                        methodHookParam.setResult((java.lang.Object) null);
                        com.tencent.qqlitepro.hook.TranslateHook.runTranslateAsync(str, obj);
                    }
                }
            } catch (java.lang.Throwable th) {
                com.tencent.qqlitepro.util.Log.w("Translator.translate hook 异常: " + th);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void runTranslateAsync(java.lang.String str, java.lang.Object obj) {
        new java.lang.Thread(new com.tencent.qqlitepro.hook.TranslateHook$$ExternalSyntheticLambda0(str, obj), "qqlite-translate").start();
    }

    static java.lang.String httpTranslate(java.lang.String str) {
        java.lang.String strUrlEncode = urlEncode(str);
        java.lang.String json = getJson(API_BASE + strUrlEncode, "自定义网关");
        if (json != null) {
            return json;
        }
        java.lang.String youdao = getYoudao(strUrlEncode);
        if (youdao != null) {
            return youdao;
        }
        return null;
    }

    private static java.lang.String urlEncode(java.lang.String str) {
        try {
            return java.net.URLEncoder.encode(str, "UTF-8").replace("+", "%20");
        } catch (java.lang.Throwable unused) {
            return str;
        }
    }

    private static java.lang.String getJson(java.lang.String str, java.lang.String str2) {
        java.net.HttpURLConnection httpURLConnection;
        try {
            httpURLConnection = (java.net.HttpURLConnection) new java.net.URL(str).openConnection();
            try {
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setConnectTimeout(8000);
                httpURLConnection.setReadTimeout(15000);
                httpURLConnection.setRequestProperty("Accept", "application/json");
                int responseCode = httpURLConnection.getResponseCode();
                if (responseCode != 200) {
                    com.tencent.qqlitepro.util.Log.w("翻译(" + str2 + ") HTTP " + responseCode);
                    if (httpURLConnection != null) {
                        try {
                            httpURLConnection.disconnect();
                        } catch (java.lang.Throwable unused) {
                        }
                    }
                    return null;
                }
                java.lang.String translated = parseTranslated(readAll(httpURLConnection.getInputStream()));
                if (httpURLConnection != null) {
                    try {
                        httpURLConnection.disconnect();
                    } catch (java.lang.Throwable unused2) {
                    }
                }
                return translated;
            } catch (java.lang.Throwable th) {
                java.lang.Throwable thRethrow = th;
                try {
                    com.tencent.qqlitepro.util.Log.w("翻译(" + str2 + ") 请求失败: " + thRethrow);
                    return null;
                } finally {
                    if (httpURLConnection != null) {
                        try {
                            httpURLConnection.disconnect();
                        } catch (java.lang.Throwable unused3) {
                        }
                    }
                }
            }
        } catch (java.lang.Throwable th2) {
            com.tencent.qqlitepro.util.Log.w("翻译(" + str2 + ") 请求失败: " + th2);
        }
        return null;
    }

    private static java.lang.String getYoudao(java.lang.String str) {
        java.net.HttpURLConnection httpURLConnection;
        try {
            httpURLConnection = (java.net.HttpURLConnection) new java.net.URL(YDAO_URL + str).openConnection();
            try {
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setConnectTimeout(8000);
                httpURLConnection.setReadTimeout(15000);
                httpURLConnection.setInstanceFollowRedirects(true);
                httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 9; Watch) AppleWebKit/537.36 QQ");
                int responseCode = httpURLConnection.getResponseCode();
                if (responseCode != 200) {
                    com.tencent.qqlitepro.util.Log.w("翻译(有道) HTTP " + responseCode);
                    if (httpURLConnection != null) {
                        try {
                            httpURLConnection.disconnect();
                        } catch (java.lang.Throwable unused) {
                        }
                    }
                    return null;
                }
                java.lang.String youdaoHtml = parseYoudaoHtml(readAll(httpURLConnection.getInputStream()));
                if (httpURLConnection != null) {
                    try {
                        httpURLConnection.disconnect();
                    } catch (java.lang.Throwable unused2) {
                    }
                }
                return youdaoHtml;
            } catch (java.lang.Throwable thRethrow2) {
                try {
                    com.tencent.qqlitepro.util.Log.w("翻译(有道) 请求失败: " + thRethrow2);
                    return null;
                } finally {
                    if (httpURLConnection != null) {
                        try {
                            httpURLConnection.disconnect();
                        } catch (java.lang.Throwable unused3) {
                        }
                    }
                }
            }
        } catch (java.lang.Throwable th2) {
            com.tencent.qqlitepro.util.Log.w("翻译(有道) 请求失败: " + th2);
        }
        return null;
    }

    private static java.lang.String parseYoudaoHtml(java.lang.String str) {
        int iIndexOf;
        if (str == null) {
            return null;
        }
        int iIndexOf2 = str.indexOf("id=\"translateResult\"");
        if (iIndexOf2 >= 0) {
            str = str.substring(iIndexOf2);
        }
        int iIndexOf3 = str.indexOf("<li>");
        if (iIndexOf3 < 0) {
            iIndexOf3 = str.indexOf("<li >");
        }
        if (iIndexOf3 < 0 || (iIndexOf = str.indexOf("</li>", iIndexOf3)) < 0) {
            return null;
        }
        java.lang.String strTrim = str.substring(iIndexOf3 + 4, iIndexOf).trim();
        if (strTrim.isEmpty()) {
            return null;
        }
        java.lang.String strReplace = strTrim.replace("&nbsp;", " ").replace("&lt;", "<").replace("&gt;", ">").replace("&amp;", "&").replace("&#39;", "'").replace("&quot;", "\"");
        if (strReplace.trim().isEmpty()) {
            return null;
        }
        return strReplace;
    }

    private static java.lang.String readAll(java.io.InputStream inputStream) throws java.lang.Exception {
        try {
            java.io.BufferedReader bufferedReader = new java.io.BufferedReader(new java.io.InputStreamReader(inputStream, java.nio.charset.StandardCharsets.UTF_8));
            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            while (true) {
                java.lang.String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                sb.append(line);
                sb.append('\n');
            }
            return sb.toString();
        } finally {
            try {
                inputStream.close();
            } catch (java.lang.Throwable unused) {
            }
        }
    }

    private static java.lang.String parseTranslated(java.lang.String str) {
        java.lang.String asString;
        if (str != null && !str.isEmpty()) {
            try {
                com.google.gson.JsonObject asJsonObject = com.google.gson.JsonParser.parseString(str.trim()).getAsJsonObject();
                if (asJsonObject.has("code") && asJsonObject.get("code").getAsInt() == 0) {
                    com.google.gson.JsonObject asJsonObject2 = (asJsonObject.has("data") && asJsonObject.get("data").isJsonObject()) ? asJsonObject.getAsJsonObject("data") : null;
                    if (asJsonObject2 != null && asJsonObject2.has("translated") && (asString = asJsonObject2.get("translated").getAsString()) != null) {
                        return asString.trim();
                    }
                    return null;
                }
            } catch (java.lang.Throwable thRethrow3) {
                com.tencent.qqlitepro.util.Log.w("翻译响应解析失败: " + thRethrow3);
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void invokeCallback(java.lang.Object obj, java.lang.String str) {
        if (obj == null) {
            return;
        }
        try {
            java.lang.reflect.Method method = obj.getClass().getMethod("invoke", java.lang.Object.class);
            method.setAccessible(true);
            method.invoke(obj, str);
        } catch (java.lang.Throwable thRethrow3) {
            com.tencent.qqlitepro.util.Log.w("翻译回调失败: " + thRethrow3);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    private static android.content.Context appContext() {
        try {
            java.lang.reflect.Method method = java.lang.Class.forName("android.app.ActivityThread").getMethod("currentApplication", new java.lang.Class[0]);
            method.setAccessible(true);
            java.lang.Object objInvoke = method.invoke(null, new java.lang.Object[0]);
            if (objInvoke instanceof android.content.Context) {
                return (android.content.Context) objInvoke;
            }
            return null;
        } catch (java.lang.Throwable unused) {
            return null;
        }
    }
}
