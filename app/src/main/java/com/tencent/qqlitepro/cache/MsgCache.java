package com.tencent.qqlitepro.cache;

/* JADX INFO: loaded from: classes.dex */
public final class MsgCache {
    private static final int MAX = 2000;
    private static final long PERSIST_INTERVAL_MS = 20000;
    private static final java.util.Set<java.lang.String> TEXT_KEYS = new java.util.HashSet(java.util.Arrays.asList("content", "text", "contentText", "summary", "description", "title", "fileName"));
    private static volatile com.tencent.qqlitepro.cache.MsgCache sInstance;
    private final com.google.gson.Gson gson = new com.google.gson.GsonBuilder().disableHtmlEscaping().create();
    private final java.util.Map<java.lang.String, com.tencent.qqlitepro.cache.CachedMsg> mem = new java.util.concurrent.ConcurrentHashMap();
    private final java.util.Map<java.lang.String, java.lang.Boolean> recalled = new java.util.concurrent.ConcurrentHashMap();
    private final java.util.Map<java.lang.String, java.util.List<?>> origElements = new java.util.concurrent.ConcurrentHashMap();
    private final java.util.Map<java.lang.String, java.util.ArrayDeque<com.tencent.qqlitepro.cache.CachedMsg>> peerQueue = new java.util.concurrent.ConcurrentHashMap();
    private final java.util.Map<java.lang.String, java.lang.String> tipContent = new java.util.concurrent.ConcurrentHashMap();
    private volatile long lastPersist = 0;
    private volatile boolean dirty = false;

    private MsgCache() {
    }

    public static com.tencent.qqlitepro.cache.MsgCache get() {
        if (sInstance != null) {
            return sInstance;
        }
        synchronized (com.tencent.qqlitepro.cache.MsgCache.class) {
            if (sInstance != null) {
                return sInstance;
            }
            sInstance = new com.tencent.qqlitepro.cache.MsgCache();
            return sInstance;
        }
    }

    private void maybePersist() {
        if (this.dirty) {
            long jCurrentTimeMillis = java.lang.System.currentTimeMillis();
            if (jCurrentTimeMillis - this.lastPersist < PERSIST_INTERVAL_MS) {
                return;
            }
            this.lastPersist = jCurrentTimeMillis;
            this.dirty = false;
            try {
                java.lang.Thread thread = new java.lang.Thread(new com.tencent.qqlitepro.cache.MsgCache$$ExternalSyntheticLambda2(this), "antirecall-persist");
                thread.setDaemon(true);
                thread.start();
            } catch (java.lang.Throwable unused) {
                persist();
            }
        }
    }

    public boolean cache(android.content.Context context, java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.Object obj, java.util.List<?> list) {
        if (str == null || list == null) {
            return false;
        }
        if (this.mem.containsKey(str) && this.origElements.containsKey(str)) {
            return false;
        }
        rememberLoaderFrom(list, obj);
        com.tencent.qqlitepro.cache.CachedMsg cachedMsg = new com.tencent.qqlitepro.cache.CachedMsg();
        cachedMsg.msgId = str;
        cachedMsg.peerUid = str2;
        cachedMsg.senderUid = str3;
        cachedMsg.summary = summarize(context, list);
        cachedMsg.elementTypes = collectTypes(context, list);
        cachedMsg.timestamp = java.lang.System.currentTimeMillis();
        cachedMsg.msgType = readMsgType(obj);
        this.mem.put(str, cachedMsg);
        this.recalled.put(str, false);
        java.util.List<?> listDeepCopy = com.tencent.qqlitepro.cache.ElementReplicator.deepCopy(list);
        if (listDeepCopy != null && !listDeepCopy.isEmpty()) {
            this.origElements.put(str, listDeepCopy);
            cachedMsg.elementsJson = com.tencent.qqlitepro.cache.ElementReplicator.toTreeJson(list);
        }
        trim();
        this.dirty = true;
        maybePersist();
        return true;
    }

    private void rememberLoaderFrom(java.util.List<?> list, java.lang.Object obj) {
        java.lang.ClassLoader classLoader = obj == null ? null : obj.getClass().getClassLoader();
        if (classLoader == null && list != null && !list.isEmpty()) {
            classLoader = list.get(0).getClass().getClassLoader();
        }
        com.tencent.qqlitepro.cache.ElementReplicator.rememberLoader(classLoader);
    }

    public void feedTip(java.lang.String str) {
        com.tencent.qqlitepro.cache.CachedMsg cachedMsg;
        if (str == null || (cachedMsg = this.mem.get(str)) == null) {
            return;
        }
        if (cachedMsg.summary != null && !cachedMsg.summary.isEmpty()) {
            this.tipContent.put(str, cachedMsg.summary);
        }
        ((java.util.ArrayDeque) this.peerQueue.computeIfAbsent(cachedMsg.peerUid, new com.tencent.qqlitepro.cache.MsgCache$$ExternalSyntheticLambda5())).addLast(cachedMsg);
    }

    static /* synthetic */ java.util.ArrayDeque lambda$feedTip$0(java.lang.String str) {
        return new java.util.ArrayDeque();
    }

    public void markRecalled(java.lang.String str) {
        if (str == null) {
            return;
        }
        boolean zEquals = java.lang.Boolean.TRUE.equals(this.recalled.get(str));
        com.tencent.qqlitepro.cache.CachedMsg cachedMsg = this.mem.get(str);
        if (cachedMsg != null) {
            cachedMsg.recalled = true;
        }
        if (cachedMsg == null && containsKeyElsewhere(str)) {
            this.recalled.put(str, true);
        }
        this.recalled.put(str, true);
        if (cachedMsg != null) {
            if (cachedMsg.summary != null && !cachedMsg.summary.isEmpty()) {
                this.tipContent.put(str, cachedMsg.summary);
            }
            if (!zEquals) {
                ((java.util.ArrayDeque) this.peerQueue.computeIfAbsent(cachedMsg.peerUid, new com.tencent.qqlitepro.cache.MsgCache$$ExternalSyntheticLambda6())).addLast(cachedMsg);
            }
        }
        this.dirty = true;
        maybePersist();
        com.tencent.qqlitepro.util.Log.i("标记为已撤且留存: " + str);
    }

    static /* synthetic */ java.util.ArrayDeque lambda$markRecalled$1(java.lang.String str) {
        return new java.util.ArrayDeque();
    }

    public java.lang.String takeRecalledSummary(java.lang.String str) {
        java.util.ArrayDeque<com.tencent.qqlitepro.cache.CachedMsg> arrayDeque;
        if (str == null || (arrayDeque = this.peerQueue.get(str)) == null) {
            return null;
        }
        com.tencent.qqlitepro.cache.CachedMsg cachedMsgPollFirst = arrayDeque.pollFirst();
        if (arrayDeque.isEmpty()) {
            this.peerQueue.remove(str);
        }
        if (cachedMsgPollFirst == null) {
            return null;
        }
        return cachedMsgPollFirst.summary;
    }

    public java.lang.String takeForTip(java.lang.String str, java.lang.String str2) {
        java.lang.String str3;
        if (str2 != null && (str3 = this.tipContent.get(str2)) != null) {
            return str3;
        }
        java.lang.String strTakeRecalledSummary = takeRecalledSummary(str);
        if (strTakeRecalledSummary != null && str2 != null) {
            if (this.tipContent.size() > MAX) {
                this.tipContent.remove(this.tipContent.keySet().iterator().next());
            }
            this.tipContent.put(str2, strTakeRecalledSummary);
        }
        return strTakeRecalledSummary;
    }

    public boolean exists(java.lang.String str) {
        return str != null && (this.mem.containsKey(str) || java.lang.Boolean.TRUE.equals(this.recalled.get(str)));
    }

    public boolean isRecallRecord(java.lang.Object obj) {
        java.lang.Object field = getField(obj, "recallTime");
        return (field instanceof java.lang.Number) && ((java.lang.Number) field).longValue() > 0;
    }

    public boolean restoreInto(java.lang.Object obj) {
        java.lang.String strStrField;
        int iInferMsgType = 0;
        if (obj == null || (strStrField = strField(obj, "msgId")) == null) {
            return false;
        }
        rememberLoaderFrom(null, obj);
        java.util.List<?> listFreshElementsFromTree = this.origElements.get(strStrField);
        if (listFreshElementsFromTree == null || listFreshElementsFromTree.isEmpty()) {
            listFreshElementsFromTree = freshElementsFromTree(strStrField);
        }
        java.lang.String strMemSummary = (listFreshElementsFromTree == null || listFreshElementsFromTree.isEmpty()) ? memSummary(strStrField) : summarize(getAppContext(), listFreshElementsFromTree);
        if (strMemSummary == null || strMemSummary.isEmpty()) {
            strMemSummary = memSummary(strStrField);
        }
        if ((listFreshElementsFromTree == null || listFreshElementsFromTree.isEmpty()) && (strMemSummary == null || strMemSummary.isEmpty())) {
            com.tencent.qqlitepro.util.Log.w("还原放弃：msgId=" + strStrField + " 无可见元素且无缓存摘要");
            return false;
        }
        markRecalled(strStrField);
        java.lang.StringBuilder sb = new java.lang.StringBuilder("还原被撤回消息 msgId=");
        sb.append(strStrField);
        sb.append(" 元素可重建=");
        sb.append((listFreshElementsFromTree == null || listFreshElementsFromTree.isEmpty()) ? false : true);
        sb.append(" 摘要=\"");
        sb.append(strMemSummary);
        sb.append("\"");
        com.tencent.qqlitepro.util.Log.i(sb.toString());
        if (listFreshElementsFromTree == null || listFreshElementsFromTree.isEmpty() || strMemSummary == null || strMemSummary.isEmpty()) {
            com.tencent.qqlitepro.util.Log.i("走灰条注入路径 msgId=" + strStrField + "（无真实元素可写回）");
            return true;
        }
        try {
            java.util.List<?> listDeepCopy = com.tencent.qqlitepro.cache.ElementReplicator.deepCopy(listFreshElementsFromTree);
            if (listDeepCopy != null && !listDeepCopy.isEmpty()) {
                setField(obj, "elements", listDeepCopy);
                setField(obj, "recallTime", 0L);
                if (this.mem.get(strStrField) != null) {
                    iInferMsgType = this.mem.get(strStrField).msgType;
                }
                if (iInferMsgType <= 0) {
                    iInferMsgType = inferMsgType(listFreshElementsFromTree);
                }
                if (iInferMsgType <= 0) {
                    iInferMsgType = 2;
                }
                setField(obj, "msgType", java.lang.Integer.valueOf(iInferMsgType));
                com.tencent.qqlitepro.util.Log.i("已写回真实元素并清除撤回状态（msgType=" + iInferMsgType + "） msgId=" + strStrField);
            }
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("写回元素失败: " + th);
        }
        return true;
    }

    private int readMsgType(java.lang.Object obj) {
        if (obj == null) {
            return 0;
        }
        java.lang.Object field = getField(obj, "msgType");
        if (field instanceof java.lang.Number) {
            return ((java.lang.Number) field).intValue();
        }
        return 0;
    }

    private int inferMsgType(java.util.List<?> list) {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        java.lang.Object field = getField(list.get(0), "elementType");
        if (!(field instanceof java.lang.Number)) {
            return 2;
        }
        int iIntValue = ((java.lang.Number) field).intValue();
        if (iIntValue == 3) {
            return 3;
        }
        if (iIntValue == 4) {
            return 7;
        }
        if (iIntValue != 5) {
            return iIntValue != 24 ? 2 : 24;
        }
        return 6;
    }

    private java.util.List<?> freshElementsFromTree(java.lang.String str) {
        com.tencent.qqlitepro.cache.CachedMsg cachedMsg = this.mem.get(str);
        if (cachedMsg != null && cachedMsg.elementsJson != null && !cachedMsg.elementsJson.isEmpty()) {
            try {
                java.util.List<?> listFromTreeJson = com.tencent.qqlitepro.cache.ElementReplicator.fromTreeJson(cachedMsg.elementsJson);
                if (listFromTreeJson != null && !listFromTreeJson.isEmpty()) {
                    this.origElements.put(str, listFromTreeJson);
                    return listFromTreeJson;
                }
            } catch (java.lang.Throwable th) {
                com.tencent.qqlitepro.util.Log.w("重建元素失败 msgId=" + str + ": " + th);
            }
        }
        return null;
    }

    private java.lang.String memSummary(java.lang.String str) {
        com.tencent.qqlitepro.cache.CachedMsg cachedMsg = this.mem.get(str);
        if (cachedMsg == null) {
            return null;
        }
        return cachedMsg.summary;
    }

    private java.lang.Object getField(java.lang.Object obj, java.lang.String str) {
        for (java.lang.Class<?> superclass = obj == null ? null : obj.getClass(); superclass != null && superclass != java.lang.Object.class; superclass = superclass.getSuperclass()) {
            try {
                java.lang.reflect.Field declaredField = superclass.getDeclaredField(str);
                declaredField.setAccessible(true);
                return declaredField.get(obj);
            } catch (java.lang.Throwable unused) {
            }
        }
        return null;
    }

    private boolean setField(java.lang.Object obj, java.lang.String str, java.lang.Object obj2) {
        for (java.lang.Class<?> superclass = obj == null ? null : obj.getClass(); superclass != null && superclass != java.lang.Object.class; superclass = superclass.getSuperclass()) {
            try {
                java.lang.reflect.Field declaredField = superclass.getDeclaredField(str);
                declaredField.setAccessible(true);
                declaredField.set(obj, obj2);
                return true;
            } catch (java.lang.Throwable unused) {
            }
        }
        return false;
    }

    private java.lang.String strField(java.lang.Object obj, java.lang.String str) {
        java.lang.Object field = getField(obj, str);
        if (field == null) {
            return null;
        }
        return java.lang.String.valueOf(field);
    }

    public com.tencent.qqlitepro.cache.CachedMsg get(java.lang.String str) {
        if (str == null) {
            return null;
        }
        return this.mem.get(str);
    }

    public boolean isRecalled(java.lang.String str) {
        if (str == null) {
            return false;
        }
        return java.lang.Boolean.TRUE.equals(this.recalled.get(str)) || containsKeyElsewhere(str);
    }

    public boolean isSelfMessage(java.lang.String str) {
        java.lang.String strSelfUid;
        try {
            com.tencent.qqlitepro.cache.CachedMsg cachedMsg = this.mem.get(str);
            return (cachedMsg == null || cachedMsg.senderUid == null || cachedMsg.senderUid.isEmpty() || (strSelfUid = com.tencent.qqlitepro.http.QqKernel.selfUid()) == null || !strSelfUid.equals(cachedMsg.senderUid)) ? false : true;
        } catch (java.lang.Throwable unused) {
            return false;
        }
    }

    public java.lang.String totalSummary(java.lang.Object obj, java.util.List<?> list) {
        return summarize(null, list);
    }

    private boolean containsKeyElsewhere(java.lang.String str) {
        return java.lang.Boolean.TRUE.equals(this.recalled.get(str));
    }

    private void trim() {
        if (this.mem.size() <= MAX) {
            return;
        }
        java.util.ArrayList arrayList = new java.util.ArrayList(this.mem.values());
        arrayList.sort(new com.tencent.qqlitepro.cache.MsgCache$$ExternalSyntheticLambda3());
        for (int i = 0; i < arrayList.size() - MAX; i++) {
            this.mem.remove(((com.tencent.qqlitepro.cache.CachedMsg) arrayList.get(i)).msgId);
        }
    }

    private java.lang.String summarize(android.content.Context context, java.util.List<?> list) {
        if (list == null || list.isEmpty()) {
            return "";
        }
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        for (java.lang.Object obj : list) {
            int length = sb.length();
            extractTexts(obj, sb);
            if (sb.length() == length) {
                sb.append(placeholderFor(obj));
            }
        }
        return sb.toString().trim();
    }

    private java.lang.String placeholderFor(java.lang.Object obj) {
        if (obj == null) {
            return "";
        }
        if (getField(obj, "faceElement") != null || getField(obj, "marketFaceElement") != null) {
            return "[表情]";
        }
        if (getField(obj, "picElement") != null) {
            return "[图片]";
        }
        if (getField(obj, "videoElement") != null) {
            return "[视频]";
        }
        if (getField(obj, "fileElement") != null) {
            return "[文件]";
        }
        if (getField(obj, "pttElement") != null) {
            return "[语音]";
        }
        if (getField(obj, "multiForwardMsgElement") != null) {
            return "[聊天记录]";
        }
        if (getField(obj, "arkElement") != null || getField(obj, "nowH5Element") != null) {
            return "[卡片]";
        }
        java.lang.Object field = getField(obj, "elementType");
        if (!(field instanceof java.lang.Number)) {
            return "[消息]";
        }
        int iIntValue = ((java.lang.Number) field).intValue();
        if (iIntValue != 2) {
            if (iIntValue == 3) {
                return "[图片]";
            }
            if (iIntValue == 4) {
                return "[视频]";
            }
            if (iIntValue == 5) {
                return "[语音]";
            }
            if (iIntValue == 6) {
                return "[文件]";
            }
            if (iIntValue != 14) {
                return (iIntValue == 24 || iIntValue == 26) ? "[聊天记录]" : "[消息]";
            }
        }
        return "[表情]";
    }

    private void extractTexts(java.lang.Object obj, java.lang.StringBuilder sb) {
        extractTexts(obj, sb, 0, new java.util.IdentityHashMap<>());
    }

    private void extractTexts(java.lang.Object obj, java.lang.StringBuilder sb, int i, java.util.IdentityHashMap<java.lang.Object, java.lang.Boolean> identityHashMap) {
        if (obj == null || i > 4 || identityHashMap.containsKey(obj)) {
            return;
        }
        if (obj instanceof java.lang.CharSequence) {
            java.lang.String strTrim = obj.toString().trim();
            if (strTrim.isEmpty()) {
                return;
            }
            if (sb.length() > 0) {
                sb.append(' ');
            }
            sb.append(strTrim);
            return;
        }
        if (obj instanceof java.lang.Iterable) {
            identityHashMap.put(obj, java.lang.Boolean.TRUE);
            java.util.Iterator it = ((java.lang.Iterable) obj).iterator();
            while (it.hasNext()) {
                extractTexts(it.next(), sb, i + 1, identityHashMap);
            }
            return;
        }
        int i2 = 0;
        if (obj.getClass().isArray()) {
            identityHashMap.put(obj, java.lang.Boolean.TRUE);
            int length = java.lang.reflect.Array.getLength(obj);
            while (i2 < length) {
                extractTexts(java.lang.reflect.Array.get(obj, i2), sb, i + 1, identityHashMap);
                i2++;
            }
            return;
        }
        if ((obj instanceof java.lang.Number) || (obj instanceof java.lang.Boolean) || (obj instanceof java.lang.Character)) {
            return;
        }
        identityHashMap.put(obj, java.lang.Boolean.TRUE);
        try {
            java.lang.reflect.Field[] declaredFields = obj.getClass().getDeclaredFields();
            int length2 = declaredFields.length;
            while (i2 < length2) {
                java.lang.reflect.Field field = declaredFields[i2];
                if (!field.isSynthetic()) {
                    try {
                        field.setAccessible(true);
                        java.lang.String name = field.getName();
                        if (TEXT_KEYS.contains(name)) {
                            extractTexts(field.get(obj), sb, i + 1, identityHashMap);
                        } else if (i < 3 && (name.endsWith("Element") || name.endsWith("element"))) {
                            extractTexts(field.get(obj), sb, i + 1, identityHashMap);
                        }
                    } catch (java.lang.Throwable unused) {
                    }
                }
                i2++;
            }
        } catch (java.lang.Throwable unused2) {
        }
    }

    private java.lang.String collectTypes(android.content.Context context, java.util.List<?> list) {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        java.util.Iterator<?> it = list.iterator();
        while (it.hasNext()) {
            java.lang.Object objSafeField = safeField(it.next(), "elementType");
            if (objSafeField instanceof java.lang.Number) {
                if (sb.length() > 0) {
                    sb.append(',');
                }
                sb.append(((java.lang.Number) objSafeField).intValue());
            }
        }
        return sb.toString();
    }

    private java.lang.Object safeField(java.lang.Object obj, java.lang.String str) {
        try {
            java.lang.reflect.Field field = obj.getClass().getField(str);
            field.setAccessible(true);
            return field.get(obj);
        } catch (java.lang.Throwable unused) {
            return null;
        }
    }

    private boolean hasField(java.lang.Object obj, java.lang.String str) {
        return safeField(obj, str) != null;
    }

    private java.lang.String safeStringField(java.lang.Object obj, java.lang.String str) {
        java.lang.Object objSafeField;
        try {
            java.lang.Object objSafeField2 = safeField(obj, str);
            if (objSafeField2 != null && (objSafeField = safeField(objSafeField2, "content")) != null) {
                return objSafeField.toString();
            }
            return null;
        } catch (java.lang.Throwable unused) {
            return null;
        }
    }

    private java.io.File storeFile(android.content.Context context) {
        return new java.io.File(context.getFilesDir(), "antirecall_cache.json");
    }

    public void persist() {
        try {
            android.content.Context appContext = getAppContext();
            if (appContext == null) {
                return;
            }
            java.lang.String json = this.gson.toJson(new java.util.ArrayList(this.mem.values()));
            java.io.File fileStoreFile = storeFile(appContext);
            java.io.File parentFile = fileStoreFile.getParentFile();
            if (parentFile != null && !parentFile.exists()) {
                parentFile.mkdirs();
            }
            java.io.OutputStreamWriter outputStreamWriter = new java.io.OutputStreamWriter(new java.io.FileOutputStream(fileStoreFile), java.nio.charset.StandardCharsets.UTF_8);
            try {
                outputStreamWriter.write(json);
                outputStreamWriter.close();
            } catch (java.lang.Throwable th) {
                try {
                    outputStreamWriter.close();
                } catch (java.lang.Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        } catch (java.lang.Throwable th3) {
            com.tencent.qqlitepro.util.Log.w("持久化失败: " + th3);
        }
    }

    public void load(android.content.Context context) {
        try {
            java.io.File fileStoreFile = storeFile(context);
            if (fileStoreFile.exists()) {
                java.io.InputStreamReader inputStreamReader = new java.io.InputStreamReader(new java.io.FileInputStream(fileStoreFile), java.nio.charset.StandardCharsets.UTF_8);
                try {
                    java.util.List<com.tencent.qqlitepro.cache.CachedMsg> list = (java.util.List) this.gson.fromJson(inputStreamReader, new com.tencent.qqlitepro.cache.MsgCache.AnonymousClass1().getType());
                    if (list != null) {
                        for (com.tencent.qqlitepro.cache.CachedMsg cachedMsg : list) {
                            if (cachedMsg != null && cachedMsg.msgId != null) {
                                this.mem.put(cachedMsg.msgId, cachedMsg);
                                if (cachedMsg.elementsJson != null && !cachedMsg.elementsJson.isEmpty()) {
                                    try {
                                        java.util.List<?> listFromTreeJson = com.tencent.qqlitepro.cache.ElementReplicator.fromTreeJson(cachedMsg.elementsJson);
                                        if (listFromTreeJson != null && !listFromTreeJson.isEmpty()) {
                                            this.origElements.put(cachedMsg.msgId, listFromTreeJson);
                                        }
                                    } catch (java.lang.Throwable unused) {
                                    }
                                }
                                if (cachedMsg.summary != null && !cachedMsg.summary.isEmpty()) {
                                    this.tipContent.put(cachedMsg.msgId, cachedMsg.summary);
                                }
                                if (cachedMsg.recalled) {
                                    this.recalled.put(cachedMsg.msgId, true);
                                }
                            }
                        }
                        for (com.tencent.qqlitepro.cache.CachedMsg cachedMsg2 : list) {
                            if (cachedMsg2 != null && cachedMsg2.msgId != null && cachedMsg2.recalled && cachedMsg2.peerUid != null && cachedMsg2.summary != null && !cachedMsg2.summary.isEmpty()) {
                                ((java.util.ArrayDeque) this.peerQueue.computeIfAbsent(cachedMsg2.peerUid, new com.tencent.qqlitepro.cache.MsgCache$$ExternalSyntheticLambda4())).addLast(cachedMsg2);
                            }
                        }
                    }
                    inputStreamReader.close();
                } catch (java.lang.Throwable th) {
                    try {
                        inputStreamReader.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                    throw th;
                }
            }
        } catch (java.lang.Throwable th3) {
            com.tencent.qqlitepro.util.Log.w("加载缓存失败: " + th3);
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.cache.MsgCache$1, reason: invalid class name */
    class AnonymousClass1 extends com.google.gson.reflect.TypeToken<java.util.List<com.tencent.qqlitepro.cache.CachedMsg>> {
        AnonymousClass1() {
        }
    }

    static /* synthetic */ java.util.ArrayDeque lambda$load$3(java.lang.String str) {
        return new java.util.ArrayDeque();
    }

    public void clear() {
        java.io.File fileStoreFile;
        this.mem.clear();
        this.recalled.clear();
        this.origElements.clear();
        this.peerQueue.clear();
        this.tipContent.clear();
        try {
            android.content.Context appContext = getAppContext();
            if (appContext == null || (fileStoreFile = storeFile(appContext)) == null || !fileStoreFile.exists()) {
                return;
            }
            fileStoreFile.delete();
        } catch (java.lang.Throwable unused) {
        }
    }

    public static android.content.Context appContext() {
        return getAppContext();
    }

    private static android.content.Context getAppContext() {
        try {
            java.lang.reflect.Method method = java.lang.Class.forName("android.app.ActivityThread").getMethod("currentApplication", new java.lang.Class[0]);
            method.setAccessible(true);
            return (android.content.Context) method.invoke(null, new java.lang.Object[0]);
        } catch (java.lang.Throwable unused) {
            return null;
        }
    }
}
