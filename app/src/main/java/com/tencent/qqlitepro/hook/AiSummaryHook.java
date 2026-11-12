package com.tencent.qqlitepro.hook;

/* JADX INFO: loaded from: classes.dex */
public final class AiSummaryHook {
    private static final int HISTORY_MAX = 100;
    private static final java.util.List<com.tencent.qqlitepro.hook.AiSummaryHook.HistoryEntry> sHistory = java.util.Collections.synchronizedList(new java.util.ArrayList());
    private static volatile boolean sLoadAttempted = false;
    private static volatile boolean sSummarizeApiInstalled = false;

    public static java.util.List<com.tencent.qqlitepro.hook.AiSummaryHook.HistoryEntry> history() {
        return sHistory;
    }

    public static boolean isInstalled() {
        return sSummarizeApiInstalled;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.lang.String styleStringFor(int i) {
        if (i != 1) {
            return i != 2 ? "bullets" : "detailed";
        }
        return "tldr";
    }

    public static final class HistoryEntry {
        public java.lang.String prompt;
        public java.lang.String response;
        public java.lang.String style;
        public long time;

        public HistoryEntry(long j, java.lang.String str, java.lang.String str2, java.lang.String str3) {
            this.time = j;
            this.style = str;
            this.prompt = str2;
            this.response = str3;
        }

        public java.lang.String toLine() {
            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            sb.append(this.time);
            sb.append((char) 1);
            sb.append(this.style);
            sb.append((char) 1);
            java.lang.String str = this.prompt;
            sb.append(str == null ? "" : str.replace('\n', ' '));
            sb.append((char) 1);
            java.lang.String str2 = this.response;
            sb.append(str2 != null ? str2.replace('\n', ' ') : "");
            return sb.toString();
        }

        public static com.tencent.qqlitepro.hook.AiSummaryHook.HistoryEntry fromLine(java.lang.String str) {
            try {
                java.lang.String[] strArrSplit = str.split("\u0001", 4);
                if (strArrSplit.length != 4) {
                    return null;
                }
                return new com.tencent.qqlitepro.hook.AiSummaryHook.HistoryEntry(java.lang.Long.parseLong(strArrSplit[0]), strArrSplit[1], strArrSplit[2], strArrSplit[3]);
            } catch (java.lang.Throwable unused) {
                return null;
            }
        }

        public java.lang.String render() {
            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            sb.append(java.text.SimpleDateFormat.getDateTimeInstance().format(new java.util.Date(this.time)));
            sb.append("  [" + com.tencent.qqlitepro.hook.AiSummaryHook.styleLabel(this.style) + "]\n");
            java.lang.String str = this.prompt;
            if (str != null && !str.isEmpty()) {
                sb.append("提示词: ");
                sb.append(this.prompt);
                sb.append('\n');
            }
            sb.append("响应: ");
            sb.append(this.response);
            return sb.toString();
        }
    }

    private AiSummaryHook() {
    }

    public static java.lang.String stylePrompt(java.lang.String str) {
        if (str == null || str.equals("bullets")) {
            return "你是一个聊天记录总结助手。请用【要点】的形式，用简洁的要点条目归纳下面聊天记录的核心内容，突出关键信息、结论和行动项，使用项目符号，语言清晰。";
        }
        return str.equals("detailed") ? "你是一个聊天记录总结助手。请对下面聊天记录进行【详细】总结，覆盖主要话题、各方观点、讨论经过与结果，保留关键细节，语言通顺完整。" : "你是一个聊天记录总结助手。请为下面聊天记录生成【简短摘要】(tldr)，用一两句话概括核心内容，简明扼要，不说废话。";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.lang.String styleLabel(java.lang.String str) {
        if (str == null || str.equals("bullets")) {
            return "要点";
        }
        return str.equals("detailed") ? "详细" : "摘要";
    }

    public static java.lang.String buildPromptWithMessages(java.lang.String str, java.lang.String str2, java.util.List<java.lang.String> list) {
        java.lang.StringBuilder sb = new java.lang.StringBuilder(promptFor(str, str2));
        sb.append("\n\n--- 聊天记录开始 ---\n");
        if (list == null || list.isEmpty()) {
            sb.append("(无消息内容)");
        } else {
            for (java.lang.String str3 : list) {
                if (str3 != null) {
                    sb.append(str3);
                    sb.append('\n');
                }
            }
        }
        sb.append("--- 聊天记录结束 ---");
        return sb.toString();
    }

    private static java.lang.String promptFor(java.lang.String str, java.lang.String str2) {
        java.lang.String strStylePrompt = stylePrompt(str);
        if (str2 == null || str2.isEmpty() || "auto".equalsIgnoreCase(str2)) {
            return strStylePrompt;
        }
        return strStylePrompt + "\n（请使用语言：" + str2 + " 输出）";
    }

    private static java.lang.String post(java.lang.String str, java.lang.String str2, java.lang.String str3) throws java.lang.Exception {
        java.io.BufferedReader bufferedReader;
        java.net.HttpURLConnection httpURLConnection = (java.net.HttpURLConnection) new java.net.URL(str).openConnection();
        try {
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setReadTimeout(60000);
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            if (str2 != null && !str2.isEmpty()) {
                httpURLConnection.setRequestProperty("Authorization", "Bearer " + str2);
            }
            byte[] bytes = str3.getBytes(java.nio.charset.StandardCharsets.UTF_8);
            httpURLConnection.setRequestProperty("Content-Length", java.lang.String.valueOf(bytes.length));
            java.io.OutputStream outputStream = httpURLConnection.getOutputStream();
            try {
                outputStream.write(bytes);
                outputStream.flush();
                outputStream.close();
                int responseCode = httpURLConnection.getResponseCode();
                if (responseCode >= 200 && responseCode < 300) {
                    bufferedReader = new java.io.BufferedReader(new java.io.InputStreamReader(httpURLConnection.getInputStream(), java.nio.charset.StandardCharsets.UTF_8));
                } else {
                    bufferedReader = new java.io.BufferedReader(new java.io.InputStreamReader(httpURLConnection.getErrorStream(), java.nio.charset.StandardCharsets.UTF_8));
                }
                java.lang.StringBuilder sb = new java.lang.StringBuilder();
                while (true) {
                    java.lang.String line = bufferedReader.readLine();
                    if (line == null) {
                        break;
                    }
                    sb.append(line);
                    sb.append('\n');
                }
                bufferedReader.close();
                if (responseCode < 200 || responseCode >= 300) {
                    throw new java.lang.RuntimeException("HTTP " + responseCode + ": " + ((java.lang.Object) sb));
                }
                java.lang.String string = sb.toString();
                httpURLConnection.disconnect();
                return string;
            } catch (java.lang.Throwable th) {
                outputStream.close();
                throw th;
            }
        } catch (java.lang.Throwable th2) {
            httpURLConnection.disconnect();
            throw th2;
        }
    }

    private static java.lang.String buildRequestBody(java.lang.String str, java.lang.String str2, java.lang.String str3, java.util.List<java.lang.String> list) {
        return requestFromContent(str, str2, buildPromptWithMessages(str2, str3, list));
    }

    private static java.lang.String requestFromContent(java.lang.String str, java.lang.String str2, java.lang.String str3) {
        return "{\"model\":\"" + jsonEscape((str == null || str.trim().isEmpty()) ? "" : str.trim()) + "\",\"messages\":[{\"role\":\"user\",\"content\":\"" + jsonEscape(str3) + "\"}],\"stream\":false}";
    }

    private static java.lang.String jsonEscape(java.lang.String str) {
        if (str == null) {
            return "";
        }
        java.lang.StringBuilder sb = new java.lang.StringBuilder(str.length());
        for (int i = 0; i < str.length(); i++) {
            char cCharAt = str.charAt(i);
            if (cCharAt == '\f') {
                sb.append("\\f");
            } else if (cCharAt == '\r') {
                sb.append("\\r");
            } else if (cCharAt == '\"') {
                sb.append("\\\"");
            } else if (cCharAt == '\\') {
                sb.append("\\\\");
            } else {
                switch (cCharAt) {
                    case '\b':
                        sb.append("\\b");
                        break;
                    case '\t':
                        sb.append("\\t");
                        break;
                    case '\n':
                        sb.append("\\n");
                        break;
                    default:
                        if (cCharAt < ' ') {
                            sb.append(java.lang.String.format("\\u%04x", java.lang.Integer.valueOf(cCharAt)));
                        } else {
                            sb.append(cCharAt);
                        }
                        break;
                }
            }
        }
        return sb.toString();
    }

    public static java.lang.String extractText(java.lang.String str) {
        if (str == null) {
            return "";
        }
        java.lang.String strTrim = str.trim();
        if (strTrim.isEmpty()) {
            return "";
        }
        java.lang.String strExtractSse = extractSse(strTrim);
        if (strExtractSse != null) {
            return strExtractSse;
        }
        java.lang.String strExtractJson = extractJson(strTrim);
        return strExtractJson != null ? strExtractJson : strTrim;
    }

    private static java.lang.String extractSse(java.lang.String str) {
        java.lang.String strDeltaFromJson;
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        boolean z = false;
        for (java.lang.String str2 : str.split("\n")) {
            java.lang.String strTrim = str2.trim();
            if (strTrim.startsWith("data:")) {
                strTrim = strTrim.substring(5).trim();
            }
            if (!strTrim.equals("[DONE]") && !strTrim.isEmpty() && (strDeltaFromJson = deltaFromJson(strTrim)) != null) {
                sb.append(strDeltaFromJson);
                z = true;
            }
        }
        if (z) {
            return sb.toString().trim();
        }
        return null;
    }

    private static java.lang.String extractJson(java.lang.String str) {
        int iLastIndexOf;
        int iIndexOf = str.indexOf(123);
        if (iIndexOf < 0 || (iLastIndexOf = str.lastIndexOf(125)) <= iIndexOf) {
            return null;
        }
        java.lang.String strSubstring = str.substring(iIndexOf, iLastIndexOf + 1);
        java.lang.String strDeltaFromJson = deltaFromJson(strSubstring);
        if (strDeltaFromJson != null) {
            return strDeltaFromJson;
        }
        java.lang.String strExtractField = extractField(strSubstring, "\"content\"");
        if (strExtractField != null) {
            return strExtractField;
        }
        return null;
    }

    private static java.lang.String deltaFromJson(java.lang.String str) {
        try {
            int iIndexOf = str.indexOf("\"choices\"");
            if (iIndexOf >= 0) {
                str = str.substring(iIndexOf);
            }
            int iIndexOf2 = str.indexOf("\"delta\"");
            int iIndexOf3 = iIndexOf2 >= 0 ? -1 : str.indexOf("\"message\"");
            if (iIndexOf2 < 0) {
                iIndexOf2 = iIndexOf3;
            }
            if (iIndexOf2 >= 0) {
                return extractField(str.substring(iIndexOf2), "\"content\"");
            }
            return extractField(str, "\"content\"");
        } catch (java.lang.Throwable unused) {
            return null;
        }
    }

    private static java.lang.String extractField(java.lang.String str, java.lang.String str2) {
        int iIndexOf;
        int iIndexOf2;
        int i;
        int iIndexOf3;
        int iIndexOf4 = str.indexOf(str2);
        if (iIndexOf4 >= 0 && (iIndexOf = str.indexOf(58, iIndexOf4 + str2.length())) >= 0 && (iIndexOf2 = str.indexOf(34, iIndexOf)) >= 0 && (iIndexOf3 = str.indexOf(34, (i = iIndexOf2 + 1))) >= 0) {
            return str.substring(i, iIndexOf3).replace("\\n", "\n").replace("\\\"", "\"").replace("\\\\", "\\");
        }
        return null;
    }

    public static java.lang.String testRequest(java.lang.String str, java.lang.String str2, java.lang.String str3) {
        if (str == null) {
            return "错误: 请先填写有效的请求地址";
        }
        try {
            if (str.trim().isEmpty()) {
                return "错误: 请先填写有效的请求地址";
            }
            java.lang.String strTrim = "";
            java.lang.String str4 = "{\"model\":\"" + ((str3 == null || str3.trim().isEmpty()) ? "" : str3.trim()) + "\",\"messages\":[{\"role\":\"user\",\"content\":\"你好\"}],\"stream\":false}";
            java.lang.String strTrim2 = str.trim();
            if (str2 != null) {
                strTrim = str2.trim();
            }
            java.lang.String strPost = post(strTrim2, strTrim, str4);
            java.lang.String strExtractText = extractText(strPost);
            java.lang.StringBuilder sb = new java.lang.StringBuilder("成功 (");
            sb.append(strPost.length());
            sb.append(" 字节)\n");
            if (strExtractText.isEmpty()) {
                strExtractText = strPost.trim();
            }
            sb.append(strExtractText);
            return sb.toString();
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("AI 测试请求失败: " + th);
            return "请求失败: " + th;
        }
    }

    public static java.lang.String keywordReply(java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.String str4, java.lang.String str5) {
        if (str != null) {
            try {
                if (!str.trim().isEmpty()) {
                    java.lang.String strTrim = "";
                    java.lang.String strTrim2 = (str3 == null || str3.trim().isEmpty()) ? "" : str3.trim();
                    java.lang.StringBuilder sb = new java.lang.StringBuilder();
                    if (str4 != null && !str4.trim().isEmpty()) {
                        sb.append("{\"role\":\"system\",\"content\":\"");
                        sb.append(jsonEscape(str4.trim()));
                        sb.append("\"},");
                    }
                    sb.append("{\"role\":\"user\",\"content\":\"");
                    if (str5 == null) {
                        str5 = "";
                    }
                    sb.append(jsonEscape(str5));
                    sb.append("\"}");
                    java.lang.String str6 = "{\"model\":\"" + strTrim2 + "\",\"messages\":[" + ((java.lang.Object) sb) + "],\"stream\":false}";
                    java.lang.String strTrim3 = str.trim();
                    if (str2 != null) {
                        strTrim = str2.trim();
                    }
                    java.lang.String strPost = post(strTrim3, strTrim, str6);
                    java.lang.String strExtractText = extractText(strPost);
                    if (strExtractText.isEmpty()) {
                        strExtractText = strPost.trim();
                    }
                    com.tencent.qqlitepro.util.Log.i("关键词 AI 回复完成 len=" + strExtractText.length());
                    return strExtractText;
                }
            } catch (java.lang.Throwable th) {
                com.tencent.qqlitepro.util.Log.w("关键词 AI 回复失败: " + th);
            }
        }
        return null;
    }

    public static java.lang.String summarize(android.content.Context context, java.util.List<java.lang.String> list, java.lang.String str, java.lang.String str2) {
        if (str == null || str.isEmpty()) {
            str = "bullets";
        }
        if (context != null) {
            com.tencent.qqlitepro.util.Config.reloadForce(context);
        }
        java.lang.String strSummaryUrl = com.tencent.qqlitepro.util.Config.summaryUrl(context);
        java.lang.String strSummaryApiKey = com.tencent.qqlitepro.util.Config.summaryApiKey(context);
        java.lang.String strSummaryModel = com.tencent.qqlitepro.util.Config.summaryModel(context);
        java.lang.String strPromptFor = promptFor(str, str2);
        if (strSummaryUrl == null || strSummaryUrl.trim().isEmpty()) {
            addHistory(str, strPromptFor, "未配置自定义模型地址，沿用 QQMax 原生总结。");
            return "";
        }
        try {
            java.lang.String strPost = post(strSummaryUrl.trim(), strSummaryApiKey, buildRequestBody(strSummaryModel, str, str2, list));
            java.lang.String strExtractText = extractText(strPost);
            if (strExtractText.isEmpty()) {
                strExtractText = strPost.trim();
            }
            addHistory(str, strPromptFor, strExtractText);
            com.tencent.qqlitepro.util.Log.i("AI 总结完成 style=" + str + " lang=" + str2 + " len=" + strExtractText.length());
            return strExtractText;
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("AI 总结请求失败: " + th);
            addHistory(str, strPromptFor, "请求失败: " + th);
            return "";
        }
    }

    private static void addHistory(java.lang.String str, java.lang.String str2, java.lang.String str3) {
        sHistory.add(0, new com.tencent.qqlitepro.hook.AiSummaryHook.HistoryEntry(java.lang.System.currentTimeMillis(), str, str2, str3));
        while (true) {
            java.util.List<com.tencent.qqlitepro.hook.AiSummaryHook.HistoryEntry> list = sHistory;
            if (list.size() > 100) {
                list.remove(list.size() - 1);
            } else {
                persistHistory();
                return;
            }
        }
    }

    private static java.io.File historyFile(android.content.Context context) {
        if (context == null) {
            return null;
        }
        return new java.io.File(context.getCacheDir(), "ai_summary_history.txt");
    }

    private static synchronized void persistHistory() {
        try {
            java.io.File fileHistoryFile = historyFile(appContext());
            if (fileHistoryFile == null) {
                return;
            }
            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            java.util.List<com.tencent.qqlitepro.hook.AiSummaryHook.HistoryEntry> list = sHistory;
            synchronized (list) {
                java.util.Iterator<com.tencent.qqlitepro.hook.AiSummaryHook.HistoryEntry> it = list.iterator();
                while (it.hasNext()) {
                    sb.append(it.next().toLine());
                    sb.append('\n');
                }
            }
            java.io.FileOutputStream fileOutputStream = new java.io.FileOutputStream(fileHistoryFile, false);
            try {
                fileOutputStream.write(sb.toString().getBytes(java.nio.charset.StandardCharsets.UTF_8));
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (java.lang.Throwable th) {
                fileOutputStream.close();
                throw th;
            }
        } catch (java.lang.Throwable th2) {
            com.tencent.qqlitepro.util.Log.w("写入总结历史失败: " + th2);
        }
    }

    private static synchronized void loadHistory(android.content.Context context) {
        if (sLoadAttempted) {
            return;
        }
        sLoadAttempted = true;
        try {
            java.io.File fileHistoryFile = historyFile(context);
            if (fileHistoryFile != null && fileHistoryFile.exists()) {
                java.io.FileInputStream fileInputStream = new java.io.FileInputStream(fileHistoryFile);
                try {
                    java.io.BufferedReader bufferedReader = new java.io.BufferedReader(new java.io.InputStreamReader(fileInputStream, java.nio.charset.StandardCharsets.UTF_8));
                    while (true) {
                        java.lang.String line = bufferedReader.readLine();
                        if (line == null) {
                            break;
                        }
                        com.tencent.qqlitepro.hook.AiSummaryHook.HistoryEntry historyEntryFromLine = com.tencent.qqlitepro.hook.AiSummaryHook.HistoryEntry.fromLine(line);
                        if (historyEntryFromLine != null) {
                            sHistory.add(historyEntryFromLine);
                        }
                    }
                    bufferedReader.close();
                    fileInputStream.close();
                } catch (java.lang.Throwable th) {
                    fileInputStream.close();
                    throw th;
                }
            }
        } catch (java.lang.Throwable th2) {
            com.tencent.qqlitepro.util.Log.w("加载总结历史失败: " + th2);
        }
    }

    public static void ensureLoaded(android.content.Context context) {
        loadHistory(context);
    }

    public static java.util.List<java.lang.String> renderAll(android.content.Context context) {
        loadHistory(context);
        java.util.ArrayList arrayList = new java.util.ArrayList();
        java.util.List<com.tencent.qqlitepro.hook.AiSummaryHook.HistoryEntry> list = sHistory;
        synchronized (list) {
            java.util.Iterator<com.tencent.qqlitepro.hook.AiSummaryHook.HistoryEntry> it = list.iterator();
            while (it.hasNext()) {
                arrayList.add(it.next().render());
            }
        }
        return arrayList;
    }

    public static void clearHistory(android.content.Context context) {
        sHistory.clear();
        try {
            java.io.File fileHistoryFile = historyFile(context);
            if (fileHistoryFile == null || !fileHistoryFile.exists() || fileHistoryFile.delete()) {
                return;
            }
            fileHistoryFile.delete();
        } catch (java.lang.Throwable unused) {
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

    public static void patchSummarizeApi(java.lang.ClassLoader classLoader) {
        java.lang.reflect.Method method;
        if (sSummarizeApiInstalled) {
            return;
        }
        try {
            java.lang.Class<?> cls = java.lang.Class.forName("momoi.mod.qqpro.hook.summarize.Summarizer", false, classLoader);
            java.lang.Class<?> cls2 = java.lang.Class.forName("momoi.mod.qqpro.hook.summarize.Summarizer$Listener", false, classLoader);
            java.lang.reflect.Method[] declaredMethods = cls.getDeclaredMethods();
            int length = declaredMethods.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    method = null;
                    break;
                }
                method = declaredMethods[i];
                if (method.getName().equals("summarize") && com.tencent.qqlitepro.util.Log$$ExternalSyntheticBackport0.m(method) == 4 && method.getParameterTypes()[0] == java.util.List.class && method.getParameterTypes()[1] == java.lang.Integer.TYPE && method.getParameterTypes()[2] == java.lang.String.class && method.getParameterTypes()[3] == cls2) {
                    break;
                } else {
                    i++;
                }
            }
            if (method == null) {
                for (java.lang.reflect.Method method2 : cls.getDeclaredMethods()) {
                    if (method2.getName().equals("summarize") && com.tencent.qqlitepro.util.Log$$ExternalSyntheticBackport0.m(method2) == 4) {
                        com.tencent.qqlitepro.util.Log.i("原生总结 API：精确签名未命中，按参数个数回退命中");
                        method = method2;
                        break;
                    }
                }
            }
            if (method == null) {
                com.tencent.qqlitepro.util.Log.w("未找到 Summarizer.summarize 目标方法（自定义模型总结接管未生效，稍后重试）");
                return;
            }
            de.robv.android.xposed.XposedBridge.hookMethod(method, new com.tencent.qqlitepro.hook.AiSummaryHook.AnonymousClass1(cls2));
            sSummarizeApiInstalled = true;
            com.tencent.qqlitepro.util.Log.i("已 hook 原生总结 API Summarizer.summarize（自定义模型接管）");
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("hook Summarizer.summarize 失败（稍后重试）: " + th);
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.AiSummaryHook$1, reason: invalid class name */
    static class AnonymousClass1 extends de.robv.android.xposed.XC_MethodHook {
        final /* synthetic */ java.lang.Class val$listenerType;

        AnonymousClass1(java.lang.Class cls) {
            this.val$listenerType = cls;
        }

        protected void beforeHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            java.lang.String strSummaryUrl;
            try {
                android.content.Context contextAppContext = com.tencent.qqlitepro.hook.AiSummaryHook.appContext();
                if (contextAppContext != null && (strSummaryUrl = com.tencent.qqlitepro.util.Config.summaryUrl(contextAppContext)) != null && !strSummaryUrl.trim().isEmpty()) {
                    int iIntValue = 0;
                    java.lang.Object obj = (methodHookParam.args == null || methodHookParam.args.length < 1) ? null : methodHookParam.args[0];
                    if ((obj instanceof java.util.List) && !((java.util.List) obj).isEmpty()) {
                        if (methodHookParam.args.length >= 2 && (methodHookParam.args[1] instanceof java.lang.Integer)) {
                            iIntValue = ((java.lang.Integer) methodHookParam.args[1]).intValue();
                        }
                        java.lang.String str = "auto";
                        if (methodHookParam.args.length >= 3 && (methodHookParam.args[2] instanceof java.lang.String)) {
                            str = (java.lang.String) methodHookParam.args[2];
                        }
                        java.util.ArrayList arrayList = new java.util.ArrayList();
                        java.util.Iterator it = ((java.util.List) obj).iterator();
                        while (it.hasNext()) {
                            java.lang.String strMsgToLine = com.tencent.qqlitepro.hook.AiSummaryHook.msgToLine(it.next());
                            if (strMsgToLine != null && !strMsgToLine.isEmpty()) {
                                arrayList.add(strMsgToLine);
                            }
                        }
                        if (arrayList.isEmpty()) {
                            com.tencent.qqlitepro.util.Log.w("原生总结 API：无法从消息列表提取文本，回退宿主");
                            return;
                        }
                        java.lang.String strStyleStringFor = com.tencent.qqlitepro.hook.AiSummaryHook.styleStringFor(iIntValue);
                        java.lang.Object obj2 = (methodHookParam.args == null || methodHookParam.args.length < 4) ? null : methodHookParam.args[3];
                        if (obj2 == null) {
                            return;
                        }
                        methodHookParam.setResult((java.lang.Object) null);
                        com.tencent.qqlitepro.hook.AiSummaryHook.runCustomSummaryAsync(contextAppContext, arrayList, strStyleStringFor, str, obj2, this.val$listenerType);
                    }
                }
            } catch (java.lang.Throwable th) {
                com.tencent.qqlitepro.util.Log.w("Summarizer.summarize hook 异常: " + th);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.lang.String msgToLine(java.lang.Object obj) {
        if (obj == null) {
            return "";
        }
        try {
            java.lang.Object objCallMethod = de.robv.android.xposed.XposedHelpers.callMethod(obj, "getSender", new java.lang.Object[0]);
            java.lang.Object objCallMethod2 = de.robv.android.xposed.XposedHelpers.callMethod(obj, "getText", new java.lang.Object[0]);
            java.lang.String strValueOf = objCallMethod == null ? "" : java.lang.String.valueOf(objCallMethod);
            java.lang.String strValueOf2 = objCallMethod2 == null ? "" : java.lang.String.valueOf(objCallMethod2);
            if (strValueOf.isEmpty()) {
                return strValueOf2;
            }
            return strValueOf + ": " + strValueOf2;
        } catch (java.lang.Throwable unused) {
            return "";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void runCustomSummaryAsync(android.content.Context context, java.util.List<java.lang.String> list, java.lang.String str, java.lang.String str2, java.lang.Object obj, java.lang.Class<?> cls) {
        new java.lang.Thread(new com.tencent.qqlitepro.hook.AiSummaryHook$$ExternalSyntheticLambda1(context, list, str, str2, obj, cls), "antirecall-summarize").start();
    }

    static /* synthetic */ void lambda$runCustomSummaryAsync$0(android.content.Context context, java.util.List list, java.lang.String str, java.lang.String str2, java.lang.Object obj, java.lang.Class cls) {
        java.lang.String strSummarize;
        java.lang.String strValueOf = null;
        try {
            strSummarize = summarize(context, list, str, str2);
        } catch (java.lang.Throwable th) {
            strValueOf = th.getMessage() == null ? java.lang.String.valueOf(th) : th.getMessage();
            strSummarize = null;
        }
        if (obj == null) {
            return;
        }
        try {
            if (strValueOf != null) {
                cls.getMethod("onError", java.lang.String.class, java.lang.Boolean.TYPE).invoke(obj, strValueOf, java.lang.Boolean.TRUE);
                return;
            }
            if (strSummarize == null || strSummarize.isEmpty()) {
                strSummarize = "(无结果)";
            }
            java.lang.reflect.Method method = cls.getMethod("onChunk", java.lang.String.class);
            java.lang.reflect.Method method2 = cls.getMethod("onDone", java.lang.Integer.TYPE, java.lang.Integer.TYPE);
            method.invoke(obj, strSummarize);
            method2.invoke(obj, 0, 0);
        } catch (java.lang.Throwable th2) {
            com.tencent.qqlitepro.util.Log.w("回调原生总结 Listener 失败: " + th2);
        }
    }
}
