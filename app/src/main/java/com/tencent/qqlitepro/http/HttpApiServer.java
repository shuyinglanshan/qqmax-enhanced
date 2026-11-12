package com.tencent.qqlitepro.http;

/* JADX INFO: loaded from: classes.dex */
public final class HttpApiServer {
    private static volatile java.net.ServerSocket sServer;
    private static final com.google.gson.Gson GSON = new com.google.gson.Gson();
    private static volatile boolean started = false;
    private static final int DEFAULT_PORT = 8081;
    private static volatile int sPort = DEFAULT_PORT;
    private static volatile long sStartedAt = 0;
    private static final java.lang.Object lock = new java.lang.Object();

    public static int getPort() {
        return sPort;
    }

    private HttpApiServer() {
    }

    public static synchronized void start() {
        int iPort;
        if (started) {
            return;
        }
        sStartedAt = java.lang.System.currentTimeMillis();
        try {
            android.content.Context contextAppContext = com.tencent.qqlitepro.cache.MsgCache.appContext();
            if (contextAppContext != null && (iPort = com.tencent.qqlitepro.util.Config.port(contextAppContext)) >= 1024 && iPort <= 65535) {
                sPort = iPort;
            }
        } catch (java.lang.Throwable unused) {
        }
        startAccept();
        started = true;
    }

    public static void setPort(int i) {
        boolean z;
        if (i < 1024 || i > 65535) {
            com.tencent.qqlitepro.util.Log.w("端口非法: " + i);
            return;
        }
        synchronized (lock) {
            if (i == sPort) {
                return;
            }
            sPort = i;
            if (started) {
                closeServer();
                z = true;
            } else {
                z = false;
            }
            if (z) {
                startAccept();
            }
        }
    }

    private static void startAccept() {
        java.lang.Thread thread = new java.lang.Thread(new com.tencent.qqlitepro.http.HttpApiServer$$ExternalSyntheticLambda0(), "antirecall-http");
        thread.setDaemon(true);
        thread.start();
        com.tencent.qqlitepro.util.Log.i("HTTP API 服务器已启动，端口 " + sPort);
    }

    private static void closeServer() {
        java.net.ServerSocket serverSocket = sServer;
        sServer = null;
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (java.io.IOException unused) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void acceptLoop() {
        int i;
        java.lang.Object obj = lock;
        synchronized (obj) {
            i = sPort;
        }
        try {
            java.net.ServerSocket serverSocket = new java.net.ServerSocket(i, 50);
            serverSocket.setReuseAddress(true);
            synchronized (obj) {
                sServer = serverSocket;
            }
            while (!serverSocket.isClosed()) {
                try {
                    java.net.Socket socketAccept = serverSocket.accept();
                    socketAccept.setSoTimeout(10000);
                    java.lang.Thread thread = new java.lang.Thread(new com.tencent.qqlitepro.http.HttpApiServer$$ExternalSyntheticLambda1(socketAccept), "antirecall-http-conn");
                    thread.setDaemon(true);
                    thread.start();
                } catch (java.io.IOException e) {
                    if (!serverSocket.isClosed()) {
                        com.tencent.qqlitepro.util.Log.w("HTTP accept 异常: " + e);
                    }
                }
            }
        } catch (java.io.IOException e2) {
            com.tencent.qqlitepro.util.Log.e("HTTP 服务器监听 " + i + " 失败: " + e2);
        }
    }

    private static final class Request {
        byte[] body;
        java.lang.String method;
        java.lang.String path;
        java.lang.String query;



        private Request() {
        }
    }

    private static com.tencent.qqlitepro.http.HttpApiServer.Request parse(java.io.InputStream inputStream) throws java.io.IOException {
        java.io.BufferedInputStream bufferedInputStream = new java.io.BufferedInputStream(inputStream);
        com.tencent.qqlitepro.http.HttpApiServer.Request request = new com.tencent.qqlitepro.http.HttpApiServer.Request();
        java.lang.String line = readLine(bufferedInputStream);
        if (line == null || line.isEmpty()) {
            throw new java.io.IOException("empty request");
        }
        java.lang.String[] strArrSplit = line.split(" ");
        if (strArrSplit.length < 2) {
            throw new java.io.IOException("bad request line: " + line);
        }
        request.method = strArrSplit[0].toUpperCase();
        java.lang.String str = strArrSplit[1];
        int iIndexOf = str.indexOf(63);
        if (iIndexOf >= 0) {
            request.path = str.substring(0, iIndexOf);
            request.query = str.substring(iIndexOf + 1);
        } else {
            request.path = str;
        }
        int i = -1;
        while (true) {
            java.lang.String line2 = readLine(bufferedInputStream);
            if (line2 == null || line2.isEmpty()) {
                break;
            }
            int iIndexOf2 = line2.indexOf(58);
            if (iIndexOf2 > 0) {
                java.lang.String lowerCase = line2.substring(0, iIndexOf2).trim().toLowerCase();
                java.lang.String strTrim = line2.substring(iIndexOf2 + 1).trim();
                if ("content-length".equals(lowerCase)) {
                    try {
                        i = java.lang.Integer.parseInt(strTrim);
                    } catch (java.lang.NumberFormatException unused) {
                    }
                }
            }
        }
        if (i > 0 && i < 4194304) {
            byte[] bArr = new byte[i];
            int i2 = 0;
            while (i2 < i) {
                int i3 = bufferedInputStream.read(bArr, i2, i - i2);
                if (i3 < 0) {
                    break;
                }
                i2 += i3;
            }
            request.body = i2 > 0 ? java.util.Arrays.copyOf(bArr, i2) : new byte[0];
        }
        return request;
    }

    private static java.lang.String readLine(java.io.InputStream inputStream) throws java.io.IOException {
        java.io.ByteArrayOutputStream byteArrayOutputStream = new java.io.ByteArrayOutputStream();
        boolean z = false;
        while (true) {
            int i = inputStream.read();
            if (i == -1) {
                if (z) {
                    break;
                }
                return null;
            }
            if (i == 10) {
                break;
            }
            if (i != 13) {
                byteArrayOutputStream.write(i);
            }
            z = true;
        }
        return byteArrayOutputStream.size() == 0 ? "" : new java.lang.String(byteArrayOutputStream.toByteArray(), java.nio.charset.StandardCharsets.UTF_8);
    }

    private static java.util.Map<java.lang.String, java.lang.String> parseQuery(java.lang.String str) {
        java.lang.String strDecode;
        java.util.LinkedHashMap linkedHashMap = new java.util.LinkedHashMap();
        if (str != null && !str.isEmpty()) {
            java.lang.String[] strArrSplit = str.split("&");
            int length = strArrSplit.length;
            for (int i = 0; i < length; i++) {
                java.lang.String strDecode2 = strArrSplit[i];
                if (!strDecode2.isEmpty()) {
                    int iIndexOf = strDecode2.indexOf(61);
                    if (iIndexOf >= 0) {
                        java.lang.String strSubstring = strDecode2.substring(0, iIndexOf);
                        strDecode = strDecode2.substring(iIndexOf + 1);
                        strDecode2 = strSubstring;
                    } else {
                        strDecode = "";
                    }
                    try {
                        strDecode2 = java.net.URLDecoder.decode(strDecode2, "UTF-8");
                        strDecode = java.net.URLDecoder.decode(strDecode, "UTF-8");
                    } catch (java.lang.Exception unused) {
                    }
                    linkedHashMap.put(strDecode2, strDecode);
                }
            }
        }
        return linkedHashMap;
    }

    private static void writeJson(java.io.OutputStream outputStream, int i, java.lang.String str, java.lang.String str2) throws java.io.IOException {
        writeRaw(outputStream, i, str, "application/json; charset=utf-8", str2);
    }

    private static void writeHtml(java.io.OutputStream outputStream, int i, java.lang.String str, java.lang.String str2) throws java.io.IOException {
        writeRaw(outputStream, i, str, "text/html; charset=utf-8", str2);
    }

    private static void writeRaw(java.io.OutputStream outputStream, int i, java.lang.String str, java.lang.String str2, java.lang.String str3) throws java.io.IOException {
        writeBytes(outputStream, i, str, str2, str3.getBytes(java.nio.charset.StandardCharsets.UTF_8));
    }

    private static void writeBytes(java.io.OutputStream outputStream, int i, java.lang.String str, java.lang.String str2, byte[] bArr) throws java.io.IOException {
        java.io.BufferedOutputStream bufferedOutputStream = new java.io.BufferedOutputStream(outputStream);
        java.lang.StringBuilder sb = new java.lang.StringBuilder(256);
        sb.append("HTTP/1.1 ");
        sb.append(i);
        sb.append(' ');
        sb.append(str);
        sb.append("\r\nContent-Type: ");
        sb.append(str2);
        sb.append("\r\nContent-Length: ");
        sb.append(bArr.length);
        sb.append("\r\nCache-Control: no-store\r\nConnection: close\r\n\r\n");
        bufferedOutputStream.write(sb.toString().getBytes(java.nio.charset.StandardCharsets.UTF_8));
        bufferedOutputStream.write(bArr);
        bufferedOutputStream.flush();
    }

    private static java.io.File mailAttDir(long j) {
        android.content.Context contextAppContext = com.tencent.qqlitepro.cache.MsgCache.appContext();
        if (contextAppContext == null || j <= 0) {
            return null;
        }
        return new java.io.File(new java.io.File(contextAppContext.getFilesDir(), "mail_att"), java.lang.String.valueOf(j));
    }

    private static java.lang.String safeName(java.lang.String str) {
        if (str == null || str.isEmpty()) {
            return "";
        }
        try {
            str = java.net.URLDecoder.decode(str, "UTF-8");
        } catch (java.lang.Throwable unused) {
        }
        java.lang.String name = new java.io.File(str).getName();
        return name == null ? "" : name;
    }

    private static byte[] readFile(java.io.File file) throws java.io.IOException {
        java.io.BufferedInputStream bufferedInputStream = new java.io.BufferedInputStream(new java.io.FileInputStream(file));
        try {
            java.io.ByteArrayOutputStream byteArrayOutputStream = new java.io.ByteArrayOutputStream((int) java.lang.Math.max(1024L, file.length()));
            byte[] bArr = new byte[8192];
            while (true) {
                int i = bufferedInputStream.read(bArr);
                if (i > 0) {
                    byteArrayOutputStream.write(bArr, 0, i);
                } else {
                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                    bufferedInputStream.close();
                    return byteArray;
                }
            }
        } catch (java.lang.Throwable th) {
            try {
                bufferedInputStream.close();
            } catch (java.lang.Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    private static java.lang.String guessMime(java.lang.String str) {
        java.lang.String lowerCase = str == null ? "" : str.toLowerCase(java.util.Locale.ROOT);
        if (lowerCase.endsWith(".png")) {
            return "image/png";
        }
        if (lowerCase.endsWith(".jpg") || lowerCase.endsWith(".jpeg")) {
            return "image/jpeg";
        }
        if (lowerCase.endsWith(".gif")) {
            return "image/gif";
        }
        if (lowerCase.endsWith(".webp")) {
            return "image/webp";
        }
        if (lowerCase.endsWith(".bmp")) {
            return "image/bmp";
        }
        if (lowerCase.endsWith(".svg")) {
            return "image/svg+xml";
        }
        if (lowerCase.endsWith(".html") || lowerCase.endsWith(".htm")) {
            return "text/html; charset=utf-8";
        }
        if (lowerCase.endsWith(".css")) {
            return "text/css; charset=utf-8";
        }
        if (lowerCase.endsWith(".js")) {
            return "application/javascript; charset=utf-8";
        }
        if (lowerCase.endsWith(".txt")) {
            return "text/plain; charset=utf-8";
        }
        if (lowerCase.endsWith(".json")) {
            return "application/json; charset=utf-8";
        }
        return lowerCase.endsWith(".pdf") ? "application/pdf" : "application/octet-stream";
    }

    private static boolean serveMailHtml(java.io.OutputStream outputStream, java.util.Map<java.lang.String, java.lang.String> map) {
        long j = 0;
        try {
            java.lang.String str = map.get("uid");
            if (str != null) {
                j = java.lang.Long.parseLong(str.trim());
            }
        } catch (java.lang.Throwable unused) {
        }
        try {
            java.lang.String strSafeName = safeName(map.get(com.sun.mail.imap.IMAPStore.ID_NAME));
            if (strSafeName.isEmpty()) {
                strSafeName = "body.html";
            }
            java.io.File fileMailAttDir = mailAttDir(j);
            java.io.File file = fileMailAttDir == null ? null : new java.io.File(fileMailAttDir, strSafeName);
            if (file != null && file.isFile()) {
                java.lang.String str2 = new java.lang.String(readFile(file), java.nio.charset.StandardCharsets.UTF_8);
                if (fileMailAttDir != null) {
                    java.lang.String strReplace = str2.replace("file://" + fileMailAttDir.getAbsolutePath() + "/", "/mail_file/" + j + "/");
                    java.lang.StringBuilder sb = new java.lang.StringBuilder("file://");
                    sb.append(fileMailAttDir.getAbsolutePath());
                    str2 = strReplace.replace(sb.toString(), "/mail_file/" + j);
                }
                writeBytes(outputStream, 200, "OK", "text/html; charset=utf-8", com.tencent.qqlitepro.hook.SettingsInjector.wrapHtmlWithCss(str2, mailBgDataUrl(), "http://127.0.0.1:" + sPort + "/mail_file/" + j + "/").getBytes(java.nio.charset.StandardCharsets.UTF_8));
                return true;
            }
            return false;
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("输出邮件网页失败: " + th);
            return false;
        }
    }

    private static java.lang.String mailBgDataUrl() {
        java.lang.String strMailWebBg;
        try {
            android.content.Context contextAppContext = com.tencent.qqlitepro.cache.MsgCache.appContext();
            if (contextAppContext != null && (strMailWebBg = com.tencent.qqlitepro.util.Config.mailWebBg(contextAppContext)) != null && !strMailWebBg.isEmpty()) {
                java.io.File file = new java.io.File(strMailWebBg);
                if (!file.isFile()) {
                    return "";
                }
                java.lang.String strGuessMime = guessMime(file.getName());
                if (!strGuessMime.startsWith("image/")) {
                    strGuessMime = "image/png";
                }
                return "data:" + strGuessMime + ";base64," + android.util.Base64.encodeToString(readFile(file), 2);
            }
            return "";
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("读取网页渲染底图失败: " + th);
            return "";
        }
    }

    private static boolean serveMailFile(java.io.OutputStream outputStream, java.lang.String str) {
        try {
            java.lang.String strSubstring = str.substring(11);
            int iIndexOf = strSubstring.indexOf(47);
            if (iIndexOf <= 0) {
                return false;
            }
            try {
                long j = java.lang.Long.parseLong(strSubstring.substring(0, iIndexOf));
                java.lang.String strSafeName = safeName(strSubstring.substring(iIndexOf + 1));
                if (strSafeName.isEmpty()) {
                    return false;
                }
                java.io.File fileMailAttDir = mailAttDir(j);
                java.io.File file = fileMailAttDir == null ? null : new java.io.File(fileMailAttDir, strSafeName);
                if (file != null && file.isFile()) {
                    writeBytes(outputStream, 200, "OK", guessMime(strSafeName), readFile(file));
                    return true;
                }
                return false;
            } catch (java.lang.Throwable unused) {
            }
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("输出邮件附件失败: " + th);
        }
        return false;
    }

    private static com.google.gson.JsonObject envelope(int i, java.lang.String str, com.google.gson.JsonElement jsonElement) {
        com.google.gson.JsonObject jsonObject = new com.google.gson.JsonObject();
        jsonObject.addProperty("code", java.lang.Integer.valueOf(i));
        if (str == null) {
            str = "";
        }
        jsonObject.addProperty("text", str);
        if (jsonElement == null) {
            jsonElement = new com.google.gson.JsonObject();
        }
        jsonObject.add("data", jsonElement);
        return jsonObject;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void handle(java.net.Socket socket) {
        int i;
        com.google.gson.JsonObject jsonObjectEnvelope;
        java.lang.String str;
        try {
            try {
                try {
                    com.tencent.qqlitepro.http.HttpApiServer.Request request = parse(socket.getInputStream());
                    java.lang.String str2 = request.body == null ? "" : new java.lang.String(request.body, java.nio.charset.StandardCharsets.UTF_8);
                    try {
                        str = "OK";
                        i = 200;
                        if ("GET".equals(request.method) && "/get_sessions_info".equals(request.path)) {
                            jsonObjectEnvelope = handleGetSessions(parseQuery(request.query));
                        } else if ("GET".equals(request.method) && "/get_recent_msgs".equals(request.path)) {
                            jsonObjectEnvelope = handleGetRecentMsgs(parseQuery(request.query));
                        } else if ("GET".equals(request.method) && "/get_msg_history".equals(request.path)) {
                            jsonObjectEnvelope = handleGetMsgHistory(parseQuery(request.query));
                        } else if ("POST".equals(request.method) && "/send_msg".equals(request.path)) {
                            jsonObjectEnvelope = handleSendMsg(str2);
                        } else if ("POST".equals(request.method) && "/send_reply_msg".equals(request.path)) {
                            jsonObjectEnvelope = handleSendReplyMsg(str2);
                        } else if ("POST".equals(request.method) && "/send_image".equals(request.path)) {
                            jsonObjectEnvelope = handleSendImage(str2);
                        } else if ("POST".equals(request.method) && "/send_video".equals(request.path)) {
                            jsonObjectEnvelope = handleSendVideo(str2);
                        } else if ("POST".equals(request.method) && "/set_whitelist".equals(request.path)) {
                            jsonObjectEnvelope = handleSetWhitelist(str2);
                        } else if ("POST".equals(request.method) && "/ban_all_status".equals(request.path)) {
                            jsonObjectEnvelope = handleBanAllStatus(str2);
                        } else if ("POST".equals(request.method) && "/get_group_notices".equals(request.path)) {
                            jsonObjectEnvelope = handleGetGroupNotices(str2);
                        } else if ("GET".equals(request.method) && "/get_friend_requests".equals(request.path)) {
                            jsonObjectEnvelope = handleGetFriendRequests(parseQuery(request.query));
                        } else if ("POST".equals(request.method) && "/send_friend_request".equals(request.path)) {
                            jsonObjectEnvelope = handleSendFriendRequest(str2);
                        } else if ("GET".equals(request.method) && "/get_group_requests".equals(request.path)) {
                            jsonObjectEnvelope = handleGetGroupRequests(parseQuery(request.query));
                        } else if ("POST".equals(request.method) && "/send_group_request".equals(request.path)) {
                            jsonObjectEnvelope = handleSendGroupRequest(str2);
                        } else if ("POST".equals(request.method) && "/handle_friend_request".equals(request.path)) {
                            jsonObjectEnvelope = handleHandleFriendRequest(str2);
                        } else if ("POST".equals(request.method) && "/handle_group_request".equals(request.path)) {
                            jsonObjectEnvelope = handleHandleGroupRequest(str2);
                        } else {
                            if ("GET".equals(request.method) && ("/".equals(request.path) || "/admin".equals(request.path))) {
                                writeHtml(socket.getOutputStream(), 200, "OK", com.tencent.qqlitepro.http.AdminWeb.buildAdminPage());
                                if (socket != null) {
                                    socket.close();
                                    return;
                                }
                                return;
                            }
                            if ("GET".equals(request.method) && "/api/config".equals(request.path)) {
                                jsonObjectEnvelope = handleApiConfigGet();
                            } else if ("POST".equals(request.method) && "/api/config".equals(request.path)) {
                                jsonObjectEnvelope = handleApiConfigSet(str2);
                            } else if ("GET".equals(request.method) && "/api/version".equals(request.path)) {
                                com.google.gson.JsonObject jsonObject = new com.google.gson.JsonObject();
                                jsonObject.addProperty(com.sun.mail.imap.IMAPStore.ID_VERSION, java.lang.Long.valueOf(com.tencent.qqlitepro.util.Config.settingsVersion()));
                                jsonObject.addProperty("port", java.lang.Integer.valueOf(sPort));
                                jsonObjectEnvelope = envelope(0, "ok", jsonObject);
                            } else if ("GET".equals(request.method) && "/api/sessions".equals(request.path)) {
                                com.google.gson.JsonObject jsonObject2 = new com.google.gson.JsonObject();
                                jsonObject2.add("sessions", fetchSessions());
                                jsonObjectEnvelope = envelope(0, "ok", jsonObject2);
                            } else if ("GET".equals(request.method) && "/api/stats".equals(request.path)) {
                                jsonObjectEnvelope = handleApiStats();
                            } else if ("GET".equals(request.method) && "/api/config".equals(request.path)) {
                                jsonObjectEnvelope = handleApiConfigGet();
                            } else if ("POST".equals(request.method) && "/api/list_files".equals(request.path)) {
                                jsonObjectEnvelope = handleApiListFiles(str2);
                            } else {
                                if ("GET".equals(request.method) && "/mail_html".equals(request.path)) {
                                    if (serveMailHtml(socket.getOutputStream(), parseQuery(request.query))) {
                                        if (socket != null) {
                                            socket.close();
                                            return;
                                        }
                                        return;
                                    }
                                    jsonObjectEnvelope = envelope(404, "邮件网页不存在", null);
                                } else if ("GET".equals(request.method) && request.path != null && request.path.startsWith("/mail_file/")) {
                                    if (serveMailFile(socket.getOutputStream(), request.path)) {
                                        if (socket != null) {
                                            socket.close();
                                            return;
                                        }
                                        return;
                                    }
                                    jsonObjectEnvelope = envelope(404, "文件不存在", null);
                                } else if ("GET".equals(request.method) && "/".equals(request.path)) {
                                    com.google.gson.JsonObject jsonObject3 = new com.google.gson.JsonObject();
                                    jsonObject3.addProperty("service", "AntiRecall HTTP API");
                                    jsonObject3.addProperty("port", java.lang.Integer.valueOf(sPort));
                                    jsonObjectEnvelope = envelope(0, "ok", jsonObject3);
                                } else {
                                    jsonObjectEnvelope = envelope(404, "Not Found", null);
                                }
                                str = "Not Found";
                                i = 404;
                            }
                        }
                    } catch (java.lang.Exception e) {
                        com.tencent.qqlitepro.util.Log.w("HTTP 处理异常: " + e);
                        com.google.gson.JsonObject jsonObject4 = new com.google.gson.JsonObject();
                        jsonObject4.addProperty("error", java.lang.String.valueOf(e));
                        i = 500;
                        jsonObjectEnvelope = envelope(500, java.lang.String.valueOf(e), jsonObject4);
                        str = "Internal Server Error";
                    }
                    writeJson(socket.getOutputStream(), i, str, GSON.toJson((com.google.gson.JsonElement) jsonObjectEnvelope));
                    if (socket != null) {
                        socket.close();
                    }
                } catch (java.lang.Throwable th) {
                    if (socket != null) {
                        try {
                            socket.close();
                        } catch (java.lang.Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    }
                    throw th;
                }
            } catch (java.io.IOException unused) {
                if (socket != null) {
                    socket.close();
                }
            }
        } catch (java.lang.Throwable th3) {
            com.tencent.qqlitepro.util.Log.w("HTTP 连接处理结束: " + th3);
        }
    }

    private static com.google.gson.JsonObject handleGetSessions(java.util.Map<java.lang.String, java.lang.String> map) {
        java.util.ArrayList<java.lang.Object> sessions = com.tencent.qqlitepro.http.QqKernel.getSessions();
        java.lang.String str = map.get("type");
        if (str != null && !str.trim().isEmpty()) {
            java.lang.String lowerCase = str.trim().toLowerCase();
            if ("friends".equals(lowerCase) || "groups".equals(lowerCase)) {
                boolean zEquals = "friends".equals(lowerCase);
                java.util.ArrayList arrayList = new java.util.ArrayList();
                for (java.lang.Object obj : sessions) {
                    if ("1".equals(chatTypeOf(obj)) == zEquals) {
                        arrayList.add(obj);
                    }
                }
                sessions = new java.util.ArrayList<>(arrayList);
            }
        }
        java.lang.String str2 = map.get(com.sun.mail.imap.IMAPStore.ID_NAME);
        if (str2 != null && !str2.trim().isEmpty()) {
            java.lang.String lowerCase2 = str2.trim().toLowerCase();
            java.util.ArrayList arrayList2 = new java.util.ArrayList();
            for (java.lang.Object obj2 : sessions) {
                if (matchesName(obj2, lowerCase2)) {
                    arrayList2.add(obj2);
                }
            }
            sessions = new java.util.ArrayList<>(arrayList2);
        }
        java.lang.String str3 = map.get("num");
        if (str3 != null && !str3.trim().isEmpty()) {
            try {
                int iMax = java.lang.Math.max(0, java.lang.Integer.parseInt(str3.trim()));
                if (iMax < sessions.size()) {
                    sessions = new java.util.ArrayList<>(sessions.subList(0, iMax));
                }
            } catch (java.lang.NumberFormatException unused) {
            }
        }
        com.google.gson.JsonArray jsonArray = new com.google.gson.JsonArray();
        java.util.Iterator<java.lang.Object> it = sessions.iterator();
        while (it.hasNext()) {
            jsonArray.add(GSON.toJsonTree(it.next()));
        }
        com.google.gson.JsonObject jsonObject = new com.google.gson.JsonObject();
        jsonObject.addProperty("total", java.lang.Integer.valueOf(jsonArray.size()));
        jsonObject.add("sessions", jsonArray);
        return envelope(0, "ok", jsonObject);
    }

    private static java.lang.String chatTypeOf(java.lang.Object obj) {
        java.lang.Object obj2;
        return (!(obj instanceof java.util.Map) || (obj2 = ((java.util.Map) obj).get("chatType")) == null) ? "2" : java.lang.String.valueOf(obj2);
    }

    private static com.google.gson.JsonObject handleGetRecentMsgs(java.util.Map<java.lang.String, java.lang.String> map) {
        int iMax;
        java.lang.String str = map.get("num");
        if (str == null || str.trim().isEmpty()) {
            iMax = 0;
        } else {
            try {
                iMax = java.lang.Math.max(0, java.lang.Integer.parseInt(str.trim()));
            } catch (java.lang.NumberFormatException unused) {
                iMax = 0;
            }
        }
        java.util.List<com.tencent.qqlitepro.cache.RawMsgStore.Entry> listRecent = com.tencent.qqlitepro.cache.RawMsgStore.recent(iMax, map.get("peer"));
        com.google.gson.JsonArray jsonArray = new com.google.gson.JsonArray();
        for (com.tencent.qqlitepro.cache.RawMsgStore.Entry entry : listRecent) {
            com.google.gson.JsonObject jsonObject = new com.google.gson.JsonObject();
            jsonObject.addProperty("msgId", entry.msgId);
            jsonObject.addProperty("peerUid", entry.peerUid);
            jsonObject.addProperty("chatType", java.lang.Integer.valueOf(entry.chatType));
            jsonObject.addProperty("time", java.lang.Long.valueOf(entry.time));
            try {
                jsonObject.add("raw", com.google.gson.JsonParser.parseString(entry.rawJson));
            } catch (java.lang.Exception unused2) {
                jsonObject.addProperty("raw", entry.rawJson);
            }
            jsonArray.add(jsonObject);
        }
        com.google.gson.JsonObject jsonObject2 = new com.google.gson.JsonObject();
        jsonObject2.addProperty("total", java.lang.Integer.valueOf(jsonArray.size()));
        jsonObject2.add("msgs", jsonArray);
        return envelope(0, "ok", jsonObject2);
    }

    private static com.google.gson.JsonObject handleGetMsgHistory(java.util.Map<java.lang.String, java.lang.String> map) {
        int iMax;
        long j;
        java.lang.String str = map.get("num");
        if (str == null || str.trim().isEmpty()) {
            iMax = 0;
        } else {
            try {
                iMax = java.lang.Math.max(0, java.lang.Integer.parseInt(str.trim()));
            } catch (java.lang.NumberFormatException unused) {
                iMax = 0;
            }
        }
        java.lang.String str2 = map.get("id");
        java.lang.String strTrim = str2 == null ? "" : str2.trim();
        if (strTrim.isEmpty()) {
            j = -1;
        } else {
            try {
                j = java.lang.Long.parseLong(strTrim);
            } catch (java.lang.NumberFormatException unused2) {
                j = -1;
            }
        }
        com.google.gson.JsonArray jsonArray = new com.google.gson.JsonArray();
        int i = 0;
        for (com.tencent.qqlitepro.cache.RawMsgStore.Entry entry : com.tencent.qqlitepro.cache.RawMsgStore.recent(0, null)) {
            if (strTrim.isEmpty() || matchPeer(entry, strTrim, j)) {
                com.google.gson.JsonObject jsonObject = new com.google.gson.JsonObject();
                jsonObject.addProperty("msgId", entry.msgId);
                jsonObject.addProperty("peerUid", entry.peerUid);
                jsonObject.addProperty("chatType", java.lang.Integer.valueOf(entry.chatType));
                jsonObject.addProperty("time", java.lang.Long.valueOf(entry.time));
                try {
                    jsonObject.add("raw", com.google.gson.JsonParser.parseString(entry.rawJson));
                } catch (java.lang.Exception unused3) {
                    jsonObject.addProperty("raw", entry.rawJson);
                }
                jsonArray.add(jsonObject);
                i++;
                if (iMax > 0 && i >= iMax) {
                    break;
                }
            }
        }
        com.google.gson.JsonObject jsonObject2 = new com.google.gson.JsonObject();
        jsonObject2.addProperty("total", java.lang.Integer.valueOf(jsonArray.size()));
        jsonObject2.add("msgs", jsonArray);
        return envelope(0, "ok", jsonObject2);
    }

    private static boolean matchPeer(com.tencent.qqlitepro.cache.RawMsgStore.Entry entry, java.lang.String str, long j) {
        if (entry.peerUid != null && entry.peerUid.equals(str)) {
            return true;
        }
        try {
            com.google.gson.JsonObject asJsonObject = com.google.gson.JsonParser.parseString(entry.rawJson).getAsJsonObject();
            java.lang.String[] strArr = {"peerUin", "peerUid", "groupCode"};
            for (int i = 0; i < 3; i++) {
                com.google.gson.JsonElement jsonElement = asJsonObject.get(strArr[i]);
                if (jsonElement != null && jsonElement.isJsonPrimitive()) {
                    java.lang.String asString = (jsonElement.getAsJsonPrimitive().isBoolean() || jsonElement.getAsJsonPrimitive().isString()) ? jsonElement.getAsString() : "";
                    if (jsonElement.getAsJsonPrimitive().isNumber()) {
                        try {
                            if (jsonElement.getAsLong() == j) {
                                return true;
                            }
                        } catch (java.lang.Throwable unused) {
                            continue;
                        }
                    } else if (!asString.isEmpty() && asString.equals(str)) {
                        return true;
                    }
                }
            }
        } catch (java.lang.Throwable unused2) {
        }
        return false;
    }

    private static boolean matchesName(java.lang.Object obj, java.lang.String str) {
        if (obj instanceof java.util.Map) {
            java.util.Map map = (java.util.Map) obj;
            java.lang.String[] strArr = {"peerName", "remark", "memberName", "peerUid"};
            for (int i = 0; i < 4; i++) {
                java.lang.Object obj2 = map.get(strArr[i]);
                if (obj2 != null && java.lang.String.valueOf(obj2).toLowerCase().contains(str)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static com.google.gson.JsonObject handleSendMsg(java.lang.String str) {
        try {
            com.google.gson.JsonObject asJsonObject = com.google.gson.JsonParser.parseString(str).getAsJsonObject();
            int asInt = asJsonObject.has("chatType") ? asJsonObject.get("chatType").getAsInt() : 1;
            java.lang.String asString = asJsonObject.has("peerUid") ? asJsonObject.get("peerUid").getAsString() : null;
            java.lang.String asString2 = asJsonObject.has("type") ? asJsonObject.get("type").getAsString() : "text";
            java.lang.String asString3 = asJsonObject.has("content") ? asJsonObject.get("content").getAsString() : null;
            if (asString == null || asString.trim().isEmpty()) {
                return envelope(400, "缺少会话参数：peerUid", null);
            }
            if (asString3 == null || asString3.trim().isEmpty()) {
                return envelope(400, "缺少发送内容：content", null);
            }
            if (asInt != 1 && asInt != 2) {
                return envelope(400, "chatType 仅支持 1(好友) 或 2(群聊)", null);
            }
            try {
                java.lang.Object objCreateContact = com.tencent.qqlitepro.http.QqKernel.createContact(asInt, asString);
                java.util.ArrayList arrayList = new java.util.ArrayList();
                if ("face".equalsIgnoreCase(asString2)) {
                    arrayList.add(com.tencent.qqlitepro.http.QqKernel.createFaceElement(java.lang.Integer.parseInt(asString3.trim())));
                } else {
                    arrayList.add(com.tencent.qqlitepro.http.QqKernel.createTextElement(asString3));
                }
                com.tencent.qqlitepro.http.QqKernel.SendResult sendResultSendMsg = com.tencent.qqlitepro.http.QqKernel.sendMsg(objCreateContact, arrayList, 5000L);
                com.google.gson.JsonObject jsonObject = new com.google.gson.JsonObject();
                jsonObject.addProperty("errCode", java.lang.Integer.valueOf(sendResultSendMsg.code));
                jsonObject.addProperty("errMsg", sendResultSendMsg.msg);
                jsonObject.addProperty("timeout", java.lang.Boolean.valueOf(sendResultSendMsg.timeout));
                jsonObject.addProperty("chatType", java.lang.Integer.valueOf(asInt));
                jsonObject.addProperty("peerUid", asString);
                jsonObject.addProperty("type", asString2);
                jsonObject.addProperty("content", asString3);
                if (sendResultSendMsg.code == 0) {
                    return envelope(0, "发送成功", jsonObject);
                }
                int i = sendResultSendMsg.code;
                java.lang.StringBuilder sb = new java.lang.StringBuilder("发送失败: ");
                sb.append((sendResultSendMsg.msg == null || sendResultSendMsg.msg.isEmpty()) ? "未知错误" : sendResultSendMsg.msg);
                return envelope(i, sb.toString(), jsonObject);
            } catch (java.lang.Throwable th) {
                com.tencent.qqlitepro.util.Log.w("POST /send_msg 异常: " + th);
                com.google.gson.JsonObject jsonObject2 = new com.google.gson.JsonObject();
                jsonObject2.addProperty("error", java.lang.String.valueOf(th));
                return envelope(500, "发送异常: " + th, jsonObject2);
            }
        } catch (java.lang.Exception e) {
            return envelope(400, "非法 JSON 请求体: " + e.getMessage(), null);
        }
    }

    private static com.google.gson.JsonObject handleSendReplyMsg(java.lang.String str) {
        try {
            com.google.gson.JsonObject asJsonObject = com.google.gson.JsonParser.parseString(str).getAsJsonObject();
            int asInt = asJsonObject.has("chatType") ? asJsonObject.get("chatType").getAsInt() : 1;
            java.lang.String asString = asJsonObject.has("peerUid") ? asJsonObject.get("peerUid").getAsString() : null;
            java.lang.String asString2 = asJsonObject.has("replyMsgId") ? asJsonObject.get("replyMsgId").getAsString() : null;
            java.lang.String asString3 = asJsonObject.has("senderUid") ? asJsonObject.get("senderUid").getAsString() : "";
            java.lang.String asString4 = asJsonObject.has("senderNick") ? asJsonObject.get("senderNick").getAsString() : "";
            java.lang.String asString5 = asJsonObject.has("content") ? asJsonObject.get("content").getAsString() : "";
            boolean z = asJsonObject.has("is_at") && asJsonObject.get("is_at").getAsBoolean();
            if (asString == null || asString.trim().isEmpty()) {
                return envelope(400, "缺少会话参数：peerUid", null);
            }
            if (asString2 == null || asString2.trim().isEmpty()) {
                return envelope(400, "缺少被引用消息参数：replyMsgId", null);
            }
            if (asInt != 1 && asInt != 2) {
                return envelope(400, "chatType 仅支持 1(好友) 或 2(群聊)", null);
            }
            try {
                com.tencent.qqlitepro.http.QqKernel.SendResult sendResultSendReplyMsg = com.tencent.qqlitepro.http.QqKernel.sendReplyMsg(com.tencent.qqlitepro.http.QqKernel.createContact(asInt, asString), com.tencent.qqlitepro.http.QqKernel.buildElementsWithAt(asString5, z, z ? asString3 : null, asString4), asString2, asString3, asString4, 8000L);
                com.google.gson.JsonObject jsonObject = new com.google.gson.JsonObject();
                jsonObject.addProperty("errCode", java.lang.Integer.valueOf(sendResultSendReplyMsg.code));
                jsonObject.addProperty("errMsg", sendResultSendReplyMsg.msg);
                jsonObject.addProperty("timeout", java.lang.Boolean.valueOf(sendResultSendReplyMsg.timeout));
                jsonObject.addProperty("chatType", java.lang.Integer.valueOf(asInt));
                jsonObject.addProperty("peerUid", asString);
                jsonObject.addProperty("replyMsgId", asString2);
                jsonObject.addProperty("is_at", java.lang.Boolean.valueOf(z));
                if (sendResultSendReplyMsg.code == 0) {
                    return envelope(0, "发送回复消息成功", jsonObject);
                }
                int i = sendResultSendReplyMsg.code;
                java.lang.StringBuilder sb = new java.lang.StringBuilder("发送回复消息失败: ");
                sb.append((sendResultSendReplyMsg.msg == null || sendResultSendReplyMsg.msg.isEmpty()) ? "未知错误" : sendResultSendReplyMsg.msg);
                return envelope(i, sb.toString(), jsonObject);
            } catch (java.lang.Throwable th) {
                com.tencent.qqlitepro.util.Log.w("POST /send_reply_msg 异常: " + th);
                com.google.gson.JsonObject jsonObject2 = new com.google.gson.JsonObject();
                jsonObject2.addProperty("error", java.lang.String.valueOf(th));
                return envelope(500, "发送回复消息异常: " + th, jsonObject2);
            }
        } catch (java.lang.Exception e) {
            return envelope(400, "非法 JSON 请求体: " + e.getMessage(), null);
        }
    }

    private static com.google.gson.JsonObject handleSendImage(java.lang.String str) {
        try {
            com.google.gson.JsonObject asJsonObject = com.google.gson.JsonParser.parseString(str).getAsJsonObject();
            int asInt = asJsonObject.has("chatType") ? asJsonObject.get("chatType").getAsInt() : 1;
            java.lang.String asString = asJsonObject.has("peerUid") ? asJsonObject.get("peerUid").getAsString() : null;
            java.lang.String asString2 = asJsonObject.has("path") ? asJsonObject.get("path").getAsString() : null;
            if (asString == null || asString.trim().isEmpty()) {
                return envelope(400, "缺少会话参数：peerUid", null);
            }
            if (asString2 == null || asString2.trim().isEmpty()) {
                return envelope(400, "缺少图片路径：path", null);
            }
            try {
                java.lang.String strResolveLocal = resolveLocal(asString2);
                if (strResolveLocal == null) {
                    return envelope(400, "图片文件不存在或下载失败", null);
                }
                java.lang.Object objCreateContact = com.tencent.qqlitepro.http.QqKernel.createContact(asInt, asString);
                java.util.ArrayList arrayList = new java.util.ArrayList();
                arrayList.add(com.tencent.qqlitepro.http.QqKernel.createPicElement(strResolveLocal));
                com.tencent.qqlitepro.http.QqKernel.SendResult sendResultSendMsg = com.tencent.qqlitepro.http.QqKernel.sendMsg(objCreateContact, arrayList, 15000L);
                com.google.gson.JsonObject jsonObject = new com.google.gson.JsonObject();
                jsonObject.addProperty("errCode", java.lang.Integer.valueOf(sendResultSendMsg.code));
                jsonObject.addProperty("errMsg", sendResultSendMsg.msg);
                jsonObject.addProperty("peerUid", asString);
                if (sendResultSendMsg.code == 0) {
                    return envelope(0, "发送图片成功", jsonObject);
                }
                int i = sendResultSendMsg.code;
                java.lang.StringBuilder sb = new java.lang.StringBuilder("发送图片失败: ");
                sb.append(sendResultSendMsg.msg == null ? "" : sendResultSendMsg.msg);
                return envelope(i, sb.toString(), jsonObject);
            } catch (java.lang.Throwable th) {
                com.google.gson.JsonObject jsonObject2 = new com.google.gson.JsonObject();
                jsonObject2.addProperty("error", java.lang.String.valueOf(th));
                return envelope(500, "发送图片异常: " + th, jsonObject2);
            }
        } catch (java.lang.Exception e) {
            return envelope(400, "非法 JSON 请求体: " + e.getMessage(), null);
        }
    }

    private static com.google.gson.JsonObject handleSendVideo(java.lang.String str) {
        try {
            com.google.gson.JsonObject asJsonObject = com.google.gson.JsonParser.parseString(str).getAsJsonObject();
            int asInt = asJsonObject.has("chatType") ? asJsonObject.get("chatType").getAsInt() : 1;
            java.lang.String asString = asJsonObject.has("peerUid") ? asJsonObject.get("peerUid").getAsString() : null;
            java.lang.String asString2 = asJsonObject.has("path") ? asJsonObject.get("path").getAsString() : null;
            if (asString == null || asString.trim().isEmpty()) {
                return envelope(400, "缺少会话参数：peerUid", null);
            }
            if (asString2 == null || asString2.trim().isEmpty()) {
                return envelope(400, "缺少视频路径：path", null);
            }
            try {
                java.lang.String strResolveLocal = resolveLocal(asString2);
                if (strResolveLocal == null) {
                    return envelope(400, "视频文件不存在或下载失败", null);
                }
                java.lang.Object objCreateContact = com.tencent.qqlitepro.http.QqKernel.createContact(asInt, asString);
                java.util.ArrayList arrayList = new java.util.ArrayList();
                arrayList.add(com.tencent.qqlitepro.http.QqKernel.createVideoElement(strResolveLocal));
                com.tencent.qqlitepro.http.QqKernel.SendResult sendResultSendMsg = com.tencent.qqlitepro.http.QqKernel.sendMsg(objCreateContact, arrayList, 20000L);
                com.google.gson.JsonObject jsonObject = new com.google.gson.JsonObject();
                jsonObject.addProperty("errCode", java.lang.Integer.valueOf(sendResultSendMsg.code));
                jsonObject.addProperty("errMsg", sendResultSendMsg.msg);
                jsonObject.addProperty("peerUid", asString);
                if (sendResultSendMsg.code == 0) {
                    return envelope(0, "发送视频成功", jsonObject);
                }
                int i = sendResultSendMsg.code;
                java.lang.StringBuilder sb = new java.lang.StringBuilder("发送视频失败: ");
                sb.append(sendResultSendMsg.msg == null ? "" : sendResultSendMsg.msg);
                return envelope(i, sb.toString(), jsonObject);
            } catch (java.lang.Throwable th) {
                com.google.gson.JsonObject jsonObject2 = new com.google.gson.JsonObject();
                jsonObject2.addProperty("error", java.lang.String.valueOf(th));
                return envelope(500, "发送视频异常: " + th, jsonObject2);
            }
        } catch (java.lang.Exception e) {
            return envelope(400, "非法 JSON 请求体: " + e.getMessage(), null);
        }
    }

    private static com.google.gson.JsonObject handleSetWhitelist(java.lang.String str) {
        try {
            com.google.gson.JsonObject asJsonObject = com.google.gson.JsonParser.parseString(str).getAsJsonObject();
            java.lang.String asString = asJsonObject.has("type") ? asJsonObject.get("type").getAsString() : null;
            java.lang.String asString2 = asJsonObject.has("peers") ? asJsonObject.get("peers").getAsString() : "";
            if (asString == null || asString.trim().isEmpty()) {
                return envelope(400, "缺少参数：type(keyword|auto|qzone)", null);
            }
            java.lang.String lowerCase = asString.trim().toLowerCase();
            try {
                android.content.Context contextAppContext = com.tencent.qqlitepro.cache.MsgCache.appContext();
                if (contextAppContext == null) {
                    return envelope(500, "模块 Context 未就绪", null);
                }
                if ("keyword".equals(lowerCase)) {
                    com.tencent.qqlitepro.util.Config.setKeywordRules(contextAppContext, com.tencent.qqlitepro.util.Config.keywordReplyEnabled(contextAppContext), com.tencent.qqlitepro.util.Config.keywordRules(contextAppContext), asString2);
                } else if ("auto".equals(lowerCase)) {
                    com.tencent.qqlitepro.util.Config.setAutoReply(contextAppContext, com.tencent.qqlitepro.util.Config.autoReplyEnabled(contextAppContext), com.tencent.qqlitepro.util.Config.autoReplyContent(contextAppContext), asString2, com.tencent.qqlitepro.util.Config.autoReplyAt(contextAppContext));
                } else if ("qzone".equals(lowerCase)) {
                    com.tencent.qqlitepro.util.Config.setQqDynamicBlacklist(contextAppContext, asString2);
                } else {
                    return envelope(400, "type 仅支持 keyword|auto|qzone", null);
                }
                com.google.gson.JsonObject jsonObject = new com.google.gson.JsonObject();
                jsonObject.addProperty("type", lowerCase);
                jsonObject.addProperty("peers", asString2);
                return envelope(0, "白名单已保存", jsonObject);
            } catch (java.lang.Throwable th) {
                com.google.gson.JsonObject jsonObject2 = new com.google.gson.JsonObject();
                jsonObject2.addProperty("error", java.lang.String.valueOf(th));
                return envelope(500, "保存白名单异常: " + th, jsonObject2);
            }
        } catch (java.lang.Exception e) {
            return envelope(400, "非法 JSON 请求体: " + e.getMessage(), null);
        }
    }

    private static java.lang.String resolveLocal(java.lang.String str) {
        if (str == null || str.trim().isEmpty()) {
            return null;
        }
        java.lang.String strTrim = str.trim();
        if (strTrim.startsWith("http://") || strTrim.startsWith("https://")) {
            return com.tencent.qqlitepro.util.Net.download(com.tencent.qqlitepro.cache.MsgCache.appContext(), strTrim, null);
        }
        if (new java.io.File(strTrim).exists()) {
            return strTrim;
        }
        return null;
    }

    private static com.google.gson.JsonObject handleBanAllStatus(java.lang.String str) {
        try {
            com.google.gson.JsonObject asJsonObject = com.google.gson.JsonParser.parseString(str).getAsJsonObject();
            java.lang.String asString = asJsonObject.has("groupId") ? asJsonObject.get("groupId").getAsString() : null;
            java.lang.String asString2 = asJsonObject.has("type") ? asJsonObject.get("type").getAsString() : null;
            if (asString == null || asString.trim().isEmpty()) {
                return envelope(400, "缺少必填参数：groupId", null);
            }
            if (asString2 == null || asString2.trim().isEmpty()) {
                return envelope(400, "缺少必填参数：type(status|open|close)", null);
            }
            java.lang.String lowerCase = asString2.trim().toLowerCase();
            if (!"status".equals(lowerCase) && !"open".equals(lowerCase) && !"close".equals(lowerCase)) {
                return envelope(400, "type 仅支持 status|open|close", null);
            }
            com.google.gson.JsonObject jsonObject = new com.google.gson.JsonObject();
            jsonObject.addProperty("groupId", asString);
            jsonObject.addProperty("type", lowerCase);
            try {
                if ("status".equals(lowerCase)) {
                    com.tencent.qqlitepro.http.QqKernel.BanResult banResultBanAllStatus = com.tencent.qqlitepro.http.QqKernel.banAllStatus(asString);
                    jsonObject.addProperty("code", java.lang.Integer.valueOf(banResultBanAllStatus.code));
                    jsonObject.addProperty("muted", java.lang.Boolean.valueOf(banResultBanAllStatus.muted));
                    jsonObject.addProperty("shutUpAllTimestamp", java.lang.Long.valueOf(banResultBanAllStatus.shutUpAllTimestamp));
                    jsonObject.addProperty("groupCode", banResultBanAllStatus.groupCode);
                    jsonObject.addProperty("errMsg", banResultBanAllStatus.msg);
                    com.google.gson.JsonElement raw = parseRaw(banResultBanAllStatus.rawJson);
                    if (raw != null) {
                        jsonObject.add("raw", raw);
                    } else {
                        jsonObject.addProperty("raw", "");
                    }
                    return envelope(banResultBanAllStatus.code, banResultBanAllStatus.msg, jsonObject);
                }
                boolean zEquals = "open".equals(lowerCase);
                com.tencent.qqlitepro.http.QqKernel.BanResult groupShutUp = com.tencent.qqlitepro.http.QqKernel.setGroupShutUp(asString, zEquals, 5000L);
                jsonObject.addProperty("code", java.lang.Integer.valueOf(groupShutUp.code));
                jsonObject.addProperty("muted", java.lang.Boolean.valueOf(zEquals));
                jsonObject.addProperty("groupCode", groupShutUp.groupCode);
                jsonObject.addProperty("errMsg", groupShutUp.msg);
                jsonObject.addProperty("timeout", java.lang.Boolean.valueOf(groupShutUp.timeout));
                com.google.gson.JsonElement raw2 = parseRaw(groupShutUp.rawJson);
                if (raw2 != null) {
                    jsonObject.add("raw", raw2);
                } else {
                    jsonObject.addProperty("raw", "");
                }
                if (groupShutUp.code == 0) {
                    return envelope(0, zEquals ? "已开启本群全体禁言" : "已关闭本群全体禁言", jsonObject);
                }
                return envelope(groupShutUp.code, (groupShutUp.msg == null || groupShutUp.msg.isEmpty()) ? "操作失败" : groupShutUp.msg, jsonObject);
            } catch (java.lang.Throwable th) {
                com.tencent.qqlitepro.util.Log.w("POST /ban_all_status 异常: " + th);
                com.google.gson.JsonObject jsonObject2 = new com.google.gson.JsonObject();
                jsonObject2.addProperty("error", java.lang.String.valueOf(th));
                return envelope(500, "操作异常: " + th, jsonObject2);
            }
        } catch (java.lang.Exception e) {
            return envelope(400, "非法 JSON 请求体: " + e.getMessage(), null);
        }
    }

    private static com.google.gson.JsonObject handleGetGroupNotices(java.lang.String str) {
        try {
            com.google.gson.JsonObject asJsonObject = com.google.gson.JsonParser.parseString(str).getAsJsonObject();
            java.lang.String asString = asJsonObject.has("groupId") ? asJsonObject.get("groupId").getAsString() : null;
            if (asString == null || asString.trim().isEmpty()) {
                java.lang.String asString2 = asJsonObject.has(com.sun.mail.imap.IMAPStore.ID_NAME) ? asJsonObject.get(com.sun.mail.imap.IMAPStore.ID_NAME).getAsString() : null;
                if (asString2 == null || asString2.trim().isEmpty()) {
                    return envelope(400, "缺少参数：groupId（群号/群 peerUid）或 name（群名）", null);
                }
                asString = resolveGroupIdByName(asString2.trim());
                if (asString == null) {
                    return envelope(404, "未找到指定名称的群聊", null);
                }
            }
            java.lang.String strTrim = asString.trim();
            try {
                java.lang.String strFetchGroupNotices = com.tencent.qqlitepro.http.QqKernel.fetchGroupNotices(strTrim);
                if (strFetchGroupNotices == null) {
                    return envelope(500, "拉取群公告失败或超时（内核未就绪/无权限）", null);
                }
                com.google.gson.JsonElement string = com.google.gson.JsonParser.parseString(strFetchGroupNotices);
                com.google.gson.JsonArray asJsonArray = string.isJsonArray() ? string.getAsJsonArray() : new com.google.gson.JsonArray();
                com.google.gson.JsonObject jsonObject = new com.google.gson.JsonObject();
                jsonObject.addProperty("groupId", strTrim);
                jsonObject.addProperty("total", java.lang.Integer.valueOf(asJsonArray.size()));
                jsonObject.add("notices", asJsonArray);
                return envelope(0, "ok", jsonObject);
            } catch (java.lang.Throwable th) {
                com.tencent.qqlitepro.util.Log.w("POST /get_group_notices 异常: " + th);
                com.google.gson.JsonObject jsonObject2 = new com.google.gson.JsonObject();
                jsonObject2.addProperty("error", java.lang.String.valueOf(th));
                return envelope(500, "操作异常: " + th, jsonObject2);
            }
        } catch (java.lang.Exception e) {
            return envelope(400, "非法 JSON 请求体: " + e.getMessage(), null);
        }
    }

    private static java.lang.String resolveGroupIdByName(java.lang.String str) {
        try {
            for (java.lang.Object obj : com.tencent.qqlitepro.http.QqKernel.getSessions()) {
                if (obj != null && "2".equals(chatTypeOf(obj)) && (obj instanceof java.util.Map)) {
                    java.util.Map map = (java.util.Map) obj;
                    java.lang.Object obj2 = map.get("peerUid");
                    java.lang.Object obj3 = map.get("peerName");
                    java.lang.Object obj4 = map.get("peerUin");
                    if (obj2 != null && obj3 != null && java.lang.String.valueOf(obj3).equalsIgnoreCase(str)) {
                        return java.lang.String.valueOf(obj2);
                    }
                    if (obj2 != null && obj4 != null && java.lang.String.valueOf(obj4).equals(str)) {
                        return java.lang.String.valueOf(obj2);
                    }
                }
            }
            if (str.matches("\\d+")) {
                return str;
            }
            return null;
        } catch (java.lang.Throwable unused) {
            return null;
        }
    }

    private static int optInt(com.google.gson.JsonObject jsonObject, java.lang.String str, int i) {
        try {
            return (!jsonObject.has(str) || jsonObject.get(str).isJsonNull()) ? i : jsonObject.get(str).getAsInt();
        } catch (java.lang.Throwable unused) {
            return i;
        }
    }

    private static java.lang.String optStr(com.google.gson.JsonObject jsonObject, java.lang.String str, java.lang.String str2) {
        try {
            return (!jsonObject.has(str) || jsonObject.get(str).isJsonNull()) ? str2 : jsonObject.get(str).getAsString();
        } catch (java.lang.Throwable unused) {
            return str2;
        }
    }

    private static int optInt(java.util.Map<java.lang.String, java.lang.String> map, java.lang.String str, int i) {
        try {
            java.lang.String str2 = map.get(str);
            if (str2 != null && !str2.trim().isEmpty()) {
                return java.lang.Integer.parseInt(str2.trim());
            }
            return i;
        } catch (java.lang.Throwable unused) {
            return i;
        }
    }

    private static com.google.gson.JsonObject handleGetFriendRequests(java.util.Map<java.lang.String, java.lang.String> map) {
        try {
            com.google.gson.JsonObject friendRequests = com.tencent.qqlitepro.http.QqKernel.getFriendRequests(optInt(map, "timeout", 5000));
            if (friendRequests == null) {
                return envelope(500, "拉取好友申请失败或超时（内核未就绪/无数据）", null);
            }
            return envelope(0, "ok", friendRequests);
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("GET /get_friend_requests 异常: " + th);
            return envelope(500, "操作异常: " + th, null);
        }
    }

    private static com.google.gson.JsonObject handleSendFriendRequest(java.lang.String str) {
        try {
            com.google.gson.JsonObject asJsonObject = com.google.gson.JsonParser.parseString(str).getAsJsonObject();
            java.lang.String strOptStr = optStr(asJsonObject, "uin", "");
            java.lang.String strOptStr2 = optStr(asJsonObject, "uid", "");
            if (strOptStr.trim().isEmpty() && strOptStr2.trim().isEmpty()) {
                return envelope(400, "缺少参数：uin 或 uid", null);
            }
            try {
                com.tencent.qqlitepro.http.QqKernel.ApplyResult applyResultSendFriendRequest = com.tencent.qqlitepro.http.QqKernel.sendFriendRequest(strOptStr, strOptStr2, optStr(asJsonObject, "phone", ""), optInt(asJsonObject, "setting", -1), optStr(asJsonObject, "answer", ""), optStr(asJsonObject, "remark", ""), optInt(asJsonObject, "sourceId", 0), optInt(asJsonObject, "sourceSubId", 0), optInt(asJsonObject, "timeout", 5000));
                com.google.gson.JsonObject jsonObject = new com.google.gson.JsonObject();
                jsonObject.addProperty("code", java.lang.Integer.valueOf(applyResultSendFriendRequest.code));
                jsonObject.addProperty("msg", applyResultSendFriendRequest.msg);
                jsonObject.addProperty("timeout", java.lang.Boolean.valueOf(applyResultSendFriendRequest.timeout));
                return envelope(applyResultSendFriendRequest.code, applyResultSendFriendRequest.code == 0 ? "ok" : applyResultSendFriendRequest.msg, jsonObject);
            } catch (java.lang.Throwable th) {
                com.tencent.qqlitepro.util.Log.w("POST /send_friend_request 异常: " + th);
                return envelope(500, "操作异常: " + th, null);
            }
        } catch (java.lang.Exception e) {
            return envelope(400, "非法 JSON 请求体: " + e.getMessage(), null);
        }
    }

    private static com.google.gson.JsonObject handleHandleFriendRequest(java.lang.String str) {
        boolean asBoolean;
        try {
            com.google.gson.JsonObject asJsonObject = com.google.gson.JsonParser.parseString(str).getAsJsonObject();
            java.lang.String strOptStr = optStr(asJsonObject, "uid", "");
            if (strOptStr.trim().isEmpty()) {
                return envelope(400, "缺少参数：uid（申请人 uid）", null);
            }
            if (asJsonObject.has("accept")) {
                asBoolean = asJsonObject.get("accept").getAsBoolean();
            } else {
                java.lang.String strTrim = optStr(asJsonObject, "operate", "agree").trim();
                asBoolean = ("refuse".equalsIgnoreCase(strTrim) || "reject".equalsIgnoreCase(strTrim) || "false".equalsIgnoreCase(strTrim)) ? false : true;
            }
            try {
                com.tencent.qqlitepro.http.QqKernel.ApplyResult applyResultApproveFriendRequest = com.tencent.qqlitepro.http.QqKernel.approveFriendRequest(strOptStr, asBoolean, optStr(asJsonObject, "refuseMsg", ""), optInt(asJsonObject, "reqTime", 0), optInt(asJsonObject, "timeout", 5000));
                com.google.gson.JsonObject jsonObject = new com.google.gson.JsonObject();
                jsonObject.addProperty("code", java.lang.Integer.valueOf(applyResultApproveFriendRequest.code));
                jsonObject.addProperty("msg", applyResultApproveFriendRequest.msg);
                jsonObject.addProperty("timeout", java.lang.Boolean.valueOf(applyResultApproveFriendRequest.timeout));
                return envelope(applyResultApproveFriendRequest.code, applyResultApproveFriendRequest.code == 0 ? "ok" : applyResultApproveFriendRequest.msg, jsonObject);
            } catch (java.lang.Throwable th) {
                com.tencent.qqlitepro.util.Log.w("POST /handle_friend_request 异常: " + th);
                return envelope(500, "操作异常: " + th, null);
            }
        } catch (java.lang.Exception e) {
            return envelope(400, "非法 JSON 请求体: " + e.getMessage(), null);
        }
    }

    private static com.google.gson.JsonObject handleGetGroupRequests(java.util.Map<java.lang.String, java.lang.String> map) {
        boolean z = "1".equals(map.get("doubt")) || "true".equalsIgnoreCase(map.get("doubt"));
        long j = 0;
        try {
            java.lang.String str = map.get("startSeq");
            if (str != null && !str.trim().isEmpty()) {
                j = java.lang.Long.parseLong(str.trim());
            }
        } catch (java.lang.Throwable unused) {
        }
        try {
            com.google.gson.JsonObject groupRequests = com.tencent.qqlitepro.http.QqKernel.getGroupRequests(z, j, optInt(map, "number", 14), optInt(map, "timeout", 5000));
            if (groupRequests == null) {
                return envelope(500, "拉取加群申请失败或超时（内核未就绪/无数据）", null);
            }
            return envelope(0, "ok", groupRequests);
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("GET /get_group_requests 异常: " + th);
            return envelope(500, "操作异常: " + th, null);
        }
    }

    private static com.google.gson.JsonObject handleSendGroupRequest(java.lang.String str) {
        try {
            com.google.gson.JsonObject asJsonObject = com.google.gson.JsonParser.parseString(str).getAsJsonObject();
            java.lang.String strOptStr = optStr(asJsonObject, "groupId", "");
            if (strOptStr.trim().isEmpty()) {
                return envelope(400, "缺少参数：groupId（群号/群 peerUid）", null);
            }
            try {
                com.tencent.qqlitepro.http.QqKernel.ApplyResult applyResultSendGroupRequest = com.tencent.qqlitepro.http.QqKernel.sendGroupRequest(strOptStr, optStr(asJsonObject, "applyMsg", ""), optInt(asJsonObject, "sourceId", 0), optInt(asJsonObject, "sourceSubId", 0), optInt(asJsonObject, "timeout", 5000));
                com.google.gson.JsonObject jsonObject = new com.google.gson.JsonObject();
                jsonObject.addProperty("code", java.lang.Integer.valueOf(applyResultSendGroupRequest.code));
                jsonObject.addProperty("msg", applyResultSendGroupRequest.msg);
                jsonObject.addProperty("timeout", java.lang.Boolean.valueOf(applyResultSendGroupRequest.timeout));
                return envelope(applyResultSendGroupRequest.code, applyResultSendGroupRequest.code == 0 ? "ok" : applyResultSendGroupRequest.msg, jsonObject);
            } catch (java.lang.Throwable th) {
                com.tencent.qqlitepro.util.Log.w("POST /send_group_request 异常: " + th);
                return envelope(500, "操作异常: " + th, null);
            }
        } catch (java.lang.Exception e) {
            return envelope(400, "非法 JSON 请求体: " + e.getMessage(), null);
        }
    }

    private static com.google.gson.JsonObject handleHandleGroupRequest(java.lang.String str) {
        try {
            com.google.gson.JsonObject asJsonObject = com.google.gson.JsonParser.parseString(str).getAsJsonObject();
            java.lang.String strOptStr = optStr(asJsonObject, "groupId", "");
            if (strOptStr.trim().isEmpty()) {
                return envelope(400, "缺少参数：groupId（群号/群 peerUid）", null);
            }
            java.lang.String strOptStr2 = optStr(asJsonObject, "operate", "");
            if (strOptStr2.trim().isEmpty()) {
                strOptStr2 = asJsonObject.has("accept") ? java.lang.String.valueOf(asJsonObject.get("accept").getAsBoolean()) : "agree";
            }
            try {
                com.tencent.qqlitepro.http.QqKernel.ApplyResult applyResultOperateGroupRequest = com.tencent.qqlitepro.http.QqKernel.operateGroupRequest(strOptStr, optInt(asJsonObject, "seq", 0), optStr(asJsonObject, "type", ""), strOptStr2, optStr(asJsonObject, "postscript", ""), optInt(asJsonObject, "timeout", 5000));
                com.google.gson.JsonObject jsonObject = new com.google.gson.JsonObject();
                jsonObject.addProperty("code", java.lang.Integer.valueOf(applyResultOperateGroupRequest.code));
                jsonObject.addProperty("msg", applyResultOperateGroupRequest.msg);
                jsonObject.addProperty("timeout", java.lang.Boolean.valueOf(applyResultOperateGroupRequest.timeout));
                return envelope(applyResultOperateGroupRequest.code, applyResultOperateGroupRequest.code == 0 ? "ok" : applyResultOperateGroupRequest.msg, jsonObject);
            } catch (java.lang.Throwable th) {
                com.tencent.qqlitepro.util.Log.w("POST /handle_group_request 异常: " + th);
                return envelope(500, "操作异常: " + th, null);
            }
        } catch (java.lang.Exception e) {
            return envelope(400, "非法 JSON 请求体: " + e.getMessage(), null);
        }
    }

    private static com.google.gson.JsonElement parseRaw(java.lang.String str) {
        if (str != null && !str.isEmpty() && !"null".equals(str)) {
            try {
                return com.google.gson.JsonParser.parseString(str);
            } catch (java.lang.Exception unused) {
            }
        }
        return null;
    }

    private static com.google.gson.JsonObject handleApiConfigGet() {
        android.content.Context contextAppContext = com.tencent.qqlitepro.cache.MsgCache.appContext();
        if (contextAppContext == null) {
            return envelope(500, "模块 Context 未就绪", null);
        }
        com.tencent.qqlitepro.util.Config.reloadForce(contextAppContext);
        com.google.gson.JsonObject jsonObject = new com.google.gson.JsonObject();
        jsonObject.add("config", com.tencent.qqlitepro.util.Config.exportAll());
        jsonObject.addProperty(com.sun.mail.imap.IMAPStore.ID_VERSION, java.lang.Long.valueOf(com.tencent.qqlitepro.util.Config.settingsVersion()));
        jsonObject.addProperty("port", java.lang.Integer.valueOf(sPort));
        jsonObject.add("sessions", fetchSessions());
        return envelope(0, "ok", jsonObject);
    }

    private static com.google.gson.JsonObject handleApiConfigSet(java.lang.String str) {
        try {
            com.google.gson.JsonElement string = com.google.gson.JsonParser.parseString(str);
            if (string == null || !string.isJsonObject()) {
                return envelope(400, "请求体必须是 JSON 对象", null);
            }
            android.content.Context contextAppContext = com.tencent.qqlitepro.cache.MsgCache.appContext();
            if (contextAppContext == null) {
                return envelope(500, "模块 Context 未就绪", null);
            }
            try {
                if (string.getAsJsonObject().has("config")) {
                    string = string.getAsJsonObject().get("config");
                }
                com.tencent.qqlitepro.util.Config.importAll(contextAppContext, string.getAsJsonObject());
                com.google.gson.JsonObject jsonObject = new com.google.gson.JsonObject();
                jsonObject.addProperty(com.sun.mail.imap.IMAPStore.ID_VERSION, java.lang.Long.valueOf(com.tencent.qqlitepro.util.Config.settingsVersion()));
                int iPort = com.tencent.qqlitepro.util.Config.port(contextAppContext);
                if (iPort >= 1024 && iPort <= 65535 && iPort != sPort) {
                    sPort = iPort;
                    closeServer();
                    startAccept();
                }
                return envelope(0, "设置已保存并同步", jsonObject);
            } catch (java.lang.Throwable th) {
                com.tencent.qqlitepro.util.Log.w("POST /api/config 异常: " + th);
                return envelope(500, "保存失败: " + th, null);
            }
        } catch (java.lang.Exception e) {
            return envelope(400, "非法 JSON: " + e.getMessage(), null);
        }
    }

    private static com.google.gson.JsonArray fetchSessions() {
        com.google.gson.JsonArray jsonArray = new com.google.gson.JsonArray();
        try {
            for (java.lang.Object obj : com.tencent.qqlitepro.http.QqKernel.getSessions()) {
                if (obj instanceof java.util.Map) {
                    java.util.Map map = (java.util.Map) obj;
                    com.google.gson.JsonObject jsonObject = new com.google.gson.JsonObject();
                    jsonObject.addProperty("chatType", chatTypeOf(obj));
                    java.lang.String[] strArr = {"peerUid", "peerUin", "peerName", "remark", "memberName", "groupCode"};
                    for (int i = 0; i < 6; i++) {
                        java.lang.String str = strArr[i];
                        java.lang.Object obj2 = map.get(str);
                        if (obj2 != null) {
                            jsonObject.addProperty(str, java.lang.String.valueOf(obj2));
                        }
                    }
                    jsonArray.add(jsonObject);
                }
            }
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("fetchSessions 异常: " + th);
        }
        return jsonArray;
    }

    private static com.google.gson.JsonObject handleApiSendFile(java.lang.String str) {
        java.lang.String str2;
        try {
            com.google.gson.JsonObject asJsonObject = com.google.gson.JsonParser.parseString(str).getAsJsonObject();
            boolean z = true;
            int asInt = asJsonObject.has("chatType") ? asJsonObject.get("chatType").getAsInt() : 1;
            java.lang.String asString = asJsonObject.has("peerUid") ? asJsonObject.get("peerUid").getAsString() : null;
            java.lang.String asString2 = asJsonObject.has("path") ? asJsonObject.get("path").getAsString() : null;
            if (asInt != 1 && asInt != 2) {
                return envelope(400, "chatType 仅支持 1(好友) 或 2(群聊)", null);
            }
            if (asString == null || asString.trim().isEmpty()) {
                return envelope(400, "缺少会话参数：peerUid", null);
            }
            if (asString2 == null || asString2.trim().isEmpty()) {
                return envelope(400, "缺少文件参数：path", null);
            }
            try {
                java.lang.String strResolveLocal = resolveLocal(asString2.trim());
                if (strResolveLocal == null) {
                    return envelope(400, "文件不存在或 URL 下载失败", null);
                }
                android.content.Context contextAppContext = com.tencent.qqlitepro.cache.MsgCache.appContext();
                if (com.tencent.qqlitepro.util.Config.sendMode(contextAppContext) != 0) {
                    z = false;
                }
                boolean zShardEnabled = com.tencent.qqlitepro.util.Config.shardEnabled(contextAppContext);
                java.lang.Object objCreateContact = com.tencent.qqlitepro.http.QqKernel.createContact(asInt, asString);
                java.util.ArrayList arrayList = new java.util.ArrayList();
                if (z) {
                    java.util.List<com.tencent.qqlitepro.http.FakeImage.Wrapped> listPrepareFakeImages = com.tencent.qqlitepro.http.FakeImage.prepareFakeImages(strResolveLocal, zShardEnabled);
                    if (listPrepareFakeImages != null && !listPrepareFakeImages.isEmpty()) {
                        java.util.Iterator<com.tencent.qqlitepro.http.FakeImage.Wrapped> it = listPrepareFakeImages.iterator();
                        while (it.hasNext()) {
                            java.lang.Object objCreatePicElement = com.tencent.qqlitepro.http.QqKernel.createPicElement(it.next().pngFile.getAbsolutePath());
                            com.tencent.qqlitepro.http.QqKernel.setPicDisplaySize(objCreatePicElement, 416, 468);
                            arrayList.add(objCreatePicElement);
                        }
                    }
                    if (arrayList.isEmpty()) {
                        return envelope(400, "伪图片封装失败", null);
                    }
                }
                java.util.List<com.tencent.qqlitepro.http.FakeVoice.Wrapped> listPrepareFakeVoices = com.tencent.qqlitepro.http.FakeVoice.prepareFakeVoices(strResolveLocal, zShardEnabled);
                if (listPrepareFakeVoices != null && !listPrepareFakeVoices.isEmpty()) {
                    java.util.Iterator<com.tencent.qqlitepro.http.FakeVoice.Wrapped> it2 = listPrepareFakeVoices.iterator();
                    while (it2.hasNext()) {
                        arrayList.add(com.tencent.qqlitepro.http.QqKernel.createPttElement(it2.next().amrFile.getAbsolutePath()));
                    }
                }
                if (arrayList.isEmpty()) {
                    return envelope(400, "伪语音封装失败", null);
                }
                com.tencent.qqlitepro.http.QqKernel.SendResult sendResultSendMsg = com.tencent.qqlitepro.http.QqKernel.sendMsg(objCreateContact, arrayList, 120000L);
                com.google.gson.JsonObject jsonObject = new com.google.gson.JsonObject();
                jsonObject.addProperty("errCode", java.lang.Integer.valueOf(sendResultSendMsg == null ? -1 : sendResultSendMsg.code));
                if (sendResultSendMsg == null) {
                    str2 = "无响应";
                } else {
                    str2 = sendResultSendMsg.msg == null ? "" : sendResultSendMsg.msg;
                }
                jsonObject.addProperty("errMsg", str2);
                jsonObject.addProperty("chatType", java.lang.Integer.valueOf(asInt));
                jsonObject.addProperty("peerUid", asString);
                jsonObject.addProperty("path", strResolveLocal);
                if (sendResultSendMsg == null || sendResultSendMsg.code != 0) {
                    return envelope(sendResultSendMsg == null ? 500 : sendResultSendMsg.code, "文件发送失败", jsonObject);
                }
                return envelope(0, "文件发送成功", jsonObject);
            } catch (java.lang.Throwable th) {
                com.tencent.qqlitepro.util.Log.w("POST /api/send_file 异常: " + th);
                com.google.gson.JsonObject jsonObject2 = new com.google.gson.JsonObject();
                jsonObject2.addProperty("error", java.lang.String.valueOf(th));
                return envelope(500, "发送文件异常: " + th, jsonObject2);
            }
        } catch (java.lang.Exception e) {
            return envelope(400, "非法 JSON: " + e.getMessage(), null);
        }
    }

    private static com.google.gson.JsonObject handleApiListFiles(java.lang.String str) {
        java.lang.String strTrim = "/sdcard";
        try {
            com.google.gson.JsonObject asJsonObject = com.google.gson.JsonParser.parseString(str).getAsJsonObject();
            java.lang.String asString = asJsonObject.has("path") ? asJsonObject.get("path").getAsString() : null;
            if (asString != null && !asString.trim().isEmpty()) {
                strTrim = asString.trim();
            }
        } catch (java.lang.Exception unused) {
        }
        java.io.File file = new java.io.File(strTrim);
        com.google.gson.JsonObject jsonObject = new com.google.gson.JsonObject();
        jsonObject.addProperty("path", strTrim);
        if (!file.exists() || !file.isDirectory()) {
            jsonObject.addProperty("exists", (java.lang.Boolean) false);
            jsonObject.add("parent", file.getParentFile() == null ? com.google.gson.JsonNull.INSTANCE : net2(file.getParentFile().getAbsolutePath()));
            jsonObject.add("items", new com.google.gson.JsonArray());
            return envelope(0, "ok", jsonObject);
        }
        jsonObject.addProperty("exists", (java.lang.Boolean) true);
        jsonObject.add("parent", file.getParentFile() == null ? com.google.gson.JsonNull.INSTANCE : net2(file.getParentFile().getAbsolutePath()));
        java.io.File[] fileArrListFiles = file.listFiles();
        com.google.gson.JsonArray jsonArray = new com.google.gson.JsonArray();
        if (fileArrListFiles != null) {
            java.util.ArrayList<java.io.File> arrayList = new java.util.ArrayList();
            java.util.ArrayList<java.io.File> arrayList2 = new java.util.ArrayList();
            for (java.io.File file2 : fileArrListFiles) {
                (file2.isDirectory() ? arrayList : arrayList2).add(file2);
            }
            for (java.io.File file3 : arrayList) {
                com.google.gson.JsonObject jsonObject2 = new com.google.gson.JsonObject();
                jsonObject2.addProperty("type", "dir");
                jsonObject2.addProperty(com.sun.mail.imap.IMAPStore.ID_NAME, file3.getName());
                jsonObject2.addProperty("path", file3.getAbsolutePath());
                jsonArray.add(jsonObject2);
            }
            for (java.io.File file4 : arrayList2) {
                com.google.gson.JsonObject jsonObject3 = new com.google.gson.JsonObject();
                jsonObject3.addProperty("type", "file");
                jsonObject3.addProperty(com.sun.mail.imap.IMAPStore.ID_NAME, file4.getName());
                jsonObject3.addProperty("path", file4.getAbsolutePath());
                jsonObject3.addProperty("size", java.lang.Long.valueOf(file4.length()));
                jsonArray.add(jsonObject3);
            }
        }
        jsonObject.add("items", jsonArray);
        return envelope(0, "ok", jsonObject);
    }

    private static com.google.gson.JsonObject net2(java.lang.String str) {
        com.google.gson.JsonObject jsonObject = new com.google.gson.JsonObject();
        jsonObject.addProperty("path", str);
        return jsonObject;
    }

    private static com.google.gson.JsonObject handleApiStats() {
        java.lang.String strSubstring = "";
        java.lang.String strSelfUin;
        int i;
        int i2;
        int i3;
        long j;
        int iIndexOf;
        int i4;
        int iIndexOf2;
        com.google.gson.JsonObject jsonObject = new com.google.gson.JsonObject();
        try {
            java.lang.String strSelfInfoJson = com.tencent.qqlitepro.http.QqKernel.selfInfoJson();
            strSubstring = (strSelfInfoJson == null || (iIndexOf = strSelfInfoJson.indexOf("\"nick\":\"")) < 0 || (iIndexOf2 = strSelfInfoJson.indexOf(34, (i4 = iIndexOf + 8))) <= i4) ? "" : strSelfInfoJson.substring(i4, iIndexOf2);
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("stats 取昵称失败: " + th);
        }
        try {
            strSelfUin = com.tencent.qqlitepro.http.QqKernel.selfUin();
        } catch (java.lang.Throwable th2) {
            com.tencent.qqlitepro.util.Log.w("stats 取uin失败: " + th2);
            strSelfUin = "";
        }
        try {
            i = 0;
            i2 = 0;
            i3 = 0;
            for (com.google.gson.JsonElement jsonElement : fetchSessions()) {
                try {
                    if (jsonElement.isJsonObject()) {
                        i++;
                        java.lang.String asString = jsonElement.getAsJsonObject().get("chatType") == null ? "" : jsonElement.getAsJsonObject().get("chatType").getAsString();
                        if ("2".equals(asString)) {
                            i2++;
                        } else if ("1".equals(asString)) {
                            i3++;
                        }
                    }
                } catch (java.lang.Throwable th3) {
                    java.lang.Throwable thRethrow = th3;
                    com.tencent.qqlitepro.util.Log.w("stats 会话统计失败: " + thRethrow);
                }
            }
        } catch (java.lang.Throwable th4) {
            java.lang.Throwable thRethrow2 = th4;
            i = 0;
            i2 = 0;
            i3 = 0;
        }
        try {
            java.lang.reflect.Field declaredField = com.tencent.qqlitepro.hook.MsgListenerHook.class.getDeclaredField("sRecvCount");
            declaredField.setAccessible(true);
            j = declaredField.getLong(null);
        } catch (java.lang.Throwable unused) {
            j = 0;
        }
        if (strSubstring == null || strSubstring.isEmpty()) {
            strSubstring = strSelfUin == null || strSelfUin.isEmpty() ? "未获取" : strSelfUin;
        }
        jsonObject.addProperty("account", strSubstring);
        jsonObject.addProperty("uin", strSelfUin);
        jsonObject.addProperty("avatarUrl", strSelfUin.isEmpty() ? "" : "https://q1.qlogo.cn/g?b=qq&nk=" + strSelfUin + "&s=100");
        jsonObject.addProperty("sessionTotal", java.lang.Integer.valueOf(i));
        jsonObject.addProperty("groupCount", java.lang.Integer.valueOf(i2));
        jsonObject.addProperty("friendCount", java.lang.Integer.valueOf(i3));
        jsonObject.addProperty("recvCount", java.lang.Long.valueOf(j));
        jsonObject.addProperty("startedAt", java.lang.Long.valueOf(sStartedAt));
        jsonObject.addProperty("uptime", java.lang.Long.valueOf(sStartedAt > 0 ? java.lang.System.currentTimeMillis() - sStartedAt : 0L));
        return envelope(0, "ok", jsonObject);
    }

}
