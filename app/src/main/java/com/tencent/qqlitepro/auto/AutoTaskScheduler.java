package com.tencent.qqlitepro.auto;

/* JADX INFO: loaded from: classes.dex */
public final class AutoTaskScheduler {
    private static volatile boolean started = false;

    static final class Rule {
        final java.lang.String content;
        final java.util.List<java.lang.String[]> peers;
        final boolean replyAt;
        final java.lang.String time;

        Rule(java.lang.String str, java.lang.String str2, java.util.List<java.lang.String[]> list, boolean z) {
            this.time = str;
            this.content = str2;
            this.peers = list;
            this.replyAt = z;
        }
    }

    private AutoTaskScheduler() {
    }

    public static synchronized void start() {
        if (started) {
            return;
        }
        java.lang.Thread thread = new java.lang.Thread(new com.tencent.qqlitepro.auto.AutoTaskScheduler$$ExternalSyntheticLambda0(), "antirecall-auto-task");
        thread.setDaemon(true);
        thread.start();
        started = true;
        com.tencent.qqlitepro.util.Log.i("定时消息调度器已启动");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void looper() {
        while (true) {
            try {
                java.lang.Thread.sleep(30000L);
                try {
                    android.content.Context contextAppContext = com.tencent.qqlitepro.cache.MsgCache.appContext();
                    if (contextAppContext != null) {
                        checkAndSend(contextAppContext);
                    }
                } catch (java.lang.Throwable th) {
                    com.tencent.qqlitepro.util.Log.w("定时消息检查异常: " + th);
                }
            } catch (java.lang.InterruptedException unused) {
                return;
            }
        }
    }

    private static void checkAndSend(android.content.Context context) {
        if (com.tencent.qqlitepro.util.Config.autoTaskEnabled(context)) {
            java.lang.String str = new java.text.SimpleDateFormat("HH:mm", java.util.Locale.US).format(new java.util.Date());
            java.lang.String str2 = new java.text.SimpleDateFormat("yyyyMMdd", java.util.Locale.US).format(new java.util.Date());
            java.util.List<com.tencent.qqlitepro.auto.AutoTaskScheduler.Rule> rules = parseRules(com.tencent.qqlitepro.util.Config.autoTaskRules(context));
            if (!rules.isEmpty()) {
                java.util.HashMap map = new java.util.HashMap();
                for (com.tencent.qqlitepro.auto.AutoTaskScheduler.Rule rule : rules) {
                    java.lang.Integer num = (java.lang.Integer) map.get(rule.time);
                    map.put(rule.time, java.lang.Integer.valueOf(num == null ? 1 : num.intValue() + 1));
                }
                java.util.HashMap map2 = new java.util.HashMap();
                for (com.tencent.qqlitepro.auto.AutoTaskScheduler.Rule rule2 : rules) {
                    try {
                        if (rule2.time.equals(str) && rule2.content != null && !rule2.content.trim().isEmpty() && !rule2.peers.isEmpty()) {
                            java.lang.Integer num2 = (java.lang.Integer) map2.get(rule2.time);
                            int iIntValue = num2 == null ? 0 : num2.intValue();
                            map2.put(rule2.time, java.lang.Integer.valueOf(iIntValue + 1));
                            java.lang.String str3 = rule2.time;
                            if (map.get(rule2.time) != null && ((java.lang.Integer) map.get(rule2.time)).intValue() > 1) {
                                str3 = rule2.time + "#" + iIntValue;
                            }
                            if (!str2.equals(com.tencent.qqlitepro.util.Config.autoTaskRuleLastSentDay(context, str3))) {
                                if (sendToPeers(context, rule2.peers, rule2.content, rule2.replyAt)) {
                                    com.tencent.qqlitepro.util.Config.markAutoTaskRuleSent(context, str3, str2);
                                    com.tencent.qqlitepro.util.Log.i("定时消息已发送 [" + rule2.time + "] -> " + rule2.peers.size() + " 个会话: " + rule2.content);
                                } else {
                                    com.tencent.qqlitepro.util.Log.w("定时消息 [" + rule2.time + "] 有目标发送失败，今日不标记(稍后重试)");
                                }
                            }
                        }
                    } catch (java.lang.Throwable th) {
                        com.tencent.qqlitepro.util.Log.w("定时规则执行异常: " + th);
                    }
                }
                return;
            }
            java.lang.String strAutoContent = com.tencent.qqlitepro.util.Config.autoContent(context);
            java.lang.String strAutoTime = com.tencent.qqlitepro.util.Config.autoTime(context);
            if (strAutoContent == null || strAutoContent.trim().isEmpty()) {
                return;
            }
            java.util.List<java.lang.String[]> peers = parsePeers(com.tencent.qqlitepro.util.Config.autoPeers(context));
            if (peers.isEmpty()) {
                int iAutoChatType = com.tencent.qqlitepro.util.Config.autoChatType(context);
                java.lang.String strAutoPeerUid = com.tencent.qqlitepro.util.Config.autoPeerUid(context);
                if (strAutoPeerUid == null || strAutoPeerUid.trim().isEmpty()) {
                    com.tencent.qqlitepro.util.Log.i("定时消息未配置目标会话，跳过");
                    return;
                } else {
                    peers.add(new java.lang.String[]{"2", strAutoPeerUid.trim()});
                    if (iAutoChatType == 1) {
                        peers.set(0, new java.lang.String[]{"1", strAutoPeerUid.trim()});
                    }
                }
            }
            if (normalizeTime(strAutoTime).equals(str) && !str2.equals(com.tencent.qqlitepro.util.Config.autoLastSentDay(context))) {
                if (sendToPeers(context, peers, strAutoContent, false)) {
                    com.tencent.qqlitepro.util.Config.markAutoSent(context, str2);
                    com.tencent.qqlitepro.util.Log.i("定时消息(旧版配置)已发送 -> " + strAutoContent);
                    return;
                }
                com.tencent.qqlitepro.util.Log.w("定时消息(旧版配置)有目标发送失败，今日不标记(稍后重试)");
            }
        }
    }

    private static boolean sendToPeers(android.content.Context context, java.util.List<java.lang.String[]> list, java.lang.String str, boolean z) {
        boolean z2 = true;
        for (java.lang.String[] strArr : list) {
            try {
                int i = java.lang.Integer.parseInt(strArr[0]);
                if (!send(context, i, strArr[1], str, z)) {
                    com.tencent.qqlitepro.util.Log.w("定时消息发送失败: " + i + "/" + strArr[1]);
                    z2 = false;
                }
            } catch (java.lang.Throwable th) {
                com.tencent.qqlitepro.util.Log.w("定时消息目标解析失败: " + th);
            }
        }
        return z2;
    }

    static java.util.List<com.tencent.qqlitepro.auto.AutoTaskScheduler.Rule> parseRules(java.lang.String str) {
        java.util.List arrayList;
        java.util.ArrayList arrayList2 = new java.util.ArrayList();
        if (str != null && !str.trim().isEmpty()) {
            for (java.lang.String str2 : str.split("\n")) {
                if (str2 != null && !str2.isEmpty()) {
                    java.lang.String[] strArrSplit = str2.split("\u0001", 4);
                    if (strArrSplit.length >= 2) {
                        java.lang.String strNormalizeTime = normalizeTime(strArrSplit[0]);
                        java.lang.String strUnescapeField = com.tencent.qqlitepro.util.Config.unescapeField(strArrSplit[1]);
                        if (strArrSplit.length >= 3) {
                            arrayList = parsePeers(com.tencent.qqlitepro.util.Config.unescapeField(strArrSplit[2]));
                        } else {
                            arrayList = new java.util.ArrayList();
                        }
                        boolean z = strArrSplit.length >= 4 && "1".equals(strArrSplit[3].trim());
                        if (!strUnescapeField.trim().isEmpty()) {
                            arrayList2.add(new com.tencent.qqlitepro.auto.AutoTaskScheduler.Rule(strNormalizeTime, strUnescapeField, arrayList, z));
                        }
                    }
                }
            }
        }
        return arrayList2;
    }

    public static java.lang.String encodeRule(java.lang.String str, java.lang.String str2, java.lang.String str3) {
        return encodeRule(str, str2, str3, false);
    }

    public static java.lang.String encodeRule(java.lang.String str, java.lang.String str2, java.lang.String str3, boolean z) {
        java.lang.String strNormalizeTime = normalizeTime(str);
        java.lang.String strEscapeField = com.tencent.qqlitepro.util.Config.escapeField(str2 == null ? "" : str2.trim());
        if (str3 == null) {
            str3 = "";
        }
        java.lang.String strEscapeField2 = com.tencent.qqlitepro.util.Config.escapeField(str3);
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        sb.append(strNormalizeTime);
        sb.append("\u0001");
        sb.append(strEscapeField);
        sb.append("\u0001");
        sb.append(strEscapeField2);
        sb.append("\u0001");
        sb.append(z ? "1" : "0");
        return sb.toString();
    }

    static java.util.List<java.lang.String[]> parsePeers(java.lang.String str) {
        java.util.ArrayList arrayList = new java.util.ArrayList();
        if (str == null) {
            return arrayList;
        }
        for (java.lang.String str2 : str.split("\n")) {
            java.lang.String strTrim = str2.trim();
            if (!strTrim.isEmpty()) {
                java.lang.String[] strArrSplit = strTrim.split("\\|", 3);
                if (strArrSplit.length >= 2 && !strArrSplit[1].trim().isEmpty() && ("1".equals(strArrSplit[0]) || "2".equals(strArrSplit[0]))) {
                    arrayList.add(new java.lang.String[]{strArrSplit[0].trim(), strArrSplit[1].trim()});
                }
            }
        }
        return arrayList;
    }

    public static java.lang.String normalizeTime(java.lang.String str) {
        java.lang.String strTrim;
        int iIndexOf;
        if (str == null || (iIndexOf = (strTrim = str.trim()).indexOf(58)) < 0) {
            return "08:00";
        }
        try {
            int i = java.lang.Integer.parseInt(strTrim.substring(0, iIndexOf).trim());
            int i2 = java.lang.Integer.parseInt(strTrim.substring(iIndexOf + 1).trim());
            if (i < 0 || i > 23) {
                i = 8;
            }
            if (i2 < 0 || i2 > 59) {
                i2 = 0;
            }
            return java.lang.String.format(java.util.Locale.US, "%02d:%02d", java.lang.Integer.valueOf(i), java.lang.Integer.valueOf(i2));
        } catch (java.lang.Throwable unused) {
            return "08:00";
        }
    }

    public static boolean send(android.content.Context context, int i, java.lang.String str, java.lang.String str2, boolean z) {
        try {
            return sendElements(context, i, str, com.tencent.qqlitepro.http.QqKernel.buildElementsWithAt(str2, z, null, null));
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("定时消息发送异常: " + th);
            return false;
        }
    }

    public static boolean sendElements(android.content.Context context, int i, java.lang.String str, java.util.ArrayList<java.lang.Object> arrayList) {
        com.tencent.qqlitepro.http.QqKernel.SendResult sendResultSendMsg;
        if (arrayList != null) {
            try {
                return (arrayList.isEmpty() || (sendResultSendMsg = com.tencent.qqlitepro.http.QqKernel.sendMsg(com.tencent.qqlitepro.http.QqKernel.createContact(i, str), arrayList, 5000L)) == null || sendResultSendMsg.code != 0) ? false : true;
            } catch (java.lang.Throwable th) {
                com.tencent.qqlitepro.util.Log.w("消息发送异常: " + th);
            }
        }
        return false;
    }
}
