package com.tencent.qqlitepro.hook;

/* JADX INFO: loaded from: classes.dex */
public final class AvatarHook {
    private static final java.lang.String SAVE_DIR = "/sdcard/Pictures/QQ Max";

    /** 图片保存目录：优先原路径，不可写则退到应用可写目录 */
    private static java.io.File saveDir(android.content.Context context) {
        java.io.File legacy = new java.io.File(SAVE_DIR);
        if (com.tencent.qqlitepro.util.Storage.ensureWritable(legacy)) {
            return legacy;
        }
        return com.tencent.qqlitepro.util.Storage.dir(context, "pictures");
    }
    private static final android.os.Handler H = new android.os.Handler(android.os.Looper.getMainLooper());
    private static final java.util.Set<java.lang.Object> sQzoneMediaAdapters = java.util.Collections.synchronizedSet(new java.util.HashSet());
    private static volatile boolean sInstalled = false;

    public static boolean isInstalled() {
        return sInstalled;
    }

    private AvatarHook() {
    }

    public static void patch(java.lang.ClassLoader classLoader) {
        if (sInstalled) {
            return;
        }
        boolean zHookQzoneMediaViewer = hookQzoneMediaViewer(classLoader);
        boolean zPatchMediaPagerLongClick = patchMediaPagerLongClick(classLoader);
        if (zHookQzoneMediaViewer && zPatchMediaPagerLongClick) {
            sInstalled = true;
        } else {
            com.tencent.qqlitepro.util.Log.w("AvatarHook: 部分 hook 未装上（目标类可能尚未加载），稍后重试");
        }
    }

    private static boolean hookQzoneMediaViewer(java.lang.ClassLoader classLoader) {
        try {
            de.robv.android.xposed.XposedHelpers.findAndHookMethod(de.robv.android.xposed.XposedHelpers.findClass("momoi.mod.qqpro.hook.QZoneMediaViewer", classLoader), "build", new java.lang.Object[]{de.robv.android.xposed.XposedHelpers.findClass("com.tencent.watch.qzone_impl.frame.QZoneMediaBrowserFragment", classLoader), android.view.LayoutInflater.class, new com.tencent.qqlitepro.hook.AvatarHook.AnonymousClass1()});
            com.tencent.qqlitepro.util.Log.i("已 hook QZoneMediaViewer.build（屏蔽动态媒体长按下载）");
            return true;
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("hook QZoneMediaViewer.build 失败: " + th);
            return false;
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.AvatarHook$1, reason: invalid class name */
    static class AnonymousClass1 extends de.robv.android.xposed.XC_MethodHook {
        AnonymousClass1() {
        }

        protected void afterHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) throws java.lang.Throwable {
            android.view.View viewFindViewPager2;
            java.lang.Object objCallMethod;
            try {
                java.lang.Object result = methodHookParam.getResult();
                if (!(result instanceof android.view.View) || (viewFindViewPager2 = com.tencent.qqlitepro.hook.AvatarHook.findViewPager2((android.view.View) result)) == null || (objCallMethod = de.robv.android.xposed.XposedHelpers.callMethod(viewFindViewPager2, "getAdapter", new java.lang.Object[0])) == null) {
                    return;
                }
                com.tencent.qqlitepro.hook.AvatarHook.sQzoneMediaAdapters.add(objCallMethod);
                com.tencent.qqlitepro.util.Log.i("动态媒体长按已屏蔽: " + objCallMethod.getClass().getName());
            } catch (java.lang.Throwable th) {
                com.tencent.qqlitepro.util.Log.w("QZoneMediaViewer 识别失败: " + th);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static android.view.View findViewPager2(android.view.View view) {
        if (view != null && isViewPager2(view)) {
            return view;
        }
        if (!(view instanceof android.view.ViewGroup)) {
            return null;
        }
        android.view.ViewGroup viewGroup = (android.view.ViewGroup) view;
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            android.view.View viewFindViewPager2 = findViewPager2(viewGroup.getChildAt(i));
            if (viewFindViewPager2 != null) {
                return viewFindViewPager2;
            }
        }
        return null;
    }

    private static boolean isViewPager2(android.view.View view) {
        try {
            return "androidx.viewpager2.widget.ViewPager2".equals(view.getClass().getName());
        } catch (java.lang.Throwable unused) {
            return false;
        }
    }

    private static boolean patchMediaPagerLongClick(java.lang.ClassLoader classLoader) {
        java.lang.reflect.Method method;
        try {
            java.lang.Class clsFindClass = de.robv.android.xposed.XposedHelpers.findClass("momoi.mod.qqpro.hook.view.MediaPager$MediaAdapter", classLoader);
            java.lang.reflect.Method[] methods = clsFindClass.getMethods();
            int length = methods.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    method = null;
                    break;
                }
                method = methods[i];
                if ("onBindViewHolder".equals(method.getName()) && method.getParameterTypes().length == 2) {
                    break;
                }
                i++;
            }
            if (method != null) {
                de.robv.android.xposed.XposedHelpers.findAndHookMethod(clsFindClass, "onBindViewHolder", new java.lang.Object[]{method.getParameterTypes()[0], java.lang.Integer.TYPE, new com.tencent.qqlitepro.hook.AvatarHook.AnonymousClass2()});
                com.tencent.qqlitepro.util.Log.i("长按已 hook：MediaPager$MediaAdapter.onBindViewHolder（挂保存图片长按）");
                return true;
            }
            com.tencent.qqlitepro.util.Log.w("MediaPager$MediaAdapter onBindViewHolder 方法未找到");
            return false;
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("MediaPager 相关类加载/hook 失败: " + th);
            return false;
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.AvatarHook$2, reason: invalid class name */
    static class AnonymousClass2 extends de.robv.android.xposed.XC_MethodHook {
        AnonymousClass2() {
        }

        protected void afterHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) throws java.lang.Throwable {
            java.lang.Object obj;
            java.lang.String str;
            java.lang.String str2;
            try {
                if (com.tencent.qqlitepro.hook.AvatarHook.sQzoneMediaAdapters.contains(methodHookParam.thisObject) || (obj = methodHookParam.args[0]) == null) {
                    return;
                }
                java.lang.Object objCallMethod = de.robv.android.xposed.XposedHelpers.callMethod(obj, "getImage", new java.lang.Object[0]);
                if (objCallMethod instanceof android.widget.ImageView) {
                    java.util.List list = (java.util.List) de.robv.android.xposed.XposedHelpers.getObjectField(methodHookParam.thisObject, "items");
                    int iIntValue = ((java.lang.Integer) methodHookParam.args[1]).intValue();
                    java.lang.String str3 = null;
                    java.lang.Object obj2 = (list == null || iIntValue < 0 || iIntValue >= list.size()) ? null : list.get(iIntValue);
                    if (obj2 != null) {
                        try {
                            str = (java.lang.String) de.robv.android.xposed.XposedHelpers.callMethod(obj2, "getImageUrl", new java.lang.Object[0]);
                        } catch (java.lang.Throwable unused) {
                            str = null;
                        }
                        try {
                            str3 = (java.lang.String) de.robv.android.xposed.XposedHelpers.callMethod(obj2, "getImageLocalPath", new java.lang.Object[0]);
                        } catch (java.lang.Throwable unused2) {
                        }
                        if (str == null) {
                            try {
                                java.lang.String str4 = str3;
                                str3 = (java.lang.String) de.robv.android.xposed.XposedHelpers.callMethod(obj2, "getFullUrl", new java.lang.Object[0]);
                                str2 = str4;
                            } catch (java.lang.Throwable unused3) {
                                str2 = str3;
                                str3 = str;
                            }
                        } else {
                            str2 = str3;
                            str3 = str;
                        }
                    } else {
                        str2 = null;
                    }
                    android.widget.ImageView imageView = (android.widget.ImageView) objCallMethod;
                    imageView.setOnLongClickListener(new com.tencent.qqlitepro.hook.AvatarHook$2$$ExternalSyntheticLambda0(imageView, iIntValue, str2, str3));
                }
            } catch (java.lang.Throwable th) {
                com.tencent.qqlitepro.util.Log.w("MediaPager onBind hook 内部: " + th);
            }
        }

        static /* synthetic */ boolean lambda$afterHookedMethod$0(android.widget.ImageView imageView, int i, java.lang.String str, java.lang.String str2, android.view.View view) {
            android.content.Context context = imageView.getContext();
            com.tencent.qqlitepro.util.Log.i("MediaPager长按: 图" + i + " local=" + str + " url=" + str2);
            com.tencent.qqlitepro.hook.AvatarHook.showSaveDialog(context, str, str2);
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void showSaveDialog(android.content.Context context, java.lang.String str, java.lang.String str2) {
        if (context == null) {
            return;
        }
        boolean z = str != null && new java.io.File(str).exists();
        java.lang.String string;
        if (z) {
            java.lang.StringBuilder sb = new java.lang.StringBuilder("已存在本地缓存\n");
            sb.append(str);
            string = sb.toString();
        } else if (str2 != null) {
            java.lang.StringBuilder sb = new java.lang.StringBuilder("网络图片\n");
            sb.append(str2);
            string = sb.toString();
        } else {
            string = "无可用图片";
        }
        H.post(new com.tencent.qqlitepro.hook.AvatarHook$$ExternalSyntheticLambda0(context, "保存图片", string, z, str, str2));
    }

    static /* synthetic */ void lambda$showSaveDialog$1(android.content.Context context, java.lang.String str, java.lang.String str2, boolean z, java.lang.String str3, java.lang.String str4) {
        try {
            new android.app.AlertDialog.Builder(context).setTitle(str).setMessage(str2).setPositiveButton(z ? "用缓存图保存" : "下载并保存", new com.tencent.qqlitepro.hook.AvatarHook$$ExternalSyntheticLambda1(z, context, str3, str4)).setNegativeButton("取消", (android.content.DialogInterface.OnClickListener) null).show();
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("保存图片弹窗失败: " + th);
        }
    }

    static /* synthetic */ void lambda$showSaveDialog$0(boolean z, android.content.Context context, java.lang.String str, java.lang.String str2, android.content.DialogInterface dialogInterface, int i) {
        try {
            if (z) {
                saveFromPath(context, str);
            } else {
                downloadAndSave(context, str2);
            }
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("保存图片失败: " + th);
        }
    }

    private static void saveFromPath(android.content.Context context, java.lang.String str) {
        try {
            // 路径修复：/sdcard/Pictures 在 Android 10+ 不可写，改用可写目录
            java.io.File file = saveDir(context);
            java.io.File file2 = new java.io.File(str);
            java.io.File file3 = new java.io.File(file, "Image_" + java.lang.System.currentTimeMillis() + ".jpg");
            java.io.FileInputStream fileInputStream = new java.io.FileInputStream(file2);
            java.io.FileOutputStream fileOutputStream = new java.io.FileOutputStream(file3);
            byte[] bArr = new byte[8192];
            while (true) {
                int i = fileInputStream.read(bArr);
                if (i > 0) {
                    fileOutputStream.write(bArr, 0, i);
                } else {
                    fileInputStream.close();
                    fileOutputStream.close();
                    com.tencent.qqlitepro.util.Log.i("图片已保存: " + file3.getAbsolutePath());
                    android.widget.Toast.makeText(context, "已保存到 " + file3.getAbsolutePath(), 1).show();
                    return;
                }
            }
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("saveFromPath 失败: " + th);
            android.widget.Toast.makeText(context, "保存失败: " + th, 0).show();
        }
    }

    private static void downloadAndSave(android.content.Context context, java.lang.String str) {
        try {
            // 路径修复：/sdcard/Pictures 在 Android 10+ 不可写，改用可写目录
            java.io.File file = saveDir(context);
            java.io.File file2 = new java.io.File(file, "Image_" + java.lang.System.currentTimeMillis() + ".jpg");
            java.net.HttpURLConnection httpURLConnection = (java.net.HttpURLConnection) new java.net.URL(str).openConnection();
            httpURLConnection.setConnectTimeout(8000);
            httpURLConnection.setReadTimeout(15000);
            httpURLConnection.setInstanceFollowRedirects(true);
            httpURLConnection.connect();
            java.io.InputStream inputStream = httpURLConnection.getInputStream();
            java.io.FileOutputStream fileOutputStream = new java.io.FileOutputStream(file2);
            byte[] bArr = new byte[8192];
            while (true) {
                int i = inputStream.read(bArr);
                if (i > 0) {
                    fileOutputStream.write(bArr, 0, i);
                } else {
                    inputStream.close();
                    fileOutputStream.close();
                    httpURLConnection.disconnect();
                    com.tencent.qqlitepro.util.Log.i("图片已下载保存: " + file2.getAbsolutePath());
                    android.widget.Toast.makeText(context, "已保存到 " + file2.getAbsolutePath(), 1).show();
                    return;
                }
            }
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("downloadAndSave 失败: " + th);
            android.widget.Toast.makeText(context, "下载失败: " + th.getMessage(), 0).show();
        }
    }
}
