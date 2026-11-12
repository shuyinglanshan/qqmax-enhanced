package com.tencent.qqlitepro.plugin;

/* JADX INFO: loaded from: classes.dex */
public class PluginAPI {
    private static final int SHARD_CONCURRENCY = 3;
    private static final int SHARD_MAX_ATTEMPTS = 3;
    private final com.tencent.qqlitepro.plugin.PluginEngine.Plugin p;

    public PluginAPI(com.tencent.qqlitepro.plugin.PluginEngine.Plugin plugin) {
        this.p = plugin;
    }

    public void log(java.lang.String str) {
        java.lang.StringBuilder sb = new java.lang.StringBuilder("[");
        sb.append(this.p.name);
        sb.append("] ");
        if (str == null) {
            str = "";
        }
        sb.append(str);
        com.tencent.qqlitepro.plugin.PluginEngine.appendLog(sb.toString());
    }

    public void toast(java.lang.String str) {
        if (str == null) {
            str = "";
        }
        com.tencent.qqlitepro.plugin.PluginEngine.toastOnMain(str);
    }

    public void registerFeature(java.lang.String str, java.lang.String str2, java.lang.String str3) {
        if (str == null || str.isEmpty() || str3 == null || str3.isEmpty()) {
            return;
        }
        com.tencent.qqlitepro.plugin.PluginEngine.Plugin plugin = this.p;
        if (str2 == null) {
            str2 = "";
        }
        com.tencent.qqlitepro.plugin.PluginEngine.registerFeature0(plugin, str, str2, str3);
    }

    public java.lang.String getSetting(java.lang.String str) {
        return getSetting(str, "");
    }

    public java.lang.String getSetting(java.lang.String str, java.lang.String str2) {
        if (str == null) {
            return str2 == null ? "" : str2;
        }
        java.io.File file = settingsFile();
        if (file == null || !file.exists()) {
            return str2 == null ? "" : str2;
        }
        for (java.lang.String str3 : readFile(file).split("\n")) {
            int iIndexOf = str3.indexOf(61);
            if (iIndexOf > 0 && str3.substring(0, iIndexOf).trim().equals(str)) {
                return str3.substring(iIndexOf + 1).trim();
            }
        }
        return str2 == null ? "" : str2;
    }

    public void setSetting(java.lang.String str, java.lang.String str2) {
        java.io.File file;
        if (str == null || str.isEmpty() || (file = settingsFile()) == null) {
            return;
        }
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        boolean z = false;
        for (java.lang.String str3 : (file.exists() ? readFile(file) : "").split("\n")) {
            int iIndexOf = str3.indexOf(61);
            if (iIndexOf > 0 && str3.substring(0, iIndexOf).trim().equals(str)) {
                sb.append(str);
                sb.append('=');
                sb.append(str2 == null ? "" : str2);
                sb.append('\n');
                z = true;
            } else {
                sb.append(str3);
                sb.append('\n');
            }
        }
        if (!z) {
            sb.append(str);
            sb.append('=');
            sb.append(str2 == null ? "" : str2);
            sb.append('\n');
        }
        writeFile(file, sb.toString());
        java.lang.StringBuilder sb2 = new java.lang.StringBuilder("设置 ");
        sb2.append(str);
        sb2.append(" = ");
        if (str2 == null) {
            str2 = "";
        }
        sb2.append(str2);
        log(sb2.toString());
    }

    public void hook(java.lang.String str, java.lang.String str2, java.lang.String str3) {
        if (str == null || str2 == null || str3 == null) {
            return;
        }
        com.tencent.qqlitepro.plugin.PluginEngine.registerFeatureHook(this.p, str, str2, str3);
    }

    public void onMessage(java.lang.String str) {
        if (str == null || str.isEmpty()) {
            return;
        }
        com.tencent.qqlitepro.plugin.PluginEngine.registerMessageCb(this.p, str);
        log("已注册消息回调函数 " + str);
    }

    public int sendText(int i, java.lang.String str, java.lang.String str2) {
        if (str == null || str2 == null || str2.isEmpty()) {
            return -1;
        }
        try {
            java.lang.Object objCreateContact = com.tencent.qqlitepro.http.QqKernel.createContact(i, str);
            java.util.ArrayList arrayList = new java.util.ArrayList();
            arrayList.add(com.tencent.qqlitepro.http.QqKernel.createTextElement(str2));
            com.tencent.qqlitepro.http.QqKernel.SendResult sendResultSendMsg = com.tencent.qqlitepro.http.QqKernel.sendMsg(objCreateContact, arrayList, 5000L);
            if (sendResultSendMsg.code != 0) {
                log("发送失败(chatType=" + i + " peer=" + str + ") 原因：" + sendResultSendMsg.msg);
            }
            return sendResultSendMsg.code;
        } catch (java.lang.Throwable th) {
            log("发送异常: " + th);
            return -2;
        }
    }

    public int banAll(java.lang.String str, boolean z) {
        if (str == null) {
            return -1;
        }
        try {
            com.tencent.qqlitepro.http.QqKernel.BanResult groupShutUp = com.tencent.qqlitepro.http.QqKernel.setGroupShutUp(str, z, 5000L);
            if (groupShutUp.code != 0) {
                log("全体禁言失败: " + groupShutUp.msg);
            }
            return groupShutUp.code;
        } catch (java.lang.Throwable th) {
            log("全体禁言异常: " + th);
            return -2;
        }
    }

    public java.lang.String banAllStatus(java.lang.String str) {
        if (str == null) {
            return "{}";
        }
        try {
            com.tencent.qqlitepro.http.QqKernel.BanResult banResultBanAllStatus = com.tencent.qqlitepro.http.QqKernel.banAllStatus(str);
            return "{\"muted\":" + banResultBanAllStatus.muted + ",\"groupCode\":\"" + banResultBanAllStatus.groupCode + "\",\"shutUpAllTimestamp\":" + banResultBanAllStatus.shutUpAllTimestamp + "}";
        } catch (java.lang.Throwable unused) {
            return "{}";
        }
    }

    public boolean isGroupAdmin(java.lang.String str, java.lang.String str2) {
        try {
            return com.tencent.qqlitepro.http.QqKernel.isGroupOwnerOrAdmin(str, str2);
        } catch (java.lang.Throwable unused) {
            return false;
        }
    }

    public java.lang.String getMsgHistory(int i) {
        try {
            java.util.List<com.tencent.qqlitepro.cache.RawMsgStore.Entry> listRecent = com.tencent.qqlitepro.cache.RawMsgStore.recent(i, null);
            com.google.gson.JsonArray jsonArray = new com.google.gson.JsonArray();
            for (com.tencent.qqlitepro.cache.RawMsgStore.Entry entry : listRecent) {
                com.google.gson.JsonObject jsonObject = new com.google.gson.JsonObject();
                jsonObject.addProperty("msgId", entry.msgId);
                jsonObject.addProperty("peerUid", entry.peerUid);
                jsonObject.addProperty("chatType", java.lang.Integer.valueOf(entry.chatType));
                jsonObject.addProperty("time", java.lang.Long.valueOf(entry.time));
                try {
                    jsonObject.add("raw", com.google.gson.JsonParser.parseString(entry.rawJson));
                } catch (java.lang.Exception unused) {
                    jsonObject.addProperty("raw", entry.rawJson);
                }
                jsonArray.add(jsonObject);
            }
            return jsonArray.toString();
        } catch (java.lang.Throwable unused2) {
            return "[]";
        }
    }

    public java.lang.String download(java.lang.String str, java.lang.String str2) {
        java.lang.String strDownload = com.tencent.qqlitepro.util.Net.download(com.tencent.qqlitepro.plugin.PluginEngine.getContext(), str, str2);
        if (strDownload != null) {
            return strDownload;
        }
        log("下载失败: " + str);
        return "";
    }

    private java.lang.String ensureLocal(java.lang.String str) {
        if (str == null || str.trim().isEmpty()) {
            return null;
        }
        java.lang.String strTrim = str.trim();
        if (strTrim.startsWith("http://") || strTrim.startsWith("https://")) {
            return com.tencent.qqlitepro.util.Net.download(com.tencent.qqlitepro.plugin.PluginEngine.getContext(), strTrim, null);
        }
        if (new java.io.File(strTrim).exists()) {
            return strTrim;
        }
        return null;
    }

    public int sendVoice(int i, java.lang.String str, java.lang.String str2) {
        java.lang.String strEnsureLocal = ensureLocal(str2);
        if (strEnsureLocal == null) {
            log("语音文件不存在或下载失败");
            return -3;
        }
        try {
            java.lang.Object objCreateContact = com.tencent.qqlitepro.http.QqKernel.createContact(i, str);
            java.util.ArrayList arrayList = new java.util.ArrayList();
            arrayList.add(com.tencent.qqlitepro.http.QqKernel.createPttElement(strEnsureLocal));
            com.tencent.qqlitepro.http.QqKernel.SendResult sendResultSendMsg = com.tencent.qqlitepro.http.QqKernel.sendMsg(objCreateContact, arrayList, 8000L);
            if (sendResultSendMsg.code != 0) {
                log("发送语音失败: " + sendResultSendMsg.msg);
            }
            return sendResultSendMsg.code;
        } catch (java.lang.Throwable th) {
            log("发送语音异常: " + th);
            return -2;
        }
    }

    public int sendImage(int i, java.lang.String str, java.lang.String str2) {
        java.lang.String strEnsureLocal = ensureLocal(str2);
        if (strEnsureLocal == null) {
            log("图片文件不存在或下载失败");
            return -3;
        }
        try {
            java.lang.Object objCreateContact = com.tencent.qqlitepro.http.QqKernel.createContact(i, str);
            java.util.ArrayList arrayList = new java.util.ArrayList();
            arrayList.add(com.tencent.qqlitepro.http.QqKernel.createPicElement(strEnsureLocal));
            com.tencent.qqlitepro.http.QqKernel.SendResult sendResultSendMsg = com.tencent.qqlitepro.http.QqKernel.sendMsg(objCreateContact, arrayList, 8000L);
            if (sendResultSendMsg.code != 0) {
                log("发送图片失败: " + sendResultSendMsg.msg);
            }
            return sendResultSendMsg.code;
        } catch (java.lang.Throwable th) {
            log("发送图片异常: " + th);
            return -2;
        }
    }

    public int sendFile(int i, java.lang.String str, java.lang.String str2) {
        java.util.List listPrepareFakeVoices;
        java.lang.String str3;
        java.lang.String strEnsureLocal = ensureLocal(str2);
        if (strEnsureLocal == null) {
            log("文件不存在或下载失败");
            return -3;
        }
        java.io.File file = new java.io.File(strEnsureLocal);
        if (!file.isFile()) {
            log("目标不是文件");
            return -3;
        }
        try {
            android.content.Context context = com.tencent.qqlitepro.plugin.PluginEngine.getContext();
            boolean z = com.tencent.qqlitepro.util.Config.sendMode(context) == 0;
            boolean zShardEnabled = com.tencent.qqlitepro.util.Config.shardEnabled(context);
            long j = z ? com.tencent.qqlitepro.http.FakeImage.SHARD_BYTES : 12582912L;
            long length = file.length();
            if (length > j && !zShardEnabled) {
                log("文件超过 " + com.tencent.qqlitepro.http.FakeVoice.formatSize(j) + " 且未开启“允许文件分片发送”，无法发送(当前 " + com.tencent.qqlitepro.http.FakeVoice.formatSize(length) + ")");
                return -4;
            }
            if (z) {
                listPrepareFakeVoices = com.tencent.qqlitepro.http.FakeImage.prepareFakeImages(strEnsureLocal, zShardEnabled);
            } else {
                listPrepareFakeVoices = com.tencent.qqlitepro.http.FakeVoice.prepareFakeVoices(strEnsureLocal, zShardEnabled);
            }
            java.util.List list = listPrepareFakeVoices;
            if (list != null && !list.isEmpty()) {
                java.lang.Object objCreateContact = com.tencent.qqlitepro.http.QqKernel.createContact(i, str);
                android.os.Handler handler = new android.os.Handler(android.os.Looper.getMainLooper());
                int size = list.size();
                java.lang.StringBuilder sb = new java.lang.StringBuilder("开始发送");
                sb.append(z ? "伪图片" : "伪语音");
                if (size > 1) {
                    str3 = "，共 " + size + " 分片（并发度 " + java.lang.Math.min(3, size) + "）";
                } else {
                    str3 = "";
                }
                sb.append(str3);
                log(sb.toString());
                java.util.concurrent.ExecutorService executorServiceNewFixedThreadPool = java.util.concurrent.Executors.newFixedThreadPool(java.lang.Math.min(3, size));
                java.util.ArrayList arrayList = new java.util.ArrayList();
                int i2 = 0;
                while (i2 < size) {
                    boolean z2 = z;
                    boolean z3 = z;
                    java.util.concurrent.ExecutorService executorService = executorServiceNewFixedThreadPool;
                    arrayList.add(executorService.submit(new com.tencent.qqlitepro.plugin.PluginAPI$$ExternalSyntheticLambda0(this, i2, list, handler, z2, objCreateContact, size)));
                    i2++;
                    executorServiceNewFixedThreadPool = executorService;
                    z = z3;
                }
                java.util.concurrent.ExecutorService executorService2 = executorServiceNewFixedThreadPool;
                java.util.Iterator it = arrayList.iterator();
                int i3 = 0;
                while (it.hasNext()) {
                    try {
                        if (((java.lang.Boolean) ((java.util.concurrent.Future) it.next()).get()).booleanValue()) {
                            i3++;
                        }
                    } catch (java.lang.Throwable th) {
                        log("等待分片结果异常: " + th);
                    }
                }
                executorService2.shutdown();
                if (i3 == size) {
                    return 0;
                }
                log("文件发送不完整: 成功 " + i3 + "/" + size);
                return -5;
            }
            log("文件封装失败");
            return -4;
        } catch (java.lang.Throwable th2) {
            log("发送文件异常: " + th2);
            return -2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.lang.Boolean lambda$sendFile$0(int i, java.util.List list, android.os.Handler handler, boolean z, java.lang.Object obj, int i2) throws java.lang.Exception {
        java.lang.String string;
        try {
            long jMin = ((long) java.lang.Math.min(i, 2)) * 350;
            if (jMin > 0) {
                java.lang.Thread.sleep(jMin);
            }
        } catch (java.lang.Throwable unused) {
        }
        java.lang.Object obj2 = list.get(i);
        java.lang.String str = "未知错误";
        int i3 = 1;
        while (i3 <= 3) {
            java.lang.Object objBuildFakeElement = buildFakeElement(handler, z, obj2);
            if (objBuildFakeElement == null) {
                if (i3 < 3) {
                    try {
                        java.lang.Thread.sleep(800L);
                    } catch (java.lang.Throwable unused2) {
                    }
                }
                string = "元素构造失败";
            } else {
                java.util.ArrayList arrayList = new java.util.ArrayList();
                arrayList.add(objBuildFakeElement);
                com.tencent.qqlitepro.http.QqKernel.SendResult sendResultSendMsg = com.tencent.qqlitepro.http.QqKernel.sendMsg(obj, arrayList, 120000L);
                java.lang.String str2 = "";
                if (sendResultSendMsg != null && sendResultSendMsg.code == 0) {
                    java.lang.StringBuilder sb = new java.lang.StringBuilder("分片 ");
                    sb.append(i + 1);
                    sb.append("/");
                    sb.append(i2);
                    if (i3 > 1) {
                        str2 = "（重试 " + (i3 - 1) + " 次后成功）";
                    }
                    sb.append(str2);
                    sb.append(" 发送成功");
                    log(sb.toString());
                    return java.lang.Boolean.TRUE;
                }
                if (sendResultSendMsg == null) {
                    string = "发送超时/无响应";
                } else {
                    java.lang.StringBuilder sb2 = new java.lang.StringBuilder("code=");
                    sb2.append(sendResultSendMsg.code);
                    sb2.append(" ");
                    sb2.append(sendResultSendMsg.msg != null ? sendResultSendMsg.msg : "");
                    string = sb2.toString();
                }
                if (i3 < 3) {
                    try {
                        java.lang.Thread.sleep(800L);
                    } catch (java.lang.Throwable unused3) {
                    }
                }
            }
            i3++;
            str = string;
        }
        log("分片 " + (i + 1) + "/" + i2 + " 发送失败: " + str);
        return java.lang.Boolean.FALSE;
    }

    private java.lang.Object buildFakeElement(android.os.Handler handler, boolean z, java.lang.Object obj) {
        java.lang.Object[] objArr = {null};
        java.lang.Throwable[] thArr = {null};
        handler.post(new com.tencent.qqlitepro.plugin.PluginAPI$$ExternalSyntheticLambda1(z, obj, objArr, thArr));
        long jCurrentTimeMillis = java.lang.System.currentTimeMillis() + 8000;
        while (objArr[0] == null && thArr[0] == null && java.lang.System.currentTimeMillis() < jCurrentTimeMillis) {
            try {
                java.lang.Thread.sleep(20L);
            } catch (java.lang.Throwable unused) {
            }
        }
        if (thArr[0] != null) {
            log("主线程构造分片元素失败: " + thArr[0]);
        }
        return objArr[0];
    }

    static /* synthetic */ void lambda$buildFakeElement$1(boolean z, java.lang.Object obj, java.lang.Object[] objArr, java.lang.Throwable[] thArr) {
        try {
            if (z) {
                java.lang.Object objCreatePicElement = com.tencent.qqlitepro.http.QqKernel.createPicElement(((com.tencent.qqlitepro.http.FakeImage.Wrapped) obj).pngFile.getAbsolutePath());
                objArr[0] = objCreatePicElement;
                com.tencent.qqlitepro.http.QqKernel.setPicDisplaySize(objCreatePicElement, 416, 468);
            } else {
                objArr[0] = com.tencent.qqlitepro.http.QqKernel.createPttElement(((com.tencent.qqlitepro.http.FakeVoice.Wrapped) obj).amrFile.getAbsolutePath());
            }
        } catch (java.lang.Throwable th) {
            thArr[0] = th;
        }
    }

    public int sendFace(int i, java.lang.String str, int i2) {
        try {
            java.lang.Object objCreateContact = com.tencent.qqlitepro.http.QqKernel.createContact(i, str);
            java.util.ArrayList arrayList = new java.util.ArrayList();
            arrayList.add(com.tencent.qqlitepro.http.QqKernel.createFaceElement(i2));
            com.tencent.qqlitepro.http.QqKernel.SendResult sendResultSendMsg = com.tencent.qqlitepro.http.QqKernel.sendMsg(objCreateContact, arrayList, 5000L);
            if (sendResultSendMsg.code != 0) {
                log("发送表情失败: " + sendResultSendMsg.msg);
            }
            return sendResultSendMsg.code;
        } catch (java.lang.Throwable th) {
            log("发送表情异常: " + th);
            return -2;
        }
    }

    public int sendVideo(int i, java.lang.String str, java.lang.String str2) {
        java.lang.String strEnsureLocal = ensureLocal(str2);
        if (strEnsureLocal == null) {
            log("视频文件不存在或下载失败");
            return -3;
        }
        try {
            java.lang.Object objCreateContact = com.tencent.qqlitepro.http.QqKernel.createContact(i, str);
            java.util.ArrayList arrayList = new java.util.ArrayList();
            arrayList.add(com.tencent.qqlitepro.http.QqKernel.createVideoElement(strEnsureLocal));
            com.tencent.qqlitepro.http.QqKernel.SendResult sendResultSendMsg = com.tencent.qqlitepro.http.QqKernel.sendMsg(objCreateContact, arrayList, 15000L);
            if (sendResultSendMsg.code != 0) {
                log("发送视频失败: " + sendResultSendMsg.msg);
            }
            return sendResultSendMsg.code;
        } catch (java.lang.Throwable th) {
            log("发送视频异常: " + th);
            return -2;
        }
    }

    public int sendReplyMsg(int i, java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.String str4, java.lang.String str5, boolean z) {
        if (str == null || str2 == null || str2.isEmpty()) {
            return -1;
        }
        try {
            com.tencent.qqlitepro.http.QqKernel.SendResult sendResultSendReplyMsg = com.tencent.qqlitepro.http.QqKernel.sendReplyMsg(com.tencent.qqlitepro.http.QqKernel.createContact(i, str), com.tencent.qqlitepro.http.QqKernel.buildElementsWithAt(str5 == null ? "" : str5, z, z ? str3 : null, str4), str2, str3, str4, 8000L);
            if (sendResultSendReplyMsg.code != 0) {
                log("发送回复消息失败: " + sendResultSendReplyMsg.msg);
            }
            return sendResultSendReplyMsg.code;
        } catch (java.lang.Throwable th) {
            log("发送回复消息异常: " + th);
            return -2;
        }
    }

    public boolean setReplyWhitelist(java.lang.String str, java.lang.String str2) {
        try {
            android.content.Context context = com.tencent.qqlitepro.plugin.PluginEngine.getContext();
            if (context == null) {
                return false;
            }
            java.lang.String lowerCase = str == null ? "" : str.trim().toLowerCase();
            if (str2 == null) {
                str2 = "";
            }
            if ("keyword".equals(lowerCase)) {
                com.tencent.qqlitepro.util.Config.setKeywordRules(context, com.tencent.qqlitepro.util.Config.keywordReplyEnabled(context), com.tencent.qqlitepro.util.Config.keywordRules(context), str2);
                return true;
            }
            if ("auto".equals(lowerCase)) {
                com.tencent.qqlitepro.util.Config.setAutoReply(context, com.tencent.qqlitepro.util.Config.autoReplyEnabled(context), com.tencent.qqlitepro.util.Config.autoReplyContent(context), str2, com.tencent.qqlitepro.util.Config.autoReplyAt(context));
                return true;
            }
            log("未知白名单类型: " + str + "（仅支持 keyword / auto）");
            return false;
        } catch (java.lang.Throwable th) {
            log("设置白名单异常: " + th);
            return false;
        }
    }

    public java.lang.String getReplyWhitelist(java.lang.String str) {
        try {
            android.content.Context context = com.tencent.qqlitepro.plugin.PluginEngine.getContext();
            if (context == null) {
                return "";
            }
            java.lang.String lowerCase = str == null ? "" : str.trim().toLowerCase();
            if ("keyword".equals(lowerCase)) {
                return com.tencent.qqlitepro.util.Config.keywordReplyPeers(context);
            }
            if ("auto".equals(lowerCase)) {
                return com.tencent.qqlitepro.util.Config.autoReplyPeers(context);
            }
            return "";
        } catch (java.lang.Throwable unused) {
        }
        return "";
    }

    public java.lang.String getSessions() {
        try {
            return new com.google.gson.Gson().toJson(com.tencent.qqlitepro.http.QqKernel.getSessions());
        } catch (java.lang.Throwable th) {
            log("获取会话失败: " + th);
            return "[]";
        }
    }

    public java.lang.String getSelfInfo() {
        try {
            return com.tencent.qqlitepro.http.QqKernel.selfInfoJson();
        } catch (java.lang.Throwable th) {
            log("获取自身信息异常: " + th);
            return "{}";
        }
    }

    public boolean isSelf(java.lang.String str) {
        try {
            return com.tencent.qqlitepro.http.QqKernel.isSelfTarget(str);
        } catch (java.lang.Throwable unused) {
            return false;
        }
    }

    public java.lang.String getSelfUid() {
        try {
            return com.tencent.qqlitepro.http.QqKernel.selfUid();
        } catch (java.lang.Throwable unused) {
            return "";
        }
    }

    public java.lang.String getSelfUin() {
        try {
            return com.tencent.qqlitepro.http.QqKernel.selfUin();
        } catch (java.lang.Throwable unused) {
            return "";
        }
    }

    public java.lang.String getGroupInfo(java.lang.String str) {
        if (str == null) {
            return "{}";
        }
        try {
            return com.tencent.qqlitepro.http.QqKernel.groupInfoJson(str);
        } catch (java.lang.Throwable th) {
            log("获取群详情异常: " + th);
            return "{}";
        }
    }

    public java.lang.String getGroupCode(java.lang.String str) {
        java.lang.String groupInfo = getGroupInfo(str);
        if (groupInfo != null && !groupInfo.isEmpty() && !"{}".equals(groupInfo)) {
            try {
                com.google.gson.JsonObject asJsonObject = com.google.gson.JsonParser.parseString(groupInfo).getAsJsonObject();
                if (asJsonObject.has("groupCode")) {
                    return asJsonObject.get("groupCode").getAsString();
                }
            } catch (java.lang.Throwable unused) {
            }
        }
        return "";
    }

    public int modifyGroupName(java.lang.String str, java.lang.String str2) {
        if (str == null || str2 == null || str2.isEmpty()) {
            return -1;
        }
        try {
            com.tencent.qqlitepro.http.QqKernel.BanResult banResultModifyGroupName = com.tencent.qqlitepro.http.QqKernel.modifyGroupName(str, str2, false, 5000L);
            if (banResultModifyGroupName.code != 0) {
                log("修改群名失败(群=" + str + "): " + banResultModifyGroupName.msg);
            }
            return banResultModifyGroupName.code;
        } catch (java.lang.Throwable th) {
            log("修改群名异常: " + th);
            return -2;
        }
    }

    public int modifyGroupRemark(java.lang.String str, java.lang.String str2) {
        if (str == null || str2 == null) {
            return -1;
        }
        try {
            com.tencent.qqlitepro.http.QqKernel.BanResult banResultModifyGroupRemark = com.tencent.qqlitepro.http.QqKernel.modifyGroupRemark(str, str2);
            if (banResultModifyGroupRemark.code != 0) {
                log("修改群备注失败(群=" + str + "): " + banResultModifyGroupRemark.msg);
            }
            return banResultModifyGroupRemark.code;
        } catch (java.lang.Throwable th) {
            log("修改群备注异常: " + th);
            return -2;
        }
    }

    public int kickMember(java.lang.String str, java.lang.String str2, java.lang.String str3) {
        if (str == null || str2 == null || str2.trim().isEmpty()) {
            return -1;
        }
        try {
            com.tencent.qqlitepro.http.QqKernel.BanResult banResultKickMember = com.tencent.qqlitepro.http.QqKernel.kickMember(str, str2, str3);
            if (banResultKickMember.code != 0) {
                log("踢人失败(群=" + str + " 成员=" + str2 + "): " + banResultKickMember.msg);
            }
            return banResultKickMember.code;
        } catch (java.lang.Throwable th) {
            log("踢人异常: " + th);
            return -2;
        }
    }

    public int quitGroup(java.lang.String str) {
        if (str == null) {
            return -1;
        }
        try {
            com.tencent.qqlitepro.http.QqKernel.BanResult banResultQuitGroup = com.tencent.qqlitepro.http.QqKernel.quitGroup(str);
            if (banResultQuitGroup.code != 0) {
                log("退群失败(群=" + str + "): " + banResultQuitGroup.msg);
            }
            return banResultQuitGroup.code;
        } catch (java.lang.Throwable th) {
            log("退群异常: " + th);
            return -2;
        }
    }

    public java.lang.String getGroupMembers(java.lang.String str) {
        if (str == null) {
            return "[]";
        }
        try {
            return com.tencent.qqlitepro.http.QqKernel.getGroupMembers(str);
        } catch (java.lang.Throwable th) {
            log("获取群成员列表异常: " + th);
            return "[]";
        }
    }

    public java.lang.String getGroupNotices(java.lang.String str) {
        if (str == null) {
            return "[]";
        }
        try {
            java.lang.String strFetchGroupNotices = com.tencent.qqlitepro.http.QqKernel.fetchGroupNotices(str);
            return strFetchGroupNotices == null ? "[]" : strFetchGroupNotices;
        } catch (java.lang.Throwable th) {
            log("获取群公告异常: " + th);
            return "[]";
        }
    }

    public java.lang.String getFriendRequests(int i) {
        try {
            com.google.gson.JsonObject friendRequests = com.tencent.qqlitepro.http.QqKernel.getFriendRequests(i <= 0 ? 5000L : i);
            return friendRequests == null ? "{}" : friendRequests.toString();
        } catch (java.lang.Throwable th) {
            log("获取好友申请列表异常: " + th);
            return "{}";
        }
    }

    public int sendFriendRequest(java.lang.String str, java.lang.String str2, java.lang.String str3, int i, java.lang.String str4, java.lang.String str5) {
        if ((str == null || str.trim().isEmpty()) && (str2 == null || str2.trim().isEmpty())) {
            return -1;
        }
        try {
            com.tencent.qqlitepro.http.QqKernel.ApplyResult applyResultSendFriendRequest = com.tencent.qqlitepro.http.QqKernel.sendFriendRequest(str, str2, str3, i, str4, str5, 0, 0, 5000L);
            if (applyResultSendFriendRequest.code != 0) {
                log("发送好友申请失败: " + applyResultSendFriendRequest.msg);
            }
            return applyResultSendFriendRequest.code;
        } catch (java.lang.Throwable th) {
            log("发送好友申请异常: " + th);
            return -2;
        }
    }

    public int approveFriendRequest(java.lang.String str, boolean z, java.lang.String str2) {
        if (str == null || str.trim().isEmpty()) {
            return -1;
        }
        try {
            com.tencent.qqlitepro.http.QqKernel.ApplyResult applyResultApproveFriendRequest = com.tencent.qqlitepro.http.QqKernel.approveFriendRequest(str, z, str2, 0L, 5000L);
            if (applyResultApproveFriendRequest.code != 0) {
                java.lang.StringBuilder sb = new java.lang.StringBuilder();
                sb.append(z ? "同意" : "拒绝");
                sb.append("好友申请失败(uid=");
                sb.append(str);
                sb.append("): ");
                sb.append(applyResultApproveFriendRequest.msg);
                log(sb.toString());
            }
            return applyResultApproveFriendRequest.code;
        } catch (java.lang.Throwable th) {
            log("处理好友申请异常: " + th);
            return -2;
        }
    }

    public int agreeFriendRequest(java.lang.String str) {
        return approveFriendRequest(str, true, "");
    }

    public int refuseFriendRequest(java.lang.String str, java.lang.String str2) {
        return approveFriendRequest(str, false, str2);
    }

    public java.lang.String getGroupRequests(long j, int i) {
        try {
            com.google.gson.JsonObject groupRequests = com.tencent.qqlitepro.http.QqKernel.getGroupRequests(false, j, i <= 0 ? 14 : i, 5000L);
            return groupRequests == null ? "{}" : groupRequests.toString();
        } catch (java.lang.Throwable th) {
            log("获取加群申请列表异常: " + th);
            return "{}";
        }
    }

    public int sendGroupRequest(java.lang.String str, java.lang.String str2) {
        if (str == null || str.trim().isEmpty()) {
            return -1;
        }
        try {
            com.tencent.qqlitepro.http.QqKernel.ApplyResult applyResultSendGroupRequest = com.tencent.qqlitepro.http.QqKernel.sendGroupRequest(str, str2, 0, 0, 5000L);
            if (applyResultSendGroupRequest.code != 0) {
                log("发送加群申请失败(群=" + str + "): " + applyResultSendGroupRequest.msg);
            }
            return applyResultSendGroupRequest.code;
        } catch (java.lang.Throwable th) {
            log("发送加群申请异常: " + th);
            return -2;
        }
    }

    public int operateGroupRequest(java.lang.String str, long j, java.lang.String str2, java.lang.String str3, java.lang.String str4) {
        if (str == null || str.trim().isEmpty()) {
            return -1;
        }
        try {
            com.tencent.qqlitepro.http.QqKernel.ApplyResult applyResultOperateGroupRequest = com.tencent.qqlitepro.http.QqKernel.operateGroupRequest(str, j, str2, str3, str4, 5000L);
            if (applyResultOperateGroupRequest.code != 0) {
                log("处理加群申请失败(群=" + str + " seq=" + j + "): " + applyResultOperateGroupRequest.msg);
            }
            return applyResultOperateGroupRequest.code;
        } catch (java.lang.Throwable th) {
            log("处理加群申请异常: " + th);
            return -2;
        }
    }

    public int agreeGroupRequest(java.lang.String str, long j, java.lang.String str2) {
        return operateGroupRequest(str, j, str2, "agree", "");
    }

    public int refuseGroupRequest(java.lang.String str, long j, java.lang.String str2, java.lang.String str3) {
        return operateGroupRequest(str, j, str2, "refuse", str3);
    }

    public int recall(java.lang.String str, long j) {
        if (str == null) {
            return -1;
        }
        try {
            com.tencent.qqlitepro.http.QqKernel.SendResult sendResultRecallMsg = com.tencent.qqlitepro.http.QqKernel.recallMsg(str, j, 5000L);
            if (sendResultRecallMsg.code != 0) {
                log("撤回消息失败: " + sendResultRecallMsg.msg);
            }
            return sendResultRecallMsg.code;
        } catch (java.lang.Throwable th) {
            log("撤回消息异常: " + th);
            return -2;
        }
    }

    public int recall(java.lang.String str, java.lang.String str2) {
        try {
            return recall(str, java.lang.Long.parseLong(str2));
        } catch (java.lang.Throwable unused) {
            return -1;
        }
    }

    public java.lang.String getOfficialNotices() {
        try {
            return com.tencent.qqlitepro.hook.SettingsInjector.officialNoticesJson();
        } catch (java.lang.Throwable th) {
            log("获取官方公告异常: " + th);
            return "[]";
        }
    }

    public java.lang.String getLatestUpdateVersion() {
        try {
            java.lang.String strFindNewerUpdateVersion = com.tencent.qqlitepro.hook.SettingsInjector.findNewerUpdateVersion(dynCtx());
            return strFindNewerUpdateVersion == null ? "" : strFindNewerUpdateVersion;
        } catch (java.lang.Throwable th) {
            log("获取最新版本号异常: " + th);
            return "";
        }
    }

    public java.lang.String getMailConfig() {
        try {
            android.content.Context contextDynCtx = dynCtx();
            if (contextDynCtx == null) {
                return "{}";
            }
            com.google.gson.JsonObject jsonObject = new com.google.gson.JsonObject();
            jsonObject.addProperty("addr", com.tencent.qqlitepro.util.Config.mailAddr(contextDynCtx));
            jsonObject.addProperty(com.sun.mail.imap.IMAPStore.ID_NAME, com.tencent.qqlitepro.util.Config.mailName(contextDynCtx));
            jsonObject.addProperty("verified", java.lang.Boolean.valueOf(com.tencent.qqlitepro.util.Config.mailVerified(contextDynCtx)));
            jsonObject.addProperty("unread", java.lang.Integer.valueOf(com.tencent.qqlitepro.util.Config.mailUnreadCount(contextDynCtx)));
            jsonObject.addProperty("pollMs", java.lang.Long.valueOf(com.tencent.qqlitepro.util.Config.mailPollMs(contextDynCtx)));
            jsonObject.addProperty("dnd", java.lang.Boolean.valueOf(com.tencent.qqlitepro.util.Config.mailDnd(contextDynCtx)));
            jsonObject.addProperty("pinned", java.lang.Boolean.valueOf(com.tencent.qqlitepro.util.Config.mailPinned(contextDynCtx)));
            jsonObject.addProperty("lastTime", java.lang.Long.valueOf(com.tencent.qqlitepro.util.Config.mailLastTime(contextDynCtx)));
            jsonObject.addProperty("lastUid", com.tencent.qqlitepro.util.Config.mailLastUid(contextDynCtx));
            jsonObject.addProperty("summary", com.tencent.qqlitepro.util.Config.mailLatestSummary(contextDynCtx));
            return jsonObject.toString();
        } catch (java.lang.Throwable th) {
            log("读取邮箱配置异常: " + th);
            return "{}";
        }
    }

    public boolean setMailConfig(java.lang.String str, java.lang.String str2, java.lang.String str3) {
        try {
            android.content.Context contextDynCtx = dynCtx();
            if (contextDynCtx == null) {
                return false;
            }
            com.tencent.qqlitepro.util.Config.setMailConfig(contextDynCtx, str, str2, str3);
            java.lang.StringBuilder sb = new java.lang.StringBuilder("已保存邮箱配置: ");
            if (str == null) {
                str = "";
            }
            sb.append(str);
            log(sb.toString());
            return true;
        } catch (java.lang.Throwable th) {
            log("保存邮箱配置异常: " + th);
            return false;
        }
    }

    public java.lang.String verifyMail() {
        java.lang.String str;
        try {
            android.content.Context contextDynCtx = dynCtx();
            if (contextDynCtx == null) {
                return "{\"ok\":false,\"message\":\"上下文未就绪\"}";
            }
            com.tencent.qqlitepro.mail.QqMailManager.VerifyResult verifyResultVerify = com.tencent.qqlitepro.mail.QqMailManager.verify(contextDynCtx, com.tencent.qqlitepro.util.Config.mailAddr(contextDynCtx), com.tencent.qqlitepro.util.Config.mailAuthCode(contextDynCtx));
            boolean z = verifyResultVerify != null && verifyResultVerify.ok;
            com.tencent.qqlitepro.util.Config.setMailVerified(contextDynCtx, z);
            com.google.gson.JsonObject jsonObject = new com.google.gson.JsonObject();
            jsonObject.addProperty("ok", java.lang.Boolean.valueOf(z));
            if (verifyResultVerify == null) {
                str = "无响应";
            } else {
                str = verifyResultVerify.message == null ? "" : verifyResultVerify.message;
            }
            jsonObject.addProperty("message", str);
            return jsonObject.toString();
        } catch (java.lang.Throwable th) {
            log("验证邮箱异常: " + th);
            return "{\"ok\":false,\"message\":\"异常\"}";
        }
    }

    public int pollMail() {
        try {
            android.content.Context contextDynCtx = dynCtx();
            if (contextDynCtx == null) {
                return -1;
            }
            if (!com.tencent.qqlitepro.util.Config.mailAddr(contextDynCtx).isEmpty() && !com.tencent.qqlitepro.util.Config.mailAuthCode(contextDynCtx).isEmpty()) {
                int iPollInbox = com.tencent.qqlitepro.mail.QqMailManager.pollInbox(contextDynCtx);
                try {
                    com.tencent.qqlitepro.hook.ChatCardInjector.refreshChatList();
                } catch (java.lang.Throwable unused) {
                }
                return iPollInbox;
            }
            log("邮箱未配置，无法拉取收件箱");
            return -1;
        } catch (java.lang.Throwable th) {
            log("拉取收件箱异常: " + th);
            return -2;
        }
    }

    public java.lang.String getMailInbox() {
        java.lang.String strMailInboxCache;
        try {
            android.content.Context contextDynCtx = dynCtx();
            return (contextDynCtx == null || (strMailInboxCache = com.tencent.qqlitepro.util.Config.mailInboxCache(contextDynCtx)) == null || strMailInboxCache.trim().isEmpty()) ? "[]" : strMailInboxCache;
        } catch (java.lang.Throwable unused) {
            return "[]";
        }
    }

    public int getMailUnread() {
        try {
            android.content.Context contextDynCtx = dynCtx();
            if (contextDynCtx == null) {
                return 0;
            }
            return com.tencent.qqlitepro.util.Config.mailUnreadCount(contextDynCtx);
        } catch (java.lang.Throwable unused) {
            return 0;
        }
    }

    public java.lang.String sendMail(java.lang.String str, java.lang.String str2, java.lang.String str3) {
        try {
            android.content.Context contextDynCtx = dynCtx();
            if (contextDynCtx == null) {
                return "{\"ok\":false,\"message\":\"上下文未就绪\"}";
            }
            com.tencent.qqlitepro.mail.QqMailManager.SendResult sendResultSend = com.tencent.qqlitepro.mail.QqMailManager.send(contextDynCtx, str, str2, str3);
            boolean z = sendResultSend != null && sendResultSend.ok;
            java.lang.String str4 = "无响应";
            if (!z) {
                java.lang.StringBuilder sb = new java.lang.StringBuilder("发送邮件失败: ");
                sb.append(sendResultSend == null ? "无响应" : sendResultSend.message);
                log(sb.toString());
            }
            com.google.gson.JsonObject jsonObject = new com.google.gson.JsonObject();
            jsonObject.addProperty("ok", java.lang.Boolean.valueOf(z));
            if (sendResultSend != null) {
                str4 = sendResultSend.message == null ? "" : sendResultSend.message;
            }
            jsonObject.addProperty("message", str4);
            return jsonObject.toString();
        } catch (java.lang.Throwable th) {
            log("发送邮件异常: " + th);
            return "{\"ok\":false,\"message\":\"异常\"}";
        }
    }

    public int markAllMailsRead() {
        try {
            android.content.Context contextDynCtx = dynCtx();
            if (contextDynCtx == null) {
                return 0;
            }
            com.google.gson.JsonArray jsonArray = new com.google.gson.JsonArray();
            java.lang.String strMailInboxCache = com.tencent.qqlitepro.util.Config.mailInboxCache(contextDynCtx);
            if (strMailInboxCache != null && !strMailInboxCache.trim().isEmpty()) {
                try {
                    jsonArray = com.google.gson.JsonParser.parseString(strMailInboxCache).getAsJsonArray();
                } catch (java.lang.Throwable unused) {
                }
            }
            int i = 0;
            for (com.google.gson.JsonElement jsonElement : jsonArray) {
                if (jsonElement.isJsonObject()) {
                    com.google.gson.JsonObject asJsonObject = jsonElement.getAsJsonObject();
                    if (!asJsonObject.has("read") || !asJsonObject.get("read").isJsonPrimitive() || !asJsonObject.get("read").getAsBoolean()) {
                        asJsonObject.addProperty("read", (java.lang.Boolean) true);
                        i++;
                    }
                }
            }
            if (i > 0) {
                com.tencent.qqlitepro.util.Config.setMailInbox(contextDynCtx, jsonArray.toString(), 0, com.tencent.qqlitepro.util.Config.mailLastUid(contextDynCtx));
                try {
                    com.tencent.qqlitepro.hook.ChatCardInjector.refreshChatList();
                } catch (java.lang.Throwable unused2) {
                }
            }
            return i;
        } catch (java.lang.Throwable th) {
            log("一键已读异常: " + th);
            return 0;
        }
    }

    public long time() {
        return java.lang.System.currentTimeMillis();
    }

    public void sleep(long j) {
        if (j <= 0) {
            return;
        }
        try {
            java.lang.Thread.sleep(j);
        } catch (java.lang.Throwable unused) {
        }
    }

    public boolean delayRun(long j, java.lang.String str) {
        if (str != null && !str.isEmpty()) {
            try {
                java.util.Timer timer = new java.util.Timer(true);
                timer.schedule(new com.tencent.qqlitepro.plugin.PluginAPI.AnonymousClass1(str, timer), java.lang.Math.max(0L, j));
                return true;
            } catch (java.lang.Throwable th) {
                log("delayRun 提交失败: " + th);
            }
        }
        return false;
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.plugin.PluginAPI$1, reason: invalid class name */
    class AnonymousClass1 extends java.util.TimerTask {
        final /* synthetic */ java.lang.String val$fnName;
        final /* synthetic */ java.util.Timer val$timer;

        AnonymousClass1(java.lang.String str, java.util.Timer timer) {
            this.val$fnName = str;
            this.val$timer = timer;
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            try {
                com.tencent.qqlitepro.plugin.PluginEngine.invokeFunction(com.tencent.qqlitepro.plugin.PluginAPI.this.p, this.val$fnName);
            } catch (java.lang.Throwable th) {
                try {
                    com.tencent.qqlitepro.plugin.PluginAPI.this.log("delayRun(" + this.val$fnName + ") 异常: " + th);
                } finally {
                    this.val$timer.cancel();
                }
            }
        }
    }

    public java.lang.String likeAllToday() {
        try {
            return logDynamicResult("一键点赞当天全部动态", com.tencent.qqlitepro.auto.QqDynamicScheduler.likeAllToday(dynCtx()));
        } catch (java.lang.Throwable th) {
            log("一键点赞当天全部动态 异常: " + th);
            return "一键点赞当天全部动态 异常: " + th;
        }
    }

    public java.lang.String commentAllToday() {
        try {
            return logDynamicResult("一键评论当天全部动态", com.tencent.qqlitepro.auto.QqDynamicScheduler.commentAllToday(dynCtx()));
        } catch (java.lang.Throwable th) {
            log("一键评论当天全部动态 异常: " + th);
            return "一键评论当天全部动态 异常: " + th;
        }
    }

    public java.lang.String selectWhitelistSessions() {
        try {
            new android.os.Handler(android.os.Looper.getMainLooper()).post(new com.tencent.qqlitepro.plugin.PluginAPI$$ExternalSyntheticLambda2());
            log("已请求打开白名单会话选择窗口（仅私聊）");
            return "已打开会话选择窗口，请在弹出的窗口中选择私聊用户";
        } catch (java.lang.Throwable th) {
            log("打开白名单选择窗口异常: " + th);
            return "打开白名单选择窗口失败: " + th;
        }
    }

    public java.lang.String runDynamicLatest(boolean z, boolean z2) {
        try {
            return logDynamicResult("立即执行 QQ 动态", com.tencent.qqlitepro.auto.QqDynamicScheduler.processLatest(dynCtx(), z, z2));
        } catch (java.lang.Throwable th) {
            log("立即执行 QQ 动态 异常: " + th);
            return "立即执行 QQ 动态 异常: " + th;
        }
    }

    private android.content.Context dynCtx() {
        android.content.Context context = com.tencent.qqlitepro.plugin.PluginEngine.getContext();
        return context != null ? context : com.tencent.qqlitepro.cache.MsgCache.appContext();
    }

    private java.lang.String logDynamicResult(java.lang.String str, java.lang.String str2) {
        java.lang.StringBuilder sb;
        if (str2 == null || str2.isEmpty()) {
            sb = new java.lang.StringBuilder();
            sb.append(str);
            sb.append(" 完成");
        } else {
            sb = new java.lang.StringBuilder();
            sb.append(str);
            sb.append(": ");
            sb.append(str2);
        }
        log(sb.toString());
        return str2 == null ? "" : str2;
    }

    private java.io.File settingsFile() {
        java.io.File filePluginDirFor = com.tencent.qqlitepro.plugin.PluginEngine.pluginDirFor(this.p);
        if (filePluginDirFor == null) {
            return null;
        }
        java.lang.String strSubstring = this.p.base;
        if (strSubstring.toLowerCase().endsWith(".js")) {
            strSubstring = strSubstring.substring(0, strSubstring.length() - 3);
        }
        return new java.io.File(filePluginDirFor, strSubstring + ".settings");
    }

    private static java.lang.String readFile(java.io.File file) {
        try {
            byte[] bArr = new byte[(int) java.lang.Math.min(file.length(), 1048576L)];
            java.io.FileInputStream fileInputStream = new java.io.FileInputStream(file);
            try {
                return new java.lang.String(bArr, 0, java.lang.Math.max(fileInputStream.read(bArr), 0), "UTF-8");
            } finally {
                fileInputStream.close();
            }
        } catch (java.lang.Throwable unused) {
            return "";
        }
    }

    private static void writeFile(java.io.File file, java.lang.String str) {
        try {
            java.io.FileOutputStream fileOutputStream = new java.io.FileOutputStream(file);
            try {
                fileOutputStream.write(str.getBytes("UTF-8"));
            } finally {
                fileOutputStream.close();
            }
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.plugin.PluginEngine.appendLog("[settings] 写设置失败: " + th);
        }
    }
}
