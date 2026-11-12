package com.tencent.qqlitepro.cache;

/* JADX INFO: loaded from: classes.dex */
public final class RawMsgStore {
    private static final java.util.Map<java.lang.String, com.tencent.qqlitepro.cache.RawMsgStore.Entry> INDEX = new com.tencent.qqlitepro.cache.RawMsgStore.AnonymousClass1();
    private static final java.lang.Object LOCK = new java.lang.Object();
    private static final int MAX = 300;

    public static final class Entry {
        public final int chatType;
        public final java.lang.String msgId;
        public final java.lang.String peerUid;
        public final java.lang.String rawJson;
        public final long time;

        Entry(java.lang.String str, java.lang.String str2, int i, long j, java.lang.String str3) {
            this.msgId = str;
            this.peerUid = str2;
            this.chatType = i;
            this.time = j;
            this.rawJson = str3;
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.cache.RawMsgStore$1, reason: invalid class name */
    static class AnonymousClass1 extends java.util.LinkedHashMap<java.lang.String, com.tencent.qqlitepro.cache.RawMsgStore.Entry> {
        AnonymousClass1() {
        }

        @Override // java.util.LinkedHashMap
        protected boolean removeEldestEntry(java.util.Map.Entry<java.lang.String, com.tencent.qqlitepro.cache.RawMsgStore.Entry> entry) {
            return size() > com.tencent.qqlitepro.cache.RawMsgStore.MAX;
        }
    }

    private RawMsgStore() {
    }

    public static boolean offer(java.lang.String str, java.lang.String str2, int i, java.lang.String str3) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        synchronized (LOCK) {
            java.util.Map<java.lang.String, com.tencent.qqlitepro.cache.RawMsgStore.Entry> map = INDEX;
            if (map.containsKey(str)) {
                return false;
            }
            map.put(str, new com.tencent.qqlitepro.cache.RawMsgStore.Entry(str, str2, i, java.lang.System.currentTimeMillis(), str3));
            return true;
        }
    }

    public static java.util.List<com.tencent.qqlitepro.cache.RawMsgStore.Entry> recent(int i, java.lang.String str) {
        java.util.ArrayList arrayList = new java.util.ArrayList();
        synchronized (LOCK) {
            for (com.tencent.qqlitepro.cache.RawMsgStore.Entry entry : INDEX.values()) {
                if (str == null || str.isEmpty() || str.equals(entry.peerUid)) {
                    arrayList.add(entry);
                }
            }
        }
        java.util.ArrayList arrayList2 = new java.util.ArrayList(arrayList.size());
        for (int size = arrayList.size() - 1; size >= 0; size--) {
            arrayList2.add((com.tencent.qqlitepro.cache.RawMsgStore.Entry) arrayList.get(size));
        }
        return (i <= 0 || i >= arrayList2.size()) ? arrayList2 : new java.util.ArrayList(arrayList2.subList(0, i));
    }

    public static int size() {
        int size;
        synchronized (LOCK) {
            size = INDEX.size();
        }
        return size;
    }

    public static void clear() {
        synchronized (LOCK) {
            INDEX.clear();
        }
    }
}
