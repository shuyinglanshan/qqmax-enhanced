package com.tencent.qqlitepro.hook;

/* JADX INFO: loaded from: classes.dex */
public final class SettingsInjector {
    private static final int ACCENT = -12878593;
    private static final int AT_TOKEN_COLOR = -14776065;
    private static final java.lang.String AUDIO_DIR_PATH = "/sdcard/QQMax增强";
    private static final int BTN_FILL = -14141105;
    private static final int BTN_FILL_PRESS = -13548450;
    private static final int BTN_TEXT = -2693889;
    private static final int DIVIDER = 536870911;
    private static final int FILE_LOG_MAX_CHARS = 12000;
    private static final int HINT = -7696488;
    private static final java.lang.String MODULE_PKG = "com.tencent.qqlitepro";
    private static final int[] MODULE_VER;
    private static final java.lang.String NODE_CONFIG_URL = "https://devfile.vip/preview/5bbpqi7q/config.json";
    private static final java.lang.String NOTICE_URL = "https://devfile.vip/preview/5bbpqi7q/notice.json";
    private static final int PAGE_ABOUT = 4;
    private static final int PAGE_AUTO = 2;
    private static final int PAGE_AUTOREPLY = 6;
    private static final int PAGE_DOWNLOAD = 10;
    private static final int PAGE_FANG = 1;
    private static final int PAGE_FILE = 9;
    private static final int PAGE_KEYWORD = 5;
    private static final int PAGE_MAIL = 16;
    private static final int PAGE_MAIL_DETAIL = 19;
    private static final int PAGE_MAIL_INBOX = 18;
    private static final int PAGE_MAIL_SEND = 20;
    private static final int PAGE_MAIL_SETTING = 17;
    private static final int PAGE_MAIL_WEB_BG = 22;
    private static final int PAGE_MENU = 0;
    private static final int PAGE_NOTICE = 11;
    private static final int PAGE_NOTICE_DETAIL = 12;
    private static final int PAGE_NOTICE_NODES = 21;
    private static final int PAGE_PLUGIN = 13;
    private static final int PAGE_PORT = 3;
    private static final int PAGE_QZDYNAMIC = 15;
    private static final int PAGE_SUMMARY = 7;
    private static final int PAGE_VOICE = 8;
    private static final int REQ_DIR_PICK = 24345;
    private static final int REQ_MEDIA_DIR_PICK = 24347;
    private static final int REQ_FILE_PICK = 24344;
    static final int REQ_MAIL_ATTACH = 24347;
    static final int REQ_MAIL_BG = 24349;
    static final int REQ_MAIL_IMAGE = 24348;
    private static final int REQ_PLUGIN_PICK = 24346;
    private static final int SHARD_CONCURRENCY = 3;
    private static final int SHARD_MAX_ATTEMPTS = 3;
    private static final int SUB = -6643546;
    private static final int SURFACE_CONTAINER = -14606554;
    private static final int SWITCH_THUMB_OFF = -6643546;
    private static final int SWITCH_TRACK_OFF = -12829889;
    private static final int TAG_AI_SW = 2132803586;
    private static final int TAG_AT_SW = 2132803587;
    private static final java.lang.String TAG_AUTO_CONTENT = "auto_content_edit";
    private static final int TAG_AUTO_PEERS = 2132803588;
    private static final int TAG_AUTO_PEER_LABEL = 2132803589;
    private static final java.lang.String TAG_AUTO_TIME = "auto_time_edit";
    private static final java.lang.String TAG_KW_EDIT = "kw_edit";
    private static final int TAG_REPLY_AT_SW = 2132803590;
    private static final int TAG_ROW_TITLE = 2132803585;
    private static final java.lang.String TAG_RP_EDIT = "rp_edit";
    private static final java.lang.String TAG_STATUS_ACT = "status_act";
    private static final java.lang.String TAG_STATUS_FW = "status_fw";
    private static final java.lang.String TAG_STATUS_HINT = "status_hint";
    private static final java.lang.String TAG_STATUS_INJ = "status_inj";
    private static final int TITLE = -855051;
    private static final java.lang.String UPDATE_DIR = "/sdcard/QQMax增强/update";
    private static final java.lang.String URL_TRAILING = ".,;:!?)]}）。，；：！？、…”’";
    private static java.io.File sFileChosen;
    private static android.app.Activity sFileHost;
    static android.widget.TextView sFileLog;
    private static java.lang.String sFileMd5;
    private static java.lang.Runnable sFileUiRefresh;
    private static volatile boolean sFileUriAllowed;
    private static volatile int sInboxAutoGen;
    private static volatile android.widget.EditText sMailBodyEdit;
    static android.app.Dialog sMailDialog;
    private static android.widget.TextView sMailLogView;
    private static volatile android.widget.EditText sMailSubjEdit;
    private static volatile android.widget.EditText sMailToEdit;
    private static volatile java.lang.Object sModuleCategory;
    private static volatile com.google.gson.JsonObject sNoticeSelected;
    private static android.view.ViewGroup sPageGroup;
    private static android.view.ViewGroup sPluginGroup;
    private static android.app.Activity sPluginHost;
    private static android.widget.LinearLayout sPluginListBox;
    static android.widget.TextView sPluginLog;
    static android.widget.TextView sQqDynamicLog;
    private static volatile java.lang.ref.WeakReference<android.app.Activity> sSettingsActRef;
    static android.widget.TextView sVoiceLog;
    private static final java.lang.String[] AUDIO_EXTS = {".amr", ".silk", ".slk", ".m4a", ".mp3"};
    private static final java.util.Map<java.lang.String, java.lang.String> sSessionCache = new java.util.LinkedHashMap();
    private static final java.util.Set<java.lang.String> sSelectedKeys = new java.util.LinkedHashSet();
    private static volatile java.util.List<java.lang.String[]> sPickedEntries = new java.util.ArrayList();
    private static volatile boolean sFileSending = false;
    private static volatile int sCurrentPage = 0;
    private static volatile int sPendingPage = 0;
    private static volatile int sMailDetailIndex = -1;
    private static final java.lang.StringBuilder sMailLog = new java.lang.StringBuilder();
    private static final java.util.List<java.io.File> sMailAttach = new java.util.ArrayList();
    private static final java.util.List<java.io.File> sMailImages = new java.util.ArrayList();
    private static volatile boolean sMailHtmlMode = false;
    private static volatile java.lang.String sMailToTxt = "";
    private static volatile java.lang.String sMailSubjTxt = "";
    private static volatile java.lang.String sMailBodyTxt = "";
    private static volatile boolean sPendingCategoryJump = false;
    private static volatile long sNotInGroupToastAt = 0;
    private static final java.util.concurrent.atomic.AtomicBoolean sSelectableTextHooked = new java.util.concurrent.atomic.AtomicBoolean(false);
    private static final android.util.LruCache<java.lang.String, android.graphics.Bitmap> sAvatarMemoryCache = new com.tencent.qqlitepro.hook.SettingsInjector.AnonymousClass2(16777216);
    private static final java.util.Set<android.widget.TextView> sLongPressCopyBound = java.util.Collections.newSetFromMap(new java.util.WeakHashMap());
    private static final java.util.Set<android.widget.TextView> sPluginNoticeTexts = java.util.Collections.newSetFromMap(new java.util.WeakHashMap());
    private static volatile boolean sLinkifyBusy = false;
    /*
     * 修复"插件市场公告链接识别错误/下载失败"：
     * 旧实现用"排除法"，只排除空白与中文标点，但没排除中文汉字本身。
     * 于是 "https://x/p.js请下载" 这种正文（链接后紧跟中文、无空格）会把
     * "请下载" 一起吞进 URL，下载必然 404。
     * 改为"白名单法"：只接受 RFC3986 允许的 ASCII 字符，从根上杜绝吞入中文。
     */
    private static final java.util.regex.Pattern URL_PATTERN = java.util.regex.Pattern.compile(
            "(?i)(?:https?://|www\\.)[A-Za-z0-9\\-._~:/?#\\[\\]@!$&'()*+,;=%]+");
    private static volatile java.lang.String sNoticeNodeUrl = null;

    /* JADX INFO: Access modifiers changed from: private */
    interface ThConsumer {
        void accept(boolean z);
    }

    /** 通用开关处理器：把 UI 开关写回 Config */
    static final class ToggleHandler implements ThConsumer {
        static final int KEY_GROUP_TOAST = 1;
        static final int KEY_TEMP_CHAT = 2;
        private final int key;
        private final android.content.Context ctx;

        ToggleHandler(int key, android.content.Context ctx) {
            this.key = key;
            this.ctx = ctx;
        }

        @Override
        public void accept(boolean z) {
            try {
                if (key == KEY_GROUP_TOAST) {
                    com.tencent.qqlitepro.util.Config.setGroupToastEnabled(ctx, z);
                } else if (key == KEY_TEMP_CHAT) {
                    com.tencent.qqlitepro.util.Config.setTempChatEnabled(ctx, z);
                    // 立即刷新会话列表，卡片是否显示马上生效
                    com.tencent.qqlitepro.http.QqKernel.refreshTempChatList();
                }
            } catch (java.lang.Throwable th) {
                com.tencent.qqlitepro.util.Log.w("开关保存失败: " + th);
            }
        }
    }

    /** 门禁状态 + 刷新按钮：加入群后点一下即可立即检测并注入 */
    /* ==================== 群聊消息数据（近 N 天消息总数 + 发言排行榜 + 截图发送） ==================== */
    private static volatile android.widget.EditText sStatDays;

    /** 群聊消息数据界面：总数/排行/天数自定义/截图发送 */
    public static void openGroupMsgStats(final android.content.Context context, final long groupUin, final String groupName) {
        try {
            android.app.Dialog dialog = new android.app.Dialog(context);
            dialog.requestWindowFeature(1);
            dialog.setCanceledOnTouchOutside(true);
            android.widget.ScrollView scroll = new android.widget.ScrollView(context);
            android.widget.LinearLayout root = new android.widget.LinearLayout(context);
            root.setOrientation(1);
            root.setBackgroundColor(-15657960);
            int pad = dp(context, 16.0f);
            root.setPadding(pad, dp(context, 14.0f), pad, dp(context, 16.0f));

            // 标题栏
            android.widget.LinearLayout header = new android.widget.LinearLayout(context);
            header.setOrientation(0);
            header.setGravity(16);
            header.addView(text(context, "消息数据", TITLE, 17.0f, 3),
                    new android.widget.LinearLayout.LayoutParams(0, -2, 1.0f));
            android.widget.TextView close = text(context, "关闭", ACCENT, 13.0f, 17);
            close.setPadding(dp(context, 10.0f), dp(context, 4.0f), 0, dp(context, 4.0f));
            close.setClickable(true);
            close.setOnClickListener(new android.view.View.OnClickListener() {
                @Override
                public void onClick(android.view.View v) {
                    try { dialog.dismiss(); } catch (Throwable ignored) {}
                }
            });
            header.addView(close);
            root.addView(header, rootLp());
            root.addView(text(context, groupName + "（" + String.valueOf(groupUin) + "）", SUB, 12.5f, 3), withTop(context, 4.0f));

            // 天数输入
            android.widget.EditText etDays = edit(context,
                    String.valueOf(com.tencent.qqlitepro.util.Config.groupStatDays(context)),
                    "统计天数（1-30，默认 7）", 1);
            sStatDays = etDays;
            android.widget.LinearLayout daysCard = filledCard(context);
            daysCard.addView(text(context, "统计范围（天）", TITLE, 14.0f, 8388611), rootLp());
            daysCard.addView(editInCard(context, etDays), inputLp());
            root.addView(daysCard, withTop(context, 10.0f));

            // 结果区
            final android.widget.TextView resultView = text(context, "点击下方按钮开始统计", HINT, 13.0f, 3);
            resultView.setLineSpacing(dp(context, 4.0f), 1.0f);
            root.addView(resultView, withTop(context, 10.0f));
            /* 排行榜容器：每行 头像 + 昵称(QQ号) + 条数 */
            final android.widget.LinearLayout rankBox = new android.widget.LinearLayout(context);
            rankBox.setOrientation(1);
            root.addView(rankBox, withTop(context, 6.0f));

            final android.content.Context fCtx = context;
            // 统计按钮
            android.view.View statBtn = saveButton(context, "开始统计", new Runnable() {
                @Override
                public void run() {
                    try {
                        int days = 7;
                        try {
                            days = Integer.parseInt(sStatDays.getText().toString().trim());
                        } catch (Throwable ignored) {}
                        final int fDays = Math.max(1, Math.min(30, days));
                        com.tencent.qqlitepro.util.Config.setGroupStatDays(fCtx, fDays);
                        resultView.setText("统计中…");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                final com.tencent.qqlitepro.http.QqKernel.GroupMsgStats st =
                                        com.tencent.qqlitepro.http.QqKernel.groupMsgStats(groupUin, fDays);
                                new android.os.Handler(android.os.Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (st.error != null && !st.error.isEmpty()) {
                                            resultView.setText("统计失败：" + st.error);
                                            return;
                                        }
                                        StringBuilder sb = new StringBuilder();
                                        sb.append("近 ").append(st.days).append(" 天消息总数：").append(st.total);
                                        resultView.setText(sb.toString());
                                        /* 排行榜：头像 + 昵称(QQ号) + 条数 */
                                        rankBox.removeAllViews();
                                        int limit = Math.min(15, st.rank.size());
                                        for (int i = 0; i < limit; i++) {
                                            String[] one = st.rank.get(i);
                                            rankBox.addView(renderRankRow(context, i + 1, one[0], one[1], one[2]),
                                                    rootLp());
                                        }
                                        if (st.rank.isEmpty()) {
                                            rankBox.addView(text(context, "（无数据）", HINT, 12.5f, 3), rootLp());
                                        }
                                    }
                                });
                            }
                        }, "group-stat").start();
                    } catch (Throwable th) {
                        com.tencent.qqlitepro.util.Log.w("群统计异常: " + th);
                    }
                }
            });
            root.addView(statBtn, withTop(context, 12.0f));

            /* 生成结果截图：跳到该群聊天，走宿主原生"长按消息→多选→截图"链路
             * （逆向：momoi.mod.qqpro.hook.screenshot.ChatScreenshot.capture 需在
             *   聊天多选界面内调用，由宿主消息长按菜单"多选"进入）。 */
            final android.content.Context fCtxShot = context;
            final android.widget.LinearLayout fRankBox = rankBox;
            final android.widget.TextView fResult = resultView;
            android.view.View shotBtn = saveButton(context, "生成结果截图", new Runnable() {
                @Override
                public void run() {
                    try {
                        /* 把统计结果区域画成 Bitmap，交给宿主原生截图预览（发送/保存/取消） */
                        android.graphics.Bitmap bm = renderStatBitmap(fCtxShot, fResult, fRankBox);
                        if (bm == null) {
                            toast(fCtxShot, "暂无可截图内容");
                            return;
                        }
                        if (openHostScreenshotPreview(fCtxShot, bm)) {
                            try { dialog.dismiss(); } catch (Throwable ignored) {}
                        } else {
                            toast(fCtxShot, "宿主截图预览不可用，已保存到本地");
                        }
                    } catch (Throwable th) {
                        com.tencent.qqlitepro.util.Log.w("生成结果截图失败: " + th);
                        toast(fCtxShot, "生成截图失败：" + th.getMessage());
                    }
                }
            });
            root.addView(shotBtn, withTop(context, 8.0f));

            scroll.addView(root);
            dialog.setContentView(scroll);
            /* 全屏显示（含状态栏区域） */
            if (dialog.getWindow() != null) {
                android.view.WindowManager.LayoutParams attrs = dialog.getWindow().getAttributes();
                attrs.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
                attrs.height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
                dialog.getWindow().setAttributes(attrs);
                dialog.getWindow().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(-15592942));
            }
            dialog.show();
        } catch (Throwable th) {
            com.tencent.qqlitepro.util.Log.w("打开消息数据失败: " + th);
        }
    }

    private static void showZanLog(final android.widget.TextView view, final String line) {
        if (view == null) return;
        new android.os.Handler(android.os.Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                appendLog(view, line);
            }
        });
    }

    /* ==================== QQ动态点赞用户昵称查看 ==================== */
    private static volatile android.widget.TextView sDynZanLog;

    /** 查看最近动态的点赞用户昵称（本人/他人通用，数据来自宿主 FeedManager） */
    public static void openDynZanViewer(android.content.Context context) {
        try {
            android.app.Dialog dialog = new android.app.Dialog(context);
            dialog.requestWindowFeature(1);
            android.widget.ScrollView scroll = new android.widget.ScrollView(context);
            android.widget.LinearLayout root = new android.widget.LinearLayout(context);
            root.setOrientation(1);
            root.setBackgroundColor(-15657960);
            int pad = dp(context, 16.0f);
            root.setPadding(pad, dp(context, 14.0f), pad, dp(context, 16.0f));

            android.widget.LinearLayout header = new android.widget.LinearLayout(context);
            header.setOrientation(0);
            header.setGravity(16);
            header.addView(text(context, "动态点赞用户", TITLE, 17.0f, 3),
                    new android.widget.LinearLayout.LayoutParams(0, -2, 1.0f));
            android.widget.TextView close = text(context, "关闭", ACCENT, 13.0f, 17);
            close.setPadding(dp(context, 10.0f), dp(context, 4.0f), 0, dp(context, 4.0f));
            close.setClickable(true);
            close.setOnClickListener(new android.view.View.OnClickListener() {
                @Override
                public void onClick(android.view.View v) {
                    try { dialog.dismiss(); } catch (Throwable ignored) {}
                }
            });
            header.addView(close);
            root.addView(header, rootLp());

            final android.widget.TextView logView = text(context, "加载中…（需先打开过一次动态页让宿主缓存动态）", HINT, 13.0f, 3);
            logView.setLineSpacing(dp(context, 4.0f), 1.0f);
            sDynZanLog = logView;
            root.addView(logView, withTop(context, 10.0f));
            scroll.addView(root);
            dialog.setContentView(scroll);
            if (dialog.getWindow() != null) {
                android.view.WindowManager.LayoutParams attrs = dialog.getWindow().getAttributes();
                attrs.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
                attrs.height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
                dialog.getWindow().setAttributes(attrs);
                dialog.getWindow().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(-15592942));
            }
            dialog.show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final StringBuilder sb = new StringBuilder();
                    try {
                        java.util.List<com.tencent.qqlitepro.auto.QqDynamicScheduler.FeedRecord> list =
                                com.tencent.qqlitepro.auto.QqDynamicScheduler.snapshot();
                        if (list.isEmpty()) {
                            sb.append("暂无动态缓存。请先打开 QQ 动态页，等宿主加载动态后重试。");
                        } else {
                            int shown = 0;
                            for (com.tencent.qqlitepro.auto.QqDynamicScheduler.FeedRecord rec : list) {
                                if (shown >= 20) { sb.append("\n（仅显示最近 20 条）"); break; }
                                java.util.ArrayList<String> nicks =
                                        com.tencent.qqlitepro.auto.QqDynamicScheduler.feedZanNicks(rec.feed);
                                int total = com.tencent.qqlitepro.auto.QqDynamicScheduler.feedZanCount(rec.feed);
                                if (total <= 0 && nicks.isEmpty()) continue;
                                sb.append("【").append(rec.nick).append("】点赞 ").append(total).append(" 人\n");
                                if (nicks.isEmpty()) {
                                    sb.append("  （内核未返回点赞人昵称）\n\n");
                                } else {
                                    for (String n : nicks) {
                                        sb.append("  · ").append(n).append("\n");
                                    }
                                    sb.append("\n");
                                }
                                shown++;
                            }
                            if (shown == 0) {
                                sb.append("缓存动态均无点赞数据");
                            }
                        }
                    } catch (Throwable th) {
                        sb.append("加载失败：").append(th.getMessage());
                    }
                    final String txt = sb.toString();
                    new android.os.Handler(android.os.Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            if (sDynZanLog != null) sDynZanLog.setText(txt);
                        }
                    });
                }
            }, "dyn-zan-view").start();
        } catch (Throwable th) {
            com.tencent.qqlitepro.util.Log.w("打开动态点赞查看失败: " + th);
        }
    }

    /** 把统计结果（标题+排行）渲染成一张 Bitmap */
    private static android.graphics.Bitmap renderStatBitmap(android.content.Context context,
                                                             android.widget.TextView result,
                                                             android.view.ViewGroup rankBox) {
        try {
            float d = context.getResources().getDisplayMetrics().density;
            int width = context.getResources().getDisplayMetrics().widthPixels;
            android.widget.LinearLayout box = new android.widget.LinearLayout(context);
            box.setOrientation(1);
            box.setBackgroundColor(-15329264);
            int pad = (int) (d * 14);
            box.setPadding(pad, pad, pad, pad);
            if (result != null) {
                android.widget.TextView tv = new android.widget.TextView(context);
                tv.setText(result.getText());
                tv.setTextColor(-1);
                tv.setTextSize(15.0f);
                box.addView(tv);
            }
            if (rankBox != null) {
                android.widget.LinearLayout.LayoutParams lp = new android.widget.LinearLayout.LayoutParams(-1, -2);
                lp.topMargin = (int) (d * 8);
                box.addView(rankBox, lp);
            }
            int wSpec = android.view.View.MeasureSpec.makeMeasureSpec(width, android.view.View.MeasureSpec.EXACTLY);
            int hSpec = android.view.View.MeasureSpec.makeMeasureSpec(0, android.view.View.MeasureSpec.UNSPECIFIED);
            box.measure(wSpec, hSpec);
            int h = Math.max(box.getMeasuredHeight(), (int) (d * 200));
            box.layout(0, 0, width, h);
            android.graphics.Bitmap bm = android.graphics.Bitmap.createBitmap(width, h,
                    android.graphics.Bitmap.Config.ARGB_8888);
            android.graphics.Canvas cv = new android.graphics.Canvas(bm);
            box.draw(cv);
            return bm;
        } catch (Throwable th) {
            com.tencent.qqlitepro.util.Log.w("renderStatBitmap 失败: " + th);
            return null;
        }
    }

    /**
     * 调宿主原生截图预览界面（momoi.mod.qqpro.hook.screenshot.ScreenshotPreview），
     * 之后"发送/保存/取消"全部交宿主处理。
     */
    private static boolean openHostScreenshotPreview(android.content.Context context, android.graphics.Bitmap bm) {
        try {
            ClassLoader cl = context.getClassLoader();
            Class<?> cls = Class.forName("momoi.mod.qqpro.hook.screenshot.ScreenshotPreview", false, cl);
            Object inst = cls.getField("INSTANCE").get(null);
            for (java.lang.reflect.Method m : cls.getDeclaredMethods()) {
                Class<?>[] ps = m.getParameterTypes();
                if (ps.length == 2 && ps[0] == android.content.Context.class && ps[1] == android.graphics.Bitmap.class) {
                    m.setAccessible(true);
                    m.invoke(inst, context, bm);
                    com.tencent.qqlitepro.util.Log.i("已打开宿主截图预览 m=" + m.getName());
                    return true;
                }
            }
            com.tencent.qqlitepro.util.Log.w("ScreenshotPreview 未找到 (Context,Bitmap) 方法");
        } catch (Throwable th) {
            com.tencent.qqlitepro.util.Log.w("打开宿主截图预览失败: " + th);
        }
        return false;
    }

    /** 排行榜单行：头像(qlogo) + "i. 昵称(QQ号)" + "N 条" */
    private static android.view.View renderRankRow(android.content.Context context, int idx,
                                                   String nick, String count, String uin) {
        android.widget.LinearLayout row = new android.widget.LinearLayout(context);
        row.setOrientation(0);
        row.setGravity(16);
        int pad = dp(context, 6.0f);
        row.setPadding(0, pad, 0, pad);

        android.widget.ImageView avatar = new android.widget.ImageView(context);
        android.widget.LinearLayout.LayoutParams avLp = new android.widget.LinearLayout.LayoutParams(
                dp(context, 34.0f), dp(context, 34.0f));
        avatar.setLayoutParams(avLp);
        android.graphics.drawable.GradientDrawable avBg = new android.graphics.drawable.GradientDrawable();
        avBg.setColor(-10785013);
        avBg.setShape(android.graphics.drawable.GradientDrawable.OVAL);
        avatar.setBackground(avBg);
        row.addView(avatar);

        android.widget.LinearLayout mid = new android.widget.LinearLayout(context);
        mid.setOrientation(1);
        android.widget.LinearLayout.LayoutParams midLp = new android.widget.LinearLayout.LayoutParams(
                0, -2, 1.0f);
        midLp.leftMargin = dp(context, 10.0f);
        mid.setLayoutParams(midLp);
        mid.addView(text(context, idx + ". " + (nick == null || nick.isEmpty() ? "未知" : nick), TITLE, 13.5f, 3),
                rootLp());
        mid.addView(text(context, "QQ:" + (uin == null || uin.isEmpty() ? "?" : uin), HINT, 11.5f, 3),
                rootLp());
        row.addView(mid);

        row.addView(text(context, (count == null ? "0" : count) + " 条", ACCENT, 12.5f, 17));
        try {
            final long u = Long.parseLong(uin);
            final android.widget.ImageView fav = avatar;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        java.net.URL url = new java.net.URL("https://q1.qlogo.cn/g?b=qq&nk=" + u + "&s=100");
                        java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
                        conn.setConnectTimeout(8000);
                        conn.setReadTimeout(8000);
                        conn.setDoInput(true);
                        android.graphics.Bitmap bm = android.graphics.BitmapFactory.decodeStream(conn.getInputStream());
                        if (bm != null) {
                            new android.os.Handler(android.os.Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    try { fav.setImageBitmap(bm); } catch (Throwable ignored) {}
                                }
                            });
                        }
                    } catch (Throwable ignored) {}
                }
            }, "rank-av-" + u).start();
        } catch (Throwable ignored) {}
        return row;
    }


    private static void addGateStatusRow(final android.content.Context context, android.view.ViewGroup viewGroup) {
        try {
            boolean passed = com.tencent.qqlitepro.MainHook.isGroupPassed();
            android.widget.LinearLayout card = new android.widget.LinearLayout(context);
            card.setOrientation(1);
            android.graphics.drawable.GradientDrawable bg = new android.graphics.drawable.GradientDrawable();
            bg.setColor(-14013129);
            bg.setCornerRadius(dpLite(10.0f));
            card.setBackground(bg);
            int p = dp(context, 12);
            card.setPadding(p, p, p, p);

            android.widget.TextView st = text(context,
                    passed ? "官方群状态：已加入（功能已注入）" : "官方群状态：未检测到（仅保留设置入口）",
                    passed ? -11751600 : -1739917, 13.5f, 3);
            card.addView(st, rootLp());

            android.widget.TextView tip = text(context,
                    passed ? "若功能异常，可点右侧刷新重新检测"
                           : "加入官方群后点「立即检测」，无需重启宿主即可注入",
                    HINT, 12.0f, 3);
            tip.setLineSpacing(dp(context, 3), 1.0f);
            android.widget.LinearLayout.LayoutParams tipLp = rootLp();
            tipLp.topMargin = dp(context, 5);
            card.addView(tip, tipLp);

            android.widget.TextView btn = text(context, "立即检测", -1, 13.5f, 17);
            android.graphics.drawable.GradientDrawable bb = new android.graphics.drawable.GradientDrawable();
            bb.setColor(-11751600);
            bb.setCornerRadius(dpLite(18.0f));
            btn.setBackground(bb);
            btn.setPadding(0, dp(context, 9), 0, dp(context, 9));
            btn.setClickable(true);
            btn.setOnClickListener(new android.view.View.OnClickListener() {
                @Override
                public void onClick(android.view.View v) {
                    try {
                        toast(context, "正在重新检测官方群…");
                        com.tencent.qqlitepro.MainHook.recheckGroupNow();
                    } catch (java.lang.Throwable th) {
                        com.tencent.qqlitepro.util.Log.w("刷新门禁失败: " + th);
                    }
                }
            });
            android.widget.LinearLayout.LayoutParams btnLp = rootLp();
            btnLp.topMargin = dp(context, 9);
            card.addView(btn, btnLp);

            android.widget.LinearLayout.LayoutParams cardLp = rootLp();
            cardLp.topMargin = dp(context, 8);
            viewGroup.addView(card, cardLp);
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("构建门禁状态行失败: " + th);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int fwColor(int i) {
        return (i == 0 || i == 1) ? -1739917 : -11751600;
    }

    private static void hideKeyboardOnClick(android.content.Context context) {
    }

    private static boolean isNearWhite(int i) {
        if (((i >> 24) & org.mozilla.classfile.ByteCode.IMPDEP2) < 16) {
            return true;
        }
        return ((i >> 16) & org.mozilla.classfile.ByteCode.IMPDEP2) >= 242 && ((i >> 8) & org.mozilla.classfile.ByteCode.IMPDEP2) >= 242 && (i & org.mozilla.classfile.ByteCode.IMPDEP2) >= 242;
    }

    public static void pendingMailInbox() {
        sPendingPage = 18;
    }

    static {
        int[] version = parseVersion(com.tencent.qqlitepro.BuildConfig.VERSION_NAME);
        if (version == null) {
            version = new int[]{0, 0, 0, 0};
        }
        MODULE_VER = version;
        sMailDialog = null;
        sInboxAutoGen = 0;
        sFileUriAllowed = false;
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.SettingsInjector$1, reason: invalid class name */
    static class AnonymousClass1 implements android.text.TextWatcher {
        @Override // android.text.TextWatcher
        public void beforeTextChanged(java.lang.CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(java.lang.CharSequence charSequence, int i, int i2, int i3) {
        }

        AnonymousClass1() {
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(android.text.Editable editable) {
            com.tencent.qqlitepro.hook.SettingsInjector.paintAtToken(editable);
        }
    }

    private static void setupAtTokenInput(android.widget.EditText editText) {
        if (editText == null) {
            return;
        }
        editText.addTextChangedListener(new com.tencent.qqlitepro.hook.SettingsInjector.AnonymousClass1());
        paintAtToken(editText.getText());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void paintAtToken(android.text.Editable editable) {
        if (editable == null) {
            return;
        }
        for (android.text.style.ForegroundColorSpan foregroundColorSpan : (android.text.style.ForegroundColorSpan[]) editable.getSpans(0, editable.length(), android.text.style.ForegroundColorSpan.class)) {
            if (foregroundColorSpan.getForegroundColor() == AT_TOKEN_COLOR) {
                editable.removeSpan(foregroundColorSpan);
            }
        }
        java.lang.String[] strArr = {com.tencent.qqlitepro.http.QqKernel.AT_TOKEN, com.tencent.qqlitepro.http.QqKernel.AT_ALL_TOKEN};
        java.lang.String string = editable.toString();
        for (int i = 0; i < 2; i++) {
            java.lang.String str = strArr[i];
            int length = 0;
            while (true) {
                int iIndexOf = string.indexOf(str, length);
                if (iIndexOf < 0) {
                    break;
                }
                editable.setSpan(new android.text.style.ForegroundColorSpan(AT_TOKEN_COLOR), iIndexOf, str.length() + iIndexOf, 33);
                length = iIndexOf + str.length();
            }
        }
    }

    private static android.view.View atInsertButton(android.content.Context context, android.widget.EditText editText) {
        android.widget.LinearLayout linearLayout = new android.widget.LinearLayout(context);
        linearLayout.setOrientation(1);
        linearLayout.addView(saveButton(context, "添加艾特", new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda119(editText)));
        android.widget.LinearLayout.LayoutParams layoutParamsRootLp = rootLp();
        layoutParamsRootLp.topMargin = dp(context, 4.0f);
        layoutParamsRootLp.leftMargin = dp(context, 12.0f);
        layoutParamsRootLp.rightMargin = dp(context, 12.0f);
        linearLayout.addView(saveButton(context, "添加艾特全体", new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda120(editText)), layoutParamsRootLp);
        return linearLayout;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void insertAtToken(android.widget.EditText editText, java.lang.String str) {
        int selectionStart = editText.getSelectionStart();
        int selectionEnd = editText.getSelectionEnd();
        if (selectionStart < 0 || selectionEnd < 0) {
            editText.append(str);
            return;
        }
        editText.getText().replace(java.lang.Math.min(selectionStart, selectionEnd), java.lang.Math.max(selectionStart, selectionEnd), str);
        editText.requestFocus();
    }

    private static android.view.View atAllInsertButton(android.content.Context context, android.widget.EditText editText) {
        return saveButton(context, "添加艾特全体", new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda135(editText));
    }

    private static android.view.View replyAtSwitchRow(android.content.Context context, boolean z, android.widget.CompoundButton[] compoundButtonArr) {
        return switchRowState(context, "回复并艾特", "开启后发送时自动在句首艾特发送者并空格；若已用「添加艾特」或「添加艾特全体」插入蓝色标记则不再自动句首艾特", z, compoundButtonArr);
    }

    private static void showSessionPicker(android.content.Context context, java.lang.Runnable runnable) {
        showSessionPicker(context, false, null, runnable);
    }

    private static void showSessionPicker(android.content.Context context, boolean z, java.lang.Runnable runnable) {
        showSessionPicker(context, z, null, runnable);
    }

    private static void showSessionPicker(android.content.Context context, boolean z, java.util.Set<java.lang.String> set, java.lang.Runnable runnable) {
        if (com.tencent.qqlitepro.util.Config.nativePickerEnabled(context) && tryNativeSessionPicker(context, z, set, runnable)) {
            return;
        }
        showLegacySessionPicker(context, z, set, runnable);
    }

    private static boolean tryNativeSessionPicker(android.content.Context context, boolean z, java.util.Set<java.lang.String> set, java.lang.Runnable runnable) {
        try {
            java.util.LinkedHashMap linkedHashMap = new java.util.LinkedHashMap(sSessionCache);
            if (set != null) {
                return com.tencent.qqlitepro.hook.NativePickerBridge.start(context, z, new java.util.LinkedHashSet(set), null, linkedHashMap, runnable);
            }
            return com.tencent.qqlitepro.hook.NativePickerBridge.start(context, z, null, new java.util.LinkedHashSet(sSelectedKeys), linkedHashMap, runnable);
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("原生选择器启动异常: " + th);
            return false;
        }
    }

    private static void showLegacySessionPicker(android.content.Context context, boolean z, java.util.Set<java.lang.String> set, java.lang.Runnable runnable) {
        if (set != null) {
            sSelectedKeys.clear();
        }
        java.util.ArrayList arrayList = new java.util.ArrayList();
        boolean[] zArr = {false};
        android.widget.LinearLayout linearLayout = new android.widget.LinearLayout(context);
        linearLayout.setOrientation(1);
        android.widget.TextView textViewText = text(context, "目标会话：已选 " + sSelectedKeys.size() + " 个", TITLE, 14.0f, 8388611);
        textViewText.setPadding(dp(context, 4.0f), 0, 4, dp(context, 6.0f));
        linearLayout.addView(textViewText);
        android.widget.EditText editTextEdit = edit(context, "", "搜索会话名称 / 备注 / UID", 1);
        editTextEdit.setSingleLine(true);
        editTextEdit.setImeOptions(3);
        editTextEdit.setInputType(524288);
        android.widget.LinearLayout linearLayout2 = new android.widget.LinearLayout(context);
        linearLayout2.setOrientation(0);
        linearLayout2.setGravity(16);
        linearLayout2.addView(editInCard(context, editTextEdit), new android.widget.LinearLayout.LayoutParams(0, -2, 1.0f));
        linearLayout2.setLayoutParams(rootLp());
        linearLayout.addView(linearLayout2);
        android.widget.TextView textViewText2 = text(context, "正在获取会话列表...", HINT, 12.0f, 8388611);
        textViewText2.setPadding(0, dp(context, 6.0f), 0, dp(context, 6.0f));
        linearLayout.addView(textViewText2);
        android.widget.LinearLayout linearLayout3 = new android.widget.LinearLayout(context);
        linearLayout3.setOrientation(1);
        linearLayout.addView(linearLayout3);
        editTextEdit.setOnEditorActionListener(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda90(linearLayout3, textViewText2, arrayList, editTextEdit));
        android.view.View viewSessionActionButton = sessionActionButton(context, "刷新会话列表", new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda101(context, linearLayout3, textViewText2, textViewText, arrayList, zArr, z, set));
        setPickerBtnLp(context, viewSessionActionButton, 6, 0);
        linearLayout.addView(viewSessionActionButton);
        android.view.View view = new android.view.View(context);
        view.setLayoutParams(new android.widget.LinearLayout.LayoutParams(-1, dp(context, 8.0f)));
        linearLayout.addView(view);
        final android.app.Dialog[] dialogArr = {showBottomSheet(context, linearLayout)};
        android.view.View viewSessionActionButton2 = sessionActionButton(context, "确认选择", new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda112(runnable, dialogArr));
        setPickerBtnLp(context, viewSessionActionButton2, 0, 6);
        linearLayout.addView(viewSessionActionButton2);
        loadSessionsAsync(context, linearLayout3, textViewText2, textViewText, arrayList, zArr, z, set);
    }

    static /* synthetic */ boolean lambda$showLegacySessionPicker$3(android.widget.LinearLayout linearLayout, android.widget.TextView textView, java.util.List list, android.widget.EditText editText, android.widget.TextView textView2, int i, android.view.KeyEvent keyEvent) {
        if (i != 3 && i != 6 && i != 2 && (keyEvent == null || keyEvent.getKeyCode() != 66)) {
            return false;
        }
        applySessionFilter(linearLayout, textView, list, editText.getText().toString());
        return true;
    }

    static /* synthetic */ void lambda$showLegacySessionPicker$5(java.lang.Runnable runnable, android.app.Dialog[] dialogArr) {
        if (runnable != null) {
            try {
                runnable.run();
            } catch (java.lang.Throwable unused) {
                return;
            }
        }
        android.app.Dialog dialog = dialogArr[0];
        if (dialog == null || !dialog.isShowing()) {
            return;
        }
        dialogArr[0].dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void loadSessionsAsync(android.content.Context context, android.widget.LinearLayout linearLayout, android.widget.TextView textView, android.widget.TextView textView2, java.util.List<java.lang.String[]> list, boolean[] zArr, boolean z, java.util.Set<java.lang.String> set) {
        if (zArr[0]) {
            textView.setText("正在获取会话列表，请稍候...");
            return;
        }
        zArr[0] = true;
        textView.setText("正在获取会话列表...");
        new java.lang.Thread(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda28(z, new android.os.Handler(android.os.Looper.getMainLooper()), zArr, textView, list, set, textView2, linearLayout), "session-picker-load").start();
    }

    static /* synthetic */ void lambda$loadSessionsAsync$8(boolean z, android.os.Handler handler, boolean[] zArr, android.widget.TextView textView, java.util.List list, java.util.Set set, android.widget.TextView textView2, android.widget.LinearLayout linearLayout) {
        try {
            java.util.ArrayList<java.lang.Object> sessions = com.tencent.qqlitepro.http.QqKernel.getSessions();
            if (z) {
                try {
                    sessions.addAll(com.tencent.qqlitepro.http.QqKernel.getBuddies());
                } catch (java.lang.Throwable th) {
                    com.tencent.qqlitepro.util.Log.w("好友列表获取失败: " + th);
                }
            }
            handler.post(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda148(zArr, list, filterSessions(sessions, "", z), set, textView2, textView, linearLayout));
        } catch (java.lang.Throwable th2) {
            com.tencent.qqlitepro.util.Log.w("会话加载失败：" + th2);
            handler.post(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda147(zArr, textView, th2));
        }
    }

    static /* synthetic */ void lambda$loadSessionsAsync$6(boolean[] zArr, android.widget.TextView textView, java.lang.Throwable th) {
        zArr[0] = false;
        if (textView != null) {
            textView.setText("获取会话失败：" + th + "，请重试");
        }
    }

    static /* synthetic */ void lambda$loadSessionsAsync$7(boolean[] zArr, java.util.List list, java.util.List list2, java.util.Set set, android.widget.TextView textView, android.widget.TextView textView2, android.widget.LinearLayout linearLayout) {
        java.lang.String str;
        java.lang.String str2;
        java.lang.String str3;
        zArr[0] = false;
        try {
            list.clear();
            list.addAll(list2);
            sPickedEntries = list2;
            populateCache(list2);
            if (set != null && !set.isEmpty()) {
                sSelectedKeys.clear();
                java.util.Iterator it = list2.iterator();
                while (it.hasNext()) {
                    java.lang.String[] strArr = (java.lang.String[]) it.next();
                    if (strArr != null && strArr.length >= 5 && (str2 = strArr[1]) != null && !str2.isEmpty() && (str3 = strArr[4]) != null && set.contains(str3)) {
                        sSelectedKeys.add(strArr[0] + ":" + strArr[1]);
                    }
                }
            }
            if (textView != null) {
                textView.setText("目标会话：已选 " + sSelectedKeys.size() + " 个（共 " + list2.size() + " 个）");
            }
            if (textView2 != null) {
                if (list2.isEmpty()) {
                    str = "暂无可选会话";
                } else {
                    str = "已加载 " + list2.size() + " 个会话，可搜索过滤";
                }
                textView2.setText(str);
            }
            renderEntries(linearLayout, list2);
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("渲染会话列表失败: " + th);
        }
    }

    private static void applySessionFilter(android.widget.LinearLayout linearLayout, android.widget.TextView textView, java.util.List<java.lang.String[]> list, java.lang.String str) {
        try {
            java.util.ArrayList arrayList = new java.util.ArrayList();
            java.lang.String lowerCase = str == null ? "" : str.trim().toLowerCase();
            for (java.lang.String[] strArr : list) {
                if (strArr != null && strArr.length >= 3) {
                    java.lang.String str2 = strArr[2];
                    java.lang.String str3 = strArr[1];
                    if (lowerCase.isEmpty() || ((str2 != null && str2.toLowerCase().contains(lowerCase)) || (str3 != null && str3.toLowerCase().contains(lowerCase)))) {
                        arrayList.add(strArr);
                    }
                }
            }
            if (textView != null) {
                textView.setText("共 " + arrayList.size() + " 个匹配会话");
            }
            renderEntries(linearLayout, arrayList);
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("过滤会话异常: " + th);
        }
    }

    private static void renderEntries(android.widget.LinearLayout linearLayout, java.util.List<java.lang.String[]> list) {
        try {
            android.content.Context context = linearLayout.getContext();
            linearLayout.removeAllViews();
            if (list != null && !list.isEmpty()) {
                java.util.ArrayList<java.lang.String[]> arrayList = new java.util.ArrayList(list);
                arrayList.sort(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda211());
                for (java.lang.String[] strArr : arrayList) {
                    if (strArr != null && strArr.length >= 3) {
                        linearLayout.addView(sessionRow(context, strArr[0], strArr[1], strArr[2], strArr.length >= 4 ? strArr[3] : null));
                    }
                }
            }
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("列表渲染异常: " + th);
        }
    }

    static /* synthetic */ int lambda$renderEntries$9(java.lang.String[] strArr, java.lang.String[] strArr2) {
        boolean zIsSessionSelected = isSessionSelected(strArr);
        if (zIsSessionSelected != isSessionSelected(strArr2)) {
            return zIsSessionSelected ? -1 : 1;
        }
        return 0;
    }

    private static boolean isSessionSelected(java.lang.String[] strArr) {
        if (strArr == null || strArr.length < 2) {
            return false;
        }
        return sSelectedKeys.contains(strArr[0] + ":" + strArr[1]);
    }

    private static android.app.Dialog showBottomSheet(android.content.Context context, android.view.View view) {
        android.app.Dialog dialog = new android.app.Dialog(context, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        com.tencent.qqlitepro.hook.SettingsInjector.ScrollableCard scrollableCard = new com.tencent.qqlitepro.hook.SettingsInjector.ScrollableCard(context, view);
        scrollableCard.setPadding(0, dp(context, 16.0f), 0, dp(context, 16.0f));
        dialog.setContentView(scrollableCard);
        android.view.WindowManager.LayoutParams attributes = dialog.getWindow().getAttributes();
        attributes.width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.94f);
        attributes.height = (int) (context.getResources().getDisplayMetrics().heightPixels * 0.8f);
        dialog.getWindow().setAttributes(attributes);
        dialog.show();
        return dialog;
    }

    private static final class ScrollableCard extends android.widget.FrameLayout {
        ScrollableCard(android.content.Context context, android.view.View view) {
            super(context);
            android.graphics.drawable.GradientDrawable gradientDrawable = new android.graphics.drawable.GradientDrawable();
            gradientDrawable.setColor(com.tencent.qqlitepro.hook.SettingsInjector.SURFACE_CONTAINER);
            gradientDrawable.setCornerRadius(com.tencent.qqlitepro.hook.SettingsInjector.dpLite(14.0f));
            setBackground(gradientDrawable);
            android.widget.ScrollView scrollView = new android.widget.ScrollView(context);
            scrollView.addView(view, new android.widget.FrameLayout.LayoutParams(-1, -2));
            addView(scrollView, new android.widget.FrameLayout.LayoutParams(-1, -1));
        }
    }

    private static java.util.List<java.lang.String[]> filterSessions(java.util.List<java.lang.Object> list, java.lang.String str, boolean z) {
        java.lang.String str2;
        java.lang.String str3;
        java.lang.String str4;
        java.util.ArrayList arrayList = new java.util.ArrayList();
        if (list == null) {
            return arrayList;
        }
        java.lang.String lowerCase = str == null ? "" : str.trim().toLowerCase();
        java.util.LinkedHashMap linkedHashMap = new java.util.LinkedHashMap();
        for (java.lang.Object obj : list) {
            boolean z2 = obj instanceof java.util.Map;
            java.lang.String strMapStr = z2 ? mapStr((java.util.Map) obj, "chatType", "2") : "2";
            java.lang.String strMapStr2 = z2 ? mapStr((java.util.Map) obj, "peerUid", "") : "";
            java.lang.String strMapStr3 = z2 ? mapStr((java.util.Map) obj, "peerUin", "") : "";
            java.lang.String strMapStr4 = z2 ? mapStr((java.util.Map) obj, "avatarPath", "") : "";
            java.lang.String strMapStr5 = z2 ? mapStr((java.util.Map) obj, "peerName", "") : "";
            java.lang.String strMapStr6 = z2 ? mapStr((java.util.Map) obj, "remark", "") : "";
            java.lang.String str5 = (strMapStr6 == null || strMapStr6.isEmpty()) ? strMapStr5 : strMapStr6;
            if (str5 == null || str5.isEmpty()) {
                str5 = strMapStr2;
            }
            if (!z || "1".equals(strMapStr)) {
                if (lowerCase.isEmpty() || ((strMapStr5 != null && strMapStr5.toLowerCase().contains(lowerCase)) || ((strMapStr6 != null && strMapStr6.toLowerCase().contains(lowerCase)) || (strMapStr2 != null && strMapStr2.toLowerCase().contains(lowerCase))))) {
                    java.lang.String str6 = strMapStr + ":" + strMapStr2;
                    java.lang.String[] strArr = strMapStr2.isEmpty() ? null : (java.lang.String[]) linkedHashMap.get(str6);
                    if (strArr != null) {
                        if (strArr.length >= 5 && (((str4 = strArr[4]) == null || str4.isEmpty()) && strMapStr3 != null && !strMapStr3.isEmpty())) {
                            strArr[4] = strMapStr3;
                        }
                        if (strArr.length >= 3 && (str3 = strArr[2]) != null && str3.equals(strArr[1]) && strMapStr6 != null && !strMapStr6.isEmpty()) {
                            strArr[2] = strMapStr6;
                        }
                        if (strArr.length >= 4 && ((str2 = strArr[3]) == null || str2.isEmpty())) {
                            if (strMapStr4 != null && !strMapStr4.isEmpty()) {
                                strArr[3] = strMapStr4;
                            }
                        }
                    } else {
                        java.lang.String[] strArr2 = {strMapStr, strMapStr2, str5, strMapStr4, strMapStr3};
                        if (!strMapStr2.isEmpty()) {
                            linkedHashMap.put(str6, strArr2);
                        }
                        arrayList.add(strArr2);
                    }
                }
            }
        }
        return arrayList;
    }

    private static java.lang.String mapStr(java.util.Map<?, ?> map, java.lang.String str, java.lang.String str2) {
        java.lang.Object obj = map.get(str);
        return obj == null ? str2 : java.lang.String.valueOf(obj);
    }

    private static void populateCache(java.util.List<java.lang.String[]> list) {
        java.lang.String str;
        for (java.lang.String[] strArr : list) {
            if (strArr.length >= 3 && (str = strArr[1]) != null && !str.isEmpty()) {
                sSessionCache.put(strArr[0] + ":" + strArr[1], strArr[0] + "|" + strArr[1] + "|" + strArr[2]);
            }
        }
    }

    private static android.view.View sessionRow(android.content.Context context, java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.String str4) {
        java.lang.String str5 = str + ":" + str2;
        android.widget.LinearLayout linearLayout = new android.widget.LinearLayout(context);
        linearLayout.setOrientation(0);
        linearLayout.setGravity(16);
        linearLayout.setPadding(dp(context, 12.0f), dp(context, 10.0f), dp(context, 12.0f), dp(context, 10.0f));
        linearLayout.setLayoutParams(rootLp());
        android.widget.ImageView imageView = new android.widget.ImageView(context);
        int iDp = dp(context, 34.0f);
        android.widget.LinearLayout.LayoutParams layoutParams = new android.widget.LinearLayout.LayoutParams(iDp, iDp);
        layoutParams.gravity = 16;
        layoutParams.rightMargin = dp(context, 10.0f);
        imageView.setLayoutParams(layoutParams);
        imageView.setBackground(avatarHolderDrawable(context));
        linearLayout.addView(imageView);
        android.widget.LinearLayout linearLayout2 = new android.widget.LinearLayout(context);
        linearLayout2.setOrientation(1);
        linearLayout2.setLayoutParams(new android.widget.LinearLayout.LayoutParams(0, -2, 1.0f));
        java.lang.String str6 = "1".equals(str) ? "好友" : "群聊";
        if (str3 == null || str3.isEmpty()) {
            str3 = str2;
        }
        android.widget.TextView textViewText = text(context, str3, TITLE, 14.0f, 8388611);
        android.widget.TextView textViewText2 = text(context, str6 + " · " + str2, -6643546, 11.0f, 8388611);
        linearLayout2.addView(textViewText, rootLp());
        linearLayout2.addView(textViewText2, rootLp());
        linearLayout.addView(linearLayout2);
        android.widget.TextView textViewText3 = text(context, "✓", ACCENT, 18.0f, 17);
        textViewText3.setWidth(dp(context, 28.0f));
        textViewText3.setGravity(17);
        textViewText3.setVisibility(sSelectedKeys.contains(str5) ? 0 : 4);
        linearLayout.addView(textViewText3);
        if (str4 != null && !str4.trim().isEmpty()) {
            loadAvatar(context, imageView, str4.trim());
        }
        linearLayout.setOnClickListener(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda116(str5, textViewText3, textViewText));
        return linearLayout;
    }

    static /* synthetic */ void lambda$sessionRow$10(java.lang.String str, android.widget.TextView textView, android.widget.TextView textView2, android.view.View view) {
        java.util.Set<java.lang.String> set = sSelectedKeys;
        if (set.contains(str)) {
            set.remove(str);
            textView.setVisibility(4);
            textView2.setTextColor(TITLE);
        } else {
            set.add(str);
            textView.setVisibility(0);
            textView2.setTextColor(ACCENT);
        }
    }

    private static android.graphics.drawable.Drawable avatarHolderDrawable(android.content.Context context) {
        return new android.graphics.drawable.LayerDrawable(new android.graphics.drawable.Drawable[]{roundRectDrawable(context, -3158065, dpLite(17.0f))});
    }

    private static android.graphics.drawable.GradientDrawable roundRectDrawable(android.content.Context context, int i, int i2) {
        android.graphics.drawable.GradientDrawable gradientDrawable = new android.graphics.drawable.GradientDrawable();
        gradientDrawable.setShape(0);
        gradientDrawable.setColor(i);
        gradientDrawable.setCornerRadius(i2);
        return gradientDrawable;
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.SettingsInjector$2, reason: invalid class name */
    static class AnonymousClass2 extends android.util.LruCache<java.lang.String, android.graphics.Bitmap> {
        AnonymousClass2(int i) {
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

    private static void loadAvatar(android.content.Context context, android.widget.ImageView imageView, java.lang.String str) {
        if (str == null || str.trim().isEmpty()) {
            return;
        }
        java.lang.String strTrim = str.trim();
        android.graphics.Bitmap bitmap = sAvatarMemoryCache.get(strTrim);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            new java.lang.Thread(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda118(strTrim, imageView), "avatar-load").start();
        }
    }

    static /* synthetic */ void lambda$loadAvatar$12(java.lang.String str, android.widget.ImageView imageView) {
        android.os.Handler handler;
        com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda117 settingsInjector$$ExternalSyntheticLambda117;
        byte[] bArrDownloadBytes;
        android.graphics.Bitmap bitmapDecodeByteArray;
        android.graphics.Bitmap bitmapCircleCrop = null;
        try {
            if (str.startsWith("http://") || str.startsWith("https://")) {
                bArrDownloadBytes = downloadBytes(str);
            } else {
                java.io.File file = new java.io.File(str);
                bArrDownloadBytes = (file.exists() && file.isFile() && file.length() > 0) ? readFile(file) : null;
            }
            if (bArrDownloadBytes != null && bArrDownloadBytes.length > 0 && (bitmapDecodeByteArray = android.graphics.BitmapFactory.decodeByteArray(bArrDownloadBytes, 0, bArrDownloadBytes.length)) != null) {
                bitmapCircleCrop = circleCrop(bitmapDecodeByteArray);
                sAvatarMemoryCache.put(str, bitmapCircleCrop);
            }
            handler = new android.os.Handler(android.os.Looper.getMainLooper());
            settingsInjector$$ExternalSyntheticLambda117 = new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda117(bitmapCircleCrop, imageView);
        } catch (java.lang.Throwable unused) {
            handler = new android.os.Handler(android.os.Looper.getMainLooper());
            settingsInjector$$ExternalSyntheticLambda117 = new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda117(bitmapCircleCrop, imageView);
        }
        handler.post(settingsInjector$$ExternalSyntheticLambda117);
    }

    static /* synthetic */ void lambda$loadAvatar$11(android.graphics.Bitmap bitmap, android.widget.ImageView imageView) {
        if (bitmap != null) {
            try {
                imageView.setImageBitmap(bitmap);
            } catch (java.lang.Throwable unused) {
            }
        }
    }

    private static byte[] downloadBytes(java.lang.String str) throws java.io.IOException {
        java.io.InputStream inputStream = null;
        try {
            java.net.HttpURLConnection httpURLConnection = (java.net.HttpURLConnection) new java.net.URL(str).openConnection();
            httpURLConnection.setConnectTimeout(com.tencent.qqlitepro.plugin.PluginEngine.LOG_MAX);
            httpURLConnection.setReadTimeout(com.tencent.qqlitepro.plugin.PluginEngine.LOG_MAX);
            httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android)");
            httpURLConnection.setDoInput(true);
            inputStream = httpURLConnection.getInputStream();
            return readAll(inputStream);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (java.lang.Throwable unused) {
                }
            }
        }
    }

    private static byte[] readFile(java.io.File file) {
        java.io.FileInputStream fileInputStream;
        try {
            fileInputStream = new java.io.FileInputStream(file);
            try {
                byte[] all = readAll(fileInputStream);
                try {
                    fileInputStream.close();
                } catch (java.lang.Throwable unused) {
                }
                return all;
            } catch (java.lang.Throwable unused2) {
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (java.lang.Throwable unused3) {
                    }
                }
                return null;
            }
        } catch (java.lang.Throwable unused4) {
            fileInputStream = null;
        }
        return null;
    }

    private static byte[] readAll(java.io.InputStream inputStream) throws java.io.IOException {
        java.io.ByteArrayOutputStream byteArrayOutputStream = new java.io.ByteArrayOutputStream();
        byte[] bArr = new byte[8192];
        while (true) {
            int i = inputStream.read(bArr);
            if (i != -1) {
                byteArrayOutputStream.write(bArr, 0, i);
            } else {
                return byteArrayOutputStream.toByteArray();
            }
        }
    }

    private static android.graphics.Bitmap circleCrop(android.graphics.Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int i = 0;
        int i2 = 0;
        while (i2 < height && !rowHasContent(bitmap, i2, 0, width - 1)) {
            i2++;
        }
        int i3 = height - 1;
        int i4 = i3;
        while (i4 > i2 && !rowHasContent(bitmap, i4, 0, width - 1)) {
            i4--;
        }
        while (i < width && !colHasContent(bitmap, i, i2, i4)) {
            i++;
        }
        int i5 = width - 1;
        int i6 = i5;
        while (i6 > i && !colHasContent(bitmap, i6, i2, i4)) {
            i6--;
        }
        if (i > 0 || i2 > 0 || i6 < i5 || i4 < i3) {
            bitmap = android.graphics.Bitmap.createBitmap(bitmap, i, i2, (i6 - i) + 1, (i4 - i2) + 1);
        }
        int iMin = java.lang.Math.min(bitmap.getWidth(), bitmap.getHeight());
        android.graphics.Bitmap bitmapCreateBitmap = android.graphics.Bitmap.createBitmap(iMin, iMin, android.graphics.Bitmap.Config.ARGB_8888);
        android.graphics.Canvas canvas = new android.graphics.Canvas(bitmapCreateBitmap);
        android.graphics.Paint paint = new android.graphics.Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        float f = iMin;
        float f2 = f / 2.0f;
        canvas.drawCircle(f2, f2, f2, paint);
        paint.setXfermode(new android.graphics.PorterDuffXfermode(android.graphics.PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, new android.graphics.Rect((bitmap.getWidth() - iMin) / 2, (bitmap.getHeight() - iMin) / 2, (bitmap.getWidth() + iMin) / 2, (bitmap.getHeight() + iMin) / 2), new android.graphics.RectF(0.0f, 0.0f, f, f), paint);
        return bitmapCreateBitmap;
    }

    private static boolean rowHasContent(android.graphics.Bitmap bitmap, int i, int i2, int i3) {
        while (i2 <= i3) {
            if (!isNearWhite(bitmap.getPixel(i2, i))) {
                return true;
            }
            i2 += 2;
        }
        return false;
    }

    private static boolean colHasContent(android.graphics.Bitmap bitmap, int i, int i2, int i3) {
        while (i2 <= i3) {
            if (!isNearWhite(bitmap.getPixel(i, i2))) {
                return true;
            }
            i2 += 2;
        }
        return false;
    }

    private static java.lang.String buildPeersString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        for (java.lang.String str : sSelectedKeys) {
            if (str != null && !str.isEmpty()) {
                java.lang.String str2 = sSessionCache.get(str);
                if (str2 == null) {
                    int iIndexOf = str.indexOf(58);
                    if (iIndexOf > 0) {
                        str2 = str.substring(0, iIndexOf) + "|" + str.substring(iIndexOf + 1);
                    }
                }
                if (sb.length() > 0) {
                    sb.append('\n');
                }
                sb.append(str2);
            }
        }
        return sb.toString();
    }

    private static java.lang.String buildWhitelistFromSelection() {
        java.lang.String str;
        java.util.HashMap map = new java.util.HashMap();
        java.util.List<java.lang.String[]> list = sPickedEntries;
        if (list != null) {
            for (java.lang.String[] strArr : list) {
                if (strArr != null && strArr.length >= 5 && (str = strArr[1]) != null && !str.isEmpty()) {
                    java.lang.String str2 = strArr[1];
                    java.lang.String str3 = strArr[4];
                    if (str3 == null) {
                        str3 = "";
                    }
                    map.put(str2, str3);
                }
            }
        }
        java.util.LinkedHashSet<java.lang.String> linkedHashSet = new java.util.LinkedHashSet();
        for (java.lang.String str4 : sSelectedKeys) {
            if (str4 != null && str4.startsWith("1:")) {
                java.lang.String strSubstring = str4.substring(2);
                java.lang.String str5 = (java.lang.String) map.get(strSubstring);
                if (str5 != null && !str5.trim().isEmpty() && str5.matches("\\d+")) {
                    strSubstring = str5;
                }
                if (!strSubstring.isEmpty()) {
                    linkedHashSet.add(strSubstring);
                }
            }
        }
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        for (java.lang.String str6 : linkedHashSet) {
            if (sb.length() > 0) {
                sb.append(',');
            }
            sb.append(str6);
        }
        return sb.toString();
    }

    static void acceptNativeResult(java.util.List<java.lang.String[]> list) {
        try {
            sSelectedKeys.clear();
            sSessionCache.clear();
            java.util.ArrayList arrayList = new java.util.ArrayList();
            if (list != null) {
                for (java.lang.String[] strArr : list) {
                    if (strArr != null && strArr.length >= 4) {
                        java.lang.String str = "2".equals(strArr[0]) ? "2" : "1";
                        java.lang.String str2 = strArr[1];
                        if (str2 == null) {
                            str2 = "";
                        }
                        java.lang.String str3 = strArr[2];
                        if (str3 == null) {
                            str3 = "";
                        }
                        java.lang.String str4 = strArr[3];
                        if (str4 == null) {
                            str4 = "";
                        }
                        if (!str2.isEmpty()) {
                            java.lang.String str5 = str + ":" + str2;
                            sSelectedKeys.add(str5);
                            sSessionCache.put(str5, str + "|" + str2 + "|" + str3);
                            arrayList.add(new java.lang.String[]{str, str2, str3, "", str4});
                        }
                    }
                }
            }
            sPickedEntries = arrayList;
            com.tencent.qqlitepro.util.Log.i("原生选择器: 结果回填 " + arrayList.size() + " 个会话");
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("原生选择器: 结果回填失败: " + th);
        }
    }

    /* QQ动态黑名单选择器：选中的用户其动态不处理 */
    public static void pm_openBlacklistPicker() {
        try {
            android.content.Context contextAppContext = com.tencent.qqlitepro.cache.MsgCache.appContext();
            android.content.Context contextCurrentActivity = com.tencent.qqlitepro.plugin.PluginEngine.currentActivity();
            if (contextCurrentActivity == null) {
                contextCurrentActivity = contextAppContext;
            }
            if (contextCurrentActivity == null) {
                com.tencent.qqlitepro.util.Log.w("黑名单选择失败: 无可用 Context");
                return;
            }
            java.util.LinkedHashSet linkedHashSet = new java.util.LinkedHashSet();
            for (java.lang.String str : (contextAppContext != null ? com.tencent.qqlitepro.util.Config.qqDynamicBlacklist(contextAppContext) : "").split("[,,\r\n ]+")) {
                if (str.matches("\\d+")) {
                    linkedHashSet.add(str);
                }
            }
            showSessionPicker(contextCurrentActivity, true, linkedHashSet, new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda27(contextAppContext, contextCurrentActivity));
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("打开黑名单选择器失败: " + th);
        }
    }

    static /* synthetic */ void lambda$pm_openBlacklistPicker$13(android.content.Context context, android.content.Context context2) {
        try {
            java.lang.String strBuildBlacklistFromSelection = buildWhitelistFromSelection();
            if (context != null) {
                com.tencent.qqlitepro.util.Config.setQqDynamicBlacklist(context, strBuildBlacklistFromSelection);
            }
            java.lang.StringBuilder sb = new java.lang.StringBuilder("已选择 ");
            java.util.Set<java.lang.String> set = sSelectedKeys;
            sb.append(set.size());
            sb.append(" 个黑名单用户（不处理其动态）");
            appendQqLog(sb.toString());
            toast(context2, "已选择 " + set.size() + " 个黑名单用户");
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("黑名单写入失败: " + th);
        }
    }

    private SettingsInjector() {
    }

    public static void patch(java.lang.ClassLoader classLoader) {
        try {
            com.tencent.qqlitepro.hook.NativePickerBridge.init(classLoader);
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("原生会话选择器桥接初始化失败: " + th);
        }
        try {
            java.lang.Class<?> settingsCls = java.lang.Class.forName("moye.wearqq.SettingsActivity", false, classLoader);
            java.lang.Class<?> catCls = java.lang.Class.forName("momoi.mod.qqpro.hook.SettingsCategory", false, classLoader);
            java.lang.Class<?> fnCls = java.lang.Class.forName("kotlin.jvm.functions.Function1", false, classLoader);
            java.lang.Object unit = java.lang.Class.forName("kotlin.Unit", false, classLoader).getField("INSTANCE").get(null);

            boolean injected = false;
            java.lang.reflect.Method[] methods = settingsCls.getDeclaredMethods();
            for (int i = 0; i < methods.length; i++) {
                java.lang.reflect.Method method = methods[i];
                if (method.getName().equals("buildCategories")
                        && java.util.List.class.isAssignableFrom(method.getReturnType())) {
                    de.robv.android.xposed.XposedBridge.hookMethod(method,
                            new com.tencent.qqlitepro.hook.SettingsInjector.AnonymousClass3(classLoader, catCls, fnCls, unit));
                    injected = true;
                    break;
                }
            }

            hookHostBack(settingsCls);
            hookActivityResult(settingsCls, classLoader);
            for (java.lang.reflect.Method method2 : settingsCls.getDeclaredMethods()) {
                if (method2.getName().equals("onCreate") || method2.getName().equals("attachBaseContext")) {
                    de.robv.android.xposed.XposedBridge.hookMethod(method2,
                            new com.tencent.qqlitepro.hook.SettingsInjector.AnonymousClass4(method2.getName().equals("onCreate")));
                }
            }
            com.tencent.qqlitepro.util.Log.i(injected ? "已注入 QQMax 设置项：增强模块设置" : "未找到 SettingsActivity.buildCategories");
        } catch (java.lang.Throwable th2) {
            com.tencent.qqlitepro.util.Log.w("QQMax 设置注入失败: " + th2);
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.SettingsInjector$3, reason: invalid class name */
    static class AnonymousClass3 extends de.robv.android.xposed.XC_MethodHook {
        final /* synthetic */ java.lang.Class val$catCls;
        final /* synthetic */ java.lang.ClassLoader val$cl;
        final /* synthetic */ java.lang.Class val$fnCls;
        final /* synthetic */ java.lang.Object val$unit;

        AnonymousClass3(java.lang.ClassLoader classLoader, java.lang.Class cls, java.lang.Class cls2, java.lang.Object obj) {
            this.val$cl = classLoader;
            this.val$catCls = cls;
            this.val$fnCls = cls2;
            this.val$unit = obj;
        }

        protected void afterHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) throws java.lang.Throwable {
            java.lang.Object result = methodHookParam.getResult();
            java.util.ArrayList arrayList = new java.util.ArrayList();
            if (result instanceof java.util.List) {
                arrayList.addAll((java.util.List) result);
            }
            arrayList.add(com.tencent.qqlitepro.hook.SettingsInjector.makeCategory(this.val$cl, this.val$catCls, this.val$fnCls, this.val$unit, "增强模块设置", "防撤回 / 定时消息 / 端口 / 关于", 0));
            com.tencent.qqlitepro.hook.SettingsInjector.sModuleCategory = arrayList.get(arrayList.size() - 1);
            methodHookParam.setResult(arrayList);
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.SettingsInjector$4, reason: invalid class name */
    static class AnonymousClass4 extends de.robv.android.xposed.XC_MethodHook {
        final /* synthetic */ boolean val$isCreate;

        AnonymousClass4(boolean z) {
            this.val$isCreate = z;
        }

        protected void afterHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            try {
                java.lang.Object obj = methodHookParam.thisObject;
                if (obj instanceof android.app.Activity) {
                    android.app.Activity activity = (android.app.Activity) obj;
                    com.tencent.qqlitepro.hook.SettingsInjector.registerBackInvoke(activity);
                    if (this.val$isCreate) {
                        com.tencent.qqlitepro.hook.SettingsInjector.sSettingsActRef = new java.lang.ref.WeakReference(activity);
                        if (com.tencent.qqlitepro.hook.SettingsInjector.sPendingCategoryJump) {
                            com.tencent.qqlitepro.hook.SettingsInjector.sPendingCategoryJump = false;
                            new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(new com.tencent.qqlitepro.hook.SettingsInjector$4$$ExternalSyntheticLambda0(activity), 300L);
                        }
                    }
                }
            } catch (java.lang.Throwable unused) {
            }
        }
    }

    public static void patchSelectableText(java.lang.ClassLoader classLoader) {
        if (sSelectableTextHooked.compareAndSet(false, true)) {
            try {
                de.robv.android.xposed.XposedHelpers.findAndHookMethod("android.widget.TextView", classLoader, "setTextIsSelectable", new java.lang.Object[]{java.lang.Boolean.TYPE, new com.tencent.qqlitepro.hook.SettingsInjector.AnonymousClass5()});
                com.tencent.qqlitepro.util.Log.i("长按文本复制面板 hook 已注册");
                de.robv.android.xposed.XposedHelpers.findAndHookMethod("android.widget.TextView", classLoader, "setText", new java.lang.Object[]{java.lang.CharSequence.class, android.widget.TextView.BufferType.class, new com.tencent.qqlitepro.hook.SettingsInjector.AnonymousClass6()});
                com.tencent.qqlitepro.util.Log.i("正文链接识别 hook 已注册");
            } catch (java.lang.Throwable th) {
                com.tencent.qqlitepro.util.Log.w("可选文本修复 hook 注册失败: " + th);
            }
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.SettingsInjector$5, reason: invalid class name */
    static class AnonymousClass5 extends de.robv.android.xposed.XC_MethodHook {
        AnonymousClass5() {
        }

        protected void afterHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            try {
                if (java.lang.Boolean.TRUE.equals(methodHookParam.args[0]) && (methodHookParam.thisObject instanceof android.widget.TextView)) {
                    com.tencent.qqlitepro.hook.SettingsInjector.attachLongPressCopy((android.widget.TextView) methodHookParam.thisObject);
                }
            } catch (java.lang.Throwable unused) {
            }
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.SettingsInjector$6, reason: invalid class name */
    static class AnonymousClass6 extends de.robv.android.xposed.XC_MethodHook {
        AnonymousClass6() {
        }

        protected void afterHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            try {
                if (!com.tencent.qqlitepro.hook.SettingsInjector.sLinkifyBusy && (methodHookParam.thisObject instanceof android.widget.TextView)) {
                    android.widget.TextView textView = (android.widget.TextView) methodHookParam.thisObject;
                    if (com.tencent.qqlitepro.hook.SettingsInjector.sLongPressCopyBound.contains(textView)) {
                        com.tencent.qqlitepro.hook.SettingsInjector.linkifyWithBrowser(textView);
                    }
                }
            } catch (java.lang.Throwable unused) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void attachLongPressCopy(android.widget.TextView textView) {
        if (textView == null || !sLongPressCopyBound.add(textView)) {
            return;
        }
        try {
            linkifyWithBrowser(textView);
            try {
                java.lang.Object objInvoke = android.view.View.class.getMethod("hasOnLongClickListeners", new java.lang.Class[0]).invoke(textView, new java.lang.Object[0]);
                if ((objInvoke instanceof java.lang.Boolean) && ((java.lang.Boolean) objInvoke).booleanValue()) {
                    return;
                }
            } catch (java.lang.Throwable unused) {
            }
            textView.setLongClickable(true);
            textView.setOnLongClickListener(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda198());
        } catch (java.lang.Throwable unused2) {
        }
    }

    static /* synthetic */ boolean lambda$attachLongPressCopy$14(android.view.View view) {
        try {
            java.lang.CharSequence text = ((android.widget.TextView) view).getText();
            java.lang.String string = text == null ? "" : text.toString();
            if (string.trim().isEmpty()) {
                return false;
            }
            showTextCopyDialog(view.getContext(), string);
            return true;
        } catch (java.lang.Throwable unused) {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void linkifyWithBrowser(android.widget.TextView textView) {
        android.text.Spannable spannable;
        java.lang.Object browserClickSpan;
        if (textView == null || sLinkifyBusy) {
            return;
        }
        try {
            java.lang.CharSequence text = textView.getText();
            if (text != null && text.length() >= 4) {
                java.lang.String string = text.toString();
                java.util.regex.Matcher matcher = URL_PATTERN.matcher(string);
                if (matcher.find()) {
                    sLinkifyBusy = true;
                    try {
                        if (text instanceof android.text.Spannable) {
                            spannable = (android.text.Spannable) text;
                        } else {
                            android.text.SpannableString spannableString = new android.text.SpannableString(text);
                            textView.setText(spannableString, android.widget.TextView.BufferType.SPANNABLE);
                            spannable = spannableString;
                        }
                        for (com.tencent.qqlitepro.hook.SettingsInjector.BrowserClickSpan browserClickSpan2 : (com.tencent.qqlitepro.hook.SettingsInjector.BrowserClickSpan[]) spannable.getSpans(0, spannable.length(), com.tencent.qqlitepro.hook.SettingsInjector.BrowserClickSpan.class)) {
                            spannable.removeSpan(browserClickSpan2);
                        }
                        for (com.tencent.qqlitepro.hook.SettingsInjector.PluginClickSpan pluginClickSpan : (com.tencent.qqlitepro.hook.SettingsInjector.PluginClickSpan[]) spannable.getSpans(0, spannable.length(), com.tencent.qqlitepro.hook.SettingsInjector.PluginClickSpan.class)) {
                            spannable.removeSpan(pluginClickSpan);
                        }
                        for (com.tencent.qqlitepro.hook.SettingsInjector.LanzouClickSpan lanzouSpan : (com.tencent.qqlitepro.hook.SettingsInjector.LanzouClickSpan[]) spannable.getSpans(0, spannable.length(), com.tencent.qqlitepro.hook.SettingsInjector.LanzouClickSpan.class)) {
                            spannable.removeSpan(lanzouSpan);
                        }
                        boolean zContains = sPluginNoticeTexts.contains(textView);
                        matcher.reset();
                        while (matcher.find()) {
                            int iStart = matcher.start();
                            int iEnd = matcher.end();
                            while (iEnd > iStart && URL_TRAILING.indexOf(string.charAt(iEnd - 1)) >= 0) {
                                iEnd--;
                            }
                            if (iEnd - iStart >= 5) {
                                java.lang.String strSubstring = string.substring(iStart, iEnd);
                                if (com.tencent.qqlitepro.util.Lanzou.isLanzou(strSubstring)) {
                                    /*
                                     * 蓝奏云链接：全局生效，点击后进入自建的详情界面
                                     * （展示文件信息 + 下载按钮），而不是丢给浏览器。
                                     */
                                    browserClickSpan = new com.tencent.qqlitepro.hook.SettingsInjector.LanzouClickSpan(
                                            strSubstring, -3066993, string);
                                } else if (zContains) {
                                    browserClickSpan = new com.tencent.qqlitepro.hook.SettingsInjector.PluginClickSpan(strSubstring, -13669408);
                                } else {
                                    browserClickSpan = new com.tencent.qqlitepro.hook.SettingsInjector.BrowserClickSpan(strSubstring, -13669408);
                                }
                                spannable.setSpan(browserClickSpan, iStart, iEnd, 33);
                            }
                        }
                        textView.setMovementMethod(new com.tencent.qqlitepro.hook.SettingsInjector.LinkClickMovementMethod());
                    } finally {
                        sLinkifyBusy = false;
                    }
                }
            }
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("识别正文链接失败: " + th);
        }
    }

    static final class BrowserClickSpan extends android.text.style.ClickableSpan {
        private final int color;
        private final java.lang.String url;

        BrowserClickSpan(java.lang.String str, int i) {
            this.url = str;
            this.color = i;
        }

        @Override // android.text.style.ClickableSpan
        public void onClick(android.view.View view) {
            com.tencent.qqlitepro.hook.SettingsInjector.openBrowserChooser(view == null ? null : view.getContext(), this.url);
        }

        @Override // android.text.style.ClickableSpan, android.text.style.CharacterStyle
        public void updateDrawState(android.text.TextPaint textPaint) {
            super.updateDrawState(textPaint);
            textPaint.setColor(this.color);
            textPaint.setUnderlineText(true);
            textPaint.setFakeBoldText(false);
        }
    }

    /**
     * 蓝奏云链接的点击高亮。
     * 点击后进入模块自建的"蓝奏云文件"界面（不是打开浏览器）。
     */
    static final class LanzouClickSpan extends android.text.style.ClickableSpan {
        private final int color;
        private final java.lang.String url;
        private final java.lang.String fullText;

        LanzouClickSpan(java.lang.String url, int color, java.lang.String fullText) {
            this.url = url;
            this.color = color;
            this.fullText = fullText;
        }

        @Override
        public void onClick(android.view.View view) {
            try {
                java.lang.String pwd = com.tencent.qqlitepro.util.Lanzou.guessPassword(this.fullText);
                openLanzouDialog(view == null ? null : view.getContext(), this.url, pwd);
            } catch (java.lang.Throwable th) {
                com.tencent.qqlitepro.util.Log.w("打开蓝奏云界面失败: " + th);
            }
        }

        @Override
        public void updateDrawState(android.text.TextPaint textPaint) {
            super.updateDrawState(textPaint);
            textPaint.setColor(this.color);
            textPaint.setUnderlineText(true);
            textPaint.setFakeBoldText(false);
        }
    }

    /* ===== 蓝奏云文件详情界面（自建，带文件信息与下载按钮） ===== */
    private static volatile android.app.Dialog sLanzouDialog;

    public static void openLanzouDialog(final android.content.Context context, final java.lang.String shareUrl, final java.lang.String password) {
        if (context == null || shareUrl == null || shareUrl.isEmpty()) {
            return;
        }
        try {
            final android.app.Dialog dialog = new android.app.Dialog(context);
            dialog.requestWindowFeature(1);
            dialog.setCanceledOnTouchOutside(true);

            final android.widget.LinearLayout root = new android.widget.LinearLayout(context);
            root.setOrientation(1);
            root.setBackgroundColor(-15657960);
            int pad = dp(context, 18.0f);
            root.setPadding(pad, dp(context, 14.0f), pad, dp(context, 16.0f));

            // 标题栏
            android.widget.LinearLayout header = new android.widget.LinearLayout(context);
            header.setOrientation(0);
            header.setGravity(16);
            header.addView(text(context, "蓝奏云文件", TITLE, 17.0f, 3),
                    new android.widget.LinearLayout.LayoutParams(0, -2, 1.0f));
            android.widget.TextView close = text(context, "关闭", ACCENT, 13.0f, 17);
            close.setPadding(dp(context, 10.0f), dp(context, 4.0f), 0, dp(context, 4.0f));
            close.setClickable(true);
            close.setOnClickListener(new android.view.View.OnClickListener() {
                @Override
                public void onClick(android.view.View v) {
                    try {
                        dialog.dismiss();
                    } catch (java.lang.Throwable ignored) {
                    }
                }
            });
            header.addView(close);
            root.addView(header, rootLp());

            // 内容卡片（先显示"解析中"）
            final android.widget.LinearLayout card = new android.widget.LinearLayout(context);
            card.setOrientation(1);
            android.graphics.drawable.GradientDrawable bg = new android.graphics.drawable.GradientDrawable();
            bg.setColor(-14013129);
            bg.setCornerRadius(dpLite(12.0f));
            card.setBackground(bg);
            card.setPadding(dp(context, 14.0f), dp(context, 12.0f), dp(context, 14.0f), dp(context, 12.0f));
            android.widget.LinearLayout.LayoutParams cardLp = rootLp();
            cardLp.topMargin = dp(context, 12.0f);
            card.setLayoutParams(cardLp);

            final android.widget.TextView nameView = text(context, "正在解析…", TITLE, 15.0f, 3);
            nameView.setSingleLine(false);
            card.addView(nameView, rootLp());

            final android.widget.TextView metaView = text(context, "", HINT, 12.5f, 3);
            metaView.setLineSpacing(dp(context, 4.0f), 1.0f);
            card.addView(metaView, withTop(context, 8.0f));

            final android.widget.TextView statusView = text(context, "获取文件信息中，请稍候…", SUB, 12.5f, 3);
            card.addView(statusView, withTop(context, 8.0f));

            root.addView(card, rootLp());

            // 下载按钮
            final android.widget.TextView downloadBtn = text(context, "下载", -1, 15.0f, 17);
            android.graphics.drawable.GradientDrawable btnBg = new android.graphics.drawable.GradientDrawable();
            btnBg.setColor(-11751600);
            btnBg.setCornerRadius(dpLite(22.0f));
            downloadBtn.setBackground(btnBg);
            downloadBtn.setPadding(0, dp(context, 12.0f), 0, dp(context, 12.0f));
            downloadBtn.setEnabled(true);
            android.widget.LinearLayout.LayoutParams btnLp = rootLp();
            btnLp.topMargin = dp(context, 14.0f);
            root.addView(downloadBtn, btnLp);

            android.widget.ScrollView scroll = new android.widget.ScrollView(context);
            scroll.setFillViewport(false);
            scroll.addView(root);
            dialog.setContentView(scroll);
            if (dialog.getWindow() != null) {
                android.view.WindowManager.LayoutParams attrs = dialog.getWindow().getAttributes();
                attrs.width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.94f);
                dialog.getWindow().setAttributes(attrs);
            }
            dialog.show();
            sLanzouDialog = dialog;

            // 后台解析
            final java.lang.String pwd = password == null ? "" : password;
            new java.lang.Thread(new java.lang.Runnable() {
                @Override
                public void run() {
                    final com.tencent.qqlitepro.util.Lanzou.Info info =
                            com.tencent.qqlitepro.util.Lanzou.resolve(shareUrl, pwd);
                    new android.os.Handler(android.os.Looper.getMainLooper()).post(new java.lang.Runnable() {
                        @Override
                        public void run() {
                            applyLanzouInfo(context, dialog, nameView, metaView, statusView, downloadBtn, info);
                        }
                    });
                }
            }, "lanzou-resolve").start();
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("打开蓝奏云界面失败: " + th);
        }
    }

    private static void applyLanzouInfo(final android.content.Context context, final android.app.Dialog dialog,
                                        android.widget.TextView nameView, android.widget.TextView metaView,
                                        android.widget.TextView statusView, android.widget.TextView downloadBtn,
                                        final com.tencent.qqlitepro.util.Lanzou.Info info) {
        try {
            if (info == null) {
                statusView.setText("解析失败，请稍后重试");
                downloadBtn.setEnabled(false);
                return;
            }
            java.lang.String name = info.name == null || info.name.isEmpty() ? "（未知文件名）" : info.name;
            nameView.setText(name);

            java.lang.StringBuilder meta = new java.lang.StringBuilder();
            if (info.size != null && !info.size.isEmpty()) {
                meta.append("• 大小：").append(info.size).append('\n');
            }
            if (info.time != null && !info.time.isEmpty()) {
                meta.append("• 时间：").append(info.time).append('\n');
            }
            meta.append("• 来源：").append(hostOf(info.shareUrl));
            if (info.password != null && !info.password.isEmpty()) {
                meta.append('\n').append("• 提取码：").append(info.password);
            }
            metaView.setText(meta.toString());

            if (info.ok()) {
                statusView.setText("已解析出下载直链");
                downloadBtn.setEnabled(true);
                downloadBtn.setText("下载");
                downloadBtn.setOnClickListener(new android.view.View.OnClickListener() {
                    @Override
                    public void onClick(android.view.View v) {
                        try {
                            downloadLanzou(context, info);
                        } catch (java.lang.Throwable th) {
                            com.tencent.qqlitepro.util.Log.w("蓝奏云下载失败: " + th);
                        }
                    }
                });
            } else {
                statusView.setText(info.error == null || info.error.isEmpty() ? "解析失败" : info.error);
                downloadBtn.setEnabled(true);
                downloadBtn.setText("用浏览器打开");
                downloadBtn.setOnClickListener(new android.view.View.OnClickListener() {
                    @Override
                    public void onClick(android.view.View v) {
                        try {
                            android.content.Intent it = new android.content.Intent(android.content.Intent.ACTION_VIEW,
                                    android.net.Uri.parse(info.shareUrl));
                            it.addFlags(268435456);
                            context.startActivity(it);
                        } catch (java.lang.Throwable th) {
                            com.tencent.qqlitepro.util.Log.w("打开浏览器失败: " + th);
                        }
                    }
                });
            }
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("填充蓝奏云信息失败: " + th);
        }
    }

    private static java.lang.String hostOf(java.lang.String url) {
        try {
            return new java.net.URL(url).getHost();
        } catch (java.lang.Throwable ignored) {
            return url;
        }
    }

    /** 下载蓝奏云文件到模块的下载目录 */
    private static void downloadLanzou(final android.content.Context context, final com.tencent.qqlitepro.util.Lanzou.Info info) {
        final android.widget.Toast[] holder = new android.widget.Toast[1];
        toast(context, "开始下载…");
        new java.lang.Thread(new java.lang.Runnable() {
            @Override
            public void run() {
                java.lang.String msg;
                try {
                    java.io.File dir = com.tencent.qqlitepro.util.Storage.dir(context, "downloads");
                    java.lang.String fileName = info.name == null || info.name.isEmpty()
                            ? ("lanzou_" + java.lang.System.currentTimeMillis())
                            : info.name.replaceAll("[\\\\/:*?\"<>|]", "_");
                    java.io.File out = new java.io.File(dir, fileName);
                    java.net.HttpURLConnection c = (java.net.HttpURLConnection) new java.net.URL(info.directUrl).openConnection();
                    c.setConnectTimeout(15000);
                    c.setReadTimeout(30000);
                    c.setInstanceFollowRedirects(true);
                    c.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 13)");
                    c.setRequestProperty("Referer", info.shareUrl);
                    java.io.InputStream in = c.getInputStream();
                    java.io.FileOutputStream fos = new java.io.FileOutputStream(out);
                    byte[] buf = new byte[8192];
                    int n;
                    long total = 0;
                    while ((n = in.read(buf)) > 0) {
                        fos.write(buf, 0, n);
                        total += n;
                    }
                    fos.flush();
                    fos.close();
                    in.close();
                    try {
                        c.disconnect();
                    } catch (java.lang.Throwable ignored) {
                    }
                    msg = total > 0 ? ("已保存：" + out.getAbsolutePath()) : "下载内容为空";
                } catch (java.lang.Throwable th) {
                    msg = "下载失败：" + th.getMessage();
                }
                final java.lang.String finalMsg = msg;
                new android.os.Handler(android.os.Looper.getMainLooper()).post(new java.lang.Runnable() {
                    @Override
                    public void run() {
                        toast(context, finalMsg);
                    }
                });
            }
        }, "lanzou-download").start();
    }

    static final class PluginClickSpan extends android.text.style.ClickableSpan {
        private final int color;
        private final java.lang.String url;

        PluginClickSpan(java.lang.String str, int i) {
            this.url = str;
            this.color = i;
        }

        @Override // android.text.style.ClickableSpan
        public void onClick(android.view.View view) {
            com.tencent.qqlitepro.hook.SettingsInjector.handlePluginNoticeLink(view == null ? null : view.getContext(), this.url);
        }

        @Override // android.text.style.ClickableSpan, android.text.style.CharacterStyle
        public void updateDrawState(android.text.TextPaint textPaint) {
            super.updateDrawState(textPaint);
            textPaint.setColor(this.color);
            textPaint.setUnderlineText(true);
            textPaint.setFakeBoldText(false);
        }
    }

    private static final class LinkClickMovementMethod extends android.text.method.LinkMovementMethod {
        private long downAt;
        private float downX;
        private float downY;



        private LinkClickMovementMethod() {
        }

        @Override // android.text.method.LinkMovementMethod, android.text.method.ScrollingMovementMethod, android.text.method.BaseMovementMethod, android.text.method.MovementMethod
        public boolean onTouchEvent(android.widget.TextView textView, android.text.Spannable spannable, android.view.MotionEvent motionEvent) {
            try {
                int actionMasked = motionEvent.getActionMasked();
                if (actionMasked == 0) {
                    this.downX = motionEvent.getX();
                    this.downY = motionEvent.getY();
                    this.downAt = android.os.SystemClock.uptimeMillis();
                    return false;
                }
                if (actionMasked != 1 || android.os.SystemClock.uptimeMillis() - this.downAt > android.view.ViewConfiguration.getLongPressTimeout()) {
                    return false;
                }
                float scaledTouchSlop = android.view.ViewConfiguration.get(textView.getContext()).getScaledTouchSlop();
                if (java.lang.Math.abs(motionEvent.getX() - this.downX) <= scaledTouchSlop && java.lang.Math.abs(motionEvent.getY() - this.downY) <= scaledTouchSlop) {
                    int x = (((int) motionEvent.getX()) - textView.getTotalPaddingLeft()) + textView.getScrollX();
                    int y = (((int) motionEvent.getY()) - textView.getTotalPaddingTop()) + textView.getScrollY();
                    android.text.Layout layout = textView.getLayout();
                    if (layout != null && y >= 0 && y <= layout.getHeight()) {
                        float f = x;
                        android.text.style.ClickableSpan[] clickableSpanArr = (android.text.style.ClickableSpan[]) spannable.getSpans(layout.getOffsetForHorizontal(layout.getLineForVertical(y), f), layout.getOffsetForHorizontal(layout.getLineForVertical(y), f), android.text.style.ClickableSpan.class);
                        if (clickableSpanArr.length > 0) {
                            clickableSpanArr[0].onClick(textView);
                            return true;
                        }
                    }
                }
                return false;
            } catch (java.lang.Throwable unused) {
            }
        return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void openBrowserChooser(android.content.Context context, java.lang.String str) {
        java.lang.String strTrim;
        if (context == null) {
            return;
        }
        if (str == null) {
            strTrim = "";
        } else {
            try {
                strTrim = str.trim();
            } catch (java.lang.Throwable th) {
                com.tencent.qqlitepro.util.Log.w("打开链接失败: " + th);
                toast(context, "无法打开链接：" + th);
                return;
            }
        }
        if (strTrim.isEmpty()) {
            return;
        }
        if (!strTrim.matches("(?i)^[a-z][a-z0-9+.\\-]*:.*")) {
            strTrim = "http://" + strTrim;
        }
        android.content.Intent intent = new android.content.Intent("android.intent.action.VIEW", android.net.Uri.parse(strTrim));
        intent.addFlags(268435456);
        android.content.Intent intentCreateChooser = android.content.Intent.createChooser(intent, "选择浏览器打开");
        intentCreateChooser.addFlags(268435456);
        context.startActivity(intentCreateChooser);
    }

    /* JADX INFO: Access modifiers changed from: private */
    static final class SelectableTextView extends android.widget.TextView {
        private int anchor;
        private final int barW;
        private float downX;
        private float downY;
        private int dragMode;
        private final java.lang.String fullText;
        private final android.os.Handler handler;
        private final int knobR;
        private com.tencent.qqlitepro.hook.SettingsInjector.SelectableTextView.Listener listener;
        private final java.lang.Runnable longPress;
        private final android.graphics.Paint paint;
        private int selEnd;
        private int selStart;
        private boolean touching;

        interface Listener {
            void onSelectionChanged(int i, int i2);
        }

        int selEnd() {
            return this.selEnd;
        }

        int selStart() {
            return this.selStart;
        }

        void setSelectionListener(com.tencent.qqlitepro.hook.SettingsInjector.SelectableTextView.Listener listener) {
            this.listener = listener;
        }

        /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.SettingsInjector$SelectableTextView$1, reason: invalid class name */
        class AnonymousClass1 implements java.lang.Runnable {
            AnonymousClass1() {
            }

            @Override // java.lang.Runnable
            public void run() {
                if (com.tencent.qqlitepro.hook.SettingsInjector.SelectableTextView.this.touching) {
                    com.tencent.qqlitepro.hook.SettingsInjector.SelectableTextView selectableTextView = com.tencent.qqlitepro.hook.SettingsInjector.SelectableTextView.this;
                    selectableTextView.beginSelectionAt(selectableTextView.downX, com.tencent.qqlitepro.hook.SettingsInjector.SelectableTextView.this.downY);
                }
            }
        }

        SelectableTextView(android.content.Context context, java.lang.String str) {
            super(context);
            android.graphics.Paint paint = new android.graphics.Paint(1);
            this.paint = paint;
            this.handler = new android.os.Handler(android.os.Looper.getMainLooper());
            this.longPress = new com.tencent.qqlitepro.hook.SettingsInjector.SelectableTextView.AnonymousClass1();
            this.selStart = -1;
            this.selEnd = -1;
            this.anchor = -1;
            str = str == null ? "" : str;
            this.fullText = str;
            this.barW = java.lang.Math.max(3, com.tencent.qqlitepro.hook.SettingsInjector.dp(context, 2.5f));
            this.knobR = java.lang.Math.max(7, com.tencent.qqlitepro.hook.SettingsInjector.dp(context, 5.0f));
            paint.setColor(-12616705);
            setText(str);
            setClickable(true);
            setLongClickable(true);
            setFocusable(false);
            setFocusableInTouchMode(false);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void selectAll() {
            setRange(0, this.fullText.length());
        }

        private void setRange(int i, int i2) {
            int length = this.fullText.length();
            int iMax = java.lang.Math.max(0, java.lang.Math.min(i, length));
            int iMax2 = java.lang.Math.max(0, java.lang.Math.min(i2, length));
            if (iMax2 >= iMax) {
                iMax2 = iMax;
                iMax = iMax2;
            }
            while (iMax > iMax2) {
                int i3 = iMax - 1;
                if (this.fullText.charAt(i3) != '\n' && this.fullText.charAt(i3) != '\r') {
                    break;
                } else {
                    iMax--;
                }
            }
            this.selStart = iMax2;
            this.selEnd = iMax;
            redraw();
            com.tencent.qqlitepro.hook.SettingsInjector.SelectableTextView.Listener listener = this.listener;
            if (listener != null) {
                listener.onSelectionChanged(this.selStart, this.selEnd);
            }
        }

        private void redraw() {
            int i;
            android.text.SpannableString spannableString = new android.text.SpannableString(this.fullText);
            int i2 = this.selStart;
            if (i2 >= 0 && (i = this.selEnd) > i2 && i <= spannableString.length()) {
                spannableString.setSpan(new android.text.style.BackgroundColorSpan(1430223871), this.selStart, this.selEnd, 33);
            }
            setText(spannableString, android.widget.TextView.BufferType.SPANNABLE);
            invalidate();
        }

        private int offsetAt(float f, float f2) {
            android.text.Layout layout = getLayout();
            if (layout == null) {
                return -1;
            }
            float totalPaddingLeft = (f - getTotalPaddingLeft()) + getScrollX();
            int lineCount = 0;
            int lineForVertical = layout.getLineForVertical(java.lang.Math.max(0, (int) ((f2 - getTotalPaddingTop()) + getScrollY())));
            if (lineForVertical >= 0) {
                lineCount = lineForVertical >= layout.getLineCount() ? layout.getLineCount() - 1 : lineForVertical;
            }
            float lineLeft = layout.getLineLeft(lineCount);
            float lineRight = layout.getLineRight(lineCount);
            if (totalPaddingLeft < lineLeft) {
                totalPaddingLeft = lineLeft;
            } else if (totalPaddingLeft > lineRight) {
                totalPaddingLeft = lineRight;
            }
            int offsetForHorizontal = layout.getOffsetForHorizontal(lineCount, totalPaddingLeft);
            if (offsetForHorizontal < 0) {
                return -1;
            }
            return java.lang.Math.min(offsetForHorizontal, this.fullText.length());
        }

        private int[] lineRangeAt(float f) {
            android.text.Layout layout = getLayout();
            if (layout == null) {
                return new int[]{-1, -1};
            }
            int lineForVertical = layout.getLineForVertical(java.lang.Math.max(0, (int) ((f - getTotalPaddingTop()) + getScrollY())));
            return (lineForVertical < 0 || lineForVertical >= layout.getLineCount()) ? new int[]{-1, -1} : new int[]{layout.getLineStart(lineForVertical), java.lang.Math.min(layout.getLineEnd(lineForVertical), this.fullText.length())};
        }

        private float[] cursorPos(int i) {
            android.text.Layout layout = getLayout();
            if (layout == null || i < 0) {
                return null;
            }
            int iMax = java.lang.Math.max(0, java.lang.Math.min(i, this.fullText.length()));
            int lineForOffset = layout.getLineForOffset(iMax);
            return new float[]{(getTotalPaddingLeft() + layout.getPrimaryHorizontal(iMax)) - getScrollX(), (getTotalPaddingTop() + layout.getLineTop(lineForOffset)) - getScrollY(), (getTotalPaddingTop() + layout.getLineBottom(lineForOffset)) - getScrollY()};
        }

        @Override // android.widget.TextView, android.view.View
        protected void onDraw(android.graphics.Canvas canvas) {
            super.onDraw(canvas);
            int i = this.selStart;
            if (i < 0 || this.selEnd < 0) {
                return;
            }
            drawCursor(canvas, cursorPos(i));
            drawCursor(canvas, cursorPos(this.selEnd));
        }

        private void drawCursor(android.graphics.Canvas canvas, float[] fArr) {
            if (fArr == null) {
                return;
            }
            float f = fArr[0];
            float f2 = fArr[2] - this.knobR;
            int i = this.barW;
            canvas.drawRect(f - (i / 2.0f), fArr[1], f + (i / 2.0f), f2, this.paint);
            canvas.drawCircle(f, f2, this.knobR, this.paint);
        }

        @Override // android.widget.TextView, android.view.View
        public boolean onTouchEvent(android.view.MotionEvent motionEvent) {
            int actionMasked = motionEvent.getActionMasked();
            if (actionMasked == 0) {
                this.downX = motionEvent.getX();
                float y = motionEvent.getY();
                this.downY = y;
                this.touching = true;
                int iHitCursor = hitCursor(this.downX, y);
                if (iHitCursor != 0) {
                    this.dragMode = iHitCursor;
                    com.tencent.qqlitepro.hook.SettingsInjector.allowParentIntercept(this, false);
                    return true;
                }
                this.dragMode = 0;
                this.handler.postDelayed(this.longPress, 400L);
                return true;
            }
            if (actionMasked != 2) {
                if (actionMasked == 1 || actionMasked == 3) {
                    this.handler.removeCallbacks(this.longPress);
                    this.touching = false;
                    if (this.dragMode != 0) {
                        this.dragMode = 0;
                        com.tencent.qqlitepro.hook.SettingsInjector.allowParentIntercept(this, true);
                    }
                    return true;
                }
                return super.onTouchEvent(motionEvent);
            }
            if (this.dragMode == 0) {
                float x = motionEvent.getX() - this.downX;
                float y2 = motionEvent.getY() - this.downY;
                float f = (x * x) + (y2 * y2);
                int i = this.knobR;
                if (f > i * i * 4) {
                    this.handler.removeCallbacks(this.longPress);
                }
                return true;
            }
            int iOffsetAt = offsetAt(motionEvent.getX(), motionEvent.getY());
            if (iOffsetAt >= 0) {
                int i2 = this.dragMode;
                if (i2 == 2) {
                    setRange(iOffsetAt, this.selEnd);
                } else if (i2 == 3) {
                    setRange(this.selStart, iOffsetAt);
                } else {
                    int i3 = this.anchor;
                    if (i3 >= 0) {
                        setRange(java.lang.Math.min(i3, iOffsetAt), java.lang.Math.max(this.anchor, iOffsetAt));
                    }
                }
            }
            return true;
        }

        private int hitCursor(float f, float f2) {
            int i = this.selStart;
            if (i >= 0 && this.selEnd >= 0) {
                float f3 = this.knobR * 2.2f;
                float[] fArrCursorPos = cursorPos(i);
                if (fArrCursorPos != null && java.lang.Math.abs(f - fArrCursorPos[0]) <= f3 && java.lang.Math.abs(f2 - (fArrCursorPos[2] - this.knobR)) <= f3) {
                    return 2;
                }
                float[] fArrCursorPos2 = cursorPos(this.selEnd);
                if (fArrCursorPos2 != null && java.lang.Math.abs(f - fArrCursorPos2[0]) <= f3 && java.lang.Math.abs(f2 - (fArrCursorPos2[2] - this.knobR)) <= f3) {
                    return 3;
                }
            }
            return 0;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void beginSelectionAt(float f, float f2) {
            int i;
            int iOffsetAt = offsetAt(f, f2);
            if (iOffsetAt < 0) {
                return;
            }
            this.anchor = iOffsetAt;
            int[] iArrLineRangeAt = lineRangeAt(f2);
            this.dragMode = 1;
            com.tencent.qqlitepro.hook.SettingsInjector.allowParentIntercept(this, false);
            int i2 = iArrLineRangeAt[0];
            if (i2 < 0 || (i = iArrLineRangeAt[1]) <= i2) {
                setRange(iOffsetAt, iOffsetAt + 1);
            } else {
                setRange(i2, i);
            }
        }
    }

    private static void showTextCopyDialog(android.content.Context context, java.lang.String str) {
        if (context == null) {
            return;
        }
        try {
            android.app.Dialog dialog = new android.app.Dialog(context);
            dialog.requestWindowFeature(1);
            android.widget.ScrollView scrollView = new android.widget.ScrollView(context);
            scrollView.setFillViewport(false);
            android.widget.LinearLayout linearLayout = new android.widget.LinearLayout(context);
            linearLayout.setOrientation(1);
            linearLayout.setBackgroundColor(-15657960);
            linearLayout.setPadding(dp(context, 14.0f), dp(context, 12.0f), dp(context, 14.0f), dp(context, 12.0f));
            scrollView.addView(linearLayout, new android.widget.FrameLayout.LayoutParams(-1, -2));
            dialog.setContentView(scrollView);
            android.view.Window window = dialog.getWindow();
            if (window != null) {
                android.graphics.drawable.GradientDrawable gradientDrawable = new android.graphics.drawable.GradientDrawable();
                gradientDrawable.setColor(-15657960);
                window.setBackgroundDrawable(gradientDrawable);
                window.setLayout(-1, -1);
                window.setSoftInputMode(3);
            }
            linearLayout.addView(section(context, "长按复制"));
            java.lang.String str2 = str == null ? "" : str;
            com.tencent.qqlitepro.hook.SettingsInjector.SelectableTextView selectableTextView = new com.tencent.qqlitepro.hook.SettingsInjector.SelectableTextView(context, str2);
            selectableTextView.setTextColor(-1644309);
            selectableTextView.setTextSize(13.0f);
            selectableTextView.setLineSpacing(dp(context, 4.0f), 1.0f);
            selectableTextView.setPadding(0, 0, 0, 0);
            selectableTextView.setBackground(null);
            android.widget.TextView textViewText = text(context, "长按正文选中文字，拖动两端光标可调整选区", HINT, 11.0f, 8388611);
            selectableTextView.setSelectionListener(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda54(textViewText));
            linearLayout.addView(selectableTextView, rootLp());
            linearLayout.addView(gap(context, 6.0f));
            linearLayout.addView(textViewText, rootLp());
            linearLayout.addView(gap(context, 10.0f));
            linearLayout.addView(saveButton(context, "复制选中", new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda55(selectableTextView, str2, context, dialog)));
            linearLayout.addView(gap(context, 8.0f));
            linearLayout.addView(saveButton(context, "全选", new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda56(selectableTextView)));
            linearLayout.addView(gap(context, 8.0f));
            linearLayout.addView(saveButton(context, "复制全文", new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda58(context, str, dialog)));
            linearLayout.addView(gap(context, 8.0f));
            linearLayout.addView(saveButton(context, "关闭", new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda205(dialog)));
            dialog.show();
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("长按复制面板弹出失败: " + th);
        }
    }

    static /* synthetic */ void lambda$showTextCopyDialog$15(android.widget.TextView textView, int i, int i2) {
        java.lang.String str;
        if (i2 > i) {
            str = "已选中 " + (i2 - i) + " 字，点「复制选中」复制";
        } else {
            str = "长按正文选中文字，拖动两端光标可调整选区";
        }
        textView.setText(str);
    }

    static /* synthetic */ void lambda$showTextCopyDialog$16(com.tencent.qqlitepro.hook.SettingsInjector.SelectableTextView selectableTextView, java.lang.String str, android.content.Context context, android.app.Dialog dialog) {
        int iSelStart = selectableTextView.selStart();
        int iSelEnd = selectableTextView.selEnd();
        if (iSelStart < 0 || iSelEnd <= iSelStart || iSelEnd > str.length()) {
            toast(context, "请先长按正文选中要复制的文字");
        } else {
            copyToClipboard(context, str.substring(iSelStart, iSelEnd));
            dialog.dismiss();
        }
    }

    static /* synthetic */ void lambda$showTextCopyDialog$18(android.content.Context context, java.lang.String str, android.app.Dialog dialog) {
        copyToClipboard(context, str);
        dialog.dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void allowParentIntercept(android.view.View view, boolean z) {
        try {
            android.view.ViewParent parent = view.getParent();
            if (parent != null) {
                parent.requestDisallowInterceptTouchEvent(!z);
            }
        } catch (java.lang.Throwable unused) {
        }
    }

    private static void copyToClipboard(android.content.Context context, java.lang.String str) {
        try {
            android.content.ClipboardManager clipboardManager = (android.content.ClipboardManager) context.getSystemService("clipboard");
            if (clipboardManager == null) {
                toast(context, "复制失败：剪贴板不可用");
                return;
            }
            if (str == null) {
                str = "";
            }
            clipboardManager.setPrimaryClip(android.content.ClipData.newPlainText("text", str));
            toast(context, "已复制到剪贴板");
        } catch (java.lang.Throwable th) {
            toast(context, "复制失败：" + th);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.lang.Object makeCategory(java.lang.ClassLoader classLoader, java.lang.Class<?> cls, java.lang.Class<?> cls2, java.lang.Object obj, java.lang.String str, java.lang.String str2, int i) throws java.lang.Exception {
        java.lang.Object objNewProxyInstance = java.lang.reflect.Proxy.newProxyInstance(classLoader, new java.lang.Class[]{cls2}, new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda180(i, obj));
        java.lang.reflect.Constructor<?> declaredConstructor = cls.getDeclaredConstructor(java.lang.String.class, java.lang.String.class, cls2);
        declaredConstructor.setAccessible(true);
        return declaredConstructor.newInstance(str, str2, objNewProxyInstance);
    }

    static /* synthetic */ java.lang.Object lambda$makeCategory$19(int i, java.lang.Object obj, java.lang.Object obj2, java.lang.reflect.Method method, java.lang.Object[] objArr) throws java.lang.Throwable {
        if (method.getName().equals("invoke")) {
            java.lang.Object obj3 = (objArr == null || objArr.length <= 0) ? null : objArr[0];
            if (obj3 != null) {
                if (!com.tencent.qqlitepro.MainHook.isGroupPassed()) {
                    toastNotInGroup(obj3);
                } else {
                    buildMenu(obj3, i, obj);
                }
            }
        } else if (method.getName().equals("toString")) {
            return "EnhancedModuleSettings";
        }
        if (method.getReturnType() == java.lang.Boolean.TYPE) {
            return java.lang.Boolean.FALSE;
        }
        return method.getReturnType() == java.lang.Integer.TYPE ? 0 : null;
    }

    private static void toastNotInGroup(java.lang.Object obj) {
        android.content.Context context = null;
        try {
            android.view.ViewGroup viewGroupScopeGroup = scopeGroup(obj);
            if (viewGroupScopeGroup != null) {
                context = viewGroupScopeGroup.getContext();
            }
        } catch (java.lang.Throwable unused) {
        }
        if (context == null) {
            return;
        }
        long jCurrentTimeMillis = java.lang.System.currentTimeMillis();
        if (jCurrentTimeMillis - sNotInGroupToastAt < 1500) {
            return;
        }
        sNotInGroupToastAt = jCurrentTimeMillis;
        toast(context, com.tencent.qqlitepro.MainHook.NOT_IN_GROUP_TIP);
    }

    private static android.view.ViewGroup scopeGroup(java.lang.Object obj) throws java.lang.Throwable {
        return (android.view.ViewGroup) obj.getClass().getMethod("getGroup", new java.lang.Class[0]).invoke(obj, new java.lang.Object[0]);
    }

    private static void buildMenu(java.lang.Object obj, int i, java.lang.Object obj2) {
        try {
            android.view.ViewGroup viewGroupScopeGroup = scopeGroup(obj);
            if (viewGroupScopeGroup == null) {
                return;
            }
            android.content.Context context = viewGroupScopeGroup.getContext();
            viewGroupScopeGroup.removeAllViews();
            renderMenu(context, viewGroupScopeGroup);
            int i2 = sPendingPage;
            if (i2 != 0) {
                sPendingPage = 0;
                openPage(viewGroupScopeGroup, i2);
            }
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("构建增强模块设置主菜单失败: " + th);
        }
    }

    private static void renderMenu(android.content.Context context, android.view.ViewGroup viewGroup) {
        viewGroup.removeAllViews();
        viewGroup.addView(section(context, "增强模块设置"));
        addDivider(viewGroup);
        viewGroup.addView(navRow(context, "基础设置", "防撤回 / 翻译接口 / 通用", 1));
        addDivider(viewGroup);
        viewGroup.addView(navRow(context, "定时消息", "按时自动发送消息", 2));
        addDivider(viewGroup);
        viewGroup.addView(navRow(context, "关键词回复", "按关键词触发回复", 5));
        addDivider(viewGroup);
        viewGroup.addView(navRow(context, "自动回复", "私聊收到消息即回复", 6));
        addDivider(viewGroup);
        viewGroup.addView(navRow(context, "端口设置", "本地 HTTP 服务器 目前端口 " + com.tencent.qqlitepro.util.Config.port(context), 3));
        addDivider(viewGroup);
        viewGroup.addView(navRow(context, "AI 总结设置", "自定义 url / apikey / 风格 / 测试", 7));
        addDivider(viewGroup);
        viewGroup.addView(navRow(context, "URL 转语音发送", "下载音频 / 选择会话发送", 8));
        addDivider(viewGroup);
        viewGroup.addView(navRow(context, "发送文件", "选择任意格式文件发送", 9));
        addDivider(viewGroup);
        viewGroup.addView(navRow(context, "下载管理", "保存路径 / 查看已下载文件", 10));
        addDivider(viewGroup);
        viewGroup.addView(navRow(context, "插件管理", "导入 / 删除 / 启停插件 / 拓展功能 / 运行日志", 13));
        addDivider(viewGroup);
        viewGroup.addView(navRow(context, "QQ 动态", "定时点赞 / 评论动态 / 黑名单 / 日志", 15));
        addDivider(viewGroup);
        viewGroup.addView(navRow(context, "关于模块", "版本 / 作者 / 官方群聊", 4));
        addDivider(viewGroup);
        viewGroup.addView(navRow(context, "官方公告", "查看最新公告 / 版本更新", 11));
    }

    private static android.view.View navRow(android.content.Context context, java.lang.String str, java.lang.String str2, int i) {
        android.widget.LinearLayout linearLayout = new android.widget.LinearLayout(context);
        linearLayout.setOrientation(0);
        linearLayout.setGravity(16);
        linearLayout.setPadding(dp(context, 20.0f), dp(context, 16.0f), dp(context, 20.0f), dp(context, 16.0f));
        linearLayout.setLayoutParams(rootLp());
        linearLayout.setOnClickListener(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda162(i));
        android.widget.LinearLayout linearLayout2 = new android.widget.LinearLayout(context);
        linearLayout2.setOrientation(1);
        linearLayout2.setLayoutParams(new android.widget.LinearLayout.LayoutParams(0, -2, 1.0f));
        android.widget.TextView textViewText = text(context, str, TITLE, 16.0f, 8388611);
        textViewText.setPadding(0, 0, dp(context, 8.0f), 0);
        android.widget.TextView textViewText2 = text(context, str2, -6643546, 12.0f, 8388611);
        linearLayout2.addView(textViewText, rootLp());
        linearLayout2.addView(textViewText2, rootLp());
        linearLayout.addView(linearLayout2);
        linearLayout.addView(text(context, "›", HINT, 22.0f, 17));
        return linearLayout;
    }

    static /* synthetic */ void lambda$navRow$20(int i, android.view.View view) {
        android.view.ViewGroup viewGroup = view.getParent() instanceof android.view.ViewGroup ? (android.view.ViewGroup) view.getParent() : null;
        if (viewGroup != null) {
            openPage(viewGroup, i);
        }
    }

    private static android.view.View backRow(android.content.Context context) {
        android.widget.LinearLayout linearLayout = new android.widget.LinearLayout(context);
        linearLayout.setOrientation(0);
        linearLayout.setGravity(16);
        linearLayout.setPadding(dp(context, 20.0f), dp(context, 14.0f), dp(context, 20.0f), dp(context, 14.0f));
        linearLayout.setLayoutParams(rootLp());
        linearLayout.setOnClickListener(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda126());
        android.app.Dialog dialog = sMailDialog;
        linearLayout.addView(text(context, (dialog == null || !dialog.isShowing()) ? "←  返回增强模块设置" : "←  返回邮箱管理", HINT, 14.0f, 8388611));
        return linearLayout;
    }

    static /* synthetic */ void lambda$backRow$21(android.view.View view) {
        if (dismissMailDialog()) {
            return;
        }
        android.view.ViewGroup viewGroup = view.getParent() instanceof android.view.ViewGroup ? (android.view.ViewGroup) view.getParent() : null;
        if (viewGroup != null) {
            openPage(viewGroup, 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void openPage(android.view.ViewGroup viewGroup, int i) {
        if (viewGroup == null) {
        }
        sPageGroup = viewGroup;
        if (sCurrentPage == 20 && i != 20) {
            clearMailSendState();
        }
        sCurrentPage = i;
        android.content.Context context = viewGroup.getContext();
        viewGroup.removeAllViews();
        switch (i) {
            case 0:
                renderMenu(context, viewGroup);
                break;
            case 1:
                buildFang(context, viewGroup);
                break;
            case 2:
                buildAutoTask(context, viewGroup);
                break;
            case 3:
                buildPort(context, viewGroup);
                break;
            case 4:
            case 14:
            default:
                buildAbout(context, viewGroup);
                break;
            case 5:
                buildKeywordReply(context, viewGroup);
                break;
            case 6:
                buildAutoReply(context, viewGroup);
                break;
            case 7:
                buildSummary(context, viewGroup);
                break;
            case 8:
                buildVoiceUrl(context, viewGroup);
                break;
            case 9:
                buildFileSend(context, viewGroup);
                break;
            case 10:
                buildDownloadManage(context, viewGroup);
                break;
            case 11:
                buildNotice(context, viewGroup);
                break;
            case 12:
                buildNoticeDetail(context, viewGroup);
                break;
            case 13:
                buildPlugin(context, viewGroup);
                break;
            case 15:
                buildQqDynamic(context, viewGroup);
                break;
            case 16:
                buildMailHome(context, viewGroup);
                break;
            case 17:
                buildMailSetting(context, viewGroup);
                break;
            case 18:
                buildMailInbox(context, viewGroup);
                break;
            case 19:
                buildMailDetail(context, viewGroup);
                break;
            case 20:
                buildMailSend(context, viewGroup);
                break;
            case 21:
                buildNoticeNodes(context, viewGroup);
                break;
            case 22:
                buildMailWebBg(context, viewGroup);
                break;
        }
    }

    private static void hookHostBack(java.lang.Class<?> cls) {
        try {
            int i = 0;
            for (java.lang.reflect.Method method : cls.getDeclaredMethods()) {
                java.lang.String name = method.getName();
                try {
                    if (name.equals("onBackPressed")) {
                        com.tencent.qqlitepro.util.Log.i("拦截宿主方法: " + method.toGenericString());
                        de.robv.android.xposed.XposedBridge.hookMethod(method, new com.tencent.qqlitepro.hook.SettingsInjector.AnonymousClass7());
                    } else {
                        if (name.equals("onKeyUp") || name.equals("onKeyDown")) {
                            de.robv.android.xposed.XposedBridge.hookMethod(method, new com.tencent.qqlitepro.hook.SettingsInjector.AnonymousClass8());
                        }
                    }
                    i++;
                } catch (java.lang.Throwable th) {
                    com.tencent.qqlitepro.util.Log.w("hook 返回方法失败 " + method.getName() + ": " + th);
                }
            }
            if (i > 0) {
                com.tencent.qqlitepro.util.Log.i("已拦截宿主返回/右滑，共 hook " + i + " 个方法 -> " + cls.getName());
            }
        } catch (java.lang.Throwable th2) {
            com.tencent.qqlitepro.util.Log.w("拦截宿主返回失败: " + th2);
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.SettingsInjector$7, reason: invalid class name */
    static class AnonymousClass7 extends de.robv.android.xposed.XC_MethodHook {
        AnonymousClass7() {
        }

        protected void beforeHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            try {
                if (com.tencent.qqlitepro.hook.SettingsInjector.interceptBack()) {
                    methodHookParam.setResult((java.lang.Object) null);
                }
            } catch (java.lang.Throwable unused) {
            }
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.SettingsInjector$8, reason: invalid class name */
    static class AnonymousClass8 extends de.robv.android.xposed.XC_MethodHook {
        AnonymousClass8() {
        }

        protected void beforeHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            try {
                java.lang.Object obj = (methodHookParam.args == null || methodHookParam.args.length <= 0) ? null : methodHookParam.args[0];
                if ((obj instanceof java.lang.Integer) && ((java.lang.Integer) obj).intValue() == 4 && com.tencent.qqlitepro.hook.SettingsInjector.interceptBack()) {
                    methodHookParam.setResult(java.lang.Boolean.TRUE);
                }
            } catch (java.lang.Throwable unused) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    private static boolean interceptBack() {
        if (sPageGroup != null && sCurrentPage != 0) {
            if (sCurrentPage == 21) {
                try {
                    openPage(sPageGroup, 11);
                    return true;
                } catch (java.lang.Throwable th) {
                    com.tencent.qqlitepro.util.Log.w("返回公告页失败: " + th);
                    return false;
                }
            }
            try {
                android.view.ViewGroup viewGroup = sPageGroup;
                android.content.Context context = viewGroup.getContext();
                sCurrentPage = 0;
                viewGroup.removeAllViews();
                renderMenu(context, viewGroup);
                return true;
            } catch (java.lang.Throwable th2) {
                com.tencent.qqlitepro.util.Log.w("返回主菜单失败: " + th2);
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void registerBackInvoke(android.app.Activity activity) {
        java.lang.Object objInvoke;
        try {
            if (android.os.Build.VERSION.SDK_INT >= 33 && (objInvoke = android.app.Activity.class.getMethod("getOnBackInvokedDispatcher", new java.lang.Class[0]).invoke(activity, new java.lang.Object[0])) != null) {
                java.lang.Class<?> cls = java.lang.Class.forName("android.window.OnBackInvokedCallback");
                objInvoke.getClass().getMethod("registerOnBackInvokedCallback", java.lang.Integer.TYPE, cls).invoke(objInvoke, 1, java.lang.reflect.Proxy.newProxyInstance(cls.getClassLoader(), new java.lang.Class[]{cls}, new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda179(activity)));
                com.tencent.qqlitepro.util.Log.i("已注册 OnBackInvokedCallback 拦截右滑返回");
            }
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("注册右滑拦截失败: " + th);
        }
    }

    static /* synthetic */ java.lang.Object lambda$registerBackInvoke$22(android.app.Activity activity, java.lang.Object obj, java.lang.reflect.Method method, java.lang.Object[] objArr) throws java.lang.Throwable {
        if (method.getName().equals("onBackInvoked")) {
            if (interceptBack()) {
                return null;
            }
            try {
                activity.onBackPressed();
                return null;
            } catch (java.lang.Throwable unused) {
                return null;
            }
        }
        return proxyDefault(method);
    }

    private static java.lang.Object proxyDefault(java.lang.reflect.Method method) {
        java.lang.Class<?> returnType = method.getReturnType();
        if (returnType == java.lang.Boolean.TYPE) {
            return java.lang.Boolean.FALSE;
        }
        if (returnType == java.lang.Integer.TYPE) {
            return 0;
        }
        if (returnType == java.lang.Long.TYPE) {
            return 0L;
        }
        if (returnType == java.lang.Float.TYPE) {
            return java.lang.Float.valueOf(0.0f);
        }
        if (returnType == java.lang.Double.TYPE) {
            return java.lang.Double.valueOf(0.0d);
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void restartHost(android.content.Context context) {
        try {
            android.content.Intent launchIntentForPackage = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
            if (launchIntentForPackage != null) {
                launchIntentForPackage.addFlags(268435456);
                context.startActivity(launchIntentForPackage);
            }
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("重启 QQMax 失败: " + th);
        }
        try {
            android.os.Process.killProcess(android.os.Process.myPid());
        } catch (java.lang.Throwable unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void clearCacheAndRestart(android.content.Context context) {
        try {
            com.tencent.qqlitepro.cache.MsgCache.get().clear();
            com.tencent.qqlitepro.cache.RawMsgStore.clear();
            com.tencent.qqlitepro.hook.UiPatcher.clearEditCache();
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("清除缓存失败: " + th);
        }
        try {
            deleteDir(com.tencent.qqlitepro.util.Storage.dir(context, ""));
        } catch (java.lang.Throwable th2) {
            com.tencent.qqlitepro.util.Log.w("清除 QQMax增强目录失败: " + th2);
        }
        try {
            java.io.File file = new java.io.File(context.getCacheDir(), "ai_summary_history.txt");
            if (file.exists()) {
                file.delete();
            }
        } catch (java.lang.Throwable unused) {
        }
        com.tencent.qqlitepro.util.Log.i("已清除防撤回缓存，重启 QQMax");
        toast(context, "缓存已清除，即将重启 QQMax");
        try {
            new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda14(context), 300L);
        } catch (java.lang.Throwable unused2) {
            restartHost(context);
        }
    }

    private static void deleteDir(java.io.File file) {
        if (file == null) {
            return;
        }
        java.io.File[] fileArrListFiles = file.listFiles();
        if (fileArrListFiles != null) {
            for (java.io.File file2 : fileArrListFiles) {
                if (file2.isDirectory()) {
                    deleteDir(file2);
                } else {
                    try {
                        file2.delete();
                    } catch (java.lang.Throwable unused) {
                    }
                }
            }
        }
        try {
            file.delete();
        } catch (java.lang.Throwable unused2) {
        }
    }

    private static void buildFang(android.content.Context context, android.view.ViewGroup viewGroup) {
        viewGroup.addView(backRow(context));
        viewGroup.addView(section(context, "防撤回"));
        viewGroup.addView(switchRow(context, "防撤回总开关", "关闭后不拦截消息撤回", com.tencent.qqlitepro.util.Config.enabled(context), new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda80(context)));
        addDivider(viewGroup);
        addDivider(viewGroup);
        viewGroup.addView(section(context, "防撤回模式"));
        int iMode = com.tencent.qqlitepro.util.Config.mode(context);
        viewGroup.addView(modeRow(context, "强制阻止撤回", "推荐 · 红边 + “已撤回”角标", iMode == 1, true));
        viewGroup.addView(modeRow(context, "注入还原", "原文注入“对方撤回”灰条", iMode == 2, false));
        android.widget.TextView textViewText = text(context, "在 QQMax 设置内切换，即时生效", HINT, 12.0f, 8388611);
        textViewText.setPadding(dp(context, 20.0f), dp(context, 10.0f), dp(context, 20.0f), dp(context, 6.0f));
        viewGroup.addView(textViewText, rootLp());
        addDivider(viewGroup);
        viewGroup.addView(section(context, "群公告"));
        viewGroup.addView(switchRow(context, "去除群公告加载数量限制", "开启后可查看本群全部公告，关闭仅显示最新一条", com.tencent.qqlitepro.util.Config.bulletinAllEnabled(context), new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda81(context)));
        addDivider(viewGroup);
        viewGroup.addView(section(context, "翻译接口"));
        viewGroup.addView(switchRow(context, "翻译接口替换", "开启后用自定义网关翻译，关闭沿用 QQMax 内置", com.tencent.qqlitepro.util.Config.translateEnabled(context), new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda82(context)));
        addDivider(viewGroup);
        viewGroup.addView(section(context, "模块与群聊"));
        // 官方群加入提示开关（加入群后可关闭启动时的 toast）
        viewGroup.addView(switchRow(context, "官方群加入提示", "关闭后启动时不再提示「已加入官方群聊」",
                com.tencent.qqlitepro.util.Config.groupToastEnabled(context),
                new com.tencent.qqlitepro.hook.SettingsInjector.ToggleHandler(
                        com.tencent.qqlitepro.hook.SettingsInjector.ToggleHandler.KEY_GROUP_TOAST, context)));
        // 门禁状态 + 刷新按钮：加入群后无需重启，点一下即可检测并注入
        addGateStatusRow(context, viewGroup);
        addDivider(viewGroup);
        viewGroup.addView(section(context, "自定义后缀"));
        android.widget.CompoundButton[] compoundButtonArr = new android.widget.CompoundButton[1];
        viewGroup.addView(switchRowState(context, "自定义后缀", "开启后自身发送的文本消息末尾自动追加自定义后缀", com.tencent.qqlitepro.util.Config.catgirlEnabled(context), compoundButtonArr));
        makeGroupGap(viewGroup);
        android.widget.LinearLayout linearLayout = new android.widget.LinearLayout(context);
        linearLayout.setOrientation(1);
        android.graphics.drawable.GradientDrawable gradientDrawable = new android.graphics.drawable.GradientDrawable();
        gradientDrawable.setShape(0);
        gradientDrawable.setColor(SURFACE_CONTAINER);
        gradientDrawable.setCornerRadius(dpLite(14.0f));
        linearLayout.setBackground(gradientDrawable);
        linearLayout.setPadding(dp(context, 12.0f), dp(context, 14.0f), dp(context, 12.0f), dp(context, 14.0f));
        linearLayout.setLayoutParams(rootLp());
        linearLayout.addView(text(context, "后缀内容（留空则默认“喵~”）", TITLE, 15.0f, 8388611), rootLp());
        java.lang.String strCatgirlSuffixRaw = com.tencent.qqlitepro.util.Config.catgirlSuffixRaw(context);
        if (strCatgirlSuffixRaw.isEmpty()) {
            strCatgirlSuffixRaw = "喵~";
        }
        android.widget.EditText editTextEdit = edit(context, strCatgirlSuffixRaw, "后缀内容，默认“喵~”", 1);
        editTextEdit.setSingleLine(true);
        linearLayout.addView(editTextEdit, inputLp());
        viewGroup.addView(linearLayout);
        makeGroupGap(viewGroup);
        android.view.View viewSaveButton = saveButton(context, "保存后缀", new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda83(editTextEdit, context));
        viewGroup.addView(viewSaveButton);
        android.widget.CompoundButton compoundButton = compoundButtonArr[0];
        boolean z = compoundButton != null && compoundButton.isChecked();
        linearLayout.setVisibility(z ? 0 : 8);
        viewSaveButton.setVisibility(z ? 0 : 8);
        android.widget.CompoundButton compoundButton2 = compoundButtonArr[0];
        if (compoundButton2 != null) {
            compoundButton2.setOnCheckedChangeListener(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda84(linearLayout, viewSaveButton, context));
        }
        addDivider(viewGroup);
        viewGroup.addView(section(context, "媒体选择器"));
        viewGroup.addView(switchRow(context, "切换媒体选择器", "开启后选择图片/视频时弹系统选择器自选处理应用，关闭沿用 QQMax 原生选择器", com.tencent.qqlitepro.util.Config.systemMediaPickerEnabled(context), new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda85(context)));
        addDivider(viewGroup);
        viewGroup.addView(section(context, "会话选择器"));
        boolean zNativePickerEnabled = com.tencent.qqlitepro.util.Config.nativePickerEnabled(context);
        viewGroup.addView(pickerModeRow(context, "QQ 原生选择器", "推荐 · 与转发聊天记录同款界面", zNativePickerEnabled, true));
        viewGroup.addView(pickerModeRow(context, "模块自建弹层", "兼容回退，列表异常时可切换", !zNativePickerEnabled, false));
        addDivider(viewGroup);
        viewGroup.addView(section(context, "QQ邮箱"));
        viewGroup.addView(switchRow(context, "QQ邮箱功能", "开启后主界面显示 QQ 邮箱会话卡片，关闭则不显示", com.tencent.qqlitepro.util.Config.mailEnabled(context), new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda86(context)));
    }

    static /* synthetic */ void lambda$buildFang$27(android.widget.EditText editText, android.content.Context context) {
        java.lang.String str;
        java.lang.String strTrim = editText.getText().toString().trim();
        com.tencent.qqlitepro.util.Config.setCatgirlSuffix(context, strTrim);
        if (strTrim.isEmpty()) {
            str = "已恢复默认后缀“喵~”";
        } else {
            str = "后缀已保存：" + strTrim;
        }
        toast(context, str);
    }

    static /* synthetic */ void lambda$buildFang$28(android.widget.LinearLayout linearLayout, android.view.View view, android.content.Context context, android.widget.CompoundButton compoundButton, boolean z) {
        linearLayout.setVisibility(z ? 0 : 8);
        view.setVisibility(z ? 0 : 8);
        com.tencent.qqlitepro.util.Config.setCatgirlEnabled(context, z);
    }

    static /* synthetic */ void lambda$buildFang$30(android.content.Context context, boolean z) {
        com.tencent.qqlitepro.util.Config.setMailEnabled(context, z);
        com.tencent.qqlitepro.hook.ChatCardInjector.setMailEnabled(z);
        toast(context, z ? "已开启 QQ 邮箱" : "已关闭 QQ 邮箱");
    }

    private static android.view.View pickerModeRow(android.content.Context context, java.lang.String str, java.lang.String str2, boolean z, boolean z2) {
        android.widget.LinearLayout linearLayout = new android.widget.LinearLayout(context);
        linearLayout.setOrientation(0);
        linearLayout.setGravity(16);
        linearLayout.setPadding(dp(context, 20.0f), dp(context, 12.0f), dp(context, 20.0f), dp(context, 12.0f));
        linearLayout.setLayoutParams(rootLp());
        linearLayout.setBackground(makeRowBackground(z));
        android.widget.LinearLayout linearLayout2 = new android.widget.LinearLayout(context);
        linearLayout2.setOrientation(1);
        linearLayout2.setLayoutParams(new android.widget.LinearLayout.LayoutParams(0, -2, 1.0f));
        android.widget.TextView textViewText = text(context, str, z ? ACCENT : TITLE, 15.0f, 8388611);
        android.widget.TextView textViewText2 = text(context, str2, -6643546, 11.0f, 8388611);
        linearLayout2.addView(textViewText, rootLp());
        linearLayout2.addView(textViewText2, rootLp());
        linearLayout.addView(linearLayout2);
        linearLayout.addView(text(context, z ? "✓" : "", z ? ACCENT : 0, 18.0f, 17));
        linearLayout.setOnClickListener(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda178(z2));
        return linearLayout;
    }

    static /* synthetic */ void lambda$pickerModeRow$31(boolean z, android.view.View view) {
        android.view.ViewGroup viewGroup = view.getParent() instanceof android.view.ViewGroup ? (android.view.ViewGroup) view.getParent() : null;
        if (viewGroup == null) {
            return;
        }
        android.content.Context context = viewGroup.getContext();
        if (com.tencent.qqlitepro.util.Config.nativePickerEnabled(context) != z) {
            com.tencent.qqlitepro.util.Config.setNativePickerEnabled(context, z);
            toast(context, z ? "已切换为 QQ 原生选择器" : "已切换为模块自建弹层");
        }
        viewGroup.removeAllViews();
        buildFang(context, viewGroup);
    }

    private static android.view.View statusRow(android.content.Context context, java.lang.String str, boolean z) {
        android.widget.LinearLayout linearLayout = new android.widget.LinearLayout(context);
        linearLayout.setOrientation(0);
        linearLayout.setGravity(16);
        linearLayout.setPadding(dp(context, 20.0f), dp(context, 10.0f), dp(context, 20.0f), dp(context, 10.0f));
        linearLayout.setLayoutParams(rootLp());
        android.widget.TextView textViewText = text(context, str, TITLE, 14.0f, 8388611);
        textViewText.setLayoutParams(new android.widget.LinearLayout.LayoutParams(0, -2, 1.0f));
        linearLayout.addView(textViewText);
        java.lang.StringBuilder sb = z ? new java.lang.StringBuilder("已") : new java.lang.StringBuilder("未");
        sb.append(str.replace("状态", ""));
        linearLayout.addView(text(context, sb.toString(), z ? -11751600 : -1739917, 14.0f, 8388613));
        return linearLayout;
    }

    private static void makeGroupGap(android.view.ViewGroup viewGroup) {
        android.view.View view = new android.view.View(viewGroup.getContext());
        view.setLayoutParams(new android.view.ViewGroup.LayoutParams(1, dp(viewGroup.getContext(), 10.0f)));
        viewGroup.addView(view);
    }

    private static android.view.View gap(android.content.Context context, float f) {
        android.view.View view = new android.view.View(context);
        view.setLayoutParams(new android.widget.LinearLayout.LayoutParams(-1, dp(context, f)));
        return view;
    }

    private static void marginBottom(android.view.View view, float f) {
        android.widget.LinearLayout.LayoutParams layoutParamsRootLp;
        android.view.ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams instanceof android.widget.LinearLayout.LayoutParams) {
            layoutParamsRootLp = new android.widget.LinearLayout.LayoutParams(layoutParams);
        } else {
            layoutParamsRootLp = rootLp();
        }
        layoutParamsRootLp.bottomMargin = dp(view.getContext(), f);
        view.setLayoutParams(layoutParamsRootLp);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void setModeAndRefresh(android.view.ViewGroup viewGroup, int i) {
        if (viewGroup == null) {
            return;
        }
        android.content.Context context = viewGroup.getContext();
        com.tencent.qqlitepro.util.Config.setMode(context, i);
        viewGroup.removeAllViews();
        buildFang(context, viewGroup);
    }

    private static void buildAutoTask(android.content.Context context, android.view.ViewGroup viewGroup) {
        java.lang.String strAutoContent;
        viewGroup.addView(backRow(context));
        viewGroup.addView(section(context, "定时消息功能"));
        viewGroup.addView(switchRow(context, "定时消息总开关", "关闭后不按时发送", com.tencent.qqlitepro.util.Config.autoTaskEnabled(context), new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda157(context)));
        makeGroupGap(viewGroup);
        viewGroup.addView(section(context, "定时消息列表（每条可独立设置时间/内容/目标会话）"));
        android.widget.LinearLayout linearLayout = new android.widget.LinearLayout(context);
        linearLayout.setOrientation(1);
        viewGroup.addView(linearLayout);
        java.lang.String strAutoTaskRules = com.tencent.qqlitepro.util.Config.autoTaskRules(context);
        if ((strAutoTaskRules == null || strAutoTaskRules.trim().isEmpty()) && (strAutoContent = com.tencent.qqlitepro.util.Config.autoContent(context)) != null && !strAutoContent.trim().isEmpty()) {
            strAutoTaskRules = com.tencent.qqlitepro.auto.AutoTaskScheduler.encodeRule(com.tencent.qqlitepro.util.Config.autoTime(context), strAutoContent, com.tencent.qqlitepro.util.Config.autoPeers(context));
            com.tencent.qqlitepro.util.Config.setAutoTaskRules(context, strAutoTaskRules);
            com.tencent.qqlitepro.util.Log.i("旧版单条定时消息已迁移到多规则集");
        }
        loadAutoTaskRules(context, viewGroup, linearLayout, strAutoTaskRules);
        addDivider(viewGroup);
        viewGroup.addView(saveButton(context, "添加定时消息", new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda158(context, viewGroup, linearLayout)));
        makeGroupGap(viewGroup);
        viewGroup.addView(saveButton(context, "保存定时消息", new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda159(linearLayout, context, viewGroup)));
        makeGroupGap(viewGroup);
        addResetButton(context, viewGroup, "恢复默认", 2, new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda160(context));
        hideKeyboardOnClick(context);
    }

    static /* synthetic */ void lambda$buildAutoTask$34(android.widget.LinearLayout linearLayout, android.content.Context context, android.view.ViewGroup viewGroup) {
        try {
            com.tencent.qqlitepro.util.Config.setAutoTaskRules(context, collectAutoTaskRules(linearLayout));
            com.tencent.qqlitepro.util.Log.i("已保存定时消息规则集");
            toast(context, "已保存定时消息");
            refreshPage(viewGroup, 2);
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("保存定时消息失败: " + th);
        }
    }

    private static void loadAutoTaskRules(android.content.Context context, android.view.ViewGroup viewGroup, android.widget.LinearLayout linearLayout, java.lang.String str) {
        if (str != null && !str.trim().isEmpty()) {
            boolean z = false;
            for (java.lang.String str2 : str.split("\n")) {
                if (str2 != null && !str2.trim().isEmpty()) {
                    java.lang.String[] strArrSplit = str2.split("\u0001", 4);
                    if (strArrSplit.length >= 2) {
                        z = true;
                        addAutoTaskRuleRow(context, viewGroup, linearLayout, com.tencent.qqlitepro.auto.AutoTaskScheduler.normalizeTime(strArrSplit[0]), com.tencent.qqlitepro.util.Config.unescapeField(strArrSplit[1]), strArrSplit.length >= 3 ? com.tencent.qqlitepro.util.Config.unescapeField(strArrSplit[2]) : "", strArrSplit.length >= 4 && "1".equals(strArrSplit[3].trim()));
                    }
                }
            }
            if (z) {
                return;
            }
        }
        addAutoTaskRuleRow(context, viewGroup, linearLayout, "", "", "", false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void addAutoTaskRuleRow(android.content.Context context, android.view.ViewGroup viewGroup, android.widget.LinearLayout linearLayout, java.lang.String str, java.lang.String str2, java.lang.String str3, boolean z) {
        android.widget.LinearLayout linearLayout2 = new android.widget.LinearLayout(context);
        linearLayout2.setOrientation(1);
        linearLayout2.setPadding(dp(context, 12.0f), dp(context, 10.0f), dp(context, 12.0f), dp(context, 10.0f));
        linearLayout2.setLayoutParams(rootLp());
        android.widget.LinearLayout linearLayout3 = new android.widget.LinearLayout(context);
        linearLayout3.setOrientation(0);
        linearLayout3.setGravity(16);
        android.widget.TextView textViewText = text(context, "定时消息", TITLE, 14.0f, 0);
        textViewText.setPadding(0, 0, dp(context, 4.0f), 0);
        linearLayout2.setTag(TAG_ROW_TITLE, textViewText);
        android.widget.TextView textViewText2 = text(context, "✕ 删除", -1739917, 13.0f, 17);
        textViewText2.setPadding(dp(context, 8.0f), dp(context, 2.0f), dp(context, 4.0f), dp(context, 2.0f));
        textViewText2.setClickable(true);
        textViewText2.setOnClickListener(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda37(linearLayout, linearLayout2));
        android.widget.LinearLayout.LayoutParams layoutParamsRootLp = rootLp();
        layoutParamsRootLp.weight = 1.0f;
        linearLayout3.addView(textViewText, layoutParamsRootLp);
        linearLayout3.addView(textViewText2);
        linearLayout2.addView(linearLayout3);
        android.widget.EditText editTextEdit = edit(context, str, "发送时间 HH:mm，如 08:00", 1);
        editTextEdit.setSingleLine(true);
        editTextEdit.setTag(TAG_AUTO_TIME);
        android.widget.LinearLayout.LayoutParams layoutParamsRootLp2 = rootLp();
        layoutParamsRootLp2.topMargin = dp(context, 6.0f);
        linearLayout2.addView(editInCard(context, editTextEdit), layoutParamsRootLp2);
        android.widget.EditText editTextEdit2 = edit(context, str2, "要自动发送的文本", 4);
        editTextEdit2.setTag(TAG_AUTO_CONTENT);
        setupAtTokenInput(editTextEdit2);
        android.widget.LinearLayout.LayoutParams layoutParamsRootLp3 = rootLp();
        layoutParamsRootLp3.topMargin = dp(context, 6.0f);
        layoutParamsRootLp3.bottomMargin = dp(context, 2.0f);
        linearLayout2.addView(editInCard(context, editTextEdit2), layoutParamsRootLp3);
        android.widget.LinearLayout.LayoutParams layoutParamsRootLp4 = rootLp();
        layoutParamsRootLp4.topMargin = dp(context, 4.0f);
        linearLayout2.addView(atAllInsertButton(context, editTextEdit2), layoutParamsRootLp4);
        android.widget.CompoundButton[] compoundButtonArr = new android.widget.CompoundButton[1];
        android.view.View view = (android.widget.LinearLayout) replyAtSwitchRow(context, z, compoundButtonArr);
        android.widget.LinearLayout.LayoutParams layoutParamsRootLp5 = rootLp();
        layoutParamsRootLp5.topMargin = dp(context, 6.0f);
        linearLayout2.addView(view, layoutParamsRootLp5);
        linearLayout2.setTag(TAG_REPLY_AT_SW, compoundButtonArr[0]);
        java.lang.String str4 = str3 == null ? "" : str3;
        android.widget.TextView textViewText3 = text(context, peersSummary(str4), -6643546, 12.0f, 8388611);
        textViewText3.setPadding(0, dp(context, 4.0f), 0, dp(context, 2.0f));
        linearLayout2.setTag(TAG_AUTO_PEER_LABEL, textViewText3);
        linearLayout2.setTag(TAG_AUTO_PEERS, str4);
        linearLayout2.addView(textViewText3, rootLp());
        android.view.View viewSaveButton = saveButton(context, "选择目标会话", new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda38(context, linearLayout, linearLayout2, viewGroup));
        android.widget.LinearLayout.LayoutParams layoutParamsRootLp6 = rootLp();
        layoutParamsRootLp6.topMargin = dp(context, 4.0f);
        layoutParamsRootLp6.bottomMargin = dp(context, 4.0f);
        linearLayout2.addView(viewSaveButton, layoutParamsRootLp6);
        linearLayout.addView(linearLayout2);
        renumberAutoTaskRows(linearLayout);
    }

    static /* synthetic */ void lambda$addAutoTaskRuleRow$36(android.widget.LinearLayout linearLayout, android.widget.LinearLayout linearLayout2, android.view.View view) {
        linearLayout.removeView(linearLayout2);
        renumberAutoTaskRows(linearLayout);
    }

    static /* synthetic */ void lambda$addAutoTaskRuleRow$38(android.content.Context context, android.widget.LinearLayout linearLayout, android.widget.LinearLayout linearLayout2, android.view.ViewGroup viewGroup) {
        com.tencent.qqlitepro.util.Config.setAutoTaskRules(context, collectAutoTaskRules(linearLayout));
        java.lang.Object tag = linearLayout2.getTag(TAG_AUTO_PEERS);
        restoreReplySelection(tag == null ? "" : java.lang.String.valueOf(tag));
        showSessionPicker(context, new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda39(linearLayout2, context, linearLayout, viewGroup));
    }

    static /* synthetic */ void lambda$addAutoTaskRuleRow$37(android.widget.LinearLayout linearLayout, android.content.Context context, android.widget.LinearLayout linearLayout2, android.view.ViewGroup viewGroup) {
        java.lang.String strBuildPeersString = buildPeersString();
        linearLayout.setTag(TAG_AUTO_PEERS, strBuildPeersString);
        java.lang.Object tag = linearLayout.getTag(TAG_AUTO_PEER_LABEL);
        if (tag instanceof android.widget.TextView) {
            ((android.widget.TextView) tag).setText(peersSummary(strBuildPeersString));
        }
        com.tencent.qqlitepro.util.Config.setAutoTaskRules(context, collectAutoTaskRules(linearLayout2));
        refreshPage(viewGroup, 2);
    }

    private static void renumberAutoTaskRows(android.widget.LinearLayout linearLayout) {
        int i = 1;
        for (int i2 = 0; i2 < linearLayout.getChildCount(); i2++) {
            android.view.View childAt = linearLayout.getChildAt(i2);
            if (childAt instanceof android.widget.LinearLayout) {
                java.lang.Object tag = childAt.getTag(TAG_ROW_TITLE);
                if (tag instanceof android.widget.TextView) {
                    ((android.widget.TextView) tag).setText("定时消息" + i);
                    i++;
                }
            }
        }
    }

    private static java.lang.String collectAutoTaskRules(android.widget.LinearLayout linearLayout) {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            android.view.View childAt = linearLayout.getChildAt(i);
            if (childAt instanceof android.widget.LinearLayout) {
                android.widget.LinearLayout linearLayout2 = (android.widget.LinearLayout) childAt;
                android.widget.EditText editText = null;
                android.widget.EditText editText2 = null;
                for (int i2 = 0; i2 < linearLayout2.getChildCount(); i2++) {
                    android.view.View childAt2 = linearLayout2.getChildAt(i2);
                    if (childAt2 instanceof android.widget.EditText) {
                        java.lang.Object tag = childAt2.getTag();
                        if (TAG_AUTO_TIME.equals(tag)) {
                            editText = (android.widget.EditText) childAt2;
                        } else if (TAG_AUTO_CONTENT.equals(tag)) {
                            editText2 = (android.widget.EditText) childAt2;
                        }
                    }
                }
                if (editText != null && editText2 != null) {
                    java.lang.String strTrim = editText2.getText() == null ? "" : editText2.getText().toString().trim();
                    java.lang.Object tag2 = linearLayout2.getTag(TAG_AUTO_PEERS);
                    java.lang.String strValueOf = tag2 == null ? "" : java.lang.String.valueOf(tag2);
                    if (!strTrim.isEmpty() || !strValueOf.trim().isEmpty()) {
                        java.lang.String strNormalizeTime = com.tencent.qqlitepro.auto.AutoTaskScheduler.normalizeTime(editText.getText() != null ? editText.getText().toString() : "");
                        android.widget.CompoundButton compoundButton = (android.widget.CompoundButton) linearLayout2.getTag(TAG_REPLY_AT_SW);
                        java.lang.String strEncodeRule = com.tencent.qqlitepro.auto.AutoTaskScheduler.encodeRule(strNormalizeTime, strTrim, strValueOf, compoundButton != null && compoundButton.isChecked());
                        if (sb.length() > 0) {
                            sb.append('\n');
                        }
                        sb.append(strEncodeRule);
                    }
                }
            }
        }
        return sb.toString();
    }

    private static java.lang.String peersSummary(java.lang.String str) {
        if (str == null || str.isEmpty()) {
            return "已选 0 个会话";
        }
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        int i = 0;
        for (java.lang.String str2 : str.split("\n")) {
            java.lang.String[] strArrSplit = str2.split("\\|", 3);
            if (strArrSplit.length >= 2 && !strArrSplit[1].trim().isEmpty()) {
                i++;
                if (sb.length() > 0) {
                    sb.append((char) 12289);
                }
                sb.append("1".equals(strArrSplit[0].trim()) ? "好友" : "群聊");
                sb.append((char) 183);
                sb.append((strArrSplit.length <= 2 || strArrSplit[2].isEmpty()) ? strArrSplit[1] : strArrSplit[2]);
            }
        }
        java.lang.StringBuilder sb2 = new java.lang.StringBuilder("已选 ");
        sb2.append(i);
        sb2.append(" 个会话");
        sb2.append(i > 0 ? "：" + ((java.lang.Object) sb) : "");
        return sb2.toString();
    }

    /* JADX WARN: Code duplicated, block: B:22:0x00a0  */
    /* JADX WARN: Code duplicated, block: B:25:0x00b7  */
    /* JADX WARN: Code duplicated, block: B:26:0x00b9  */
    private static void buildKeywordReply(android.content.Context context, android.view.ViewGroup viewGroup) {
        java.lang.String strTrim;
        viewGroup.addView(backRow(context));
        viewGroup.addView(section(context, "关键词回复功能"));
        android.widget.CompoundButton[] compoundButtonArr = new android.widget.CompoundButton[1];
        viewGroup.addView(switchRowState(context, "关键词回复总开关", "开启后在白名单会话内触发回复", com.tencent.qqlitepro.util.Config.keywordReplyEnabled(context), compoundButtonArr));
        makeGroupGap(viewGroup);
        viewGroup.addView(section(context, "关键词规则（每个关键词单独配一个回复）"));
        android.widget.LinearLayout linearLayout = new android.widget.LinearLayout(context);
        linearLayout.setOrientation(1);
        viewGroup.addView(linearLayout);
        java.lang.String strKeywordRules = com.tencent.qqlitepro.util.Config.keywordRules(context);
        java.lang.String strKeywordReplyKeywords = com.tencent.qqlitepro.util.Config.keywordReplyKeywords(context);
        java.lang.String strKeywordReplyContent = com.tencent.qqlitepro.util.Config.keywordReplyContent(context);
        if (strKeywordReplyKeywords != null && !strKeywordReplyKeywords.trim().isEmpty()) {
            java.lang.StringBuilder sb = new java.lang.StringBuilder(strKeywordRules == null ? "" : strKeywordRules.trim());
            if (sb.length() > 0) {
                for (java.lang.String str : sb.toString().split("\n")) {
                    java.lang.String[] strArrSplit = str.split("\\|", 2);
                    if (strArrSplit.length <= 0 || !strArrSplit[0].trim().equalsIgnoreCase(strKeywordReplyKeywords.trim())) {
                    }
                }
                if (sb.length() > 0) {
                    sb.append('\n');
                }
                sb.append(com.tencent.qqlitepro.util.Config.escapeField(strKeywordReplyKeywords.trim()));
                sb.append('|');
                if (strKeywordReplyContent == null) {
                    strTrim = "";
                } else {
                    strTrim = strKeywordReplyContent.trim();
                }
                sb.append(com.tencent.qqlitepro.util.Config.escapeField(strTrim));
                strKeywordRules = sb.toString();
                com.tencent.qqlitepro.util.Config.setKeywordRules(context, com.tencent.qqlitepro.util.Config.keywordReplyEnabled(context), strKeywordRules, com.tencent.qqlitepro.util.Config.keywordReplyPeers(context));
                com.tencent.qqlitepro.util.Config.setKeywordReply(context, com.tencent.qqlitepro.util.Config.keywordReplyEnabled(context), "", "", com.tencent.qqlitepro.util.Config.keywordReplyPeers(context));
            } else {
                if (sb.length() > 0) {
                    sb.append('\n');
                }
                sb.append(com.tencent.qqlitepro.util.Config.escapeField(strKeywordReplyKeywords.trim()));
                sb.append('|');
                if (strKeywordReplyContent == null) {
                    strTrim = "";
                } else {
                    strTrim = strKeywordReplyContent.trim();
                }
                sb.append(com.tencent.qqlitepro.util.Config.escapeField(strTrim));
                strKeywordRules = sb.toString();
                com.tencent.qqlitepro.util.Config.setKeywordRules(context, com.tencent.qqlitepro.util.Config.keywordReplyEnabled(context), strKeywordRules, com.tencent.qqlitepro.util.Config.keywordReplyPeers(context));
                com.tencent.qqlitepro.util.Config.setKeywordReply(context, com.tencent.qqlitepro.util.Config.keywordReplyEnabled(context), "", "", com.tencent.qqlitepro.util.Config.keywordReplyPeers(context));
            }
        }
        loadKeywordRules(context, linearLayout, strKeywordRules);
        addDivider(viewGroup);
        viewGroup.addView(saveButton(context, "添加关键词", new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda123(context, linearLayout)));
        addDivider(viewGroup);
        viewGroup.addView(section(context, "关键词回复白名单(多选)"));
        restoreReplySelection(com.tencent.qqlitepro.util.Config.keywordReplyPeers(context));
        viewGroup.addView(replySessionLabel(context, com.tencent.qqlitepro.util.Config.keywordReplyPeers(context)));
        viewGroup.addView(saveButton(context, "选择白名单会话", new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda134(linearLayout, compoundButtonArr, context, viewGroup)));
        makeGroupGap(viewGroup);
        viewGroup.addView(saveButton(context, "保存关键词回复", new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda144(linearLayout, compoundButtonArr, context)));
        makeGroupGap(viewGroup);
        viewGroup.addView(section(context, "AI 回复模型配置"));
        android.widget.LinearLayout linearLayout2 = new android.widget.LinearLayout(context);
        linearLayout2.setOrientation(1);
        android.graphics.drawable.GradientDrawable gradientDrawable = new android.graphics.drawable.GradientDrawable();
        gradientDrawable.setShape(0);
        gradientDrawable.setColor(SURFACE_CONTAINER);
        gradientDrawable.setCornerRadius(dpLite(14.0f));
        linearLayout2.setBackground(gradientDrawable);
        linearLayout2.setPadding(dp(context, 12.0f), dp(context, 14.0f), dp(context, 12.0f), dp(context, 14.0f));
        linearLayout2.setLayoutParams(rootLp());
        linearLayout2.addView(text(context, "请求地址 (url)", TITLE, 15.0f, 8388611), rootLp());
        android.widget.EditText editTextEdit = edit(context, com.tencent.qqlitepro.util.Config.keywordAiUrl(context), "https://api.openai.com/v1/chat/completions", 2);
        linearLayout2.addView(editTextEdit, inputLp());
        android.widget.TextView textViewText = text(context, "API 密钥 (apikey)", TITLE, 15.0f, 8388611);
        textViewText.setPadding(0, dp(context, 6.0f), 0, 0);
        linearLayout2.addView(textViewText, rootLp());
        android.widget.EditText editTextEdit2 = edit(context, com.tencent.qqlitepro.util.Config.keywordAiApiKey(context), "可留空", 1);
        linearLayout2.addView(editTextEdit2, inputLp());
        android.widget.TextView textViewText2 = text(context, "模型名 (model)", TITLE, 15.0f, 8388611);
        textViewText2.setPadding(0, dp(context, 6.0f), 0, 0);
        linearLayout2.addView(textViewText2, rootLp());
        android.widget.EditText editTextEdit3 = edit(context, com.tencent.qqlitepro.util.Config.keywordAiModel(context), "如 gpt-3.5-turbo（可留空）", 1);
        linearLayout2.addView(editTextEdit3, inputLp());
        android.widget.TextView textViewText3 = text(context, "自定义人格 (system)", TITLE, 15.0f, 8388611);
        textViewText3.setPadding(0, dp(context, 6.0f), 0, 0);
        linearLayout2.addView(textViewText3, rootLp());
        android.widget.EditText editTextEdit4 = edit(context, com.tencent.qqlitepro.util.Config.keywordAiPersona(context), "可留空；未配置时默认以原始关键词消息请求发送响应", 1);
        linearLayout2.addView(editTextEdit4, inputLp());
        viewGroup.addView(linearLayout2);
        makeGroupGap(viewGroup);
        viewGroup.addView(saveButton(context, "保存 AI 人格/模型配置", new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda145(context, editTextEdit, editTextEdit2, editTextEdit3, editTextEdit4)));
        makeGroupGap(viewGroup);
        addResetButton(context, viewGroup, "恢复默认", 5, new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda146(context));
    }

    static /* synthetic */ void lambda$buildKeywordReply$41(android.widget.LinearLayout linearLayout, android.widget.CompoundButton[] compoundButtonArr, android.content.Context context, android.view.ViewGroup viewGroup) {
        java.lang.String strCollectKeywordRules = collectKeywordRules(linearLayout);
        boolean z = false;
        android.widget.CompoundButton compoundButton = compoundButtonArr[0];
        if (compoundButton != null && compoundButton.isChecked()) {
            z = true;
        }
        showSessionPicker(context, new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda165(context, z, strCollectKeywordRules, viewGroup));
    }

    static /* synthetic */ void lambda$buildKeywordReply$40(android.content.Context context, boolean z, java.lang.String str, android.view.ViewGroup viewGroup) {
        com.tencent.qqlitepro.util.Config.setKeywordRules(context, z, str, buildPeersString());
        refreshPage(viewGroup, 5);
    }

    static /* synthetic */ void lambda$buildKeywordReply$42(android.widget.LinearLayout linearLayout, android.widget.CompoundButton[] compoundButtonArr, android.content.Context context) {
        try {
            java.lang.String strCollectKeywordRules = collectKeywordRules(linearLayout);
            boolean z = false;
            android.widget.CompoundButton compoundButton = compoundButtonArr[0];
            if (compoundButton != null && compoundButton.isChecked()) {
                z = true;
            }
            com.tencent.qqlitepro.util.Config.setKeywordRules(context, z, strCollectKeywordRules, buildPeersString());
            com.tencent.qqlitepro.util.Log.i("已保存关键词回复");
            toast(context, "已保存关键词回复");
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("保存关键词回复失败: " + th);
        }
    }

    static /* synthetic */ void lambda$buildKeywordReply$43(android.content.Context context, android.widget.EditText editText, android.widget.EditText editText2, android.widget.EditText editText3, android.widget.EditText editText4) {
        com.tencent.qqlitepro.util.Config.setKeywordAiConfig(context, editText.getText().toString(), editText2.getText().toString(), editText3.getText().toString(), editText4.getText().toString());
        toast(context, "已保存 AI 配置");
    }

    private static void loadKeywordRules(android.content.Context context, android.widget.LinearLayout linearLayout, java.lang.String str) {
        boolean zEquals = false;
        java.lang.String strTrim = "";
        boolean zEquals2 = false;
        boolean zEquals3 = false;
        if (str != null && !str.trim().isEmpty()) {
            boolean z = false;
            for (java.lang.String str2 : str.split("\n")) {
                java.lang.String strTrim2 = str2.trim();
                if (!strTrim2.isEmpty()) {
                    java.lang.String[] strArrSplit = strTrim2.split("\\|");
                    java.lang.String strTrim3 = strArrSplit.length > 0 ? strArrSplit[0].trim() : "";
                    if (strArrSplit.length == 2) {
                        strTrim = strArrSplit[1].trim();
                        zEquals3 = false;
                    } else {
                        if (strArrSplit.length == 3) {
                            java.lang.String strTrim4 = strArrSplit[1].trim();
                            zEquals3 = "1".equals(strArrSplit[2].trim());
                            strTrim = strTrim4;
                        } else if (strArrSplit.length == 4) {
                            java.lang.String strTrim5 = strArrSplit[1].trim();
                            boolean zEquals4 = "1".equals(strArrSplit[2].trim());
                            zEquals2 = "1".equals(strArrSplit[3].trim());
                            strTrim = strTrim5;
                            zEquals3 = zEquals4;
                            zEquals = false;
                        } else {
                            java.lang.StringBuilder sb = new java.lang.StringBuilder(strArrSplit[1]);
                            for (int i = 2; i < strArrSplit.length - 3; i++) {
                                sb.append('|');
                                sb.append(strArrSplit[i]);
                            }
                            java.lang.String strTrim6 = sb.toString().trim();
                            boolean zEquals5 = "1".equals(strArrSplit[strArrSplit.length - 3].trim());
                            boolean zEquals6 = "1".equals(strArrSplit[strArrSplit.length - 2].trim());
                            zEquals = "1".equals(strArrSplit[strArrSplit.length - 1].trim());
                            strTrim = strTrim6;
                            zEquals2 = zEquals6;
                            zEquals3 = zEquals5;
                        }
                        addKeywordRuleRow(context, linearLayout, com.tencent.qqlitepro.util.Config.unescapeField(strTrim3), com.tencent.qqlitepro.util.Config.unescapeField(strTrim), zEquals3, zEquals2, zEquals);
                        z = true;
                    }
                    zEquals2 = false;
                    zEquals = false;
                    addKeywordRuleRow(context, linearLayout, com.tencent.qqlitepro.util.Config.unescapeField(strTrim3), com.tencent.qqlitepro.util.Config.unescapeField(strTrim), zEquals3, zEquals2, zEquals);
                    z = true;
                }
            }
            if (z) {
                return;
            }
        }
        addKeywordRuleRow(context, linearLayout, "", "", false, false, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void addKeywordRuleRow(android.content.Context context, android.widget.LinearLayout linearLayout, java.lang.String str, java.lang.String str2, boolean z, boolean z2, boolean z3) {
        android.widget.LinearLayout linearLayout2 = new android.widget.LinearLayout(context);
        linearLayout2.setOrientation(1);
        linearLayout2.setPadding(dp(context, 12.0f), dp(context, 10.0f), dp(context, 12.0f), dp(context, 10.0f));
        linearLayout2.setLayoutParams(rootLp());
        android.widget.LinearLayout linearLayout3 = new android.widget.LinearLayout(context);
        linearLayout3.setOrientation(0);
        linearLayout3.setGravity(16);
        android.widget.TextView textViewText = text(context, "关键词", TITLE, 14.0f, 0);
        textViewText.setPadding(0, 0, dp(context, 4.0f), 0);
        linearLayout2.setTag(TAG_ROW_TITLE, textViewText);
        android.widget.TextView textViewText2 = text(context, "✕ 删除", -1739917, 13.0f, 17);
        textViewText2.setPadding(dp(context, 8.0f), dp(context, 2.0f), dp(context, 4.0f), dp(context, 2.0f));
        textViewText2.setClickable(true);
        textViewText2.setOnClickListener(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda114(linearLayout, linearLayout2));
        android.widget.LinearLayout.LayoutParams layoutParamsRootLp = rootLp();
        layoutParamsRootLp.weight = 1.0f;
        linearLayout3.addView(textViewText, layoutParamsRootLp);
        linearLayout3.addView(textViewText2);
        linearLayout2.addView(linearLayout3);
        android.widget.EditText editTextEdit = edit(context, str, "键入关键词", 1);
        editTextEdit.setSingleLine(true);
        editTextEdit.setTag(TAG_KW_EDIT);
        android.widget.LinearLayout.LayoutParams layoutParamsRootLp2 = rootLp();
        layoutParamsRootLp2.topMargin = dp(context, 6.0f);
        linearLayout2.addView(editInCard(context, editTextEdit), layoutParamsRootLp2);
        android.widget.EditText editTextEdit2 = edit(context, str2, "键入回复内容", 2);
        editTextEdit2.setTag(TAG_RP_EDIT);
        setupAtTokenInput(editTextEdit2);
        android.widget.LinearLayout.LayoutParams layoutParamsRootLp3 = rootLp();
        layoutParamsRootLp3.topMargin = dp(context, 6.0f);
        layoutParamsRootLp3.bottomMargin = dp(context, 2.0f);
        linearLayout2.addView(editInCard(context, editTextEdit2), layoutParamsRootLp3);
        android.widget.LinearLayout.LayoutParams layoutParamsRootLp4 = rootLp();
        layoutParamsRootLp4.topMargin = dp(context, 4.0f);
        linearLayout2.addView(atInsertButton(context, editTextEdit2), layoutParamsRootLp4);
        android.widget.CompoundButton[] compoundButtonArr = new android.widget.CompoundButton[1];
        android.view.View view = (android.widget.LinearLayout) replyAtSwitchRow(context, z3, compoundButtonArr);
        android.widget.LinearLayout.LayoutParams layoutParamsRootLp5 = rootLp();
        layoutParamsRootLp5.topMargin = dp(context, 6.0f);
        linearLayout2.addView(view, layoutParamsRootLp5);
        linearLayout2.setTag(TAG_REPLY_AT_SW, compoundButtonArr[0]);
        android.view.View[] viewArr = new android.view.View[1];
        android.widget.CompoundButton[] compoundButtonArr2 = new android.widget.CompoundButton[1];
        android.view.View view2 = (android.widget.LinearLayout) switchRowState(context, "AI回复", "命中该关键词后由AI生成回复", z, compoundButtonArr2, new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda115(viewArr));
        android.widget.LinearLayout.LayoutParams layoutParamsRootLp6 = rootLp();
        layoutParamsRootLp6.topMargin = dp(context, 6.0f);
        linearLayout2.addView(view2, layoutParamsRootLp6);
        linearLayout2.setTag(TAG_AI_SW, compoundButtonArr2[0]);
        android.widget.CompoundButton[] compoundButtonArr3 = new android.widget.CompoundButton[1];
        android.widget.LinearLayout linearLayout4 = (android.widget.LinearLayout) switchRowState(context, "群聊@触发", "仅当@本人且包含关键词时回复", z && z2, compoundButtonArr3);
        android.widget.LinearLayout.LayoutParams layoutParamsRootLp7 = rootLp();
        layoutParamsRootLp7.topMargin = dp(context, 6.0f);
        linearLayout4.setVisibility(z ? 0 : 8);
        linearLayout2.addView(linearLayout4, layoutParamsRootLp7);
        linearLayout2.setTag(TAG_AT_SW, compoundButtonArr3[0]);
        viewArr[0] = linearLayout4;
        linearLayout.addView(linearLayout2);
        renumberKeywordRows(linearLayout);
    }

    static /* synthetic */ void lambda$addKeywordRuleRow$45(android.widget.LinearLayout linearLayout, android.widget.LinearLayout linearLayout2, android.view.View view) {
        linearLayout.removeView(linearLayout2);
        renumberKeywordRows(linearLayout);
    }

    static /* synthetic */ void lambda$addKeywordRuleRow$46(android.view.View[] viewArr, boolean z) {
        android.view.View view = viewArr[0];
        if (view != null) {
            view.setVisibility(z ? 0 : 8);
        }
    }

    private static void renumberKeywordRows(android.widget.LinearLayout linearLayout) {
        int i = 1;
        for (int i2 = 0; i2 < linearLayout.getChildCount(); i2++) {
            android.view.View childAt = linearLayout.getChildAt(i2);
            if (childAt instanceof android.widget.LinearLayout) {
                java.lang.Object tag = childAt.getTag(TAG_ROW_TITLE);
                if (tag instanceof android.widget.TextView) {
                    ((android.widget.TextView) tag).setText("关键词" + i);
                    i++;
                }
            }
        }
    }

    private static java.lang.String collectKeywordRules(android.widget.LinearLayout linearLayout) {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            android.view.View childAt = linearLayout.getChildAt(i);
            if (childAt instanceof android.widget.LinearLayout) {
                android.widget.LinearLayout linearLayout2 = (android.widget.LinearLayout) childAt;
                android.widget.EditText editText = null;
                android.widget.EditText editText2 = null;
                for (int i2 = 0; i2 < linearLayout2.getChildCount(); i2++) {
                    android.view.View childAt2 = linearLayout2.getChildAt(i2);
                    if (childAt2 instanceof android.widget.EditText) {
                        java.lang.Object tag = childAt2.getTag();
                        if (TAG_KW_EDIT.equals(tag)) {
                            editText = (android.widget.EditText) childAt2;
                        } else if (TAG_RP_EDIT.equals(tag)) {
                            editText2 = (android.widget.EditText) childAt2;
                        }
                    }
                }
                if (editText != null && editText2 != null) {
                    android.widget.CompoundButton compoundButton = (android.widget.CompoundButton) linearLayout2.getTag(TAG_AI_SW);
                    boolean z = compoundButton != null && compoundButton.isChecked();
                    android.widget.CompoundButton compoundButton2 = (android.widget.CompoundButton) linearLayout2.getTag(TAG_AT_SW);
                    boolean z2 = compoundButton2 != null && compoundButton2.isChecked();
                    android.widget.CompoundButton compoundButton3 = (android.widget.CompoundButton) linearLayout2.getTag(TAG_REPLY_AT_SW);
                    boolean z3 = compoundButton3 != null && compoundButton3.isChecked();
                    java.lang.String strEscapeField = com.tencent.qqlitepro.util.Config.escapeField(editText.getText().toString().trim());
                    java.lang.String strEscapeField2 = com.tencent.qqlitepro.util.Config.escapeField(editText2.getText().toString().trim());
                    if (!strEscapeField.isEmpty() && (!strEscapeField2.isEmpty() || z)) {
                        if (sb.length() > 0) {
                            sb.append('\n');
                        }
                        sb.append(strEscapeField);
                        sb.append('|');
                        sb.append(strEscapeField2);
                        sb.append('|');
                        sb.append(z ? "1" : "0");
                        sb.append('|');
                        sb.append(z2 ? "1" : "0");
                        sb.append('|');
                        sb.append(z3 ? "1" : "0");
                    }
                }
            }
        }
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void refreshPage(android.view.ViewGroup viewGroup, int i) {
        if (viewGroup == null) {
            return;
        }
        viewGroup.removeAllViews();
        openPage(viewGroup, i);
    }

    private static void buildAutoReply(android.content.Context context, android.view.ViewGroup viewGroup) {
        viewGroup.addView(backRow(context));
        viewGroup.addView(section(context, "自动回复功能"));
        android.widget.CompoundButton[] compoundButtonArr = new android.widget.CompoundButton[1];
        viewGroup.addView(switchRowState(context, "自动回复总开关", "开启后收到白名单私聊消息即回复", com.tencent.qqlitepro.util.Config.autoReplyEnabled(context), compoundButtonArr));
        makeGroupGap(viewGroup);
        viewGroup.addView(section(context, "回复内容"));
        android.widget.EditText editTextEdit = edit(context, com.tencent.qqlitepro.util.Config.autoReplyContent(context), "收到消息时自动发送的回复", 4);
        setupAtTokenInput(editTextEdit);
        editTextEdit.setLayoutParams(inputLp());
        viewGroup.addView(editTextEdit);
        viewGroup.addView(atAllInsertButton(context, editTextEdit), inputLp());
        android.widget.CompoundButton[] compoundButtonArr2 = new android.widget.CompoundButton[1];
        viewGroup.addView(replyAtSwitchRow(context, com.tencent.qqlitepro.util.Config.autoReplyAt(context), compoundButtonArr2));
        addDivider(viewGroup);
        viewGroup.addView(section(context, "自动回复白名单(多选)"));
        restoreReplySelection(com.tencent.qqlitepro.util.Config.autoReplyPeers(context));
        viewGroup.addView(replySessionLabel(context, com.tencent.qqlitepro.util.Config.autoReplyPeers(context)));
        viewGroup.addView(saveButton(context, "选择白名单会话", new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda92(editTextEdit, compoundButtonArr, compoundButtonArr2, context, viewGroup)));
        makeGroupGap(viewGroup);
        viewGroup.addView(saveButton(context, "保存自动回复", new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda93(compoundButtonArr, editTextEdit, compoundButtonArr2, context)));
        makeGroupGap(viewGroup);
        addResetButton(context, viewGroup, "恢复默认", 6, new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda94(context));
    }

    static /* synthetic */ void lambda$buildAutoReply$48(android.widget.EditText editText, android.widget.CompoundButton[] compoundButtonArr, android.widget.CompoundButton[] compoundButtonArr2, android.content.Context context, android.view.ViewGroup viewGroup) {
        java.lang.String strTrim = editText.getText() == null ? "" : editText.getText().toString().trim();
        android.widget.CompoundButton compoundButton = compoundButtonArr[0];
        boolean z = compoundButton != null && compoundButton.isChecked();
        android.widget.CompoundButton compoundButton2 = compoundButtonArr2[0];
        showSessionPicker(context, new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda3(context, z, strTrim, compoundButton2 != null && compoundButton2.isChecked(), viewGroup));
    }

    static /* synthetic */ void lambda$buildAutoReply$47(android.content.Context context, boolean z, java.lang.String str, boolean z2, android.view.ViewGroup viewGroup) {
        com.tencent.qqlitepro.util.Config.setAutoReply(context, z, str, buildPeersString(), z2);
        refreshPage(viewGroup, 6);
    }

    static /* synthetic */ void lambda$buildAutoReply$49(android.widget.CompoundButton[] compoundButtonArr, android.widget.EditText editText, android.widget.CompoundButton[] compoundButtonArr2, android.content.Context context) {
        boolean z = false;
        try {
            android.widget.CompoundButton compoundButton = compoundButtonArr[0];
            boolean z2 = compoundButton != null && compoundButton.isChecked();
            java.lang.String strTrim = editText.getText() == null ? "" : editText.getText().toString().trim();
            android.widget.CompoundButton compoundButton2 = compoundButtonArr2[0];
            if (compoundButton2 != null && compoundButton2.isChecked()) {
                z = true;
            }
            com.tencent.qqlitepro.util.Config.setAutoReply(context, z2, strTrim, buildPeersString(), z);
            com.tencent.qqlitepro.util.Log.i("已保存自动回复");
            toast(context, "已保存自动回复");
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("保存自动回复失败: " + th);
        }
    }

    private static void restoreReplySelection(java.lang.String str) {
        sSelectedKeys.clear();
        sSessionCache.clear();
        if (str == null) {
            return;
        }
        for (java.lang.String str2 : str.split("\n")) {
            java.lang.String strTrim = str2.trim();
            if (!strTrim.isEmpty()) {
                java.lang.String[] strArrSplit = strTrim.split("\\|", 3);
                if (strArrSplit.length >= 2 && !strArrSplit[1].trim().isEmpty()) {
                    java.lang.String str3 = strArrSplit[0] + ":" + strArrSplit[1];
                    sSelectedKeys.add(str3);
                    sSessionCache.put(str3, strTrim);
                }
            }
        }
    }

    private static android.view.View replySessionLabel(android.content.Context context, java.lang.String str) {
        android.widget.LinearLayout linearLayout = new android.widget.LinearLayout(context);
        linearLayout.setOrientation(1);
        linearLayout.setPadding(dp(context, 20.0f), dp(context, 4.0f), dp(context, 20.0f), dp(context, 4.0f));
        linearLayout.addView(text(context, "已选 " + sSelectedKeys.size() + " 个会话", TITLE, 14.0f, 8388611));
        if (str != null && !str.trim().isEmpty()) {
            java.lang.String[] strArrSplit = str.split("\n");
            int length = strArrSplit.length;
            for (int i = 0; i < length; i++) {
                java.lang.String str2 = strArrSplit[i];
                if (!str2.trim().isEmpty()) {
                    java.lang.String[] strArrSplit2 = str2.split("\\|", 3);
                    if (strArrSplit2.length > 2) {
                        str2 = strArrSplit2[2];
                    } else if (strArrSplit2.length > 1) {
                        str2 = strArrSplit2[1];
                    }
                    linearLayout.addView(text(context, ((strArrSplit2.length <= 0 || !"1".equals(strArrSplit2[0])) ? "群聊" : "好友") + " · " + str2, -6643546, 12.0f, 8388611));
                }
            }
        }
        return linearLayout;
    }

    private static void buildPort(android.content.Context context, android.view.ViewGroup viewGroup) {
        viewGroup.addView(backRow(context));
        viewGroup.addView(section(context, "端口设置"));
        android.widget.LinearLayout linearLayout = new android.widget.LinearLayout(context);
        linearLayout.setOrientation(1);
        android.graphics.drawable.GradientDrawable gradientDrawable = new android.graphics.drawable.GradientDrawable();
        gradientDrawable.setShape(0);
        gradientDrawable.setColor(SURFACE_CONTAINER);
        gradientDrawable.setCornerRadius(dpLite(14.0f));
        linearLayout.setBackground(gradientDrawable);
        linearLayout.setPadding(dp(context, 12.0f), dp(context, 14.0f), dp(context, 12.0f), dp(context, 14.0f));
        linearLayout.setLayoutParams(rootLp());
        linearLayout.addView(text(context, "本地 HTTP 服务器监听端口", TITLE, 15.0f, 8388611), rootLp());
        android.widget.EditText editTextEdit = edit(context, java.lang.String.valueOf(com.tencent.qqlitepro.util.Config.port(context)), "1024 - 65535", 1);
        editTextEdit.setInputType(2);
        linearLayout.addView(editTextEdit, inputLp());
        android.widget.TextView textViewText = text(context, "修改后保存即重启服务器；开放该端口可能被同网段他人调用，请谨慎。", HINT, 11.0f, 8388611);
        textViewText.setPadding(0, dp(context, 6.0f), 0, 0);
        linearLayout.addView(textViewText, rootLp());
        viewGroup.addView(linearLayout);
        makeGroupGap(viewGroup);
        viewGroup.addView(saveButton(context, "保存端口", new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda172(editTextEdit, context, viewGroup)));
    }

    static /* synthetic */ void lambda$buildPort$51(android.widget.EditText editText, android.content.Context context, android.view.ViewGroup viewGroup) {
        int i;
        java.lang.String strTrim = editText.getText().toString().trim();
        try {
            i = java.lang.Integer.parseInt(strTrim);
        } catch (java.lang.Throwable unused) {
            i = 0;
        }
        if (i < 1024 || i > 65535) {
            com.tencent.qqlitepro.util.Log.w("端口无效: " + strTrim);
            return;
        }
        com.tencent.qqlitepro.util.Config.setPort(context, i);
        com.tencent.qqlitepro.http.HttpApiServer.setPort(i);
        viewGroup.removeAllViews();
        buildPort(context, viewGroup);
        com.tencent.qqlitepro.util.Log.i("端口已修改为 " + i);
    }

    private static void buildSummary(android.content.Context context, android.view.ViewGroup viewGroup) {
        viewGroup.addView(backRow(context));
        viewGroup.addView(section(context, "AI 总结设置"));
        android.widget.LinearLayout linearLayout = new android.widget.LinearLayout(context);
        linearLayout.setOrientation(1);
        android.graphics.drawable.GradientDrawable gradientDrawable = new android.graphics.drawable.GradientDrawable();
        gradientDrawable.setShape(0);
        gradientDrawable.setColor(SURFACE_CONTAINER);
        gradientDrawable.setCornerRadius(dpLite(14.0f));
        linearLayout.setBackground(gradientDrawable);
        linearLayout.setPadding(dp(context, 12.0f), dp(context, 14.0f), dp(context, 12.0f), dp(context, 14.0f));
        linearLayout.setLayoutParams(rootLp());
        linearLayout.addView(text(context, "请求地址 (url)", TITLE, 15.0f, 8388611), rootLp());
        android.widget.EditText editTextEdit = edit(context, com.tencent.qqlitepro.util.Config.summaryUrl(context), "https://api.openai.com/v1/chat/completions", 2);
        linearLayout.addView(editTextEdit, inputLp());
        linearLayout.addView(text(context, "API 密钥 (apikey)", TITLE, 15.0f, 8388611), rootLp());
        android.widget.EditText editTextEdit2 = edit(context, com.tencent.qqlitepro.util.Config.summaryApiKey(context), "可留空", 1);
        linearLayout.addView(editTextEdit2, inputLp());
        android.widget.TextView textViewText = text(context, "模型名 (model)", TITLE, 15.0f, 8388611);
        textViewText.setPadding(0, dp(context, 6.0f), 0, 0);
        linearLayout.addView(textViewText, rootLp());
        android.widget.EditText editTextEdit3 = edit(context, com.tencent.qqlitepro.util.Config.summaryModel(context), "如 gpt-3.5-turbo（可留空）", 1);
        linearLayout.addView(editTextEdit3, inputLp());
        android.widget.TextView textViewText2 = text(context, "（总结风格、语言沿用 QQMax 「聊天总结」设置中的原有选项）", HINT, 10.0f, 8388611);
        textViewText2.setPadding(0, dp(context, 4.0f), 0, dp(context, 8.0f));
        linearLayout.addView(textViewText2, rootLp());
        viewGroup.addView(linearLayout);
        makeGroupGap(viewGroup);
        android.widget.LinearLayout linearLayout2 = new android.widget.LinearLayout(context);
        linearLayout2.setOrientation(1);
        android.graphics.drawable.GradientDrawable gradientDrawable2 = new android.graphics.drawable.GradientDrawable();
        gradientDrawable2.setColor(-15461356);
        gradientDrawable2.setCornerRadius(dpLite(12.0f));
        gradientDrawable2.setStroke(1, -13743516);
        linearLayout2.setBackground(gradientDrawable2);
        linearLayout2.setPadding(dp(context, 4.0f), dp(context, 8.0f), dp(context, 4.0f), dp(context, 6.0f));
        android.widget.LinearLayout.LayoutParams layoutParamsRootLp = rootLp();
        layoutParamsRootLp.leftMargin = dp(context, 12.0f);
        layoutParamsRootLp.rightMargin = dp(context, 12.0f);
        linearLayout2.setLayoutParams(layoutParamsRootLp);
        android.widget.ScrollView scrollView = new android.widget.ScrollView(context);
        scrollView.setLayoutParams(new android.widget.LinearLayout.LayoutParams(-1, dp(context, 180.0f)));
        android.widget.LinearLayout linearLayout3 = new android.widget.LinearLayout(context);
        linearLayout3.setOrientation(1);
        linearLayout3.setPadding(dp(context, 8.0f), dp(context, 4.0f), dp(context, 8.0f), dp(context, 4.0f));
        scrollView.addView(linearLayout3, rootLp());
        android.widget.TextView textViewText3 = text(context, "", -6643546, 12.0f, 8388611);
        textViewText3.setPadding(dp(context, 8.0f), dp(context, 4.0f), dp(context, 8.0f), dp(context, 4.0f));
        textViewText3.setMovementMethod(new android.text.method.ScrollingMovementMethod());
        linearLayout3.addView(textViewText3, rootLp());
        linearLayout2.addView(scrollView, rootLp());
        addLogTitleRow(context, linearLayout2, "日志输出", textViewText3);
        viewGroup.addView(linearLayout2);
        makeGroupGap(viewGroup);
        viewGroup.addView(saveButton(context, "测试请求·发送-你好-", new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda152(editTextEdit, editTextEdit2, textViewText3, context, editTextEdit3)));
        makeGroupGap(viewGroup);
        viewGroup.addView(saveButton(context, "保存设置", new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda153(context, editTextEdit, editTextEdit2, editTextEdit3, textViewText3)));
    }

    static /* synthetic */ void lambda$buildSummary$55(android.widget.EditText editText, android.widget.EditText editText2, android.widget.TextView textView, android.content.Context context, android.widget.EditText editText3) {
        java.lang.String strTrim = editText.getText().toString().trim();
        java.lang.String strTrim2 = editText2.getText().toString().trim();
        if (strTrim.isEmpty()) {
            appendLog(textView, "请先填写请求地址");
            toast(context, "请先填写请求地址");
            return;
        }
        appendLog(textView, "—— 测试请求开始 url=" + strTrim + " ——");
        new java.lang.Thread(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda33(strTrim, strTrim2, editText3, textView)).start();
    }

    static /* synthetic */ void lambda$buildSummary$54(java.lang.String str, java.lang.String str2, android.widget.EditText editText, android.widget.TextView textView) {
        try {
            new android.os.Handler(android.os.Looper.getMainLooper()).post(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda168(textView, com.tencent.qqlitepro.hook.AiSummaryHook.testRequest(str, str2, editText.getText().toString().trim())));
        } catch (java.lang.Throwable th) {
            new android.os.Handler(android.os.Looper.getMainLooper()).post(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda169(textView, "异常: " + th));
        }
    }

    static /* synthetic */ void lambda$buildSummary$56(android.content.Context context, android.widget.EditText editText, android.widget.EditText editText2, android.widget.EditText editText3, android.widget.TextView textView) {
        com.tencent.qqlitepro.util.Config.setSummaryConfig(context, editText.getText().toString(), editText2.getText().toString(), editText3.getText().toString());
        appendLog(textView, "已保存设置 model=" + editText3.getText().toString().trim());
        toast(context, "已保存");
    }

    private static void buildNotice(android.content.Context context, android.view.ViewGroup viewGroup) {
        android.widget.FrameLayout frameLayout = new android.widget.FrameLayout(context);
        frameLayout.addView(section(context, "官方公告"), new android.widget.FrameLayout.LayoutParams(-1, -2, 16));
        android.view.View viewNoticeNodeSwitcher = noticeNodeSwitcher(context, viewGroup);
        android.widget.FrameLayout.LayoutParams layoutParams = new android.widget.FrameLayout.LayoutParams(dp(context, 44.0f), dp(context, 44.0f), 21);
        layoutParams.rightMargin = dp(context, 8.0f);
        frameLayout.addView(viewNoticeNodeSwitcher, layoutParams);
        viewGroup.addView(frameLayout);
        android.widget.TextView textViewText = text(context, "加载中…", HINT, 13.0f, 8388611);
        textViewText.setPadding(dp(context, 12.0f), 0, dp(context, 12.0f), dp(context, 8.0f));
        viewGroup.addView(textViewText);
        new java.lang.Thread(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda124(textViewText, context, viewGroup), "notice-fetch").start();
    }

    static /* synthetic */ void lambda$buildNotice$60(android.widget.TextView textView, android.content.Context context, android.view.ViewGroup viewGroup) {
        try {
            new android.os.Handler(android.os.Looper.getMainLooper()).post(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda191(textView, parseNotices(fetchJson(effectiveNoticeUrl())), context, viewGroup));
        } catch (java.lang.Throwable th) {
            new android.os.Handler(android.os.Looper.getMainLooper()).post(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda192(textView, "加载失败/超时：" + th.getMessage(), viewGroup, context));
        }
    }

    static /* synthetic */ void lambda$buildNotice$57(android.widget.TextView textView, java.util.List list, android.content.Context context, android.view.ViewGroup viewGroup) {
        try {
            textView.setText("共 " + list.size() + " 条公告");
            renderNoticeCards(context, viewGroup, list);
        } catch (java.lang.Throwable th) {
            textView.setText("执行异常: " + th.getMessage());
        }
    }

    static /* synthetic */ void lambda$buildNotice$59(android.widget.TextView textView, java.lang.String str, android.view.ViewGroup viewGroup, android.content.Context context) {
        textView.setText(str);
        makeGroupGap(viewGroup);
        viewGroup.addView(saveButton(context, "刷新重试", new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda111(viewGroup)));
    }

    private static java.lang.String effectiveNoticeUrl() {
        if (sNoticeNodeUrl != null && !sNoticeNodeUrl.isEmpty()) {
            return sNoticeNodeUrl;
        }
        try {
            for (com.google.gson.JsonObject jsonObject : fetchNoticeNodeConfig()) {
                if (noticeNodeDefault(jsonObject)) {
                    java.lang.String strNoticeStr = noticeStr(jsonObject, "url");
                    if (!strNoticeStr.isEmpty()) {
                        return strNoticeStr;
                    }
                }
            }
            return NOTICE_URL;
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("解析公告默认节点失败: " + th);
            return NOTICE_URL;
        }
    }

    private static java.util.List<com.google.gson.JsonObject> fetchNoticeNodeConfig() throws java.lang.Exception {
        com.google.gson.JsonObject asJsonObject = com.google.gson.JsonParser.parseString(fetchJson(NODE_CONFIG_URL)).getAsJsonObject();
        java.util.ArrayList arrayList = new java.util.ArrayList();
        com.google.gson.JsonElement jsonElement = asJsonObject.get("configs");
        if (jsonElement != null && jsonElement.isJsonArray()) {
            for (com.google.gson.JsonElement jsonElement2 : jsonElement.getAsJsonArray()) {
                if (jsonElement2 != null && jsonElement2.isJsonObject()) {
                    arrayList.add(jsonElement2.getAsJsonObject());
                }
            }
        }
        arrayList.sort(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda113());
        return arrayList;
    }

    static int noticeNodeId(com.google.gson.JsonObject jsonObject) {
        try {
            if (jsonObject.has("id") && jsonObject.get("id").isJsonPrimitive()) {
                return jsonObject.get("id").getAsInt();
            }
            return 0;
        } catch (java.lang.Throwable unused) {
            return 0;
        }
    }

    private static boolean noticeNodeDefault(com.google.gson.JsonObject jsonObject) {
        try {
            return jsonObject.has("is_default") && jsonObject.get("is_default").getAsBoolean();
        } catch (java.lang.Throwable unused) {
            return false;
        }
    }

    private static android.view.View noticeNodeSwitcher(android.content.Context context, android.view.ViewGroup viewGroup) {
        android.widget.LinearLayout linearLayout = new android.widget.LinearLayout(context);
        linearLayout.setOrientation(1);
        linearLayout.setGravity(17);
        linearLayout.setClickable(true);
        linearLayout.setFocusable(true);
        int iDp = dp(context, 20.0f);
        int iMax = java.lang.Math.max(2, dp(context, 2.5f));
        int iDp2 = dp(context, 4.0f);
        for (int i = 0; i < 3; i++) {
            android.view.View view = new android.view.View(context);
            android.graphics.drawable.GradientDrawable gradientDrawable = new android.graphics.drawable.GradientDrawable();
            gradientDrawable.setShape(0);
            gradientDrawable.setColor(-1);
            gradientDrawable.setCornerRadius(iMax);
            view.setBackground(gradientDrawable);
            android.widget.LinearLayout.LayoutParams layoutParams = new android.widget.LinearLayout.LayoutParams(iDp, iMax);
            if (i > 0) {
                layoutParams.topMargin = iDp2;
            }
            linearLayout.addView(view, layoutParams);
        }
        linearLayout.setOnClickListener(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda41(viewGroup));
        return linearLayout;
    }

    private static void buildNoticeNodes(android.content.Context context, android.view.ViewGroup viewGroup) {
        android.widget.LinearLayout linearLayout = new android.widget.LinearLayout(context);
        linearLayout.setOrientation(0);
        linearLayout.setGravity(16);
        linearLayout.setPadding(dp(context, 20.0f), dp(context, 12.0f), dp(context, 20.0f), dp(context, 12.0f));
        linearLayout.setLayoutParams(rootLp());
        linearLayout.setOnClickListener(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda8(viewGroup));
        linearLayout.addView(text(context, "←  返回公告", HINT, 14.0f, 8388611));
        viewGroup.addView(linearLayout);
        viewGroup.addView(section(context, "公告节点"));
        android.widget.FrameLayout frameLayout = new android.widget.FrameLayout(context);
        frameLayout.setPadding(dp(context, 20.0f), dp(context, 6.0f), dp(context, 20.0f), dp(context, 10.0f));
        android.widget.TextView textViewText = text(context, "加载中…", -6643546, 14.0f, 8388611);
        textViewText.setPadding(0, dp(context, 16.0f), 0, dp(context, 16.0f));
        frameLayout.addView(textViewText, new android.widget.FrameLayout.LayoutParams(-1, -2));
        viewGroup.addView(frameLayout, rootLp());
        new java.lang.Thread(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda9(context, frameLayout, textViewText, viewGroup), "notice-node-fetch").start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void fillNoticeNodeList(android.content.Context context, android.widget.FrameLayout frameLayout, android.widget.TextView textView, android.view.ViewGroup viewGroup) {
        java.util.List<com.google.gson.JsonObject> listFetchNoticeNodeConfig;
        java.lang.String str;
        try {
            str = null;
            listFetchNoticeNodeConfig = fetchNoticeNodeConfig();
        } catch (java.lang.Throwable th) {
            listFetchNoticeNodeConfig = null;
            str = "节点加载失败：" + th.getMessage();
        }
        new android.os.Handler(android.os.Looper.getMainLooper()).post(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda32(frameLayout, str, textView, context, viewGroup, listFetchNoticeNodeConfig));
    }

    static /* synthetic */ void lambda$fillNoticeNodeList$68(android.widget.FrameLayout frameLayout, java.lang.String str, android.widget.TextView textView, android.content.Context context, android.view.ViewGroup viewGroup, java.util.List list) {
        try {
            frameLayout.removeAllViews();
            if (str != null) {
                textView.setText(str);
                frameLayout.addView(textView, new android.widget.FrameLayout.LayoutParams(-1, -2));
                android.view.View viewSaveButton = saveButton(context, "重试", new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda204(textView, frameLayout, context, viewGroup));
                android.widget.FrameLayout.LayoutParams layoutParams = new android.widget.FrameLayout.LayoutParams(-1, -2);
                layoutParams.topMargin = dp(context, 6.0f);
                frameLayout.addView(viewSaveButton, layoutParams);
                return;
            }
            if (list != null && !list.isEmpty()) {
                android.widget.ScrollView scrollView = new android.widget.ScrollView(context);
                android.widget.LinearLayout linearLayout = new android.widget.LinearLayout(context);
                int i = 1;
                linearLayout.setOrientation(1);
                java.util.Iterator it = list.iterator();
                while (it.hasNext()) {
                    com.google.gson.JsonObject jsonObject = (com.google.gson.JsonObject) it.next();
                    java.lang.String strNoticeStr = noticeStr(jsonObject, com.sun.mail.imap.IMAPStore.ID_NAME);
                    if (strNoticeStr.isEmpty()) {
                        strNoticeStr = "节点";
                    }
                    java.lang.String strNoticeStr2 = noticeStr(jsonObject, "desc");
                    java.lang.String strNoticeStr3 = noticeStr(jsonObject, "url");
                    boolean zNoticeNodeDefault = noticeNodeDefault(jsonObject);
                    android.widget.LinearLayout linearLayout2 = new android.widget.LinearLayout(context);
                    linearLayout2.setOrientation(i);
                    android.graphics.drawable.GradientDrawable gradientDrawable = new android.graphics.drawable.GradientDrawable();
                    gradientDrawable.setShape(0);
                    gradientDrawable.setColor(-14013129);
                    gradientDrawable.setCornerRadius(dpLite(10.0f));
                    linearLayout2.setBackground(gradientDrawable);
                    java.util.Iterator it2 = it;
                    linearLayout2.setPadding(dp(context, 12.0f), dp(context, 10.0f), dp(context, 12.0f), dp(context, 10.0f));
                    if (zNoticeNodeDefault) {
                        strNoticeStr = strNoticeStr + "（默认）";
                    }
                    linearLayout2.addView(text(context, strNoticeStr, -1, 14.0f, 8388611), rootLp());
                    if (strNoticeStr2 != null && !strNoticeStr2.isEmpty()) {
                        android.widget.TextView textViewText = text(context, strNoticeStr2, -6643546, 12.0f, 8388611);
                        textViewText.setPadding(0, dp(context, 2.0f), 0, 0);
                        linearLayout2.addView(textViewText, rootLp());
                    }
                    android.widget.LinearLayout.LayoutParams layoutParamsRootLp = rootLp();
                    layoutParamsRootLp.bottomMargin = dp(context, 10.0f);
                    linearLayout2.setLayoutParams(layoutParamsRootLp);
                    linearLayout2.setClickable(true);
                    linearLayout2.setOnClickListener(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda206(strNoticeStr3, viewGroup));
                    linearLayout.addView(linearLayout2);
                    it = it2;
                    i = 1;
                }
                scrollView.addView(linearLayout, new android.widget.FrameLayout.LayoutParams(-1, -2));
                frameLayout.addView(scrollView, new android.widget.FrameLayout.LayoutParams(-1, -2));
                return;
            }
            textView.setText("暂无可用节点");
            frameLayout.addView(textView, new android.widget.FrameLayout.LayoutParams(-1, -2));
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("公告节点列表渲染失败: " + th);
            try {
                textView.setText("节点渲染失败：" + th.getMessage());
                frameLayout.removeAllViews();
                frameLayout.addView(textView, new android.widget.FrameLayout.LayoutParams(-1, -2));
            } catch (java.lang.Throwable unused) {
            }
        }
    }

    static /* synthetic */ void lambda$fillNoticeNodeList$66(android.widget.TextView textView, android.widget.FrameLayout frameLayout, android.content.Context context, android.view.ViewGroup viewGroup) {
        textView.setText("加载中…");
        frameLayout.removeAllViews();
        frameLayout.addView(textView, new android.widget.FrameLayout.LayoutParams(-1, -2));
        new java.lang.Thread(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda69(context, frameLayout, textView, viewGroup), "notice-node-retry").start();
    }

    static /* synthetic */ void lambda$fillNoticeNodeList$67(java.lang.String str, android.view.ViewGroup viewGroup, android.view.View view) {
        if (str != null && !str.isEmpty()) {
            sNoticeNodeUrl = str;
        }
        openPage(viewGroup, 11);
    }

    private static java.lang.String fetchJson(java.lang.String str) throws java.lang.Exception {
        java.io.BufferedReader bufferedReader;
        java.net.HttpURLConnection httpURLConnection = (java.net.HttpURLConnection) new java.net.URL(str).openConnection();
        try {
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setReadTimeout(15000);
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode >= 200 && responseCode < 300) {
                bufferedReader = new java.io.BufferedReader(new java.io.InputStreamReader(httpURLConnection.getInputStream(), java.nio.charset.StandardCharsets.UTF_8));
            } else {
                bufferedReader = new java.io.BufferedReader(new java.io.InputStreamReader(httpURLConnection.getErrorStream(), java.nio.charset.StandardCharsets.UTF_8));
            }
            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            while (true) {
                java.lang.String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                sb.append(line);
                sb.append('\n');
            }
            bufferedReader.close();
            if (responseCode < 200 || responseCode >= 300) {
                throw new java.lang.RuntimeException("HTTP " + responseCode);
            }
            java.lang.String string = sb.toString();
            httpURLConnection.disconnect();
            return string;
        } catch (java.lang.Throwable th) {
            httpURLConnection.disconnect();
            throw th;
        }
    }

    private static java.util.List<com.google.gson.JsonObject> parseNotices(java.lang.String str) throws java.lang.Exception {
        java.lang.String asString;
        int asInt;
        com.google.gson.JsonObject asJsonObject = com.google.gson.JsonParser.parseString(str).getAsJsonObject();
        if (asJsonObject.has("code") && (asInt = asJsonObject.get("code").getAsInt()) != 200) {
            throw new java.lang.RuntimeException("code=" + asInt);
        }
        if (asJsonObject.has("msg") && ((asString = asJsonObject.get("msg").getAsString()) == null || !"ok".equals(asString.trim()))) {
            throw new java.lang.RuntimeException("msg=" + asString);
        }
        com.google.gson.JsonElement jsonElement = asJsonObject.get("data");
        java.util.ArrayList<com.google.gson.JsonObject> arrayList = new java.util.ArrayList();
        if (jsonElement != null && jsonElement.isJsonArray()) {
            for (com.google.gson.JsonElement jsonElement2 : jsonElement.getAsJsonArray()) {
                if (jsonElement2 != null && jsonElement2.isJsonObject()) {
                    arrayList.add(jsonElement2.getAsJsonObject());
                }
            }
        } else if (jsonElement != null && jsonElement.isJsonObject()) {
            arrayList.add(jsonElement.getAsJsonObject());
        }
        arrayList.removeIf(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda181());
        arrayList.sort(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda182());
        java.util.ArrayList arrayList2 = new java.util.ArrayList();
        java.util.ArrayList arrayList3 = new java.util.ArrayList();
        for (com.google.gson.JsonObject jsonObject : arrayList) {
            ((jsonObject.has("is_top") && jsonObject.get("is_top").isJsonPrimitive() && jsonObject.get("is_top").getAsBoolean()) ? arrayList2 : arrayList3).add(jsonObject);
        }
        java.util.ArrayList arrayList4 = new java.util.ArrayList(arrayList2);
        arrayList4.addAll(arrayList3);
        return arrayList4;
    }

    static int noticeId(com.google.gson.JsonObject jsonObject) {
        try {
            if (jsonObject.has("id") && jsonObject.get("id").isJsonPrimitive()) {
                return jsonObject.get("id").getAsInt();
            }
            return 0;
        } catch (java.lang.Throwable unused) {
            return 0;
        }
    }

    private static java.lang.String noticeStr(com.google.gson.JsonObject jsonObject, java.lang.String str) {
        try {
            return (jsonObject.has(str) && jsonObject.get(str).isJsonPrimitive()) ? jsonObject.get(str).getAsString() : "";
        } catch (java.lang.Throwable unused) {
            return "";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isForceNotice(com.google.gson.JsonObject jsonObject) {
        return "read".equals(noticeStr(jsonObject, "type"));
    }

    private static int[] installedVersion(android.content.Context context) {
        try {
            int[] version = parseVersion(context.getPackageManager().getPackageInfo("com.tencent.qqlitepro", 0).versionName);
            if (version != null) {
                return version;
            }
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("读取模块版本失败: " + th);
        }
        return MODULE_VER;
    }

    private static boolean isUpdateAvailable(android.content.Context context, java.lang.String str) {
        int[] version = parseVersion(str);
        if (version == null) {
            return false;
        }
        int[] iArrInstalledVersion = installedVersion(context);
        int iMax = java.lang.Math.max(version.length, iArrInstalledVersion.length);
        int i = 0;
        while (i < iMax) {
            int i2 = i < version.length ? version[i] : 0;
            int i3 = i < iArrInstalledVersion.length ? iArrInstalledVersion[i] : 0;
            if (i2 > i3) {
                return true;
            }
            if (i2 < i3) {
                return false;
            }
            i++;
        }
        return false;
    }

    private static int compareVersion(int[] iArr, int[] iArr2) {
        int iMax = java.lang.Math.max(iArr.length, iArr2.length);
        int i = 0;
        while (i < iMax) {
            int i2 = i < iArr.length ? iArr[i] : 0;
            int i3 = i < iArr2.length ? iArr2[i] : 0;
            if (i2 != i3) {
                return i2 - i3;
            }
            i++;
        }
        return 0;
    }

    public static java.lang.String findNewerUpdateVersion(android.content.Context context) {
        java.lang.String strNoticeStr;
        try {
            java.lang.String str = null;
            int[] iArr = null;
            for (com.google.gson.JsonObject jsonObject : parseNotices(fetchJson(effectiveNoticeUrl()))) {
                if ("update".equals(noticeStr(jsonObject, "type")) && jsonObject.has("is_latest") && jsonObject.get("is_latest").isJsonPrimitive() && jsonObject.get("is_latest").getAsBoolean() && (strNoticeStr = noticeStr(jsonObject, com.sun.mail.imap.IMAPStore.ID_VERSION)) != null && !strNoticeStr.isEmpty() && isUpdateAvailable(context, strNoticeStr)) {
                    int[] version = parseVersion(strNoticeStr);
                    if (str == null || (version != null && iArr != null && compareVersion(version, iArr) > 0)) {
                        str = strNoticeStr;
                        iArr = version;
                    }
                }
            }
            return str;
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("检查版本更新公告失败: " + th);
            return null;
        }
    }

    public static java.lang.String officialNoticesJson() {
        try {
            java.util.List<com.google.gson.JsonObject> notices = parseNotices(fetchJson(effectiveNoticeUrl()));
            com.google.gson.JsonArray jsonArray = new com.google.gson.JsonArray();
            java.util.Iterator<com.google.gson.JsonObject> it = notices.iterator();
            while (it.hasNext()) {
                jsonArray.add(it.next());
            }
            return jsonArray.toString();
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("获取官方公告列表失败: " + th);
            return "[]";
        }
    }

    private static int[] parseVersion(java.lang.String str) {
        if (str == null) {
            return null;
        }
        java.lang.String strTrim = str.trim();
        int i = 0;
        while (i < strTrim.length() && !java.lang.Character.isDigit(strTrim.charAt(i))) {
            i++;
        }
        if (i >= strTrim.length()) {
            return null;
        }
        java.lang.String[] strArrSplit = strTrim.substring(i).split("\\.");
        int[] iArr = {0, 0, 0, 0};
        for (int i2 = 0; i2 < strArrSplit.length; i2++) {
            java.lang.String str2 = strArrSplit[i2];
            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            for (char c : str2.toCharArray()) {
                if (!java.lang.Character.isDigit(c)) {
                    break;
                }
                sb.append(c);
            }
            if (sb.length() != 0) {
                int i3 = java.lang.Integer.parseInt(sb.toString());
                if (i2 < 3) {
                    iArr[i2] = i3;
                    if (sb.length() < str2.length()) {
                        iArr[3] = 1;
                    }
                } else {
                    iArr[3] = java.lang.Math.max(iArr[3], i3);
                }
            }
        }
        return iArr;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void downloadAndUpdate(android.content.Context context, java.lang.String str, java.lang.String str2) {
        android.widget.LinearLayout linearLayout = new android.widget.LinearLayout(context);
        linearLayout.setOrientation(1);
        int iDp = dp(context, 20.0f);
        linearLayout.setPadding(iDp, iDp, iDp, 0);
        linearLayout.addView(text(context, "正在下载更新…", TITLE, 14.0f, 8388611), rootLp());
        android.widget.ProgressBar progressBar = new android.widget.ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        progressBar.setMax(100);
        android.widget.LinearLayout.LayoutParams layoutParamsRootLp = rootLp();
        layoutParamsRootLp.topMargin = dp(context, 10.0f);
        progressBar.setLayoutParams(layoutParamsRootLp);
        linearLayout.addView(progressBar, layoutParamsRootLp);
        android.widget.TextView textViewText = text(context, "准备下载…", -6643546, 12.0f, 8388611);
        textViewText.setPadding(0, dp(context, 6.0f), 0, dp(context, 16.0f));
        linearLayout.addView(textViewText, rootLp());
        android.app.AlertDialog alertDialogCreate = new android.app.AlertDialog.Builder(context).setView(linearLayout).setCancelable(false).create();
        try {
            alertDialogCreate.show();
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("更新进度弹窗失败: " + th);
        }
        new java.lang.Thread(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda186(str, str2, progressBar, textViewText, alertDialogCreate, context), "notice-update-download").start();
    }

    static /* synthetic */ void lambda$downloadAndUpdate$73(java.lang.String str, java.lang.String str2, android.widget.ProgressBar progressBar, android.widget.TextView textView, android.app.AlertDialog alertDialog, android.content.Context context) {
        java.net.HttpURLConnection httpURLConnection;
        java.io.File file = null;
        try {
            // 卡顿/路径修复：不再直接使用 /sdcard 硬编码目录（Android 10+ 无权限会 mkdirs 失败→ENOENT）
            java.io.File file2 = com.tencent.qqlitepro.util.Storage.dir(context, "update");
            if (!com.tencent.qqlitepro.util.Storage.ensureWritable(file2)) {
                throw new java.io.IOException("无法创建更新目录: " + file2.getAbsolutePath());
            }
            httpURLConnection = (java.net.HttpURLConnection) new java.net.URL(str).openConnection();
            try {
                httpURLConnection.setConnectTimeout(15000);
                httpURLConnection.setReadTimeout(30000);
                httpURLConnection.setInstanceFollowRedirects(true);
                httpURLConnection.connect();
                int responseCode = httpURLConnection.getResponseCode();
                if (responseCode != 200) {
                    throw new java.lang.RuntimeException("HTTP " + responseCode);
                }
                java.lang.String strUpdateFileName = updateFileName(str, httpURLConnection, str2);
                java.io.File file3 = new java.io.File(file2, strUpdateFileName);
                java.io.File file4 = new java.io.File(file2, strUpdateFileName + ".tmp");
                try {
                    long contentLength = httpURLConnection.getContentLength();
                    java.io.InputStream inputStream = httpURLConnection.getInputStream();
                    java.io.FileOutputStream fileOutputStream = new java.io.FileOutputStream(file4);
                    byte[] bArr = new byte[8192];
                    long j = 0;
                    while (true) {
                        int i = inputStream.read(bArr);
                        if (i <= 0) {
                            break;
                        }
                        fileOutputStream.write(bArr, 0, i);
                        long j2 = j + ((long) i);
                        new android.os.Handler(android.os.Looper.getMainLooper()).post(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda187(contentLength, progressBar, j2, textView));
                        contentLength = contentLength;
                        j = j2;
                        bArr = bArr;
                    }
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    inputStream.close();
                    if (file4.exists() && file4.length() > 0) {
                        if (file3.exists()) {
                            file3.delete();
                        }
                        file4.renameTo(file3);
                    }
                    new android.os.Handler(android.os.Looper.getMainLooper()).post(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda188(alertDialog, context, file3));
                    if (httpURLConnection == null) {
                    }
                } catch (java.lang.Throwable th) {
                    java.lang.Throwable thRethrow = th;
                    file = file4;
                    if (file != null) {
                        try {
                            file.delete();
                        } finally {
                            if (httpURLConnection != null) {
                                try {
                                    httpURLConnection.disconnect();
                                } catch (java.lang.Throwable unused) {
                                }
                            }
                        }
                    }
                    com.tencent.qqlitepro.util.Log.w("更新下载失败: " + thRethrow);
                    new android.os.Handler(android.os.Looper.getMainLooper()).post(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda189(alertDialog, context, thRethrow));
                }
            } catch (java.lang.Throwable th2) {
                java.lang.Throwable thRethrow2 = th2;
            }
        } catch (java.lang.Throwable th3) {
            java.lang.Throwable thRethrow3 = th3;
            httpURLConnection = null;
        }
    }

    static /* synthetic */ void lambda$downloadAndUpdate$70(long j, android.widget.ProgressBar progressBar, long j2, android.widget.TextView textView) {
        if (j > 0) {
            long j3 = 100 * j2;
            try {
                progressBar.setProgress((int) (j3 / j));
                textView.setText("正在下载… " + (j3 / j) + "%  (" + humanSize(j2) + " / " + humanSize(j) + ")");
                return;
            } catch (java.lang.Throwable unused) {
                return;
            }
        }
        textView.setText("正在下载…  " + humanSize(j2));
    }

    static /* synthetic */ void lambda$downloadAndUpdate$71(android.app.AlertDialog alertDialog, android.content.Context context, java.io.File file) {
        try {
            alertDialog.dismiss();
        } catch (java.lang.Throwable unused) {
        }
        toast(context, "下载完成");
        installApk(context, file);
    }

    static /* synthetic */ void lambda$downloadAndUpdate$72(android.app.AlertDialog alertDialog, android.content.Context context, java.lang.Throwable thRethrow3) {
        try {
            alertDialog.dismiss();
        } catch (java.lang.Throwable unused) {
        }
        // 路径修复：把 ENOENT 这类底层错误翻译成用户能照做的提示
        toast(context, describeDownloadError(context, thRethrow3));
    }

    /** 把下载异常翻译成可操作的提示（尤其是 ENOENT / 无写权限） */
    private static java.lang.String describeDownloadError(android.content.Context context, java.lang.Throwable th) {
        java.lang.String raw = th == null ? "" : java.lang.String.valueOf(th.getMessage());
        java.lang.String low = raw.toLowerCase(java.util.Locale.US);
        java.lang.String dir = com.tencent.qqlitepro.util.Storage.root(context);
        if (low.contains("enoent") || low.contains("no such file")) {
            return "下载失败：目录不可写（" + dir + "），请检查存储权限或剩余空间";
        }
        if (low.contains("eacces") || low.contains("permission denied")) {
            return "下载失败：没有存储写入权限，请授予后重试（目标目录 " + dir + "）";
        }
        if (low.contains("enospc") || low.contains("no space")) {
            return "下载失败：存储空间不足";
        }
        return "下载失败: " + raw;
    }

    private static void renderNoticeCards(android.content.Context context, android.view.ViewGroup viewGroup, java.util.List<com.google.gson.JsonObject> list) {
        makeGroupGap(viewGroup);
        for (com.google.gson.JsonObject jsonObject : list) {
            if (jsonObject != null) {
                java.lang.String strNoticeStr = noticeStr(jsonObject, "title");
                java.lang.String strNoticeStr2 = noticeStr(jsonObject, "time");
                noticeStr(jsonObject, "content");
                boolean z = jsonObject.has("is_top") && jsonObject.get("is_top").isJsonPrimitive() && jsonObject.get("is_top").getAsBoolean();
                if (strNoticeStr == null || strNoticeStr.isEmpty()) {
                    strNoticeStr = "（无标题）";
                }
                if (strNoticeStr2 == null || strNoticeStr2.isEmpty()) {
                    strNoticeStr2 = "（时间未知）";
                }
                android.widget.LinearLayout linearLayout = new android.widget.LinearLayout(context);
                linearLayout.setOrientation(1);
                android.graphics.drawable.GradientDrawable gradientDrawable = new android.graphics.drawable.GradientDrawable();
                gradientDrawable.setColor(SURFACE_CONTAINER);
                gradientDrawable.setCornerRadius(dpLite(14.0f));
                linearLayout.setBackground(gradientDrawable);
                linearLayout.setPadding(dp(context, 14.0f), dp(context, 12.0f), dp(context, 14.0f), dp(context, 12.0f));
                android.widget.LinearLayout.LayoutParams layoutParamsRootLp = rootLp();
                layoutParamsRootLp.topMargin = dp(context, 6.0f);
                linearLayout.setLayoutParams(layoutParamsRootLp);
                android.widget.LinearLayout linearLayout2 = new android.widget.LinearLayout(context);
                linearLayout2.setOrientation(0);
                linearLayout2.setGravity(16);
                linearLayout2.setLayoutParams(rootLp());
                linearLayout2.addView(text(context, strNoticeStr, TITLE, 15.0f, 8388611), new android.widget.LinearLayout.LayoutParams(0, -2, 1.0f));
                if (z) {
                    android.widget.TextView textViewText = text(context, " 置顶 ", -11751600, 11.0f, 17);
                    android.graphics.drawable.GradientDrawable gradientDrawable2 = new android.graphics.drawable.GradientDrawable();
                    gradientDrawable2.setColor(575450960);
                    gradientDrawable2.setCornerRadius(dpLite(8.0f));
                    textViewText.setBackground(gradientDrawable2);
                    textViewText.setPadding(dp(context, 6.0f), dp(context, 2.0f), dp(context, 6.0f), dp(context, 2.0f));
                    linearLayout2.addView(textViewText);
                }
                linearLayout.addView(linearLayout2);
                android.widget.TextView textViewText2 = text(context, strNoticeStr2, -6643546, 12.0f, 8388611);
                textViewText2.setPadding(0, dp(context, 3.0f), 0, 0);
                linearLayout.addView(textViewText2, rootLp());
                linearLayout.setClickable(true);
                linearLayout.setOnClickListener(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda142(jsonObject, viewGroup));
                viewGroup.addView(linearLayout);
            }
        }
    }

    static /* synthetic */ void lambda$renderNoticeCards$74(com.google.gson.JsonObject jsonObject, android.view.ViewGroup viewGroup, android.view.View view) {
        sNoticeSelected = jsonObject;
        openPage(viewGroup, 12);
    }

    private static void buildNoticeDetail(android.content.Context context, android.view.ViewGroup viewGroup) {
        java.lang.String strNoticeStr;
        android.widget.LinearLayout linearLayout = new android.widget.LinearLayout(context);
        linearLayout.setOrientation(0);
        linearLayout.setGravity(16);
        linearLayout.setPadding(dp(context, 20.0f), dp(context, 12.0f), dp(context, 20.0f), dp(context, 12.0f));
        linearLayout.setLayoutParams(rootLp());
        linearLayout.setOnClickListener(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda222(viewGroup));
        linearLayout.addView(text(context, "←  返回公告", HINT, 14.0f, 8388611));
        viewGroup.addView(linearLayout);
        com.google.gson.JsonObject jsonObject = sNoticeSelected;
        if (jsonObject == null) {
            android.widget.TextView textViewText = text(context, "（无公告数据）", -6643546, 13.0f, 8388611);
            textViewText.setPadding(dp(context, 20.0f), dp(context, 4.0f), 0, 0);
            viewGroup.addView(textViewText, rootLp());
            return;
        }
        java.lang.String strNoticeStr2 = noticeStr(jsonObject, "title");
        java.lang.String strNoticeStr3 = noticeStr(jsonObject, "time");
        java.lang.String strNoticeStr4 = noticeStr(jsonObject, "content");
        if (strNoticeStr2 == null || strNoticeStr2.isEmpty()) {
            strNoticeStr2 = "（无标题）";
        }
        viewGroup.addView(section(context, strNoticeStr2));
        java.lang.StringBuilder sb = new java.lang.StringBuilder("发布时间：");
        if (strNoticeStr3 == null || strNoticeStr3.isEmpty()) {
            strNoticeStr3 = "（时间未知）";
        }
        sb.append(strNoticeStr3);
        android.widget.TextView textViewText2 = text(context, sb.toString(), -6643546, 13.0f, 8388611);
        textViewText2.setPadding(dp(context, 20.0f), dp(context, 2.0f), 0, 0);
        viewGroup.addView(textViewText2, rootLp());
        boolean zEquals = "plugin".equals(noticeStr(jsonObject, "type"));
        if (strNoticeStr4 == null || strNoticeStr4.isEmpty()) {
            strNoticeStr4 = "（无正文）";
        }
        if (zEquals && (strNoticeStr = noticeStr(jsonObject, "url")) != null && !strNoticeStr.isEmpty() && !strNoticeStr4.contains(strNoticeStr)) {
            strNoticeStr4 = strNoticeStr4 + "\n" + strNoticeStr;
        }
        android.widget.TextView textViewText3 = text(context, strNoticeStr4, TITLE, 14.0f, 8388611);
        textViewText3.setPadding(dp(context, 20.0f), dp(context, 12.0f), dp(context, 20.0f), 0);
        if (zEquals) {
            sPluginNoticeTexts.add(textViewText3);
        }
        textViewText3.setTextIsSelectable(true);
        textViewText3.setLongClickable(true);
        attachLongPressCopy(textViewText3);
        viewGroup.addView(textViewText3, rootLp());
        if ("update".equals(noticeStr(jsonObject, "type"))) {
            java.lang.String strNoticeStr5 = noticeStr(jsonObject, com.sun.mail.imap.IMAPStore.ID_VERSION);
            java.lang.String strNoticeStr6 = noticeStr(jsonObject, "url");
            if (!isUpdateAvailable(context, strNoticeStr5) || strNoticeStr6 == null || strNoticeStr6.isEmpty()) {
                return;
            }
            makeGroupGap(viewGroup);
            java.lang.StringBuilder sb2 = new java.lang.StringBuilder("发现新版本：");
            sb2.append(strNoticeStr5.isEmpty() ? "" : strNoticeStr5);
            android.widget.TextView textViewText4 = text(context, sb2.toString(), -6643546, 13.0f, 8388611);
            textViewText4.setPadding(dp(context, 20.0f), dp(context, 6.0f), 0, dp(context, 4.0f));
            viewGroup.addView(textViewText4, rootLp());
            android.graphics.drawable.GradientDrawable gradientDrawable = new android.graphics.drawable.GradientDrawable();
            gradientDrawable.setColor(-1739917);
            gradientDrawable.setCornerRadius(dpLite(22.0f));
            android.widget.TextView textViewText5 = text(context, "下载并更新", -1, 15.0f, 17);
            textViewText5.setBackground(gradientDrawable);
            textViewText5.setPadding(0, dp(context, 12.0f), 0, dp(context, 12.0f));
            textViewText5.setGravity(17);
            android.widget.LinearLayout.LayoutParams layoutParamsRootLp = rootLp();
            layoutParamsRootLp.leftMargin = dp(context, 20.0f);
            layoutParamsRootLp.rightMargin = dp(context, 20.0f);
            textViewText5.setLayoutParams(layoutParamsRootLp);
            textViewText5.setClickable(true);
            textViewText5.setOnClickListener(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda223(context, strNoticeStr6, strNoticeStr5));
            viewGroup.addView(textViewText5, layoutParamsRootLp);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void handlePluginNoticeLink(android.content.Context context, java.lang.String str) {
        if (context == null) {
            return;
        }
        java.lang.String strTrim = str == null ? "" : str.trim();
        if (strTrim.isEmpty()) {
            return;
        }
        if (!strTrim.matches("(?i)^[a-z][a-z0-9+.\\-]*:.*")) {
            toast(context, "插件链接无效：" + strTrim);
        } else {
            java.io.File file = new java.io.File(pluginDir(context), pluginFileNameFromUrl(strTrim));
            if (file.exists() && file.length() > 0) {
                confirmImportPlugin(context, file);
            } else {
                downloadPluginForNotice(context, strTrim, file);
            }
        }
    }

    private static java.lang.String pluginFileNameFromUrl(java.lang.String str) {
        java.lang.String strValueOf;
        try {
            strValueOf = java.lang.String.valueOf(android.net.Uri.parse(str).getLastPathSegment());
        } catch (java.lang.Throwable unused) {
            strValueOf = "";
        }
        if (strValueOf.isEmpty() || "null".equals(strValueOf)) {
            strValueOf = "plugin_" + java.lang.System.currentTimeMillis() + ".js";
        }
        java.lang.String strReplaceAll = strValueOf.replaceAll("[\\\\/:*?\"<>|]", "_");
        if (strReplaceAll.toLowerCase().endsWith(".js")) {
            return strReplaceAll;
        }
        return strReplaceAll + ".js";
    }

    private static void downloadPluginForNotice(android.content.Context context, java.lang.String str, java.io.File file) {
        android.widget.LinearLayout linearLayout = new android.widget.LinearLayout(context);
        linearLayout.setOrientation(1);
        int iDp = dp(context, 20.0f);
        linearLayout.setPadding(iDp, iDp, iDp, 0);
        linearLayout.addView(text(context, "正在下载插件…", TITLE, 14.0f, 8388611), rootLp());
        android.widget.ProgressBar progressBar = new android.widget.ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        progressBar.setMax(100);
        android.widget.LinearLayout.LayoutParams layoutParamsRootLp = rootLp();
        layoutParamsRootLp.topMargin = dp(context, 10.0f);
        progressBar.setLayoutParams(layoutParamsRootLp);
        linearLayout.addView(progressBar, layoutParamsRootLp);
        android.widget.TextView textViewText = text(context, "准备下载…", -6643546, 12.0f, 8388611);
        textViewText.setPadding(0, dp(context, 6.0f), 0, dp(context, 16.0f));
        linearLayout.addView(textViewText, rootLp());
        android.app.AlertDialog alertDialogCreate = new android.app.AlertDialog.Builder(context).setView(linearLayout).setCancelable(false).create();
        try {
            alertDialogCreate.show();
        } catch (java.lang.Throwable thRethrow3) {
            com.tencent.qqlitepro.util.Log.w("插件下载进度弹窗失败: " + thRethrow3);
        }
        new java.lang.Thread(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda171(file, str, progressBar, textViewText, alertDialogCreate, context), "plugin-notice-download").start();
    }

    static /* synthetic */ void lambda$downloadPluginForNotice$80(java.io.File file, java.lang.String str, android.widget.ProgressBar progressBar, android.widget.TextView textView, android.app.AlertDialog alertDialog, android.content.Context context) {
        java.net.HttpURLConnection httpURLConnection;
        java.io.File file2 = null;
        try {
            java.io.File parentFile = file.getParentFile();
            if (parentFile != null && !parentFile.isDirectory()) {
                parentFile.mkdirs();
            }
            java.io.File file3 = new java.io.File(parentFile, file.getName() + ".tmp");
            try {
                httpURLConnection = (java.net.HttpURLConnection) new java.net.URL(str).openConnection();
                try {
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setConnectTimeout(15000);
                    httpURLConnection.setReadTimeout(30000);
                    httpURLConnection.setInstanceFollowRedirects(true);
                    httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 13) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36");
                    httpURLConnection.connect();
                    int responseCode = httpURLConnection.getResponseCode();
                    if (responseCode != 200) {
                        throw new java.lang.RuntimeException("HTTP " + responseCode);
                    }
                    long contentLength = httpURLConnection.getContentLength();
                    java.io.InputStream inputStream = httpURLConnection.getInputStream();
                    java.io.FileOutputStream fileOutputStream = new java.io.FileOutputStream(file3);
                    byte[] bArr = new byte[8192];
                    long j = 0;
                    while (true) {
                        int i = inputStream.read(bArr);
                        if (i <= 0) {
                            break;
                        }
                        fileOutputStream.write(bArr, 0, i);
                        long j2 = j + ((long) i);
                        new android.os.Handler(android.os.Looper.getMainLooper()).post(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda207(contentLength, progressBar, j2, textView));
                        j = j2;
                        contentLength = contentLength;
                        bArr = bArr;
                    }
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    inputStream.close();
                    if (!file3.exists() || file3.length() <= 0) {
                        throw new java.lang.RuntimeException("下载内容为空");
                    }
                    if (file.exists()) {
                        file.delete();
                    }
                    if (!file3.renameTo(file)) {
                        throw new java.lang.RuntimeException("写入插件文件失败");
                    }
                    new android.os.Handler(android.os.Looper.getMainLooper()).post(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda208(alertDialog, context, file));
                    if (httpURLConnection == null) {
                    }
                } catch (java.lang.Throwable thRethrow3) {
                    file2 = file3;
                    if (file2 != null) {
                        try {
                            file2.delete();
                        } finally {
                            if (httpURLConnection != null) {
                                try {
                                    httpURLConnection.disconnect();
                                } catch (java.lang.Throwable unused) {
                                }
                            }
                        }
                    }
                    com.tencent.qqlitepro.util.Log.w("插件下载失败: " + thRethrow3);
                    new android.os.Handler(android.os.Looper.getMainLooper()).post(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda209(alertDialog, context, thRethrow3));
                }
            } catch (java.lang.Throwable th2) {
                java.lang.Throwable thRethrow4 = th2;
                httpURLConnection = null;
            }
        } catch (java.lang.Throwable th3) {
            java.lang.Throwable thRethrow5 = th3;
            httpURLConnection = null;
        }
    }

    static /* synthetic */ void lambda$downloadPluginForNotice$77(long j, android.widget.ProgressBar progressBar, long j2, android.widget.TextView textView) {
        if (j > 0) {
            long j3 = 100 * j2;
            try {
                progressBar.setProgress((int) (j3 / j));
                textView.setText("正在下载… " + (j3 / j) + "%  (" + humanSize(j2) + " / " + humanSize(j) + ")");
                return;
            } catch (java.lang.Throwable unused) {
                return;
            }
        }
        textView.setText("正在下载…  " + humanSize(j2));
    }

    static /* synthetic */ void lambda$downloadPluginForNotice$78(android.app.AlertDialog alertDialog, android.content.Context context, java.io.File file) {
        try {
            alertDialog.dismiss();
        } catch (java.lang.Throwable unused) {
        }
        confirmImportPlugin(context, file);
    }

    static /* synthetic */ void lambda$downloadPluginForNotice$79(android.app.AlertDialog alertDialog, android.content.Context context, java.lang.Throwable thRethrow5) {
        try {
            alertDialog.dismiss();
        } catch (java.lang.Throwable unused) {
        }
        toast(context, "插件下载失败：" + thRethrow5);
    }

    /**
     * 插件导入详情页。
     *
     * 替代原来的 AlertDialog 简易确认框：现在展示插件名/版本/介绍/文件大小/设置项数量，
     * 底部提供「导入」「取消」两个按钮，风格与模块其它页面一致（ScrollableCard）。
     */
    private static void confirmImportPlugin(final android.content.Context context, final java.io.File file) {
        try {
            // 解析插件元信息（放到后台，避免解析大文件卡住 UI）
            final com.tencent.qqlitepro.hook.SettingsInjector.PluginInfo[] holder = new com.tencent.qqlitepro.hook.SettingsInjector.PluginInfo[1];
            try {
                holder[0] = parsePlugin(readTextFile(file));
            } catch (java.lang.Throwable ignored) {
            }
            final com.tencent.qqlitepro.hook.SettingsInjector.PluginInfo info = holder[0];
            showPluginImportPage(context, file, info);
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("插件导入页打开失败: " + th);
        }
    }

    /** 插件导入详情界面 */
    private static void showPluginImportPage(final android.content.Context context, final java.io.File file,
                                             final com.tencent.qqlitepro.hook.SettingsInjector.PluginInfo info) {
        if (context == null || file == null) {
            return;
        }
        android.widget.LinearLayout root = new android.widget.LinearLayout(context);
        root.setOrientation(1);
        int pad = dp(context, 18.0f);
        root.setPadding(pad, dp(context, 14.0f), pad, dp(context, 16.0f));

        // 标题
        root.addView(text(context, "导入插件", TITLE, 17.0f, 3), rootLp());

        // 插件卡片
        android.widget.LinearLayout card = new android.widget.LinearLayout(context);
        card.setOrientation(1);
        android.graphics.drawable.GradientDrawable bg = new android.graphics.drawable.GradientDrawable();
        bg.setColor(-14013129);
        bg.setCornerRadius(dpLite(12.0f));
        card.setBackground(bg);
        card.setPadding(dp(context, 14.0f), dp(context, 12.0f), dp(context, 14.0f), dp(context, 12.0f));
        android.widget.LinearLayout.LayoutParams cardLp = rootLp();
        cardLp.topMargin = dp(context, 12.0f);
        card.setLayoutParams(cardLp);

        java.lang.String name = (info != null && info.name != null && !info.name.trim().isEmpty())
                ? info.name.trim() : file.getName();
        java.lang.String version = (info != null && info.version != null && !info.version.trim().isEmpty())
                ? info.version.trim() : "未知";
        java.lang.String desc = (info != null && info.desc != null) ? info.desc.trim() : "";

        card.addView(text(context, name, TITLE, 16.0f, 3), rootLp());
        card.addView(text(context, "版本：v" + version, SUB, 12.5f, 3), withTop(context, 6.0f));

        // 介绍
        if (!desc.isEmpty()) {
            android.widget.TextView descView = text(context, desc, SUB, 13.0f, 3);
            descView.setLineSpacing(dp(context, 3.0f), 1.0f);
            card.addView(descView, withTop(context, 10.0f));
        }

        // 元信息
        java.lang.StringBuilder meta = new java.lang.StringBuilder();
        meta.append("• 文件：").append(file.getName()).append('\n');
        meta.append("• 大小：").append(humanSize(file.length())).append('\n');
        if (info != null) {
            int cnt = info.settings == null ? 0 : info.settings.size();
            meta.append("• 设置项：").append(cnt).append(" 个").append('\n');
            meta.append("• 自定义设置：").append(info.hasSettings ? "支持" : "不支持");
        } else {
            meta.append("• 无法解析插件头信息");
        }
        android.widget.TextView metaView = text(context, meta.toString(), HINT, 12.5f, 3);
        metaView.setLineSpacing(dp(context, 4.0f), 1.0f);
        card.addView(metaView, withTop(context, 10.0f));

        root.addView(card, rootLp());

        // 说明
        android.widget.TextView tip = text(context, "导入后将重启 QQMax 使其生效。", HINT, 12.0f, 3);
        tip.setLineSpacing(dp(context, 3.0f), 1.0f);
        root.addView(tip, withTop(context, 12.0f));

        // 按钮区
        android.widget.LinearLayout btnRow = new android.widget.LinearLayout(context);
        btnRow.setOrientation(1);
        android.widget.LinearLayout.LayoutParams btnLp = rootLp();
        btnLp.topMargin = dp(context, 14.0f);
        btnRow.setLayoutParams(btnLp);

        android.view.View importBtn = saveButton(context, "导入", new java.lang.Runnable() {
            @Override
            public void run() {
                try {
                    dismissTopDialog(context);
                } catch (java.lang.Throwable ignored) {
                }
                importPluginFile(context, file);
            }
        });
        btnRow.addView(importBtn);
        root.addView(btnRow, btnLp);

        // 取消
        android.widget.TextView cancel = text(context, "取消", ACCENT, 14.0f, 17);
        cancel.setPadding(0, dp(context, 12.0f), 0, dp(context, 8.0f));
        cancel.setClickable(true);
        cancel.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                try {
                    dismissTopDialog(context);
                } catch (java.lang.Throwable ignored) {
                }
            }
        });
        root.addView(cancel, rootLp());

        showAsDialog(context, root);
    }

    /** 关闭最近弹出的对话框（导入页用） */
    private static void dismissTopDialog(android.content.Context context) {
        android.app.Dialog d = sLastDialog;
        sLastDialog = null;
        if (d != null) {
            try {
                d.dismiss();
            } catch (java.lang.Throwable ignored) {
            }
        }
    }

    private static volatile android.app.Dialog sLastDialog;

    /** 以「卡片式对话框」呈现（非系统 AlertDialog，风格与模块一致） */
    private static void showAsDialog(android.content.Context context, android.view.View content) {
        try {
            android.app.Dialog dialog = new android.app.Dialog(context, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            }
            com.tencent.qqlitepro.hook.SettingsInjector.ScrollableCard card =
                    new com.tencent.qqlitepro.hook.SettingsInjector.ScrollableCard(context, content);
            card.setPadding(0, dp(context, 12.0f), 0, dp(context, 12.0f));
            dialog.setContentView(card);
            if (dialog.getWindow() != null) {
                android.view.WindowManager.LayoutParams attrs = dialog.getWindow().getAttributes();
                attrs.width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.94f);
                dialog.getWindow().setAttributes(attrs);
            }
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();
            sLastDialog = dialog;
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("展示插件导入页失败: " + th);
        }
    }

    private static android.widget.LinearLayout.LayoutParams withTop(android.content.Context context, float f) {
        android.widget.LinearLayout.LayoutParams lp = rootLp();
        lp.topMargin = dp(context, f);
        return lp;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void importPluginFile(android.content.Context context, java.io.File file) {
        new java.lang.Thread(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda91(file, new android.os.Handler(android.os.Looper.getMainLooper()), context), "plugin-notice-import").start();
    }

    static /* synthetic */ void lambda$importPluginFile$85(java.io.File file, android.os.Handler handler, android.content.Context context) {
        try {
            com.tencent.qqlitepro.hook.SettingsInjector.PluginInfo plugin = parsePlugin(readTextFile(file));
            if (plugin == null) {
                handler.post(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda72(context));
            } else {
                removeDupPlugins(pluginDir(context), plugin.name, file.getName());
                handler.post(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda73(context, file.getName(), plugin));
            }
        } catch (java.lang.Throwable thRethrow5) {
            handler.post(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda74(context, thRethrow5));
        }
    }

    static /* synthetic */ void lambda$importPluginFile$83(android.content.Context context, java.lang.String str, com.tencent.qqlitepro.hook.SettingsInjector.PluginInfo pluginInfo) {
        com.tencent.qqlitepro.util.Config.setPluginEnabled(context, str, true);
        renderPluginList(context);
        appendLog(sPluginLog, "[公告导入] 已导入并默认开启: " + pluginInfo.name + " (v" + pluginInfo.version + ")");
        reloadPluginAndRestart(context);
    }

    private static java.io.File audioDir(android.content.Context context) {
        // 路径修复：改用 Storage 解析可写目录，避免 Android 10+ 上 mkdirs 失败导致后续 ENOENT
        java.io.File file = com.tencent.qqlitepro.util.Storage.dir(context, "");
        if (!com.tencent.qqlitepro.util.Storage.ensureWritable(file)) {
            com.tencent.qqlitepro.util.Log.w("创建音频目录失败: " + file.getAbsolutePath());
        }
        return file;
    }

    private static boolean isAudioFile(java.lang.String str) {
        if (str == null) {
            return false;
        }
        java.lang.String lowerCase = str.toLowerCase();
        for (java.lang.String str2 : AUDIO_EXTS) {
            if (lowerCase.endsWith(str2)) {
                return true;
            }
        }
        return false;
    }

    private static java.util.List<java.io.File> listAudioFiles(android.content.Context context) {
        java.util.ArrayList arrayList = new java.util.ArrayList();
        try {
            java.io.File[] fileArrListFiles = audioDir(context).listFiles();
            if (fileArrListFiles == null) {
                return arrayList;
            }
            for (java.io.File file : fileArrListFiles) {
                if (file.isFile() && isAudioFile(file.getName())) {
                    arrayList.add(file);
                }
            }
            arrayList.sort(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda218());
        } catch (java.lang.Throwable thRethrow5) {
            com.tencent.qqlitepro.util.Log.w("扫描音频目录失败: " + thRethrow5);
        }
        return arrayList;
    }

    private static java.lang.String humanSize(long j) {
        if (j >= 1024) {
            return j < 1048576 ? java.lang.String.format(java.util.Locale.US, "%.1f KB", java.lang.Double.valueOf(j / 1024.0d)) : java.lang.String.format(java.util.Locale.US, "%.1f MB", java.lang.Double.valueOf((j / 1024.0d) / 1024.0d));
        }
        return j + " B";
    }

    private static void hookActivityResult(java.lang.Class<?> cls, java.lang.ClassLoader classLoader) {
        java.lang.reflect.Method method;
        try {
            int i = 0;
            java.lang.reflect.Method[] declaredMethods = java.lang.Class.forName("android.app.Activity", false, classLoader).getDeclaredMethods();
            int length = declaredMethods.length;
            while (true) {
                if (i >= length) {
                    method = null;
                    break;
                }
                method = declaredMethods[i];
                if (method.getName().equals("onActivityResult")) {
                    break;
                } else {
                    i++;
                }
            }
            if (method == null) {
                return;
            }
            de.robv.android.xposed.XposedBridge.hookMethod(method, new com.tencent.qqlitepro.hook.SettingsInjector.AnonymousClass9());
            com.tencent.qqlitepro.util.Log.i("已拦截宿主 onActivityResult 接收文件选择结果");
        } catch (java.lang.Throwable thRethrow5) {
            com.tencent.qqlitepro.util.Log.w("hook onActivityResult 失败: " + thRethrow5);
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.SettingsInjector$9, reason: invalid class name */
    static class AnonymousClass9 extends de.robv.android.xposed.XC_MethodHook {
        AnonymousClass9() {
        }

        protected void afterHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            try {
                java.lang.Object obj = methodHookParam.thisObject;
                if (obj instanceof android.app.Activity) {
                    int iIntValue = 0;
                    int iIntValue2 = (methodHookParam.args == null || methodHookParam.args.length <= 0 || !(methodHookParam.args[0] instanceof java.lang.Integer)) ? -1 : ((java.lang.Integer) methodHookParam.args[0]).intValue();
                    if (iIntValue2 == com.tencent.qqlitepro.hook.SettingsInjector.REQ_FILE_PICK || iIntValue2 == com.tencent.qqlitepro.hook.SettingsInjector.REQ_DIR_PICK || iIntValue2 == com.tencent.qqlitepro.hook.SettingsInjector.REQ_PLUGIN_PICK || iIntValue2 == com.tencent.qqlitepro.hook.SettingsInjector.REQ_MAIL_ATTACH || iIntValue2 == com.tencent.qqlitepro.hook.SettingsInjector.REQ_MAIL_IMAGE || iIntValue2 == com.tencent.qqlitepro.hook.SettingsInjector.REQ_MAIL_BG) {
                        if (methodHookParam.args.length > 1 && (methodHookParam.args[1] instanceof java.lang.Integer)) {
                            iIntValue = ((java.lang.Integer) methodHookParam.args[1]).intValue();
                        }
                        java.lang.Object obj2 = methodHookParam.args.length > 2 ? methodHookParam.args[2] : null;
                        if (iIntValue == -1 && (obj2 instanceof android.content.Intent)) {
                            android.net.Uri uriPickUriFromIntent = com.tencent.qqlitepro.hook.SettingsInjector.pickUriFromIntent((android.content.Intent) obj2);
                            if (uriPickUriFromIntent == null) {
                                com.tencent.qqlitepro.util.Log.w("选择器回传缺少 Uri req=" + iIntValue2);
                                return;
                            }
                            com.tencent.qqlitepro.util.Log.i("收到选择器回传 req=" + iIntValue2 + " uri=" + uriPickUriFromIntent);
                            android.app.Activity activity = obj instanceof android.app.Activity ? (android.app.Activity) obj : null;
                            if (iIntValue2 == com.tencent.qqlitepro.hook.SettingsInjector.REQ_DIR_PICK) {
                                com.tencent.qqlitepro.hook.SettingsInjector.handleDirPicked(activity, uriPickUriFromIntent);
                                return;
                            }
                            if (iIntValue2 == com.tencent.qqlitepro.hook.SettingsInjector.REQ_MEDIA_DIR_PICK) {
                                com.tencent.qqlitepro.hook.SettingsInjector.handleMediaDirPicked(activity, uriPickUriFromIntent, sPickKind);
                                return;
                            }
                            if (iIntValue2 == com.tencent.qqlitepro.hook.SettingsInjector.REQ_PLUGIN_PICK) {
                                com.tencent.qqlitepro.hook.SettingsInjector.handlePluginPicked(activity, uriPickUriFromIntent);
                                return;
                            }
                            if (iIntValue2 != com.tencent.qqlitepro.hook.SettingsInjector.REQ_MAIL_ATTACH && iIntValue2 != com.tencent.qqlitepro.hook.SettingsInjector.REQ_MAIL_IMAGE) {
                                if (iIntValue2 == com.tencent.qqlitepro.hook.SettingsInjector.REQ_MAIL_BG) {
                                    com.tencent.qqlitepro.hook.SettingsInjector.handleMailBgPicked(activity, uriPickUriFromIntent);
                                    return;
                                } else {
                                    com.tencent.qqlitepro.hook.SettingsInjector.handleFilePicked(activity, uriPickUriFromIntent);
                                    return;
                                }
                            }
                            com.tencent.qqlitepro.hook.SettingsInjector.handleMailPicked(activity, uriPickUriFromIntent, iIntValue2);
                            return;
                        }
                        com.tencent.qqlitepro.util.Log.w("选择器回传异常 req=" + iIntValue2 + " res=" + iIntValue + " data=" + obj2);
                    }
                }
            } catch (java.lang.Throwable thRethrow5) {
                com.tencent.qqlitepro.util.Log.w("解析文件选择结果失败: " + thRethrow5);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static android.net.Uri pickUriFromIntent(android.content.Intent intent) {
        android.net.Uri uri;
        if (intent == null) {
            return null;
        }
        try {
            android.net.Uri data = intent.getData();
            if (data != null) {
                return data;
            }
        } catch (java.lang.Throwable unused) {
        }
        try {
            android.content.ClipData clipData = intent.getClipData();
            if (clipData != null && clipData.getItemCount() > 0 && (uri = clipData.getItemAt(0).getUri()) != null) {
                return uri;
            }
        } catch (java.lang.Throwable unused2) {
        }
        try {
            android.os.Parcelable parcelableExtra = intent.getParcelableExtra("android.intent.extra.STREAM");
            if (parcelableExtra instanceof android.net.Uri) {
                return (android.net.Uri) parcelableExtra;
            }
            if (parcelableExtra instanceof java.util.ArrayList) {
                java.util.List list = (java.util.List) parcelableExtra;
                if (!list.isEmpty() && (list.get(0) instanceof android.net.Uri)) {
                    return (android.net.Uri) list.get(0);
                }
            }
            return null;
        } catch (java.lang.Throwable unused3) {
        }
        return null;
    }

    private static void buildFileSend(android.content.Context context, android.view.ViewGroup viewGroup) {
        viewGroup.addView(backRow(context));
        viewGroup.addView(section(context, "发送文件"));
        sFileLog = null;
        if (context instanceof android.app.Activity) {
            sFileHost = (android.app.Activity) context;
        }
        android.widget.LinearLayout linearLayout = new android.widget.LinearLayout(context);
        linearLayout.setOrientation(1);
        android.graphics.drawable.GradientDrawable gradientDrawable = new android.graphics.drawable.GradientDrawable();
        gradientDrawable.setShape(0);
        gradientDrawable.setColor(SURFACE_CONTAINER);
        gradientDrawable.setCornerRadius(dpLite(14.0f));
        linearLayout.setBackground(gradientDrawable);
        linearLayout.setPadding(dp(context, 12.0f), dp(context, 14.0f), dp(context, 12.0f), dp(context, 14.0f));
        linearLayout.setLayoutParams(rootLp());
        android.widget.TextView textViewText = text(context, "尚未选择文件（可发送任意格式）", -6643546, 13.0f, 8388611);
        linearLayout.addView(textViewText, rootLp());
        viewGroup.addView(linearLayout);
        makeGroupGap(viewGroup);
        viewGroup.addView(saveButton(context, "选择文件", new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda61()));
        makeGroupGap(viewGroup);
        android.widget.LinearLayout linearLayout2 = new android.widget.LinearLayout(context);
        linearLayout2.setOrientation(1);
        android.graphics.drawable.GradientDrawable gradientDrawable2 = new android.graphics.drawable.GradientDrawable();
        gradientDrawable2.setShape(0);
        gradientDrawable2.setColor(SURFACE_CONTAINER);
        gradientDrawable2.setCornerRadius(dpLite(14.0f));
        linearLayout2.setBackground(gradientDrawable2);
        linearLayout2.setPadding(dp(context, 12.0f), dp(context, 14.0f), dp(context, 12.0f), dp(context, 14.0f));
        linearLayout2.setLayoutParams(rootLp());
        android.widget.TextView textViewText2 = text(context, "尚未选择会话", -6643546, 13.0f, 8388611);
        linearLayout2.addView(textViewText2, rootLp());
        viewGroup.addView(linearLayout2);
        makeGroupGap(viewGroup);
        viewGroup.addView(saveButton(context, "选择会话", new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda62(context, textViewText2)));
        makeGroupGap(viewGroup);
        android.widget.LinearLayout linearLayout3 = new android.widget.LinearLayout(context);
        linearLayout3.setOrientation(1);
        android.graphics.drawable.GradientDrawable gradientDrawable3 = new android.graphics.drawable.GradientDrawable();
        gradientDrawable3.setShape(0);
        gradientDrawable3.setColor(SURFACE_CONTAINER);
        gradientDrawable3.setCornerRadius(dpLite(14.0f));
        linearLayout3.setBackground(gradientDrawable3);
        linearLayout3.setPadding(dp(context, 12.0f), dp(context, 14.0f), dp(context, 12.0f), dp(context, 10.0f));
        linearLayout3.setLayoutParams(rootLp());
        viewGroup.addView(linearLayout3);
        linearLayout3.addView(text(context, "发送传输模式", TITLE, 14.0f, 8388611), rootLp());
        int[] iArr = {com.tencent.qqlitepro.util.Config.sendMode(context)};
        android.widget.TextView textViewText3 = text(context, "", -6643546, 12.0f, 8388611);
        textViewText3.setPadding(0, dp(context, 2.0f), 0, dp(context, 6.0f));
        linearLayout3.addView(textViewText3, rootLp());
        java.util.ArrayList arrayList = new java.util.ArrayList();
        com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda63 settingsInjector$$ExternalSyntheticLambda63 = new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda63(iArr, textViewText3, arrayList);
        android.widget.LinearLayout.LayoutParams layoutParams = new android.widget.LinearLayout.LayoutParams(-1, -2);
        layoutParams.topMargin = dp(context, 2.0f);
        com.tencent.qqlitepro.hook.SettingsInjector.RenderModeRow renderModeRow = new com.tencent.qqlitepro.hook.SettingsInjector.RenderModeRow(context, "伪图片", 0, linearLayout3, iArr, arrayList, textViewText3, settingsInjector$$ExternalSyntheticLambda63);
        com.tencent.qqlitepro.hook.SettingsInjector.RenderModeRow renderModeRow2 = new com.tencent.qqlitepro.hook.SettingsInjector.RenderModeRow(context, "伪语音", 1, linearLayout3, iArr, arrayList, textViewText3, settingsInjector$$ExternalSyntheticLambda63);
        linearLayout3.addView(renderModeRow.row, layoutParams);
        linearLayout3.addView(renderModeRow2.row, layoutParams);
        settingsInjector$$ExternalSyntheticLambda63.run();
        makeGroupGap(viewGroup);
        makeGroupGap(viewGroup);
        viewGroup.addView(switchRow(context, "允许文件分片发送", "关闭：伪图片>20MB/伪语音>12MB时拦截并提示；开启：自动分片依次发送", com.tencent.qqlitepro.util.Config.shardEnabled(context), new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda64(context)));
        makeGroupGap(viewGroup);
        makeGroupGap(viewGroup);
        android.widget.LinearLayout linearLayout4 = new android.widget.LinearLayout(context);
        linearLayout4.setOrientation(0);
        linearLayout4.setGravity(16);
        linearLayout4.setLayoutParams(rootLp());
        android.widget.TextView textViewText4 = text(context, "取消", BTN_TEXT, 15.0f, 17);
        android.graphics.drawable.GradientDrawable gradientDrawable4 = new android.graphics.drawable.GradientDrawable();
        gradientDrawable4.setShape(0);
        gradientDrawable4.setColor(BTN_FILL);
        gradientDrawable4.setCornerRadius(dpLite(14.0f));
        textViewText4.setBackground(gradientDrawable4);
        textViewText4.setPadding(dp(context, 16.0f), dp(context, 13.0f), dp(context, 16.0f), dp(context, 13.0f));
        android.widget.LinearLayout.LayoutParams layoutParams2 = new android.widget.LinearLayout.LayoutParams(0, -2, 1.0f);
        layoutParams2.rightMargin = dp(context, 6.0f);
        textViewText4.setLayoutParams(layoutParams2);
        textViewText4.setClickable(true);
        textViewText4.setOnClickListener(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda65());
        linearLayout4.addView(textViewText4);
        android.widget.TextView textViewText5 = text(context, "发送", BTN_TEXT, 15.0f, 17);
        android.graphics.drawable.GradientDrawable gradientDrawable5 = new android.graphics.drawable.GradientDrawable();
        gradientDrawable5.setShape(0);
        gradientDrawable5.setColor(-14721222);
        gradientDrawable5.setCornerRadius(dpLite(14.0f));
        textViewText5.setBackground(gradientDrawable5);
        textViewText5.setPadding(dp(context, 16.0f), dp(context, 13.0f), dp(context, 16.0f), dp(context, 13.0f));
        android.widget.LinearLayout.LayoutParams layoutParams3 = new android.widget.LinearLayout.LayoutParams(0, -2, 1.0f);
        layoutParams3.leftMargin = dp(context, 6.0f);
        textViewText5.setLayoutParams(layoutParams3);
        textViewText5.setClickable(true);
        textViewText5.setOnClickListener(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda66(context));
        linearLayout4.addView(textViewText5);
        viewGroup.addView(linearLayout4);
        sFileUiRefresh = new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda67(textViewText);
        makeGroupGap(viewGroup);
        android.widget.LinearLayout linearLayout5 = new android.widget.LinearLayout(context);
        linearLayout5.setOrientation(1);
        android.graphics.drawable.GradientDrawable gradientDrawable6 = new android.graphics.drawable.GradientDrawable();
        gradientDrawable6.setShape(0);
        gradientDrawable6.setColor(-15461356);
        gradientDrawable6.setCornerRadius(dpLite(12.0f));
        gradientDrawable6.setStroke(1, -13743516);
        linearLayout5.setBackground(gradientDrawable6);
        linearLayout5.setPadding(dp(context, 4.0f), dp(context, 8.0f), dp(context, 4.0f), dp(context, 6.0f));
        android.widget.LinearLayout.LayoutParams layoutParamsRootLp = rootLp();
        layoutParamsRootLp.leftMargin = dp(context, 12.0f);
        layoutParamsRootLp.rightMargin = dp(context, 12.0f);
        linearLayout5.setLayoutParams(layoutParamsRootLp);
        android.widget.ScrollView scrollView = new android.widget.ScrollView(context);
        scrollView.setLayoutParams(new android.widget.LinearLayout.LayoutParams(-1, dp(context, 200.0f)));
        android.widget.LinearLayout linearLayout6 = new android.widget.LinearLayout(context);
        linearLayout6.setOrientation(1);
        linearLayout6.setPadding(dp(context, 8.0f), dp(context, 4.0f), dp(context, 8.0f), dp(context, 4.0f));
        scrollView.addView(linearLayout6, rootLp());
        android.widget.TextView textViewText6 = text(context, "", -7288592, 12.0f, 8388611);
        sFileLog = textViewText6;
        textViewText6.setPadding(dp(context, 8.0f), dp(context, 4.0f), dp(context, 8.0f), dp(context, 4.0f));
        sFileLog.setMovementMethod(new android.text.method.ScrollingMovementMethod());
        linearLayout6.addView(sFileLog, rootLp());
        linearLayout5.addView(scrollView, rootLp());
        addLogTitleRow(context, linearLayout5, "运行日志", sFileLog);
        viewGroup.addView(linearLayout5);
        appendLog(sFileLog, "请先点击“选择文件”选择要发送的文件");
    }

    static /* synthetic */ void lambda$buildFileSend$89(android.content.Context context, android.widget.TextView textView) {
        restoreReplySelection("");
        showSessionPicker(context, new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda23(textView, context));
    }

    static /* synthetic */ void lambda$buildFileSend$88(android.widget.TextView textView, android.content.Context context) {
        textView.setText(buildPeersString().isEmpty() ? "不选会话" : buildPeersString());
        android.widget.TextView textView2 = sFileLog;
        java.lang.StringBuilder sb = new java.lang.StringBuilder("[会话] 已选 ");
        java.util.Set<java.lang.String> set = sSelectedKeys;
        sb.append(set.size());
        sb.append(" 个会话");
        appendLog(textView2, sb.toString());
        toast(context, "已选择 " + set.size() + " 个会话");
    }

    static /* synthetic */ void lambda$buildFileSend$90(int[] iArr, android.widget.TextView textView, java.util.ArrayList arrayList) {
        int i = iArr[0];
        textView.setText(i == 0 ? "默认：伪图片（文件装入合法PNG中走图片通道，原图上传）" : "伪语音（文件装入带魔数头的 .amr 走语音通道）");
        int i2 = 0;
        while (i2 < arrayList.size()) {
            boolean z = (i2 == 0 ? 0 : 1) == i;
            android.widget.TextView textView2 = (android.widget.TextView) arrayList.get(i2);
            textView2.setText(z ? "◉" : "○");
            textView2.setTextColor(z ? -13734992 : -7694426);
            i2++;
        }
    }

    static /* synthetic */ void lambda$buildFileSend$92(android.view.View view) {
        sFileChosen = null;
        sFileMd5 = null;
        appendLog(sFileLog, "[取消] 已清空当前选择");
        java.lang.Runnable runnable = sFileUiRefresh;
        if (runnable != null) {
            runnable.run();
        }
    }

    static /* synthetic */ void lambda$buildFileSend$94(android.widget.TextView textView) {
        java.io.File file = sFileChosen;
        if (file == null) {
            textView.setText("尚未选择文件（可发送任意格式）");
            return;
        }
        java.lang.String strFileExt = fileExt(file.getName());
        java.lang.StringBuilder sb = new java.lang.StringBuilder("文件名：");
        sb.append(sFileChosen.getName());
        sb.append("\n格式：");
        sb.append(strFileExt);
        sb.append("\n文件MD5：");
        java.lang.String str = sFileMd5;
        if (str == null) {
            str = "";
        }
        sb.append(str);
        sb.append("\n文件大小：");
        sb.append(humanSize(sFileChosen.length()));
        textView.setText(sb.toString());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void launchFilePicker() {
        android.app.Activity activity = sFileHost;
        if (activity == null) {
            toast(activity, "未获取宿主 Activity，无法调起选择器");
            return;
        }
        try {
            android.content.Intent intent = new android.content.Intent("android.intent.action.GET_CONTENT");
            intent.setType("*/*");
            intent.addCategory("android.intent.category.OPENABLE");
            activity.startActivityForResult(android.content.Intent.createChooser(intent, "选择要发送的文件"), REQ_FILE_PICK);
            appendLog(sFileLog, "[选择] 已调起系统文件选择器");
        } catch (java.lang.Throwable thRethrow5) {
            com.tencent.qqlitepro.util.Log.w("调起文件选择器失败: " + thRethrow5);
            appendLog(sFileLog, "[选择] 失败：" + thRethrow5);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void handleFilePicked(android.app.Activity activity, android.net.Uri uri) {
        if (uri == null) {
            return;
        }
        appendLog(sFileLog, "[选择] 收到文件选择结果");
        new java.lang.Thread(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda129(activity, uri, audioDir(activity != null ? activity : sFileHost)), "file-pick").start();
    }

    static /* synthetic */ void lambda$handleFilePicked$97(android.app.Activity activity, android.net.Uri uri, java.io.File file) {
        java.io.InputStream inputStreamOpenInputStream;
        try {
            java.lang.String strUriDisplayName = uriDisplayName(activity, uri);
            if (strUriDisplayName == null || strUriDisplayName.isEmpty()) {
                strUriDisplayName = "file_" + java.lang.System.currentTimeMillis();
            }
            java.lang.String strReplaceAll = strUriDisplayName.replaceAll("[\\\\\\\\/:*?\"<>|]", "_");
            java.io.File file2 = new java.io.File(file, strReplaceAll);
            java.io.File file3 = new java.io.File(file, strReplaceAll + ".tmp");
            java.security.MessageDigest messageDigest = java.security.MessageDigest.getInstance("MD5");
            if ("file".equals(uri.getScheme())) {
                inputStreamOpenInputStream = new java.io.FileInputStream(new java.io.File(uri.getPath()));
            } else {
                inputStreamOpenInputStream = activity != null ? activity.getContentResolver().openInputStream(uri) : null;
            }
            if (inputStreamOpenInputStream == null) {
                throw new java.lang.RuntimeException("无法读取所选文件(uri)");
            }
            java.io.FileOutputStream fileOutputStream = new java.io.FileOutputStream(file3);
            try {
                byte[] bArr = new byte[8192];
                while (true) {
                    int i = inputStreamOpenInputStream.read(bArr);
                    if (i > 0) {
                        fileOutputStream.write(bArr, 0, i);
                        messageDigest.update(bArr, 0, i);
                    } else {
                        try {
                            break;
                        } catch (java.lang.Throwable unused) {
                        }
                    }
                }
                inputStreamOpenInputStream.close();
                fileOutputStream.flush();
                fileOutputStream.close();
                if (file2.exists()) {
                    file2.delete();
                }
                file3.renameTo(file2);
                java.lang.StringBuilder sb = new java.lang.StringBuilder();
                for (byte b : messageDigest.digest()) {
                    sb.append(java.lang.String.format("%02x", java.lang.Byte.valueOf(b)));
                }
                new android.os.Handler(android.os.Looper.getMainLooper()).post(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda140(file2, sb.toString()));
            } catch (java.lang.Throwable thRethrow5) {
                try {
                    inputStreamOpenInputStream.close();
                } catch (java.lang.Throwable unused2) {
                }
                throw thRethrow5;
            }
        } catch (java.lang.Throwable th2) {
            java.lang.String strValueOf = java.lang.String.valueOf(th2);
            com.tencent.qqlitepro.util.Log.w("处理选择文件失败: " + strValueOf);
            new android.os.Handler(android.os.Looper.getMainLooper()).post(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda141(strValueOf));
        }
    }

    static /* synthetic */ void lambda$handleFilePicked$95(java.io.File file, java.lang.String str) {
        sFileChosen = file;
        sFileMd5 = str;
        appendLog(sFileLog, "[选择] " + file.getName() + " (" + humanSize(file.length()) + ") MD5=" + str);
        java.lang.Runnable runnable = sFileUiRefresh;
        if (runnable != null) {
            runnable.run();
        }
    }

    private static java.lang.String uriDisplayName(android.content.Context context, android.net.Uri uri) {
        java.lang.String lastPathSegment;
        android.database.Cursor cursorQuery;
        int columnIndex;
        java.lang.String string;
        java.lang.String str = null;
        try {
            if (uri.getScheme() != null && "content".equals(uri.getScheme()) && context != null && (cursorQuery = context.getContentResolver().query(uri, null, null, null, null)) != null) {
                try {
                    if (cursorQuery.moveToFirst()) {
                        for (java.lang.String str2 : cursorQuery.getColumnNames()) {
                            if (("_display_name".equalsIgnoreCase(str2) || "display_name".equalsIgnoreCase(str2)) && (columnIndex = cursorQuery.getColumnIndex(str2)) >= 0 && (string = cursorQuery.getString(columnIndex)) != null && !string.isEmpty()) {
                                str = string;
                                break;
                            }
                        }
                    }
                    cursorQuery.close();
                } catch (java.lang.Throwable thRethrow5) {
                    try {
                        cursorQuery.close();
                    } catch (java.lang.Throwable unused) {
                    }
                    throw thRethrow5;
                }
            }
        } catch (java.lang.Throwable unused2) {
        }
        return ((str == null || str.isEmpty()) && (lastPathSegment = uri.getLastPathSegment()) != null && lastPathSegment.indexOf(46) >= 0) ? lastPathSegment : str;
    }

    private static java.lang.String fileExt(java.lang.String str) {
        if (str == null) {
            return "未知";
        }
        int iLastIndexOf = str.lastIndexOf(46);
        return (iLastIndexOf <= 0 || iLastIndexOf == str.length() + (-1)) ? "无扩展名" : str.substring(iLastIndexOf + 1).toUpperCase(java.util.Locale.US);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void sendFilesToSession(android.content.Context context) {
        java.io.File file = sFileChosen;
        if (file == null || !file.exists()) {
            toast(context, "请先选择文件");
            appendLog(sFileLog, "[发送] 未选择文件");
            return;
        }
        java.util.Set<java.lang.String> set = sSelectedKeys;
        if (set == null || set.isEmpty()) {
            toast(context, "请先选择会话");
            appendLog(sFileLog, "[发送] 未选择会话");
            return;
        }
        if (sFileSending) {
            toast(context, "正在发送中，请勿重复点击");
            return;
        }
        sFileSending = true;
        java.lang.String absolutePath = sFileChosen.getAbsolutePath();
        java.util.ArrayList arrayList = new java.util.ArrayList(set);
        android.os.Handler handler = new android.os.Handler(android.os.Looper.getMainLooper());
        long jCurrentTimeMillis = java.lang.System.currentTimeMillis();
        appendLog(sFileLog, "────────────────");
        appendLog(sFileLog, "[发送] " + sFileChosen.getName() + " (" + com.tencent.qqlitepro.http.FakeVoice.formatSize(sFileChosen.length()) + ") -> " + arrayList.size() + " 个会话");
        new java.lang.Thread(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda176(context, handler, absolutePath, arrayList, jCurrentTimeMillis), "file-send").start();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r7v16, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r7v8 */
    static /* synthetic */ void lambda$sendFilesToSession$103(android.content.Context context, android.os.Handler handler, java.lang.String str, java.util.List list, long j) throws java.lang.Throwable {
        android.content.Context context2;
        android.os.Handler handler2;
        boolean z;
        java.lang.String chatType = null;
        java.util.List listPrepareFakeVoices;
        java.lang.String str2;
        int i;
        int i2;
        boolean z2;
        int i3 = 0;
        try {
            try {
                boolean z3 = com.tencent.qqlitepro.util.Config.sendMode(context) == 0;
                boolean zShardEnabled = com.tencent.qqlitepro.util.Config.shardEnabled(context);
                long j2 = z3 ? com.tencent.qqlitepro.http.FakeImage.SHARD_BYTES : 12582912L;
                long length = sFileChosen.length();
                java.lang.String str3 = "伪图片";
                if (length > j2 && !zShardEnabled) {
                    java.lang.String size = com.tencent.qqlitepro.http.FakeVoice.formatSize(j2);
                    java.lang.String size2 = com.tencent.qqlitepro.http.FakeVoice.formatSize(length);
                    java.lang.StringBuilder sb = new java.lang.StringBuilder();
                    if (!z3) {
                        str3 = "伪语音";
                    }
                    sb.append(str3);
                    sb.append("模式文件超过 ");
                    sb.append(size);
                    sb.append("（当前 ");
                    sb.append(size2);
                    sb.append("），请开启“允许文件分片发送”后重试");
                    handler.post(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda199(context, sb.toString()));
                    sFileSending = false;
                    return;
                }
                if (z3) {
                    listPrepareFakeVoices = com.tencent.qqlitepro.http.FakeImage.prepareFakeImages(str, zShardEnabled);
                } else {
                    listPrepareFakeVoices = com.tencent.qqlitepro.http.FakeVoice.prepareFakeVoices(str, zShardEnabled);
                }
                java.util.List list2 = listPrepareFakeVoices;
                if (list2 != null) {
                    try {
                        if (!list2.isEmpty()) {
                            int size3 = list2.size();
                            android.widget.TextView textView = sFileLog;
                            java.lang.StringBuilder sb2 = new java.lang.StringBuilder("[发送] 模式：");
                            if (!z3) {
                                str3 = "伪语音";
                            }
                            sb2.append(str3);
                            int i4 = 3;
                            if (size3 > 1) {
                                str2 = "，共 " + size3 + " 分片（并发度 " + java.lang.Math.min(3, size3) + "）";
                            } else {
                                str2 = "，单条发送";
                            }
                            sb2.append(str2);
                            appendLog(textView, sb2.toString());
                            try {
                                int size4 = list2.size();
                                int i5 = 0;
                                int i6 = 0;
                                while (i5 < list.size()) {
                                    java.lang.String str4 = (java.lang.String) list.get(i5);
                                    java.lang.StringBuilder sb3 = new java.lang.StringBuilder();
                                    sb3.append("[会话 ");
                                    int i7 = i5 + 1;
                                    sb3.append(i7);
                                    sb3.append("/");
                                    sb3.append(list.size());
                                    sb3.append("] ");
                                    sb3.append(str4);
                                    java.lang.String string = sb3.toString();
                                    int iIndexOf = str4.indexOf(58);
                                    if (iIndexOf <= 0) {
                                        appendLog(sFileLog, string + " 格式错误，跳过");
                                        i2 = i7;
                                        z2 = z3;
                                    } else {
                                        try {
                                            i = java.lang.Integer.parseInt(str4.substring(i3, iIndexOf));
                                        } catch (java.lang.Throwable unused) {
                                            i = 2;
                                        }
                                        chatType = str4.substring(iIndexOf + 1);
                                        appendLog(sFileLog, string);
                                        try {
                                            java.lang.Object objCreateContact = com.tencent.qqlitepro.http.QqKernel.createContact(i, chatType);
                                            java.util.concurrent.ExecutorService executorServiceNewFixedThreadPool = java.util.concurrent.Executors.newFixedThreadPool(java.lang.Math.min(i4, size4));
                                            java.util.ArrayList arrayList = new java.util.ArrayList();
                                            int i8 = 0;
                                            while (i8 < size4) {
                                                int i9 = i8;
                                                java.util.ArrayList arrayList2 = arrayList;
                                                java.util.concurrent.ExecutorService executorService = executorServiceNewFixedThreadPool;
                                                java.lang.String str5 = str4;
                                                arrayList2.add(executorService.submit(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda202(i9, list2, z3, handler, objCreateContact, size4, str5)));
                                                i8 = i9 + 1;
                                                arrayList = arrayList2;
                                                executorServiceNewFixedThreadPool = executorService;
                                                i7 = i7;
                                                str4 = str5;
                                                z3 = z3;
                                                i8++;
                                            }
                                            java.util.concurrent.ExecutorService executorService2 = executorServiceNewFixedThreadPool;
                                            i2 = i7;
                                            z2 = z3;
                                            java.util.Iterator it = arrayList.iterator();
                                            int i10 = 0;
                                            while (it.hasNext()) {
                                                try {
                                                    try {
                                                        if (((int[]) ((java.util.concurrent.Future) it.next()).get())[0] == 1) {
                                                            i10++;
                                                        }
                                                    } catch (java.lang.Throwable thRethrow5) {
                                                        com.tencent.qqlitepro.util.Log.w("等待分片结果异常: " + thRethrow5);
                                                    }
                                                } catch (java.lang.Throwable th2) {
                                                    java.lang.Throwable thRethrow6 = th2;
                                                }
                                            }
                                            executorService2.shutdown();
                                            if (i10 == size4) {
                                                i6++;
                                                appendLog(sFileLog, "  完成 " + i10 + "/" + size4);
                                            } else {
                                                appendLog(sFileLog, "  本会话仅 " + i10 + "/" + size4 + " 分片成功，可重新发送补齐");
                                            }
                                        } catch (java.lang.Throwable th3) {
                                            i2 = i7;
                                            z2 = z3;
                                            com.tencent.qqlitepro.util.Log.w("创建会话联系人失败 " + str4 + ": " + th3);
                                            android.widget.TextView textView2 = sFileLog;
                                            java.lang.StringBuilder sb4 = new java.lang.StringBuilder();
                                            sb4.append("  ✖ 创建会话失败：");
                                            sb4.append(th3);
                                            appendLog(textView2, sb4.toString());
                                        }
                                    }
                                    i5 = i2;
                                    z3 = z2;
                                    i4 = 3;
                                    i3 = 0;
                                }
                                handler.post(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda203(i6, list, (java.lang.System.currentTimeMillis() - j) / 1000.0d, context));
                                sFileSending = false;
                                return;
                            } catch (java.lang.Throwable th4) {
                                java.lang.Throwable thRethrow7 = th4;
                                chatType = null;
                            }
                        }
                    } catch (java.lang.Throwable th5) {
                        com.tencent.qqlitepro.util.Log.w("文件封装失败: " + th5);
                        handler.post(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda201(context, th5));
                        sFileSending = false;
                        return;
                    }
                }
                handler2 = handler;
                z = false;
                try {
                    context2 = context;
                    try {
                        handler2.post(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda200(context2, z3));
                        sFileSending = false;
                        return;
                    } catch (java.lang.Throwable th6) {
                        java.lang.Throwable thRethrow9 = th6;
                    }
                } catch (java.lang.Throwable th7) {
                    java.lang.Throwable thRethrow10 = th7;
                    context2 = context;
                }
            } catch (java.lang.Throwable th8) {
                java.lang.Throwable thRethrow11 = th8;
            }
        } catch (java.lang.Throwable th9) {
            com.tencent.qqlitepro.util.Log.w("发送文件异常: " + th9);
            sFileSending = false;
        }
    }

    static /* synthetic */ void lambda$sendFilesToSession$98(android.content.Context context, java.lang.String str) {
        toast(context, str);
        appendLog(sFileLog, "[发送] 超限拦截：" + str);
    }

    static /* synthetic */ void lambda$sendFilesToSession$99(android.content.Context context, boolean z) {
        toast(context, "文件封装失败");
        android.widget.TextView textView = sFileLog;
        java.lang.StringBuilder sb = new java.lang.StringBuilder("[发送] ");
        sb.append(z ? "伪图片" : "伪语音");
        sb.append("封装失败");
        appendLog(textView, sb.toString());
    }

    static /* synthetic */ void lambda$sendFilesToSession$100(android.content.Context context, java.lang.Throwable thRethrow12) {
        toast(context, "发送失败：" + thRethrow12);
        appendLog(sFileLog, "[发送] 封装失败：" + thRethrow12);
    }

    static /* synthetic */ int[] lambda$sendFilesToSession$101(int i, java.util.List list, boolean z, android.os.Handler handler, java.lang.Object obj, int i2, java.lang.String str) throws java.lang.Exception {
        long length;
        java.lang.String string;
        try {
            long jMin = ((long) java.lang.Math.min(i, 2)) * 350;
            if (jMin > 0) {
                java.lang.Thread.sleep(jMin);
            }
        } catch (java.lang.Throwable unused) {
        }
        java.lang.Object obj2 = list.get(i);
        if (z) {
            length = ((com.tencent.qqlitepro.http.FakeImage.Wrapped) obj2).pngFile.length();
        } else {
            length = ((com.tencent.qqlitepro.http.FakeVoice.Wrapped) obj2).amrFile.length();
        }
        java.lang.String size = com.tencent.qqlitepro.http.FakeVoice.formatSize(length);
        java.lang.String str2 = "未知错误";
        int i3 = 1;
        while (i3 <= 3) {
            java.lang.Object objBuildFakeElement = buildFakeElement(handler, z, obj2);
            if (objBuildFakeElement == null) {
                if (i3 < 3) {
                    try {
                        java.lang.Thread.sleep(800L);
                    } catch (java.lang.Throwable unused2) {
                    }
                }
                string = "元素构造失败";
            } else {
                java.util.ArrayList arrayList = new java.util.ArrayList();
                arrayList.add(objBuildFakeElement);
                com.tencent.qqlitepro.http.QqKernel.SendResult sendResultSendMsg = com.tencent.qqlitepro.http.QqKernel.sendMsg(obj, arrayList, 120000L);
                java.lang.String string2 = "";
                if (sendResultSendMsg != null && sendResultSendMsg.code == 0) {
                    if (i3 > 1) {
                        java.lang.StringBuilder sb = new java.lang.StringBuilder("（重试 ");
                        sb.append(i3 - 1);
                        sb.append(" 次后成功）");
                        string2 = sb.toString();
                    }
                    appendLog(sFileLog, "  ✔ 分片 " + (i + 1) + "/" + i2 + " (" + size + ")" + string2);
                    return new int[]{1, i3};
                }
                if (sendResultSendMsg == null) {
                    string = "发送超时/无响应";
                } else {
                    java.lang.StringBuilder sb2 = new java.lang.StringBuilder("code=");
                    sb2.append(sendResultSendMsg.code);
                    sb2.append(" ");
                    sb2.append(sendResultSendMsg.msg != null ? sendResultSendMsg.msg : "");
                    string = sb2.toString();
                }
                com.tencent.qqlitepro.util.Log.w("文件发送 " + str + " 分片" + (i + 1) + "/" + i2 + " 第" + i3 + "次失败: " + string);
                if (i3 < 3) {
                    try {
                        java.lang.Thread.sleep(800L);
                    } catch (java.lang.Throwable unused3) {
                    }
                }
            }
            i3++;
            str2 = string;
        }
        appendLog(sFileLog, "  ✖ 分片 " + (i + 1) + "/" + i2 + " (" + size + ")：" + str2);
        return new int[]{0, 3};
    }

    static /* synthetic */ void lambda$sendFilesToSession$102(int i, java.util.List list, double d, android.content.Context context) {
        android.widget.TextView textView = sFileLog;
        java.lang.StringBuilder sb = new java.lang.StringBuilder("[完成] ");
        sb.append(i == list.size() ? "全部发送成功" : "部分会话失败");
        sb.append("：");
        sb.append(i);
        sb.append("/");
        sb.append(list.size());
        sb.append(" 会话，用时 ");
        sb.append(java.lang.String.format(java.util.Locale.US, "%.1f", java.lang.Double.valueOf(d)));
        sb.append("s");
        appendLog(textView, sb.toString());
        if (i > 0) {
            toast(context, "文件发送完成（" + i + "/" + list.size() + "）");
            return;
        }
        toast(context, "文件发送失败，请查看日志");
    }

    private static java.lang.Object buildFakeElement(android.os.Handler handler, boolean z, java.lang.Object obj) {
        java.lang.Object[] objArr = {null};
        java.lang.Throwable[] thArr = {null};
        handler.post(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda224(z, obj, objArr, thArr));
        long jCurrentTimeMillis = java.lang.System.currentTimeMillis() + 8000;
        while (objArr[0] == null && thArr[0] == null && java.lang.System.currentTimeMillis() < jCurrentTimeMillis) {
            try {
                java.lang.Thread.sleep(20L);
            } catch (java.lang.Throwable unused) {
            }
        }
        if (thArr[0] != null) {
            com.tencent.qqlitepro.util.Log.w("主线程构造分片元素失败: " + thArr[0]);
        }
        return objArr[0];
    }

    static /* synthetic */ void lambda$buildFakeElement$104(boolean z, java.lang.Object obj, java.lang.Object[] objArr, java.lang.Throwable[] thArr) {
        try {
            if (z) {
                java.lang.Object objCreatePicElement = com.tencent.qqlitepro.http.QqKernel.createPicElement(((com.tencent.qqlitepro.http.FakeImage.Wrapped) obj).pngFile.getAbsolutePath());
                com.tencent.qqlitepro.http.QqKernel.setPicDisplaySize(objCreatePicElement, 416, 468);
                objArr[0] = objCreatePicElement;
            } else {
                objArr[0] = com.tencent.qqlitepro.http.QqKernel.createPttElement(((com.tencent.qqlitepro.http.FakeVoice.Wrapped) obj).amrFile.getAbsolutePath());
            }
        } catch (java.lang.Throwable thRethrow12) {
            thArr[0] = thRethrow12;
        }
    }

    private static void buildVoiceUrl(android.content.Context context, android.view.ViewGroup viewGroup) {
        viewGroup.addView(backRow(context));
        viewGroup.addView(section(context, "URL 转语音发送"));
        sVoiceLog = null;
        android.widget.LinearLayout linearLayout = new android.widget.LinearLayout(context);
        linearLayout.setOrientation(1);
        android.widget.TextView textViewText = text(context, "暂无音频文件，可使用上方下载或手动放入文件。", HINT, 12.0f, 8388611);
        com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda225 settingsInjector$$ExternalSyntheticLambda225 = new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda225(context, linearLayout, textViewText);
        android.widget.LinearLayout linearLayout2 = new android.widget.LinearLayout(context);
        linearLayout2.setOrientation(1);
        android.graphics.drawable.GradientDrawable gradientDrawable = new android.graphics.drawable.GradientDrawable();
        gradientDrawable.setShape(0);
        gradientDrawable.setColor(SURFACE_CONTAINER);
        gradientDrawable.setCornerRadius(dpLite(14.0f));
        linearLayout2.setBackground(gradientDrawable);
        linearLayout2.setPadding(dp(context, 12.0f), dp(context, 14.0f), dp(context, 12.0f), dp(context, 14.0f));
        linearLayout2.setLayoutParams(rootLp());
        linearLayout2.addView(text(context, "音频下载地址 (URL)", TITLE, 15.0f, 8388611), rootLp());
        android.widget.EditText editTextEdit = edit(context, "", "https://…/音频文件.amr / .m4a / .mp3 / .silk", 1);
        linearLayout2.addView(editTextEdit, inputLp());
        android.widget.TextView textViewText2 = text(context, "请输入 URL 后点击下载", -6643546, 12.0f, 8388611);
        textViewText2.setPadding(0, dp(context, 4.0f), 0, dp(context, 4.0f));
        linearLayout2.addView(textViewText2, rootLp());
        android.widget.ProgressBar progressBar = new android.widget.ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        android.widget.LinearLayout.LayoutParams layoutParamsRootLp = rootLp();
        layoutParamsRootLp.topMargin = dp(context, 4.0f);
        layoutParamsRootLp.bottomMargin = dp(context, 6.0f);
        progressBar.setMax(100);
        progressBar.setLayoutParams(layoutParamsRootLp);
        progressBar.setVisibility(8);
        linearLayout2.addView(progressBar);
        viewGroup.addView(linearLayout2);
        makeGroupGap(viewGroup);
        viewGroup.addView(saveButton(context, "下载音频", new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda226(context, editTextEdit, textViewText2, progressBar, settingsInjector$$ExternalSyntheticLambda225)));
        makeGroupGap(viewGroup);
        viewGroup.addView(section(context, "已下载音频 · 选择发送"));
        viewGroup.addView(linearLayout);
        viewGroup.addView(textViewText);
        makeGroupGap(viewGroup);
        viewGroup.addView(saveButton(context, "刷新音频列表", settingsInjector$$ExternalSyntheticLambda225));
        settingsInjector$$ExternalSyntheticLambda225.run();
        makeGroupGap(viewGroup);
        android.widget.LinearLayout linearLayout3 = new android.widget.LinearLayout(context);
        linearLayout3.setOrientation(1);
        android.graphics.drawable.GradientDrawable gradientDrawable2 = new android.graphics.drawable.GradientDrawable();
        gradientDrawable2.setShape(0);
        gradientDrawable2.setColor(-15461356);
        gradientDrawable2.setCornerRadius(dpLite(12.0f));
        gradientDrawable2.setStroke(1, -13743516);
        linearLayout3.setBackground(gradientDrawable2);
        linearLayout3.setPadding(dp(context, 4.0f), dp(context, 8.0f), dp(context, 4.0f), dp(context, 6.0f));
        android.widget.LinearLayout.LayoutParams layoutParamsRootLp2 = rootLp();
        layoutParamsRootLp2.leftMargin = dp(context, 12.0f);
        layoutParamsRootLp2.rightMargin = dp(context, 12.0f);
        linearLayout3.setLayoutParams(layoutParamsRootLp2);
        android.widget.ScrollView scrollView = new android.widget.ScrollView(context);
        scrollView.setLayoutParams(new android.widget.LinearLayout.LayoutParams(-1, dp(context, 200.0f)));
        android.widget.LinearLayout linearLayout4 = new android.widget.LinearLayout(context);
        linearLayout4.setOrientation(1);
        linearLayout4.setPadding(dp(context, 8.0f), dp(context, 4.0f), dp(context, 8.0f), dp(context, 4.0f));
        scrollView.addView(linearLayout4, rootLp());
        android.widget.TextView textViewText3 = text(context, "", -7288592, 12.0f, 8388611);
        sVoiceLog = textViewText3;
        textViewText3.setPadding(dp(context, 8.0f), dp(context, 4.0f), dp(context, 8.0f), dp(context, 4.0f));
        sVoiceLog.setMovementMethod(new android.text.method.ScrollingMovementMethod());
        linearLayout4.addView(sVoiceLog, rootLp());
        linearLayout3.addView(scrollView, rootLp());
        addLogTitleRow(context, linearLayout3, "运行日志", sVoiceLog);
        viewGroup.addView(linearLayout3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void startAudioDownload(android.content.Context context, android.widget.EditText editText, android.widget.TextView textView, android.widget.ProgressBar progressBar, java.lang.Runnable runnable) {
        java.lang.String strTrim = editText == null ? "" : editText.getText().toString().trim();
        if (strTrim.isEmpty()) {
            textView.setText("URL 不能为空");
            toast(context, "请先输入音频 URL");
            appendLog(sVoiceLog, "[下载] URL 不能为空");
            return;
        }
        android.os.Handler handler = new android.os.Handler(android.os.Looper.getMainLooper());
        java.io.File fileAudioDir = audioDir(context);
        textView.setText("准备下载…");
        progressBar.setVisibility(0);
        progressBar.setProgress(0);
        appendLog(sVoiceLog, "[下载] 开始：" + strTrim);
        new java.lang.Thread(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda215(strTrim, fileAudioDir, handler, progressBar, textView, context, runnable), "url-audio-download").start();
    }

    static /* synthetic */ void lambda$startAudioDownload$111(java.lang.String str, java.io.File file, android.os.Handler handler, android.widget.ProgressBar progressBar, android.widget.TextView textView, android.content.Context context, java.lang.Runnable runnable) {
        java.net.HttpURLConnection httpURLConnection;
        java.io.File file2 = null;
        try {
            httpURLConnection = (java.net.HttpURLConnection) new java.net.URL(str).openConnection();
            try {
                httpURLConnection.setConnectTimeout(15000);
                httpURLConnection.setReadTimeout(30000);
                httpURLConnection.setInstanceFollowRedirects(true);
                httpURLConnection.connect();
                int responseCode = httpURLConnection.getResponseCode();
                if (responseCode != 200) {
                    throw new java.lang.RuntimeException("HTTP " + responseCode);
                }
                java.lang.String strUrlFileName = urlFileName(str, httpURLConnection);
                java.io.File file3 = new java.io.File(file, strUrlFileName);
                java.io.File file4 = new java.io.File(file, strUrlFileName + ".tmp");
                try {
                    long contentLength = httpURLConnection.getContentLength();
                    java.io.InputStream inputStream = httpURLConnection.getInputStream();
                    java.io.FileOutputStream fileOutputStream = new java.io.FileOutputStream(file4);
                    byte[] bArr = new byte[8192];
                    long j = 0;
                    while (true) {
                        int i = inputStream.read(bArr);
                        if (i <= 0) {
                            break;
                        }
                        fileOutputStream.write(bArr, 0, i);
                        long j2 = j + ((long) i);
                        handler.post(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda47(contentLength, j2, progressBar, textView));
                        contentLength = contentLength;
                        j = j2;
                    }
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    inputStream.close();
                    if (file4.exists() && file4.length() > 0) {
                        if (file3.exists()) {
                            file3.delete();
                        }
                        file4.renameTo(file3);
                    }
                    handler.post(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda48(progressBar, textView, file3.getName(), humanSize(file3.length()), context, runnable));
                    if (httpURLConnection == null) {
                    }
                } catch (java.lang.Throwable thRethrow12) {
                    file2 = file4;
                    try {
                        java.lang.String str2 = "下载失败：" + thRethrow12;
                        if (file2 != null) {
                            file2.delete();
                        }
                        handler.post(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda49(textView, str2, progressBar));
                        com.tencent.qqlitepro.util.Log.w(str2);
                    } finally {
                        if (httpURLConnection != null) {
                            try {
                                httpURLConnection.disconnect();
                            } catch (java.lang.Throwable unused) {
                            }
                        }
                    }
                }
            } catch (java.lang.Throwable th2) {
                java.lang.Throwable thRethrow13 = th2;
            }
        } catch (java.lang.Throwable th3) {
            java.lang.Throwable thRethrow14 = th3;
            httpURLConnection = null;
        }
    }

    static /* synthetic */ void lambda$startAudioDownload$107(long j, long j2, android.widget.ProgressBar progressBar, android.widget.TextView textView) {
        if (j > 0) {
            int i = (int) ((100 * j2) / j);
            progressBar.setProgress(i);
            textView.setText("正在下载… " + i + "%  (" + humanSize(j2) + " / " + humanSize(j) + ")");
            return;
        }
        textView.setText("正在下载…  " + humanSize(j2));
    }

    static /* synthetic */ void lambda$startAudioDownload$109(android.widget.ProgressBar progressBar, android.widget.TextView textView, java.lang.String str, java.lang.String str2, android.content.Context context, java.lang.Runnable runnable) {
        progressBar.setProgress(100);
        textView.setText("下载完成… " + str + " (" + str2 + ")");
        progressBar.postDelayed(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda155(progressBar), 600L);
        java.lang.StringBuilder sb = new java.lang.StringBuilder("下载完成：");
        sb.append(str);
        toast(context, sb.toString());
        appendLog(sVoiceLog, "[下载] 完成：" + str + " (" + str2 + ")");
        if (runnable != null) {
            runnable.run();
        }
    }

    static /* synthetic */ void lambda$startAudioDownload$110(android.widget.TextView textView, java.lang.String str, android.widget.ProgressBar progressBar) {
        textView.setText(str);
        progressBar.setVisibility(8);
        appendLog(sVoiceLog, "[下载] 失败：" + str);
    }

    private static java.lang.String urlFileName(java.lang.String str, java.net.HttpURLConnection httpURLConnection) {
        int iIndexOf;
        java.lang.String headerField = httpURLConnection.getHeaderField("Content-Disposition");
        java.lang.String str2 = "";
        if (headerField != null && (iIndexOf = headerField.toLowerCase().indexOf("filename=")) >= 0) {
            java.lang.String strTrim = headerField.substring(iIndexOf + 9).replace("\"", "").trim();
            if (!strTrim.isEmpty()) {
                str2 = strTrim;
            }
        }
        if (str2.isEmpty()) {
            int iIndexOf2 = str.indexOf(63);
            if (iIndexOf2 >= 0) {
                str = str.substring(0, iIndexOf2);
            }
            int iLastIndexOf = str.lastIndexOf(47);
            if (iLastIndexOf >= 0) {
                str = str.substring(iLastIndexOf + 1);
            }
            str2 = str;
        }
        if (str2.isEmpty() || "\\".equals(str2)) {
            str2 = "audio_" + java.lang.System.currentTimeMillis();
        }
        if (isAudioFile(str2)) {
            return str2;
        }
        return "audio_" + java.lang.System.currentTimeMillis() + ".amr";
    }

    private static java.lang.String updateFileName(java.lang.String str, java.net.HttpURLConnection httpURLConnection, java.lang.String str2) {
        int iIndexOf;
        java.lang.String headerField = httpURLConnection == null ? null : httpURLConnection.getHeaderField("Content-Disposition");
        java.lang.String strSubstring = "";
        if (headerField != null && (iIndexOf = headerField.toLowerCase().indexOf("filename=")) >= 0) {
            java.lang.String strTrim = headerField.substring(iIndexOf + 9).replace("\"", "").trim();
            if (!strTrim.isEmpty()) {
                strSubstring = strTrim;
            }
        }
        if (strSubstring.isEmpty()) {
            int iIndexOf2 = str.indexOf(63);
            if (iIndexOf2 >= 0) {
                str = str.substring(0, iIndexOf2);
            }
            int iLastIndexOf = str.lastIndexOf(47);
            if (iLastIndexOf >= 0) {
                str = str.substring(iLastIndexOf + 1);
            }
            strSubstring = str;
        }
        if (strSubstring.isEmpty()) {
            if (str2 == null || str2.isEmpty()) {
                str2 = "update";
            }
            strSubstring = str2;
        }
        int iLastIndexOf2 = strSubstring.lastIndexOf(46);
        if (iLastIndexOf2 > strSubstring.lastIndexOf(47)) {
            strSubstring = strSubstring.substring(0, iLastIndexOf2);
        }
        return strSubstring + ".apk";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void renderAudioList(android.content.Context context, android.widget.LinearLayout linearLayout, android.widget.TextView textView) {
        if (linearLayout == null) {
            return;
        }
        linearLayout.removeAllViews();
        java.util.List<java.io.File> listListAudioFiles = listAudioFiles(context);
        if (textView != null) {
            textView.setVisibility(listListAudioFiles.isEmpty() ? 0 : 8);
        }
        if (listListAudioFiles.isEmpty()) {
            return;
        }
        for (java.io.File file : listListAudioFiles) {
            android.widget.LinearLayout linearLayout2 = new android.widget.LinearLayout(context);
            linearLayout2.setOrientation(0);
            linearLayout2.setGravity(16);
            android.graphics.drawable.GradientDrawable gradientDrawable = new android.graphics.drawable.GradientDrawable();
            gradientDrawable.setShape(0);
            gradientDrawable.setColor(SURFACE_CONTAINER);
            gradientDrawable.setCornerRadius(dpLite(12.0f));
            linearLayout2.setBackground(gradientDrawable);
            linearLayout2.setPadding(dp(context, 12.0f), dp(context, 10.0f), dp(context, 12.0f), dp(context, 10.0f));
            android.widget.LinearLayout.LayoutParams layoutParamsRootLp = rootLp();
            layoutParamsRootLp.topMargin = dp(context, 4.0f);
            layoutParamsRootLp.bottomMargin = dp(context, 4.0f);
            linearLayout2.setLayoutParams(layoutParamsRootLp);
            android.widget.LinearLayout linearLayout3 = new android.widget.LinearLayout(context);
            linearLayout3.setOrientation(1);
            linearLayout3.setLayoutParams(new android.widget.LinearLayout.LayoutParams(0, -2, 1.0f));
            android.widget.TextView textViewText = text(context, file.getName(), TITLE, 14.0f, 8388611);
            textViewText.setMaxLines(1);
            android.widget.TextView textViewText2 = text(context, humanSize(file.length()), -6643546, 11.0f, 8388611);
            linearLayout3.addView(textViewText, rootLp());
            linearLayout3.addView(textViewText2, rootLp());
            linearLayout2.addView(linearLayout3);
            android.widget.TextView textViewText3 = text(context, "发送", BTN_TEXT, 13.0f, 17);
            android.graphics.drawable.GradientDrawable gradientDrawable2 = new android.graphics.drawable.GradientDrawable();
            gradientDrawable2.setShape(0);
            gradientDrawable2.setColor(BTN_FILL);
            gradientDrawable2.setCornerRadius(dpLite(10.0f));
            textViewText3.setBackground(gradientDrawable2);
            textViewText3.setPadding(dp(context, 14.0f), dp(context, 7.0f), dp(context, 14.0f), dp(context, 7.0f));
            textViewText3.setClickable(true);
            textViewText3.setOnClickListener(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda34(context, file.getAbsolutePath()));
            linearLayout2.addView(textViewText3);
            linearLayout.addView(linearLayout2);
        }
    }

    static /* synthetic */ void lambda$renderAudioList$113(android.content.Context context, java.lang.String str, android.view.View view) {
        restoreReplySelection("");
        showSessionPicker(context, new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda151(context, str));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void sendVoiceFiles(android.content.Context context, java.lang.String str) {
        java.io.File file = new java.io.File(str);
        if (!file.exists()) {
            toast(context, "音频文件不存在：" + str);
            appendLog(sVoiceLog, "[发送] 文件不存在：" + str);
            return;
        }
        java.util.Set<java.lang.String> set = sSelectedKeys;
        if (set == null || set.isEmpty()) {
            toast(context, "请先选择会话");
            appendLog(sVoiceLog, "[发送] 未选择会话");
            return;
        }
        java.util.ArrayList arrayList = new java.util.ArrayList(set);
        android.os.Handler handler = new android.os.Handler(android.os.Looper.getMainLooper());
        java.lang.Object[] objArr = {null};
        java.lang.Throwable[] thArr = {null};
        appendLog(sVoiceLog, "[发送] 准备发送 " + file.getName() + " 到 " + arrayList.size() + " 个会话");
        handler.post(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda184(objArr, str, thArr));
        new java.lang.Thread(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda185(objArr, thArr, handler, context, arrayList), "voice-send").start();
    }

    static /* synthetic */ void lambda$sendVoiceFiles$114(java.lang.Object[] objArr, java.lang.String str, java.lang.Throwable[] thArr) {
        try {
            objArr[0] = com.tencent.qqlitepro.http.QqKernel.createPttElement(str);
            appendLog(sVoiceLog, "[发送] 语音元素构造完成");
        } catch (java.lang.Throwable thRethrow14) {
            thArr[0] = thRethrow14;
            com.tencent.qqlitepro.util.Log.w("语音元素构造失败: " + thRethrow14);
            appendLog(sVoiceLog, "[发送] 元素构造失败：" + thRethrow14);
        }
    }

    static /* synthetic */ void lambda$sendVoiceFiles$119(java.lang.Object[] objArr, java.lang.Throwable[] thArr, android.os.Handler handler, android.content.Context context, java.util.List list) {
        java.lang.Object obj;
        int i;
        long jCurrentTimeMillis = java.lang.System.currentTimeMillis() + 8000;
        while (objArr[0] == null && thArr[0] == null && java.lang.System.currentTimeMillis() < jCurrentTimeMillis) {
            try {
                java.lang.Thread.sleep(20L);
            } catch (java.lang.Throwable unused) {
            }
        }
        java.lang.Throwable thRethrow14 = thArr[0];
        if (thRethrow14 != null || (obj = objArr[0]) == null) {
            handler.post(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda42(context, thRethrow14 != null ? java.lang.String.valueOf(thRethrow14) : "语音元素构造超时"));
            return;
        }
        java.util.Iterator it = list.iterator();
        int i2 = 0;
        while (it.hasNext()) {
            java.lang.String str = (java.lang.String) it.next();
            int iIndexOf = str.indexOf(58);
            if (iIndexOf > 0) {
                try {
                    i = java.lang.Integer.parseInt(str.substring(0, iIndexOf));
                } catch (java.lang.Throwable unused2) {
                    i = 2;
                }
                try {
                    java.lang.Object objCreateContact = com.tencent.qqlitepro.http.QqKernel.createContact(i, str.substring(iIndexOf + 1));
                    java.util.ArrayList arrayList = new java.util.ArrayList();
                    arrayList.add(obj);
                    com.tencent.qqlitepro.http.QqKernel.SendResult sendResultSendMsg = com.tencent.qqlitepro.http.QqKernel.sendMsg(objCreateContact, arrayList, 10000L);
                    if (sendResultSendMsg != null && sendResultSendMsg.code == 0) {
                        i2++;
                    }
                    int i3 = sendResultSendMsg == null ? -1 : sendResultSendMsg.code;
                    java.lang.String str2 = sendResultSendMsg == null ? "" : sendResultSendMsg.msg;
                    com.tencent.qqlitepro.util.Log.i("URL语音发送 " + str + " 结果 code=" + i3 + " msg=" + str2);
                    handler.post(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda43(str, i3, str2));
                } catch (java.lang.Throwable th2) {
                    com.tencent.qqlitepro.util.Log.w("URL语音发送失败 " + str + ": " + th2);
                    handler.post(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda44(str, th2));
                }
            }
        }
        handler.post(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda45(i2, list, context));
    }

    static /* synthetic */ void lambda$sendVoiceFiles$118(int i, java.util.List list, android.content.Context context) {
        appendLog(sVoiceLog, "[发送] 发送结束，成功 " + i + "/" + list.size());
        if (i > 0) {
            toast(context, "语音发送完成（" + i + "）");
            return;
        }
        toast(context, "语音发送失败，请查看日志");
    }

    private static void addLogTitleRow(android.content.Context context, android.widget.LinearLayout linearLayout, java.lang.String str, android.widget.TextView textView) {
        if (context == null || linearLayout == null) {
            return;
        }
        android.widget.LinearLayout linearLayout2 = new android.widget.LinearLayout(context);
        linearLayout2.setOrientation(0);
        linearLayout2.setGravity(16);
        linearLayout2.setLayoutParams(rootLp());
        android.widget.TextView textViewText = text(context, str, TITLE, 13.0f, 8388611);
        textViewText.setPadding(dp(context, 8.0f), 0, dp(context, 8.0f), dp(context, 4.0f));
        linearLayout2.addView(textViewText, new android.widget.LinearLayout.LayoutParams(0, -2, 1.0f));
        android.widget.TextView textViewText2 = text(context, "复制", -10046721, 12.0f, 17);
        textViewText2.setPadding(dp(context, 12.0f), dp(context, 3.0f), dp(context, 12.0f), dp(context, 3.0f));
        android.graphics.drawable.GradientDrawable gradientDrawable = new android.graphics.drawable.GradientDrawable();
        gradientDrawable.setColor(-14796204);
        gradientDrawable.setCornerRadius(dpLite(10.0f));
        textViewText2.setBackground(gradientDrawable);
        textViewText2.setClickable(true);
        textViewText2.setOnClickListener(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda4(context, textView));
        android.widget.LinearLayout.LayoutParams layoutParams = new android.widget.LinearLayout.LayoutParams(-2, -2);
        layoutParams.rightMargin = dp(context, 8.0f);
        linearLayout2.addView(textViewText2, layoutParams);
        linearLayout.addView(linearLayout2, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void copyTextToClipboard(android.content.Context context, java.lang.String str) {
        try {
            java.lang.Object systemService = context.getSystemService("clipboard");
            if (systemService instanceof android.content.ClipboardManager) {
                android.content.ClipboardManager clipboardManager = (android.content.ClipboardManager) systemService;
                if (str == null) {
                    str = "";
                }
                clipboardManager.setPrimaryClip(android.content.ClipData.newPlainText("日志", str));
                toast(context, "已复制日志");
            }
        } catch (java.lang.Throwable thRethrow14) {
            com.tencent.qqlitepro.util.Log.w("复制日志失败: " + thRethrow14);
            toast(context, "复制失败：" + thRethrow14);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void appendLog(android.widget.TextView textView, java.lang.String str) {
        if (textView == null) {
            return;
        }
        com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda132 settingsInjector$$ExternalSyntheticLambda132 = new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda132(textView, str + "\n");
        if (android.os.Looper.myLooper() == android.os.Looper.getMainLooper()) {
            settingsInjector$$ExternalSyntheticLambda132.run();
        } else {
            textView.post(settingsInjector$$ExternalSyntheticLambda132);
        }
    }

    static /* synthetic */ void lambda$appendLog$121(android.widget.TextView textView, java.lang.String str) {
        try {
            java.lang.CharSequence text = textView.getText();
            if (text.length() + str.length() > FILE_LOG_MAX_CHARS) {
                java.lang.String string = text.toString();
                int length = string.length() - java.lang.Math.max(0, FILE_LOG_MAX_CHARS - str.length());
                int iIndexOf = string.indexOf(10, length);
                textView.setText(iIndexOf >= 0 ? string.substring(iIndexOf + 1) : string.substring(java.lang.Math.min(length, string.length())));
            }
            textView.append(str);
            android.view.ViewParent parent = textView.getParent();
            while (parent != null && !(parent instanceof android.widget.ScrollView)) {
                parent = parent.getParent();
            }
            if (parent instanceof android.widget.ScrollView) {
                ((android.widget.ScrollView) parent).fullScroll(130);
            }
        } catch (java.lang.Throwable unused) {
        }
    }

    public static void appendQqLog(java.lang.String str) {
        android.widget.TextView textView = sQqDynamicLog;
        if (textView != null) {
            try {
                textView.post(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda167(str));
            } catch (java.lang.Throwable unused) {
            }
        }
    }

    private static void buildQqDynamic(android.content.Context context, android.view.ViewGroup viewGroup) {
        viewGroup.addView(backRow(context));
        viewGroup.addView(section(context, "QQ 动态"));
        sQqDynamicLog = null;
        viewGroup.addView(switchRow(context, "定时点赞/评论", "开启后每天到设定时刻处理当天动态", com.tencent.qqlitepro.util.Config.qqDynamicEnabled(context), new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda17(context)));
        android.widget.CompoundButton[] compoundButtonArr = new android.widget.CompoundButton[1];
        android.widget.CompoundButton[] compoundButtonArr2 = new android.widget.CompoundButton[1];
        viewGroup.addView(switchRowState(context, "定时点赞", "开启后每次运行为当天动态点赞", com.tencent.qqlitepro.util.Config.qqDynamicLike(context), compoundButtonArr));
        viewGroup.addView(switchRowState(context, "定时评论", "开启后每次运行为当天动态评论", com.tencent.qqlitepro.util.Config.qqDynamicComment(context), compoundButtonArr2));
        android.widget.EditText editTextEdit = edit(context, com.tencent.qqlitepro.util.Config.qqDynamicTime(context), "每天执行时刻，如 09:30", 1);
        makeGroupGap(viewGroup);
        android.widget.LinearLayout linearLayoutFilledCard = filledCard(context);
        linearLayoutFilledCard.addView(text(context, "每天执行时刻 (HH:MM)", TITLE, 14.0f, 8388611), rootLp());
        linearLayoutFilledCard.addView(editInCard(context, editTextEdit), inputLp());
        viewGroup.addView(linearLayoutFilledCard);
        android.widget.EditText editTextEdit2 = edit(context, com.tencent.qqlitepro.util.Config.qqDynamicBlacklist(context), "逗号/换行分隔 uin；名单内用户的动态不处理", 3);
        makeGroupGap(viewGroup);
        android.widget.LinearLayout linearLayoutFilledCard2 = filledCard(context);
        linearLayoutFilledCard2.addView(text(context, "黑名单用户（不处理其动态）", TITLE, 14.0f, 8388611), rootLp());
        linearLayoutFilledCard2.addView(editInCard(context, editTextEdit2), inputLp());
        viewGroup.addView(linearLayoutFilledCard2);
        makeGroupGap(viewGroup);
        android.view.View viewSaveButton = saveButton(context, "从会话选择黑名单(只私聊)", new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda18(editTextEdit2, context));
        marginBottom(viewSaveButton, 10.0f);
        viewGroup.addView(viewSaveButton);
        android.widget.EditText editTextEdit3 = edit(context, com.tencent.qqlitepro.util.Config.qqDynamicCommentText(context), "评论模板文字，默认“不错哦~”", 1);
        makeGroupGap(viewGroup);
        android.widget.LinearLayout linearLayoutFilledCard3 = filledCard(context);
        linearLayoutFilledCard3.addView(text(context, "评论模板", TITLE, 14.0f, 8388611), rootLp());
        linearLayoutFilledCard3.addView(editInCard(context, editTextEdit3), inputLp());
        viewGroup.addView(linearLayoutFilledCard3);
        android.widget.EditText editTextEdit4 = edit(context, com.tencent.qqlitepro.util.Config.qqDynamicLikeCount(context) > 0 ? java.lang.String.valueOf(com.tencent.qqlitepro.util.Config.qqDynamicLikeCount(context)) : "", "单次最多点赞多少条，留空/0 表示全部", 1);
        android.widget.EditText editTextEdit5 = edit(context, com.tencent.qqlitepro.util.Config.qqDynamicCommentCount(context) > 0 ? java.lang.String.valueOf(com.tencent.qqlitepro.util.Config.qqDynamicCommentCount(context)) : "", "单次最多评论多少条，留空/0 表示全部", 1);
        makeGroupGap(viewGroup);
        android.widget.LinearLayout linearLayoutFilledCard4 = filledCard(context);
        linearLayoutFilledCard4.addView(text(context, "自定义数量", TITLE, 14.0f, 8388611), rootLp());
        linearLayoutFilledCard4.addView(text(context, "点赞条数（0=不限制）", -6643546, 12.0f, 8388611), rootLp());
        linearLayoutFilledCard4.addView(editInCard(context, editTextEdit4), inputLp());
        linearLayoutFilledCard4.addView(text(context, "评论条数（0=不限制）", -6643546, 12.0f, 8388611), rootLp());
        linearLayoutFilledCard4.addView(editInCard(context, editTextEdit5), inputLp());
        viewGroup.addView(linearLayoutFilledCard4);
        makeGroupGap(viewGroup);
        viewGroup.addView(saveButton(context, "保存设置", new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda19(compoundButtonArr, compoundButtonArr2, editTextEdit, editTextEdit4, editTextEdit5, context, editTextEdit2, editTextEdit3)));
        makeGroupGap(viewGroup);
        viewGroup.addView(saveButton(context, "一键点赞当天全部动态", new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda20(context)));
        makeGroupGap(viewGroup);
        viewGroup.addView(saveButton(context, "一键评论当天全部动态", new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda21(context)));
        makeGroupGap(viewGroup);
        viewGroup.addView(saveButton(context, "立即执行（按自定义数量从最近动态开始）", new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda22(context, compoundButtonArr, compoundButtonArr2, editTextEdit4, editTextEdit5, editTextEdit3, editTextEdit2)));
        makeGroupGap(viewGroup);
        android.widget.LinearLayout linearLayout = new android.widget.LinearLayout(context);
        linearLayout.setOrientation(1);
        android.graphics.drawable.GradientDrawable gradientDrawable = new android.graphics.drawable.GradientDrawable();
        gradientDrawable.setShape(0);
        gradientDrawable.setColor(-15461356);
        gradientDrawable.setCornerRadius(dpLite(12.0f));
        gradientDrawable.setStroke(1, -13743516);
        linearLayout.setBackground(gradientDrawable);
        linearLayout.setPadding(dp(context, 4.0f), dp(context, 8.0f), dp(context, 4.0f), dp(context, 6.0f));
        android.widget.LinearLayout.LayoutParams layoutParamsRootLp = rootLp();
        layoutParamsRootLp.leftMargin = dp(context, 12.0f);
        layoutParamsRootLp.rightMargin = dp(context, 12.0f);
        linearLayout.setLayoutParams(layoutParamsRootLp);
        android.widget.ScrollView scrollView = new android.widget.ScrollView(context);
        scrollView.setLayoutParams(new android.widget.LinearLayout.LayoutParams(-1, dp(context, 200.0f)));
        android.widget.LinearLayout linearLayout2 = new android.widget.LinearLayout(context);
        linearLayout2.setOrientation(1);
        linearLayout2.setPadding(dp(context, 8.0f), dp(context, 4.0f), dp(context, 8.0f), dp(context, 4.0f));
        scrollView.addView(linearLayout2, rootLp());
        android.widget.TextView textViewText = text(context, "", -7288592, 12.0f, 8388611);
        sQqDynamicLog = textViewText;
        textViewText.setPadding(dp(context, 8.0f), dp(context, 4.0f), dp(context, 8.0f), dp(context, 4.0f));
        sQqDynamicLog.setMovementMethod(new android.text.method.ScrollingMovementMethod());
        linearLayout2.addView(sQqDynamicLog, rootLp());
        linearLayout.addView(scrollView, rootLp());
        addLogTitleRow(context, linearLayout, "操作日志", sQqDynamicLog);
        viewGroup.addView(linearLayout);
    }

    static /* synthetic */ void lambda$buildQqDynamic$125(android.widget.EditText editText, android.content.Context context) {
        java.util.LinkedHashSet linkedHashSet = new java.util.LinkedHashSet();
        for (java.lang.String str : (editText.getText() == null ? "" : editText.getText().toString().trim()).split("[,,\r\n ]+")) {
            if (str.matches("\\d+")) {
                linkedHashSet.add(str);
            }
        }
        showSessionPicker(context, true, linkedHashSet, new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda110(editText, context));
    }

    static /* synthetic */ void lambda$buildQqDynamic$124(android.widget.EditText editText, android.content.Context context) {
        java.lang.String strBuildWhitelistFromSelection = buildWhitelistFromSelection();
        if (editText != null) {
            editText.setText(strBuildWhitelistFromSelection);
        }
        java.lang.StringBuilder sb = new java.lang.StringBuilder("已选择 ");
        java.util.Set<java.lang.String> set = sSelectedKeys;
        sb.append(set.size());
        sb.append(" 个私聊白名单用户");
        appendQqLog(sb.toString());
        toast(context, "已选择 " + set.size() + " 个私聊用户");
    }

    static /* synthetic */ void lambda$buildQqDynamic$126(android.widget.CompoundButton[] compoundButtonArr, android.widget.CompoundButton[] compoundButtonArr2, android.widget.EditText editText, android.widget.EditText editText2, android.widget.EditText editText3, android.content.Context context, android.widget.EditText editText4, android.widget.EditText editText5) {
        java.lang.String str;
        boolean z = false;
        android.widget.CompoundButton compoundButton = compoundButtonArr[0];
        boolean z2 = compoundButton != null && compoundButton.isChecked();
        android.widget.CompoundButton compoundButton2 = compoundButtonArr2[0];
        if (compoundButton2 != null && compoundButton2.isChecked()) {
            z = true;
        }
        java.lang.String str2 = "";
        java.lang.String strNormalizeTime = com.tencent.qqlitepro.util.Config.normalizeTime(editText.getText() == null ? "" : editText.getText().toString().trim());
        int count = parseCount(editText2.getText() == null ? "" : editText2.getText().toString());
        int count2 = parseCount(editText3.getText() == null ? "" : editText3.getText().toString());
        com.tencent.qqlitepro.util.Config.setQqDynamicAll(context, true, strNormalizeTime, z2, z, editText4.getText().toString(), editText5.getText().toString(), count, count2);
        toast(context, "已保存设置");
        java.lang.StringBuilder sb = new java.lang.StringBuilder("已保存：每天 ");
        sb.append(strNormalizeTime);
        sb.append("，点赞=");
        sb.append(z2);
        if (count > 0) {
            str = "(≤" + count + "条)";
        } else {
            str = "";
        }
        sb.append(str);
        sb.append(" 评论=");
        sb.append(z);
        if (count2 > 0) {
            str2 = "(≤" + count2 + "条)";
        }
        sb.append(str2);
        appendQqLog(sb.toString());
    }

    static /* synthetic */ void lambda$buildQqDynamic$129(android.content.Context context, android.widget.CompoundButton[] compoundButtonArr, android.widget.CompoundButton[] compoundButtonArr2, android.widget.EditText editText, android.widget.EditText editText2, android.widget.EditText editText3, android.widget.EditText editText4) {
        android.widget.CompoundButton compoundButton = compoundButtonArr[0];
        boolean z = compoundButton != null && compoundButton.isChecked();
        android.widget.CompoundButton compoundButton2 = compoundButtonArr2[0];
        qqDynamicRunLatest(context, z, compoundButton2 != null && compoundButton2.isChecked(), parseCount(editText.getText() == null ? "" : editText.getText().toString()), parseCount(editText2.getText() == null ? "" : editText2.getText().toString()), editText3.getText() == null ? "" : editText3.getText().toString(), editText4.getText() == null ? "" : editText4.getText().toString());
    }

    private static int parseCount(java.lang.String str) {
        java.lang.String strTrim;
        if (str == null) {
            strTrim = "";
        } else {
            try {
                strTrim = str.trim();
            } catch (java.lang.Throwable unused) {
                return 0;
            }
        }
        if (strTrim.isEmpty()) {
            return 0;
        }
        return java.lang.Math.max(0, java.lang.Integer.parseInt(strTrim));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void qqDynamicRun(android.content.Context context, boolean z, boolean z2) {
        appendQqLog(z ? "开始一键点赞..." : "开始一键评论...");
        new java.lang.Thread(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda6(z, context, new android.os.Handler(android.os.Looper.getMainLooper())), "qq-dynamic-run").start();
    }

    static /* synthetic */ void lambda$qqDynamicRun$131(boolean z, android.content.Context context, android.os.Handler handler) {
        java.lang.String strCommentAllToday;
        if (z) {
            strCommentAllToday = com.tencent.qqlitepro.auto.QqDynamicScheduler.likeAllToday(context);
        } else {
            strCommentAllToday = com.tencent.qqlitepro.auto.QqDynamicScheduler.commentAllToday(context);
        }
        java.lang.StringBuilder sb = z ? new java.lang.StringBuilder("点赞结果：") : new java.lang.StringBuilder("评论结果：");
        sb.append(strCommentAllToday);
        appendQqLog(sb.toString());
        handler.post(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda156(context, strCommentAllToday));
    }

    private static void qqDynamicRunLatest(android.content.Context context, boolean z, boolean z2, int i, int i2, java.lang.String str, java.lang.String str2) {
        java.lang.String str3;
        if (!z && !z2) {
            toast(context, "请勾选点赞或评论开关");
            return;
        }
        try {
            com.tencent.qqlitepro.util.Config.setQqDynamicAll(context, com.tencent.qqlitepro.util.Config.qqDynamicEnabled(context), com.tencent.qqlitepro.util.Config.qqDynamicTime(context), z, z2, str2, str, i, i2);
        } catch (java.lang.Throwable unused) {
        }
        java.lang.StringBuilder sb = new java.lang.StringBuilder("开始立即执行（点赞=");
        sb.append(z);
        java.lang.String str4 = "";
        if (i > 0) {
            str3 = "≤" + i;
        } else {
            str3 = "";
        }
        sb.append(str3);
        sb.append(" 评论=");
        sb.append(z2);
        if (i2 > 0) {
            str4 = "≤" + i2;
        }
        sb.append(str4);
        sb.append("）...");
        appendQqLog(sb.toString());
        new java.lang.Thread(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda164(context, z, z2, new android.os.Handler(android.os.Looper.getMainLooper())), "qq-dynamic-run-latest").start();
    }

    static /* synthetic */ void lambda$qqDynamicRunLatest$133(android.content.Context context, boolean z, boolean z2, android.os.Handler handler) {
        java.lang.String strProcessLatest = com.tencent.qqlitepro.auto.QqDynamicScheduler.processLatest(context, z, z2);
        appendQqLog("立即执行结果：" + strProcessLatest);
        handler.post(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda161(context, strProcessLatest));
    }

    /**
     * 临时会话列表界面。
     *
     * 与 QQ 邮箱卡片同样的交互：会话列表里只保留一张汇总卡片，
     * 点进来后在这个独立界面里查看、管理全部临时会话。
     */
    public static void openTempChatList(android.content.Context context) {
        try {
            android.app.Dialog dialog = new android.app.Dialog(context);
            dialog.requestWindowFeature(1);
            dialog.setCanceledOnTouchOutside(true);

            android.widget.ScrollView scroll = new android.widget.ScrollView(context);
            scroll.setFillViewport(false);
            android.widget.LinearLayout root = new android.widget.LinearLayout(context);
            root.setOrientation(1);
            root.setBackgroundColor(-15657960);
            int pad = dp(context, 16.0f);
            root.setPadding(pad, dp(context, 14.0f), pad, dp(context, 16.0f));

            // 顶部标题栏
            android.widget.LinearLayout header = new android.widget.LinearLayout(context);
            header.setOrientation(0);
            header.setGravity(16);
            header.addView(text(context, "临时会话", TITLE, 17.0f, 3),
                    new android.widget.LinearLayout.LayoutParams(0, -2, 1.0f));
            android.widget.TextView close = text(context, "关闭", ACCENT, 13.0f, 17);
            close.setPadding(dp(context, 10.0f), dp(context, 4.0f), 0, dp(context, 4.0f));
            close.setClickable(true);
            close.setOnClickListener(new android.view.View.OnClickListener() {
                @Override
                public void onClick(android.view.View v) {
                    try {
                        dialog.dismiss();
                    } catch (java.lang.Throwable ignored) {
                    }
                }
            });
            header.addView(close);
            root.addView(header, rootLp());

            java.util.List<java.lang.Object[]> items = com.tencent.qqlitepro.http.QqKernel.tempChatSnapshot();
            if (items.isEmpty()) {
                android.widget.TextView empty = text(context, "暂无临时会话", HINT, 13.0f, 17);
                empty.setPadding(0, dp(context, 27.0f), 0, dp(context, 27.0f));
                root.addView(empty, rootLp());
            } else {
                root.addView(text(context, "共 " + items.size() + " 个", HINT, 12.0f, 3), withTop(context, 6.0f));
                for (int i = 0; i < items.size(); i++) {
                    root.addView(tempChatRow(context, dialog, items.get(i)), withTop(context, 8.0f));
                }
            }

            scroll.addView(root);
            dialog.setContentView(scroll);
            if (dialog.getWindow() != null) {
                android.view.WindowManager.LayoutParams attrs = dialog.getWindow().getAttributes();
                attrs.width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.94f);
                attrs.height = (int) (context.getResources().getDisplayMetrics().heightPixels * 0.80f);
                dialog.getWindow().setAttributes(attrs);
            }
            dialog.show();
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("打开临时会话列表失败: " + th);
        }
    }

    /** 列表中单行临时会话 */
    private static android.view.View tempChatRow(final android.content.Context context,
                                                 final android.app.Dialog dialog,
                                                 final java.lang.Object[] item) {
        final java.lang.String peerUid = (java.lang.String) item[0];
        java.lang.String name = (java.lang.String) item[1];
        java.lang.String preview = (java.lang.String) item[2];
        int unread = ((java.lang.Integer) item[3]).intValue();

        android.widget.LinearLayout row = new android.widget.LinearLayout(context);
        row.setOrientation(1);
        android.graphics.drawable.GradientDrawable bg = new android.graphics.drawable.GradientDrawable();
        bg.setColor(-14013129);
        bg.setCornerRadius(dpLite(10.0f));
        row.setBackground(bg);
        row.setPadding(dp(context, 12.0f), dp(context, 10.0f), dp(context, 12.0f), dp(context, 10.0f));

        // 第一行：名字 + 未读角标
        android.widget.LinearLayout line1 = new android.widget.LinearLayout(context);
        line1.setOrientation(0);
        line1.setGravity(16);
        java.lang.String title = (name == null || name.trim().isEmpty()) ? peerUid : name.trim();
        if (com.tencent.qqlitepro.http.QqKernel.isTempChatTop(peerUid)) {
            title = "↑ " + title;
        }
        line1.addView(text(context, title, TITLE, 14.5f, 3),
                new android.widget.LinearLayout.LayoutParams(0, -2, 1.0f));
        if (unread > 0) {
            android.widget.TextView badge = text(context, unread > 99 ? "99+" : java.lang.String.valueOf(unread), -1, 11.0f, 17);
            badge.setBackground(rounded(-11751600, 9.0f));
            badge.setPadding(dp(context, 6.0f), dp(context, 1.0f), dp(context, 6.0f), dp(context, 1.0f));
            badge.setMinWidth(dp(context, 18.0f));
            line1.addView(badge);
        }
        row.addView(line1, rootLp());

        // 第二行：预览
        if (preview != null && !preview.trim().isEmpty()) {
            android.widget.TextView pv = text(context, preview.trim(), SUB, 12.5f, 3);
            pv.setSingleLine(true);
            pv.setEllipsize(android.text.TextUtils.TruncateAt.END);
            row.addView(pv, withTop(context, 5.0f));
        }

        // 第三行：操作
        android.widget.LinearLayout ops = new android.widget.LinearLayout(context);
        ops.setOrientation(0);
        ops.setGravity(16);
        boolean isTop = com.tencent.qqlitepro.http.QqKernel.isTempChatTop(peerUid);
        boolean isDnd = com.tencent.qqlitepro.http.QqKernel.isTempChatDnd(peerUid);
        ops.addView(rowAction(context, isTop ? "取消置顶" : "置顶", new java.lang.Runnable() {
            @Override
            public void run() {
                com.tencent.qqlitepro.http.QqKernel.setTempChatTop(peerUid,
                        !com.tencent.qqlitepro.http.QqKernel.isTempChatTop(peerUid));
                reopenTempChatList(context, dialog);
            }
        }));
        ops.addView(rowAction(context, isDnd ? "取消免打扰" : "免打扰", new java.lang.Runnable() {
            @Override
            public void run() {
                com.tencent.qqlitepro.http.QqKernel.setTempChatDnd(peerUid,
                        !com.tencent.qqlitepro.http.QqKernel.isTempChatDnd(peerUid));
                reopenTempChatList(context, dialog);
            }
        }));
        ops.addView(rowAction(context, "清除未读", new java.lang.Runnable() {
            @Override
            public void run() {
                com.tencent.qqlitepro.http.QqKernel.setTempChatUnread(peerUid, 0);
                reopenTempChatList(context, dialog);
            }
        }));
        ops.addView(rowAction(context, "删除", new java.lang.Runnable() {
            @Override
            public void run() {
                com.tencent.qqlitepro.http.QqKernel.forgetTempChat(peerUid);
                reopenTempChatList(context, dialog);
            }
        }));
        android.widget.LinearLayout.LayoutParams opsLp = rootLp();
        opsLp.topMargin = dp(context, 9.0f);
        row.addView(ops, opsLp);

        // 点击整行进会话
        row.setClickable(true);
        row.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                try {
                    dialog.dismiss();
                    com.tencent.qqlitepro.http.QqKernel.openTempChatSession(context, peerUid);
                } catch (java.lang.Throwable th) {
                    com.tencent.qqlitepro.util.Log.w("打开临时会话失败: " + th);
                }
            }
        });
        return row;
    }

    private static android.view.View rowAction(final android.content.Context context, java.lang.String label, final java.lang.Runnable action) {
        android.widget.TextView tv = text(context, label, ACCENT, 12.0f, 17);
        tv.setPadding(0, dp(context, 5.0f), dp(context, 14.0f), dp(context, 5.0f));
        tv.setClickable(true);
        tv.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                try {
                    action.run();
                } catch (java.lang.Throwable th) {
                    com.tencent.qqlitepro.util.Log.w("临时会话操作失败: " + th);
                }
            }
        });
        return tv;
    }

    /** 操作后刷新列表界面（关旧开新，保证数据最新） */
    private static void reopenTempChatList(final android.content.Context context, final android.app.Dialog dialog) {
        try {
            dialog.dismiss();
        } catch (java.lang.Throwable ignored) {
        }
        new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(new java.lang.Runnable() {
            @Override
            public void run() {
                openTempChatList(context);
            }
        }, 120L);
    }

    private static android.graphics.drawable.GradientDrawable rounded(int color, float radius) {
        android.graphics.drawable.GradientDrawable d = new android.graphics.drawable.GradientDrawable();
        d.setColor(color);
        d.setCornerRadius(dpLite(radius));
        return d;
    }

    public static void openMailDialog(android.content.Context context) {
        try {
            android.app.Dialog dialog = sMailDialog;
            if (dialog == null || !dialog.isShowing()) {
                android.app.Dialog dialog2 = new android.app.Dialog(context);
                dialog2.requestWindowFeature(1);
                dialog2.setCanceledOnTouchOutside(false);
                dialog2.setOnDismissListener(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda137());
                android.widget.ScrollView scrollView = new android.widget.ScrollView(context);
                scrollView.setFillViewport(false);
                android.widget.LinearLayout linearLayout = new android.widget.LinearLayout(context);
                linearLayout.setOrientation(1);
                linearLayout.setBackgroundColor(-15657960);
                scrollView.addView(linearLayout, new android.widget.FrameLayout.LayoutParams(-1, -2));
                dialog2.setContentView(scrollView);
                android.view.Window window = dialog2.getWindow();
                if (window != null) {
                    android.graphics.drawable.GradientDrawable gradientDrawable = new android.graphics.drawable.GradientDrawable();
                    gradientDrawable.setColor(-15657960);
                    window.setBackgroundDrawable(gradientDrawable);
                    window.setLayout(-1, -1);
                }
                sMailDialog = dialog2;
                openPage(linearLayout, 16);
                dialog2.show();
            }
        } catch (java.lang.Throwable thRethrow14) {
            com.tencent.qqlitepro.util.Log.w("打开邮箱管理界面失败: " + thRethrow14);
            toast(context, "无法打开邮箱管理界面：" + thRethrow14);
        }
    }

    public static boolean dismissMailDialog() {
        android.app.Dialog dialog = sMailDialog;
        if (dialog == null || !dialog.isShowing()) {
            return false;
        }
        dialog.dismiss();
        sMailDialog = null;
        return true;
    }

    public static void openMailPage(android.content.Context context) {
        sPendingPage = 16;
        try {
            android.content.Intent intent = new android.content.Intent();
            intent.setClassName(context, "moye.wearqq.SettingsActivity");
            intent.addFlags(268435456);
            context.startActivity(intent);
        } catch (java.lang.Throwable thRethrow14) {
            com.tencent.qqlitepro.util.Log.w("打开设置页失败: " + thRethrow14);
            toast(context, "无法打开邮箱管理页：" + thRethrow14);
            sPendingPage = 0;
        }
    }

    public static void openModuleSettings(android.content.Context context) {
        if (context == null) {
            return;
        }
        if (!com.tencent.qqlitepro.MainHook.isGroupPassed()) {
            toast(context, com.tencent.qqlitepro.MainHook.NOT_IN_GROUP_TIP);
            return;
        }
        if (sModuleCategory == null) {
            toast(context, "增强模块设置尚未就绪，请稍后重试");
            return;
        }
        android.app.Activity activity = sSettingsActRef != null ? sSettingsActRef.get() : null;
        if (activity != null && !activity.isFinishing()) {
            showModuleCategory(activity);
            bringSettingsToFront(context);
            return;
        }
        sPendingCategoryJump = true;
        if (bringSettingsToFront(context)) {
            return;
        }
        sPendingCategoryJump = false;
        toast(context, "无法打开增强模块设置");
    }

    private static boolean bringSettingsToFront(android.content.Context context) {
        try {
            android.content.Intent intent = new android.content.Intent();
            intent.setClassName(context, "moye.wearqq.SettingsActivity");
            intent.addFlags(268566528);
            context.startActivity(intent);
            return true;
        } catch (java.lang.Throwable thRethrow14) {
            com.tencent.qqlitepro.util.Log.w("打开设置页失败: " + thRethrow14);
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean showModuleCategory(android.app.Activity activity) {
        java.lang.Object obj = sModuleCategory;
        if (activity != null && !activity.isFinishing() && obj != null) {
            try {
                for (java.lang.Class<?> superclass = activity.getClass(); superclass != null; superclass = superclass.getSuperclass()) {
                    for (java.lang.reflect.Method method : superclass.getDeclaredMethods()) {
                        if (method.getName().equals("showCategory") && method.getParameterTypes().length == 1) {
                            method.setAccessible(true);
                            method.invoke(activity, obj);
                            return true;
                        }
                    }
                }
            } catch (java.lang.Throwable thRethrow14) {
                com.tencent.qqlitepro.util.Log.w("展开增强模块设置失败: " + thRethrow14);
            }
        }
        return false;
    }

    public static android.view.View chatPanelSettingsEntry(android.content.Context context) {
        return chatPanelEntry(context, "模块增强设置", new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda30(context));
    }

    public static android.view.View chatPanelEntry(android.content.Context context, java.lang.String str, java.lang.Runnable runnable) {
        android.widget.TextView textViewText = text(context, str, TITLE, 13.0f, 17);
        textViewText.setTypeface(android.graphics.Typeface.DEFAULT, 1);
        android.graphics.drawable.GradientDrawable gradientDrawable = new android.graphics.drawable.GradientDrawable();
        gradientDrawable.setShape(0);
        gradientDrawable.setColor(SURFACE_CONTAINER);
        gradientDrawable.setCornerRadius(dpLite(14.0f));
        textViewText.setBackground(gradientDrawable);
        textViewText.setPadding(dp(context, 12.0f), dp(context, 10.0f), dp(context, 12.0f), dp(context, 10.0f));
        textViewText.setOnClickListener(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda68(runnable));
        return textViewText;
    }

    private static void buildMailHome(android.content.Context context, android.view.ViewGroup viewGroup) {
        java.lang.String str;
        viewGroup.addView(backRow(context));
        viewGroup.addView(section(context, "QQ 邮箱管理"));
        java.lang.String strMailAddr = com.tencent.qqlitepro.util.Config.mailAddr(context);
        if (strMailAddr.isEmpty()) {
            android.widget.LinearLayout linearLayoutFilledCard = filledCard(context);
            linearLayoutFilledCard.addView(text(context, "尚未配置邮箱", TITLE, 15.0f, 8388611), rootLp());
            linearLayoutFilledCard.addView(text(context, "配置 QQ 邮箱后即可接收 / 发送邮件，新邮件将实时提醒", HINT, 12.0f, 8388611), rootLp());
            viewGroup.addView(linearLayoutFilledCard);
            makeGroupGap(viewGroup);
            viewGroup.addView(saveButton(context, "设置邮箱", new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda87(viewGroup)));
            return;
        }
        if (!com.tencent.qqlitepro.util.Config.mailVerified(context)) {
            android.widget.LinearLayout linearLayoutFilledCard2 = filledCard(context);
            linearLayoutFilledCard2.addView(text(context, "已填写邮箱：" + strMailAddr, TITLE, 15.0f, 8388611), rootLp());
            linearLayoutFilledCard2.addView(text(context, "尚未验证连接，请先完成验证后再使用收件 / 发件", HINT, 12.0f, 8388611), rootLp());
            viewGroup.addView(linearLayoutFilledCard2);
            makeGroupGap(viewGroup);
            viewGroup.addView(saveButton(context, "验证连接", new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda88(viewGroup)));
            makeGroupGap(viewGroup);
            viewGroup.addView(navRow(context, "重新设置邮箱", "修改邮箱地址 / 授权码 / 名称", 17));
            return;
        }
        int iMailUnreadCount = com.tencent.qqlitepro.util.Config.mailUnreadCount(context);
        if (iMailUnreadCount > 0) {
            str = "未读 " + iMailUnreadCount + " 封新邮件";
        } else {
            str = "查看收件箱 · 暂无未读";
        }
        viewGroup.addView(mailActionCard(context, viewGroup, "接收邮件", str, iMailUnreadCount, 18));
        viewGroup.addView(mailActionCard(context, viewGroup, "发送邮件", "编写并发送新邮件", -1, 20));
        makeGroupGap(viewGroup);
        viewGroup.addView(navRow(context, "邮箱设置", "修改邮箱 / 授权码 / 名称", 17));
        addDivider(viewGroup);
        addResetButton(context, viewGroup, "清除邮箱配置", 16, new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda89(context));
    }

    static /* synthetic */ void lambda$buildMailHome$139(android.content.Context context) {
        com.tencent.qqlitepro.util.Config.resetMail(context);
        com.tencent.qqlitepro.mail.QqMailManager.stopPolling();
    }

    private static android.view.View mailActionCard(android.content.Context context, android.view.ViewGroup viewGroup, java.lang.String str, java.lang.String str2, int i, int i2) {
        android.widget.FrameLayout frameLayout = new android.widget.FrameLayout(context);
        android.widget.LinearLayout linearLayout = new android.widget.LinearLayout(context);
        linearLayout.setOrientation(1);
        android.graphics.drawable.GradientDrawable gradientDrawable = new android.graphics.drawable.GradientDrawable();
        gradientDrawable.setShape(0);
        gradientDrawable.setColor(SURFACE_CONTAINER);
        gradientDrawable.setCornerRadius(dpLite(14.0f));
        linearLayout.setBackground(gradientDrawable);
        linearLayout.setPadding(dp(context, 14.0f), dp(context, 16.0f), dp(context, 14.0f), dp(context, 16.0f));
        android.widget.FrameLayout.LayoutParams layoutParams = new android.widget.FrameLayout.LayoutParams(-1, -2);
        layoutParams.setMargins(dp(context, 12.0f), dp(context, 4.0f), dp(context, 12.0f), dp(context, 8.0f));
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.addView(text(context, str, TITLE, 16.0f, 8388611), rootLp());
        linearLayout.addView(text(context, str2, -6643546, 12.0f, 8388611), rootLp());
        frameLayout.addView(linearLayout);
        if (i > 0) {
            android.view.View viewRedBadge = redBadge(context, i);
            android.widget.FrameLayout.LayoutParams layoutParams2 = new android.widget.FrameLayout.LayoutParams(-2, -2, 8388661);
            layoutParams2.setMargins(0, dp(context, 10.0f), dp(context, 18.0f), 0);
            frameLayout.addView(viewRedBadge, layoutParams2);
        }
        frameLayout.setLayoutParams(rootLp());
        frameLayout.setClickable(true);
        frameLayout.setOnClickListener(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda170(viewGroup, i2));
        return frameLayout;
    }

    private static android.widget.TextView redBadge(android.content.Context context, int i) {
        android.widget.TextView textViewText = text(context, i > 99 ? "99+" : java.lang.String.valueOf(i), -1, 10.0f, 17);
        android.graphics.drawable.GradientDrawable gradientDrawable = new android.graphics.drawable.GradientDrawable();
        gradientDrawable.setShape(1);
        gradientDrawable.setColor(-1754827);
        textViewText.setBackground(gradientDrawable);
        textViewText.setPadding(dp(context, 6.0f), dp(context, 2.0f), dp(context, 6.0f), dp(context, 2.0f));
        return textViewText;
    }

    private static android.widget.TextView redDot(android.content.Context context) {
        android.widget.TextView textView = new android.widget.TextView(context);
        textView.setText("");
        android.graphics.drawable.GradientDrawable gradientDrawable = new android.graphics.drawable.GradientDrawable();
        gradientDrawable.setShape(1);
        gradientDrawable.setColor(-1754827);
        textView.setBackground(gradientDrawable);
        android.widget.LinearLayout.LayoutParams layoutParams = new android.widget.LinearLayout.LayoutParams(dp(context, 8.0f), dp(context, 8.0f));
        layoutParams.topMargin = dp(context, 3.0f);
        textView.setLayoutParams(layoutParams);
        return textView;
    }

    private static void buildMailSetting(android.content.Context context, android.view.ViewGroup viewGroup) {
        java.lang.String str;
        viewGroup.addView(backRow(context));
        viewGroup.addView(section(context, "邮箱设置"));
        android.widget.LinearLayout linearLayoutFilledCard = filledCard(context);
        linearLayoutFilledCard.addView(text(context, "邮箱地址", TITLE, 14.0f, 8388611), rootLp());
        android.widget.EditText editTextEdit = edit(context, com.tencent.qqlitepro.util.Config.mailAddr(context), "QQ 邮箱地址，如 123456@qq.com", 1);
        editTextEdit.setInputType(33);
        linearLayoutFilledCard.addView(editInCard(context, editTextEdit), inputLp());
        linearLayoutFilledCard.addView(text(context, "授权码", TITLE, 14.0f, 8388611), rootLp());
        android.widget.EditText editTextEdit2 = edit(context, com.tencent.qqlitepro.util.Config.mailAuthCode(context), "QQ 邮箱授权码（非 QQ 密码）", 1);
        editTextEdit2.setInputType(129);
        linearLayoutFilledCard.addView(editInCard(context, editTextEdit2), inputLp());
        java.lang.String strDefaultName = com.tencent.qqlitepro.mail.QqMailManager.defaultName();
        linearLayoutFilledCard.addView(text(context, "自定义名称（可选）", TITLE, 14.0f, 8388611), rootLp());
        java.lang.String strMailName = com.tencent.qqlitepro.util.Config.mailName(context);
        if (strDefaultName.isEmpty()) {
            str = "默认当前 QQ 昵称";
        } else {
            str = "默认当前 QQ 昵称：" + strDefaultName;
        }
        android.widget.EditText editTextEdit3 = edit(context, strMailName, str, 1);
        linearLayoutFilledCard.addView(editInCard(context, editTextEdit3), inputLp());
        viewGroup.addView(linearLayoutFilledCard);
        viewGroup.addView(text(context, "授权码获取：QQ 邮箱网页版 → 设置 → 账户 → 开启 IMAP/SMTP 服务 → 生成授权码。服务器以 QQ 官方为准（imap.qq.com:993 / smtp.qq.com:465）。", HINT, 11.0f, 8388611));
        makeGroupGap(viewGroup);
        viewGroup.addView(saveButton(context, "验证并保存", new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda121(editTextEdit, editTextEdit2, editTextEdit3, context, viewGroup)));
        makeGroupGap(viewGroup);
        addResetButton(context, viewGroup, "清除邮箱配置", 17, new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda122(context));
    }

    static /* synthetic */ void lambda$buildMailSetting$143(android.widget.EditText editText, android.widget.EditText editText2, android.widget.EditText editText3, android.content.Context context, android.view.ViewGroup viewGroup) {
        java.lang.String strTrim = editText.getText() == null ? "" : editText.getText().toString().trim();
        java.lang.String strTrim2 = editText2.getText() == null ? "" : editText2.getText().toString().trim();
        java.lang.String strTrim3 = editText3.getText() != null ? editText3.getText().toString().trim() : "";
        if (strTrim.isEmpty() || strTrim2.isEmpty()) {
            toast(context, "邮箱地址和授权码不能为空");
            return;
        }
        com.tencent.qqlitepro.util.Config.setMailConfig(context, strTrim, strTrim2, strTrim3);
        toast(context, "正在验证连接...");
        new java.lang.Thread(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda25(context, strTrim, strTrim2, new android.os.Handler(android.os.Looper.getMainLooper()), viewGroup), "mail-verify").start();
    }

    static /* synthetic */ void lambda$buildMailSetting$141(com.tencent.qqlitepro.mail.QqMailManager.VerifyResult verifyResult, android.content.Context context, android.view.ViewGroup viewGroup) {
        if (verifyResult.ok) {
            com.tencent.qqlitepro.util.Config.setMailVerified(context, true);
            com.tencent.qqlitepro.mail.QqMailManager.ensurePolling(context);
            toast(context, "验证成功，已进入邮箱管理");
            refreshPage(viewGroup, 16);
            com.tencent.qqlitepro.hook.ChatCardInjector.refreshChatList();
            return;
        }
        showMailError(context, "验证失败", verifyResult.message);
    }

    static /* synthetic */ void lambda$buildMailSetting$144(android.content.Context context) {
        com.tencent.qqlitepro.util.Config.resetMail(context);
        com.tencent.qqlitepro.mail.QqMailManager.stopPolling();
        com.tencent.qqlitepro.hook.ChatCardInjector.refreshChatList();
    }

    private static void showMailError(android.content.Context context, java.lang.String str, java.lang.String str2) {
        try {
            new android.app.AlertDialog.Builder(context).setTitle(str).setMessage(str2 == null ? "未知错误" : str2).setPositiveButton("知道了", (android.content.DialogInterface.OnClickListener) null).show();
        } catch (java.lang.Throwable unused) {
            toast(context, str2);
        }
    }

    private static void buildMailInbox(android.content.Context context, android.view.ViewGroup viewGroup) {
        viewGroup.addView(mailBackRow(context, viewGroup, 16));
        viewGroup.addView(section(context, "收件箱"));
        viewGroup.addView(saveButton(context, "刷新收件箱", new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda51(context, viewGroup)));
        viewGroup.addView(gap(context, 8.0f));
        viewGroup.addView(saveButton(context, "一键已读", new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda52(context, viewGroup)));
        viewGroup.addView(gap(context, 8.0f));
        java.lang.String strMailWebBg = com.tencent.qqlitepro.util.Config.mailWebBg(context);
        viewGroup.addView(saveButton(context, (strMailWebBg.isEmpty() || !new java.io.File(strMailWebBg).exists()) ? "网页渲染背景图（默认白底）" : "网页渲染背景图（已自定义）", new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda53(viewGroup)));
        makeGroupGap(viewGroup);
        scheduleInboxAutoRefresh(context, viewGroup);
        java.util.List<com.google.gson.JsonObject> mailCacheList = parseMailCacheList(com.tencent.qqlitepro.util.Config.mailInboxCache(context));
        if (mailCacheList.isEmpty()) {
            viewGroup.addView(text(context, "收件箱为空，点击上方「刷新收件箱」拉取", HINT, 12.0f, 8388611));
            return;
        }
        for (int i = 0; i < mailCacheList.size(); i++) {
            viewGroup.addView(mailRow(context, mailCacheList.get(i), i, viewGroup));
        }
    }

    static /* synthetic */ void lambda$buildMailInbox$147(android.content.Context context, android.view.ViewGroup viewGroup) {
        toast(context, "正在拉取...");
        new java.lang.Thread(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda136(context, new android.os.Handler(android.os.Looper.getMainLooper()), viewGroup), "mail-fetch").start();
    }

    static /* synthetic */ void lambda$buildMailInbox$146(android.content.Context context, android.os.Handler handler, android.view.ViewGroup viewGroup) {
        try {
            com.tencent.qqlitepro.mail.QqMailManager.pollInbox(context);
        } catch (java.lang.Throwable unused) {
        }
        handler.post(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda166(viewGroup));
    }

    static /* synthetic */ void lambda$buildMailInbox$148(android.content.Context context, android.view.ViewGroup viewGroup) {
        java.util.List<com.google.gson.JsonObject> mailCacheList = parseMailCacheList(com.tencent.qqlitepro.util.Config.mailInboxCache(context));
        boolean z = false;
        for (com.google.gson.JsonObject jsonObject : mailCacheList) {
            if (!bool(jsonObject, "read", false)) {
                z = true;
                jsonObject.addProperty("read", (java.lang.Boolean) true);
            }
        }
        if (z) {
            com.tencent.qqlitepro.util.Config.setMailInbox(context, cacheToJson(mailCacheList), 0, com.tencent.qqlitepro.util.Config.mailLastUid(context));
        }
        toast(context, z ? "已全部标记为已读" : "没有未读邮件");
        refreshPage(viewGroup, 18);
        com.tencent.qqlitepro.hook.ChatCardInjector.refreshChatList();
    }

    private static void scheduleInboxAutoRefresh(android.content.Context context, android.view.ViewGroup viewGroup) {
        int i = sInboxAutoGen + 1;
        sInboxAutoGen = i;
        android.os.Handler handler = new android.os.Handler(android.os.Looper.getMainLooper());
        handler.postDelayed(new com.tencent.qqlitepro.hook.SettingsInjector.AnonymousClass10(i, viewGroup, new boolean[]{false}, context, handler), 6000L);
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.SettingsInjector$10, reason: invalid class name */
    static class AnonymousClass10 implements java.lang.Runnable {
        final /* synthetic */ boolean[] val$busy;
        final /* synthetic */ android.content.Context val$c;
        final /* synthetic */ int val$gen;
        final /* synthetic */ android.view.ViewGroup val$group;
        final /* synthetic */ android.os.Handler val$h;

        AnonymousClass10(int i, android.view.ViewGroup viewGroup, boolean[] zArr, android.content.Context context, android.os.Handler handler) {
            this.val$gen = i;
            this.val$group = viewGroup;
            this.val$busy = zArr;
            this.val$c = context;
            this.val$h = handler;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (this.val$gen == com.tencent.qqlitepro.hook.SettingsInjector.sInboxAutoGen && com.tencent.qqlitepro.hook.SettingsInjector.sPageGroup == this.val$group && com.tencent.qqlitepro.hook.SettingsInjector.sCurrentPage == 18) {
                boolean[] zArr = this.val$busy;
                if (zArr[0]) {
                    return;
                }
                zArr[0] = true;
                new java.lang.Thread(new com.tencent.qqlitepro.hook.SettingsInjector$10$$ExternalSyntheticLambda1(this.val$c, this.val$h, this.val$busy, this.val$gen, this.val$group), "mail-inbox-auto").start();
            }
        }

        static /* synthetic */ void lambda$run$1(android.content.Context context, android.os.Handler handler, boolean[] zArr, int i, android.view.ViewGroup viewGroup) {
            try {
                com.tencent.qqlitepro.mail.QqMailManager.pollInbox(context);
            } catch (java.lang.Throwable unused) {
            }
            handler.post(new com.tencent.qqlitepro.hook.SettingsInjector$10$$ExternalSyntheticLambda0(zArr, i, viewGroup));
        }

        static /* synthetic */ void lambda$run$0(boolean[] zArr, int i, android.view.ViewGroup viewGroup) {
            zArr[0] = false;
            if (i == com.tencent.qqlitepro.hook.SettingsInjector.sInboxAutoGen && com.tencent.qqlitepro.hook.SettingsInjector.sPageGroup == viewGroup && com.tencent.qqlitepro.hook.SettingsInjector.sCurrentPage == 18) {
                com.tencent.qqlitepro.hook.SettingsInjector.refreshPage(viewGroup, 18);
            }
        }
    }

    private static android.view.View mailRow(android.content.Context context, com.google.gson.JsonObject jsonObject, int i, android.view.ViewGroup viewGroup) {
        java.lang.String str;
        android.widget.LinearLayout linearLayout = new android.widget.LinearLayout(context);
        linearLayout.setOrientation(0);
        linearLayout.setGravity(16);
        android.graphics.drawable.GradientDrawable gradientDrawable = new android.graphics.drawable.GradientDrawable();
        gradientDrawable.setShape(0);
        gradientDrawable.setColor(SURFACE_CONTAINER);
        gradientDrawable.setCornerRadius(dpLite(12.0f));
        linearLayout.setBackground(gradientDrawable);
        android.widget.LinearLayout.LayoutParams layoutParamsRootLp = rootLp();
        layoutParamsRootLp.leftMargin = dp(context, 12.0f);
        layoutParamsRootLp.rightMargin = dp(context, 12.0f);
        layoutParamsRootLp.bottomMargin = dp(context, 6.0f);
        linearLayout.setLayoutParams(layoutParamsRootLp);
        linearLayout.setPadding(dp(context, 12.0f), dp(context, 10.0f), dp(context, 12.0f), dp(context, 10.0f));
        android.widget.LinearLayout linearLayout2 = new android.widget.LinearLayout(context);
        linearLayout2.setOrientation(1);
        linearLayout2.setLayoutParams(new android.widget.LinearLayout.LayoutParams(0, -2, 1.0f));
        java.lang.String str2 = str(jsonObject, "fromName");
        if (str2.isEmpty()) {
            str2 = str(jsonObject, "fromAddr");
        }
        if (str2.isEmpty()) {
            str2 = "(未知发件人)";
        }
        android.widget.TextView textViewText = text(context, str2, TITLE, 14.0f, 8388611);
        textViewText.setSingleLine(true);
        textViewText.setEllipsize(android.text.TextUtils.TruncateAt.END);
        java.lang.String str3 = str(jsonObject, "subject");
        if (str3.isEmpty()) {
            str3 = "(无主题)";
        }
        int size = mailAtts(jsonObject).size();
        if (size > 0) {
            str = "〔附件" + size + "〕";
        } else {
            str = "";
        }
        if (bool(jsonObject, "hasHtml", false)) {
            str = str + "〔网页〕";
        }
        android.widget.TextView textViewText2 = text(context, str + str3, -6643546, 12.0f, 8388611);
        textViewText2.setSingleLine(true);
        textViewText2.setEllipsize(android.text.TextUtils.TruncateAt.END);
        linearLayout2.addView(textViewText, rootLp());
        linearLayout2.addView(textViewText2, rootLp());
        linearLayout.addView(linearLayout2);
        android.widget.LinearLayout linearLayout3 = new android.widget.LinearLayout(context);
        linearLayout3.setOrientation(1);
        linearLayout3.setGravity(5);
        linearLayout3.addView(text(context, str(jsonObject, "timeStr"), HINT, 10.0f, 5), rootLp());
        if (!bool(jsonObject, "read", false)) {
            linearLayout3.addView(redDot(context));
        }
        linearLayout.addView(linearLayout3);
        linearLayout.setClickable(true);
        linearLayout.setOnClickListener(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda133(i, viewGroup));
        return linearLayout;
    }

    static /* synthetic */ void lambda$mailRow$150(int i, android.view.ViewGroup viewGroup, android.view.View view) {
        sMailDetailIndex = i;
        openPage(viewGroup, 19);
    }

    private static void buildMailWebBg(android.content.Context context, android.view.ViewGroup viewGroup) {
        viewGroup.addView(mailBackRow(context, viewGroup, 18));
        viewGroup.addView(section(context, "网页渲染背景图"));
        java.lang.String strMailWebBg = com.tencent.qqlitepro.util.Config.mailWebBg(context);
        boolean z = !strMailWebBg.isEmpty() && new java.io.File(strMailWebBg).exists();
        android.widget.LinearLayout linearLayoutFilledCard = filledCard(context);
        linearLayoutFilledCard.addView(text(context, z ? "当前：已使用自定义背景图" : "当前：默认白底（未设置背景图）", TITLE, 13.0f, 8388611), rootLp());
        if (z) {
            try {
                android.graphics.Bitmap bitmapDecodeFile = android.graphics.BitmapFactory.decodeFile(strMailWebBg);
                if (bitmapDecodeFile != null) {
                    android.widget.ImageView imageView = new android.widget.ImageView(context);
                    imageView.setImageBitmap(bitmapDecodeFile);
                    imageView.setScaleType(android.widget.ImageView.ScaleType.CENTER_CROP);
                    android.widget.LinearLayout.LayoutParams layoutParamsRootLp = rootLp();
                    layoutParamsRootLp.height = dp(context, 130.0f);
                    layoutParamsRootLp.topMargin = dp(context, 6.0f);
                    imageView.setClickable(true);
                    imageView.setOnClickListener(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda10(context, strMailWebBg));
                    linearLayoutFilledCard.addView(imageView, layoutParamsRootLp);
                }
            } catch (java.lang.Throwable unused) {
            }
        }
        linearLayoutFilledCard.addView(text(context, "背景图按视口等比铺满邮件网页，正文保持透明、文字加同心光晕，确保清晰可读；点击缩略图可全屏查看。", HINT, 11.0f, 8388611), rootLp());
        viewGroup.addView(linearLayoutFilledCard);
        makeGroupGap(viewGroup);
        viewGroup.addView(saveButton(context, z ? "更换底图" : "选择底图", new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda11()));
        viewGroup.addView(gap(context, 8.0f));
        viewGroup.addView(saveButton(context, "恢复默认", new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda12(context, viewGroup)));
    }

    static /* synthetic */ void lambda$buildMailWebBg$153(android.content.Context context, android.view.ViewGroup viewGroup) {
        com.tencent.qqlitepro.util.Config.setMailWebBg(context, "");
        try {
            java.io.File[] fileArrListFiles = new java.io.File(context.getFilesDir(), "mail_bg").listFiles();
            if (fileArrListFiles != null) {
                for (java.io.File file : fileArrListFiles) {
                    file.delete();
                }
            }
        } catch (java.lang.Throwable unused) {
        }
        toast(context, "已恢复默认白底");
        refreshPage(viewGroup, 22);
    }

    private static long copyUriToFile(android.content.Context context, android.net.Uri uri, java.io.File file) throws java.lang.Exception {
        java.io.InputStream inputStreamOpenInputStream;
        if ("file".equals(uri.getScheme())) {
            inputStreamOpenInputStream = new java.io.FileInputStream(new java.io.File(uri.getPath()));
        } else {
            inputStreamOpenInputStream = context.getContentResolver().openInputStream(uri);
        }
        if (inputStreamOpenInputStream == null) {
            throw new java.lang.RuntimeException("无法读取所选文件");
        }
        java.io.FileOutputStream fileOutputStream = new java.io.FileOutputStream(file);
        try {
            byte[] bArr = new byte[8192];
            long j = 0;
            while (true) {
                int i = inputStreamOpenInputStream.read(bArr);
                if (i > 0) {
                    fileOutputStream.write(bArr, 0, i);
                    j += (long) i;
                } else {
                    try {
                        break;
                    } catch (java.lang.Throwable unused) {
                    }
                }
            }
            inputStreamOpenInputStream.close();
            try {
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (java.lang.Throwable unused2) {
            }
            return j;
        } catch (java.lang.Throwable thRethrow14) {
            try {
                inputStreamOpenInputStream.close();
            } catch (java.lang.Throwable unused3) {
            }
            try {
                fileOutputStream.flush();
                fileOutputStream.close();
                throw thRethrow14;
            } catch (java.lang.Throwable unused4) {
                throw thRethrow14;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void handleMailBgPicked(android.app.Activity activity, android.net.Uri uri) {
        android.content.Context context = null;
        if (uri == null) {
            return;
        }
        if (activity != null) {
            context = activity;
        } else {
            android.view.ViewGroup viewGroup = sPageGroup;
            if (viewGroup == null) {
                return;
            }
            context = viewGroup.getContext();
        }
        if (context == null) {
            return;
        }
        android.os.Handler handler = new android.os.Handler(android.os.Looper.getMainLooper());
        toast(context, "正在保存背景图...");
        new java.lang.Thread(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda210(context, uri, handler), "mail-bg-pick").start();
    }

    static /* synthetic */ void lambda$handleMailBgPicked$156(android.content.Context context, android.net.Uri uri, android.os.Handler handler) {
        try {
            java.io.File file = new java.io.File(context.getFilesDir(), "mail_bg");
            if (!file.exists() && !file.mkdirs()) {
                throw new java.lang.RuntimeException("无法创建目录");
            }
            java.io.File[] fileArrListFiles = file.listFiles();
            if (fileArrListFiles != null) {
                for (java.io.File file2 : fileArrListFiles) {
                    file2.delete();
                }
            }
            java.lang.String strUriDisplayName = uriDisplayName(context, uri);
            if (strUriDisplayName == null || strUriDisplayName.isEmpty()) {
                strUriDisplayName = "bg.png";
            }
            java.io.File file3 = new java.io.File(file, java.lang.System.currentTimeMillis() + "_" + strUriDisplayName.replaceAll("[\\\\/:*?\"<>|\\r\\n\\t]", "_"));
            if (copyUriToFile(context, uri, file3) <= 0) {
                throw new java.lang.RuntimeException("图片内容为空");
            }
            com.tencent.qqlitepro.util.Config.setMailWebBg(context, file3.getAbsolutePath());
            handler.post(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda195(context));
        } catch (java.lang.Throwable thRethrow14) {
            java.lang.String strValueOf = java.lang.String.valueOf(thRethrow14);
            com.tencent.qqlitepro.util.Log.w("设置网页渲染背景图失败: " + strValueOf);
            handler.post(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda196(context, strValueOf));
        }
    }

    static /* synthetic */ void lambda$handleMailBgPicked$154(android.content.Context context) {
        toast(context, "背景图已设置");
        if (sPageGroup == null || sCurrentPage != 22) {
            return;
        }
        refreshPage(sPageGroup, 22);
    }

    private static void buildMailDetail(android.content.Context context, android.view.ViewGroup viewGroup) {
        long asLong = 0L;
        viewGroup.addView(mailBackRow(context, viewGroup, 18));
        viewGroup.addView(section(context, "邮件详情"));
        int i = sMailDetailIndex;
        java.util.List<com.google.gson.JsonObject> mailCacheList = parseMailCacheList(com.tencent.qqlitepro.util.Config.mailInboxCache(context));
        if (i < 0 || i >= mailCacheList.size()) {
            viewGroup.addView(text(context, "邮件不存在或缓存已更新", HINT, 12.0f, 8388611));
            return;
        }
        com.google.gson.JsonObject jsonObject = mailCacheList.get(i);
        markMailRead(context, i);
        long asLong2 = 0;
        if (!jsonObject.has("hasHtml") || !jsonObject.has("atts")) {
            try {
                asLong = jsonObject.has("uid") ? jsonObject.get("uid").getAsLong() : 0L;
            } catch (java.lang.Throwable unused) {
            }
            if (asLong > 0) {
                viewGroup.addView(text(context, "正在解析该邮件的网页正文与附件，请稍候...", HINT, 12.0f, 8388611));
                new java.lang.Thread(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda36(context, asLong, new android.os.Handler(android.os.Looper.getMainLooper())), "mail-upgrade").start();
            }
        }
        com.google.gson.JsonArray jsonArrayMailAtts = mailAtts(jsonObject);
        java.lang.String str = str(jsonObject, "contentType");
        if (bool(jsonObject, "hasHtml", false)) {
            str = str.isEmpty() ? "text/html（网页）" : str + "（网页）";
        }
        if (jsonArrayMailAtts.size() > 0) {
            str = str + " · 含 " + jsonArrayMailAtts.size() + " 个附件/图片";
        }
        android.widget.LinearLayout linearLayoutFilledCard = filledCard(context);
        linearLayoutFilledCard.addView(label(context, "发件人", str(jsonObject, "fromName")), rootLp());
        linearLayoutFilledCard.addView(label(context, "发件邮箱", str(jsonObject, "fromAddr")), rootLp());
        linearLayoutFilledCard.addView(label(context, "收件人", str(jsonObject, "to")), rootLp());
        linearLayoutFilledCard.addView(label(context, "主题", str(jsonObject, "subject")), rootLp());
        linearLayoutFilledCard.addView(label(context, "发送时间", str(jsonObject, "timeStr")), rootLp());
        linearLayoutFilledCard.addView(label(context, "内容类型", str), rootLp());
        viewGroup.addView(linearLayoutFilledCard);
        makeGroupGap(viewGroup);
        try {
            if (jsonObject.has("uid")) {
                asLong2 = jsonObject.get("uid").getAsLong();
            }
        } catch (java.lang.Throwable unused2) {
        }
        java.lang.String strNormalizePlainBody = normalizePlainBody(str(jsonObject, "body"));
        java.lang.String str2 = str(jsonObject, "htmlPath");
        boolean z = !str2.isEmpty() && new java.io.File(str2).isFile();
        if (!z && looksLikeHtml(strNormalizePlainBody)) {
            str2 = com.tencent.qqlitepro.mail.QqMailManager.writeHtmlBody(context, asLong2, strNormalizePlainBody);
            z = !str2.isEmpty() && new java.io.File(str2).isFile();
        }
        if (z) {
            viewGroup.addView(section(context, "正文（网页）"));
            viewGroup.addView(mailHtmlLinkRow(context, str2, asLong2));
            makeGroupGap(viewGroup);
        }
        viewGroup.addView(section(context, z ? "正文文本（长按可复制）" : "正文（长按可复制）"));
        if (strNormalizePlainBody.isEmpty()) {
            strNormalizePlainBody = "(无正文内容)";
        }
        android.view.View viewSelectableText = selectableText(context, strNormalizePlainBody, TITLE, 13.0f);
        android.widget.LinearLayout.LayoutParams layoutParamsRootLp = rootLp();
        layoutParamsRootLp.leftMargin = dp(context, 12.0f);
        layoutParamsRootLp.rightMargin = dp(context, 12.0f);
        viewGroup.addView(viewSelectableText, layoutParamsRootLp);
        if (jsonArrayMailAtts.size() > 0) {
            makeGroupGap(viewGroup);
            viewGroup.addView(section(context, "附件与图片（可查看 / 下载）"));
            for (int i2 = 0; i2 < jsonArrayMailAtts.size(); i2++) {
                try {
                    viewGroup.addView(mailAttRow(context, jsonArrayMailAtts.get(i2).getAsJsonObject()));
                } catch (java.lang.Throwable unused3) {
                }
            }
        }
    }

    static /* synthetic */ void lambda$buildMailDetail$157(boolean z, android.content.Context context) {
        if (sPageGroup == null || sCurrentPage != 19) {
            return;
        }
        if (z) {
            refreshPage(sPageGroup, 19);
        } else {
            toast(context, "该邮件内容解析失败（可能已被删除）");
        }
    }

    private static boolean looksLikeHtml(java.lang.String str) {
        if (str == null || str.length() < 12) {
            return false;
        }
        java.lang.String lowerCase = str.toLowerCase(java.util.Locale.ROOT);
        if (!lowerCase.contains("<html") && !lowerCase.contains("<!doctype") && !lowerCase.contains("<body") && !lowerCase.contains("<div") && !lowerCase.contains("<table") && !lowerCase.contains("<span") && !lowerCase.contains("<font") && !lowerCase.contains("<p>") && !lowerCase.contains("<br")) {
            return false;
        }
        java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("<[a-zA-Z!/][^>]{0,200}>").matcher(lowerCase);
        int i = 0;
        while (matcher.find()) {
            i++;
            if (i >= 3) {
                return true;
            }
        }
        return i >= 1 && lowerCase.contains("</");
    }

    private static java.lang.String normalizePlainBody(java.lang.String str) {
        int i;
        if (str == null || str.isEmpty()) {
            return "";
        }
        java.lang.String strReplace = str.replace("\r\n", "\n").replace('\r', '\n');
        try {
            java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("&#(?:x([0-9a-fA-F]{1,6})|(\\d{1,7}));").matcher(strReplace);
            java.lang.StringBuffer stringBuffer = new java.lang.StringBuffer();
            while (matcher.find()) {
                try {
                    i = matcher.group(1) != null ? java.lang.Integer.parseInt(matcher.group(1), 16) : java.lang.Integer.parseInt(matcher.group(2));
                } catch (java.lang.Throwable unused) {
                    i = -1;
                }
                matcher.appendReplacement(stringBuffer, java.util.regex.Matcher.quoteReplacement((i <= 0 || i > 1114111) ? "" : new java.lang.String(java.lang.Character.toChars(i))));
            }
            matcher.appendTail(stringBuffer);
            strReplace = stringBuffer.toString();
        } catch (java.lang.Throwable unused2) {
        }
        java.lang.String strReplace2 = strReplace.replace("&nbsp;", " ").replace("&amp;", "&").replace("&lt;", "<").replace("&gt;", ">").replace("&quot;", "\"").replace("&#39;", "'");
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        boolean z = true;
        for (java.lang.String str2 : strReplace2.split("\n", -1)) {
            java.lang.String strTrim = str2.replace('\t', ' ').replaceAll("[ \\u00A0\\u3000]+", " ").trim();
            if (strTrim.isEmpty()) {
                if (sb.length() != 0 && !z) {
                    z = true;
                }
            } else {
                z = false;
            }
            sb.append(strTrim);
            sb.append('\n');
        }
        return sb.toString().trim();
    }

    private static com.google.gson.JsonArray mailAtts(com.google.gson.JsonObject jsonObject) {
        try {
            if (jsonObject.has("atts") && jsonObject.get("atts").isJsonArray()) {
                return jsonObject.getAsJsonArray("atts");
            }
        } catch (java.lang.Throwable unused) {
        }
        return new com.google.gson.JsonArray();
    }

    private static android.view.View mailHtmlLinkRow(android.content.Context context, java.lang.String str, long j) {
        android.widget.LinearLayout linearLayout = new android.widget.LinearLayout(context);
        linearLayout.setOrientation(1);
        android.graphics.drawable.GradientDrawable gradientDrawable = new android.graphics.drawable.GradientDrawable();
        gradientDrawable.setShape(0);
        gradientDrawable.setColor(SURFACE_CONTAINER);
        gradientDrawable.setCornerRadius(dpLite(12.0f));
        linearLayout.setBackground(gradientDrawable);
        android.widget.LinearLayout.LayoutParams layoutParamsRootLp = rootLp();
        layoutParamsRootLp.leftMargin = dp(context, 12.0f);
        layoutParamsRootLp.rightMargin = dp(context, 12.0f);
        layoutParamsRootLp.bottomMargin = dp(context, 6.0f);
        linearLayout.setLayoutParams(layoutParamsRootLp);
        linearLayout.setPadding(dp(context, 12.0f), dp(context, 10.0f), dp(context, 12.0f), dp(context, 10.0f));
        java.lang.String name = new java.io.File(str).getName();
        android.widget.TextView textViewText = text(context, name.isEmpty() ? "body.html" : name, -13669408, 13.0f, 8388611);
        textViewText.setPaintFlags(textViewText.getPaintFlags() | 8);
        textViewText.setSingleLine(true);
        textViewText.setEllipsize(android.text.TextUtils.TruncateAt.MIDDLE);
        linearLayout.addView(textViewText, rootLp());
        linearLayout.addView(text(context, "点击用浏览器打开阅读", HINT, 11.0f, 8388611), rootLp());
        linearLayout.setOnClickListener(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda214(j, name));
        return linearLayout;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void openMailHtmlInBrowser(android.content.Context context, long j, java.lang.String str) {
        if (context == null) {
            return;
        }
        try {
            com.tencent.qqlitepro.http.HttpApiServer.start();
            int port = com.tencent.qqlitepro.http.HttpApiServer.getPort();
            java.lang.StringBuilder sb = new java.lang.StringBuilder("http://127.0.0.1:");
            sb.append(port);
            sb.append("/mail_html?uid=");
            sb.append(j);
            sb.append("&name=");
            if (str == null || str.isEmpty()) {
                str = "body.html";
            }
            sb.append(java.net.URLEncoder.encode(str, "UTF-8"));
            openBrowserChooser(context, sb.toString());
        } catch (java.lang.Throwable thRethrow14) {
            com.tencent.qqlitepro.util.Log.w("打开邮件网页失败: " + thRethrow14);
            toast(context, "打开失败：" + thRethrow14);
        }
    }

    public static java.lang.String wrapHtmlWithCss(java.lang.String str, java.lang.String str2, java.lang.String str3) {
        java.lang.String str4;
        java.lang.String str5;
        java.lang.String str6;
        int iIndexOf;
        boolean z = true;
        boolean z2 = (str2 == null || str2.isEmpty()) ? false : true;
        if (z2) {
            str5 = "html::before{background:url('" + str2.replace("'", "%27") + "') center center / cover no-repeat;}";
            str4 = "html{background:#ffffff !important;color-scheme:light !important;}html::before{content:'';position:fixed;top:0;left:0;right:0;bottom:0;z-index:-1;pointer-events:none;}";
        } else {
            str4 = "html{background:#f4f5f7 !important;color-scheme:light !important;}";
            str5 = "";
        }
        if (str3 == null || str3.isEmpty()) {
            str6 = "";
        } else {
            str6 = "<base href=\"" + str3.replace("\"", "%22") + "\">";
        }
        java.lang.String str7 = str6 + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"><meta name=\"color-scheme\" content=\"light\"><style>html{-webkit-text-size-adjust:none;text-size-adjust:none;}" + str4 + str5 + "</style>";
        java.lang.String str8 = z2 ? "body{margin:0 !important;padding:16px 14px !important;border-radius:0 !important;background:transparent !important;color:#0d0e11 !important;box-sizing:border-box !important;word-break:break-word !important;overflow-wrap:break-word !important;font-family:-apple-system,'Noto Sans CJK SC','Source Han Sans SC','PingFang SC',sans-serif !important;}" : "body{margin:10px 8px !important;padding:12px 11px !important;border-radius:12px !important;background:#ffffff !important;color:#1b1c1f !important;box-sizing:border-box !important;word-break:break-word !important;overflow-wrap:break-word !important;font-family:-apple-system,'Noto Sans CJK SC','Source Han Sans SC','PingFang SC',sans-serif !important;}";
        java.lang.String str9 = z2 ? "text-shadow:0 0 2px rgba(255,255,255,0.5) !important;" : "";
        java.lang.String str10 = "<style>" + str4 + "*{-webkit-user-select:text !important;user-select:text !important;}html{-webkit-text-size-adjust:none !important;text-size-adjust:none !important;color-scheme:light !important;}" + str8 + (z2 ? "body,body *{background-color:transparent !important;background-image:none !important;}" : "") + "body,body *{font-size:13px !important;line-height:1.72 !important;max-width:100% !important;" + str9 + "box-sizing:border-box !important;}p{margin:0 0 10px !important;}h1,h2,h3,h4,h5,h6{line-height:1.4 !important;margin:14px 0 8px !important;font-weight:600 !important;color:#101114 !important;}h1{font-size:17px !important;}h2{font-size:16px !important;}h3{font-size:15px !important;}h4,h5,h6{font-size:14px !important;}h1:first-child,h2:first-child,h3:first-child,h4:first-child,p:first-child{margin-top:0 !important;}b,strong{font-weight:600 !important;color:#101114 !important;}img{max-width:100% !important;height:auto !important;border-radius:8px !important;margin:6px 0 !important;vertical-align:middle !important;}a{color:#2F6BE0 !important;text-decoration:none !important;word-break:break-all !important;}blockquote{margin:10px 0 !important;padding:8px 10px !important;border-left:3px solid #2F6BE0 !important;border-radius:0 8px 8px 0 !important;background:#f2f4f8 !important;color:#4a4f5a !important;}pre{white-space:pre-wrap !important;word-break:break-word !important;background:#f2f4f8 !important;border-radius:8px !important;padding:8px 9px !important;font-family:'Roboto Mono',monospace !important;font-size:12px !important;}code{background:#f2f4f8 !important;border-radius:5px !important;padding:1px 4px !important;font-family:'Roboto Mono',monospace !important;font-size:12px !important;}table{width:100% !important;max-width:100% !important;table-layout:fixed !important;border-collapse:collapse !important;margin:8px 0 !important;}td,th{padding:5px 6px !important;border:1px solid #e6e8ee !important;word-break:break-word !important;vertical-align:top !important;}th{background:#f2f4f8 !important;font-weight:600 !important;}ul,ol{padding-left:18px !important;margin:0 0 10px !important;}li{margin:3px 0 !important;}hr{border:0 !important;border-top:1px dashed #dfe2ea !important;margin:14px 0 !important;}font{font-size:inherit !important;}</style>";
        try {
            str = str.replaceAll("(?is)<meta[^>]*name\\s*=\\s*[\"']?viewport[\"']?[^>]*>", "");
        } catch (java.lang.Throwable unused) {
        }
        int iIndexOfIgnoreCase = indexOfIgnoreCase(str, "<head>");
        if (iIndexOfIgnoreCase >= 0) {
            int i = iIndexOfIgnoreCase + 6;
            str = str.substring(0, i) + str7 + str.substring(i);
        } else {
            int iIndexOfIgnoreCase2 = indexOfIgnoreCase(str, "<html");
            if (iIndexOfIgnoreCase2 < 0 || (iIndexOf = str.indexOf(62, iIndexOfIgnoreCase2)) <= 0) {
                z = false;
            } else {
                java.lang.StringBuilder sb = new java.lang.StringBuilder();
                int i2 = iIndexOf + 1;
                sb.append(str.substring(0, i2));
                sb.append("<head>");
                sb.append(str7);
                sb.append("</head>");
                sb.append(str.substring(i2));
                str = sb.toString();
            }
        }
        if (!z) {
            str = "<!DOCTYPE html><html><head>" + str7 + "</head><body>" + str + "</body></html>";
        }
        int iIndexOfIgnoreCase3 = indexOfIgnoreCase(str, "</body>");
        if (iIndexOfIgnoreCase3 >= 0) {
            return str.substring(0, iIndexOfIgnoreCase3) + str10 + str.substring(iIndexOfIgnoreCase3);
        }
        int iIndexOfIgnoreCase4 = indexOfIgnoreCase(str, "</html>");
        if (iIndexOfIgnoreCase4 >= 0) {
            return str.substring(0, iIndexOfIgnoreCase4) + str10 + str.substring(iIndexOfIgnoreCase4);
        }
        return str + str10;
    }

    private static int indexOfIgnoreCase(java.lang.String str, java.lang.String str2) {
        if (str == null || str2 == null) {
            return -1;
        }
        return str.toLowerCase(java.util.Locale.ROOT).indexOf(str2.toLowerCase(java.util.Locale.ROOT));
    }

    private static android.view.View mailAttRow(android.content.Context context, com.google.gson.JsonObject jsonObject) {
        android.widget.LinearLayout linearLayout = new android.widget.LinearLayout(context);
        linearLayout.setOrientation(1);
        android.graphics.drawable.GradientDrawable gradientDrawable = new android.graphics.drawable.GradientDrawable();
        gradientDrawable.setShape(0);
        gradientDrawable.setColor(SURFACE_CONTAINER);
        gradientDrawable.setCornerRadius(dpLite(12.0f));
        linearLayout.setBackground(gradientDrawable);
        android.widget.LinearLayout.LayoutParams layoutParamsRootLp = rootLp();
        layoutParamsRootLp.leftMargin = dp(context, 12.0f);
        layoutParamsRootLp.rightMargin = dp(context, 12.0f);
        layoutParamsRootLp.bottomMargin = dp(context, 6.0f);
        linearLayout.setLayoutParams(layoutParamsRootLp);
        linearLayout.setPadding(dp(context, 12.0f), dp(context, 10.0f), dp(context, 12.0f), dp(context, 8.0f));
        java.lang.String str = str(jsonObject, com.sun.mail.imap.IMAPStore.ID_NAME);
        boolean zBool = bool(jsonObject, "image", false);
        boolean zBool2 = bool(jsonObject, javax.mail.Part.INLINE, false);
        long asLong = 0;
        try {
            if (jsonObject.has("size")) {
                asLong = jsonObject.get("size").getAsLong();
            }
        } catch (java.lang.Throwable unused) {
        }
        java.lang.String str2 = str(jsonObject, "path");
        java.lang.String strHumanSize = humanSize(asLong);
        java.lang.String str3 = str(jsonObject, "mime");
        if (!str3.isEmpty()) {
            strHumanSize = strHumanSize + " · " + str3;
        }
        if (zBool2) {
            strHumanSize = strHumanSize + " · 内嵌";
        }
        android.widget.TextView textViewText = text(context, str.isEmpty() ? "(未命名)" : str, TITLE, 13.0f, 8388611);
        textViewText.setSingleLine(true);
        textViewText.setEllipsize(android.text.TextUtils.TruncateAt.MIDDLE);
        linearLayout.addView(textViewText, rootLp());
        linearLayout.addView(text(context, strHumanSize, HINT, 11.0f, 8388611), rootLp());
        android.widget.LinearLayout linearLayout2 = new android.widget.LinearLayout(context);
        linearLayout2.setOrientation(0);
        linearLayout2.setGravity(16);
        android.widget.LinearLayout.LayoutParams layoutParamsRootLp2 = rootLp();
        layoutParamsRootLp2.topMargin = dp(context, 6.0f);
        linearLayout2.setLayoutParams(layoutParamsRootLp2);
        if (!str2.isEmpty() && new java.io.File(str2).exists()) {
            if (zBool) {
                linearLayout2.addView(mailSmallBtn(context, "查看", new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda220(context, str2, str)));
            }
            linearLayout2.addView(mailSmallBtn(context, "下载", new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda221(context, str, str2)));
        } else {
            linearLayout2.addView(text(context, "（内容未缓存：超过大小上限或读取失败）", HINT, 11.0f, 8388611));
        }
        linearLayout.addView(linearLayout2);
        return linearLayout;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void viewMailImage(android.content.Context context, java.lang.String str, java.lang.String str2) {
        try {
            android.graphics.Bitmap bitmapDecodeFile = android.graphics.BitmapFactory.decodeFile(str);
            if (bitmapDecodeFile == null) {
                toast(context, "无法解码该图片");
                return;
            }
            android.widget.ImageView imageView = new android.widget.ImageView(context);
            imageView.setImageBitmap(bitmapDecodeFile);
            imageView.setScaleType(android.widget.ImageView.ScaleType.FIT_CENTER);
            imageView.setBackgroundColor(-16777216);
            imageView.setAdjustViewBounds(true);
            new android.app.AlertDialog.Builder(context).setTitle(str2.isEmpty() ? "图片预览" : str2).setView(imageView).setPositiveButton("下载", new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda26(context, str2, str)).setNegativeButton("关闭", (android.content.DialogInterface.OnClickListener) null).show();
        } catch (java.lang.Throwable thRethrow14) {
            toast(context, "预览失败：" + thRethrow14);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void downloadMailAtt(android.content.Context context, java.lang.String str, java.lang.String str2) {
        if (str2 == null || str2.isEmpty() || !new java.io.File(str2).exists()) {
            toast(context, "附件内容未缓存，请刷新收件箱后重试");
            return;
        }
        android.os.Handler handler = new android.os.Handler(android.os.Looper.getMainLooper());
        toast(context, "正在保存...");
        new java.lang.Thread(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda190(context, str2, str, handler), "mail-att-download").start();
    }

    static /* synthetic */ void lambda$downloadMailAtt$165(android.content.Context context, java.lang.String str, java.lang.String str2, android.os.Handler handler) {
        try {
            java.io.File file = new java.io.File(com.tencent.qqlitepro.util.Config.downloadDir(context));
            if (!file.exists() && !file.mkdirs()) {
                throw new java.lang.RuntimeException("无法创建保存目录");
            }
            java.io.File file2 = new java.io.File(str);
            if (str2 == null || str2.isEmpty()) {
                str2 = file2.getName();
            }
            java.lang.String strReplaceAll = str2.replaceAll("[\\\\/:*?\"<>|\\r\\n\\t]", "_");
            java.io.File file3 = new java.io.File(file, strReplaceAll);
            int i = 1;
            while (file3.exists()) {
                file3 = new java.io.File(file, i + "_" + strReplaceAll);
                i++;
            }
            java.io.FileInputStream fileInputStream = new java.io.FileInputStream(file2);
            java.io.FileOutputStream fileOutputStream = new java.io.FileOutputStream(file3);
            try {
                byte[] bArr = new byte[8192];
                while (true) {
                    int i2 = fileInputStream.read(bArr);
                    if (i2 > 0) {
                        fileOutputStream.write(bArr, 0, i2);
                    } else {
                        try {
                            break;
                        } catch (java.lang.Throwable unused) {
                        }
                    }
                }
                fileInputStream.close();
                try {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                } catch (java.lang.Throwable unused2) {
                }
                handler.post(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda59(context, file3.getAbsolutePath()));
            } catch (java.lang.Throwable thRethrow14) {
                try {
                    fileInputStream.close();
                } catch (java.lang.Throwable unused3) {
                }
                try {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    throw thRethrow14;
                } catch (java.lang.Throwable unused4) {
                    throw thRethrow14;
                }
            }
        } catch (java.lang.Throwable th2) {
            handler.post(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda60(context, java.lang.String.valueOf(th2)));
        }
    }

    private static void markMailRead(android.content.Context context, int i) {
        try {
            java.util.List<com.google.gson.JsonObject> mailCacheList = parseMailCacheList(com.tencent.qqlitepro.util.Config.mailInboxCache(context));
            if (i >= 0 && i < mailCacheList.size()) {
                com.google.gson.JsonObject jsonObject = mailCacheList.get(i);
                if (!bool(jsonObject, "read", false)) {
                    jsonObject.addProperty("read", (java.lang.Boolean) true);
                    com.tencent.qqlitepro.util.Config.setMailInbox(context, cacheToJson(mailCacheList), java.lang.Math.max(0, com.tencent.qqlitepro.util.Config.mailUnreadCount(context) - 1), com.tencent.qqlitepro.util.Config.mailLastUid(context));
                }
            }
        } catch (java.lang.Throwable unused) {
        }
        com.tencent.qqlitepro.hook.ChatCardInjector.refreshChatList();
    }

    private static void buildMailSend(android.content.Context context, android.view.ViewGroup viewGroup) {
        android.widget.EditText editText;
        int i;
        viewGroup.addView(backRow(context));
        viewGroup.addView(section(context, "发送邮件"));
        android.widget.EditText[] editTextArr = new android.widget.EditText[1];
        android.widget.LinearLayout linearLayoutFilledCard = filledCard(context);
        linearLayoutFilledCard.addView(text(context, "收件人", -6643546, 12.0f, 8388611), rootLp());
        android.widget.EditText editTextEdit = edit(context, sMailToTxt, "多个邮箱用英文逗号分隔", 1);
        linearLayoutFilledCard.addView(editInCard(context, editTextEdit), inputLp());
        linearLayoutFilledCard.addView(text(context, "主题", -6643546, 12.0f, 8388611), rootLp());
        android.widget.EditText editTextEdit2 = edit(context, sMailSubjTxt, "邮件主题", 1);
        linearLayoutFilledCard.addView(editInCard(context, editTextEdit2), inputLp());
        linearLayoutFilledCard.addView(text(context, "格式", -6643546, 12.0f, 8388611), rootLp());
        android.widget.LinearLayout linearLayout = new android.widget.LinearLayout(context);
        char c = 0;
        linearLayout.setOrientation(0);
        linearLayout.setGravity(16);
        android.widget.LinearLayout.LayoutParams layoutParamsRootLp = rootLp();
        layoutParamsRootLp.topMargin = dp(context, 4.0f);
        layoutParamsRootLp.bottomMargin = dp(context, 10.0f);
        linearLayout.setLayoutParams(layoutParamsRootLp);
        android.view.View viewMailChip = mailChip(context, "纯文本", !sMailHtmlMode, new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda98(editTextEdit, editTextEdit2, editTextArr, viewGroup));
        android.view.View viewMailChip2 = mailChip(context, "HTML 网页", sMailHtmlMode, new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda99(editTextEdit, editTextEdit2, editTextArr, viewGroup));
        linearLayout.addView(viewMailChip, new android.widget.LinearLayout.LayoutParams(0, -2, 1.0f));
        android.widget.LinearLayout.LayoutParams layoutParams = new android.widget.LinearLayout.LayoutParams(0, -2, 1.0f);
        layoutParams.leftMargin = dp(context, 6.0f);
        linearLayout.addView(viewMailChip2, layoutParams);
        linearLayoutFilledCard.addView(linearLayout);
        linearLayoutFilledCard.addView(text(context, "正文", -6643546, 12.0f, 8388611), rootLp());
        android.widget.EditText editTextEdit3 = edit(context, sMailBodyTxt, sMailHtmlMode ? "HTML 源码，如 <h3>标题</h3><p>正文</p>" : "邮件正文", 3);
        editTextEdit3.setMaxLines(6);
        editTextEdit3.setVerticalScrollBarEnabled(true);
        if (sMailHtmlMode) {
            editTextEdit3.setInputType(655361);
        }
        linearLayoutFilledCard.addView(editInCard(context, editTextEdit3), inputLp());
        editTextArr[0] = editTextEdit3;
        viewGroup.addView(linearLayoutFilledCard);
        sMailToEdit = editTextEdit;
        sMailSubjEdit = editTextEdit2;
        sMailBodyEdit = editTextEdit3;
        viewGroup.addView(section(context, "附件与图片"));
        android.widget.LinearLayout linearLayout2 = new android.widget.LinearLayout(context);
        linearLayout2.setOrientation(0);
        linearLayout2.setGravity(16);
        android.widget.LinearLayout.LayoutParams layoutParamsRootLp2 = rootLp();
        layoutParamsRootLp2.leftMargin = dp(context, 12.0f);
        layoutParamsRootLp2.rightMargin = dp(context, 12.0f);
        linearLayout2.setLayoutParams(layoutParamsRootLp2);
        android.view.View viewMailChip3 = mailChip(context, "＋ 添加附件", false, new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda100());
        android.view.View viewMailChip4 = mailChip(context, "＋ 添加图片", false, new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda102());
        linearLayout2.addView(viewMailChip3, new android.widget.LinearLayout.LayoutParams(0, -2, 1.0f));
        android.widget.LinearLayout.LayoutParams layoutParams2 = new android.widget.LinearLayout.LayoutParams(0, -2, 1.0f);
        layoutParams2.leftMargin = dp(context, 6.0f);
        linearLayout2.addView(viewMailChip4, layoutParams2);
        viewGroup.addView(linearLayout2);
        java.util.List<java.io.File> list = sMailAttach;
        if (list.isEmpty() && sMailImages.isEmpty()) {
            editText = editTextEdit3;
            i = 0;
        } else {
            android.widget.LinearLayout linearLayoutFilledCard2 = filledCard(context);
            linearLayoutFilledCard2.addView(text(context, "已选择 " + (list.size() + sMailImages.size()) + " 个文件（随邮件一并发送）", TITLE, 12.0f, 8388611), rootLp());
            java.util.Iterator<java.io.File> it = list.iterator();
            while (it.hasNext()) {
                linearLayoutFilledCard2.addView(mailPickedRow(context, viewGroup, editTextEdit, editTextEdit2, editTextArr[c], it.next(), false));
                editTextEdit3 = editTextEdit3;
                c = 0;
            }
            editText = editTextEdit3;
            i = 0;
            java.util.Iterator<java.io.File> it2 = sMailImages.iterator();
            while (it2.hasNext()) {
                linearLayoutFilledCard2.addView(mailPickedRow(context, viewGroup, editTextEdit, editTextEdit2, editTextArr[0], it2.next(), true));
            }
            viewGroup.addView(linearLayoutFilledCard2);
        }
        makeGroupGap(viewGroup);
        viewGroup.addView(saveButton(context, "发送邮件", new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda103(editTextEdit, editTextEdit2, editText, context, viewGroup)));
        makeGroupGap(viewGroup);
        android.widget.LinearLayout linearLayout3 = new android.widget.LinearLayout(context);
        linearLayout3.setOrientation(1);
        android.graphics.drawable.GradientDrawable gradientDrawable = new android.graphics.drawable.GradientDrawable();
        gradientDrawable.setShape(i);
        gradientDrawable.setColor(-15461356);
        gradientDrawable.setCornerRadius(dpLite(12.0f));
        gradientDrawable.setStroke(1, -13743516);
        linearLayout3.setBackground(gradientDrawable);
        linearLayout3.setPadding(dp(context, 4.0f), dp(context, 8.0f), dp(context, 4.0f), dp(context, 6.0f));
        android.widget.LinearLayout.LayoutParams layoutParamsRootLp3 = rootLp();
        layoutParamsRootLp3.leftMargin = dp(context, 12.0f);
        layoutParamsRootLp3.rightMargin = dp(context, 12.0f);
        linearLayout3.setLayoutParams(layoutParamsRootLp3);
        android.widget.ScrollView scrollView = new android.widget.ScrollView(context);
        scrollView.setLayoutParams(new android.widget.LinearLayout.LayoutParams(-1, dp(context, 160.0f)));
        android.widget.LinearLayout linearLayout4 = new android.widget.LinearLayout(context);
        linearLayout4.setOrientation(1);
        linearLayout4.setPadding(dp(context, 8.0f), dp(context, 4.0f), dp(context, 8.0f), dp(context, 4.0f));
        scrollView.addView(linearLayout4, rootLp());
        android.widget.TextView textViewText = text(context, mailLogDisplayText(), -7288592, 12.0f, 8388611);
        textViewText.setPadding(dp(context, 8.0f), dp(context, 4.0f), dp(context, 8.0f), dp(context, 4.0f));
        textViewText.setMovementMethod(new android.text.method.ScrollingMovementMethod());
        linearLayout4.addView(textViewText, rootLp());
        linearLayout3.addView(scrollView, rootLp());
        addLogTitleRow(context, linearLayout3, "发送日志", textViewText);
        viewGroup.addView(linearLayout3);
        sMailLogView = textViewText;
        makeGroupGap(viewGroup);
        viewGroup.addView(saveButton(context, "清空日志", new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda104(viewGroup)));
    }

    static /* synthetic */ void lambda$buildMailSend$166(android.widget.EditText editText, android.widget.EditText editText2, android.widget.EditText[] editTextArr, android.view.ViewGroup viewGroup) {
        captureMailInputs(editText, editText2, editTextArr[0]);
        sMailHtmlMode = false;
        refreshPage(viewGroup, 20);
    }

    static /* synthetic */ void lambda$buildMailSend$167(android.widget.EditText editText, android.widget.EditText editText2, android.widget.EditText[] editTextArr, android.view.ViewGroup viewGroup) {
        captureMailInputs(editText, editText2, editTextArr[0]);
        sMailHtmlMode = true;
        refreshPage(viewGroup, 20);
    }

    static /* synthetic */ void lambda$buildMailSend$172(android.widget.EditText editText, android.widget.EditText editText2, android.widget.EditText editText3, android.content.Context context, android.view.ViewGroup viewGroup) {
        java.lang.String str = "";
        java.lang.String strTrim = editText.getText() == null ? "" : editText.getText().toString().trim();
        java.lang.String strTrim2 = editText2.getText() == null ? "" : editText2.getText().toString().trim();
        java.lang.String string = editText3.getText() == null ? "" : editText3.getText().toString();
        captureMailInputs(editText, editText2, editText3);
        if (strTrim.isEmpty()) {
            toast(context, "请填写收件人邮箱");
            return;
        }
        java.util.ArrayList arrayList = new java.util.ArrayList();
        arrayList.addAll(sMailAttach);
        arrayList.addAll(sMailImages);
        if (string.trim().isEmpty() && arrayList.isEmpty()) {
            toast(context, "请填写正文或添加附件");
            return;
        }
        boolean z = sMailHtmlMode;
        java.lang.StringBuilder sb = new java.lang.StringBuilder("发送至 ");
        sb.append(strTrim);
        sb.append("（");
        sb.append(z ? "HTML 网页" : "纯文本");
        if (!arrayList.isEmpty()) {
            str = " + " + arrayList.size() + " 个附件/图片";
        }
        sb.append(str);
        sb.append("）...");
        appendMailLog(sb.toString());
        updateMailLogView();
        new java.lang.Thread(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda173(context, strTrim, strTrim2, string, z, arrayList, new android.os.Handler(android.os.Looper.getMainLooper()), viewGroup), "mail-send").start();
    }

    static /* synthetic */ void lambda$buildMailSend$170(com.tencent.qqlitepro.mail.QqMailManager.SendResult sendResult, android.content.Context context, android.view.ViewGroup viewGroup) {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        sb.append(sendResult.ok ? "✔ 成功：" : "✘ 失败：");
        sb.append(sendResult.message);
        appendMailLog(sb.toString());
        updateMailLogView();
        toast(context, sendResult.ok ? "发送成功" : "发送失败");
        if (sendResult.ok) {
            clearMailSendState();
        }
        refreshPage(viewGroup, 20);
    }

    static /* synthetic */ void lambda$buildMailSend$173(android.view.ViewGroup viewGroup) {
        sMailLog.setLength(0);
        refreshPage(viewGroup, 20);
    }

    private static java.lang.String mailLogDisplayText() {
        java.lang.StringBuilder sb = sMailLog;
        return sb.length() > 0 ? sb.toString() : "（暂无日志）\n填写收件人与正文后点「发送邮件」，发送过程与结果会实时显示在这里。";
    }

    private static void appendMailLog(java.lang.String str) {
        java.lang.StringBuilder sb = sMailLog;
        sb.append(new java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale.getDefault()).format(new java.util.Date()));
        sb.append(" ");
        sb.append(str);
        sb.append("\n");
        if (sb.length() > 20000) {
            sb.delete(0, com.tencent.qqlitepro.plugin.PluginEngine.LOG_MAX);
        }
    }

    private static void updateMailLogView() {
        android.widget.TextView textView = sMailLogView;
        if (textView == null) {
            return;
        }
        textView.setText(mailLogDisplayText());
        try {
            android.view.View view = (android.view.View) sMailLogView.getParent();
            if (view instanceof android.widget.ScrollView) {
                view.post(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda143(view));
            }
        } catch (java.lang.Throwable unused) {
        }
    }

    private static android.view.View mailChip(android.content.Context context, java.lang.String str, boolean z, java.lang.Runnable runnable) {
        android.widget.TextView textViewText = text(context, str, z ? -1 : BTN_TEXT, 13.0f, 17);
        android.graphics.drawable.GradientDrawable gradientDrawable = new android.graphics.drawable.GradientDrawable();
        gradientDrawable.setShape(0);
        gradientDrawable.setColor(z ? ACCENT : BTN_FILL);
        gradientDrawable.setCornerRadius(dpLite(12.0f));
        textViewText.setBackground(gradientDrawable);
        textViewText.setPadding(dp(context, 10.0f), dp(context, 10.0f), dp(context, 10.0f), dp(context, 10.0f));
        textViewText.setClickable(true);
        textViewText.setOnClickListener(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda79(runnable));
        return textViewText;
    }

    static /* synthetic */ void lambda$mailChip$175(java.lang.Runnable runnable, android.view.View view) {
        if (runnable != null) {
            runnable.run();
        }
    }

    private static android.view.View mailSmallBtn(android.content.Context context, java.lang.String str, java.lang.Runnable runnable) {
        android.widget.TextView textViewText = text(context, str, BTN_TEXT, 12.0f, 17);
        android.graphics.drawable.GradientDrawable gradientDrawable = new android.graphics.drawable.GradientDrawable();
        gradientDrawable.setShape(0);
        gradientDrawable.setColor(BTN_FILL);
        gradientDrawable.setCornerRadius(dpLite(10.0f));
        textViewText.setBackground(gradientDrawable);
        textViewText.setPadding(dp(context, 14.0f), dp(context, 7.0f), dp(context, 14.0f), dp(context, 7.0f));
        android.widget.LinearLayout.LayoutParams layoutParams = new android.widget.LinearLayout.LayoutParams(-2, -2);
        layoutParams.rightMargin = dp(context, 8.0f);
        textViewText.setLayoutParams(layoutParams);
        textViewText.setClickable(true);
        textViewText.setOnClickListener(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda29(runnable));
        return textViewText;
    }

    static /* synthetic */ void lambda$mailSmallBtn$176(java.lang.Runnable runnable, android.view.View view) {
        if (runnable != null) {
            runnable.run();
        }
    }

    private static android.view.View mailPickedRow(android.content.Context context, android.view.ViewGroup viewGroup, android.widget.EditText editText, android.widget.EditText editText2, android.widget.EditText editText3, java.io.File file, boolean z) {
        android.widget.LinearLayout linearLayout = new android.widget.LinearLayout(context);
        linearLayout.setOrientation(0);
        linearLayout.setGravity(16);
        android.widget.LinearLayout.LayoutParams layoutParamsRootLp = rootLp();
        layoutParamsRootLp.topMargin = dp(context, 6.0f);
        linearLayout.setLayoutParams(layoutParamsRootLp);
        android.widget.LinearLayout linearLayout2 = new android.widget.LinearLayout(context);
        linearLayout2.setOrientation(1);
        linearLayout2.setLayoutParams(new android.widget.LinearLayout.LayoutParams(0, -2, 1.0f));
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        sb.append(z ? "[图片] " : "[附件] ");
        sb.append(file.getName());
        android.widget.TextView textViewText = text(context, sb.toString(), TITLE, 12.0f, 8388611);
        textViewText.setSingleLine(true);
        textViewText.setEllipsize(android.text.TextUtils.TruncateAt.MIDDLE);
        linearLayout2.addView(textViewText, rootLp());
        linearLayout2.addView(text(context, humanSize(file.length()), HINT, 10.0f, 8388611), rootLp());
        linearLayout.addView(linearLayout2);
        linearLayout.addView(mailSmallBtn(context, "移除", new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda50(editText, editText2, editText3, z, file, viewGroup)));
        return linearLayout;
    }

    static /* synthetic */ void lambda$mailPickedRow$177(android.widget.EditText editText, android.widget.EditText editText2, android.widget.EditText editText3, boolean z, java.io.File file, android.view.ViewGroup viewGroup) {
        captureMailInputs(editText, editText2, editText3);
        if (z) {
            sMailImages.remove(file);
        } else {
            sMailAttach.remove(file);
        }
        refreshPage(viewGroup, 20);
    }

    private static void captureMailInputs(android.widget.EditText editText, android.widget.EditText editText2, android.widget.EditText editText3) {
        java.lang.String strTrim = sMailToTxt;
        java.lang.String strTrim2 = sMailSubjTxt;
        if (editText != null) {
            try {
                strTrim = editText.getText() == null ? sMailToTxt : editText.getText().toString().trim();
            } catch (java.lang.Throwable unused) {
            }
        }
        sMailToTxt = strTrim;
        if (editText2 != null) {
            try {
                strTrim2 = editText2.getText() == null ? sMailSubjTxt : editText2.getText().toString().trim();
            } catch (java.lang.Throwable unused2) {
            }
        }
        sMailSubjTxt = strTrim2;
        if (editText3 != null) {
            try {
                if (editText3.getText() != null) {
                    sMailBodyTxt = editText3.getText().toString();
                }
            } catch (java.lang.Throwable unused3) {
            }
        }
    }

    private static void clearMailSendState() {
        sMailToTxt = "";
        sMailSubjTxt = "";
        sMailBodyTxt = "";
        sMailHtmlMode = false;
        sMailAttach.clear();
        sMailImages.clear();
        sMailToEdit = null;
        sMailSubjEdit = null;
        sMailBodyEdit = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void launchMailPicker(int i) {
        java.lang.String str;
        android.app.Activity activityHostActivity = hostActivity();
        if (activityHostActivity == null) {
            android.view.ViewGroup viewGroup = sPageGroup;
            toast(viewGroup != null ? viewGroup.getContext() : null, "未获取宿主 Activity，无法调起选择器");
            return;
        }
        boolean z = i == REQ_MAIL_IMAGE || i == REQ_MAIL_BG;
        if (i == REQ_MAIL_BG) {
            str = "选择作为网页渲染背景的图片";
        } else {
            str = i == REQ_MAIL_IMAGE ? "选择要作为图片发送的图片" : "选择要作为附件发送的文件";
        }
        try {
            android.content.Intent intent = new android.content.Intent("android.intent.action.GET_CONTENT");
            intent.setType(z ? "image/*" : "*/*");
            if (!z) {
                intent.putExtra("android.intent.extra.MIME_TYPES", new java.lang.String[]{"*/*"});
            }
            intent.addCategory("android.intent.category.OPENABLE");
            activityHostActivity.startActivityForResult(android.content.Intent.createChooser(intent, str), i);
            com.tencent.qqlitepro.util.Log.i("已调起选择器 req=" + i + " act=" + activityHostActivity.getClass().getName());
        } catch (java.lang.Throwable thRethrow14) {
            com.tencent.qqlitepro.util.Log.w("调起选择器失败: " + thRethrow14);
            toast(activityHostActivity, "调起选择器失败：" + thRethrow14);
        }
    }

    private static android.app.Activity hostActivity() {
        android.view.ViewGroup viewGroup = sPageGroup;
        android.app.Activity activityUnwrapActivity = unwrapActivity(viewGroup != null ? viewGroup.getContext() : null);
        if (activityUnwrapActivity != null) {
            return activityUnwrapActivity;
        }
        android.app.Activity activity = sFileHost;
        if (activity != null) {
            return activity;
        }
        android.app.Activity activity2 = sPluginHost;
        if (activity2 != null) {
            return activity2;
        }
        try {
            java.lang.Class<?> cls = java.lang.Class.forName("android.app.ActivityThread");
            java.lang.Object objInvoke = cls.getMethod("currentActivityThread", new java.lang.Class[0]).invoke(null, new java.lang.Object[0]);
            java.lang.reflect.Field declaredField = cls.getDeclaredField("mActivities");
            declaredField.setAccessible(true);
            java.util.Map map = (java.util.Map) declaredField.get(objInvoke);
            if (map != null) {
                for (java.lang.Object obj : map.values()) {
                    java.lang.reflect.Field declaredField2 = obj.getClass().getDeclaredField("activity");
                    declaredField2.setAccessible(true);
                    java.lang.Object obj2 = declaredField2.get(obj);
                    if (obj2 instanceof android.app.Activity) {
                        return (android.app.Activity) obj2;
                    }
                }
            }
        } catch (java.lang.Throwable unused) {
        }
        return null;
    }

    private static android.app.Activity unwrapActivity(android.content.Context context) {
        android.app.Activity activity;
        int i = 0;
        while (true) {
            if (context instanceof android.content.ContextWrapper) {
                int i2 = i + 1;
                if (i < 12) {
                    if (context instanceof android.app.Activity) {
                        activity = (android.app.Activity) context;
                        break;
                    }
                    context = ((android.content.ContextWrapper) context).getBaseContext();
                    i = i2;
                }
            }
            activity = null;
            break;
        }
        return (activity == null && (context instanceof android.app.Activity)) ? (android.app.Activity) context : activity;
    }

    private static void refreshMailSendPage() {
        if (sPageGroup == null || sCurrentPage != 20) {
            return;
        }
        captureMailInputs(sMailToEdit, sMailSubjEdit, sMailBodyEdit);
        refreshPage(sPageGroup, 20);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void handleMailPicked(android.app.Activity activity, android.net.Uri uri, int i) {
        if (uri == null) {
            return;
        }
        android.content.Context context;
        if (activity != null) {
            context = activity;
        } else {
            android.view.ViewGroup viewGroup = sPageGroup;
            if (viewGroup == null) {
                return;
            }
            context = viewGroup.getContext();
        }
        if (context == null) {
            return;
        }
        new java.lang.Thread(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda109(context, uri, i, new android.os.Handler(android.os.Looper.getMainLooper())), "mail-pick").start();
    }

    static /* synthetic */ void lambda$handleMailPicked$180(android.content.Context context, android.net.Uri uri, int i, android.os.Handler handler) {
        java.io.InputStream inputStreamOpenInputStream;
        try {
            java.lang.String strUriDisplayName = uriDisplayName(context, uri);
            if (strUriDisplayName == null || strUriDisplayName.isEmpty()) {
                java.lang.StringBuilder sb = new java.lang.StringBuilder();
                sb.append(i == REQ_MAIL_IMAGE ? "image_" : "file_");
                sb.append(java.lang.System.currentTimeMillis());
                strUriDisplayName = sb.toString();
            }
            java.lang.String strReplaceAll = strUriDisplayName.replaceAll("[\\\\/:*?\"<>|\\r\\n\\t]", "_");
            java.io.File file = new java.io.File(context.getFilesDir(), "mail_send");
            if (!file.exists() && !file.mkdirs()) {
                throw new java.lang.RuntimeException("无法创建临时目录");
            }
            java.io.File file2 = new java.io.File(file, java.lang.System.currentTimeMillis() + "_" + strReplaceAll);
            if ("file".equals(uri.getScheme())) {
                inputStreamOpenInputStream = new java.io.FileInputStream(new java.io.File(uri.getPath()));
            } else {
                inputStreamOpenInputStream = context.getContentResolver().openInputStream(uri);
            }
            if (inputStreamOpenInputStream == null) {
                throw new java.lang.RuntimeException("无法读取所选文件");
            }
            java.io.FileOutputStream fileOutputStream = new java.io.FileOutputStream(file2);
            try {
                byte[] bArr = new byte[8192];
                while (true) {
                    int i2 = inputStreamOpenInputStream.read(bArr);
                    if (i2 > 0) {
                        fileOutputStream.write(bArr, 0, i2);
                    } else {
                        try {
                            break;
                        } catch (java.lang.Throwable unused) {
                        }
                    }
                }
                inputStreamOpenInputStream.close();
                try {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                } catch (java.lang.Throwable unused2) {
                }
                handler.post(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda174(i == REQ_MAIL_IMAGE, file2, context));
            } catch (java.lang.Throwable thRethrow14) {
                try {
                    inputStreamOpenInputStream.close();
                } catch (java.lang.Throwable unused3) {
                }
                try {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    throw thRethrow14;
                } catch (java.lang.Throwable unused4) {
                    throw thRethrow14;
                }
            }
        } catch (java.lang.Throwable th2) {
            java.lang.String strValueOf = java.lang.String.valueOf(th2);
            com.tencent.qqlitepro.util.Log.w("处理邮件附件选择失败: " + strValueOf);
            handler.post(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda175(context, strValueOf));
        }
    }

    static /* synthetic */ void lambda$handleMailPicked$178(boolean z, java.io.File file, android.content.Context context) {
        if (z) {
            sMailImages.add(file);
        } else {
            sMailAttach.add(file);
        }
        toast(context, "已添加：" + file.getName());
        refreshMailSendPage();
    }

    private static android.view.View mailBackRow(android.content.Context context, android.view.ViewGroup viewGroup, int i) {
        android.widget.LinearLayout linearLayout = new android.widget.LinearLayout(context);
        linearLayout.setOrientation(0);
        linearLayout.setGravity(16);
        linearLayout.setPadding(dp(context, 20.0f), dp(context, 14.0f), dp(context, 20.0f), dp(context, 14.0f));
        linearLayout.setLayoutParams(rootLp());
        linearLayout.setOnClickListener(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda71(viewGroup, i));
        linearLayout.addView(text(context, "←  返回", HINT, 14.0f, 8388611));
        return linearLayout;
    }

    private static android.view.View label(android.content.Context context, java.lang.String str, java.lang.String str2) {
        android.widget.LinearLayout linearLayout = new android.widget.LinearLayout(context);
        linearLayout.setOrientation(1);
        linearLayout.addView(text(context, str, HINT, 11.0f, 8388611), rootLp());
        if (str2 == null || str2.isEmpty()) {
            str2 = "-";
        }
        android.widget.TextView textViewText = text(context, str2, TITLE, 14.0f, 8388611);
        textViewText.setPadding(0, dp(context, 2.0f), 0, dp(context, 8.0f));
        linearLayout.addView(textViewText, rootLp());
        return linearLayout;
    }

    private static java.util.List<com.google.gson.JsonObject> parseMailCacheList(java.lang.String str) {
        java.util.ArrayList arrayList = new java.util.ArrayList();
        if (str != null && !str.isEmpty()) {
            try {
                com.google.gson.JsonArray jsonArray = (com.google.gson.JsonArray) new com.google.gson.Gson().fromJson(str, com.google.gson.JsonArray.class);
                if (jsonArray != null) {
                    for (int i = 0; i < jsonArray.size(); i++) {
                        arrayList.add(jsonArray.get(i).getAsJsonObject());
                    }
                }
            } catch (java.lang.Throwable unused) {
            }
        }
        return arrayList;
    }

    private static java.lang.String cacheToJson(java.util.List<com.google.gson.JsonObject> list) {
        com.google.gson.JsonArray jsonArray = new com.google.gson.JsonArray();
        java.util.Iterator<com.google.gson.JsonObject> it = list.iterator();
        while (it.hasNext()) {
            jsonArray.add(it.next());
        }
        return jsonArray.toString();
    }

    private static java.lang.String str(com.google.gson.JsonObject jsonObject, java.lang.String str) {
        try {
            return (!jsonObject.has(str) || jsonObject.get(str).isJsonNull()) ? "" : jsonObject.get(str).getAsString();
        } catch (java.lang.Throwable unused) {
            return "";
        }
    }

    private static boolean bool(com.google.gson.JsonObject jsonObject, java.lang.String str, boolean z) {
        try {
            return (!jsonObject.has(str) || jsonObject.get(str).isJsonNull()) ? z : jsonObject.get(str).getAsBoolean();
        } catch (java.lang.Throwable unused) {
            return z;
        }
    }

    private static android.widget.LinearLayout filledCard(android.content.Context context) {
        android.widget.LinearLayout linearLayout = new android.widget.LinearLayout(context);
        linearLayout.setOrientation(1);
        android.graphics.drawable.GradientDrawable gradientDrawable = new android.graphics.drawable.GradientDrawable();
        gradientDrawable.setShape(0);
        gradientDrawable.setColor(SURFACE_CONTAINER);
        gradientDrawable.setCornerRadius(dpLite(14.0f));
        linearLayout.setBackground(gradientDrawable);
        linearLayout.setPadding(dp(context, 12.0f), dp(context, 12.0f), dp(context, 12.0f), dp(context, 12.0f));
        android.widget.LinearLayout.LayoutParams layoutParamsRootLp = rootLp();
        layoutParamsRootLp.leftMargin = dp(context, 12.0f);
        layoutParamsRootLp.rightMargin = dp(context, 12.0f);
        layoutParamsRootLp.bottomMargin = dp(context, 6.0f);
        linearLayout.setLayoutParams(layoutParamsRootLp);
        return linearLayout;
    }

    private static android.view.View featureCardRow(android.content.Context context, com.tencent.qqlitepro.plugin.PluginEngine.Feature feature) {
        android.widget.LinearLayout linearLayout = new android.widget.LinearLayout(context);
        linearLayout.setOrientation(1);
        android.graphics.drawable.GradientDrawable gradientDrawable = new android.graphics.drawable.GradientDrawable();
        gradientDrawable.setShape(0);
        gradientDrawable.setColor(SURFACE_CONTAINER);
        gradientDrawable.setCornerRadius(dpLite(14.0f));
        linearLayout.setBackground(gradientDrawable);
        android.widget.LinearLayout.LayoutParams layoutParamsRootLp = rootLp();
        layoutParamsRootLp.leftMargin = dp(context, 12.0f);
        layoutParamsRootLp.rightMargin = dp(context, 12.0f);
        layoutParamsRootLp.bottomMargin = dp(context, 8.0f);
        linearLayout.setLayoutParams(layoutParamsRootLp);
        linearLayout.setPadding(dp(context, 12.0f), dp(context, 10.0f), dp(context, 12.0f), dp(context, 10.0f));
        android.widget.LinearLayout linearLayout2 = new android.widget.LinearLayout(context);
        linearLayout2.setOrientation(0);
        linearLayout2.setGravity(16);
        linearLayout2.setLayoutParams(new android.widget.LinearLayout.LayoutParams(-1, -2));
        android.widget.LinearLayout linearLayout3 = new android.widget.LinearLayout(context);
        linearLayout3.setOrientation(1);
        linearLayout3.setLayoutParams(new android.widget.LinearLayout.LayoutParams(0, -2, 1.0f));
        android.widget.TextView textViewText = text(context, feature.name, TITLE, 13.0f, 8388611);
        textViewText.setTypeface(android.graphics.Typeface.DEFAULT, 1);
        textViewText.setPadding(0, 0, dp(context, 4.0f), dp(context, 1.0f));
        textViewText.setSingleLine(true);
        textViewText.setEllipsize(android.text.TextUtils.TruncateAt.END);
        java.lang.String desc = feature.getDesc();
        if (desc == null || desc.isEmpty()) {
            if (feature.isHook()) {
                desc = "Hook · " + feature.hookMethodName();
            } else {
                desc = "";
            }
        }
        if (desc != null && desc.contains("\n")) {
            desc = desc.substring(0, desc.indexOf(10));
        }
        if (desc == null || desc.isEmpty()) {
            desc = "（无描述）";
        }
        android.widget.TextView textViewText2 = text(context, desc, -6643546, 11.0f, 8388611);
        textViewText2.setSingleLine(true);
        textViewText2.setEllipsize(android.text.TextUtils.TruncateAt.END);
        linearLayout3.addView(textViewText, rootLp());
        linearLayout3.addView(textViewText2, rootLp());
        linearLayout2.addView(linearLayout3);
        boolean zIsHook = feature.isHook();
        android.widget.TextView textViewText3 = text(context, zIsHook ? "详情" : "运行", ACCENT, 12.0f, 17);
        textViewText3.setClickable(true);
        android.graphics.drawable.GradientDrawable gradientDrawable2 = new android.graphics.drawable.GradientDrawable();
        gradientDrawable2.setShape(0);
        gradientDrawable2.setColor(440261764);
        gradientDrawable2.setCornerRadius(dpLite(16.0f));
        textViewText3.setBackground(gradientDrawable2);
        textViewText3.setPadding(dp(context, 8.0f), dp(context, 4.0f), dp(context, 8.0f), dp(context, 4.0f));
        android.widget.LinearLayout.LayoutParams layoutParams = new android.widget.LinearLayout.LayoutParams(-2, dp(context, 26.0f));
        layoutParams.leftMargin = dp(context, 6.0f);
        textViewText3.setLayoutParams(layoutParams);
        textViewText3.setOnClickListener(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda77(zIsHook, context, feature));
        linearLayout2.addView(textViewText3);
        linearLayout.addView(linearLayout2, rootLp());
        android.widget.LinearLayout linearLayout4 = new android.widget.LinearLayout(context);
        linearLayout4.setOrientation(0);
        linearLayout4.setGravity(21);
        android.widget.LinearLayout.LayoutParams layoutParams2 = new android.widget.LinearLayout.LayoutParams(-1, -2);
        layoutParams2.topMargin = dp(context, 6.0f);
        linearLayout4.setLayoutParams(layoutParams2);
        android.widget.CompoundButton compoundButtonCreateSwitch = createSwitch(context);
        compoundButtonCreateSwitch.setChecked(com.tencent.qqlitepro.plugin.PluginEngine.isFeatureEnabled(context, feature));
        compoundButtonCreateSwitch.setOnCheckedChangeListener(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda78(context, feature));
        linearLayout4.addView(compoundButtonCreateSwitch);
        linearLayout.addView(linearLayout4, rootLp());
        return linearLayout;
    }

    static /* synthetic */ void lambda$featureCardRow$182(boolean z, android.content.Context context, com.tencent.qqlitepro.plugin.PluginEngine.Feature feature, android.view.View view) {
        if (z) {
            hookFeatureDialog(context, feature);
        } else {
            com.tencent.qqlitepro.plugin.PluginEngine.runFeature(feature, null);
        }
    }

    private static void hookFeatureDialog(android.content.Context context, com.tencent.qqlitepro.plugin.PluginEngine.Feature feature) {
        android.app.Dialog dialog = new android.app.Dialog(context, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        android.widget.LinearLayout linearLayout = new android.widget.LinearLayout(context);
        linearLayout.setOrientation(1);
        linearLayout.setPadding(dp(context, 18.0f), dp(context, 14.0f), dp(context, 18.0f), dp(context, 16.0f));
        linearLayout.addView(text(context, "插件 Hook 详情", TITLE, 16.0f, 3), rootLp());
        java.lang.String strHookMethodName = feature.hookMethodName();
        int iLastIndexOf = strHookMethodName.lastIndexOf(46);
        java.lang.String strSubstring = iLastIndexOf >= 0 ? strHookMethodName.substring(iLastIndexOf + 1) : strHookMethodName;
        java.lang.String strSubstring2 = iLastIndexOf >= 0 ? strHookMethodName.substring(0, iLastIndexOf) : feature.name;
        linearLayout.addView(text(context, feature.pluginName() + " · v" + feature.pluginVersion(), -6643546, 12.0f, 3), rootLp());
        makeGroupGap(linearLayout);
        android.widget.LinearLayout linearLayout2 = new android.widget.LinearLayout(context);
        linearLayout2.setOrientation(1);
        android.graphics.drawable.GradientDrawable gradientDrawable = new android.graphics.drawable.GradientDrawable();
        gradientDrawable.setShape(0);
        gradientDrawable.setColor(SURFACE_CONTAINER);
        gradientDrawable.setCornerRadius(dpLite(14.0f));
        linearLayout2.setBackground(gradientDrawable);
        linearLayout2.setPadding(dp(context, 14.0f), dp(context, 12.0f), dp(context, 14.0f), dp(context, 12.0f));
        linearLayout2.setLayoutParams(rootLp());
        linearLayout2.addView(text(context, "宿主类（Class）", ACCENT, 12.0f, 3), rootLp());
        linearLayout2.addView(text(context, strSubstring2, TITLE, 13.0f, 3), rootLp());
        makeGroupGap(linearLayout2);
        linearLayout2.addView(text(context, "类方法（Method）", ACCENT, 12.0f, 3), rootLp());
        linearLayout2.addView(text(context, strSubstring, TITLE, 13.0f, 3), rootLp());
        java.lang.String desc = feature.getDesc();
        if (desc != null && !desc.isEmpty()) {
            makeGroupGap(linearLayout2);
            linearLayout2.addView(text(context, "描述", ACCENT, 12.0f, 3), rootLp());
            android.widget.TextView textViewText = text(context, desc, TITLE, 13.0f, 3);
            textViewText.setPadding(0, dp(context, 4.0f), 0, 0);
            linearLayout2.addView(textViewText, rootLp());
        }
        linearLayout.addView(linearLayout2);
        linearLayout.addView(switchRow(context, "启用该 Hook", "开启后自动挂载，关闭即卸载", com.tencent.qqlitepro.plugin.PluginEngine.isFeatureEnabled(context, feature), new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda138(context, feature)), rootLp());
        android.view.View viewSaveButton = saveButton(context, "关闭", new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda205(dialog));
        marginBottom(viewSaveButton, 10.0f);
        linearLayout.addView(viewSaveButton);
        com.tencent.qqlitepro.hook.SettingsInjector.ScrollableCard scrollableCard = new com.tencent.qqlitepro.hook.SettingsInjector.ScrollableCard(context, linearLayout);
        scrollableCard.setPadding(0, dp(context, 14.0f), 0, dp(context, 14.0f));
        dialog.setContentView(scrollableCard);
        android.view.WindowManager.LayoutParams attributes = dialog.getWindow().getAttributes();
        attributes.width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.94f);
        dialog.getWindow().setAttributes(attributes);
        dialog.show();
    }

    private static void addPluginFeaturesBlock(android.content.Context context, android.view.ViewGroup viewGroup, java.io.File file) {
        java.lang.String name = file == null ? null : file.getName();
        if (name != null && name.endsWith(".js")) {
            name = name.substring(0, name.length() - 3);
        }
        boolean z = false;
        for (com.tencent.qqlitepro.plugin.PluginEngine.Feature feature : com.tencent.qqlitepro.plugin.PluginEngine.features()) {
            java.lang.String strPluginBase = feature.pluginBase();
            if (strPluginBase != null && strPluginBase.endsWith(".js")) {
                strPluginBase = strPluginBase.substring(0, strPluginBase.length() - 3);
            }
            if (name != null && name.equals(strPluginBase)) {
                viewGroup.addView(featureCardRow(context, feature));
                z = true;
            }
        }
        if (z) {
            return;
        }
        android.widget.TextView textViewText = text(context, "（该插件暂无子功能）", -6643546, 12.0f, 3);
        textViewText.setPadding(dp(context, 4.0f), dp(context, 4.0f), 0, 0);
        viewGroup.addView(textViewText, rootLp());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void openPluginSettingsFor(android.content.Context context, java.io.File file) {
        try {
            com.tencent.qqlitepro.hook.SettingsInjector.PluginInfo plugin = parsePlugin(readTextFile(file));
            if (plugin != null) {
                pluginSettingsDialog(context, file, plugin);
            } else {
                toast(context, "读取插件设置失败");
            }
        } catch (java.lang.Throwable thRethrow14) {
            com.tencent.qqlitepro.util.Log.w("打开插件设置失败: " + thRethrow14);
        }
    }

    private static void openPluginSettingsFor(android.content.Context context, com.tencent.qqlitepro.plugin.PluginEngine.Feature feature) {
        try {
            java.io.File file = new java.io.File(pluginDir(context), feature.pluginBase());
            com.tencent.qqlitepro.hook.SettingsInjector.PluginInfo plugin = parsePlugin(readTextFile(file));
            if (plugin != null) {
                pluginSettingsDialog(context, file, plugin);
            } else {
                toast(context, "读取插件设置失败");
            }
        } catch (java.lang.Throwable thRethrow14) {
            com.tencent.qqlitepro.util.Log.w("打开插件设置失败: " + thRethrow14);
        }
    }

    private static void reloadPluginEngine(android.content.Context context) {
        if (context != null) {
            try {
                com.tencent.qqlitepro.plugin.PluginEngine.ensureLoaded(context);
                com.tencent.qqlitepro.plugin.PluginEngine.reload();
            } catch (java.lang.Throwable thRethrow14) {
                com.tencent.qqlitepro.util.Log.w("插件引擎重载失败: " + thRethrow14);
            }
        }
    }

    private static void reloadPluginAndRestart(android.content.Context context) {
        try {
            reloadPluginEngine(context);
        } catch (java.lang.Throwable thRethrow14) {
            com.tencent.qqlitepro.util.Log.w("插件重载失败: " + thRethrow14);
        }
        try {
            toast(context, "插件已变更，即将重启 QQMax 使其生效");
            new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda76(context), 400L);
        } catch (java.lang.Throwable unused) {
            restartHost(context);
        }
    }

    private static java.io.File pluginDir(android.content.Context context) {
        java.io.File file = new java.io.File(context.getFilesDir(), "qqlite_plugins");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    /* JADX INFO: Access modifiers changed from: private */
    static final class PluginInfo {
        java.lang.String desc;
        boolean hasSettings;
        java.lang.String mainCode;
        java.lang.String name;
        java.util.List<java.lang.String[]> settings;
        java.lang.String version;

        PluginInfo() {
        }

        PluginInfo(java.lang.String str, java.lang.String str2, java.lang.String str3, boolean z, java.lang.String str4) {
            this(str, str2, str3, z, str4, null);
        }

        PluginInfo(java.lang.String str, java.lang.String str2, java.lang.String str3, boolean z, java.lang.String str4, java.util.List<java.lang.String[]> list) {
            this.name = str;
            this.desc = str2;
            this.version = str3;
            this.hasSettings = z;
            this.mainCode = str4;
            this.settings = list == null ? new java.util.ArrayList<>() : list;
        }
    }

    /* JADX WARN: Code duplicated, block: B:97:0x00b6 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:99:0x0137 A[SYNTHETIC] */
    private static com.tencent.qqlitepro.hook.SettingsInjector.PluginInfo parsePlugin(java.lang.String str) {
        boolean z;
        int i;
        if (str == null || str.isEmpty()) {
            return null;
        }
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        java.util.ArrayList arrayList = new java.util.ArrayList();
        java.lang.String[] strArrSplit = str.split("\n", -1);
        int i2 = 0;
        boolean z2 = false;
        java.lang.String strTagVal = null;
        java.lang.String strTagVal2 = null;
        boolean zEqualsIgnoreCase = false;
        while (true) {
            if (i2 >= strArrSplit.length) {
                z = zEqualsIgnoreCase;
                i = -1;
                break;
            }
            java.lang.String str2 = strArrSplit[i2];
            java.lang.String strTrim = str2.trim();
            java.lang.String str3 = strTagVal;
            java.lang.String str4 = strTagVal2;
            z = zEqualsIgnoreCase;
            if (z2) {
                if (strTrim.startsWith("*")) {
                    strTrim = strTrim.substring(1).trim();
                }
                if (strTrim.startsWith("@name")) {
                    strTagVal = tagVal(str2, "@name");
                } else {
                    if (strTrim.startsWith("@description")) {
                        appendDesc(sb, tagVal(str2, "@description"));
                    } else if (strTrim.startsWith("@desc ")) {
                        appendDesc(sb, tagVal(str2, "@desc"));
                    } else {
                        if (strTrim.startsWith("@version")) {
                            strTagVal2 = tagVal(str2, "@version");
                            strTagVal = str3;
                            zEqualsIgnoreCase = z;
                        } else if (strTrim.startsWith("@hasSettings")) {
                            zEqualsIgnoreCase = "true".equalsIgnoreCase(tagVal(str2, "@hasSettings"));
                            strTagVal = str3;
                            strTagVal2 = str4;
                        } else if (strTrim.startsWith("@setting")) {
                            parseSettingLine(arrayList, str2, "@setting");
                        }
                        if (str2.contains("*/")) {
                            i = i2 + 1;
                            z = zEqualsIgnoreCase;
                            break;
                        }
                        i2++;
                    }
                    strTagVal = str3;
                }
                strTagVal2 = str4;
                zEqualsIgnoreCase = z;
                if (str2.contains("*/")) {
                    i = i2 + 1;
                    z = zEqualsIgnoreCase;
                    break;
                }
                i2++;
            } else {
                if (!strTrim.startsWith("/*")) {
                    if (!strTrim.startsWith("//")) {
                        i = i2;
                        strTagVal = str3;
                        strTagVal2 = str4;
                        break;
                    }
                    java.lang.String strTrim2 = strTrim.substring(2).trim();
                    if (strTrim2.startsWith("@name")) {
                        strTagVal = tagVal(strTrim2, "@name");
                    } else {
                        if (strTrim2.startsWith("@description") || strTrim2.startsWith("@desc ")) {
                            appendDesc(sb, tagVal(strTrim2, strTrim2.startsWith("@desc") ? "@desc" : "@description"));
                        } else if (strTrim2.startsWith("@version")) {
                            strTagVal2 = tagVal(strTrim2, "@version");
                            strTagVal = str3;
                            zEqualsIgnoreCase = z;
                        } else if (strTrim2.startsWith("@hasSettings")) {
                            zEqualsIgnoreCase = "true".equalsIgnoreCase(tagVal(strTrim2, "@hasSettings"));
                            strTagVal = str3;
                            strTagVal2 = str4;
                        } else if (strTrim2.startsWith("@setting")) {
                            parseSettingLine(arrayList, strTrim2, "@setting");
                        }
                        strTagVal = str3;
                    }
                    strTagVal2 = str4;
                    zEqualsIgnoreCase = z;
                } else {
                    strTagVal = str3;
                    strTagVal2 = str4;
                    zEqualsIgnoreCase = z;
                    z2 = true;
                }
                i2++;
            }
        }
        if (strTagVal == null || strTagVal.isEmpty()) {
            return null;
        }
        java.lang.StringBuilder sb2 = new java.lang.StringBuilder();
        for (int iMax = java.lang.Math.max(i, 0); iMax < strArrSplit.length; iMax++) {
            if (sb2.length() > 0) {
                sb2.append('\n');
            }
            sb2.append(strArrSplit[iMax]);
        }
        return new com.tencent.qqlitepro.hook.SettingsInjector.PluginInfo(strTagVal.trim(), sb.toString().trim(), (strTagVal2 == null || strTagVal2.isEmpty()) ? "1.0.0" : strTagVal2, z, sb2.toString(), arrayList);
    }

    private static void parseSettingLine(java.util.List<java.lang.String[]> list, java.lang.String str, java.lang.String str2) {
        java.lang.String strTrim;
        java.lang.String strTrim2 = tagVal(str, str2).trim();
        if (strTrim2.isEmpty()) {
            return;
        }
        int iIndexOf = strTrim2.indexOf(58);
        java.lang.String strTrim3 = "";
        if (iIndexOf >= 0) {
            java.lang.String strTrim4 = strTrim2.substring(0, iIndexOf).trim();
            java.lang.String strSubstring = strTrim2.substring(iIndexOf + 1);
            int iIndexOf2 = strSubstring.indexOf(58);
            if (iIndexOf2 < 0) {
                strTrim = strSubstring.trim();
            } else {
                strTrim = strSubstring.substring(0, iIndexOf2).trim();
                strTrim3 = strSubstring.substring(iIndexOf2 + 1).trim();
            }
            strTrim2 = strTrim4;
        } else {
            strTrim = strTrim2;
        }
        if (strTrim2.isEmpty()) {
            return;
        }
        if (strTrim.isEmpty()) {
            strTrim = strTrim2;
        }
        list.add(new java.lang.String[]{strTrim2, strTrim, strTrim3});
    }

    private static void appendDesc(java.lang.StringBuilder sb, java.lang.String str) {
        if (str == null || str.isEmpty()) {
            return;
        }
        if (sb.length() > 0) {
            sb.append('\n');
        }
        sb.append(str);
    }

    private static java.lang.String tagVal(java.lang.String str, java.lang.String str2) {
        int iIndexOf = str.indexOf(str2);
        if (iIndexOf < 0) {
            return "";
        }
        java.lang.String strSubstring = str.substring(iIndexOf + str2.length());
        if (strSubstring.startsWith(":")) {
            strSubstring = strSubstring.substring(1);
        }
        java.lang.String strTrim = strSubstring.trim();
        return strTrim.endsWith("*/") ? strTrim.substring(0, strTrim.length() - 2).trim() : strTrim;
    }

    private static java.lang.String readTextFile(java.io.File file) {
        if (file != null) {
            try {
                if (file.exists()) {
                    int iMin = (int) java.lang.Math.min(file.length(), 1048576L);
                    byte[] bArr = new byte[iMin];
                    java.io.FileInputStream fileInputStream = new java.io.FileInputStream(file);
                    int i = 0;
                    while (i < iMin) {
                        try {
                            int i2 = fileInputStream.read(bArr, i, iMin - i);
                            if (i2 <= 0) {
                                break;
                            }
                            i += i2;
                        } finally {
                            fileInputStream.close();
                        }
                    }
                    return new java.lang.String(bArr, 0, i, "UTF-8");
                }
            } catch (java.lang.Throwable unused) {
            }
        }
        return "";
    }

    private static boolean writeTextFile(java.io.File file, java.lang.String str) {
        try {
            java.io.FileOutputStream fileOutputStream = new java.io.FileOutputStream(file);
            try {
                fileOutputStream.write(str.getBytes("UTF-8"));
                return true;
            } finally {
                fileOutputStream.close();
            }
        } catch (java.lang.Throwable unused) {
            return false;
        }
    }

    private static void buildPlugin(android.content.Context context, android.view.ViewGroup viewGroup) {
        sPluginGroup = viewGroup;
        sPluginHost = context instanceof android.app.Activity ? (android.app.Activity) context : null;
        sPluginLog = null;
        sPluginListBox = null;
        viewGroup.addView(backRow(context));
        viewGroup.addView(section(context, "插件管理"));
        viewGroup.addView(saveButton(context, "导入插件 ( .js )", new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda193()));
        makeGroupGap(viewGroup);
        viewGroup.addView(section(context, "已加载插件"));
        android.widget.LinearLayout linearLayout = new android.widget.LinearLayout(context);
        linearLayout.setOrientation(1);
        linearLayout.setLayoutParams(rootLp());
        sPluginListBox = linearLayout;
        viewGroup.addView(linearLayout, rootLp());
        try {
            com.tencent.qqlitepro.plugin.PluginEngine.ensureLoaded(context);
        } catch (java.lang.Throwable thRethrow14) {
            com.tencent.qqlitepro.util.Log.w("插件引擎初始化失败: " + thRethrow14);
        }
        makeGroupGap(viewGroup);
        viewGroup.addView(section(context, "拓展功能"));
        android.widget.TextView textViewText = text(context, "已启用插件注册的功能集中在此，可直接运行或启停。", -6643546, 12.0f, 8388611);
        textViewText.setPadding(dp(context, 16.0f), dp(context, 4.0f), dp(context, 16.0f), dp(context, 4.0f));
        viewGroup.addView(textViewText, rootLp());
        android.widget.LinearLayout linearLayout2 = new android.widget.LinearLayout(context);
        linearLayout2.setOrientation(1);
        linearLayout2.setLayoutParams(rootLp());
        viewGroup.addView(linearLayout2, rootLp());
        renderAllFeatures(context, linearLayout2);
        makeGroupGap(viewGroup);
        android.widget.LinearLayout linearLayout3 = new android.widget.LinearLayout(context);
        linearLayout3.setOrientation(1);
        android.graphics.drawable.GradientDrawable gradientDrawable = new android.graphics.drawable.GradientDrawable();
        gradientDrawable.setShape(0);
        gradientDrawable.setColor(SURFACE_CONTAINER);
        gradientDrawable.setCornerRadius(dpLite(14.0f));
        linearLayout3.setBackground(gradientDrawable);
        android.widget.LinearLayout.LayoutParams layoutParamsRootLp = rootLp();
        layoutParamsRootLp.leftMargin = dp(context, 12.0f);
        layoutParamsRootLp.rightMargin = dp(context, 12.0f);
        linearLayout3.setLayoutParams(layoutParamsRootLp);
        android.widget.ScrollView scrollView = new android.widget.ScrollView(context);
        scrollView.setLayoutParams(new android.widget.LinearLayout.LayoutParams(-1, dp(context, 200.0f)));
        android.widget.LinearLayout linearLayout4 = new android.widget.LinearLayout(context);
        linearLayout4.setOrientation(1);
        linearLayout4.setPadding(dp(context, 8.0f), dp(context, 4.0f), dp(context, 8.0f), dp(context, 4.0f));
        scrollView.addView(linearLayout4, rootLp());
        android.widget.TextView textViewText2 = text(context, "", -7288592, 12.0f, 8388611);
        sPluginLog = textViewText2;
        textViewText2.setPadding(dp(context, 8.0f), dp(context, 4.0f), dp(context, 8.0f), dp(context, 4.0f));
        sPluginLog.setMovementMethod(new android.text.method.ScrollingMovementMethod());
        linearLayout4.addView(sPluginLog, rootLp());
        linearLayout3.addView(scrollView, rootLp());
        addLogTitleRow(context, linearLayout3, "插件运行日志", sPluginLog);
        viewGroup.addView(linearLayout3);
        com.tencent.qqlitepro.plugin.PluginEngine.setLogView(sPluginLog);
        renderPluginList(context);
        appendLog(sPluginLog, "请先导入 .js 插件，然后拨动开关启用。");
    }

    private static void renderAllFeatures(android.content.Context context, android.view.ViewGroup viewGroup) {
        java.util.List<com.tencent.qqlitepro.plugin.PluginEngine.Feature> listFeatures = com.tencent.qqlitepro.plugin.PluginEngine.features();
        if (listFeatures == null || listFeatures.isEmpty()) {
            android.widget.TextView textViewText = text(context, "（暂无插件功能，请先在上方导入并启用 .js 插件）", -6643546, 13.0f, 8388611);
            textViewText.setPadding(dp(context, 16.0f), dp(context, 6.0f), dp(context, 16.0f), dp(context, 6.0f));
            viewGroup.addView(textViewText, rootLp());
        } else {
            java.util.Iterator<com.tencent.qqlitepro.plugin.PluginEngine.Feature> it = listFeatures.iterator();
            while (it.hasNext()) {
                viewGroup.addView(featureCardRow(context, it.next()));
            }
        }
    }

    private static void renderPluginList(android.content.Context context) {
        android.widget.LinearLayout linearLayout = sPluginListBox;
        if (linearLayout == null) {
            return;
        }
        linearLayout.removeAllViews();
        java.io.File[] fileArrListFiles = pluginDir(context).listFiles(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda15());
        if (fileArrListFiles == null || fileArrListFiles.length == 0) {
            android.widget.TextView textViewText = text(context, "（暂无插件，点击上方“导入插件”添加 .js 插件）", -6643546, 13.0f, 8388611);
            textViewText.setPadding(dp(context, 16.0f), dp(context, 6.0f), dp(context, 16.0f), dp(context, 6.0f));
            sPluginListBox.addView(textViewText, rootLp());
            return;
        }
        java.util.Arrays.sort(fileArrListFiles, new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda16());
        for (java.io.File file : fileArrListFiles) {
            sPluginListBox.addView(pluginCardRow(context, file));
        }
    }

    private static android.view.View pluginCardRow(android.content.Context context, java.io.File file) {
        int i;
        java.lang.String str;
        com.tencent.qqlitepro.hook.SettingsInjector.PluginInfo plugin = parsePlugin(readTextFile(file));
        if (plugin == null) {
            plugin = new com.tencent.qqlitepro.hook.SettingsInjector.PluginInfo(file.getName(), "（无效插件：缺少 @name 头部注释）", "1.0.0", false, "");
        }
        java.lang.String strSubstring = (plugin.desc == null || plugin.desc.isEmpty()) ? "（无描述）" : plugin.desc;
        if (strSubstring.contains("\n")) {
            strSubstring = strSubstring.substring(0, strSubstring.indexOf(10));
        }
        java.lang.String strSubstring2 = file.getName().endsWith(".js") ? file.getName().substring(0, file.getName().length() - 3) : file.getName();
        try {
            java.util.Iterator<com.tencent.qqlitepro.plugin.PluginEngine.Feature> it = com.tencent.qqlitepro.plugin.PluginEngine.features().iterator();
            i = 0;
            while (it.hasNext()) {
                try {
                    java.lang.String strPluginBase = it.next().pluginBase();
                    if (strPluginBase != null && strPluginBase.endsWith(".js")) {
                        strPluginBase = strPluginBase.substring(0, strPluginBase.length() - 3);
                    }
                    if (strSubstring2.equals(strPluginBase)) {
                        i++;
                    }
                } catch (java.lang.Throwable unused) {
                }
            }
        } catch (java.lang.Throwable unused2) {
            i = 0;
        }
        android.widget.LinearLayout linearLayout = new android.widget.LinearLayout(context);
        linearLayout.setOrientation(1);
        android.graphics.drawable.GradientDrawable gradientDrawable = new android.graphics.drawable.GradientDrawable();
        gradientDrawable.setShape(0);
        gradientDrawable.setColor(SURFACE_CONTAINER);
        gradientDrawable.setCornerRadius(dpLite(14.0f));
        linearLayout.setBackground(gradientDrawable);
        android.widget.LinearLayout.LayoutParams layoutParamsRootLp = rootLp();
        layoutParamsRootLp.leftMargin = dp(context, 8.0f);
        layoutParamsRootLp.rightMargin = dp(context, 8.0f);
        layoutParamsRootLp.bottomMargin = dp(context, 10.0f);
        linearLayout.setLayoutParams(layoutParamsRootLp);
        linearLayout.setPadding(dp(context, 12.0f), dp(context, 10.0f), dp(context, 12.0f), dp(context, 10.0f));
        android.widget.LinearLayout linearLayout2 = new android.widget.LinearLayout(context);
        linearLayout2.setOrientation(1);
        linearLayout2.setLayoutParams(new android.widget.LinearLayout.LayoutParams(-1, -2));
        android.widget.LinearLayout linearLayout3 = new android.widget.LinearLayout(context);
        linearLayout3.setOrientation(0);
        linearLayout3.setGravity(16);
        linearLayout3.setLayoutParams(new android.widget.LinearLayout.LayoutParams(-1, -2));
        android.widget.TextView textViewText = text(context, (plugin.name == null || plugin.name.isEmpty()) ? "?" : plugin.name.substring(0, 1), -1, 16.0f, 0);
        textViewText.setGravity(17);
        textViewText.setTypeface(android.graphics.Typeface.DEFAULT, 1);
        android.graphics.drawable.GradientDrawable gradientDrawable2 = new android.graphics.drawable.GradientDrawable();
        gradientDrawable2.setShape(1);
        gradientDrawable2.setColor(ACCENT);
        textViewText.setBackground(gradientDrawable2);
        android.widget.LinearLayout.LayoutParams layoutParams = new android.widget.LinearLayout.LayoutParams(dp(context, 30.0f), dp(context, 30.0f));
        layoutParams.rightMargin = dp(context, 8.0f);
        textViewText.setLayoutParams(layoutParams);
        linearLayout3.addView(textViewText);
        android.widget.LinearLayout linearLayout4 = new android.widget.LinearLayout(context);
        linearLayout4.setOrientation(1);
        linearLayout4.setLayoutParams(new android.widget.LinearLayout.LayoutParams(0, -2, 1.0f));
        android.widget.TextView textViewText2 = text(context, plugin.name, TITLE, 13.0f, 3);
        textViewText2.setTypeface(android.graphics.Typeface.DEFAULT, 1);
        textViewText2.setPadding(0, 0, dp(context, 4.0f), dp(context, 1.0f));
        textViewText2.setSingleLine(true);
        textViewText2.setEllipsize(android.text.TextUtils.TruncateAt.END);
        java.lang.StringBuilder sb = new java.lang.StringBuilder("v");
        sb.append(plugin.version);
        sb.append(" · ");
        sb.append(strSubstring);
        if (i > 0) {
            str = " · " + i + " 项子功能";
        } else {
            str = "";
        }
        sb.append(str);
        android.widget.TextView textViewText3 = text(context, sb.toString(), -6643546, 10.0f, 3);
        textViewText3.setPadding(0, 0, dp(context, 4.0f), 0);
        textViewText3.setSingleLine(true);
        textViewText3.setEllipsize(android.text.TextUtils.TruncateAt.END);
        linearLayout4.addView(textViewText2, rootLp());
        linearLayout4.addView(textViewText3, rootLp());
        linearLayout3.addView(linearLayout4);
        android.widget.TextView textViewText4 = text(context, "设置", ACCENT, 12.0f, 0);
        textViewText4.setClickable(true);
        textViewText4.setTextColor(ACCENT);
        textViewText4.setTypeface(android.graphics.Typeface.DEFAULT, 1);
        textViewText4.setGravity(17);
        textViewText4.setPadding(dp(context, 7.0f), dp(context, 4.0f), dp(context, 7.0f), dp(context, 4.0f));
        android.graphics.drawable.GradientDrawable gradientDrawable3 = new android.graphics.drawable.GradientDrawable();
        gradientDrawable3.setShape(0);
        gradientDrawable3.setColor(419430399);
        gradientDrawable3.setCornerRadius(dpLite(14.0f));
        textViewText4.setBackground(gradientDrawable3);
        android.widget.LinearLayout.LayoutParams layoutParams2 = new android.widget.LinearLayout.LayoutParams(-2, -2);
        layoutParams2.leftMargin = dp(context, 6.0f);
        textViewText4.setLayoutParams(layoutParams2);
        textViewText4.setOnClickListener(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda105(context, file));
        linearLayout3.addView(textViewText4);
        linearLayout2.addView(linearLayout3, rootLp());
        android.widget.LinearLayout linearLayout5 = new android.widget.LinearLayout(context);
        linearLayout5.setOrientation(0);
        linearLayout5.setGravity(21);
        android.widget.LinearLayout.LayoutParams layoutParams3 = new android.widget.LinearLayout.LayoutParams(-1, -2);
        layoutParams3.topMargin = dp(context, 6.0f);
        linearLayout5.setLayoutParams(layoutParams3);
        boolean zIsPluginEnabled = com.tencent.qqlitepro.util.Config.isPluginEnabled(context, file.getName());
        android.widget.TextView textViewText5 = text(context, zIsPluginEnabled ? "已启用" : "已停用", zIsPluginEnabled ? -12723068 : -6643546, 11.0f, 3);
        textViewText5.setPadding(0, 0, dp(context, 8.0f), 0);
        linearLayout5.addView(textViewText5, new android.widget.LinearLayout.LayoutParams(-2, -2));
        android.widget.CompoundButton compoundButtonCreateSwitch = createSwitch(context);
        compoundButtonCreateSwitch.setChecked(zIsPluginEnabled);
        compoundButtonCreateSwitch.setOnCheckedChangeListener(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda106(context, file, textViewText5, plugin));
        linearLayout5.addView(compoundButtonCreateSwitch);
        linearLayout2.addView(linearLayout5, rootLp());
        linearLayout.addView(linearLayout2, rootLp());
        linearLayout.setOnClickListener(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda107(context, file, plugin));
        linearLayout.setOnLongClickListener(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda108(context, file, plugin));
        return linearLayout;
    }

    static /* synthetic */ void lambda$pluginCardRow$190(android.content.Context context, java.io.File file, android.widget.TextView textView, com.tencent.qqlitepro.hook.SettingsInjector.PluginInfo pluginInfo, android.widget.CompoundButton compoundButton, boolean z) {
        try {
            com.tencent.qqlitepro.util.Config.setPluginEnabled(context, file.getName(), z);
            textView.setText(z ? "已启用" : "已停用");
            textView.setTextColor(z ? -12723068 : -6643546);
            android.widget.TextView textView2 = sPluginLog;
            java.lang.StringBuilder sb = new java.lang.StringBuilder("[插件] ");
            sb.append(pluginInfo.name);
            sb.append(" 已");
            sb.append(z ? "启用" : "停用");
            appendLog(textView2, sb.toString());
            java.lang.StringBuilder sb2 = new java.lang.StringBuilder();
            sb2.append(pluginInfo.name);
            sb2.append(z ? " 已启用" : " 已停用");
            toast(context, sb2.toString());
            reloadPluginEngine(context);
        } catch (java.lang.Throwable thRethrow14) {
            appendLog(sPluginLog, "[插件] 异常: " + thRethrow14);
        }
    }

    static /* synthetic */ boolean lambda$pluginCardRow$192(android.content.Context context, java.io.File file, com.tencent.qqlitepro.hook.SettingsInjector.PluginInfo pluginInfo, android.view.View view) {
        pluginDetailDialog(context, file, pluginInfo);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void pluginDetailDialog(android.content.Context context, java.io.File file, com.tencent.qqlitepro.hook.SettingsInjector.PluginInfo pluginInfo) {
        android.app.Dialog dialog = new android.app.Dialog(context, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        android.widget.LinearLayout linearLayout = new android.widget.LinearLayout(context);
        linearLayout.setOrientation(1);
        linearLayout.setPadding(dp(context, 18.0f), dp(context, 14.0f), dp(context, 18.0f), dp(context, 16.0f));
        linearLayout.addView(text(context, pluginInfo.name, TITLE, 18.0f, 3), rootLp());
        linearLayout.addView(text(context, "版本 v" + pluginInfo.version, -6643546, 12.0f, 3), rootLp());
        makeGroupGap(linearLayout);
        linearLayout.addView(text(context, "描述", ACCENT, 13.0f, 3), rootLp());
        android.widget.TextView textViewText = text(context, (pluginInfo.desc == null || pluginInfo.desc.isEmpty()) ? "（无描述）" : pluginInfo.desc, TITLE, 13.0f, 3);
        textViewText.setPadding(0, dp(context, 4.0f), 0, dp(context, 6.0f));
        linearLayout.addView(textViewText, rootLp());
        linearLayout.addView(gap(context, 2.0f));
        android.view.View viewSaveButton = saveButton(context, "删除插件", new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda150(context, file, pluginInfo, dialog));
        marginBottom(viewSaveButton, 10.0f);
        linearLayout.addView(viewSaveButton);
        android.view.View viewSaveButton2 = saveButton(context, "关闭", new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda205(dialog));
        marginBottom(viewSaveButton2, 10.0f);
        linearLayout.addView(viewSaveButton2);
        com.tencent.qqlitepro.hook.SettingsInjector.ScrollableCard scrollableCard = new com.tencent.qqlitepro.hook.SettingsInjector.ScrollableCard(context, linearLayout);
        scrollableCard.setPadding(0, dp(context, 14.0f), 0, dp(context, 14.0f));
        dialog.setContentView(scrollableCard);
        android.view.WindowManager.LayoutParams attributes = dialog.getWindow().getAttributes();
        attributes.width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.94f);
        dialog.getWindow().setAttributes(attributes);
        dialog.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void confirmDeletePlugin(android.content.Context context, java.io.File file, com.tencent.qqlitepro.hook.SettingsInjector.PluginInfo pluginInfo, java.lang.Runnable runnable) {
        android.app.Dialog dialog = new android.app.Dialog(context, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        android.widget.LinearLayout linearLayout = new android.widget.LinearLayout(context);
        linearLayout.setOrientation(1);
        linearLayout.setPadding(dp(context, 18.0f), dp(context, 16.0f), dp(context, 18.0f), dp(context, 16.0f));
        linearLayout.addView(text(context, "确认删除插件？", TITLE, 16.0f, 3), rootLp());
        linearLayout.addView(text(context, "将删除 " + pluginInfo.name + " 及其设置，不可恢复。", -6643546, 12.0f, 3), rootLp());
        makeGroupGap(linearLayout);
        android.view.View viewSaveButton = saveButton(context, "删除", new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda216(context, file, pluginInfo, dialog, runnable));
        marginBottom(viewSaveButton, 10.0f);
        linearLayout.addView(viewSaveButton);
        android.view.View viewSaveButton2 = saveButton(context, "取消", new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda205(dialog));
        marginBottom(viewSaveButton2, 10.0f);
        linearLayout.addView(viewSaveButton2);
        com.tencent.qqlitepro.hook.SettingsInjector.ScrollableCard scrollableCard = new com.tencent.qqlitepro.hook.SettingsInjector.ScrollableCard(context, linearLayout);
        scrollableCard.setPadding(0, dp(context, 14.0f), 0, dp(context, 14.0f));
        dialog.setContentView(scrollableCard);
        android.view.WindowManager.LayoutParams attributes = dialog.getWindow().getAttributes();
        attributes.width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.94f);
        dialog.getWindow().setAttributes(attributes);
        dialog.show();
    }

    static /* synthetic */ void lambda$confirmDeletePlugin$195(android.content.Context context, java.io.File file, com.tencent.qqlitepro.hook.SettingsInjector.PluginInfo pluginInfo, android.app.Dialog dialog, java.lang.Runnable runnable) {
        try {
            com.tencent.qqlitepro.util.Config.setPluginEnabled(context, file.getName(), false);
            if (file.exists()) {
                file.delete();
            }
            java.io.File filePluginSettingsFile = pluginSettingsFile(context, file);
            if (filePluginSettingsFile.exists()) {
                filePluginSettingsFile.delete();
            }
            appendLog(sPluginLog, "[插件] 已删除 " + pluginInfo.name);
            renderPluginList(context);
            reloadPluginAndRestart(context);
        } catch (java.lang.Throwable thRethrow14) {
            appendLog(sPluginLog, "[插件] 删除失败: " + thRethrow14);
        }
        dialog.dismiss();
        if (runnable != null) {
            runnable.run();
        }
    }

    private static java.io.File pluginSettingsFile(android.content.Context context, java.io.File file) {
        java.io.File filePluginDir = pluginDir(context);
        java.lang.String name = file.getName();
        if (name.endsWith(".js")) {
            name = name.substring(0, name.length() - 3);
        }
        return new java.io.File(filePluginDir, name + ".settings");
    }

    private static void pluginSettingsDialog(android.content.Context context, java.io.File file, com.tencent.qqlitepro.hook.SettingsInjector.PluginInfo pluginInfo) {
        if (pluginInfo.settings != null && !pluginInfo.settings.isEmpty()) {
            pluginSettingsStructured(context, file, pluginInfo);
            return;
        }
        android.app.Dialog dialog = new android.app.Dialog(context, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        java.io.File filePluginSettingsFile = pluginSettingsFile(context, file);
        android.widget.LinearLayout linearLayout = new android.widget.LinearLayout(context);
        linearLayout.setOrientation(1);
        linearLayout.setPadding(dp(context, 18.0f), dp(context, 14.0f), dp(context, 18.0f), dp(context, 16.0f));
        linearLayout.addView(text(context, "插件设置 - " + pluginInfo.name, TITLE, 16.0f, 3), rootLp());
        linearLayout.addView(text(context, "每行一条 key=value，保存后可被插件读取。", -6643546, 12.0f, 3), rootLp());
        makeGroupGap(linearLayout);
        linearLayout.addView(text(context, "子功能", TITLE, 14.0f, 3), rootLp());
        addPluginFeaturesBlock(context, linearLayout, file);
        makeGroupGap(linearLayout);
        linearLayout.addView(text(context, "设置项", TITLE, 14.0f, 3), rootLp());
        android.widget.EditText editText = new android.widget.EditText(context);
        editText.setText(readTextFile(filePluginSettingsFile));
        editText.setGravity(48);
        editText.setTextColor(TITLE);
        editText.setHintTextColor(HINT);
        editText.setTextSize(13.0f);
        editText.setPadding(0, dp(context, 8.0f), 0, dp(context, 8.0f));
        linearLayout.addView(editText, rootLp());
        makeGroupGap(linearLayout);
        android.view.View viewSaveButton = saveButton(context, "保存设置", new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda131(editText, filePluginSettingsFile, pluginInfo, context, dialog));
        marginBottom(viewSaveButton, 10.0f);
        linearLayout.addView(viewSaveButton);
        android.view.View viewSaveButton2 = saveButton(context, "关闭", new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda205(dialog));
        marginBottom(viewSaveButton2, 10.0f);
        linearLayout.addView(viewSaveButton2);
        com.tencent.qqlitepro.hook.SettingsInjector.ScrollableCard scrollableCard = new com.tencent.qqlitepro.hook.SettingsInjector.ScrollableCard(context, linearLayout);
        scrollableCard.setPadding(0, dp(context, 14.0f), 0, dp(context, 14.0f));
        dialog.setContentView(scrollableCard);
        android.view.WindowManager.LayoutParams attributes = dialog.getWindow().getAttributes();
        attributes.width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.94f);
        dialog.getWindow().setAttributes(attributes);
        dialog.show();
    }

    static /* synthetic */ void lambda$pluginSettingsDialog$196(android.widget.EditText editText, java.io.File file, com.tencent.qqlitepro.hook.SettingsInjector.PluginInfo pluginInfo, android.content.Context context, android.app.Dialog dialog) {
        boolean zWriteTextFile = writeTextFile(file, editText.getText() == null ? "" : editText.getText().toString());
        android.widget.TextView textView = sPluginLog;
        java.lang.StringBuilder sb = new java.lang.StringBuilder("[插件] ");
        sb.append(pluginInfo.name);
        sb.append(" 设置已");
        sb.append(zWriteTextFile ? "保存" : "保存失败");
        appendLog(textView, sb.toString());
        toast(context, zWriteTextFile ? "设置已保存" : "保存失败");
        dialog.dismiss();
    }

    private static java.util.LinkedHashMap<java.lang.String, java.lang.String> readSettings(java.io.File file) {
        java.lang.String textFile;
        java.util.LinkedHashMap<java.lang.String, java.lang.String> linkedHashMap = new java.util.LinkedHashMap<>();
        if (file != null && file.exists() && (textFile = readTextFile(file)) != null && !textFile.isEmpty()) {
            for (java.lang.String str : textFile.split("\\n")) {
                int iIndexOf = str.indexOf(61);
                if (iIndexOf > 0) {
                    java.lang.String strTrim = str.substring(0, iIndexOf).trim();
                    if (!strTrim.isEmpty()) {
                        linkedHashMap.put(strTrim, str.substring(iIndexOf + 1).trim());
                    }
                }
            }
        }
        return linkedHashMap;
    }

    private static void writeSettings(java.io.File file, java.util.LinkedHashMap<java.lang.String, java.lang.String> linkedHashMap) {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        if (linkedHashMap != null) {
            for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : linkedHashMap.entrySet()) {
                sb.append(entry.getKey());
                sb.append('=');
                sb.append(entry.getValue() == null ? "" : entry.getValue());
                sb.append('\n');
            }
        }
        writeTextFile(file, sb.toString());
    }

    private static void pluginSettingsStructured(android.content.Context context, java.io.File file, com.tencent.qqlitepro.hook.SettingsInjector.PluginInfo pluginInfo) {
        java.io.File filePluginSettingsFile = pluginSettingsFile(context, file);
        java.util.LinkedHashMap<java.lang.String, java.lang.String> settings = readSettings(filePluginSettingsFile);
        java.util.ArrayList arrayList = new java.util.ArrayList();
        android.app.Dialog dialog = new android.app.Dialog(context, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        android.widget.LinearLayout linearLayout = new android.widget.LinearLayout(context);
        char c = 1;
        linearLayout.setOrientation(1);
        linearLayout.setPadding(dp(context, 18.0f), dp(context, 14.0f), dp(context, 18.0f), dp(context, 16.0f));
        linearLayout.addView(text(context, "插件设置 - " + pluginInfo.name, TITLE, 16.0f, 3), rootLp());
        linearLayout.addView(text(context, "可自定义开启/关闭指令等，保存后即时生效。", -6643546, 12.0f, 3), rootLp());
        makeGroupGap(linearLayout);
        linearLayout.addView(text(context, "子功能", TITLE, 14.0f, 3), rootLp());
        addPluginFeaturesBlock(context, linearLayout, file);
        makeGroupGap(linearLayout);
        linearLayout.addView(text(context, "设置项", TITLE, 14.0f, 3), rootLp());
        makeGroupGap(linearLayout);
        android.widget.LinearLayout linearLayout2 = new android.widget.LinearLayout(context);
        linearLayout2.setOrientation(1);
        linearLayout2.setLayoutParams(rootLp());
        linearLayout.addView(linearLayout2);
        java.util.LinkedHashMap linkedHashMap = new java.util.LinkedHashMap();
        for (java.lang.String[] strArr : pluginInfo.settings) {
            linkedHashMap.put(strArr[0], strArr);
        }
        java.util.LinkedHashSet<java.lang.String> linkedHashSet = new java.util.LinkedHashSet();
        java.util.Iterator<java.lang.String[]> it = pluginInfo.settings.iterator();
        while (it.hasNext()) {
            linkedHashSet.add(it.next()[0]);
        }
        java.util.Iterator<java.lang.String> it2 = settings.keySet().iterator();
        while (it2.hasNext()) {
            linkedHashSet.add(it2.next());
        }
        for (java.lang.String str : linkedHashSet) {
            java.lang.String[] strArr2 = (java.lang.String[]) linkedHashMap.get(str);
            java.lang.String str2 = strArr2 != null ? strArr2[c] : str;
            java.lang.String str3 = strArr2 != null ? strArr2[2] : "";
            if (settings.containsKey(str)) {
                str3 = settings.get(str);
            }
            addSettingRow(linearLayout2, arrayList, str, str2, str3, strArr2 != null, context);
            linearLayout2 = linearLayout2;
            linkedHashMap = linkedHashMap;
            c = 1;
        }
        makeGroupGap(linearLayout);
        android.view.View viewSaveButton = saveButton(context, "新增设置项", new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda183(linearLayout2, arrayList, context));
        marginBottom(viewSaveButton, 10.0f);
        linearLayout.addView(viewSaveButton);
        android.view.View viewSaveButton2 = saveButton(context, "保存设置", new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda194(arrayList, filePluginSettingsFile, pluginInfo, context, dialog));
        marginBottom(viewSaveButton2, 10.0f);
        linearLayout.addView(viewSaveButton2);
        android.view.View viewSaveButton3 = saveButton(context, "关闭", new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda205(dialog));
        marginBottom(viewSaveButton3, 10.0f);
        linearLayout.addView(viewSaveButton3);
        com.tencent.qqlitepro.hook.SettingsInjector.ScrollableCard scrollableCard = new com.tencent.qqlitepro.hook.SettingsInjector.ScrollableCard(context, linearLayout);
        scrollableCard.setPadding(0, dp(context, 14.0f), 0, dp(context, 14.0f));
        dialog.setContentView(scrollableCard);
        android.view.WindowManager.LayoutParams attributes = dialog.getWindow().getAttributes();
        attributes.width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.94f);
        dialog.getWindow().setAttributes(attributes);
        dialog.show();
    }

    static /* synthetic */ void lambda$pluginSettingsStructured$198(java.util.List list, java.io.File file, com.tencent.qqlitepro.hook.SettingsInjector.PluginInfo pluginInfo, android.content.Context context, android.app.Dialog dialog) {
        java.util.LinkedHashMap linkedHashMap = new java.util.LinkedHashMap();
        java.util.Iterator it = list.iterator();
        while (it.hasNext()) {
            java.util.List list2 = (java.util.List) it.next();
            java.lang.String strTrim = ((android.widget.EditText) list2.get(0)).getText() == null ? "" : ((android.widget.EditText) list2.get(0)).getText().toString().trim();
            java.lang.String string = ((android.widget.EditText) list2.get(1)).getText() != null ? ((android.widget.EditText) list2.get(1)).getText().toString() : "";
            if (!strTrim.isEmpty()) {
                linkedHashMap.put(strTrim, string);
            }
        }
        writeSettings(file, linkedHashMap);
        appendLog(sPluginLog, "[插件] " + pluginInfo.name + " 设置已保存");
        toast(context, "设置已保存");
        dialog.dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void addSettingRow(android.widget.LinearLayout linearLayout, java.util.List<java.util.List<android.widget.EditText>> list, java.lang.String str, java.lang.String str2, java.lang.String str3, boolean z, android.content.Context context) {
        android.widget.LinearLayout linearLayout2 = new android.widget.LinearLayout(context);
        linearLayout2.setOrientation(1);
        android.widget.LinearLayout.LayoutParams layoutParamsRootLp = rootLp();
        layoutParamsRootLp.bottomMargin = dp(context, 8.0f);
        linearLayout2.setLayoutParams(layoutParamsRootLp);
        android.widget.LinearLayout linearLayout3 = new android.widget.LinearLayout(context);
        linearLayout3.setOrientation(0);
        linearLayout3.setGravity(16);
        linearLayout3.setLayoutParams(rootLp());
        android.widget.EditText editText = new android.widget.EditText(context);
        editText.setText(str);
        editText.setEnabled(!z);
        editText.setHint(z ? str2 : "键（key）");
        editText.setTextColor(TITLE);
        editText.setHintTextColor(HINT);
        editText.setTextSize(13.0f);
        editText.setGravity(17);
        editText.setPadding(0, dp(context, 6.0f), 0, dp(context, 6.0f));
        android.widget.EditText editText2 = new android.widget.EditText(context);
        editText2.setText(str3);
        editText2.setHint(str2);
        editText2.setTextColor(TITLE);
        editText2.setHintTextColor(HINT);
        editText2.setTextSize(13.0f);
        editText2.setGravity(17);
        editText2.setPadding(dp(context, 8.0f), dp(context, 6.0f), 0, dp(context, 6.0f));
        if (z) {
            linearLayout2.addView(text(context, str2, ACCENT, 12.0f, 3), rootLp());
            linearLayout3.addView(editText2, new android.widget.LinearLayout.LayoutParams(0, -2, 1.0f));
        } else {
            linearLayout3.addView(editText, new android.widget.LinearLayout.LayoutParams(0, -2, 1.0f));
            linearLayout3.addView(editText2, new android.widget.LinearLayout.LayoutParams(0, -2, 1.6f));
        }
        if (!z) {
            android.widget.TextView textViewText = text(context, "✕", -1944482, 15.0f, 17);
            textViewText.setClickable(true);
            textViewText.setPadding(dp(context, 8.0f), dp(context, 6.0f), dp(context, 4.0f), dp(context, 6.0f));
            textViewText.setOnClickListener(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda197(linearLayout, linearLayout2, list, editText2));
            linearLayout3.addView(textViewText, new android.widget.LinearLayout.LayoutParams(-2, -2));
        }
        linearLayout2.addView(linearLayout3, rootLp());
        linearLayout.addView(linearLayout2);
        java.util.ArrayList arrayList = new java.util.ArrayList();
        arrayList.add(editText);
        arrayList.add(editText2);
        list.add(arrayList);
    }

    static /* synthetic */ void lambda$addSettingRow$199(android.widget.LinearLayout linearLayout, android.widget.LinearLayout linearLayout2, java.util.List list, android.widget.EditText editText, android.view.View view) {
        linearLayout.removeView(linearLayout2);
        java.util.Iterator it = list.iterator();
        while (it.hasNext()) {
            if (((java.util.List) it.next()).get(1) == editText) {
                it.remove();
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void launchPluginPicker() {
        android.app.Activity activity = sPluginHost;
        if (activity == null) {
            toast(activity, "未获取宿主 Activity，无法调起选择器");
            return;
        }
        try {
            android.content.Intent intent = new android.content.Intent("android.intent.action.GET_CONTENT");
            intent.setType("*/*");
            intent.addCategory("android.intent.category.OPENABLE");
            activity.startActivityForResult(android.content.Intent.createChooser(intent, "选择处理的应用后再选择 .js 插件文件"), REQ_PLUGIN_PICK);
            appendLog(sPluginLog, "[导入] 已调起系统应用选择器（先选用适合的处理应用）");
        } catch (java.lang.Throwable thRethrow14) {
            appendLog(sPluginLog, "[导入] 失败: " + thRethrow14);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void handlePluginPicked(android.app.Activity activity, android.net.Uri uri) {
        if (uri == null) {
            return;
        }
        appendLog(sPluginLog, "[导入] 收到文件选择结果");
        if (activity == null && (activity = sPluginHost) == null) {
            activity = null;
        }
        java.io.File filePluginDir = activity != null ? pluginDir(activity) : null;
        if (activity == null || filePluginDir == null) {
            appendLog(sPluginLog, "[导入] 未获取下文无法处理");
        } else {
            new java.lang.Thread(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda125(activity, uri, new android.os.Handler(android.os.Looper.getMainLooper()), filePluginDir), "plugin-import").start();
        }
    }

    static /* synthetic */ void lambda$handlePluginPicked$203(android.content.Context context, android.net.Uri uri, android.os.Handler handler, java.io.File file) {
        java.io.InputStream inputStreamOpenInputStream;
        try {
            java.lang.String strUriDisplayName = uriDisplayName(context, uri);
            if (strUriDisplayName == null || strUriDisplayName.isEmpty()) {
                strUriDisplayName = "plugin_" + java.lang.System.currentTimeMillis() + ".js";
            }
            byte[] bArr = new byte[8192];
            java.io.ByteArrayOutputStream byteArrayOutputStream = new java.io.ByteArrayOutputStream();
            if ("file".equals(uri.getScheme())) {
                inputStreamOpenInputStream = new java.io.FileInputStream(new java.io.File(uri.getPath()));
            } else {
                inputStreamOpenInputStream = context.getContentResolver().openInputStream(uri);
            }
            if (inputStreamOpenInputStream == null) {
                throw new java.lang.RuntimeException("无法读取所选文件");
            }
            while (true) {
                int i = inputStreamOpenInputStream.read(bArr);
                if (i < 0) {
                    break;
                } else {
                    byteArrayOutputStream.write(bArr, 0, i);
                }
            }
            inputStreamOpenInputStream.close();
            java.lang.String str = new java.lang.String(byteArrayOutputStream.toByteArray(), "UTF-8");
            com.tencent.qqlitepro.hook.SettingsInjector.PluginInfo plugin = parsePlugin(str);
            if (plugin == null) {
                handler.post(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda24(context));
                return;
            }
            java.lang.String strReplaceAll = strUriDisplayName.replaceAll("[\\\\/:*?\"<>|]", "_");
            if (!strReplaceAll.toLowerCase().endsWith(".js")) {
                strReplaceAll = strReplaceAll + ".js";
            }
            java.io.File file2 = new java.io.File(file, strReplaceAll);
            removeDupPlugins(file, plugin.name, strReplaceAll);
            writeTextFile(file2, str);
            handler.post(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda35(context, strReplaceAll, plugin));
        } catch (java.lang.Throwable thRethrow14) {
            handler.post(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda46(thRethrow14));
        }
    }

    static /* synthetic */ void lambda$handlePluginPicked$200(android.content.Context context) {
        toast(context, "插件无效（缺少 @name 头部注释）");
        appendLog(sPluginLog, "[导入] 拒绝：缺少 @name 头部注释");
    }

    static /* synthetic */ void lambda$handlePluginPicked$201(android.content.Context context, java.lang.String str, com.tencent.qqlitepro.hook.SettingsInjector.PluginInfo pluginInfo) {
        com.tencent.qqlitepro.util.Config.setPluginEnabled(context, str, true);
        renderPluginList(context);
        appendLog(sPluginLog, "[导入] 已导入并默认开启: " + pluginInfo.name + " (v" + pluginInfo.version + ")");
        reloadPluginAndRestart(context);
    }

    private static void removeDupPlugins(java.io.File file, java.lang.String str, java.lang.String str2) {
        if (file == null || str == null || str.isEmpty()) {
            return;
        }
        try {
            java.io.File[] fileArrListFiles = file.listFiles(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda127());
            if (fileArrListFiles == null) {
                return;
            }
            for (java.io.File file2 : fileArrListFiles) {
                if (!file2.getName().equalsIgnoreCase(str2)) {
                    try {
                        com.tencent.qqlitepro.hook.SettingsInjector.PluginInfo plugin = parsePlugin(readTextFile(file2));
                        if (plugin != null && str.equals(plugin.name)) {
                            java.lang.String name = file2.getName();
                            java.io.File file3 = new java.io.File(file, name + ".features");
                            if (file3.exists()) {
                                file3.delete();
                            }
                            if (name.endsWith(".js")) {
                                name = name.substring(0, name.length() - 3);
                            }
                            java.io.File file4 = new java.io.File(file, name + ".settings");
                            if (file4.exists()) {
                                file4.delete();
                            }
                            if (file2.exists()) {
                                file2.delete();
                            }
                        }
                    } catch (java.lang.Throwable unused) {
                    }
                }
            }
        } catch (java.lang.Throwable unused2) {
        }
    }

    private static void buildAbout(android.content.Context context, android.view.ViewGroup viewGroup) {
        viewGroup.addView(backRow(context));
        android.widget.LinearLayout linearLayout = new android.widget.LinearLayout(context);
        linearLayout.setOrientation(1);
        linearLayout.setGravity(1);
        linearLayout.setPadding(dp(context, 20.0f), dp(context, 12.0f), dp(context, 20.0f), dp(context, 20.0f));
        linearLayout.setLayoutParams(rootLp());
        android.widget.ImageView imageView = new android.widget.ImageView(context);
        android.graphics.drawable.Drawable drawableLoadModuleIcon = loadModuleIcon(context);
        if (drawableLoadModuleIcon != null) {
            imageView.setImageDrawable(drawableLoadModuleIcon);
        }
        android.graphics.drawable.GradientDrawable gradientDrawable = new android.graphics.drawable.GradientDrawable();
        gradientDrawable.setShape(1);
        gradientDrawable.setColor(-14671580);
        gradientDrawable.setStroke(dp(context, 2.0f), ACCENT);
        int iDp = dp(context, 96.0f);
        imageView.setLayoutParams(new android.widget.LinearLayout.LayoutParams(iDp, iDp));
        imageView.setPadding(dp(context, 18.0f), dp(context, 18.0f), dp(context, 18.0f), dp(context, 18.0f));
        imageView.setBackground(gradientDrawable);
        linearLayout.addView(imageView);
        android.widget.TextView textViewText = text(context, "QQMax 增强模块", TITLE, 18.0f, 17);
        textViewText.setPadding(0, dp(context, 18.0f), 0, dp(context, 4.0f));
        linearLayout.addView(textViewText, rootLp());
        linearLayout.addView(infoLine(context, "版本", "v1.2.1"));
        linearLayout.addView(infoLine(context, "作者", "疏影阑珊"));
        linearLayout.addView(infoLine(context, "官方群聊", "801524531"));
        linearLayout.addView(infoLine(context, "开源许可", "GPL-3.0"));
        makeGroupGap(linearLayout);
        android.widget.TextView textViewText2 = text(context, "模块状态", ACCENT, 14.0f, 8388611);
        textViewText2.setPadding(0, dp(context, 8.0f), 0, 0);
        linearLayout.addView(textViewText2);
        linearLayout.addView(statusTextLine(context, "运行框架", com.tencent.qqlitepro.util.Status.frameworkName(com.tencent.qqlitepro.util.Status.framework(context)), fwColor(com.tencent.qqlitepro.util.Status.framework(context)), TAG_STATUS_FW));
        linearLayout.addView(statusLine(context, "激活状态", com.tencent.qqlitepro.util.Status.activated(context), TAG_STATUS_ACT));
        linearLayout.addView(statusLine(context, "注入状态", com.tencent.qqlitepro.util.Status.injected(context), TAG_STATUS_INJ));
        android.widget.TextView textViewText3 = text(context, "", -1739917, 13.0f, 17);
        textViewText3.setPadding(0, dp(context, 4.0f), 0, 0);
        textViewText3.setTag(TAG_STATUS_HINT);
        linearLayout.addView(textViewText3);
        startStatusRefresh(viewGroup);
        makeGroupGap(linearLayout);
        linearLayout.addView(saveButton(context, "重启 QQ", new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda95(context)));
        makeGroupGap(linearLayout);
        linearLayout.addView(saveButton(context, "清除缓存并重启 QQ", new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda96(context)));
        makeGroupGap(linearLayout);
        addResetButton(context, linearLayout, "恢复默认", 4, new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda97(context));
        android.widget.TextView textViewText4 = text(context, "Made by TraeWork", -10177034, 11.0f, 17);
        textViewText4.setPadding(0, dp(context, 14.0f), 0, 0);
        linearLayout.addView(textViewText4, rootLp());
        viewGroup.addView(linearLayout);
    }

    private static android.graphics.drawable.Drawable loadModuleIcon(android.content.Context context) {
        try {
            return context.createPackageContext("com.tencent.qqlitepro", 2).getResources().getDrawable(com.tencent.qqlitepro.R.mipmap.ic_launcher);
        } catch (java.lang.Throwable thRethrow14) {
            com.tencent.qqlitepro.util.Log.w("加载模块图标失败: " + thRethrow14);
            return null;
        }
    }

    private static android.view.View infoLine(android.content.Context context, java.lang.String str, java.lang.String str2) {
        android.widget.TextView textViewText = text(context, str + " : " + str2, -6643546, 14.0f, 17);
        textViewText.setPadding(0, dp(context, 5.0f), 0, dp(context, 5.0f));
        return textViewText;
    }

    private static android.view.View statusLine(android.content.Context context, java.lang.String str, boolean z, java.lang.String str2) {
        java.lang.StringBuilder sb = z ? new java.lang.StringBuilder("已") : new java.lang.StringBuilder("未");
        sb.append(str.replace("状态", ""));
        return statusTextLine(context, str, sb.toString(), z ? -11751600 : -1739917, str2);
    }

    private static android.view.View statusTextLine(android.content.Context context, java.lang.String str, java.lang.String str2, int i, java.lang.String str3) {
        android.widget.TextView textViewText = text(context, str + " : " + str2, i, 14.0f, 17);
        textViewText.setPadding(0, dp(context, 5.0f), 0, dp(context, 5.0f));
        textViewText.setTag(str3);
        return textViewText;
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.SettingsInjector$11, reason: invalid class name */
    static class AnonymousClass11 implements java.lang.Runnable {
        final /* synthetic */ android.view.ViewGroup val$group;
        final /* synthetic */ android.os.Handler val$h;

        AnonymousClass11(android.view.ViewGroup viewGroup, android.os.Handler handler) {
            this.val$group = viewGroup;
            this.val$h = handler;
        }

        @Override // java.lang.Runnable
        public void run() {
            android.view.View viewFindViewWithTag = this.val$group.findViewWithTag(com.tencent.qqlitepro.hook.SettingsInjector.TAG_STATUS_FW);
            android.view.View viewFindViewWithTag2 = this.val$group.findViewWithTag(com.tencent.qqlitepro.hook.SettingsInjector.TAG_STATUS_ACT);
            android.view.View viewFindViewWithTag3 = this.val$group.findViewWithTag(com.tencent.qqlitepro.hook.SettingsInjector.TAG_STATUS_INJ);
            android.view.View viewFindViewWithTag4 = this.val$group.findViewWithTag(com.tencent.qqlitepro.hook.SettingsInjector.TAG_STATUS_HINT);
            if (viewFindViewWithTag == null || viewFindViewWithTag2 == null || viewFindViewWithTag3 == null || viewFindViewWithTag.getParent() == null || viewFindViewWithTag2.getParent() == null || viewFindViewWithTag3.getParent() == null) {
                return;
            }
            android.content.Context context = this.val$group.getContext();
            int iFramework = com.tencent.qqlitepro.util.Status.framework(context);
            boolean zActivated = com.tencent.qqlitepro.util.Status.activated(context);
            boolean zInjected = com.tencent.qqlitepro.util.Status.injected(context);
            if (viewFindViewWithTag instanceof android.widget.TextView) {
                android.widget.TextView textView = (android.widget.TextView) viewFindViewWithTag;
                textView.setText("运行框架 : " + com.tencent.qqlitepro.util.Status.frameworkName(iFramework));
                textView.setTextColor(com.tencent.qqlitepro.hook.SettingsInjector.fwColor(iFramework));
            }
            if (viewFindViewWithTag2 instanceof android.widget.TextView) {
                android.widget.TextView textView2 = (android.widget.TextView) viewFindViewWithTag2;
                textView2.setText("激活状态 : ".concat(zActivated ? "已激活" : "未激活"));
                textView2.setTextColor(zActivated ? -11751600 : -1739917);
            }
            if (viewFindViewWithTag3 instanceof android.widget.TextView) {
                android.widget.TextView textView3 = (android.widget.TextView) viewFindViewWithTag3;
                textView3.setText("注入状态 : ".concat(zInjected ? "已注入" : "未注入"));
                textView3.setTextColor(zInjected ? -11751600 : -1739917);
            }
            if (viewFindViewWithTag4 instanceof android.widget.TextView) {
                java.lang.StringBuilder sb = new java.lang.StringBuilder();
                if (zActivated && zInjected) {
                    sb.append("模块已在 QQMax 中生效");
                } else {
                    sb.append("未生效：");
                    sb.append(com.tencent.qqlitepro.util.Status.activationHint(iFramework));
                }
                java.lang.String strAndroid81Hint = com.tencent.qqlitepro.util.Status.android81Hint();
                if (strAndroid81Hint.length() > 0) {
                    sb.append('\n');
                    sb.append(strAndroid81Hint);
                }
                android.widget.TextView textView4 = (android.widget.TextView) viewFindViewWithTag4;
                textView4.setText(sb.toString());
                textView4.setTextColor((zActivated && zInjected) ? -11751600 : -1739917);
            }
            this.val$h.postDelayed(this, 1000L);
        }
    }

    private static void startStatusRefresh(android.view.ViewGroup viewGroup) {
        if (viewGroup == null) {
            return;
        }
        android.os.Handler handler = new android.os.Handler(android.os.Looper.getMainLooper());
        handler.post(new com.tencent.qqlitepro.hook.SettingsInjector.AnonymousClass11(viewGroup, handler));
    }

    private static android.widget.TextView section(android.content.Context context, java.lang.String str) {
        android.widget.TextView textViewText = text(context, str, ACCENT, 14.0f, 8388611);
        textViewText.setPadding(dp(context, 20.0f), dp(context, 14.0f), dp(context, 20.0f), dp(context, 6.0f));
        return textViewText;
    }

    private static android.view.View switchRow(android.content.Context context, java.lang.String str, java.lang.String str2, boolean z, com.tencent.qqlitepro.hook.SettingsInjector.ThConsumer thConsumer) {
        return switchRowState(context, str, str2, z, null, thConsumer);
    }

    private static android.view.View switchRowState(android.content.Context context, java.lang.String str, java.lang.String str2, boolean z, android.widget.CompoundButton[] compoundButtonArr) {
        return switchRowState(context, str, str2, z, compoundButtonArr, null);
    }

    private static android.view.View switchRowState(android.content.Context context, java.lang.String str, java.lang.String str2, boolean z, android.widget.CompoundButton[] compoundButtonArr, com.tencent.qqlitepro.hook.SettingsInjector.ThConsumer thConsumer) {
        android.widget.LinearLayout linearLayout = new android.widget.LinearLayout(context);
        linearLayout.setOrientation(0);
        linearLayout.setGravity(16);
        android.graphics.drawable.GradientDrawable gradientDrawable = new android.graphics.drawable.GradientDrawable();
        gradientDrawable.setShape(0);
        gradientDrawable.setColor(SURFACE_CONTAINER);
        gradientDrawable.setCornerRadius(dpLite(14.0f));
        linearLayout.setBackground(gradientDrawable);
        linearLayout.setPadding(dp(context, 12.0f), dp(context, 12.0f), dp(context, 12.0f), dp(context, 12.0f));
        android.widget.LinearLayout.LayoutParams layoutParamsRootLp = rootLp();
        layoutParamsRootLp.leftMargin = dp(context, 12.0f);
        layoutParamsRootLp.rightMargin = dp(context, 12.0f);
        layoutParamsRootLp.bottomMargin = dp(context, 6.0f);
        linearLayout.setLayoutParams(layoutParamsRootLp);
        android.widget.LinearLayout linearLayout2 = new android.widget.LinearLayout(context);
        linearLayout2.setOrientation(1);
        linearLayout2.setLayoutParams(new android.widget.LinearLayout.LayoutParams(0, -2, 1.0f));
        android.widget.TextView textViewText = text(context, str, TITLE, 15.0f, 8388611);
        textViewText.setPadding(0, 0, dp(context, 8.0f), 0);
        android.widget.TextView textViewText2 = text(context, str2, -6643546, 11.0f, 8388611);
        linearLayout2.addView(textViewText, rootLp());
        linearLayout2.addView(textViewText2, rootLp());
        linearLayout.addView(linearLayout2);
        android.widget.CompoundButton compoundButtonCreateSwitch = createSwitch(context);
        compoundButtonCreateSwitch.setChecked(z);
        if (compoundButtonArr != null) {
            compoundButtonArr[0] = compoundButtonCreateSwitch;
        }
        if (thConsumer != null) {
            compoundButtonCreateSwitch.setOnCheckedChangeListener(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda139(thConsumer));
        }
        linearLayout.addView(compoundButtonCreateSwitch);
        return linearLayout;
    }

    private static android.widget.CompoundButton createSwitch(android.content.Context context) {
        try {
            return (android.widget.CompoundButton) java.lang.Class.forName("com.google.android.material.switchmaterial.SwitchMaterial", false, context.getClassLoader()).getDeclaredConstructor(android.content.Context.class).newInstance(context);
        } catch (java.lang.Throwable unused) {
            android.widget.Switch r0 = new android.widget.Switch(context);
            styleM3Switch(r0);
            return r0;
        }
    }

    private static void styleM3Switch(android.widget.Switch r4) {
        try {
            int[][] iArr = {new int[]{android.R.attr.state_checked}, new int[0]};
            android.content.res.ColorStateList colorStateList = new android.content.res.ColorStateList(iArr, new int[]{ACCENT, SWITCH_TRACK_OFF});
            android.content.res.ColorStateList colorStateList2 = new android.content.res.ColorStateList(iArr, new int[]{-1, -6643546});
            r4.setTrackTintList(colorStateList);
            r4.setThumbTintList(colorStateList2);
            r4.setScaleX(0.88f);
            r4.setScaleY(0.88f);
        } catch (java.lang.Throwable thRethrow14) {
            com.tencent.qqlitepro.util.Log.w("设置 Switch M3 样式失败: " + thRethrow14);
        }
    }

    private static android.view.View modeRow(android.content.Context context, java.lang.String str, java.lang.String str2, boolean z, boolean z2) {
        int i = z2 ? 1 : 2;
        android.widget.LinearLayout linearLayout = new android.widget.LinearLayout(context);
        linearLayout.setOrientation(0);
        linearLayout.setGravity(16);
        linearLayout.setPadding(dp(context, 20.0f), dp(context, 12.0f), dp(context, 20.0f), dp(context, 12.0f));
        linearLayout.setLayoutParams(rootLp());
        linearLayout.setTag(java.lang.Integer.valueOf(i));
        linearLayout.setBackground(makeRowBackground(z));
        android.widget.LinearLayout linearLayout2 = new android.widget.LinearLayout(context);
        linearLayout2.setOrientation(1);
        linearLayout2.setLayoutParams(new android.widget.LinearLayout.LayoutParams(0, -2, 1.0f));
        android.widget.TextView textViewText = text(context, str, z ? ACCENT : TITLE, 15.0f, 8388611);
        android.widget.TextView textViewText2 = text(context, str2, -6643546, 11.0f, 8388611);
        linearLayout2.addView(textViewText, rootLp());
        linearLayout2.addView(textViewText2, rootLp());
        linearLayout.addView(linearLayout2);
        linearLayout.addView(text(context, z ? "✓" : "", z ? ACCENT : 0, 18.0f, 17));
        linearLayout.setOnClickListener(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda70(i));
        return linearLayout;
    }

    private static android.graphics.drawable.Drawable makeRowBackground(boolean z) {
        android.graphics.drawable.GradientDrawable gradientDrawable = new android.graphics.drawable.GradientDrawable();
        gradientDrawable.setShape(0);
        gradientDrawable.setColor(z ? 339442943 : 0);
        gradientDrawable.setCornerRadius(dpLite(12.0f));
        return gradientDrawable;
    }

    private static android.widget.EditText edit(android.content.Context context, java.lang.String str, java.lang.String str2, int i) {
        android.widget.EditText editText = new android.widget.EditText(context);
        if (str == null) {
            str = "";
        }
        editText.setText(str);
        if (str2 == null) {
            str2 = "";
        }
        editText.setHint(str2);
        editText.setMinLines(i);
        editText.setTextSize(14.0f);
        editText.setTextColor(TITLE);
        editText.setHintTextColor(HINT);
        editText.setPadding(dp(context, 12.0f), dp(context, 8.0f), dp(context, 12.0f), dp(context, 8.0f));
        return editText;
    }

    private static android.view.View editInCard(android.content.Context context, android.widget.EditText editText) {
        android.graphics.drawable.GradientDrawable gradientDrawable = new android.graphics.drawable.GradientDrawable();
        gradientDrawable.setShape(0);
        gradientDrawable.setColor(-16777216);
        gradientDrawable.setStroke(1, DIVIDER);
        gradientDrawable.setCornerRadius(dpLite(10.0f));
        editText.setBackground(gradientDrawable);
        return editText;
    }

    private static android.widget.LinearLayout.LayoutParams inputLp() {
        android.widget.LinearLayout.LayoutParams layoutParamsRootLp = rootLp();
        layoutParamsRootLp.topMargin = dpLite(4.0f);
        layoutParamsRootLp.bottomMargin = dpLite(6.0f);
        return layoutParamsRootLp;
    }

    private static void buildDownloadManage(android.content.Context context, android.view.ViewGroup viewGroup) {
        if (context instanceof android.app.Activity) {
            sFileHost = (android.app.Activity) context;
        }
        viewGroup.addView(section(context, "下载管理"));

        /* 下载文件路径 */
        android.widget.LinearLayout dlCard = new android.widget.LinearLayout(context);
        dlCard.setOrientation(1);
        android.graphics.drawable.GradientDrawable dlBg = new android.graphics.drawable.GradientDrawable();
        dlBg.setColor(SURFACE_CONTAINER);
        dlBg.setCornerRadius(dpLite(14.0f));
        dlCard.setBackground(dlBg);
        dlCard.setPadding(dp(context, 12.0f), dp(context, 14.0f), dp(context, 12.0f), dp(context, 14.0f));
        dlCard.setLayoutParams(rootLp());
        dlCard.addView(text(context, "文件保存路径", TITLE, 14.0f, 8388611), rootLp());
        dlCard.addView(text(context, com.tencent.qqlitepro.util.Config.downloadDir(context), -6643546, 12.0f, 8388611), rootLp());
        viewGroup.addView(dlCard);
        makeGroupGap(viewGroup);
        viewGroup.addView(saveButton(context, "修改文件保存路径", new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda217()));
        makeGroupGap(viewGroup);

        /* 图片保存路径 */
        android.widget.LinearLayout imgCard = new android.widget.LinearLayout(context);
        imgCard.setOrientation(1);
        android.graphics.drawable.GradientDrawable imgBg = new android.graphics.drawable.GradientDrawable();
        imgBg.setColor(SURFACE_CONTAINER);
        imgBg.setCornerRadius(dpLite(14.0f));
        imgCard.setBackground(imgBg);
        imgCard.setPadding(dp(context, 12.0f), dp(context, 14.0f), dp(context, 12.0f), dp(context, 14.0f));
        imgCard.setLayoutParams(rootLp());
        imgCard.addView(text(context, "图片保存路径（独立）", TITLE, 14.0f, 8388611), rootLp());
        imgCard.addView(text(context, com.tencent.qqlitepro.util.Config.imageSaveDir(context), -6643546, 12.0f, 8388611), rootLp());
        viewGroup.addView(imgCard);
        makeGroupGap(viewGroup);
        viewGroup.addView(saveButton(context, "修改图片保存路径", new Runnable() {
            @Override
            public void run() {
                pickMediaSaveDir(context, "image");
            }
        }));
        makeGroupGap(viewGroup);

        /* 视频保存路径 */
        android.widget.LinearLayout vidCard = new android.widget.LinearLayout(context);
        vidCard.setOrientation(1);
        android.graphics.drawable.GradientDrawable vidBg = new android.graphics.drawable.GradientDrawable();
        vidBg.setColor(SURFACE_CONTAINER);
        vidBg.setCornerRadius(dpLite(14.0f));
        vidCard.setBackground(vidBg);
        vidCard.setPadding(dp(context, 12.0f), dp(context, 14.0f), dp(context, 12.0f), dp(context, 14.0f));
        vidCard.setLayoutParams(rootLp());
        vidCard.addView(text(context, "视频保存路径（独立）", TITLE, 14.0f, 8388611), rootLp());
        vidCard.addView(text(context, com.tencent.qqlitepro.util.Config.videoSaveDir(context), -6643546, 12.0f, 8388611), rootLp());
        viewGroup.addView(vidCard);
        makeGroupGap(viewGroup);
        viewGroup.addView(saveButton(context, "修改视频保存路径", new Runnable() {
            @Override
            public void run() {
                pickMediaSaveDir(context, "video");
            }
        }));
        makeGroupGap(viewGroup);

        /* 合并列表：已下载文件 + 已保存媒体 */
        android.widget.LinearLayout listCard = new android.widget.LinearLayout(context);
        listCard.setOrientation(1);
        android.graphics.drawable.GradientDrawable listBg = new android.graphics.drawable.GradientDrawable();
        listBg.setColor(SURFACE_CONTAINER);
        listBg.setCornerRadius(dpLite(14.0f));
        listCard.setBackground(listBg);
        listCard.setPadding(dp(context, 12.0f), dp(context, 14.0f), dp(context, 12.0f), dp(context, 14.0f));
        listCard.setLayoutParams(rootLp());
        listCard.addView(text(context, "已下载文件与已保存媒体（长按操作）", TITLE, 14.0f, 8388611), rootLp());

        java.util.ArrayList<java.io.File> all = new java.util.ArrayList<java.io.File>();
        java.util.HashSet<String> seen = new java.util.HashSet<String>();
        java.io.File[] dl = listDownloadFiles(context);
        if (dl != null) {
            for (java.io.File f : dl) {
                if (f != null && f.isFile() && seen.add(f.getAbsolutePath())) all.add(f);
            }
        }
        java.io.File[] imgs = listDirFiles(com.tencent.qqlitepro.util.Config.imageSaveDir(context));
        if (imgs != null) {
            for (java.io.File f : imgs) {
                if (f != null && f.isFile() && seen.add(f.getAbsolutePath())) all.add(f);
            }
        }
        java.io.File[] vids = listDirFiles(com.tencent.qqlitepro.util.Config.videoSaveDir(context));
        if (vids != null) {
            for (java.io.File f : vids) {
                if (f != null && f.isFile() && seen.add(f.getAbsolutePath())) all.add(f);
            }
        }
        java.io.File[] merged = all.toArray(new java.io.File[0]);

        android.widget.ScrollView scrollView = new android.widget.ScrollView(context);
        scrollView.setFillViewport(false);
        scrollView.setVerticalScrollBarEnabled(true);
        android.widget.LinearLayout.LayoutParams lp = new android.widget.LinearLayout.LayoutParams(-1, -2);
        lp.topMargin = dp(context, 6.0f);
        if (merged.length > 0) {
            lp.height = java.lang.Math.max(dp(context, 96.0f), java.lang.Math.min(dp(context, 320.0f), (merged.length * dp(context, 56.0f)) + dp(context, 12.0f)));
        }
        scrollView.setLayoutParams(lp);
        android.widget.LinearLayout listInner = new android.widget.LinearLayout(context);
        listInner.setOrientation(1);
        scrollView.addView(listInner);
        listCard.addView(scrollView);
        viewGroup.addView(listCard);
        populateFileList(context, listInner, merged);
    }

    /** 列出目录下文件（不存在/为空返回 null） */
    private static java.io.File[] listDirFiles(String dir) {
        try {
            java.io.File f = new java.io.File(dir);
            if (!f.exists() || !f.isDirectory()) {
                return null;
            }
            return f.listFiles();
        } catch (Throwable t) {
            return null;
        }
    }

    /**
     * 图片/视频保存路径选择：
     * 先让用户选择"用哪个应用来处理"（系统 chooser，与下载路径选择一致的交互），
     * 由所选应用返回目录（ACTION_OPEN_DOCUMENT_TREE）后转真实路径写入。
     */
    private static void pickMediaSaveDir(android.content.Context context, String kind) {
        try {
            if (!(context instanceof android.app.Activity)) {
                toast(context, "需在设置界面内使用");
                return;
            }
            android.app.Activity act = (android.app.Activity) context;
            sPickKind = kind;
            android.content.Intent intent = new android.content.Intent(android.content.Intent.ACTION_OPEN_DOCUMENT_TREE);
            intent.addFlags(android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
                    | android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    | android.content.Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            act.startActivityForResult(android.content.Intent.createChooser(intent,
                    "选择处理的应用后选择" + ("image".equals(kind) ? "图片" : "视频") + "保存目录"), REQ_MEDIA_DIR_PICK);
        } catch (Throwable th) {
            com.tencent.qqlitepro.util.Log.w("打开目录选择器失败: " + th);
            toast(context, "打开目录选择器失败：" + th.getMessage());
        }
    }

    private static volatile String sPickKind = "image";

    /** 目录选择结果：tree URI -> 真实路径 -> 按图片/视频分别写入 */
    public static void handleMediaDirPicked(android.app.Activity activity, android.net.Uri uri, String kind) {
        try {
            if (uri == null) {
                return;
            }
            /* 持久化授权，之后内核/媒体写入可直接读写 */
            try {
                activity.getContentResolver().takePersistableUriPermission(uri,
                        android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
                                | android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            } catch (Throwable ignored) {
            }
            String path = com.tencent.qqlitepro.util.Config.mapTreeUriToPathPublic(uri.toString());
            if (path == null) {
                path = uri.toString();
            }
            if ("image".equals(kind)) {
                com.tencent.qqlitepro.util.Config.setImageSaveDir(activity, path);
            } else {
                com.tencent.qqlitepro.util.Config.setVideoSaveDir(activity, path);
            }
            toast(activity, ("image".equals(kind) ? "图片" : "视频") + "保存路径已设为：\n" + path);
            com.tencent.qqlitepro.util.Log.i("媒体保存路径已设置 kind=" + kind + " path=" + path);
            try {
                if (sPageGroup != null) {
                    openPage(sPageGroup, 10);
                }
            } catch (Throwable ignored) {}
        } catch (Throwable th) {
            com.tencent.qqlitepro.util.Log.w("处理目录选择失败: " + th);
        }
    }

    private static java.io.File[] listDownloadFiles(android.content.Context context) {
        java.io.File[] fileArrListFiles;
        try {
            java.io.File file = new java.io.File(com.tencent.qqlitepro.util.Config.downloadDir(context));
            if (!file.exists() || !file.isDirectory() || (fileArrListFiles = file.listFiles()) == null) {
                return null;
            }
            java.util.Arrays.sort(fileArrListFiles, new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda219());
            return fileArrListFiles;
        } catch (java.lang.Throwable thRethrow14) {
            com.tencent.qqlitepro.util.Log.w("列举下载目录失败: " + thRethrow14);
            return null;
        }
    }

    private static void populateFileList(android.content.Context context, android.widget.LinearLayout linearLayout, java.io.File[] fileArr) {
        linearLayout.removeAllViews();
        if (fileArr == null || fileArr.length == 0) {
            linearLayout.addView(text(context, "暂无下载文件（目录不存在或为空）", HINT, 12.0f, 8388611), rootLp());
            return;
        }
        boolean z = true;
        for (java.io.File file : fileArr) {
            if (file.isFile()) {
                android.widget.LinearLayout linearLayout2 = new android.widget.LinearLayout(context);
                linearLayout2.setOrientation(1);
                linearLayout2.setPadding(dp(context, 2.0f), dp(context, 8.0f), dp(context, 2.0f), dp(context, 8.0f));
                linearLayout2.setLayoutParams(rootLp());
                linearLayout2.setClickable(true);
                linearLayout2.setLongClickable(true);
                linearLayout2.setTag(file);
                linearLayout2.setOnLongClickListener(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda31(context));
                android.widget.TextView textViewText = text(context, file.getName() + "  （长按操作）", TITLE, 14.0f, 8388611);
                android.widget.TextView textViewText2 = text(context, humanSize(file.length()) + "  ·  " + com.tencent.qqlitepro.http.FakeVoice.formatTime(file.lastModified()), -6643546, 11.0f, 8388611);
                textViewText.setSingleLine(true);
                linearLayout2.addView(textViewText, rootLp());
                linearLayout2.addView(textViewText2, rootLp());
                linearLayout.addView(linearLayout2, rootLp());
                if (!z) {
                    android.view.View view = new android.view.View(context);
                    view.setBackgroundColor(DIVIDER);
                    linearLayout.addView(view, new android.widget.LinearLayout.LayoutParams(-1, dp(context, 1.0f)));
                }
                z = false;
            }
        }
    }

    static /* synthetic */ boolean lambda$populateFileList$212(android.content.Context context, android.view.View view) {
        showDownloadFileMenu(context, (java.io.File) view.getTag());
        return true;
    }

    /** 自绘文件操作卡片（不用安卓原生 AlertDialog 样式） */
    private static void showDownloadFileMenu(android.content.Context context, java.io.File file) {
        try {
            android.app.Dialog dialog = new android.app.Dialog(context);
            dialog.requestWindowFeature(1);
            android.widget.LinearLayout root = new android.widget.LinearLayout(context);
            root.setOrientation(1);
            android.graphics.drawable.GradientDrawable bg = new android.graphics.drawable.GradientDrawable();
            bg.setColor(-14605507);
            bg.setCornerRadius(dpLite(16.0f));
            root.setBackground(bg);
            int pad = dp(context, 16.0f);
            root.setPadding(pad, dp(context, 18.0f), pad, dp(context, 12.0f));

            root.addView(text(context, file.getName(), TITLE, 15.5f, 3), rootLp());
            root.addView(text(context, humanSize(file.length()) + "  ·  "
                    + com.tencent.qqlitepro.http.FakeVoice.formatTime(file.lastModified())
                    + "  ·  " + file.getParent(), HINT, 11.0f, 3), withTop(context, 4.0f));

            final android.app.Dialog fDialog = dialog;
            final android.content.Context fCtx = context;
            final java.io.File fFile = file;
            String[][] items = {
                    {"打开（选择应用）", "open"},
                    {"删除文件", "delete"},
                    {"取消", "cancel"}
            };
            for (String[] it : items) {
                android.widget.LinearLayout row = new android.widget.LinearLayout(context);
                row.setOrientation(1);
                row.setClickable(true);
                android.graphics.drawable.GradientDrawable rowBg = new android.graphics.drawable.GradientDrawable();
                rowBg.setColor(-15461382);
                rowBg.setCornerRadius(dpLite(12.0f));
                row.setBackground(rowBg);
                int rp = dp(context, 10.0f);
                row.setPadding(rp, dp(context, 12.0f), rp, dp(context, 12.0f));
                row.addView(text(context, it[0], it[2].equals("cancel") ? HINT : TITLE, 14.5f, 3), rootLp());
                row.setOnClickListener(new android.view.View.OnClickListener() {
                    @Override
                    public void onClick(android.view.View v) {
                        try {
                            dialog.dismiss();
                        } catch (Throwable ignored) {}
                        if ("open".equals(it[2])) {
                            openDownloadFile(fCtx, fFile);
                        } else if ("delete".equals(it[2])) {
                            deleteDownloadFile(fCtx, fFile);
                        }
                    }
                });
                android.widget.LinearLayout.LayoutParams rowLp = rootLp();
                rowLp.topMargin = dp(context, 8.0f);
                root.addView(row, rowLp);
            }

            scrollHostDialog(dialog, root);
        } catch (java.lang.Throwable thRethrow14) {
            com.tencent.qqlitepro.util.Log.w("弹出文件操作菜单失败: " + thRethrow14);
        }
    }

    /** 包一层透明 ScrollView 展示内容卡片（居中，宽度 92%） */
    private static void scrollHostDialog(android.app.Dialog dialog, android.view.View content) {
        try {
            android.content.Context ctx = (android.content.Context) content.getTag();
        } catch (Throwable ignored) {}
        try {
            android.content.Context c = null;
            try {
                c = ((android.view.View) content).getContext();
            } catch (Throwable ignored) {}
            android.widget.ScrollView sv = new android.widget.ScrollView(c);
            android.widget.FrameLayout wrap = new android.widget.FrameLayout(c);
            int pad = dp(c, 20.0f);
            wrap.setPadding(pad, dp(c, 60.0f), pad, dp(c, 40.0f));
            android.widget.FrameLayout.LayoutParams clp = new android.widget.FrameLayout.LayoutParams(
                    -1, -2);
            clp.setMargins(dp(c, 10.0f), 0, dp(c, 10.0f), 0);
            wrap.addView(content, clp);
            sv.addView(wrap);
            dialog.setContentView(sv);
            if (dialog.getWindow() != null) {
                android.view.WindowManager.LayoutParams attrs = dialog.getWindow().getAttributes();
                attrs.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
                attrs.height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
                dialog.getWindow().setAttributes(attrs);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            }
            dialog.show();
        } catch (Throwable th) {
            com.tencent.qqlitepro.util.Log.w("展示操作卡片失败: " + th);
        }
    }

    static /* synthetic */ void lambda$showDownloadFileMenu$213(android.content.Context context, java.io.File file, android.content.DialogInterface dialogInterface, int i) {
        if (i == 0) {
            openDownloadFile(context, file);
        } else if (i == 1) {
            deleteDownloadFile(context, file);
        }
    }

    private static void deleteDownloadFile(android.content.Context context, java.io.File file) {
        new android.app.AlertDialog.Builder(context).setTitle("删除文件").setMessage("确认删除 " + file.getName() + " ？").setNegativeButton("取消", (android.content.DialogInterface.OnClickListener) null).setPositiveButton("删除", new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda7(file, context)).show();
    }

    static /* synthetic */ void lambda$deleteDownloadFile$214(java.io.File file, android.content.Context context, android.content.DialogInterface dialogInterface, int i) {
        java.lang.String str = "删除失败";
        try {
            str = file.delete() ? "已删除" : "删除失败";
        } catch (java.lang.Throwable thRethrow14) {
            com.tencent.qqlitepro.util.Log.w("删除下载文件失败: " + thRethrow14);
        }
        toast(context, str);
        android.view.ViewGroup viewGroup = sPageGroup;
        if (viewGroup != null) {
            refreshPage(viewGroup, 10);
        }
    }

    private static void allowFileUriLaunch() {
        if (sFileUriAllowed) {
            return;
        }
        sFileUriAllowed = true;
        try {
            java.lang.Class<?> cls = java.lang.Class.forName("android.os.StrictMode");
            java.lang.Class<?> cls2 = java.lang.Class.forName("android.os.StrictMode$VmPolicy");
            java.lang.Object obj = cls2.getField("LAX").get(null);
            java.lang.Class<?> cls3 = java.lang.Class.forName("android.os.StrictMode$VmPolicy$Builder");
            cls.getMethod("setVmPolicy", cls2).invoke(null, cls3.getMethod("build", new java.lang.Class[0]).invoke(cls3.getConstructor(cls2).newInstance(obj), new java.lang.Object[0]));
        } catch (java.lang.Throwable unused) {
        }
    }

    private static void openDownloadFile(android.content.Context context, java.io.File file) {
        try {
            allowFileUriLaunch();
            android.net.Uri uriFromFile = android.net.Uri.fromFile(file);
            java.lang.String str = "*/*";
            int iLastIndexOf = file.getName().lastIndexOf(46);
            if (iLastIndexOf >= 0 && iLastIndexOf < file.getName().length() - 1) {
                java.lang.String mimeTypeFromExtension = android.webkit.MimeTypeMap.getSingleton().getMimeTypeFromExtension(file.getName().substring(iLastIndexOf + 1).toLowerCase(java.util.Locale.US));
                if (mimeTypeFromExtension != null && !mimeTypeFromExtension.isEmpty()) {
                    str = mimeTypeFromExtension;
                }
            }
            android.content.Intent intent = new android.content.Intent("android.intent.action.VIEW");
            intent.setDataAndType(uriFromFile, str);
            intent.addFlags(1);
            context.startActivity(android.content.Intent.createChooser(intent, "选择用什么打开 " + file.getName()));
        } catch (java.lang.Throwable thRethrow14) {
            com.tencent.qqlitepro.util.Log.w("打开下载文件失败: " + thRethrow14);
            toast(context, "无法打开该文件");
        }
    }

    private static void installApk(android.content.Context context, java.io.File file) {
        try {
            allowFileUriLaunch();
            android.net.Uri uriFromFile = android.net.Uri.fromFile(file);
            android.content.Intent intent = new android.content.Intent("android.intent.action.VIEW");
            intent.setDataAndType(uriFromFile, "application/vnd.android.package-archive");
            intent.addFlags(268435456);
            intent.addFlags(1);
            context.startActivity(intent);
        } catch (java.lang.Throwable thRethrow14) {
            com.tencent.qqlitepro.util.Log.w("启动系统安装器失败: " + thRethrow14);
            toast(context, "无法启动安装器: " + thRethrow14.getMessage());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void launchDirPicker() {
        android.app.Activity activity = sFileHost;
        if (activity == null) {
            toast(activity, "未获取宿主 Activity，无法调起目录选择器");
            return;
        }
        try {
            android.content.Intent intent = new android.content.Intent("android.intent.action.OPEN_DOCUMENT_TREE");
            intent.addFlags(67);
            activity.startActivityForResult(android.content.Intent.createChooser(intent, "选择处理目录选择的应用"), REQ_DIR_PICK);
        } catch (java.lang.Throwable thRethrow14) {
            com.tencent.qqlitepro.util.Log.w("调起目录选择器失败: " + thRethrow14);
            toast(activity, "调起目录选择器失败");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void handleDirPicked(android.app.Activity activity, android.net.Uri uri) {
        if (activity == null) {
            activity = sFileHost;
        }
        new android.os.Handler(android.os.Looper.getMainLooper()).post(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda227(resolveTreePath(activity, uri)));
    }

    static /* synthetic */ void lambda$handleDirPicked$215(java.lang.String str) {
        android.view.ViewGroup viewGroup = sPageGroup;
        android.content.Context context = viewGroup != null ? viewGroup.getContext() : null;
        if (context == null) {
            return;
        }
        if (str == null) {
            toast(context, "无法解析所选目录，请重试");
            return;
        }
        com.tencent.qqlitepro.util.Config.setDownloadDir(context, str);
        toast(context, "已设置下载路径：" + str);
        refreshPage(viewGroup, 10);
    }

    private static java.lang.String resolveTreePath(android.content.Context context, android.net.Uri uri) {
        int iIndexOf;
        java.lang.String str;
        try {
            java.lang.String lastPathSegment = uri.getLastPathSegment();
            if ("com.android.externalstorage.documents".equals(uri.getAuthority()) && lastPathSegment != null) {
                if (lastPathSegment.startsWith("primary:")) {
                    java.lang.String strSubstring = lastPathSegment.substring(8);
                    if (strSubstring != null && !strSubstring.isEmpty()) {
                        return "/storage/emulated/0/" + strSubstring;
                    }
                    return "/storage/emulated/0";
                }
                int iIndexOf2 = lastPathSegment.indexOf(58);
                if (iIndexOf2 > 0) {
                    java.lang.String strSubstring2 = lastPathSegment.substring(0, iIndexOf2);
                    java.lang.String strSubstring3 = lastPathSegment.substring(iIndexOf2 + 1);
                    java.lang.StringBuilder sb = new java.lang.StringBuilder("/storage/");
                    sb.append(strSubstring2);
                    if (strSubstring3.isEmpty()) {
                        str = "";
                    } else {
                        str = "/" + strSubstring3;
                    }
                    sb.append(str);
                    java.lang.String string = sb.toString();
                    if (new java.io.File(string).isDirectory()) {
                        return string;
                    }
                }
            }
            java.lang.String strUriDisplayName = uriDisplayName(context, uri);
            if (strUriDisplayName != null && !strUriDisplayName.isEmpty() && !strUriDisplayName.contains("/") && !strUriDisplayName.contains("\\")) {
                return "/sdcard/" + strUriDisplayName;
            }
            if (lastPathSegment != null && (iIndexOf = lastPathSegment.indexOf(58)) >= 0) {
                java.lang.String strSubstring4 = lastPathSegment.substring(iIndexOf + 1);
                if (!strSubstring4.isEmpty()) {
                    java.lang.String str2 = "/storage/emulated/0/" + strSubstring4;
                    if (new java.io.File(str2).isDirectory()) {
                        return str2;
                    }
                }
            }
            return null;
        } catch (java.lang.Throwable thRethrow14) {
            com.tencent.qqlitepro.util.Log.w("解析目录失败: " + thRethrow14);
            return null;
        }
    }

    private static android.view.View saveButton(android.content.Context context, java.lang.String str, java.lang.Runnable runnable) {
        android.widget.TextView textViewText = text(context, str, TITLE, 15.0f, 17);
        android.graphics.drawable.GradientDrawable gradientDrawable = new android.graphics.drawable.GradientDrawable();
        gradientDrawable.setShape(0);
        gradientDrawable.setColor(BTN_FILL);
        gradientDrawable.setCornerRadius(dpLite(14.0f));
        textViewText.setBackground(gradientDrawable);
        textViewText.setTextColor(BTN_TEXT);
        textViewText.setPadding(dp(context, 16.0f), dp(context, 13.0f), dp(context, 16.0f), dp(context, 13.0f));
        android.widget.LinearLayout.LayoutParams layoutParamsRootLp = rootLp();
        layoutParamsRootLp.leftMargin = dp(context, 12.0f);
        layoutParamsRootLp.rightMargin = dp(context, 12.0f);
        textViewText.setLayoutParams(layoutParamsRootLp);
        textViewText.setClickable(true);
        textViewText.setOnClickListener(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda154(textViewText, gradientDrawable, runnable));
        return textViewText;
    }

    static /* synthetic */ void lambda$saveButton$217(android.widget.TextView textView, android.graphics.drawable.GradientDrawable gradientDrawable, java.lang.Runnable runnable, android.view.View view) {
        try {
            android.graphics.drawable.GradientDrawable gradientDrawable2 = new android.graphics.drawable.GradientDrawable();
            gradientDrawable2.setShape(0);
            gradientDrawable2.setColor(BTN_FILL_PRESS);
            gradientDrawable2.setCornerRadius(dpLite(14.0f));
            textView.setBackground(gradientDrawable2);
            textView.postDelayed(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda5(textView, gradientDrawable), 180L);
        } catch (java.lang.Throwable unused) {
        }
        if (runnable != null) {
            runnable.run();
        }
    }

    private static void addResetButton(android.content.Context context, android.view.ViewGroup viewGroup, java.lang.String str, int i, java.lang.Runnable runnable) {
        android.widget.TextView textViewText = text(context, str, -1, 15.0f, 17);
        android.graphics.drawable.GradientDrawable gradientDrawable = new android.graphics.drawable.GradientDrawable();
        gradientDrawable.setShape(0);
        gradientDrawable.setColor(-5036514);
        gradientDrawable.setCornerRadius(dpLite(14.0f));
        textViewText.setBackground(gradientDrawable);
        textViewText.setTextColor(-1);
        textViewText.setPadding(dp(context, 16.0f), dp(context, 13.0f), dp(context, 16.0f), dp(context, 13.0f));
        android.widget.LinearLayout.LayoutParams layoutParamsRootLp = rootLp();
        layoutParamsRootLp.leftMargin = dp(context, 12.0f);
        layoutParamsRootLp.rightMargin = dp(context, 12.0f);
        textViewText.setLayoutParams(layoutParamsRootLp);
        textViewText.setClickable(true);
        textViewText.setOnClickListener(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda13(textViewText, gradientDrawable, context, runnable, viewGroup, i));
        viewGroup.addView(textViewText);
    }

    static /* synthetic */ void lambda$addResetButton$220(android.widget.TextView textView, android.graphics.drawable.GradientDrawable gradientDrawable, android.content.Context context, java.lang.Runnable runnable, android.view.ViewGroup viewGroup, int i, android.view.View view) {
        try {
            android.graphics.drawable.GradientDrawable gradientDrawable2 = new android.graphics.drawable.GradientDrawable();
            gradientDrawable2.setShape(0);
            gradientDrawable2.setColor(-7594728);
            gradientDrawable2.setCornerRadius(dpLite(14.0f));
            textView.setBackground(gradientDrawable2);
            textView.postDelayed(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda212(textView, gradientDrawable), 180L);
        } catch (java.lang.Throwable unused) {
        }
        try {
            new android.app.AlertDialog.Builder(context).setTitle("恢复默认").setMessage("将清除本页页面的全部设置，确认继续？").setNegativeButton("取消", (android.content.DialogInterface.OnClickListener) null).setPositiveButton("确定", new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda213(runnable, context, viewGroup, i)).show();
        } catch (java.lang.Throwable unused2) {
            if (runnable != null) {
                runnable.run();
            }
            toast(context, "已恢复默认");
            refreshPage(viewGroup, i);
        }
    }

    static /* synthetic */ void lambda$addResetButton$219(java.lang.Runnable runnable, android.content.Context context, android.view.ViewGroup viewGroup, int i, android.content.DialogInterface dialogInterface, int i2) {
        if (runnable != null) {
            try {
                runnable.run();
            } catch (java.lang.Throwable unused) {
                return;
            }
        }
        toast(context, "已恢复默认");
        refreshPage(viewGroup, i);
    }

    private static android.view.View sessionActionButton(android.content.Context context, java.lang.String str, java.lang.Runnable runnable) {
        android.widget.TextView textViewText = text(context, str, BTN_TEXT, 13.0f, 17);
        android.graphics.drawable.GradientDrawable gradientDrawable = new android.graphics.drawable.GradientDrawable();
        gradientDrawable.setShape(0);
        gradientDrawable.setColor(BTN_FILL);
        gradientDrawable.setCornerRadius(dpLite(10.0f));
        textViewText.setBackground(gradientDrawable);
        textViewText.setPadding(dp(context, 12.0f), dp(context, 6.0f), dp(context, 12.0f), dp(context, 6.0f));
        android.widget.LinearLayout.LayoutParams layoutParamsRootLp = rootLp();
        layoutParamsRootLp.leftMargin = dp(context, 12.0f);
        layoutParamsRootLp.rightMargin = dp(context, 12.0f);
        textViewText.setLayoutParams(layoutParamsRootLp);
        textViewText.setMinHeight(0);
        textViewText.setClickable(true);
        textViewText.setOnClickListener(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda57(textViewText, gradientDrawable, runnable));
        return textViewText;
    }

    static /* synthetic */ void lambda$sessionActionButton$222(android.widget.TextView textView, android.graphics.drawable.GradientDrawable gradientDrawable, java.lang.Runnable runnable, android.view.View view) {
        try {
            android.graphics.drawable.GradientDrawable gradientDrawable2 = new android.graphics.drawable.GradientDrawable();
            gradientDrawable2.setShape(0);
            gradientDrawable2.setColor(BTN_FILL_PRESS);
            gradientDrawable2.setCornerRadius(dpLite(10.0f));
            textView.setBackground(gradientDrawable2);
            textView.postDelayed(new com.tencent.qqlitepro.hook.SettingsInjector$$ExternalSyntheticLambda177(textView, gradientDrawable), 180L);
        } catch (java.lang.Throwable unused) {
        }
        if (runnable != null) {
            runnable.run();
        }
    }

    private static void setPickerBtnLp(android.content.Context context, android.view.View view, int i, int i2) {
        try {
            android.view.ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            if (layoutParams instanceof android.widget.LinearLayout.LayoutParams) {
                android.widget.LinearLayout.LayoutParams layoutParams2 = (android.widget.LinearLayout.LayoutParams) layoutParams;
                layoutParams2.topMargin = dp(context, i);
                layoutParams2.bottomMargin = dp(context, i2);
            }
        } catch (java.lang.Throwable unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void toast(android.content.Context context, java.lang.String str) {
        try {
            android.widget.Toast.makeText(context, str, 0).show();
        } catch (java.lang.Throwable unused) {
        }
    }

    private static void addDivider(android.view.ViewGroup viewGroup) {
        android.view.View view = new android.view.View(viewGroup.getContext());
        view.setBackgroundColor(DIVIDER);
        android.widget.LinearLayout.LayoutParams layoutParams = new android.widget.LinearLayout.LayoutParams(-1, java.lang.Math.max(1, dp(viewGroup.getContext(), 1.0f)));
        layoutParams.leftMargin = dp(viewGroup.getContext(), 20.0f);
        layoutParams.rightMargin = dp(viewGroup.getContext(), 20.0f);
        viewGroup.addView(view, layoutParams);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static android.widget.TextView text(android.content.Context context, java.lang.String str, int i, float f, int i2) {
        android.widget.TextView textView = new android.widget.TextView(context);
        textView.setText(str);
        textView.setTextColor(i);
        textView.setTextSize(f);
        textView.setGravity(i2);
        textView.setIncludeFontPadding(false);
        return textView;
    }

    private static android.widget.TextView selectableText(android.content.Context context, java.lang.String str, int i, float f) {
        android.widget.TextView textViewText = text(context, str, i, f, 8388611);
        if (str == null) {
            str = "";
        }
        textViewText.setText(new android.text.SpannableString(str));
        textViewText.setTextIsSelectable(true);
        textViewText.setLongClickable(true);
        textViewText.setFocusable(true);
        textViewText.setFocusableInTouchMode(true);
        textViewText.setLineSpacing(dp(context, 4.0f), 1.0f);
        attachLongPressCopy(textViewText);
        return textViewText;
    }

    private static android.widget.LinearLayout.LayoutParams rootLp() {
        return new android.widget.LinearLayout.LayoutParams(-1, -2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int dp(android.content.Context context, float f) {
        return java.lang.Math.round(f * context.getResources().getDisplayMetrics().density);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int dpLite(float f) {
        return java.lang.Math.round(f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    static final class RenderModeRow {
        final int mode;
        final android.widget.LinearLayout row;

        RenderModeRow(android.content.Context context, java.lang.String str, int i, android.view.ViewGroup viewGroup, int[] iArr, java.util.ArrayList<android.widget.TextView> arrayList, android.widget.TextView textView, java.lang.Runnable runnable) {
            this.mode = i;
            android.widget.LinearLayout linearLayout = new android.widget.LinearLayout(context);
            this.row = linearLayout;
            linearLayout.setOrientation(0);
            linearLayout.setGravity(16);
            linearLayout.setPadding(com.tencent.qqlitepro.hook.SettingsInjector.dp(context, 6.0f), com.tencent.qqlitepro.hook.SettingsInjector.dp(context, 6.0f), com.tencent.qqlitepro.hook.SettingsInjector.dp(context, 6.0f), com.tencent.qqlitepro.hook.SettingsInjector.dp(context, 6.0f));
            linearLayout.setClickable(true);
            android.graphics.drawable.GradientDrawable gradientDrawable = new android.graphics.drawable.GradientDrawable();
            gradientDrawable.setShape(0);
            gradientDrawable.setColor(0);
            gradientDrawable.setCornerRadius(com.tencent.qqlitepro.hook.SettingsInjector.dpLite(12.0f));
            linearLayout.setBackground(gradientDrawable);
            android.widget.TextView textView2 = new android.widget.TextView(context);
            textView2.setTextSize(16.0f);
            textView2.setIncludeFontPadding(false);
            arrayList.add(textView2);
            linearLayout.addView(textView2, new android.widget.LinearLayout.LayoutParams(com.tencent.qqlitepro.hook.SettingsInjector.dp(context, 22.0f), com.tencent.qqlitepro.hook.SettingsInjector.dp(context, 22.0f)));
            android.widget.TextView textViewText = com.tencent.qqlitepro.hook.SettingsInjector.text(context, str, -13878709, 14.0f, 3);
            textViewText.setPadding(com.tencent.qqlitepro.hook.SettingsInjector.dp(context, 10.0f), 0, 0, 0);
            linearLayout.addView(textViewText, new android.widget.LinearLayout.LayoutParams(0, -2, 1.0f));
            linearLayout.setOnClickListener(new com.tencent.qqlitepro.hook.SettingsInjector$RenderModeRow$$ExternalSyntheticLambda0(this, iArr, i, context, runnable, textView, gradientDrawable));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$1(int[] iArr, int i, android.content.Context context, java.lang.Runnable runnable, android.widget.TextView textView, android.graphics.drawable.GradientDrawable gradientDrawable, android.view.View view) {
            if (iArr[0] != i) {
                iArr[0] = i;
                try {
                    com.tencent.qqlitepro.util.Config.setSendMode(context, i);
                } catch (java.lang.Throwable unused) {
                }
                if (runnable != null) {
                    runnable.run();
                }
                com.tencent.qqlitepro.hook.SettingsInjector.toast(context, i == 0 ? "已切换为伪图片发送" : "已切换为伪语音发送");
            }
            if (textView == null || textView.getContext() == null) {
                return;
            }
            try {
                android.graphics.drawable.GradientDrawable gradientDrawable2 = new android.graphics.drawable.GradientDrawable();
                gradientDrawable2.setShape(0);
                gradientDrawable2.setColor(339442943);
                gradientDrawable2.setCornerRadius(com.tencent.qqlitepro.hook.SettingsInjector.dpLite(12.0f));
                this.row.setBackground(gradientDrawable2);
                this.row.postDelayed(new com.tencent.qqlitepro.hook.SettingsInjector$RenderModeRow$$ExternalSyntheticLambda1(this, gradientDrawable), 180L);
            } catch (java.lang.Throwable unused2) {
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0(android.graphics.drawable.GradientDrawable gradientDrawable) {
            this.row.setBackground(gradientDrawable);
        }
    }

}
