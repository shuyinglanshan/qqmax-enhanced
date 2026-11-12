package com.tencent.qqlitepro.hook;

/* JADX INFO: loaded from: classes.dex */
public final class QzoneDynamicHook {
    private static final java.lang.String ADAPTER = "com.tencent.watch.qzone_impl.frame.QZoneFeedAdapter";
    private static final java.lang.String FEED_DATA = "com.tencent.watch.qzone_impl.feed.model.BusinessFeedData";
    private static final java.lang.String FEED_SERVICE = "com.tencent.watch.qzone_impl.feed.QZoneFeedService";
    private static final java.lang.String MAIN_FRAME = "com.tencent.watch.qzone_impl.frame.QZoneMainFrame";
    private static final java.lang.String MINE_FRAGMENT = "com.tencent.watch.qzone_impl.frame.QZoneMineFragment";
    private static volatile boolean sDiagnosed = false;
    private static volatile boolean sInstalled = false;

    public static boolean isInstalled() {
        return sInstalled;
    }

    private QzoneDynamicHook() {
    }

    public static void patch(java.lang.ClassLoader classLoader) {
        int i;
        if (sInstalled) {
            return;
        }
        try {
            de.robv.android.xposed.XposedHelpers.findAndHookMethod(de.robv.android.xposed.XposedHelpers.findClass(FEED_SERVICE, classLoader), "b", new java.lang.Object[]{java.util.List.class, java.lang.Boolean.TYPE, java.lang.Boolean.TYPE, java.lang.Boolean.TYPE, java.lang.Integer.TYPE, new com.tencent.qqlitepro.hook.QzoneDynamicHook.AnonymousClass1()});
            i = 1;
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("QQ动态: hook 全局数据回调 QZoneFeedService.b 失败 " + th);
            i = 0;
        }
        java.lang.String[] strArr = {MAIN_FRAME, MINE_FRAGMENT};
        for (int i2 = 0; i2 < 2; i2++) {
            java.lang.String str = strArr[i2];
            try {
                java.lang.Class clsFindClass = de.robv.android.xposed.XposedHelpers.findClass(str, classLoader);
                com.tencent.qqlitepro.hook.QzoneDynamicHook.AnonymousClass2 anonymousClass2 = new com.tencent.qqlitepro.hook.QzoneDynamicHook.AnonymousClass2();
                try {
                    de.robv.android.xposed.XposedHelpers.findAndHookMethod(clsFindClass, "O_0", new java.lang.Object[]{java.util.List.class, java.lang.Boolean.TYPE, anonymousClass2});
                } catch (java.lang.Throwable unused) {
                    de.robv.android.xposed.XposedHelpers.findAndHookMethod(clsFindClass, "O", new java.lang.Object[]{java.util.List.class, java.lang.Boolean.TYPE, anonymousClass2});
                }
                i++;
            } catch (java.lang.Throwable th2) {
                com.tencent.qqlitepro.util.Log.w("QQ动态: hook 动态 Fragment 列表失败 " + str + " " + th2);
            }
        }
        try {
            java.lang.Class clsFindClass2 = de.robv.android.xposed.XposedHelpers.findClass("androidx.recyclerview.widget.RecyclerView$ViewHolder", classLoader);
            java.lang.Class clsFindClass3 = de.robv.android.xposed.XposedHelpers.findClass(ADAPTER, classLoader);
            de.robv.android.xposed.XposedHelpers.findAndHookMethod(clsFindClass3, "onBindViewHolder", new java.lang.Object[]{clsFindClass2, java.lang.Integer.TYPE, new com.tencent.qqlitepro.hook.QzoneDynamicHook.AnonymousClass3()});
            try {
                de.robv.android.xposed.XposedHelpers.findAndHookMethod(clsFindClass3, "onBindViewHolder", new java.lang.Object[]{clsFindClass2, java.lang.Integer.TYPE, java.util.List.class, new com.tencent.qqlitepro.hook.QzoneDynamicHook.AnonymousClass4()});
            } catch (java.lang.Throwable unused2) {
            }
            i++;
        } catch (java.lang.Throwable th3) {
            com.tencent.qqlitepro.util.Log.e("QQ动态: hook 动态列表适配器失败 " + th3);
            plusDiag(classLoader);
        }
        if (i == 4) {
            sInstalled = true;
            com.tencent.qqlitepro.util.Log.i("QQ动态: 数据捕获 hook 安装完成（4/4 组）");
        } else {
            com.tencent.qqlitepro.util.Log.w("QQ动态: 部分数据捕获 hook 未装上（" + i + "/4 组），稍后重试");
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.QzoneDynamicHook$1, reason: invalid class name */
    static class AnonymousClass1 extends de.robv.android.xposed.XC_MethodHook {
        AnonymousClass1() {
        }

        protected void afterHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            try {
                java.lang.Object obj = (methodHookParam.args == null || methodHookParam.args.length <= 0) ? null : methodHookParam.args[0];
                if (obj instanceof java.util.List) {
                    com.tencent.qqlitepro.auto.QqDynamicScheduler.onNativeFeedList((java.util.List) obj);
                }
            } catch (java.lang.Throwable th) {
                com.tencent.qqlitepro.util.Log.w("QQ动态: 全局列表回调处理异常 " + th);
            }
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.QzoneDynamicHook$2, reason: invalid class name */
    static class AnonymousClass2 extends de.robv.android.xposed.XC_MethodHook {
        AnonymousClass2() {
        }

        protected void afterHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            try {
                java.lang.Object obj = (methodHookParam.args == null || methodHookParam.args.length <= 0) ? null : methodHookParam.args[0];
                if (obj instanceof java.util.List) {
                    com.tencent.qqlitepro.auto.QqDynamicScheduler.onUiFeedList(methodHookParam.thisObject, (java.util.List) obj);
                }
            } catch (java.lang.Throwable th) {
                com.tencent.qqlitepro.util.Log.w("QQ动态: Fragment 列表回调处理异常 " + th);
            }
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.QzoneDynamicHook$3, reason: invalid class name */
    static class AnonymousClass3 extends de.robv.android.xposed.XC_MethodHook {
        AnonymousClass3() {
        }

        protected void afterHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            int iIntValue = methodHookParam.args[1] instanceof java.lang.Integer ? ((java.lang.Integer) methodHookParam.args[1]).intValue() : -1;
            com.tencent.qqlitepro.hook.QzoneDynamicHook.dispatch(methodHookParam.thisObject, com.tencent.qqlitepro.hook.QzoneDynamicHook.extractFeed(methodHookParam.thisObject, iIntValue), iIntValue);
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.QzoneDynamicHook$4, reason: invalid class name */
    static class AnonymousClass4 extends de.robv.android.xposed.XC_MethodHook {
        AnonymousClass4() {
        }

        protected void afterHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            java.lang.Object obj = methodHookParam.thisObject;
            int iPos0 = com.tencent.qqlitepro.hook.QzoneDynamicHook.pos0(methodHookParam);
            com.tencent.qqlitepro.hook.QzoneDynamicHook.dispatch(obj, com.tencent.qqlitepro.hook.QzoneDynamicHook.extractFeed(obj, iPos0), iPos0);
        }
    }

    private static void plusDiag(java.lang.ClassLoader classLoader) {
        try {
            java.lang.String[] strArr = {ADAPTER, "momoi.mod.qqpro.hook.qzone.QZoneFeedAdapter"};
            for (int i = 0; i < 2; i++) {
                java.lang.String str = strArr[i];
                try {
                    com.tencent.qqlitepro.util.Log.i("QQ动态: 额外适配器候选命中 " + str + " => " + classLoader.loadClass(str).getName());
                    return;
                } catch (java.lang.Throwable unused) {
                }
            }
        } catch (java.lang.Throwable unused2) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int pos0(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
        try {
            if (methodHookParam.args[1] instanceof java.lang.Integer) {
                return ((java.lang.Integer) methodHookParam.args[1]).intValue();
            }
            return -1;
        } catch (java.lang.Throwable unused) {
            return -1;
        }
    }

    private static java.lang.Object SafeFeed(java.lang.Object obj, int i) {
        if (!(obj instanceof java.util.List) || i < 0) {
            return null;
        }
        java.util.List list = (java.util.List) obj;
        if (i < list.size()) {
            return list.get(i);
        }
        return null;
    }

    private static java.lang.Object safeField(java.lang.Object obj, java.lang.String str) {
        try {
            return de.robv.android.xposed.XposedHelpers.getObjectField(obj, str);
        } catch (java.lang.Throwable unused) {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.lang.Object extractFeed(java.lang.Object obj, int i) {
        java.lang.Object objSafeField = safeField(obj, "c");
        java.lang.Object objSafeFeed = SafeFeed(objSafeField, i);
        if (objSafeFeed != null && FEED_DATA.equals(objSafeFeed.getClass().getName())) {
            return objSafeFeed;
        }
        diagOnce(obj, objSafeFeed, i, objSafeField);
        return null;
    }

    private static void diagOnce(java.lang.Object obj, java.lang.Object obj2, int i, java.lang.Object obj3) {
        if (sDiagnosed) {
            return;
        }
        sDiagnosed = true;
        try {
            java.lang.Object objSafeField = safeField(obj, "b");
            java.lang.StringBuilder sb = new java.lang.StringBuilder("QQ动态: 捕获诊断 适配器=");
            sb.append(obj.getClass().getName());
            sb.append(" pos=");
            sb.append(i);
            sb.append(" b字段=");
            java.lang.String name = "null";
            sb.append(objSafeField == null ? "null" : objSafeField.getClass().getName());
            sb.append(" c字段=");
            sb.append(obj3 == null ? "null" : obj3.getClass().getName());
            sb.append(" (期望 com.tencent.watch.qzone_impl.frame.QZoneFeedAdapter 的 c 为 List<com.tencent.watch.qzone_impl.feed.model.BusinessFeedData>) feed=");
            if (obj2 != null) {
                name = obj2.getClass().getName();
            }
            sb.append(name);
            com.tencent.qqlitepro.util.Log.w(sb.toString());
        } catch (java.lang.Throwable unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void dispatch(java.lang.Object obj, java.lang.Object obj2, int i) {
        if (obj2 == null) {
            return;
        }
        try {
            com.tencent.qqlitepro.auto.QqDynamicScheduler.onFeedCaptured(safeField(obj, "b"), obj2);
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("QQ动态: 捕获动态失败 " + th);
        }
    }
}
