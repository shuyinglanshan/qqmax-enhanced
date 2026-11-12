package com.tencent.qqlitepro.http;

/* JADX INFO: loaded from: classes.dex */
public final class QqKernel {
    public static final java.lang.String AT_ALL_TOKEN = "{@All}";
    public static final java.lang.String AT_TOKEN = "{@Sender}";
    private static final long BULLETIN_CACHE_TTL_MS = 300000;
    private static final int BULLETIN_PREFETCH_MAX = 4;
    private static final long GROUP_LIST_TIMEOUT_MS = 2500;
    public static final long OFFICIAL_GROUP_CODE = 801524531;
    private static final long ROLE_TTL_MS = 60000;
    private static final long SELF_CACHE_TTL_MS = 60000;
    private static final int TEMP_CHAT_KERNEL_CHAT_TYPE = 100;
    private static final int TEMP_CHAT_LIST_TYPE_NORMAL = 1;
    private static final java.lang.String TEMP_CHAT_PREF = "qqlitepro_temp_chat";
    private static final java.lang.String TEMP_CHAT_PREF_KEY = "known";
    private static final int TEMP_CHAT_RENDER_CHAT_TYPE = 1;
    private static final long TEMP_CHAT_RESTORE_MAX_WAIT_MS = 60000;
    private static final java.lang.String TEMP_CHAT_TAG = "（临时会话）";
    private static volatile java.lang.Object sApplyListener;
    private static volatile com.tencent.qqlitepro.http.QqKernel.ApplyWaiter sBuddyReqWaiter;
    private static volatile com.tencent.qqlitepro.http.QqKernel.ApplyWaiter sBuddySettingWaiter;
    private static volatile java.lang.ClassLoader sCl;
    private static volatile com.tencent.qqlitepro.http.QqKernel.ApplyWaiter sGroupListWaiter;
    private static volatile com.tencent.qqlitepro.http.QqKernel.ApplyWaiter sGroupNotifyWaiter;
    private static volatile java.lang.reflect.Method sKernelGetMsgMethod;
    private static volatile java.lang.String[] sSelfCache;
    private static volatile long sSelfCacheAtMs;
    private static volatile java.lang.Object sTempChatDatasource;
    private static volatile boolean sTempChatRestoreDone;
    private static volatile java.lang.Object sTempChatTemplate;
    private static final java.util.concurrent.ConcurrentHashMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> sRoleUid = new java.util.concurrent.ConcurrentHashMap<>();
    private static final java.util.concurrent.ConcurrentHashMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> sRoleUin = new java.util.concurrent.ConcurrentHashMap<>();
    private static final java.util.concurrent.ConcurrentHashMap<java.lang.String, java.lang.Long> sRoleAt = new java.util.concurrent.ConcurrentHashMap<>();
    private static final java.util.concurrent.ConcurrentHashMap<java.lang.String, com.tencent.qqlitepro.http.QqKernel.BulletinWaiter> sKernelListWaiters = new java.util.concurrent.ConcurrentHashMap<>();
    private static final java.util.concurrent.ConcurrentHashMap<java.lang.Long, java.util.List<java.lang.Object>> sFetchCallbacks = new java.util.concurrent.ConcurrentHashMap<>();
    private static volatile boolean sGlobalBulletinInitDone = false;
    private static final java.util.concurrent.ConcurrentHashMap<java.lang.String, com.tencent.qqlitepro.http.QqKernel.BulletinCacheEntry> sBulletinCache = new java.util.concurrent.ConcurrentHashMap<>();
    private static final java.util.Set<java.lang.Long> sBulletinFetching = java.util.Collections.newSetFromMap(new java.util.concurrent.ConcurrentHashMap());
    private static final java.util.concurrent.ConcurrentHashMap<java.lang.Long, java.util.concurrent.CountDownLatch> sBulletinDone = new java.util.concurrent.ConcurrentHashMap<>();
    private static final java.util.concurrent.ConcurrentHashMap<java.lang.String, com.tencent.qqlitepro.http.QqKernel.BulletinWaiter> sWaiters = new java.util.concurrent.ConcurrentHashMap<>();
    private static final java.util.concurrent.ConcurrentHashMap<java.lang.String, com.tencent.qqlitepro.http.QqKernel.BulletinWaiter> sRichMediaWaiters = new java.util.concurrent.ConcurrentHashMap<>();
    private static final java.util.concurrent.ConcurrentHashMap<java.lang.String, java.lang.String> sRichMediaUrlCache = new java.util.concurrent.ConcurrentHashMap<>();
    private static final java.util.Set<java.lang.String> sRichMediaPrefetching = java.util.Collections.newSetFromMap(new java.util.concurrent.ConcurrentHashMap());
    private static volatile boolean sRichMediaListenerReady = false;
    private static final android.util.LruCache<java.lang.String, android.graphics.Bitmap> sBulletinBitmapCache = new com.tencent.qqlitepro.http.QqKernel.AnonymousClass1(8388608);
    private static final java.util.concurrent.atomic.AtomicInteger sBulletinPrefetchRunning = new java.util.concurrent.atomic.AtomicInteger(0);
    private static volatile boolean sTempChatShowHookInstalled = false;
    private static final java.lang.String[] TEMP_CHAT_LISTENERS = {"com.tencent.qqnt.kernel.api.impl.MsgService$getListener$1", "com.tencent.qqnt.kernel.api.impl.MsgService$addDefaultListener$1", "com.tencent.qqnt.kernel.invorker.KMsgInvorkService$initListener$1", "com.tencent.qqnt.msg.MsgListenerAdapter", "com.tencent.qqnt.watch.startup.daily.AfterMsgListener", "com.tencent.qqnt.watch.chat.refresh.RefreshHelper", "com.tencent.qqnt.chats.main.vm.datasource.PlatformRecentContactsDataSource", "com.tencent.watch.aio_impl.kernel.adapter.WatchAIOFetchService$registerMsgListener$2", "com.tencent.watch.aio_impl.reserve1.unreadbubble.UnreadBubbleVM$registerMessageReceive$1", "com.tencent.watch.aio_impl.ext.AIOPicDownloader$downloadPic$2$1", "com.tencent.qqnt.aio.adapter.api.impl.AIOFileErrHandlerNtImpl$errHandler$1", "com.tencent.qqnt.kernel.api.impl.KickApiImpl$setupKickListen$listener$1", "com.tencent.mobileqq.unitedconfig_android.api.impl.UnitedConfigManagerImpl"};
    private static final java.util.Set<java.lang.String> sTempChatSeen = java.util.Collections.synchronizedSet(new java.util.LinkedHashSet());
    private static final java.util.concurrent.ConcurrentHashMap<java.lang.Integer, java.lang.Boolean> sTempChatTypeCache = new java.util.concurrent.ConcurrentHashMap<>();
    private static volatile java.lang.Object sKernelMsgSvc = null;
    private static final java.util.concurrent.ConcurrentHashMap<java.lang.String, java.lang.Object[]> sTempChatPending = new java.util.concurrent.ConcurrentHashMap<>();
    private static final java.util.concurrent.ConcurrentHashMap<java.lang.String, java.lang.Object[]> sTempChatKnown = new java.util.concurrent.ConcurrentHashMap<>();
    public static java.lang.String sDebug = "";
    private static volatile boolean sBuddyListenerReady = false;
    private static volatile boolean sGroupNotifyListenerReady = false;

    public static final class ApplyResult {
        public int code = -1;
        public java.lang.String msg = "";
        public boolean timeout = false;
    }

    public static final class BanResult {
        public int code = -1;
        public java.lang.String msg = "";
        public boolean timeout = false;
        public boolean muted = false;
        public long shutUpAllTimestamp = 0;
        public java.lang.String groupCode = "";
        public java.lang.String rawJson = "";
    }

    public static final class SendResult {
        public int code = -1;
        public java.lang.String msg = "";
        public boolean timeout = false;
    }

    public static boolean isGlobalBulletinListenerReady() {
        return sGlobalBulletinInitDone;
    }

    private static boolean isRealTempChatType(int i) {
        return i == 99 || i == 100;
    }

    private static long secondsToMs(long j) {
        return (j <= 0 || j >= 100000000000L) ? j : j * 1000;
    }

    public static void setClassLoader(java.lang.ClassLoader classLoader) {
        if (classLoader != null) {
            sCl = classLoader;
        }
    }

    private QqKernel() {
    }

    public static void init(java.lang.ClassLoader classLoader) {
        setClassLoader(classLoader);
        try {
            patchForwardSelectLimit(classLoader);
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("转发选择上限 hook 失败: " + th);
        }
    }

    public static java.lang.String qzoneSkey() {
        return skeyTicket(null);
    }

    public static java.lang.String qzonePskey(java.lang.String str) {
        return skeyTicket(str);
    }

    public static java.lang.String qzonePt4Token(java.lang.String str) {
        return pt4Token(str);
    }

    public static int qzoneBkn(java.lang.String str) {
        return bkn(str);
    }

    public static java.lang.String qzoneHttpGet(java.lang.String str, java.lang.String str2) {
        return httpGet(str, str2);
    }

    public static java.lang.String qzoneHttpPost(java.lang.String str, java.lang.String str2, java.lang.String str3) {
        return httpPost(str, str2, str3);
    }

    public static java.lang.String qzoneSelfUin() {
        return selfUin();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.lang.Class<?> cls(java.lang.String str) throws java.lang.ClassNotFoundException {
        if (sCl != null) {
            return java.lang.Class.forName(str, false, sCl);
        }
        return java.lang.Class.forName(str);
    }

    private static java.lang.Class<?> clsQuiet(java.lang.String str) {
        try {
            return cls(str);
        } catch (java.lang.Throwable unused) {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.lang.Object fieldValue(java.lang.Object obj, java.lang.String str) {
        if (obj == null) {
            return null;
        }
        for (java.lang.Class<?> superclass = obj.getClass(); superclass != null && superclass != java.lang.Object.class; superclass = superclass.getSuperclass()) {
            try {
                java.lang.reflect.Field declaredField = superclass.getDeclaredField(str);
                declaredField.setAccessible(true);
                return declaredField.get(obj);
            } catch (java.lang.Throwable unused) {
            }
        }
        try {
            java.lang.reflect.Method method = obj.getClass().getMethod("get" + java.lang.Character.toUpperCase(str.charAt(0)) + str.substring(1), new java.lang.Class[0]);
            method.setAccessible(true);
            return method.invoke(obj, new java.lang.Object[0]);
        } catch (java.lang.Throwable unused2) {
            return null;
        }
    }

    private static java.lang.reflect.Method findMethod(java.lang.Class<?> cls, java.lang.String str, java.lang.Class<?>... clsArr) {
        try {
            java.lang.reflect.Method method = cls.getMethod(str, clsArr);
            method.setAccessible(true);
            return method;
        } catch (java.lang.Throwable unused) {
            while (cls != null && cls != java.lang.Object.class) {
                try {
                    java.lang.reflect.Method declaredMethod = cls.getDeclaredMethod(str, clsArr);
                    declaredMethod.setAccessible(true);
                    return declaredMethod;
                } catch (java.lang.Throwable unused2) {
                    cls = cls.getSuperclass();
                }
            }
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int colSize(java.lang.Object obj) {
        if (obj instanceof java.util.Collection) {
            return ((java.util.Collection) obj).size();
        }
        if (obj instanceof java.lang.Object[]) {
            return ((java.lang.Object[]) obj).length;
        }
        return -1;
    }

    private static android.content.Context appContext() {
        /*
         * 修复"临时会话长按设置无效 / 设置连带影响所有会话"的根因：
         * 旧实现只用 ActivityThread.currentApplication() 反射，在 Xposed 早期回调
         * 以及部分 ROM 上返回 null，于是所有 SharedPreferences 读写静默失败：
         *   - 写入无效 -> 设置了没反应
         *   - 读取返回空集合 -> 所有会话看起来"都没设置"，一改就"牵连全部"
         * 这里优先用已经可靠登记过的 Ctx，再退回反射。
         */
        try {
            android.content.Context cached = com.tencent.qqlitepro.util.Ctx.get();
            if (cached != null) {
                return cached;
            }
        } catch (java.lang.Throwable ignored) {
        }
        try {
            java.lang.reflect.Method method = java.lang.Class.forName("android.app.ActivityThread").getMethod("currentApplication", new java.lang.Class[0]);
            method.setAccessible(true);
            android.content.Context c = (android.content.Context) method.invoke(null, new java.lang.Object[0]);
            if (c != null) {
                try {
                    com.tencent.qqlitepro.util.Ctx.set(c);
                } catch (java.lang.Throwable ignored) {
                }
            }
            return c;
        } catch (java.lang.Throwable unused) {
            return null;
        }
    }

    private static java.lang.Object qrouteApi(java.lang.String str) throws java.lang.Throwable {
        java.lang.reflect.Method methodFindMethod = findMethod(cls("com.tencent.mobileqq.qroute.QRoute"), "api", java.lang.Class.class);
        if (methodFindMethod == null) {
            throw new java.lang.NoSuchMethodException("QRoute.api(Class) not found");
        }
        methodFindMethod.setAccessible(true);
        return methodFindMethod.invoke(null, cls(str));
    }

    public static java.lang.Object createContact(int i, java.lang.String str) throws java.lang.Throwable {
        return cls("com.tencent.qqnt.kernel.nativeinterface.Contact").getConstructor(java.lang.Integer.TYPE, java.lang.String.class, java.lang.String.class).newInstance(java.lang.Integer.valueOf(i), str, "");
    }

    public static java.lang.Object createTextElement(java.lang.String str) throws java.lang.Throwable {
        java.lang.Object objQrouteApi = qrouteApi("com.tencent.qqnt.msg.api.IMsgUtilApi");
        java.lang.reflect.Method methodFindMethod = findMethod(objQrouteApi.getClass(), "createTextElement", java.lang.String.class);
        if (methodFindMethod == null) {
            methodFindMethod = findMethod(cls("com.tencent.qqnt.msg.api.IMsgUtilApi"), "createTextElement", java.lang.String.class);
        }
        return methodFindMethod.invoke(objQrouteApi, str);
    }

    public static java.lang.Object createFaceElement(int i) throws java.lang.Throwable {
        java.lang.Object objQrouteApi = qrouteApi("com.tencent.qqnt.msg.api.IMsgUtilApi");
        return findMethod(objQrouteApi.getClass(), "createFaceElement", java.lang.Integer.TYPE, java.lang.Integer.TYPE, java.lang.String.class).invoke(objQrouteApi, java.lang.Integer.valueOf(i), 0, "");
    }

    public static java.lang.Object createAtElement(java.lang.String str, java.lang.String str2) {
        if (str != null && !str.isEmpty()) {
            try {
                java.lang.Object objQrouteApi = qrouteApi("com.tencent.qqnt.msg.api.IMsgUtilApi");
                java.lang.reflect.Method methodFindMethod = findMethod(objQrouteApi.getClass(), "createAtTextElement", java.lang.String.class, java.lang.String.class, java.lang.Integer.TYPE);
                if (methodFindMethod != null) {
                    if (str2 == null || str2.isEmpty()) {
                        str2 = str;
                    }
                    return methodFindMethod.invoke(objQrouteApi, "@" + str2 + " ", str, 2);
                }
                java.lang.reflect.Method methodFindMethod2 = findMethod(objQrouteApi.getClass(), "createAtElement", java.lang.String.class, java.lang.String.class);
                if (methodFindMethod2 != null) {
                    java.lang.Object[] objArr = new java.lang.Object[2];
                    objArr[0] = str;
                    if (str2 == null) {
                        str2 = "";
                    }
                    objArr[1] = str2;
                    return methodFindMethod2.invoke(objQrouteApi, objArr);
                }
                java.lang.reflect.Method methodFindMethod3 = findMethod(objQrouteApi.getClass(), "createAtElement", java.lang.String.class);
                if (methodFindMethod3 != null) {
                    return methodFindMethod3.invoke(objQrouteApi, str);
                }
            } catch (java.lang.Throwable th) {
                com.tencent.qqlitepro.util.Log.w("构造艾特元素失败: " + th);
            }
        }
        return null;
    }

    public static java.lang.Object createAtAllElement() {
        try {
            java.lang.Object objQrouteApi = qrouteApi("com.tencent.qqnt.msg.api.IMsgUtilApi");
            java.lang.reflect.Method methodFindMethod = findMethod(objQrouteApi.getClass(), "createAtTextElement", java.lang.String.class, java.lang.String.class, java.lang.Integer.TYPE);
            if (methodFindMethod != null) {
                return methodFindMethod.invoke(objQrouteApi, "@全体成员 ", "all", 1);
            }
            return null;
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("构造艾特全体元素失败: " + th);
            return null;
        }
    }

    public static java.util.ArrayList<java.lang.Object> buildElementsWithAt(java.lang.String str, boolean z, java.lang.String str2, java.lang.String str3) throws java.lang.Throwable {
        java.util.ArrayList<java.lang.Object> arrayList = new java.util.ArrayList<>();
        if (str == null) {
            str = "";
        }
        boolean z2 = (str2 == null || str2.isEmpty()) ? false : true;
        if (z2 && z && !str.contains(AT_TOKEN) && !str.contains(AT_ALL_TOKEN)) {
            str = "{@Sender} " + str;
        }
        int i = 0;
        while (true) {
            int iIndexOf = str.indexOf(AT_TOKEN, i);
            int iIndexOf2 = str.indexOf(AT_ALL_TOKEN, i);
            if (iIndexOf < 0 && iIndexOf2 < 0) {
                break;
            }
            boolean z3 = iIndexOf2 >= 0 && (iIndexOf < 0 || iIndexOf2 < iIndexOf);
            if (z3) {
                iIndexOf = iIndexOf2;
            }
            java.lang.String str4 = z3 ? AT_ALL_TOKEN : AT_TOKEN;
            if (iIndexOf > i) {
                arrayList.add(createTextElement(str.substring(i, iIndexOf)));
            }
            if (z3) {
                java.lang.Object objCreateAtAllElement = createAtAllElement();
                if (objCreateAtAllElement != null) {
                    arrayList.add(objCreateAtAllElement);
                } else {
                    arrayList.add(createTextElement("@全体成员 "));
                }
            } else if (z2) {
                java.lang.Object objCreateAtElement = createAtElement(str2, str3);
                if (objCreateAtElement != null) {
                    arrayList.add(objCreateAtElement);
                } else {
                    java.lang.StringBuilder sb = new java.lang.StringBuilder("@");
                    sb.append((str3 == null || str3.isEmpty()) ? str2 : str3);
                    sb.append(" ");
                    arrayList.add(createTextElement(sb.toString()));
                }
            }
            int length = iIndexOf + str4.length();
            if (!z3 && z2 && (length >= str.length() || !java.lang.Character.isWhitespace(str.charAt(length)))) {
                arrayList.add(createTextElement(" "));
            }
            i = length;
        }
        if (i < str.length()) {
            arrayList.add(createTextElement(str.substring(i)));
        }
        return arrayList;
    }

    private static java.lang.String relocateToRichMediaSandbox(java.lang.String str) {
        java.lang.reflect.Method methodFindMethod;
        java.lang.Object objInvoke;
        try {
            if (!new java.io.File(str).exists()) {
                return null;
            }
            java.lang.String strComputeMd5Hex = computeMd5Hex(new java.io.File(str));
            if (strComputeMd5Hex.isEmpty() || (methodFindMethod = findMethod(cls("com.tencent.qqnt.msg.KernelServiceUtil"), "c", new java.lang.Class[0])) == null || (objInvoke = methodFindMethod.invoke(null, new java.lang.Object[0])) == null) {
                return null;
            }
            java.lang.Class<?> cls = cls("com.tencent.qqnt.kernel.nativeinterface.RichMediaFilePathInfo");
            java.lang.reflect.Method methodFindMethod2 = findMethod(objInvoke.getClass(), "getRichMediaFilePathForMobileQQSend", cls);
            if (methodFindMethod2 == null) {
                methodFindMethod2 = findMethod(cls("com.tencent.qqnt.kernel.api.IMsgService"), "getRichMediaFilePathForMobileQQSend", cls);
            }
            if (methodFindMethod2 == null) {
                return null;
            }
            java.lang.Object objInvoke2 = methodFindMethod2.invoke(objInvoke, cls.getConstructor(java.lang.Integer.TYPE, java.lang.Integer.TYPE, java.lang.String.class, java.lang.String.class, java.lang.Integer.TYPE, java.lang.Integer.TYPE, byte[].class, java.lang.String.class, java.lang.Boolean.TYPE).newInstance(4, 3, strComputeMd5Hex, fileNameOf(str), 1, 0, null, "", true));
            if ((objInvoke2 instanceof java.lang.String) && !((java.lang.String) objInvoke2).isEmpty()) {
                copyTo(str, (java.lang.String) objInvoke2);
                if (new java.io.File((java.lang.String) objInvoke2).exists() && new java.io.File((java.lang.String) objInvoke2).length() > 0) {
                    return (java.lang.String) objInvoke2;
                }
            }
            return null;
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("复制到富媒体沙盒失败，回退原路径: " + th);
        }
        return null;
    }

    private static void copyTo(java.lang.String str, java.lang.String str2) {
        try {
            java.io.File file = new java.io.File(str2);
            java.io.File parentFile = file.getParentFile();
            if (parentFile != null && !parentFile.exists()) {
                parentFile.mkdirs();
            }
            java.io.FileInputStream fileInputStream = new java.io.FileInputStream(str);
            java.io.FileOutputStream fileOutputStream = new java.io.FileOutputStream(file, false);
            try {
                byte[] bArr = new byte[8192];
                while (true) {
                    int i = fileInputStream.read(bArr);
                    if (i > 0) {
                        fileOutputStream.write(bArr, 0, i);
                    } else {
                        break;
                    }
                }
                fileInputStream.close();
                try {
                    fileOutputStream.close();
                } catch (java.lang.Throwable unused2) {
                }
            } catch (java.lang.Throwable th) {
                try {
                    fileInputStream.close();
                } catch (java.lang.Throwable unused3) {
                }
                try {
                    fileOutputStream.close();
                    throw th;
                } catch (java.lang.Throwable unused4) {
                    throw th;
                }
            }
        } catch (java.lang.Throwable th2) {
            com.tencent.qqlitepro.util.Log.w("富媒体沙盒复制失败: " + th2);
        }
    }

    private static java.lang.String computeMd5Hex(java.io.File file) {
        try {
            if (!file.exists()) {
                return "";
            }
            java.security.MessageDigest messageDigest = java.security.MessageDigest.getInstance("MD5");
            java.io.FileInputStream fileInputStream = new java.io.FileInputStream(file);
            try {
                byte[] bArr = new byte[8192];
                while (true) {
                    int i = fileInputStream.read(bArr);
                    if (i > 0) {
                        messageDigest.update(bArr, 0, i);
                    } else {
                        try {
                            break;
                        } catch (java.lang.Throwable unused) {
                        }
                    }
                }
                fileInputStream.close();
                java.lang.StringBuilder sb = new java.lang.StringBuilder();
                for (byte b : messageDigest.digest()) {
                    sb.append(java.lang.String.format("%02x", java.lang.Byte.valueOf(b)));
                }
                return sb.toString();
            } catch (java.lang.Throwable th) {
                try {
                    fileInputStream.close();
                } catch (java.lang.Throwable unused2) {
                }
                throw th;
            }
        } catch (java.lang.Throwable unused3) {
            return "";
        }
    }

    private static java.lang.String fileNameOf(java.lang.String str) {
        if (str == null) {
            return "";
        }
        int iLastIndexOf = str.lastIndexOf(47);
        return iLastIndexOf < 0 ? str : str.substring(iLastIndexOf + 1);
    }

    private static void setField(java.lang.Object obj, java.lang.String str, java.lang.Object obj2) {
        if (obj == null) {
            return;
        }
        for (java.lang.Class<?> superclass = obj.getClass(); superclass != null && superclass != java.lang.Object.class; superclass = superclass.getSuperclass()) {
            try {
                java.lang.reflect.Field declaredField = superclass.getDeclaredField(str);
                declaredField.setAccessible(true);
                declaredField.set(obj, obj2);
                return;
            } catch (java.lang.Throwable unused) {
            }
        }
    }

    public static java.lang.Object createPttElement(java.lang.String str) throws java.lang.Throwable {
        java.lang.reflect.Method method = cls("momoi.mod.qqpro.hook.CameraSendKt").getMethod("buildPttElement", java.lang.String.class, java.lang.Long.TYPE);
        method.setAccessible(true);
        return method.invoke(null, str, -1L);
    }

    public static java.lang.Object createPicElement(java.lang.String str) throws java.lang.Throwable {
        java.lang.Object objQrouteApi = qrouteApi("com.tencent.qqnt.watch.api.IMsgApi");
        java.lang.reflect.Method methodFindMethod = findMethod(objQrouteApi.getClass(), "createPicElement", java.lang.String.class, java.lang.Integer.TYPE);
        if (methodFindMethod == null) {
            methodFindMethod = findMethod(cls("com.tencent.qqnt.watch.api.IMsgApi"), "createPicElement", java.lang.String.class, java.lang.Integer.TYPE);
        }
        if (methodFindMethod != null) {
            return methodFindMethod.invoke(objQrouteApi, str, 0);
        }
        throw new java.lang.NoSuchMethodException("watch IMsgApi.createPicElement(String,int) not found");
    }

    public static void setPicDisplaySize(java.lang.Object obj, int i, int i2) {
        if (obj == null) {
            return;
        }
        try {
            java.lang.Object obj2 = obj.getClass().getField("picElement").get(obj);
            if (obj2 != null) {
                obj = obj2;
            }
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("设置图片显示尺寸失败: " + th);
            return;
        }
        try {
            setIntField(obj, "picWidth", i);
            setIntField(obj, "picHeight", i2);
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("设置图片显示尺寸失败: " + th);
        }
    }

    public static java.lang.Object createReplyElement(java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.String str4, long j, int i) {
        long j2;
        java.lang.String str5 = str3;
        long jCurrentTimeMillis = j;
        if (str == null || str.isEmpty()) {
            return null;
        }
        try {
            j2 = java.lang.Long.parseLong(str.trim());
        } catch (java.lang.Throwable unused) {
            j2 = 0;
        }
        try {
            java.lang.Object objQrouteApi = qrouteApi("com.tencent.qqnt.msg.api.IMsgUtilApi");
            java.lang.reflect.Method methodFindMethod = findMethod(objQrouteApi.getClass(), "createReplyElement", java.lang.Long.TYPE, java.lang.String.class, java.lang.String.class);
            if (methodFindMethod != null) {
                java.lang.Object[] objArr = new java.lang.Object[3];
                objArr[0] = java.lang.Long.valueOf(j2);
                objArr[1] = str2 == null ? "" : str2;
                objArr[2] = str4 == null ? "" : str4;
                java.lang.Object objInvoke = methodFindMethod.invoke(objQrouteApi, objArr);
                if (objInvoke != null) {
                    fillReplyExtra(objInvoke, str5, jCurrentTimeMillis, i);
                    return objInvoke;
                }
            }
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("IMsgUtilApi.createReplyElement 失败，回退手工构造: " + th);
        }
        try {
            java.lang.Class<?> cls = cls("com.tencent.qqnt.kernel.nativeinterface.ReplyElement");
            java.lang.Object objNewInstance = cls.getConstructor(new java.lang.Class[0]).newInstance(new java.lang.Object[0]);
            setField(objNewInstance, "replayMsgId", java.lang.Long.valueOf(j2));
            setField(objNewInstance, "senderUidStr", str2 == null ? "" : str2);
            setField(objNewInstance, "sourceMsgText", str4 == null ? "" : str4);
            if (jCurrentTimeMillis <= 0) {
                jCurrentTimeMillis = java.lang.System.currentTimeMillis() / 1000;
            }
            setField(objNewInstance, "replyMsgTime", java.lang.Long.valueOf(jCurrentTimeMillis));
            if (i >= 0) {
                setField(objNewInstance, "replyMsgRevokeType", java.lang.Integer.valueOf(i));
            }
            try {
                java.lang.reflect.Method method = cls.getMethod("setSenderName", java.lang.String.class);
                method.setAccessible(true);
                java.lang.Object[] objArr2 = new java.lang.Object[1];
                if (str5 == null) {
                    str5 = "";
                }
                objArr2[0] = str5;
                method.invoke(objNewInstance, objArr2);
            } catch (java.lang.Throwable unused2) {
            }
            java.lang.Object objNewMsgElement = newMsgElement();
            setField(objNewMsgElement, "replyElement", objNewInstance);
            return objNewMsgElement;
        } catch (java.lang.Throwable th2) {
            com.tencent.qqlitepro.util.Log.w("构造回复元素失败: " + th2);
            return null;
        }
    }

    private static void fillReplyExtra(java.lang.Object obj, java.lang.String str, long j, int i) {
        java.lang.Object obj2;
        try {
            obj2 = obj.getClass().getField("replyElement").get(obj);
        } catch (java.lang.Throwable unused) {
            obj2 = null;
        }
        if (obj2 == null) {
            return;
        }
        if (str != null) {
            try {
                if (!str.isEmpty()) {
                    try {
                        java.lang.reflect.Method method = obj2.getClass().getMethod("setSenderName", java.lang.String.class);
                        method.setAccessible(true);
                        method.invoke(obj2, str);
                    } catch (java.lang.Throwable unused2) {
                    }
                }
            } catch (java.lang.Throwable unused3) {
                return;
            }
        }
        if (j > 0) {
            setField(obj2, "replyMsgTime", java.lang.Long.valueOf(j));
        }
        if (i >= 0) {
            setField(obj2, "replyMsgRevokeType", java.lang.Integer.valueOf(i));
        }
    }

    public static com.tencent.qqlitepro.http.QqKernel.SendResult sendReplyMsg(java.lang.Object obj, java.util.ArrayList<java.lang.Object> arrayList, java.lang.String str, java.lang.String str2, java.lang.String str3, long j) {
        java.util.ArrayList arrayList2 = new java.util.ArrayList();
        java.lang.Object objCreateReplyElement = createReplyElement(str, str2, str3, null, 0L, -1);
        if (objCreateReplyElement != null) {
            arrayList2.add(objCreateReplyElement);
        }
        if (arrayList != null) {
            arrayList2.addAll(arrayList);
        }
        return sendMsg(obj, arrayList2, j);
    }

    public static java.lang.Object createVideoElement(java.lang.String str) throws java.lang.Throwable {
        if (str == null || str.isEmpty()) {
            return null;
        }
        try {
            java.lang.Object objQrouteApi = qrouteApi("com.tencent.qqnt.msg.api.IMsgUtilApi");
            java.lang.reflect.Method methodFindMethod = findMethod(objQrouteApi.getClass(), "createVideoElement", java.lang.String.class);
            if (methodFindMethod != null) {
                return methodFindMethod.invoke(objQrouteApi, str);
            }
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("IMsgUtilApi createVideoElement 失败: " + th);
        }
        try {
            java.lang.Object objQrouteApi2 = qrouteApi("com.tencent.qqnt.watch.api.IMsgApi");
            java.lang.reflect.Method methodFindMethod2 = findMethod(objQrouteApi2.getClass(), "createVideoElement", java.lang.String.class, java.lang.Integer.TYPE);
            if (methodFindMethod2 == null) {
                methodFindMethod2 = findMethod(cls("com.tencent.qqnt.watch.api.IMsgApi"), "createVideoElement", java.lang.String.class, java.lang.Integer.TYPE);
            }
            if (methodFindMethod2 != null) {
                return methodFindMethod2.invoke(objQrouteApi2, str, 0);
            }
        } catch (java.lang.Throwable th2) {
            com.tencent.qqlitepro.util.Log.w("watch createVideoElement 失败，回退手工构造: " + th2);
        }
        java.lang.Object objNewInstance = cls("com.tencent.qqnt.kernel.nativeinterface.VideoElement").getConstructor(new java.lang.Class[0]).newInstance(new java.lang.Object[0]);
        setField(objNewInstance, "filePath", str);
        setField(objNewInstance, "fileName", new java.io.File(str).getName());
        setIntField(objNewInstance, "fileFormat", 2);
        java.io.File file = new java.io.File(str);
        if (file.exists()) {
            setField(objNewInstance, "fileSize", java.lang.Long.valueOf(file.length()));
            setField(objNewInstance, "videoMd5", computeMd5Hex(file));
        }
        java.lang.Object objNewMsgElement = newMsgElement();
        setIntField(objNewMsgElement, "elementType", 5);
        setField(objNewMsgElement, "videoElement", objNewInstance);
        return objNewMsgElement;
    }

    private static java.lang.Object newMsgElement() throws java.lang.Throwable {
        return cls("com.tencent.qqnt.kernel.nativeinterface.MsgElement").getConstructor(new java.lang.Class[0]).newInstance(new java.lang.Object[0]);
    }

    private static void setIntField(java.lang.Object obj, java.lang.String str, int i) {
        try {
            try {
                java.lang.reflect.Field field = obj.getClass().getField(str);
                field.setAccessible(true);
                field.setInt(obj, i);
            } catch (java.lang.Throwable unused) {
                java.lang.reflect.Field declaredField = obj.getClass().getDeclaredField(str);
                declaredField.setAccessible(true);
                declaredField.setInt(obj, i);
            }
        } catch (java.lang.Throwable unused2) {
        }
    }

    private static java.lang.Object kernelMsgService() {
        java.lang.reflect.Method methodFindMethod;
        try {
            java.lang.reflect.Method method = sKernelGetMsgMethod;
            if (method == null) {
                synchronized (com.tencent.qqlitepro.http.QqKernel.class) {
                    methodFindMethod = sKernelGetMsgMethod;
                    if (methodFindMethod == null) {
                        methodFindMethod = findMethod(cls("com.tencent.qqnt.msg.KernelServiceUtil"), "c", new java.lang.Class[0]);
                        sKernelGetMsgMethod = methodFindMethod;
                    }
                }
                method = methodFindMethod;
            }
            if (method == null) {
                return null;
            }
            return method.invoke(null, new java.lang.Object[0]);
        } catch (java.lang.Throwable unused) {
            return null;
        }
    }

    public static com.tencent.qqlitepro.http.QqKernel.SendResult sendMsg(java.lang.Object obj, java.util.ArrayList<java.lang.Object> arrayList, long j) {
        com.tencent.qqlitepro.http.QqKernel.SendResult sendResult = new com.tencent.qqlitepro.http.QqKernel.SendResult();
        try {
            if (kernelMsgService() == null) {
                sendResult.code = -3;
                sendResult.msg = "内核消息服务未就绪";
                return sendResult;
            }
            java.lang.Object objQrouteApi = qrouteApi("com.tencent.qqnt.msg.api.IMsgService");
            java.lang.Class<?> cls = cls("com.tencent.qqnt.kernel.nativeinterface.IOperateCallback");
            int[] iArr = {-1};
            java.lang.String[] strArr = {""};
            java.util.concurrent.CountDownLatch countDownLatch = new java.util.concurrent.CountDownLatch(1);
            java.lang.Object objNewProxyInstance = java.lang.reflect.Proxy.newProxyInstance(cls.getClassLoader(), new java.lang.Class[]{cls}, new com.tencent.qqlitepro.http.QqKernel$$ExternalSyntheticLambda16(iArr, strArr, countDownLatch));
            java.lang.Class<?> cls2 = cls("com.tencent.qqnt.kernel.nativeinterface.Contact");
            java.lang.reflect.Method methodFindMethod = findMethod(objQrouteApi.getClass(), "sendMsg", cls2, java.util.ArrayList.class, cls);
            if (methodFindMethod == null) {
                methodFindMethod = findMethod(cls("com.tencent.qqnt.msg.api.IMsgService"), "sendMsg", cls2, java.util.ArrayList.class, cls);
            }
            if (methodFindMethod == null) {
                sendResult.msg = "sendMsg(Contact,ArrayList,IOperateCallback) not found";
                return sendResult;
            }
            methodFindMethod.invoke(objQrouteApi, obj, new java.util.ArrayList(arrayList), objNewProxyInstance);
            long jCurrentTimeMillis = java.lang.System.currentTimeMillis() + java.lang.Math.max(j, 1L);
            boolean zAwait = false;
            while (!zAwait && java.lang.System.currentTimeMillis() < jCurrentTimeMillis) {
                zAwait = countDownLatch.await(java.lang.Math.min(1000L, jCurrentTimeMillis - java.lang.System.currentTimeMillis()), java.util.concurrent.TimeUnit.MILLISECONDS);
                if (!zAwait && kernelMsgService() == null) {
                    sendResult.code = -3;
                    sendResult.msg = "等待发送回调时内核服务掉线";
                    return sendResult;
                }
            }
            if (!zAwait) {
                sendResult.timeout = true;
                sendResult.msg = "等待发送回调超时";
            } else {
                sendResult.code = iArr[0];
                sendResult.msg = strArr[0];
            }
            return sendResult;
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("发送消息反射失败: " + th);
            sendResult.msg = java.lang.String.valueOf(th);
            return sendResult;
        }
    }

    static /* synthetic */ java.lang.Object lambda$sendMsg$0(int[] iArr, java.lang.String[] strArr, java.util.concurrent.CountDownLatch countDownLatch, java.lang.Object obj, java.lang.reflect.Method method, java.lang.Object[] objArr) throws java.lang.Throwable {
        java.lang.Object obj2;
        java.lang.Object obj3;
        if (!"onResult".equals(method.getName())) {
            return null;
        }
        if (objArr != null) {
            try {
                if (objArr.length > 0 && (obj2 = objArr[0]) != null) {
                    if (obj2 instanceof java.lang.Integer) {
                        iArr[0] = ((java.lang.Integer) obj2).intValue();
                    } else if (obj2 instanceof java.lang.Number) {
                        iArr[0] = ((java.lang.Number) obj2).intValue();
                    } else {
                        try {
                            iArr[0] = java.lang.Integer.parseInt(java.lang.String.valueOf(obj2).trim());
                        } catch (java.lang.Throwable unused) {
                        }
                    }
                    if (objArr.length > 1 && (obj3 = objArr[1]) != null) {
                        strArr[0] = java.lang.String.valueOf(obj3);
                    }
                }
            } finally {
                countDownLatch.countDown();
            }
        }
        return null;
    }

    public static java.lang.Object groupDetail(java.lang.String str) {
        try {
            java.lang.reflect.Field declaredField = cls("com.tencent.watch.aio_impl.coreImpl.helper.GroupAIOHelper").getDeclaredField("b");
            declaredField.setAccessible(true);
            java.lang.Object obj = declaredField.get(null);
            if (obj instanceof java.util.Map) {
                return ((java.util.Map) obj).get(str);
            }
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("读取群详情缓存失败: " + th);
        }
        return null;
    }

    public static long groupCodeOf(java.lang.String str) {
        try {
            java.lang.Object objGroupDetail = groupDetail(str);
            if (objGroupDetail != null) {
                java.lang.Object objFieldValue = fieldValue(objGroupDetail, "groupCode");
                if ((objFieldValue instanceof java.lang.Number) && ((java.lang.Number) objFieldValue).longValue() != 0) {
                    return ((java.lang.Number) objFieldValue).longValue();
                }
            }
            return java.lang.Long.parseLong(str.trim());
        } catch (java.lang.Throwable unused) {
            return 0L;
        }
    }

    public static boolean isGroupOwnerOrAdmin(java.lang.String str, java.lang.String str2) {
        if (str != null && str2 != null && !str2.trim().isEmpty()) {
            java.lang.String strTrim = str2.trim();
            try {
                java.lang.Object objGroupDetail = groupDetail(str);
                if (objGroupDetail != null) {
                    java.lang.Object objFieldValue = fieldValue(objGroupDetail, "ownerUid");
                    if (objFieldValue != null && strTrim.equals(java.lang.String.valueOf(objFieldValue))) {
                        return true;
                    }
                    java.lang.String strMemberRoleName = memberRoleName(fieldValue(objGroupDetail, "cmdUinPrivilege"));
                    if (("OWNER".equals(strMemberRoleName) || "ADMIN".equals(strMemberRoleName)) && isSelfTarget(strTrim)) {
                        return true;
                    }
                    java.util.ArrayList<java.lang.String> arrayList = new java.util.ArrayList();
                    java.lang.Object objFieldValue2 = fieldValue(objGroupDetail, "ownerUin");
                    if (objFieldValue2 instanceof java.lang.Number) {
                        arrayList.add(java.lang.String.valueOf(((java.lang.Number) objFieldValue2).longValue()));
                    }
                    collectRefs(objGroupDetail, arrayList, "adminUins");
                    java.lang.String[] strArrSelfUidUin = selfUidUin();
                    if (strArrSelfUidUin[1].length() > 0 && strTrim.equals(strArrSelfUidUin[1]) && arrayList.contains(strArrSelfUidUin[1])) {
                        return true;
                    }
                    for (java.lang.String str3 : arrayList) {
                        if (str3.length() > 0 && strTrim.equals(str3)) {
                            return true;
                        }
                    }
                }
                java.lang.String strMemberRoleOfTarget = memberRoleOfTarget(str, strTrim);
                if (!"OWNER".equals(strMemberRoleOfTarget) && !"ADMIN".equals(strMemberRoleOfTarget)) {
                    if (strTrim.startsWith("u_")) {
                        java.lang.String strUidToUin = uidToUin(strTrim);
                        if (!strUidToUin.isEmpty()) {
                            java.lang.String strMemberRoleOfTarget2 = memberRoleOfTarget(str, strUidToUin);
                            if ("OWNER".equals(strMemberRoleOfTarget2) || "ADMIN".equals(strMemberRoleOfTarget2)) {
                                return true;
                            }
                        }
                    }
                    return false;
                }
                return true;
            } catch (java.lang.Throwable unused) {
            }
        }
        return false;
    }

    private static void collectRefs(java.lang.Object obj, java.util.ArrayList<java.lang.String> arrayList, java.lang.String str) {
        java.lang.Object objFieldValue = fieldValue(obj, str);
        if (objFieldValue instanceof java.util.Collection) {
            for (java.lang.Object obj2 : (java.util.Collection) objFieldValue) {
                if (obj2 != null) {
                    arrayList.add(java.lang.String.valueOf(obj2));
                }
            }
            return;
        }
        if (objFieldValue == null || !objFieldValue.getClass().isArray()) {
            return;
        }
        int length = java.lang.reflect.Array.getLength(objFieldValue);
        for (int i = 0; i < length; i++) {
            java.lang.Object obj3 = java.lang.reflect.Array.get(objFieldValue, i);
            if (obj3 != null) {
                arrayList.add(java.lang.String.valueOf(obj3));
            }
        }
    }

    private static java.lang.String memberRoleName(java.lang.Object obj) {
        if (obj == null) {
            return "";
        }
        java.lang.String strValueOf = java.lang.String.valueOf(obj);
        int iLastIndexOf = strValueOf.lastIndexOf(46);
        return iLastIndexOf >= 0 ? strValueOf.substring(iLastIndexOf + 1) : strValueOf;
    }

    private static java.lang.String memberRoleOfTarget(java.lang.String str, java.lang.String str2) {
        java.util.HashMap map;
        java.util.HashMap map2;
        java.util.HashMap map3;
        java.lang.String str3;
        java.util.HashMap map4;
        java.util.HashMap map5;
        java.util.HashMap map6;
        if (str != null && str2 != null && !str2.trim().isEmpty()) {
            java.lang.String strTrim = str2.trim();
            java.lang.String str4 = strTrim.startsWith("u_") ? strTrim : "";
            long jCurrentTimeMillis = java.lang.System.currentTimeMillis();
            java.util.Map<java.lang.String, java.lang.String> map7 = sRoleUid.get(str);
            java.util.Map<java.lang.String, java.lang.String> map8 = sRoleUin.get(str);
            java.lang.Long l = sRoleAt.get(str);
            if (l != null && map7 != null && map8 != null && jCurrentTimeMillis - l.longValue() < 60000) {
                if (!str4.isEmpty()) {
                    java.lang.String str5 = map7.get(str4);
                    if (str5 != null) {
                        return str5;
                    }
                } else {
                    java.lang.String str6 = map8.get(strTrim);
                    if (str6 != null) {
                        return str6;
                    }
                }
            }
            try {
                java.lang.Object objKernelGroupService = kernelGroupService();
                if (objKernelGroupService == null) {
                    return "";
                }
                long jGroupCodeOf = groupCodeOf(str);
                if (jGroupCodeOf == 0) {
                    return "";
                }
                java.lang.Class<?> cls = cls("com.tencent.qqnt.kernel.nativeinterface.IGroupMemberListCallback");
                java.util.HashMap map9 = new java.util.HashMap();
                java.util.HashMap map10 = new java.util.HashMap();
                if (str4.isEmpty()) {
                    map = map10;
                } else {
                    map = map10;
                    fetchMemberRoles(objKernelGroupService, "getMemberInfoForMqq", jGroupCodeOf, str4, false, cls, map9, map);
                    java.lang.String str7 = (java.lang.String) map9.get(str4);
                    if (str7 != null && !str7.isEmpty()) {
                        map9 = map9;
                        mergeRoleCaches(str, map9, map, map7, map8, jCurrentTimeMillis);
                        return str7;
                    }
                }
                map9 = map9;
                map9 = map9;
                if (map9.get(str4) == null) {
                    java.util.HashMap map11 = map;
                    if (map11.get(strTrim) == null) {
                        map = map11;
                        map2 = map9;
                        fetchMemberRoles(objKernelGroupService, "getAllMemberList", jGroupCodeOf, null, false, cls, map2, map);
                    } else {
                        map = map11;
                        map2 = map9;
                    }
                } else {
                    map2 = map9;
                }
                java.lang.String str8 = null;
                if (str4.isEmpty()) {
                    map3 = map2;
                    str3 = null;
                } else {
                    map3 = map2;
                    str3 = (java.lang.String) map3.get(str4);
                }
                if (str3 == null || str3.isEmpty()) {
                    map4 = map;
                    str3 = (java.lang.String) map4.get(strTrim);
                } else {
                    map4 = map;
                }
                if (str3 != null && !str3.isEmpty()) {
                    mergeRoleCaches(str, map3, map4, map7, map8, jCurrentTimeMillis);
                    return str3;
                }
                if (map3.isEmpty() && map4.isEmpty()) {
                    java.util.HashMap map12 = map4;
                    java.util.HashMap map13 = map3;
                    fetchMemberRoles(objKernelGroupService, "getAllMemberList", jGroupCodeOf, null, true, cls, map13, map12);
                    if (str4.isEmpty()) {
                        map3 = map13;
                    } else {
                        map3 = map13;
                        str8 = (java.lang.String) map3.get(str4);
                    }
                    if (str8 == null || str8.isEmpty()) {
                        map4 = map12;
                        str8 = (java.lang.String) map4.get(strTrim);
                    } else {
                        map4 = map12;
                    }
                    if (str8 != null && !str8.isEmpty()) {
                        mergeRoleCaches(str, map3, map4, map7, map8, jCurrentTimeMillis);
                        return str8;
                    }
                }
                if (str4.isEmpty() || map3.get(str4) != null) {
                    map5 = map4;
                    map6 = map3;
                } else {
                    map5 = map4;
                    java.util.HashMap map14 = map3;
                    fetchMemberRoles(objKernelGroupService, "getMemberInfoForMqq", jGroupCodeOf, str4, true, cls, map14, map5);
                    java.lang.String str9 = (java.lang.String) map14.get(str4);
                    if (str9 != null && !str9.isEmpty()) {
                        mergeRoleCaches(str, map14, map5, map7, map8, jCurrentTimeMillis);
                        return str9;
                    }
                    map6 = map14;
                }
                mergeRoleCaches(str, map6, map5, map7, map8, jCurrentTimeMillis);
            } catch (java.lang.Throwable th) {
                com.tencent.qqlitepro.util.Log.w("查询群成员角色失败: " + th);
            }
        }
        return "";
    }

    private static void mergeRoleCaches(java.lang.String str, java.util.Map<java.lang.String, java.lang.String> map, java.util.Map<java.lang.String, java.lang.String> map2, java.util.Map<java.lang.String, java.lang.String> map3, java.util.Map<java.lang.String, java.lang.String> map4, long j) {
        if (map3 != null) {
            map.putAll(map3);
        }
        if (map4 != null) {
            map2.putAll(map4);
        }
        sRoleUid.put(str, map);
        sRoleUin.put(str, map2);
        sRoleAt.put(str, java.lang.Long.valueOf(j));
    }

    private static void fetchMemberRoles(java.lang.Object obj, java.lang.String str, long j, java.lang.String str2, boolean z, java.lang.Class<?> cls, java.util.Map<java.lang.String, java.lang.String> map, java.util.Map<java.lang.String, java.lang.String> map2) {
        java.lang.Object obj2;
        try {
            java.lang.Object[] objArr = new java.lang.Object[1];
            java.util.concurrent.CountDownLatch countDownLatch = new java.util.concurrent.CountDownLatch(1);
            java.lang.Object objNewProxyInstance = java.lang.reflect.Proxy.newProxyInstance(cls.getClassLoader(), new java.lang.Class[]{cls}, new com.tencent.qqlitepro.http.QqKernel$$ExternalSyntheticLambda34(objArr, countDownLatch));
            java.lang.Class<?> cls2 = cls("com.tencent.qqnt.kernel.nativeinterface.IKernelGroupService");
            if ("getMemberInfoForMqq".equals(str)) {
                java.util.ArrayList arrayList = new java.util.ArrayList();
                arrayList.add(str2);
                java.lang.reflect.Method methodFindMethod = findMethod(cls2, "getMemberInfoForMqq", java.lang.Long.TYPE, java.util.ArrayList.class, java.lang.Boolean.TYPE, cls);
                if (methodFindMethod == null) {
                    return;
                } else {
                    methodFindMethod.invoke(obj, java.lang.Long.valueOf(j), arrayList, java.lang.Boolean.valueOf(z), objNewProxyInstance);
                }
            } else {
                java.lang.reflect.Method methodFindMethod2 = findMethod(cls2, "getAllMemberList", java.lang.Long.TYPE, java.lang.Boolean.TYPE, cls);
                if (methodFindMethod2 == null) {
                    return;
                } else {
                    methodFindMethod2.invoke(obj, java.lang.Long.valueOf(j), java.lang.Boolean.valueOf(z), objNewProxyInstance);
                }
            }
            if (countDownLatch.await(6000L, java.util.concurrent.TimeUnit.MILLISECONDS) && (obj2 = objArr[0]) != null) {
                java.lang.Object objFieldValue = fieldValue(obj2, "infos");
                if (objFieldValue instanceof java.util.Map) {
                    for (java.util.Map.Entry<?, ?> entry : ((java.util.Map<?, ?>) objFieldValue).entrySet()) {
                        java.lang.String strMemberRoleName = memberRoleName(fieldValue(entry.getValue(), "role"));
                        if (!strMemberRoleName.isEmpty()) {
                            java.lang.String strValueOf = java.lang.String.valueOf(entry.getKey());
                            if (!strValueOf.isEmpty()) {
                                map.put(strValueOf, strMemberRoleName);
                            }
                            java.lang.Object objFieldValue2 = fieldValue(entry.getValue(), "uin");
                            if ((objFieldValue2 instanceof java.lang.Number) && ((java.lang.Number) objFieldValue2).longValue() > 0) {
                                map2.put(java.lang.String.valueOf(((java.lang.Number) objFieldValue2).longValue()), strMemberRoleName);
                            }
                            java.lang.Object objFieldValue3 = fieldValue(entry.getValue(), "uid");
                            if (objFieldValue3 != null && !java.lang.String.valueOf(objFieldValue3).isEmpty()) {
                                map.put(java.lang.String.valueOf(objFieldValue3), strMemberRoleName);
                            }
                        }
                    }
                }
            }
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("拉取群成员角色失败(" + str + ", fromServer=" + z + "): " + th);
        }
    }

    static /* synthetic */ java.lang.Object lambda$fetchMemberRoles$1(java.lang.Object[] objArr, java.util.concurrent.CountDownLatch countDownLatch, java.lang.Object obj, java.lang.reflect.Method method, java.lang.Object[] objArr2) throws java.lang.Throwable {
        if (!"onResult".equals(method.getName()) || objArr2 == null || objArr2.length < 3) {
            return null;
        }
        objArr[0] = objArr2[2];
        countDownLatch.countDown();
        return null;
    }

    private static java.lang.String[] selfUidUin() {
        java.lang.String strSubstring;
        int i;
        int iIndexOf;
        java.lang.String strGroup = "";
        long jCurrentTimeMillis = java.lang.System.currentTimeMillis();
        if (sSelfCache != null && jCurrentTimeMillis - sSelfCacheAtMs < 60000) {
            return sSelfCache;
        }
        try {
            java.lang.String strSelfInfoJson = selfInfoJson();
            int iIndexOf2 = strSelfInfoJson.indexOf("\"uid\":\"");
            strSubstring = (iIndexOf2 < 0 || (iIndexOf = strSelfInfoJson.indexOf(34, (i = iIndexOf2 + 7))) <= i) ? "" : strSelfInfoJson.substring(i, iIndexOf);
            try {
                java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("\"uin\":(\\d+)").matcher(strSelfInfoJson);
                if (matcher.find()) {
                    strGroup = matcher.group(1);
                }
            } catch (java.lang.Throwable unused) {
            }
        } catch (java.lang.Throwable unused2) {
            strSubstring = "";
        }
        sSelfCache = new java.lang.String[]{strSubstring, strGroup};
        sSelfCacheAtMs = jCurrentTimeMillis;
        return sSelfCache;
    }

    public static boolean isSelfTarget(java.lang.String str) {
        if (str != null && !str.isEmpty()) {
            java.lang.String[] strArrSelfUidUin = selfUidUin();
            java.lang.String str2 = strArrSelfUidUin[0];
            java.lang.String str3 = strArrSelfUidUin[1];
            if (!str2.isEmpty() && str2.equals(str)) {
                return true;
            }
            if (!str3.isEmpty() && str3.equals(str)) {
                return true;
            }
            if (!str3.isEmpty() && str.startsWith("u_")) {
                java.lang.String strUidToUin = uidToUin(str);
                if (!strUidToUin.isEmpty() && str3.equals(strUidToUin)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static java.lang.String selfUid() {
        return selfUidUin()[0];
    }

    public static java.lang.String selfUin() {
        return selfUidUin()[1];
    }

    private static java.lang.String firstField(java.lang.Object obj, java.lang.String... strArr) {
        for (java.lang.String str : strArr) {
            java.lang.Object objFieldValue = fieldValue(obj, str);
            if (objFieldValue != null) {
                java.lang.String strValueOf = java.lang.String.valueOf(objFieldValue);
                if (!strValueOf.isEmpty()) {
                    return strValueOf;
                }
            }
        }
        return "";
    }

    private static java.lang.String joinJsonArray(java.lang.Object obj, java.lang.String str) {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        try {
            java.lang.Object objFieldValue = fieldValue(obj, str);
            java.util.ArrayList arrayList = new java.util.ArrayList();
            if (objFieldValue instanceof java.util.Collection) {
                arrayList.addAll((java.util.Collection) objFieldValue);
            } else if (objFieldValue != null && objFieldValue.getClass().isArray()) {
                int length = java.lang.reflect.Array.getLength(objFieldValue);
                for (int i = 0; i < length; i++) {
                    arrayList.add(java.lang.reflect.Array.get(objFieldValue, i));
                }
            }
            for (int i2 = 0; i2 < arrayList.size(); i2++) {
                if (i2 > 0) {
                    sb.append(",");
                }
                sb.append("\"");
                sb.append(jsonEsc(java.lang.String.valueOf(arrayList.get(i2))));
                sb.append("\"");
            }
        } catch (java.lang.Throwable unused) {
        }
        return sb.toString();
    }

    public static java.lang.String groupInfoJson(java.lang.String str) {
        boolean z;
        try {
            java.lang.Object objGroupDetail = groupDetail(str);
            if (objGroupDetail == null) {
                return "{}";
            }
            java.lang.StringBuilder sb = new java.lang.StringBuilder("{");
            java.lang.String strFirstField = firstField(objGroupDetail, "groupCode");
            if (strFirstField.isEmpty()) {
                z = true;
            } else {
                sb.append("\"groupCode\":\"");
                sb.append(strFirstField);
                sb.append("\"");
                z = false;
            }
            java.lang.String strFirstField2 = firstField(objGroupDetail, com.sun.mail.imap.IMAPStore.ID_NAME, "groupName", "peerName");
            if (!strFirstField2.isEmpty()) {
                if (!z) {
                    sb.append(",");
                }
                sb.append("\"name\":\"");
                sb.append(jsonEsc(strFirstField2));
                sb.append("\"");
                z = false;
            }
            java.lang.String strFirstField3 = firstField(objGroupDetail, "ownerUid");
            if (!strFirstField3.isEmpty()) {
                if (!z) {
                    sb.append(",");
                }
                sb.append("\"ownerUid\":\"");
                sb.append(jsonEsc(strFirstField3));
                sb.append("\"");
                z = false;
            }
            java.lang.String strFirstField4 = firstField(objGroupDetail, "ownerUin");
            if (!strFirstField4.isEmpty()) {
                if (!z) {
                    sb.append(",");
                }
                sb.append("\"ownerUin\":\"");
                sb.append(strFirstField4);
                sb.append("\"");
            } else {
                if (!z) {
                }
                sb.append("\"adminUins\":[");
                sb.append(joinJsonArray(objGroupDetail, "adminUins"));
                sb.append("]}");
                return sb.toString();
            }
            sb.append(",");
            sb.append("\"adminUins\":[");
            sb.append(joinJsonArray(objGroupDetail, "adminUins"));
            sb.append("]}");
            return sb.toString();
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("群详情序列化失败: " + th);
            return "{}";
        }
    }

    private static java.lang.String uidToUin(java.lang.String str) {
        java.lang.Object obj;
        if (str != null && !str.isEmpty() && str.startsWith("u_")) {
            try {
                java.lang.Object objQrouteApi = qrouteApi("com.tencent.qqnt.watch.contact.api.IContactRuntimeService");
                if (objQrouteApi == null) {
                    return "";
                }
                java.lang.StringBuilder sb = new java.lang.StringBuilder();
                java.lang.String[] strArr = {"uid2UinMap", "uidUinMap", "uidToUinMap", "uinMap"};
                for (int i = 0; i < 4; i++) {
                    try {
                        java.lang.Object objFieldValue = fieldValue(objQrouteApi, strArr[i]);
                        if ((objFieldValue instanceof java.util.Map) && (obj = ((java.util.Map) objFieldValue).get(str)) != null && !java.lang.String.valueOf(obj).isEmpty()) {
                            sb.append(java.lang.String.valueOf(obj));
                            break;
                        }
                    } catch (java.lang.Throwable unused) {
                    }
                }
                return sb.toString();
            } catch (java.lang.Throwable unused2) {
            }
        }
        return "";
    }

    private static java.lang.Object invokeNoArgs(java.lang.Object obj, java.lang.String str) {
        try {
            java.lang.reflect.Method method = obj.getClass().getMethod(str, new java.lang.Class[0]);
            method.setAccessible(true);
            return method.invoke(obj, new java.lang.Object[0]);
        } catch (java.lang.Throwable unused) {
            for (java.lang.Class<?> superclass = obj.getClass(); superclass != null && superclass != java.lang.Object.class; superclass = superclass.getSuperclass()) {
                try {
                    java.lang.reflect.Method declaredMethod = superclass.getDeclaredMethod(str, new java.lang.Class[0]);
                    declaredMethod.setAccessible(true);
                    return declaredMethod.invoke(obj, new java.lang.Object[0]);
                } catch (java.lang.Throwable unused2) {
                }
            }
            return null;
        }
    }

    private static java.lang.String jsonEsc(java.lang.String str) {
        if (str == null) {
            return "";
        }
        java.lang.StringBuilder sb = new java.lang.StringBuilder(str.length() + 8);
        for (int i = 0; i < str.length(); i++) {
            char cCharAt = str.charAt(i);
            if (cCharAt == '\t') {
                sb.append("\\t");
            } else if (cCharAt == '\n') {
                sb.append("\\n");
            } else if (cCharAt == '\r') {
                sb.append("\\r");
            } else if (cCharAt == '\"') {
                sb.append("\\\"");
            } else if (cCharAt == '\\') {
                sb.append("\\\\");
            } else if (cCharAt < ' ') {
                sb.append(java.lang.String.format("\\u%04x", java.lang.Integer.valueOf(cCharAt)));
            } else {
                sb.append(cCharAt);
            }
        }
        return sb.toString();
    }

    public static java.lang.String selfInfoJson() {
        java.lang.String strValueOf;
        java.lang.String strValueOf2;
        java.lang.String strValueOf3;
        try {
            java.lang.Class<?> cls = cls("mqq.app.MobileQQ");
            java.lang.Object objInvoke = cls.getMethod("getMobileQQ", new java.lang.Class[0]).invoke(null, new java.lang.Object[0]);
            if (objInvoke == null) {
                return "{}";
            }
            java.lang.Object objInvoke2 = cls.getMethod("waitAppRuntime", new java.lang.Class[0]).invoke(objInvoke, new java.lang.Object[0]);
            java.lang.String strReplaceAll = "";
            if (objInvoke2 != null) {
                java.lang.Object objInvokeNoArgs = invokeNoArgs(objInvoke2, "getCurrentUin");
                strValueOf2 = objInvokeNoArgs != null ? java.lang.String.valueOf(objInvokeNoArgs) : "";
                java.lang.Object objInvokeNoArgs2 = invokeNoArgs(objInvoke2, "getCurrentUid");
                strValueOf3 = objInvokeNoArgs2 != null ? java.lang.String.valueOf(objInvokeNoArgs2) : "";
                java.lang.Object objInvokeNoArgs3 = invokeNoArgs(objInvoke2, "getCurrentNick");
                strValueOf = objInvokeNoArgs3 != null ? java.lang.String.valueOf(objInvokeNoArgs3) : "";
            } else {
                strValueOf = "";
                strValueOf2 = strValueOf;
                strValueOf3 = strValueOf2;
            }
            if (strValueOf2 != null) {
                strReplaceAll = strValueOf2.replaceAll("[^0-9]", "");
            }
            return "{\"uid\":\"" + jsonEsc(strValueOf3) + "\",\"uin\":" + (strReplaceAll.isEmpty() ? 0L : java.lang.Long.parseLong(strReplaceAll)) + ",\"nick\":\"" + jsonEsc(strValueOf) + "\"}";
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("获取自身信息失败: " + th);
            return "{}";
        }
    }

    public static com.tencent.qqlitepro.http.QqKernel.SendResult recallMsg(java.lang.String str, long j, long j2) {
        com.tencent.qqlitepro.http.QqKernel.SendResult sendResult = new com.tencent.qqlitepro.http.QqKernel.SendResult();
        try {
            java.lang.reflect.Method methodFindMethod = findMethod(cls("com.tencent.qqnt.msg.KernelServiceUtil"), "g", new java.lang.Class[0]);
            if (methodFindMethod == null) {
                sendResult.msg = "KernelServiceUtil.g()(IQQNTWrapperSession) 未找到";
                return sendResult;
            }
            java.lang.Object objInvoke = methodFindMethod.invoke(null, new java.lang.Object[0]);
            if (objInvoke == null) {
                sendResult.msg = "IQQNTWrapperSession 为 null";
                return sendResult;
            }
            java.lang.reflect.Method methodFindMethod2 = findMethod(objInvoke.getClass(), "getMsgService", new java.lang.Class[0]);
            if (methodFindMethod2 == null) {
                sendResult.msg = "IQQNTWrapperSession.getMsgService() 未找到";
                return sendResult;
            }
            java.lang.Object objInvoke2 = methodFindMethod2.invoke(objInvoke, new java.lang.Object[0]);
            if (objInvoke2 == null) {
                sendResult.msg = "IKernelMsgService 为 null";
                return sendResult;
            }
            java.lang.Object objCreateContact = createContact((str == null || !str.matches("\\d+")) ? 1 : 2, str);
            java.lang.Class<?> cls = cls("com.tencent.qqnt.kernel.nativeinterface.Contact");
            java.lang.Class<?> cls2 = cls("com.tencent.qqnt.kernel.nativeinterface.IOperateCallback");
            int[] iArr = {-1};
            java.lang.String[] strArr = {""};
            java.util.concurrent.CountDownLatch countDownLatch = new java.util.concurrent.CountDownLatch(1);
            java.lang.Object objNewProxyInstance = java.lang.reflect.Proxy.newProxyInstance(cls2.getClassLoader(), new java.lang.Class[]{cls2}, new com.tencent.qqlitepro.http.QqKernel$$ExternalSyntheticLambda10(iArr, strArr, countDownLatch));
            java.util.ArrayList arrayList = new java.util.ArrayList();
            arrayList.add(java.lang.Long.valueOf(j));
            java.lang.reflect.Method methodFindMethod3 = findMethod(cls("com.tencent.qqnt.kernel.nativeinterface.IKernelMsgService"), "recallMsg", cls, java.util.ArrayList.class, cls2);
            if (methodFindMethod3 == null) {
                methodFindMethod3 = findMethod(objInvoke2.getClass(), "recallMsg", cls, java.util.ArrayList.class, cls2);
            }
            if (methodFindMethod3 == null) {
                sendResult.msg = "IKernelMsgService.recallMsg(Contact,ArrayList<Long>,IOperateCallback) 未找到";
                return sendResult;
            }
            methodFindMethod3.invoke(objInvoke2, objCreateContact, arrayList, objNewProxyInstance);
            if (!countDownLatch.await(j2, java.util.concurrent.TimeUnit.MILLISECONDS)) {
                sendResult.timeout = true;
                sendResult.msg = "等待 recallMsg 回调超时";
            } else {
                sendResult.code = iArr[0];
                sendResult.msg = strArr[0];
            }
            return sendResult;
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("撤回消息反射失败: " + th);
            sendResult.msg = java.lang.String.valueOf(th);
            return sendResult;
        }
    }

    static /* synthetic */ java.lang.Object lambda$recallMsg$2(int[] iArr, java.lang.String[] strArr, java.util.concurrent.CountDownLatch countDownLatch, java.lang.Object obj, java.lang.reflect.Method method, java.lang.Object[] objArr) throws java.lang.Throwable {
        java.lang.Object obj2;
        int iLongValue;
        if (!"onResult".equals(method.getName()) || objArr == null || objArr.length <= 0) {
            return null;
        }
        java.lang.Object obj3 = objArr[0];
        if (obj3 != null) {
            if (obj3 instanceof java.lang.Integer) {
                iLongValue = ((java.lang.Integer) obj3).intValue();
            } else {
                iLongValue = obj3 instanceof java.lang.Number ? (int) ((java.lang.Number) obj3).longValue() : -1;
            }
            iArr[0] = iLongValue;
        }
        if (objArr.length > 1 && (obj2 = objArr[1]) != null) {
            strArr[0] = java.lang.String.valueOf(obj2);
        }
        countDownLatch.countDown();
        return null;
    }

    private static java.lang.Object kernelGroupService() throws java.lang.Throwable {
        java.lang.reflect.Method methodFindMethod = findMethod(cls("com.tencent.qqnt.msg.KernelServiceUtil"), "g", new java.lang.Class[0]);
        if (methodFindMethod == null) {
            throw new java.lang.NoSuchMethodException("KernelServiceUtil.g()(IQQNTWrapperSession) not found");
        }
        java.lang.Object objInvoke = methodFindMethod.invoke(null, new java.lang.Object[0]);
        if (objInvoke == null) {
            return null;
        }
        java.lang.reflect.Method methodFindMethod2 = findMethod(objInvoke.getClass(), "getGroupService", new java.lang.Class[0]);
        if (methodFindMethod2 == null) {
            throw new java.lang.NoSuchMethodException("IQQNTWrapperSession.getGroupService() not found");
        }
        return methodFindMethod2.invoke(objInvoke, new java.lang.Object[0]);
    }

    private static java.lang.String rawDetailString(java.lang.Object obj) {
        if (obj == null) {
            return "null";
        }
        try {
            java.lang.reflect.Method method = obj.getClass().getMethod("toJson", new java.lang.Class[0]);
            method.setAccessible(true);
            java.lang.Object objInvoke = method.invoke(obj, new java.lang.Object[0]);
            if (objInvoke != null) {
                return java.lang.String.valueOf(objInvoke);
            }
        } catch (java.lang.Throwable unused) {
        }
        java.lang.Object objFieldValue = fieldValue(obj, "groupCode");
        java.lang.Object objFieldValue2 = fieldValue(obj, "shutUpAllTimestamp");
        return "{\"groupCode\":" + (objFieldValue instanceof java.lang.Number ? ((java.lang.Number) objFieldValue).longValue() : 0L) + ",\"shutUpAllTimestamp\":" + (objFieldValue2 instanceof java.lang.Number ? ((java.lang.Number) objFieldValue2).longValue() : 0L) + "}";
    }

    public static com.tencent.qqlitepro.http.QqKernel.BanResult banAllStatus(java.lang.String str) {
        com.tencent.qqlitepro.http.QqKernel.BanResult banResult = new com.tencent.qqlitepro.http.QqKernel.BanResult();
        try {
            java.lang.Object objGroupDetail = groupDetail(str);
            java.lang.Object objFieldValue = fieldValue(objGroupDetail, "shutUpAllTimestamp");
            banResult.shutUpAllTimestamp = objFieldValue instanceof java.lang.Number ? ((java.lang.Number) objFieldValue).longValue() : 0L;
            int i = 1;
            banResult.muted = banResult.shutUpAllTimestamp != 0;
            banResult.groupCode = java.lang.String.valueOf(groupCodeOf(str));
            banResult.rawJson = rawDetailString(objGroupDetail);
            if (objGroupDetail != null) {
                i = 0;
            }
            banResult.code = i;
            banResult.msg = banResult.muted ? "当前群已开启全员禁言" : "当前群未全员禁言";
            if (objGroupDetail == null) {
                banResult.msg = "暂未获取到该群详情缓存（可先进入该群聊天触发加载）";
            }
            return banResult;
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("查询全员禁言状态失败: " + th);
            banResult.msg = java.lang.String.valueOf(th);
            return banResult;
        }
    }

    public static com.tencent.qqlitepro.http.QqKernel.BanResult setGroupShutUp(java.lang.String str, boolean z, long j) {
        java.lang.String str2 = "";
        com.tencent.qqlitepro.http.QqKernel.BanResult banResult = new com.tencent.qqlitepro.http.QqKernel.BanResult();
        try {
            java.lang.Object objKernelGroupService = kernelGroupService();
            if (objKernelGroupService == null) {
                banResult.msg = "IKernelGroupService 为 null（内核未就绪）";
                return banResult;
            }
            long jGroupCodeOf = groupCodeOf(str);
            if (jGroupCodeOf == 0) {
                banResult.msg = "无法解析群号 groupCode（groupId 不是数字且无群详情）";
                return banResult;
            }
            banResult.groupCode = java.lang.String.valueOf(jGroupCodeOf);
            java.lang.Class<?> cls = cls("com.tencent.qqnt.kernel.nativeinterface.IOperateCallback");
            int[] iArr = {-1};
            java.lang.String[] strArr = {""};
            java.util.concurrent.CountDownLatch countDownLatch = new java.util.concurrent.CountDownLatch(1);
            java.lang.Object objNewProxyInstance = java.lang.reflect.Proxy.newProxyInstance(cls.getClassLoader(), new java.lang.Class[]{cls}, new com.tencent.qqlitepro.http.QqKernel$$ExternalSyntheticLambda31(iArr, strArr, countDownLatch));
            java.lang.reflect.Method methodFindMethod = findMethod(cls("com.tencent.qqnt.kernel.nativeinterface.IKernelGroupService"), "setGroupShutUp", java.lang.Long.TYPE, java.lang.Boolean.TYPE, cls);
            if (methodFindMethod == null) {
                methodFindMethod = findMethod(objKernelGroupService.getClass(), "setGroupShutUp", java.lang.Long.TYPE, java.lang.Boolean.TYPE, cls);
            }
            if (methodFindMethod == null) {
                banResult.msg = "setGroupShutUp(long,boolean,IOperateCallback) 未找到";
                return banResult;
            }
            methodFindMethod.invoke(objKernelGroupService, java.lang.Long.valueOf(jGroupCodeOf), java.lang.Boolean.valueOf(z), objNewProxyInstance);
            if (!countDownLatch.await(j, java.util.concurrent.TimeUnit.MILLISECONDS)) {
                banResult.timeout = true;
                banResult.msg = "等待 setGroupShutUp 回调超时";
            } else {
                banResult.code = iArr[0];
                banResult.msg = strArr[0];
            }
            java.lang.StringBuilder sb = new java.lang.StringBuilder("{\"result\":");
            sb.append(iArr[0]);
            sb.append(",\"errMsg\":\"");
            java.lang.String str3 = strArr[0];
            if (str3 != null) {
                str2 = str3;
            }
            sb.append(str2);
            sb.append("\"}");
            banResult.rawJson = sb.toString();
            return banResult;
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("setGroupShutUp 反射失败: " + th);
            banResult.msg = java.lang.String.valueOf(th);
            return banResult;
        }
    }

    static /* synthetic */ java.lang.Object lambda$setGroupShutUp$3(int[] iArr, java.lang.String[] strArr, java.util.concurrent.CountDownLatch countDownLatch, java.lang.Object obj, java.lang.reflect.Method method, java.lang.Object[] objArr) throws java.lang.Throwable {
        java.lang.Object obj2;
        int iLongValue;
        if (!"onResult".equals(method.getName()) || objArr == null || objArr.length <= 0) {
            return null;
        }
        java.lang.Object obj3 = objArr[0];
        if (obj3 != null) {
            if (obj3 instanceof java.lang.Integer) {
                iLongValue = ((java.lang.Integer) obj3).intValue();
            } else {
                iLongValue = obj3 instanceof java.lang.Number ? (int) ((java.lang.Number) obj3).longValue() : -1;
            }
            iArr[0] = iLongValue;
        }
        if (objArr.length > 1 && (obj2 = objArr[1]) != null) {
            strArr[0] = java.lang.String.valueOf(obj2);
        }
        countDownLatch.countDown();
        return null;
    }

    public static com.tencent.qqlitepro.http.QqKernel.BanResult modifyGroupName(java.lang.String str, java.lang.String str2, boolean z, long j) {
        java.lang.String str3 = "";
        com.tencent.qqlitepro.http.QqKernel.BanResult banResult = new com.tencent.qqlitepro.http.QqKernel.BanResult();
        try {
            java.lang.Object objKernelGroupService = kernelGroupService();
            if (objKernelGroupService == null) {
                banResult.msg = "IKernelGroupService 为 null（内核未就绪）";
                return banResult;
            }
            long jGroupCodeOf = groupCodeOf(str);
            if (jGroupCodeOf == 0) {
                banResult.msg = "无法解析群号 groupCode（groupId 不是数字且无群详情）";
                return banResult;
            }
            banResult.groupCode = java.lang.String.valueOf(jGroupCodeOf);
            java.lang.Class<?> cls = cls("com.tencent.qqnt.kernel.nativeinterface.IOperateCallback");
            int[] iArr = {-1};
            java.lang.String[] strArr = {""};
            java.util.concurrent.CountDownLatch countDownLatch = new java.util.concurrent.CountDownLatch(1);
            java.lang.Object objNewProxyInstance = java.lang.reflect.Proxy.newProxyInstance(cls.getClassLoader(), new java.lang.Class[]{cls}, new com.tencent.qqlitepro.http.QqKernel$$ExternalSyntheticLambda32(iArr, strArr, countDownLatch));
            java.lang.reflect.Method methodFindMethod = findMethod(cls("com.tencent.qqnt.kernel.nativeinterface.IKernelGroupService"), "modifyGroupName", java.lang.Long.TYPE, java.lang.String.class, java.lang.Boolean.TYPE, cls);
            if (methodFindMethod == null) {
                methodFindMethod = findMethod(objKernelGroupService.getClass(), "modifyGroupName", java.lang.Long.TYPE, java.lang.String.class, java.lang.Boolean.TYPE, cls);
            }
            if (methodFindMethod == null) {
                banResult.msg = "modifyGroupName(long,String,boolean,IOperateCallback) 未找到";
                return banResult;
            }
            methodFindMethod.invoke(objKernelGroupService, java.lang.Long.valueOf(jGroupCodeOf), str2, java.lang.Boolean.valueOf(z), objNewProxyInstance);
            if (!countDownLatch.await(j, java.util.concurrent.TimeUnit.MILLISECONDS)) {
                banResult.timeout = true;
                banResult.msg = "等待 modifyGroupName 回调超时";
            } else {
                banResult.code = iArr[0];
                banResult.msg = strArr[0];
            }
            java.lang.StringBuilder sb = new java.lang.StringBuilder("{\"result\":");
            sb.append(iArr[0]);
            sb.append(",\"errMsg\":\"");
            java.lang.String str4 = strArr[0];
            if (str4 != null) {
                str3 = str4;
            }
            sb.append(str3);
            sb.append("\"}");
            banResult.rawJson = sb.toString();
            return banResult;
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("modifyGroupName 反射失败: " + th);
            banResult.msg = java.lang.String.valueOf(th);
            return banResult;
        }
    }

    static /* synthetic */ java.lang.Object lambda$modifyGroupName$4(int[] iArr, java.lang.String[] strArr, java.util.concurrent.CountDownLatch countDownLatch, java.lang.Object obj, java.lang.reflect.Method method, java.lang.Object[] objArr) throws java.lang.Throwable {
        java.lang.Object obj2;
        int iLongValue;
        if (!"onResult".equals(method.getName()) || objArr == null || objArr.length <= 0) {
            return null;
        }
        java.lang.Object obj3 = objArr[0];
        if (obj3 != null) {
            if (obj3 instanceof java.lang.Integer) {
                iLongValue = ((java.lang.Integer) obj3).intValue();
            } else {
                iLongValue = obj3 instanceof java.lang.Number ? (int) ((java.lang.Number) obj3).longValue() : -1;
            }
            iArr[0] = iLongValue;
        }
        if (objArr.length > 1 && (obj2 = objArr[1]) != null) {
            strArr[0] = java.lang.String.valueOf(obj2);
        }
        countDownLatch.countDown();
        return null;
    }

    private static java.lang.String uinToUid(java.lang.String str) {
        java.lang.Object obj;
        if (str != null && !str.isEmpty()) {
            try {
                java.lang.Object objQrouteApi = qrouteApi("com.tencent.qqnt.watch.contact.api.IContactRuntimeService");
                if (objQrouteApi == null) {
                    return "";
                }
                java.lang.String[] strArr = {"uin2UidMap", "uinUidMap", "uinToUidMap", "uidMap"};
                for (int i = 0; i < 4; i++) {
                    try {
                        java.lang.Object objFieldValue = fieldValue(objQrouteApi, strArr[i]);
                        if ((objFieldValue instanceof java.util.Map) && (obj = ((java.util.Map) objFieldValue).get(str)) != null && !java.lang.String.valueOf(obj).isEmpty()) {
                            return java.lang.String.valueOf(obj);
                        }
                    } catch (java.lang.Throwable unused) {
                    }
                }
            } catch (java.lang.Throwable th) {
                com.tencent.qqlitepro.util.Log.w("uin→uid 换算失败: " + th);
            }
        }
        return "";
    }

    public static com.tencent.qqlitepro.http.QqKernel.BanResult kickMember(java.lang.String str, java.lang.String str2, java.lang.String str3) {
        com.tencent.qqlitepro.http.QqKernel.BanResult banResult = new com.tencent.qqlitepro.http.QqKernel.BanResult();
        try {
            java.lang.Object objKernelGroupService = kernelGroupService();
            if (objKernelGroupService == null) {
                banResult.msg = "IKernelGroupService 为 null（内核未就绪）";
                return banResult;
            }
            long jGroupCodeOf = groupCodeOf(str);
            if (jGroupCodeOf == 0) {
                banResult.msg = "无法解析群号 groupCode（groupId 不是数字且无群详情）";
                return banResult;
            }
            banResult.groupCode = java.lang.String.valueOf(jGroupCodeOf);
            java.lang.String str4 = "";
            java.lang.String strTrim = (str2 == null || str2.trim().isEmpty()) ? "" : str2.trim();
            if (strTrim.matches("\\d+")) {
                strTrim = uinToUid(strTrim);
                if (strTrim.isEmpty()) {
                    banResult.msg = "纯数字 uin 无法换算为 uid（该成员可能不是好友/未在缓存中），请传 u_ 开头的 uid";
                    banResult.code = -6;
                    return banResult;
                }
            }
            if (!strTrim.startsWith("u_")) {
                banResult.msg = "memberUidOrUin 无法识别（需 u_ 开头 uid 或纯数字 uin）";
                banResult.code = -6;
                return banResult;
            }
            java.lang.Class<?> cls = cls("com.tencent.qqnt.kernel.nativeinterface.IKickMemberOperateCallback");
            int[] iArr = {-1};
            java.lang.String[] strArr = {""};
            java.util.concurrent.CountDownLatch countDownLatch = new java.util.concurrent.CountDownLatch(1);
            java.lang.Object objNewProxyInstance = java.lang.reflect.Proxy.newProxyInstance(cls.getClassLoader(), new java.lang.Class[]{cls}, new com.tencent.qqlitepro.http.QqKernel$$ExternalSyntheticLambda6(iArr, strArr, countDownLatch));
            java.util.ArrayList arrayList = new java.util.ArrayList();
            arrayList.add(strTrim);
            java.lang.reflect.Method methodFindMethod = findMethod(cls("com.tencent.qqnt.kernel.nativeinterface.IKernelGroupService"), "kickMember", java.lang.Long.TYPE, java.util.ArrayList.class, java.lang.Boolean.TYPE, java.lang.String.class, cls);
            if (methodFindMethod == null) {
                methodFindMethod = findMethod(objKernelGroupService.getClass(), "kickMember", java.lang.Long.TYPE, java.util.ArrayList.class, java.lang.Boolean.TYPE, java.lang.String.class, cls);
            }
            if (methodFindMethod == null) {
                banResult.msg = "kickMember(long,ArrayList<String>,boolean,String,IKickMemberOperateCallback) 未找到";
                return banResult;
            }
            java.lang.Object[] objArr = new java.lang.Object[5];
            objArr[0] = java.lang.Long.valueOf(jGroupCodeOf);
            objArr[1] = arrayList;
            objArr[2] = false;
            if (str3 != null) {
                str4 = str3;
            }
            objArr[3] = str4;
            objArr[4] = objNewProxyInstance;
            methodFindMethod.invoke(objKernelGroupService, objArr);
            if (!countDownLatch.await(8000L, java.util.concurrent.TimeUnit.MILLISECONDS)) {
                banResult.timeout = true;
                banResult.msg = "等待 kickMember 回调超时";
            } else {
                banResult.code = iArr[0];
                banResult.msg = strArr[0];
            }
            return banResult;
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("kickMember 反射失败: " + th);
            banResult.msg = java.lang.String.valueOf(th);
            return banResult;
        }
    }

    static /* synthetic */ java.lang.Object lambda$kickMember$5(int[] iArr, java.lang.String[] strArr, java.util.concurrent.CountDownLatch countDownLatch, java.lang.Object obj, java.lang.reflect.Method method, java.lang.Object[] objArr) throws java.lang.Throwable {
        java.lang.Object obj2;
        int iLongValue;
        if (!"onResult".equals(method.getName()) || objArr == null || objArr.length <= 0) {
            return null;
        }
        java.lang.Object obj3 = objArr[0];
        if (obj3 != null) {
            if (obj3 instanceof java.lang.Integer) {
                iLongValue = ((java.lang.Integer) obj3).intValue();
            } else {
                iLongValue = obj3 instanceof java.lang.Number ? (int) ((java.lang.Number) obj3).longValue() : -1;
            }
            iArr[0] = iLongValue;
        }
        if (objArr.length > 1 && (obj2 = objArr[1]) != null) {
            strArr[0] = java.lang.String.valueOf(obj2);
        }
        countDownLatch.countDown();
        return null;
    }

    public static com.tencent.qqlitepro.http.QqKernel.BanResult modifyGroupRemark(java.lang.String str, java.lang.String str2) {
        java.lang.String str3 = "";
        com.tencent.qqlitepro.http.QqKernel.BanResult banResult = new com.tencent.qqlitepro.http.QqKernel.BanResult();
        try {
            java.lang.Object objKernelGroupService = kernelGroupService();
            if (objKernelGroupService == null) {
                banResult.msg = "IKernelGroupService 为 null（内核未就绪）";
                return banResult;
            }
            long jGroupCodeOf = groupCodeOf(str);
            if (jGroupCodeOf == 0) {
                banResult.msg = "无法解析群号 groupCode（groupId 不是数字且无群详情）";
                return banResult;
            }
            banResult.groupCode = java.lang.String.valueOf(jGroupCodeOf);
            java.lang.Class<?> cls = cls("com.tencent.qqnt.kernel.nativeinterface.IOperateCallback");
            int[] iArr = {-1};
            java.lang.String[] strArr = {""};
            java.util.concurrent.CountDownLatch countDownLatch = new java.util.concurrent.CountDownLatch(1);
            java.lang.Object objNewProxyInstance = java.lang.reflect.Proxy.newProxyInstance(cls.getClassLoader(), new java.lang.Class[]{cls}, new com.tencent.qqlitepro.http.QqKernel$$ExternalSyntheticLambda30(iArr, strArr, countDownLatch));
            java.lang.reflect.Method methodFindMethod = findMethod(cls("com.tencent.qqnt.kernel.nativeinterface.IKernelGroupService"), "modifyGroupRemark", java.lang.Long.TYPE, java.lang.String.class, cls);
            if (methodFindMethod == null) {
                methodFindMethod = findMethod(objKernelGroupService.getClass(), "modifyGroupRemark", java.lang.Long.TYPE, java.lang.String.class, cls);
            }
            if (methodFindMethod == null) {
                banResult.msg = "modifyGroupRemark(long,String,IOperateCallback) 未找到";
                return banResult;
            }
            java.lang.Object[] objArr = new java.lang.Object[3];
            objArr[0] = java.lang.Long.valueOf(jGroupCodeOf);
            if (str2 != null) {
                str3 = str2;
            }
            objArr[1] = str3;
            objArr[2] = objNewProxyInstance;
            methodFindMethod.invoke(objKernelGroupService, objArr);
            if (!countDownLatch.await(8000L, java.util.concurrent.TimeUnit.MILLISECONDS)) {
                banResult.timeout = true;
                banResult.msg = "等待 modifyGroupRemark 回调超时";
            } else {
                banResult.code = iArr[0];
                banResult.msg = strArr[0];
            }
            return banResult;
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("modifyGroupRemark 反射失败: " + th);
            banResult.msg = java.lang.String.valueOf(th);
            return banResult;
        }
    }

    static /* synthetic */ java.lang.Object lambda$modifyGroupRemark$6(int[] iArr, java.lang.String[] strArr, java.util.concurrent.CountDownLatch countDownLatch, java.lang.Object obj, java.lang.reflect.Method method, java.lang.Object[] objArr) throws java.lang.Throwable {
        java.lang.Object obj2;
        int iLongValue;
        if (!"onResult".equals(method.getName()) || objArr == null || objArr.length <= 0) {
            return null;
        }
        java.lang.Object obj3 = objArr[0];
        if (obj3 != null) {
            if (obj3 instanceof java.lang.Integer) {
                iLongValue = ((java.lang.Integer) obj3).intValue();
            } else {
                iLongValue = obj3 instanceof java.lang.Number ? (int) ((java.lang.Number) obj3).longValue() : -1;
            }
            iArr[0] = iLongValue;
        }
        if (objArr.length > 1 && (obj2 = objArr[1]) != null) {
            strArr[0] = java.lang.String.valueOf(obj2);
        }
        countDownLatch.countDown();
        return null;
    }

    public static java.lang.String getGroupMembers(java.lang.String str) {
        try {
            java.lang.Object objKernelGroupService = kernelGroupService();
            if (objKernelGroupService == null) {
                return "[]";
            }
            long jGroupCodeOf = groupCodeOf(str);
            if (jGroupCodeOf == 0) {
                return "[]";
            }
            java.lang.Class<?> cls = cls("com.tencent.qqnt.kernel.nativeinterface.IGroupMemberListCallback");
            java.lang.Object[] objArr = new java.lang.Object[1];
            java.lang.String str2 = "isRobot";
            java.util.concurrent.CountDownLatch countDownLatch = new java.util.concurrent.CountDownLatch(1);
            java.lang.String str3 = "lastSpeakTime";
            java.lang.String str4 = "joinTime";
            java.lang.Object objNewProxyInstance = java.lang.reflect.Proxy.newProxyInstance(cls.getClassLoader(), new java.lang.Class[]{cls}, new com.tencent.qqlitepro.http.QqKernel$$ExternalSyntheticLambda23(objArr, countDownLatch));
            java.lang.String str5 = "shutUpTime";
            java.lang.reflect.Method methodFindMethod = findMethod(cls("com.tencent.qqnt.kernel.nativeinterface.IKernelGroupService"), "getAllMemberList", java.lang.Long.TYPE, java.lang.Boolean.TYPE, cls);
            if (methodFindMethod == null) {
                methodFindMethod = findMethod(objKernelGroupService.getClass(), "getAllMemberList", java.lang.Long.TYPE, java.lang.Boolean.TYPE, cls);
            }
            if (methodFindMethod == null) {
                return "[]";
            }
            methodFindMethod.invoke(objKernelGroupService, java.lang.Long.valueOf(jGroupCodeOf), false, objNewProxyInstance);
            if (!countDownLatch.await(6000L, java.util.concurrent.TimeUnit.MILLISECONDS) || objArr[0] == null) {
                java.lang.Object[] objArr2 = new java.lang.Object[1];
                java.util.concurrent.CountDownLatch countDownLatch2 = new java.util.concurrent.CountDownLatch(1);
                methodFindMethod.invoke(objKernelGroupService, java.lang.Long.valueOf(jGroupCodeOf), true, java.lang.reflect.Proxy.newProxyInstance(cls.getClassLoader(), new java.lang.Class[]{cls}, new com.tencent.qqlitepro.http.QqKernel$$ExternalSyntheticLambda24(objArr2, countDownLatch2)));
                if (!countDownLatch2.await(8000L, java.util.concurrent.TimeUnit.MILLISECONDS)) {
                    return "[]";
                }
                objArr[0] = objArr2[0];
            }
            java.lang.Object objFieldValue = fieldValue(objArr[0], "infos");
            if (!(objFieldValue instanceof java.util.Map)) {
                return "[]";
            }
            com.google.gson.JsonArray jsonArray = new com.google.gson.JsonArray();
            for (java.lang.Object obj : ((java.util.Map) objFieldValue).values()) {
                if (obj != null) {
                    com.google.gson.JsonObject jsonObject = new com.google.gson.JsonObject();
                    java.lang.Object objFieldValue2 = fieldValue(obj, "uid");
                    java.lang.String strValueOf = "";
                    jsonObject.addProperty("uid", objFieldValue2 == null ? "" : java.lang.String.valueOf(objFieldValue2));
                    java.lang.Object objFieldValue3 = fieldValue(obj, "uin");
                    jsonObject.addProperty("uin", java.lang.Long.valueOf(objFieldValue3 instanceof java.lang.Number ? ((java.lang.Number) objFieldValue3).longValue() : 0L));
                    java.lang.Object objFieldValue4 = fieldValue(obj, "nick");
                    jsonObject.addProperty("nick", objFieldValue4 == null ? "" : java.lang.String.valueOf(objFieldValue4));
                    java.lang.Object objFieldValue5 = fieldValue(obj, "cardName");
                    if (objFieldValue5 != null) {
                        strValueOf = java.lang.String.valueOf(objFieldValue5);
                    }
                    jsonObject.addProperty("cardName", strValueOf);
                    jsonObject.addProperty("role", memberRoleName(fieldValue(obj, "role")));
                    java.lang.String str6 = str5;
                    java.lang.Object objFieldValue6 = fieldValue(obj, str6);
                    jsonObject.addProperty(str6, java.lang.Long.valueOf(objFieldValue6 instanceof java.lang.Number ? ((java.lang.Number) objFieldValue6).longValue() : 0L));
                    java.lang.String str7 = str4;
                    java.lang.Object objFieldValue7 = fieldValue(obj, str7);
                    jsonObject.addProperty(str7, java.lang.Long.valueOf(objFieldValue7 instanceof java.lang.Number ? ((java.lang.Number) objFieldValue7).longValue() : 0L));
                    java.lang.String str8 = str3;
                    java.lang.Object objFieldValue8 = fieldValue(obj, str8);
                    jsonObject.addProperty(str8, java.lang.Long.valueOf(objFieldValue8 instanceof java.lang.Number ? ((java.lang.Number) objFieldValue8).longValue() : 0L));
                    java.lang.String str9 = str2;
                    java.lang.Object objFieldValue9 = fieldValue(obj, str9);
                    jsonObject.addProperty(str9, java.lang.Boolean.valueOf((objFieldValue9 instanceof java.lang.Boolean) && ((java.lang.Boolean) objFieldValue9).booleanValue()));
                    jsonArray.add(jsonObject);
                    str5 = str6;
                    str4 = str7;
                    str3 = str8;
                    str2 = str9;
                }
            }
            return jsonArray.toString();
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("获取群成员列表失败: " + th);
            return "[]";
        }
    }

    static /* synthetic */ java.lang.Object lambda$getGroupMembers$7(java.lang.Object[] objArr, java.util.concurrent.CountDownLatch countDownLatch, java.lang.Object obj, java.lang.reflect.Method method, java.lang.Object[] objArr2) throws java.lang.Throwable {
        if (!"onResult".equals(method.getName()) || objArr2 == null || objArr2.length < 3) {
            return null;
        }
        objArr[0] = objArr2[2];
        countDownLatch.countDown();
        return null;
    }

    static /* synthetic */ java.lang.Object lambda$getGroupMembers$8(java.lang.Object[] objArr, java.util.concurrent.CountDownLatch countDownLatch, java.lang.Object obj, java.lang.reflect.Method method, java.lang.Object[] objArr2) throws java.lang.Throwable {
        if (!"onResult".equals(method.getName()) || objArr2 == null || objArr2.length < 3) {
            return null;
        }
        objArr[0] = objArr2[2];
        countDownLatch.countDown();
        return null;
    }

    public static com.tencent.qqlitepro.http.QqKernel.BanResult quitGroup(java.lang.String str) {
        com.tencent.qqlitepro.http.QqKernel.BanResult banResult = new com.tencent.qqlitepro.http.QqKernel.BanResult();
        try {
            java.lang.Object objKernelGroupService = kernelGroupService();
            if (objKernelGroupService == null) {
                banResult.msg = "IKernelGroupService 为 null（内核未就绪）";
                return banResult;
            }
            long jGroupCodeOf = groupCodeOf(str);
            if (jGroupCodeOf == 0) {
                banResult.msg = "无法解析群号 groupCode（groupId 不是数字且无群详情）";
                return banResult;
            }
            banResult.groupCode = java.lang.String.valueOf(jGroupCodeOf);
            java.lang.Class<?> cls = cls("com.tencent.qqnt.kernel.nativeinterface.IOperateCallback");
            int[] iArr = {-1};
            java.lang.String[] strArr = {""};
            java.util.concurrent.CountDownLatch countDownLatch = new java.util.concurrent.CountDownLatch(1);
            java.lang.Object objNewProxyInstance = java.lang.reflect.Proxy.newProxyInstance(cls.getClassLoader(), new java.lang.Class[]{cls}, new com.tencent.qqlitepro.http.QqKernel$$ExternalSyntheticLambda27(iArr, strArr, countDownLatch));
            java.lang.reflect.Method methodFindMethod = findMethod(cls("com.tencent.qqnt.kernel.nativeinterface.IKernelGroupService"), "quitGroup", java.lang.Long.TYPE, cls);
            if (methodFindMethod == null) {
                methodFindMethod = findMethod(objKernelGroupService.getClass(), "quitGroup", java.lang.Long.TYPE, cls);
            }
            if (methodFindMethod == null) {
                banResult.msg = "quitGroup(long,IOperateCallback) 未找到";
                return banResult;
            }
            methodFindMethod.invoke(objKernelGroupService, java.lang.Long.valueOf(jGroupCodeOf), objNewProxyInstance);
            if (!countDownLatch.await(8000L, java.util.concurrent.TimeUnit.MILLISECONDS)) {
                banResult.timeout = true;
                banResult.msg = "等待 quitGroup 回调超时";
            } else {
                banResult.code = iArr[0];
                banResult.msg = strArr[0];
            }
            return banResult;
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("quitGroup 反射失败: " + th);
            banResult.msg = java.lang.String.valueOf(th);
            return banResult;
        }
    }

    static /* synthetic */ java.lang.Object lambda$quitGroup$9(int[] iArr, java.lang.String[] strArr, java.util.concurrent.CountDownLatch countDownLatch, java.lang.Object obj, java.lang.reflect.Method method, java.lang.Object[] objArr) throws java.lang.Throwable {
        java.lang.Object obj2;
        int iLongValue;
        if (!"onResult".equals(method.getName()) || objArr == null || objArr.length <= 0) {
            return null;
        }
        java.lang.Object obj3 = objArr[0];
        if (obj3 != null) {
            if (obj3 instanceof java.lang.Integer) {
                iLongValue = ((java.lang.Integer) obj3).intValue();
            } else {
                iLongValue = obj3 instanceof java.lang.Number ? (int) ((java.lang.Number) obj3).longValue() : -1;
            }
            iArr[0] = iLongValue;
        }
        if (objArr.length > 1 && (obj2 = objArr[1]) != null) {
            strArr[0] = java.lang.String.valueOf(obj2);
        }
        countDownLatch.countDown();
        return null;
    }

    private static long epochSec(java.lang.Object obj) {
        if (obj == null) {
            return 0L;
        }
        if (obj instanceof java.lang.Number) {
            long jLongValue = ((java.lang.Number) obj).longValue();
            return java.lang.Math.abs(jLongValue) > 1000000000000L ? jLongValue / 1000 : jLongValue;
        }
        java.lang.String strTrim = java.lang.String.valueOf(obj).trim();
        if (strTrim.isEmpty()) {
            return 0L;
        }
        if (strTrim.charAt(0) == '+' || strTrim.charAt(0) == '-' || java.lang.Character.isDigit(strTrim.charAt(0))) {
            int i = 0;
            while (true) {
                if (i < strTrim.length()) {
                    char cCharAt = strTrim.charAt(i);
                    if (!java.lang.Character.isDigit(cCharAt) && cCharAt != '+' && cCharAt != '-' && cCharAt != '.') {
                        break;
                    }
                    i++;
                } else {
                    try {
                        long j = (long) java.lang.Double.parseDouble(strTrim);
                        return java.lang.Math.abs(j) > 1000000000000L ? j / 1000 : j;
                    } catch (java.lang.Throwable unused) {
                    }
                }
            }
        }
        java.lang.String[] strArr = {"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM-dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM/dd"};
        java.text.ParsePosition parsePosition = new java.text.ParsePosition(0);
        for (int i2 = 0; i2 < 6; i2++) {
            try {
                java.text.SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat(strArr[i2], java.util.Locale.US);
                simpleDateFormat.setLenient(false);
                simpleDateFormat.setTimeZone(java.util.TimeZone.getTimeZone("Asia/Shanghai"));
                parsePosition.setIndex(0);
                parsePosition.setErrorIndex(-1);
                java.util.Date date = simpleDateFormat.parse(strTrim, parsePosition);
                if (date != null && parsePosition.getIndex() == strTrim.length()) {
                    return date.getTime() / 1000;
                }
            } catch (java.lang.Throwable unused2) {
            }
        }
        try {
            return java.time.OffsetDateTime.parse(strTrim).toEpochSecond();
        } catch (java.lang.Throwable unused3) {
            try {
                return java.time.LocalDateTime.parse(strTrim.replace(' ', 'T')).atZone(java.time.ZoneId.of("Asia/Shanghai")).toEpochSecond();
            } catch (java.lang.Throwable unused4) {
                return 0L;
            }
        }
    }

    private static long feedTimeOr(com.google.gson.JsonObject jsonObject, java.lang.String... strArr) {
        if (jsonObject == null) {
            return 0L;
        }
        for (java.lang.String str : strArr) {
            com.google.gson.JsonElement jsonElement = jsonObject.get(str);
            if (jsonElement != null && !jsonElement.isJsonNull() && jsonElement.isJsonPrimitive()) {
                com.google.gson.JsonPrimitive asJsonPrimitive = jsonElement.getAsJsonPrimitive();
                long jEpochSec = epochSec(asJsonPrimitive.isNumber() ? java.lang.Long.valueOf(asJsonPrimitive.getAsLong()) : asJsonPrimitive.getAsString());
                if (jEpochSec > 0) {
                    return jEpochSec;
                }
            }
        }
        return 0L;
    }

    public static long longOf(java.lang.Object obj) {
        if (obj instanceof java.lang.Number) {
            return ((java.lang.Number) obj).longValue();
        }
        if (obj == null) {
            return 0L;
        }
        try {
            return java.lang.Long.parseLong(java.lang.String.valueOf(obj).trim());
        } catch (java.lang.Throwable unused) {
            return 0L;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int intOf(java.lang.Object obj) {
        if (obj instanceof java.lang.Number) {
            return ((java.lang.Number) obj).intValue();
        }
        if (obj == null) {
            return 0;
        }
        try {
            return java.lang.Integer.parseInt(java.lang.String.valueOf(obj).trim());
        } catch (java.lang.Throwable unused) {
            return 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.lang.String strOf(java.lang.Object obj) {
        return obj == null ? "" : java.lang.String.valueOf(obj);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.lang.String chain(java.lang.Throwable th) {
        if (th == null) {
            return "null";
        }
        java.lang.StringBuilder sb = new java.lang.StringBuilder(java.lang.String.valueOf(th));
        java.lang.Throwable cause = th.getCause();
        for (int i = 0; cause != null && cause != th && i < 6; i++) {
            sb.append(" <- ");
            sb.append(cause);
            cause = cause.getCause();
        }
        return sb.toString();
    }

    private static java.lang.Object kernelGroupApiService() throws java.lang.Throwable {
        java.lang.reflect.Method methodFindMethod = findMethod(cls("com.tencent.qqnt.msg.KernelServiceUtil"), "b", new java.lang.Class[0]);
        if (methodFindMethod == null) {
            throw new java.lang.NoSuchMethodException("KernelServiceUtil.b() not found");
        }
        methodFindMethod.setAccessible(true);
        try {
            java.lang.Object objInvoke = methodFindMethod.invoke(null, new java.lang.Object[0]);
            java.lang.StringBuilder sb = new java.lang.StringBuilder("群公告: KernelServiceUtil.b()=");
            sb.append(objInvoke == null ? "null" : objInvoke.getClass().getName());
            com.tencent.qqlitepro.util.Log.i(sb.toString());
            return objInvoke;
        } catch (java.lang.reflect.InvocationTargetException e) {
            throw new java.lang.RuntimeException("KernelServiceUtil.b() 抛异常: " + chain(e.getCause()), e);
        }
    }

    private static java.lang.Object nativeGroupService(java.lang.Object obj) {
        if (obj == null) {
            return null;
        }
        for (java.lang.reflect.Method method : obj.getClass().getMethods()) {
            if ("getService".equals(method.getName()) && com.tencent.qqlitepro.util.Log$$ExternalSyntheticBackport0.m(method) == 0) {
                try {
                    method.setAccessible(true);
                    java.lang.Object objInvoke = method.invoke(obj, new java.lang.Object[0]);
                    java.lang.StringBuilder sb = new java.lang.StringBuilder();
                    sb.append("群公告: 原生群服务=");
                    sb.append(objInvoke == null ? "null" : objInvoke.getClass().getName());
                    com.tencent.qqlitepro.util.Log.i(sb.toString());
                    return objInvoke;
                } catch (java.lang.Throwable th) {
                    com.tencent.qqlitepro.util.Log.w("群公告: 获取原生群服务失败 " + th);
                    return null;
                }
            }
        }
        return null;
    }

    private static java.lang.String feedsArrToJson(java.lang.Object obj) {
        com.google.gson.JsonArray jsonArray = new com.google.gson.JsonArray();
        java.lang.Object objFieldValue = fieldValue(obj, "feeds");
        if (objFieldValue instanceof java.util.Collection) {
            java.util.Iterator it = ((java.util.Collection) objFieldValue).iterator();
            while (it.hasNext()) {
                jsonArray.add(feedToJson(it.next()));
            }
        }
        return jsonArray.toString();
    }

    /* JADX WARN: Code duplicated, block: B:40:0x0186 A[Catch: all -> 0x02b5, TryCatch #3 {all -> 0x02b5, blocks: (B:11:0x004c, B:13:0x008c, B:15:0x00b8, B:16:0x00dd, B:19:0x00f0, B:23:0x011d, B:25:0x012a, B:27:0x0132, B:29:0x0137, B:30:0x014e, B:31:0x0155, B:33:0x0159, B:38:0x016f, B:40:0x0186, B:42:0x019b, B:44:0x01a0, B:46:0x01a7, B:54:0x01cf, B:56:0x01db, B:58:0x01df, B:60:0x01e3, B:62:0x01ec, B:64:0x01f8, B:65:0x01fe, B:67:0x0204, B:76:0x0238, B:80:0x0255, B:84:0x0264, B:68:0x0210, B:70:0x0214, B:72:0x0220, B:73:0x0226, B:75:0x022c, B:87:0x0281, B:52:0x01b3, B:22:0x0106), top: B:106:0x004c, outer: #0, inners: #1 }] */
    /* JADX WARN: Code duplicated, block: B:42:0x019b A[Catch: all -> 0x02b5, TRY_LEAVE, TryCatch #3 {all -> 0x02b5, blocks: (B:11:0x004c, B:13:0x008c, B:15:0x00b8, B:16:0x00dd, B:19:0x00f0, B:23:0x011d, B:25:0x012a, B:27:0x0132, B:29:0x0137, B:30:0x014e, B:31:0x0155, B:33:0x0159, B:38:0x016f, B:40:0x0186, B:42:0x019b, B:44:0x01a0, B:46:0x01a7, B:54:0x01cf, B:56:0x01db, B:58:0x01df, B:60:0x01e3, B:62:0x01ec, B:64:0x01f8, B:65:0x01fe, B:67:0x0204, B:76:0x0238, B:80:0x0255, B:84:0x0264, B:68:0x0210, B:70:0x0214, B:72:0x0220, B:73:0x0226, B:75:0x022c, B:87:0x0281, B:52:0x01b3, B:22:0x0106), top: B:106:0x004c, outer: #0, inners: #1 }] */
    /* JADX WARN: Code duplicated, block: B:53:0x01cd  */
    /* JADX WARN: Code duplicated, block: B:91:0x02a1  */
    /* JADX WARN: Code duplicated, block: B:93:0x02b3 A[RETURN] */
    /* JADX WARN: Instruction removed from duplicated block: B:91:0x02a1, please report this as an issue */
    public static java.lang.String fetchGroupNotices(java.lang.String str) {
        java.lang.String str2;
        java.lang.String str3;
        java.lang.reflect.Method methodFindMethod;
        java.lang.String strFetchBulletinsViaWeb;
        long jGroupCodeOf = groupCodeOf(str);
        if (jGroupCodeOf == 0) {
            com.tencent.qqlitepro.util.Log.w("群公告: 无法解析群号 groupCode（groupId=" + str + "）");
            return null;
        }
        try {
            if (!ensureGroupBulletinListenerRegistered()) {
                com.tencent.qqlitepro.util.Log.w("群公告: 群公告监听未就绪");
                return null;
            }
            java.lang.Object objKernelGroupApiService = kernelGroupApiService();
            com.tencent.qqlitepro.http.QqKernel.BulletinWaiter bulletinWaiter = new com.tencent.qqlitepro.http.QqKernel.BulletinWaiter();
            sWaiters.put(java.lang.String.valueOf(jGroupCodeOf), bulletinWaiter);
            try {
                java.lang.Class<?> cls = cls("com.tencent.qqnt.kernel.nativeinterface.IOperateCallback");
                java.lang.Object objNewProxyInstance = java.lang.reflect.Proxy.newProxyInstance(cls.getClassLoader(), new java.lang.Class[]{cls}, new com.tencent.qqlitepro.http.QqKernel$$ExternalSyntheticLambda8());
                java.lang.Object objNativeGroupService = nativeGroupService(objKernelGroupApiService);
                java.lang.Class<?> cls2 = cls("com.tencent.qqnt.kernel.nativeinterface.GroupBulletinListReq");
                java.lang.Object objNewInstance = cls2.newInstance();
                de.robv.android.xposed.XposedHelpers.setIntField(objNewInstance, "startIndex", 0);
                de.robv.android.xposed.XposedHelpers.setIntField(objNewInstance, "num", 50);
                de.robv.android.xposed.XposedHelpers.setIntField(objNewInstance, "needInstructionsForJoinGroup", 0);
                de.robv.android.xposed.XposedHelpers.setIntField(objNewInstance, "needPublisherInfo", 0);
                if (objNativeGroupService != null) {
                    str2 = "群公告: 等待公告数据超时 groupCode=";
                    str3 = "群公告: getGroupBulletin 抛异常: ";
                    java.lang.reflect.Method methodFindMethod2 = findMethod(objNativeGroupService.getClass(), "getGroupBulletinList", java.lang.Long.TYPE, java.lang.String.class, java.lang.String.class, cls2, cls);
                    if (methodFindMethod2 != null) {
                        java.util.concurrent.CountDownLatch countDownLatch = new java.util.concurrent.CountDownLatch(1);
                        int[] iArr = {-1};
                        java.lang.Object objNewProxyInstance2 = java.lang.reflect.Proxy.newProxyInstance(cls.getClassLoader(), new java.lang.Class[]{cls}, new com.tencent.qqlitepro.http.QqKernel$$ExternalSyntheticLambda9(iArr, countDownLatch));
                        methodFindMethod2.setAccessible(true);
                        try {
                            java.lang.String strSelfUid = selfUid();
                            java.lang.Object[] objArr = new java.lang.Object[5];
                            objArr[0] = java.lang.Long.valueOf(jGroupCodeOf);
                            if (strSelfUid == null) {
                                strSelfUid = "";
                            }
                            objArr[1] = strSelfUid;
                            objArr[2] = java.lang.String.valueOf(jGroupCodeOf);
                            objArr[3] = objNewInstance;
                            objArr[4] = objNewProxyInstance2;
                            methodFindMethod2.invoke(objNativeGroupService, objArr);
                        } catch (java.lang.reflect.InvocationTargetException e) {
                            com.tencent.qqlitepro.util.Log.w("群公告: getGroupBulletinList 抛异常: " + chain(e.getCause()));
                        }
                        bulletinWaiter.latch.await(2000L, java.util.concurrent.TimeUnit.MILLISECONDS);
                        if (bulletinWaiter.listResult == null) {
                            if (!countDownLatch.await(2000L, java.util.concurrent.TimeUnit.MILLISECONDS) || iArr[0] == 0) {
                                bulletinWaiter.latch.await(2000L, java.util.concurrent.TimeUnit.MILLISECONDS);
                            } else {
                                com.tencent.qqlitepro.util.Log.w("群公告: 全量列表接口失败 code=" + iArr[0] + "，回退单条接口");
                            }
                        }
                        if (bulletinWaiter.listResult != null) {
                            java.lang.String strFeedsArrToJson = feedsArrToJson(bulletinWaiter.listResult);
                            sWaiters.remove(java.lang.String.valueOf(jGroupCodeOf));
                            return strFeedsArrToJson;
                        }
                    }
                    methodFindMethod = findMethod(cls("com.tencent.qqnt.kernel.api.IGroupService"), "getGroupBulletin", java.lang.Long.TYPE, cls);
                    if (methodFindMethod == null) {
                        methodFindMethod = findMethod(objKernelGroupApiService.getClass(), "getGroupBulletin", java.lang.Long.TYPE, cls);
                    }
                    if (methodFindMethod != null) {
                        methodFindMethod.setAccessible(true);
                        try {
                            java.lang.Object[] objArr2 = new java.lang.Object[2];
                            try {
                                objArr2[0] = java.lang.Long.valueOf(jGroupCodeOf);
                                objArr2[1] = objNewProxyInstance;
                                methodFindMethod.invoke(objKernelGroupApiService, objArr2);
                            } catch (java.lang.Throwable e2) {
                                com.tencent.qqlitepro.util.Log.w(str3 + chain(e2));
                            }
                        } catch (java.lang.Throwable e3) {
                            com.tencent.qqlitepro.util.Log.w(str3 + chain(e3));
                        }
                    }
                    if (bulletinWaiter.latch.await(9000L, java.util.concurrent.TimeUnit.MILLISECONDS) || (bulletinWaiter.listResult == null && bulletinWaiter.change == null)) {
                        com.tencent.qqlitepro.util.Log.w(str2 + jGroupCodeOf);
                        sWaiters.remove(java.lang.String.valueOf(jGroupCodeOf));
                        strFetchBulletinsViaWeb = fetchBulletinsViaWeb(jGroupCodeOf);
                        if (strFetchBulletinsViaWeb != null) {
                            return null;
                        }
                        com.tencent.qqlitepro.util.Log.i("群公告: web list_announce 取回全量公告 groupCode=" + jGroupCodeOf);
                        return strFetchBulletinsViaWeb;
                    }
                    com.google.gson.JsonArray jsonArray = new com.google.gson.JsonArray();
                    if (bulletinWaiter.listResult != null) {
                        java.lang.Object objFieldValue = fieldValue(bulletinWaiter.listResult, "feeds");
                        if (objFieldValue instanceof java.util.Collection) {
                            java.util.Iterator it = ((java.util.Collection) objFieldValue).iterator();
                            while (it.hasNext()) {
                                jsonArray.add(feedToJson(it.next()));
                            }
                        }
                    } else if (bulletinWaiter.change != null) {
                        java.lang.Object objFieldValue2 = fieldValue(bulletinWaiter.change, "feedsRecords");
                        if (objFieldValue2 instanceof java.util.Collection) {
                            java.util.Iterator it2 = ((java.util.Collection) objFieldValue2).iterator();
                            while (it2.hasNext()) {
                                jsonArray.add(recordToJson(it2.next()));
                            }
                        }
                    }
                    java.lang.StringBuilder sb = new java.lang.StringBuilder();
                    sb.append("群公告: 内核路径返回条数=");
                    sb.append(jsonArray.size());
                    sb.append(" (listResult=");
                    sb.append(bulletinWaiter.listResult != null);
                    sb.append(" change=");
                    sb.append(bulletinWaiter.change != null);
                    sb.append(")");
                    com.tencent.qqlitepro.util.Log.i(sb.toString());
                    java.lang.String string = jsonArray.toString();
                    sWaiters.remove(java.lang.String.valueOf(jGroupCodeOf));
                    return string;
                }
                str2 = "群公告: 等待公告数据超时 groupCode=";
                str3 = "群公告: getGroupBulletin 抛异常: ";
                methodFindMethod = findMethod(cls("com.tencent.qqnt.kernel.api.IGroupService"), "getGroupBulletin", java.lang.Long.TYPE, cls);
                if (methodFindMethod == null) {
                    methodFindMethod = findMethod(objKernelGroupApiService.getClass(), "getGroupBulletin", java.lang.Long.TYPE, cls);
                }
                if (methodFindMethod != null) {
                    methodFindMethod.setAccessible(true);
                    java.lang.Object[] objArr3 = new java.lang.Object[2];
                    objArr3[0] = java.lang.Long.valueOf(jGroupCodeOf);
                    objArr3[1] = objNewProxyInstance;
                    methodFindMethod.invoke(objKernelGroupApiService, objArr3);
                }
                if (bulletinWaiter.latch.await(9000L, java.util.concurrent.TimeUnit.MILLISECONDS)) {
                }
                com.tencent.qqlitepro.util.Log.w(str2 + jGroupCodeOf);
                sWaiters.remove(java.lang.String.valueOf(jGroupCodeOf));
                strFetchBulletinsViaWeb = fetchBulletinsViaWeb(jGroupCodeOf);
                if (strFetchBulletinsViaWeb != null) {
                    return null;
                }
                com.tencent.qqlitepro.util.Log.i("群公告: web list_announce 取回全量公告 groupCode=" + jGroupCodeOf);
                return strFetchBulletinsViaWeb;
            } catch (java.lang.Throwable th) {
                sWaiters.remove(java.lang.String.valueOf(jGroupCodeOf));
                throw th;
            }
        } catch (java.lang.Throwable th2) {
            com.tencent.qqlitepro.util.Log.w("拉取群公告失败: " + chain(th2));
            return null;
        }
    }

    static /* synthetic */ java.lang.Object lambda$fetchGroupNotices$10(java.lang.Object obj, java.lang.reflect.Method method, java.lang.Object[] objArr) throws java.lang.Throwable {
        if ("onResult".equals(method.getName()) && objArr != null) {
            java.lang.StringBuilder sb = new java.lang.StringBuilder("群公告: 公告请求 onResult args=");
            sb.append(objArr.length);
            sb.append(" code=");
            sb.append(objArr.length > 0 ? objArr[0] : null);
            sb.append(" msg=");
            sb.append(objArr.length > 1 ? objArr[1] : null);
            com.tencent.qqlitepro.util.Log.i(sb.toString());
        }
        return null;
    }

    static /* synthetic */ java.lang.Object lambda$fetchGroupNotices$11(int[] iArr, java.util.concurrent.CountDownLatch countDownLatch, java.lang.Object obj, java.lang.reflect.Method method, java.lang.Object[] objArr) throws java.lang.Throwable {
        if ("onResult".equals(method.getName()) && objArr != null && objArr.length > 0) {
            java.lang.Object obj2 = objArr[0];
            iArr[0] = obj2 instanceof java.lang.Number ? ((java.lang.Number) obj2).intValue() : -1;
            com.tencent.qqlitepro.util.Log.i("群公告: getGroupBulletinList onResult code=" + iArr[0]);
        }
        countDownLatch.countDown();
        return null;
    }

    private static com.google.gson.JsonObject feedToJson(java.lang.Object obj) {
        com.google.gson.JsonObject jsonObject = new com.google.gson.JsonObject();
        if (obj == null) {
            return jsonObject;
        }
        java.lang.Object objFieldValue = fieldValue(obj, "msg");
        jsonObject.addProperty("feedId", strOf(fieldValue(obj, "feedId")));
        jsonObject.addProperty("type", java.lang.Integer.valueOf(intOf(fieldValue(obj, "type"))));
        jsonObject.addProperty("publishTime", java.lang.Long.valueOf(longOf(fieldValue(obj, "publishTime"))));
        jsonObject.addProperty("pinned", java.lang.Integer.valueOf(intOf(fieldValue(obj, "pinned"))));
        jsonObject.addProperty("uin", java.lang.Long.valueOf(longOf(fieldValue(obj, "uin"))));
        jsonObject.addProperty("readNum", java.lang.Integer.valueOf(intOf(fieldValue(obj, "readNum"))));
        jsonObject.addProperty("title", strOf(fieldValue(objFieldValue, "title")));
        jsonObject.addProperty("text", strOf(fieldValue(objFieldValue, "text")));
        jsonObject.addProperty("textFace", strOf(fieldValue(objFieldValue, "textFace")));
        com.google.gson.JsonArray jsonArray = new com.google.gson.JsonArray();
        java.lang.Object objFieldValue2 = fieldValue(objFieldValue, "pics");
        if (objFieldValue2 instanceof java.util.Collection) {
            for (java.lang.Object obj2 : (java.util.Collection) objFieldValue2) {
                java.lang.String strStrOf = strOf(fieldValue(obj2, "url"));
                if (strStrOf.isEmpty()) {
                    strStrOf = strOf(fieldValue(obj2, "picUrl"));
                }
                if (strStrOf.isEmpty()) {
                    java.lang.String strStrOf2 = strOf(fieldValue(obj2, "id"));
                    if (!strStrOf2.isEmpty()) {
                        jsonArray.add(strStrOf2);
                    }
                } else {
                    jsonArray.add(strStrOf);
                }
            }
        }
        jsonObject.add("pics", jsonArray);
        return jsonObject;
    }

    private static com.google.gson.JsonObject recordToJson(java.lang.Object obj) {
        com.google.gson.JsonObject jsonObject = new com.google.gson.JsonObject();
        if (obj == null) {
            return jsonObject;
        }
        java.lang.Object objFieldValue = fieldValue(obj, "feedsMsg");
        jsonObject.addProperty("feedsId", strOf(fieldValue(obj, "feedsId")));
        jsonObject.addProperty("fromUid", strOf(fieldValue(obj, "fromUid")));
        jsonObject.addProperty("createTime", java.lang.Integer.valueOf(intOf(fieldValue(obj, "createTime"))));
        jsonObject.addProperty("modifyTime", java.lang.Integer.valueOf(intOf(fieldValue(obj, "modifyTime"))));
        jsonObject.addProperty("setTop", java.lang.Integer.valueOf(intOf(fieldValue(obj, "setTop"))));
        jsonObject.addProperty("groupCode", java.lang.Long.valueOf(longOf(fieldValue(obj, "groupCode"))));
        jsonObject.addProperty("feedsType", java.lang.Integer.valueOf(intOf(fieldValue(objFieldValue, "feedsType"))));
        com.google.gson.JsonArray jsonArray = new com.google.gson.JsonArray();
        java.lang.Object objFieldValue2 = fieldValue(objFieldValue, "feedsContents");
        if (objFieldValue2 instanceof java.util.Collection) {
            for (java.lang.Object obj2 : (java.util.Collection) objFieldValue2) {
                com.google.gson.JsonObject jsonObject2 = new com.google.gson.JsonObject();
                jsonObject2.addProperty("contentType", java.lang.Integer.valueOf(intOf(fieldValue(obj2, "contentType"))));
                jsonObject2.addProperty("contentValue", strOf(fieldValue(obj2, "contentValue")));
                jsonObject2.addProperty("picUrl", strOf(fieldValue(obj2, "picUrl")));
                jsonObject2.addProperty("fileUrl", strOf(fieldValue(obj2, "fileUrl")));
                jsonObject2.addProperty("picId", strOf(fieldValue(obj2, "picId")));
                jsonArray.add(jsonObject2);
            }
        }
        jsonObject.add("contents", jsonArray);
        return jsonObject;
    }

    private static java.lang.String fetchBulletinsViaWeb(long j) {
        try {
            java.lang.String strSelfUin = selfUin();
            java.lang.String strSkeyTicket = skeyTicket(null);
            if (strSelfUin != null && !strSelfUin.isEmpty() && strSkeyTicket != null && !strSkeyTicket.isEmpty()) {
                int iBkn = bkn(strSkeyTicket);
                java.lang.String strSkeyTicket2 = skeyTicket("qun.qq.com");
                java.lang.String strPt4Token = pt4Token("qun.qq.com");
                java.lang.StringBuilder sb = new java.lang.StringBuilder("群公告: web票据 uin=");
                sb.append(strSelfUin);
                sb.append(" skey=");
                sb.append(strSkeyTicket);
                sb.append(" bkn=");
                sb.append(iBkn);
                sb.append(" pskey=");
                java.lang.String str = "null";
                sb.append(strSkeyTicket2 == null ? "null" : strSkeyTicket2);
                sb.append(" ptToken=");
                if (strPt4Token != null) {
                    str = strPt4Token;
                }
                sb.append(str);
                com.tencent.qqlitepro.util.Log.i(sb.toString());
                java.lang.StringBuilder sb2 = new java.lang.StringBuilder("uin=o");
                sb2.append(strSelfUin);
                sb2.append("; skey=");
                sb2.append(strSkeyTicket);
                if (strSkeyTicket2 != null && !strSkeyTicket2.isEmpty()) {
                    sb2.append("; p_skey=");
                    sb2.append(strSkeyTicket2);
                }
                if (strPt4Token != null && !strPt4Token.isEmpty()) {
                    sb2.append("; pt4_token=");
                    sb2.append(strPt4Token);
                }
                java.lang.String strHttpGet = httpGet("https://web.qun.qq.com/cgi-bin/announce/get_t_list?bkn=" + iBkn + "&qid=" + j + "&ft=23&s=-1&n=100", sb2.toString());
                if (strHttpGet == null) {
                    com.tencent.qqlitepro.util.Log.w("群公告: web get_t_list 无响应");
                    return null;
                }
                com.google.gson.JsonArray webFeeds = parseWebFeeds(strHttpGet);
                if (webFeeds != null && webFeeds.size() != 0) {
                    com.google.gson.JsonArray jsonArray = new com.google.gson.JsonArray();
                    for (com.google.gson.JsonElement jsonElement : webFeeds) {
                        com.google.gson.JsonObject asJsonObject = jsonElement instanceof com.google.gson.JsonObject ? jsonElement.getAsJsonObject() : null;
                        if (asJsonObject != null) {
                            jsonArray.add(webFeedToJson(asJsonObject));
                        }
                    }
                    if (jsonArray.size() == 0) {
                        return null;
                    }
                    return jsonArray.toString();
                }
                com.tencent.qqlitepro.util.Log.w("群公告: web get_t_list 未解析到公告，raw=" + strHttpGet);
                return null;
            }
            java.lang.StringBuilder sb3 = new java.lang.StringBuilder("群公告: web 拉取无登录票据（uin=");
            sb3.append(strSelfUin);
            sb3.append(" skey空=");
            sb3.append(strSkeyTicket == null);
            com.tencent.qqlitepro.util.Log.w(sb3.toString());
            return null;
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("群公告: web 拉取异常 " + chain(th));
            return null;
        }
    }

    private static com.google.gson.JsonArray parseWebFeeds(java.lang.String str) {
        try {
            com.google.gson.JsonElement string = com.google.gson.JsonParser.parseString(str);
            if (!string.isJsonObject()) {
                return null;
            }
            com.google.gson.JsonObject asJsonObject = string.getAsJsonObject();
            com.google.gson.JsonArray jsonArray = getJsonArray(asJsonObject, "feeds");
            if (jsonArray != null) {
                return jsonArray;
            }
            com.google.gson.JsonElement jsonElement = asJsonObject.get("t_list");
            if (jsonElement instanceof com.google.gson.JsonObject) {
                com.google.gson.JsonObject asJsonObject2 = jsonElement.getAsJsonObject();
                com.google.gson.JsonArray jsonArray2 = getJsonArray(asJsonObject2, "feeds");
                jsonArray = jsonArray2 == null ? getJsonArray(asJsonObject2, "sec") : jsonArray2;
            }
            if (jsonArray != null) {
                return jsonArray;
            }
            com.google.gson.JsonElement jsonElement2 = asJsonObject.get("result");
            if ((jsonElement2 instanceof com.google.gson.JsonObject) && (jsonArray = getJsonArray(jsonElement2.getAsJsonObject(), "t_list")) == null) {
                jsonArray = getJsonArray(jsonElement2.getAsJsonObject(), "feeds");
            }
            if (jsonArray != null) {
                return jsonArray;
            }
            com.google.gson.JsonElement jsonElement3 = asJsonObject.get("data");
            return jsonElement3 instanceof com.google.gson.JsonObject ? getJsonArray(jsonElement3.getAsJsonObject(), "feeds") : jsonArray;
        } catch (java.lang.Throwable unused) {
            return null;
        }
    }

    private static com.google.gson.JsonArray getJsonArray(com.google.gson.JsonObject jsonObject, java.lang.String str) {
        com.google.gson.JsonElement jsonElement = jsonObject.get(str);
        if (jsonElement instanceof com.google.gson.JsonArray) {
            return jsonElement.getAsJsonArray();
        }
        return null;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code duplicated, block: B:33:0x009a  */
    private static java.lang.String htmlUnescape(java.lang.String str) {
        int iIndexOf;
        java.lang.String str2;
        if (str == null || str.isEmpty() || str.indexOf(38) < 0) {
            return str;
        }
        java.lang.StringBuilder sb = new java.lang.StringBuilder(str.length());
        int length = str.length();
        int i = 0;
        while (i < length) {
            char cCharAt = str.charAt(i);
            if (cCharAt == '&' && (iIndexOf = str.indexOf(59, i)) > i && iIndexOf - i <= 10) {
                java.lang.String strSubstring = str.substring(i + 1, iIndexOf);
                java.lang.String str3 = null;
                if (strSubstring.length() > 2 && (strSubstring.startsWith("#x") || strSubstring.startsWith("#X"))) {
                    try {
                        str2 = new java.lang.String(java.lang.Character.toChars(java.lang.Integer.parseInt(strSubstring.substring(2), 16)));
                        str3 = str2;
                    } catch (java.lang.Throwable unused) {
                    }
                } else if (strSubstring.length() > 1 && strSubstring.charAt(0) == '#') {
                    str2 = new java.lang.String(java.lang.Character.toChars(java.lang.Integer.parseInt(strSubstring.substring(1))));
                    str3 = str2;
                } else {
                    strSubstring.hashCode();
                    switch (strSubstring) {
                        case "gt":
                            str3 = ">";
                            break;
                        case "lt":
                            str3 = "<";
                            break;
                        case "amp":
                            str3 = "&";
                            break;
                        case "apos":
                            str3 = "'";
                            break;
                        case "nbsp":
                            str3 = " ";
                            break;
                        case "quot":
                            str3 = "\"";
                            break;
                    }
                }
                if (str3 != null) {
                    sb.append(str3);
                    i = iIndexOf + 1;
                }
            }
            sb.append(cCharAt);
            i++;
        }
        return sb.toString();
    }

    private static com.google.gson.JsonObject webFeedToJson(com.google.gson.JsonObject jsonObject) {
        java.lang.String asString;
        com.google.gson.JsonObject asJsonObject = jsonObject;
        com.google.gson.JsonObject jsonObject2 = new com.google.gson.JsonObject();
        try {
            com.google.gson.JsonElement jsonElement = asJsonObject.get("feed");
            if (jsonElement instanceof com.google.gson.JsonObject) {
                asJsonObject = jsonElement.getAsJsonObject();
            } else {
                com.google.gson.JsonElement jsonElement2 = asJsonObject.get("detail");
                if (jsonElement2 instanceof com.google.gson.JsonObject) {
                    asJsonObject = jsonElement2.getAsJsonObject();
                }
            }
            jsonObject2.addProperty("feedId", strOr(asJsonObject, "feedId", "fid"));
            jsonObject2.addProperty("pinned", java.lang.Integer.valueOf(intOr(asJsonObject, "pinType", "pinn", "pinned")));
            jsonObject2.addProperty("publishTime", java.lang.Long.valueOf(feedTimeOr(asJsonObject, "pubt", "publishTime", "publish_time", "time")));
            long jLongOr = longOr(asJsonObject, "u", "uin", "publisherUin", "fromUin");
            if (jLongOr == 0) {
                jLongOr = longOr(firstObj(asJsonObject, "member"), "uin");
            }
            jsonObject2.addProperty("uin", java.lang.Long.valueOf(jLongOr));
            jsonObject2.addProperty("type", java.lang.Integer.valueOf(intOr(asJsonObject, "announceType", "type")));
            jsonObject2.addProperty("readNum", java.lang.Integer.valueOf(intOr(asJsonObject, "readNum", "viewNum")));
            com.google.gson.JsonObject jsonObjectFirstObj = firstObj(asJsonObject, "msg", "feedMsg", "contentText");
            if (jsonObjectFirstObj == null) {
                jsonObjectFirstObj = asJsonObject;
            }
            java.lang.String strStrOr = strOr(jsonObjectFirstObj, "text", "content");
            java.lang.String strStrOr2 = strOr(jsonObjectFirstObj, "title");
            if (strStrOr.isEmpty() && strStrOr2.isEmpty()) {
                strStrOr = strOr(asJsonObject, "detailContent", "htmlText");
            }
            jsonObject2.addProperty("title", htmlUnescape(strStrOr2));
            jsonObject2.addProperty("text", htmlUnescape(strStrOr));
            jsonObject2.addProperty("textFace", strOr(jsonObjectFirstObj, "face", "textFace"));
            com.google.gson.JsonArray jsonArray = new com.google.gson.JsonArray();
            com.google.gson.JsonArray jsonArray2 = getJsonArray(jsonObjectFirstObj, "pics");
            if (jsonArray2 == null) {
                jsonArray2 = getJsonArray(asJsonObject, "pics");
            }
            if (jsonArray2 != null) {
                for (com.google.gson.JsonElement jsonElement3 : jsonArray2) {
                    if (jsonElement3.isJsonObject()) {
                        asString = strOr(jsonElement3.getAsJsonObject(), "url", "picUrl", "originUrl", "id", "picId");
                    } else {
                        asString = jsonElement3.getAsString();
                    }
                    if (!asString.isEmpty()) {
                        jsonArray.add(asString);
                    }
                }
            }
            jsonObject2.add("pics", jsonArray);
        } catch (java.lang.Throwable unused) {
        }
        return jsonObject2;
    }

    private static java.lang.String strOr(com.google.gson.JsonObject jsonObject, java.lang.String... strArr) {
        java.lang.String asString;
        if (jsonObject == null) {
            return "";
        }
        for (java.lang.String str : strArr) {
            com.google.gson.JsonElement jsonElement = jsonObject.get(str);
            if (jsonElement != null && !jsonElement.isJsonNull() && (asString = jsonElement.getAsString()) != null && !asString.isEmpty()) {
                return asString;
            }
        }
        return "";
    }

    private static int intOr(com.google.gson.JsonObject jsonObject, java.lang.String... strArr) {
        if (jsonObject == null) {
            return 0;
        }
        java.lang.String strStrOr = strOr(jsonObject, strArr);
        if (strStrOr.isEmpty()) {
            return 0;
        }
        try {
            return (int) java.lang.Long.parseLong(strStrOr);
        } catch (java.lang.Throwable unused) {
            return 0;
        }
    }

    private static long longOr(com.google.gson.JsonObject jsonObject, java.lang.String... strArr) {
        if (jsonObject == null) {
            return 0L;
        }
        java.lang.String strStrOr = strOr(jsonObject, strArr);
        if (strStrOr.isEmpty()) {
            return 0L;
        }
        try {
            try {
                return (long) java.lang.Double.parseDouble(strStrOr);
            } catch (java.lang.Throwable unused) {
                return 0L;
            }
        } catch (java.lang.Throwable unused2) {
            try {
                java.text.SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                simpleDateFormat.setTimeZone(java.util.TimeZone.getTimeZone("Asia/Shanghai"));
                return simpleDateFormat.parse(strStrOr).getTime() / 1000;
            } catch (java.lang.Throwable th) {
                return 0L;
            }
        }
    }

    private static com.google.gson.JsonObject firstObj(com.google.gson.JsonObject jsonObject, java.lang.String... strArr) {
        if (jsonObject == null) {
            return null;
        }
        for (java.lang.String str : strArr) {
            com.google.gson.JsonElement jsonElement = jsonObject.get(str);
            if (jsonElement instanceof com.google.gson.JsonObject) {
                return jsonElement.getAsJsonObject();
            }
        }
        return null;
    }

    private static java.lang.String skeyTicket(java.lang.String str) {
        try {
            java.lang.Object objManagerOf = managerOf(cls("mqq.app.AppRuntime"), "mqq.manager.TicketManager");
            java.lang.StringBuilder sb = new java.lang.StringBuilder("群公告: skeyTicket domain=");
            sb.append(str == null ? "skey" : str);
            sb.append(" 管理器=");
            sb.append(objManagerOf);
            com.tencent.qqlitepro.util.Log.i(sb.toString());
            if (objManagerOf == null) {
                return "";
            }
            java.lang.String str2 = "null";
            if (str == null) {
                java.lang.String strSelfUin = selfUin();
                java.lang.reflect.Method method = objManagerOf.getClass().getMethod("getSkey", java.lang.String.class);
                method.setAccessible(true);
                java.lang.String str3 = (java.lang.String) method.invoke(objManagerOf, strSelfUin);
                java.lang.StringBuilder sb2 = new java.lang.StringBuilder("群公告: getSkey(");
                sb2.append(strSelfUin);
                sb2.append(") -> ");
                if (str3 != null) {
                    str2 = "len=" + str3.length();
                }
                sb2.append(str2);
                com.tencent.qqlitepro.util.Log.i(sb2.toString());
                return str3 == null ? "" : str3;
            }
            java.lang.reflect.Method method2 = objManagerOf.getClass().getMethod("getPskey", java.lang.String.class, java.lang.String.class);
            method2.setAccessible(true);
            java.lang.String str4 = (java.lang.String) method2.invoke(objManagerOf, str, selfUin());
            if (str4 == null || str4.isEmpty()) {
                str4 = (java.lang.String) method2.invoke(objManagerOf, str, "");
            }
            java.lang.StringBuilder sb3 = new java.lang.StringBuilder("群公告: getPskey(");
            sb3.append(str);
            sb3.append(",uin)->");
            if (str4 != null) {
                str2 = "len=" + str4.length();
            }
            sb3.append(str2);
            com.tencent.qqlitepro.util.Log.i(sb3.toString());
            return str4 == null ? "" : str4;
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("群公告: skeyTicket 异常 " + chain(th));
            return "";
        }
    }

    private static java.lang.String pt4Token(java.lang.String str) {
        try {
            java.lang.Object objManagerOf = managerOf(cls("mqq.app.AppRuntime"), "mqq.manager.TicketManager");
            if (objManagerOf == null) {
                return "";
            }
            java.lang.reflect.Method method = objManagerOf.getClass().getMethod("getPt4Token", java.lang.String.class, java.lang.String.class);
            method.setAccessible(true);
            return (java.lang.String) method.invoke(objManagerOf, str, "");
        } catch (java.lang.Throwable unused) {
            return "";
        }
    }

    private static java.lang.Object managerOf(java.lang.Class<?> cls, java.lang.String str) {
        try {
            java.lang.Object objCurrentAppRuntime = currentAppRuntime();
            if (objCurrentAppRuntime == null) {
                return null;
            }
            java.lang.reflect.Method methodFindMethod = findMethod(cls, "getManager", java.lang.Integer.TYPE);
            if (methodFindMethod == null) {
                methodFindMethod = objCurrentAppRuntime.getClass().getMethod("getManager", java.lang.Integer.TYPE);
            }
            if (methodFindMethod == null) {
                return null;
            }
            methodFindMethod.setAccessible(true);
            return methodFindMethod.invoke(objCurrentAppRuntime, 2);
        } catch (java.lang.Throwable unused) {
            return null;
        }
    }

    private static java.lang.Object currentAppRuntime() {
        try {
            java.lang.Class<?> cls = cls("mqq.app.MobileQQ");
            java.lang.Object objInvoke = cls.getMethod("getMobileQQ", new java.lang.Class[0]).invoke(null, new java.lang.Object[0]);
            if (objInvoke == null) {
                return null;
            }
            return cls.getMethod("waitAppRuntime", new java.lang.Class[0]).invoke(objInvoke, new java.lang.Object[0]);
        } catch (java.lang.Throwable unused) {
            return null;
        }
    }

    private static int bkn(java.lang.String str) {
        int iCharAt = 5381;
        for (int i = 0; i < str.length(); i++) {
            iCharAt += (iCharAt << 5) + str.charAt(i);
        }
        return Integer.MAX_VALUE & iCharAt;
    }

    private static java.lang.String httpPost(java.lang.String str, java.lang.String str2, java.lang.String str3) {
        java.net.HttpURLConnection httpURLConnection;
        try {
            httpURLConnection = (java.net.HttpURLConnection) new java.net.URL(str).openConnection();
            try {
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setConnectTimeout(6000);
                httpURLConnection.setReadTimeout(6000);
                httpURLConnection.setInstanceFollowRedirects(true);
                httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                httpURLConnection.setRequestProperty("Referer", "https://qun.qq.com/member.html");
                httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android) QQ/9.2.5 (''; '')");
                httpURLConnection.setRequestProperty("Accept", "application/json, text/plain, */*");
                if (str2 != null && !str2.isEmpty()) {
                    httpURLConnection.setRequestProperty("Cookie", str2);
                }
                java.io.OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(str3.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();
                int responseCode = httpURLConnection.getResponseCode();
                com.tencent.qqlitepro.util.Log.i("群公告: httpSend 状态码 " + responseCode + " url=" + str);
                java.io.InputStream errorStream = (responseCode < 200 || responseCode >= 300) ? httpURLConnection.getErrorStream() : httpURLConnection.getInputStream();
                if (errorStream == null) {
                    if (httpURLConnection != null) {
                        try {
                            httpURLConnection.disconnect();
                        } catch (java.lang.Throwable unused) {
                        }
                    }
                    return null;
                }
                java.io.BufferedReader bufferedReader = new java.io.BufferedReader(new java.io.InputStreamReader(errorStream, "UTF-8"));
                java.lang.StringBuilder sb = new java.lang.StringBuilder();
                while (true) {
                    java.lang.String line = bufferedReader.readLine();
                    if (line == null) {
                        break;
                    }
                    sb.append(line);
                }
                bufferedReader.close();
                java.lang.String string = sb.toString();
                if (httpURLConnection != null) {
                    try {
                        httpURLConnection.disconnect();
                    } catch (java.lang.Throwable unused2) {
                    }
                }
                return string;
            } catch (java.lang.Throwable th) {
                java.lang.Throwable thRethrow = th;
                try {
                    com.tencent.qqlitepro.util.Log.w("群公告: httpPost 失败 url=" + str + " -> " + thRethrow);
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
            java.lang.Throwable thRethrow2 = th2;
            httpURLConnection = null;
        }
        return null;
    }

    private static java.lang.String httpGet(java.lang.String str, java.lang.String str2) {
        java.net.HttpURLConnection httpURLConnection;
        try {
            httpURLConnection = (java.net.HttpURLConnection) new java.net.URL(str).openConnection();
            try {
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setConnectTimeout(6000);
                httpURLConnection.setReadTimeout(6000);
                httpURLConnection.setInstanceFollowRedirects(true);
                httpURLConnection.setRequestProperty("Referer", "https://qun.qq.com/member.html");
                httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android) QQ/9.2.5 (''; '')");
                httpURLConnection.setRequestProperty("Accept", "application/json, text/plain, */*");
                if (str2 != null && !str2.isEmpty()) {
                    httpURLConnection.setRequestProperty("Cookie", str2);
                }
                int responseCode = httpURLConnection.getResponseCode();
                com.tencent.qqlitepro.util.Log.i("群公告: httpSend 状态码 " + responseCode + " url=" + str);
                java.io.InputStream errorStream = (responseCode < 200 || responseCode >= 300) ? httpURLConnection.getErrorStream() : httpURLConnection.getInputStream();
                if (errorStream == null) {
                    if (httpURLConnection != null) {
                        try {
                            httpURLConnection.disconnect();
                        } catch (java.lang.Throwable unused) {
                        }
                    }
                    return null;
                }
                java.io.BufferedReader bufferedReader = new java.io.BufferedReader(new java.io.InputStreamReader(errorStream, "UTF-8"));
                java.lang.StringBuilder sb = new java.lang.StringBuilder();
                while (true) {
                    java.lang.String line = bufferedReader.readLine();
                    if (line == null) {
                        break;
                    }
                    sb.append(line);
                }
                bufferedReader.close();
                java.lang.String string = sb.toString();
                if (httpURLConnection != null) {
                    try {
                        httpURLConnection.disconnect();
                    } catch (java.lang.Throwable unused2) {
                    }
                }
                return string;
            } catch (java.lang.Throwable thRethrow2) {
                try {
                    com.tencent.qqlitepro.util.Log.w("群公告: httpGet 失败 url=" + str + " -> " + thRethrow2);
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
            java.lang.Throwable thRethrow3 = th2;
            httpURLConnection = null;
        }
        return null;
    }

    public static void deliverAllBulletins(java.lang.Object obj) {
        if (obj == null) {
            return;
        }
        try {
            long jLongOf = longOf(fieldValue(obj, "groupCode"));
            if (jLongOf == 0) {
                return;
            }
            java.lang.Object objNewInstance = cls("com.tencent.qqnt.kernel.nativeinterface.GroupBulletin").newInstance();
            java.util.ArrayList<java.lang.Object> arrayListBuildRecordsFromFeeds = buildRecordsFromFeeds(jLongOf, fieldValue(obj, "feeds"));
            if (!arrayListBuildRecordsFromFeeds.isEmpty()) {
                sBulletinCache.put(java.lang.String.valueOf(jLongOf), new com.tencent.qqlitepro.http.QqKernel.BulletinCacheEntry(arrayListBuildRecordsFromFeeds, java.lang.System.currentTimeMillis()));
            }
            de.robv.android.xposed.XposedHelpers.setObjectField(objNewInstance, "feedsRecords", arrayListBuildRecordsFromFeeds);
            java.lang.Class<?> cls = cls("momoi.mod.qqpro.api.GroupBulletinApi");
            java.lang.Object staticObjectField = de.robv.android.xposed.XposedHelpers.getStaticObjectField(cls, "INSTANCE");
            java.lang.reflect.Method declaredMethod = cls.getDeclaredMethod("flatten", cls("com.tencent.qqnt.kernel.nativeinterface.GroupBulletin"));
            declaredMethod.setAccessible(true);
            java.lang.Object objInvoke = declaredMethod.invoke(staticObjectField, objNewInstance);
            java.lang.reflect.Method declaredMethod2 = cls.getDeclaredMethod("deliver", java.lang.Long.TYPE, java.util.List.class);
            declaredMethod2.setAccessible(true);
            declaredMethod2.invoke(staticObjectField, java.lang.Long.valueOf(jLongOf), objInvoke);
            com.tencent.qqlitepro.util.Log.i("去除数量限制: 已注入全量公告 groupCode=" + jLongOf + " 条数=" + arrayListBuildRecordsFromFeeds.size());
        } catch (java.lang.Throwable thRethrow3) {
            com.tencent.qqlitepro.util.Log.w("去除数量限制注入失败: " + thRethrow3);
        }
    }

    private static java.util.ArrayList<java.lang.Object> buildRecordsFromFeeds(long j, java.lang.Object obj) {
        java.lang.String strStrOf;
        java.util.ArrayList<java.lang.Object> arrayList = new java.util.ArrayList<>();
        if (!(obj instanceof java.util.Collection)) {
            return arrayList;
        }
        try {
            java.lang.Class<?> cls = cls("com.tencent.qqnt.kernel.nativeinterface.BulletinFeedsRecord");
            java.lang.Class<?> cls2 = cls("com.tencent.qqnt.kernel.nativeinterface.BulletinFeedsMsg");
            java.lang.Class<?> cls3 = cls("com.tencent.qqnt.kernel.nativeinterface.BulletinFeedsContent");
            java.util.Iterator it = ((java.util.Collection) obj).iterator();
            while (it.hasNext()) {
                java.lang.Object next = it.next();
                if (next != null) {
                    java.lang.Object objFieldValue = fieldValue(next, "msg");
                    java.lang.String strStrOf2 = strOf(fieldValue(objFieldValue, "text"));
                    if (strStrOf2.isEmpty()) {
                        strStrOf2 = strOf(fieldValue(objFieldValue, "title"));
                    }
                    java.util.ArrayList arrayList2 = new java.util.ArrayList();
                    if (strStrOf2 != null && !strStrOf2.isEmpty()) {
                        java.lang.Object objNewInstance = cls3.newInstance();
                        de.robv.android.xposed.XposedHelpers.setObjectField(objNewInstance, "contentType", 0);
                        de.robv.android.xposed.XposedHelpers.setObjectField(objNewInstance, "contentValue", strStrOf2);
                        arrayList2.add(objNewInstance);
                    }
                    java.lang.Object objFieldValue2 = fieldValue(objFieldValue, "pics");
                    if (objFieldValue2 instanceof java.util.Collection) {
                        for (java.lang.Object obj2 : (java.util.Collection) objFieldValue2) {
                            java.lang.String strStrOf3 = strOf(fieldValue(obj2, "url"));
                            if (strStrOf3.isEmpty()) {
                                strStrOf3 = strOf(fieldValue(obj2, "picUrl"));
                            }
                            if (strStrOf3.isEmpty()) {
                                strStrOf = strOf(fieldValue(obj2, "id"));
                                if (strStrOf.isEmpty()) {
                                    strStrOf = strOf(fieldValue(obj2, "picId"));
                                }
                            } else {
                                strStrOf = "";
                            }
                            java.lang.Object objNewInstance2 = cls3.newInstance();
                            java.util.Iterator it2 = it;
                            de.robv.android.xposed.XposedHelpers.setObjectField(objNewInstance2, "contentType", 1);
                            de.robv.android.xposed.XposedHelpers.setObjectField(objNewInstance2, "contentValue", "");
                            if (!strStrOf3.isEmpty() && strStrOf3.startsWith("http")) {
                                de.robv.android.xposed.XposedHelpers.setObjectField(objNewInstance2, "picUrl", strStrOf3);
                            } else {
                                if (!strStrOf.isEmpty()) {
                                    de.robv.android.xposed.XposedHelpers.setObjectField(objNewInstance2, "picId", strStrOf);
                                }
                                it = it2;
                            }
                            arrayList2.add(objNewInstance2);
                            it = it2;
                        }
                    }
                    java.util.Iterator it3 = it;
                    java.lang.Object objNewInstance3 = cls2.newInstance();
                    de.robv.android.xposed.XposedHelpers.setObjectField(objNewInstance3, "feedsContents", arrayList2);
                    java.lang.Object objNewInstance4 = cls.newInstance();
                    de.robv.android.xposed.XposedHelpers.setObjectField(objNewInstance4, "feedsMsg", objNewInstance3);
                    de.robv.android.xposed.XposedHelpers.setObjectField(objNewInstance4, "feedsId", strOf(fieldValue(next, "feedId")));
                    de.robv.android.xposed.XposedHelpers.setObjectField(objNewInstance4, "fromUid", strOf(fieldValue(next, "uin")));
                    int iEpochSec = (int) epochSec(fieldValue(next, "publishTime"));
                    de.robv.android.xposed.XposedHelpers.setObjectField(objNewInstance4, "createTime", java.lang.Integer.valueOf(iEpochSec));
                    de.robv.android.xposed.XposedHelpers.setObjectField(objNewInstance4, "modifyTime", java.lang.Integer.valueOf(iEpochSec));
                    de.robv.android.xposed.XposedHelpers.setObjectField(objNewInstance4, "groupCode", java.lang.Long.valueOf(j));
                    de.robv.android.xposed.XposedHelpers.setObjectField(objNewInstance4, "setTop", java.lang.Integer.valueOf(intOf(fieldValue(next, "pinned"))));
                    arrayList.add(objNewInstance4);
                    it = it3;
                }
            }
        } catch (java.lang.Throwable thRethrow3) {
            com.tencent.qqlitepro.util.Log.w("去除数量限制: 内核 feeds 转换失败 " + chain(thRethrow3));
        }
        return arrayList;
    }

    private static java.util.ArrayList<java.lang.Object> fullRecordsViaKernel(long j, long j2) {
        java.lang.String strValueOf = java.lang.String.valueOf(j);
        try {
            ensureRichMediaListener();
            java.util.concurrent.ConcurrentHashMap<java.lang.String, com.tencent.qqlitepro.http.QqKernel.BulletinWaiter> concurrentHashMap = sKernelListWaiters;
            com.tencent.qqlitepro.http.QqKernel.BulletinWaiter bulletinWaiter = concurrentHashMap.get(strValueOf);
            if (bulletinWaiter == null) {
                bulletinWaiter = new com.tencent.qqlitepro.http.QqKernel.BulletinWaiter();
                com.tencent.qqlitepro.http.QqKernel.BulletinWaiter bulletinWaiterPutIfAbsent = concurrentHashMap.putIfAbsent(strValueOf, bulletinWaiter);
                if (bulletinWaiterPutIfAbsent != null) {
                    bulletinWaiter = bulletinWaiterPutIfAbsent;
                } else {
                    java.lang.Object objNativeGroupService = nativeGroupService(kernelGroupApiService());
                    java.lang.Class<?> cls = cls("com.tencent.qqnt.kernel.nativeinterface.IOperateCallback");
                    java.lang.Class<?> cls2 = cls("com.tencent.qqnt.kernel.nativeinterface.GroupBulletinListReq");
                    java.lang.Object objNewInstance = cls2.newInstance();
                    de.robv.android.xposed.XposedHelpers.setIntField(objNewInstance, "startIndex", 0);
                    de.robv.android.xposed.XposedHelpers.setIntField(objNewInstance, "num", 50);
                    de.robv.android.xposed.XposedHelpers.setIntField(objNewInstance, "needInstructionsForJoinGroup", 0);
                    de.robv.android.xposed.XposedHelpers.setIntField(objNewInstance, "needPublisherInfo", 0);
                    java.lang.reflect.Method methodFindMethod = findMethod(objNativeGroupService.getClass(), "getGroupBulletinList", java.lang.Long.TYPE, java.lang.String.class, java.lang.String.class, cls2, cls);
                    if (methodFindMethod == null) {
                        concurrentHashMap.remove(strValueOf);
                        com.tencent.qqlitepro.util.Log.w("去除数量限制: 内核 getGroupBulletinList 方法未找到");
                        return null;
                    }
                    java.lang.Object objNewProxyInstance = java.lang.reflect.Proxy.newProxyInstance(cls.getClassLoader(), new java.lang.Class[]{cls}, new com.tencent.qqlitepro.http.QqKernel$$ExternalSyntheticLambda7(bulletinWaiter));
                    methodFindMethod.setAccessible(true);
                    java.lang.String strSelfUid = selfUid();
                    java.lang.Object[] objArr = new java.lang.Object[5];
                    objArr[0] = java.lang.Long.valueOf(j);
                    if (strSelfUid == null) {
                        strSelfUid = "";
                    }
                    objArr[1] = strSelfUid;
                    objArr[2] = java.lang.String.valueOf(j);
                    objArr[3] = objNewInstance;
                    objArr[4] = objNewProxyInstance;
                    methodFindMethod.invoke(objNativeGroupService, objArr);
                }
            }
            bulletinWaiter.latch.await(j2, java.util.concurrent.TimeUnit.MILLISECONDS);
            concurrentHashMap.remove(strValueOf);
            java.lang.Object obj = bulletinWaiter.listResult;
            if (obj == null) {
                return null;
            }
            java.util.ArrayList<java.lang.Object> arrayListBuildRecordsFromFeeds = buildRecordsFromFeeds(j, fieldValue(obj, "feeds"));
            if (arrayListBuildRecordsFromFeeds.isEmpty()) {
                com.tencent.qqlitepro.util.Log.w("去除数量限制: 内核 getGroupBulletinList 返回空列表 gc=" + j);
                return null;
            }
            sBulletinCache.put(strValueOf, new com.tencent.qqlitepro.http.QqKernel.BulletinCacheEntry(arrayListBuildRecordsFromFeeds, java.lang.System.currentTimeMillis()));
            com.tencent.qqlitepro.util.Log.i("去除数量限制: 内核 getGroupBulletinList 取回全量公告 gc=" + j + " 条数=" + arrayListBuildRecordsFromFeeds.size());
            return arrayListBuildRecordsFromFeeds;
        } catch (java.lang.Throwable thRethrow3) {
            sKernelListWaiters.remove(strValueOf);
            com.tencent.qqlitepro.util.Log.w("去除数量限制: 内核全量接口失败 gc=" + j + " " + chain(thRethrow3));
            return null;
        }
    }

    static /* synthetic */ java.lang.Object lambda$fullRecordsViaKernel$12(com.tencent.qqlitepro.http.QqKernel.BulletinWaiter bulletinWaiter, java.lang.Object obj, java.lang.reflect.Method method, java.lang.Object[] objArr) throws java.lang.Throwable {
        int iIntValue;
        if (!"onResult".equals(method.getName()) || objArr == null || objArr.length <= 0) {
            return null;
        }
        java.lang.Object obj2 = objArr[0];
        if (!(obj2 instanceof java.lang.Number) || (iIntValue = ((java.lang.Number) obj2).intValue()) == 0) {
            return null;
        }
        com.tencent.qqlitepro.util.Log.i("群公告: getGroupBulletinList onResult code=" + iIntValue);
        bulletinWaiter.latch.countDown();
        return null;
    }

    private static java.util.ArrayList<java.lang.Object> fullRecordsAny(long j) {
        java.util.ArrayList<java.lang.Object> arrayListCachedFullRecords = cachedFullRecords(j);
        if (arrayListCachedFullRecords != null) {
            return arrayListCachedFullRecords;
        }
        java.util.ArrayList<java.lang.Object> arrayListFullRecordsViaKernel = fullRecordsViaKernel(j, 3000L);
        return arrayListFullRecordsViaKernel != null ? arrayListFullRecordsViaKernel : fullRecordsViaWeb(j);
    }

    private static java.util.ArrayList<java.lang.Object> fullRecordsViaWeb(long j) {
        java.lang.String str;
        java.util.Iterator<com.google.gson.JsonElement> it;
        java.lang.String asString;
        java.lang.String str2 = "title";
        java.lang.String str3 = "pics";
        try {
            java.lang.String strValueOf = java.lang.String.valueOf(j);
            com.tencent.qqlitepro.http.QqKernel.BulletinCacheEntry bulletinCacheEntry = sBulletinCache.get(strValueOf);
            if (bulletinCacheEntry != null && java.lang.System.currentTimeMillis() - bulletinCacheEntry.atMs < BULLETIN_CACHE_TTL_MS) {
                return bulletinCacheEntry.records;
            }
            java.lang.String strFetchBulletinsViaWeb = fetchBulletinsViaWeb(j);
            if (strFetchBulletinsViaWeb == null) {
                if (bulletinCacheEntry != null) {
                    return bulletinCacheEntry.records;
                }
                return null;
            }
            com.google.gson.JsonArray asJsonArray = com.google.gson.JsonParser.parseString(strFetchBulletinsViaWeb).getAsJsonArray();
            java.lang.Class<?> cls = cls("com.tencent.qqnt.kernel.nativeinterface.BulletinFeedsRecord");
            java.lang.Class<?> cls2 = cls("com.tencent.qqnt.kernel.nativeinterface.BulletinFeedsMsg");
            java.lang.Class<?> cls3 = cls("com.tencent.qqnt.kernel.nativeinterface.BulletinFeedsContent");
            java.util.ArrayList<java.lang.Object> arrayList = new java.util.ArrayList<>();
            java.util.Iterator<com.google.gson.JsonElement> it2 = asJsonArray.iterator();
            while (it2.hasNext()) {
                com.google.gson.JsonElement next = it2.next();
                if (next instanceof com.google.gson.JsonObject) {
                    com.google.gson.JsonObject asJsonObject = next.getAsJsonObject();
                    java.lang.String strStrOr = strOr(asJsonObject, "text", str2);
                    java.lang.String strStrOr2 = strOr(asJsonObject, str2);
                    if (strStrOr.isEmpty()) {
                        strStrOr = strStrOr2;
                    }
                    java.lang.String asString2 = "";
                    if (strStrOr == null || strStrOr.isEmpty()) {
                        strStrOr = "";
                    }
                    java.util.ArrayList arrayList2 = new java.util.ArrayList();
                    if (!strStrOr.isEmpty()) {
                        java.lang.Object objNewInstance = cls3.newInstance();
                        de.robv.android.xposed.XposedHelpers.setObjectField(objNewInstance, "contentType", 0);
                        de.robv.android.xposed.XposedHelpers.setObjectField(objNewInstance, "contentValue", strStrOr);
                        arrayList2.add(objNewInstance);
                    }
                    java.lang.String strStrOr3 = strOr(asJsonObject, "feedId", "fid");
                    com.google.gson.JsonArray asJsonArray2 = (asJsonObject.has(str3) && asJsonObject.get(str3).isJsonArray()) ? asJsonObject.getAsJsonArray(str3) : null;
                    if (asJsonArray2 != null) {
                        java.util.Iterator<com.google.gson.JsonElement> it3 = asJsonArray2.iterator();
                        while (it3.hasNext()) {
                            com.google.gson.JsonElement next2 = it3.next();
                            java.lang.String str4 = str3;
                            if (next2.isJsonObject()) {
                                it = it3;
                                str = strValueOf;
                                asString = strOr(next2.getAsJsonObject(), "url", "picUrl", "originUrl", "id", "picId");
                            } else {
                                str = strValueOf;
                                it = it3;
                                asString = next2.isJsonPrimitive() ? next2.getAsString() : "";
                            }
                            if (asString != null && !asString.isEmpty()) {
                                java.lang.Object objNewInstance2 = cls3.newInstance();
                                java.lang.Class<?> cls4 = cls3;
                                de.robv.android.xposed.XposedHelpers.setObjectField(objNewInstance2, "contentType", 1);
                                de.robv.android.xposed.XposedHelpers.setObjectField(objNewInstance2, "contentValue", "");
                                if (asString.startsWith("http")) {
                                    de.robv.android.xposed.XposedHelpers.setObjectField(objNewInstance2, "picUrl", asString);
                                } else {
                                    de.robv.android.xposed.XposedHelpers.setObjectField(objNewInstance2, "picId", asString);
                                }
                                arrayList2.add(objNewInstance2);
                                cls3 = cls4;
                            }
                            it3 = it;
                            str3 = str4;
                            strValueOf = str;
                        }
                    }
                    java.lang.String str5 = str3;
                    java.lang.String str6 = strValueOf;
                    java.lang.Class<?> cls5 = cls3;
                    java.lang.Object objNewInstance3 = cls2.newInstance();
                    de.robv.android.xposed.XposedHelpers.setObjectField(objNewInstance3, "feedsContents", arrayList2);
                    java.lang.Object objNewInstance4 = cls.newInstance();
                    de.robv.android.xposed.XposedHelpers.setObjectField(objNewInstance4, "feedsMsg", objNewInstance3);
                    de.robv.android.xposed.XposedHelpers.setObjectField(objNewInstance4, "feedsId", strStrOr3);
                    if (asJsonObject.get("uin") != null) {
                        asString2 = asJsonObject.get("uin").getAsString();
                    }
                    de.robv.android.xposed.XposedHelpers.setObjectField(objNewInstance4, "fromUid", strOf(asString2));
                    int iFeedTimeOr = (int) feedTimeOr(asJsonObject, "publishTime", "publish_time", "time");
                    de.robv.android.xposed.XposedHelpers.setObjectField(objNewInstance4, "createTime", java.lang.Integer.valueOf(iFeedTimeOr));
                    de.robv.android.xposed.XposedHelpers.setObjectField(objNewInstance4, "modifyTime", java.lang.Integer.valueOf(iFeedTimeOr));
                    de.robv.android.xposed.XposedHelpers.setObjectField(objNewInstance4, "groupCode", java.lang.Long.valueOf(j));
                    de.robv.android.xposed.XposedHelpers.setObjectField(objNewInstance4, "setTop", java.lang.Integer.valueOf(intOr(asJsonObject, "pinned", "pinType", "pinn")));
                    arrayList.add(objNewInstance4);
                    str2 = str2;
                    it2 = it2;
                    cls3 = cls5;
                    str3 = str5;
                    strValueOf = str6;
                }
            }
            java.lang.String str7 = strValueOf;
            if (arrayList.isEmpty()) {
                if (bulletinCacheEntry != null) {
                    return bulletinCacheEntry.records;
                }
                return null;
            }
            sBulletinCache.put(str7, new com.tencent.qqlitepro.http.QqKernel.BulletinCacheEntry(arrayList, java.lang.System.currentTimeMillis()));
            com.tencent.qqlitepro.util.Log.i("去除数量限制: web 取回群全量公告 gc=" + j + " 条数=" + arrayList.size());
            return arrayList;
        } catch (java.lang.Throwable thRethrow3) {
            com.tencent.qqlitepro.util.Log.w("去除数量限制: 构建全量记录失败 " + chain(thRethrow3));
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.util.ArrayList<java.lang.Object> cachedFullRecords(long j) {
        com.tencent.qqlitepro.http.QqKernel.BulletinCacheEntry bulletinCacheEntry = sBulletinCache.get(java.lang.String.valueOf(j));
        if (bulletinCacheEntry == null || java.lang.System.currentTimeMillis() - bulletinCacheEntry.atMs >= BULLETIN_CACHE_TTL_MS) {
            return null;
        }
        return bulletinCacheEntry.records;
    }

    public static void forceRefreshBulletins(long j) {
        if (j == 0) {
            return;
        }
        sBulletinCache.remove(java.lang.String.valueOf(j));
        scheduleAsyncBulletinDeliver(j);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void scheduleAsyncBulletinDeliver(long j) {
        if (sBulletinFetching.add(java.lang.Long.valueOf(j))) {
            java.util.concurrent.CountDownLatch countDownLatch = new java.util.concurrent.CountDownLatch(1);
            sBulletinDone.put(java.lang.Long.valueOf(j), countDownLatch);
            new java.lang.Thread(new com.tencent.qqlitepro.http.QqKernel$$ExternalSyntheticLambda26(j, countDownLatch), "bulletin-web-loader").start();
        }
    }

    static /* synthetic */ void lambda$scheduleAsyncBulletinDeliver$14(long j, java.util.concurrent.CountDownLatch countDownLatch) {
        try {
            java.util.ArrayList<java.lang.Object> arrayListFullRecordsAny = fullRecordsAny(j);
            if (arrayListFullRecordsAny == null) {
                try {
                    java.lang.Thread.sleep(400L);
                } catch (java.lang.InterruptedException unused) {
                    java.lang.Thread.currentThread().interrupt();
                }
                arrayListFullRecordsAny = fullRecordsAny(j);
            }
            new android.os.Handler(android.os.Looper.getMainLooper()).post(new com.tencent.qqlitepro.http.QqKernel$$ExternalSyntheticLambda13(j, arrayListFullRecordsAny));
        } catch (java.lang.Throwable thRethrow3) {
            try {
                com.tencent.qqlitepro.util.Log.w("去除数量限制: 异步拉取异常 " + chain(thRethrow3));
            } finally {
                sBulletinFetching.remove(java.lang.Long.valueOf(j));
                countDownLatch.countDown();
            }
        }
    }

    static /* synthetic */ void lambda$scheduleAsyncBulletinDeliver$13(long j, java.util.ArrayList arrayList) {
        try {
            deliverRecordsToUI(j, arrayList);
        } catch (java.lang.Throwable thRethrow3) {
            com.tencent.qqlitepro.util.Log.w("去除数量限制: 异步 deliver 异常 " + chain(thRethrow3));
        }
    }

    public static java.util.ArrayList<java.lang.Object> awaitFullBulletins(long j, long j2) {
        java.util.ArrayList<java.lang.Object> arrayListCachedFullRecords = cachedFullRecords(j);
        if (arrayListCachedFullRecords != null) {
            return arrayListCachedFullRecords;
        }
        java.util.concurrent.ConcurrentHashMap<java.lang.Long, java.util.concurrent.CountDownLatch> concurrentHashMap = sBulletinDone;
        java.util.concurrent.CountDownLatch countDownLatch = concurrentHashMap.get(java.lang.Long.valueOf(j));
        if (countDownLatch == null || countDownLatch.getCount() == 0) {
            scheduleAsyncBulletinDeliver(j);
            countDownLatch = concurrentHashMap.get(java.lang.Long.valueOf(j));
        }
        if (countDownLatch != null) {
            try {
                countDownLatch.await(j2, java.util.concurrent.TimeUnit.MILLISECONDS);
            } catch (java.lang.InterruptedException unused) {
                java.lang.Thread.currentThread().interrupt();
            }
        }
        return cachedFullRecords(j);
    }

    private static void deliverRecordsToUI(long j, java.util.ArrayList<java.lang.Object> arrayList) {
        if (arrayList != null) {
            try {
                if (arrayList.isEmpty()) {
                    return;
                }
                android.content.Context contextAppContext = com.tencent.qqlitepro.cache.MsgCache.appContext();
                if (contextAppContext == null || com.tencent.qqlitepro.util.Config.bulletinAllEnabled(contextAppContext)) {
                    java.lang.Class<?> cls = cls("com.tencent.qqnt.kernel.nativeinterface.GroupBulletin");
                    java.lang.Class<?> cls2 = cls("momoi.mod.qqpro.api.GroupBulletinApi");
                    java.lang.Object objNewInstance = cls.newInstance();
                    de.robv.android.xposed.XposedHelpers.setObjectField(objNewInstance, "feedsRecords", arrayList);
                    java.lang.Object staticObjectField = de.robv.android.xposed.XposedHelpers.getStaticObjectField(cls2, "INSTANCE");
                    java.lang.reflect.Method declaredMethod = cls2.getDeclaredMethod("flatten", cls);
                    declaredMethod.setAccessible(true);
                    java.lang.Object objInvoke = declaredMethod.invoke(staticObjectField, objNewInstance);
                    java.lang.reflect.Method declaredMethod2 = cls2.getDeclaredMethod("deliver", java.lang.Long.TYPE, java.util.List.class);
                    declaredMethod2.setAccessible(true);
                    try {
                        java.lang.Object staticObjectField2 = de.robv.android.xposed.XposedHelpers.getStaticObjectField(cls2, "pending");
                        java.lang.Object obj = staticObjectField2 instanceof java.util.Map ? ((java.util.Map) staticObjectField2).get(java.lang.Long.valueOf(j)) : null;
                        if ((obj instanceof java.util.List) && !((java.util.List) obj).isEmpty()) {
                            declaredMethod2.invoke(staticObjectField, java.lang.Long.valueOf(j), objInvoke);
                            com.tencent.qqlitepro.util.Log.i("去除数量限制: 异步注入全量公告 gc=" + j + " 条数=" + arrayList.size() + " (deliver)");
                            return;
                        }
                    } catch (java.lang.Throwable thRethrow3) {
                        com.tencent.qqlitepro.util.Log.w("去除数量限制: pending 读取失败 " + chain(thRethrow3));
                    }
                    java.util.List<java.lang.Object> list = sFetchCallbacks.get(java.lang.Long.valueOf(j));
                    if (list != null && !list.isEmpty()) {
                        java.util.Iterator<java.lang.Object> it = list.iterator();
                        while (it.hasNext()) {
                            try {
                                de.robv.android.xposed.XposedHelpers.callMethod(it.next(), "invoke", new java.lang.Object[]{objInvoke});
                            } catch (java.lang.Throwable th2) {
                                com.tencent.qqlitepro.util.Log.w("去除数量限制: 补投回调异常 " + chain(th2));
                            }
                        }
                        com.tencent.qqlitepro.util.Log.i("去除数量限制: 异步补投全量公告 gc=" + j + " 条数=" + arrayList.size() + " 回调数=" + list.size());
                        return;
                    }
                    declaredMethod2.invoke(staticObjectField, java.lang.Long.valueOf(j), objInvoke);
                    com.tencent.qqlitepro.util.Log.i("去除数量限制: 异步注入全量公告 gc=" + j + " 条数=" + arrayList.size());
                }
            } catch (java.lang.Throwable th3) {
                com.tencent.qqlitepro.util.Log.w("去除数量限制: 异步 deliver 反射失败 " + chain(th3));
            }
        }
    }

    private static final class BulletinCacheEntry {
        final long atMs;
        final java.util.ArrayList<java.lang.Object> records;

        BulletinCacheEntry(java.util.ArrayList<java.lang.Object> arrayList, long j) {
            this.records = arrayList;
            this.atMs = j;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static final class BulletinWaiter {
        volatile java.lang.Object change;
        final java.util.concurrent.CountDownLatch latch;
        volatile java.lang.Object listResult;
        volatile int richErr;
        volatile java.lang.String richUrl;



        private BulletinWaiter() {
            this.latch = new java.util.concurrent.CountDownLatch(1);
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.http.QqKernel$1, reason: invalid class name */
    static class AnonymousClass1 extends android.util.LruCache<java.lang.String, android.graphics.Bitmap> {
        AnonymousClass1(int i) {
            super(i);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.util.LruCache
        public int sizeOf(java.lang.String str, android.graphics.Bitmap bitmap) {
            if (bitmap == null) {
                return 0;
            }
            return bitmap.getByteCount();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static android.graphics.Bitmap bulletinBitmapCached(java.lang.String str) {
        if (str == null || !str.startsWith("http")) {
            return null;
        }
        android.util.LruCache<java.lang.String, android.graphics.Bitmap> lruCache = sBulletinBitmapCache;
        android.graphics.Bitmap bitmap = lruCache.get(str);
        if (bitmap != null) {
            return bitmap;
        }
        android.graphics.Bitmap bitmapDownloadBulletinBitmap = downloadBulletinBitmap(str);
        if (bitmapDownloadBulletinBitmap != null) {
            lruCache.put(str, bitmapDownloadBulletinBitmap);
        }
        return bitmapDownloadBulletinBitmap;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void prefetchBulletinUrl(java.lang.String str, java.lang.String str2) {
        if (str2 == null || str2.isEmpty() || sBulletinBitmapCache.get(bulletinDirectUrl(str2)) != null) {
            return;
        }
        java.util.concurrent.atomic.AtomicInteger atomicInteger = sBulletinPrefetchRunning;
        if (atomicInteger.get() < 4 && sRichMediaPrefetching.add(str2)) {
            atomicInteger.incrementAndGet();
            new java.lang.Thread(new com.tencent.qqlitepro.http.QqKernel$$ExternalSyntheticLambda12(str2, str), "bulletin-prefetch").start();
        }
    }

    static /* synthetic */ void lambda$prefetchBulletinUrl$15(java.lang.String str, java.lang.String str2) {
        try {
            java.lang.String strBulletinDirectUrl = bulletinDirectUrl(str);
            if (bulletinBitmapCached(strBulletinDirectUrl) != null) {
                sRichMediaUrlCache.put(str, strBulletinDirectUrl);
                sRichMediaPrefetching.remove(str);
                sBulletinPrefetchRunning.decrementAndGet();
                return;
            }
            java.lang.String strResolveBulletinPicUrl = resolveBulletinPicUrl(0L, str2, str, 3000L);
            if (strResolveBulletinPicUrl != null && strResolveBulletinPicUrl.startsWith("http") && !strResolveBulletinPicUrl.equals(strBulletinDirectUrl) && bulletinBitmapCached(strResolveBulletinPicUrl) != null) {
                sRichMediaUrlCache.put(str, strResolveBulletinPicUrl);
            }
            sRichMediaPrefetching.remove(str);
            sBulletinPrefetchRunning.decrementAndGet();
        } catch (java.lang.Throwable unused) {
        }
    }

    private static void ensureRichMediaListener() {
        if (sRichMediaListenerReady) {
            return;
        }
        try {
            java.lang.Object objKernelGroupApiService = kernelGroupApiService();
            java.lang.Class<?> cls = cls("com.tencent.qqnt.kernel.nativeinterface.IKernelGroupListener");
            java.lang.Object objNewProxyInstance = java.lang.reflect.Proxy.newProxyInstance(cls.getClassLoader(), new java.lang.Class[]{cls}, new com.tencent.qqlitepro.http.QqKernel$$ExternalSyntheticLambda14());
            java.lang.reflect.Method methodFindMethod = findMethod(cls("com.tencent.qqnt.kernel.api.IGroupService"), "m", cls);
            if (methodFindMethod != null) {
                methodFindMethod.setAccessible(true);
                methodFindMethod.invoke(objKernelGroupApiService, objNewProxyInstance);
                sRichMediaListenerReady = true;
            }
        } catch (java.lang.Throwable thRethrow3) {
            com.tencent.qqlitepro.util.Log.w("群公告: 富媒体监听注册失败 " + chain(thRethrow3));
        }
    }

    static /* synthetic */ java.lang.Object lambda$ensureRichMediaListener$16(java.lang.Object obj, java.lang.reflect.Method method, java.lang.Object[] objArr) throws java.lang.Throwable {
        com.tencent.qqlitepro.http.QqKernel.BulletinWaiter bulletinWaiter;
        java.lang.Object obj2;
        try {
            if ("onGroupBulletinRichMediaDownloadComplete".equals(method.getName()) && objArr != null && objArr.length >= 1 && (obj2 = objArr[0]) != null) {
                java.lang.String strStrOf = strOf(fieldValue(obj2, "fileId"));
                int iIntOf = intOf(fieldValue(obj2, "errorCode"));
                java.lang.String strStrOf2 = strOf(fieldValue(obj2, "url"));
                com.tencent.qqlitepro.http.QqKernel.BulletinWaiter bulletinWaiterRemove = sRichMediaWaiters.remove(strStrOf);
                if (bulletinWaiterRemove != null) {
                    bulletinWaiterRemove.richUrl = strStrOf2;
                    bulletinWaiterRemove.richErr = iIntOf;
                    bulletinWaiterRemove.latch.countDown();
                }
                if (iIntOf == 0 && !strStrOf2.isEmpty()) {
                    sRichMediaUrlCache.put(strStrOf, strStrOf2);
                }
            } else if ("onGetGroupBulletinListResult".equals(method.getName()) && objArr != null && objArr.length >= 3 && objArr[2] != null && (bulletinWaiter = sKernelListWaiters.get(java.lang.String.valueOf(objArr[0]))) != null) {
                bulletinWaiter.listResult = objArr[2];
                bulletinWaiter.latch.countDown();
            }
        } catch (java.lang.Throwable unused) {
        }
        java.lang.Class<?> returnType = method.getReturnType();
        if (returnType == java.lang.Boolean.TYPE) {
            return false;
        }
        return returnType.isPrimitive() ? 0 : null;
    }

    public static java.lang.String resolveBulletinPicUrl(long j, java.lang.String str, java.lang.String str2, long j2) {
        if (str2 != null) {
            try {
                if (!str2.isEmpty()) {
                    java.util.concurrent.ConcurrentHashMap<java.lang.String, java.lang.String> concurrentHashMap = sRichMediaUrlCache;
                    java.lang.String str3 = concurrentHashMap.get(str2);
                    if (str3 != null && !str3.isEmpty()) {
                        return str3;
                    }
                    ensureRichMediaListener();
                    com.tencent.qqlitepro.http.QqKernel.BulletinWaiter bulletinWaiter = new com.tencent.qqlitepro.http.QqKernel.BulletinWaiter();
                    java.util.concurrent.ConcurrentHashMap<java.lang.String, com.tencent.qqlitepro.http.QqKernel.BulletinWaiter> concurrentHashMap2 = sRichMediaWaiters;
                    com.tencent.qqlitepro.http.QqKernel.BulletinWaiter bulletinWaiterPutIfAbsent = concurrentHashMap2.putIfAbsent(str2, bulletinWaiter);
                    if (bulletinWaiterPutIfAbsent != null) {
                        bulletinWaiter = bulletinWaiterPutIfAbsent;
                    } else {
                        java.lang.Object objNativeGroupService = nativeGroupService(kernelGroupApiService());
                        java.lang.Class<?> cls = cls("com.tencent.qqnt.kernel.nativeinterface.BulletinFeedsRichMediaDownloadReq");
                        java.lang.Object objNewInstance = cls.newInstance();
                        if (str == null) {
                            str = "";
                        }
                        de.robv.android.xposed.XposedHelpers.setObjectField(objNewInstance, "feedsId", str);
                        de.robv.android.xposed.XposedHelpers.setObjectField(objNewInstance, "fileId", str2);
                        de.robv.android.xposed.XposedHelpers.setObjectField(objNewInstance, "url", "");
                        java.lang.reflect.Method methodFindMethod = findMethod(objNativeGroupService.getClass(), "downloadGroupBulletinRichMedia", cls);
                        if (methodFindMethod == null) {
                            concurrentHashMap2.remove(str2);
                            return "";
                        }
                        methodFindMethod.setAccessible(true);
                        methodFindMethod.invoke(objNativeGroupService, objNewInstance);
                    }
                    bulletinWaiter.latch.await(j2, java.util.concurrent.TimeUnit.MILLISECONDS);
                    java.lang.String str4 = bulletinWaiter.richUrl;
                    if (str4 != null && !str4.isEmpty()) {
                        concurrentHashMap.putIfAbsent(str2, str4);
                        return str4;
                    }
                }
            } catch (java.lang.Throwable thRethrow3) {
                com.tencent.qqlitepro.util.Log.w("群公告: 富媒体图片解析失败 fileId=" + str2 + " " + chain(thRethrow3));
            }
        }
        return "";
    }

    private static boolean ensureGroupBulletinListenerRegistered() {
        try {
            java.lang.Object objKernelGroupApiService = kernelGroupApiService();
            if (objKernelGroupApiService == null) {
                return false;
            }
            java.lang.Class<?> cls = cls("com.tencent.qqnt.kernel.nativeinterface.IKernelGroupListener");
            java.lang.Object staticObjectField = de.robv.android.xposed.XposedHelpers.getStaticObjectField(cls("momoi.mod.qqpro.api.GroupBulletinApi$Listener"), "INSTANCE");
            java.lang.reflect.Method methodFindMethod = findMethod(cls("com.tencent.qqnt.kernel.api.IGroupService"), "m", cls);
            if (methodFindMethod == null) {
                com.tencent.qqlitepro.util.Log.w("群公告: IGroupService.m(IKernelGroupListener) 未找到");
                return false;
            }
            try {
                methodFindMethod.invoke(objKernelGroupApiService, staticObjectField);
            } catch (java.lang.Throwable thRethrow3) {
                com.tencent.qqlitepro.util.Log.w("群公告: 注册 QQMax 公告监听受限: " + chain(thRethrow3));
            }
            return true;
        } catch (java.lang.Throwable th2) {
            com.tencent.qqlitepro.util.Log.w("群公告: 确保公告监听失败 " + chain(th2));
            return false;
        }
    }

    /* JADX WARN: Code duplicated, block: B:24:0x007f  */
    /* JADX WARN: Code duplicated, block: B:26:0x0085  */
    /* JADX WARN: Code duplicated, block: B:29:0x0094 A[Catch: all -> 0x00de, TryCatch #4 {all -> 0x00de, blocks: (B:27:0x0087, B:29:0x0094, B:40:0x00c1, B:32:0x00a3, B:34:0x00aa, B:36:0x00b0, B:39:0x00b7, B:41:0x00c4), top: B:80:0x0087 }] */
    /* JADX WARN: Code duplicated, block: B:31:0x00a2  */
    /* JADX WARN: Code duplicated, block: B:32:0x00a3 A[Catch: all -> 0x00de, TryCatch #4 {all -> 0x00de, blocks: (B:27:0x0087, B:29:0x0094, B:40:0x00c1, B:32:0x00a3, B:34:0x00aa, B:36:0x00b0, B:39:0x00b7, B:41:0x00c4), top: B:80:0x0087 }] */
    /* JADX WARN: Code duplicated, block: B:47:0x012a A[Catch: all -> 0x0154, TryCatch #0 {all -> 0x0154, blocks: (B:45:0x00f4, B:47:0x012a, B:49:0x0138, B:55:0x0146, B:52:0x0140), top: B:72:0x00f4 }] */
    /* JADX WARN: Code duplicated, block: B:55:0x0146 A[Catch: all -> 0x0154, TRY_LEAVE, TryCatch #0 {all -> 0x0154, blocks: (B:45:0x00f4, B:47:0x012a, B:49:0x0138, B:55:0x0146, B:52:0x0140), top: B:72:0x00f4 }] */
    /* JADX WARN: Code duplicated, block: B:61:0x0175 A[Catch: all -> 0x019c, TryCatch #3 {all -> 0x019c, blocks: (B:59:0x016a, B:61:0x0175, B:63:0x0183, B:66:0x018b), top: B:78:0x016a }] */
    /* JADX WARN: Code duplicated, block: B:92:0x0143 A[SYNTHETIC] */
    public static void registerGlobalBulletinListener() {
        if (sGlobalBulletinInitDone) {
            return;
        }
        int hooked = 0;
        try {
            java.lang.String[] names = {"onGetGroupBulletinListResult", "onGroupBulletinChange", "onGroupBulletinRemindNotify"};
            for (java.lang.reflect.Method m : cls("momoi.mod.qqpro.api.GroupBulletinApi$Listener").getDeclaredMethods()) {
                for (java.lang.String name : names) {
                    if (name.equals(m.getName())) {
                        de.robv.android.xposed.XposedBridge.hookMethod(m, new com.tencent.qqlitepro.http.QqKernel.AnonymousClass2());
                        hooked++;
                        break;
                    }
                }
            }
            com.tencent.qqlitepro.util.Log.i("去除数量限制: 已 hook " + hooked + " 个公告监听回调");
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("去除数量限制: hook 安装失败 " + chain(th));
        }
        if (hooked == 0) {
            com.tencent.qqlitepro.util.Log.w("去除数量限制: 公告监听回调未挂载，等待下次重试");
            return;
        }
        sGlobalBulletinInitDone = true;

        // 1) hook GroupBulletinApi 中按时间戳拉取的 fetch 方法
        int fetchHooked = 0;
        try {
            for (java.lang.reflect.Method m : cls("momoi.mod.qqpro.api.GroupBulletinApi").getDeclaredMethods()) {
                if (!"fetch".equals(m.getName())) {
                    continue;
                }
                java.lang.Class<?>[] parameterTypes = m.getParameterTypes();
                if (parameterTypes.length >= 1
                        && (parameterTypes[0] == java.lang.Long.TYPE || parameterTypes[0] == java.lang.Long.class)) {
                    de.robv.android.xposed.XposedBridge.hookMethod(m, new com.tencent.qqlitepro.http.QqKernel.AnonymousClass3());
                    fetchHooked++;
                }
            }
            com.tencent.qqlitepro.util.Log.i("去除数量限制: 已 hook fetch 预取 " + fetchHooked + " 个方法");
        } catch (java.lang.Throwable th2) {
            com.tencent.qqlitepro.util.Log.w("去除数量限制: hook fetch 预取失败 " + chain(th2));
        }

        // 2) hook GroupBulletinApi$Item 的 flatten，过滤空图片
        try {
            java.lang.Class<?> apiCls = cls("momoi.mod.qqpro.api.GroupBulletinApi");
            java.lang.reflect.Constructor<?> itemCtor = cls("momoi.mod.qqpro.api.GroupBulletinApi$Item")
                    .getDeclaredConstructor(java.lang.String.class, java.lang.String.class, java.lang.Integer.TYPE,
                            java.lang.Boolean.TYPE, java.lang.String.class, java.util.List.class);
            itemCtor.setAccessible(true);
            for (java.lang.reflect.Method m : apiCls.getDeclaredMethods()) {
                if ("flatten".equals(m.getName()) && m.getParameterTypes().length == 1) {
                    de.robv.android.xposed.XposedBridge.hookMethod(m, new com.tencent.qqlitepro.http.QqKernel.AnonymousClass4(itemCtor));
                    com.tencent.qqlitepro.util.Log.i("群公告: 已 hook flatten 过滤空图片");
                    break;
                }
            }
        } catch (java.lang.Throwable th3) {
            com.tencent.qqlitepro.util.Log.w("群公告: flatten hook 安装失败 " + chain(th3));
        }

        // 3) hook downloadImage 增强下载
        try {
            for (java.lang.reflect.Method m : cls("momoi.mod.qqpro.api.GroupBulletinApi").getDeclaredMethods()) {
                if ("downloadImage".equals(m.getName()) && m.getParameterTypes().length == 2) {
                    de.robv.android.xposed.XposedBridge.hookMethod(m, new com.tencent.qqlitepro.http.QqKernel.AnonymousClass5());
                    com.tencent.qqlitepro.util.Log.i("群公告: 已 hook downloadImage 增强下载");
                    return;
                }
            }
        } catch (java.lang.Throwable th4) {
            com.tencent.qqlitepro.util.Log.w("群公告: downloadImage hook 安装失败 " + chain(th4));
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.http.QqKernel$2, reason: invalid class name */
    static class AnonymousClass2 extends de.robv.android.xposed.XC_MethodHook {
        AnonymousClass2() {
        }

        protected void beforeHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) throws java.lang.Throwable {
            com.tencent.qqlitepro.http.QqKernel.BulletinWaiter bulletinWaiter;
            java.lang.Object obj;
            java.lang.Object obj2;
            try {
                java.lang.Object[] objArr = methodHookParam.args;
                if (objArr != null && objArr.length >= 1) {
                    boolean z = false;
                    java.lang.Object obj3 = objArr[0];
                    long jLongValue = obj3 instanceof java.lang.Number ? ((java.lang.Number) obj3).longValue() : 0L;
                    java.lang.String name = methodHookParam.method.getName();
                    boolean z2 = "onGroupBulletinChange".equals(name) && objArr.length >= 2;
                    if ("onGetGroupBulletinListResult".equals(name) && objArr.length >= 3) {
                        z = true;
                    }
                    com.tencent.qqlitepro.util.Log.i("去除数量限制: 回调 " + name + " gc=" + jLongValue + " args=" + objArr.length);
                    if (z2 && (obj2 = objArr[1]) != null) {
                        com.tencent.qqlitepro.util.Log.i("群公告: onGroupBulletinChange gc=" + jLongValue + " feedsRecords=" + com.tencent.qqlitepro.http.QqKernel.colSize(com.tencent.qqlitepro.http.QqKernel.fieldValue(obj2, "feedsRecords")));
                        android.content.Context contextAppContext = com.tencent.qqlitepro.cache.MsgCache.appContext();
                        if (contextAppContext != null && com.tencent.qqlitepro.util.Config.bulletinAllEnabled(contextAppContext)) {
                            java.util.ArrayList<java.lang.Object> arrayListCachedFullRecords = com.tencent.qqlitepro.http.QqKernel.cachedFullRecords(jLongValue);
                            if (arrayListCachedFullRecords == null && android.os.Looper.myLooper() != android.os.Looper.getMainLooper()) {
                                arrayListCachedFullRecords = com.tencent.qqlitepro.http.QqKernel.awaitFullBulletins(jLongValue, com.tencent.qqlitepro.http.QqKernel.GROUP_LIST_TIMEOUT_MS);
                            }
                            if (arrayListCachedFullRecords != null) {
                                de.robv.android.xposed.XposedHelpers.setObjectField(objArr[1], "feedsRecords", arrayListCachedFullRecords);
                            } else {
                                com.tencent.qqlitepro.http.QqKernel.scheduleAsyncBulletinDeliver(jLongValue);
                            }
                        }
                    }
                    if (z && (obj = objArr[2]) != null) {
                        com.tencent.qqlitepro.util.Log.i("群公告: onGetGroupBulletinListResult gc=" + jLongValue + " feeds=" + com.tencent.qqlitepro.http.QqKernel.colSize(com.tencent.qqlitepro.http.QqKernel.fieldValue(obj, "feeds")));
                        android.content.Context contextAppContext2 = com.tencent.qqlitepro.cache.MsgCache.appContext();
                        if (contextAppContext2 != null && com.tencent.qqlitepro.util.Config.bulletinAllEnabled(contextAppContext2)) {
                            try {
                                com.tencent.qqlitepro.http.QqKernel.deliverAllBulletins(objArr[2]);
                            } catch (java.lang.Throwable thRethrow4) {
                                com.tencent.qqlitepro.util.Log.w("去除数量限制: 注入全量公告异常 " + thRethrow4);
                            }
                        }
                    }
                    if (z2 && objArr[1] != null && (bulletinWaiter = (com.tencent.qqlitepro.http.QqKernel.BulletinWaiter) com.tencent.qqlitepro.http.QqKernel.sWaiters.get(java.lang.String.valueOf(jLongValue))) != null) {
                        bulletinWaiter.change = objArr[1];
                        bulletinWaiter.latch.countDown();
                    }
                    if (!z || objArr[2] == null) {
                        return;
                    }
                    com.tencent.qqlitepro.http.QqKernel.BulletinWaiter bulletinWaiter2 = (com.tencent.qqlitepro.http.QqKernel.BulletinWaiter) com.tencent.qqlitepro.http.QqKernel.sWaiters.get(java.lang.String.valueOf(jLongValue));
                    if (bulletinWaiter2 != null) {
                        bulletinWaiter2.listResult = objArr[2];
                        bulletinWaiter2.latch.countDown();
                    }
                    com.tencent.qqlitepro.http.QqKernel.BulletinWaiter bulletinWaiter3 = (com.tencent.qqlitepro.http.QqKernel.BulletinWaiter) com.tencent.qqlitepro.http.QqKernel.sKernelListWaiters.get(java.lang.String.valueOf(jLongValue));
                    if (bulletinWaiter3 != null) {
                        bulletinWaiter3.listResult = objArr[2];
                        bulletinWaiter3.latch.countDown();
                    }
                }
            } catch (java.lang.Throwable unused) {
            }
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.http.QqKernel$3, reason: invalid class name */
    static class AnonymousClass3 extends de.robv.android.xposed.XC_MethodHook {
        AnonymousClass3() {
        }

        protected void beforeHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) throws java.lang.Throwable {
            android.content.Context contextAppContext;
            try {
                java.lang.Object obj = (methodHookParam.args == null || methodHookParam.args.length <= 0) ? null : methodHookParam.args[0];
                long jLongValue = obj instanceof java.lang.Number ? ((java.lang.Number) obj).longValue() : 0L;
                if (jLongValue == 0 || (contextAppContext = com.tencent.qqlitepro.cache.MsgCache.appContext()) == null || !com.tencent.qqlitepro.util.Config.bulletinAllEnabled(contextAppContext)) {
                    return;
                }
                com.tencent.qqlitepro.http.QqKernel.forceRefreshBulletins(jLongValue);
            } catch (java.lang.Throwable unused) {
            }
        }

        protected void afterHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) throws java.lang.Throwable {
            try {
                java.lang.Object obj = (methodHookParam.args == null || methodHookParam.args.length <= 0) ? null : methodHookParam.args[0];
                long jLongValue = obj instanceof java.lang.Number ? ((java.lang.Number) obj).longValue() : 0L;
                if (jLongValue == 0) {
                    return;
                }
                java.lang.Object staticObjectField = de.robv.android.xposed.XposedHelpers.getStaticObjectField(com.tencent.qqlitepro.http.QqKernel.cls("momoi.mod.qqpro.api.GroupBulletinApi"), "pending");
                if (staticObjectField instanceof java.util.Map) {
                    java.lang.Object obj2 = ((java.util.Map) staticObjectField).get(java.lang.Long.valueOf(jLongValue));
                    if (!(obj2 instanceof java.util.List) || ((java.util.List) obj2).isEmpty()) {
                        return;
                    }
                    com.tencent.qqlitepro.http.QqKernel.sFetchCallbacks.put(java.lang.Long.valueOf(jLongValue), new java.util.ArrayList((java.util.List) obj2));
                }
            } catch (java.lang.Throwable unused) {
            }
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.http.QqKernel$4, reason: invalid class name */
    static class AnonymousClass4 extends de.robv.android.xposed.XC_MethodHook {
        final /* synthetic */ java.lang.reflect.Constructor val$itemCtor;

        AnonymousClass4(java.lang.reflect.Constructor constructor) {
            this.val$itemCtor = constructor;
        }

        protected void afterHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            try {
                java.lang.Object result = methodHookParam.getResult();
                if (result instanceof java.util.List) {
                    java.util.List list = (java.util.List) result;
                    java.util.ArrayList arrayList = new java.util.ArrayList(list.size());
                    boolean z = false;
                    for (java.lang.Object obj : list) {
                        java.lang.Object objCallMethod = de.robv.android.xposed.XposedHelpers.callMethod(obj, "getImages", new java.lang.Object[0]);
                        java.util.List list2 = objCallMethod instanceof java.util.List ? (java.util.List) objCallMethod : null;
                        if (list2 == null || list2.isEmpty()) {
                            arrayList.add(obj);
                        } else {
                            java.util.ArrayList arrayList2 = new java.util.ArrayList(list2.size());
                            for (java.lang.Object obj2 : list2) {
                                java.lang.String strStrOf = com.tencent.qqlitepro.http.QqKernel.strOf(de.robv.android.xposed.XposedHelpers.callMethod(obj2, "getUrl", new java.lang.Object[0]));
                                java.lang.String strStrOf2 = com.tencent.qqlitepro.http.QqKernel.strOf(de.robv.android.xposed.XposedHelpers.callMethod(obj2, "getFileId", new java.lang.Object[0]));
                                boolean zStartsWith = strStrOf.startsWith("http");
                                if (zStartsWith || !strStrOf2.isEmpty() || !strStrOf.isEmpty()) {
                                    arrayList2.add(obj2);
                                }
                                if (!zStartsWith) {
                                    if (!strStrOf2.isEmpty()) {
                                        strStrOf = strStrOf2;
                                    }
                                    if (!strStrOf.isEmpty()) {
                                        com.tencent.qqlitepro.http.QqKernel.prefetchBulletinUrl(com.tencent.qqlitepro.http.QqKernel.strOf(de.robv.android.xposed.XposedHelpers.callMethod(obj2, "getFeedsId", new java.lang.Object[0])), strStrOf);
                                    }
                                }
                            }
                            if (arrayList2.size() == list2.size()) {
                                arrayList.add(obj);
                            } else {
                                arrayList.add(this.val$itemCtor.newInstance(de.robv.android.xposed.XposedHelpers.callMethod(obj, "getFeedId", new java.lang.Object[0]), de.robv.android.xposed.XposedHelpers.callMethod(obj, "getFromUid", new java.lang.Object[0]), de.robv.android.xposed.XposedHelpers.callMethod(obj, "getTime", new java.lang.Object[0]), de.robv.android.xposed.XposedHelpers.callMethod(obj, "getPinned", new java.lang.Object[0]), de.robv.android.xposed.XposedHelpers.callMethod(obj, "getText", new java.lang.Object[0]), arrayList2));
                                z = true;
                            }
                        }
                    }
                    if (z) {
                        list.clear();
                        list.addAll(arrayList);
                        com.tencent.qqlitepro.util.Log.i("群公告: flatten 已过滤空 url 图片入口");
                    }
                }
            } catch (java.lang.Throwable thRethrow4) {
                com.tencent.qqlitepro.util.Log.w("群公告: flatten 图片过滤异常 " + com.tencent.qqlitepro.http.QqKernel.chain(thRethrow4));
            }
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.http.QqKernel$5, reason: invalid class name */
    static class AnonymousClass5 extends de.robv.android.xposed.XC_MethodHook {
        AnonymousClass5() {
        }

        protected void beforeHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            try {
                java.lang.Object obj = methodHookParam.args[0];
                java.lang.Object obj2 = methodHookParam.args[1];
                java.lang.String strStrOf = com.tencent.qqlitepro.http.QqKernel.strOf(de.robv.android.xposed.XposedHelpers.callMethod(obj, "getUrl", new java.lang.Object[0]));
                java.lang.String strStrOf2 = com.tencent.qqlitepro.http.QqKernel.strOf(de.robv.android.xposed.XposedHelpers.callMethod(obj, "getFileId", new java.lang.Object[0]));
                java.lang.String strStrOf3 = com.tencent.qqlitepro.http.QqKernel.strOf(de.robv.android.xposed.XposedHelpers.callMethod(obj, "getFeedsId", new java.lang.Object[0]));
                android.os.Handler handler = new android.os.Handler(android.os.Looper.getMainLooper());
                methodHookParam.setResult((java.lang.Object) null);
                new java.lang.Thread(new com.tencent.qqlitepro.http.QqKernel$5$$ExternalSyntheticLambda1(strStrOf, strStrOf2, strStrOf3, handler, obj2), "bulletin-img").start();
            } catch (java.lang.Throwable thRethrow4) {
                com.tencent.qqlitepro.util.Log.w("群公告: 接管图片下载异常 " + com.tencent.qqlitepro.http.QqKernel.chain(thRethrow4));
            }
        }

        static /* synthetic */ void lambda$beforeHookedMethod$1(java.lang.String str, java.lang.String str2, java.lang.String str3, android.os.Handler handler, java.lang.Object obj) {
            java.lang.String str4;
            java.lang.String strResolveBulletinPicUrl;
            java.lang.String str5;
            java.lang.String strBulletinDirectUrl = str.startsWith("http") ? str : "";
            if (strBulletinDirectUrl.isEmpty() && (str5 = (java.lang.String) com.tencent.qqlitepro.http.QqKernel.sRichMediaUrlCache.get(str2)) != null && str5.startsWith("http")) {
                strBulletinDirectUrl = str5;
            }
            if (strBulletinDirectUrl.isEmpty()) {
                strBulletinDirectUrl = com.tencent.qqlitepro.http.QqKernel.bulletinDirectUrl(!str.isEmpty() ? str : str2);
            }
            android.graphics.Bitmap bitmapBulletinBitmapCached = com.tencent.qqlitepro.http.QqKernel.bulletinBitmapCached(strBulletinDirectUrl);
            if (bitmapBulletinBitmapCached != null || str2.isEmpty() || (strResolveBulletinPicUrl = com.tencent.qqlitepro.http.QqKernel.resolveBulletinPicUrl(0L, str3, str2, 3000L)) == null || !strResolveBulletinPicUrl.startsWith("http") || strResolveBulletinPicUrl.equals(strBulletinDirectUrl) || (bitmapBulletinBitmapCached = com.tencent.qqlitepro.http.QqKernel.bulletinBitmapCached(strResolveBulletinPicUrl)) == null) {
                str4 = strBulletinDirectUrl;
            } else {
                com.tencent.qqlitepro.http.QqKernel.sRichMediaUrlCache.put(str2, strResolveBulletinPicUrl);
                str4 = strResolveBulletinPicUrl;
            }
            handler.post(new com.tencent.qqlitepro.http.QqKernel$5$$ExternalSyntheticLambda0(obj, bitmapBulletinBitmapCached, str, str2, str4));
        }

        static /* synthetic */ void lambda$beforeHookedMethod$0(java.lang.Object obj, android.graphics.Bitmap bitmap, java.lang.String str, java.lang.String str2, java.lang.String str3) {
            try {
                com.tencent.qqlitepro.http.QqKernel.invokeBulletinCallback(obj, bitmap);
                java.lang.StringBuilder sb = new java.lang.StringBuilder("群公告: 图片");
                sb.append(bitmap != null ? "下载成功" : "下载失败");
                sb.append(" url=");
                sb.append(str);
                sb.append(" fid=");
                sb.append(str2);
                sb.append(" -> ");
                sb.append(str3);
                com.tencent.qqlitepro.util.Log.i(sb.toString());
            } catch (java.lang.Throwable thRethrow4) {
                com.tencent.qqlitepro.util.Log.w("群公告: 图片回调异常 " + com.tencent.qqlitepro.http.QqKernel.chain(thRethrow4));
            }
        }
    }

    private static android.graphics.Bitmap downloadBulletinBitmap(java.lang.String str) {
        java.io.InputStream inputStream;
        java.net.HttpURLConnection httpURLConnection;
        try {
            if (str.contains("/gdynamic/") && !str.matches(".*\\/\\d+$")) {
                str = str + "/0";
            }
            httpURLConnection = (java.net.HttpURLConnection) new java.net.URL(str).openConnection();
            try {
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setConnectTimeout(com.tencent.qqlitepro.plugin.PluginEngine.LOG_MAX);
                httpURLConnection.setReadTimeout(12000);
                httpURLConnection.setInstanceFollowRedirects(true);
                httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 13) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36");
                httpURLConnection.setRequestProperty("Referer", "https://web.qun.qq.com/");
                httpURLConnection.setRequestProperty("Accept", "image/avif,image/webp,image/apng,image/*,*/*;q=0.8");
                int responseCode = httpURLConnection.getResponseCode();
                if (responseCode != 200) {
                    com.tencent.qqlitepro.util.Log.w("群公告: 图片 http=" + responseCode + " url=" + str);
                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
                    }
                    return null;
                }
                java.io.InputStream inputStream2 = httpURLConnection.getInputStream();
                try {
                    android.graphics.Bitmap bitmapDecodeStream = android.graphics.BitmapFactory.decodeStream(inputStream2);
                    if (inputStream2 != null) {
                        try {
                            inputStream2.close();
                        } catch (java.lang.Throwable unused) {
                        }
                    }
                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
                    }
                    return bitmapDecodeStream;
                } catch (java.lang.Throwable thRethrow4) {
                    inputStream = inputStream2;
                    try {
                        com.tencent.qqlitepro.util.Log.w("群公告: 图片下载异常 " + chain(thRethrow4));
                        return null;
                    } finally {
                        if (inputStream != null) {
                            try {
                                inputStream.close();
                            } catch (java.lang.Throwable unused2) {
                            }
                        }
                        if (httpURLConnection != null) {
                            httpURLConnection.disconnect();
                        }
                    }
                }
            } catch (java.lang.Throwable th2) {
                java.lang.Throwable thRethrow5 = th2;
                inputStream = null;
            }
        } catch (java.lang.Throwable th3) {
            java.lang.Throwable thRethrow6 = th3;
            inputStream = null;
            httpURLConnection = null;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.lang.String bulletinDirectUrl(java.lang.String str) {
        if (str == null || str.isEmpty()) {
            return "";
        }
        if (str.startsWith("http")) {
            return str;
        }
        if (str.startsWith("/")) {
            return "https://web.qun.qq.com" + str;
        }
        return "http://gdynamic.qpic.cn/gdynamic/" + str + "/0";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void invokeBulletinCallback(java.lang.Object obj, java.lang.Object obj2) throws java.lang.Throwable {
        java.lang.reflect.Method declaredMethod;
        java.lang.Class<?> superclass = obj.getClass();
        while (true) {
            if (superclass == null || superclass == java.lang.Object.class) {
                declaredMethod = null;
                break;
            } else {
                try {
                    declaredMethod = superclass.getDeclaredMethod("invoke", java.lang.Object.class);
                    break;
                } catch (java.lang.Throwable unused) {
                    superclass = superclass.getSuperclass();
                }
            }
        }
        if (declaredMethod == null) {
            com.tencent.qqlitepro.util.Log.w("群公告: 图片回调 invoke 未找到 " + obj.getClass().getName());
        } else {
            declaredMethod.setAccessible(true);
            declaredMethod.invoke(obj, obj2);
        }
    }

    public static void installTempChatForceShowHook() {
        if (sTempChatShowHookInstalled) {
            return;
        }
        loadTempChatKnown();
        int iHookTempChatRecv = 0;
        for (java.lang.String str : TEMP_CHAT_LISTENERS) {
            iHookTempChatRecv = iHookTempChatRecv + hookTempChatRecv(str, "onRecvMsg") + hookTempChatRecv(str, "onMsgInfoListAdd");
        }
        int iHookTempChatInfo = iHookTempChatRecv + hookTempChatInfo("com.tencent.qqnt.chats.main.vm.datasource.PlatformRecentContactsDataSource") + hookTempChatInfo("com.tencent.qqnt.watch.chat.refresh.RefreshHelper") + installTempChatListInjectHook() + installTempChatRenderFixHook() + installTempChatRenderDedupHook() + installTempChatFriendBypassHook() + installTempChatSendBypassHook() + installTempChatManualDeleteHook();
        if (iHookTempChatInfo > 0) {
            sTempChatShowHookInstalled = true;
        }
        com.tencent.qqlitepro.util.Log.i("临时会话: 强制显示 hook 安装 " + iHookTempChatInfo + " 个（消息回调/数据源捕获/渲染关口/好友校验/发送放行/手动删除）");
        scheduleTempChatRestore();
    }

    private static int installTempChatListInjectHook() {
        try {
            java.lang.Class<?> cls = cls("com.tencent.qqnt.chats.main.vm.datasource.PlatformRecentContactsDataSource");
            if (cls == null) {
                return 1;
            }
            de.robv.android.xposed.XposedBridge.hookAllConstructors(cls, new com.tencent.qqlitepro.http.QqKernel.AnonymousClass6());
            java.lang.reflect.Method methodFindMethod = findMethod(cls, "g", new java.lang.Class[0]);
            if (methodFindMethod == null) {
                return 1;
            }
            de.robv.android.xposed.XposedBridge.hookMethod(methodFindMethod, new com.tencent.qqlitepro.http.QqKernel.AnonymousClass7());
            return 1;
        } catch (java.lang.Throwable thRethrow6) {
            com.tencent.qqlitepro.util.Log.w("临时会话: 安装主列表注入 hook 失败 " + chain(thRethrow6));
            return 0;
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.http.QqKernel$6, reason: invalid class name */
    static class AnonymousClass6 extends de.robv.android.xposed.XC_MethodHook {
        AnonymousClass6() {
        }

        protected void afterHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            com.tencent.qqlitepro.http.QqKernel.noteTempChatDatasource(methodHookParam.thisObject);
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.http.QqKernel$7, reason: invalid class name */
    static class AnonymousClass7 extends de.robv.android.xposed.XC_MethodHook {
        AnonymousClass7() {
        }

        protected void afterHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            com.tencent.qqlitepro.http.QqKernel.noteTempChatDatasource(methodHookParam.thisObject);
        }
    }

    private static int installTempChatFriendBypassHook() {
        int i = 0;
        try {
            java.lang.reflect.Method methodFindMethod = findMethod(cls("com.tencent.qqnt.watch.contact.api.impl.ContactRuntimeServiceImpl"), "isFriend", java.lang.String.class);
            if (methodFindMethod != null) {
                de.robv.android.xposed.XposedBridge.hookMethod(methodFindMethod, new com.tencent.qqlitepro.http.QqKernel.AnonymousClass8());
                i = 1;
            }
        } catch (java.lang.Throwable thRethrow6) {
            com.tencent.qqlitepro.util.Log.w("临时会话: 安装好友校验放行 hook 失败 " + chain(thRethrow6));
        }
        com.tencent.qqlitepro.util.Log.i("临时会话: 好友校验放行 hook 安装 " + i + " 个");
        return i;
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.http.QqKernel$8, reason: invalid class name */
    static class AnonymousClass8 extends de.robv.android.xposed.XC_MethodHook {
        AnonymousClass8() {
        }

        protected void beforeHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            if (methodHookParam.args == null || methodHookParam.args.length <= 0 || !com.tencent.qqlitepro.http.QqKernel.isKnownTempChatPeer(methodHookParam.args[0])) {
                return;
            }
            methodHookParam.setResult(java.lang.Boolean.TRUE);
        }
    }

    private static int installTempChatSendBypassHook() {
        java.lang.Class<?> clsClsQuiet = clsQuiet("com.tencent.qqnt.kernel.api.impl.MsgService");
        if (clsClsQuiet == null) {
            com.tencent.qqlitepro.util.Log.w("临时会话: 未找到内核 MsgService，跳过发送放行");
            return 0;
        }
        int i = 0;
        for (java.lang.reflect.Method method : clsClsQuiet.getDeclaredMethods()) {
            java.lang.Class<?>[] parameterTypes = method.getParameterTypes();
            if ("sendMsg".equals(method.getName()) && parameterTypes.length == 5 && parameterTypes[0] == java.lang.Long.TYPE && parameterTypes[1].getName().endsWith(".Contact")) {
                de.robv.android.xposed.XposedBridge.hookMethod(method, new com.tencent.qqlitepro.http.QqKernel.AnonymousClass9());
                i++;
            }
        }
        com.tencent.qqlitepro.util.Log.i("临时会话: 发送放行 hook 安装 " + i + " 个");
        return i;
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.http.QqKernel$9, reason: invalid class name */
    static class AnonymousClass9 extends de.robv.android.xposed.XC_MethodHook {
        AnonymousClass9() {
        }

        protected void beforeHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            try {
                java.lang.Object obj = methodHookParam.args[1];
                if (obj != null && com.tencent.qqlitepro.http.QqKernel.isKnownTempChatPeer(com.tencent.qqlitepro.http.QqKernel.fieldValue(obj, "peerUid")) && com.tencent.qqlitepro.http.QqKernel.intOf(com.tencent.qqlitepro.http.QqKernel.fieldValue(obj, "chatType")) == 1) {
                    de.robv.android.xposed.XposedHelpers.setIntField(obj, "chatType", 100);
                    if (methodHookParam.args.length > 4 && methodHookParam.args[4] != null) {
                        methodHookParam.args[4] = com.tencent.qqlitepro.http.QqKernel.wrapTempChatSendCallback(methodHookParam.args[4]);
                    }
                    com.tencent.qqlitepro.util.Log.i("临时会话: 发送放行 peerUid=" + com.tencent.qqlitepro.http.QqKernel.strOf(com.tencent.qqlitepro.http.QqKernel.fieldValue(obj, "peerUid")) + " chatType 1->100");
                }
            } catch (java.lang.Throwable thRethrow6) {
                com.tencent.qqlitepro.util.Log.w("临时会话: 发送放行失败 " + com.tencent.qqlitepro.http.QqKernel.chain(thRethrow6));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.lang.Object wrapTempChatSendCallback(java.lang.Object obj) {
        try {
            java.lang.Class<?> cls = cls("com.tencent.qqnt.kernel.nativeinterface.IOperateCallback");
            if (cls != null && cls.isInstance(obj)) {
                return java.lang.reflect.Proxy.newProxyInstance(obj.getClass().getClassLoader(), new java.lang.Class[]{cls}, new com.tencent.qqlitepro.http.QqKernel$$ExternalSyntheticLambda33(obj));
            }
            return obj;
        } catch (java.lang.Throwable thRethrow6) {
            com.tencent.qqlitepro.util.Log.w("临时会话: 包装发送回调失败 " + chain(thRethrow6));
            return obj;
        }
    }

    static /* synthetic */ java.lang.Object lambda$wrapTempChatSendCallback$17(java.lang.Object obj, java.lang.Object obj2, java.lang.reflect.Method method, java.lang.Object[] objArr) throws java.lang.Throwable {
        try {
            if ("onResult".equals(method.getName()) && objArr != null && objArr.length > 0) {
                java.lang.Object obj3 = objArr[0];
                if ((obj3 instanceof java.lang.Integer) && ((java.lang.Integer) obj3).intValue() != 0) {
                    showTempChatSendFailToast();
                }
            }
        } catch (java.lang.Throwable unused) {
        }
        try {
            return method.invoke(obj, objArr);
        } catch (java.lang.reflect.InvocationTargetException e) {
            if (e.getCause() != null) {
                throw e.getCause();
            }
            throw e;
        }
    }

    private static void showTempChatSendFailToast() {
        android.content.Context contextAppContext = appContext();
        if (contextAppContext == null) {
            return;
        }
        new android.os.Handler(android.os.Looper.getMainLooper()).post(new com.tencent.qqlitepro.http.QqKernel$$ExternalSyntheticLambda11(contextAppContext));
    }

    static /* synthetic */ void lambda$showTempChatSendFailToast$18(android.content.Context context) {
        try {
            java.lang.Class<?> clsClsQuiet = clsQuiet("com.tencent.qqnt.watch.ui.componet.toast.WatchToast");
            if (clsClsQuiet == null) {
                return;
            }
            java.lang.Object objInvoke = clsClsQuiet.getMethod("g", android.content.Context.class, java.lang.Integer.TYPE, java.lang.CharSequence.class, java.lang.Integer.TYPE).invoke(null, context, 1, "需在移动端主动发起临时会话", 0);
            if (objInvoke != null) {
                objInvoke.getClass().getMethod("l", new java.lang.Class[0]).invoke(objInvoke, new java.lang.Object[0]);
            }
            com.tencent.qqlitepro.util.Log.i("临时会话: 已提示「需在移动端主动发起临时会话」(原生 Toast)");
        } catch (java.lang.Throwable thRethrow6) {
            com.tencent.qqlitepro.util.Log.w("临时会话: 原生 Toast 提示失败 " + chain(thRethrow6));
        }
    }

    private static int installTempChatManualDeleteHook() {
        java.lang.Class<?> clsClsQuiet = clsQuiet("com.tencent.qqnt.chats.main.vm.usecase.menu.DefaultDeleteMenuUseCase");
        if (clsClsQuiet == null) {
            com.tencent.qqlitepro.util.Log.w("临时会话: 未找到 DefaultDeleteMenuUseCase，跳过手动删除 hook");
            return 0;
        }
        int i = 0;
        for (java.lang.reflect.Method method : clsClsQuiet.getDeclaredMethods()) {
            if ("b".equals(method.getName()) && method.getParameterTypes().length == 1) {
                de.robv.android.xposed.XposedBridge.hookMethod(method, new com.tencent.qqlitepro.http.QqKernel.AnonymousClass10());
                i++;
            }
        }
        com.tencent.qqlitepro.util.Log.i("临时会话: 手动删除 hook 安装 " + i + " 个");
        return i;
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.http.QqKernel$10, reason: invalid class name */
    static class AnonymousClass10 extends de.robv.android.xposed.XC_MethodHook {
        AnonymousClass10() {
        }

        protected void beforeHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            try {
                java.lang.Object objFieldValue = com.tencent.qqlitepro.http.QqKernel.fieldValue(methodHookParam.args[0], "item");
                java.lang.String strStrOf = com.tencent.qqlitepro.http.QqKernel.strOf(com.tencent.qqlitepro.http.QqKernel.fieldValue(objFieldValue, "chatUid"));
                if (strStrOf.isEmpty()) {
                    strStrOf = com.tencent.qqlitepro.http.QqKernel.strOf(com.tencent.qqlitepro.http.QqKernel.fieldValue(com.tencent.qqlitepro.http.QqKernel.fieldValue(objFieldValue, "originData"), "peerUid"));
                }
                com.tencent.qqlitepro.http.QqKernel.forgetTempChat(strStrOf);
            } catch (java.lang.Throwable thRethrow6) {
                com.tencent.qqlitepro.util.Log.w("临时会话: 手动删除处理失败 " + com.tencent.qqlitepro.http.QqKernel.chain(thRethrow6));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void forgetTempChat(java.lang.String str) {
        if (str == null || str.isEmpty()) {
            return;
        }
        java.util.concurrent.ConcurrentHashMap<java.lang.String, java.lang.Object[]> concurrentHashMap = sTempChatKnown;
        if (concurrentHashMap.remove(str.trim()) == null) {
            return;
        }
        sTempChatPending.remove(str.trim());
        saveTempChatKnown();
        com.tencent.qqlitepro.util.Log.i("临时会话: 手动删除卡片 peerUid=" + str + " 剩余登记=" + concurrentHashMap.size());
    }

    public static boolean isKnownTempChatPeer(java.lang.Object obj) {
        return tempChatKnownEntry(obj) != null;
    }

    public static java.lang.Object[] tempChatKnownEntry(java.lang.Object obj) {
        if (!(obj instanceof java.lang.String)) {
            return null;
        }
        java.lang.String strTrim = ((java.lang.String) obj).trim();
        if (strTrim.isEmpty()) {
            return null;
        }
        java.util.concurrent.ConcurrentHashMap<java.lang.String, java.lang.Object[]> concurrentHashMap = sTempChatKnown;
        java.lang.Object[] objArr = concurrentHashMap.get(strTrim);
        if (objArr != null) {
            return objArr;
        }
        java.lang.String strNormPeerUid = normPeerUid(strTrim);
        for (java.util.Map.Entry<java.lang.String, java.lang.Object[]> entry : concurrentHashMap.entrySet()) {
            java.lang.String key = entry.getKey();
            if (key.equalsIgnoreCase(strTrim) || normPeerUid(key).equals(strNormPeerUid)) {
                return entry.getValue();
            }
        }
        return null;
    }

    private static java.lang.String normPeerUid(java.lang.String str) {
        return str == null ? "" : str.replace("_", "").toLowerCase(java.util.Locale.ROOT);
    }

    public static long tempChatUinOf(java.lang.String str) {
        java.lang.Object[] objArrTempChatKnownEntry = tempChatKnownEntry(str);
        if (objArrTempChatKnownEntry == null) {
            return 0L;
        }
        return longOf(objArrTempChatKnownEntry[0]);
    }

    /* ===== 临时会话卡片：独立长按菜单（可置顶/免打扰/删除，互不影响其它会话） ===== */
    private static final java.util.Map<android.view.View, java.lang.String> sTempChatViewUid =
            java.util.Collections.synchronizedMap(new java.util.WeakHashMap<android.view.View, java.lang.String>());

    /** 给临时会话卡片的 View 挂上独立长按（只对该卡片生效） */
    public static void attachTempChatLongPress(android.view.View view, final java.lang.String peerUid) {
        if (view == null || peerUid == null || peerUid.isEmpty()) {
            return;
        }
        String norm = strictKey(peerUid);
        String prev = sTempChatViewUid.get(view);
        if (norm.equals(prev)) {
            return; // 已挂且归属一致，避免重复设置
        }
        sTempChatViewUid.put(view, norm);
        view.setOnLongClickListener(new android.view.View.OnLongClickListener() {
            @Override
            public boolean onLongClick(android.view.View v) {
                try {
                    showTempChatMenu(v.getContext(), peerUid);
                    return true;
                } catch (java.lang.Throwable th) {
                    com.tencent.qqlitepro.util.Log.w("临时会话: 长按菜单异常 " + chain(th));
                    return false;
                }
            }
        });
    }

    /** 临时会话专属菜单 */
    /**
     * 临时会话长按菜单：与宿主会话卡片菜单保持一致（不自建）。
     * 直接复用 ChatCardInjector.showNativeMailMenu 的宿主 SelectDialogFragment 路径，
     * 菜单项（删除/置顶/免打扰/清空消息）全部是宿主原生长按菜单项，
     * 点击行为由 ensureNativeMenuHooks 注册的宿主逻辑处理。
     */
    public static void showTempChatMenu(final android.content.Context context, final java.lang.String peerUid) {
        if (context == null || peerUid == null || peerUid.isEmpty()) {
            return;
        }
        try {
            // 先登记"本次要操作的会话"，供菜单 hook 内的删除逻辑定位目标
            sMenuTargetUid = normPeerUid(peerUid);
            com.tencent.qqlitepro.hook.ChatCardInjector.showNativeMailMenu(context);
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("临时会话: 弹出宿主菜单失败 " + chain(th));
        }
    }

    /** 当前长按菜单对应的目标会话（宿主菜单 hook 回调时读取） */
    private static volatile java.lang.String sMenuTargetUid;

    /** 供宿主菜单 hook（删除/置顶/免打扰）取目标 peerUid */
    public static java.lang.String menuTargetUid() {
        return sMenuTargetUid;
    }

    /** 当前长按菜单目标的展示名 */
    private static volatile java.lang.String sMenuTargetName;

    public static void setMenuTargetName(java.lang.String n) {
        sMenuTargetName = n;
    }

    public static java.lang.String menuTargetName() {
        return sMenuTargetName;
    }

    /** 群聊消息统计入口：登记目标 uin/名称后打开（由群聊菜单调用） */
    public static void noteMenuTarget(long uin, java.lang.String name) {
        sMenuTargetUinLong = uin;
        sMenuTargetName = name;
    }

    private static volatile long sMenuTargetUinLong;

    public static long menuTargetUinLong() {
        return sMenuTargetUinLong;
    }

    private static int installTempChatRenderFixHook() {
        java.lang.Class<?> clsClsQuiet = clsQuiet("com.tencent.qqnt.chats.main.vm.datasource.LocalChatsCacheDataSource");
        if (clsClsQuiet == null) {
            com.tencent.qqlitepro.util.Log.w("临时会话: 未找到 LocalChatsCacheDataSource");
            return 0;
        }
        int i = 0;
        for (java.lang.reflect.Method method : clsClsQuiet.getDeclaredMethods()) {
            java.lang.String name = method.getName();
            java.lang.Class<?>[] parameterTypes = method.getParameterTypes();
            boolean z = "a".equals(name) && parameterTypes.length == 3 && parameterTypes[0] == java.lang.Integer.TYPE && parameterTypes[2].getName().endsWith("RecentContactInfo");
            boolean z2 = "e".equals(name) && parameterTypes.length == 2 && parameterTypes[1] == java.lang.Boolean.TYPE && parameterTypes[0].getName().endsWith("RecentContactListChangedInfo");
            boolean z3 = "f".equals(name) && parameterTypes.length == 3 && parameterTypes[0] == java.util.List.class && parameterTypes[1] == java.util.List.class && parameterTypes[2] == java.lang.Boolean.TYPE;
            if (z || z2 || z3) {
                de.robv.android.xposed.XposedBridge.hookMethod(method, new com.tencent.qqlitepro.http.QqKernel.AnonymousClass11(name));
                i++;
            }
        }
        com.tencent.qqlitepro.util.Log.i("临时会话: 渲染关口 hook 安装 " + i + " 个（LocalChatsCacheDataSource）");
        return i;
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.http.QqKernel$11, reason: invalid class name */
    static class AnonymousClass11 extends de.robv.android.xposed.XC_MethodHook {
        final /* synthetic */ java.lang.String val$name;

        AnonymousClass11(java.lang.String str) {
            this.val$name = str;
        }

        protected void beforeHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            try {
                com.tencent.qqlitepro.http.QqKernel.patchTempChatRenderArgs(this.val$name, methodHookParam);
            } catch (java.lang.Throwable thRethrow6) {
                com.tencent.qqlitepro.util.Log.w("临时会话: 渲染关口转换失败 " + com.tencent.qqlitepro.http.QqKernel.chain(thRethrow6));
            }
        }
    }

    private static int installTempChatRenderDedupHook() {
        java.lang.Class<?> clsClsQuiet = clsQuiet("com.tencent.qqnt.chats.main.vm.datasource.LocalChatsCacheDataSource");
        if (clsClsQuiet == null) {
            return 0;
        }
        int i = 0;
        for (java.lang.reflect.Method method : clsClsQuiet.getDeclaredMethods()) {
            if ("j".equals(method.getName())) {
                java.lang.Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length == 6 && parameterTypes[0] == java.util.List.class && parameterTypes[1] == java.util.List.class && parameterTypes[2] == java.util.HashSet.class) {
                    de.robv.android.xposed.XposedBridge.hookMethod(method, new com.tencent.qqlitepro.http.QqKernel.AnonymousClass12());
                    i++;
                }
            }
        }
        com.tencent.qqlitepro.util.Log.i("临时会话: 列表去重 hook 安装 " + i + " 个");
        return i;
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.http.QqKernel$12, reason: invalid class name */
    static class AnonymousClass12 extends de.robv.android.xposed.XC_MethodHook {
        AnonymousClass12() {
        }

        protected void beforeHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            try {
                com.tencent.qqlitepro.http.QqKernel.dedupTempChatRenderArgs(methodHookParam);
            } catch (java.lang.Throwable thRethrow6) {
                com.tencent.qqlitepro.util.Log.w("临时会话: 列表去重失败 " + com.tencent.qqlitepro.http.QqKernel.chain(thRethrow6));
            }
        }

        protected void afterHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            try {
                com.tencent.qqlitepro.http.QqKernel.dedupTempChatRenderMap(methodHookParam.thisObject);
                com.tencent.qqlitepro.http.QqKernel.recordTempChatOrder(methodHookParam.thisObject);
            } catch (java.lang.Throwable thRethrow6) {
                com.tencent.qqlitepro.util.Log.w("临时会话: 列表去重(兜底)失败 " + com.tencent.qqlitepro.http.QqKernel.chain(thRethrow6));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void dedupTempChatRenderArgs(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
        java.lang.Object obj = methodHookParam.args[0];
        if (obj instanceof java.util.List) {
            java.util.List list = (java.util.List) obj;
            if (list.size() < 2) {
                return;
            }
            java.util.HashMap map = new java.util.HashMap();
            java.util.HashMap map2 = new java.util.HashMap();
            if (methodHookParam.args[1] instanceof java.util.List) {
                for (java.lang.Object obj2 : (java.util.List) methodHookParam.args[1]) {
                    java.lang.Object objFieldValue = fieldValue(obj2, "a");
                    if (isRecentContactInfo(objFieldValue)) {
                        putTempChatKey(map, map2, obj2, longOf(fieldValue(objFieldValue, "contactId")));
                    }
                }
            }
            java.lang.Object objFieldValue2 = fieldValue(methodHookParam.thisObject, "f");
            if (objFieldValue2 instanceof java.util.Map) {
                for (java.util.Map.Entry<?, ?> entry : ((java.util.Map<?, ?>) objFieldValue2).entrySet()) {
                    putTempChatKey(map, map2, entry.getValue(), longOf(entry.getKey()));
                }
            }
            if (map.isEmpty()) {
                return;
            }
            java.util.HashMap map3 = new java.util.HashMap();
            java.util.HashMap map4 = new java.util.HashMap();
            java.util.Iterator it = list.iterator();
            while (it.hasNext()) {
                long jLongOf = longOf(it.next());
                java.lang.String str = (java.lang.String) map.get(java.lang.Long.valueOf(jLongOf));
                if (str != null) {
                    long jLongValue = map2.containsKey(java.lang.Long.valueOf(jLongOf)) ? ((java.lang.Long) map2.get(java.lang.Long.valueOf(jLongOf))).longValue() : 0L;
                    java.lang.Long l = (java.lang.Long) map4.get(str);
                    if (l == null || jLongValue > l.longValue()) {
                        map4.put(str, java.lang.Long.valueOf(jLongValue));
                        map3.put(str, java.lang.Long.valueOf(jLongOf));
                    }
                }
            }
            java.util.ArrayList arrayList = new java.util.ArrayList(list.size());
            java.util.HashSet hashSet = new java.util.HashSet();
            boolean z = false;
            for (java.lang.Object obj3 : list) {
                long jLongOf2 = longOf(obj3);
                java.lang.String str2 = (java.lang.String) map.get(java.lang.Long.valueOf(jLongOf2));
                if (str2 != null) {
                    if (hashSet.contains(str2) || !java.lang.Long.valueOf(jLongOf2).equals(map3.get(str2))) {
                        z = true;
                    } else {
                        hashSet.add(str2);
                    }
                }
                arrayList.add(obj3);
            }
            if (z) {
                methodHookParam.args[0] = arrayList;
                com.tencent.qqlitepro.util.Log.i("临时会话: 列表去重 id数 " + list.size() + "->" + arrayList.size());
            }
        }
    }

    private static void putTempChatKey(java.util.HashMap<java.lang.Long, java.lang.String> map, java.util.HashMap<java.lang.Long, java.lang.Long> map2, java.lang.Object obj, long j) {
        java.lang.String strTempChatRenderKey;
        if (j == 0 || obj == null || (strTempChatRenderKey = tempChatRenderKey(obj, java.lang.Long.valueOf(j))) == null) {
            return;
        }
        map.put(java.lang.Long.valueOf(j), strTempChatRenderKey);
        map2.put(java.lang.Long.valueOf(j), java.lang.Long.valueOf(tempChatRenderMsgTime(obj)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void recordTempChatOrder(java.lang.Object obj) {
        if (sTempChatKnown.isEmpty()) {
            return;
        }
        java.lang.Object objFieldValue = fieldValue(obj, "f");
        if (objFieldValue instanceof java.util.Map) {
            int i = 0;
            for (java.util.Map.Entry<?, ?> entry : ((java.util.Map<?, ?>) objFieldValue).entrySet()) {
                java.lang.String strRawTempChatUidOfItem = rawTempChatUidOfItem(entry.getValue(), longOf(entry.getKey()));
                if (strRawTempChatUidOfItem != null) {
                    rememberTempChatOrder(strRawTempChatUidOfItem, i);
                }
                i++;
            }
        }
    }

    /**
     * 从会话列表条目里解析出"临时会话"的 peerUid；不是临时会话则返回 null。
     * 供长按菜单挂载使用，确保只有临时会话卡片被接管。
     */
    public static java.lang.String tempChatPeerUidOfItem(java.lang.Object item) {
        if (item == null) {
            return null;
        }
        try {
            java.lang.Object info = fieldValue(item, "a");
            if (info == null) {
                info = item;
            }
            java.lang.String peerUid = strOf(fieldValue(info, "peerUid"));
            if (peerUid.isEmpty()) {
                peerUid = strOf(fieldValue(item, "peerUid"));
            }
            if (peerUid.isEmpty()) {
                return null;
            }
            // 必须是已登记的临时会话，避免误伤好友/群聊
            return isKnownTempChatPeer(peerUid) ? normPeerUid(peerUid) : null;
        } catch (java.lang.Throwable unused) {
            return null;
        }
    }

    private static java.lang.String rawTempChatUidOfItem(java.lang.Object obj, long j) {
        java.lang.String strTempChatRenderKey = tempChatRenderKey(obj, java.lang.Long.valueOf(j));
        if (strTempChatRenderKey == null) {
            return null;
        }
        for (java.lang.String str : sTempChatKnown.keySet()) {
            if (normPeerUid(str).equals(strTempChatRenderKey)) {
                return str;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /** value 是否为临时会话条目（用于去重前的安全判定） */
    private static boolean isTempChatRenderEntry(java.lang.Object value) {
        try {
            java.lang.Object info = fieldValue(value, "a");
            if (info == null) {
                return false;
            }
            if (!isRecentContactInfo(info)) {
                return false;
            }
            java.lang.String peerUid = strOf(fieldValue(info, "peerUid"));
            if (peerUid.isEmpty()) {
                return false;
            }
            return isKnownTempChatPeer(peerUid);
        } catch (java.lang.Throwable unused) {
            return false;
        }
    }

    public static void dedupTempChatRenderMap(java.lang.Object obj) {
        java.lang.Object objFieldValue = fieldValue(obj, "f");
        if (objFieldValue instanceof java.util.Map) {
            java.util.Map map = (java.util.Map) objFieldValue;
            if (map.size() < 2) {
                return;
            }
            java.util.HashMap map2 = new java.util.HashMap();
            java.util.ArrayList arrayList = new java.util.ArrayList();
            for (java.util.Map.Entry<?, ?> entry : ((java.util.Map<?, ?>) map).entrySet()) {
                java.lang.Object key = entry.getKey();
                java.lang.Object value = entry.getValue();
                /*
                 * 安全护栏：只对"确实是临时会话"的条目做去重。
                 * 否则一旦 key 计算退化，会把好友/群聊会话也一起删掉，
                 * 表现为"会话列表少了会话 / 卡片顺序错乱"。
                 */
                if (!isTempChatRenderEntry(value)) {
                    continue;
                }
                java.lang.String strTempChatRenderKey = tempChatRenderKey(value, key);
                if (strTempChatRenderKey != null) {
                    java.lang.Object obj2 = map2.get(strTempChatRenderKey);
                    if (obj2 == null) {
                        map2.put(strTempChatRenderKey, key);
                    } else if (tempChatRenderMsgTime(value) > tempChatRenderMsgTime(map.get(obj2))) {
                        arrayList.add(obj2);
                        map2.put(strTempChatRenderKey, key);
                    } else {
                        arrayList.add(key);
                    }
                }
            }
            if (arrayList.isEmpty()) {
                return;
            }
            java.util.Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                map.remove(it.next());
            }
            com.tencent.qqlitepro.util.Log.i("临时会话: 列表去重 移除=" + arrayList.size() + " 剩余=" + map.size());
        }
    }

    private static java.lang.String tempChatRenderKey(java.lang.Object obj, java.lang.Object obj2) {
        /*
         * 修复"多个临时会话卡片被合并 / 顺序连在一起"：
         * 旧逻辑最后无条件 return tempChatKeyByUin(longOf(obj2))，
         * 而 obj2（会话 key）在很多情况下是 0 或相同的占位值，
         * 于是多个不同会话拿到同一个 key 被去重逻辑合并成一张。
         *
         * 新逻辑严格分级：
         *   1) 有 peerUid 且已登记 -> 用 peerUid（最可靠，唯一）
         *   2) 有 peerUid 未登记 -> 仍用 peerUid（不同会话 peerUid 必然不同，不会误合并）
         *   3) 只能靠 uin 反查 -> 仅唯一命中才用
         *   4) 全都不可判定 -> 返回 null，表示"不参与去重"（宁可不合并，也不能错合并）
         */
        java.lang.Object info = fieldValue(obj, "a");
        if (isRecentContactInfo(info)) {
            java.lang.String peerUid = strOf(fieldValue(info, "peerUid"));
            if (!peerUid.isEmpty()) {
                // peerUid 是会话的唯一标识，直接用它，不要求已登记
                return normPeerUid(peerUid);
            }
            long uin = longOf(fieldValue(info, "peerUin"));
            if (uin == 0) {
                uin = longOf(fieldValue(info, "contactId"));
            }
            java.lang.String byUin = tempChatKeyByUin(uin);
            if (byUin != null) {
                return byUin;
            }
        }
        long keyUin = longOf(obj2);
        if (keyUin != 0) {
            return tempChatKeyByUin(keyUin);
        }
        // 无法唯一确定会话身份：不参与去重，避免把不同会话合并
        return null;
    }

    private static java.lang.String tempChatKeyByUin(long j) {
        if (j == 0) {
            return null;
        }
        /*
         * 修复"昵称错乱 / 多个临时会话被并成一个"：
         * 旧实现按 uin 线性遍历，命中第一个就返回。
         * 但多个临时会话可能共享同一个 uin（宿主对未加载资料的会话会给 0 或复用占位值），
         * 于是所有这类会话都被映射到同一个 peerUid，导致：
         *   - 卡片昵称被统一成某一个好友的名字
         *   - 去重逻辑把它们合并成一张卡片
         * 这里改为：只有"唯一命中"时才认这个 uin；命中多个则视为不可判定，返回 null，
         * 由调用方改用 peerUid 作为主键。
         */
        java.lang.String hit = null;
        int count = 0;
        for (java.util.Map.Entry<java.lang.String, java.lang.Object[]> entry : sTempChatKnown.entrySet()) {
            java.lang.Object[] value = entry.getValue();
            if (value != null && longOf(value[0]) == j) {
                count++;
                if (count > 1) {
                    // 存在歧义，宁可不用也不能错配
                    return null;
                }
                hit = normPeerUid(entry.getKey());
            }
        }
        return hit;
    }

    private static long tempChatRenderMsgTime(java.lang.Object obj) {
        java.lang.Object objFieldValue = fieldValue(obj, "a");
        if (isRecentContactInfo(objFieldValue)) {
            obj = objFieldValue;
        }
        return longOf(fieldValue(obj, "msgTime"));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void patchTempChatRenderArgs(java.lang.String str, de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
        java.util.List list;
        java.util.List list2;
        java.lang.Boolean boolIsTempChatTypeCached;
        if ("a".equals(str)) {
            java.lang.Object obj = methodHookParam.args[2];
            if (isRecentContactInfo(obj) && (boolIsTempChatTypeCached = isTempChatTypeCached(intOf(fieldValue(obj, "chatType")))) != null && boolIsTempChatTypeCached.booleanValue()) {
                rememberTempChat(strOf(fieldValue(obj, "peerUid")), longOf(fieldValue(obj, "peerUin")), strOf(fieldValue(obj, "peerName")), secondsToMs(longOf(fieldValue(obj, "msgTime"))), 1);
                java.lang.Object objBuildTempChatRenderCopy = buildTempChatRenderCopy(obj);
                if (objBuildTempChatRenderCopy != null) {
                    methodHookParam.args[2] = objBuildTempChatRenderCopy;
                    return;
                }
                return;
            }
            return;
        }
        java.lang.Object obj2 = "e".equals(str) ? methodHookParam.args[0] : null;
        if (obj2 != null) {
            java.lang.Object objFieldValue = fieldValue(obj2, "changedList");
            list2 = objFieldValue instanceof java.util.List ? (java.util.List) objFieldValue : null;
            java.lang.Object objFieldValue2 = fieldValue(obj2, "sortedContactList");
            list = objFieldValue2 instanceof java.util.List ? (java.util.List) objFieldValue2 : null;
        } else {
            list = methodHookParam.args[0] instanceof java.util.List ? (java.util.List) methodHookParam.args[0] : null;
            list2 = methodHookParam.args[1] instanceof java.util.List ? (java.util.List) methodHookParam.args[1] : null;
        }
        if (list2 == null) {
            return;
        }
        if (list2.isEmpty() && (list == null || list.isEmpty())) {
            return;
        }
        java.util.ArrayList arrayList = new java.util.ArrayList(list2);
        java.util.ArrayList arrayList2 = list != null ? new java.util.ArrayList(list) : null;
        rememberTempChatTemplate(arrayList);
        int iPatchTempChatEntriesInPlace = patchTempChatEntriesInPlace(arrayList);
        /*
         * 架构调整：临时会话不再"分散插入"到会话列表，改为聚合成一张卡片
         * （与 QQ 邮箱卡片一致：列表只出现一张，点进去看全部临时会话）。
         * 这里保留登记/样式处理，但关闭列表注入。
         */
        int iEnsureTempChatsFromKnown = 0;
        if (iPatchTempChatEntriesInPlace == 0 && iEnsureTempChatsFromKnown == 0) {
            return;
        }
        if (obj2 != null) {
            de.robv.android.xposed.XposedHelpers.setObjectField(obj2, "changedList", arrayList);
            if (arrayList2 != null) {
                de.robv.android.xposed.XposedHelpers.setObjectField(obj2, "sortedContactList", arrayList2);
            }
        } else {
            methodHookParam.args[1] = arrayList;
            if (arrayList2 != null) {
                methodHookParam.args[0] = arrayList2;
            }
        }
        com.tencent.qqlitepro.util.Log.i("临时会话: 渲染关口 " + str + "() 转换=" + iPatchTempChatEntriesInPlace + " 补登记=" + iEnsureTempChatsFromKnown + " size=" + arrayList.size());
    }

    private static int patchTempChatEntriesInPlace(java.util.List<?> list) {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        int i = 0;
        for (int i2 = 0; i2 < list.size(); i2++) {
            try {
                java.lang.Object obj = list.get(i2);
                if (isRecentContactInfo(obj)) {
                    int iIntOf = intOf(fieldValue(obj, "chatType"));
                    if (java.lang.Boolean.TRUE.equals(isTempChatTypeCached(iIntOf))) {
                        rememberTempChat(strOf(fieldValue(obj, "peerUid")), longOf(fieldValue(obj, "peerUin")), strOf(fieldValue(obj, "peerName")), secondsToMs(longOf(fieldValue(obj, "msgTime"))), iIntOf);
                        setTempChatPreview(strOf(fieldValue(obj, "peerUid")), previewTextOfAbstract(fieldValue(obj, "abstractContent")));
                        java.lang.Object objBuildTempChatRenderCopy = buildTempChatRenderCopy(obj);
                        if (objBuildTempChatRenderCopy != null && replaceAt(list, i2, objBuildTempChatRenderCopy)) {
                            i++;
                        }
                    }
                }
            } catch (java.lang.Throwable unused) {
            }
        }
        return i;
    }

    private static java.lang.Object buildTempChatRenderCopy(java.lang.Object obj) {
        try {
            java.lang.Object objNewInstance = cls("com.tencent.qqnt.kernel.nativeinterface.RecentContactInfo").newInstance();
            copyFields(obj, objNewInstance);
            de.robv.android.xposed.XposedHelpers.setObjectField(objNewInstance, "peerName", tempChatDisplayName(strOf(fieldValue(obj, "peerUid")), strOf(fieldValue(obj, "peerName"))));
            /*
             * 修复"临时会话卡片位置错乱、顺序连在一起"：
             * 旧实现无条件把 chatType/sessionType 改成 1。
             * 但不同临时会话可能来自不同会话类型，强行统一会让宿主把它们
             * 归到同一分组并紧邻排列（表现为多个卡片挤在一起）。
             * 这里只补齐非法值，不覆盖宿主原本给出的类型。
             */
            int origChatType = intOf(fieldValue(obj, "chatType"));
            if (origChatType <= 0) {
                de.robv.android.xposed.XposedHelpers.setIntField(objNewInstance, "chatType", 1);
            }
            int origSessionType = intOf(fieldValue(obj, "sessionType"));
            if (origSessionType <= 0) {
                de.robv.android.xposed.XposedHelpers.setIntField(objNewInstance, "sessionType", 1);
            }
            java.lang.Object objFieldValue = fieldValue(objNewInstance, "abstractContent");
            if (!(objFieldValue instanceof java.util.List) || ((java.util.List) objFieldValue).isEmpty()) {
                java.lang.String strPreviewOf = previewOf(sTempChatKnown.get(strOf(fieldValue(obj, "peerUid"))));
                if (!strPreviewOf.isEmpty()) {
                    de.robv.android.xposed.XposedHelpers.setObjectField(objNewInstance, "abstractContent", buildAbstractContent(strPreviewOf));
                }
            }
            return objNewInstance;
        } catch (java.lang.Throwable thRethrow6) {
            com.tencent.qqlitepro.util.Log.w("临时会话: 复制渲染条目失败 " + chain(thRethrow6));
            return null;
        }
    }

    private static java.util.ArrayList buildAbstractContent(java.lang.String str) {
        java.util.ArrayList arrayList = new java.util.ArrayList();
        if (str != null && !str.isEmpty()) {
            try {
                java.lang.Object objNewInstance = cls("com.tencent.qqnt.kernel.nativeinterface.MsgAbstractElement").newInstance();
                de.robv.android.xposed.XposedHelpers.setIntField(objNewInstance, "elementType", 1);
                de.robv.android.xposed.XposedHelpers.setObjectField(objNewInstance, "content", str);
                de.robv.android.xposed.XposedHelpers.setObjectField(objNewInstance, "elementSubType", 0);
                de.robv.android.xposed.XposedHelpers.setObjectField(objNewInstance, "index", 0);
                arrayList.add(objNewInstance);
            } catch (java.lang.Throwable thRethrow6) {
                com.tencent.qqlitepro.util.Log.w("临时会话: 构造消息预览失败 " + chain(thRethrow6));
            }
        }
        return arrayList;
    }

    private static java.lang.String tempChatDisplayName(java.lang.String str, java.lang.String str2) {
        java.lang.String strTrim = str2 == null ? "" : str2.trim();
        if (strTrim.isEmpty() || strTrim.equals(str) || strTrim.startsWith("u_") || strTrim.length() > 40) {
            /*
             * 修复"多个临时会话昵称被统一成同一个好友名"：
             * 旧实现直接 sTempChatKnown.get(str)，要求 key 与原始 peerUid 完全一致；
             * 而登记表可能存的是不同大小写/下划线形式，查不到就回退成空，
             * 之后再由上层拿到错误名字。
             * 改用 tempChatKnownEntry（内部做归一化 + 忽略大小写匹配），
             * 并且只有确实命中本会话时才采用该名字。
             */
            java.lang.Object[] objArr = tempChatKnownEntry(str);
            strTrim = objArr != null ? strOf(objArr[1]) : "";
            if (strTrim.isEmpty()) {
                java.lang.Object[] objArr2 = sTempChatPending.get(str);
                strTrim = objArr2 != null ? strOf(objArr2[2]) : "";
            }
        }
        if (!strTrim.isEmpty() && !strTrim.equals(str)) {
            str = strTrim;
        }
        if (str.contains(TEMP_CHAT_TAG)) {
            return str;
        }
        return str + TEMP_CHAT_TAG;
    }

    private static void rememberTempChatTemplate(java.util.List<?> list) {
        java.lang.Object obj = null;
        for (java.lang.Object obj2 : list) {
            if (isRecentContactInfo(obj2)) {
                int iIntOf = intOf(fieldValue(obj2, "chatType"));
                if (java.lang.Boolean.TRUE.equals(isTempChatTypeCached(iIntOf))) {
                    continue;
                } else if (iIntOf == 1) {
                    sTempChatTemplate = obj2;
                    return;
                } else if (obj == null) {
                    obj = obj2;
                }
            }
        }
        if (obj != null) {
            sTempChatTemplate = obj;
        }
    }

    private static int ensureTempChatsFromKnown(java.util.List<?> list, java.lang.Object obj, java.lang.Object obj2) {
        if (sTempChatKnown.isEmpty()) {
            return 0;
        }
        java.util.HashSet<Long> usedContactIds = new java.util.HashSet<>();
        java.util.HashSet<String> presentUids = new java.util.HashSet<>();
        boolean hasIdList = obj instanceof java.util.List;
        if (hasIdList) {
            for (java.lang.Object one : (java.util.List) obj) {
                long id = longOf(one);
                if (id != 0) {
                    usedContactIds.add(java.lang.Long.valueOf(id));
                }
            }
        }
        // 模板条目 & 已存在的会话
        java.lang.Object template = null;
        for (java.lang.Object it : list) {
            if (isRecentContactInfo(it)) {
                if (template == null) {
                    template = it;
                }
                java.lang.String uid = strOf(fieldValue(it, "peerUid"));
                if (!uid.isEmpty()) {
                    presentUids.add(normPeerUid(uid));
                }
            }
        }
        if (template == null) {
            template = sTempChatTemplate;
        }
        if (template == null) {
            return 0;
        }

        /*
         * 关键修复："多个临时会话卡片连在一起 / 位置集中"。
         *
         * 旧实现边遍历边 insertAt：每次插入都会改变 list 索引，
         * 后续会话计算出的插入点被前面的插入推挤，最终全部堆在相邻位置。
         *
         * 新实现：
         *   1) 先把所有"缺失的"临时会话收集起来（含各自的 msgTime）
         *   2) 按 msgTime 从新到旧排序
         *   3) 逐个插入 —— 因为始终是从新到旧插入，后插入的一定落在更靠后的位置，
         *      已排好的顺序不会被破坏，每个会话都停在正确的时间位。
         */
        java.util.ArrayList<java.lang.Object> planned = new java.util.ArrayList<>();
        java.util.ArrayList<Long> plannedTimes = new java.util.ArrayList<>();
        java.util.ArrayList<Long> plannedContactIds = new java.util.ArrayList<>();

        java.util.ArrayList<java.util.Map.Entry> sorted = new java.util.ArrayList<>(sTempChatKnown.entrySet());
        sorted.sort(new com.tencent.qqlitepro.http.QqKernel$$ExternalSyntheticLambda18());

        for (java.util.Map.Entry entry : sorted) {
            java.lang.String uid = (java.lang.String) entry.getKey();
            if (presentUids.contains(normPeerUid(uid))) {
                continue;
            }
            java.lang.Object[] value = (java.lang.Object[]) entry.getValue();
            long uin = value != null ? longOf(value[0]) : 0L;
            if (uin != 0 && usedContactIds.contains(java.lang.Long.valueOf(uin))) {
                continue;
            }
            java.lang.Object info = buildTempChatInfo(template, uid, uin, 0,
                    value != null ? strOf(value[1]) : "",
                    value != null ? longOf(value[2]) : 0L);
            if (info == null) {
                continue;
            }
            presentUids.add(normPeerUid(uid));
            planned.add(info);
            plannedTimes.add(java.lang.Long.valueOf(longOf(fieldValue(info, "msgTime"))));
            long cid = longOf(fieldValue(info, "contactId"));
            plannedContactIds.add(java.lang.Long.valueOf(cid));
            if (cid != 0) {
                usedContactIds.add(java.lang.Long.valueOf(cid));
            }
        }

        if (planned.isEmpty()) {
            return 0;
        }

        // 按 msgTime 从新到旧（稳定），保证多次插入不乱序
        java.util.ArrayList<Integer> order = new java.util.ArrayList<>();
        for (int k = 0; k < planned.size(); k++) {
            order.add(java.lang.Integer.valueOf(k));
        }
        final java.util.ArrayList<Long> times = plannedTimes;
        order.sort(new java.util.Comparator<java.lang.Integer>() {
            @Override
            public int compare(java.lang.Integer a, java.lang.Integer b) {
                return java.lang.Long.compare(times.get(b).longValue(), times.get(a).longValue());
            }
        });

        int inserted = 0;
        java.util.List idList = hasIdList ? (java.util.List) obj : null;
        for (int k = 0; k < order.size(); k++) {
            int idx = order.get(k).intValue();
            long t = plannedTimes.get(idx).longValue();
            if (insertAt(list, tempChatInsertIndex(list, t), planned.get(idx))) {
                inserted++;
                long cid = plannedContactIds.get(idx).longValue();
                if (idList != null && cid != 0) {
                    int pos = tempChatIdInsertIndex(obj2, idList, list, t, cid);
                    insertAt(idList, pos, java.lang.Long.valueOf(cid));
                }
            }
        }
        return inserted;
    }

    static /* synthetic */ int lambda$ensureTempChatsFromKnown$19(java.util.Map.Entry entry, java.util.Map.Entry entry2) {
        /*
         * 修复"临时会话卡片顺序错乱 / 每次刷新顺序都在变"：
         * 旧比较器在 order 相同时返回 0，排序不稳定（TimSort 对相等元素不保序，
         * 且 Map 遍历顺序本身不固定），表现为卡片顺序来回跳。
         * 现在：
         *   1) 置顶的排前面
         *   2) 再按 order（越新越靠前）
         *   3) order 相同则按 peerUid 字典序，保证确定性
         */
        // 置顶表用严格键保存，这里也必须用原样键查询
        java.lang.String uidA = strictKey(strOf(entry.getKey()));
        java.lang.String uidB = strictKey(strOf(entry2.getKey()));
        boolean topA = isTempChatTop(uidA);
        boolean topB = isTempChatTop(uidB);
        if (topA != topB) {
            return topA ? -1 : 1;
        }
        int oA = orderOf((java.lang.Object[]) entry.getValue());
        int oB = orderOf((java.lang.Object[]) entry2.getValue());
        if (oA < 0) {
            oA = Integer.MAX_VALUE;
        }
        if (oB < 0) {
            oB = Integer.MAX_VALUE;
        }
        if (oA != oB) {
            return java.lang.Integer.compare(oA, oB);
        }
        return uidA.compareTo(uidB);
    }

    private static int tempChatIdInsertIndex(java.lang.Object obj, java.util.List<?> list, java.util.List<?> list2, long j, long j2) {
        java.lang.Long l;
        java.util.HashMap map = new java.util.HashMap();
        for (java.lang.Object obj2 : list2) {
            if (isRecentContactInfo(obj2)) {
                map.put(java.lang.Long.valueOf(longOf(fieldValue(obj2, "contactId"))), java.lang.Long.valueOf(longOf(fieldValue(obj2, "msgTime"))));
            }
        }
        java.lang.Object objFieldValue = fieldValue(obj, "f");
        if (objFieldValue instanceof java.util.Map) {
            java.util.Iterator it = ((java.util.Map) objFieldValue).values().iterator();
            while (it.hasNext()) {
                java.lang.Object objFieldValue2 = fieldValue(it.next(), "a");
                if (isRecentContactInfo(objFieldValue2)) {
                    long jLongOf = longOf(fieldValue(objFieldValue2, "contactId"));
                    if (!map.containsKey(java.lang.Long.valueOf(jLongOf))) {
                        map.put(java.lang.Long.valueOf(jLongOf), java.lang.Long.valueOf(longOf(fieldValue(objFieldValue2, "msgTime"))));
                    }
                }
            }
        }
        for (int i = 0; i < list.size(); i++) {
            long jLongOf2 = longOf(list.get(i));
            if (jLongOf2 != j2 && (l = (java.lang.Long) map.get(java.lang.Long.valueOf(jLongOf2))) != null && l.longValue() < j) {
                return i;
            }
        }
        return list.size();
    }

    private static int tempChatInsertIndex(java.util.List<?> list, long j) {
        for (int i = 0; i < list.size(); i++) {
            java.lang.Object obj = list.get(i);
            if (isRecentContactInfo(obj) && longOf(fieldValue(obj, "msgTime")) < j) {
                return i;
            }
        }
        return list.size();
    }

    private static boolean insertAt(java.util.List list, int i, java.lang.Object obj) {
        try {
            list.add(i, obj);
            return true;
        } catch (java.lang.Throwable unused) {
            return false;
        }
    }

    private static boolean replaceAt(java.util.List list, int i, java.lang.Object obj) {
        try {
            list.set(i, obj);
            return true;
        } catch (java.lang.Throwable unused) {
            return false;
        }
    }

    private static int hookTempChatRecv(java.lang.String str, java.lang.String str2) {
        try {
            java.lang.Class<?> cls = cls(str);
            if (cls == null) {
                return 0;
            }
            int i = 0;
            for (java.lang.reflect.Method method : cls.getDeclaredMethods()) {
                if (str2.equals(method.getName())) {
                    java.lang.Class<?>[] parameterTypes = method.getParameterTypes();
                    if (parameterTypes.length == 1 && java.util.Collection.class.isAssignableFrom(parameterTypes[0])) {
                        de.robv.android.xposed.XposedBridge.hookMethod(method, new com.tencent.qqlitepro.http.QqKernel.AnonymousClass13());
                        i++;
                    }
                }
            }
            return i;
        } catch (java.lang.Throwable thRethrow6) {
            com.tencent.qqlitepro.util.Log.w("临时会话: hook " + str + "." + str2 + " 失败 " + chain(thRethrow6));
            return 0;
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.http.QqKernel$13, reason: invalid class name */
    static class AnonymousClass13 extends de.robv.android.xposed.XC_MethodHook {
        AnonymousClass13() {
        }

        protected void afterHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            java.lang.Object obj = methodHookParam.thisObject;
            if (obj != null && obj.getClass().getName().contains("PlatformRecentContactsDataSource")) {
                com.tencent.qqlitepro.http.QqKernel.noteTempChatDatasource(obj);
            }
            java.lang.Object obj2 = (methodHookParam.args == null || methodHookParam.args.length <= 0) ? null : methodHookParam.args[0];
            if (obj2 instanceof java.util.Collection) {
                com.tencent.qqlitepro.http.QqKernel.scanTempChatRecords((java.util.Collection) obj2, obj);
            }
        }
    }

    private static int hookTempChatInfo(java.lang.String str) {
        java.lang.reflect.Method methodFindMethod;
        try {
            java.lang.Class<?> cls = cls(str);
            if (cls == null || (methodFindMethod = findMethod(cls, "onTempChatInfoUpdate", cls("com.tencent.qqnt.kernel.nativeinterface.TempChatInfo"))) == null) {
                return 0;
            }
            de.robv.android.xposed.XposedBridge.hookMethod(methodFindMethod, new com.tencent.qqlitepro.http.QqKernel.AnonymousClass14());
            return 1;
        } catch (java.lang.Throwable thRethrow6) {
            com.tencent.qqlitepro.util.Log.w("临时会话: hook " + str + " 失败 " + chain(thRethrow6));
            return 0;
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.http.QqKernel$14, reason: invalid class name */
    static class AnonymousClass14 extends de.robv.android.xposed.XC_MethodHook {
        AnonymousClass14() {
        }

        protected void afterHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            java.lang.Object obj = (methodHookParam.args == null || methodHookParam.args.length <= 0) ? null : methodHookParam.args[0];
            if (obj != null) {
                com.tencent.qqlitepro.http.QqKernel.forceShowTempChat(obj);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void scanTempChatRecords(java.util.Collection<?> collection, java.lang.Object obj) {
        try {
            for (java.lang.Object obj2 : collection) {
                if (obj2 != null) {
                    int iIntOf = intOf(invokeGetterAny(obj2, "getChatType", "chatType"));
                    java.lang.String strStrOf = strOf(invokeGetterAny(obj2, "getPeerUid", "peerUid"));
                    long jLongOf = longOf(invokeGetterAny(obj2, "getMsgId", "msgId"));
                    if (jLongOf != 0) {
                        java.util.Set<java.lang.String> set = sTempChatSeen;
                        if (set.size() > 512) {
                            set.clear();
                        }
                        if (!set.add(iIntOf + "#" + strStrOf + "#" + jLongOf)) {
                        }
                    }
                    java.lang.Boolean boolIsTempChatTypeCached = isTempChatTypeCached(iIntOf);
                    if (boolIsTempChatTypeCached != null && boolIsTempChatTypeCached.booleanValue() && !strStrOf.isEmpty()) {
                        com.tencent.qqlitepro.util.Log.i("临时会话: 命中临时会话 chatType=" + iIntOf + " peerUid=" + strStrOf);
                        registerTempChat(strStrOf, longOf(invokeGetterAny(obj2, "getPeerUin", "peerUin")), iIntOf, strOf(invokeGetterAny(obj2, "getSendMemberName", "getSendNickName", "getPeerName", "sendMemberName", "sendNickName", "peerName")), longOf(invokeGetterAny(obj2, "getMsgTime", "msgTime")), obj);
                        setTempChatPreview(strStrOf, previewTextOfMessage(obj2));
                    }
                }
            }
        } catch (java.lang.Throwable thRethrow6) {
            com.tencent.qqlitepro.util.Log.w("临时会话: 扫描消息失败 " + chain(thRethrow6));
        }
    }

    private static java.lang.String previewTextOfMessage(java.lang.Object obj) {
        java.lang.Object objFieldValue;
        try {
            java.lang.Object objInvokeGetterAny = invokeGetterAny(obj, "getElements", "elements");
            if (!(objInvokeGetterAny instanceof java.util.List)) {
                return "";
            }
            java.util.List list = (java.util.List) objInvokeGetterAny;
            for (java.lang.Object obj2 : list) {
                if (obj2 != null && (objFieldValue = fieldValue(obj2, "textElement")) != null) {
                    java.lang.String strStrOf = strOf(fieldValue(objFieldValue, "content"));
                    if (!strStrOf.isEmpty()) {
                        return strStrOf;
                    }
                }
            }
            for (java.lang.Object obj3 : list) {
                if (obj3 != null) {
                    if (fieldValue(obj3, "picElement") != null) {
                        return "[图片]";
                    }
                    if (fieldValue(obj3, "pttElement") != null) {
                        return "[语音]";
                    }
                    if (fieldValue(obj3, "videoElement") != null) {
                        return "[视频]";
                    }
                    if (fieldValue(obj3, "fileElement") != null) {
                        return "[文件]";
                    }
                    if (fieldValue(obj3, "faceElement") == null && fieldValue(obj3, "marketFaceElement") == null) {
                        if (fieldValue(obj3, "arkElement") != null || fieldValue(obj3, "structMsgElement") != null) {
                            return "[卡片消息]";
                        }
                    }
                    return "[表情]";
                }
            }
        } catch (java.lang.Throwable thRethrow6) {
            com.tencent.qqlitepro.util.Log.w("临时会话: 解析消息预览失败 " + chain(thRethrow6));
        }
        return "";
    }

    public static java.lang.String previewTextOfAbstract(java.lang.Object obj) {
        if (!(obj instanceof java.util.List)) {
            return "";
        }
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        for (java.lang.Object obj2 : (java.util.List) obj) {
            if (obj2 != null) {
                int iIntOf = intOf(fieldValue(obj2, "elementType"));
                java.lang.String strStrOf = strOf(fieldValue(obj2, "content"));
                if (iIntOf != 8) {
                    if (iIntOf == 6) {
                        sb.append("[表情]");
                    } else if (iIntOf == 2) {
                        sb.append("[图片]");
                    } else if (iIntOf == 4 || iIntOf == 21) {
                        sb.append("[语音]");
                    } else if (iIntOf == 5) {
                        sb.append("[视频]");
                    } else if (iIntOf == 3) {
                        sb.append("[文件]");
                    } else if (!strStrOf.isEmpty()) {
                        sb.append(strStrOf);
                    }
                    if (sb.length() > 0) {
                        break;
                    }
                } else {
                    continue;
                }
            }
        }
        java.lang.String strTrim = sb.toString().trim();
        return strTrim.length() > 80 ? strTrim.substring(0, 80) : strTrim;
    }

    private static java.lang.Object invokeGetterAny(java.lang.Object obj, java.lang.String... strArr) {
        if (obj == null) {
            return null;
        }
        for (java.lang.String str : strArr) {
            try {
                java.lang.reflect.Method method = obj.getClass().getMethod(str, new java.lang.Class[0]);
                method.setAccessible(true);
                return method.invoke(obj, new java.lang.Object[0]);
            } catch (java.lang.Throwable unused) {
            }
        }
        for (java.lang.String str2 : strArr) {
            try {
                return de.robv.android.xposed.XposedHelpers.getObjectField(obj, str2);
            } catch (java.lang.Throwable unused2) {
            }
        }
        return null;
    }

    private static java.lang.Boolean isTempChatType(int i) {
        java.lang.Object objTempChatKernelService = tempChatKernelService();
        if (objTempChatKernelService == null) {
            return null;
        }
        try {
            java.lang.reflect.Method method = objTempChatKernelService.getClass().getMethod("IsTempChatType", java.lang.Integer.TYPE);
            method.setAccessible(true);
            java.lang.Object objInvoke = method.invoke(objTempChatKernelService, java.lang.Integer.valueOf(i));
            if (objInvoke instanceof java.lang.Boolean) {
                return (java.lang.Boolean) objInvoke;
            }
            return null;
        } catch (java.lang.Throwable unused) {
            return null;
        }
    }

    private static java.lang.Boolean isTempChatTypeCached(int i) {
        java.util.concurrent.ConcurrentHashMap<java.lang.Integer, java.lang.Boolean> concurrentHashMap = sTempChatTypeCache;
        java.lang.Boolean bool = concurrentHashMap.get(java.lang.Integer.valueOf(i));
        if (bool != null) {
            return bool;
        }
        if (!isRealTempChatType(i)) {
            java.lang.Boolean bool2 = java.lang.Boolean.FALSE;
            concurrentHashMap.put(java.lang.Integer.valueOf(i), bool2);
            return bool2;
        }
        java.lang.Boolean boolIsTempChatType = isTempChatType(i);
        if (boolIsTempChatType != null) {
            concurrentHashMap.put(java.lang.Integer.valueOf(i), boolIsTempChatType);
        }
        return boolIsTempChatType;
    }

    private static java.lang.Object tempChatKernelService() {
        if (sKernelMsgSvc != null) {
            return sKernelMsgSvc;
        }
        try {
            java.lang.Object objCallStaticNoArg = callStaticNoArg(cls("com.tencent.qqnt.msg.KernelServiceUtil"), "g");
            if (objCallStaticNoArg == null) {
                return null;
            }
            for (java.lang.reflect.Method method : objCallStaticNoArg.getClass().getMethods()) {
                if (method.getParameterTypes().length == 0 && method.getReturnType() != java.lang.Void.TYPE) {
                    try {
                        java.lang.Object objInvoke = method.invoke(objCallStaticNoArg, new java.lang.Object[0]);
                        if (objInvoke != null && hasIntMethod(objInvoke.getClass(), "IsTempChatType")) {
                            sKernelMsgSvc = objInvoke;
                            com.tencent.qqlitepro.util.Log.i("临时会话: 内核消息服务=" + objInvoke.getClass().getName() + " via " + method.getName());
                            return objInvoke;
                        }
                    } catch (java.lang.Throwable unused) {
                        continue;
                    }
                }
            }
            return null;
        } catch (java.lang.Throwable thRethrow6) {
            com.tencent.qqlitepro.util.Log.w("临时会话: 获取内核消息服务失败 " + chain(thRethrow6));
            return null;
        }
    }

    private static boolean hasIntMethod(java.lang.Class<?> cls, java.lang.String str) {
        try {
            cls.getMethod(str, java.lang.Integer.TYPE);
            return true;
        } catch (java.lang.Throwable unused) {
            return false;
        }
    }

    private static java.lang.Object callStaticNoArg(java.lang.Class<?> cls, java.lang.String str) {
        try {
            java.lang.reflect.Method method = cls.getMethod(str, new java.lang.Class[0]);
            method.setAccessible(true);
            return method.invoke(null, new java.lang.Object[0]);
        } catch (java.lang.Throwable unused) {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void forceShowTempChat(java.lang.Object obj) {
        try {
            java.lang.String strStrOf = strOf(fieldValue(obj, "peerUid"));
            int iIntOf = intOf(fieldValue(obj, "chatType"));
            java.lang.String strStrOf2 = strOf(fieldValue(obj, "fromNick"));
            com.tencent.qqlitepro.util.Log.i("临时会话: 收到临时会话更新 peerUid=" + strStrOf + " chatType=" + iIntOf + " group=" + strOf(fieldValue(obj, "groupCode")) + " nick=" + strStrOf2);
            if (!strStrOf.isEmpty() && isRealTempChatType(iIntOf)) {
                /*
                 * 修复"多个临时会话昵称统一变成同一个名字 / 卡片位置错乱"：
                 * 旧代码这里把 uin 硬编码成 0，导致所有临时会话在登记表里 uin 都是 0。
                 * 而 tempChatKeyByUin(0) 无法区分会话 → 渲染时全部命中同一条记录，
                 * 于是昵称被统一成某一条，卡片也被去重逻辑挤在一起。
                 * 这里尽量从消息对象里取出真实 uin/peerUin/contactId。
                 */
                long uin = longOf(fieldValue(obj, "peerUin"));
                if (uin == 0) {
                    uin = longOf(fieldValue(obj, "contactId"));
                }
                if (uin == 0) {
                    uin = longOf(fieldValue(obj, "senderUin"));
                }
                long msgTime = longOf(fieldValue(obj, "msgTime"));
                registerTempChat(strStrOf, uin, iIntOf, strStrOf2, msgTime, null);
            }
        } catch (java.lang.Throwable thRethrow6) {
            com.tencent.qqlitepro.util.Log.w("临时会话: 处理失败 " + chain(thRethrow6));
        }
    }

    private static void loadTempChatKnown() {
        loadTempChatUnread();
        java.lang.String string;
        try {
            android.content.Context contextAppContext = appContext();
            if (contextAppContext != null && (string = contextAppContext.getSharedPreferences(TEMP_CHAT_PREF, 0).getString(TEMP_CHAT_PREF_KEY, "")) != null && !string.isEmpty()) {
                for (java.lang.String str : string.split("\n")) {
                    java.lang.String[] strArrSplit = str.split("\t", -1);
                    if (strArrSplit.length >= 4 && !strArrSplit[0].isEmpty()) {
                        try {
                            int i = strArrSplit.length > 4 ? java.lang.Integer.parseInt(strArrSplit[4]) : 1;
                            if (isRealTempChatType(i)) {
                                sTempChatKnown.put(strArrSplit[0], new java.lang.Object[]{java.lang.Long.valueOf(java.lang.Long.parseLong(strArrSplit[1])), strArrSplit[2], java.lang.Long.valueOf(java.lang.Long.parseLong(strArrSplit[3])), java.lang.Integer.valueOf(i), java.lang.Integer.valueOf(strArrSplit.length > 5 ? java.lang.Integer.parseInt(strArrSplit[5]) : -1), strArrSplit.length > 6 ? strArrSplit[6] : ""});
                            }
                        } catch (java.lang.Throwable unused) {
                        }
                    }
                }
                com.tencent.qqlitepro.util.Log.i("临时会话: 载入持久登记 " + sTempChatKnown.size() + " 条");
            }
        } catch (java.lang.Throwable thRethrow6) {
            com.tencent.qqlitepro.util.Log.w("临时会话: 载入持久登记失败 " + chain(thRethrow6));
        }
    }

    private static void saveTempChatKnown() {
        try {
            android.content.Context contextAppContext = appContext();
            if (contextAppContext == null) {
                return;
            }
            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            for (java.util.Map.Entry<java.lang.String, java.lang.Object[]> entry : sTempChatKnown.entrySet()) {
                java.lang.Object[] value = entry.getValue();
                if (value != null) {
                    sb.append(entry.getKey());
                    sb.append('\t');
                    sb.append(longOf(value[0]));
                    sb.append('\t');
                    sb.append(strOf(value[1]).replace('\t', ' ').replace('\n', ' '));
                    sb.append('\t');
                    sb.append(longOf(value[2]));
                    sb.append('\t');
                    sb.append(intOf(value[3]));
                    sb.append('\t');
                    sb.append(orderOf(value));
                    sb.append('\t');
                    sb.append(previewOf(value).replace('\t', ' ').replace('\n', ' '));
                    sb.append('\n');
                }
            }
            contextAppContext.getSharedPreferences(TEMP_CHAT_PREF, 0).edit().putString(TEMP_CHAT_PREF_KEY, sb.toString()).apply();
        } catch (java.lang.Throwable thRethrow6) {
            com.tencent.qqlitepro.util.Log.w("临时会话: 保存持久登记失败 " + chain(thRethrow6));
        }
    }

    private static void rememberTempChat(java.lang.String str, long j, java.lang.String str2, long j2, int i) {
        long jLongOf;
        int iIntOf;
        if (str == null || str.isEmpty()) {
            return;
        }
        java.util.concurrent.ConcurrentHashMap<java.lang.String, java.lang.Object[]> concurrentHashMap = sTempChatKnown;
        java.lang.Object[] objArr = concurrentHashMap.get(str);
        long jLongOf2 = 0;
        if (j != 0) {
            jLongOf = j;
        } else {
            jLongOf = objArr != null ? longOf(objArr[0]) : 0L;
        }
        java.lang.String strTrim = str2 == null ? "" : str2.trim();
        if (strTrim.isEmpty() || strTrim.equals(str)) {
            strTrim = objArr != null ? strOf(objArr[1]) : "";
        }
        if (j2 > 0) {
            jLongOf2 = j2;
        } else if (objArr != null) {
            jLongOf2 = longOf(objArr[2]);
        }
        if (i != 0) {
            iIntOf = i;
        } else {
            iIntOf = objArr != null ? intOf(objArr[3]) : 1;
        }
        int iOrderOf = objArr != null ? orderOf(objArr) : -1;
        java.lang.String strPreviewOf = objArr != null ? previewOf(objArr) : "";
        if (objArr != null && longOf(objArr[0]) == jLongOf && strOf(objArr[1]).equals(strTrim) && longOf(objArr[2]) == jLongOf2 && intOf(objArr[3]) == iIntOf) {
            return;
        }
        concurrentHashMap.put(str, new java.lang.Object[]{java.lang.Long.valueOf(jLongOf), strTrim, java.lang.Long.valueOf(jLongOf2), java.lang.Integer.valueOf(iIntOf), java.lang.Integer.valueOf(iOrderOf), strPreviewOf});
        saveTempChatKnown();
    }

    /* ===== 临时会话：置顶 / 免打扰（按 peerUid 独立存储，互不影响） ===== */
    private static final java.lang.String TEMP_CHAT_TOP_PREF_KEY = "top_uids";
    private static final java.lang.String TEMP_CHAT_DND_PREF_KEY = "dnd_uids";

    /*
     * 修复"临时会话长按设置无效 / 一个设置牵连所有"：
     *
     * 旧实现两个致命问题：
     *  1) 依赖 appContext()（反射 ActivityThread.currentApplication），
     *     在 Xposed 早期回调里常返回 null → 读取永远为空、写入被静默丢弃，
     *     于是"设置了但没效果"，且每次读都是全空（看起来像全被牵连）。
     *  2) 每次 get 都新建一个 Set，再按顺序覆盖写回，
     *     并发下会互相覆盖（一个操作把另一个的改动冲掉）。
     *
     * 新实现：
     *  - Context 走 Ctx（多路回退 + 主动登记）
     *  - 内存里维护唯一一份 Set，读写都在同一实例上做增删
     *  - SharedPreferences 只作为持久化镜像
     */
    private static final java.util.Map<java.lang.String, java.util.Set<java.lang.String>> sTempChatFlagCache =
            new java.util.concurrent.ConcurrentHashMap<>();

    private static java.util.Set<java.lang.String> tempChatFlags(java.lang.String key) {
        java.util.Set<java.lang.String> cached = sTempChatFlagCache.get(key);
        if (cached != null) {
            return cached;
        }
        java.util.Set<java.lang.String> set = java.util.Collections.synchronizedSet(new java.util.LinkedHashSet<java.lang.String>());
        try {
            android.content.Context c = com.tencent.qqlitepro.util.Ctx.get();
            if (c != null) {
                java.lang.String raw = c.getSharedPreferences(TEMP_CHAT_PREF, 0).getString(key, "");
                if (raw != null && !raw.isEmpty()) {
                    for (java.lang.String one : raw.split("\n")) {
                        java.lang.String t = one.trim();
                        if (!t.isEmpty()) {
                            set.add(t);
                        }
                    }
                }
            }
        } catch (java.lang.Throwable ignored) {
        }
        java.util.Set<java.lang.String> prev = sTempChatFlagCache.putIfAbsent(key, set);
        return prev != null ? prev : set;
    }

    private static void saveTempChatFlags(java.lang.String key, java.util.Set<java.lang.String> set) {
        java.util.Set<java.lang.String> live = tempChatFlags(key);
        // 写进同一份缓存集合，避免调用方传入的临时 Set 覆盖别人的改动
        synchronized (live) {
            live.clear();
            synchronized (set) {
                live.addAll(set);
            }
        }
        try {
            android.content.Context c = com.tencent.qqlitepro.util.Ctx.get();
            if (c == null) {
                return;
            }
            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            synchronized (live) {
                for (java.lang.String one : live) {
                    sb.append(one).append('\n');
                }
            }
            c.getSharedPreferences(TEMP_CHAT_PREF, 0).edit().putString(key, sb.toString()).apply();
        } catch (java.lang.Throwable ignored) {
        }
    }

    @Deprecated
    private static java.util.Set<java.lang.String> loadTempChatFlagsUnused(java.lang.String key) {
        return tempChatFlags(key);
    }

    private static java.util.Set<java.lang.String> loadTempChatFlags(java.lang.String key) {
        return tempChatFlags(key);
    }

    /**
     * 严格键：用于"按会话独立保存设置"的场景。
     *
     * 修复"改一个临时会话的设置，结果牵连全部"：
     * normPeerUid() 会删掉下划线并转小写，而 peerUid 里下划线/大小写都是有效信息，
     *   u_HrAwT3l-_wkGo_s5ythg-w  ->  uhrawt3l-wkgos5ythg-w
     *   u_HRawT3l-_wkGo_s5ythg-w  ->  uhrawt3l-wkgos5ythg-w   ← 撞键
     * 一旦撞键，两个会话就共享同一条置顶/免打扰/未读记录。
     * 这里改用"仅 trim、保持原样"的键，保证一一对应。
     */
    /**
     * 打开某个临时会话的聊天界面。
     *
     * 宿主没有对外暴露的 Intent，这里尝试走它的数据源/跳转入口；
     * 全部失败时静默（不崩溃），由调用方决定是否提示。
     */
    /** 从当前 resumed Activity（反射 ActivityThread）取 */
    private static android.app.Activity findCurrentActivity() {
        try {
            Object act = null;
            Class<?> atCls = Class.forName("android.app.ActivityThread");
            Object at = atCls.getMethod("currentActivityThread").invoke(null);
            try {
                act = atCls.getMethod("currentApplication").invoke(null);
            } catch (Throwable ignored) {}
            if (act instanceof android.app.Activity) {
                return (android.app.Activity) act;
            }
            // currentApplication 是 Application，不是 Activity；遍历其 fragments 由调用方处理
            // 通过.ActivityThread 的 mActivities 拿 resumed activity
            try {
                java.lang.reflect.Field f = atCls.getDeclaredField("mActivities");
                f.setAccessible(true);
                Object arr = f.get(at);
                if (arr instanceof android.util.ArrayMap) {
                    for (Object v : ((android.util.ArrayMap) arr).values()) {
                        try {
                            Object r = de.robv.android.xposed.XposedHelpers.getObjectField(v, "activity");
                            if (r instanceof android.app.Activity) {
                                android.app.Activity a = (android.app.Activity) r;
                                if (!a.isFinishing() && a.hasWindowFocus()) {
                                    return a;
                                }
                                if (act == null) act = a;
                            }
                        } catch (Throwable ignored) {}
                    }
                }
            } catch (Throwable ignored) {}
            return act instanceof android.app.Activity ? (android.app.Activity) act : null;
        } catch (Throwable t) {
            return null;
        }
    }

    /** 递归取 FragmentManager 里第一个 Fragment（含子 FragmentManager） */
    private static Object firstFragmentIn(Object fm, int depth) {
        if (fm == null || depth > 4) {
            return null;
        }
        try {
            Object list = de.robv.android.xposed.XposedHelpers.callMethod(fm, "getFragments", new Object[0]);
            if (list instanceof java.util.List) {
                for (Object f : (java.util.List) list) {
                    if (f != null) {
                        // 优先返回自身
                        return f;
                    }
                }
            }
        } catch (Throwable ignored) {}
        return null;
    }

    /** 通用：按 uin 打开聊天（chatType 1=好友 2=群），走宿主 AIO 路由 */
    public static boolean openChatByUin(android.content.Context context, long uin, java.lang.String nick, int chatType) {
        if (context == null || uin <= 0) {
            return false;
        }
        try {
            android.content.Context app = context.getApplicationContext() != null
                    ? context.getApplicationContext() : context;
            java.lang.Class<?> apiCls = cls("com.tencent.qqnt.aio.adapter.api.IAIOStarterApi");
            if (apiCls != null) {
                java.lang.Object api = invokeQRouteApi(apiCls);
                if (api != null) {
                    for (java.lang.reflect.Method m : api.getClass().getMethods()) {
                        if (!"navigateToAIOActivity".equals(m.getName())) continue;
                        Class<?>[] ps = m.getParameterTypes();
                        if (ps.length == 7 && ps[0] == android.content.Context.class
                                && ps[1] == Integer.TYPE && ps[2] == java.lang.String.class
                                && ps[3] == java.lang.String.class && ps[4] == Integer.TYPE
                                && ps[5] == Long.TYPE && ps[6] == android.os.Bundle.class) {
                            m.setAccessible(true);
                            m.invoke(api, app, Integer.valueOf(chatType),
                                    String.valueOf(uin),
                                    nick == null || nick.isEmpty() ? String.valueOf(uin) : nick,
                                    Integer.valueOf(0), Long.valueOf(uin), null);
                            return true;
                        }
                    }
                }
            }
            // 兜底：直接实现类
            java.lang.Class<?> implCls = cls("com.tencent.qqnt.aio.adapter.api.impl.AIOStarterApiNtImpl");
            if (implCls != null) {
                java.lang.reflect.Constructor<?> c = implCls.getDeclaredConstructor();
                c.setAccessible(true);
                java.lang.Object impl = c.newInstance();
                for (java.lang.reflect.Method m : implCls.getMethods()) {
                    if ("navigateToAIOActivity".equals(m.getName()) && m.getParameterTypes().length == 7) {
                        m.setAccessible(true);
                        m.invoke(impl, app, Integer.valueOf(chatType),
                                String.valueOf(uin),
                                nick == null || nick.isEmpty() ? String.valueOf(uin) : nick,
                                Integer.valueOf(0), Long.valueOf(uin), null);
                        return true;
                    }
                }
            }
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("打开聊天失败 " + chain(th));
        }
        return false;
    }

    /**
     * 打开临时会话的聊天界面（宿主原生 AIO）。
     *
     * 逆向自宿主 com.tencent.qqnt.aio.adapter.api.impl.AIOStarterApiNtImpl：
     *   navigateToAIOActivity(context, chatType, uid, nick, routeFlags, uin, bundle)
     *   内部 -> ActivityURIRequest(context, "/base/main/aio") + QRoute
     * 参数：chatType 1=好友(临时会话属此类) 2=群；uid 用 peerUid；uin 用 peerUin。
     * 优先用 QRoute 反射调用宿主实现，失败再退化到显式构造的 Uri 路由。
     */
    /**
     * 打开临时会话聊天界面。
     *
     * 逆向自宿主（QQ Max M2.5）WatchRecentItemBuilder$listener$1.b()：
     *   Bundle bundle = WatchPicElementExtKt.c0(peerUid, chatType, nick, 0, uin, 8);
     *   NavController nav = NavHostFragment.findNavController(builder 的 d 字段 WatchFragment);
     *   nav.f(2114519145, bundle, null)   [builder.d 即 WatchFragment]
     * 这是宿主会话卡片点击进聊天的真实路径（手表版 ViewPager 页面导航），
     * 而非 QRoute AIO 路由（手表版走的是 NavHostFragment）。
     */
    public static boolean openTempChatSession(android.content.Context context, java.lang.String peerUid) {
        if (context == null || peerUid == null || peerUid.isEmpty()) {
            return false;
        }
        try {
            java.lang.Object[] entry = tempChatKnownEntry(peerUid);
            long uin = entry != null ? longOf(entry[0]) : 0L;
            java.lang.String nick = entry != null ? strOf(entry[1]) : "";
            if (nick != null) {
                nick = nick.replace("（临时会话）", "").replace("(临时会话)", "").trim();
            }
            if (nick == null || nick.isEmpty()) {
                nick = peerUid;
            }
            // 1) 构造与宿主一致的参数 Bundle
            android.os.Bundle bundle = new android.os.Bundle();
            bundle.putString("key_bundle_peer_id", peerUid);
            bundle.putInt("key_bundle_chat_type", 1);
            bundle.putString("key_bundle_chat_nick", nick);
            if (uin > 0) {
                bundle.putString("key_bundle_chat_uin", String.valueOf(uin));
            }
            // 2) 找一个 NavHost 内的 Fragment（宿主主界面 MainActivity 的 fragment 树）。
            //    主列表 builder 是 WatchChatListBuilder（无 d 字段），所以从 Activity 的
            //    FragmentManager 遍历取第一个 Fragment，findNavController 会向上找宿主 NavHost。
            android.app.Activity act = findCurrentActivity();
            Object watchFragment = null;
            if (act != null) {
                try {
                    android.view.View decor = act.getWindow().getDecorView();
                    // 直接从 Activity 取 FragmentManager 遍历
                    Object fm = null;
                    try {
                        fm = de.robv.android.xposed.XposedHelpers.callMethod(act,
                                "getSupportFragmentManager", new Object[0]);
                    } catch (Throwable ignored) {}
                    if (fm == null) {
                        // androidx activity: getSupportFragmentManager 是 FragmentActivity 方法
                        try {
                            fm = de.robv.android.xposed.XposedHelpers.callMethod(act,
                                    "getFragmentManager", new Object[0]);
                        } catch (Throwable ignored) {}
                    }
                    if (fm != null) {
                        watchFragment = firstFragmentIn(fm, 0);
                    }
                } catch (Throwable th) {
                    com.tencent.qqlitepro.util.Log.w("临时会话: 遍历 Fragment 失败 " + chain(th));
                }
            }
            if (watchFragment == null) {
                com.tencent.qqlitepro.util.Log.w("临时会话: 未找到可用 Fragment（NavHost 未就绪）");
                return false;
            }
            // NavHostFragment.findNavController(fragment) — 静态方法名在 release 里可能是 S
            Object navController = null;
            try {
                navController = de.robv.android.xposed.XposedHelpers.callStaticMethod(
                        cls("androidx.navigation.fragment.NavHostFragment"),
                        "findNavController",
                        watchFragment);
            } catch (Throwable t1) {
                // 混淆名兜底：遍历 NavHostFragment 静态方法（返回 NavController 且参数是 Fragment）
                try {
                    Class<?> navHostCls = cls("androidx.navigation.fragment.NavHostFragment");
                    for (java.lang.reflect.Method m : navHostCls.getDeclaredMethods()) {
                        if (!java.lang.reflect.Modifier.isStatic(m.getModifiers())) continue;
                        Class<?>[] ps = m.getParameterTypes();
                        if (ps.length == 1 && ps[0].getName().endsWith("Fragment")
                                && m.getReturnType().getName().equals("androidx.navigation.NavController")) {
                            m.setAccessible(true);
                            navController = m.invoke(null, watchFragment);
                            break;
                        }
                    }
                } catch (Throwable t2) {
                    com.tencent.qqlitepro.util.Log.w("临时会话: findNavController 失败 " + chain(t2));
                }
            }
            if (navController == null) {
                return false;
            }
            // 3) nav.f(2114519145, bundle, null) — 混淆名 f，遍历找单参 int 的 NavController 方法
            boolean navigated = false;
            for (java.lang.reflect.Method m : navController.getClass().getDeclaredMethods()) {
                if (!java.lang.reflect.Modifier.isPublic(m.getModifiers())) continue;
                Class<?>[] ps = m.getParameterTypes();
                if (ps.length == 3 && ps[0] == Integer.TYPE
                        && ps[1] == android.os.Bundle.class
                        && (ps[2] == null || ps[2].getName().endsWith("NavOptions"))) {
                    m.setAccessible(true);
                    m.invoke(navController, Integer.valueOf(2114519145), bundle, null);
                    navigated = true;
                    break;
                }
            }
            if (!navigated) {
                // 再试公开方法（含继承）
                for (java.lang.reflect.Method m : navController.getClass().getMethods()) {
                    Class<?>[] ps = m.getParameterTypes();
                    if (ps.length == 3 && ps[0] == Integer.TYPE
                            && ps[1] == android.os.Bundle.class
                            && ps[2] != null && ps[2].getName().endsWith("NavOptions")) {
                        m.setAccessible(true);
                        m.invoke(navController, Integer.valueOf(2114519145), bundle, null);
                        navigated = true;
                        break;
                    }
                }
            }
            if (navigated) {
                com.tencent.qqlitepro.util.Log.i("临时会话: NavController 导航成功 uid=" + peerUid);
                return true;
            }
            com.tencent.qqlitepro.util.Log.w("临时会话: NavController 导航方法未找到");
            return false;
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("临时会话: 打开会话失败 " + chain(th));
        }
        return false;
    }

    /** 通过 QRoute 取得 API 实现（反射封装） */
    private static java.lang.Object invokeQRouteApi(java.lang.Class<?> apiCls) {
        try {
            java.lang.Class<?> qroute = cls("com.tencent.mobileqq.qroute.QRoute");
            if (qroute == null) {
                return null;
            }
            java.lang.reflect.Method m = qroute.getMethod("api", java.lang.Class.class);
            m.setAccessible(true);
            return m.invoke(null, apiCls);
        } catch (java.lang.Throwable ignored) {
        }
        return null;
    }

    /** 临时会话总数（供汇总卡片展示） */
    /* ==================== 在线状态点赞（逆向自宿主 IKernelOnlineStatusService） ==================== */
    private static volatile Object sSessionRef;

    /** 点赞类型：对应宿主 OnlineStatusConstant */
    public static final int LIKE_TYPE_ZAN = 1;      // 赞
    public static final int LIKE_TYPE_XIN = 2;      // 心
    public static final int LIKE_TYPE_GOUTOU = 3;   // 狗头
    public static final int LIKE_TYPE_JINGLI = 4;   // 惊了
    public static final int LIKE_TYPE_BAOBAO = 5;   // 宝宝
    public static final int LIKE_TYPE_UNKNOWN = 0;

    /**
     * 取内核在线状态服务（IKernelOnlineStatusService）。
     * 逆向链路：IQQNTWrapperSession.getOnlineStatusService()
     */
    private static Object onlineStatusService() throws Throwable {
        try {
            java.lang.reflect.Method mSession = Class.forName("com.tencent.qqnt.kernel.nativeinterface.IQQNTWrapperSession")
                    .getMethod("getOnlineStatusService");
            // session 实例：与 getMsgService 同源，从已缓存的 kernel session 取
            Object session = kernelSessionInstance();
            if (session == null) {
                return null;
            }
            return mSession.invoke(session);
        } catch (Throwable t) {
            com.tencent.qqlitepro.util.Log.w("在线状态: 获取 service 失败 " + chain(t));
            return null;
        }
    }

    /** 取内核 session 实例（与 getMsgService / recallMsg 一致的获取方式） */
    private static Object kernelSessionInstance() {
        try {
            java.lang.reflect.Method m = QqKernel.class.getDeclaredMethod("invokeKernelSession");
            m.setAccessible(true);
            return m.invoke(null);
        } catch (Throwable t) {
            try {
                // 兜底：反射找任何返回 session 的静态入口
                Class<?> sesCls = cls("com.tencent.qqnt.kernel.nativeinterface.IQQNTWrapperSession");
                if (sesCls != null) {
                    for (java.lang.reflect.Method m2 : sesCls.getDeclaredMethods()) {
                        if (java.lang.reflect.Modifier.isStatic(m2.getModifiers())
                                && m2.getParameterTypes().length == 0
                                && m2.getReturnType() == sesCls) {
                            m2.setAccessible(true);
                            return m2.invoke(null);
                        }
                    }
                }
            } catch (Throwable ignored) {
            }
            return null;
        }
    }

    /** 内核 session 获取的公共入口（若模块已有同名方法则直接复用其返回值） */
    private static Object invokeKernelSession() {
        try {
            // 已有代码里 recallMsg 使用的方式是拿 sCl 后调用宿主 QRoute；
            // 这里退回用 getMsgService 所在类里保存的 session（如有）
            java.lang.reflect.Field f = QqKernel.class.getDeclaredField("sSessionRef");
            f.setAccessible(true);
            Object v = f.get(null);
            if (v != null) {
                return v;
            }
        } catch (Throwable ignored) {
        }
        try {
            // 宿主全局 Kernel：IQQNTWrapperSession 的 CppProxy 由内核持有，
            // 通过 BaseService 的静态字段是最常见入口
            Class<?> base = cls("com.tencent.qqnt.kernel.api.impl.BaseService");
            if (base != null) {
                for (java.lang.reflect.Field f : base.getDeclaredFields()) {
                    if (java.lang.reflect.Modifier.isStatic(f.getModifiers())
                            && f.getType().getName().endsWith("IQQNTWrapperSession")) {
                        f.setAccessible(true);
                        Object v = f.get(null);
                        if (v != null) {
                            try {
                                java.lang.reflect.Field sf = QqKernel.class.getDeclaredField("sSessionRef");
                                sf.setAccessible(true);
                                sf.set(null, v);
                            } catch (Throwable ignored) {
                            }
                            return v;
                        }
                    }
                }
            }
        } catch (Throwable ignored) {
        }
        return null;
    }

    /**
     * 给好友点在线状态赞。
     * 逆向自：IKernelOnlineStatusService.setLikeStatus(OSLikeStatusReq, IOSLikeStatusCallback)
     *   OSLikeStatusReq{businessType:int, uin:long, likeItems:ArrayList<OSLikeItem{likeType:int, extInfo}>, isCancel:long}
     */
    public static String sendOnlineStatusLike(long targetUin, int likeType, int count) {
        StringBuilder sb = new StringBuilder();
        try {
            Object svc = onlineStatusService();
            if (svc == null) {
                return "内核在线状态服务不可用（宿主未就绪）";
            }
            Class<?> reqCls = Class.forName("com.tencent.qqnt.kernel.nativeinterface.OSLikeStatusReq");
            Class<?> itemCls = Class.forName("com.tencent.qqnt.kernel.nativeinterface.OSLikeItem");
            Class<?> cbCls = Class.forName("com.tencent.qqnt.kernel.nativeinterface.IOSLikeStatusCallback");

            java.lang.reflect.Constructor<?> reqCtor = reqCls.getConstructor(Integer.TYPE, Long.TYPE, java.util.ArrayList.class, Long.TYPE);
            Object req = reqCtor.newInstance(Integer.valueOf(0), Long.valueOf(targetUin),
                    new java.util.ArrayList(), Long.valueOf(0L));

            java.util.ArrayList items = new java.util.ArrayList();
            for (int i = 0; i < Math.max(1, Math.min(count, 20)); i++) {
                items.add(itemCls.getConstructor(Integer.TYPE,
                        Class.forName("com.tencent.qqnt.kernel.nativeinterface.OSLikeExtInfo"))
                        .newInstance(Integer.valueOf(likeType), null));
            }
            reqCls.getField("likeItems").set(req, items);

            final int[] code = {-999};
            final String[] err = {""};
            final java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
            Object cb = java.lang.reflect.Proxy.newProxyInstance(cbCls.getClassLoader(),
                    new Class<?>[]{cbCls},
                    new java.lang.reflect.InvocationHandler() {
                        @Override
                        public Object invoke(Object proxy, java.lang.reflect.Method m, Object[] args) {
                            if ("onResult".equals(m.getName()) && args != null && args.length >= 2) {
                                code[0] = args[0] instanceof Integer ? (Integer) args[0] : -1;
                                err[0] = args[1] == null ? "" : String.valueOf(args[1]);
                                latch.countDown();
                            }
                            return null;
                        }
                    });

            java.lang.reflect.Method setLike = findMethod(svc.getClass(), "setLikeStatus", reqCls, cbCls);
            if (setLike == null) {
                setLike = findMethod(svc.getClass(), "setLikeStatus", reqCls, cbCls);
            }
            if (setLike == null) {
                return "内核未提供 setLikeStatus";
            }
            setLike.setAccessible(true);
            setLike.invoke(svc, req, cb);
            if (!latch.await(15000, java.util.concurrent.TimeUnit.MILLISECONDS)) {
                return "点赞回调超时";
            }
            if (code[0] == 0) {
                return null;   // 成功
            }
            return "点赞失败 code=" + code[0] + (err[0].isEmpty() ? "" : " " + err[0]);
        } catch (Throwable t) {
            return "点赞异常 " + chain(t);
        }
    }

    /** 查某好友的点赞列表（供界面展示） */
    public static java.util.ArrayList<?> getOnlineStatusLikeList(long targetUin) {
        try {
            Object svc = onlineStatusService();
            if (svc == null) {
                return null;
            }
            Class<?> reqCls = Class.forName("com.tencent.qqnt.kernel.nativeinterface.OSGetLikeListReq");
            Class<?> cbCls = Class.forName("com.tencent.qqnt.kernel.nativeinterface.IOSGetLikeListCallback");
            Object req = reqCls.newInstance();
            try {
                reqCls.getField("uin").set(req, Long.valueOf(targetUin));
            } catch (Throwable ignored) {
            }
            final java.util.ArrayList[] out = new java.util.ArrayList[1];
            final java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
            Object cb = java.lang.reflect.Proxy.newProxyInstance(cbCls.getClassLoader(),
                    new Class<?>[]{cbCls},
                    new java.lang.reflect.InvocationHandler() {
                        @Override
                        public Object invoke(Object proxy, java.lang.reflect.Method m, Object[] args) {
                            if ("onResult".equals(m.getName())) {
                                for (Object a : (args == null ? new Object[0] : args)) {
                                    if (a instanceof java.util.ArrayList) {
                                        out[0] = (java.util.ArrayList) a;
                                    }
                                }
                                latch.countDown();
                            }
                            return null;
                        }
                    });
            java.lang.reflect.Method get = findMethod(svc.getClass(), "getLikeList", reqCls, cbCls);
            if (get == null) {
                return null;
            }
            get.setAccessible(true);
            get.invoke(svc, req, cb);
            latch.await(15000, java.util.concurrent.TimeUnit.MILLISECONDS);
            return out[0];
        } catch (Throwable t) {
            com.tencent.qqlitepro.util.Log.w("在线状态: 点赞列表获取失败 " + chain(t));
            return null;
        }
    }

    /* ==================== 群聊消息统计（近 N 天消息总数 + 发言排行榜） ==================== */

    /**
     * 统计结果：total=消息总数, rank=发言排行 {发言人名, 条数}（已按条数降序）
     */
    public static final class GroupMsgStats {
        public long total;
        public final java.util.ArrayList<java.lang.String[]> rank = new java.util.ArrayList<>();
        public int days;
        public java.lang.String error;
    }

    /**
     * 从宿主内核拉取群消息并按发言人聚合。
     * 调用链（逆向）：IQQNTWrapperSession.getMsgService() -> IKernelMsgService.getMsgs(Contact, long startMsgId, int count, boolean, IKernelGetMsgsCallback)
     */
    public static GroupMsgStats groupMsgStats(long groupUin, int days) {
        GroupMsgStats st = new GroupMsgStats();
        st.days = days;
        try {
            Object svc = kernelMsgService();
            if (svc == null) {
                st.error = "内核消息服务不可用";
                return st;
            }
            Class<?> contactCls = cls("com.tencent.qqnt.kernel.nativeinterface.Contact");
            Object contact = contactCls.newInstance();
            de.robv.android.xposed.XposedHelpers.setIntField(contact, "chatType", 2);
            de.robv.android.xposed.XposedHelpers.setLongField(contact, "peerUin", groupUin);
            de.robv.android.xposed.XposedHelpers.setLongField(contact, "peerId", groupUin);

            Class<?> cbCls = cls("com.tencent.qqnt.kernel.nativeinterface.IKernelGetMsgsCallback");
            final Object[] msgsBox = new Object[1];
            final java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
            Object cb = java.lang.reflect.Proxy.newProxyInstance(cbCls.getClassLoader(),
                    new Class<?>[]{cbCls},
                    new java.lang.reflect.InvocationHandler() {
                        @Override
                        public Object invoke(Object proxy, java.lang.reflect.Method m, Object[] args) {
                            if ("onResult".equals(m.getName()) && args != null && args.length >= 3) {
                                msgsBox[0] = args[2];
                                latch.countDown();
                            }
                            return null;
                        }
                    });

            java.lang.reflect.Method getMsgs = null;
            for (java.lang.reflect.Method m : svc.getClass().getMethods()) {
                if (!"getMsgs".equals(m.getName())) continue;
                Class<?>[] ps = m.getParameterTypes();
                if (ps.length >= 3 && ps[0] == contactCls) { getMsgs = m; break; }
            }
            if (getMsgs == null) {
                st.error = "内核未提供 getMsgs";
                return st;
            }
            getMsgs.setAccessible(true);
            // 参数：contact, startMsgId=0(最新), count, 是否向前, callback —— 按类型填充
            Class<?>[] ps = getMsgs.getParameterTypes();
            Object[] argv = new Object[ps.length];
            boolean cbSet = false;
            for (int i = 0; i < ps.length; ++i) {
                if (ps[i] == contactCls) argv[i] = contact;
                else if (ps[i] == Long.TYPE) argv[i] = Long.valueOf(0L);
                else if (ps[i] == Integer.TYPE) argv[i] = Integer.valueOf(1500);
                else if (ps[i] == Boolean.TYPE) argv[i] = Boolean.FALSE;
                else if (cbCls.isAssignableFrom(ps[i])) { argv[i] = cb; cbSet = true; }
                else argv[i] = null;
            }
            if (!cbSet) {
                st.error = "getMsgs 回调参数未匹配";
                return st;
            }
            getMsgs.invoke(svc, argv);
            if (!latch.await(20000, java.util.concurrent.TimeUnit.MILLISECONDS)) {
                st.error = "拉取消息超时";
                return st;
            }
            Object result = msgsBox[0];
            java.util.List list = null;
            if (result instanceof java.util.ArrayList) {
                list = (java.util.ArrayList) result;
            } else if (result != null) {
                // 可能是包一层的结果对象，找 ArrayList 字段
                for (java.lang.reflect.Field f : result.getClass().getDeclaredFields()) {
                    if (f.getType() == java.util.ArrayList.class) {
                        f.setAccessible(true);
                        Object v = f.get(result);
                        if (v instanceof java.util.ArrayList) { list = (java.util.ArrayList) v; break; }
                    }
                }
            }
            if (list == null || list.isEmpty()) {
                st.error = "未取到消息";
                return st;
            }

            long cutoff = System.currentTimeMillis() - days * 86400000L;
            java.util.HashMap<String, Integer> bySender = new java.util.HashMap<>();
            java.util.HashMap<String, String> nameBySender = new java.util.HashMap<>();
            for (Object msg : list) {
                try {
                    long msgTime = 0;
                    try { msgTime = de.robv.android.xposed.XposedHelpers.getLongField(msg, "msgTime"); } catch (Throwable ignored) {}
                    if (msgTime <= 0) {
                        try { msgTime = de.robv.android.xposed.XposedHelpers.getLongField(msg, "time"); } catch (Throwable ignored) {}
                    }
                    // msgTime 是秒
                    if (msgTime > 0 && msgTime < cutoff / 1000L) continue;
                    String senderKey = null;
                    try { senderKey = (String) de.robv.android.xposed.XposedHelpers.getObjectField(msg, "senderUid"); } catch (Throwable ignored) {}
                    if (senderKey == null || senderKey.isEmpty()) {
                        try { senderKey = String.valueOf(de.robv.android.xposed.XposedHelpers.getLongField(msg, "senderUin")); } catch (Throwable ignored) {}
                    }
                    if (senderKey == null || senderKey.isEmpty()) continue;
                    Integer c = bySender.get(senderKey);
                    bySender.put(senderKey, Integer.valueOf(c == null ? 1 : c + 1));
                    if (!nameBySender.containsKey(senderKey)) {
                        String nm = null;
                        try { nm = (String) de.robv.android.xposed.XposedHelpers.getObjectField(msg, "sendMemberName"); } catch (Throwable ignored) {}
                        if (nm == null || nm.isEmpty()) {
                            try { nm = (String) de.robv.android.xposed.XposedHelpers.getObjectField(msg, "sendNickName"); } catch (Throwable ignored) {}
                        }
                        nameBySender.put(senderKey, nm == null || nm.isEmpty() ? senderKey : nm);
                    }
                    st.total++;
                } catch (Throwable ignored) {
                }
            }
            // 排行排序
            java.util.ArrayList<java.util.Map.Entry<String, Integer>> sorted =
                    new java.util.ArrayList<java.util.Map.Entry<String, Integer>>(bySender.entrySet());
            java.util.Collections.sort(sorted, new java.util.Comparator<java.util.Map.Entry<String, Integer>>() {
                @Override
                public int compare(java.util.Map.Entry<String, Integer> a, java.util.Map.Entry<String, Integer> b) {
                    return Integer.compare(b.getValue().intValue(), a.getValue().intValue());
                }
            });
            for (java.util.Map.Entry<String, Integer> e : sorted) {
                String k = (String) e.getKey();
                st.rank.add(new java.lang.String[]{nameBySender.get(k), String.valueOf(e.getValue())});
            }
            return st;
        } catch (Throwable t) {
            st.error = "统计异常 " + chain(t);
            return st;
        }
    }

    public static int tempChatCount() {
        return sTempChatKnown.size();
    }

    /** 临时会话未读总数（供汇总卡片角标） */
    public static int tempChatTotalUnread() {
        int sum = 0;
        for (java.lang.Integer v : sTempChatUnread.values()) {
            if (v != null && v.intValue() > 0) {
                sum += v.intValue();
            }
        }
        return sum;
    }

    /** 全部临时会话快照：{peerUid, 昵称, 预览, 未读, msgTime} */
    /**
     * 为"全屏临时会话页"构造一个可直接交给宿主渲染的 RecentContactInfo。
     * 会尽量补齐头像/昵称/预览/时间/未读，使宿主卡片能完整显示。
     */
    public static java.lang.Object buildTempChatEntryForUi(java.lang.String peerUid) {
        if (peerUid == null || peerUid.isEmpty()) {
            return null;
        }
        try {
            /*
             * 模板可能尚未捕获（全屏页在列表渲染前打开时）。
             * 这里兜底：直接用内核类 new 一个空条目，
             * 后续 buildTempChatInfo 会逐字段填充，不依赖模板。
             */
            java.lang.Object template = sTempChatTemplate;
            if (template == null) {
                try {
                    template = cls("com.tencent.qqnt.kernel.nativeinterface.RecentContactInfo").newInstance();
                } catch (java.lang.Throwable ignored) {
                }
            }
            java.lang.Object[] v = sTempChatKnown.get(peerUid);
            long uin = v != null ? longOf(v[0]) : 0L;
            java.lang.String nick = v != null ? strOf(v[1]) : "";
            long msgTime = v != null ? longOf(v[2]) : 0L;
            java.lang.Object info = buildTempChatInfo(template, peerUid, uin, 1, nick, msgTime);
            if (info == null) {
                return null;
            }
            // 预览
            java.lang.String preview = previewOf(v);
            if (preview != null && !preview.isEmpty()) {
                try {
                    de.robv.android.xposed.XposedHelpers.setObjectField(info, "abstractContent", buildAbstractContent(preview));
                } catch (java.lang.Throwable ignored) {
                }
            }
            // 未读
            int unread = tempChatUnread(peerUid);
            if (unread > 0) {
                try {
                    de.robv.android.xposed.XposedHelpers.setIntField(info, "unreadCnt", unread);
                    de.robv.android.xposed.XposedHelpers.setLongField(info, "unreadFlag", 1L);
                    java.lang.Object ui = de.robv.android.xposed.XposedHelpers.getObjectField(info, "unreadInfo");
                    if (ui != null) {
                        de.robv.android.xposed.XposedHelpers.setIntField(ui, "count", unread);
                        de.robv.android.xposed.XposedHelpers.setIntField(ui, "type", 2);
                    }
                } catch (java.lang.Throwable ignored) {
                }
            }
            return info;
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("临时会话: 构造界面条目失败 " + chain(th));
            return null;
        }
    }


    public static java.util.List<java.lang.Object[]> tempChatSnapshot() {
        java.util.ArrayList<java.lang.Object[]> out = new java.util.ArrayList<>();
        try {
            java.util.ArrayList<java.util.Map.Entry> list = new java.util.ArrayList<>(sTempChatKnown.entrySet());
            for (java.util.Map.Entry e : list) {
                java.lang.String uid = (java.lang.String) e.getKey();
                java.lang.Object[] v = (java.lang.Object[]) e.getValue();
                if (uid == null || uid.isEmpty() || v == null) {
                    continue;
                }
                out.add(new java.lang.Object[]{uid, strOf(v[1]), previewOf(v),
                        java.lang.Integer.valueOf(tempChatUnread(uid)), java.lang.Long.valueOf(longOf(v[2]))});
            }
            out.sort(new java.util.Comparator<java.lang.Object[]>() {
                @Override
                public int compare(java.lang.Object[] a, java.lang.Object[] b) {
                    boolean topA = isTempChatTop((java.lang.String) a[0]);
                    boolean topB = isTempChatTop((java.lang.String) b[0]);
                    if (topA != topB) {
                        return topA ? -1 : 1;
                    }
                    long ta = ((java.lang.Long) a[4]).longValue();
                    long tb = ((java.lang.Long) b[4]).longValue();
                    if (ta != tb) {
                        return java.lang.Long.compare(tb, ta);
                    }
                    return ((java.lang.String) a[0]).compareTo((java.lang.String) b[0]);
                }
            });
        } catch (java.lang.Throwable ignored) {
        }
        return out;
    }


    private static java.lang.String strictKey(java.lang.String peerUid) {
        return peerUid == null ? "" : peerUid.trim();
    }

    /** 临时会话是否置顶 */
    public static boolean isTempChatTop(java.lang.String peerUid) {
        if (peerUid == null || peerUid.isEmpty()) {
            return false;
        }
        return loadTempChatFlags(TEMP_CHAT_TOP_PREF_KEY).contains(strictKey(peerUid));
    }

    /** 设置/取消临时会话置顶（只影响这一个 peerUid） */
    public static void setTempChatTop(java.lang.String peerUid, boolean top) {
        if (peerUid == null || peerUid.isEmpty()) {
            return;
        }
        java.lang.String uid = strictKey(peerUid);
        java.util.Set<java.lang.String> live = tempChatFlags(TEMP_CHAT_TOP_PREF_KEY);
        synchronized (live) {
            if (top) {
                live.add(uid);
            } else {
                live.remove(uid);
            }
        }
        persistTempChatFlag(TEMP_CHAT_TOP_PREF_KEY);
        com.tencent.qqlitepro.util.Log.i("临时会话: 置顶 " + uid + " = " + top
                + "（当前置顶数 " + live.size() + "）");
        refreshTempChatList();
    }

    /** 把内存中的标志集合写回 SharedPreferences */
    private static void persistTempChatFlag(java.lang.String key) {
        try {
            android.content.Context c = com.tencent.qqlitepro.util.Ctx.get();
            if (c == null) {
                return;
            }
            java.util.Set<java.lang.String> live = tempChatFlags(key);
            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            synchronized (live) {
                for (java.lang.String one : live) {
                    sb.append(one).append('\n');
                }
            }
            c.getSharedPreferences(TEMP_CHAT_PREF, 0).edit().putString(key, sb.toString()).apply();
        } catch (java.lang.Throwable ignored) {
        }
    }

    /** 临时会话是否免打扰 */
    public static boolean isTempChatDnd(java.lang.String peerUid) {
        if (peerUid == null || peerUid.isEmpty()) {
            return false;
        }
        return loadTempChatFlags(TEMP_CHAT_DND_PREF_KEY).contains(strictKey(peerUid));
    }

    /** 设置/取消临时会话免打扰 */
    public static void setTempChatDnd(java.lang.String peerUid, boolean dnd) {
        if (peerUid == null || peerUid.isEmpty()) {
            return;
        }
        java.lang.String uid = strictKey(peerUid);
        java.util.Set<java.lang.String> live = tempChatFlags(TEMP_CHAT_DND_PREF_KEY);
        synchronized (live) {
            if (dnd) {
                live.add(uid);
            } else {
                live.remove(uid);
            }
        }
        persistTempChatFlag(TEMP_CHAT_DND_PREF_KEY);
        com.tencent.qqlitepro.util.Log.i("临时会话: 免打扰 " + uid + " = " + dnd
                + "（当前免打扰数 " + live.size() + "）");
    }

    private static int orderOf(java.lang.Object[] objArr) {
        if (objArr == null || objArr.length <= 4) {
            return -1;
        }
        return intOf(objArr[4]);
    }

    public static java.lang.String previewOf(java.lang.Object[] objArr) {
        return (objArr == null || objArr.length <= 5) ? "" : strOf(objArr[5]);
    }

    private static void setTempChatPreview(java.lang.String str, java.lang.String str2) {
        java.util.concurrent.ConcurrentHashMap<java.lang.String, java.lang.Object[]> concurrentHashMap;
        java.lang.Object[] objArr;
        if (str == null || str.isEmpty() || str2 == null || str2.isEmpty() || (objArr = (concurrentHashMap = sTempChatKnown).get(str)) == null || str2.equals(previewOf(objArr))) {
            return;
        }
        concurrentHashMap.put(str, new java.lang.Object[]{objArr[0], objArr[1], objArr[2], objArr[3], objArr[4], str2});
        saveTempChatKnown();
        com.tencent.qqlitepro.util.Log.i("临时会话: 更新消息预览 peerUid=" + str + " 预览=" + str2);
    }

    private static void rememberTempChatOrder(java.lang.String str, int i) {
        java.util.concurrent.ConcurrentHashMap<java.lang.String, java.lang.Object[]> concurrentHashMap;
        java.lang.Object[] objArr;
        if (str == null || i < 0 || (objArr = (concurrentHashMap = sTempChatKnown).get(str)) == null || orderOf(objArr) == i) {
            return;
        }
        java.lang.Object[] objArr2 = new java.lang.Object[6];
        objArr2[0] = objArr[0];
        objArr2[1] = objArr[1];
        objArr2[2] = objArr[2];
        objArr2[3] = objArr[3];
        objArr2[4] = java.lang.Integer.valueOf(i);
        objArr2[5] = objArr.length > 5 ? objArr[5] : "";
        concurrentHashMap.put(str, objArr2);
        saveTempChatKnown();
    }

    private static void registerTempChat(java.lang.String str, long j, int i, java.lang.String str2, long j2, java.lang.Object obj) {
        long jLongOf;
        if (obj != null && obj.getClass().getName().contains("PlatformRecentContactsDataSource")) {
            noteTempChatDatasource(obj);
        }
        java.util.concurrent.ConcurrentHashMap<java.lang.String, java.lang.Object[]> concurrentHashMap = sTempChatPending;
        java.lang.Object[] objArr = concurrentHashMap.get(str);
        java.lang.String strTrim = str2 == null ? "" : str2.trim();
        if (strTrim.isEmpty() && objArr != null) {
            strTrim = strOf(objArr[2]);
        }
        java.lang.String str3 = strTrim;
        if (j != 0) {
            jLongOf = j;
        } else {
            jLongOf = objArr != null ? longOf(objArr[0]) : 0L;
        }
        long jCurrentTimeMillis = java.lang.System.currentTimeMillis();
        long jSecondsToMs = secondsToMs(j2);
        boolean isNew = objArr == null || longOf(objArr[3]) == 0L;
        concurrentHashMap.put(str, new java.lang.Object[]{java.lang.Long.valueOf(jLongOf), java.lang.Integer.valueOf(i), str3, java.lang.Long.valueOf(jCurrentTimeMillis)});
        rememberTempChat(str, jLongOf, str3, jSecondsToMs, i);
        /*
         * 修复"临时会话右侧没有消息数气泡"：
         * 新消息到达（首次登记）时给该 peerUid 累加未读，
         * 与宿主自己发消息的回显区分开：只有 msgTime 推进才算新消息。
         */
        if (isNew) {
            bumpTempChatUnread(str, 1);
        }
        com.tencent.qqlitepro.util.Log.i("临时会话: 登记待显示 peerUid=" + str + " uin=" + jLongOf + " chatType=" + i + " nick=" + str3 + " msgTime=" + jSecondsToMs + " 待显示数=" + concurrentHashMap.size());
        refreshTempChatList();
        if (str3.isEmpty()) {
            new java.lang.Thread(new com.tencent.qqlitepro.http.QqKernel$$ExternalSyntheticLambda29(i, str), "temp-chat-nick").start();
        }
    }

    static /* synthetic */ void lambda$registerTempChat$20(int i, java.lang.String str) {
        java.lang.String strFetchTempChatNick = fetchTempChatNick(i, str);
        if (strFetchTempChatNick == null || strFetchTempChatNick.isEmpty()) {
            return;
        }
        java.util.concurrent.ConcurrentHashMap<java.lang.String, java.lang.Object[]> concurrentHashMap = sTempChatPending;
        java.lang.Object[] objArr = concurrentHashMap.get(str);
        if (objArr != null) {
            concurrentHashMap.put(str, new java.lang.Object[]{objArr[0], objArr[1], strFetchTempChatNick, objArr[3]});
        }
        rememberTempChat(str, objArr != null ? longOf(objArr[0]) : 0L, strFetchTempChatNick, 0L, i);
        refreshTempChatList();
    }

    public static void refreshTempChatList() {
        new android.os.Handler(android.os.Looper.getMainLooper()).post(new com.tencent.qqlitepro.http.QqKernel$$ExternalSyntheticLambda15());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean refreshTempChatListOnMain() {
        java.lang.reflect.Method methodFindMethod;
        try {
            java.lang.Object obj = sTempChatDatasource;
            if (obj == null || (methodFindMethod = findMethod(obj.getClass(), "g", new java.lang.Class[0])) == null) {
                return false;
            }
            java.lang.Object objInvoke = methodFindMethod.invoke(obj, new java.lang.Object[0]);
            com.tencent.qqlitepro.util.Log.i("临时会话: 触发会话列表刷新 g()=" + objInvoke);
            /*
             * 修复"临时会话卡片根本不出现 / 恢复流程提前放弃"：
             * 旧逻辑要求 g() 严格返回 Boolean.TRUE 才算成功，
             * 很多 ROM 版本 g() 返回 void 或 false（仅仅表示"无需重算"），
             * 于是恢复线程认为失败，一直重试到超时后放弃，卡片永远不注入。
             * 只要方法调用没抛异常，就说明数据源可用，视为成功。
             */
            return true;
        } catch (java.lang.Throwable thRethrow6) {
            com.tencent.qqlitepro.util.Log.w("临时会话: 触发刷新失败 " + chain(thRethrow6));
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void noteTempChatDatasource(java.lang.Object obj) {
        if (obj == null || obj == sTempChatDatasource) {
            return;
        }
        sTempChatDatasource = obj;
        refreshTempChatList();
    }

    private static void scheduleTempChatRestore() {
        sTempChatRestoreDone = false;
        android.os.Handler handler = new android.os.Handler(android.os.Looper.getMainLooper());
        java.lang.Runnable[] runnableArr = new java.lang.Runnable[1];
        runnableArr[0] = new com.tencent.qqlitepro.http.QqKernel.AnonymousClass15(java.lang.System.currentTimeMillis(), handler, runnableArr);
        handler.post(runnableArr[0]);
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.http.QqKernel$15, reason: invalid class name */
    static class AnonymousClass15 implements java.lang.Runnable {
        final /* synthetic */ android.os.Handler val$h;
        final /* synthetic */ long val$start;
        final /* synthetic */ java.lang.Runnable[] val$task;

        AnonymousClass15(long j, android.os.Handler handler, java.lang.Runnable[] runnableArr) {
            this.val$start = j;
            this.val$h = handler;
            this.val$task = runnableArr;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (com.tencent.qqlitepro.http.QqKernel.sTempChatRestoreDone) {
                return;
            }
            if (com.tencent.qqlitepro.http.QqKernel.refreshTempChatListOnMain()) {
                com.tencent.qqlitepro.http.QqKernel.sTempChatRestoreDone = true;
                com.tencent.qqlitepro.util.Log.i("临时会话: 数据源就绪，已立即原位注入（用时 " + (java.lang.System.currentTimeMillis() - this.val$start) + "ms）");
                return;
            }
            long jCurrentTimeMillis = java.lang.System.currentTimeMillis() - this.val$start;
            if (jCurrentTimeMillis >= 60000) {
                com.tencent.qqlitepro.util.Log.i("临时会话: 原位注入轮询结束（数据源始终未就绪）");
            } else {
                this.val$h.postDelayed(this.val$task[0], jCurrentTimeMillis < 10000 ? 300L : 1500L);
            }
        }
    }

    /* ===== 临时会话：未读消息数（按 peerUid 独立，右侧气泡依赖它） ===== */
    private static final java.util.concurrent.ConcurrentHashMap<java.lang.String, java.lang.Integer> sTempChatUnread =
            new java.util.concurrent.ConcurrentHashMap<>();
    private static final java.lang.String TEMP_CHAT_UNREAD_PREF_KEY = "unread_counts";

    /** 记录某临时会话的未读数 */
    public static void setTempChatUnread(java.lang.String peerUid, int count) {
        if (peerUid == null || peerUid.isEmpty()) {
            return;
        }
        java.lang.String key = strictKey(peerUid);
        if (count <= 0) {
            sTempChatUnread.remove(key);
        } else {
            sTempChatUnread.put(key, java.lang.Integer.valueOf(count));
        }
        saveTempChatUnread();
        refreshTempChatList();
    }

    /** 读取某临时会话的未读数 */
    public static int tempChatUnread(java.lang.String peerUid) {
        if (peerUid == null || peerUid.isEmpty()) {
            return 0;
        }
        java.lang.Integer v = sTempChatUnread.get(strictKey(peerUid));
        return v == null ? 0 : v.intValue();
    }

    /** 累加未读（收到新消息时调用） */
    public static void bumpTempChatUnread(java.lang.String peerUid, int delta) {
        if (peerUid == null || peerUid.isEmpty() || delta == 0) {
            return;
        }
        setTempChatUnread(peerUid, java.lang.Math.max(0, tempChatUnread(peerUid) + delta));
    }

    private static void loadTempChatUnread() {
        try {
            android.content.Context c = appContext();
            if (c == null) {
                return;
            }
            java.lang.String raw = c.getSharedPreferences(TEMP_CHAT_PREF, 0).getString(TEMP_CHAT_UNREAD_PREF_KEY, "");
            if (raw == null || raw.isEmpty()) {
                return;
            }
            for (java.lang.String line : raw.split("\n")) {
                int idx = line.indexOf('\t');
                if (idx > 0) {
                    try {
                        int n = java.lang.Integer.parseInt(line.substring(idx + 1).trim());
                        if (n > 0) {
                            sTempChatUnread.put(line.substring(0, idx), java.lang.Integer.valueOf(n));
                        }
                    } catch (java.lang.Throwable ignored) {
                    }
                }
            }
        } catch (java.lang.Throwable ignored) {
        }
    }

    private static void saveTempChatUnread() {
        try {
            android.content.Context c = appContext();
            if (c == null) {
                return;
            }
            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            for (java.util.Map.Entry<java.lang.String, java.lang.Integer> e : sTempChatUnread.entrySet()) {
                sb.append(e.getKey()).append('\t').append(e.getValue().intValue()).append('\n');
            }
            c.getSharedPreferences(TEMP_CHAT_PREF, 0).edit()
                    .putString(TEMP_CHAT_UNREAD_PREF_KEY, sb.toString()).apply();
        } catch (java.lang.Throwable ignored) {
        }
    }

    private static java.lang.Object buildTempChatInfo(java.lang.Object obj, java.lang.String str, long j, int i, java.lang.String str2, long j2) {
        try {
            java.lang.Object objNewInstance = cls("com.tencent.qqnt.kernel.nativeinterface.RecentContactInfo").newInstance();
            copyFields(obj, objNewInstance);
            long jDigitsOf = j != 0 ? j : digitsOf(str);
            long jAbs = jDigitsOf != 0 ? jDigitsOf : ((long) java.lang.Math.abs(str.hashCode() & Integer.MAX_VALUE)) + 900000000000000000L;
            java.lang.String str3 = (str2 == null || str2.isEmpty()) ? str : str2;
            if (!str3.contains(TEMP_CHAT_TAG)) {
                str3 = str3 + TEMP_CHAT_TAG;
            }
            de.robv.android.xposed.XposedHelpers.setObjectField(objNewInstance, "peerUid", str);
            de.robv.android.xposed.XposedHelpers.setLongField(objNewInstance, "peerUin", jDigitsOf);
            de.robv.android.xposed.XposedHelpers.setLongField(objNewInstance, "contactId", jAbs);
            de.robv.android.xposed.XposedHelpers.setObjectField(objNewInstance, "peerName", str3);
            /*
             * 修复"多个临时会话卡片连在一起、位置错乱"的关键：
             * 旧实现无条件把 chatType/sessionType 设为 1（好友类型）。
             * 宿主按 chatType 分组渲染，所有临时会话被强行归进同一组，
             * 就会在列表里紧挨着排成一串。
             * 这里保留模板/原始条目给出的真实类型；只有当它非法时才兜底为 1。
             */
            int keepChatType = intOf(fieldValue(obj, "chatType"));
            if (keepChatType <= 0) {
                keepChatType = i > 0 ? i : 1;   // 调用方传进来的原始 chatType 优先
            }
            if (keepChatType <= 0) {
                keepChatType = 1;
            }
            de.robv.android.xposed.XposedHelpers.setIntField(objNewInstance, "chatType", keepChatType);
            int keepSessionType = intOf(fieldValue(obj, "sessionType"));
            if (keepSessionType <= 0) {
                keepSessionType = keepChatType;
            }
            de.robv.android.xposed.XposedHelpers.setIntField(objNewInstance, "sessionType", keepSessionType);
            de.robv.android.xposed.XposedHelpers.setLongField(objNewInstance, "msgTime", j2 / 1000);
            de.robv.android.xposed.XposedHelpers.setLongField(objNewInstance, "msgId", j2);
            /*
             * 修复"临时会话右侧没有消息数气泡"：
             * 旧实现把 unreadCnt / unreadFlag 硬编码为 0，气泡自然不会出现。
             * 这里按 peerUid 取该会话自己的未读数写入。
             */
            int unread = tempChatUnread(str);
            if (unread > 0) {
                de.robv.android.xposed.XposedHelpers.setIntField(objNewInstance, "unreadCnt", unread);
                de.robv.android.xposed.XposedHelpers.setLongField(objNewInstance, "unreadFlag", 1L);
                // 同时补齐 unreadInfo 结构，部分宿主版本只认这个
                try {
                    java.lang.Object info = de.robv.android.xposed.XposedHelpers.getObjectField(objNewInstance, "unreadInfo");
                    if (info != null) {
                        de.robv.android.xposed.XposedHelpers.setIntField(info, "count", unread);
                        de.robv.android.xposed.XposedHelpers.setIntField(info, "type", 2);
                    }
                } catch (java.lang.Throwable ignored) {
                }
            } else {
                de.robv.android.xposed.XposedHelpers.setIntField(objNewInstance, "unreadCnt", 0);
                de.robv.android.xposed.XposedHelpers.setLongField(objNewInstance, "unreadFlag", 0L);
            }
            java.lang.String strPreviewOf = previewOf(sTempChatKnown.get(str));
            de.robv.android.xposed.XposedHelpers.setObjectField(objNewInstance, "abstractContent", buildAbstractContent(strPreviewOf));
            java.lang.StringBuilder sb = new java.lang.StringBuilder("临时会话: 合成会话条目 peerUid=");
            sb.append(str);
            sb.append(" uin=");
            sb.append(jDigitsOf);
            sb.append(" contactId=");
            sb.append(jAbs);
            sb.append(" name=");
            sb.append(str3);
            sb.append(" 原始chatType=");
            sb.append(i);
            sb.append(" 预览=");
            if (strPreviewOf.isEmpty()) {
                strPreviewOf = "(空)";
            }
            sb.append(strPreviewOf);
            com.tencent.qqlitepro.util.Log.i(sb.toString());
            return objNewInstance;
        } catch (java.lang.Throwable thRethrow6) {
            com.tencent.qqlitepro.util.Log.w("临时会话: 合成会话条目失败 " + chain(thRethrow6));
            return null;
        }
    }

    private static void copyFields(java.lang.Object obj, java.lang.Object obj2) {
        if (obj == null || obj2 == null) {
            return;   // 模板还没准备好时不做拷贝，字段由调用方逐个填充
        }
        for (java.lang.Class<?> superclass = obj.getClass(); superclass != null && superclass != java.lang.Object.class; superclass = superclass.getSuperclass()) {
            for (java.lang.reflect.Field field : superclass.getDeclaredFields()) {
                if (!java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                    try {
                        field.setAccessible(true);
                        java.lang.Object v = field.get(obj);
                        /*
                         * 修复"昵称偶发统一变成同一个好友名/卡片互相串内容"：
                         * 浅拷贝会让多个渲染副本共享同一个 List/Map 字段，
                         * 之后对任一副本改名都会波及全部副本。
                         * 对集合/字符串列表做一层复制，切断共享。
                         */
                        if (v instanceof java.util.List) {
                            v = new java.util.ArrayList((java.util.List) v);
                        } else if (v instanceof java.util.Map) {
                            v = new java.util.HashMap((java.util.Map) v);
                        }
                        field.set(obj2, v);
                    } catch (java.lang.Throwable unused) {
                    }
                }
            }
        }
    }

    private static boolean isRecentContactInfo(java.lang.Object obj) {
        return obj != null && obj.getClass().getName().endsWith("nativeinterface.RecentContactInfo");
    }

    private static long digitsOf(java.lang.String str) {
        if (str == null) {
            return 0L;
        }
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char cCharAt = str.charAt(i);
            if (cCharAt >= '0' && cCharAt <= '9') {
                sb.append(cCharAt);
            } else if (sb.length() > 0) {
                break;
            }
        }
        if (sb.length() == 0) {
            return 0L;
        }
        try {
            return java.lang.Long.parseLong(sb.toString());
        } catch (java.lang.Throwable unused) {
            return 0L;
        }
    }

    private static java.lang.String fetchTempChatNick(int i, java.lang.String str) {
        try {
            java.lang.Object objKernelMsgService = kernelMsgService();
            if (objKernelMsgService == null) {
                return "";
            }
            java.lang.Class<?> cls = cls("com.tencent.qqnt.kernel.nativeinterface.IGetTempChatInfoCallback");
            java.lang.String[] strArr = {""};
            java.util.concurrent.CountDownLatch countDownLatch = new java.util.concurrent.CountDownLatch(1);
            java.lang.Object objNewProxyInstance = java.lang.reflect.Proxy.newProxyInstance(cls.getClassLoader(), new java.lang.Class[]{cls}, new com.tencent.qqlitepro.http.QqKernel$$ExternalSyntheticLambda28(strArr, countDownLatch));
            java.lang.reflect.Method method = cls("com.tencent.qqnt.kernel.nativeinterface.IKernelMsgService").getMethod("getTempChatInfo", java.lang.Integer.TYPE, java.lang.String.class, cls);
            method.setAccessible(true);
            method.invoke(objKernelMsgService, java.lang.Integer.valueOf(i), str, objNewProxyInstance);
            countDownLatch.await(2L, java.util.concurrent.TimeUnit.SECONDS);
            java.lang.String str2 = strArr[0];
            if (str2 != null && !str2.isEmpty()) {
                com.tencent.qqlitepro.util.Log.i("临时会话: getTempChatInfo 昵称=" + str2);
            }
            return str2 == null ? "" : str2;
        } catch (java.lang.Throwable unused) {
            return "";
        }
    }

    static /* synthetic */ java.lang.Object lambda$fetchTempChatNick$21(java.lang.String[] strArr, java.util.concurrent.CountDownLatch countDownLatch, java.lang.Object obj, java.lang.reflect.Method method, java.lang.Object[] objArr) throws java.lang.Throwable {
        java.lang.Object obj2;
        if ("onResult".equals(method.getName()) && objArr != null && objArr.length >= 2 && (obj2 = objArr[1]) != null) {
            strArr[0] = strOf(fieldValue(obj2, "fromNick"));
        }
        countDownLatch.countDown();
        return null;
    }

    public static java.util.ArrayList<java.lang.Object> getSessions() {
        java.util.ArrayList<java.lang.Object> arrayList = new java.util.ArrayList<>();
        sDebug = "";
        try {
            java.lang.reflect.Method methodFindMethod = findMethod(cls("com.tencent.qqnt.msg.KernelServiceUtil"), "g", new java.lang.Class[0]);
            if (methodFindMethod == null) {
                sDebug = "KernelServiceUtil.g() 未找到";
                com.tencent.qqlitepro.util.Log.i("KernelServiceUtil.g() 未找到");
                return arrayList;
            }
            java.lang.Object objInvoke = methodFindMethod.invoke(null, new java.lang.Object[0]);
            if (objInvoke == null) {
                sDebug = "IQQNTWrapperSession 为 null，内核尚未就绪";
                com.tencent.qqlitepro.util.Log.i("IQQNTWrapperSession 为 null，内核尚未就绪");
                return arrayList;
            }
            com.tencent.qqlitepro.util.Log.i("getSessions: session=" + objInvoke.getClass().getName());
            java.lang.reflect.Method method = cls("com.tencent.qqnt.kernel.nativeinterface.IQQNTWrapperSession").getMethod("getRecentContactService", new java.lang.Class[0]);
            method.setAccessible(true);
            java.lang.Object objInvoke2 = method.invoke(objInvoke, new java.lang.Object[0]);
            if (objInvoke2 == null) {
                sDebug = "getRecentContactService() 返回 null";
                com.tencent.qqlitepro.util.Log.i("getRecentContactService() 返回 null");
                return arrayList;
            }
            com.tencent.qqlitepro.util.Log.i("getSessions: rcs=" + objInvoke2.getClass().getName());
            java.lang.Class<?> cls = cls("com.tencent.qqnt.kernel.nativeinterface.IKernelRecentContactService");
            java.util.ArrayList<java.lang.Object> sessionsViaGetInfos = getSessionsViaGetInfos(objInvoke2, cls);
            if (!sessionsViaGetInfos.isEmpty()) {
                return sessionsViaGetInfos;
            }
            java.lang.reflect.Method method2 = cls.getMethod("getRecentContactListSync", new java.lang.Class[0]);
            method2.setAccessible(true);
            java.lang.Object objInvoke3 = method2.invoke(objInvoke2, new java.lang.Object[0]);
            if (objInvoke3 != null) {
                java.lang.String str = "sync sortedContactList count=" + extractSessions(fieldValue(objInvoke3, "sortedContactList"), arrayList) + "\ninfo=" + safeToString(objInvoke3);
                sDebug = str;
                com.tencent.qqlitepro.util.Log.i(str);
                if (!arrayList.isEmpty()) {
                    return arrayList;
                }
            }
            java.util.ArrayList<java.lang.Object> sessionsViaSnapshot = getSessionsViaSnapshot(objInvoke2, cls);
            return !sessionsViaSnapshot.isEmpty() ? sessionsViaSnapshot : arrayList;
        } catch (java.lang.Throwable thRethrow6) {
            java.lang.String str2 = "获取会话列表失败: " + thRethrow6;
            sDebug = str2;
            com.tencent.qqlitepro.util.Log.w(str2);
        }
        return arrayList;
    }

    private static java.util.ArrayList<java.lang.Object> getSessionsViaGetInfos(java.lang.Object obj, java.lang.Class<?> cls) {
        java.util.ArrayList<java.lang.Object> arrayList = new java.util.ArrayList<>();
        try {
            java.lang.Class<?> cls2 = cls("com.tencent.qqnt.kernel.nativeinterface.IKernelRecentGetContactCallback");
            java.lang.Object[] objArr = new java.lang.Object[1];
            java.util.concurrent.CountDownLatch countDownLatch = new java.util.concurrent.CountDownLatch(1);
            java.lang.Object objNewProxyInstance = java.lang.reflect.Proxy.newProxyInstance(cls2.getClassLoader(), new java.lang.Class[]{cls2}, new com.tencent.qqlitepro.http.QqKernel$$ExternalSyntheticLambda20(objArr, countDownLatch));
            java.lang.reflect.Method method = cls.getMethod("getRecentContactInfos", cls2);
            method.setAccessible(true);
            method.invoke(obj, objNewProxyInstance);
            if (!countDownLatch.await(5000L, java.util.concurrent.TimeUnit.MILLISECONDS)) {
                sDebug = "getRecentContactInfos 回调超时";
                com.tencent.qqlitepro.util.Log.i("getRecentContactInfos 回调超时");
                return arrayList;
            }
            java.lang.String str = "getRecentContactInfos count=" + extractSessions(objArr[0], arrayList);
            sDebug = str;
            com.tencent.qqlitepro.util.Log.i(str);
            return arrayList;
        } catch (java.lang.Throwable thRethrow6) {
            java.lang.String str2 = "getRecentContactInfos 失败: " + thRethrow6;
            sDebug = str2;
            com.tencent.qqlitepro.util.Log.w(str2);
        }
        return arrayList;
    }

    static /* synthetic */ java.lang.Object lambda$getSessionsViaGetInfos$22(java.lang.Object[] objArr, java.util.concurrent.CountDownLatch countDownLatch, java.lang.Object obj, java.lang.reflect.Method method, java.lang.Object[] objArr2) throws java.lang.Throwable {
        if (!"onResult".equals(method.getName()) || objArr2 == null || objArr2.length < 3) {
            return null;
        }
        objArr[0] = objArr2[2];
        countDownLatch.countDown();
        return null;
    }

    private static java.util.ArrayList<java.lang.Object> getSessionsViaSnapshot(java.lang.Object obj, java.lang.Class<?> cls) {
        java.util.ArrayList<java.lang.Object> arrayList = new java.util.ArrayList<>();
        try {
            java.lang.Class<?> cls2 = cls("com.tencent.qqnt.kernel.nativeinterface.IKernelRecentSnapShotCallback");
            cls("com.tencent.qqnt.kernel.nativeinterface.CompleteRecentContactInfo");
            java.lang.Object[] objArr = new java.lang.Object[1];
            java.util.concurrent.CountDownLatch countDownLatch = new java.util.concurrent.CountDownLatch(1);
            java.lang.Object objNewProxyInstance = java.lang.reflect.Proxy.newProxyInstance(cls2.getClassLoader(), new java.lang.Class[]{cls2}, new com.tencent.qqlitepro.http.QqKernel$$ExternalSyntheticLambda17(objArr, countDownLatch));
            java.lang.reflect.Method method = cls.getMethod("getRecentContactListSnapShot", java.lang.Integer.TYPE, cls2);
            method.setAccessible(true);
            method.invoke(obj, 0, objNewProxyInstance);
            if (!countDownLatch.await(5000L, java.util.concurrent.TimeUnit.MILLISECONDS)) {
                sDebug = "snapshot 回调超时";
                com.tencent.qqlitepro.util.Log.i("snapshot 回调超时");
                return arrayList;
            }
            java.lang.Object obj2 = objArr[0];
            if (obj2 == null) {
                sDebug = "snapshot 返回 null";
                return arrayList;
            }
            java.lang.String str = "snapshot sortedContactList count=" + extractSessions(fieldValue(obj2, "sortedContactList"), arrayList);
            sDebug = str;
            com.tencent.qqlitepro.util.Log.i(str);
            return arrayList;
        } catch (java.lang.Throwable thRethrow6) {
            java.lang.String str2 = "snapshot 获取失败: " + thRethrow6;
            sDebug = str2;
            com.tencent.qqlitepro.util.Log.w(str2);
        }
        return arrayList;
    }

    static /* synthetic */ java.lang.Object lambda$getSessionsViaSnapshot$23(java.lang.Object[] objArr, java.util.concurrent.CountDownLatch countDownLatch, java.lang.Object obj, java.lang.reflect.Method method, java.lang.Object[] objArr2) throws java.lang.Throwable {
        if (!"onResult".equals(method.getName()) || objArr2 == null || objArr2.length < 3) {
            return null;
        }
        objArr[0] = objArr2[2];
        countDownLatch.countDown();
        return null;
    }

    public static java.util.ArrayList<java.lang.Object> getBuddies() {
        java.util.ArrayList<java.lang.Object> arrayList = new java.util.ArrayList<>();
        try {
            java.lang.reflect.Method methodFindMethod = findMethod(cls("com.tencent.qqnt.msg.KernelServiceUtil"), "g", new java.lang.Class[0]);
            if (methodFindMethod == null) {
                com.tencent.qqlitepro.util.Log.i("getBuddies: KernelServiceUtil.g() 未找到");
                return arrayList;
            }
            java.lang.Object objInvoke = methodFindMethod.invoke(null, new java.lang.Object[0]);
            if (objInvoke == null) {
                com.tencent.qqlitepro.util.Log.i("getBuddies: IQQNTWrapperSession 为 null");
                return arrayList;
            }
            java.lang.reflect.Method method = cls("com.tencent.qqnt.kernel.nativeinterface.IQQNTWrapperSession").getMethod("getBuddyService", new java.lang.Class[0]);
            method.setAccessible(true);
            java.lang.Object objInvoke2 = method.invoke(objInvoke, new java.lang.Object[0]);
            if (objInvoke2 == null) {
                com.tencent.qqlitepro.util.Log.i("getBuddies: getBuddyService() 返回 null");
                return arrayList;
            }
            java.lang.Class<?> cls = cls("com.tencent.qqnt.kernel.nativeinterface.IKernelBuddyService");
            java.lang.Class<?> cls2 = cls("com.tencent.qqnt.kernel.nativeinterface.BuddyListReqType");
            java.lang.Enum enumValueOf = java.lang.Enum.valueOf(cls2.asSubclass(java.lang.Enum.class), "KNOMAL");
            java.util.ArrayList arrayList2 = new java.util.ArrayList();
            try {
                java.lang.Class<?> cls3 = cls("com.tencent.qqnt.kernel.nativeinterface.IBuddyListCallback");
                java.lang.Object[] objArr = new java.lang.Object[1];
                java.util.concurrent.CountDownLatch countDownLatch = new java.util.concurrent.CountDownLatch(1);
                java.lang.Object objNewProxyInstance = java.lang.reflect.Proxy.newProxyInstance(cls3.getClassLoader(), new java.lang.Class[]{cls3}, new com.tencent.qqlitepro.http.QqKernel$$ExternalSyntheticLambda25(objArr, countDownLatch));
                java.lang.reflect.Method method2 = cls.getMethod("getBuddyListV2", java.lang.Boolean.TYPE, cls2, cls3);
                method2.setAccessible(true);
                method2.invoke(objInvoke2, false, enumValueOf, objNewProxyInstance);
                if (countDownLatch.await(6000L, java.util.concurrent.TimeUnit.MILLISECONDS)) {
                    java.lang.Object obj = objArr[0];
                    if (obj instanceof java.util.List) {
                        extractBuddies(obj, arrayList2);
                    }
                }
            } catch (java.lang.Throwable thRethrow6) {
                com.tencent.qqlitepro.util.Log.w("getBuddies: getBuddyListV2 失败: " + thRethrow6);
            }
            if (arrayList2.isEmpty()) {
                try {
                    java.lang.reflect.Method method3 = cls.getMethod("getBuddyListFromCache", cls2);
                    method3.setAccessible(true);
                    java.lang.Object objInvoke3 = method3.invoke(objInvoke2, enumValueOf);
                    if (objInvoke3 instanceof java.util.List) {
                        extractBuddies(objInvoke3, arrayList2);
                    }
                } catch (java.lang.Throwable th2) {
                    com.tencent.qqlitepro.util.Log.w("getBuddies: getBuddyListFromCache 失败: " + th2);
                }
            }
            arrayList.addAll(arrayList2);
            com.tencent.qqlitepro.util.Log.i("getBuddies: 好友 " + arrayList.size() + " 个");
        } catch (java.lang.Throwable th3) {
            com.tencent.qqlitepro.util.Log.w("获取好友列表失败: " + th3);
        }
        return arrayList;
    }

    static /* synthetic */ java.lang.Object lambda$getBuddies$24(java.lang.Object[] objArr, java.util.concurrent.CountDownLatch countDownLatch, java.lang.Object obj, java.lang.reflect.Method method, java.lang.Object[] objArr2) throws java.lang.Throwable {
        if (!"onResult".equals(method.getName()) || objArr2 == null || objArr2.length < 3) {
            return null;
        }
        objArr[0] = objArr2[2];
        countDownLatch.countDown();
        return null;
    }

    /* JADX WARN: Code duplicated, block: B:40:0x009e  */
    private static void extractBuddies(java.lang.Object obj, java.util.ArrayList<java.lang.Object> arrayList) {
        java.lang.String strValueOf;
        if (obj instanceof java.util.List) {
            java.util.LinkedHashMap linkedHashMap = new java.util.LinkedHashMap();
            for (java.lang.Object obj2 : arrayList) {
                if (obj2 instanceof java.util.Map) {
                    java.lang.String str = str(((java.util.Map) obj2).get("peerUid"));
                    if (!str.isEmpty()) {
                        linkedHashMap.put(str, obj2);
                    }
                }
            }
            for (java.lang.Object obj3 : (java.util.List) obj) {
                if (obj3 != null) {
                    java.lang.Object objFieldValue = fieldValue(obj3, "buddyList");
                    if (objFieldValue instanceof java.util.List) {
                        for (java.lang.Object obj4 : (java.util.List) objFieldValue) {
                            if (obj4 != null) {
                                java.lang.Object objFieldValue2 = fieldValue(obj4, "coreInfo");
                                if (objFieldValue2 == null) {
                                    objFieldValue2 = obj4;
                                }
                                java.lang.String str2 = str(fieldValue(objFieldValue2, "uid"));
                                if (!str2.isEmpty()) {
                                    java.lang.Object objFieldValue3 = fieldValue(objFieldValue2, "uin");
                                    if (objFieldValue3 instanceof java.lang.Number) {
                                        java.lang.Number number = (java.lang.Number) objFieldValue3;
                                        if (number.longValue() > 0) {
                                            strValueOf = java.lang.String.valueOf(number.longValue());
                                        } else {
                                            strValueOf = "";
                                        }
                                    } else {
                                        strValueOf = "";
                                    }
                                    java.lang.String str3 = str(fieldValue(objFieldValue2, "nick"));
                                    java.lang.String str4 = str(fieldValue(objFieldValue2, "remark"));
                                    java.util.LinkedHashMap linkedHashMap2 = new java.util.LinkedHashMap();
                                    linkedHashMap2.put("chatType", 1);
                                    linkedHashMap2.put("peerUid", str2);
                                    linkedHashMap2.put("peerUin", strValueOf);
                                    linkedHashMap2.put("peerName", str3);
                                    linkedHashMap2.put("remark", str4);
                                    linkedHashMap2.put("avatarPath", str(fieldValue(obj4, "avatarPath")));
                                    linkedHashMap2.put("typeName", "好友");
                                    java.lang.Object obj5 = linkedHashMap.get(str2);
                                    if (obj5 instanceof java.util.Map) {
                                        java.util.Map map = (java.util.Map) obj5;
                                        java.lang.Object obj6 = map.get("peerUin");
                                        if (obj6 == null || java.lang.String.valueOf(obj6).isEmpty()) {
                                            map.put("peerUin", strValueOf);
                                        }
                                        java.lang.Object obj7 = map.get("remark");
                                        if (obj7 == null || java.lang.String.valueOf(obj7).isEmpty()) {
                                            map.put("remark", str4);
                                        }
                                    } else {
                                        linkedHashMap.put(str2, linkedHashMap2);
                                        arrayList.add(linkedHashMap2);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private static int extractSessions(java.lang.Object obj, java.util.ArrayList<java.lang.Object> arrayList) {
        if (obj instanceof java.util.List) {
            java.util.Iterator it = ((java.util.List) obj).iterator();
            while (it.hasNext()) {
                arrayList.add(sessionToMap(it.next()));
            }
            return arrayList.size();
        }
        if (obj == null || !obj.getClass().isArray()) {
            return 0;
        }
        int length = java.lang.reflect.Array.getLength(obj);
        for (int i = 0; i < length; i++) {
            arrayList.add(sessionToMap(java.lang.reflect.Array.get(obj, i)));
        }
        return arrayList.size();
    }

    public static java.lang.Boolean queryOfficialGroupState(long j) {
        long jCurrentTimeMillis = java.lang.System.currentTimeMillis() + java.lang.Math.max(0L, j);
        while (true) {
            if (kernelSessionReady()) {
                java.lang.Boolean boolInGroupViaGroupList = inGroupViaGroupList(OFFICIAL_GROUP_CODE, GROUP_LIST_TIMEOUT_MS);
                if (java.lang.Boolean.TRUE.equals(boolInGroupViaGroupList)) {
                    com.tencent.qqlitepro.util.Log.i("官方群校验: 内核群列表命中 群号=801524531");
                    return java.lang.Boolean.TRUE;
                }
                if (java.lang.Boolean.FALSE.equals(boolInGroupViaGroupList)) {
                    /*
                     * 修复"已加入官方群，重启后仍提示未加入"：
                     * getGroupList 返回的是内核已缓存的群列表，刚启动时往往只加载了部分
                     * （甚至只有最近会话涉及的群），此时"未命中"并不等于"没加入"。
                     * 旧逻辑在这里直接 return FALSE，导致误判。
                     *
                     * 新逻辑：群列表未命中时继续交叉验证——
                     *   1) 扫描会话列表
                     *   2) 直接向内核查询该群资料（最权威）
                     * 任一命中即视为已加入；全部未命中才判 FALSE。
                     */
                    com.tencent.qqlitepro.util.Log.i("官方群校验: 群列表未命中，转交叉验证（避免缓存不全导致误判）");
                    try {
                        java.util.ArrayList<java.lang.Object> sessions = getSessions();
                        if (!sessions.isEmpty() && containsGroupSession(sessions, OFFICIAL_GROUP_CODE)) {
                            com.tencent.qqlitepro.util.Log.i("官方群校验: 会话列表命中官方群，判定为已加入");
                            return java.lang.Boolean.TRUE;
                        }
                    } catch (java.lang.Throwable ignored) {
                    }
                    // 直接向内核查询群资料：能拿到就说明在群里
                    java.lang.Boolean direct = queryGroupInfoExists(OFFICIAL_GROUP_CODE, 12000L);
                    if (java.lang.Boolean.TRUE.equals(direct)) {
                        com.tencent.qqlitepro.util.Log.i("官方群校验: 内核群资料查询命中，判定为已加入");
                        return java.lang.Boolean.TRUE;
                    }
                    if (java.lang.Boolean.FALSE.equals(direct)) {
                        com.tencent.qqlitepro.util.Log.i("官方群校验: 群列表与会话均未命中，且群资料查询确认不在群，判定为未加入");
                        return java.lang.Boolean.FALSE;
                    }
                    // 无法确认：交给外层重试，绝不误判为"未加入"
                    com.tencent.qqlitepro.util.Log.w("官方群校验: 群列表未命中但无法确认，本轮不下结论");
                    return null;
                }
                try {
                    java.util.ArrayList<java.lang.Object> sessions = getSessions();
                    if (!sessions.isEmpty()) {
                        boolean zContainsGroupSession = containsGroupSession(sessions, OFFICIAL_GROUP_CODE);
                        com.tencent.qqlitepro.util.Log.i("官方群校验: 群列表不可用，会话数=" + sessions.size() + " 群号=" + OFFICIAL_GROUP_CODE + " 会话命中=" + zContainsGroupSession);
                        return zContainsGroupSession ? java.lang.Boolean.TRUE : java.lang.Boolean.FALSE;
                    }
                } catch (java.lang.Throwable thRethrow6) {
                    com.tencent.qqlitepro.util.Log.w("官方群校验会话扫描异常: " + thRethrow6);
                }
            }
            if (java.lang.System.currentTimeMillis() >= jCurrentTimeMillis) {
                com.tencent.qqlitepro.util.Log.i("官方群校验: 内核数据未就绪，本轮无法判定（不等同于未加入）");
                return null;
            }
            try {
                java.lang.Thread.sleep(200L);
            } catch (java.lang.InterruptedException unused) {
                com.tencent.qqlitepro.util.Log.w("官方群校验被中断，本轮无法判定");
                return null;
            }
        }
    }

    public static boolean hasOfficialGroupSession(long j) {
        return java.lang.Boolean.TRUE.equals(queryOfficialGroupState(j));
    }

    /**
     * 直接向内核查询某个群是否存在（用于门禁兜底校验）。
     *
     * 不硬编码具体方法名：内核不同版本叫 getGroupInfo / getSimpleGroupInfo / getGroupExtInfo，
     * 这里按"参数含 long 群号 + 回调"且返回 void 的特征动态匹配。
     * 返回 TRUE=在群、FALSE=明确不在群、null=无法确认。
     */
    private static java.lang.Boolean queryGroupInfoExists(long groupCode, long timeoutMs) {
        try {
            java.lang.Object svc = kernelGroupService();
            if (svc == null) {
                return null;
            }
            java.lang.Class<?> cbCls = cls("com.tencent.qqnt.kernel.nativeinterface.IOperateCallback");
            if (cbCls == null) {
                return null;
            }
            java.lang.reflect.Method target = null;
            for (java.lang.reflect.Method m : svc.getClass().getMethods()) {
                java.lang.String n = m.getName();
                if (!n.toLowerCase(java.util.Locale.ROOT).contains("groupinfo")) {
                    continue;
                }
                java.lang.Class<?>[] ps = m.getParameterTypes();
                if (ps.length < 2) {
                    continue;
                }
                boolean hasLong = false;
                boolean hasCb = false;
                for (java.lang.Class<?> pc : ps) {
                    if (pc == java.lang.Long.TYPE) {
                        hasLong = true;
                    }
                    if (pc.isAssignableFrom(cbCls) || cbCls.isAssignableFrom(pc)) {
                        hasCb = true;
                    }
                }
                if (hasLong && hasCb) {
                    target = m;
                    break;
                }
            }
            if (target == null) {
                com.tencent.qqlitepro.util.Log.i("官方群校验: 内核无可用群资料查询方法");
                return null;
            }
            target.setAccessible(true);
            final java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
            final java.lang.Object[] result = new java.lang.Object[1];
            java.lang.Object cb = java.lang.reflect.Proxy.newProxyInstance(
                    cbCls.getClassLoader(), new java.lang.Class<?>[]{cbCls},
                    new java.lang.reflect.InvocationHandler() {
                        @Override
                        public java.lang.Object invoke(java.lang.Object proxy, java.lang.reflect.Method m, java.lang.Object[] args) {
                            if ("onResult".equals(m.getName()) && args != null && args.length >= 2) {
                                // args[0]=code(0 成功) args[1]=结果对象
                                result[0] = args[1];
                                result[1 & 0] = args[0];
                            }
                            latch.countDown();
                            return null;
                        }
                    });
            // 组装参数：long 群号、回调，其余填默认值
            java.lang.Class<?>[] ps = target.getParameterTypes();
            java.lang.Object[] argv = new java.lang.Object[ps.length];
            boolean longFilled = false;
            for (int i = 0; i < ps.length; i++) {
                if (ps[i] == java.lang.Long.TYPE && !longFilled) {
                    argv[i] = java.lang.Long.valueOf(groupCode);
                    longFilled = true;
                } else if (ps[i].isAssignableFrom(cbCls) || cbCls.isAssignableFrom(ps[i])) {
                    argv[i] = cb;
                } else if (ps[i] == java.lang.Integer.TYPE) {
                    argv[i] = java.lang.Integer.valueOf(0);
                } else if (ps[i] == java.lang.Boolean.TYPE) {
                    argv[i] = java.lang.Boolean.FALSE;
                } else {
                    argv[i] = null;
                }
            }
            target.invoke(svc, argv);
            if (!latch.await(timeoutMs, java.util.concurrent.TimeUnit.MILLISECONDS)) {
                com.tencent.qqlitepro.util.Log.i("官方群校验: 群资料查询超时");
                return null;
            }
            if (result[0] == null) {
                return java.lang.Boolean.FALSE;   // 查不到资料，视为不在群
            }
            com.tencent.qqlitepro.util.Log.i("官方群校验: 群资料查询成功，判定为已加入");
            return java.lang.Boolean.TRUE;
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("官方群校验: 群资料查询异常 " + chain(th));
            return null;
        }
    }

    private static java.lang.Boolean inGroupViaGroupList(long j, long j2) {
        try {
            java.lang.Object objKernelGroupService = kernelGroupService();
            if (objKernelGroupService == null || ensureGroupNotifyListener(objKernelGroupService) == null) {
                return null;
            }
            java.lang.Class<?> cls = cls("com.tencent.qqnt.kernel.nativeinterface.IOperateCallback");
            java.lang.reflect.Method methodFindMethod = findMethod(objKernelGroupService.getClass(), "getGroupList", java.lang.Boolean.TYPE, cls);
            if (methodFindMethod == null) {
                return null;
            }
            methodFindMethod.setAccessible(true);
            com.tencent.qqlitepro.http.QqKernel.ApplyWaiter applyWaiter = new com.tencent.qqlitepro.http.QqKernel.ApplyWaiter();
            sGroupListWaiter = applyWaiter;
            try {
                methodFindMethod.invoke(objKernelGroupService, false, loggingOperateCallback(cls, "getGroupList"));
                if (!applyWaiter.latch.await(j2, java.util.concurrent.TimeUnit.MILLISECONDS)) {
                    com.tencent.qqlitepro.util.Log.i("官方群校验: getGroupList 回调超时（" + j2 + "ms）");
                    return null;
                }
                java.lang.Object obj = applyWaiter.groupList;
                if (!(obj instanceof java.util.List)) {
                    return null;
                }
                java.util.List list = (java.util.List) obj;
                if (list.isEmpty()) {
                    return null;
                }
                com.tencent.qqlitepro.util.Log.i("官方群校验: 群列表已加载，群数=" + list.size());
                return java.lang.Boolean.valueOf(containsGroupCode(list, j));
            } finally {
                sGroupListWaiter = null;
            }
        } catch (java.lang.Throwable thRethrow6) {
            com.tencent.qqlitepro.util.Log.w("官方群校验: getGroupList 路径不可用 " + chain(thRethrow6));
            return null;
        }
    }

    private static boolean containsGroupCode(java.lang.Object obj, long j) {
        if (!(obj instanceof java.util.List)) {
            return false;
        }
        for (java.lang.Object obj2 : (java.util.List) obj) {
            if (!(obj2 instanceof java.lang.Number)) {
                java.lang.Object objFieldValue = fieldValue(obj2, "groupCode");
                if ((objFieldValue instanceof java.lang.Number) && ((java.lang.Number) objFieldValue).longValue() == j) {
                    return true;
                }
            } else if (((java.lang.Number) obj2).longValue() == j) {
                return true;
            }
        }
        return false;
    }

    private static boolean containsGroupSession(java.util.ArrayList<java.lang.Object> arrayList, long j) {
        java.lang.String strValueOf = java.lang.String.valueOf(j);
        for (java.lang.Object obj : arrayList) {
            if (obj instanceof java.util.Map) {
                java.util.Map map = (java.util.Map) obj;
                java.lang.String strValueOf2 = java.lang.String.valueOf(map.get("peerUid"));
                java.lang.String strValueOf3 = java.lang.String.valueOf(map.get("peerUin"));
                if (strValueOf.equals(strValueOf2) || strValueOf.equals(strValueOf3)) {
                    java.lang.Object obj2 = map.get("chatType");
                    if (obj2 == null || intOf(obj2) == 2) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean kernelSessionReady() {
        try {
            java.lang.reflect.Method methodFindMethod = findMethod(cls("com.tencent.qqnt.msg.KernelServiceUtil"), "g", new java.lang.Class[0]);
            return (methodFindMethod == null || methodFindMethod.invoke(null, new java.lang.Object[0]) == null) ? false : true;
        } catch (java.lang.Throwable unused) {
            return false;
        }
    }

    private static java.lang.Object sessionToMap(java.lang.Object obj) {
        java.util.LinkedHashMap linkedHashMap = new java.util.LinkedHashMap();
        linkedHashMap.put("chatType", fieldValue(obj, "chatType"));
        linkedHashMap.put("peerUid", str(fieldValue(obj, "peerUid")));
        linkedHashMap.put("peerUin", fieldValue(obj, "peerUin"));
        linkedHashMap.put("peerName", str(fieldValue(obj, "peerName")));
        linkedHashMap.put("remark", str(fieldValue(obj, "remark")));
        linkedHashMap.put("memberName", str(fieldValue(obj, "memberName")));
        linkedHashMap.put("avatarPath", resolveAvatarLocalPath(obj));
        linkedHashMap.put("unreadCnt", fieldValue(obj, "unreadCnt"));
        linkedHashMap.put("lastMsgTime", fieldValue(obj, "msgTime"));
        linkedHashMap.put("lastMsg", summarizeLastMsg(fieldValue(obj, "abstractContent")));
        linkedHashMap.put("typeName", chatTypeName(intOf(fieldValue(obj, "chatType"))));
        linkedHashMap.put("rawJson", sessionToRaw(obj));
        return linkedHashMap;
    }

    private static java.lang.String resolveAvatarLocalPath(java.lang.Object obj) {
        java.lang.String str = str(fieldValue(obj, "avatarPath"));
        if (str != null && !str.trim().isEmpty()) {
            return str;
        }
        try {
            for (java.lang.Class<?> superclass = obj.getClass(); superclass != null && superclass != java.lang.Object.class; superclass = superclass.getSuperclass()) {
                for (java.lang.reflect.Field field : superclass.getDeclaredFields()) {
                    if (field.getType() == java.lang.String.class) {
                        field.setAccessible(true);
                        java.lang.Object obj2 = field.get(obj);
                        if (obj2 instanceof java.lang.String) {
                            java.lang.String strTrim = ((java.lang.String) obj2).trim();
                            if (!strTrim.isEmpty() && !strTrim.startsWith("http") && (strTrim.startsWith("/") || strTrim.startsWith("file:"))) {
                                return strTrim;
                            }
                        } else {
                            continue;
                        }
                    }
                }
            }
        } catch (java.lang.Throwable unused) {
        }
        java.lang.String str2 = str(fieldValue(obj, "avatarUrl"));
        if (str2 != null && (str2.startsWith("http://") || str2.startsWith("https://"))) {
            return str2;
        }
        try {
            if (intOf(fieldValue(obj, "chatType")) != 2) {
                return null;
            }
            java.lang.String str3 = str(fieldValue(obj, "peerUin"));
            if (str3 == null || str3.isEmpty()) {
                str3 = str(fieldValue(obj, "uin"));
            }
            if (str3 == null || !str3.matches("\\d+")) {
                return null;
            }
            return "https://p.qlogo.cn/gh/" + str3 + "/" + str3 + "/0";
        } catch (java.lang.Throwable unused2) {
            return null;
        }
    }

    private static java.lang.String sessionToRaw(java.lang.Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            com.google.gson.JsonObject jsonObject = new com.google.gson.JsonObject();
            java.util.HashSet hashSet = new java.util.HashSet();
            for (java.lang.Class<?> superclass = obj.getClass(); superclass != null && superclass != java.lang.Object.class; superclass = superclass.getSuperclass()) {
                for (java.lang.reflect.Field field : superclass.getDeclaredFields()) {
                    java.lang.String name = field.getName();
                    if (name != null && !hashSet.contains(name) && !name.startsWith("$") && !name.contains("this$")) {
                        hashSet.add(name);
                        try {
                            field.setAccessible(true);
                            addRawField(jsonObject, name, field.get(obj));
                        } catch (java.lang.Throwable unused) {
                        }
                    }
                }
            }
            return new com.google.gson.Gson().toJson((com.google.gson.JsonElement) jsonObject);
        } catch (java.lang.Throwable thRethrow6) {
            com.tencent.qqlitepro.util.Log.w("会话原始JSON编排失败: " + thRethrow6);
            return null;
        }
    }

    private static void addRawField(com.google.gson.JsonObject jsonObject, java.lang.String str, java.lang.Object obj) {
        try {
            if (obj == null) {
                jsonObject.addProperty(str, (java.lang.String) null);
                return;
            }
            if (!(obj instanceof java.lang.String) && !(obj instanceof java.lang.Character)) {
                if (!(obj instanceof java.lang.Long) && !(obj instanceof java.lang.Integer) && !(obj instanceof java.lang.Short) && !(obj instanceof java.lang.Byte)) {
                    if (!(obj instanceof java.lang.Double) && !(obj instanceof java.lang.Float)) {
                        if (obj instanceof java.lang.Boolean) {
                            jsonObject.addProperty(str, (java.lang.Boolean) obj);
                            return;
                        }
                        if (obj instanceof java.lang.Number) {
                            jsonObject.addProperty(str, java.lang.Double.valueOf(((java.lang.Number) obj).doubleValue()));
                            return;
                        }
                        if (obj.getClass().isEnum()) {
                            jsonObject.addProperty(str, java.lang.String.valueOf(obj));
                            return;
                        }
                        if (obj.getClass().isArray() && obj.getClass().getComponentType().isPrimitive()) {
                            java.lang.StringBuilder sb = new java.lang.StringBuilder();
                            sb.append('[');
                            int length = java.lang.reflect.Array.getLength(obj);
                            for (int i = 0; i < length; i++) {
                                if (i > 0) {
                                    sb.append(',');
                                }
                                sb.append(java.lang.reflect.Array.get(obj, i));
                            }
                            sb.append(']');
                            jsonObject.addProperty(str, sb.toString());
                            return;
                        }
                        java.lang.String strValueOf = java.lang.String.valueOf(obj);
                        if (strValueOf.length() > 400) {
                            strValueOf = strValueOf.substring(0, 400) + "...";
                        }
                        jsonObject.addProperty(str, strValueOf);
                        return;
                    }
                    jsonObject.addProperty(str, java.lang.Double.valueOf(((java.lang.Number) obj).doubleValue()));
                    return;
                }
                jsonObject.addProperty(str, java.lang.Long.valueOf(((java.lang.Number) obj).longValue()));
                return;
            }
            jsonObject.addProperty(str, java.lang.String.valueOf(obj));
        } catch (java.lang.Throwable unused) {
        }
    }

    private static java.lang.String summarizeLastMsg(java.lang.Object obj) {
        java.lang.String strElementToText;
        if (!(obj instanceof java.util.List)) {
            return null;
        }
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        for (java.lang.Object obj2 : (java.util.List) obj) {
            if (obj2 != null && (strElementToText = elementToText(obj2)) != null && !strElementToText.isEmpty()) {
                if (sb.length() > 0) {
                    sb.append(' ');
                }
                if (sb.length() + strElementToText.length() > 80) {
                    break;
                }
                sb.append(strElementToText);
            }
        }
        if (sb.length() == 0) {
            return null;
        }
        return sb.toString();
    }

    private static java.lang.String elementToText(java.lang.Object obj) {
        java.lang.String str;
        java.lang.Integer numIntObj = intObj(fieldValue(obj, "elementType"));
        if (numIntObj == null) {
            return "";
        }
        int iIntValue = numIntObj.intValue();
        if (iIntValue == 1) {
            return "[图片]";
        }
        if (iIntValue == 2) {
            java.lang.Object objFieldValue = fieldValue(obj, "textElement");
            return (objFieldValue == null || (str = str(fieldValue(objFieldValue, "content"))) == null || str.isEmpty()) ? "[文本]" : str;
        }
        if (iIntValue == 3) {
            return "[语音]";
        }
        if (iIntValue != 4) {
            return iIntValue != 6 ? "[消息]" : "[表情]";
        }
        return "[视频]";
    }

    private static java.lang.String chatTypeName(int i) {
        if (i == 1) {
            return "好友";
        }
        if (i == 2) {
            return "群聊";
        }
        if (i == 3) {
            return "临时会话";
        }
        if (i == 4) {
            return "频道(子区)";
        }
        return "类型" + i;
    }

    private static java.lang.Integer intObj(java.lang.Object obj) {
        if (obj instanceof java.lang.Number) {
            return java.lang.Integer.valueOf(((java.lang.Number) obj).intValue());
        }
        return null;
    }

    private static java.lang.String safeToString(java.lang.Object obj) {
        if (obj == null) {
            return "null";
        }
        try {
            java.lang.String strValueOf = java.lang.String.valueOf(obj);
            if (strValueOf.length() <= 600) {
                return strValueOf;
            }
            return strValueOf.substring(0, 600) + "...";
        } catch (java.lang.Throwable thRethrow6) {
            return "toString threw " + thRethrow6;
        }
    }

    private static java.lang.String str(java.lang.Object obj) {
        if (obj == null) {
            return null;
        }
        return java.lang.String.valueOf(obj);
    }

    private static void patchForwardSelectLimit(java.lang.ClassLoader classLoader) {
        int i;
        if (classLoader == null) {
            return;
        }
        try {
            de.robv.android.xposed.XposedHelpers.findAndHookMethod(java.lang.Class.forName("com.tencent.qqnt.watch.fs.FriendSelectFragment", false, classLoader), "onCreate", new java.lang.Object[]{android.os.Bundle.class, new com.tencent.qqlitepro.http.QqKernel.AnonymousClass16()});
            try {
                com.tencent.qqlitepro.util.Log.i("[转发限制] FriendSelectFragment.r(maxSize) 上限已去除");
                i = 1;
            } catch (java.lang.Throwable thRethrow6) {
                i = 1;
                com.tencent.qqlitepro.util.Log.w("[转发限制] FriendSelectFragment hook 失败: " + thRethrow6);
            }
        } catch (java.lang.Throwable th2) {
            java.lang.Throwable thRethrow7 = th2;
            i = 0;
        }
        try {
            de.robv.android.xposed.XposedHelpers.findAndHookMethod(java.lang.Class.forName("com.tencent.qqnt.watch.fs.GroupMemberSelectFragment", false, classLoader), "onCreate", new java.lang.Object[]{android.os.Bundle.class, new com.tencent.qqlitepro.http.QqKernel.AnonymousClass17()});
            i++;
            com.tencent.qqlitepro.util.Log.i("[转发限制] GroupMemberSelectFragment.k(maxSize) 上限已去除");
        } catch (java.lang.Throwable th3) {
            com.tencent.qqlitepro.util.Log.w("[转发限制] GroupMemberSelectFragment hook 失败: " + th3);
        }
        if (i > 0) {
            com.tencent.qqlitepro.util.Log.i("[转发限制] 转发会话选择数量上限已去除（hook " + i + " 个 Fragment）");
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.http.QqKernel$16, reason: invalid class name */
    static class AnonymousClass16 extends de.robv.android.xposed.XC_MethodHook {
        AnonymousClass16() {
        }

        protected void afterHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            try {
                de.robv.android.xposed.XposedHelpers.setIntField(methodHookParam.thisObject, "r", Integer.MAX_VALUE);
            } catch (java.lang.Throwable unused) {
            }
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.http.QqKernel$17, reason: invalid class name */
    static class AnonymousClass17 extends de.robv.android.xposed.XC_MethodHook {
        AnonymousClass17() {
        }

        protected void afterHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            try {
                de.robv.android.xposed.XposedHelpers.setIntField(methodHookParam.thisObject, "k", Integer.MAX_VALUE);
            } catch (java.lang.Throwable unused) {
            }
        }
    }

    private static java.lang.Object kernelBuddyService() throws java.lang.Throwable {
        java.lang.reflect.Method methodFindMethod = findMethod(cls("com.tencent.qqnt.msg.KernelServiceUtil"), "g", new java.lang.Class[0]);
        if (methodFindMethod == null) {
            throw new java.lang.NoSuchMethodException("KernelServiceUtil.g()(IQQNTWrapperSession) not found");
        }
        java.lang.Object objInvoke = methodFindMethod.invoke(null, new java.lang.Object[0]);
        if (objInvoke == null) {
            throw new java.lang.IllegalStateException("IQQNTWrapperSession 为 null（内核未就绪）");
        }
        java.lang.reflect.Method methodFindMethod2 = findMethod(objInvoke.getClass(), "getBuddyService", new java.lang.Class[0]);
        if (methodFindMethod2 == null) {
            throw new java.lang.NoSuchMethodException("IQQNTWrapperSession.getBuddyService() not found");
        }
        java.lang.Object objInvoke2 = methodFindMethod2.invoke(objInvoke, new java.lang.Object[0]);
        if (objInvoke2 != null) {
            return objInvoke2;
        }
        throw new java.lang.IllegalStateException("IKernelBuddyService 为 null（内核未就绪）");
    }

    private static long parseLongOr0(java.lang.String str) {
        if (str == null) {
            return 0L;
        }
        try {
            return java.lang.Long.parseLong(str.trim());
        } catch (java.lang.Throwable unused) {
            return 0L;
        }
    }

    private static boolean boolOf(java.lang.Object obj) {
        if (obj instanceof java.lang.Boolean) {
            return ((java.lang.Boolean) obj).booleanValue();
        }
        return obj != null && java.lang.Boolean.parseBoolean(java.lang.String.valueOf(obj));
    }

    private static java.lang.String enumName(java.lang.Object obj) {
        return obj instanceof java.lang.Enum ? ((java.lang.Enum) obj).name() : "";
    }

    private static int enumOrdinal(java.lang.Object obj) {
        if (obj instanceof java.lang.Enum) {
            return ((java.lang.Enum) obj).ordinal();
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    static final class ApplyWaiter {
        volatile java.lang.Object buddyReqInfo;
        volatile java.lang.Object buddySetting;
        volatile java.lang.Object groupList;
        volatile long groupNextSeq;
        volatile java.lang.Object groupNotifies;
        final java.util.concurrent.CountDownLatch latch;



        private ApplyWaiter() {
            this.latch = new java.util.concurrent.CountDownLatch(1);
        }
    }

    private static java.lang.Object defaultReturnValue(java.lang.Class<?> cls) {
        if (cls == java.lang.Boolean.TYPE) {
            return java.lang.Boolean.FALSE;
        }
        if (cls == java.lang.Long.TYPE) {
            return 0L;
        }
        if (cls == java.lang.Integer.TYPE) {
            return 0;
        }
        if (cls == java.lang.Short.TYPE) {
            return (short) 0;
        }
        if (cls == java.lang.Byte.TYPE) {
            return (byte) 0;
        }
        if (cls == java.lang.Character.TYPE) {
            return (char) 0;
        }
        if (cls == java.lang.Float.TYPE) {
            return java.lang.Float.valueOf(0.0f);
        }
        if (cls == java.lang.Double.TYPE) {
            return java.lang.Double.valueOf(0.0d);
        }
        return null;
    }

    private static java.lang.Object applyListener() {
        java.lang.Object obj = sApplyListener;
        if (obj != null) {
            return obj;
        }
        synchronized (com.tencent.qqlitepro.http.QqKernel.class) {
            if (sApplyListener != null) {
                return sApplyListener;
            }
            try {
                java.lang.Class<?> cls = cls("com.tencent.qqnt.kernel.nativeinterface.IKernelBuddyListener");
                java.lang.Object objNewProxyInstance = java.lang.reflect.Proxy.newProxyInstance(cls.getClassLoader(), new java.lang.Class[]{cls, cls("com.tencent.qqnt.kernel.nativeinterface.IKernelGroupListener")}, new com.tencent.qqlitepro.http.QqKernel$$ExternalSyntheticLambda19());
                sApplyListener = objNewProxyInstance;
                return objNewProxyInstance;
            } catch (java.lang.Throwable thRethrow7) {
                com.tencent.qqlitepro.util.Log.w("申请: 监听创建失败 " + chain(thRethrow7));
                return null;
            }
        }
    }

    static /* synthetic */ java.lang.Object lambda$applyListener$25(java.lang.Object obj, java.lang.reflect.Method method, java.lang.Object[] objArr) throws java.lang.Throwable {
        com.tencent.qqlitepro.http.QqKernel.ApplyWaiter applyWaiter;
        try {
            java.lang.String name = method.getName();
            if ("onBuddyReqChange".equals(name) && objArr != null && objArr.length >= 1) {
                com.tencent.qqlitepro.http.QqKernel.ApplyWaiter applyWaiter2 = sBuddyReqWaiter;
                if (applyWaiter2 != null) {
                    applyWaiter2.buddyReqInfo = objArr[0];
                    applyWaiter2.latch.countDown();
                }
            } else if ("onCheckBuddySettingResult".equals(name) && objArr != null && objArr.length >= 1) {
                com.tencent.qqlitepro.http.QqKernel.ApplyWaiter applyWaiter3 = sBuddySettingWaiter;
                if (applyWaiter3 != null) {
                    applyWaiter3.buddySetting = objArr[0];
                    applyWaiter3.latch.countDown();
                }
            } else if ("onGroupSingleScreenNotifies".equals(name) && objArr != null && objArr.length >= 3) {
                com.tencent.qqlitepro.http.QqKernel.ApplyWaiter applyWaiter4 = sGroupNotifyWaiter;
                if (applyWaiter4 != null) {
                    applyWaiter4.groupNextSeq = longOf(objArr[1]);
                    applyWaiter4.groupNotifies = objArr[2];
                    applyWaiter4.latch.countDown();
                }
            } else if ("onGroupListUpdate".equals(name) && objArr != null && objArr.length >= 2 && (applyWaiter = sGroupListWaiter) != null) {
                applyWaiter.groupList = objArr[1];
                applyWaiter.latch.countDown();
            }
        } catch (java.lang.Throwable unused) {
        }
        return defaultReturnValue(method.getReturnType());
    }

    private static java.lang.Object ensureBuddyListener(java.lang.Object obj) {
        if (sBuddyListenerReady) {
            return obj;
        }
        try {
            java.lang.Object objApplyListener = applyListener();
            java.lang.reflect.Method methodFindMethod = findMethod(obj.getClass(), "addKernelBuddyListener", cls("com.tencent.qqnt.kernel.nativeinterface.IKernelBuddyListener"));
            if (methodFindMethod == null) {
                com.tencent.qqlitepro.util.Log.w("申请: IKernelBuddyService.addKernelBuddyListener 未找到");
                return null;
            }
            methodFindMethod.setAccessible(true);
            methodFindMethod.invoke(obj, objApplyListener);
            sBuddyListenerReady = true;
            com.tencent.qqlitepro.util.Log.i("申请: 好友监听已注册");
            return obj;
        } catch (java.lang.Throwable thRethrow7) {
            com.tencent.qqlitepro.util.Log.w("申请: 好友监听注册失败 " + chain(thRethrow7));
            return null;
        }
    }

    private static java.lang.Object ensureGroupNotifyListener(java.lang.Object obj) {
        if (sGroupNotifyListenerReady) {
            return obj;
        }
        try {
            java.lang.Object objApplyListener = applyListener();
            java.lang.reflect.Method methodFindMethod = findMethod(obj.getClass(), "addKernelGroupListener", cls("com.tencent.qqnt.kernel.nativeinterface.IKernelGroupListener"));
            if (methodFindMethod == null) {
                com.tencent.qqlitepro.util.Log.w("申请: IKernelGroupService.addKernelGroupListener 未找到");
                return null;
            }
            methodFindMethod.setAccessible(true);
            methodFindMethod.invoke(obj, objApplyListener);
            sGroupNotifyListenerReady = true;
            com.tencent.qqlitepro.util.Log.i("申请: 群通知监听已注册");
            return obj;
        } catch (java.lang.Throwable thRethrow7) {
            com.tencent.qqlitepro.util.Log.w("申请: 群通知监听注册失败 " + chain(thRethrow7));
            return null;
        }
    }

    private static java.lang.Object loggingOperateCallback(java.lang.Class<?> cls, java.lang.String str) {
        return java.lang.reflect.Proxy.newProxyInstance(cls.getClassLoader(), new java.lang.Class[]{cls}, new com.tencent.qqlitepro.http.QqKernel$$ExternalSyntheticLambda5(str));
    }

    static /* synthetic */ java.lang.Object lambda$loggingOperateCallback$26(java.lang.String str, java.lang.Object obj, java.lang.reflect.Method method, java.lang.Object[] objArr) throws java.lang.Throwable {
        if ("onResult".equals(method.getName()) && objArr != null) {
            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            sb.append(str);
            sb.append(" onResult code=");
            sb.append(objArr.length > 0 ? objArr[0] : null);
            sb.append(" msg=");
            sb.append(objArr.length > 1 ? objArr[1] : null);
            com.tencent.qqlitepro.util.Log.i(sb.toString());
        }
        return null;
    }

    private static com.tencent.qqlitepro.http.QqKernel.ApplyResult awaitOperate(java.lang.reflect.Method method, java.lang.Object obj, java.lang.Object[] objArr, long j) {
        return awaitOperate(method, obj, objArr, j, false);
    }

    private static com.tencent.qqlitepro.http.QqKernel.ApplyResult awaitOperate(java.lang.reflect.Method method, java.lang.Object obj, java.lang.Object[] objArr, long j, boolean z) {
        com.tencent.qqlitepro.http.QqKernel.ApplyResult applyResult = new com.tencent.qqlitepro.http.QqKernel.ApplyResult();
        try {
            if (method == null) {
                applyResult.msg = "内核方法未找到";
                return applyResult;
            }
            java.lang.Class<?> cls = cls("com.tencent.qqnt.kernel.nativeinterface.IOperateCallback");
            int[] iArr = {-1};
            java.lang.String[] strArr = {""};
            java.util.concurrent.CountDownLatch countDownLatch = new java.util.concurrent.CountDownLatch(1);
            java.lang.Object objNewProxyInstance = java.lang.reflect.Proxy.newProxyInstance(cls.getClassLoader(), new java.lang.Class[]{cls}, new com.tencent.qqlitepro.http.QqKernel$$ExternalSyntheticLambda21(iArr, strArr, countDownLatch));
            java.lang.Object[] objArr2 = new java.lang.Object[objArr.length + 1];
            java.lang.System.arraycopy(objArr, 0, objArr2, 0, objArr.length);
            objArr2[objArr.length] = objNewProxyInstance;
            method.setAccessible(true);
            if (z && android.os.Looper.myLooper() != android.os.Looper.getMainLooper()) {
                java.util.concurrent.CountDownLatch countDownLatch2 = new java.util.concurrent.CountDownLatch(1);
                new android.os.Handler(android.os.Looper.getMainLooper()).post(new com.tencent.qqlitepro.http.QqKernel$$ExternalSyntheticLambda22(method, obj, objArr2, countDownLatch2));
                countDownLatch2.await(3000L, java.util.concurrent.TimeUnit.MILLISECONDS);
            } else {
                method.invoke(obj, objArr2);
            }
            if (j <= 0) {
                j = 5000;
            }
            if (!countDownLatch.await(j, java.util.concurrent.TimeUnit.MILLISECONDS)) {
                applyResult.timeout = true;
                applyResult.msg = "等待内核 onResult 回调超时";
                return applyResult;
            }
            applyResult.code = iArr[0];
            applyResult.msg = strArr[0];
            return applyResult;
        } catch (java.lang.reflect.InvocationTargetException e) {
            applyResult.msg = "内核调用异常: " + chain(e.getCause());
            return applyResult;
        } catch (java.lang.Throwable thRethrow7) {
            applyResult.msg = "内核调用异常: " + chain(thRethrow7);
            return applyResult;
        }
    }

    static /* synthetic */ java.lang.Object lambda$awaitOperate$27(int[] iArr, java.lang.String[] strArr, java.util.concurrent.CountDownLatch countDownLatch, java.lang.Object obj, java.lang.reflect.Method method, java.lang.Object[] objArr) throws java.lang.Throwable {
        if (!"onResult".equals(method.getName()) || objArr == null) {
            return null;
        }
        if (objArr.length > 0) {
            java.lang.Object obj2 = objArr[0];
            if (obj2 instanceof java.lang.Number) {
                iArr[0] = ((java.lang.Number) obj2).intValue();
            }
        }
        if (objArr.length > 1) {
            strArr[0] = strOf(objArr[1]);
        }
        countDownLatch.countDown();
        return null;
    }

    static /* synthetic */ void lambda$awaitOperate$28(java.lang.reflect.Method method, java.lang.Object obj, java.lang.Object[] objArr, java.util.concurrent.CountDownLatch countDownLatch) {
        try {
            method.invoke(obj, objArr);
        } catch (java.lang.Throwable thRethrow7) {
            try {
                com.tencent.qqlitepro.util.Log.w("内核调用(主线程)异常: " + chain(thRethrow7));
            } finally {
                countDownLatch.countDown();
            }
        }
    }

    public static com.google.gson.JsonObject getFriendRequests(long j) {
        try {
            java.lang.Object objKernelBuddyService = kernelBuddyService();
            if (ensureBuddyListener(objKernelBuddyService) == null) {
                return null;
            }
            java.lang.Class<?> cls = cls("com.tencent.qqnt.kernel.nativeinterface.IOperateCallback");
            java.lang.reflect.Method methodFindMethod = findMethod(objKernelBuddyService.getClass(), "getBuddyReq", cls);
            if (methodFindMethod == null) {
                com.tencent.qqlitepro.util.Log.w("好友申请: getBuddyReq(IOperateCallback) 未找到");
                return null;
            }
            com.tencent.qqlitepro.http.QqKernel.ApplyWaiter applyWaiter = new com.tencent.qqlitepro.http.QqKernel.ApplyWaiter();
            sBuddyReqWaiter = applyWaiter;
            try {
                methodFindMethod.setAccessible(true);
                methodFindMethod.invoke(objKernelBuddyService, loggingOperateCallback(cls, "好友申请: getBuddyReq"));
                java.util.concurrent.CountDownLatch countDownLatch = applyWaiter.latch;
                if (j <= 0) {
                    j = 5000;
                }
                countDownLatch.await(j, java.util.concurrent.TimeUnit.MILLISECONDS);
                sBuddyReqWaiter = null;
                if (applyWaiter.buddyReqInfo == null) {
                    com.tencent.qqlitepro.util.Log.w("好友申请: 内核未推送 onBuddyReqChange（超时）");
                    return null;
                }
                com.google.gson.JsonObject jsonObject = new com.google.gson.JsonObject();
                jsonObject.addProperty("unreadNums", java.lang.Integer.valueOf(intOf(fieldValue(applyWaiter.buddyReqInfo, "unreadNums"))));
                com.google.gson.JsonArray jsonArray = new com.google.gson.JsonArray();
                java.lang.Object objFieldValue = fieldValue(applyWaiter.buddyReqInfo, "buddyReqs");
                if (objFieldValue instanceof java.util.Collection) {
                    java.util.Iterator it = ((java.util.Collection) objFieldValue).iterator();
                    while (it.hasNext()) {
                        jsonArray.add(friendReqToJson(it.next()));
                    }
                }
                jsonObject.addProperty("total", java.lang.Integer.valueOf(jsonArray.size()));
                jsonObject.add("requests", jsonArray);
                return jsonObject;
            } catch (java.lang.Throwable thRethrow7) {
                sBuddyReqWaiter = null;
                throw thRethrow7;
            }
        } catch (java.lang.Throwable th2) {
            com.tencent.qqlitepro.util.Log.w("获取好友申请失败: " + chain(th2));
            return null;
        }
    }

    private static com.google.gson.JsonObject friendReqToJson(java.lang.Object obj) {
        com.google.gson.JsonObject jsonObject = new com.google.gson.JsonObject();
        jsonObject.addProperty("uid", strOf(fieldValue(obj, "friendUid")));
        jsonObject.addProperty("nick", strOf(fieldValue(obj, "friendNick")));
        jsonObject.addProperty("avatarUrl", strOf(fieldValue(obj, "friendAvatarUrl")));
        jsonObject.addProperty("verifyMsg", strOf(fieldValue(obj, "extWords")));
        jsonObject.addProperty("reqTime", java.lang.Long.valueOf(longOf(fieldValue(obj, "reqTime"))));
        int iIntOf = intOf(fieldValue(obj, "reqType"));
        jsonObject.addProperty("reqType", java.lang.Integer.valueOf(iIntOf));
        jsonObject.addProperty("reqTypeName", reqTypeName(iIntOf));
        jsonObject.addProperty("isBuddy", java.lang.Boolean.valueOf(boolOf(fieldValue(obj, "isBuddy"))));
        jsonObject.addProperty("isUnread", java.lang.Boolean.valueOf(boolOf(fieldValue(obj, "isUnread"))));
        jsonObject.addProperty("isDoubt", java.lang.Boolean.valueOf(boolOf(fieldValue(obj, "isDoubt"))));
        jsonObject.addProperty("isAgreed", java.lang.Boolean.valueOf(boolOf(fieldValue(obj, "isAgreed"))));
        jsonObject.addProperty("isInitiator", java.lang.Boolean.valueOf(boolOf(fieldValue(obj, "isInitiator"))));
        java.lang.Object objFieldValue = fieldValue(obj, "groupCode");
        jsonObject.addProperty("groupCode", java.lang.Long.valueOf(objFieldValue == null ? 0L : longOf(objFieldValue)));
        jsonObject.addProperty("sourceId", java.lang.Integer.valueOf(intOf(fieldValue(obj, "sourceId"))));
        jsonObject.addProperty("commFriendNum", java.lang.Integer.valueOf(intOf(fieldValue(obj, "commFriendNum"))));
        return jsonObject;
    }

    private static java.lang.String reqTypeName(int i) {
        try {
            java.lang.Object[] enumConstants = cls("com.tencent.qqnt.kernel.nativeinterface.ReqType").getEnumConstants();
            return (i < 0 || i >= enumConstants.length) ? "" : java.lang.String.valueOf(enumConstants[i]);
        } catch (java.lang.Throwable unused) {
            return "";
        }
    }

    private static java.lang.Object fetchBuddySetting(java.lang.Object obj, long j, java.lang.String str, long j2) {
        try {
            if (ensureBuddyListener(obj) == null) {
                return null;
            }
            java.lang.Class<?> cls = cls("com.tencent.qqnt.kernel.nativeinterface.ReqBuddySettingType");
            java.lang.Enum enumValueOf = java.lang.Enum.valueOf(cls.asSubclass(java.lang.Enum.class), "KBYUIN");
            java.lang.Class<?> cls2 = cls("com.tencent.qqnt.kernel.nativeinterface.IOperateCallback");
            java.lang.reflect.Method methodFindMethod = findMethod(obj.getClass(), "getTargetBuddySettingByType", java.lang.Long.TYPE, java.lang.String.class, cls, cls2);
            if (methodFindMethod == null) {
                com.tencent.qqlitepro.util.Log.w("好友申请: getTargetBuddySettingByType 未找到");
                return null;
            }
            com.tencent.qqlitepro.http.QqKernel.ApplyWaiter applyWaiter = new com.tencent.qqlitepro.http.QqKernel.ApplyWaiter();
            sBuddySettingWaiter = applyWaiter;
            try {
                methodFindMethod.setAccessible(true);
                java.lang.Object[] objArr = new java.lang.Object[4];
                objArr[0] = java.lang.Long.valueOf(j);
                objArr[1] = str == null ? "" : str;
                objArr[2] = enumValueOf;
                objArr[3] = loggingOperateCallback(cls2, "好友申请: 查设置");
                methodFindMethod.invoke(obj, objArr);
                applyWaiter.latch.await(j2 <= 0 ? 3000L : j2, java.util.concurrent.TimeUnit.MILLISECONDS);
                return applyWaiter.buddySetting;
            } finally {
                sBuddySettingWaiter = null;
            }
        } catch (java.lang.Throwable thRethrow7) {
            com.tencent.qqlitepro.util.Log.w("好友申请: 查询目标好友设置失败 " + chain(thRethrow7));
            return null;
        }
    }

    public static com.tencent.qqlitepro.http.QqKernel.ApplyResult sendFriendRequest(java.lang.String str, java.lang.String str2, java.lang.String str3, int i, java.lang.String str4, java.lang.String str5, int i2, int i3, long j) {
        int iIntOf;
        com.tencent.qqlitepro.http.QqKernel.ApplyResult applyResult = new com.tencent.qqlitepro.http.QqKernel.ApplyResult();
        try {
            java.lang.Object objKernelApiBuddyService = kernelApiBuddyService();
            if (objKernelApiBuddyService == null) {
                objKernelApiBuddyService = kernelBuddyService();
            }
            long longOr0 = parseLongOr0(str);
            java.lang.String strTrim = str2 == null ? "" : str2.trim();
            java.lang.String strTrim2 = str3 == null ? "" : str3.trim();
            if (longOr0 == 0 && strTrim.isEmpty()) {
                applyResult.msg = "缺少参数：uin 或 uid";
                return applyResult;
            }
            if (i < 0) {
                java.lang.Object objFetchBuddySetting = fetchBuddySetting(kernelBuddyService(), longOr0, strTrim, 3000L);
                if (objFetchBuddySetting != null) {
                    iIntOf = intOf(fieldValue(objFetchBuddySetting, "addFriendSetting"));
                    if (strTrim2.isEmpty()) {
                        strTrim2 = strOf(fieldValue(objFetchBuddySetting, "phoneNumber"));
                    }
                } else {
                    iIntOf = 0;
                }
            } else {
                iIntOf = i;
            }
            if (iIntOf == 2) {
                applyResult.msg = "对方已设置拒绝任何人添加（addFriendSetting=2）";
                return applyResult;
            }
            int i4 = i2 > 0 ? i2 : 3001;
            java.lang.Class<?> cls = cls("com.tencent.qqnt.kernel.nativeinterface.ReqToFriend");
            java.lang.Class<?> cls2 = cls("com.tencent.qqnt.kernel.nativeinterface.Verify");
            java.lang.Object objNewInstance = cls2.newInstance();
            java.lang.reflect.Constructor<?> constructor = cls.getConstructor(java.lang.Long.TYPE, java.lang.String.class, java.lang.String.class, java.lang.Integer.TYPE, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, cls2, java.lang.Integer.TYPE, java.lang.Integer.TYPE);
            java.lang.Object[] objArr = new java.lang.Object[11];
            objArr[0] = java.lang.Long.valueOf(longOr0);
            if (longOr0 > 0) {
                strTrim = null;
            }
            objArr[1] = strTrim;
            objArr[2] = strTrim2;
            objArr[3] = java.lang.Integer.valueOf(iIntOf);
            objArr[4] = str4 == null ? "" : str4;
            objArr[5] = str5 == null ? "" : str5;
            objArr[6] = 0;
            objArr[7] = "";
            objArr[8] = objNewInstance;
            objArr[9] = java.lang.Integer.valueOf(i4);
            objArr[10] = java.lang.Integer.valueOf(i3);
            java.lang.Object objNewInstance2 = constructor.newInstance(objArr);
            java.lang.reflect.Method methodFindMethodAny = findMethodAny(objKernelApiBuddyService.getClass(), cls, cls("com.tencent.qqnt.kernel.nativeinterface.IOperateCallback"), "f", "reqToAddFriends");
            java.lang.StringBuilder sb = new java.lang.StringBuilder("好友申请: svc=");
            sb.append(objKernelApiBuddyService.getClass().getName());
            sb.append(" method=");
            sb.append(methodFindMethodAny == null ? "null" : methodFindMethodAny.getName());
            sb.append(" uin=");
            sb.append(longOr0);
            sb.append(" setting=");
            sb.append(iIntOf);
            sb.append(" src=");
            sb.append(i4);
            sb.append(" phone=");
            sb.append(strTrim2.isEmpty() ? "(空)" : "(有)");
            com.tencent.qqlitepro.util.Log.i(sb.toString());
            if (methodFindMethodAny == null) {
                applyResult.msg = "内核要求方法未找到（reqToAddFriends/f）";
                return applyResult;
            }
            return awaitOperate(methodFindMethodAny, objKernelApiBuddyService, new java.lang.Object[]{objNewInstance2}, j, true);
        } catch (java.lang.Throwable thRethrow7) {
            applyResult.msg = "发送好友申请异常: " + chain(thRethrow7);
            return applyResult;
        }
    }

    private static java.lang.Object kernelApiBuddyService() {
        try {
            java.lang.reflect.Method methodFindMethod = findMethod(cls("com.tencent.qqnt.msg.KernelServiceUtil"), "a", new java.lang.Class[0]);
            if (methodFindMethod == null) {
                com.tencent.qqlitepro.util.Log.w("好友申请: KernelServiceUtil.a() 未找到");
                return null;
            }
            return methodFindMethod.invoke(null, new java.lang.Object[0]);
        } catch (java.lang.Throwable thRethrow7) {
            com.tencent.qqlitepro.util.Log.w("好友申请: 获取 IBuddyService 失败 " + chain(thRethrow7));
            return null;
        }
    }

    private static java.lang.reflect.Method findMethodAny(java.lang.Class<?> cls, java.lang.Class<?> cls2, java.lang.Class<?> cls3, java.lang.String... strArr) {
        java.util.ArrayList<java.lang.reflect.Method> arrayList = new java.util.ArrayList();
        while (cls != null && cls != java.lang.Object.class) {
            for (java.lang.reflect.Method method : cls.getDeclaredMethods()) {
                java.lang.Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length == 2 && parameterTypes[0] == cls2 && parameterTypes[1] == cls3) {
                    arrayList.add(method);
                }
            }
            cls = cls.getSuperclass();
        }
        for (java.lang.String str : strArr) {
            for (java.lang.reflect.Method method2 : arrayList) {
                if (str.equals(method2.getName())) {
                    method2.setAccessible(true);
                    return method2;
                }
            }
        }
        if (arrayList.isEmpty()) {
            return null;
        }
        java.lang.reflect.Method method3 = (java.lang.reflect.Method) arrayList.get(0);
        method3.setAccessible(true);
        return method3;
    }

    public static com.tencent.qqlitepro.http.QqKernel.ApplyResult approveFriendRequest(java.lang.String str, boolean z, java.lang.String str2, long j, long j2) {
        com.tencent.qqlitepro.http.QqKernel.ApplyResult applyResult = new com.tencent.qqlitepro.http.QqKernel.ApplyResult();
        if (str != null) {
            try {
                if (!str.trim().isEmpty()) {
                    java.lang.Object objKernelBuddyService = kernelBuddyService();
                    java.lang.Class<?> cls = cls("com.tencent.qqnt.kernel.nativeinterface.ApprovalBuddyRequest");
                    java.lang.Object objNewInstance = cls.newInstance();
                    de.robv.android.xposed.XposedHelpers.setObjectField(objNewInstance, "friendUid", str.trim());
                    de.robv.android.xposed.XposedHelpers.setBooleanField(objNewInstance, "accept", z);
                    if (str2 == null) {
                        str2 = "";
                    }
                    de.robv.android.xposed.XposedHelpers.setObjectField(objNewInstance, "refuseMsg", str2);
                    if (j <= 0) {
                        j = java.lang.System.currentTimeMillis();
                    }
                    de.robv.android.xposed.XposedHelpers.setLongField(objNewInstance, "reqTime", j);
                    java.lang.reflect.Method methodFindMethod = findMethod(objKernelBuddyService.getClass(), "approvalFriendRequest", cls, cls("com.tencent.qqnt.kernel.nativeinterface.IOperateCallback"));
                    if (methodFindMethod == null) {
                        applyResult.msg = "approvalFriendRequest(ApprovalBuddyRequest,IOperateCallback) 未找到";
                        return applyResult;
                    }
                    return awaitOperate(methodFindMethod, objKernelBuddyService, new java.lang.Object[]{objNewInstance}, j2);
                }
            } catch (java.lang.Throwable thRethrow7) {
                java.lang.StringBuilder sb = new java.lang.StringBuilder();
                sb.append(z ? "同意" : "拒绝");
                sb.append("好友申请异常: ");
                sb.append(chain(thRethrow7));
                applyResult.msg = sb.toString();
                return applyResult;
            }
        }
        applyResult.msg = "缺少参数：uid（申请人 uid）";
        return applyResult;
    }

    public static com.google.gson.JsonObject getGroupRequests(boolean z, long j, int i, long j2) {
        try {
            java.lang.Object objKernelGroupService = kernelGroupService();
            if (objKernelGroupService == null) {
                com.tencent.qqlitepro.util.Log.w("群申请: IKernelGroupService 为 null（内核未就绪）");
                return null;
            }
            if (ensureGroupNotifyListener(objKernelGroupService) == null) {
                return null;
            }
            java.lang.Class<?> cls = cls("com.tencent.qqnt.kernel.nativeinterface.IOperateCallback");
            java.lang.reflect.Method methodFindMethod = findMethod(objKernelGroupService.getClass(), "getSingleScreenNotifies", java.lang.Boolean.TYPE, java.lang.Long.TYPE, java.lang.Integer.TYPE, cls);
            if (methodFindMethod == null) {
                com.tencent.qqlitepro.util.Log.w("群申请: getSingleScreenNotifies 未找到");
                return null;
            }
            com.tencent.qqlitepro.http.QqKernel.ApplyWaiter applyWaiter = new com.tencent.qqlitepro.http.QqKernel.ApplyWaiter();
            sGroupNotifyWaiter = applyWaiter;
            try {
                methodFindMethod.setAccessible(true);
                java.lang.Object[] objArr = new java.lang.Object[4];
                objArr[0] = java.lang.Boolean.valueOf(z);
                objArr[1] = java.lang.Long.valueOf(j);
                objArr[2] = java.lang.Integer.valueOf(i <= 0 ? 14 : i);
                objArr[3] = loggingOperateCallback(cls, "群申请: getSingleScreenNotifies");
                methodFindMethod.invoke(objKernelGroupService, objArr);
                applyWaiter.latch.await(j2 <= 0 ? 5000L : j2, java.util.concurrent.TimeUnit.MILLISECONDS);
                sGroupNotifyWaiter = null;
                if (applyWaiter.groupNotifies == null) {
                    com.tencent.qqlitepro.util.Log.w("群申请: 内核未推送 onGroupSingleScreenNotifies（超时）");
                    return null;
                }
                com.google.gson.JsonObject jsonObject = new com.google.gson.JsonObject();
                com.google.gson.JsonArray jsonArray = new com.google.gson.JsonArray();
                if (applyWaiter.groupNotifies instanceof java.util.Collection) {
                    java.util.Iterator it = ((java.util.Collection) applyWaiter.groupNotifies).iterator();
                    while (it.hasNext()) {
                        jsonArray.add(groupNotifyToJson(it.next()));
                    }
                }
                jsonObject.addProperty("nextSeq", java.lang.Long.valueOf(applyWaiter.groupNextSeq));
                jsonObject.addProperty("total", java.lang.Integer.valueOf(jsonArray.size()));
                jsonObject.add("requests", jsonArray);
                return jsonObject;
            } catch (java.lang.Throwable thRethrow7) {
                sGroupNotifyWaiter = null;
                throw thRethrow7;
            }
        } catch (java.lang.Throwable th2) {
            com.tencent.qqlitepro.util.Log.w("获取加群申请失败: " + chain(th2));
            return null;
        }
    }

    private static com.google.gson.JsonObject groupNotifyToJson(java.lang.Object obj) {
        com.google.gson.JsonObject jsonObject = new com.google.gson.JsonObject();
        jsonObject.addProperty("seq", java.lang.Long.valueOf(longOf(fieldValue(obj, "seq"))));
        jsonObject.addProperty("actionTime", java.lang.Long.valueOf(longOf(fieldValue(obj, "actionTime"))));
        jsonObject.addProperty("postscript", strOf(fieldValue(obj, "postscript")));
        jsonObject.addProperty("warningTips", strOf(fieldValue(obj, "warningTips")));
        java.lang.Object objFieldValue = fieldValue(obj, "type");
        jsonObject.addProperty("type", java.lang.Integer.valueOf(enumOrdinal(objFieldValue)));
        jsonObject.addProperty("typeName", enumName(objFieldValue));
        java.lang.Object objFieldValue2 = fieldValue(obj, "status");
        jsonObject.addProperty("status", java.lang.Integer.valueOf(enumOrdinal(objFieldValue2)));
        jsonObject.addProperty("statusName", enumName(objFieldValue2));
        java.lang.Object objFieldValue3 = fieldValue(obj, "group");
        jsonObject.addProperty("groupCode", java.lang.Long.valueOf(objFieldValue3 == null ? 0L : longOf(fieldValue(objFieldValue3, "groupCode"))));
        jsonObject.addProperty("groupName", objFieldValue3 == null ? "" : strOf(fieldValue(objFieldValue3, "groupName")));
        jsonObject.add("applicant", notifyUserToJson(fieldValue(obj, "user1")));
        jsonObject.add("inviter", notifyUserToJson(fieldValue(obj, "user2")));
        jsonObject.add("actionUser", notifyUserToJson(fieldValue(obj, "actionUser")));
        return jsonObject;
    }

    private static com.google.gson.JsonObject notifyUserToJson(java.lang.Object obj) {
        com.google.gson.JsonObject jsonObject = new com.google.gson.JsonObject();
        jsonObject.addProperty("uid", strOf(fieldValue(obj, "uid")));
        jsonObject.addProperty("nick", strOf(fieldValue(obj, "nickName")));
        return jsonObject;
    }

    private static java.lang.Object groupNotifyMsgType(java.lang.String str) throws java.lang.Throwable {
        java.lang.String upperCase = str == null ? "" : str.trim().toUpperCase();
        if (upperCase.isEmpty()) {
            upperCase = "REQUESTJOINNEEDADMINISTRATORPASS";
        }
        return java.lang.Enum.valueOf(cls("com.tencent.qqnt.kernel.nativeinterface.GroupNotifyMsgType").asSubclass(java.lang.Enum.class), upperCase);
    }

    /* JADX WARN: Code duplicated, block: B:33:0x0073  */
    private static java.lang.Object groupNotifyOperateType(java.lang.String str) throws java.lang.Throwable {
        java.lang.String upperCase = str == null ? "" : str.trim().toUpperCase();
        if ("AGREE".equals(upperCase) || "ACCEPT".equals(upperCase) || "TRUE".equals(upperCase) || "1".equals(upperCase)) {
            upperCase = "KAGREE";
        } else if ("REFUSE".equals(upperCase) || "REJECT".equals(upperCase) || "FALSE".equals(upperCase) || "0".equals(upperCase)) {
            upperCase = "KREFUSE";
        } else if ("IGNORE".equals(upperCase)) {
            upperCase = "KIGNORE";
        } else if ("DELETE".equals(upperCase)) {
            upperCase = "KDELETE";
        } else if (!upperCase.startsWith("K")) {
            upperCase = "KAGREE";
        }
        return java.lang.Enum.valueOf(cls("com.tencent.qqnt.kernel.nativeinterface.GroupNotifyOperateType").asSubclass(java.lang.Enum.class), upperCase);
    }

    public static com.tencent.qqlitepro.http.QqKernel.ApplyResult operateGroupRequest(java.lang.String str, long j, java.lang.String str2, java.lang.String str3, java.lang.String str4, long j2) {
        com.tencent.qqlitepro.http.QqKernel.ApplyResult applyResult = new com.tencent.qqlitepro.http.QqKernel.ApplyResult();
        if (str != null) {
            try {
                if (!str.trim().isEmpty()) {
                    long j3 = str.trim().matches("\\d+") ? java.lang.Long.parseLong(str.trim()) : groupCodeOf(str.trim());
                    if (j3 == 0) {
                        applyResult.msg = "无法解析群号 groupCode（groupId=" + str + "）";
                        return applyResult;
                    }
                    java.lang.Object objKernelGroupService = kernelGroupService();
                    if (objKernelGroupService == null) {
                        applyResult.msg = "IKernelGroupService 为 null（内核未就绪）";
                        return applyResult;
                    }
                    java.lang.Object objNewInstance = cls("com.tencent.qqnt.kernel.nativeinterface.GroupNotifyTargetMsg").newInstance();
                    de.robv.android.xposed.XposedHelpers.setLongField(objNewInstance, "seq", j);
                    de.robv.android.xposed.XposedHelpers.setLongField(objNewInstance, "groupCode", j3);
                    de.robv.android.xposed.XposedHelpers.setObjectField(objNewInstance, "type", groupNotifyMsgType(str2));
                    if (str4 == null) {
                        str4 = "";
                    }
                    de.robv.android.xposed.XposedHelpers.setObjectField(objNewInstance, "postscript", str4);
                    java.lang.Class<?> cls = cls("com.tencent.qqnt.kernel.nativeinterface.GroupNotifyOperateMsg");
                    java.lang.Object objNewInstance2 = cls.newInstance();
                    de.robv.android.xposed.XposedHelpers.setObjectField(objNewInstance2, "operateType", groupNotifyOperateType(str3));
                    de.robv.android.xposed.XposedHelpers.setObjectField(objNewInstance2, "targetMsg", objNewInstance);
                    java.lang.reflect.Method methodFindMethod = findMethod(objKernelGroupService.getClass(), "operateSysNotify", java.lang.Boolean.TYPE, cls, cls("com.tencent.qqnt.kernel.nativeinterface.IOperateCallback"));
                    if (methodFindMethod != null) {
                        return awaitOperate(methodFindMethod, objKernelGroupService, new java.lang.Object[]{java.lang.Boolean.FALSE, objNewInstance2}, j2);
                    }
                    applyResult.msg = "operateSysNotify(boolean,GroupNotifyOperateMsg,IOperateCallback) 未找到";
                    return applyResult;
                }
            } catch (java.lang.Throwable thRethrow7) {
                applyResult.msg = "处理加群申请异常: " + chain(thRethrow7);
                return applyResult;
            }
        }
        applyResult.msg = "缺少参数：groupId";
        return applyResult;
    }

    public static com.tencent.qqlitepro.http.QqKernel.ApplyResult sendGroupRequest(java.lang.String str, java.lang.String str2, int i, int i2, long j) {
        com.tencent.qqlitepro.http.QqKernel.ApplyResult applyResult = new com.tencent.qqlitepro.http.QqKernel.ApplyResult();
        if (str != null) {
            try {
                if (!str.trim().isEmpty()) {
                    long j2 = str.trim().matches("\\d+") ? java.lang.Long.parseLong(str.trim()) : groupCodeOf(str.trim());
                    if (j2 == 0) {
                        applyResult.msg = "无法解析群号 groupCode（groupId=" + str + "）";
                        return applyResult;
                    }
                    java.lang.Object objKernelGroupService = kernelGroupService();
                    if (objKernelGroupService == null) {
                        applyResult.msg = "IKernelGroupService 为 null（内核未就绪）";
                        return applyResult;
                    }
                    java.lang.Class<?> cls = cls("com.tencent.qqnt.kernel.nativeinterface.ReqToGroup");
                    java.lang.Object objNewInstance = cls.newInstance();
                    de.robv.android.xposed.XposedHelpers.setLongField(objNewInstance, "groupCode", j2);
                    de.robv.android.xposed.XposedHelpers.setIntField(objNewInstance, "sourceId", i);
                    de.robv.android.xposed.XposedHelpers.setIntField(objNewInstance, "sourceSubId", i2);
                    if (str2 == null) {
                        str2 = "";
                    }
                    de.robv.android.xposed.XposedHelpers.setObjectField(objNewInstance, "applyMsg", str2);
                    de.robv.android.xposed.XposedHelpers.setObjectField(objNewInstance, "auth", "");
                    de.robv.android.xposed.XposedHelpers.setObjectField(objNewInstance, "token", "");
                    java.lang.reflect.Method methodFindMethod = findMethod(objKernelGroupService.getClass(), "reqToJoinGroup", cls, cls("com.tencent.qqnt.kernel.nativeinterface.IOperateCallback"));
                    if (methodFindMethod == null) {
                        applyResult.msg = "reqToJoinGroup(ReqToGroup,IOperateCallback) 未找到";
                        return applyResult;
                    }
                    return awaitOperate(methodFindMethod, objKernelGroupService, new java.lang.Object[]{objNewInstance}, j);
                }
            } catch (java.lang.Throwable thRethrow7) {
                applyResult.msg = "申请加群异常: " + chain(thRethrow7);
                return applyResult;
            }
        }
        applyResult.msg = "缺少参数：groupId";
        return applyResult;
    }


}
