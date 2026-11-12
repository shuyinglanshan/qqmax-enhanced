package com.tencent.qqlitepro.hook;

/* JADX INFO: loaded from: classes.dex */
public final class FakeMediaReceiver {
    private static final java.lang.String CLS_EXT_KIT = "defpackage.WatchPicElementExtKt";
    private static final java.lang.String CLS_FILE_PATH_INFO = "com.tencent.qqnt.kernel.nativeinterface.RichMediaFilePathInfo";
    private static final java.lang.String CLS_KERNEL_SVC_UTIL = "com.tencent.qqnt.msg.KernelServiceUtil";
    private static final java.lang.String CLS_RICH_REQ = "com.tencent.qqnt.kernel.nativeinterface.RichMediaElementGetReq";
    private static final java.util.Set<java.lang.Long> REQUESTED = java.util.Collections.newSetFromMap(new java.util.concurrent.ConcurrentHashMap());
    public static final int TYPE_PIC = 2;
    public static final int TYPE_PTT = 4;
    private static volatile java.lang.ClassLoader sCl;

    public static void init(java.lang.ClassLoader classLoader) {
        sCl = classLoader;
    }

    private FakeMediaReceiver() {
    }

    public static java.lang.String picOriginalPath(java.lang.Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            java.lang.Object objInvoke = java.lang.Class.forName(CLS_EXT_KIT, false, loaderOf(obj)).getMethod("C0", obj.getClass()).invoke(null, obj);
            if ((objInvoke instanceof java.lang.String) && !((java.lang.String) objInvoke).isEmpty()) {
                return (java.lang.String) objInvoke;
            }
        } catch (java.lang.Throwable unused) {
        }
        return assemble(2, intOf(obj, "picSubType"), strOf(obj, "md5HexStr"), strOf(obj, "fileName"), 1, 0, bytesOf(obj, "importRichMediaContext"), strOf(obj, "fileUuid"));
    }

    public static java.lang.String pttPath(java.lang.Object obj) {
        java.lang.String strAssemble;
        if (obj == null) {
            return null;
        }
        java.lang.String strStrOf = strOf(obj, "filePath");
        return ((strStrOf != null && !strStrOf.isEmpty()) || (strAssemble = assemble(4, 3, strOf(obj, "md5HexStr"), strOf(obj, "fileName"), 1, 0, bytesOf(obj, "importRichMediaContext"), strOf(obj, "fileUuid"))) == null || strAssemble.isEmpty()) ? strStrOf : strAssemble;
    }

    public static void plusPicThumbPaths(java.lang.Object obj, java.util.List<java.lang.String> list) {
        if (obj == null || list == null) {
            return;
        }
        try {
            java.lang.reflect.Member memberField = field(obj, "thumbPath");
            if (memberField instanceof java.util.Map) {
                for (java.lang.Object obj2 : ((java.util.Map) memberField).values()) {
                    if ((obj2 instanceof java.lang.String) && !((java.lang.String) obj2).isEmpty() && !list.contains(obj2)) {
                        list.add((java.lang.String) obj2);
                    }
                }
            }
        } catch (java.lang.Throwable unused) {
        }
    }

    private static java.lang.String assemble(int i, int i2, java.lang.String str, java.lang.String str2, int i3, int i4, byte[] bArr, java.lang.String str3) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        try {
            java.lang.Object objKernelService = kernelService();
            if (objKernelService == null) {
                return null;
            }
            java.lang.Class<?> cls = java.lang.Class.forName(CLS_FILE_PATH_INFO, false, loaderOf(objKernelService));
            java.lang.reflect.Constructor<?> constructor = cls.getConstructor(java.lang.Integer.TYPE, java.lang.Integer.TYPE, java.lang.String.class, java.lang.String.class, java.lang.Integer.TYPE, java.lang.Integer.TYPE, byte[].class, java.lang.String.class, java.lang.Boolean.TYPE);
            java.lang.Object[] objArr = new java.lang.Object[9];
            objArr[0] = java.lang.Integer.valueOf(i);
            objArr[1] = java.lang.Integer.valueOf(i2);
            objArr[2] = str;
            java.lang.String str4 = "";
            objArr[3] = str2 == null ? "" : str2;
            objArr[4] = java.lang.Integer.valueOf(i3);
            objArr[5] = java.lang.Integer.valueOf(i4);
            objArr[6] = bArr;
            if (str3 != null) {
                str4 = str3;
            }
            objArr[7] = str4;
            objArr[8] = false;
            java.lang.Object objNewInstance = constructor.newInstance(objArr);
            java.lang.reflect.Method methodFindMethod = findMethod(objKernelService.getClass(), "assembleMobileQQRichMediaFilePath", cls);
            if (methodFindMethod == null) {
                return null;
            }
            java.lang.Object objInvoke = methodFindMethod.invoke(objKernelService, objNewInstance);
            if (objInvoke instanceof java.lang.String) {
                return (java.lang.String) objInvoke;
            }
            return null;
        } catch (java.lang.Throwable unused) {
            return null;
        }
    }

    private static java.lang.Object kernelService() {
        try {
            java.lang.reflect.Method method = java.lang.Class.forName(CLS_KERNEL_SVC_UTIL, false, cl()).getMethod("c", new java.lang.Class[0]);
            method.setAccessible(true);
            return method.invoke(null, new java.lang.Object[0]);
        } catch (java.lang.Throwable unused) {
            return null;
        }
    }

    public static boolean requestOriginal(java.lang.Object obj, java.lang.Object obj2, boolean z) {
        if (obj != null && obj2 != null) {
            long jLongOf = longOf(obj2, "elementId");
            if (jLongOf != 0 && !REQUESTED.add(java.lang.Long.valueOf(jLongOf))) {
                return false;
            }
            try {
                java.lang.Object objKernelService = kernelService();
                if (objKernelService == null) {
                    return false;
                }
                java.lang.Class<?> cls = java.lang.Class.forName(CLS_RICH_REQ, false, loaderOf(objKernelService));
                java.lang.Object objNewInstance = cls.newInstance();
                set(objNewInstance, "msgId", java.lang.Long.valueOf(longOf(obj, "msgId")));
                set(objNewInstance, "peerUid", strOf(obj, "peerUid"));
                set(objNewInstance, "chatType", java.lang.Integer.valueOf(intOf(obj, "chatType")));
                set(objNewInstance, "elementId", java.lang.Long.valueOf(jLongOf));
                set(objNewInstance, "downloadType", 1);
                set(objNewInstance, "thumbSize", 0);
                set(objNewInstance, "downSourceType", 1);
                set(objNewInstance, "triggerType", java.lang.Integer.valueOf(z ? 1 : 0));
                java.lang.reflect.Method methodFindMethod = findMethod(objKernelService.getClass(), "getRichMediaElement", cls);
                if (methodFindMethod == null) {
                    return false;
                }
                methodFindMethod.invoke(objKernelService, objNewInstance);
                com.tencent.qqlitepro.util.Log.i("[伪媒体] 已下发接收端原文件下载 elementId=" + jLongOf + " ptt=" + z);
                return true;
            } catch (java.lang.Throwable th) {
                com.tencent.qqlitepro.util.Log.w("[伪媒体] 原文件下载请求失败: " + th);
            }
        }
        return false;
    }

    public static java.lang.Object elementOfType(java.lang.Object obj, int i) {
        for (java.lang.Object obj2 : elementsOf(obj)) {
            if (obj2 != null && intOf(obj2, "elementType") == i) {
                return obj2;
            }
        }
        return null;
    }

    private static java.util.List<java.lang.Object> elementsOf(java.lang.Object obj) {
        java.util.ArrayList arrayList = new java.util.ArrayList();
        if (obj == null) {
            return arrayList;
        }
        java.lang.String[] strArr = {"elements", "msgElements"};
        for (int i = 0; i < 2; i++) {
            try {
                java.lang.reflect.Member memberField = field(obj, strArr[i]);
                if (memberField instanceof java.lang.Iterable) {
                    java.util.Iterator it = ((java.lang.Iterable) memberField).iterator();
                    while (it.hasNext()) {
                        arrayList.add(it.next());
                    }
                } else if (memberField != null && memberField.getClass().isArray()) {
                    int length = java.lang.reflect.Array.getLength(memberField);
                    for (int i2 = 0; i2 < length; i2++) {
                        arrayList.add(java.lang.reflect.Array.get(memberField, i2));
                    }
                }
            } catch (java.lang.Throwable unused) {
            }
        }
        return arrayList;
    }

    private static java.lang.ClassLoader cl() {
        java.lang.ClassLoader classLoader = sCl;
        return classLoader != null ? classLoader : com.tencent.qqlitepro.hook.FakeMediaReceiver.class.getClassLoader();
    }

    private static java.lang.ClassLoader loaderOf(java.lang.Object obj) {
        java.lang.ClassLoader classLoader = obj == null ? null : obj.getClass().getClassLoader();
        return classLoader != null ? classLoader : cl();
    }

    private static java.lang.reflect.Field field(java.lang.Object obj, java.lang.String str) {
        if (obj == null) {
            return null;
        }
        for (java.lang.Class<?> superclass = obj.getClass(); superclass != null && superclass != java.lang.Object.class; superclass = superclass.getSuperclass()) {
            try {
                java.lang.reflect.Field declaredField = superclass.getDeclaredField(str);
                declaredField.setAccessible(true);
                return declaredField;
            } catch (java.lang.Throwable unused) {
            }
        }
        return null;
    }

    private static java.lang.reflect.Method findMethod(java.lang.Class<?> cls, java.lang.String str, java.lang.Class<?>... clsArr) {
        if (cls == null) {
            return null;
        }
        for (java.lang.Class<?> superclass = cls; superclass != null && superclass != java.lang.Object.class; superclass = superclass.getSuperclass()) {
            try {
                java.lang.reflect.Method declaredMethod = superclass.getDeclaredMethod(str, clsArr);
                declaredMethod.setAccessible(true);
                return declaredMethod;
            } catch (java.lang.Throwable unused) {
            }
        }
        for (java.lang.Class<?> cls2 : cls.getInterfaces()) {
            try {
                java.lang.reflect.Method method = cls2.getMethod(str, clsArr);
                method.setAccessible(true);
                return method;
            } catch (java.lang.Throwable unused2) {
            }
        }
        try {
            java.lang.reflect.Method method2 = cls.getMethod(str, clsArr);
            method2.setAccessible(true);
            return method2;
        } catch (java.lang.Throwable unused3) {
            return null;
        }
    }

    private static void set(java.lang.Object obj, java.lang.String str, java.lang.Object obj2) {
        try {
            java.lang.reflect.Field field = field(obj, str);
            if (field != null) {
                field.set(obj, obj2);
            }
        } catch (java.lang.Throwable unused) {
        }
    }

    private static java.lang.String strOf(java.lang.Object obj, java.lang.String str) {
        try {
            java.lang.Object obj2 = field(obj, str) == null ? null : field(obj, str).get(obj);
            if (obj2 instanceof java.lang.String) {
                return (java.lang.String) obj2;
            }
            return null;
        } catch (java.lang.Throwable unused) {
            return null;
        }
    }

    private static int intOf(java.lang.Object obj, java.lang.String str) {
        try {
            java.lang.reflect.Field field = field(obj, str);
            java.lang.Object obj2 = field == null ? null : field.get(obj);
            if (obj2 instanceof java.lang.Number) {
                return ((java.lang.Number) obj2).intValue();
            }
            return 0;
        } catch (java.lang.Throwable unused) {
            return 0;
        }
    }

    private static long longOf(java.lang.Object obj, java.lang.String str) {
        try {
            java.lang.reflect.Field field = field(obj, str);
            java.lang.Object obj2 = field == null ? null : field.get(obj);
            if (obj2 instanceof java.lang.Number) {
                return ((java.lang.Number) obj2).longValue();
            }
            return 0L;
        } catch (java.lang.Throwable unused) {
            return 0L;
        }
    }

    private static byte[] bytesOf(java.lang.Object obj, java.lang.String str) {
        try {
            java.lang.reflect.Field field = field(obj, str);
            java.lang.Object obj2 = field == null ? null : field.get(obj);
            if (obj2 instanceof byte[]) {
                return (byte[]) obj2;
            }
            return null;
        } catch (java.lang.Throwable unused) {
            return null;
        }
    }
}
