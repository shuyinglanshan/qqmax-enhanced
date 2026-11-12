package com.tencent.qqlitepro.hook;

/* JADX INFO: loaded from: classes.dex */
public final class AvatarHook {
    private static final java.lang.String SAVE_DIR = "/sdcard/Pictures/QQ Max";

    /** 图片保存目录：优先原路径，不可写则退到应用可写目录 */
    private static java.io.File saveDir(android.content.Context context) {
        /* 用户设置的图片保存路径（独立于文件/视频路径） */
        try {
            return new java.io.File(com.tencent.qqlitepro.util.Config.imageSaveDir(context));
        } catch (Throwable t) {
            java.io.File legacy = new java.io.File(SAVE_DIR);
            if (com.tencent.qqlitepro.util.Storage.ensureWritable(legacy)) {
                return legacy;
            }
            return com.tencent.qqlitepro.util.Storage.dir(context, "pictures");
        }
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
            java.lang.String str = null;
            java.lang.String str2 = null;
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
                        /* 多方法回退取 URL：动态图片对象的取值方法名可能随版本变化 */
                        str = tryCallString(obj2, "getImageUrl");
                        if (str == null || str.isEmpty()) {
                            str = tryCallString(obj2, "getFullUrl");
                        }
                        if (str == null || str.isEmpty()) {
                            str = tryCallString(obj2, "getUrl");
                        }
                        if (str == null || str.isEmpty()) {
                            str = tryCallString(obj2, "getPicUrl");
                        }
                        if (str == null || str.isEmpty()) {
                            str = tryCallString(obj2, "getOriginUrl");
                        }
                        if (str == null || str.isEmpty()) {
                            str = tryCallString(obj2, "getRawUrl");
                        }
                        if (str == null || str.isEmpty()) {
                            str = tryReadStringField(obj2, "url");
                        }
                        if (str == null || str.isEmpty()) {
                            str = tryReadStringField(obj2, "imageUrl");
                        }
                        /* QZone PicElement 常见字段（动态图片） */
                        if (str == null || str.isEmpty()) {
                            str = tryReadStringField(obj2, "url");
                        }
                        if (str == null || str.isEmpty()) {
                            str = tryReadStringField(obj2, "pictureUrl");
                        }
                        if (str == null || str.isEmpty()) {
                            str = tryReadStringField(obj2, "boycacheUrl");
                        }
                        if (str == null || str.isEmpty()) {
                            str = tryReadStringField(obj2, "llocUrl");
                        }
                        if (str == null || str.isEmpty()) {
                            str = tryCallString(obj2, "getPictureUrl");
                        }
                        if (str == null || str.isEmpty()) {
                            str = tryCallString(obj2, "getLlocUrl");
                        }
                        /* QZone 本地路径兜底 */
                        if (str3 == null || str3.isEmpty()) {
                            str3 = tryReadStringField(obj2, "llocPath");
                        }
                        if (str3 == null || str3.isEmpty()) {
                            str3 = tryCallString(obj2, "getLocalPath");
                        }
                        /* 本地路径：有本地文件就优先（免下载） */
                        str3 = tryCallString(obj2, "getImageLocalPath");
                        if (str3 == null || str3.isEmpty()) {
                            str3 = tryReadStringField(obj2, "localPath");
                        }
                        /* 视频条目：MediaItem.videoUrl（imageUrl 为空时用它） */
                        String videoUrl = tryCallString(obj2, "getVideoUrl");
                        if ((str == null || str.isEmpty()) && videoUrl != null && !videoUrl.isEmpty()) {
                            str = videoUrl;
                        }
                        str2 = str3;
                        /* URL 为空时用本地路径兜底（保存已有文件） */
                        if ((str == null || str.isEmpty()) && str2 != null && !str2.isEmpty()) {
                            str = str2;
                        }
                    } else {
                        str2 = null;
                    }
                    android.widget.ImageView imageView = (android.widget.ImageView) objCallMethod;
                    /* showSaveDialog(ctx, local, url)——此前传反导致"无可用图片" */
                    imageView.setOnLongClickListener(new com.tencent.qqlitepro.hook.AvatarHook$2$$ExternalSyntheticLambda0(imageView, iIntValue, str2, str));
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
    private static String tryCallString(Object obj, String method) {
        try {
            Object v = de.robv.android.xposed.XposedHelpers.callMethod(obj, method, new Object[0]);
            return v instanceof String ? (String) v : null;
        } catch (Throwable t) {
            return null;
        }
    }

    private static String tryReadStringField(Object obj, String field) {
        try {
            Object v = de.robv.android.xposed.XposedHelpers.getObjectField(obj, field);
            return v instanceof String ? (String) v : null;
        } catch (Throwable t) {
            return null;
        }
    }

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

    private static void downloadAndSave(final android.content.Context context, final java.lang.String str) {
        /* URL 校验：空/非法直接提示，不再抛出 message=null */
        if (str == null || str.trim().isEmpty() || !(str.startsWith("http://") || str.startsWith("https://"))) {
            android.widget.Toast.makeText(context,
                    "下载失败：图片地址无效" + (str == null ? "（null）" : "（" + str.trim() + "）"), 0).show();
            com.tencent.qqlitepro.util.Log.w("downloadAndSave: 非法 URL=" + str);
            return;
        }
        /* 网络请求必须离开主线程（否则 NetworkOnMainThreadException） */
        new Thread(new Runnable() {
            @Override
            public void run() {
                doDownloadAndSave(context, str);
            }
        }, "qqmax-dl").start();
    }

    private static void doDownloadAndSave(android.content.Context context, java.lang.String str) {
        try {
            /* 视频链接 -> 视频保存路径；图片链接 -> 图片保存路径 */
            boolean isVideo = str.toLowerCase().matches(".*\\.(mp4|mov|avi|mkv|webm|3gp)(\\?.*)?")
                    || str.contains("video");
            java.io.File file;
            if (isVideo) {
                file = new java.io.File(com.tencent.qqlitepro.util.Config.videoSaveDir(context));
            } else {
                file = saveDir(context);
            }
            try {
                if (!file.exists()) file.mkdirs();
            } catch (Throwable ignored) {}
            String ext = isVideo ? ".mp4" : ".jpg";
            java.io.File file2 = new java.io.File(file, "Media_" + java.lang.System.currentTimeMillis() + ext);
            java.net.HttpURLConnection httpURLConnection = (java.net.HttpURLConnection) new java.net.URL(str).openConnection();
            httpURLConnection.setConnectTimeout(8000);
            httpURLConnection.setReadTimeout(15000);
            httpURLConnection.setInstanceFollowRedirects(true);
            /* 很多图床对无 UA 的请求拒绝（返回 403/空 body） */
            httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Android)");
            int code = httpURLConnection.getResponseCode();
            if (code < 200 || code >= 300) {
                android.widget.Toast.makeText(context, "下载失败：服务器返回 " + code, 0).show();
                httpURLConnection.disconnect();
                return;
            }
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
                    if (file2.length() <= 0) {
                        file2.delete();
                        android.widget.Toast.makeText(context, "下载失败：返回内容为空", 0).show();
                        return;
                    }
                    com.tencent.qqlitepro.util.Log.i("图片已下载保存: " + file2.getAbsolutePath());
                    android.widget.Toast.makeText(context, "已保存到 " + file2.getAbsolutePath(), 1).show();
                    return;
                }
            }
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("downloadAndSave 失败: " + th);
            String msg = th.getMessage();
            if (msg == null || msg.trim().isEmpty()) {
                msg = th.getClass().getSimpleName();
            }
            android.widget.Toast.makeText(context, "下载失败: " + msg, 0).show();
        }
    }
}
