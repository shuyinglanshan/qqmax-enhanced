package com.tencent.qqlitepro.hook;

/* JADX INFO: loaded from: classes.dex */
public final class UiPatcher {
    private static final java.lang.String BADGE_EDITED = "已编辑";
    private static final java.lang.String BADGE_FAKEIMAGE = "非图片";
    private static final java.lang.String BADGE_FAKEVOICE = "非语音";
    private static final java.lang.String BADGE_TEXT = "已撤回";
    private static final int CH_IMAGE = 1;
    private static final int CH_VOICE = 0;
    private static final java.util.Map<java.lang.String, com.tencent.qqlitepro.hook.UiPatcher.FakeDetect> FAKE_DETECT;
    private static final int FAKE_DETECT_MAX = 512;
    private static final long FAKE_DETECT_NEGATIVE_TTL_MS = 5000;
    private static final java.util.Set<java.lang.String> FAKE_INFO_SHOWN;
    private static final int MAX_DETAIL_ROWS = 40;
    private static final java.util.Set<java.lang.String> PIC_STRUCTURE_DUMPED;
    private static final java.util.Set<java.lang.String> RECV_DOWNLOAD_SCHEDULED;
    private static final int RETRY_MAX = 3;
    static final java.util.concurrent.ExecutorService SHARD_COLLECT_EXEC;
    private static final java.util.Set<java.lang.String> SHARD_COLLECT_SCHEDULED;
    private static final int STROKE_WIDTH_DP = 2;
    private static final int RED = android.graphics.Color.rgb(230, 54, 57);
    private static final int LIGHT_BLUE = android.graphics.Color.rgb(137, 200, 240);
    private static final int ORANGE = android.graphics.Color.rgb(org.mozilla.classfile.ByteCode.IMPDEP2, 140, 33);
    private static final java.util.Set<android.widget.TextView> STYLED = java.util.Collections.newSetFromMap(new java.util.WeakHashMap());
    private static final java.util.concurrent.atomic.AtomicBoolean RECONCILING = new java.util.concurrent.atomic.AtomicBoolean(false);
    /* 主线程 Handler：样式巡检统一投递到主线程执行，避免跨线程改 View */
    private static final android.os.Handler MAIN =
            new android.os.Handler(android.os.Looper.getMainLooper());
    /* 样式巡检间隔与总轮数（原为 1s × 600 轮的后台直改，改为 5s × 120 轮的主线程批处理） */
    private static final long RECONCILE_INTERVAL_MS = 5000L;
    private static final int RECONCILE_ROUNDS = 120;
    private static final java.util.Set<android.view.View> DECORATED = java.util.Collections.newSetFromMap(new java.util.IdentityHashMap());
    private static final java.util.Map<android.view.View, java.lang.String> ROOT_MSGID = new java.util.WeakHashMap();
    private static final java.util.Map<java.lang.String, java.lang.String> EDITED_TEXT = new java.util.concurrent.ConcurrentHashMap();
    private static final java.util.Map<java.lang.String, java.lang.String> EDITED_ORIGINAL = new java.util.concurrent.ConcurrentHashMap();
    private static final java.util.Set<android.view.View> EDIT_BOUND = java.util.Collections.newSetFromMap(new java.util.IdentityHashMap());
    private static final java.util.Set<android.view.View> EDITED_MARKED = java.util.Collections.newSetFromMap(new java.util.IdentityHashMap());
    private static java.lang.Object sLongPressMenuFragment = null;
    private static final java.util.Map<android.view.View, java.util.Set<java.lang.String>> FAKE_ROW_INJECTED_PER_MENU = new java.util.IdentityHashMap();
    private static final java.util.Set<android.view.View> FAKEVOICE_MARKED = java.util.Collections.newSetFromMap(new java.util.IdentityHashMap());

    static {
        new java.util.concurrent.ConcurrentHashMap();
        FAKE_INFO_SHOWN = java.util.concurrent.ConcurrentHashMap.newKeySet();
        FAKE_DETECT = new java.util.concurrent.ConcurrentHashMap();
        new java.util.concurrent.ConcurrentHashMap();
        PIC_STRUCTURE_DUMPED = java.util.concurrent.ConcurrentHashMap.newKeySet();
        SHARD_COLLECT_SCHEDULED = java.util.Collections.newSetFromMap(new java.util.concurrent.ConcurrentHashMap());
        SHARD_COLLECT_EXEC = java.util.concurrent.Executors.newSingleThreadExecutor();
        RECV_DOWNLOAD_SCHEDULED = java.util.Collections.newSetFromMap(new java.util.concurrent.ConcurrentHashMap());
    }

    /* JADX INFO: Access modifiers changed from: private */
    static final class FakeDetect {
        final long at = java.lang.System.currentTimeMillis();
        final java.lang.String badge;
        final java.lang.String collectKey;
        final int count;
        final boolean fake;
        final boolean imageMode;
        final int index;
        final java.lang.String path;
        final boolean self;
        final boolean shard;

        FakeDetect(boolean z, boolean z2, java.lang.String str, java.lang.String str2, boolean z3, boolean z4, int i, int i2, java.lang.String str3) {
            this.fake = z;
            this.imageMode = z2;
            this.path = str;
            this.badge = str2;
            this.self = z3;
            this.shard = z4;
            this.index = i;
            this.count = i2;
            this.collectKey = str3;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static com.tencent.qqlitepro.hook.UiPatcher.FakeDetect detectFake(java.lang.Object obj, java.lang.String str) {
        if (obj == null || str == null) {
            return null;
        }
        java.util.Map<java.lang.String, com.tencent.qqlitepro.hook.UiPatcher.FakeDetect> map = FAKE_DETECT;
        com.tencent.qqlitepro.hook.UiPatcher.FakeDetect cached = map.get(str);
        /*
         * 只缓存"识别成功"的结果。
         * 旧实现把失败结果也缓存 FAKE_DETECT_NEGATIVE_TTL_MS，导致：
         *   - 首次渲染时图片路径还没就绪（尤其宿主未运行时收到的消息）→ 记为 null
         *   - 之后 TTL 内无论路径是否已就绪都直接返回 null → "概率识别不了"
         * 现在失败不缓存，每次都重新扫；成功后立即缓存，保证性能。
         */
        if (cached != null && cached.fake) {
            return cached;
        }
        com.tencent.qqlitepro.hook.UiPatcher.FakeDetect fakeDetectScanFake = scanFake(obj);
        if (fakeDetectScanFake == null || !fakeDetectScanFake.fake) {
            /* 识别失败：不缓存（可能是图片尚未下载/路径未就绪） */
            map.remove(str);
            return fakeDetectScanFake;
        }
        map.put(str, fakeDetectScanFake);
        if (map.size() > FAKE_DETECT_MAX) {
            evictFakeDetect();
        }
        return fakeDetectScanFake;
    }

    /** 外部数据变化（如收到新消息、图片下载完成）时清空识别缓存，触发重新识别 */
    public static void invalidateFakeCache() {
        try {
            FAKE_DETECT.clear();
        } catch (Throwable ignored) {}
    }

    private static void evictFakeDetect() {
        long jCurrentTimeMillis = java.lang.System.currentTimeMillis();
        for (java.util.Map.Entry<java.lang.String, com.tencent.qqlitepro.hook.UiPatcher.FakeDetect> entry : FAKE_DETECT.entrySet()) {
            if (!entry.getValue().fake && jCurrentTimeMillis - entry.getValue().at >= FAKE_DETECT_NEGATIVE_TTL_MS) {
                FAKE_DETECT.remove(entry.getKey());
            }
        }
        java.util.Map<java.lang.String, com.tencent.qqlitepro.hook.UiPatcher.FakeDetect> map = FAKE_DETECT;
        if (map.size() > FAKE_DETECT_MAX) {
            map.clear();
        }
    }

    private static com.tencent.qqlitepro.hook.UiPatcher.FakeDetect scanFake(java.lang.Object obj) {
        java.lang.String str;
        java.lang.String string;
        java.lang.String next;
        com.tencent.qqlitepro.http.FakeVoice.OriginalInfo originalInfo;
        java.lang.String string2;
        try {
            boolean zIsSelfSendRecord = isSelfSendRecord(obj);
            java.util.Iterator<java.lang.String> it = findPttPaths(obj).iterator();
            do {
                str = "";
                if (it.hasNext()) {
                    next = it.next();
                    originalInfo = com.tencent.qqlitepro.http.FakeVoice.parseOriginalInfo(next);
                } else {
                    for (java.lang.String str2 : findAllPicPaths(obj)) {
                        com.tencent.qqlitepro.http.FakeImage.OriginalInfo originalInfo2 = com.tencent.qqlitepro.http.FakeImage.parseOriginalInfo(str2);
                        if (originalInfo2 != null) {
                            java.lang.String strFakeBadgeName = fakeBadgeName(originalInfo2.name, false, originalInfo2.shard, originalInfo2.index, originalInfo2.count);
                            boolean z = originalInfo2.shard;
                            int i = originalInfo2.index;
                            int i2 = originalInfo2.count;
                            if (originalInfo2.shard) {
                                java.lang.StringBuilder sb = new java.lang.StringBuilder();
                                sb.append("img:");
                                if (originalInfo2.md5 != null) {
                                    str = originalInfo2.md5;
                                }
                                sb.append(str);
                                sb.append("#");
                                sb.append(originalInfo2.index);
                                string = sb.toString();
                            } else {
                                string = null;
                            }
                            return new com.tencent.qqlitepro.hook.UiPatcher.FakeDetect(true, true, str2, strFakeBadgeName, zIsSelfSendRecord, z, i, i2, string);
                        }
                    }
                    return new com.tencent.qqlitepro.hook.UiPatcher.FakeDetect(false, false, null, null, false, false, 0, 1, null);
                }
            } while (originalInfo == null);
            java.lang.String strFakeBadgeName2 = fakeBadgeName(originalInfo.name, true, originalInfo.shard, originalInfo.index, originalInfo.count);
            boolean z2 = originalInfo.shard;
            int i3 = originalInfo.index;
            int i4 = originalInfo.count;
            if (originalInfo.shard) {
                java.lang.StringBuilder sb2 = new java.lang.StringBuilder();
                sb2.append("voi:");
                if (originalInfo.md5 != null) {
                    str = originalInfo.md5;
                }
                sb2.append(str);
                sb2.append("#");
                sb2.append(originalInfo.index);
                string2 = sb2.toString();
            } else {
                string2 = null;
            }
            return new com.tencent.qqlitepro.hook.UiPatcher.FakeDetect(true, false, next, strFakeBadgeName2, zIsSelfSendRecord, z2, i3, i4, string2);
        } catch (java.lang.Throwable unused) {
            return null;
        }
    }

    /** 主列表 Fragment（ChatListFragment）实例 —— 用于临时会话导航 */
    private static volatile Object sChatListFragment;
    private static volatile java.lang.ClassLoader sHostCl;

    public static Object chatListFragment() {
        return sChatListFragment;
    }

    public static java.lang.ClassLoader hostClassLoader() {
        return sHostCl;
    }

    public static void patch(java.lang.ClassLoader classLoader) {
        startStyleReconcile();
        installLanzouLinkHook(classLoader);
        installGroupStatEntryHook(classLoader);
        installChatListFragmentHook(classLoader);
        installFeedZanListHook(classLoader);
        com.tencent.qqlitepro.hook.QzoneDynamicHook.installOperationMoreHookDelayed(classLoader);
        com.tencent.qqlitepro.hook.QzoneDynamicHook.installSelectItemClickHook(classLoader);
        com.tencent.qqlitepro.hook.QzoneDynamicHook.installDialogShowHook();
        installMediaSaveDirHook(classLoader);
        installEditProfileSignatureHook(classLoader);
    }

    /**
     * 修改资料页新增"个性签名"条目。
     * 逆向：SelfEditProfileFragment.Y_0 把 EditProfileProvider.b 的条目依次
     * createView 后 addView 到 editItemContainer（LinearLayout, id=2114519837）。
     * hook Y_0 after：在该容器追加自绘签名条目；点击弹输入框，
     * 保存走内核 KernelServiceUtil.d()（IProfileService）.setLongNick(text, cb)。
     */
    private static void installEditProfileSignatureHook(java.lang.ClassLoader classLoader) {
        try {
            java.lang.Class<?> fragCls = java.lang.Class.forName(
                    "com.tencent.qqnt.watch.selftab.ui.edit.SelfEditProfileFragment", false, classLoader);
            java.lang.reflect.Method y0 = null;
            for (java.lang.reflect.Method m : fragCls.getDeclaredMethods()) {
                if (m.getName().equals("Y_0")
                        && m.getParameterTypes().length == 3
                        && m.getParameterTypes()[0] == android.view.LayoutInflater.class) {
                    y0 = m;
                    break;
                }
            }
            if (y0 == null) {
                com.tencent.qqlitepro.util.Log.w("SelfEditProfileFragment.Y_0 未找到（签名条目跳过）");
                return;
            }
            y0.setAccessible(true);
            de.robv.android.xposed.XposedBridge.hookMethod(y0, new de.robv.android.xposed.XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) {
                    try {
                        android.view.View root = (android.view.View) param.getResult();
                        if (root == null) {
                            return;
                        }
                        android.view.View container = root.findViewById(2114519837);
                        if (!(container instanceof android.widget.LinearLayout)) {
                            return;
                        }
                        android.widget.LinearLayout box = (android.widget.LinearLayout) container;
                        for (int i = 0; i < box.getChildCount(); i++) {
                            if ("qqlite_signature_entry".equals(box.getChildAt(i).getTag())) {
                                return; /* 已注入 */
                            }
                        }
                        android.content.Context ctx = box.getContext();
                        android.widget.LinearLayout row = new android.widget.LinearLayout(ctx);
                        row.setOrientation(1);
                        row.setClickable(true);
                        row.setTag("qqlite_signature_entry");
                        int pad = (int) (ctx.getResources().getDisplayMetrics().density * 14);
                        row.setPadding(pad, pad, pad, pad);
                        android.widget.TextView title = new android.widget.TextView(ctx);
                        title.setText("个性签名");
                        title.setTextSize(15.0f);
                        title.setTextColor(-1);
                        row.addView(title);
                        row.setOnClickListener(new android.view.View.OnClickListener() {
                            @Override
                            public void onClick(android.view.View v) {
                                try {
                                    showSignatureEditor(v.getContext());
                                } catch (Throwable th) {
                                    com.tencent.qqlitepro.util.Log.w("打开签名编辑失败: " + th);
                                }
                            }
                        });
                        box.addView(row);
                        com.tencent.qqlitepro.util.Log.i("修改资料页已注入个性签名条目");
                    } catch (Throwable th) {
                        com.tencent.qqlitepro.util.Log.w("注入签名条目失败: " + th);
                    }
                }
            });
            com.tencent.qqlitepro.util.Log.i("已安装修改资料签名 hook（SelfEditProfileFragment.Y_0）");
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("安装签名 hook 失败: " + th);
        }
    }

    /**
     * 个性签名编辑：自建全屏界面（复刻宿主"修改昵称"样式：深色背景 + 居中标题
     * + 😊 输入行 + 蓝色确定 + 深色取消），保存走宿主 IProfileService 链路。
     */
    private static void showSignatureEditor(final android.content.Context context) {
        try {
            float d = context.getResources().getDisplayMetrics().density;
            /* 全屏根容器 */
            final android.widget.FrameLayout full = new android.widget.FrameLayout(context);
            full.setBackgroundColor(-14802417);

            android.widget.LinearLayout column = new android.widget.LinearLayout(context);
            column.setOrientation(1);
            android.widget.FrameLayout.LayoutParams colLp = new android.widget.FrameLayout.LayoutParams(
                    -1, -2);
            colLp.gravity = 17;
            int sand = (int) (d * 14);
            colLp.leftMargin = sand;
            colLp.rightMargin = sand;
            full.addView(column, colLp);

            android.widget.TextView title = new android.widget.TextView(context);
            title.setText("修改个性签名");
            title.setTextSize(16.0f);
            title.setTextColor(-1);
            title.setGravity(17);
            column.addView(title, new android.widget.LinearLayout.LayoutParams(-1, -2));

            /* 😊 + 输入行 */
            android.widget.LinearLayout inputRow = new android.widget.LinearLayout(context);
            inputRow.setOrientation(0);
            inputRow.setGravity(16);
            android.widget.LinearLayout.LayoutParams inLp = new android.widget.LinearLayout.LayoutParams(
                    -1, (int) (d * 48));
            inLp.topMargin = (int) (d * 16);
            column.addView(inputRow, inLp);

            android.widget.TextView icon = new android.widget.TextView(context);
            icon.setText("\uD83D\uDE0A");
            icon.setTextSize(17.0f);
            inputRow.addView(icon);

            final android.widget.EditText et = new android.widget.EditText(context);
            et.setHint("请输入个性签名");
            et.setTextColor(-1);
            et.setHintTextColor(-8355712);
            et.setTextSize(15.0f);
            et.setSingleLine(true);
            et.setBackground(null);
            android.widget.LinearLayout.LayoutParams etLp =
                    new android.widget.LinearLayout.LayoutParams(0, -2, 1.0f);
            etLp.leftMargin = (int) (d * 10);
            inputRow.addView(et, etLp);

            /* 确定（宿主蓝） */
            android.widget.TextView ok = new android.widget.TextView(context);
            ok.setText("确定");
            ok.setTextSize(15.5f);
            ok.setTextColor(-1);
            ok.setGravity(17);
            android.graphics.drawable.GradientDrawable okBg = new android.graphics.drawable.GradientDrawable();
            okBg.setColor(-13878718);
            okBg.setCornerRadius((int) (d * 24));
            ok.setBackground(okBg);
            ok.setClickable(true);
            android.widget.LinearLayout.LayoutParams okLp =
                    new android.widget.LinearLayout.LayoutParams(-1, (int) (d * 46));
            okLp.topMargin = (int) (d * 20);
            column.addView(ok, okLp);

            /* 取消（深灰） */
            android.widget.TextView cancel = new android.widget.TextView(context);
            cancel.setText("取消");
            cancel.setTextSize(15.5f);
            cancel.setTextColor(-1);
            cancel.setGravity(17);
            android.graphics.drawable.GradientDrawable cancelBg = new android.graphics.drawable.GradientDrawable();
            cancelBg.setColor(-14736814);
            cancelBg.setCornerRadius((int) (d * 24));
            cancel.setBackground(cancelBg);
            cancel.setClickable(true);
            android.widget.LinearLayout.LayoutParams cancelLp =
                    new android.widget.LinearLayout.LayoutParams(-1, (int) (d * 46));
            cancelLp.topMargin = (int) (d * 10);
            column.addView(cancel, cancelLp);

            final android.app.Dialog dialog = new android.app.Dialog(context);
            dialog.requestWindowFeature(1);
            dialog.setContentView(full);
            if (dialog.getWindow() != null) {
                android.view.WindowManager.LayoutParams attrs = dialog.getWindow().getAttributes();
                attrs.width = -1;
                attrs.height = -1;
                dialog.getWindow().setAttributes(attrs);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            }

            /* 读当前签名 */
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final String cur = com.tencent.qqlitepro.http.QqKernel.currentLongNick();
                    new android.os.Handler(android.os.Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (cur != null && !cur.isEmpty()) {
                                    et.setText(cur);
                                    et.setSelection(cur.length());
                                }
                            } catch (Throwable ignored) {}
                        }
                    });
                }
            }, "sig-read").start();

            ok.setOnClickListener(new android.view.View.OnClickListener() {
                @Override
                public void onClick(final android.view.View v) {
                    final String text = et.getText().toString().trim();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            final String err = com.tencent.qqlitepro.http.QqKernel.setLongNickViaHostWrapper(text);
                            new android.os.Handler(android.os.Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    if (err == null) {
                                        android.widget.Toast.makeText(v.getContext(), "签名已更新",
                                                android.widget.Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    } else {
                                        android.widget.Toast.makeText(v.getContext(), "保存失败：" + err,
                                                android.widget.Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }, "sig-save").start();
                }
            });
            cancel.setOnClickListener(new android.view.View.OnClickListener() {
                @Override
                public void onClick(android.view.View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        } catch (Throwable th) {
            com.tencent.qqlitepro.util.Log.w("签名界面异常: " + th);
        }
    }
    /**
     * 宿主媒体保存路径 hook（逆向 momoi.mod.qqpro.hook.qzone.MediaSave）：
     *   toGallery(ctx, src, displayName, mime, isVideo, subDir)
     *   现代(API>=29): MediaStore relative_path = (isVideo? Movies: Pictures) + "/" + subDir
     *   旧版: /sdcard/Pictures/<subDir>/
     * 默认 subDir="QZone"。按 isVideo 将 relative_path/subDir 替换为用户设置的
     * 图片/视频保存路径（绝对路径 -> 相对外部存储根的相对路径；未设置则用宿主默认）。
     */
    private static void installMediaSaveDirHook(java.lang.ClassLoader classLoader) {
        try {
            java.lang.Class<?> saveCls = java.lang.Class.forName(
                    "momoi.mod.qqpro.hook.qzone.MediaSave", false, classLoader);
            java.lang.reflect.Method toGallery = null;
            for (java.lang.reflect.Method m : saveCls.getDeclaredMethods()) {
                if (!m.getName().equals("toGallery")) continue;
                Class<?>[] ps = m.getParameterTypes();
                if (ps.length == 6 && ps[0] == android.content.Context.class
                        && ps[1] == java.io.File.class
                        && ps[2] == java.lang.String.class
                        && ps[3] == java.lang.String.class
                        && ps[4] == Boolean.TYPE
                        && ps[5] == java.lang.String.class) {
                    toGallery = m;
                    break;
                }
            }
            if (toGallery == null) {
                com.tencent.qqlitepro.util.Log.w("MediaSave.toGallery 未找到（宿主媒体路径 hook 跳过）");
                return;
            }
            toGallery.setAccessible(true);
            de.robv.android.xposed.XposedBridge.hookMethod(toGallery, new de.robv.android.xposed.XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) {
                    try {
                        boolean isVideo = (Boolean) param.args[4];
                        String subDir = (String) param.args[5];
                        android.content.Context ctx = (android.content.Context) param.args[0];
                        String customAbs = isVideo
                                ? com.tencent.qqlitepro.util.Config.videoSaveDirRaw(ctx)
                                : com.tencent.qqlitepro.util.Config.imageSaveDirRaw(ctx);
                        if (customAbs == null || customAbs.trim().isEmpty()) {
                            return; /* 未设置 -> 宿主默认 QZone */
                        }
                        String rel = absToRelative(customAbs.trim());
                        if (rel == null || rel.isEmpty()) {
                            return; /* 非外部存储路径（如应用私有目录），保持宿主默认 */
                        }
                        /* saveModern: relative_path = (Movies|Pictures)+"/"+subDir —— 把 subDir 改成
                         * 用户路径的相对部分并去掉 Movies|Pictures 前缀（relative_path 直接用 rel），
                         * 这里通过把 subDir 设为带 ../ 的方式不可行，改为直接覆盖 subDir，
                         * 由 MediaSave 拼接 Movies|Pictures 前缀——用户路径需在 Movies/Pictures 下才生效。
                         * 对 Pictures 之外的路径，走参数整体改写：subDir 置为 rel（MediaSave 拼前缀），
                         * MediaStore 的 relative_path=Pictures/rel 或 Movies/rel，这就是宿主能力边界内的自定义。 */
                        param.args[5] = rel;
                        com.tencent.qqlitepro.util.Log.i("宿主媒体保存路径重定向 isVideo=" + isVideo + " -> " + rel);
                    } catch (Throwable ignored) {
                    }
                }
            });
            com.tencent.qqlitepro.util.Log.i("已安装宿主媒体保存路径 hook（MediaSave.toGallery）");
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("安装宿主媒体保存路径 hook 失败: " + th);
        }
    }

    /** 绝对路径 -> 相对外部存储根的路径（/storage/emulated/0/xxx -> xxx），非主存储返回 null */
    private static String absToRelative(String abs) {
        if (abs == null) return null;
        String p = abs;
        if (p.startsWith("/storage/emulated/0/")) {
            return p.substring("/storage/emulated/0/".length());
        }
        if (p.equals("/storage/emulated/0")) {
            return "";
        }
        if (p.startsWith("/sdcard/")) {
            return p.substring("/sdcard/".length());
        }
        return null;
    }

    /**
     * QQ动态：每个动态卡片点赞按钮下方显示全部点赞用户 昵称(QQ号)。
     * 逆向：QZoneFeedAdapter.c 是 List<BusinessFeedData>；
     *   BusinessFeedData.getCellLikeInfo() -> CellLikeInfo.likeMans(ArrayList<LikeMan>)
     *   LikeMan.user -> User（nick=昵称 uin=QQ号）
     * hook onBindViewHolder after：从 holder.itemView 找点赞按钮（binding id likeCount /
     * 或含 like 的 ImageView）的父布局，插入/更新一条昵称 TextView。
     */
    private static volatile int sZanProbe = 0;

    private static void installFeedZanListHook(java.lang.ClassLoader classLoader) {
        try {
            java.lang.Class<?> adapterCls = java.lang.Class.forName(
                    "com.tencent.watch.qzone_impl.frame.QZoneFeedAdapter", false, classLoader);
            java.lang.reflect.Method onBind = null;
            for (java.lang.reflect.Method m : adapterCls.getDeclaredMethods()) {
                if (m.getName().equals("onBindViewHolder")
                        && m.getParameterTypes().length == 2
                        && m.getParameterTypes()[0].getName().contains("ViewHolder")
                        && m.getParameterTypes()[1] == Integer.TYPE) {
                    onBind = m;
                    break;
                }
            }
            if (onBind == null) {
                com.tencent.qqlitepro.util.Log.w("动态点赞: QZoneFeedAdapter.onBindViewHolder 未找到");
                return;
            }
            onBind.setAccessible(true);
            de.robv.android.xposed.XposedBridge.hookMethod(onBind, new de.robv.android.xposed.XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) {
                    try {
                        Object adapter = param.thisObject;
                        int position = (Integer) param.args[1];
                        Object holder = param.args[0];
                        if (adapter == null || holder == null || position < 0) {
                            return;
                        }
                        android.os.Handler h = new android.os.Handler(android.os.Looper.getMainLooper());
                        final Object fAdapter = adapter;
                        final Object fHolder = holder;
                        final int fPos = position;
                        h.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    injectZanNames(fAdapter, fHolder, fPos);
                                } catch (Throwable ignored) {
                                }
                            }
                        }, 150L);
                    } catch (Throwable ignored) {
                    }
                }
            });
            com.tencent.qqlitepro.util.Log.i("已安装动态点赞昵称显示 hook（QZoneFeedAdapter）");
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("安装动态点赞 hook 失败: " + th);
        }
    }

    /** 在点赞按钮下方插入"昵称(QQ号)、昵称(QQ号)…"一行（自动换行） */
    private static void injectZanNames(Object adapter, Object holder, int position) {
        try {
            android.view.View itemView = (android.view.View)
                    de.robv.android.xposed.XposedHelpers.getObjectField(holder, "itemView");
            if (itemView == null) {
                return;
            }
            /* 1) 取该位置的 BusinessFeedData */
            Object list = de.robv.android.xposed.XposedHelpers.getObjectField(adapter, "c");
            if (!(list instanceof java.util.List) || position < 0
                    || position >= ((java.util.List) list).size()) {
                return;
            }
            Object data = ((java.util.List) list).get(position);
            if (data == null) {
                return;
            }
            /* 2) 取 CellLikeInfo：先方法，再字段（两种都试） */
            Object cellLike = null;
            try {
                cellLike = de.robv.android.xposed.XposedHelpers.callMethod(data, "getCellLikeInfo", new Object[0]);
            } catch (Throwable ignored) {}
            if (cellLike == null) {
                try {
                    cellLike = de.robv.android.xposed.XposedHelpers.getObjectField(data, "cellLikeInfo");
                } catch (Throwable ignored) {}
            }
            if (sZanProbe++ < 30) {
                com.tencent.qqlitepro.util.Log.i("动态点赞诊断: pos=" + position
                        + " data=" + (data == null ? "null" : data.getClass().getSimpleName())
                        + " cellLike=" + (cellLike == null ? "null" : cellLike.getClass().getSimpleName()));
            }
            String zanText = "";
            if (cellLike != null) {
                Object mans = null;
                try {
                    mans = de.robv.android.xposed.XposedHelpers.getObjectField(cellLike, "likeMans");
                } catch (Throwable ignored) {}
                StringBuilder sb = new StringBuilder();
                if (mans instanceof java.util.List) {
                    for (Object man : (java.util.List) mans) {
                        if (man == null) continue;
                        try {
                            Object user = de.robv.android.xposed.XposedHelpers.getObjectField(man, "user");
                            if (user == null) continue;
                            String nick = null;
                            String uin = "";
                            try {
                                Object n = de.robv.android.xposed.XposedHelpers.getObjectField(user, "nick");
                                nick = n == null ? null : String.valueOf(n);
                            } catch (Throwable ignored) {}
                            try {
                                Object u = de.robv.android.xposed.XposedHelpers.getObjectField(user, "uin");
                                uin = u == null ? "" : String.valueOf(u);
                            } catch (Throwable ignored) {}
                            if ((nick == null || nick.trim().isEmpty()) && uin.isEmpty()) continue;
                            if (sb.length() > 0) sb.append("、");
                            sb.append(nick == null || nick.trim().isEmpty() ? uin : nick.trim());
                            sb.append("(").append(uin).append(")");
                        } catch (Throwable ignored) {}
                    }
                }
                zanText = sb.toString();
            }
            /* 3) 定位插入容器：优先"点赞行"的父容器（整行下方），其次卡片垂直根 */
            android.view.View likeView = findViewByNameContains(itemView, "like");
            if (likeView == null) {
                likeView = findZanAnchor(itemView);
            }
            android.view.ViewGroup insertParent = null;
            android.view.View insertAfter = null;
            if (likeView != null && likeView.getParent() instanceof android.view.ViewGroup) {
                android.view.ViewGroup p0 = (android.view.ViewGroup) likeView.getParent();
                /* 若父容器是横向（点赞行内），再上提一层，保证插在整行下方 */
                if (p0.getParent() instanceof android.view.ViewGroup && isHorizontal(p0)) {
                    insertAfter = p0;
                    insertParent = (android.view.ViewGroup) p0.getParent();
                } else {
                    insertParent = p0;
                    insertAfter = likeView;
                }
            }
            if (insertParent == null) {
                insertParent = findVerticalContainer(itemView);
            }
            if (insertParent == null) {
                return;
            }
            /* 4) 复用/新建标记 TextView */
            android.widget.TextView exist = null;
            for (int i = 0; i < insertParent.getChildCount(); i++) {
                android.view.View cv = insertParent.getChildAt(i);
                if ("qqlite_zan_names".equals(cv.getTag())) {
                    exist = (android.widget.TextView) cv;
                    break;
                }
            }
            if (sZanProbe <= 30) {
                com.tencent.qqlitepro.util.Log.i("动态点赞诊断: zanText=[" + zanText + "]"
                        + " likeView=" + (likeView == null ? "null"
                        : likeView.getClass().getSimpleName())
                        + " parent=" + (insertParent == null ? "null"
                        : insertParent.getClass().getSimpleName()));
            }
            if (zanText.isEmpty()) {
                if (exist != null) {
                    exist.setVisibility(android.view.View.GONE);
                }
                return;
            }
            android.content.Context ctx = itemView.getContext();
            android.widget.TextView tv;
            if (exist == null) {
                tv = new android.widget.TextView(ctx);
                tv.setTag("qqlite_zan_names");
                tv.setTextSize(11.5f);
                tv.setTextColor(-6643546);
                tv.setSingleLine(false);
                tv.setMaxLines(20);
                tv.setEllipsize(null);
                tv.setLineSpacing(0f, 1.15f);
                int pad = (int) (ctx.getResources().getDisplayMetrics().density * 8);
                tv.setPadding(pad, 2, pad, 6);
                android.widget.LinearLayout.LayoutParams lp =
                        new android.widget.LinearLayout.LayoutParams(-1, -2);
                int idx = insertAfter == null ? -1 : insertParent.indexOfChild(insertAfter);
                if (idx < 0) {
                    insertParent.addView(tv, lp);
                } else {
                    insertParent.addView(tv, Math.min(idx + 1, insertParent.getChildCount()), lp);
                }
            } else {
                tv = exist;
                tv.setVisibility(android.view.View.VISIBLE);
            }
            tv.setText("赞：" + zanText);
        } catch (Throwable ignored) {
        }
    }

    private static boolean isHorizontal(android.view.ViewGroup vg) {
        try {
            return vg instanceof android.widget.LinearLayout
                    && ((android.widget.LinearLayout) vg).getOrientation()
                    == android.widget.LinearLayout.HORIZONTAL;
        } catch (Throwable ignored) {
            return false;
        }
    }

    /** 找卡片里第一个纵向容器作为插入父节点 */
    private static android.view.ViewGroup findVerticalContainer(android.view.View root) {
        try {
            java.util.ArrayList<android.view.View> all = new java.util.ArrayList<>();
            collectViews(root, all, 0);
            for (android.view.View v : all) {
                if (v instanceof android.widget.LinearLayout
                        && ((android.widget.LinearLayout) v).getOrientation()
                        == android.widget.LinearLayout.VERTICAL) {
                    return (android.view.ViewGroup) v;
                }
            }
            if (root instanceof android.view.ViewGroup) {
                return (android.view.ViewGroup) root;
            }
        } catch (Throwable ignored) {}
        return null;
    }

    /**
     * 找"点赞"锚点：优先找显示纯数字的 TextView（点赞数），
     * 找不到就取卡片底部最后一个横向容器。
     */
    private static android.view.View findZanAnchor(android.view.View root) {
        try {
            java.util.ArrayList<android.view.View> all = new java.util.ArrayList<>();
            collectViews(root, all, 0);
            /* 1) 纯数字 TextView 且长度<=5（点赞数） */
            android.view.View digitTv = null;
            for (android.view.View v : all) {
                if (!(v instanceof android.widget.TextView)) continue;
                String txt = String.valueOf(((android.widget.TextView) v).getText()).trim();
                if (txt.isEmpty() || txt.length() > 5) continue;
                boolean allDigit = true;
                for (int i = 0; i < txt.length(); i++) {
                    if (!Character.isDigit(txt.charAt(i))) { allDigit = false; break; }
                }
                if (!allDigit) continue;
                if ((int) ((android.widget.TextView) v).getTextSize() > 60) continue;
                digitTv = v;
            }
            if (digitTv != null && digitTv.getParent() instanceof android.view.ViewGroup) {
                return digitTv;
            }
            /* 2) 兜底：最靠下的横向容器 */
            android.view.View best = null;
            int bestBottom = -1;
            for (android.view.View v : all) {
                if (!(v instanceof android.widget.LinearLayout)) continue;
                android.widget.LinearLayout ll = (android.widget.LinearLayout) v;
                if (ll.getOrientation() != android.widget.LinearLayout.HORIZONTAL) continue;
                int b = v.getBottom();
                if (b > bestBottom) {
                    bestBottom = b;
                    best = v;
                }
            }
            return best;
        } catch (Throwable ignored) {
            return null;
        }
    }

    private static void collectViews(android.view.View root, java.util.ArrayList<android.view.View> out, int depth) {
        if (root == null || depth > 20 || out.size() > 300) {
            return;
        }
        out.add(root);
        if (root instanceof android.view.ViewGroup) {
            android.view.ViewGroup vg = (android.view.ViewGroup) root;
            for (int i = 0; i < vg.getChildCount(); i++) {
                collectViews(vg.getChildAt(i), out, depth + 1);
            }
        }
    }

    /** 深度遍历找 id 名含关键字的 View（ViewBinding 混淆后字段拿不到，用资源名匹配） */
    private static android.view.View findViewByNameContains(android.view.View root, String keyword) {
        try {
            android.content.res.Resources res = root.getResources();
            String pkg = root.getContext().getPackageName();
            java.util.ArrayDeque<android.view.View> queue = new java.util.ArrayDeque<>();
            queue.add(root);
            int visited = 0;
            while (!queue.isEmpty() && visited < 60) {
                android.view.View v = queue.poll();
                visited++;
                try {
                    int id = v.getId();
                    if (id != android.view.View.NO_ID) {
                        String name = res.getResourceEntryName(id);
                        if (name != null && name.toLowerCase().contains(keyword)) {
                            return v;
                        }
                    }
                } catch (Throwable ignored) {}
                if (v instanceof android.view.ViewGroup) {
                    android.view.ViewGroup vg = (android.view.ViewGroup) v;
                    for (int i = 0; i < vg.getChildCount(); i++) {
                        queue.add(vg.getChildAt(i));
                    }
                }
            }
        } catch (Throwable ignored) {}
        return null;
    }

    /**
     * 记录宿主主列表 Fragment（ChatListFragment）实例。
     * 逆向：ChatListFragment.onCreate 会把自身写入 WatchRecentItemBuilder.d，
     * 但实测该静态字段可能未就绪；直接 hook onCreate 抓实例最可靠。
     */
    private static void installChatListFragmentHook(java.lang.ClassLoader classLoader) {
        try {
            sHostCl = classLoader;
            java.lang.Class<?> fragCls = java.lang.Class.forName(
                    "com.tencent.qqnt.watch.chat.ui.ChatListFragment", false, classLoader);
            java.lang.reflect.Method onCreate = null;
            for (java.lang.reflect.Method m : fragCls.getDeclaredMethods()) {
                if (m.getName().equals("onCreate") && m.getParameterTypes().length == 1
                        && m.getParameterTypes()[0] == android.os.Bundle.class) {
                    onCreate = m;
                    break;
                }
            }
            if (onCreate == null) {
                com.tencent.qqlitepro.util.Log.w("ChatListFragment.onCreate 未找到");
                return;
            }
            onCreate.setAccessible(true);
            de.robv.android.xposed.XposedBridge.hookMethod(onCreate, new de.robv.android.xposed.XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) {
                    sChatListFragment = param.thisObject;
                    com.tencent.qqlitepro.util.Log.i("ChatListFragment 实例已记录（导航可用）");
                }
            });
            com.tencent.qqlitepro.util.Log.i("已安装 ChatListFragment 实例捕获 hook");
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("安装 ChatListFragment hook 失败: " + th);
        }
    }

    /**
     * 聊天界面左滑（SettingFrame）注入"消息数据"入口。
     * 逆向自宿主模组 ChatSearchKt.addChatSearchEntry：
     *   SettingFrame.i(ScrollView) -> childAt(0) 是竖向 LinearLayout 条目容器，
     *   用 setting_item layout inflate 条目（icon+desc），addView 后 CardMarginUnify.cardMargin。
     * 我们 hook SettingFrame.onViewCreated_0_1 的 after（那里宿主模组也注入自己的 entry），
     * 再从 fragment arguments 读 key_bundle_peer_id / key_bundle_chat_nick 定位当前群。
     */
    private static void installGroupStatEntryHook(java.lang.ClassLoader classLoader) {
        try {
            java.lang.Class<?> frameCls = java.lang.Class.forName(
                    "com.tencent.watch.aio_impl.ui.frames.SettingFrame", false, classLoader);
            java.lang.reflect.Method target = null;
            for (java.lang.reflect.Method m : frameCls.getDeclaredMethods()) {
                if (m.getName().equals("onViewCreated_0_1")) {
                    target = m;
                    break;
                }
            }
            if (target == null) {
                // 某些版本可能叫 onViewCreated_0
                for (java.lang.reflect.Method m : frameCls.getDeclaredMethods()) {
                    if (java.lang.reflect.Modifier.isPrivate(m.getModifiers())
                            && m.getName().startsWith("onViewCreated")
                            && m.getParameterTypes().length == 2) {
                        target = m;
                        break;
                    }
                }
            }
            if (target == null) {
                com.tencent.qqlitepro.util.Log.w("SettingFrame 注入点未找到");
                return;
            }
            target.setAccessible(true);
            de.robv.android.xposed.XposedBridge.hookMethod(target, new de.robv.android.xposed.XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) {
                    try {
                        Object fragment = param.thisObject;
                        // 只在群聊（chatType=2）时注入
                        Object args = null;
                        try {
                            args = de.robv.android.xposed.XposedHelpers.callMethod(fragment, "requireArguments", new Object[0]);
                        } catch (Throwable ignored) {}
                        if (args == null) {
                            try {
                                args = de.robv.android.xposed.XposedHelpers.getObjectField(fragment, "mArguments");
                            } catch (Throwable ignored) {}
                        }
                        if (args == null) {
                            return;
                        }
                        int chatType = (args instanceof android.os.Bundle)
                                ? ((android.os.Bundle) args).getInt("key_bundle_chat_type", 0) : 0;
                        if (chatType != 2) {
                            return;
                        }
                        long peerId = 0L;
                        try {
                            String pid = (args instanceof android.os.Bundle)
                                    ? ((android.os.Bundle) args).getString("key_bundle_peer_id") : null;
                            peerId = pid == null ? 0L : Long.parseLong(pid.trim());
                        } catch (Throwable ignored) {}
                        String nick = "";
                        try {
                            nick = (args instanceof android.os.Bundle)
                                    ? ((android.os.Bundle) args).getString("key_bundle_chat_nick") : null;
                        } catch (Throwable ignored) {}
                        if (peerId <= 0) {
                            return;
                        }
                        final long fUin = peerId;
                        final String fNick = nick == null || nick.isEmpty() ? String.valueOf(peerId) : nick;
                        android.os.Handler h = new android.os.Handler(android.os.Looper.getMainLooper());
                        h.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    addGroupStatEntry(fragment, fUin, fNick);
                                } catch (Throwable th) {
                                    com.tencent.qqlitepro.util.Log.w("注入消息数据入口失败: " + th);
                                }
                            }
                        }, 260L);
                    } catch (Throwable ignored) {
                    }
                }
            });
            com.tencent.qqlitepro.util.Log.i("已安装聊天左滑面板消息数据注入 hook");
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("安装左滑面板 hook 失败: " + th);
        }
    }

    /** 仿 ChatSearchKt.addChatSearchEntry 注入一个"消息数据"条目 */
    private static void addGroupStatEntry(Object fragment, long groupUin, String groupName) {
        try {
            android.widget.ScrollView scroll = (android.widget.ScrollView) de.robv.android.xposed.XposedHelpers.getObjectField(fragment, "i");
            if (scroll == null || scroll.getChildCount() == 0) {
                return;
            }
            android.view.View child0 = scroll.getChildAt(0);
            if (!(child0 instanceof android.widget.LinearLayout)) {
                return;
            }
            android.widget.LinearLayout container = (android.widget.LinearLayout) child0;
            android.content.Context ctx = null;
            try {
                ctx = (android.content.Context) de.robv.android.xposed.XposedHelpers.callMethod(fragment, "requireContext", new Object[0]);
            } catch (Throwable ignored) {}
            if (ctx == null) {
                ctx = container.getContext();
            }
            android.content.res.Resources res = ctx.getResources();
            String pkg = ctx.getPackageName();
            int idDesc = res.getIdentifier("desc", "id", pkg);
            int layoutSettingItem = res.getIdentifier("setting_item", "layout", pkg);
            if (idDesc == 0 || layoutSettingItem == 0) {
                com.tencent.qqlitepro.util.Log.w("setting_item/desc 资源未找到");
                return;
            }
            // 查重：已有"消息数据"就不再注入
            for (int i = 0; i < container.getChildCount(); ++i) {
                android.view.View row = container.getChildAt(i);
                android.widget.TextView desc = (android.widget.TextView) row.findViewById(idDesc);
                if (desc != null && "消息数据".equals(String.valueOf(desc.getText()))) {
                    return;
                }
            }
            android.view.View entry = android.view.LayoutInflater.from(ctx).inflate(layoutSettingItem, container, false);
            try {
                android.widget.ImageView icon = (android.widget.ImageView) entry.findViewById(
                        res.getIdentifier("icon", "id", pkg));
                if (icon != null) {
                    // 用宿主搜索图标样式（无合适资源则保持默认）
                }
            } catch (Throwable ignored) {}
            android.widget.TextView desc = (android.widget.TextView) entry.findViewById(idDesc);
            if (desc != null) {
                desc.setText("消息数据");
            }
            final long fUin = groupUin;
            final String fName = groupName;
            entry.setOnClickListener(new android.view.View.OnClickListener() {
                @Override
                public void onClick(android.view.View v) {
                    try {
                        com.tencent.qqlitepro.hook.SettingsInjector.openGroupMsgStats(v.getContext(), fUin, fName);
                    } catch (Throwable th) {
                        com.tencent.qqlitepro.util.Log.w("打开消息数据失败: " + th);
                    }
                }
            });
            container.addView(entry, Math.min(4, container.getChildCount()));
            try {
                java.lang.Class<?> cardCls = java.lang.Class.forName(
                        "momoi.mod.qqpro.hook.view.CardMarginUnifyKt", false, ctx.getClassLoader());
                for (java.lang.reflect.Method m : cardCls.getDeclaredMethods()) {
                    if (m.getName().equals("cardMargin") && m.getParameterTypes().length == 1) {
                        m.setAccessible(true);
                        m.invoke(null, entry);
                        break;
                    }
                }
            } catch (Throwable ignored) {
            }
            com.tencent.qqlitepro.util.Log.i("消息数据入口已注入: group=" + groupUin);
        } catch (Throwable th) {
            com.tencent.qqlitepro.util.Log.w("addGroupStatEntry 异常: " + th);
        }
    }

    /*
     * 全局蓝奏云链接识别。
     *
     * 只 hook TextView.setText(CharSequence)：
     *   - after 阶段拿到最终文本，若含蓝奏云链接则加可点击 Span
     *   - 命中率 100%（所有文本最终都会走 setText）
     *   - 用"是否包含 lanzou 关键字"做一次廉价预判，避免给每个 TextView 都做正则
     *   - 通过 Tag 去重，同一个文本不会重复处理
     */
    /** 在 CharSequence 上做大小写无关的包含判断，不产生临时字符串 */
    private static boolean containsIgnoreCase(CharSequence cs, String needle) {
        int n = needle.length();
        int max = cs.length() - n;
        for (int i = 0; i <= max; i++) {
            boolean hit = true;
            for (int j = 0; j < n; j++) {
                char a = cs.charAt(i + j);
                char b = needle.charAt(j);
                if (a != b && java.lang.Character.toLowerCase(a) != b) {
                    hit = false;
                    break;
                }
            }
            if (hit) {
                return true;
            }
        }
        return false;
    }

    private static void installLanzouLinkHook(java.lang.ClassLoader classLoader) {
        try {
            java.lang.Class<?> tvCls = android.widget.TextView.class;
            java.lang.reflect.Method setText = tvCls.getMethod("setText", java.lang.CharSequence.class);
            de.robv.android.xposed.XposedBridge.hookMethod(setText, new de.robv.android.xposed.XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) {
                    try {
                        Object self = param.thisObject;
                        if (!(self instanceof android.widget.TextView)) {
                            return;
                        }
                        android.widget.TextView tv = (android.widget.TextView) self;
                        CharSequence cs = tv.getText();
                        if (cs == null) {
                            return;
                        }
                        /*
                         * 性能优化：setText 调用极频繁（每帧可能几十次），
                         * 绝不能在这里做 toString()/toLowerCase() —— 会产生大量临时对象导致掉帧。
                         * 改为：
                         *   1) 只在长度够时才检查
                         *   2) 直接在 CharSequence 上做大小写无关的 contains("lanzou")，零分配
                         */
                        int len = cs.length();
                        if (len < 12 || len > 4000) {
                            return;
                        }
                        if (!containsIgnoreCase(cs, "lanzou")) {
                            return;
                        }
                        String text = cs.toString();
                        if (!com.tencent.qqlitepro.util.Lanzou.contains(text)) {
                            return;
                        }
                        com.tencent.qqlitepro.hook.SettingsInjector.linkifyWithBrowser(tv);
                    } catch (java.lang.Throwable ignored) {
                    }
                }
            });
            com.tencent.qqlitepro.util.Log.i("已安装全局蓝奏云链接识别 hook");
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("安装蓝奏云链接 hook 失败: " + th);
        }
    }

    public static void patchBaseCell(java.lang.ClassLoader classLoader) {
        com.tencent.qqlitepro.hook.FakeMediaReceiver.init(classLoader);
        try {
            java.lang.reflect.Method[] baseMethods = java.lang.Class
                    .forName("com.tencent.watch.aio_impl.ui.cell.base.BaseWatchItemCell", false, classLoader)
                    .getDeclaredMethods();
            boolean hooked = false;
            for (java.lang.reflect.Method m : baseMethods) {
                if (!m.getName().equals("i")) {
                    continue;
                }
                java.lang.Class<?>[] parameterTypes = m.getParameterTypes();
                if (parameterTypes.length >= 2 && android.view.View.class.isAssignableFrom(parameterTypes[0])) {
                    de.robv.android.xposed.XposedBridge.hookMethod(m, baseCellBindHook());
                    com.tencent.qqlitepro.util.Log.i("已 hook BaseWatchItemCell.i（红边+角标确定性入口）");
                    hooked = true;
                    break;
                }
            }
            if (!hooked) {
                com.tencent.qqlitepro.util.Log.i("BaseWatchItemCell.i 未找到目标方法");
            }
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("hook BaseWatchItemCell 失败: " + th);
        }
        try {
            java.lang.reflect.Method[] picMethods = java.lang.Class
                    .forName("com.tencent.watch.aio_impl.ui.cell.pic.WatchPicItemCell", false, classLoader)
                    .getDeclaredMethods();
            boolean picHooked = false;
            for (java.lang.reflect.Method m : picMethods) {
                if (m.getName().equals("d") && m.getParameterTypes().length >= 2
                        && android.view.View.class.isAssignableFrom(m.getParameterTypes()[0])) {
                    de.robv.android.xposed.XposedBridge.hookMethod(m, picCellBindHook());
                    com.tencent.qqlitepro.util.Log.i("已 hook WatchPicItemCell.d（图片消息绑定入口）");
                    picHooked = true;
                    break;
                }
            }
            if (!picHooked) {
                com.tencent.qqlitepro.util.Log.w("WatchPicItemCell.d 未找到目标方法");
            }
        } catch (java.lang.Throwable th2) {
            com.tencent.qqlitepro.util.Log.w("hook WatchPicItemCell 失败: " + th2);
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.UiPatcher$1, reason: invalid class name */
    static class AnonymousClass1 extends de.robv.android.xposed.XC_MethodHook {
        AnonymousClass1() {
        }

        protected void afterHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            try {
                java.lang.Object obj = (methodHookParam.args == null || methodHookParam.args.length < 1) ? null : methodHookParam.args[0];
                if ((obj instanceof android.view.View) && methodHookParam.args != null && methodHookParam.args.length >= 2) {
                    android.view.View view = (android.view.View) obj;
                    java.lang.Object obj2 = methodHookParam.args[1];
                    java.lang.Object objObjField = com.tencent.qqlitepro.hook.UiPatcher.objField(obj2, "d");
                    if (objObjField != null) {
                        obj2 = objObjField;
                    }
                    java.lang.String strSafeString = obj2 != null ? com.tencent.qqlitepro.hook.UiPatcher.safeString(com.tencent.qqlitepro.hook.UiPatcher.objField(obj2, "msgId"), null) : null;
                    if (strSafeString == null) {
                        return;
                    }
                    com.tencent.qqlitepro.hook.UiPatcher.ROOT_MSGID.put(view, strSafeString);
                    com.tencent.qqlitepro.hook.UiPatcher.FakeDetect fakeDetectDetectFake = com.tencent.qqlitepro.hook.UiPatcher.detectFake(obj2, strSafeString);
                    if (fakeDetectDetectFake != null && fakeDetectDetectFake.fake) {
                        com.tencent.qqlitepro.hook.UiPatcher.applyFakeMark(view, strSafeString, fakeDetectDetectFake.badge, fakeDetectDetectFake.self, fakeDetectDetectFake.shard, fakeDetectDetectFake.index, fakeDetectDetectFake.count);
                        com.tencent.qqlitepro.hook.UiPatcher.scheduleShardCollect(true, fakeDetectDetectFake.path, fakeDetectDetectFake.shard, fakeDetectDetectFake.collectKey);
                    } else {
                        com.tencent.qqlitepro.hook.UiPatcher.restoreFakeVoiceMark(view);
                        if (com.tencent.qqlitepro.hook.UiPatcher.PIC_STRUCTURE_DUMPED.add(strSafeString)) {
                            com.tencent.qqlitepro.hook.UiPatcher.dumpPicElement(obj2);
                        }
                        com.tencent.qqlitepro.hook.UiPatcher.ensureReceiverMedia(view, obj2, strSafeString);
                    }
                }
            } catch (java.lang.Throwable unused) {
            }
        }
    }

    private static de.robv.android.xposed.XC_MethodHook picCellBindHook() {
        return new com.tencent.qqlitepro.hook.UiPatcher.AnonymousClass1();
    }

    public static void onMessagesRecalled() {
        try {
            if (ROOT_MSGID.isEmpty()) {
                return;
            }
            android.os.Handler handler = new android.os.Handler(android.os.Looper.getMainLooper());
            handler.post(new com.tencent.qqlitepro.hook.UiPatcher$$ExternalSyntheticLambda13());
            handler.postDelayed(new com.tencent.qqlitepro.hook.UiPatcher.AnonymousClass2(new int[]{0}, handler), 200L);
        } catch (java.lang.Throwable unused) {
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.UiPatcher$2, reason: invalid class name */
    static class AnonymousClass2 implements java.lang.Runnable {
        final /* synthetic */ android.os.Handler val$handler;
        final /* synthetic */ int[] val$tries;

        AnonymousClass2(int[] iArr, android.os.Handler handler) {
            this.val$tries = iArr;
            this.val$handler = handler;
        }

        @Override // java.lang.Runnable
        public void run() {
            com.tencent.qqlitepro.hook.UiPatcher.refreshNow();
            int[] iArr = this.val$tries;
            int i = iArr[0] + 1;
            iArr[0] = i;
            if (i < 3) {
                this.val$handler.postDelayed(this, 300L);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void refreshNow() {
        int iMode;
        com.tencent.qqlitepro.cache.CachedMsg cachedMsg;
        try {
            iMode = com.tencent.qqlitepro.util.Config.mode(appContext());
        } catch (java.lang.Throwable unused) {
            iMode = 1;
        }
        try {
            com.tencent.qqlitepro.cache.MsgCache msgCache = com.tencent.qqlitepro.cache.MsgCache.get();
            for (java.util.Map.Entry<android.view.View, java.lang.String> entry : ROOT_MSGID.entrySet()) {
                try {
                    android.view.View key = entry.getKey();
                    java.lang.String value = entry.getValue();
                    if (key != null && value != null && (key.getWidth() > 0 || key.getHeight() > 0)) {
                        if (key.isShown() && (cachedMsg = msgCache.get(value)) != null && java.lang.Boolean.TRUE.equals(de.robv.android.xposed.XposedHelpers.getObjectField(cachedMsg, "recalled")) && iMode == 1 && isEnabled()) {
                            applyRedMark(key, cachedMsg, value);
                        }
                    }
                } catch (java.lang.Throwable unused2) {
                }
            }
        } catch (java.lang.Throwable unused3) {
        }
    }

    private static void forceRedraw() {
        try {
            new android.os.Handler(android.os.Looper.getMainLooper()).post(new com.tencent.qqlitepro.hook.UiPatcher$$ExternalSyntheticLambda31());
        } catch (java.lang.Throwable unused) {
        }
    }

    static /* synthetic */ void lambda$forceRedraw$1() {
        try {
            for (java.util.Map.Entry<android.view.View, java.lang.String> entry : ROOT_MSGID.entrySet()) {
                android.view.View key = entry.getKey();
                java.lang.String value = entry.getValue();
                if (key != null && value != null && (key.getWidth() > 0 || key.getHeight() > 0)) {
                    if (key.isShown()) {
                        java.lang.String str = EDITED_TEXT.get(value);
                        if (str != null) {
                            android.widget.TextView textViewFindTextLabel = findTextLabel(key, value);
                            if (textViewFindTextLabel != null) {
                                textViewFindTextLabel.setText(str);
                            }
                            applyEditedMark(key, value);
                        } else {
                            restoreEditedMark(key);
                        }
                        try {
                            key.invalidate();
                        } catch (java.lang.Throwable unused) {
                        }
                    }
                }
            }
        } catch (java.lang.Throwable unused2) {
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.UiPatcher$3, reason: invalid class name */
    static class AnonymousClass3 extends de.robv.android.xposed.XC_MethodHook {
        AnonymousClass3() {
        }

        protected void afterHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            int iMode;
            try {
                boolean z = false;
                java.lang.Object obj = (methodHookParam.args == null || methodHookParam.args.length < 1) ? null : methodHookParam.args[0];
                java.lang.Object obj2 = (methodHookParam.args == null || methodHookParam.args.length < 2) ? null : methodHookParam.args[1];
                if ((obj instanceof android.view.View) && obj2 != null) {
                    android.view.View view = (android.view.View) obj;
                    java.lang.Object objObjField = com.tencent.qqlitepro.hook.UiPatcher.objField(obj2, "d");
                    java.lang.String strSafeString = objObjField != null ? com.tencent.qqlitepro.hook.UiPatcher.safeString(com.tencent.qqlitepro.hook.UiPatcher.objField(objObjField, "msgId"), null) : null;
                    if (strSafeString == null) {
                        return;
                    }
                    com.tencent.qqlitepro.hook.UiPatcher.ROOT_MSGID.put(view, strSafeString);
                    com.tencent.qqlitepro.cache.CachedMsg cachedMsg = com.tencent.qqlitepro.cache.MsgCache.get().get(strSafeString);
                    if (cachedMsg != null && java.lang.Boolean.TRUE.equals(de.robv.android.xposed.XposedHelpers.getObjectField(cachedMsg, "recalled"))) {
                        z = true;
                    }
                    try {
                        iMode = com.tencent.qqlitepro.util.Config.mode(view.getContext());
                    } catch (java.lang.Throwable unused) {
                        iMode = 1;
                    }
                    if (z && iMode == 1 && com.tencent.qqlitepro.hook.UiPatcher.isEnabled()) {
                        com.tencent.qqlitepro.hook.UiPatcher.applyRedMark(view, cachedMsg, strSafeString);
                    } else {
                        com.tencent.qqlitepro.hook.UiPatcher.restoreRedMark(view);
                    }
                    try {
                        com.tencent.qqlitepro.hook.UiPatcher.bindEdit(view, obj2, strSafeString);
                    } catch (java.lang.Throwable unused2) {
                    }
                    java.lang.String strRecordText = com.tencent.qqlitepro.hook.UiPatcher.recordText(objObjField);
                    if (strRecordText != null && !strRecordText.isEmpty()) {
                        com.tencent.qqlitepro.hook.UiPatcher.rememberOriginal(strSafeString, strRecordText);
                    }
                    java.lang.String str = (java.lang.String) com.tencent.qqlitepro.hook.UiPatcher.EDITED_TEXT.get(strSafeString);
                    if (str != null) {
                        android.widget.TextView textViewFindTextLabel = com.tencent.qqlitepro.hook.UiPatcher.findTextLabel(view, strSafeString);
                        if (textViewFindTextLabel != null) {
                            textViewFindTextLabel.setText(str);
                        }
                        com.tencent.qqlitepro.hook.UiPatcher.applyEditedMark(view, strSafeString);
                    } else {
                        com.tencent.qqlitepro.hook.UiPatcher.restoreEditedMark(view);
                    }
                    com.tencent.qqlitepro.hook.UiPatcher.FakeDetect fakeDetectDetectFake = com.tencent.qqlitepro.hook.UiPatcher.detectFake(objObjField, strSafeString);
                    if (fakeDetectDetectFake != null && fakeDetectDetectFake.fake) {
                        com.tencent.qqlitepro.hook.UiPatcher.applyFakeMark(view, strSafeString, fakeDetectDetectFake.badge, fakeDetectDetectFake.self, fakeDetectDetectFake.shard, fakeDetectDetectFake.index, fakeDetectDetectFake.count);
                        com.tencent.qqlitepro.hook.UiPatcher.scheduleShardCollect(fakeDetectDetectFake.imageMode, fakeDetectDetectFake.path, fakeDetectDetectFake.shard, fakeDetectDetectFake.collectKey);
                    } else {
                        com.tencent.qqlitepro.hook.UiPatcher.restoreFakeVoiceMark(view);
                        com.tencent.qqlitepro.hook.UiPatcher.ensureReceiverMedia(view, objObjField, strSafeString);
                    }
                }
            } catch (java.lang.Throwable unused3) {
            }
        }
    }

    private static de.robv.android.xposed.XC_MethodHook baseCellBindHook() {
        return new com.tencent.qqlitepro.hook.UiPatcher.AnonymousClass3();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void applyRedMark(android.view.View view, java.lang.Object obj, java.lang.String str) {
        try {
            java.util.ArrayList<android.view.View> arrayList = new java.util.ArrayList();
            collectDecorated(view, arrayList);
            for (android.view.View view2 : arrayList) {
                try {
                    view2.setForeground(null);
                } catch (java.lang.Throwable unused) {
                }
                DECORATED.remove(view2);
            }
            android.view.View viewResolveStrokeBubble = resolveStrokeBubble(view, str);
            if (viewResolveStrokeBubble != null) {
                java.util.Set<android.view.View> set = DECORATED;
                if (set.contains(viewResolveStrokeBubble)) {
                    return;
                }
                viewResolveStrokeBubble.setForeground(buildRecalledDrawable(viewResolveStrokeBubble, (obj instanceof com.tencent.qqlitepro.cache.CachedMsg) && ((com.tencent.qqlitepro.cache.CachedMsg) obj).senderUid != null && ((com.tencent.qqlitepro.cache.CachedMsg) obj).senderUid.equals(com.tencent.qqlitepro.hook.MsgListenerHook.selfUid())));
                set.add(viewResolveStrokeBubble);
            }
        } catch (java.lang.Throwable unused2) {
        }
    }

    private static android.view.View resolveBubble(android.view.View view) {
        android.view.View viewFindCellBlock = findCellBlock(view);
        if (viewFindCellBlock != null) {
            try {
                java.lang.Object objCallMethod = de.robv.android.xposed.XposedHelpers.callMethod(viewFindCellBlock, "getContentWidget", new java.lang.Object[0]);
                if (objCallMethod instanceof android.view.View) {
                    return (android.view.View) objCallMethod;
                }
            } catch (java.lang.Throwable unused) {
            }
        }
        android.view.View[] viewArr = {null};
        bestLeafWithin(view, new int[]{-1}, viewArr);
        android.view.View view2 = viewArr[0];
        return view2 != null ? view2 : view;
    }

    private static android.view.View resolveStrokeBubble(android.view.View view, java.lang.String str) {
        android.view.View viewClimbToBubble;
        android.view.View viewResolveBubble = resolveBubble(view);
        if (viewResolveBubble == null) {
            return view;
        }
        int width = view.getWidth();
        int height = view.getHeight();
        if (width > 0 && height > 0 && viewResolveBubble.getWidth() > 0 && viewResolveBubble.getHeight() > 0 && viewResolveBubble.getWidth() * 1.05f >= width && viewResolveBubble.getHeight() * 1.2f >= height) {
            android.view.View viewFindNarrowBubble = null;
            try {
                android.widget.TextView textViewFindTextLabel = findTextLabel(view, str);
                if (textViewFindTextLabel != null && (viewClimbToBubble = climbToBubble(view, textViewFindTextLabel)) != view) {
                    viewFindNarrowBubble = viewClimbToBubble;
                }
            } catch (java.lang.Throwable unused) {
            }
            if (viewFindNarrowBubble == null) {
                viewFindNarrowBubble = findNarrowBubble(view, width, height);
            }
            if (viewFindNarrowBubble != null) {
                return viewFindNarrowBubble;
            }
        }
        return viewResolveBubble;
    }

    private static android.view.View findNarrowBubble(android.view.View view, int i, int i2) {
        android.view.View[] viewArr = {null};
        long[] jArr = {-1};
        java.util.ArrayDeque arrayDeque = new java.util.ArrayDeque();
        arrayDeque.add(view);
        while (!arrayDeque.isEmpty()) {
            android.view.View view2 = (android.view.View) arrayDeque.poll();
            if (view2 != null && view2 != view) {
                int width = view2.getWidth();
                int height = view2.getHeight();
                if (width > 0 && height > 0) {
                    float f = width;
                    float f2 = i;
                    if (f <= 0.92f * f2 && height <= i2 * 0.9f && f >= f2 * 0.18f) {
                        long j = ((long) width) * ((long) height);
                        if (j > jArr[0]) {
                            jArr[0] = j;
                            viewArr[0] = view2;
                        }
                    }
                }
            }
            if (view2 instanceof android.view.ViewGroup) {
                android.view.ViewGroup viewGroup = (android.view.ViewGroup) view2;
                for (int i3 = 0; i3 < viewGroup.getChildCount(); i3++) {
                    android.view.View childAt = viewGroup.getChildAt(i3);
                    if (childAt != null) {
                        arrayDeque.add(childAt);
                    }
                }
            }
        }
        return viewArr[0];
    }

    private static android.view.View climbToBubble(android.view.View view, android.view.View view2) {
        android.view.View view3 = view2;
        while (view2 != null && view2 != view) {
            android.view.View view4 = view2.getParent() instanceof android.view.View ? (android.view.View) view2.getParent() : null;
            if (view4 == null || view4 == view || (view.getWidth() > 0 && view4.getWidth() > 0 && view4.getWidth() * 1.05f >= view.getWidth())) {
                break;
            }
            view3 = view2;
            view2 = view4;
        }
        return view3;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void restoreRedMark(android.view.View view) {
        try {
            java.util.ArrayList<android.view.View> arrayList = new java.util.ArrayList();
            collectDecorated(view, arrayList);
            for (android.view.View view2 : arrayList) {
                try {
                    view2.setForeground(null);
                } catch (java.lang.Throwable unused) {
                }
                DECORATED.remove(view2);
            }
        } catch (java.lang.Throwable unused2) {
        }
    }

    private static void collectDecorated(android.view.View view, java.util.List<android.view.View> list) {
        if (view == null) {
            return;
        }
        if (DECORATED.contains(view)) {
            list.add(view);
        }
        if (view instanceof android.view.ViewGroup) {
            android.view.ViewGroup viewGroup = (android.view.ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                collectDecorated(viewGroup.getChildAt(i), list);
            }
        }
    }

    private static android.view.View resolveContent(android.view.View view) {
        android.view.View viewFindCellBlock = findCellBlock(view);
        if (viewFindCellBlock != null) {
            try {
                java.lang.Object objCallMethod = de.robv.android.xposed.XposedHelpers.callMethod(viewFindCellBlock, "getContentWidget", new java.lang.Object[0]);
                view = objCallMethod instanceof android.view.View ? (android.view.View) objCallMethod : viewFindCellBlock;
            } catch (java.lang.Throwable unused) {
            }
        }
        android.view.View[] viewArr = {null};
        bestLeafWithin(view, new int[]{-1}, viewArr);
        android.view.View view2 = viewArr[0];
        return view2 != null ? view2 : view;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.lang.String recordText(java.lang.Object obj) {
        boolean z = false;
        java.lang.Object objObjField;
        java.lang.Object objObjField2;
        try {
            java.lang.String[] strArr = {"elements", "msgElements"};
            for (int i = 0; i < 2; i++) {
                java.lang.String str = strArr[i];
                java.lang.StringBuilder sb = new java.lang.StringBuilder();
                java.lang.Object objObjField3 = objField(obj, str);
                if (objObjField3 instanceof java.lang.Iterable) {
                    java.util.Iterator it = ((java.lang.Iterable) objObjField3).iterator();
                    z = false;
                    while (it.hasNext()) {
                        java.lang.Object objObjField4 = objField(it.next(), "textElement");
                        if (objObjField4 != null && (objObjField2 = objField(objObjField4, "content")) != null && !objObjField2.toString().isEmpty()) {
                            if (z) {
                                sb.append(' ');
                            } else {
                                z = true;
                            }
                            sb.append(objObjField2);
                        }
                    }
                } else {
                    if (objObjField3 != null && objObjField3.getClass().isArray()) {
                        int length = java.lang.reflect.Array.getLength(objObjField3);
                        boolean z2 = false;
                        for (int i2 = 0; i2 < length; i2++) {
                            java.lang.Object objObjField5 = objField(java.lang.reflect.Array.get(objObjField3, i2), "textElement");
                            if (objObjField5 != null && (objObjField = objField(objObjField5, "content")) != null && !objObjField.toString().isEmpty()) {
                                if (z2) {
                                    sb.append(' ');
                                } else {
                                    z2 = true;
                                }
                                sb.append(objObjField);
                            }
                        }
                        z = z2;
                    }
                }
                if (z) {
                    return sb.toString();
                }
            }
            return null;
        } catch (java.lang.Throwable unused) {
            return null;
        }
    }

    /* JADX WARN: Code duplicated, block: B:9:0x0017  */
    private static android.view.View resolveMessageWidget(android.view.View view, java.lang.String str) {
        android.view.View view2 = view;
        android.widget.TextView textViewFindTextByContent;
        android.widget.TextView textViewFindTextByContent2;
        android.view.View viewFindCellBlock = findCellBlock(view);
        if (viewFindCellBlock != null) {
            try {
                java.lang.Object objCallMethod = de.robv.android.xposed.XposedHelpers.callMethod(viewFindCellBlock, "getContentWidget", new java.lang.Object[0]);
                if (objCallMethod instanceof android.view.View) {
                    view2 = (android.view.View) objCallMethod;
                } else {
                    view2 = view;
                }
            } catch (java.lang.Throwable unused) {
            }
        } else {
            view2 = view;
        }
        java.lang.String str2 = EDITED_TEXT.get(str);
        if (str2 != null && !str2.trim().isEmpty() && (textViewFindTextByContent2 = findTextByContent(view2, str2.trim())) != null) {
            return textViewFindTextByContent2;
        }
        java.lang.String str3 = EDITED_ORIGINAL.get(str);
        if (str3 != null && !str3.trim().isEmpty() && (textViewFindTextByContent = findTextByContent(view2, str3.trim())) != null) {
            return textViewFindTextByContent;
        }
        android.widget.TextView textViewFindLargestText = findLargestText(view2);
        return textViewFindLargestText != null ? textViewFindLargestText : resolveContent(view);
    }

    private static android.widget.TextView findTextByContent(android.view.View view, java.lang.String str) {
        android.widget.TextView textView;
        java.lang.CharSequence text;
        android.widget.TextView textView2 = null;
        if (str != null && !str.isEmpty()) {
            java.util.ArrayDeque arrayDeque = new java.util.ArrayDeque();
            if (view instanceof android.view.ViewGroup) {
                android.view.ViewGroup viewGroup = (android.view.ViewGroup) view;
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    android.view.View childAt = viewGroup.getChildAt(i);
                    if (childAt != null) {
                        arrayDeque.add(childAt);
                    }
                }
            }
            while (!arrayDeque.isEmpty()) {
                android.view.View view2 = (android.view.View) arrayDeque.poll();
                if ((view2 instanceof android.widget.TextView) && (text = (textView = (android.widget.TextView) view2).getText()) != null && text.length() > 0) {
                    java.lang.String strTrim = text.toString().trim();
                    if (strTrim.equals(str)) {
                        return textView;
                    }
                    if (textView2 == null && strTrim.contains(str)) {
                        textView2 = textView;
                    }
                }
                if (view2 instanceof android.view.ViewGroup) {
                    android.view.ViewGroup viewGroup2 = (android.view.ViewGroup) view2;
                    for (int i2 = 0; i2 < viewGroup2.getChildCount(); i2++) {
                        android.view.View childAt2 = viewGroup2.getChildAt(i2);
                        if (childAt2 != null) {
                            arrayDeque.add(childAt2);
                        }
                    }
                }
            }
        }
        return textView2;
    }

    private static android.widget.TextView findLargestText(android.view.View view) {
        android.widget.TextView textView;
        java.lang.CharSequence text;
        int width;
        int[] iArr = {-1};
        android.widget.TextView[] textViewArr = {null};
        java.util.ArrayDeque arrayDeque = new java.util.ArrayDeque();
        if (view instanceof android.view.ViewGroup) {
            android.view.ViewGroup viewGroup = (android.view.ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                android.view.View childAt = viewGroup.getChildAt(i);
                if (childAt != null) {
                    arrayDeque.add(childAt);
                }
            }
        }
        while (!arrayDeque.isEmpty()) {
            android.view.View view2 = (android.view.View) arrayDeque.poll();
            if ((view2 instanceof android.widget.TextView) && (text = (textView = (android.widget.TextView) view2).getText()) != null && text.length() > 0 && (width = view2.getWidth() * view2.getHeight()) > iArr[0]) {
                iArr[0] = width;
                textViewArr[0] = textView;
            }
            if (view2 instanceof android.view.ViewGroup) {
                android.view.ViewGroup viewGroup2 = (android.view.ViewGroup) view2;
                for (int i2 = 0; i2 < viewGroup2.getChildCount(); i2++) {
                    android.view.View childAt2 = viewGroup2.getChildAt(i2);
                    if (childAt2 != null) {
                        arrayDeque.add(childAt2);
                    }
                }
            }
        }
        return textViewArr[0];
    }

    private static android.view.View findCellBlock(android.view.View view) {
        if (view == null) {
            return null;
        }
        java.util.ArrayDeque arrayDeque = new java.util.ArrayDeque();
        if (view instanceof android.view.ViewGroup) {
            android.view.ViewGroup viewGroup = (android.view.ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                android.view.View childAt = viewGroup.getChildAt(i);
                if (childAt != null) {
                    arrayDeque.add(childAt);
                }
            }
        }
        while (!arrayDeque.isEmpty()) {
            android.view.View view2 = (android.view.View) arrayDeque.poll();
            try {
                if (de.robv.android.xposed.XposedHelpers.callMethod(view2, "getContentWidget", new java.lang.Object[0]) instanceof android.view.View) {
                    return view2;
                }
            } catch (java.lang.Throwable unused) {
            }
            if (view2 instanceof android.view.ViewGroup) {
                android.view.ViewGroup viewGroup2 = (android.view.ViewGroup) view2;
                for (int i2 = 0; i2 < viewGroup2.getChildCount(); i2++) {
                    android.view.View childAt2 = viewGroup2.getChildAt(i2);
                    if (childAt2 != null) {
                        arrayDeque.add(childAt2);
                    }
                }
            }
        }
        return null;
    }

    private static void bestLeafWithin(android.view.View view, int[] iArr, android.view.View[] viewArr) {
        java.util.ArrayDeque arrayDeque = new java.util.ArrayDeque();
        if (view instanceof android.view.ViewGroup) {
            android.view.ViewGroup viewGroup = (android.view.ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                android.view.View childAt = viewGroup.getChildAt(i);
                if (childAt != null) {
                    arrayDeque.add(childAt);
                }
            }
        }
        while (!arrayDeque.isEmpty()) {
            android.view.View view2 = (android.view.View) arrayDeque.poll();
            considerContent(view2, iArr, viewArr);
            if (view2 instanceof android.view.ViewGroup) {
                android.view.ViewGroup viewGroup2 = (android.view.ViewGroup) view2;
                for (int i2 = 0; i2 < viewGroup2.getChildCount(); i2++) {
                    android.view.View childAt2 = viewGroup2.getChildAt(i2);
                    if (childAt2 != null) {
                        arrayDeque.add(childAt2);
                    }
                }
            }
        }
    }

    private static void considerContent(android.view.View view, int[] iArr, android.view.View[] viewArr) {
        int i;
        if (view == null) {
            return;
        }
        try {
            int width = view.getWidth();
            int height = view.getHeight();
            if (width > 0 && height > 0) {
                if ((view instanceof android.view.ViewGroup) && ((android.view.ViewGroup) view).getChildCount() != 0) {
                    return;
                }
                if (!(view instanceof android.widget.ImageView) && ((!(view instanceof android.widget.TextView) || ((android.widget.TextView) view).getText().toString().isEmpty()) && view.getBackground() == null && (android.os.Build.VERSION.SDK_INT < 23 || view.getForeground() == null))) {
                    return;
                }
                int iRound = java.lang.Math.round(view.getResources().getDisplayMetrics().density * 20.0f);
                if (width <= iRound || height <= iRound || (i = width * height) <= iArr[0]) {
                    return;
                }
                iArr[0] = i;
                viewArr[0] = view;
            }
        } catch (java.lang.Throwable unused) {
        }
    }

    private static android.graphics.drawable.Drawable buildRecalledDrawable(android.view.View view, boolean z) {
        return buildDecorDrawable(view, RED, BADGE_TEXT, z);
    }

    private static android.graphics.drawable.Drawable buildEditedDrawable(android.view.View view) {
        return buildDecorDrawable(view, LIGHT_BLUE, BADGE_EDITED, false);
    }

    private static android.graphics.drawable.Drawable buildFakeDrawable(android.view.View view, java.lang.String str, boolean z) {
        return buildFakeDrawable(view, str, z, true, true);
    }

    private static android.graphics.drawable.Drawable buildFakeDrawable(android.view.View view, java.lang.String str, boolean z, boolean z2, boolean z3) {
        return buildDecorDrawable(view, ORANGE, str, z, z2, z3);
    }

    private static android.graphics.drawable.Drawable buildDecorDrawable(android.view.View view, int i, java.lang.String str, boolean z) {
        return buildDecorDrawable(view, i, str, z, true, true);
    }

    private static android.graphics.drawable.Drawable buildDecorDrawable(android.view.View view, int i, java.lang.String str, boolean z, boolean z2, boolean z3) {
        float f = view.getResources().getDisplayMetrics().density;
        float f2 = 2.0f * f;
        int iMax = java.lang.Math.max(1, java.lang.Math.round(f2));
        float f3 = f * 8.0f;
        int iRound = java.lang.Math.round(10.0f * f);
        int iRound2 = java.lang.Math.round(f * 6.0f);
        int iRound3 = java.lang.Math.round(f2);
        android.graphics.Paint paint = new android.graphics.Paint();
        paint.setStyle(android.graphics.Paint.Style.STROKE);
        paint.setStrokeWidth(iMax);
        paint.setColor(i);
        paint.setAntiAlias(true);
        android.graphics.Paint paint2 = new android.graphics.Paint();
        paint2.setStyle(android.graphics.Paint.Style.FILL);
        paint2.setColor(i);
        paint2.setAntiAlias(true);
        android.text.TextPaint textPaint = new android.text.TextPaint(1);
        textPaint.setColor(-1);
        textPaint.setTextSize(iRound);
        android.graphics.RectF rectF = new android.graphics.RectF();
        android.graphics.RectF rectF2 = new android.graphics.RectF();
        float[] fArr = new float[8];
        fArr[0] = z2 ? f3 : 0.0f;
        fArr[1] = z2 ? f3 : 0.0f;
        fArr[2] = z2 ? f3 : 0.0f;
        fArr[3] = z2 ? f3 : 0.0f;
        fArr[4] = z3 ? f3 : 0.0f;
        fArr[5] = z3 ? f3 : 0.0f;
        fArr[6] = z3 ? f3 : 0.0f;
        fArr[7] = z3 ? f3 : 0.0f;
        return new com.tencent.qqlitepro.hook.UiPatcher.AnonymousClass4(iMax, rectF, new android.graphics.Path(), fArr, paint, str, iRound2, textPaint, iRound3, z, rectF2, f3, paint2);
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.UiPatcher$4, reason: invalid class name */
    static class AnonymousClass4 extends android.graphics.drawable.Drawable {
        final /* synthetic */ java.lang.String val$badge;
        final /* synthetic */ boolean val$badgeLeft;
        final /* synthetic */ android.graphics.Paint val$badgeP;
        final /* synthetic */ android.graphics.RectF val$badgeRect;
        final /* synthetic */ float val$radius;
        final /* synthetic */ int val$rectHPad;
        final /* synthetic */ int val$rectVPad;
        final /* synthetic */ android.graphics.Paint val$strokeP;
        final /* synthetic */ android.graphics.Path val$strokePath;
        final /* synthetic */ int val$strokePx;
        final /* synthetic */ float[] val$strokeRadii;
        final /* synthetic */ android.graphics.RectF val$strokeRect;
        final /* synthetic */ android.text.TextPaint val$textP;

        @Override // android.graphics.drawable.Drawable
        public int getOpacity() {
            return -3;
        }

        @Override // android.graphics.drawable.Drawable
        public boolean isStateful() {
            return false;
        }

        AnonymousClass4(int i, android.graphics.RectF rectF, android.graphics.Path path, float[] fArr, android.graphics.Paint paint, java.lang.String str, int i2, android.text.TextPaint textPaint, int i3, boolean z, android.graphics.RectF rectF2, float f, android.graphics.Paint paint2) {
            this.val$strokePx = i;
            this.val$strokeRect = rectF;
            this.val$strokePath = path;
            this.val$strokeRadii = fArr;
            this.val$strokeP = paint;
            this.val$badge = str;
            this.val$rectHPad = i2;
            this.val$textP = textPaint;
            this.val$rectVPad = i3;
            this.val$badgeLeft = z;
            this.val$badgeRect = rectF2;
            this.val$radius = f;
            this.val$badgeP = paint2;
        }

        @Override // android.graphics.drawable.Drawable
        public void draw(android.graphics.Canvas canvas) {
            float fMin;
            float fMax;
            java.lang.CharSequence charSequenceEllipsize;
            int iWidth = getBounds().width();
            int iHeight = getBounds().height();
            if (iWidth <= 0 || iHeight <= 0) {
                return;
            }
            int i = this.val$strokePx / 2;
            float f = i;
            this.val$strokeRect.set(f, f, iWidth - i, iHeight - i);
            this.val$strokePath.reset();
            this.val$strokePath.addRoundRect(this.val$strokeRect, this.val$strokeRadii, android.graphics.Path.Direction.CW);
            canvas.drawPath(this.val$strokePath, this.val$strokeP);
            java.lang.String string = this.val$badge;
            float f2 = (iWidth - (this.val$strokePx * 2.0f)) - (this.val$rectHPad * 2.0f);
            if (f2 > 0.0f && this.val$textP.measureText(string) > f2 && (charSequenceEllipsize = android.text.TextUtils.ellipsize(this.val$badge, this.val$textP, f2, android.text.TextUtils.TruncateAt.MIDDLE)) != null && charSequenceEllipsize.length() > 0) {
                string = charSequenceEllipsize.toString();
            }
            float fMeasureText = this.val$textP.measureText(string);
            float fAscent = this.val$textP.ascent();
            float fDescent = this.val$textP.descent();
            float f3 = (fDescent - fAscent) + (this.val$rectVPad * 2.0f);
            float f4 = fMeasureText + (this.val$rectHPad * 2.0f);
            int i2 = this.val$strokePx;
            float f5 = i2;
            if (this.val$badgeLeft) {
                fMax = i2;
                fMin = java.lang.Math.min(iWidth - i2, f4 + fMax);
            } else {
                fMin = iWidth - i2;
                fMax = java.lang.Math.max(0.0f, fMin - f4);
            }
            if (f3 >= iHeight) {
                return;
            }
            this.val$badgeRect.set(fMax, f5, fMin, f3 + f5);
            android.graphics.RectF rectF = this.val$badgeRect;
            float f6 = this.val$radius;
            canvas.drawRoundRect(rectF, f6, f6, this.val$badgeP);
            canvas.drawText(string, this.val$badgeRect.left + this.val$rectHPad, this.val$badgeRect.centerY() - ((fAscent + fDescent) / 2.0f), this.val$textP);
        }

        @Override // android.graphics.drawable.Drawable
        public void setAlpha(int i) {
            this.val$strokeP.setAlpha(i);
            this.val$badgeP.setAlpha(i);
            this.val$textP.setAlpha(i);
            invalidateSelf();
        }

        @Override // android.graphics.drawable.Drawable
        public void setColorFilter(android.graphics.ColorFilter colorFilter) {
            this.val$strokeP.setColorFilter(colorFilter);
            this.val$badgeP.setColorFilter(colorFilter);
        }
    }

    private static void startStyleReconcile() {
        if (RECONCILING.compareAndSet(false, true)) {
            java.lang.Thread thread = new java.lang.Thread(new com.tencent.qqlitepro.hook.UiPatcher$$ExternalSyntheticLambda26(), "antirecall-style-reconcile");
            thread.setDaemon(true);
            thread.start();
        }
    }

    static /* synthetic */ void lambda$startStyleReconcile$2() {
        /*
         * 卡顿修复：
         * 原实现每秒在后台线程直接遍历 STYLED 并改 View 属性（跨线程操作 View），
         * 列表长时既会拖慢 UI，也可能触发 View 内部状态竞争。
         * 改为：低频巡检（默认 5s）+ 统一切到主线程一次性批量处理，
         * 且当集合为空时直接跳过，避免空转。
         */
        for (int i = 0; i < RECONCILE_ROUNDS; i++) {
            try {
                java.lang.Thread.sleep(RECONCILE_INTERVAL_MS);
            } catch (java.lang.InterruptedException unused) {
                break;
            }
            if (STYLED.isEmpty()) {
                continue;
            }
            try {
                final java.util.ArrayList<android.widget.TextView> snapshot = new java.util.ArrayList<>(STYLED);
                android.os.Handler handler = MAIN;
                if (handler == null) {
                    continue;
                }
                handler.post(new java.lang.Runnable() {
                    @Override
                    public void run() {
                        for (int k = 0; k < snapshot.size(); k++) {
                            android.widget.TextView tv = snapshot.get(k);
                            if (tv == null) {
                                continue;
                            }
                            try {
                                if (tv.getContext() != null
                                        && com.tencent.qqlitepro.util.Config.mode(tv.getContext()) != 2) {
                                    resetGrayTipsStyle(tv);
                                }
                            } catch (java.lang.Throwable unused) {
                            }
                        }
                    }
                });
            } catch (java.lang.Throwable unused2) {
            }
        }
        RECONCILING.set(false);
    }

    public static void patchMsgFactory(java.lang.ClassLoader classLoader) {
        try {
            java.lang.Class<?> cls = java.lang.Class.forName("com.tencent.watch.aio_impl.ext.MsgListUtilKt", false, classLoader);
            java.lang.Class<?> cls2 = java.lang.Class.forName("com.tencent.qqnt.kernel.nativeinterface.MsgRecord", false, classLoader);
            boolean z = false;
            for (java.lang.reflect.Method method : cls.getDeclaredMethods()) {
                if (method.getName().equals("c_0")) {
                    java.lang.Class<?>[] parameterTypes = method.getParameterTypes();
                    if (parameterTypes.length == 1 && parameterTypes[0].equals(cls2)) {
                        de.robv.android.xposed.XposedBridge.hookMethod(method, new com.tencent.qqlitepro.hook.UiPatcher.AnonymousClass5());
                        z = true;
                    }
                }
            }
            com.tencent.qqlitepro.util.Log.i(z ? "已 hook 消息 item 工厂 MsgListUtilKt.c_0（强制阻止）" : "未找到 MsgListUtilKt.c_0 目标方法");
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("hook 消息 item 工厂失败: " + th);
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.UiPatcher$5, reason: invalid class name */
    static class AnonymousClass5 extends de.robv.android.xposed.XC_MethodHook {
        AnonymousClass5() {
        }

        protected void beforeHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            try {
                java.lang.Object obj = (methodHookParam.args == null || methodHookParam.args.length < 1) ? null : methodHookParam.args[0];
                if (obj != null && com.tencent.qqlitepro.util.Config.mode(com.tencent.qqlitepro.hook.UiPatcher.appContext()) == 1 && com.tencent.qqlitepro.hook.UiPatcher.isEnabled()) {
                    com.tencent.qqlitepro.cache.MsgCache msgCache = com.tencent.qqlitepro.cache.MsgCache.get();
                    if (msgCache.isRecallRecord(obj) && msgCache.restoreInto(obj)) {
                        com.tencent.qqlitepro.util.Log.i("c_0 界面记录还原成功 -> 渲染为原气泡");
                    }
                }
            } catch (java.lang.Throwable th) {
                com.tencent.qqlitepro.util.Log.w("c_0 还原异常: " + th);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    private static android.content.Context appContext() {
        try {
            java.lang.reflect.Method method = java.lang.Class.forName("android.app.ActivityThread").getMethod("currentApplication", new java.lang.Class[0]);
            method.setAccessible(true);
            java.lang.Object objInvoke = method.invoke(null, new java.lang.Object[0]);
            if (objInvoke instanceof android.content.Context) {
                return (android.content.Context) objInvoke;
            }
            return null;
        } catch (java.lang.Throwable unused) {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    private static boolean isEnabled() {
        try {
            return com.tencent.qqlitepro.util.Config.enabled(appContext());
        } catch (java.lang.Throwable unused) {
            return true;
        }
    }

    public static void patchGrayTipCell(java.lang.ClassLoader classLoader) {
        try {
            boolean z = false;
            for (java.lang.reflect.Method method : java.lang.Class.forName("com.tencent.watch.aio_impl.ui.cell.graytips.WatchGrayTipsCell", false, classLoader).getDeclaredMethods()) {
                if (method.getName().equals("d") && com.tencent.qqlitepro.util.Log$$ExternalSyntheticBackport0.m(method) >= 2) {
                    de.robv.android.xposed.XposedBridge.hookMethod(method, grayTipBindHook());
                    z = true;
                }
            }
            com.tencent.qqlitepro.util.Log.i(z ? "已 hook WatchGrayTipsCell.d（灰条占位）" : "WatchGrayTipsCell 未找到目标方法 d");
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("hook WatchGrayTipsCell 失败: " + th);
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.UiPatcher$6, reason: invalid class name */
    static class AnonymousClass6 extends de.robv.android.xposed.XC_MethodHook {
        AnonymousClass6() {
        }

        protected void afterHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            int iMode;
            try {
                if (methodHookParam.args != null && methodHookParam.args.length >= 2) {
                    java.lang.Object obj = methodHookParam.args[0];
                    if (!(obj instanceof android.view.View)) {
                        return;
                    }
                    android.view.View view = (android.view.View) obj;
                    boolean zIsRecallTip = com.tencent.qqlitepro.hook.UiPatcher.isRecallTip(methodHookParam.args[1], view);
                    try {
                        iMode = com.tencent.qqlitepro.util.Config.mode(view.getContext());
                    } catch (java.lang.Throwable unused) {
                        iMode = 1;
                    }
                    if (zIsRecallTip) {
                        com.tencent.qqlitepro.hook.UiPatcher.styleRecalledTip(view, methodHookParam.args[1], iMode == 2);
                    } else {
                        com.tencent.qqlitepro.hook.UiPatcher.resetGrayTipsStyle(view);
                    }
                }
            } catch (java.lang.Throwable unused2) {
            }
        }
    }

    private static de.robv.android.xposed.XC_MethodHook grayTipBindHook() {
        return new com.tencent.qqlitepro.hook.UiPatcher.AnonymousClass6();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isRecallTip(java.lang.Object obj, android.view.View view) {
        java.lang.CharSequence text;
        try {
            java.lang.Object objObjField = objField(obj, "d");
            if (objObjField != null && hasRevokeElementIn(objObjField)) {
                return true;
            }
        } catch (java.lang.Throwable unused) {
        }
        return (view instanceof android.widget.TextView) && (text = ((android.widget.TextView) view).getText()) != null && text.toString().contains("撤回");
    }

    private static boolean hasRevokeElementIn(java.lang.Object obj) {
        java.lang.Object objObjField = objField(obj, "msgElements");
        if (objObjField == null) {
            return false;
        }
        if (objObjField instanceof java.lang.Iterable) {
            java.util.Iterator it = ((java.lang.Iterable) objObjField).iterator();
            while (it.hasNext()) {
                if (hasRevokeElement(it.next())) {
                    return true;
                }
            }
        } else if (objObjField.getClass().isArray()) {
            int length = java.lang.reflect.Array.getLength(objObjField);
            for (int i = 0; i < length; i++) {
                if (hasRevokeElement(java.lang.reflect.Array.get(objObjField, i))) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean hasRevokeElement(java.lang.Object obj) {
        if (obj == null) {
            return false;
        }
        try {
            java.lang.Object objObjField = objField(obj, "grayTipElement");
            return (objObjField == null || objField(objObjField, "revokeElement") == null) ? false : true;
        } catch (java.lang.Throwable unused) {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void styleRecalledTip(android.view.View view, java.lang.Object obj, boolean z) {
        if (view instanceof android.widget.TextView) {
            if (!isEnabled()) {
                resetGrayTipsStyle((android.widget.TextView) view);
                return;
            }
            android.widget.TextView textView = (android.widget.TextView) view;
            java.lang.Object objObjField = objField(obj, "d");
            java.lang.String strSafeString = objObjField == null ? null : safeString(objField(objObjField, "peerUid"), null);
            java.lang.String strSafeString2 = objObjField == null ? null : safeString(objField(objObjField, "msgId"), null);
            java.lang.String strTakeForTip = com.tencent.qqlitepro.cache.MsgCache.get().takeForTip(strSafeString, strSafeString2);
            if (strTakeForTip == null || (strTakeForTip.isEmpty() && objObjField != null)) {
                java.lang.Object objFindRevokeElement = findRevokeElement(objObjField);
                java.lang.String strSafeString3 = objFindRevokeElement != null ? safeString(objField(objFindRevokeElement, "wording"), null) : null;
                if (strSafeString3 != null && !strSafeString3.isEmpty()) {
                    strTakeForTip = strSafeString3;
                }
            }
            if (strTakeForTip == null || strTakeForTip.isEmpty()) {
                retryTipIfMissing(view, obj, strSafeString, strSafeString2);
            }
            if (z) {
                int iRgb = android.graphics.Color.rgb(230, 54, 57);
                android.graphics.drawable.GradientDrawable gradientDrawable = new android.graphics.drawable.GradientDrawable();
                gradientDrawable.setColor(451294777);
                gradientDrawable.setCornerRadius(dp(view, 8.0f));
                gradientDrawable.setStroke(dp(view, 2.0f), iRgb);
                textView.setBackground(gradientDrawable);
                int iDp = dp(view, 8.0f);
                textView.setPadding(iDp, dp(view, 6.0f), iDp, dp(view, 6.0f));
                textView.setTextSize(14.0f);
                textView.setTextColor(iRgb);
                STYLED.add(textView);
            } else {
                resetGrayTipsStyle(textView);
            }
            if (strTakeForTip == null || strTakeForTip.isEmpty()) {
                return;
            }
            textView.setText("【已撤回】" + strTakeForTip);
            com.tencent.qqlitepro.util.Log.i("灰条注入原文: peer=" + strSafeString + " content=\"" + strTakeForTip + "\" red=" + z);
        }
    }

    private static void retryTipIfMissing(android.view.View view, java.lang.Object obj, java.lang.String str, java.lang.String str2) {
        long j;
        try {
            java.lang.Object tag = view.getTag();
            int iIntValue = (tag instanceof java.lang.Number ? ((java.lang.Number) tag).intValue() : 0) + 1;
            view.setTag(java.lang.Integer.valueOf(iIntValue));
            if (iIntValue > 3) {
                return;
            }
            if (iIntValue == 1) {
                com.tencent.qqlitepro.util.Log.i("灰条原文暂缺，计划延迟重试: peer=" + str + " tipId=" + str2);
            }
            if (iIntValue == 1) {
                j = 400;
            } else {
                j = iIntValue == 2 ? 1200L : 2600L;
            }
            view.postDelayed(new com.tencent.qqlitepro.hook.UiPatcher$$ExternalSyntheticLambda18(str, str2, view, obj, iIntValue), j);
        } catch (java.lang.Throwable unused) {
        }
    }

    static /* synthetic */ void lambda$retryTipIfMissing$3(java.lang.String str, java.lang.String str2, android.view.View view, java.lang.Object obj, int i) {
        boolean z = false;
        try {
            java.lang.String strTakeForTip = com.tencent.qqlitepro.cache.MsgCache.get().takeForTip(str, str2);
            if (strTakeForTip != null && !strTakeForTip.isEmpty()) {
                view.setTag(null);
                try {
                    z = com.tencent.qqlitepro.util.Config.mode(view.getContext()) == 2;
                } catch (java.lang.Throwable unused) {
                }
                styleRecalledTip(view, obj, z);
            } else if (i < 3) {
                retryTipIfMissing(view, obj, str, str2);
            }
        } catch (java.lang.Throwable unused2) {
        }
    }

    private static java.lang.Object findRevokeElement(java.lang.Object obj) {
        java.lang.Object objObjField;
        java.lang.Object objObjField2;
        if (obj == null) {
            return null;
        }
        try {
            java.lang.Object objObjField3 = objField(obj, "msgElements");
            if (objObjField3 instanceof java.lang.Iterable) {
                java.util.Iterator it = ((java.lang.Iterable) objObjField3).iterator();
                while (it.hasNext()) {
                    java.lang.Object objObjField4 = objField(it.next(), "grayTipElement");
                    if (objObjField4 != null && (objObjField2 = objField(objObjField4, "revokeElement")) != null) {
                        return objObjField2;
                    }
                }
            } else if (objObjField3 != null && objObjField3.getClass().isArray()) {
                int length = java.lang.reflect.Array.getLength(objObjField3);
                for (int i = 0; i < length; i++) {
                    java.lang.Object objObjField5 = objField(java.lang.reflect.Array.get(objObjField3, i), "grayTipElement");
                    if (objObjField5 != null && (objObjField = objField(objObjField5, "revokeElement")) != null) {
                        return objObjField;
                    }
                }
            }
        } catch (java.lang.Throwable unused) {
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.lang.Object objField(java.lang.Object obj, java.lang.String str) {
        try {
            return de.robv.android.xposed.XposedHelpers.getObjectField(obj, str);
        } catch (java.lang.Throwable unused) {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void resetGrayTipsStyle(android.view.View view) {
        if (view instanceof android.widget.TextView) {
            android.widget.TextView textView = (android.widget.TextView) view;
            if (STYLED.remove(textView)) {
                try {
                    textView.setBackground(null);
                    textView.setPadding(0, 0, 0, 0);
                    textView.setTextSize(10.0f);
                    textView.setTextColor(-1711276033);
                } catch (java.lang.Throwable unused) {
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.lang.String safeString(java.lang.Object obj, java.lang.String str) {
        return obj == null ? str : obj.toString();
    }

    private static int dp(android.view.View view, float f) {
        return java.lang.Math.round(dpInt(view, f));
    }

    private static int dpInt(android.view.View view, float f) {
        return view == null ? (int) f : java.lang.Math.round(f * view.getResources().getDisplayMetrics().density);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void bindEdit(android.view.View view, java.lang.Object obj, java.lang.String str) {
        if (view == null || obj == null || str == null) {
            return;
        }
        java.util.Set<android.view.View> set = EDIT_BOUND;
        if (!set.contains(view) && isEditedEligible(obj, str)) {
            set.add(view);
        }
    }

    private static boolean isEditedEligible(java.lang.Object obj, java.lang.String str) {
        try {
            java.lang.Object objObjField = objField(obj, "d");
            if (objObjField != null && hasTextElement(objObjField)) {
                if (str != null && com.tencent.qqlitepro.cache.MsgCache.get().isRecalled(str)) {
                    return false;
                }
                java.lang.String strSafeString = safeString(objField(objObjField, "senderUid"), null);
                java.lang.String strSelfUid = com.tencent.qqlitepro.hook.MsgListenerHook.selfUid();
                return strSelfUid == null || !strSelfUid.equals(strSafeString);
            }
            return false;
        } catch (java.lang.Throwable unused) {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean hasTextElement(java.lang.Object obj) {
        java.lang.String[] strArr = {"elements", "msgElements"};
        for (int i = 0; i < 2; i++) {
            try {
                java.lang.Object objObjField = objField(obj, strArr[i]);
                if (objObjField instanceof java.lang.Iterable) {
                    for (java.lang.Object obj2 : (java.lang.Iterable) objObjField) {
                        if (obj2 != null && objField(obj2, "textElement") != null) {
                            return true;
                        }
                    }
                } else if (objObjField != null && objObjField.getClass().isArray()) {
                    int length = java.lang.reflect.Array.getLength(objObjField);
                    for (int i2 = 0; i2 < length; i2++) {
                        java.lang.Object obj3 = java.lang.reflect.Array.get(objObjField, i2);
                        if (obj3 != null && objField(obj3, "textElement") != null) {
                            return true;
                        }
                    }
                }
            } catch (java.lang.Throwable unused) {
            }
        }
        return false;
    }

    private static void showEditMenu(android.view.View view, java.lang.String str) {
        try {
            new android.app.AlertDialog.Builder(view.getContext()).setTitle("消息操作").setItems(new java.lang.String[]{"编辑消息"}, new com.tencent.qqlitepro.hook.UiPatcher$$ExternalSyntheticLambda36(view, str)).setNegativeButton("取消", (android.content.DialogInterface.OnClickListener) null).show();
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("弹出编辑菜单失败: " + th);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void showEditDialog(android.view.View view, java.lang.String str) {
        showEditDialog2(view == null ? null : view.getContext(), view, str);
    }

    private static void showEditDialog2(android.content.Context context, android.view.View view, java.lang.String str) {
        if (context == null) {
            return;
        }
        try {
            java.lang.String strCurrentText = EDITED_TEXT.get(str);
            java.lang.String str2 = "";
            if (strCurrentText == null && ((strCurrentText = EDITED_ORIGINAL.get(str)) == null || strCurrentText.isEmpty())) {
                strCurrentText = view == null ? "" : currentText(view, str);
            }
            android.widget.LinearLayout linearLayout = new android.widget.LinearLayout(context);
            linearLayout.setOrientation(1);
            linearLayout.setPadding(dp(view, 20.0f), dp(view, 20.0f), dp(view, 20.0f), dp(view, 14.0f));
            android.graphics.drawable.GradientDrawable gradientDrawable = new android.graphics.drawable.GradientDrawable();
            gradientDrawable.setColor(-1);
            gradientDrawable.setCornerRadius(dp(view, 20.0f));
            linearLayout.setBackground(gradientDrawable);
            android.widget.TextView textView = new android.widget.TextView(context);
            textView.setText("编辑消息");
            textView.setTextColor(-12957089);
            textView.setTextSize(17.0f);
            textView.setTypeface(android.graphics.Typeface.DEFAULT, 1);
            textView.setPadding(0, 0, 0, dp(view, 12.0f));
            linearLayout.addView(textView);
            android.widget.EditText editText = new android.widget.EditText(context);
            editText.setSingleLine(false);
            editText.setMinLines(3);
            if (strCurrentText != null) {
                str2 = strCurrentText;
            }
            editText.setText(str2);
            editText.setTextSize(15.0f);
            editText.setTextColor(-13878709);
            editText.setHint("输入编辑后的消息内容");
            editText.setHintTextColor(-6641736);
            editText.setPadding(dp(view, 12.0f), dp(view, 10.0f), dp(view, 12.0f), dp(view, 10.0f));
            android.graphics.drawable.GradientDrawable gradientDrawable2 = new android.graphics.drawable.GradientDrawable();
            gradientDrawable2.setShape(0);
            gradientDrawable2.setColor(-854536);
            gradientDrawable2.setStroke(dp(view, 1.0f), 536870911);
            gradientDrawable2.setCornerRadius(dp(view, 12.0f));
            editText.setBackground(gradientDrawable2);
            editText.setLayoutParams(new android.widget.LinearLayout.LayoutParams(-1, -2));
            linearLayout.addView(editText);
            int iDp = dp(view, 16.0f);
            android.widget.LinearLayout linearLayout2 = new android.widget.LinearLayout(context);
            linearLayout2.setOrientation(0);
            linearLayout2.setGravity(21);
            linearLayout2.setPadding(0, iDp, 0, 0);
            linearLayout.addView(linearLayout2);
            android.widget.TextView textView2 = new android.widget.TextView(context);
            textView2.setText("取消");
            textView2.setTextColor(-10785924);
            textView2.setTextSize(15.0f);
            textView2.setPadding(dp(view, 8.0f), dp(view, 6.0f), dp(view, 8.0f), dp(view, 6.0f));
            linearLayout2.addView(textView2);
            android.widget.TextView textView3 = new android.widget.TextView(context);
            textView3.setText("确定");
            textView3.setTextColor(-2693889);
            textView3.setTextSize(15.0f);
            android.graphics.drawable.GradientDrawable gradientDrawable3 = new android.graphics.drawable.GradientDrawable();
            gradientDrawable3.setColor(-14141105);
            gradientDrawable3.setCornerRadius(dp(view, 14.0f));
            textView3.setBackground(gradientDrawable3);
            textView3.setPadding(dp(view, 20.0f), dp(view, 10.0f), dp(view, 20.0f), dp(view, 10.0f));
            android.widget.LinearLayout.LayoutParams layoutParams = new android.widget.LinearLayout.LayoutParams(-2, -2);
            layoutParams.leftMargin = dp(view, 12.0f);
            linearLayout2.addView(textView3, layoutParams);
            android.app.Dialog dialog = new android.app.Dialog(context, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            android.widget.FrameLayout frameLayout = new android.widget.FrameLayout(context);
            frameLayout.setPadding(dp(view, 10.0f), dp(view, 10.0f), dp(view, 10.0f), dp(view, 10.0f));
            frameLayout.addView(linearLayout);
            dialog.setContentView(frameLayout);
            textView2.setOnClickListener(new com.tencent.qqlitepro.hook.UiPatcher$$ExternalSyntheticLambda20(dialog));
            textView3.setOnClickListener(new com.tencent.qqlitepro.hook.UiPatcher$$ExternalSyntheticLambda21(editText, str, view, dialog));
            dialog.show();
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("编辑弹窗失败: " + th);
        }
    }

    static /* synthetic */ void lambda$showEditDialog2$6(android.widget.EditText editText, java.lang.String str, android.view.View view, android.app.Dialog dialog, android.view.View view2) {
        java.lang.String string = editText.getText() == null ? "" : editText.getText().toString();
        if (string.trim().isEmpty()) {
            string = "（已编辑）";
        }
        EDITED_TEXT.put(str, string);
        if (view != null) {
            android.widget.TextView textViewFindTextLabel = findTextLabel(view, str);
            if (textViewFindTextLabel != null) {
                textViewFindTextLabel.setText(string);
            }
            applyEditedMark(view, str);
            view.invalidate();
        }
        dialog.dismiss();
        forceRedraw();
    }

    public static void dismissLongPressMenu() {
        java.lang.Object obj = sLongPressMenuFragment;
        if (obj == null) {
            return;
        }
        try {
            obj.getClass().getMethod("dismiss", new java.lang.Class[0]).invoke(obj, new java.lang.Object[0]);
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("关闭长按菜单失败: " + th);
        }
        sLongPressMenuFragment = null;
    }

    public static void openEditByMsgId(java.lang.String str) {
        try {
            dismissLongPressMenu();
            if (str == null) {
                return;
            }
            android.view.View view = null;
            android.view.View view2 = null;
            for (java.util.Map.Entry<android.view.View, java.lang.String> entry : ROOT_MSGID.entrySet()) {
                android.view.View key = entry.getKey();
                if (str.equals(entry.getValue())) {
                    if (key != null && key.isShown()) {
                        view = key;
                        break;
                    } else if (view2 == null) {
                        view2 = key;
                    }
                }
            }
            if (view != null) {
                view2 = view;
            }
            showEditDialog2(view2 != null ? view2.getContext() : appContext(), view2, str);
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("打开编辑失败: " + th);
        }
    }

    public static boolean injectedEditRowExists(android.view.View view) {
        return findLabeledTextView(view, "编辑") != null;
    }

    private static android.widget.TextView findLabeledTextView(android.view.View view, java.lang.String str) {
        android.widget.TextView textView;
        java.lang.CharSequence text;
        if (view == null) {
            return null;
        }
        if ((view instanceof android.widget.TextView) && (text = (textView = (android.widget.TextView) view).getText()) != null && str.equals(text.toString())) {
            return textView;
        }
        if (view instanceof android.view.ViewGroup) {
            android.view.ViewGroup viewGroup = (android.view.ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                android.widget.TextView textViewFindLabeledTextView = findLabeledTextView(viewGroup.getChildAt(i), str);
                if (textViewFindLabeledTextView != null) {
                    return textViewFindLabeledTextView;
                }
            }
        }
        return null;
    }

    public static android.view.ViewGroup findMenuRowsContainer(android.view.View view) {
        return findScrollFirstChild(view);
    }

    private static android.view.ViewGroup findScrollFirstChild(android.view.View view) {
        if (view == null) {
            return null;
        }
        if (view instanceof android.widget.ScrollView) {
            android.widget.ScrollView scrollView = (android.widget.ScrollView) view;
            if (scrollView.getChildCount() > 0 && (scrollView.getChildAt(0) instanceof android.view.ViewGroup)) {
                return (android.view.ViewGroup) scrollView.getChildAt(0);
            }
        }
        if (view instanceof android.view.ViewGroup) {
            android.view.ViewGroup viewGroup = (android.view.ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                android.view.ViewGroup viewGroupFindScrollFirstChild = findScrollFirstChild(viewGroup.getChildAt(i));
                if (viewGroupFindScrollFirstChild != null) {
                    return viewGroupFindScrollFirstChild;
                }
            }
        }
        return null;
    }

    public static void injectEditRow(android.view.ViewGroup viewGroup, java.lang.String str, android.content.Context context, java.lang.Runnable runnable) {
        if (viewGroup == null || context == null || str == null) {
            return;
        }
        try {
            android.widget.LinearLayout linearLayout = new android.widget.LinearLayout(context);
            linearLayout.setOrientation(0);
            linearLayout.setGravity(16);
            int iDp = dp(linearLayout, 18.0f);
            int iDp2 = dp(linearLayout, 12.0f);
            linearLayout.setPadding(iDp, iDp2, iDp, iDp2);
            linearLayout.setClickable(true);
            android.graphics.drawable.GradientDrawable gradientDrawable = new android.graphics.drawable.GradientDrawable();
            gradientDrawable.setColor(-2145115100);
            gradientDrawable.setCornerRadius(dp(linearLayout, 14.0f));
            linearLayout.setBackground(new android.graphics.drawable.RippleDrawable(android.content.res.ColorStateList.valueOf(872415231), gradientDrawable, gradientDrawable));
            android.widget.ImageView imageView = new android.widget.ImageView(context);
            try {
                imageView.setImageResource(android.R.drawable.ic_menu_edit);
                imageView.setColorFilter(-1, android.graphics.PorterDuff.Mode.SRC_IN);
            } catch (java.lang.Throwable unused) {
            }
            linearLayout.addView(imageView, new android.widget.LinearLayout.LayoutParams(dp(linearLayout, 22.0f), dp(linearLayout, 22.0f)));
            android.widget.TextView textView = new android.widget.TextView(context);
            android.widget.LinearLayout.LayoutParams layoutParams = new android.widget.LinearLayout.LayoutParams(-2, -2);
            layoutParams.leftMargin = dp(linearLayout, 12.0f);
            textView.setText("编辑");
            textView.setTextColor(-1);
            textView.setTextSize(15.0f);
            textView.setIncludeFontPadding(false);
            linearLayout.addView(textView, layoutParams);
            if (runnable != null) {
                linearLayout.setOnClickListener(new com.tencent.qqlitepro.hook.UiPatcher$$ExternalSyntheticLambda30(runnable));
            }
            android.widget.LinearLayout.LayoutParams layoutParams2 = new android.widget.LinearLayout.LayoutParams(-1, -2);
            layoutParams2.leftMargin = dp(linearLayout, 10.0f);
            layoutParams2.rightMargin = dp(linearLayout, 10.0f);
            layoutParams2.topMargin = dp(linearLayout, 3.0f);
            layoutParams2.bottomMargin = dp(linearLayout, 3.0f);
            viewGroup.addView(linearLayout, layoutParams2);
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("注入编辑行失败: " + th);
        }
    }

    private static java.lang.String findPttPath(java.lang.Object obj) {
        java.util.List<java.lang.String> listFindPttPaths = findPttPaths(obj);
        if (listFindPttPaths == null || listFindPttPaths.isEmpty()) {
            return null;
        }
        return listFindPttPaths.get(0);
    }

    private static java.util.List<java.lang.String> findPttPaths(java.lang.Object obj) {
        java.util.ArrayList arrayList = new java.util.ArrayList();
        java.lang.String[] strArr = {"elements", "msgElements"};
        for (int i = 0; i < 2; i++) {
            try {
                java.lang.Object objObjField = objField(obj, strArr[i]);
                java.util.ArrayList arrayList2 = new java.util.ArrayList();
                if (objObjField instanceof java.lang.Iterable) {
                    java.util.Iterator it = ((java.lang.Iterable) objObjField).iterator();
                    while (it.hasNext()) {
                        arrayList2.add(it.next());
                    }
                } else if (objObjField != null && objObjField.getClass().isArray()) {
                    int length = java.lang.reflect.Array.getLength(objObjField);
                    for (int i2 = 0; i2 < length; i2++) {
                        arrayList2.add(java.lang.reflect.Array.get(objObjField, i2));
                    }
                }
                for (java.lang.Object obj2 : arrayList2) {
                    if (obj2 != null) {
                        try {
                            java.lang.Object objObjField2 = objField(obj2, "pttElement");
                            if (objObjField2 != null) {
                                java.lang.Object objObjField3 = objField(objObjField2, "filePath");
                                if ((objObjField3 instanceof java.lang.String) && !((java.lang.String) objObjField3).isEmpty() && !arrayList.contains(objObjField3)) {
                                    arrayList.add((java.lang.String) objObjField3);
                                }
                                java.lang.String strPttPath = com.tencent.qqlitepro.hook.FakeMediaReceiver.pttPath(objObjField2);
                                if (strPttPath != null && !strPttPath.isEmpty() && !arrayList.contains(strPttPath)) {
                                    arrayList.add(strPttPath);
                                }
                            }
                        } catch (java.lang.Throwable unused) {
                        }
                    }
                }
            } catch (java.lang.Throwable unused2) {
            }
        }
        return arrayList;
    }

    private static java.lang.String findFirstPicPath(java.lang.Object obj) {
        java.util.List<java.lang.String> listFindAllPicPaths = findAllPicPaths(obj);
        if (listFindAllPicPaths == null || listFindAllPicPaths.isEmpty()) {
            return null;
        }
        return listFindAllPicPaths.get(0);
    }

    /** 深度遍历对象图，收集所有可能携带媒体信息的节点（限深防环） */
    private static void collectNodesDeep(java.lang.Object root,
                                          java.util.ArrayList<java.lang.Object> out, int depth) {
        if (root == null || depth > 6 || out.size() > 200) {
            return;
        }
        try {
            String cn = root.getClass().getName();
            if (cn.startsWith("java.lang.") || cn.startsWith("java.util.")
                    || root instanceof Number || root instanceof CharSequence) {
                return;
            }
            out.add(root);
            if (root instanceof java.util.Map) {
                for (java.lang.Object v : ((java.util.Map<?, ?>) root).values()) {
                    collectNodesDeep(v, out, depth + 1);
                }
                return;
            }
            if (root instanceof java.lang.Iterable) {
                for (java.lang.Object v : (java.lang.Iterable<?>) root) {
                    collectNodesDeep(v, out, depth + 1);
                }
                return;
            }
            if (root.getClass().isArray()) {
                int n = java.lang.reflect.Array.getLength(root);
                for (int i = 0; i < n && i < 50; i++) {
                    collectNodesDeep(java.lang.reflect.Array.get(root, i), out, depth + 1);
                }
                return;
            }
            for (Class<?> c = root.getClass(); c != null && c != java.lang.Object.class;
                 c = c.getSuperclass()) {
                for (java.lang.reflect.Field f : c.getDeclaredFields()) {
                    if (java.lang.reflect.Modifier.isStatic(f.getModifiers())) continue;
                    try {
                        f.setAccessible(true);
                        java.lang.Object v = f.get(root);
                        if (v == null) continue;
                        collectNodesDeep(v, out, depth + 1);
                    } catch (java.lang.Throwable ignored) {
                    }
                }
            }
        } catch (java.lang.Throwable ignored) {
        }
    }

    private static java.util.List<java.lang.String> findAllPicPaths(java.lang.Object obj) {
        java.lang.String strPngByMd5;
        java.util.ArrayList arrayList = new java.util.ArrayList();
        java.lang.String[] strArr = {"elements", "msgElements"};
        int i = 3;
        java.lang.String[][] strArr2 = {new java.lang.String[]{"sourcePath"}, new java.lang.String[]{"filePath"}, new java.lang.String[]{"thumbPath"}};
        int i2 = 0;
        while (i2 < 2) {
            try {
                java.lang.Object objObjField = objField(obj, strArr[i2]);
                java.util.ArrayList arrayList2 = new java.util.ArrayList();
                if (objObjField instanceof java.lang.Iterable) {
                    java.util.Iterator it = ((java.lang.Iterable) objObjField).iterator();
                    while (it.hasNext()) {
                        arrayList2.add(it.next());
                    }
                } else if (objObjField != null && objObjField.getClass().isArray()) {
                    int length = java.lang.reflect.Array.getLength(objObjField);
                    for (int i3 = 0; i3 < length; i3++) {
                        arrayList2.add(java.lang.reflect.Array.get(objObjField, i3));
                    }
                }
                for (java.lang.Object obj2 : arrayList2) {
                    if (obj2 != null) {
                        try {
                            java.lang.Object objObjField2 = objField(obj2, "picElement");
                            if (objObjField2 != null) {
                                int i4 = 0;
                                while (i4 < i) {
                                    for (java.lang.String str : strArr2[i4]) {
                                        java.lang.Object objObjField3 = objField(objObjField2, str);
                                        if ((objObjField3 instanceof java.lang.String) && !((java.lang.String) objObjField3).isEmpty() && !arrayList.contains(objObjField3)) {
                                            arrayList.add((java.lang.String) objObjField3);
                                        }
                                    }
                                    i4++;
                                    i = 3;
                                }
                                plusStringPaths(objObjField2, arrayList);
                                java.lang.String strSafeString = safeString(objField(objObjField2, "md5HexStr"), null);
                                if (strSafeString != null && !strSafeString.isEmpty() && (strPngByMd5 = com.tencent.qqlitepro.http.FakeImage.pngByMd5(strSafeString)) != null && !arrayList.contains(strPngByMd5)) {
                                    arrayList.add(strPngByMd5);
                                }
                                java.lang.String strPicOriginalPath = com.tencent.qqlitepro.hook.FakeMediaReceiver.picOriginalPath(objObjField2);
                                if (strPicOriginalPath != null && !strPicOriginalPath.isEmpty() && !arrayList.contains(strPicOriginalPath)) {
                                    arrayList.add(strPicOriginalPath);
                                }
                                com.tencent.qqlitepro.hook.FakeMediaReceiver.plusPicThumbPaths(objObjField2, arrayList);
                                plusStringPaths(obj2, arrayList);
                                i = 3;
                            }
                        } catch (java.lang.Throwable unused) {
                        }
                    }
                }
            } catch (java.lang.Throwable unused2) {
            }
            i2++;
            i = 3;
        }
        /*
         * 兜底：宿主未运行期间收到的消息，picElement 可能缺失或结构不同，
         * 这里从整个消息对象做一次深度遍历，把任何带 md5HexStr / fileName 的节点都收进来，
         * 再用 md5 反查下载目录里的真实文件。
         */
        try {
            java.util.ArrayList<java.lang.Object> allNodes = new java.util.ArrayList<>();
            collectNodesDeep(obj, allNodes, 0);
            for (java.lang.Object node : allNodes) {
                java.lang.String md5 = safeString(objField(node, "md5HexStr"), null);
                if (md5 == null || md5.isEmpty()) {
                    md5 = safeString(objField(node, "md5"), null);
                }
                if (md5 == null || md5.isEmpty()) {
                    continue;
                }
                try {
                    java.lang.String png = com.tencent.qqlitepro.http.FakeImage.pngByMd5(md5);
                    if (png != null && !png.isEmpty() && !arrayList.contains(png)) {
                        arrayList.add(png);
                    }
                } catch (java.lang.Throwable ignored) {
                }
                try {
                    java.lang.String orig = com.tencent.qqlitepro.hook.FakeMediaReceiver.picOriginalPath(node);
                    if (orig != null && !orig.isEmpty() && !arrayList.contains(orig)) {
                        arrayList.add(orig);
                    }
                } catch (java.lang.Throwable ignored) {
                }
            }
        } catch (java.lang.Throwable ignored) {
        }
        return arrayList;
    }

    private static void plusStringPaths(java.lang.Object obj, java.util.ArrayList<java.lang.String> arrayList) {
        if (obj == null) {
            return;
        }
        try {
            for (java.lang.Class<?> superclass = obj.getClass(); superclass != null && superclass != java.lang.Object.class; superclass = superclass.getSuperclass()) {
                try {
                    for (java.lang.reflect.Field field : superclass.getDeclaredFields()) {
                        try {
                            if (field.getType().equals(java.lang.String.class)) {
                                field.setAccessible(true);
                                java.lang.Object obj2 = field.get(obj);
                                if (obj2 instanceof java.lang.String) {
                                    java.lang.String str = (java.lang.String) obj2;
                                    if (!str.isEmpty() && !str.startsWith("http") && str.indexOf(47) >= 0 && !arrayList.contains(str)) {
                                        arrayList.add(str);
                                    }
                                }
                            }
                        } catch (java.lang.Throwable unused) {
                        }
                    }
                } catch (java.lang.Throwable unused2) {
                }
            }
        } catch (java.lang.Throwable unused3) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void dumpPicElement(java.lang.Object obj) {
        java.lang.String string;
        java.lang.String string2;
        try {
            java.lang.Object objObjField = objField(obj, "elements");
            if (!(objObjField instanceof java.lang.Iterable) && !(objObjField instanceof java.util.Collection)) {
                objObjField = objField(obj, "msgElements");
            }
            java.util.ArrayList arrayList = new java.util.ArrayList();
            if (objObjField instanceof java.lang.Iterable) {
                java.util.Iterator it = ((java.lang.Iterable) objObjField).iterator();
                while (it.hasNext()) {
                    arrayList.add(it.next());
                }
            }
            if (arrayList.isEmpty()) {
                com.tencent.qqlitepro.util.Log.i("伪图结构 record 无 elements,class=" + obj.getClass().getName());
                return;
            }
            for (java.lang.Object obj2 : arrayList) {
                if (obj2 != null) {
                    java.lang.StringBuilder sb = new java.lang.StringBuilder();
                    sb.append("伪图结构 el=");
                    sb.append(obj2.getClass().getName());
                    sb.append(" ");
                    java.lang.Object objObjField2 = objField(obj2, "picElement");
                    if (objObjField2 != null) {
                        sb.append("pic=");
                        sb.append(objObjField2.getClass().getName());
                        sb.append("{");
                    }
                    sb.append("el{");
                    java.lang.Class<?> superclass = obj2.getClass();
                    while (true) {
                        boolean z = true;
                        if (superclass == null || superclass == java.lang.Object.class) {
                            break;
                        }
                        java.lang.reflect.Field[] declaredFields = superclass.getDeclaredFields();
                        int length = declaredFields.length;
                        int i = 0;
                        while (i < length) {
                            java.lang.reflect.Field field = declaredFields[i];
                            try {
                                field.setAccessible(z);
                                if (!java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                                    java.lang.Object obj3 = field.get(obj2);
                                    if (obj3 == null) {
                                        string2 = "null";
                                    } else {
                                        string2 = obj3 instanceof java.lang.CharSequence ? obj3.toString() : obj3.getClass().getSimpleName();
                                    }
                                    if (string2.length() > 100) {
                                        string2 = string2.substring(0, 100);
                                    }
                                    sb.append(field.getName());
                                    sb.append("=");
                                    sb.append(string2);
                                    sb.append("; ");
                                }
                            } catch (java.lang.Throwable unused) {
                            }
                            i++;
                            z = true;
                        }
                        superclass = superclass.getSuperclass();
                    }
                    sb.append("}");
                    if (objObjField2 != null) {
                        for (java.lang.Class<?> superclass2 = objObjField2.getClass(); superclass2 != null && superclass2 != java.lang.Object.class; superclass2 = superclass2.getSuperclass()) {
                            for (java.lang.reflect.Field field2 : superclass2.getDeclaredFields()) {
                                try {
                                    field2.setAccessible(true);
                                    if (!java.lang.reflect.Modifier.isStatic(field2.getModifiers())) {
                                        java.lang.Object obj4 = field2.get(objObjField2);
                                        if (obj4 == null) {
                                            string = "null";
                                        } else {
                                            string = obj4 instanceof java.lang.CharSequence ? obj4.toString() : obj4.getClass().getSimpleName();
                                        }
                                        if (string.length() > 100) {
                                            string = string.substring(0, 100);
                                        }
                                        sb.append(field2.getName());
                                        sb.append("=");
                                        sb.append(string);
                                        sb.append("; ");
                                    }
                                } catch (java.lang.Throwable unused2) {
                                }
                            }
                        }
                    }
                    sb.append("}");
                    com.tencent.qqlitepro.util.Log.i(sb.toString());
                    return;
                }
            }
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("伪图结构 dump 失败 " + th);
        }
    }

    private static java.util.List<java.lang.String> findAllMediaPaths(java.lang.Object obj) {
        java.util.ArrayList arrayList = new java.util.ArrayList();
        for (java.lang.String str : findPttPaths(obj)) {
            if (!arrayList.contains(str)) {
                arrayList.add(str);
            }
        }
        for (java.lang.String str2 : findAllPicPaths(obj)) {
            if (!arrayList.contains(str2)) {
                arrayList.add(str2);
            }
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    static final class FileInfoUI {
        int channel;
        int count;
        int index;
        java.lang.String md5;
        java.lang.String name;
        boolean shard;
        long size;
        java.lang.String sourcePath;
        long time;



        private FileInfoUI() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static com.tencent.qqlitepro.hook.UiPatcher.FileInfoUI detectFakeFile(java.lang.Object obj) {
        java.lang.Object objObjField;
        java.util.List<java.lang.String> listFindAllMediaPaths;
        com.tencent.qqlitepro.hook.UiPatcher.FileInfoUI fileInfoUIFileUIFromVoice;
        if (obj == null || (objObjField = objField(obj, "d")) == null || (listFindAllMediaPaths = findAllMediaPaths(objObjField)) == null || listFindAllMediaPaths.isEmpty()) {
            return null;
        }
        com.tencent.qqlitepro.hook.UiPatcher.FileInfoUI fileInfoUI = null;
        for (java.lang.String str : listFindAllMediaPaths) {
            com.tencent.qqlitepro.http.FakeVoice.OriginalInfo originalInfo = com.tencent.qqlitepro.http.FakeVoice.parseOriginalInfo(str);
            if (originalInfo != null) {
                fileInfoUIFileUIFromVoice = fileUIFromVoice(str, originalInfo);
                if (fileInfoUI == null || fileInfoUIFileUIFromVoice.index < fileInfoUI.index) {
                    fileInfoUI = fileInfoUIFileUIFromVoice;
                }
            } else {
                com.tencent.qqlitepro.http.FakeImage.OriginalInfo originalInfo2 = com.tencent.qqlitepro.http.FakeImage.parseOriginalInfo(str);
                if (originalInfo2 != null) {
                    fileInfoUIFileUIFromVoice = fileUIFromImage(str, originalInfo2);
                    if (fileInfoUI == null || fileInfoUIFileUIFromVoice.index < fileInfoUI.index) {
                        fileInfoUI = fileInfoUIFileUIFromVoice;
                    }
                }
            }
        }
        if (fileInfoUI == null) {
            return null;
        }
        for (java.lang.String str2 : listFindAllMediaPaths) {
            try {
                if (fileInfoUI.channel == 0 && com.tencent.qqlitepro.http.FakeVoice.isFakeVoice(str2) && com.tencent.qqlitepro.http.FakeVoice.isShard(str2)) {
                    com.tencent.qqlitepro.http.FakeVoice.collectPart(str2);
                } else if (fileInfoUI.channel == 1 && com.tencent.qqlitepro.http.FakeImage.isFakeImage(str2) && com.tencent.qqlitepro.http.FakeImage.isShard(str2)) {
                    com.tencent.qqlitepro.http.FakeImage.collectPart(str2);
                }
            } catch (java.lang.Throwable unused) {
            }
        }
        return fileInfoUI;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void injectFakeVoiceDownloadRow(android.view.View view, java.lang.String str, com.tencent.qqlitepro.hook.UiPatcher.FileInfoUI fileInfoUI) {
        if (view == null || str == null || fileInfoUI == null) {
            return;
        }
        java.lang.StringBuilder sb = new java.lang.StringBuilder("伪语音: 识别到伪装文件 ");
        sb.append(fileInfoUI.name);
        sb.append(" msgId=");
        sb.append(str);
        sb.append(fileInfoUI.shard ? " 分片 " + fileInfoUI.count : "");
        com.tencent.qqlitepro.util.Log.i(sb.toString());
        long[] jArr = {60, 180, 360, 600, 1000, 1600};
        for (int i = 0; i < 6; i++) {
            view.postDelayed(new com.tencent.qqlitepro.hook.UiPatcher$$ExternalSyntheticLambda33(view, str, fileInfoUI), jArr[i]);
        }
    }

    static /* synthetic */ void lambda$injectFakeVoiceDownloadRow$9(android.view.View view, java.lang.String str, com.tencent.qqlitepro.hook.UiPatcher.FileInfoUI fileInfoUI) {
        try {
            android.view.ViewGroup viewGroupFindMenuRowsContainer = findMenuRowsContainer(view);
            if (viewGroupFindMenuRowsContainer == null) {
                return;
            }
            java.util.Map<android.view.View, java.util.Set<java.lang.String>> map = FAKE_ROW_INJECTED_PER_MENU;
            java.util.Set<java.lang.String> hashSet = map.get(viewGroupFindMenuRowsContainer);
            if (hashSet == null) {
                hashSet = new java.util.HashSet<>();
                map.put(viewGroupFindMenuRowsContainer, hashSet);
            }
            if (hashSet.add(str)) {
                injectDownloadRow(viewGroupFindMenuRowsContainer, fileInfoUI.name, new com.tencent.qqlitepro.hook.UiPatcher$$ExternalSyntheticLambda29(view, str, fileInfoUI));
                com.tencent.qqlitepro.util.Log.i("伪语音: 已注入下载还原行 -> " + viewGroupFindMenuRowsContainer.getClass().getSimpleName());
            }
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("注入伪语音下载行失败: " + th);
        }
    }

    static /* synthetic */ void lambda$injectFakeVoiceDownloadRow$8(android.view.View view, java.lang.String str, com.tencent.qqlitepro.hook.UiPatcher.FileInfoUI fileInfoUI) {
        try {
            showFakeFileInfoDialog(view.getContext(), str, fileInfoUI);
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("弹出文件信息失败: " + th);
            toastMsg(view.getContext(), "打开文件信息失败: " + th);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void injectFakeImageDownloadRow(android.view.View view, java.lang.String str, com.tencent.qqlitepro.hook.UiPatcher.FileInfoUI fileInfoUI) {
        if (view == null || str == null || fileInfoUI == null) {
            return;
        }
        java.lang.StringBuilder sb = new java.lang.StringBuilder("伪图片: 识别到伪装文件 ");
        sb.append(fileInfoUI.name);
        sb.append(" msgId=");
        sb.append(str);
        sb.append(fileInfoUI.shard ? " 分片 " + fileInfoUI.count : "");
        com.tencent.qqlitepro.util.Log.i(sb.toString());
        long[] jArr = {60, 180, 360, 600, 1000, 1600};
        for (int i = 0; i < 6; i++) {
            view.postDelayed(new com.tencent.qqlitepro.hook.UiPatcher$$ExternalSyntheticLambda3(view, str, fileInfoUI), jArr[i]);
        }
    }

    static /* synthetic */ void lambda$injectFakeImageDownloadRow$11(android.view.View view, java.lang.String str, com.tencent.qqlitepro.hook.UiPatcher.FileInfoUI fileInfoUI) {
        try {
            android.view.ViewGroup viewGroupFindMenuRowsContainer = findMenuRowsContainer(view);
            if (viewGroupFindMenuRowsContainer == null) {
                return;
            }
            java.util.Map<android.view.View, java.util.Set<java.lang.String>> map = FAKE_ROW_INJECTED_PER_MENU;
            java.util.Set<java.lang.String> hashSet = map.get(viewGroupFindMenuRowsContainer);
            if (hashSet == null) {
                hashSet = new java.util.HashSet<>();
                map.put(viewGroupFindMenuRowsContainer, hashSet);
            }
            if (hashSet.add(str)) {
                injectDownloadRow(viewGroupFindMenuRowsContainer, fileInfoUI.name, new com.tencent.qqlitepro.hook.UiPatcher$$ExternalSyntheticLambda8(view, str, fileInfoUI));
                com.tencent.qqlitepro.util.Log.i("伪图片: 已注入下载还原行 -> " + viewGroupFindMenuRowsContainer.getClass().getSimpleName());
            }
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("注入伪图片下载行失败: " + th);
        }
    }

    static /* synthetic */ void lambda$injectFakeImageDownloadRow$10(android.view.View view, java.lang.String str, com.tencent.qqlitepro.hook.UiPatcher.FileInfoUI fileInfoUI) {
        try {
            showFakeFileInfoDialog(view.getContext(), str, fileInfoUI);
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("弹出文件信息失败: " + th);
            toastMsg(view.getContext(), "打开文件信息失败: " + th);
        }
    }

    private static com.tencent.qqlitepro.hook.UiPatcher.FileInfoUI fileUIFromVoice(java.lang.String str, com.tencent.qqlitepro.http.FakeVoice.OriginalInfo originalInfo) {
        com.tencent.qqlitepro.hook.UiPatcher.FileInfoUI fileInfoUI = new com.tencent.qqlitepro.hook.UiPatcher.FileInfoUI();
        fileInfoUI.channel = 0;
        fileInfoUI.sourcePath = str;
        fileInfoUI.name = originalInfo.name;
        fileInfoUI.md5 = originalInfo.md5;
        fileInfoUI.size = originalInfo.size;
        fileInfoUI.time = originalInfo.time;
        fileInfoUI.index = originalInfo.index;
        fileInfoUI.shard = originalInfo.shard;
        fileInfoUI.count = originalInfo.count;
        return fileInfoUI;
    }

    private static com.tencent.qqlitepro.hook.UiPatcher.FileInfoUI fileUIFromImage(java.lang.String str, com.tencent.qqlitepro.http.FakeImage.OriginalInfo originalInfo) {
        com.tencent.qqlitepro.hook.UiPatcher.FileInfoUI fileInfoUI = new com.tencent.qqlitepro.hook.UiPatcher.FileInfoUI();
        fileInfoUI.channel = 1;
        fileInfoUI.sourcePath = str;
        fileInfoUI.name = originalInfo.name;
        fileInfoUI.md5 = originalInfo.md5;
        fileInfoUI.size = originalInfo.size;
        fileInfoUI.time = originalInfo.time;
        fileInfoUI.index = originalInfo.index;
        fileInfoUI.shard = originalInfo.shard;
        fileInfoUI.count = originalInfo.count;
        return fileInfoUI;
    }

    private static void toastMsg(android.content.Context context, java.lang.String str) {
        try {
            android.widget.Toast.makeText(context, str, 1).show();
        } catch (java.lang.Throwable unused) {
        }
    }

    private static void injectDownloadRow(android.view.ViewGroup viewGroup, java.lang.String str, java.lang.Runnable runnable) {
        if (viewGroup == null) {
            return;
        }
        try {
            android.content.Context context = viewGroup.getContext();
            android.widget.LinearLayout linearLayout = new android.widget.LinearLayout(context);
            linearLayout.setOrientation(0);
            linearLayout.setGravity(16);
            int iDp = dp(linearLayout, 18.0f);
            int iDp2 = dp(linearLayout, 12.0f);
            linearLayout.setPadding(iDp, iDp2, iDp, iDp2);
            linearLayout.setClickable(true);
            android.graphics.drawable.GradientDrawable gradientDrawable = new android.graphics.drawable.GradientDrawable();
            gradientDrawable.setColor(-2145115100);
            gradientDrawable.setCornerRadius(dp(linearLayout, 14.0f));
            linearLayout.setBackground(new android.graphics.drawable.RippleDrawable(android.content.res.ColorStateList.valueOf(872415231), gradientDrawable, gradientDrawable));
            android.widget.ImageView imageView = new android.widget.ImageView(context);
            try {
                imageView.setImageResource(android.R.drawable.ic_menu_save);
                imageView.setColorFilter(-1, android.graphics.PorterDuff.Mode.SRC_IN);
            } catch (java.lang.Throwable unused) {
            }
            linearLayout.addView(imageView, new android.widget.LinearLayout.LayoutParams(dp(linearLayout, 22.0f), dp(linearLayout, 22.0f)));
            android.widget.TextView textView = new android.widget.TextView(context);
            android.widget.LinearLayout.LayoutParams layoutParams = new android.widget.LinearLayout.LayoutParams(-2, -2);
            layoutParams.leftMargin = dp(linearLayout, 12.0f);
            textView.setText("下载并还原" + str);
            textView.setTextColor(-1);
            textView.setTextSize(15.0f);
            textView.setIncludeFontPadding(false);
            linearLayout.addView(textView, layoutParams);
            if (runnable != null) {
                linearLayout.setOnClickListener(new com.tencent.qqlitepro.hook.UiPatcher$$ExternalSyntheticLambda32(runnable));
            }
            android.widget.LinearLayout.LayoutParams layoutParams2 = new android.widget.LinearLayout.LayoutParams(-1, -2);
            layoutParams2.leftMargin = dp(linearLayout, 10.0f);
            layoutParams2.rightMargin = dp(linearLayout, 10.0f);
            layoutParams2.topMargin = dp(linearLayout, 3.0f);
            layoutParams2.bottomMargin = dp(linearLayout, 3.0f);
            viewGroup.addView(linearLayout, layoutParams2);
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("注入下载还原行失败: " + th);
        }
    }

    private static void showFakeFileInfoDialog(android.content.Context context, java.lang.String str, com.tencent.qqlitepro.hook.UiPatcher.FileInfoUI fileInfoUI) {
        if (context == null || fileInfoUI == null) {
            return;
        }
        if (str != null) {
            try {
                if (!FAKE_INFO_SHOWN.add(str)) {
                    return;
                }
            } catch (java.lang.Throwable th) {
                if (str != null) {
                    FAKE_INFO_SHOWN.remove(str);
                }
                com.tencent.qqlitepro.util.Log.w("伪装文件信息弹窗失败: " + th);
                return;
            }
        }
        try {
            dismissLongPressMenu();
        } catch (java.lang.Throwable unused) {
        }
        android.widget.LinearLayout linearLayout = new android.widget.LinearLayout(context);
        linearLayout.setOrientation(1);
        linearLayout.setPadding(dp(context, 18.0f), dp(context, 16.0f), dp(context, 18.0f), dp(context, 12.0f));
        android.graphics.drawable.GradientDrawable gradientDrawable = new android.graphics.drawable.GradientDrawable();
        gradientDrawable.setColor(-1);
        gradientDrawable.setCornerRadius(dp(context, 18.0f));
        linearLayout.setBackground(gradientDrawable);
        android.widget.TextView textView = new android.widget.TextView(context);
        textView.setText("文件原始信息");
        textView.setTextColor(-12957089);
        textView.setTextSize(14.0f);
        textView.setTypeface(android.graphics.Typeface.DEFAULT, 1);
        textView.setPadding(0, 0, 0, dp(context, 3.0f));
        linearLayout.addView(textView);
        android.widget.TextView textView2 = new android.widget.TextView(context);
        textView2.setText(fileInfoUI.channel == 1 ? "此消息是伪图片、以图片通道传输的文件" : "此消息是非语音、以语音通道传输的文件");
        textView2.setTextColor(ORANGE);
        textView2.setTextSize(10.0f);
        textView2.setPadding(0, 0, 0, dp(context, 8.0f));
        linearLayout.addView(textView2);
        android.widget.LinearLayout linearLayout2 = new android.widget.LinearLayout(context);
        linearLayout2.setOrientation(1);
        java.lang.String strExtensionOf = com.tencent.qqlitepro.http.FakeVoice.extensionOf(fileInfoUI.name);
        java.lang.String[][] strArr = new java.lang.String[5][];
        java.lang.String[] strArr2 = new java.lang.String[2];
        strArr2[0] = "文件名";
        java.lang.String str2 = "未知";
        strArr2[1] = fileInfoUI.name == null ? "未知" : fileInfoUI.name;
        strArr[0] = strArr2;
        java.lang.String[] strArr3 = new java.lang.String[2];
        strArr3[0] = "文件格式";
        if (strExtensionOf == null || strExtensionOf.isEmpty()) {
            strExtensionOf = "未知";
        }
        strArr3[1] = strExtensionOf;
        strArr[1] = strArr3;
        strArr[2] = new java.lang.String[]{"文件大小", com.tencent.qqlitepro.http.FakeVoice.formatSize(fileInfoUI.size)};
        java.lang.String[] strArr4 = new java.lang.String[2];
        strArr4[0] = "文件 MD5";
        if (fileInfoUI.md5 != null && !fileInfoUI.md5.isEmpty()) {
            str2 = fileInfoUI.md5;
        }
        strArr4[1] = str2;
        strArr[3] = strArr4;
        strArr[4] = new java.lang.String[]{"上传时间", com.tencent.qqlitepro.http.FakeVoice.formatTime(fileInfoUI.time)};
        for (int i = 0; i < 5; i++) {
            java.lang.String[] strArr5 = strArr[i];
            linearLayout2.addView(buildInfoRow(context, strArr5[0], strArr5[1]));
        }
        android.widget.ScrollView scrollView = new android.widget.ScrollView(context);
        scrollView.setVerticalScrollBarEnabled(false);
        scrollView.addView(linearLayout2, new android.widget.FrameLayout.LayoutParams(-1, -2));
        linearLayout.addView(scrollView, new android.widget.LinearLayout.LayoutParams(-1, 0, 1.0f));
        android.widget.ProgressBar progressBar = new android.widget.ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        progressBar.setMax(100);
        progressBar.setProgress(0);
        android.widget.LinearLayout.LayoutParams layoutParams = new android.widget.LinearLayout.LayoutParams(-1, -2);
        layoutParams.topMargin = dp(context, 6.0f);
        progressBar.setVisibility(8);
        linearLayout.addView(progressBar, layoutParams);
        android.widget.TextView textView3 = new android.widget.TextView(context);
        textView3.setTextSize(12.0f);
        textView3.setTextColor(-12948644);
        textView3.setIncludeFontPadding(false);
        textView3.setVisibility(8);
        linearLayout.addView(textView3);
        int iDp = dp(context, 12.0f);
        android.widget.LinearLayout linearLayout3 = new android.widget.LinearLayout(context);
        linearLayout3.setOrientation(0);
        linearLayout3.setGravity(21);
        linearLayout3.setPadding(0, iDp, 0, 0);
        linearLayout.addView(linearLayout3);
        android.widget.TextView textView4 = new android.widget.TextView(context);
        textView4.setText("取消");
        textView4.setTextColor(-10785924);
        textView4.setTextSize(13.0f);
        textView4.setPadding(dp(context, 8.0f), dp(context, 6.0f), dp(context, 8.0f), dp(context, 6.0f));
        linearLayout3.addView(textView4);
        android.widget.TextView textView5 = new android.widget.TextView(context);
        textView5.setText("下载");
        textView5.setTextColor(-2693889);
        textView5.setTextSize(13.0f);
        android.graphics.drawable.GradientDrawable gradientDrawable2 = new android.graphics.drawable.GradientDrawable();
        gradientDrawable2.setColor(-14141105);
        gradientDrawable2.setCornerRadius(dp(context, 14.0f));
        textView5.setBackground(gradientDrawable2);
        textView5.setPadding(dp(context, 20.0f), dp(context, 9.0f), dp(context, 20.0f), dp(context, 9.0f));
        android.widget.LinearLayout.LayoutParams layoutParams2 = new android.widget.LinearLayout.LayoutParams(-2, -2);
        layoutParams2.leftMargin = dp(context, 12.0f);
        linearLayout3.addView(textView5, layoutParams2);
        android.app.Dialog dialog = new android.app.Dialog(context, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(-1, -1);
        android.widget.FrameLayout frameLayout = new android.widget.FrameLayout(context);
        frameLayout.setPadding(dp(context, 16.0f), dp(context, 16.0f), dp(context, 16.0f), dp(context, 16.0f));
        frameLayout.addView(linearLayout, new android.widget.FrameLayout.LayoutParams(-1, -1));
        dialog.setContentView(frameLayout);
        android.os.Handler handler = new android.os.Handler(android.os.Looper.getMainLooper());
        boolean[] zArr = {false};
        textView4.setOnClickListener(new com.tencent.qqlitepro.hook.UiPatcher$$ExternalSyntheticLambda4(dialog));
        textView5.setOnClickListener(new com.tencent.qqlitepro.hook.UiPatcher$$ExternalSyntheticLambda6(zArr, fileInfoUI, progressBar, textView3, textView5, new com.tencent.qqlitepro.hook.UiPatcher$$ExternalSyntheticLambda5(zArr, textView5, progressBar, textView3, fileInfoUI, handler, dialog)));
        dialog.setOnDismissListener(new com.tencent.qqlitepro.hook.UiPatcher$$ExternalSyntheticLambda7(str));
        dialog.show();
    }

    static /* synthetic */ void lambda$showFakeFileInfoDialog$20(boolean[] zArr, android.widget.TextView textView, android.widget.ProgressBar progressBar, android.widget.TextView textView2, com.tencent.qqlitepro.hook.UiPatcher.FileInfoUI fileInfoUI, android.os.Handler handler, android.app.Dialog dialog, android.view.View view) {
        if (zArr[0]) {
            return;
        }
        zArr[0] = true;
        textView.setClickable(false);
        textView.setEnabled(false);
        textView.setAlpha(0.6f);
        progressBar.setVisibility(0);
        textView2.setVisibility(0);
        progressBar.setProgress(0);
        textView2.setText("开始还原...");
        new java.lang.Thread(new com.tencent.qqlitepro.hook.UiPatcher$$ExternalSyntheticLambda11(fileInfoUI, handler, progressBar, textView2, textView, dialog, zArr)).start();
    }

    static /* synthetic */ void lambda$showFakeFileInfoDialog$19(com.tencent.qqlitepro.hook.UiPatcher.FileInfoUI fileInfoUI, android.os.Handler handler, android.widget.ProgressBar progressBar, android.widget.TextView textView, android.widget.TextView textView2, android.app.Dialog dialog, boolean[] zArr) {
        try {
            handler.post(new com.tencent.qqlitepro.hook.UiPatcher$$ExternalSyntheticLambda25(restoreFileWithProgress(fileInfoUI, new com.tencent.qqlitepro.hook.UiPatcher$$ExternalSyntheticLambda23(handler, progressBar, textView)), progressBar, textView, textView2, dialog, zArr));
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("还原异常: " + th);
            handler.post(new com.tencent.qqlitepro.hook.UiPatcher$$ExternalSyntheticLambda24(progressBar, textView, th));
        }
    }

    static /* synthetic */ void lambda$showFakeFileInfoDialog$14(android.widget.ProgressBar progressBar, int i, android.widget.TextView textView, java.lang.String str) {
        progressBar.setProgress(i);
        textView.setText(str + "  " + i + "%");
    }

    static /* synthetic */ void lambda$showFakeFileInfoDialog$16(android.widget.ProgressBar progressBar, android.widget.TextView textView, java.lang.Throwable th) {
        progressBar.setProgress(0);
        textView.setTextColor(-4634038);
        textView.setText("还原异常：" + th);
    }

    static /* synthetic */ void lambda$showFakeFileInfoDialog$18(java.io.File file, android.widget.ProgressBar progressBar, android.widget.TextView textView, android.widget.TextView textView2, android.app.Dialog dialog, boolean[] zArr) {
        if (file != null && file.exists()) {
            progressBar.setProgress(100);
            textView.setTextColor(-12948644);
            textView.setText("已完成：" + file.getAbsolutePath());
            textView2.setText("完成");
            textView2.setClickable(true);
            textView2.setEnabled(true);
            textView2.setAlpha(1.0f);
            textView2.setOnClickListener(new com.tencent.qqlitepro.hook.UiPatcher$$ExternalSyntheticLambda22(dialog));
            return;
        }
        textView.setTextColor(-4634038);
        textView.setText("还原失败，请查看日志");
        zArr[0] = false;
        textView2.setClickable(true);
        textView2.setEnabled(true);
        textView2.setAlpha(1.0f);
    }

    static /* synthetic */ void lambda$showFakeFileInfoDialog$22(boolean[] zArr, com.tencent.qqlitepro.hook.UiPatcher.FileInfoUI fileInfoUI, android.widget.ProgressBar progressBar, android.widget.TextView textView, android.widget.TextView textView2, android.view.View.OnClickListener onClickListener, android.view.View view) {
        if (zArr[0]) {
            return;
        }
        java.io.File file = new java.io.File(com.tencent.qqlitepro.util.Config.downloadDir(null), fileInfoUI.name);
        if (file.exists() && file.isFile()) {
            progressBar.setVisibility(8);
            textView.setVisibility(0);
            textView.setTextColor(-4626934);
            textView.setText("文件已存在：" + file.getAbsolutePath() + "。删除后可重新下载");
            textView2.setText("删除");
            textView2.setOnClickListener(new com.tencent.qqlitepro.hook.UiPatcher$$ExternalSyntheticLambda12(file, textView, textView2, onClickListener));
            return;
        }
        onClickListener.onClick(view);
    }

    static /* synthetic */ void lambda$showFakeFileInfoDialog$21(java.io.File file, android.widget.TextView textView, android.widget.TextView textView2, android.view.View.OnClickListener onClickListener, android.view.View view) {
        try {
            if (file.delete()) {
                textView.setTextColor(-12948644);
                textView.setText("已删除，可重新下载");
                textView2.setText("下载");
                textView2.setOnClickListener(onClickListener);
            } else {
                textView.setTextColor(-4634038);
                textView.setText("删除失败：" + file.getAbsolutePath());
            }
        } catch (java.lang.Throwable th) {
            textView.setTextColor(-4634038);
            textView.setText("删除失败：" + th);
        }
    }

    static /* synthetic */ void lambda$showFakeFileInfoDialog$23(java.lang.String str, android.content.DialogInterface dialogInterface) {
        if (str != null) {
            FAKE_INFO_SHOWN.remove(str);
        }
    }

    private static java.io.File restoreFileWithProgress(com.tencent.qqlitepro.hook.UiPatcher.FileInfoUI fileInfoUI, com.tencent.qqlitepro.http.FakeVoice.RestoreProgress restoreProgress) {
        if (fileInfoUI == null) {
            return null;
        }
        /* 伪图片/语音恢复只走下载目录（图片/视频保存路径仅针对宿主与模块保存的真实媒体） */
        java.lang.String strDownloadDir = com.tencent.qqlitepro.util.Config.downloadDir(null);
        if (fileInfoUI.channel == 1) {
            return com.tencent.qqlitepro.http.FakeImage.restoreWithProgress(fileInfoUI.sourcePath, strDownloadDir, restoreProgress);
        }
        return com.tencent.qqlitepro.http.FakeVoice.restoreWithProgress(fileInfoUI.sourcePath, strDownloadDir, restoreProgress);
    }

    private static android.view.View buildInfoRow(android.content.Context context, java.lang.String str, java.lang.String str2) {
        android.widget.LinearLayout linearLayout = new android.widget.LinearLayout(context);
        linearLayout.setOrientation(0);
        linearLayout.setGravity(3);
        linearLayout.setPadding(0, dp(context, 6.0f), 0, dp(context, 6.0f));
        android.widget.TextView textView = new android.widget.TextView(context);
        textView.setText(str);
        textView.setTextColor(-8615012);
        textView.setTextSize(13.0f);
        textView.setIncludeFontPadding(false);
        android.widget.LinearLayout.LayoutParams layoutParams = new android.widget.LinearLayout.LayoutParams(-2, -2);
        layoutParams.width = dp(context, 78.0f);
        linearLayout.addView(textView, layoutParams);
        android.widget.TextView textView2 = new android.widget.TextView(context);
        if (str2 == null) {
            str2 = "未知";
        }
        textView2.setText(str2);
        textView2.setTextColor(-13878709);
        textView2.setTextSize(13.0f);
        textView2.setIncludeFontPadding(false);
        textView2.setSingleLine(false);
        textView2.setMaxLines(6);
        android.widget.LinearLayout.LayoutParams layoutParams2 = new android.widget.LinearLayout.LayoutParams(0, -2, 1.0f);
        layoutParams2.leftMargin = dp(context, 14.0f);
        linearLayout.addView(textView2, layoutParams2);
        return linearLayout;
    }

    private static int dp(android.content.Context context, float f) {
        return java.lang.Math.round(f * context.getResources().getDisplayMetrics().density);
    }

    private static java.lang.String currentText(android.view.View view, java.lang.String str) {
        java.lang.CharSequence text;
        try {
            android.widget.TextView textViewFindTextLabel = findTextLabel(view, str);
            if (textViewFindTextLabel != null && (text = textViewFindTextLabel.getText()) != null) {
                return text.toString();
            }
            return "";
        } catch (java.lang.Throwable unused) {
            return "";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static android.widget.TextView findTextLabel(android.view.View view, java.lang.String str) {
        android.widget.TextView textViewFindLargestText;
        try {
            android.view.View viewResolveMessageWidget = resolveMessageWidget(view, str);
            if (viewResolveMessageWidget instanceof android.widget.TextView) {
                return (android.widget.TextView) viewResolveMessageWidget;
            }
            if (!(viewResolveMessageWidget instanceof android.view.ViewGroup) || (textViewFindLargestText = findLargestText(viewResolveMessageWidget)) == null) {
                return null;
            }
            return textViewFindLargestText;
        } catch (java.lang.Throwable unused) {
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void applyEditedMark(android.view.View view, java.lang.String str) {
        try {
            java.util.ArrayList<android.view.View> arrayList = new java.util.ArrayList();
            collectEditedMarked(view, arrayList);
            for (android.view.View view2 : arrayList) {
                try {
                    view2.setForeground(null);
                } catch (java.lang.Throwable unused) {
                }
                EDITED_MARKED.remove(view2);
            }
            android.view.View viewResolveStrokeBubble = resolveStrokeBubble(view, str);
            if (viewResolveStrokeBubble != null) {
                viewResolveStrokeBubble.setForeground(buildEditedDrawable(viewResolveStrokeBubble));
                EDITED_MARKED.add(viewResolveStrokeBubble);
                viewResolveStrokeBubble.invalidate();
            }
        } catch (java.lang.Throwable unused2) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void restoreEditedMark(android.view.View view) {
        try {
            java.util.ArrayList<android.view.View> arrayList = new java.util.ArrayList();
            collectEditedMarked(view, arrayList);
            for (android.view.View view2 : arrayList) {
                try {
                    view2.setForeground(null);
                } catch (java.lang.Throwable unused) {
                }
                EDITED_MARKED.remove(view2);
            }
        } catch (java.lang.Throwable unused2) {
        }
    }

    private static void collectEditedMarked(android.view.View view, java.util.List<android.view.View> list) {
        if (view == null) {
            return;
        }
        if (EDITED_MARKED.contains(view)) {
            list.add(view);
        }
        if (view instanceof android.view.ViewGroup) {
            android.view.ViewGroup viewGroup = (android.view.ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                collectEditedMarked(viewGroup.getChildAt(i), list);
            }
        }
    }

    private static java.lang.String fakeBadgeName(java.lang.String str, boolean z) {
        return fakeBadgeName(str, z, false, 0, 1);
    }

    private static java.lang.String fakeBadgeName(java.lang.String str, boolean z, boolean z2, int i, int i2) {
        java.lang.String strTrim;
        if (str == null || str.isEmpty()) {
            strTrim = z ? BADGE_FAKEVOICE : BADGE_FAKEIMAGE;
        } else {
            strTrim = str.replace('\n', ' ').replace('\r', ' ').replace('\t', ' ').trim();
        }
        if (!z2 || i2 <= 1) {
            return strTrim;
        }
        if (i <= 0) {
            return strTrim + " (1/" + i2 + ")";
        }
        return "(" + (i + 1) + "/" + i2 + ")";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void scheduleShardCollect(boolean z, java.lang.String str, boolean z2, java.lang.String str2) {
        if (str == null || !z2 || str2 == null || !SHARD_COLLECT_SCHEDULED.add(str2)) {
            return;
        }
        com.tencent.qqlitepro.hook.UiPatcher$$ExternalSyntheticLambda15 uiPatcher$$ExternalSyntheticLambda15 = new com.tencent.qqlitepro.hook.UiPatcher$$ExternalSyntheticLambda15(z, str);
        SHARD_COLLECT_EXEC.execute(uiPatcher$$ExternalSyntheticLambda15);
        android.os.Handler handler = new android.os.Handler(android.os.Looper.getMainLooper());
        handler.postDelayed(new com.tencent.qqlitepro.hook.UiPatcher$$ExternalSyntheticLambda16(uiPatcher$$ExternalSyntheticLambda15), 2000L);
        handler.postDelayed(new com.tencent.qqlitepro.hook.UiPatcher$$ExternalSyntheticLambda17(uiPatcher$$ExternalSyntheticLambda15), 6000L);
    }

    static /* synthetic */ void lambda$scheduleShardCollect$24(boolean z, java.lang.String str) {
        try {
            if (z) {
                com.tencent.qqlitepro.http.FakeImage.collectPart(str);
            } else {
                com.tencent.qqlitepro.http.FakeVoice.collectPart(str);
            }
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("绑定时归集分片失败: " + th);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void ensureReceiverMedia(android.view.View view, java.lang.Object obj, java.lang.String str) {
        if (obj == null || str == null || isSelfSendRecord(obj)) {
            return;
        }
        java.lang.Object objElementOfType = com.tencent.qqlitepro.hook.FakeMediaReceiver.elementOfType(obj, 4);
        java.lang.Object objElementOfType2 = com.tencent.qqlitepro.hook.FakeMediaReceiver.elementOfType(obj, 2);
        if (!(objElementOfType == null && objElementOfType2 == null) && RECV_DOWNLOAD_SCHEDULED.add(str)) {
            SHARD_COLLECT_EXEC.execute(new com.tencent.qqlitepro.hook.UiPatcher$$ExternalSyntheticLambda9(objElementOfType, view, obj, str, objElementOfType2));
        }
    }

    static /* synthetic */ void lambda$ensureReceiverMedia$27(java.lang.Object obj, android.view.View view, java.lang.Object obj2, java.lang.String str, java.lang.Object obj3) {
        if (obj != null) {
            try {
                java.lang.String strPttPath = com.tencent.qqlitepro.hook.FakeMediaReceiver.pttPath(objField(obj, "pttElement"));
                if (strPttPath != null && new java.io.File(strPttPath).exists()) {
                    recheckFake(view, obj2, str);
                    return;
                }
                com.tencent.qqlitepro.hook.FakeMediaReceiver.requestOriginal(obj2, obj, true);
            } catch (java.lang.Throwable th) {
                com.tencent.qqlitepro.util.Log.w("[伪媒体] 接收端下载调度失败: " + th);
                return;
            }
        }
        if (obj3 != null) {
            java.lang.String strPicOriginalPath = com.tencent.qqlitepro.hook.FakeMediaReceiver.picOriginalPath(objField(obj3, "picElement"));
            if (strPicOriginalPath != null && new java.io.File(strPicOriginalPath).exists()) {
                recheckFake(view, obj2, str);
                return;
            }
            com.tencent.qqlitepro.hook.FakeMediaReceiver.requestOriginal(obj2, obj3, false);
        }
        scheduleReceiverRecheck(view, obj2, str);
    }

    private static void scheduleReceiverRecheck(android.view.View view, java.lang.Object obj, java.lang.String str) {
        android.os.Handler handler = new android.os.Handler(android.os.Looper.getMainLooper());
        long[] jArr = {1000, 2500, FAKE_DETECT_NEGATIVE_TTL_MS, 9000};
        for (int i = 0; i < 4; i++) {
            handler.postDelayed(new com.tencent.qqlitepro.hook.UiPatcher$$ExternalSyntheticLambda10(view, obj, str), jArr[i]);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void recheckFake(android.view.View view, java.lang.Object obj, java.lang.String str) {
        try {
            FAKE_DETECT.remove(str);
            com.tencent.qqlitepro.hook.UiPatcher.FakeDetect fakeDetectDetectFake = detectFake(obj, str);
            if (fakeDetectDetectFake != null && fakeDetectDetectFake.fake) {
                new android.os.Handler(android.os.Looper.getMainLooper()).post(new com.tencent.qqlitepro.hook.UiPatcher$$ExternalSyntheticLambda34(view, str, fakeDetectDetectFake));
            }
        } catch (java.lang.Throwable unused) {
        }
    }

    static /* synthetic */ void lambda$recheckFake$30(android.view.View view, java.lang.String str, com.tencent.qqlitepro.hook.UiPatcher.FakeDetect fakeDetect) {
        try {
            applyFakeMark(view, str, fakeDetect.badge, fakeDetect.self, fakeDetect.shard, fakeDetect.index, fakeDetect.count);
            scheduleShardCollect(fakeDetect.imageMode, fakeDetect.path, fakeDetect.shard, fakeDetect.collectKey);
        } catch (java.lang.Throwable unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void applyFakeMark(android.view.View view, java.lang.String str, java.lang.String str2, boolean z, boolean z2, int i, int i2) {
        try {
            java.util.ArrayList<android.view.View> arrayList = new java.util.ArrayList();
            collectFakeVoiceMarked(view, arrayList);
            for (android.view.View view2 : arrayList) {
                try {
                    view2.setForeground(null);
                } catch (java.lang.Throwable unused) {
                }
                FAKEVOICE_MARKED.remove(view2);
            }
            android.view.View viewResolveStrokeBubble = resolveStrokeBubble(view, str);
            if (viewResolveStrokeBubble != null) {
                viewResolveStrokeBubble.setForeground(buildFakeDrawable(viewResolveStrokeBubble, str2, z, !z2 || i2 <= 1 || i <= 0, !z2 || i2 <= 1 || i >= i2 - 1));
                FAKEVOICE_MARKED.add(viewResolveStrokeBubble);
                viewResolveStrokeBubble.invalidate();
            }
        } catch (java.lang.Throwable unused2) {
        }
    }

    private static boolean isSelfSendRecord(java.lang.Object obj) {
        java.lang.Object objObjField;
        try {
            java.lang.String strSelfUid = com.tencent.qqlitepro.hook.MsgListenerHook.selfUid();
            return (strSelfUid == null || obj == null || (objObjField = objField(obj, "senderUid")) == null || !strSelfUid.equals(java.lang.String.valueOf(objObjField))) ? false : true;
        } catch (java.lang.Throwable unused) {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void restoreFakeVoiceMark(android.view.View view) {
        try {
            java.util.ArrayList<android.view.View> arrayList = new java.util.ArrayList();
            collectFakeVoiceMarked(view, arrayList);
            for (android.view.View view2 : arrayList) {
                try {
                    view2.setForeground(null);
                } catch (java.lang.Throwable unused) {
                }
                FAKEVOICE_MARKED.remove(view2);
            }
        } catch (java.lang.Throwable unused2) {
        }
    }

    private static void collectFakeVoiceMarked(android.view.View view, java.util.List<android.view.View> list) {
        if (view == null) {
            return;
        }
        if (FAKEVOICE_MARKED.contains(view)) {
            list.add(view);
        }
        if (view instanceof android.view.ViewGroup) {
            android.view.ViewGroup viewGroup = (android.view.ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                collectFakeVoiceMarked(viewGroup.getChildAt(i), list);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void rememberOriginal(java.lang.String str, java.lang.String str2) {
        if (str == null || str2 == null || str2.isEmpty()) {
            return;
        }
        java.util.Map<java.lang.String, java.lang.String> map = EDITED_ORIGINAL;
        if (map.size() < 4000 || map.containsKey(str)) {
            map.put(str, str2);
        }
    }

    public static void clearEditCache() {
        try {
            EDITED_TEXT.clear();
            EDITED_ORIGINAL.clear();
            EDITED_MARKED.clear();
        } catch (java.lang.Throwable unused) {
        }
    }

    public static void patchLongPressMenu(java.lang.ClassLoader classLoader) {
        java.lang.String str;
        try {
            for (java.lang.reflect.Method method : java.lang.Class.forName("momoi.mod.qqpro.hook.style.LongPressMenu", false, classLoader).getDeclaredMethods()) {
                if (method.getName().equals("build") && com.tencent.qqlitepro.util.Log$$ExternalSyntheticBackport0.m(method) >= 4 && method.getReturnType() == android.view.View.class) {
                    de.robv.android.xposed.XposedBridge.hookMethod(method, longPressMenuBuildHook());
                    com.tencent.qqlitepro.util.Log.i("菜单hook: 匹配到 build, paramCount=" + com.tencent.qqlitepro.util.Log$$ExternalSyntheticBackport0.m(method));
                    str = "已 hook 长按菜单 LongPressMenu.build（注入编辑行）";
                    com.tencent.qqlitepro.util.Log.i(str);
                }
            }
            str = "未找到 LongPressMenu.build 目标方法";
            com.tencent.qqlitepro.util.Log.i(str);
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("hook 长按菜单失败: " + th);
        }
    }

    public static void patchPttWaveView(java.lang.ClassLoader classLoader) {
        try {
            boolean z = false;
            for (java.lang.reflect.Method method : java.lang.Class.forName("com.tencent.watch.aio_impl.ui.cell.ptt.PttAudioWaveView", false, classLoader).getDeclaredMethods()) {
                if (method.getName().equals("f") && com.tencent.qqlitepro.util.Log$$ExternalSyntheticBackport0.m(method) >= 2) {
                    if (java.util.List.class.isAssignableFrom(method.getParameterTypes()[0])) {
                        de.robv.android.xposed.XposedBridge.hookMethod(method, pttWaveSetDataHook());
                        com.tencent.qqlitepro.util.Log.i("wavehook: 已 hook PttAudioWaveView.f -> " + method.toGenericString());
                        z = true;
                    } else {
                        com.tencent.qqlitepro.util.Log.i("wavehook: 跳过非 List 首参 " + method.toGenericString());
                    }
                }
            }
            com.tencent.qqlitepro.util.Log.i(z ? "已 hook PttAudioWaveView.f（伪语音波形防崩）" : "未找到 PttAudioWaveView.f 目标方法");
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("hook 语音波形视图失败: " + th);
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.UiPatcher$7, reason: invalid class name */
    static class AnonymousClass7 extends de.robv.android.xposed.XC_MethodHook {
        AnonymousClass7() {
        }

        protected void beforeHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            try {
                java.lang.Object obj = (methodHookParam.args == null || methodHookParam.args.length < 1) ? null : methodHookParam.args[0];
                if (obj instanceof java.util.List) {
                    java.util.List list = (java.util.List) obj;
                    for (java.lang.Object obj2 : list) {
                        if (obj2 != null && !(obj2 instanceof java.lang.Byte)) {
                            java.util.ArrayList arrayList = new java.util.ArrayList(list.size());
                            for (java.lang.Object obj3 : list) {
                                if (obj3 instanceof java.lang.Byte) {
                                    arrayList.add((java.lang.Byte) obj3);
                                } else if (obj3 instanceof java.lang.Number) {
                                    arrayList.add(java.lang.Byte.valueOf(((java.lang.Number) obj3).byteValue()));
                                }
                            }
                            methodHookParam.args[0] = arrayList;
                            return;
                        }
                    }
                }
            } catch (java.lang.Throwable unused) {
            }
        }
    }

    private static de.robv.android.xposed.XC_MethodHook pttWaveSetDataHook() {
        return new com.tencent.qqlitepro.hook.UiPatcher.AnonymousClass7();
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.UiPatcher$8, reason: invalid class name */
    static class AnonymousClass8 extends de.robv.android.xposed.XC_MethodHook {
        AnonymousClass8() {
        }

        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:68:0x011f -> B:89:0x012e). Please report as a decompilation issue!!! */
        protected void afterHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            try {
                boolean z = false;
                if (methodHookParam.args != null && methodHookParam.args.length >= 1) {
                    com.tencent.qqlitepro.hook.UiPatcher.sLongPressMenuFragment = methodHookParam.args[0];
                }
                java.lang.Object obj = (methodHookParam.args == null || methodHookParam.args.length < 3) ? null : methodHookParam.args[2];
                java.lang.Object obj2 = (methodHookParam.args == null || methodHookParam.args.length < 4) ? null : methodHookParam.args[3];
                java.lang.Object result = methodHookParam.getResult();
                java.lang.String name = "null";
                if (!(result instanceof android.view.View) || obj == null || obj2 == null) {
                    java.lang.StringBuilder sb = new java.lang.StringBuilder("菜单hook: 跳过, result/longpressed=null result=");
                    if (result != null) {
                        name = result.getClass().getName();
                    }
                    sb.append(name);
                    com.tencent.qqlitepro.util.Log.w(sb.toString());
                    return;
                }
                android.view.View view = (android.view.View) result;
                java.lang.String strSafeString = com.tencent.qqlitepro.hook.UiPatcher.objField(obj, "msgId") == null ? null : com.tencent.qqlitepro.hook.UiPatcher.safeString(com.tencent.qqlitepro.hook.UiPatcher.objField(obj, "msgId"), null);
                java.lang.Object objObjField = com.tencent.qqlitepro.hook.UiPatcher.objField(obj2, "d");
                boolean z2 = objObjField != null && com.tencent.qqlitepro.hook.UiPatcher.hasTextElement(objObjField);
                boolean z3 = strSafeString != null && com.tencent.qqlitepro.cache.MsgCache.get().isRecalled(strSafeString);
                java.lang.String strSafeString2 = com.tencent.qqlitepro.hook.UiPatcher.safeString(com.tencent.qqlitepro.hook.UiPatcher.objField(objObjField, "senderUid"), null);
                java.lang.String strSelfUid = com.tencent.qqlitepro.hook.MsgListenerHook.selfUid();
                if (strSafeString != null && !z3 && z2 && (strSelfUid == null || !strSelfUid.equals(strSafeString2))) {
                    z = true;
                }
                java.lang.String strRecordText = com.tencent.qqlitepro.hook.UiPatcher.recordText(obj);
                if (strRecordText != null && !strRecordText.isEmpty()) {
                    com.tencent.qqlitepro.hook.UiPatcher.rememberOriginal(strSafeString, strRecordText);
                }
                java.lang.StringBuilder sb2 = new java.lang.StringBuilder("菜单hook: msgId=");
                sb2.append(strSafeString);
                sb2.append(" record=");
                if (objObjField != null) {
                    name = objObjField.getClass().getSimpleName();
                }
                sb2.append(name);
                sb2.append(" hasText=");
                sb2.append(z2);
                sb2.append(" recalled=");
                sb2.append(z3);
                sb2.append(" sender=");
                sb2.append(strSafeString2);
                sb2.append(" self=");
                sb2.append(strSelfUid);
                sb2.append(" elig=");
                sb2.append(z);
                com.tencent.qqlitepro.util.Log.i(sb2.toString());
                try {
                    com.tencent.qqlitepro.hook.UiPatcher.FileInfoUI fileInfoUIDetectFakeFile = com.tencent.qqlitepro.hook.UiPatcher.detectFakeFile(obj2);
                    if (fileInfoUIDetectFakeFile == null) {
                        com.tencent.qqlitepro.util.Log.i("菜单hook: 非伪装文件，跳过还原入口 msgId=" + strSafeString);
                    } else if (fileInfoUIDetectFakeFile.channel == 1) {
                        com.tencent.qqlitepro.hook.UiPatcher.injectFakeImageDownloadRow(view, strSafeString, fileInfoUIDetectFakeFile);
                    } else {
                        com.tencent.qqlitepro.hook.UiPatcher.injectFakeVoiceDownloadRow(view, strSafeString, fileInfoUIDetectFakeFile);
                    }
                } catch (java.lang.Throwable th) {
                    com.tencent.qqlitepro.util.Log.w("接管伪装文件还原入口异常: " + th);
                }
                try {
                    java.util.List listCollectRealFileInfo = com.tencent.qqlitepro.hook.UiPatcher.collectRealFileInfo(objObjField);
                    if (listCollectRealFileInfo != null) {
                        com.tencent.qqlitepro.hook.UiPatcher.injectFileDetailRow(view, strSafeString, listCollectRealFileInfo);
                    }
                } catch (java.lang.Throwable th2) {
                    com.tencent.qqlitepro.util.Log.w("接管真实文件详情入口异常: " + th2);
                }
                if (z) {
                    long[] jArr = {60, 180, 360, 600, 1000, 1600};
                    for (int i = 0; i < 6; i++) {
                        view.postDelayed(new com.tencent.qqlitepro.hook.UiPatcher$8$$ExternalSyntheticLambda0(view, strSafeString), jArr[i]);
                    }
                }
            } catch (java.lang.Throwable th3) {
                com.tencent.qqlitepro.util.Log.w("长按菜单注入编辑行异常: " + th3);
            }
        }

        static /* synthetic */ void lambda$afterHookedMethod$1(android.view.View view, java.lang.String str) {
            try {
                com.tencent.qqlitepro.util.Log.i("菜单hook: 尝试注入 delay, menu=" + view.getClass().getSimpleName());
                android.view.ViewGroup viewGroupFindMenuRowsContainer = com.tencent.qqlitepro.hook.UiPatcher.findMenuRowsContainer(view);
                if (viewGroupFindMenuRowsContainer == null) {
                    com.tencent.qqlitepro.util.Log.w("菜单hook: 未找到菜单行容器(ScrollView 子视图为 null), " + com.tencent.qqlitepro.hook.UiPatcher.dumpTree(view, 0));
                } else {
                    if (com.tencent.qqlitepro.hook.UiPatcher.injectedEditRowExists(view)) {
                        return;
                    }
                    com.tencent.qqlitepro.hook.UiPatcher.injectEditRow(viewGroupFindMenuRowsContainer, str, view.getContext(), new com.tencent.qqlitepro.hook.UiPatcher$8$$ExternalSyntheticLambda1(str));
                    com.tencent.qqlitepro.util.Log.i("菜单hook: 已注入编辑行 -> " + viewGroupFindMenuRowsContainer.getClass().getSimpleName());
                }
            } catch (java.lang.Throwable th) {
                com.tencent.qqlitepro.util.Log.w("注入长按编辑行失败: " + th);
            }
        }
    }

    private static de.robv.android.xposed.XC_MethodHook longPressMenuBuildHook() {
        return new com.tencent.qqlitepro.hook.UiPatcher.AnonymousClass8();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.util.List<java.lang.String[]> collectRealFileInfo(java.lang.Object obj) {
        java.util.ArrayList arrayList;
        java.util.ArrayList arrayList2 = null;
        if (obj == null) {
            return null;
        }
        java.lang.String[] strArr = {"elements", "msgElements"};
        for (int i = 0; i < 2; i++) {
            try {
                java.lang.Object objObjField = objField(obj, strArr[i]);
                if (objObjField instanceof java.lang.Iterable) {
                    for (java.lang.Object obj2 : (java.lang.Iterable) objObjField) {
                        if (obj2 != null && objField(obj2, "fileElement") != null) {
                            arrayList = new java.util.ArrayList();
                            try {
                                dumpFields(objField(obj2, "fileElement"), "", arrayList, 0);
                            } catch (java.lang.Throwable unused) {
                            }
                            arrayList2 = arrayList;
                            break;
                        }
                    }
                } else if (objObjField != null && objObjField.getClass().isArray()) {
                    int length = java.lang.reflect.Array.getLength(objObjField);
                    for (int i2 = 0; i2 < length; i2++) {
                        java.lang.Object obj3 = java.lang.reflect.Array.get(objObjField, i2);
                        if (obj3 != null && objField(obj3, "fileElement") != null) {
                            arrayList = new java.util.ArrayList();
                            dumpFields(objField(obj3, "fileElement"), "", arrayList, 0);
                            arrayList2 = arrayList;
                            break;
                        }
                    }
                }
            } catch (java.lang.Throwable unused2) {
            }
            if (arrayList2 != null) {
                break;
            }
        }
        return arrayList2;
    }

    private static void dumpFields(java.lang.Object obj, java.lang.String str, java.util.List<java.lang.String[]> list, int i) {
        java.lang.String name;
        if (obj == null || i > 3 || list.size() >= 40) {
            return;
        }
        int i2 = 0;
        if (obj instanceof java.lang.CharSequence) {
            java.lang.String strTrim = obj.toString().trim();
            if (strTrim.isEmpty()) {
                return;
            }
            java.lang.String[] strArr = new java.lang.String[2];
            if (str.isEmpty()) {
                str = "字段";
            }
            strArr[0] = str;
            strArr[1] = strTrim;
            list.add(strArr);
            return;
        }
        if ((obj instanceof java.lang.Number) || (obj instanceof java.lang.Boolean) || (obj instanceof java.lang.Character)) {
            if (str.isEmpty()) {
                return;
            }
            list.add(new java.lang.String[]{str, obj.toString()});
            return;
        }
        if (obj instanceof java.lang.Iterable) {
            java.util.Iterator it = ((java.lang.Iterable) obj).iterator();
            while (it.hasNext()) {
                dumpFields(it.next(), str + "[" + i2 + "]", list, i + 1);
                i2++;
            }
            return;
        }
        if (obj.getClass().isArray()) {
            int length = java.lang.reflect.Array.getLength(obj);
            while (i2 < length) {
                dumpFields(java.lang.reflect.Array.get(obj, i2), str + "[" + i2 + "]", list, i + 1);
                i2++;
            }
            return;
        }
        try {
            java.lang.reflect.Field[] declaredFields = obj.getClass().getDeclaredFields();
            int length2 = declaredFields.length;
            while (i2 < length2) {
                java.lang.reflect.Field field = declaredFields[i2];
                if (!field.isSynthetic()) {
                    try {
                        field.setAccessible(true);
                        java.lang.Object obj2 = field.get(obj);
                        if (str.isEmpty()) {
                            name = field.getName();
                        } else {
                            name = str + "." + field.getName();
                        }
                        dumpFields(obj2, name, list, i + 1);
                    } catch (java.lang.Throwable unused) {
                    }
                }
                i2++;
            }
        } catch (java.lang.Throwable unused2) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void injectFileDetailRow(android.view.View view, java.lang.String str, java.util.List<java.lang.String[]> list) {
        if (view == null || str == null || list == null) {
            return;
        }
        java.util.ArrayList arrayList = new java.util.ArrayList(list);
        long[] jArr = {60, 180, 360, 600, 1000, 1600};
        for (int i = 0; i < 6; i++) {
            view.postDelayed(new com.tencent.qqlitepro.hook.UiPatcher$$ExternalSyntheticLambda14(view, str, arrayList), jArr[i]);
        }
    }

    static /* synthetic */ void lambda$injectFileDetailRow$32(android.view.View view, java.lang.String str, java.util.List list) {
        try {
            android.view.ViewGroup viewGroupFindMenuRowsContainer = findMenuRowsContainer(view);
            if (viewGroupFindMenuRowsContainer == null) {
                return;
            }
            java.util.Map<android.view.View, java.util.Set<java.lang.String>> map = FAKE_ROW_INJECTED_PER_MENU;
            java.util.Set<java.lang.String> hashSet = map.get(viewGroupFindMenuRowsContainer);
            if (hashSet == null) {
                hashSet = new java.util.HashSet<>();
                map.put(viewGroupFindMenuRowsContainer, hashSet);
            }
            if (hashSet.add("detail_" + str)) {
                injectFileDetailRow(viewGroupFindMenuRowsContainer, new com.tencent.qqlitepro.hook.UiPatcher$$ExternalSyntheticLambda2(viewGroupFindMenuRowsContainer, list));
                com.tencent.qqlitepro.util.Log.i("真实文件: 已注入详情行 -> " + viewGroupFindMenuRowsContainer.getClass().getSimpleName());
            }
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("注入文件详情行失败: " + th);
        }
    }

    static /* synthetic */ void lambda$injectFileDetailRow$31(android.view.ViewGroup viewGroup, java.util.List list) {
        try {
            showFileDetailDialog(viewGroup.getContext(), list);
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("弹出文件详情失败: " + th);
            toastMsg(viewGroup.getContext(), "打开文件详情失败: " + th);
        }
    }

    private static void injectFileDetailRow(android.view.ViewGroup viewGroup, java.lang.Runnable runnable) {
        if (viewGroup == null) {
            return;
        }
        try {
            android.content.Context context = viewGroup.getContext();
            android.widget.LinearLayout linearLayout = new android.widget.LinearLayout(context);
            linearLayout.setOrientation(0);
            linearLayout.setGravity(16);
            int iDp = dp(linearLayout, 18.0f);
            int iDp2 = dp(linearLayout, 12.0f);
            linearLayout.setPadding(iDp, iDp2, iDp, iDp2);
            linearLayout.setClickable(true);
            android.graphics.drawable.GradientDrawable gradientDrawable = new android.graphics.drawable.GradientDrawable();
            gradientDrawable.setColor(-2145115100);
            gradientDrawable.setCornerRadius(dp(linearLayout, 14.0f));
            linearLayout.setBackground(new android.graphics.drawable.RippleDrawable(android.content.res.ColorStateList.valueOf(872415231), gradientDrawable, gradientDrawable));
            android.widget.ImageView imageView = new android.widget.ImageView(context);
            try {
                imageView.setImageResource(android.R.drawable.ic_menu_info_details);
                imageView.setColorFilter(-1, android.graphics.PorterDuff.Mode.SRC_IN);
            } catch (java.lang.Throwable unused) {
            }
            linearLayout.addView(imageView, new android.widget.LinearLayout.LayoutParams(dp(linearLayout, 22.0f), dp(linearLayout, 22.0f)));
            android.widget.TextView textView = new android.widget.TextView(context);
            android.widget.LinearLayout.LayoutParams layoutParams = new android.widget.LinearLayout.LayoutParams(-2, -2);
            layoutParams.leftMargin = dp(linearLayout, 12.0f);
            textView.setText("详情");
            textView.setTextColor(-1);
            textView.setTextSize(15.0f);
            textView.setIncludeFontPadding(false);
            linearLayout.addView(textView, layoutParams);
            if (runnable != null) {
                linearLayout.setOnClickListener(new com.tencent.qqlitepro.hook.UiPatcher$$ExternalSyntheticLambda28(runnable));
            }
            android.widget.LinearLayout.LayoutParams layoutParams2 = new android.widget.LinearLayout.LayoutParams(-1, -2);
            layoutParams2.leftMargin = dp(linearLayout, 10.0f);
            layoutParams2.rightMargin = dp(linearLayout, 10.0f);
            layoutParams2.topMargin = dp(linearLayout, 3.0f);
            layoutParams2.bottomMargin = dp(linearLayout, 3.0f);
            viewGroup.addView(linearLayout, layoutParams2);
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("注入文件详情行失败: " + th);
        }
    }

    private static void showFileDetailDialog(android.content.Context context, java.util.List<java.lang.String[]> list) {
        if (context == null || list == null) {
            return;
        }
        try {
            if (list.isEmpty()) {
                return;
            }
            try {
                dismissLongPressMenu();
            } catch (java.lang.Throwable unused) {
            }
            android.widget.LinearLayout linearLayout = new android.widget.LinearLayout(context);
            linearLayout.setOrientation(1);
            linearLayout.setPadding(dp(context, 18.0f), dp(context, 16.0f), dp(context, 18.0f), dp(context, 12.0f));
            android.graphics.drawable.GradientDrawable gradientDrawable = new android.graphics.drawable.GradientDrawable();
            gradientDrawable.setColor(-1);
            gradientDrawable.setCornerRadius(dp(context, 18.0f));
            linearLayout.setBackground(gradientDrawable);
            android.widget.TextView textView = new android.widget.TextView(context);
            textView.setText("文件详情");
            textView.setTextColor(-12957089);
            textView.setTextSize(14.0f);
            textView.setTypeface(android.graphics.Typeface.DEFAULT, 1);
            textView.setPadding(0, 0, 0, dp(context, 3.0f));
            linearLayout.addView(textView);
            android.widget.TextView textView2 = new android.widget.TextView(context);
            textView2.setText("此消息为真实文件，以下为缓存消息中的文件信息");
            textView2.setTextColor(ORANGE);
            textView2.setTextSize(10.0f);
            textView2.setPadding(0, 0, 0, dp(context, 8.0f));
            linearLayout.addView(textView2);
            android.widget.LinearLayout linearLayout2 = new android.widget.LinearLayout(context);
            linearLayout2.setOrientation(1);
            for (java.lang.String[] strArr : list) {
                linearLayout2.addView(buildInfoRow(context, strArr[0], strArr[1]));
            }
            android.widget.ScrollView scrollView = new android.widget.ScrollView(context);
            scrollView.setVerticalScrollBarEnabled(false);
            scrollView.addView(linearLayout2, new android.widget.FrameLayout.LayoutParams(-1, -2));
            linearLayout.addView(scrollView, new android.widget.LinearLayout.LayoutParams(-1, 0, 1.0f));
            int iDp = dp(context, 12.0f);
            android.widget.LinearLayout linearLayout3 = new android.widget.LinearLayout(context);
            linearLayout3.setOrientation(0);
            linearLayout3.setGravity(21);
            linearLayout3.setPadding(0, iDp, 0, 0);
            linearLayout.addView(linearLayout3);
            android.widget.TextView textView3 = new android.widget.TextView(context);
            textView3.setText("关闭");
            textView3.setTextColor(-2693889);
            textView3.setTextSize(13.0f);
            android.graphics.drawable.GradientDrawable gradientDrawable2 = new android.graphics.drawable.GradientDrawable();
            gradientDrawable2.setColor(-14141105);
            gradientDrawable2.setCornerRadius(dp(context, 14.0f));
            textView3.setBackground(gradientDrawable2);
            textView3.setPadding(dp(context, 20.0f), dp(context, 9.0f), dp(context, 20.0f), dp(context, 9.0f));
            android.widget.LinearLayout.LayoutParams layoutParams = new android.widget.LinearLayout.LayoutParams(-2, -2);
            layoutParams.leftMargin = dp(context, 12.0f);
            linearLayout3.addView(textView3, layoutParams);
            android.app.Dialog dialog = new android.app.Dialog(context, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.getWindow().setLayout(-1, -1);
            android.widget.FrameLayout frameLayout = new android.widget.FrameLayout(context);
            frameLayout.setPadding(dp(context, 16.0f), dp(context, 16.0f), dp(context, 16.0f), dp(context, 16.0f));
            frameLayout.addView(linearLayout, new android.widget.FrameLayout.LayoutParams(-1, -1));
            dialog.setContentView(frameLayout);
            textView3.setOnClickListener(new com.tencent.qqlitepro.hook.UiPatcher$$ExternalSyntheticLambda19(dialog));
            dialog.show();
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("文件详情弹窗失败: " + th);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.lang.String dumpTree(android.view.View view, int i) {
        try {
            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            sb.append('\n');
            for (int i2 = 0; i2 < i; i2++) {
                sb.append("  ");
            }
            sb.append(view.getClass().getSimpleName());
            sb.append('[');
            sb.append(view.getId());
            sb.append(']');
            if (view instanceof android.view.ViewGroup) {
                android.view.ViewGroup viewGroup = (android.view.ViewGroup) view;
                for (int i3 = 0; i3 < java.lang.Math.min(viewGroup.getChildCount(), 6); i3++) {
                    sb.append(dumpTree(viewGroup.getChildAt(i3), i + 1));
                }
            }
            return sb.toString();
        } catch (java.lang.Throwable unused) {
            return "";
        }
    }

}
