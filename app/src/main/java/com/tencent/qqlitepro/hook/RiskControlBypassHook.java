package com.tencent.qqlitepro.hook;

/* JADX INFO: loaded from: classes.dex */
public class RiskControlBypassHook {
    private static final java.lang.String TURING_PHONE_SERVICE = "com.tencent.turingfd.sdk.xq.TuringPhoneService";
    private static final java.lang.String TURING_SERVICE = "com.tencent.turingfd.sdk.xq.TuringRiskService";
    private static final java.util.WeakHashMap<java.io.FileInputStream, java.lang.Boolean> sProcStatusStreams = new java.util.WeakHashMap<>();
    private static final java.util.Set<java.lang.String> ACCOUNT_KICK_TYPES = new com.tencent.qqlitepro.hook.RiskControlBypassHook.AnonymousClass7();

    public static void patch(de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam loadPackageParam) {
        java.lang.ClassLoader classLoader = loadPackageParam.classLoader;
        hookRootDetection(classLoader);
        hookDebugDetection(classLoader);
        hookEmulatorDetection(classLoader);
        hookForcedLogout(classLoader);
        hookTuringRisk(classLoader);
        com.tencent.qqlitepro.util.Log.i("[风控绕过] 所有反检测 hook 已注册");
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.RiskControlBypassHook$1, reason: invalid class name */
    static class AnonymousClass1 extends de.robv.android.xposed.XC_MethodHook {
        AnonymousClass1() {
        }

        protected void beforeHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            try {
                if (com.tencent.qqlitepro.hook.RiskControlBypassHook.isRootPath(((java.io.File) methodHookParam.thisObject).getPath())) {
                    methodHookParam.setResult(false);
                }
            } catch (java.lang.Throwable unused) {
            }
        }
    }

    private static void hookRootDetection(java.lang.ClassLoader classLoader) {
        try {
            de.robv.android.xposed.XposedHelpers.findAndHookMethod(java.io.File.class, "exists", new java.lang.Object[]{new com.tencent.qqlitepro.hook.RiskControlBypassHook.AnonymousClass1()});
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("[风控] hook File.exists 失败: " + th);
        }
        try {
            de.robv.android.xposed.XposedHelpers.findAndHookMethod(java.lang.Runtime.class, "exec", new java.lang.Object[]{java.lang.String.class, java.lang.String[].class, java.io.File.class, new com.tencent.qqlitepro.hook.RiskControlBypassHook.AnonymousClass2()});
        } catch (java.lang.Throwable th2) {
            com.tencent.qqlitepro.util.Log.w("[风控] hook Runtime.exec 失败: " + th2);
        }
        hookSystemProperties();
        try {
            de.robv.android.xposed.XposedHelpers.setStaticObjectField(android.os.Build.class, "TAGS", "release-keys");
        } catch (java.lang.Throwable unused) {
        }
        com.tencent.qqlitepro.util.Log.i("[风控] Root 检测绕过已启用");
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.RiskControlBypassHook$2, reason: invalid class name */
    static class AnonymousClass2 extends de.robv.android.xposed.XC_MethodHook {
        AnonymousClass2() {
        }

        protected void beforeHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            try {
                java.lang.String str = (java.lang.String) methodHookParam.args[0];
                if (str != null) {
                    if (str.contains("su") || str.contains("busybox") || str.contains("magisk") || str.contains("supersu")) {
                        methodHookParam.setThrowable(new java.lang.SecurityException("Permission denied"));
                    }
                }
            } catch (java.lang.Throwable unused) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isRootPath(java.lang.String str) {
        if (str == null) {
            return false;
        }
        java.lang.String lowerCase = str.toLowerCase();
        return lowerCase.contains("/su") || lowerCase.endsWith("su") || lowerCase.contains("busybox") || lowerCase.contains("magisk") || lowerCase.contains("supersu") || lowerCase.contains("superuser") || lowerCase.contains("daemonsu") || lowerCase.contains(".su");
    }

    private static void hookSystemProperties() {
        try {
            java.lang.Class clsFindClass = de.robv.android.xposed.XposedHelpers.findClass("android.os.SystemProperties", (java.lang.ClassLoader) null);
            de.robv.android.xposed.XposedHelpers.findAndHookMethod(clsFindClass, "get", new java.lang.Object[]{java.lang.String.class, new com.tencent.qqlitepro.hook.RiskControlBypassHook.AnonymousClass3()});
            de.robv.android.xposed.XposedHelpers.findAndHookMethod(clsFindClass, "get", new java.lang.Object[]{java.lang.String.class, java.lang.String.class, new com.tencent.qqlitepro.hook.RiskControlBypassHook.AnonymousClass4()});
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("[风控] hook SystemProperties 失败: " + th);
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.RiskControlBypassHook$3, reason: invalid class name */
    static class AnonymousClass3 extends de.robv.android.xposed.XC_MethodHook {
        AnonymousClass3() {
        }

        protected void afterHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            java.lang.String str = (java.lang.String) methodHookParam.args[0];
            if (str == null) {
                return;
            }
            java.lang.String lowerCase = str.toLowerCase();
            if (lowerCase.equals("ro.debuggable")) {
                methodHookParam.setResult("0");
                return;
            }
            if (lowerCase.equals("ro.secure")) {
                methodHookParam.setResult("1");
                return;
            }
            if (lowerCase.contains("qemu")) {
                methodHookParam.setResult("0");
                return;
            }
            if (!lowerCase.contains("ro.hardware") || methodHookParam.getResult() == null) {
                return;
            }
            java.lang.String lowerCase2 = methodHookParam.getResult().toString().toLowerCase();
            if (lowerCase2.contains("goldfish") || lowerCase2.contains("ranchu") || lowerCase2.contains("vbox")) {
                methodHookParam.setResult("qcom");
            }
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.RiskControlBypassHook$4, reason: invalid class name */
    static class AnonymousClass4 extends de.robv.android.xposed.XC_MethodHook {
        AnonymousClass4() {
        }

        protected void afterHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            java.lang.String str = (java.lang.String) methodHookParam.args[0];
            if (str == null) {
                return;
            }
            java.lang.String lowerCase = str.toLowerCase();
            if (lowerCase.equals("ro.debuggable")) {
                methodHookParam.setResult("0");
            } else if (lowerCase.equals("ro.secure")) {
                methodHookParam.setResult("1");
            } else if (lowerCase.contains("qemu")) {
                methodHookParam.setResult("0");
            }
        }
    }

    private static void hookDebugDetection(java.lang.ClassLoader classLoader) {
        try {
            de.robv.android.xposed.XposedHelpers.findAndHookMethod(android.os.Debug.class, "isDebuggerConnected", new java.lang.Object[]{de.robv.android.xposed.XC_MethodReplacement.returnConstant(false)});
        } catch (java.lang.Throwable unused) {
        }
        try {
            de.robv.android.xposed.XposedHelpers.findAndHookMethod(android.os.Debug.class, "waitingForDebugger", new java.lang.Object[]{de.robv.android.xposed.XC_MethodReplacement.returnConstant(false)});
        } catch (java.lang.Throwable unused2) {
        }
        try {
            java.lang.Class clsFindClass = de.robv.android.xposed.XposedHelpers.findClass("java.io.FileInputStream", classLoader);
            de.robv.android.xposed.XposedBridge.hookAllConstructors(clsFindClass, new com.tencent.qqlitepro.hook.RiskControlBypassHook.AnonymousClass5());
            de.robv.android.xposed.XposedBridge.hookAllMethods(clsFindClass, "read", new com.tencent.qqlitepro.hook.RiskControlBypassHook.AnonymousClass6());
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("[风控] hook /proc/self/status 失败: " + th);
        }
        com.tencent.qqlitepro.util.Log.i("[风控] Debug/Trace 检测绕过已启用");
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.RiskControlBypassHook$5, reason: invalid class name */
    static class AnonymousClass5 extends de.robv.android.xposed.XC_MethodHook {
        AnonymousClass5() {
        }

        protected void afterHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            java.lang.String path;
            try {
                if (methodHookParam.args != null && methodHookParam.args.length != 0) {
                    java.lang.Object obj = methodHookParam.args[0];
                    if (obj instanceof java.io.File) {
                        path = ((java.io.File) obj).getPath();
                    } else if (!(obj instanceof java.lang.String)) {
                        return;
                    } else {
                        path = (java.lang.String) obj;
                    }
                    if (path != null && path.contains("/proc/") && path.endsWith("/status")) {
                        com.tencent.qqlitepro.hook.RiskControlBypassHook.sProcStatusStreams.put((java.io.FileInputStream) methodHookParam.thisObject, true);
                    }
                }
            } catch (java.lang.Throwable unused) {
            }
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.RiskControlBypassHook$6, reason: invalid class name */
    static class AnonymousClass6 extends de.robv.android.xposed.XC_MethodHook {
        AnonymousClass6() {
        }

        protected void afterHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            int iIntValue;
            try {
                if (java.lang.Boolean.TRUE.equals(com.tencent.qqlitepro.hook.RiskControlBypassHook.sProcStatusStreams.get((java.io.FileInputStream) methodHookParam.thisObject))) {
                    java.lang.Object result = methodHookParam.getResult();
                    if ((result instanceof java.lang.Integer) && (iIntValue = ((java.lang.Integer) result).intValue()) > 0) {
                        byte[] bArr = (methodHookParam.args.length <= 0 || !(methodHookParam.args[0] instanceof byte[])) ? null : (byte[]) methodHookParam.args[0];
                        if (bArr == null) {
                            return;
                        }
                        java.lang.String str = new java.lang.String(bArr, 0, iIntValue, "UTF-8");
                        if (str.contains("TracerPid")) {
                            byte[] bytes = str.replaceAll("(?m)^TracerPid:\\s*\\d+", "TracerPid:\t0").getBytes("UTF-8");
                            java.lang.System.arraycopy(bytes, 0, bArr, 0, bytes.length);
                            methodHookParam.setResult(java.lang.Integer.valueOf(bytes.length));
                        }
                    }
                }
            } catch (java.lang.Throwable unused) {
            }
        }
    }

    private static void hookEmulatorDetection(java.lang.ClassLoader classLoader) {
        java.lang.String[][] strArr = {new java.lang.String[]{"FINGERPRINT", "Android/wt/watch:13/TQ3A.230805.001/1234567:user/release-keys"}, new java.lang.String[]{"HARDWARE", "qcom"}, new java.lang.String[]{"MODEL", "Watch"}, new java.lang.String[]{"MANUFACTURER", "Tencent"}, new java.lang.String[]{"BRAND", "Tencent"}, new java.lang.String[]{"DEVICE", "watch"}, new java.lang.String[]{"PRODUCT", "watch"}, new java.lang.String[]{"BOARD", "qcom"}, new java.lang.String[]{"BOOTLOADER", "unknown"}};
        for (int i = 0; i < 9; i++) {
            java.lang.String[] strArr2 = strArr[i];
            try {
                de.robv.android.xposed.XposedHelpers.setStaticObjectField(android.os.Build.class, strArr2[0], strArr2[1]);
            } catch (java.lang.Throwable unused) {
            }
        }
        com.tencent.qqlitepro.util.Log.i("[风控] 模拟器检测绕过已启用");
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.RiskControlBypassHook$7, reason: invalid class name */
    static class AnonymousClass7 extends java.util.HashSet<java.lang.String> {
        AnonymousClass7() {
            add("KKICKBYPASSWORDCHANGE");
            add("KKCIKBYLOWVERSION");
        }
    }

    /* JADX WARN: Code duplicated, block: B:23:0x0055 A[Catch: all -> 0x008e, TryCatch #2 {all -> 0x008e, blocks: (B:21:0x004d, B:23:0x0055, B:25:0x005c, B:27:0x006e, B:29:0x0072, B:31:0x0080), top: B:42:0x004d }] */
    /* JADX WARN: Code duplicated, block: B:25:0x005c A[Catch: all -> 0x008e, TryCatch #2 {all -> 0x008e, blocks: (B:21:0x004d, B:23:0x0055, B:25:0x005c, B:27:0x006e, B:29:0x0072, B:31:0x0080), top: B:42:0x004d }] */
    private static void hookForcedLogout(java.lang.ClassLoader classLoader) {
        int hooked = 0;
        try {
            java.lang.Class<?> listenerCls = de.robv.android.xposed.XposedHelpers.findClassIfExists(
                    "com.tencent.qqnt.kernel.api.impl.KickApiImpl$setupKickListen$listener$1", classLoader);
            if (listenerCls != null) {
                for (java.lang.reflect.Method m : listenerCls.getDeclaredMethods()) {
                    if ("onKickedOffLine".equals(m.getName()) && m.getParameterTypes().length == 1) {
                        de.robv.android.xposed.XposedBridge.hookMethod(m, buildKickHook("[踢线] 监听器"));
                        hooked++;
                    }
                }
            }
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("[风控] hook 踢线监听器失败: " + th);
        }
        try {
            java.lang.Class<?> processorCls = de.robv.android.xposed.XposedHelpers.findClassIfExists(
                    "com.tencent.qqnt.account.kick.api.impl.KickProcessorApiImpl$WatchKickProcessor", classLoader);
            if (processorCls != null) {
                for (java.lang.reflect.Method m2 : processorCls.getDeclaredMethods()) {
                    java.lang.Class<?>[] parameterTypes = m2.getParameterTypes();
                    if ("a".equals(m2.getName()) && parameterTypes.length == 2
                            && "KickedInfo".equals(parameterTypes[1].getSimpleName())) {
                        de.robv.android.xposed.XposedBridge.hookMethod(m2, buildKickHook("[踢线] 处理器"));
                        hooked++;
                    }
                }
            }
        } catch (java.lang.Throwable th2) {
            com.tencent.qqlitepro.util.Log.w("[风控] hook 踢线处理器失败: " + th2);
        }
        com.tencent.qqlitepro.util.Log.i("[风控] 服务器踢线拦截已注册: " + hooked + " 个方法");
        com.tencent.qqlitepro.util.Log.i("[风控] 服务器踢线拦截已启用（仅拦截风控检测下线，账号类正常下线放行）");
    }

    public static void patchMsfProcess(de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam loadPackageParam) {
        java.lang.ClassLoader classLoader = loadPackageParam.classLoader;
        hookGrayRoleCheck(classLoader);
        hookMsfAccountNoLogin(classLoader);
    }

    private static void hookMsfAccountNoLogin(java.lang.ClassLoader classLoader) {
        try {
            java.lang.Class clsFindClassIfExists = de.robv.android.xposed.XposedHelpers.findClassIfExists("com.tencent.mobileqq.msf.core.auth.b", classLoader);
            if (clsFindClassIfExists != null) {
                for (java.lang.reflect.Method method : clsFindClassIfExists.getDeclaredMethods()) {
                    java.lang.Class<?>[] parameterTypes = method.getParameterTypes();
                    if ("n".equals(method.getName()) && parameterTypes.length == 1 && parameterTypes[0] == java.lang.String.class) {
                        de.robv.android.xposed.XposedBridge.hookMethod(method, new com.tencent.qqlitepro.hook.RiskControlBypassHook.AnonymousClass8());
                    }
                }
            }
        } catch (java.lang.Throwable unused) {
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.RiskControlBypassHook$8, reason: invalid class name */
    static class AnonymousClass8 extends de.robv.android.xposed.XC_MethodHook {
        AnonymousClass8() {
        }

        protected void beforeHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            com.tencent.qqlitepro.util.Log.w("[踢线] setAccountNoLogin(msf) 执行 uin=" + methodHookParam.args[0]);
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.RiskControlBypassHook$9, reason: invalid class name */
    static class AnonymousClass9 extends de.robv.android.xposed.XC_MethodHook {
        final /* synthetic */ java.lang.String val$tag;

        AnonymousClass9(java.lang.String str) {
            this.val$tag = str;
        }

        protected void beforeHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            java.lang.Object obj;
            int intField;
            try {
                boolean z = false;
                if (methodHookParam.args == null) {
                    return;
                }
                java.lang.Object[] objArr = methodHookParam.args;
                int length = objArr.length;
                int i = 0;
                while (true) {
                    if (i < length) {
                        obj = objArr[i];
                        if (obj != null && "KickedInfo".equals(obj.getClass().getSimpleName())) {
                            break;
                        } else {
                            i++;
                        }
                    } else {
                        obj = null;
                        break;
                    }
                }
                if (obj == null) {
                    return;
                }
                java.lang.String string = "null";
                try {
                    java.lang.Object objectField = de.robv.android.xposed.XposedHelpers.getObjectField(obj, "kickedType");
                    if (objectField != null) {
                        string = objectField.toString();
                    }
                } catch (java.lang.Throwable unused) {
                }
                try {
                    intField = de.robv.android.xposed.XposedHelpers.getIntField(obj, "securityKickedType");
                } catch (java.lang.Throwable unused2) {
                    intField = 0;
                }
                if (!com.tencent.qqlitepro.hook.RiskControlBypassHook.ACCOUNT_KICK_TYPES.contains(string) && intField != 0) {
                    z = true;
                }
                java.lang.StringBuilder sb = new java.lang.StringBuilder();
                sb.append(this.val$tag);
                sb.append(" type=");
                sb.append(string);
                sb.append(" securityType=");
                sb.append(intField);
                sb.append(" tips=");
                sb.append(com.tencent.qqlitepro.hook.RiskControlBypassHook.tips(obj));
                sb.append(" -> ");
                sb.append(z ? "拦截(风控检测下线)" : "放行(正常下线)");
                com.tencent.qqlitepro.util.Log.w(sb.toString());
                if (z) {
                    methodHookParam.setResult((java.lang.Object) null);
                }
            } catch (java.lang.Throwable thRethrow2) {
                com.tencent.qqlitepro.util.Log.w(this.val$tag + " 判定异常，放行: " + thRethrow2);
            }
        }
    }

    private static de.robv.android.xposed.XC_MethodHook buildKickHook(java.lang.String str) {
        return new com.tencent.qqlitepro.hook.RiskControlBypassHook.AnonymousClass9(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.lang.String tips(java.lang.Object obj) {
        try {
            java.lang.Object objectField = de.robv.android.xposed.XposedHelpers.getObjectField(obj, "tipsTitle");
            return objectField == null ? "" : objectField.toString();
        } catch (java.lang.Throwable unused) {
            return "";
        }
    }

    private static void hookGrayRoleCheck(java.lang.ClassLoader classLoader) {
        int i = 0;
        try {
            java.lang.Class clsFindClassIfExists = de.robv.android.xposed.XposedHelpers.findClassIfExists("com.tencent.mobileqq.msf.core.r", classLoader);
            if (clsFindClassIfExists != null) {
                int i2 = 0;
                for (java.lang.reflect.Method method : clsFindClassIfExists.getDeclaredMethods()) {
                    try {
                        if ("a".equals(method.getName())) {
                            java.lang.Class<?>[] parameterTypes = method.getParameterTypes();
                            if (parameterTypes.length == 1 && "FromServiceMsg".equals(parameterTypes[0].getSimpleName())) {
                                de.robv.android.xposed.XposedBridge.hookMethod(method, new com.tencent.qqlitepro.hook.RiskControlBypassHook.AnonymousClass10());
                                i2++;
                            }
                            if (parameterTypes.length == 2 && "GrayUinCheckResp".equals(parameterTypes[0].getSimpleName())) {
                                de.robv.android.xposed.XposedBridge.hookMethod(method, new com.tencent.qqlitepro.hook.RiskControlBypassHook.AnonymousClass11());
                                i2++;
                            }
                        }
                    } catch (java.lang.Throwable thRethrow2) {
                        i = i2;
                        com.tencent.qqlitepro.util.Log.w("[风控] hook 灰度角色检查失败: " + thRethrow2);
                        com.tencent.qqlitepro.util.Log.i("[风控] 灰度角色风控已关闭: " + i + " 个方法");
                    }
                }
                i = i2;
            }
        } catch (java.lang.Throwable th2) {
            java.lang.Throwable thRethrow3 = th2;
        }
        com.tencent.qqlitepro.util.Log.i("[风控] 灰度角色风控已关闭: " + i + " 个方法");
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.RiskControlBypassHook$10, reason: invalid class name */
    static class AnonymousClass10 extends de.robv.android.xposed.XC_MethodHook {
        AnonymousClass10() {
        }

        protected void beforeHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            try {
                java.lang.Object objCallMethod = de.robv.android.xposed.XposedHelpers.callMethod(methodHookParam.args[0], "getServiceCmd", new java.lang.Object[0]);
                java.lang.String string = objCallMethod == null ? "" : objCallMethod.toString();
                if (string.contains("GrayUin")) {
                    com.tencent.qqlitepro.util.Log.w("[风控] 拦截灰度角色检查(响应解析) cmd=" + string);
                    methodHookParam.setResult((java.lang.Object) null);
                }
            } catch (java.lang.Throwable unused) {
            }
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.RiskControlBypassHook$11, reason: invalid class name */
    static class AnonymousClass11 extends de.robv.android.xposed.XC_MethodHook {
        AnonymousClass11() {
        }

        protected void beforeHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            com.tencent.qqlitepro.util.Log.w("[风控] 拦截灰度角色检查(上报 PROCESS_QQ)");
            methodHookParam.setResult((java.lang.Object) null);
        }
    }

    /* JADX WARN: Code duplicated, block: B:21:0x004c A[Catch: all -> 0x006e, TryCatch #2 {all -> 0x006e, blocks: (B:19:0x0044, B:21:0x004c, B:23:0x0053, B:25:0x0061), top: B:35:0x0044 }] */
    /* JADX WARN: Code duplicated, block: B:23:0x0053 A[Catch: all -> 0x006e, TryCatch #2 {all -> 0x006e, blocks: (B:19:0x0044, B:21:0x004c, B:23:0x0053, B:25:0x0061), top: B:35:0x0044 }] */
    /* JADX WARN: Code duplicated, block: B:25:0x0061 A[Catch: all -> 0x006e, TRY_LEAVE, TryCatch #2 {all -> 0x006e, blocks: (B:19:0x0044, B:21:0x004c, B:23:0x0053, B:25:0x0061), top: B:35:0x0044 }] */
    /* JADX WARN: Code duplicated, block: B:41:0x006b A[SYNTHETIC] */
    /* JADX WARN: Instruction removed from duplicated block: B:21:0x004c, please report this as an issue */
    private static void hookTuringRisk(java.lang.ClassLoader classLoader) {
        int hooked = 0;
        try {
            java.lang.Class<?> phoneCls = de.robv.android.xposed.XposedHelpers.findClassIfExists(TURING_PHONE_SERVICE, classLoader);
            if (phoneCls != null) {
                for (java.lang.reflect.Method m : phoneCls.getDeclaredMethods()) {
                    if ("reqPhoneToken".equals(m.getName())) {
                        de.robv.android.xposed.XposedBridge.hookMethod(m, new com.tencent.qqlitepro.hook.RiskControlBypassHook.AnonymousClass12(classLoader));
                        hooked++;
                    }
                }
            }
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("[风控] hook TuringPhoneService 失败: " + th);
        }
        try {
            java.lang.Class<?> riskCls = de.robv.android.xposed.XposedHelpers.findClassIfExists(TURING_SERVICE, classLoader);
            if (riskCls != null) {
                for (java.lang.reflect.Method m2 : riskCls.getDeclaredMethods()) {
                    if (m2.getName().startsWith("reqRiskDetect")) {
                        de.robv.android.xposed.XposedBridge.hookMethod(m2, new com.tencent.qqlitepro.hook.RiskControlBypassHook.AnonymousClass13());
                        hooked++;
                    }
                }
            }
        } catch (java.lang.Throwable th2) {
            // 原实现此处不记录日志
        }
        com.tencent.qqlitepro.util.Log.i("[风控] 图灵风控已关闭: " + hooked + " 个方法");
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.RiskControlBypassHook$12, reason: invalid class name */
    static class AnonymousClass12 extends de.robv.android.xposed.XC_MethodHook {
        final /* synthetic */ java.lang.ClassLoader val$cl;

        AnonymousClass12(java.lang.ClassLoader classLoader) {
            this.val$cl = classLoader;
        }

        protected void beforeHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            java.lang.Object objEmptyPhoneResp = com.tencent.qqlitepro.hook.RiskControlBypassHook.emptyPhoneResp(this.val$cl);
            if (objEmptyPhoneResp != null) {
                com.tencent.qqlitepro.util.Log.w("[风控] 拦截图灵设备指纹 reqPhoneToken -> 空 token");
                methodHookParam.setResult(objEmptyPhoneResp);
            }
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.RiskControlBypassHook$13, reason: invalid class name */
    static class AnonymousClass13 extends de.robv.android.xposed.XC_MethodHook {
        AnonymousClass13() {
        }

        protected void beforeHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            java.lang.Class<?> returnType = ((java.lang.reflect.Method) methodHookParam.method).getReturnType();
            if (returnType == java.lang.Boolean.TYPE) {
                methodHookParam.setResult(false);
                return;
            }
            if (returnType == java.lang.Integer.TYPE) {
                methodHookParam.setResult(0);
            } else if (returnType == java.lang.Long.TYPE) {
                methodHookParam.setResult(0L);
            } else {
                methodHookParam.setResult((java.lang.Object) null);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.lang.Object emptyPhoneResp(java.lang.ClassLoader classLoader) {
        try {
            return java.lang.reflect.Proxy.newProxyInstance(classLoader, new java.lang.Class[]{de.robv.android.xposed.XposedHelpers.findClass("com.tencent.turingfd.sdk.xq.PhoneResp", classLoader)}, new com.tencent.qqlitepro.hook.RiskControlBypassHook$$ExternalSyntheticLambda0());
        } catch (java.lang.Throwable unused) {
            return null;
        }
    }

    static /* synthetic */ java.lang.Object lambda$emptyPhoneResp$0(java.lang.Object obj, java.lang.reflect.Method method, java.lang.Object[] objArr) throws java.lang.Throwable {
        java.lang.Class<?> returnType = method.getReturnType();
        if (returnType == java.lang.String.class) {
            return "";
        }
        if (returnType == java.lang.Long.TYPE) {
            return 0L;
        }
        if (returnType == java.lang.Integer.TYPE) {
            return 0;
        }
        return returnType == java.lang.Boolean.TYPE ? false : null;
    }
}
