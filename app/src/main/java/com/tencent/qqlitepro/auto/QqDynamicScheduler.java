package com.tencent.qqlitepro.auto;

/* JADX INFO: loaded from: classes.dex */
public final class QqDynamicScheduler {
    private static final java.lang.String CLS_ADAPTER_HOST = "com.tencent.watch.qzone_impl.frame.IAdapterHost";
    private static final java.lang.String CLS_CB_WRAPPER = "com.tencent.watch.qzone_impl.feed.ServiceCallbackWrapper";
    private static final java.lang.String CLS_FEED_SERVICE = "com.tencent.watch.qzone_impl.feed.QZoneFeedService";
    private static volatile boolean inited = false;
    private static volatile java.lang.ClassLoader sCl = null;
    private static volatile java.lang.ref.WeakReference<java.lang.Object> sLastUiHost = null;
    private static volatile java.lang.Object sProxyHost = null;
    private static volatile boolean started = false;
    private static final java.util.Map<java.lang.String, com.tencent.qqlitepro.auto.QqDynamicScheduler.FeedRecord> sCaptured = new java.util.LinkedHashMap();
    private static final java.util.Set<java.lang.String> sHandled = new java.util.HashSet();
    private static final java.util.concurrent.atomic.AtomicLong sDataVersion = new java.util.concurrent.atomic.AtomicLong(0);
    private static final java.lang.Object sDataLock = new java.lang.Object();
    private static volatile java.lang.String sLastRunDay = "";
    private static volatile int sCaptureCount = 0;
    private static final android.os.Handler sMain = new android.os.Handler(android.os.Looper.getMainLooper());

    private QqDynamicScheduler() {
    }

    public static synchronized void init(java.lang.ClassLoader classLoader) {
        sCl = classLoader;
        inited = true;
    }

    public static synchronized void start() {
        if (started) {
            return;
        }
        java.lang.Thread thread = new java.lang.Thread(new com.tencent.qqlitepro.auto.QqDynamicScheduler$$ExternalSyntheticLambda0(), "qq-dynamic-scheduler");
        thread.setDaemon(true);
        thread.start();
        started = true;
        com.tencent.qqlitepro.util.Log.i("QQ动态调度器已启动");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void looper() {
        while (true) {
            try {
                java.lang.Thread.sleep(15000L);
                try {
                    android.content.Context contextAppContext = com.tencent.qqlitepro.cache.MsgCache.appContext();
                    if (contextAppContext != null) {
                        checkAndRun(contextAppContext);
                    }
                } catch (java.lang.Throwable th) {
                    com.tencent.qqlitepro.util.Log.w("QQ动态调度检查异常: " + th);
                }
            } catch (java.lang.InterruptedException unused) {
                return;
            }
        }
    }

    private static void checkAndRun(android.content.Context context) {
        if (com.tencent.qqlitepro.util.Config.qqDynamicEnabled(context)) {
            java.lang.String strQqDynamicTime = com.tencent.qqlitepro.util.Config.qqDynamicTime(context);
            long j = todayTargetMillis(strQqDynamicTime);
            long jCurrentTimeMillis = java.lang.System.currentTimeMillis();
            java.lang.String strDayStr = dayStr(jCurrentTimeMillis);
            if (jCurrentTimeMillis >= j && !strDayStr.equals(sLastRunDay)) {
                sLastRunDay = strDayStr;
                boolean zQqDynamicLike = com.tencent.qqlitepro.util.Config.qqDynamicLike(context);
                boolean zQqDynamicComment = com.tencent.qqlitepro.util.Config.qqDynamicComment(context);
                if (!zQqDynamicLike && !zQqDynamicComment) {
                    com.tencent.qqlitepro.util.Log.i("QQ动态: 定时到点，但未勾选点赞/评论，跳过");
                    return;
                }
                com.tencent.qqlitepro.util.Log.i("QQ动态: 定时任务触发（每日 " + strQqDynamicTime + "，like=" + zQqDynamicLike + " comment=" + zQqDynamicComment + "）");
                java.lang.String strProcessToday = processToday(context, zQqDynamicLike, zQqDynamicComment, true);
                java.lang.StringBuilder sb = new java.lang.StringBuilder("QQ动态: 定时任务完成 ");
                sb.append(strProcessToday);
                com.tencent.qqlitepro.util.Log.i(sb.toString());
                java.lang.StringBuilder sb2 = new java.lang.StringBuilder("[定时] ");
                sb2.append(strProcessToday);
                com.tencent.qqlitepro.hook.SettingsInjector.appendQqLog(sb2.toString());
            }
        }
    }

    public static void onFeedCaptured(java.lang.Object obj, java.lang.Object obj2) {
        boolean zIsEmpty;
        try {
            java.lang.String strFeedKey = feedKey(obj2);
            if (strFeedKey != null && !strFeedKey.isEmpty()) {
                long jOwnerUin = ownerUin(obj2);
                long jFeedTime = feedTime(obj2);
                java.lang.String strFeedNick = feedNick(obj2);
                com.tencent.qqlitepro.auto.QqDynamicScheduler.FeedRecord feedRecord = new com.tencent.qqlitepro.auto.QqDynamicScheduler.FeedRecord(obj2, obj, strFeedKey, jOwnerUin, strFeedNick, jFeedTime);
                java.util.Map<java.lang.String, com.tencent.qqlitepro.auto.QqDynamicScheduler.FeedRecord> map = sCaptured;
                synchronized (map) {
                    zIsEmpty = map.isEmpty();
                    com.tencent.qqlitepro.auto.QqDynamicScheduler.FeedRecord feedRecord2 = map.get(strFeedKey);
                    if (feedRecord2 != null && feedRecord.host == null && feedRecord2.host != null) {
                        feedRecord.host = feedRecord2.host;
                    }
                    map.put(strFeedKey, feedRecord);
                    while (true) {
                        java.util.Map<java.lang.String, com.tencent.qqlitepro.auto.QqDynamicScheduler.FeedRecord> map2 = sCaptured;
                        if (map2.size() <= 500) {
                            break;
                        }
                        java.util.Iterator<java.lang.String> it = map2.keySet().iterator();
                        if (it.hasNext()) {
                            it.next();
                            it.remove();
                        }
                    }
                }
                sCaptureCount++;
                if (zIsEmpty) {
                    com.tencent.qqlitepro.util.Log.i("QQ动态: 首次捕获动态 " + strFeedNick + "(" + strFeedKey + ")，累计 " + sCaptureCount);
                    return;
                }
                if (sCaptureCount % 20 == 0) {
                    com.tencent.qqlitepro.util.Log.i("QQ动态: 累计捕获 " + sCaptureCount + " 条动态");
                }
            }
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("QQ动态: onFeedCaptured 异常 " + th);
        }
    }

    private static java.util.List<com.tencent.qqlitepro.auto.QqDynamicScheduler.FeedRecord> snapshot() {
        java.util.ArrayList arrayList;
        java.util.Map<java.lang.String, com.tencent.qqlitepro.auto.QqDynamicScheduler.FeedRecord> map = sCaptured;
        synchronized (map) {
            arrayList = new java.util.ArrayList(map.values());
        }
        return arrayList;
    }

    public static void onNativeFeedList(java.util.List<?> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        for (java.lang.Object obj : list) {
            if (obj != null) {
                onFeedCaptured(null, obj);
            }
        }
        java.lang.Object obj2 = sDataLock;
        synchronized (obj2) {
            sDataVersion.incrementAndGet();
            obj2.notifyAll();
        }
    }

    public static void onUiFeedList(java.lang.Object obj, java.util.List<?> list) {
        if (obj != null) {
            sLastUiHost = new java.lang.ref.WeakReference<>(obj);
        }
        if (list == null || list.isEmpty()) {
            return;
        }
        for (java.lang.Object obj2 : list) {
            if (obj2 != null) {
                onFeedCaptured(obj, obj2);
            }
        }
        java.lang.Object obj3 = sDataLock;
        synchronized (obj3) {
            sDataVersion.incrementAndGet();
            obj3.notifyAll();
        }
    }

    /* JADX WARN: Code duplicated, block: B:30:0x0096 A[PHI: r18 r19
      0x0096: PHI (r18v7 boolean) = (r18v5 boolean), (r18v5 boolean), (r18v5 boolean), (r18v8 boolean) binds: [B:50:0x00c0, B:36:0x00a7, B:29:0x0094, B:19:0x007a] A[DONT_GENERATE, DONT_INLINE]
      0x0096: PHI (r19v8 int) = (r19v4 int), (r19v4 int), (r19v4 int), (r19v9 int) binds: [B:50:0x00c0, B:36:0x00a7, B:29:0x0094, B:19:0x007a] A[DONT_GENERATE, DONT_INLINE]] */
    public static java.lang.String processToday(android.content.Context context, boolean z, boolean z2, boolean z3) {
        boolean z4;
        int i;
        java.util.Set<java.lang.String> set = new java.util.LinkedHashSet<>();
        java.lang.Object th;
        long j = 0L;
        java.lang.String str = "";
        if (!inited || sCl == null) {
            return "内核尚未就绪（hook 未注册）";
        }
        int iPrepareFeeds = prepareFeeds();
        java.util.Set<java.lang.String> whitelist = com.tencent.qqlitepro.http.QqDynamic.parseWhitelist(com.tencent.qqlitepro.util.Config.qqDynamicWhitelist(context));
        boolean z5 = !whitelist.isEmpty();
        long jSelfUin = selfUin();
        long j2 = todayStartMillis();
        java.util.List<com.tencent.qqlitepro.auto.QqDynamicScheduler.FeedRecord> listSnapshot = snapshot();
        if (listSnapshot.isEmpty()) {
            return "暂无可处理动态（主动拉取 " + iPrepareFeeds + " 条均无效或网络未就绪，请稍后重试）";
        }
        java.util.ArrayList arrayList = new java.util.ArrayList();
        int iQqDynamicLikeCount = com.tencent.qqlitepro.util.Config.qqDynamicLikeCount(context);
        int iQqDynamicCommentCount = com.tencent.qqlitepro.util.Config.qqDynamicCommentCount(context);
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        int i6 = 0;
        int i7 = 0;
        int i8 = 0;
        for (com.tencent.qqlitepro.auto.QqDynamicScheduler.FeedRecord feedRecord : listSnapshot) {
            if (z5) {
                z4 = z5;
                i = i2;
                try {
                    if (!whitelist.contains(java.lang.String.valueOf(feedRecord.owner))) {
                    }
                    set = whitelist;
                    j = jSelfUin;
                    z5 = z4;
                    i2 = i;
                } catch (java.lang.Throwable th2) {
                    java.lang.Throwable thRethrow = th2;
                    set = whitelist;
                    j = jSelfUin;
                    int i9 = i8;
                    i2 = i;
                    com.tencent.qqlitepro.util.Log.w("QQ动态: 处理动态异常 " + thRethrow);
                    i8 = i9;
                    z5 = z4;
                    whitelist = set;
                    jSelfUin = j;
                }
                whitelist = set;
                jSelfUin = j;
            } else {
                z4 = z5;
                i = i2;
            }
            if (jSelfUin <= 0 || feedRecord.owner != jSelfUin) {
                try {
                    if (feedRecord.timeMs <= 0 || feedRecord.timeMs >= j2) {
                        boolean z6 = z && iQqDynamicLikeCount > 0 && i4 + i6 >= iQqDynamicLikeCount;
                        boolean z7 = z2 && iQqDynamicCommentCount > 0 && i3 >= iQqDynamicCommentCount;
                        if (z6 && z7) {
                            set = whitelist;
                            j = jSelfUin;
                        } else {
                            try {
                                if (z3) {
                                    set = whitelist;
                                    try {
                                        java.util.Set<java.lang.String> set2 = sHandled;
                                        synchronized (set2) {
                                            j = jSelfUin;
                                            try {
                                                if (!set2.contains(feedRecord.key)) {
                                                    set2.add(feedRecord.key);
                                                }
                                            } catch (java.lang.Throwable th3) {
                                                throw th3;
                                            }
                                        }
                                    } catch (java.lang.Throwable th4) {
                                        java.lang.Throwable thRethrow2 = th4;
                                        j = jSelfUin;
                                        int i10 = i8;
                                        i2 = i;
                                        com.tencent.qqlitepro.util.Log.w("QQ动态: 处理动态异常 " + thRethrow2);
                                        i8 = i10;
                                    }
                                    whitelist = set;
                                    jSelfUin = j;
                                } else {
                                    set = whitelist;
                                    j = jSelfUin;
                                }
                                if (feedRecord.nick != null) {
                                    try {
                                        str = feedRecord.nick.isEmpty() ? feedRecord.key : feedRecord.nick + "(" + feedRecord.key + ")";
                                    } catch (java.lang.Throwable th5) {
                                        java.lang.Throwable thRethrow3 = th5;
                                        int i11 = i8;
                                        i2 = i;
                                        com.tencent.qqlitepro.util.Log.w("QQ动态: 处理动态异常 " + thRethrow3);
                                        i8 = i11;
                                    }
                                }
                                arrayList.add(str);
                                java.lang.Object objResolveHost = resolveHost(feedRecord);
                                if (!z || z6) {
                                    if (z) {
                                        i++;
                                    }
                                } else if (readLiked(feedRecord.feed)) {
                                    i6++;
                                    com.tencent.qqlitepro.util.Log.i("QQ动态: 已赞过，跳过 " + str);
                                } else {
                                    i5++;
                                    if (safeLike(objResolveHost, feedRecord.feed)) {
                                        i4++;
                                        com.tencent.qqlitepro.util.Log.i("QQ动态: 已点赞 " + str);
                                    } else {
                                        com.tencent.qqlitepro.util.Log.w("QQ动态: 点赞失败 " + str);
                                    }
                                    if (iQqDynamicLikeCount > 0 && i4 + i6 >= iQqDynamicLikeCount) {
                                        com.tencent.qqlitepro.util.Log.i("QQ动态: 点赞已达上限 " + iQqDynamicLikeCount + " 条");
                                    }
                                }
                                if (!z2 || z7) {
                                    int i12 = i7;
                                    if (z2) {
                                        i8++;
                                    }
                                    i7 = i12;
                                } else {
                                    java.lang.String strQqDynamicCommentText = com.tencent.qqlitepro.util.Config.qqDynamicCommentText(context);
                                    if (safeComment(objResolveHost, feedRecord.feed, strQqDynamicCommentText)) {
                                        i3++;
                                        com.tencent.qqlitepro.util.Log.i("QQ动态: 已发送评论 " + str + " -> " + strQqDynamicCommentText);
                                    } else {
                                        i7++;
                                        com.tencent.qqlitepro.util.Log.w("QQ动态: 评论失败 " + str);
                                    }
                                    if (iQqDynamicCommentCount > 0 && i3 >= iQqDynamicCommentCount) {
                                        com.tencent.qqlitepro.util.Log.i("QQ动态: 评论已达上限 " + iQqDynamicCommentCount + " 条");
                                    }
                                }
                                i2 = i;
                            } catch (java.lang.Throwable th6) {
                                java.lang.Throwable thRethrow4 = th6;
                                int i13 = i8;
                                i2 = i;
                                com.tencent.qqlitepro.util.Log.w("QQ动态: 处理动态异常 " + thRethrow4);
                                i8 = i13;
                            }
                            z5 = z4;
                            whitelist = set;
                            jSelfUin = j;
                        }
                    } else {
                        set = whitelist;
                        j = jSelfUin;
                    }
                } catch (java.lang.Throwable th7) {
                    java.lang.Throwable thRethrow5 = th7;
                    set = whitelist;
                    j = jSelfUin;
                }
            } else {
                set = whitelist;
                j = jSelfUin;
            }
            z5 = z4;
            i2 = i;
            whitelist = set;
            jSelfUin = j;
        }
        boolean z8 = z5;
        int i14 = i2;
        int i15 = i7;
        int i16 = i8;
        java.lang.String str2 = z8 ? "白名单动态" : "全部动态";
        java.lang.StringBuilder sb = new java.lang.StringBuilder("今天");
        sb.append(str2);
        sb.append(" 命中 ");
        sb.append(arrayList.size());
        sb.append(" 条");
        if (z) {
            sb.append("；点赞成功 ");
            sb.append(i4);
            sb.append("/失败 ");
            sb.append(i5 - i4);
            sb.append("/已赞过 ");
            sb.append(i6);
            if (iQqDynamicLikeCount > 0) {
                sb.append("（上限 ");
                sb.append(iQqDynamicLikeCount);
                sb.append("，未处理 ");
                sb.append(java.lang.Math.max(0, i14));
                sb.append("）");
            }
        }
        if (z2) {
            sb.append("；评论成功 ");
            sb.append(i3);
            sb.append("/失败 ");
            sb.append(i15);
            if (iQqDynamicCommentCount > 0) {
                sb.append("（上限 ");
                sb.append(iQqDynamicCommentCount);
                sb.append("，未处理 ");
                sb.append(java.lang.Math.max(0, i16));
                sb.append("）");
            }
        }
        sb.append("。");
        return sb.toString();
    }

    public static java.lang.String likeAllToday(android.content.Context context) {
        com.tencent.qqlitepro.util.Log.i("QQ动态: 一键点赞今天全部动态");
        return processToday(context, true, false, false);
    }

    public static java.lang.String commentAllToday(android.content.Context context) {
        com.tencent.qqlitepro.util.Log.i("QQ动态: 一键评论今天全部动态");
        return processToday(context, false, true, false);
    }

    public static java.lang.String processLatest(android.content.Context context, boolean z, boolean z2) {
        int i;
        boolean z3;
        int i2;
        java.util.Set<java.lang.String> set = new java.util.LinkedHashSet<>();
        java.lang.Object thRethrow5;
        long j = 0L;
        java.lang.String string = "";
        if (!inited || sCl == null) {
            return "内核尚未就绪（hook 未注册）";
        }
        int iPrepareFeeds = prepareFeeds();
        java.util.Set<java.lang.String> whitelist = com.tencent.qqlitepro.http.QqDynamic.parseWhitelist(com.tencent.qqlitepro.util.Config.qqDynamicWhitelist(context));
        boolean z4 = !whitelist.isEmpty();
        long jSelfUin = selfUin();
        int iQqDynamicLikeCount = com.tencent.qqlitepro.util.Config.qqDynamicLikeCount(context);
        int iQqDynamicCommentCount = com.tencent.qqlitepro.util.Config.qqDynamicCommentCount(context);
        java.util.List<com.tencent.qqlitepro.auto.QqDynamicScheduler.FeedRecord> listSnapshot = snapshot();
        if (listSnapshot.isEmpty()) {
            return "暂无可处理动态（主动拉取 " + iPrepareFeeds + " 条均无效或网络未就绪，请稍后重试）";
        }
        java.util.Collections.sort(listSnapshot, new com.tencent.qqlitepro.auto.QqDynamicScheduler$$ExternalSyntheticLambda5());
        java.util.ArrayList arrayList = new java.util.ArrayList();
        java.util.Iterator<com.tencent.qqlitepro.auto.QqDynamicScheduler.FeedRecord> it = listSnapshot.iterator();
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        int i6 = 0;
        int i7 = 0;
        int i8 = 0;
        int i9 = 0;
        while (true) {
            i = i4;
            if (!it.hasNext()) {
                break;
            }
            com.tencent.qqlitepro.auto.QqDynamicScheduler.FeedRecord next = it.next();
            if (z4) {
                z3 = z4;
                i2 = i3;
                try {
                    if (!whitelist.contains(java.lang.String.valueOf(next.owner))) {
                    }
                    i4 = i;
                    z4 = z3;
                    i3 = i2;
                } catch (java.lang.Throwable th2) {
                    java.lang.Throwable thRethrow6 = th2;
                    set = whitelist;
                    j = jSelfUin;
                    i3 = i2;
                    com.tencent.qqlitepro.util.Log.w("QQ动态: 处理动态异常 " + thRethrow6);
                    i4 = i;
                    z4 = z3;
                    whitelist = set;
                    jSelfUin = j;
                }
            } else {
                z3 = z4;
                i2 = i3;
            }
            if (jSelfUin <= 0 || next.owner != jSelfUin) {
                boolean z5 = z && iQqDynamicLikeCount > 0 && i5 + i7 >= iQqDynamicLikeCount;
                boolean z6 = z2 && iQqDynamicCommentCount > 0 && i9 >= iQqDynamicCommentCount;
                if (!z5 || !z6) {
                    set = whitelist;
                    try {
                        if (next.nick == null || next.nick.isEmpty()) {
                            j = jSelfUin;
                            string = next.key;
                        } else {
                            java.lang.StringBuilder sb = new java.lang.StringBuilder();
                            j = jSelfUin;
                            try {
                                sb.append(next.nick);
                                sb.append("(");
                                sb.append(next.key);
                                sb.append(")");
                                string = sb.toString();
                            } catch (java.lang.Throwable th3) {
                                java.lang.Throwable thRethrow7 = th3;
                                i3 = i2;
                                com.tencent.qqlitepro.util.Log.w("QQ动态: 处理动态异常 " + thRethrow7);
                                i4 = i;
                                z4 = z3;
                                whitelist = set;
                                jSelfUin = j;
                            }
                        }
                        arrayList.add(string);
                        java.lang.Object objResolveHost = resolveHost(next);
                        if (!z || z5) {
                            if (z) {
                                i8++;
                            }
                        } else if (readLiked(next.feed)) {
                            i7++;
                            com.tencent.qqlitepro.util.Log.i("QQ动态: 已赞过，跳过 " + string);
                        } else {
                            i6++;
                            if (safeLike(objResolveHost, next.feed)) {
                                i5++;
                                com.tencent.qqlitepro.util.Log.i("QQ动态: 已点赞 " + string);
                            } else {
                                com.tencent.qqlitepro.util.Log.w("QQ动态: 点赞失败 " + string);
                            }
                            if (iQqDynamicLikeCount > 0 && i5 + i7 >= iQqDynamicLikeCount) {
                                com.tencent.qqlitepro.util.Log.i("QQ动态: 点赞已达上限 " + iQqDynamicLikeCount + " 条");
                            }
                        }
                        if (!z2 || z6) {
                            i4 = z2 ? i + 1 : i;
                            i3 = i2;
                        } else {
                            java.lang.String strQqDynamicCommentText = com.tencent.qqlitepro.util.Config.qqDynamicCommentText(context);
                            if (safeComment(objResolveHost, next.feed, strQqDynamicCommentText)) {
                                i9++;
                                com.tencent.qqlitepro.util.Log.i("QQ动态: 已发送评论 " + string + " -> " + strQqDynamicCommentText);
                                i3 = i2;
                            } else {
                                i3 = i2 + 1;
                                com.tencent.qqlitepro.util.Log.w("QQ动态: 评论失败 " + string);
                            }
                            if (iQqDynamicCommentCount > 0 && i9 >= iQqDynamicCommentCount) {
                                try {
                                    com.tencent.qqlitepro.util.Log.i("QQ动态: 评论已达上限 " + iQqDynamicCommentCount + " 条");
                                } catch (java.lang.Throwable th4) {
                                    java.lang.Throwable thRethrow8 = th4;
                                    com.tencent.qqlitepro.util.Log.w("QQ动态: 处理动态异常 " + thRethrow8);
                                }
                            }
                            i4 = i;
                        }
                    } catch (java.lang.Throwable th5) {
                        java.lang.Throwable thRethrow9 = th5;
                        j = jSelfUin;
                        i3 = i2;
                        com.tencent.qqlitepro.util.Log.w("QQ动态: 处理动态异常 " + thRethrow9);
                        i4 = i;
                        z4 = z3;
                        whitelist = set;
                        jSelfUin = j;
                    }
                    z4 = z3;
                    whitelist = set;
                    jSelfUin = j;
                }
            }
            i4 = i;
            z4 = z3;
            i3 = i2;
        }
        int i10 = i3;
        java.lang.String str = z4 ? "白名单动态" : "全部动态";
        java.lang.StringBuilder sb2 = new java.lang.StringBuilder("立即执行（按时间倒序）命中 ");
        sb2.append(arrayList.size());
        sb2.append(" 条");
        sb2.append(str);
        if (z) {
            sb2.append("；点赞成功 ");
            sb2.append(i5);
            sb2.append("/失败 ");
            sb2.append(i6 - i5);
            sb2.append("/已赞过 ");
            sb2.append(i7);
            if (iQqDynamicLikeCount > 0) {
                sb2.append("（上限 ");
                sb2.append(iQqDynamicLikeCount);
                sb2.append("，未处理 ");
                sb2.append(java.lang.Math.max(0, i8));
                sb2.append("）");
            }
        }
        if (z2) {
            sb2.append("；评论成功 ");
            sb2.append(i9);
            sb2.append("/失败 ");
            sb2.append(i10);
            if (iQqDynamicCommentCount > 0) {
                sb2.append("（上限 ");
                sb2.append(iQqDynamicCommentCount);
                sb2.append("，未处理 ");
                sb2.append(java.lang.Math.max(0, i));
                sb2.append("）");
            }
        }
        sb2.append("。");
        return sb2.toString();
    }

    private static int prepareFeeds() {
        mergeManagerFeeds();
        if (selfUin() <= 0) {
            com.tencent.qqlitepro.util.Log.i("QQ动态: 未取得自身 uin，跳过主动刷新");
            return snapshot().size();
        }
        long j = sDataVersion.get();
        boolean zTriggerNativeRefresh = triggerNativeRefresh();
        if (zTriggerNativeRefresh) {
            awaitNativeData(j, 8000L);
        }
        mergeManagerFeeds();
        int size = snapshot().size();
        com.tencent.qqlitepro.util.Log.i("QQ动态: prepareFeeds 触发刷新=" + zTriggerNativeRefresh + " 总动态=" + size);
        return size;
    }

    private static void mergeManagerFeeds() {
        try {
            java.lang.Object objFeedManager = feedManager();
            if (objFeedManager == null) {
                return;
            }
            java.lang.Object objCallMethod = de.robv.android.xposed.XposedHelpers.callMethod(objFeedManager, "n", new java.lang.Object[0]);
            if (objCallMethod instanceof java.util.List) {
                java.lang.Object objLastUiHost = lastUiHost();
                for (java.lang.Object obj : (java.util.List) objCallMethod) {
                    if (obj != null) {
                        onFeedCaptured(objLastUiHost, obj);
                    }
                }
            }
        } catch (java.lang.Throwable thRethrow9) {
            com.tencent.qqlitepro.util.Log.w("QQ动态: 合并 manager 列表失败 " + thRethrow9);
        }
    }

    private static java.lang.Object feedManager() {
        try {
            return de.robv.android.xposed.XposedHelpers.getObjectField(de.robv.android.xposed.XposedHelpers.callStaticMethod(de.robv.android.xposed.XposedHelpers.findClass(CLS_FEED_SERVICE, sCl), "h", new java.lang.Object[0]), "h");
        } catch (java.lang.Throwable unused) {
            return null;
        }
    }

    private static boolean triggerNativeRefresh() {
        boolean[] zArr = {false};
        return runOnMain(new com.tencent.qqlitepro.auto.QqDynamicScheduler$$ExternalSyntheticLambda3(zArr), 5000L) && zArr[0];
    }

    static /* synthetic */ void lambda$triggerNativeRefresh$1(boolean[] zArr) {
        try {
            java.lang.Class clsFindClass = de.robv.android.xposed.XposedHelpers.findClass(CLS_FEED_SERVICE, sCl);
            java.lang.Class clsFindClass2 = de.robv.android.xposed.XposedHelpers.findClass(CLS_CB_WRAPPER, sCl);
            java.lang.Object objectField = de.robv.android.xposed.XposedHelpers.getObjectField(de.robv.android.xposed.XposedHelpers.callStaticMethod(clsFindClass, "h", new java.lang.Object[0]), "h");
            java.lang.Object objNewInstance = de.robv.android.xposed.XposedHelpers.newInstance(clsFindClass2, new java.lang.Object[0]);
            de.robv.android.xposed.XposedHelpers.setObjectField(objNewInstance, "a", new java.lang.ref.WeakReference(new android.os.Handler(android.os.Looper.getMainLooper())));
            de.robv.android.xposed.XposedHelpers.callMethod(objectField, "b", new java.lang.Object[]{0, objNewInstance, false});
            zArr[0] = true;
        } catch (java.lang.Throwable thRethrow9) {
            com.tencent.qqlitepro.util.Log.w("QQ动态: 主动刷新请求失败 " + thRethrow9);
        }
    }

    private static void awaitNativeData(long j, long j2) {
        java.lang.Object obj = sDataLock;
        synchronized (obj) {
            if (sDataVersion.get() > j) {
                return;
            }
            try {
                obj.wait(j2);
            } catch (java.lang.InterruptedException unused) {
                java.lang.Thread.currentThread().interrupt();
            }
        }
    }

    private static java.lang.Object resolveHost(com.tencent.qqlitepro.auto.QqDynamicScheduler.FeedRecord feedRecord) {
        if (hostAlive(feedRecord.host)) {
            return feedRecord.host;
        }
        java.lang.Object objLastUiHost = lastUiHost();
        return hostAlive(objLastUiHost) ? objLastUiHost : proxyHost();
    }

    private static java.lang.Object lastUiHost() {
        java.lang.ref.WeakReference<java.lang.Object> weakReference = sLastUiHost;
        if (weakReference == null) {
            return null;
        }
        return weakReference.get();
    }

    private static boolean hostAlive(java.lang.Object obj) {
        if (obj == null) {
            return false;
        }
        try {
            return de.robv.android.xposed.XposedHelpers.callMethod(obj, "requireContext", new java.lang.Object[0]) != null;
        } catch (java.lang.Throwable unused) {
            return false;
        }
    }

    private static java.lang.Object proxyHost() {
        java.lang.Object obj;
        java.lang.Object obj2 = sProxyHost;
        if (obj2 != null) {
            return obj2;
        }
        synchronized (com.tencent.qqlitepro.auto.QqDynamicScheduler.class) {
            if (sProxyHost == null) {
                sProxyHost = java.lang.reflect.Proxy.newProxyInstance(sCl, new java.lang.Class[]{de.robv.android.xposed.XposedHelpers.findClass(CLS_ADAPTER_HOST, sCl)}, new com.tencent.qqlitepro.auto.QqDynamicScheduler$$ExternalSyntheticLambda2());
                com.tencent.qqlitepro.util.Log.i("QQ动态: 已创建无界面代理 IAdapterHost");
            }
            obj = sProxyHost;
        }
        return obj;
    }

    static /* synthetic */ java.lang.Object lambda$proxyHost$2(java.lang.Object obj, java.lang.reflect.Method method, java.lang.Object[] objArr) throws java.lang.Throwable {
        java.lang.String name = method.getName();
        if ("requireContext".equals(name)) {
            return com.tencent.qqlitepro.cache.MsgCache.appContext();
        }
        if ("toString".equals(name)) {
            return "AntiRecallQzoneProxyHost";
        }
        java.lang.Class<?> returnType = method.getReturnType();
        if (returnType == java.lang.Boolean.TYPE) {
            return false;
        }
        if (returnType == java.lang.Integer.TYPE) {
            return 0;
        }
        return returnType == java.lang.Long.TYPE ? 0L : null;
    }

    private static boolean safeLike(java.lang.Object obj, java.lang.Object obj2) {
        if (obj == null) {
            return false;
        }
        boolean[] zArr = {false};
        java.lang.Throwable[] thArr = {null};
        boolean zRunOnMain = runOnMain(new com.tencent.qqlitepro.auto.QqDynamicScheduler$$ExternalSyntheticLambda1(obj, obj2, zArr, thArr));
        if (thArr[0] != null) {
            com.tencent.qqlitepro.util.Log.w("QQ动态: toggleLike 异常 " + thArr[0]);
        }
        return zRunOnMain && thArr[0] == null && zArr[0];
    }

    static /* synthetic */ void lambda$safeLike$3(java.lang.Object obj, java.lang.Object obj2, boolean[] zArr, java.lang.Throwable[] thArr) {
        try {
            boolean z = true;
            java.lang.Object objCallMethod = de.robv.android.xposed.XposedHelpers.callMethod(de.robv.android.xposed.XposedHelpers.getStaticObjectField(de.robv.android.xposed.XposedHelpers.findClass("momoi.mod.qqpro.hook.qzone.QzoneActions", sCl), "INSTANCE"), "toggleLike", new java.lang.Object[]{obj, obj2});
            if (objCallMethod == null || !((java.lang.Boolean) objCallMethod).booleanValue()) {
                z = false;
            }
            zArr[0] = z;
        } catch (java.lang.Throwable thRethrow9) {
            thArr[0] = thRethrow9;
        }
    }

    private static boolean safeComment(java.lang.Object obj, java.lang.Object obj2, java.lang.String str) {
        if (obj == null) {
            return false;
        }
        java.lang.Throwable[] thArr = {null};
        boolean zRunOnMain = runOnMain(new com.tencent.qqlitepro.auto.QqDynamicScheduler$$ExternalSyntheticLambda4(obj2, obj, str, thArr));
        if (thArr[0] != null) {
            com.tencent.qqlitepro.util.Log.w("QQ动态: postComment 异常 " + thArr[0]);
        }
        return zRunOnMain && thArr[0] == null;
    }

    static /* synthetic */ void lambda$safeComment$4(java.lang.Object obj, java.lang.Object obj2, java.lang.String str, java.lang.Throwable[] thArr) {
        try {
            de.robv.android.xposed.XposedHelpers.callMethod(de.robv.android.xposed.XposedHelpers.newInstance(de.robv.android.xposed.XposedHelpers.findClass("momoi.mod.qqpro.hook.qzone.QzoneCommentThread", sCl), new java.lang.Object[]{obj, obj2}), "postComment", new java.lang.Object[]{obj, str});
        } catch (java.lang.Throwable thRethrow9) {
            thArr[0] = thRethrow9;
        }
    }

    private static boolean runOnMain(java.lang.Runnable runnable, long j) {
        if (android.os.Looper.myLooper() == android.os.Looper.getMainLooper()) {
            runnable.run();
            return true;
        }
        java.util.concurrent.CountDownLatch countDownLatch = new java.util.concurrent.CountDownLatch(1);
        sMain.post(new com.tencent.qqlitepro.auto.QqDynamicScheduler$$ExternalSyntheticLambda6(runnable, countDownLatch));
        try {
            return countDownLatch.await(j, java.util.concurrent.TimeUnit.MILLISECONDS);
        } catch (java.lang.InterruptedException unused) {
            return false;
        }
    }

    static /* synthetic */ void lambda$runOnMain$5(java.lang.Runnable runnable, java.util.concurrent.CountDownLatch countDownLatch) {
        try {
            runnable.run();
        } finally {
            countDownLatch.countDown();
        }
    }

    private static boolean runOnMain(java.lang.Runnable runnable) {
        return runOnMain(runnable, 8000L);
    }

    private static java.lang.String feedKey(java.lang.Object obj) {
        java.lang.Object objectField;
        try {
            java.lang.Object objCallMethod = de.robv.android.xposed.XposedHelpers.callMethod(obj, "getFeedCommInfo", new java.lang.Object[0]);
            if (objCallMethod != null && (objectField = de.robv.android.xposed.XposedHelpers.getObjectField(objCallMethod, "feedskey")) != null && !java.lang.String.valueOf(objectField).isEmpty()) {
                return java.lang.String.valueOf(objectField);
            }
        } catch (java.lang.Throwable unused) {
        }
        try {
            java.lang.Object objCallMethod2 = de.robv.android.xposed.XposedHelpers.callMethod(obj, "getIdInfo", new java.lang.Object[0]);
            return objCallMethod2 != null ? java.lang.String.valueOf(objCallMethod2) : "";
        } catch (java.lang.Throwable unused2) {
            return "";
        }
    }

    private static long ownerUin(java.lang.Object obj) {
        try {
            long longField = de.robv.android.xposed.XposedHelpers.getLongField(obj, "owner_uin");
            if (longField != 0) {
                return longField;
            }
            java.lang.Object objCallMethod = de.robv.android.xposed.XposedHelpers.callMethod(obj, "getUser", new java.lang.Object[0]);
            if (objCallMethod != null) {
                return de.robv.android.xposed.XposedHelpers.getLongField(objCallMethod, "uin");
            }
            return 0L;
        } catch (java.lang.Throwable unused) {
            return 0L;
        }
    }

    private static java.lang.String feedNick(java.lang.Object obj) {
        java.lang.Object objectField;
        try {
            java.lang.Object objCallMethod = de.robv.android.xposed.XposedHelpers.callMethod(obj, "getUser", new java.lang.Object[0]);
            return (objCallMethod == null || (objectField = de.robv.android.xposed.XposedHelpers.getObjectField(objCallMethod, "nickName")) == null) ? "" : java.lang.String.valueOf(objectField);
        } catch (java.lang.Throwable unused) {
            return "";
        }
    }

    private static long feedTime(java.lang.Object obj) {
        try {
            java.lang.Object objCallMethod = de.robv.android.xposed.XposedHelpers.callMethod(obj, "getFeedCommInfo", new java.lang.Object[0]);
            if (objCallMethod != null) {
                return de.robv.android.xposed.XposedHelpers.getLongField(objCallMethod, "time");
            }
        } catch (java.lang.Throwable unused) {
        }
        try {
            return de.robv.android.xposed.XposedHelpers.getLongField(obj, "feeds_update_time");
        } catch (java.lang.Throwable unused2) {
            return 0L;
        }
    }

    private static boolean readLiked(java.lang.Object obj) {
        try {
            java.lang.Object objCallMethod = de.robv.android.xposed.XposedHelpers.callMethod(obj, "getLikeInfo", new java.lang.Object[0]);
            if (objCallMethod != null) {
                return de.robv.android.xposed.XposedHelpers.getBooleanField(objCallMethod, "isLiked");
            }
        } catch (java.lang.Throwable unused) {
        }
        return false;
    }

    private static long selfUin() {
        try {
            java.lang.String strQzoneSelfUin = com.tencent.qqlitepro.http.QqKernel.qzoneSelfUin();
            if (strQzoneSelfUin == null || strQzoneSelfUin.isEmpty()) {
                return 0L;
            }
            return java.lang.Long.parseLong(strQzoneSelfUin);
        } catch (java.lang.Throwable unused) {
            return 0L;
        }
    }

    private static long todayStartMillis() {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        return calendar.getTimeInMillis();
    }

    private static long todayTargetMillis(java.lang.String str) {
        long j;
        long j2 = todayStartMillis();
        try {
            java.lang.String[] strArrSplit = str.split(":");
            j2 += ((long) java.lang.Integer.parseInt(strArrSplit[0])) * 3600000;
            j = ((long) java.lang.Integer.parseInt(strArrSplit[1])) * 60000;
        } catch (java.lang.Throwable unused) {
            j = 32400000;
        }
        return j2 + j;
    }

    private static java.lang.String dayStr(long j) {
        return new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US).format(new java.util.Date(j));
    }

    /* JADX INFO: Access modifiers changed from: private */
    static final class FeedRecord {
        final java.lang.Object feed;
        java.lang.Object host;
        final java.lang.String key;
        final java.lang.String nick;
        final long owner;
        final long timeMs;

        FeedRecord(java.lang.Object obj, java.lang.Object obj2, java.lang.String str, long j, java.lang.String str2, long j2) {
            this.feed = obj;
            this.host = obj2;
            this.key = str;
            this.owner = j;
            this.nick = str2;
            this.timeMs = j2;
        }
    }
}
