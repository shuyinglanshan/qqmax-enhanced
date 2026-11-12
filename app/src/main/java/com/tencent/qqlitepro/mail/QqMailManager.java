package com.tencent.qqlitepro.mail;

/* JADX INFO: loaded from: classes.dex */
public class QqMailManager {
    private static final int FIRST_FETCH = 20;
    private static final long IDLE_OK_POLL_MS = 60000;
    public static final java.lang.String IMAP_HOST = "imap.qq.com";
    public static final int IMAP_PORT = 993;
    private static final long MAX_ATT_BYTES = 12582912;
    private static final int MAX_BODY = 30000;
    private static final int MAX_CACHE = 200;
    private static final int MAX_HTML = 400000;
    private static final long MAX_SEND_ATT_BYTES = 20971520;
    public static final java.lang.String SMTP_HOST = "smtp.qq.com";
    public static final int SMTP_PORT = 465;
    private static final java.lang.String TAG = "QqMail";
    private static final java.lang.Object sFetchLock = new java.lang.Object();
    private static final java.lang.Object sFetchReqLock = new java.lang.Object();
    private static java.lang.Thread sFetchThread = null;
    private static volatile int sIdleFailCount = 0;
    private static volatile com.sun.mail.imap.IMAPFolder sIdleFolder = null;
    private static volatile boolean sIdleGaveUp = false;
    private static volatile int sIdleGen = 0;
    private static volatile boolean sIdleRunning = false;
    private static volatile javax.mail.Store sIdleStore = null;
    private static volatile java.lang.Thread sIdleThread = null;
    private static long sPendingDelayMs = -1;
    private static volatile int sPollGen = 0;
    private static volatile boolean sPollRunning = false;
    private static volatile java.lang.Thread sPollThread = null;
    private static volatile java.lang.String sRunningAddr = "";

    private QqMailManager() {
    }

    public static final class VerifyResult {
        public final java.lang.String message;
        public final boolean ok;

        public VerifyResult(boolean z, java.lang.String str) {
            this.ok = z;
            this.message = str;
        }
    }

    public static final class SendResult {
        public final java.lang.String message;
        public final boolean ok;

        public SendResult(boolean z, java.lang.String str) {
            this.ok = z;
            this.message = str;
        }
    }

    private static final class MailContent {
        final java.util.List<com.tencent.qqlitepro.mail.QqMailManager.Att> atts;
        java.lang.String html;
        final java.lang.StringBuilder plain;



        private MailContent() {
            this.plain = new java.lang.StringBuilder();
            this.html = "";
            this.atts = new java.util.ArrayList();
        }
    }

    private static final class Att {
        java.lang.String cid;
        boolean image;
        java.lang.String mime;
        java.lang.String name;
        java.lang.String path;
        long size;



        private Att() {
            this.name = "";
            this.mime = "";
            this.path = "";
        }
    }

    public static android.content.Context appContext() {
        try {
            java.lang.reflect.Method method = java.lang.Class.forName("android.app.ActivityThread").getMethod("currentApplication", new java.lang.Class[0]);
            method.setAccessible(true);
            return (android.content.Context) method.invoke(null, new java.lang.Object[0]);
        } catch (java.lang.Throwable unused) {
            return null;
        }
    }

    public static java.lang.String defaultName() {
        int iIndexOf;
        int i;
        int iIndexOf2;
        try {
            java.lang.String strSelfInfoJson = com.tencent.qqlitepro.http.QqKernel.selfInfoJson();
            return (strSelfInfoJson == null || (iIndexOf = strSelfInfoJson.indexOf("\"nick\":\"")) < 0 || (iIndexOf2 = strSelfInfoJson.indexOf(34, (i = iIndexOf + 8))) <= i) ? "" : strSelfInfoJson.substring(i, iIndexOf2);
        } catch (java.lang.Throwable unused) {
            return "";
        }
    }

    public static com.tencent.qqlitepro.mail.QqMailManager.VerifyResult verify(android.content.Context context, java.lang.String str, java.lang.String str2) {
        java.lang.String strTrim = str == null ? "" : str.trim();
        java.lang.String strTrim2 = str2 != null ? str2.trim() : "";
        if (strTrim.isEmpty() || strTrim2.isEmpty()) {
            return new com.tencent.qqlitepro.mail.QqMailManager.VerifyResult(false, "邮箱地址和授权码不能为空");
        }
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        try {
            javax.mail.Store store = javax.mail.Session.getInstance(imapProps()).getStore("imaps");
            store.connect(IMAP_HOST, IMAP_PORT, strTrim, strTrim2);
            try {
                javax.mail.Folder folder = store.getFolder("INBOX");
                if (folder != null && folder.exists()) {
                    sb.append("IMAP 收件连接成功（INBOX 正常）\n");
                } else {
                    sb.append("IMAP 收件连接成功\n");
                }
                store.close();
                try {
                    com.sun.mail.smtp.SMTPTransport sMTPTransport = (com.sun.mail.smtp.SMTPTransport) javax.mail.Session.getInstance(smtpProps(strTrim, strTrim2), new com.tencent.qqlitepro.mail.QqMailManager.AnonymousClass1(strTrim, strTrim2)).getTransport("smtp");
                    try {
                        sMTPTransport.connect(SMTP_HOST, SMTP_PORT, strTrim, strTrim2);
                        sb.append("SMTP 发件连接成功（服务端响应：");
                        sb.append(sMTPTransport.getLastServerResponse());
                        sb.append("）");
                        return new com.tencent.qqlitepro.mail.QqMailManager.VerifyResult(true, sb.toString());
                    } finally {
                        sMTPTransport.close();
                    }
                } catch (java.lang.Throwable th) {
                    return new com.tencent.qqlitepro.mail.QqMailManager.VerifyResult(false, "SMTP 发件连接失败：" + errorText(th));
                }
            } catch (java.lang.Throwable th2) {
                store.close();
                throw th2;
            }
        } catch (java.lang.Throwable th3) {
            return new com.tencent.qqlitepro.mail.QqMailManager.VerifyResult(false, "IMAP 收件连接失败：" + errorText(th3));
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.mail.QqMailManager$1, reason: invalid class name */
    static class AnonymousClass1 extends javax.mail.Authenticator {
        final /* synthetic */ java.lang.String val$fAddr;
        final /* synthetic */ java.lang.String val$fAuth;

        AnonymousClass1(java.lang.String str, java.lang.String str2) {
            this.val$fAddr = str;
            this.val$fAuth = str2;
        }

        @Override // javax.mail.Authenticator
        protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
            return new javax.mail.PasswordAuthentication(this.val$fAddr, this.val$fAuth);
        }
    }

    public static int pollInbox(android.content.Context context) {
        return fetchOnce(context);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v1 */
    /* JADX WARN: Type inference failed for: r1v2, types: [javax.mail.Folder] */
    /* JADX WARN: Type inference failed for: r1v3 */
    /* JADX WARN: Type inference failed for: r3v4 */
    /* JADX WARN: Type inference failed for: r3v6, types: [javax.mail.Folder] */
    /* JADX WARN: Type inference failed for: r3v7 */
    private static int pollInboxDirect(android.content.Context context) {
        javax.mail.Folder r1;
        java.lang.String strMailAddr = com.tencent.qqlitepro.util.Config.mailAddr(context);
        java.lang.String strMailAuthCode = com.tencent.qqlitepro.util.Config.mailAuthCode(context);
        if (strMailAddr.isEmpty() || strMailAuthCode.isEmpty()) {
            return 0;
        }
        javax.mail.Store store = null;
        javax.mail.Folder folder = null;
        try {
            javax.mail.Store store2 = javax.mail.Session.getInstance(imapProps()).getStore("imaps");
            try {
                store2.connect(IMAP_HOST, IMAP_PORT, strMailAddr, strMailAuthCode);
                folder = store2.getFolder("INBOX");
                if (folder != null && folder.exists()) {
                    folder.open(1);
                    int iFetchIncremental = fetchIncremental(context, folder, (javax.mail.UIDFolder) folder);
                    if (folder != null) {
                        try {
                            if (folder.isOpen()) {
                                folder.close(false);
                            }
                        } catch (java.lang.Throwable unused) {
                        }
                    }
                    if (store2 != null) {
                        try {
                            store2.close();
                        } catch (java.lang.Throwable unused2) {
                        }
                    }
                    return iFetchIncremental;
                }
                if (folder != null) {
                    try {
                        if (folder.isOpen()) {
                            folder.close(false);
                        }
                    } catch (java.lang.Throwable unused3) {
                    }
                }
                if (store2 != null) {
                    try {
                        store2.close();
                    } catch (java.lang.Throwable unused4) {
                    }
                }
                return 0;
            } catch (java.lang.Throwable th) {
                java.lang.Throwable thRethrow = th;
                r1 = folder;
                store = store2;
                try {
                    com.tencent.qqlitepro.util.Log.w("QqMail 收件拉取失败: " + thRethrow);
                    return 0;
                } finally {
                    if (r1 != null) {
                        try {
                            if (r1.isOpen()) {
                                r1.close(false);
                            }
                        } catch (java.lang.Throwable unused5) {
                        }
                    }
                    if (store != null) {
                        try {
                            store.close();
                        } catch (java.lang.Throwable unused6) {
                        }
                    }
                }
            }
        } catch (java.lang.Throwable th2) {
            java.lang.Throwable thRethrow2 = th2;
            r1 = null;
        }
        return 0;
    }

    private static int fetchIncremental(android.content.Context context, javax.mail.Folder folder, javax.mail.UIDFolder uIDFolder) {
        int iFetchIncrementalLocked;
        synchronized (sFetchLock) {
            iFetchIncrementalLocked = fetchIncrementalLocked(context, folder, uIDFolder);
        }
        return iFetchIncrementalLocked;
    }

    private static int fetchIncrementalLocked(android.content.Context context, javax.mail.Folder folder, javax.mail.UIDFolder uIDFolder) {
        javax.mail.Message[] messages;
        boolean z;
        try {
            long j = 0;
            long longSafe = parseLongSafe(com.tencent.qqlitepro.util.Config.mailLastUid(context), 0L);
            if (longSafe > 0) {
                try {
                    messages = uIDFolder.getMessagesByUID(1 + longSafe, -1L);
                    z = true;
                } catch (java.lang.Throwable unused) {
                    messages = null;
                    z = false;
                }
            } else {
                messages = null;
                z = false;
            }
            if (messages == null) {
                try {
                    int messageCount = folder.getMessageCount();
                    if (messageCount <= 0) {
                        return 0;
                    }
                    messages = folder.getMessages(java.lang.Math.max(1, messageCount - 19), messageCount);
                } catch (java.lang.Throwable unused2) {
                    messages = new javax.mail.Message[0];
                }
            }
            java.util.TreeMap treeMap = new java.util.TreeMap();
            int length = messages.length;
            int i = 0;
            while (i < length) {
                javax.mail.Message message = messages[i];
                try {
                    long uid = uIDFolder.getUID(message);
                    if (uid > j) {
                        treeMap.put(java.lang.Long.valueOf(uid), message);
                    }
                } catch (java.lang.Throwable unused3) {
                }
                i++;
                j = 0;
            }
            long jLongValue = treeMap.isEmpty() ? longSafe : ((java.lang.Long) treeMap.lastKey()).longValue();
            if (treeMap.isEmpty()) {
                return 0;
            }
            java.util.ArrayList arrayList = new java.util.ArrayList();
            if (longSafe <= 0) {
                java.util.Iterator it = treeMap.descendingKeySet().iterator();
                while (it.hasNext()) {
                    arrayList.add((javax.mail.Message) treeMap.get((java.lang.Long) it.next()));
                    if (arrayList.size() >= 20) {
                        break;
                    }
                }
            } else {
                for (java.lang.Object lObj : ((java.util.Map<?, ?>) treeMap).keySet()) {
                    java.lang.Long l = (java.lang.Long) lObj;
                    if (l.longValue() > longSafe) {
                        arrayList.add((javax.mail.Message) treeMap.get(l));
                    }
                }
            }
            if (arrayList.isEmpty()) {
                return 0;
            }
            if (!z) {
                treeMap = null;
            }
            java.util.Collections.sort(arrayList, new com.tencent.qqlitepro.mail.QqMailManager.AnonymousClass2(treeMap, uIDFolder));
            java.util.List<com.google.gson.JsonObject> cache = parseCache(com.tencent.qqlitepro.util.Config.mailInboxCache(context));
            java.util.Iterator it2 = arrayList.iterator();
            int i2 = 0;
            while (it2.hasNext()) {
                try {
                    com.google.gson.JsonObject jsonObjectBuildEntry = buildEntry(context, uIDFolder, (javax.mail.Message) it2.next());
                    if (jsonObjectBuildEntry != null && jsonObjectBuildEntry.has("uid")) {
                        removeByUid(cache, jsonObjectBuildEntry.get("uid").getAsLong());
                        cache.add(0, jsonObjectBuildEntry);
                        i2++;
                    }
                } catch (java.lang.Throwable thRethrow2) {
                    com.tencent.qqlitepro.util.Log.w("QqMail 处理新邮件失败，已跳过该封: " + thRethrow2);
                }
            }
            int iMailUnreadCount = com.tencent.qqlitepro.util.Config.mailUnreadCount(context) + i2;
            while (cache.size() > 200) {
                cache.remove(cache.size() - 1);
            }
            if (i2 > 0) {
                if (jLongValue > 0) {
                    longSafe = jLongValue;
                }
                com.tencent.qqlitepro.util.Config.setMailInbox(context, cacheToJson(cache), iMailUnreadCount, java.lang.String.valueOf(longSafe));
                if (i2 > 0) {
                    com.tencent.qqlitepro.util.Log.i("QqMail 拉取到新邮件 " + i2 + " 封，未读总数 " + iMailUnreadCount);
                }
            }
            return i2;
        } catch (java.lang.Throwable th2) {
            com.tencent.qqlitepro.util.Log.w("QqMail 收件拉取失败: " + th2);
            return 0;
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.mail.QqMailManager$2, reason: invalid class name */
    static class AnonymousClass2 implements java.util.Comparator<javax.mail.Message> {
        final /* synthetic */ javax.mail.UIDFolder val$uf;
        final /* synthetic */ java.util.TreeMap val$uidIndex;

        AnonymousClass2(java.util.TreeMap treeMap, javax.mail.UIDFolder uIDFolder) {
            this.val$uidIndex = treeMap;
            this.val$uf = uIDFolder;
        }

        @Override // java.util.Comparator
        public int compare(javax.mail.Message message, javax.mail.Message message2) {
            return java.lang.Long.compare(com.tencent.qqlitepro.mail.QqMailManager.fastUid(this.val$uidIndex, this.val$uf, message2), com.tencent.qqlitepro.mail.QqMailManager.fastUid(this.val$uidIndex, this.val$uf, message));
        }
    }

    private static long safeUid(javax.mail.UIDFolder uIDFolder, javax.mail.Message message) {
        try {
            long uid = uIDFolder.getUID(message);
            if (uid > 0) {
                return uid;
            }
            return Long.MAX_VALUE;
        } catch (java.lang.Throwable unused) {
            return Long.MAX_VALUE;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static long fastUid(java.util.TreeMap<java.lang.Long, javax.mail.Message> treeMap, javax.mail.UIDFolder uIDFolder, javax.mail.Message message) {
        if (treeMap != null) {
            for (java.util.Map.Entry<java.lang.Long, javax.mail.Message> entry : treeMap.entrySet()) {
                if (entry.getValue() == message) {
                    return entry.getKey().longValue();
                }
            }
        }
        return safeUid(uIDFolder, message);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v1 */
    /* JADX WARN: Type inference failed for: r0v2, types: [javax.mail.Folder] */
    /* JADX WARN: Type inference failed for: r0v3 */
    /* JADX WARN: Type inference failed for: r4v5 */
    /* JADX WARN: Type inference failed for: r4v7, types: [javax.mail.Folder] */
    /* JADX WARN: Type inference failed for: r4v8 */
    public static boolean upgradeEntry(android.content.Context context, long j) {
        javax.mail.Folder r0;
        if (j <= 0) {
            return false;
        }
        java.lang.String strMailAddr = com.tencent.qqlitepro.util.Config.mailAddr(context);
        java.lang.String strMailAuthCode = com.tencent.qqlitepro.util.Config.mailAuthCode(context);
        if (strMailAddr.isEmpty() || strMailAuthCode.isEmpty()) {
            return false;
        }
        javax.mail.Store store = null;
        javax.mail.Folder folder = null;
        try {
            javax.mail.Store store2 = javax.mail.Session.getInstance(imapProps()).getStore("imaps");
            try {
                store2.connect(IMAP_HOST, IMAP_PORT, strMailAddr, strMailAuthCode);
                folder = store2.getFolder("INBOX");
                if (folder != null && folder.exists()) {
                    folder.open(1);
                    javax.mail.UIDFolder uIDFolder = (javax.mail.UIDFolder) folder;
                    javax.mail.Message messageByUID = uIDFolder.getMessageByUID(j);
                    if (messageByUID == null) {
                        if (folder != null) {
                            try {
                                if (folder.isOpen()) {
                                    folder.close(false);
                                }
                            } catch (java.lang.Throwable unused) {
                            }
                        }
                        if (store2 != null) {
                            try {
                                if (store2.isConnected()) {
                                    store2.close();
                                }
                            } catch (java.lang.Throwable unused2) {
                            }
                        }
                        return false;
                    }
                    com.google.gson.JsonObject jsonObjectBuildEntry = buildEntry(context, uIDFolder, messageByUID);
                    if (jsonObjectBuildEntry == null) {
                        if (folder != null) {
                            try {
                                if (folder.isOpen()) {
                                    folder.close(false);
                                }
                            } catch (java.lang.Throwable unused3) {
                            }
                        }
                        if (store2 != null) {
                            try {
                                if (store2.isConnected()) {
                                    store2.close();
                                }
                            } catch (java.lang.Throwable unused4) {
                            }
                        }
                        return false;
                    }
                    java.util.List<com.google.gson.JsonObject> cache = parseCache(com.tencent.qqlitepro.util.Config.mailInboxCache(context));
                    int i = 0;
                    while (true) {
                        if (i >= cache.size()) {
                            i = -1;
                            break;
                        }
                        com.google.gson.JsonObject jsonObject = cache.get(i);
                        if (jsonObject.has("uid") && jsonObject.get("uid").getAsLong() == j) {
                            break;
                        }
                        i++;
                    }
                    if (i < 0) {
                        if (folder != null) {
                            try {
                                if (folder.isOpen()) {
                                    folder.close(false);
                                }
                            } catch (java.lang.Throwable unused5) {
                            }
                        }
                        if (store2 != null) {
                            try {
                                if (store2.isConnected()) {
                                    store2.close();
                                }
                            } catch (java.lang.Throwable unused6) {
                            }
                        }
                        return false;
                    }
                    jsonObjectBuildEntry.addProperty("read", java.lang.Boolean.valueOf(cache.get(i).has("read") && cache.get(i).get("read").getAsBoolean()));
                    jsonObjectBuildEntry.addProperty("uid", java.lang.Long.valueOf(j));
                    cache.set(i, jsonObjectBuildEntry);
                    com.tencent.qqlitepro.util.Config.setMailInbox(context, cacheToJson(cache), com.tencent.qqlitepro.util.Config.mailUnreadCount(context), com.tencent.qqlitepro.util.Config.mailLastUid(context));
                    if (folder != null) {
                        try {
                            if (folder.isOpen()) {
                                folder.close(false);
                            }
                        } catch (java.lang.Throwable unused7) {
                        }
                    }
                    if (store2 != null) {
                        try {
                            if (store2.isConnected()) {
                                store2.close();
                            }
                        } catch (java.lang.Throwable unused8) {
                        }
                    }
                    return true;
                }
                if (folder != null) {
                    try {
                        if (folder.isOpen()) {
                            folder.close(false);
                        }
                    } catch (java.lang.Throwable unused9) {
                    }
                }
                if (store2 != null) {
                    try {
                        if (store2.isConnected()) {
                            store2.close();
                        }
                    } catch (java.lang.Throwable unused10) {
                    }
                }
                return false;
            } catch (java.lang.Throwable thRethrow2) {
                r0 = folder;
                store = store2;
                try {
                    com.tencent.qqlitepro.util.Log.w("QqMail 单封邮件补解析失败(uid=" + j + "): " + thRethrow2);
                    return false;
                } finally {
                    if (r0 != null) {
                        try {
                            if (r0.isOpen()) {
                                r0.close(false);
                            }
                        } catch (java.lang.Throwable unused11) {
                        }
                    }
                    if (store != null) {
                        try {
                            if (store.isConnected()) {
                                store.close();
                            }
                        } catch (java.lang.Throwable unused12) {
                        }
                    }
                }
            }
        } catch (java.lang.Throwable th2) {
            java.lang.Throwable thRethrow3 = th2;
            r0 = null;
        }
        return false;
    }

    /* JADX WARN: Code duplicated, block: B:130:0x0274 A[Catch: all -> 0x027b, TRY_LEAVE, TryCatch #13 {all -> 0x027b, blocks: (B:128:0x026e, B:130:0x0274), top: B:163:0x026e }] */
    private static com.google.gson.JsonObject buildEntry(android.content.Context context, javax.mail.UIDFolder uIDFolder, javax.mail.Message message) {
        long jCurrentTimeMillis;
        java.lang.String personal = "";
        java.lang.String address = "";
        java.util.Date receivedDate;
        java.lang.String lowerCase;
        java.lang.String absolutePath;
        com.google.gson.JsonObject jsonObject = new com.google.gson.JsonObject();
        long j = 0;
        try {
            jCurrentTimeMillis = uIDFolder.getUID(message);
        } catch (java.lang.Throwable thRethrow3) {
            try {
                com.tencent.qqlitepro.util.Log.w("QqMail 取邮件 UID 失败: " + thRethrow3);
                jCurrentTimeMillis = 0;
            } catch (java.lang.Throwable th2) {
                com.tencent.qqlitepro.util.Log.w("QqMail 构建邮件条目异常，仍入库 uid=" + j + ": " + th2);
                try {
                    if (!jsonObject.has("read")) {
                        jsonObject.addProperty("read", (java.lang.Boolean) false);
                    }
                } catch (java.lang.Throwable unused) {
                }
                return jsonObject;
            }
        }
        if (jCurrentTimeMillis <= 0) {
            try {
                jCurrentTimeMillis = java.lang.System.currentTimeMillis();
            } catch (java.lang.Throwable th3) {
                j = jCurrentTimeMillis;
                com.tencent.qqlitepro.util.Log.w("QqMail 构建邮件条目异常，仍入库 uid=" + j + ": " + th3);
                if (!jsonObject.has("read")) {
                    jsonObject.addProperty("read", (java.lang.Boolean) false);
                }
                return jsonObject;
            }
        }
        jsonObject.addProperty("uid", java.lang.Long.valueOf(jCurrentTimeMillis));
        try {
            javax.mail.internet.InternetAddress[] internetAddressArr = (javax.mail.internet.InternetAddress[]) message.getFrom();
            if (internetAddressArr == null || internetAddressArr.length <= 0) {
                address = "";
                personal = address;
            } else {
                personal = internetAddressArr[0].getPersonal();
                try {
                    address = internetAddressArr[0].getAddress();
                } catch (java.lang.Throwable unused2) {
                    address = "";
                }
            }
        } catch (java.lang.Throwable unused3) {
            personal = "";
        }
        if (personal == null) {
            personal = "";
        }
        jsonObject.addProperty("fromName", personal);
        if (address == null) {
            address = "";
        }
        jsonObject.addProperty("fromAddr", address);
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        try {
            javax.mail.internet.InternetAddress[] internetAddressArr2 = (javax.mail.internet.InternetAddress[]) message.getRecipients(javax.mail.Message.RecipientType.TO);
            if (internetAddressArr2 != null) {
                for (int i = 0; i < internetAddressArr2.length; i++) {
                    if (i > 0) {
                        sb.append("，");
                    }
                    java.lang.String personal2 = internetAddressArr2[i].getPersonal();
                    java.lang.String address2 = internetAddressArr2[i].getAddress();
                    if (personal2 == null || personal2.isEmpty()) {
                        personal2 = address2 == null ? "" : address2;
                    }
                    sb.append(personal2);
                }
            }
        } catch (java.lang.Throwable unused4) {
        }
        jsonObject.addProperty("to", sb.toString());
        java.lang.String subject = "";
        try {
            subject = message.getSubject();
        } catch (java.lang.Throwable unused5) {
        }
        if (subject == null) {
            subject = "";
        }
        jsonObject.addProperty("subject", subject);
        java.io.File fileAttDir = null;
        try {
            receivedDate = message.getSentDate();
        } catch (java.lang.Throwable unused5) {
            receivedDate = null;
        }
        if (receivedDate == null) {
            try {
                receivedDate = message.getReceivedDate();
            } catch (java.lang.Throwable unused6) {
            }
        }
        long time = receivedDate == null ? 0L : receivedDate.getTime();
        jsonObject.addProperty("time", java.lang.Long.valueOf(time));
        jsonObject.addProperty("timeStr", time <= 0 ? "" : fmt(time));
        try {
            java.lang.String contentType = message.getContentType();
            lowerCase = contentType == null ? "" : contentType.trim().toLowerCase(java.util.Locale.ROOT);
            try {
                int iIndexOf = lowerCase.indexOf(59);
                if (iIndexOf >= 0) {
                    lowerCase = lowerCase.substring(0, iIndexOf).trim();
                }
            } catch (java.lang.Throwable th4) {
                java.lang.Throwable thRethrow6 = th4;
                com.tencent.qqlitepro.util.Log.w("QqMail 读取邮件 Content-Type 失败: " + thRethrow6);
            }
        } catch (java.lang.Throwable th5) {
            java.lang.Throwable thRethrow7 = th5;
            lowerCase = "";
        }
        jsonObject.addProperty("contentType", lowerCase);
        com.tencent.qqlitepro.mail.QqMailManager.MailContent mailContent = new com.tencent.qqlitepro.mail.QqMailManager.MailContent();
        try {
            fileAttDir = attDir(context, jCurrentTimeMillis);
        } catch (java.lang.Throwable unused7) {
        }
        try {
            walkParts(message, mailContent, fileAttDir, new int[]{0});
        } catch (java.lang.Throwable th6) {
            com.tencent.qqlitepro.util.Log.w("QqMail 解析邮件内容失败: " + th6);
        }
        java.lang.String strTrim = mailContent.plain.toString().trim();
        boolean z = (mailContent.html == null || mailContent.html.isEmpty()) ? false : true;
        if (strTrim.isEmpty() && z) {
            try {
                strTrim = htmlToText(mailContent.html).trim();
            } catch (java.lang.Throwable th7) {
                com.tencent.qqlitepro.util.Log.w("QqMail 网页正文转纯文本失败: " + th7);
            }
        }
        if (strTrim.length() > MAX_BODY) {
            strTrim = strTrim.substring(0, MAX_BODY) + "\n…（内容过长已截断）";
        }
        jsonObject.addProperty("body", strTrim);
        jsonObject.addProperty("hasHtml", java.lang.Boolean.valueOf(z));
        if (!z || fileAttDir == null) {
            absolutePath = "";
        } else {
            try {
                java.io.File file = new java.io.File(fileAttDir, "body.html");
                writeBytes(file, rewriteCid(mailContent.html, mailContent.atts).getBytes("UTF-8"));
                absolutePath = file.getAbsolutePath();
            } catch (java.lang.Throwable unused8) {
                absolutePath = "";
            }
        }
        jsonObject.addProperty("htmlPath", absolutePath);
        com.google.gson.JsonArray jsonArray = new com.google.gson.JsonArray();
        for (com.tencent.qqlitepro.mail.QqMailManager.Att att : mailContent.atts) {
            com.google.gson.JsonObject jsonObject2 = new com.google.gson.JsonObject();
            jsonObject2.addProperty(com.sun.mail.imap.IMAPStore.ID_NAME, att.name);
            jsonObject2.addProperty("size", java.lang.Long.valueOf(att.size));
            jsonObject2.addProperty("mime", att.mime);
            jsonObject2.addProperty("path", att.path == null ? "" : att.path);
            jsonObject2.addProperty("image", java.lang.Boolean.valueOf(att.image));
            jsonObject2.addProperty(javax.mail.Part.INLINE, java.lang.Boolean.valueOf((att.cid == null || att.cid.isEmpty()) ? false : true));
            jsonArray.add(jsonObject2);
        }
        jsonObject.add("atts", jsonArray);
        jsonObject.addProperty("read", (java.lang.Boolean) false);
        return jsonObject;
    }

    private static void walkParts(javax.mail.Part part, com.tencent.qqlitepro.mail.QqMailManager.MailContent mailContent, java.io.File file, int[] iArr) throws java.lang.Exception {
        java.lang.String fileName;
        java.lang.String disposition;
        java.lang.String strTrim = null;
        long size;
        try {
            fileName = part.getFileName();
        } catch (java.lang.Throwable unused) {
            fileName = null;
        }
        try {
            disposition = part.getDisposition();
        } catch (java.lang.Throwable unused2) {
            disposition = null;
        }
        try {
            java.lang.String[] header = part.getHeader("Content-ID");
            strTrim = (header == null || header.length <= 0) ? null : header[0];
        } catch (java.lang.Throwable unused3) {
        }
        if (strTrim != null) {
            strTrim = strTrim.trim();
            if (strTrim.startsWith("<") && strTrim.endsWith(">") && strTrim.length() > 2) {
                strTrim = strTrim.substring(1, strTrim.length() - 1);
            }
            if (strTrim.isEmpty()) {
                strTrim = null;
            }
        }
        if (!part.isMimeType("multipart/*") && (javax.mail.Part.ATTACHMENT.equalsIgnoreCase(disposition) || ((fileName != null && !fileName.trim().isEmpty()) || strTrim != null))) {
            com.tencent.qqlitepro.mail.QqMailManager.Att att = new com.tencent.qqlitepro.mail.QqMailManager.Att();
            att.name = decodeName(fileName);
            att.mime = safeMime(part);
            att.image = att.mime.startsWith("image/");
            att.cid = strTrim;
            int i = iArr[0] + 1;
            iArr[0] = i;
            if (att.name.isEmpty()) {
                java.lang.StringBuilder sb = new java.lang.StringBuilder();
                sb.append(att.image ? "image_" : "file_");
                sb.append(i);
                sb.append(guessExt(att.mime));
                att.name = sb.toString();
            }
            try {
                size = part.getSize();
            } catch (java.lang.Throwable unused4) {
                size = -1;
            }
            if (size > MAX_ATT_BYTES) {
                att.size = size;
                mailContent.atts.add(att);
                com.tencent.qqlitepro.util.Log.w("QqMail 附件超过缓存上限（" + size + " 字节），仅记录元信息: " + att.name);
                return;
            }
            try {
                long[] jArr = new long[1];
                att.path = savePart(part, file, i + "_" + safeName(att.name), jArr);
                att.size = jArr[0];
            } catch (java.lang.Throwable thRethrow7) {
                com.tencent.qqlitepro.util.Log.w("QqMail 附件内容缓存失败（邮件与正文仍保留）: " + att.name + ": " + thRethrow7);
            }
            if (att.path == null || att.path.isEmpty()) {
                att.path = "";
                if (size > 0) {
                    att.size = size;
                }
            }
            mailContent.atts.add(att);
            return;
        }
        if (part.isMimeType("text/plain")) {
            java.lang.Object objPartContent = partContent(part, "text/plain");
            if (objPartContent != null) {
                mailContent.plain.append(objPartContent.toString());
                return;
            }
            return;
        }
        if (part.isMimeType("text/html")) {
            java.lang.Object objPartContent2 = partContent(part, "text/html");
            if (objPartContent2 == null || !mailContent.html.isEmpty()) {
                return;
            }
            mailContent.html = objPartContent2.toString();
            return;
        }
        java.lang.Object objPartContent3 = partContent(part, "其他段");
        if (objPartContent3 instanceof javax.mail.Multipart) {
            javax.mail.Multipart multipart = (javax.mail.Multipart) objPartContent3;
            for (int i2 = 0; i2 < multipart.getCount(); i2++) {
                walkParts(multipart.getBodyPart(i2), mailContent, file, iArr);
            }
            return;
        }
        if (part.isMimeType("message/rfc822") && (objPartContent3 instanceof javax.mail.Part)) {
            walkParts((javax.mail.Part) objPartContent3, mailContent, file, iArr);
        } else if ((objPartContent3 instanceof java.lang.String) && part.isMimeType("text/*")) {
            mailContent.plain.append(objPartContent3.toString());
        }
    }

    private static java.lang.Object partContent(javax.mail.Part part, java.lang.String str) {
        try {
            return part.getContent();
        } catch (java.lang.Throwable thRethrow7) {
            com.tencent.qqlitepro.util.Log.w("QqMail 读取邮件段内容失败（" + str + "），已跳过该段: " + thRethrow7);
            return null;
        }
    }

    private static java.io.File attDir(android.content.Context context, long j) {
        if (context == null) {
            context = appContext();
        }
        if (context == null) {
            return null;
        }
        java.io.File file = new java.io.File(new java.io.File(context.getFilesDir(), "mail_att"), java.lang.String.valueOf(j));
        if (file.exists() || file.mkdirs()) {
            return file;
        }
        return null;
    }

    public static java.lang.String writeHtmlBody(android.content.Context context, long j, java.lang.String str) {
        if (j > 0 && str != null && !str.isEmpty()) {
            try {
                java.io.File fileAttDir = attDir(context, j);
                if (fileAttDir == null) {
                    return "";
                }
                java.io.File file = new java.io.File(fileAttDir, "body.html");
                writeBytes(file, rewriteCid(str, new java.util.ArrayList()).getBytes("UTF-8"));
                return file.getAbsolutePath();
            } catch (java.lang.Throwable thRethrow7) {
                com.tencent.qqlitepro.util.Log.w("QqMail 写入网页正文文件失败: " + thRethrow7);
            }
        }
        return "";
    }

    private static java.lang.String savePart(javax.mail.Part part, java.io.File file, java.lang.String str, long[] jArr) throws java.lang.Exception {
        java.io.InputStream inputStream;
        if (file == null) {
            return null;
        }
        if ((!file.exists() && !file.mkdirs()) || (inputStream = part.getInputStream()) == null) {
            return null;
        }
        java.io.File file2 = new java.io.File(file, str);
        java.io.FileOutputStream fileOutputStream = new java.io.FileOutputStream(file2);
        long j = 0;
        try {
            try {
                byte[] bArr = new byte[8192];
                while (true) {
                    int i = inputStream.read(bArr);
                    if (i <= 0) {
                        try {
                            inputStream.close();
                        } catch (java.lang.Throwable unused) {
                        }
                        try {
                            fileOutputStream.flush();
                            fileOutputStream.close();
                        } catch (java.lang.Throwable unused2) {
                        }
                        jArr[0] = j;
                        return file2.getAbsolutePath();
                    }
                    j += (long) i;
                    if (j > MAX_ATT_BYTES) {
                        throw new java.lang.RuntimeException("附件超过缓存上限");
                    }
                    fileOutputStream.write(bArr, 0, i);
                    try {
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    } catch (java.lang.Throwable unused3) {
                    }
                    jArr[0] = j;
                    try {
                        file2.delete();
                    } catch (java.lang.Throwable unused4) {
                    }
                    throw new java.lang.RuntimeException("附件缓存失败");
                }
            } catch (java.lang.Throwable unused5) {
                inputStream.close();
                fileOutputStream.flush();
                fileOutputStream.close();
                jArr[0] = j;
                file2.delete();
                throw new java.lang.RuntimeException("附件缓存失败");
            }
        } catch (java.lang.Throwable unused6) {
        }
        return null;
    }

    private static java.lang.String rewriteCid(java.lang.String str, java.util.List<com.tencent.qqlitepro.mail.QqMailManager.Att> list) {
        try {
            for (com.tencent.qqlitepro.mail.QqMailManager.Att att : list) {
                if (att.cid != null && !att.cid.isEmpty() && att.path != null && !att.path.isEmpty()) {
                    java.lang.String str2 = "file://" + att.path;
                    str = str.replace("cid:" + att.cid, str2).replace("cid:" + att.cid.toUpperCase(java.util.Locale.ROOT), str2);
                }
            }
        } catch (java.lang.Throwable unused) {
        }
        return str.length() > MAX_HTML ? str.substring(0, MAX_HTML) : str;
    }

    private static java.lang.String safeMime(javax.mail.Part part) {
        try {
            java.lang.String contentType = part.getContentType();
            if (contentType == null) {
                return "";
            }
            java.lang.String lowerCase = contentType.trim().toLowerCase(java.util.Locale.ROOT);
            int iIndexOf = lowerCase.indexOf(59);
            return iIndexOf >= 0 ? lowerCase.substring(0, iIndexOf).trim() : lowerCase;
        } catch (java.lang.Throwable unused) {
            return "";
        }
    }

    private static java.lang.String decodeName(java.lang.String str) {
        if (str == null) {
            return "";
        }
        java.lang.String strTrim = str.trim();
        try {
            strTrim = javax.mail.internet.MimeUtility.decodeText(strTrim);
        } catch (java.lang.Throwable unused) {
        }
        return strTrim == null ? "" : strTrim.trim();
    }

    private static java.lang.String safeName(java.lang.String str) {
        if (str == null) {
            str = "";
        }
        java.lang.String strTrim = str.replaceAll("[\\\\/:*?\"<>|\\r\\n\\t]", "_").trim();
        if (strTrim.isEmpty()) {
            strTrim = "file_" + java.lang.System.currentTimeMillis();
        }
        return strTrim.length() > 120 ? strTrim.substring(0, 120) : strTrim;
    }

    private static java.lang.String guessExt(java.lang.String str) {
        if (str == null) {
            str = "";
        }
        if (str.contains("jpeg")) {
            return ".jpg";
        }
        if (str.contains("png")) {
            return ".png";
        }
        if (str.contains("gif")) {
            return ".gif";
        }
        if (str.contains("webp")) {
            return ".webp";
        }
        if (str.contains("bmp")) {
            return ".bmp";
        }
        if (str.contains("pdf")) {
            return ".pdf";
        }
        if (str.contains("html")) {
            return ".html";
        }
        if (str.contains("plain")) {
            return ".txt";
        }
        if (str.contains("zip")) {
            return ".zip";
        }
        return str.contains("msword") ? ".doc" : "";
    }

    private static java.lang.String guessMimeType(java.lang.String str) {
        java.lang.String lowerCase = str == null ? "" : str.toLowerCase(java.util.Locale.ROOT);
        if (lowerCase.endsWith(".jpg") || lowerCase.endsWith(".jpeg")) {
            return "image/jpeg";
        }
        if (lowerCase.endsWith(".png")) {
            return "image/png";
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
        if (lowerCase.endsWith(".pdf")) {
            return "application/pdf";
        }
        if (lowerCase.endsWith(".txt")) {
            return "text/plain";
        }
        if (lowerCase.endsWith(".html") || lowerCase.endsWith(".htm")) {
            return "text/html";
        }
        if (lowerCase.endsWith(".zip")) {
            return "application/zip";
        }
        if (lowerCase.endsWith(".rar")) {
            return "application/x-rar-compressed";
        }
        if (lowerCase.endsWith(".mp3")) {
            return "audio/mpeg";
        }
        if (lowerCase.endsWith(".mp4")) {
            return "video/mp4";
        }
        if (lowerCase.endsWith(".doc")) {
            return "application/msword";
        }
        if (lowerCase.endsWith(".docx")) {
            return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        }
        if (lowerCase.endsWith(".xls")) {
            return "application/vnd.ms-excel";
        }
        if (lowerCase.endsWith(".xlsx")) {
            return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        }
        return lowerCase.endsWith(".apk") ? "application/vnd.android.package-archive" : "application/octet-stream";
    }

    private static byte[] readAllBytes(java.io.InputStream inputStream) throws java.lang.Exception {
        java.io.ByteArrayOutputStream byteArrayOutputStream = new java.io.ByteArrayOutputStream();
        byte[] bArr = new byte[8192];
        while (true) {
            int i = inputStream.read(bArr);
            if (i != -1) {
                byteArrayOutputStream.write(bArr, 0, i);
            } else {
                return byteArrayOutputStream.toByteArray();
            }
        }
    }

    private static void writeBytes(java.io.File file, byte[] bArr) throws java.lang.Exception {
        java.io.FileOutputStream fileOutputStream = new java.io.FileOutputStream(file);
        try {
            fileOutputStream.write(bArr);
        } finally {
            try {
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (java.lang.Throwable unused) {
            }
        }
    }

    private static java.lang.String htmlToText(java.lang.String str) {
        if (str == null) {
            return "";
        }
        try {
            return str.replaceAll("(?is)<style[^>]*>.*?</style>", " ").replaceAll("(?is)<script[^>]*>.*?</script>", " ").replaceAll("(?i)<br\\s*/?>", "\n").replaceAll("(?i)</p>", "\n").replaceAll("(?i)</div>", "\n").replaceAll("(?i)</tr>", "\n").replaceAll("(?i)</td>", "\t").replaceAll("(?s)<[^>]+>", " ").replace("&nbsp;", " ").replace("&amp;", "&").replace("&lt;", "<").replace("&gt;", ">").replace("&quot;", "\"").replace("&#39;", "'");
        } catch (java.lang.Throwable unused) {
            return str;
        }
    }

    public static com.tencent.qqlitepro.mail.QqMailManager.SendResult send(android.content.Context context, java.lang.String str, java.lang.String str2, java.lang.String str3) {
        return send(context, str, str2, str3, false, null);
    }

    public static com.tencent.qqlitepro.mail.QqMailManager.SendResult send(android.content.Context context, java.lang.String str, java.lang.String str2, java.lang.String str3, boolean z, java.util.List<java.io.File> list) {
        java.lang.String strMailAddr = com.tencent.qqlitepro.util.Config.mailAddr(context);
        java.lang.String strMailAuthCode = com.tencent.qqlitepro.util.Config.mailAuthCode(context);
        java.lang.String strMailName = com.tencent.qqlitepro.util.Config.mailName(context);
        if (strMailAddr.isEmpty() || strMailAuthCode.isEmpty()) {
            return new com.tencent.qqlitepro.mail.QqMailManager.SendResult(false, "尚未配置邮箱，请先在设置中完成配置");
        }
        if (str == null || str.trim().isEmpty()) {
            return new com.tencent.qqlitepro.mail.QqMailManager.SendResult(false, "收件人不能为空");
        }
        java.lang.String strTrim = str.trim();
        java.lang.String str4 = str2 == null ? "" : str2;
        java.lang.String str5 = str3 != null ? str3 : "";
        java.util.ArrayList<java.io.File> arrayList = new java.util.ArrayList();
        long length = 0;
        if (list != null) {
            for (java.io.File file : list) {
                if (file != null && file.isFile() && file.length() > 0) {
                    arrayList.add(file);
                }
            }
        }
        java.util.Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            length += ((java.io.File) it.next()).length();
        }
        if (length > 20971520) {
            return new com.tencent.qqlitepro.mail.QqMailManager.SendResult(false, "附件总大小超过 20MB 限制");
        }
        java.lang.StringBuilder sb = new java.lang.StringBuilder("QqMail 开始发信：收件人=");
        sb.append(strTrim);
        sb.append("，正文=");
        sb.append(str5.length());
        sb.append(" 字符，附件=");
        sb.append(arrayList.size());
        sb.append(" 个/");
        sb.append(length / 1024);
        sb.append("KB，格式=");
        sb.append(z ? "html" : "text");
        com.tencent.qqlitepro.util.Log.i(sb.toString());
        try {
            javax.mail.Session session = javax.mail.Session.getInstance(smtpProps(strMailAddr, strMailAuthCode), new com.tencent.qqlitepro.mail.QqMailManager.AnonymousClass3(strMailAddr, strMailAuthCode));
            javax.mail.internet.MimeMessage mimeMessage = new javax.mail.internet.MimeMessage(session);
            if (strMailName == null || strMailName.isEmpty()) {
                strMailName = strMailAddr;
            }
            mimeMessage.setFrom(new javax.mail.internet.InternetAddress(strMailAddr, strMailName, "UTF-8"));
            mimeMessage.setRecipients(javax.mail.Message.RecipientType.TO, javax.mail.internet.InternetAddress.parse(strTrim, false));
            mimeMessage.setSubject(str4, "UTF-8");
            if (!arrayList.isEmpty()) {
                javax.mail.Multipart mimeMultipart = new javax.mail.internet.MimeMultipart("mixed");
                javax.mail.internet.MimeBodyPart mimeBodyPart = new javax.mail.internet.MimeBodyPart();
                if (z) {
                    mimeBodyPart.setContent(str5, "text/html; charset=UTF-8");
                } else {
                    mimeBodyPart.setText(str5, "UTF-8");
                }
                mimeMultipart.addBodyPart(mimeBodyPart);
                for (java.io.File file2 : arrayList) {
                    java.io.FileInputStream fileInputStream = new java.io.FileInputStream(file2);
                    try {
                        byte[] allBytes = readAllBytes(fileInputStream);
                        try {
                            fileInputStream.close();
                        } catch (java.lang.Throwable unused) {
                        }
                        if (allBytes.length == 0) {
                            return new com.tencent.qqlitepro.mail.QqMailManager.SendResult(false, "附件读取为空：" + file2.getName());
                        }
                        javax.mail.internet.MimeBodyPart mimeBodyPart2 = new javax.mail.internet.MimeBodyPart();
                        mimeBodyPart2.setDataHandler(new javax.activation.DataHandler(new javax.mail.util.ByteArrayDataSource(allBytes, guessMimeType(file2.getName()))));
                        try {
                            mimeBodyPart2.setFileName(javax.mail.internet.MimeUtility.encodeText(file2.getName(), "UTF-8", null));
                        } catch (java.lang.Throwable unused2) {
                            mimeBodyPart2.setFileName(file2.getName());
                        }
                        mimeMultipart.addBodyPart(mimeBodyPart2);
                    } catch (java.lang.Throwable thRethrow7) {
                        try {
                            fileInputStream.close();
                            throw thRethrow7;
                        } catch (java.lang.Throwable unused3) {
                            throw thRethrow7;
                        }
                    }
                }
                mimeMessage.setContent(mimeMultipart);
            } else if (z) {
                mimeMessage.setContent(str5, "text/html; charset=UTF-8");
            } else {
                mimeMessage.setText(str5, "UTF-8");
            }
            mimeMessage.saveChanges();
            com.sun.mail.smtp.SMTPTransport sMTPTransport = (com.sun.mail.smtp.SMTPTransport) session.getTransport("smtp");
            try {
                sMTPTransport.connect(SMTP_HOST, SMTP_PORT, strMailAddr, strMailAuthCode);
                sMTPTransport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
                java.lang.String lastServerResponse = sMTPTransport.getLastServerResponse();
                return new com.tencent.qqlitepro.mail.QqMailManager.SendResult(true, (lastServerResponse == null || lastServerResponse.isEmpty()) ? "发送成功" : "发送成功（服务端响应：" + lastServerResponse + "）");
            } finally {
                try {
                    sMTPTransport.close();
                } catch (java.lang.Throwable unused4) {
                }
            }
        } catch (java.lang.Throwable th2) {
            com.tencent.qqlitepro.util.Log.w("QqMail 发信异常: " + th2);
            return new com.tencent.qqlitepro.mail.QqMailManager.SendResult(false, "发送失败：" + errorText(th2));
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.mail.QqMailManager$3, reason: invalid class name */
    static class AnonymousClass3 extends javax.mail.Authenticator {
        final /* synthetic */ java.lang.String val$fAddr;
        final /* synthetic */ java.lang.String val$fAuth;

        AnonymousClass3(java.lang.String str, java.lang.String str2) {
            this.val$fAddr = str;
            this.val$fAuth = str2;
        }

        @Override // javax.mail.Authenticator
        protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
            return new javax.mail.PasswordAuthentication(this.val$fAddr, this.val$fAuth);
        }
    }

    private static synchronized void startIdle(android.content.Context context) {
        if (sIdleThread == null || !sIdleThread.isAlive()) {
            if (context == null) {
                return;
            }
            java.lang.String strMailAddr = com.tencent.qqlitepro.util.Config.mailAddr(context);
            java.lang.String strMailAuthCode = com.tencent.qqlitepro.util.Config.mailAuthCode(context);
            if (!strMailAddr.isEmpty() && !strMailAuthCode.isEmpty()) {
                int i = sIdleGen + 1;
                sIdleGen = i;
                sIdleRunning = true;
                sIdleThread = new java.lang.Thread(new com.tencent.qqlitepro.mail.QqMailManager.AnonymousClass4(i, strMailAddr, strMailAuthCode), "AntiRecall-QqMail-Idle");
                sIdleThread.setDaemon(true);
                sIdleThread.start();
                com.tencent.qqlitepro.util.Log.i("QqMail IDLE 即时推送已启动");
            }
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.mail.QqMailManager$4, reason: invalid class name */
    static class AnonymousClass4 implements java.lang.Runnable {
        final /* synthetic */ java.lang.String val$addr;
        final /* synthetic */ java.lang.String val$authCode;
        final /* synthetic */ int val$gen;

        AnonymousClass4(int i, java.lang.String str, java.lang.String str2) {
            this.val$gen = i;
            this.val$addr = str;
            this.val$authCode = str2;
        }

        /* JADX WARN: Code duplicated, block: B:102:0x015d A[EDGE_INSN: B:102:0x015d->B:156:0x0231 BREAK  A[LOOP:0: B:3:0x0005->B:215:0x0005]] */
        /* JADX WARN: Code duplicated, block: B:131:0x01dd  */
        @Override // java.lang.Runnable
        public void run() {
            javax.mail.Store store;
            com.sun.mail.imap.IMAPFolder iMAPFolder;
            com.tencent.qqlitepro.util.Log.i("QqMail IDLE 线程启动（imap.qq.com:993）");
            while (com.tencent.qqlitepro.mail.QqMailManager.sIdleRunning && this.val$gen == com.tencent.qqlitepro.mail.QqMailManager.sIdleGen && !com.tencent.qqlitepro.mail.QqMailManager.sIdleGaveUp) {
                try {
                    android.content.Context contextAppContext = com.tencent.qqlitepro.mail.QqMailManager.appContext();
                    if (contextAppContext == null) {
                        java.lang.Thread.sleep(1000L);
                        if (com.tencent.qqlitepro.mail.QqMailManager.sIdleStore == null) {
                            com.tencent.qqlitepro.mail.QqMailManager.sIdleStore = null;
                            com.tencent.qqlitepro.mail.QqMailManager.sIdleFolder = null;
                        }
                    } else if (!com.tencent.qqlitepro.util.Config.mailVerified(contextAppContext) || com.tencent.qqlitepro.util.Config.mailAddr(contextAppContext).isEmpty()) {
                        java.lang.Thread.sleep(3000L);
                        if (com.tencent.qqlitepro.mail.QqMailManager.sIdleStore == null) {
                            com.tencent.qqlitepro.mail.QqMailManager.sIdleStore = null;
                            com.tencent.qqlitepro.mail.QqMailManager.sIdleFolder = null;
                        }
                    } else {
                        java.util.Properties propertiesImapProps = com.tencent.qqlitepro.mail.QqMailManager.imapProps();
                        propertiesImapProps.put("mail.imap.enableimapevents", "true");
                        propertiesImapProps.put("mail.imap.timeout", "60000");
                        javax.mail.Store store2 = javax.mail.Session.getInstance(propertiesImapProps).getStore("imaps");
                        try {
                            store2.connect(com.tencent.qqlitepro.mail.QqMailManager.IMAP_HOST, com.tencent.qqlitepro.mail.QqMailManager.IMAP_PORT, this.val$addr, this.val$authCode);
                            com.tencent.qqlitepro.mail.QqMailManager.sIdleFailCount = 0;
                            com.tencent.qqlitepro.mail.QqMailManager.sIdleStore = store2;
                            com.sun.mail.imap.IMAPFolder iMAPFolder2 = (com.sun.mail.imap.IMAPFolder) store2.getFolder("INBOX");
                            if (iMAPFolder2 != null) {
                                try {
                                    if (iMAPFolder2.exists()) {
                                        iMAPFolder2.open(1);
                                        com.tencent.qqlitepro.mail.QqMailManager.sIdleFolder = iMAPFolder2;
                                        iMAPFolder2.addMessageCountListener(new com.tencent.qqlitepro.mail.QqMailManager.AnonymousClass4.AnonymousClass1());
                                        com.tencent.qqlitepro.util.Log.i("QqMail IDLE 已进入等待（服务器推送即达）");
                                        while (com.tencent.qqlitepro.mail.QqMailManager.sIdleRunning
                                                && this.val$gen == com.tencent.qqlitepro.mail.QqMailManager.sIdleGen) {
                                            long idleStart = java.lang.System.currentTimeMillis();
                                            iMAPFolder2.idle();
                                            synchronized (com.tencent.qqlitepro.mail.QqMailManager.sFetchLock) {
                                                // 与拉取互斥，避免 IDLE 与 fetch 并发操作同一 Folder
                                            }
                                            com.tencent.qqlitepro.mail.QqMailManager.requestFetch();
                                            long cost = java.lang.System.currentTimeMillis() - idleStart;
                                            if (cost < 300) {
                                                java.lang.Thread.sleep(500L);
                                            }
                                        }
                                        if (iMAPFolder2 != null) {
                                            try {
                                                if (iMAPFolder2.isOpen()) {
                                                    iMAPFolder2.close(false);
                                                }
                                            } catch (java.lang.Throwable unused) {
                                            }
                                        }
                                        if (store2 != null) {
                                            try {
                                                store2.close();
                                            } catch (java.lang.Throwable unused2) {
                                            }
                                        }
                                        if (com.tencent.qqlitepro.mail.QqMailManager.sIdleStore == store2) {
                                            com.tencent.qqlitepro.mail.QqMailManager.sIdleStore = null;
                                            com.tencent.qqlitepro.mail.QqMailManager.sIdleFolder = null;
                                        }
                                    }
                                } catch (java.lang.Throwable th2) {
                                    store = store2;
                                    java.lang.Throwable thRethrow8 = th2;
                                    iMAPFolder = iMAPFolder2;
                                    try {
                                        if (this.val$gen != com.tencent.qqlitepro.mail.QqMailManager.sIdleGen) {
                                            if (iMAPFolder != null) {
                                                try {
                                                    if (iMAPFolder.isOpen()) {
                                                        iMAPFolder.close(false);
                                                    }
                                                } catch (java.lang.Throwable unused3) {
                                                }
                                            }
                                            if (store != null) {
                                                try {
                                                    store.close();
                                                } catch (java.lang.Throwable unused4) {
                                                }
                                            }
                                            if (com.tencent.qqlitepro.mail.QqMailManager.sIdleStore != store) {
                                                break;
                                            }
                                            com.tencent.qqlitepro.mail.QqMailManager.sIdleStore = null;
                                            com.tencent.qqlitepro.mail.QqMailManager.sIdleFolder = null;
                                            break;
                                        }
                                        com.tencent.qqlitepro.util.Log.w("QqMail IDLE 异常，稍后重连: " + thRethrow8);
                                        com.tencent.qqlitepro.mail.QqMailManager.sIdleFailCount = com.tencent.qqlitepro.mail.QqMailManager.sIdleFailCount + 1;
                                        if (com.tencent.qqlitepro.mail.QqMailManager.sIdleFailCount > 5) {
                                            com.tencent.qqlitepro.util.Log.w("QqMail IDLE 连续 " + com.tencent.qqlitepro.mail.QqMailManager.sIdleFailCount + " 次失败，暂停推送重连");
                                            com.tencent.qqlitepro.mail.QqMailManager.sIdleGaveUp = true;
                                            if (iMAPFolder != null) {
                                                try {
                                                    if (iMAPFolder.isOpen()) {
                                                        iMAPFolder.close(false);
                                                    }
                                                } catch (java.lang.Throwable unused5) {
                                                }
                                            }
                                            if (store != null) {
                                                try {
                                                    store.close();
                                                } catch (java.lang.Throwable unused6) {
                                                }
                                            }
                                            if (com.tencent.qqlitepro.mail.QqMailManager.sIdleStore != store) {
                                                break;
                                            }
                                            com.tencent.qqlitepro.mail.QqMailManager.sIdleStore = null;
                                            com.tencent.qqlitepro.mail.QqMailManager.sIdleFolder = null;
                                            break;
                                        }
                                        if (iMAPFolder != null) {
                                            try {
                                                if (iMAPFolder.isOpen()) {
                                                    iMAPFolder.close(false);
                                                }
                                            } catch (java.lang.Throwable unused7) {
                                            }
                                        }
                                        if (store != null) {
                                            try {
                                                store.close();
                                            } catch (java.lang.Throwable unused8) {
                                            }
                                        }
                                        if (com.tencent.qqlitepro.mail.QqMailManager.sIdleStore == store) {
                                            com.tencent.qqlitepro.mail.QqMailManager.sIdleStore = null;
                                            com.tencent.qqlitepro.mail.QqMailManager.sIdleFolder = null;
                                        }
                                        com.tencent.qqlitepro.util.Log.i("QqMail IDLE 线程退出");
                                    } catch (java.lang.Throwable th3) {
                                        if (iMAPFolder != null) {
                                            try {
                                                if (iMAPFolder.isOpen()) {
                                                    iMAPFolder.close(false);
                                                }
                                            } catch (java.lang.Throwable unused9) {
                                            }
                                        }
                                        if (store != null) {
                                            try {
                                                store.close();
                                            } catch (java.lang.Throwable unused10) {
                                            }
                                        }
                                        if (com.tencent.qqlitepro.mail.QqMailManager.sIdleStore != store) {
                                            throw th3;
                                        }
                                        com.tencent.qqlitepro.mail.QqMailManager.sIdleStore = null;
                                        com.tencent.qqlitepro.mail.QqMailManager.sIdleFolder = null;
                                        throw th3;
                                    }
                                }
                                if (com.tencent.qqlitepro.mail.QqMailManager.sIdleRunning && this.val$gen == com.tencent.qqlitepro.mail.QqMailManager.sIdleGen && !com.tencent.qqlitepro.mail.QqMailManager.sIdleGaveUp) {
                                    try {
                                        java.lang.Thread.sleep(java.lang.Math.min(30000L, ((long) java.lang.Math.max(1, com.tencent.qqlitepro.mail.QqMailManager.sIdleFailCount)) * 2000));
                                    } catch (java.lang.InterruptedException unused11) {
                                    }
                                }
                            }
                            java.lang.Thread.sleep(3000L);
                            if (iMAPFolder2 != null) {
                                try {
                                    if (iMAPFolder2.isOpen()) {
                                        iMAPFolder2.close(false);
                                    }
                                } catch (java.lang.Throwable unused12) {
                                }
                            }
                            if (store2 != null) {
                                try {
                                    store2.close();
                                } catch (java.lang.Throwable unused13) {
                                }
                            }
                            if (com.tencent.qqlitepro.mail.QqMailManager.sIdleStore == store2) {
                                com.tencent.qqlitepro.mail.QqMailManager.sIdleStore = null;
                                com.tencent.qqlitepro.mail.QqMailManager.sIdleFolder = null;
                            }
                        } catch (java.lang.Throwable th4) {
                            iMAPFolder = null;
                            store = store2;
                            java.lang.Throwable thRethrow9 = th4;
                        }
                    }
                } catch (java.lang.Throwable th5) {
                    java.lang.Throwable thRethrow10 = th5;
                    store = null;
                    iMAPFolder = null;
                }
            }
            com.tencent.qqlitepro.util.Log.i("QqMail IDLE 线程退出");
        }

        /* JADX INFO: renamed from: com.tencent.qqlitepro.mail.QqMailManager$4$1, reason: invalid class name */
        class AnonymousClass1 implements javax.mail.event.MessageCountListener {
            AnonymousClass1() {
            }

            @Override // javax.mail.event.MessageCountListener
            public void messagesAdded(javax.mail.event.MessageCountEvent messageCountEvent) {
                com.tencent.qqlitepro.mail.QqMailManager.onPush();
            }

            @Override // javax.mail.event.MessageCountListener
            public void messagesRemoved(javax.mail.event.MessageCountEvent messageCountEvent) {
                com.tencent.qqlitepro.mail.QqMailManager.onPush();
            }
        }
    }

    private static void stopIdle() {
        sIdleRunning = false;
        sIdleGen++;
        javax.mail.Store store = sIdleStore;
        if (store != null) {
            try {
                store.close();
            } catch (java.lang.Throwable unused) {
            }
        }
        java.lang.Thread thread = sIdleThread;
        if (thread != null && thread.isAlive()) {
            thread.interrupt();
        }
        sIdleThread = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void ensureIdleAlive(android.content.Context context) {
        if (context == null || sIdleGaveUp || !com.tencent.qqlitepro.util.Config.mailVerified(context) || com.tencent.qqlitepro.util.Config.mailAddr(context).isEmpty()) {
            return;
        }
        java.lang.Thread thread = sIdleThread;
        if (sIdleRunning && thread != null && thread.isAlive()) {
            return;
        }
        com.tencent.qqlitepro.util.Log.i("QqMail IDLE 线程未存活，自动重启");
        startIdle(context);
    }

    /* JADX INFO: Access modifiers changed from: private */
    private static boolean isIdleHealthy() {
        javax.mail.Store store = sIdleStore;
        if (sIdleRunning && store != null) {
            try {
                return store.isConnected();
            } catch (java.lang.Throwable unused) {
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    private static void onPush() {
        requestFetch();
    }

    /* JADX INFO: Access modifiers changed from: private */
    private static void requestFetch() {
        java.lang.Object obj = sFetchReqLock;
        synchronized (obj) {
            sPendingDelayMs = 0L;
            java.lang.Thread thread = sFetchThread;
            if (thread == null || !thread.isAlive()) {
                java.lang.Thread thread2 = new java.lang.Thread(new com.tencent.qqlitepro.mail.QqMailManager.AnonymousClass5(), "AntiRecall-QqMail-Fetch");
                sFetchThread = thread2;
                thread2.setDaemon(true);
                sFetchThread.start();
                com.tencent.qqlitepro.util.Log.i("QqMail 拉取工作线程启动（复用长连接，省去每次握手登录）");
            }
            obj.notifyAll();
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.mail.QqMailManager$5, reason: invalid class name */
    static class AnonymousClass5 implements java.lang.Runnable {
        AnonymousClass5() {
        }

        @Override // java.lang.Runnable
        public void run() {
            com.tencent.qqlitepro.mail.QqMailManager.runFetchLoop();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    private static void runFetchLoop() {
        while (true) {
            if (sIdleRunning || sPollRunning) {
                synchronized (sFetchReqLock) {
                    while (sPendingDelayMs < 0) {
                        if (!sIdleRunning && !sPollRunning) {
                            return;
                        }
                        try {
                            sFetchReqLock.wait(30000L);
                        } catch (java.lang.InterruptedException unused) {
                            java.lang.Thread.currentThread().interrupt();
                            return;
                        }
                        sPendingDelayMs++;
                    }
                    sPendingDelayMs = -1L;
                }
                android.content.Context contextAppContext = appContext();
                if (contextAppContext != null && com.tencent.qqlitepro.util.Config.mailVerified(contextAppContext)) {
                    try {
                        runFetch(contextAppContext);
                    } catch (java.lang.Throwable thRethrow10) {
                        com.tencent.qqlitepro.util.Log.w("QqMail 后台拉取异常: " + thRethrow10);
                    }
                }
            } else {
                com.tencent.qqlitepro.util.Log.i("QqMail 拉取工作线程退出");
                return;
            }
        }
    }

    private static void runFetch(android.content.Context context) {
        int i;
        long[] jArr = {-1, 300, 900};
        for (int i2 = 0; i2 < 3; i2++) {
            int iFetchOnce = fetchOnce(context);
            if (iFetchOnce > 0) {
                com.tencent.qqlitepro.util.Log.i("QqMail 收到新邮件 " + iFetchOnce + " 封");
                notifyNewMail(context, iFetchOnce);
                return;
            }
            refreshList(context);
            if (jArr[i2] >= 0 && (i = i2 + 1) < 3) {
                try {
                    java.lang.Thread.sleep(jArr[i]);
                } catch (java.lang.InterruptedException unused) {
                    java.lang.Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }

    private static int fetchOnce(android.content.Context context) {
        com.sun.mail.imap.IMAPFolder iMAPFolder = sIdleFolder;
        javax.mail.Store store = sIdleStore;
        if (iMAPFolder != null && store != null) {
            try {
                if (store.isConnected() && iMAPFolder.isOpen()) {
                    return fetchIncremental(context, iMAPFolder, iMAPFolder);
                }
            } catch (java.lang.Throwable unused) {
            }
        }
        return pollInboxDirect(context);
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.mail.QqMailManager$6, reason: invalid class name */
    static class AnonymousClass6 implements java.lang.Runnable {
        AnonymousClass6() {
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                com.tencent.qqlitepro.hook.ChatCardInjector.refreshChatList();
            } catch (java.lang.Throwable unused) {
            }
        }
    }

    private static void refreshList(android.content.Context context) {
        try {
            new android.os.Handler(android.os.Looper.getMainLooper()).post(new com.tencent.qqlitepro.mail.QqMailManager.AnonymousClass6());
        } catch (java.lang.Throwable unused) {
        }
    }

    public static synchronized void ensurePolling(android.content.Context context) {
        try {
            android.content.Context applicationContext = context != null ? context.getApplicationContext() : appContext();
            if (applicationContext == null) {
                com.tencent.qqlitepro.util.Log.w("QqMail ensurePolling: 无应用上下文，后台等待重试");
                java.lang.Thread thread = new java.lang.Thread(new com.tencent.qqlitepro.mail.QqMailManager.AnonymousClass7(), "AntiRecall-QqMail-Retry");
                thread.setDaemon(true);
                thread.start();
                return;
            }
            if (!com.tencent.qqlitepro.util.Config.mailVerified(applicationContext)) {
                com.tencent.qqlitepro.util.Log.i("QqMail ensurePolling: 未验证通过，暂不轮询（addr=" + com.tencent.qqlitepro.util.Config.mailAddr(applicationContext) + " verified=" + com.tencent.qqlitepro.util.Config.mailVerified(applicationContext) + "）");
                java.lang.Thread thread2 = new java.lang.Thread(new com.tencent.qqlitepro.mail.QqMailManager.AnonymousClass8(), "AntiRecall-QqMail-WaitVerify");
                thread2.setDaemon(true);
                thread2.start();
                return;
            }
            java.lang.String strMailAddr = com.tencent.qqlitepro.util.Config.mailAddr(applicationContext);
            if (sPollThread != null && sPollThread.isAlive()) {
                if (strMailAddr.equals(sRunningAddr)) {
                    return;
                }
                com.tencent.qqlitepro.util.Log.i("QqMail 邮箱凭据已变更，重建收件连接");
                stopPolling();
            }
            sRunningAddr = strMailAddr;
            sIdleFailCount = 0;
            sIdleGaveUp = false;
            startIdle(applicationContext);
            new java.lang.Thread(new com.tencent.qqlitepro.mail.QqMailManager.AnonymousClass9(), "AntiRecall-QqMail-Startup").start();
            sPollRunning = true;
            sPollGen++;
            sPollThread = new java.lang.Thread(new com.tencent.qqlitepro.mail.QqMailManager.AnonymousClass10(applicationContext, sPollGen), "AntiRecall-QqMail-Poll");
            sPollThread.setDaemon(true);
            sPollThread.start();
        } catch (java.lang.Throwable thRethrow10) {
            throw thRethrow10;
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.mail.QqMailManager$7, reason: invalid class name */
    static class AnonymousClass7 implements java.lang.Runnable {
        AnonymousClass7() {
        }

        @Override // java.lang.Runnable
        public void run() {
            for (int i = 0; i < 600; i++) {
                try {
                    java.lang.Thread.sleep(100L);
                    if (com.tencent.qqlitepro.mail.QqMailManager.appContext() != null) {
                        com.tencent.qqlitepro.mail.QqMailManager.ensurePolling(null);
                        return;
                    }
                } catch (java.lang.InterruptedException unused) {
                    return;
                }
            }
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.mail.QqMailManager$8, reason: invalid class name */
    static class AnonymousClass8 implements java.lang.Runnable {
        AnonymousClass8() {
        }

        @Override // java.lang.Runnable
        public void run() {
            for (int i = 0; i < 100; i++) {
                try {
                    java.lang.Thread.sleep(300L);
                    android.content.Context contextAppContext = com.tencent.qqlitepro.mail.QqMailManager.appContext();
                    if (contextAppContext != null && com.tencent.qqlitepro.util.Config.mailVerified(contextAppContext)) {
                        com.tencent.qqlitepro.mail.QqMailManager.ensurePolling(null);
                        return;
                    }
                } catch (java.lang.InterruptedException unused) {
                    return;
                }
            }
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.mail.QqMailManager$9, reason: invalid class name */
    static class AnonymousClass9 implements java.lang.Runnable {
        AnonymousClass9() {
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                android.content.Context contextAppContext = com.tencent.qqlitepro.mail.QqMailManager.appContext();
                if (contextAppContext == null || !com.tencent.qqlitepro.util.Config.mailVerified(contextAppContext)) {
                    return;
                }
                int iPollInbox = com.tencent.qqlitepro.mail.QqMailManager.pollInbox(contextAppContext);
                com.tencent.qqlitepro.util.Log.i("QqMail 启动主动拉取完成，新邮件 " + iPollInbox + " 封");
                if (iPollInbox > 0) {
                    com.tencent.qqlitepro.mail.QqMailManager.notifyNewMail(contextAppContext, iPollInbox);
                }
            } catch (java.lang.Throwable unused) {
            }
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.mail.QqMailManager$10, reason: invalid class name */
    static class AnonymousClass10 implements java.lang.Runnable {
        final /* synthetic */ android.content.Context val$app;
        final /* synthetic */ int val$pollGen;

        AnonymousClass10(android.content.Context context, int i) {
            this.val$app = context;
            this.val$pollGen = i;
        }

        @Override // java.lang.Runnable
        public void run() {
            com.tencent.qqlitepro.util.Log.i("QqMail 轮询线程启动，间隔 " + com.tencent.qqlitepro.util.Config.mailPollMs(this.val$app) + "ms");
            while (com.tencent.qqlitepro.mail.QqMailManager.sPollRunning && this.val$pollGen == com.tencent.qqlitepro.mail.QqMailManager.sPollGen) {
                try {
                    android.content.Context contextAppContext = com.tencent.qqlitepro.mail.QqMailManager.appContext();
                    if (contextAppContext == null || !com.tencent.qqlitepro.util.Config.mailVerified(contextAppContext) || com.tencent.qqlitepro.util.Config.mailAddr(contextAppContext).isEmpty()) {
                        java.lang.Thread.sleep(3000L);
                    } else {
                        com.tencent.qqlitepro.mail.QqMailManager.ensureIdleAlive(contextAppContext);
                        try {
                            int iPollInbox = com.tencent.qqlitepro.mail.QqMailManager.pollInbox(contextAppContext);
                            if (iPollInbox > 0) {
                                com.tencent.qqlitepro.mail.QqMailManager.notifyNewMail(contextAppContext, iPollInbox);
                            }
                        } catch (java.lang.Throwable thRethrow10) {
                            com.tencent.qqlitepro.util.Log.w("QqMail 轮询异常: " + thRethrow10);
                        }
                        try {
                            long jMailPollMs = com.tencent.qqlitepro.util.Config.mailPollMs(this.val$app);
                            if (jMailPollMs < 5000) {
                                jMailPollMs = 30000;
                            }
                            if (com.tencent.qqlitepro.mail.QqMailManager.isIdleHealthy()) {
                                jMailPollMs = java.lang.Math.max(jMailPollMs, com.tencent.qqlitepro.mail.QqMailManager.IDLE_OK_POLL_MS);
                            }
                            java.lang.Thread.sleep(jMailPollMs);
                        } catch (java.lang.InterruptedException unused) {
                        }
                    }
                } catch (java.lang.Throwable unused2) {
                }
            }
            com.tencent.qqlitepro.util.Log.i("QqMail 轮询线程退出");
        }
    }

    public static void stopPolling() {
        sPollRunning = false;
        sPollGen++;
        java.lang.Thread thread = sPollThread;
        if (thread != null && thread.isAlive()) {
            thread.interrupt();
        }
        sPollThread = null;
        stopIdle();
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.mail.QqMailManager$11, reason: invalid class name */
    static class AnonymousClass11 implements java.lang.Runnable {
        final /* synthetic */ android.content.Context val$ctx;
        final /* synthetic */ int val$n;

        AnonymousClass11(android.content.Context context, int i) {
            this.val$ctx = context;
            this.val$n = i;
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                com.tencent.qqlitepro.hook.ChatCardInjector.refreshChatList();
            } catch (java.lang.Throwable unused) {
            }
            android.content.Context context = this.val$ctx;
            android.content.Context applicationContext = context != null ? context.getApplicationContext() : com.tencent.qqlitepro.mail.QqMailManager.appContext();
            if (applicationContext == null || com.tencent.qqlitepro.util.Config.mailDnd(applicationContext)) {
                return;
            }
            try {
                com.tencent.qqlitepro.mail.QqMailManager.showSystemNotification(applicationContext, this.val$n);
            } catch (java.lang.Throwable unused2) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void notifyNewMail(android.content.Context context, int i) {
        try {
            new android.os.Handler(android.os.Looper.getMainLooper()).post(new com.tencent.qqlitepro.mail.QqMailManager.AnonymousClass11(context, i));
        } catch (java.lang.Throwable unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void showSystemNotification(android.content.Context context, int i) {
        android.app.Notification.Builder builder;
        java.lang.String str = "收到 " + i + " 封新邮件";
        try {
            java.lang.String strMailLatestSummary = com.tencent.qqlitepro.util.Config.mailLatestSummary(context);
            if (strMailLatestSummary != null && !strMailLatestSummary.isEmpty()) {
                java.lang.String strTrim = strMailLatestSummary.replaceAll("[\\r\\n\\t]+", " ").trim();
                if (strTrim.length() > 50) {
                    strTrim = strTrim.substring(0, 50) + "…";
                }
                str = str + "：" + strTrim;
            }
        } catch (java.lang.Throwable unused) {
        }
        android.app.NotificationManager notificationManager = (android.app.NotificationManager) context.getSystemService("notification");
        if (notificationManager == null) {
            return;
        }
        if (android.os.Build.VERSION.SDK_INT >= 26) {
            android.app.NotificationChannel notificationChannelM = new android.app.NotificationChannel("qqlite_mail_new", "QQ 邮箱新邮件", 4);
            notificationChannelM.setDescription("收到新邮件时提醒");
            notificationManager.createNotificationChannel(notificationChannelM);
        }
        if (android.os.Build.VERSION.SDK_INT >= 26) {
            builder = new android.app.Notification.Builder(context, "qqlite_mail_new");
        } else {
            builder = new android.app.Notification.Builder(context);
        }
        builder.setSmallIcon(android.R.drawable.ic_dialog_email).setContentTitle("QQ 邮箱").setContentText(str).setAutoCancel(true).setPriority(1);
        try {
            android.content.Intent intent = new android.content.Intent();
            intent.setClassName(context, "moye.wearqq.SettingsActivity");
            intent.addFlags(268435456);
            com.tencent.qqlitepro.hook.SettingsInjector.pendingMailInbox();
            builder.setContentIntent(android.app.PendingIntent.getActivity(context, 0, intent, 201326592));
        } catch (java.lang.Throwable unused2) {
        }
        notificationManager.notify(9527, builder.build());
    }

    private static java.util.List<com.google.gson.JsonObject> parseCache(java.lang.String str) {
        java.util.ArrayList arrayList = new java.util.ArrayList();
        if (str != null && !str.isEmpty()) {
            try {
                com.google.gson.JsonArray jsonArray = (com.google.gson.JsonArray) new com.google.gson.Gson().fromJson(str, com.google.gson.JsonArray.class);
                if (jsonArray != null) {
                    for (int i = 0; i < jsonArray.size(); i++) {
                        arrayList.add(jsonArray.get(i).getAsJsonObject());
                    }
                }
            } catch (java.lang.Throwable unused) {
            }
        }
        return arrayList;
    }

    private static java.lang.String cacheToJson(java.util.List<com.google.gson.JsonObject> list) {
        com.google.gson.JsonArray jsonArray = new com.google.gson.JsonArray();
        java.util.Iterator<com.google.gson.JsonObject> it = list.iterator();
        while (it.hasNext()) {
            jsonArray.add(it.next());
        }
        return jsonArray.toString();
    }

    private static void removeByUid(java.util.List<com.google.gson.JsonObject> list, long j) {
        for (int size = list.size() - 1; size >= 0; size--) {
            com.google.gson.JsonObject jsonObject = list.get(size);
            try {
                if (jsonObject.has("uid") && jsonObject.get("uid").getAsLong() == j) {
                    list.remove(size);
                }
            } catch (java.lang.Throwable unused) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    private static java.util.Properties imapProps() {
        java.util.Properties properties = new java.util.Properties();
        properties.put("mail.imap.host", IMAP_HOST);
        properties.put("mail.imap.port", java.lang.String.valueOf(IMAP_PORT));
        properties.put("mail.imap.ssl.enable", "true");
        properties.put("mail.imap.ssl.trust", "*");
        properties.put("mail.imap.connectiontimeout", "30000");
        properties.put("mail.imap.timeout", "120000");
        return properties;
    }

    private static java.util.Properties smtpProps(java.lang.String str, java.lang.String str2) {
        java.util.Properties properties = new java.util.Properties();
        properties.put("mail.smtp.host", SMTP_HOST);
        properties.put("mail.smtp.port", java.lang.String.valueOf(SMTP_PORT));
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.ssl.trust", "*");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.connectiontimeout", "30000");
        properties.put("mail.smtp.timeout", "120000");
        properties.put("mail.smtp.writetimeout", "120000");
        properties.put("mail.smtp.quitwait", "false");
        properties.put("mail.debug", "false");
        return properties;
    }

    private static long parseLongSafe(java.lang.String str, long j) {
        if (str == null) {
            return j;
        }
        try {
            return java.lang.Long.parseLong(str.trim());
        } catch (java.lang.Throwable unused) {
            return j;
        }
    }

    private static java.lang.String fmt(long j) {
        return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault()).format(new java.util.Date(j));
    }

    private static java.lang.String errorText(java.lang.Throwable thRethrow10) {
        java.lang.String message = thRethrow10.getMessage();
        if (message == null || message.isEmpty()) {
            message = thRethrow10.getClass().getSimpleName();
        }
        java.lang.String strRootCauseText = rootCauseText(thRethrow10);
        if (!strRootCauseText.isEmpty() && !message.toLowerCase(java.util.Locale.ROOT).contains(strRootCauseText.toLowerCase(java.util.Locale.ROOT))) {
            message = message + "（根因：" + strRootCauseText + "）";
        }
        java.lang.String lowerCase = message.toLowerCase(java.util.Locale.ROOT);
        if (lowerCase.contains("535") || lowerCase.contains("authentication failed") || lowerCase.contains("auth")) {
            return "认证失败（" + message + "）——请检查授权码是否正确、是否已开启 IMAP/SMTP 服务";
        }
        if (lowerCase.contains("connection refused") || lowerCase.contains("connect timed out") || lowerCase.contains("timeout")) {
            return "连接超时/被拒（" + message + "）——请检查网络";
        }
        if (!lowerCase.contains("hostname") && !lowerCase.contains("ssl") && !lowerCase.contains("certificate")) {
            return message;
        }
        return "SSL/证书异常（" + message + "）";
    }

    private static java.lang.String rootCauseText(java.lang.Throwable thRethrow10) {
        int i = 0;
        java.lang.Throwable cause = new java.lang.RuntimeException();
        while (cause.getCause() != null && cause.getCause() != cause) {
            int i2 = i + 1;
            if (i >= 10) {
                break;
            }
            cause = cause.getCause();
            i = i2;
        }
        java.lang.String str = "";
        if (cause == thRethrow10) {
            return "";
        }
        java.lang.String message = cause.getMessage();
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        sb.append(cause.getClass().getSimpleName());
        if (message != null && !message.isEmpty()) {
            str = ": " + message;
        }
        sb.append(str);
        return sb.toString();
    }


}
