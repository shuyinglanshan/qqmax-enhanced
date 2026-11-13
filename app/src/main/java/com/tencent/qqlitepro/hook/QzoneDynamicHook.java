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

    /**
     * 在动态卡片"点赞按钮"下方渲染全部点赞用户：昵称(QQ号)、昵称(QQ号)…（自动换行）
     * 数据结构（已逆向确认）：
     *   BusinessFeedData.cellLikeInfo : CellLikeInfo
     *   CellLikeInfo.likeMans : ArrayList<CellLikeInfo$LikeMan>
     *   LikeMan.user : User (nick / uin)
     */
    public static void renderZanNames(Object holder, Object data) {
        try {
            if (sZanProbe++ < 8) {
                com.tencent.qqlitepro.util.Log.i("动态点赞诊断: 进入 holder="
                        + (holder == null ? "null" : holder.getClass().getSimpleName())
                        + " data=" + (data == null ? "null" : data.getClass().getSimpleName()));
            }
            if (holder == null || data == null) {
                return;
            }
            android.view.View itemView = (android.view.View)
                    de.robv.android.xposed.XposedHelpers.getObjectField(holder, "itemView");
            if (itemView == null) {
                return;
            }
            /* 取 CellLikeInfo：字段优先，方法兜底 */
            Object cellLike = null;
            try {
                cellLike = de.robv.android.xposed.XposedHelpers.getObjectField(data, "cellLikeInfo");
            } catch (Throwable ignored) {}
            if (cellLike == null) {
                try {
                    cellLike = de.robv.android.xposed.XposedHelpers.callMethod(data, "getCellLikeInfo", new Object[0]);
                } catch (Throwable ignored) {}
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
                            for (String nf : new String[]{"nickName", "nameSeperate", "nick"}) {
                                try {
                                    Object n = de.robv.android.xposed.XposedHelpers.getObjectField(user, nf);
                                    if (n != null && !String.valueOf(n).trim().isEmpty()) {
                                        nick = String.valueOf(n).trim();
                                        break;
                                    }
                                } catch (Throwable ignored) {}
                            }
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
            if (sZanProbe < 30) {
                com.tencent.qqlitepro.util.Log.i("动态点赞诊断: cellLike="
                        + (cellLike == null ? "null" : "ok")
                        + " zanText=[" + zanText + "]");
            }
            /* 找插入位置：卡片里最靠下的"点赞行"容器 */
            android.view.ViewGroup parent = findZanParent(itemView);
            if (parent == null) {
                return;
            }
            android.widget.TextView exist = null;
            for (int i = 0; i < parent.getChildCount(); i++) {
                android.view.View cv = parent.getChildAt(i);
                if ("qqlite_zan_names".equals(cv.getTag())) {
                    exist = (android.widget.TextView) cv;
                    break;
                }
            }
            if (zanText.isEmpty()) {
                if (exist != null) {
                    exist.setVisibility(android.view.View.GONE);
                }
                return;
            }
            android.content.Context ctx = itemView.getContext();
            android.widget.TextView tv = exist;
            if (tv == null) {
                tv = new android.widget.TextView(ctx);
                tv.setTag("qqlite_zan_names");
                tv.setTextSize(11.5f);
                tv.setTextColor(-6643546);
                tv.setSingleLine(false);
                tv.setMaxLines(30);
                tv.setEllipsize(null);
                tv.setLineSpacing(0f, 1.15f);
                int pad = (int) (ctx.getResources().getDisplayMetrics().density * 8);
                tv.setPadding(pad, 2, pad, 6);
                parent.addView(tv, new android.widget.LinearLayout.LayoutParams(-1, -2));
            } else {
                tv.setVisibility(android.view.View.VISIBLE);
            }
            tv.setText("赞：" + zanText);
        } catch (Throwable t) {
            if (sZanErr++ < 5) {
                com.tencent.qqlitepro.util.Log.w("动态点赞异常: " + t);
            }
        }
    }

    /* ================= 自建：点赞用户全屏列表 =================
     * 需求：全屏、点赞用户标蓝、实时刷新、点击跳到该用户 QQ 动态（走宿主跳转逻辑）。
     * 宿主跳转（逆向自 QZoneFeedViewHolderKtKt$handleOperationMore$1$1.invokeSuspend）：
     *   Bundle b = new Bundle();
     *   b.putString("uin", 当前登录uin);
     *   b.putString("report_uin", 目标用户uin);
     *   b.putString("report_ext_data", json);
     *   NavController nc = NavHostFragment.S(fragment);
     *   nc.f(2114521173, b, null);
     */
    private static android.app.Dialog sZanDialog = null;
    private static Object sZanDialogData = null;

    /** 打开点赞用户全屏列表 */
    public static void showZanUsersDialog(android.content.Context ctx, Object feedData, Object host) {
        try {
            if (ctx == null || feedData == null) {
                return;
            }
            dismissZanDialog();
            sZanDialogData = feedData;

            final android.app.Dialog dialog = new android.app.Dialog(ctx);
            dialog.requestWindowFeature(1);
            sZanDialog = dialog;

            android.widget.LinearLayout root = new android.widget.LinearLayout(ctx);
            root.setOrientation(1);
            root.setBackgroundColor(-15657960);
            int pad = (int) (ctx.getResources().getDisplayMetrics().density * 14);
            root.setPadding(pad, pad, pad, pad);

            /* 标题栏 */
            android.widget.LinearLayout header = new android.widget.LinearLayout(ctx);
            header.setOrientation(0);
            header.setGravity(16);
            android.widget.TextView title = new android.widget.TextView(ctx);
            title.setText("点赞用户");
            title.setTextSize(17f);
            title.setTextColor(-1);
            header.addView(title, new android.widget.LinearLayout.LayoutParams(0, -2, 1f));
            android.widget.TextView close = new android.widget.TextView(ctx);
            close.setText("关闭");
            close.setTextSize(13f);
            close.setTextColor(-16737793);
            close.setPadding(pad, 4, 0, 4);
            close.setOnClickListener(new android.view.View.OnClickListener() {
                @Override
                public void onClick(android.view.View v) {
                    dismissZanDialog();
                }
            });
            header.addView(close);
            root.addView(header);

            /* 列表容器（滚动） */
            final android.widget.LinearLayout listBox = new android.widget.LinearLayout(ctx);
            listBox.setOrientation(1);
            android.widget.ScrollView sv = new android.widget.ScrollView(ctx);
            sv.addView(listBox);
            root.addView(sv, new android.widget.LinearLayout.LayoutParams(-1, 0, 1f));

            dialog.setContentView(root);
            if (dialog.getWindow() != null) {
                android.view.WindowManager.LayoutParams attrs = dialog.getWindow().getAttributes();
                attrs.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
                attrs.height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
                dialog.getWindow().setAttributes(attrs);
                dialog.getWindow().setBackgroundDrawable(
                        new android.graphics.drawable.ColorDrawable(-15592942));
            }
            dialog.show();

            fillZanUserList(ctx, listBox, dialog);
            /* 实时刷新：每 2 秒重读一次 */
            startZanAutoRefresh(ctx, listBox, dialog);
        } catch (Throwable t) {
            com.tencent.qqlitepro.util.Log.w("点赞用户页打开失败: " + t);
        }
    }

    private static android.os.Handler sZanHandler = null;
    private static Runnable sZanTask = null;

    private static void startZanAutoRefresh(final android.content.Context ctx,
                                            final android.widget.LinearLayout box,
                                            final android.app.Dialog dialog) {
        stopZanAutoRefresh();
        sZanHandler = new android.os.Handler(android.os.Looper.getMainLooper());
        sZanTask = new Runnable() {
            @Override
            public void run() {
                try {
                    if (dialog == null || !dialog.isShowing()) {
                        stopZanAutoRefresh();
                        return;
                    }
                    fillZanUserList(ctx, box, dialog);
                } catch (Throwable ignored) {}
                if (sZanHandler != null) {
                    sZanHandler.postDelayed(this, 2000L);
                }
            }
        };
        sZanHandler.postDelayed(sZanTask, 2000L);
    }

    private static void stopZanAutoRefresh() {
        try {
            if (sZanHandler != null && sZanTask != null) {
                sZanHandler.removeCallbacks(sZanTask);
            }
        } catch (Throwable ignored) {}
        sZanHandler = null;
        sZanTask = null;
    }

    private static void dismissZanDialog() {
        stopZanAutoRefresh();
        try {
            if (sZanDialog != null) {
                sZanDialog.dismiss();
            }
        } catch (Throwable ignored) {}
        sZanDialog = null;
        sZanDialogData = null;
    }

    /** 重新填充点赞用户列表（每项：头像 + 昵称 + QQ号，全部标蓝可点击） */
    private static void fillZanUserList(final android.content.Context ctx,
                                        final android.widget.LinearLayout box,
                                        final android.app.Dialog dialog) {
        try {
            Object data = sZanDialogData;
            if (data == null || box == null) {
                return;
            }
            Object cellLike = null;
            try {
                cellLike = de.robv.android.xposed.XposedHelpers.getObjectField(data, "cellLikeInfo");
            } catch (Throwable ignored) {}
            if (cellLike == null) {
                try {
                    cellLike = de.robv.android.xposed.XposedHelpers
                            .callMethod(data, "getCellLikeInfo", new Object[0]);
                } catch (Throwable ignored) {}
            }
            java.util.List<Object[]> users = new java.util.ArrayList<>();
            if (cellLike != null) {
                Object mans = null;
                try {
                    mans = de.robv.android.xposed.XposedHelpers.getObjectField(cellLike, "likeMans");
                } catch (Throwable ignored) {}
                if (mans instanceof java.util.List) {
                    for (Object man : (java.util.List) mans) {
                        if (man == null) continue;
                        try {
                            Object user = de.robv.android.xposed.XposedHelpers
                                    .getObjectField(man, "user");
                            if (user == null) continue;
                            String nick = "";
                            for (String nf : new String[]{"nickName", "nameSeperate", "nick"}) {
                                try {
                                    Object n = de.robv.android.xposed.XposedHelpers
                                            .getObjectField(user, nf);
                                    if (n != null && !String.valueOf(n).trim().isEmpty()) {
                                        nick = String.valueOf(n).trim();
                                        break;
                                    }
                                } catch (Throwable ignored) {}
                            }
                            long uin = 0L;
                            try {
                                Object u = de.robv.android.xposed.XposedHelpers
                                        .getObjectField(user, "uin");
                                uin = u == null ? 0L : Long.parseLong(String.valueOf(u));
                            } catch (Throwable ignored) {}
                            if (nick.isEmpty() && uin == 0L) continue;
                            users.add(new Object[]{nick, Long.valueOf(uin)});
                        } catch (Throwable ignored) {}
                    }
                }
            }

            box.removeAllViews();
            if (users.isEmpty()) {
                android.widget.TextView empty = new android.widget.TextView(ctx);
                empty.setText("暂无点赞用户");
                empty.setTextSize(13f);
                empty.setTextColor(-8355712);
                empty.setPadding(0, 20, 0, 20);
                box.addView(empty);
                return;
            }
            android.widget.TextView countTv = new android.widget.TextView(ctx);
            countTv.setText("共 " + users.size() + " 人");
            countTv.setTextSize(12.5f);
            countTv.setTextColor(-8355712);
            countTv.setPadding(0, 4, 0, 10);
            box.addView(countTv);

            for (Object[] one : users) {
                final String nick = (String) one[0];
                final long uin = ((Long) one[1]).longValue();
                android.widget.LinearLayout row = new android.widget.LinearLayout(ctx);
                row.setOrientation(0);
                row.setGravity(16);
                row.setPadding(0, 10, 0, 10);
                row.setClickable(true);

                android.widget.ImageView av = new android.widget.ImageView(ctx);
                android.widget.LinearLayout.LayoutParams avLp =
                        new android.widget.LinearLayout.LayoutParams(
                                (int) (ctx.getResources().getDisplayMetrics().density * 34),
                                (int) (ctx.getResources().getDisplayMetrics().density * 34));
                av.setLayoutParams(avLp);
                android.graphics.drawable.GradientDrawable bg =
                        new android.graphics.drawable.GradientDrawable();
                bg.setShape(android.graphics.drawable.GradientDrawable.OVAL);
                bg.setColor(-10785013);
                av.setBackground(bg);
                row.addView(av);

                android.widget.TextView tv = new android.widget.TextView(ctx);
                /* 需求：点赞用户标蓝 */
                tv.setText((nick.isEmpty() ? String.valueOf(uin) : nick)
                        + (uin > 0 ? "(" + uin + ")" : ""));
                tv.setTextSize(14f);
                tv.setTextColor(-16737793);
                tv.setSingleLine(true);
                tv.setEllipsize(android.text.TextUtils.TruncateAt.END);
                android.widget.LinearLayout.LayoutParams tvLp =
                        new android.widget.LinearLayout.LayoutParams(0, -2, 1f);
                tvLp.leftMargin = 12;
                row.addView(tv, tvLp);

                row.setClickable(false);
                box.addView(row);

                /* 头像异步加载 */
                if (uin > 0) {
                    final android.widget.ImageView fAv = av;
                    final long fUin = uin;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                java.net.URL u = new java.net.URL(
                                        "https://q1.qlogo.cn/g?b=qq&nk=" + fUin + "&s=100");
                                java.net.HttpURLConnection conn =
                                        (java.net.HttpURLConnection) u.openConnection();
                                conn.setConnectTimeout(8000);
                                conn.setReadTimeout(8000);
                                final android.graphics.Bitmap bm =
                                        android.graphics.BitmapFactory.decodeStream(conn.getInputStream());
                                if (bm != null) {
                                    new android.os.Handler(android.os.Looper.getMainLooper())
                                            .post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    try { fAv.setImageBitmap(bm); }
                                                    catch (Throwable ignored) {}
                                                }
                                            });
                                }
                            } catch (Throwable ignored) {}
                        }
                    }, "zan-avatar").start();
                }
            }
        } catch (Throwable ignored) {}
    }

    /** 跳到指定用户的 QQ 动态（复用宿主 NavController 跳转） */
    public static void jumpToUserZone(final android.content.Context ctx, final long targetUin) {
        try {
            Object frag = null;
            if (frag == null) {
                /* 从 Activity 拿当前 Fragment */
                android.app.Activity act = null;
                android.content.Context c = ctx;
                while (c instanceof android.content.ContextWrapper) {
                    if (c instanceof android.app.Activity) {
                        act = (android.app.Activity) c;
                        break;
                    }
                    c = ((android.content.ContextWrapper) c).getBaseContext();
                }
                if (act != null) {
                    try {
                        Object fm = act.getClass().getMethod("getSupportFragmentManager").invoke(act);
                        java.util.List<?> frags = (java.util.List<?>)
                                fm.getClass().getMethod("getFragments").invoke(fm);
                        if (frags != null && !frags.isEmpty()) {
                            frag = frags.get(frags.size() - 1);
                        }
                    } catch (Throwable ignored) {}
                }
            }
            if (frag == null) {
                com.tencent.qqlitepro.util.Log.w("跳转用户动态: 未找到 Fragment");
                return;
            }
            /* NavHostFragment.S(fragment) */
            Class<?> nhf = Class.forName("androidx.navigation.fragment.NavHostFragment");
            Object nav = null;
            for (java.lang.reflect.Method m : nhf.getDeclaredMethods()) {
                if (m.getParameterTypes().length == 1
                        && android.app.Fragment.class.isAssignableFrom(m.getParameterTypes()[0])) {
                    m.setAccessible(true);
                    nav = m.invoke(null, frag);
                    break;
                }
            }
            if (nav == null) {
                com.tencent.qqlitepro.util.Log.w("跳转用户动态: NavController 获取失败");
                return;
            }
            String myUin = "";
            try {
                Class<?> mqq = Class.forName("mqq.app.MobileQQ");
                Object app = mqq.getField("sMobileQQ").get(null);
                myUin = String.valueOf(app.getClass()
                        .getMethod("getCurrentUin").invoke(app));
            } catch (Throwable ignored) {}
            android.os.Bundle b = new android.os.Bundle();
            b.putString("uin", myUin);
            b.putString("report_uin", String.valueOf(targetUin));
            b.putString("report_ext_data", "{}");
            /* NavController.f(int, Bundle, NavOptions) */
            java.lang.reflect.Method f = null;
            for (java.lang.reflect.Method m : nav.getClass().getMethods()) {
                Class<?>[] ps = m.getParameterTypes();
                if (m.getName().equals("f") && ps.length == 3 && ps[0] == Integer.TYPE
                        && ps[1] == android.os.Bundle.class) {
                    f = m;
                    break;
                }
            }
            if (f == null) {
                com.tencent.qqlitepro.util.Log.w("跳转用户动态: NavController.f 未找到");
                return;
            }
            f.setAccessible(true);
            f.invoke(nav, Integer.valueOf(0x7E0C3D15), b, null);
            com.tencent.qqlitepro.util.Log.i("跳转用户动态: -> " + targetUin);
        } catch (Throwable t) {
            com.tencent.qqlitepro.util.Log.w("跳转用户动态失败: " + t);
        }
    }

    /* ================= 更多操作菜单：注入"查看点赞用户" =================
     * 逆向：
     *   WatchPicElementExtKt.f1(Fragment, ArrayList<SelectFragmentItem>, String requestKey, Continuation)
     *   是宿主的"更多操作"菜单弹出入口。
     *   SelectFragmentItem(int iconRes, int actionId, int arg, Bundle extra, String eventId)
     * 这里 hook 它，往 items 里塞一个我们自己的项；点击后开自建全屏页。
     */
    private static final int OUR_ACTION_ID = 0x7F0E9001;   /* 自定义 actionId，避免与宿主冲突 */
    private static volatile int sMenuInjected = 0;
    private static volatile boolean sOperationMoreHooked = false;
    private static volatile int sInjectProbe = 0;

    /** 延迟重试安装（WatchPicElementExtKt 在别的 dex，早期解析会失败） */
    public static void installOperationMoreHookDelayed(final java.lang.ClassLoader cl) {
        final android.os.Handler h = new android.os.Handler(android.os.Looper.getMainLooper());
        final int[] tries = {0};
        Runnable r = new Runnable() {
            @Override
            public void run() {
                tries[0]++;
                boolean ok = installOperationMoreHookOnce(cl);
                if (!ok && tries[0] < 12) {
                    h.postDelayed(this, 5000L);
                }
            }
        };
        h.postDelayed(r, 3000L);
    }

    private static boolean installOperationMoreHookOnce(java.lang.ClassLoader cl) {
        if (sOperationMoreHooked) {
            return true;
        }
        try {
            /*
             * 关键：不能 Class.forName("WatchPicElementExtKt")。
             * 那个 dex 里有 SingleLineTextView 等类，跨 dex 解析会抛
             * NoClassDefFoundError。改用 XposedHelpers.findClass + 只取方法签名，
             * 并跳过参数类型解析（用 findMethodExact 的名称+参数个数匹配）。
             */
            /*
             * 该类的静态验证会拉入 SingleLineTextView（在别的 dex），
             * 直接 findClass 会 NoClassDefFoundError。
             * 先尝试加载 SingleLineTextView 补齐依赖，再加载目标类。
             */
            try {
                de.robv.android.xposed.XposedHelpers.findClass(
                        "com.tencent.qqnt.classadapter.SingleLineTextView", cl);
            } catch (Throwable ignored) {}
            try {
                de.robv.android.xposed.XposedHelpers.findClass(
                        "com.tencent.qqnt.watch.ui.componet.SingleLineTextView", cl);
            } catch (Throwable ignored) {}
            Class<?> extKt = de.robv.android.xposed.XposedHelpers.findClass(
                    "WatchPicElementExtKt", cl);
            java.lang.reflect.Method target = null;
            for (java.lang.reflect.Method m : extKt.getDeclaredMethods()) {
                if (m.getName().equals("f1") && m.getParameterCount() == 4) {
                    target = m;
                    break;
                }
            }
            if (target == null) {
                com.tencent.qqlitepro.util.Log.w("动态点赞: WatchPicElementExtKt.f1 未找到");
                return false;
            }
            de.robv.android.xposed.XposedBridge.hookMethod(target,
                    new de.robv.android.xposed.XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) {
                            try {
                                Object r = param.getResult();
                                if (r == null) {
                                    return;
                                }
                                int actionId = -1;
                                try {
                                    Object c = de.robv.android.xposed.XposedHelpers
                                            .getObjectField(r, "c");
                                    actionId = c == null ? -1 : ((Integer) c).intValue();
                                } catch (Throwable ignored) {}
                                if (actionId == OUR_ACTION_ID) {
                                    Object feed = sPendingFeed;
                                    android.content.Context ctx = null;
                                    try {
                                        Object frag = param.args[0];
                                        java.lang.Object v = frag.getClass()
                                                .getMethod("getContext").invoke(frag);
                                        if (v instanceof android.content.Context) {
                                            ctx = (android.content.Context) v;
                                        }
                                    } catch (Throwable ignored) {}
                                    if (ctx == null) {
                                        try {
                                            android.app.Activity a =
                                                    com.tencent.qqlitepro.hook.ChatCardInjector
                                                            .anyActivityPublic();
                                            if (a != null) {
                                                ctx = a;
                                            }
                                        } catch (Throwable ignored) {}
                                    }
                                    showZanUsersDialog(ctx, feed, null);
                                    com.tencent.qqlitepro.util.Log.i(
                                            "动态点赞: 打开自建点赞用户页");
                                }
                            } catch (Throwable t) {
                                com.tencent.qqlitepro.util.Log.w("动态点赞: 菜单结果处理失败 " + t);
                            }
                        }

                        protected void beforeHookedMethod(MethodHookParam param) {
                            try {
                                Object listObj = param.args[1];
                                if (!(listObj instanceof java.util.ArrayList)) {
                                    return;
                                }
                                java.util.ArrayList list = (java.util.ArrayList) listObj;
                                Class<?> itemCls = null;
                                for (Object it : list) {
                                    if (it != null) {
                                        itemCls = it.getClass();
                                        break;
                                    }
                                }
                                if (itemCls == null) {
                                    return;
                                }
                                /* 用已有项的构造器创建我们自己的项 */
                                Object newItem = null;
                                for (java.lang.reflect.Constructor<?> c : itemCls.getDeclaredConstructors()) {
                                    Class<?>[] ps = c.getParameterTypes();
                                    if (ps.length == 5 && ps[0] == Integer.TYPE
                                            && ps[1] == Integer.TYPE && ps[2] == Integer.TYPE
                                            && ps[3] == android.os.Bundle.class && ps[4] == String.class) {
                                        c.setAccessible(true);
                                        newItem = c.newInstance(Integer.valueOf(0),
                                                Integer.valueOf(OUR_ACTION_ID), Integer.valueOf(0),
                                                new android.os.Bundle(), "em_watch_report");
                                        break;
                                    }
                                }
                                if (newItem == null) {
                                    return;
                                }
                                list.add(newItem);
                                /* 用 requestKey 关联当前 feed（f1 的调用方随后会拿着它判断） */
                                try {
                                    Object feed = sLastFeedForMenu;
                                    if (feed != null) {
                                        sFeedByRequestKey.put(String.valueOf(param.args[2]), feed);
                                        sPendingFeed = feed;
                                    }
                                } catch (Throwable ignored) {}
                                if (sMenuInjected++ < 3) {
                                    com.tencent.qqlitepro.util.Log.i("动态点赞: 已注入查看点赞用户菜单项");
                                }
                            } catch (Throwable t) {
                                com.tencent.qqlitepro.util.Log.w("动态点赞: 注入菜单项失败 " + t);
                            }
                        }
                    });
            sOperationMoreHooked = true;
            com.tencent.qqlitepro.util.Log.i("动态点赞: 更多操作菜单 hook 已安装");
            return true;
        } catch (Throwable t) {
            com.tencent.qqlitepro.util.Log.w("动态点赞: 更多操作菜单 hook 安装失败(将重试) " + t);
            return false;
        }
    }

    /**
     * 在每张动态卡片上追加一个"查看点赞用户"入口（小按钮，紧贴三点按钮）。
     * 走 QZoneFeedViewHolder 的公开字段：
     *   h = BusinessFeedData（该卡片的数据）
     *   g = ViewGroup（卡片内容容器）
     * 点击后打开自建全屏点赞用户页，点用户跳其动态。
     */
    public static void attachZanButton(Object holder) {
        try {
            if (holder == null) return;
            Object feed = null;
            Object content = null;
            try { feed = de.robv.android.xposed.XposedHelpers.getObjectField(holder, "h"); } catch (Throwable ignored) {}
            try { content = de.robv.android.xposed.XposedHelpers.getObjectField(holder, "g"); } catch (Throwable ignored) {}
            if (!(content instanceof android.view.ViewGroup)) return;
            final android.view.ViewGroup box = (android.view.ViewGroup) content;
            if (box.findViewWithTag("qqmax_zan_entry") != null) {
                /* 已有则只更新 feed 引用 */
                box.findViewWithTag("qqmax_zan_entry").setTag(-0x7f0e0003, feed);
                return;
            }
            if (feed == null) return;
            /* 只有确实有人点赞才显示入口 */
            Object cellLike = null;
            try { cellLike = de.robv.android.xposed.XposedHelpers.getObjectField(feed, "cellLikeInfo"); } catch (Throwable ignored) {}
            if (cellLike == null) return;
            Object mans = null;
            try { mans = de.robv.android.xposed.XposedHelpers.getObjectField(cellLike, "likeMans"); } catch (Throwable ignored) {}
            if (!(mans instanceof java.util.List) || ((java.util.List) mans).isEmpty()) return;

            android.content.Context ctx = box.getContext();
            if (ctx == null) return;
            android.widget.TextView btn = new android.widget.TextView(ctx);
            btn.setTag("qqmax_zan_entry");
            btn.setTag(-0x7f0e0003, feed);
            btn.setText("查看点赞 (" + ((java.util.List) mans).size() + ")");
            btn.setTextSize(10.5f);
            btn.setTextColor(-16737793);
            int p = (int) (ctx.getResources().getDisplayMetrics().density * 6);
            btn.setPadding(p, 2, p, 2);
            btn.setClickable(true);
            btn.setOnClickListener(new android.view.View.OnClickListener() {
                @Override
                public void onClick(android.view.View v) {
                    Object f = v.getTag(-0x7f0e0003);
                    showZanUsersDialog(v.getContext(), f, null);
                }
            });
            android.widget.LinearLayout.LayoutParams lp =
                    new android.widget.LinearLayout.LayoutParams(-2, -2);
            lp.leftMargin = (int) (ctx.getResources().getDisplayMetrics().density * 4);
            box.addView(btn, lp);
        } catch (Throwable t) {
            com.tencent.qqlitepro.util.Log.w("动态点赞: 添加入口失败 " + t);
        }
    }

    /**
     * 拦截"选择菜单项"的点击回调，识别我们注入的 actionId。
     * 宿主在 f1(...) 返回后，会用 SelectFragmentItem.c（actionId）做分支判断，
     * 这里 hook 一个通用的"拿 item 后判断 actionId"的位置不便，改为直接 hook
     * 菜单 Fragment 的 onItemClick / 或对 SelectFragmentItem 做标记后由
     * QZoneFeedViewHolderKtKt.handleOperationMore 的返回值处拦截。
     *
     * 实现：hook WatchPicElementExtKt.f1 的返回值为我们自己构造的 item（当用户点了我们项时无法拿到），
     * 因此改用"注册一个监听：hook 菜单关闭后判断是否选中了我们的 actionId"。
     */
    public static void installSelectItemClickHook(java.lang.ClassLoader cl) {
        /* 首先无条件安装菜单注入（不依赖 WatchPicElementExtKt） */
        try {
            installMenuResultHook(cl);
        } catch (Throwable t) {
            com.tencent.qqlitepro.util.Log.w("动态点赞: installMenuResultHook 异常 " + t);
        }
        try {
            Class<?> cls = de.robv.android.xposed.XposedHelpers.findClass(
                    "com.tencent.qqnt.watch.ui.componet.select.SelectFragmentItem", cl);
            /* 记录 item 实例 -> 来源 feed，点击时用 */
            for (java.lang.reflect.Field f : cls.getDeclaredFields()) {
                if (f.getType() == android.os.Bundle.class) {
                    f.setAccessible(true);
                }
            }
            com.tencent.qqlitepro.util.Log.i("动态点赞: SelectFragmentItem 已就绪");
        } catch (Throwable t) {
            com.tencent.qqlitepro.util.Log.w("动态点赞: SelectFragmentItem hook 失败 " + t);
        }
    }

    /**
     * 最可靠的注入方式：hook Dialog.show()，在窗口显示后扫描它的内容容器，
     * 若是一个竖排按钮列表（"更多操作"），就在末尾追加我们的按钮。
     * 不依赖任何宿主内部类名。
     */
    private static volatile boolean sDialogShowHooked = false;

    public static void installDialogShowHook() {
        if (sDialogShowHooked) {
            return;
        }
        sDialogShowHooked = true;
        try {
            de.robv.android.xposed.XposedBridge.hookAllMethods(
                    android.app.Dialog.class, "show",
                    new de.robv.android.xposed.XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) {
                            try {
                                final android.app.Dialog d = (android.app.Dialog) param.thisObject;
                                if (d == null || d.getWindow() == null) {
                                    return;
                                }
                                android.view.View decor = d.getWindow().getDecorView();
                                if (decor == null) {
                                    return;
                                }
                                decor.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            injectIntoDialog(d);
                                        } catch (Throwable ignored) {}
                                    }
                                }, 60L);
                            } catch (Throwable ignored) {}
                        }
                    });
            com.tencent.qqlitepro.util.Log.i("动态点赞: Dialog.show hook 已安装");
        } catch (Throwable t) {
            com.tencent.qqlitepro.util.Log.w("动态点赞: Dialog.show hook 失败 " + t);
        }
    }

    /**
     * 给每张卡片的"三点按钮"挂一个轻量点击监听（不拦截原逻辑，只记录当前卡片）。
     * 宿主三点按钮 id 通常含 more/menu，这里用容器遍历找。
     */
    public static void hookMoreButtonOfHolder(Object holder) {
        try {
            if (holder == null) return;
            Object feed = null;
            Object content = null;
            try { feed = de.robv.android.xposed.XposedHelpers.getObjectField(holder, "h"); } catch (Throwable ignored) {}
            try { content = de.robv.android.xposed.XposedHelpers.getObjectField(holder, "g"); } catch (Throwable ignored) {}
            if (feed == null || !(content instanceof android.view.ViewGroup)) return;
            final android.view.ViewGroup box = (android.view.ViewGroup) content;
            if (box.findViewWithTag("qqmax_more_probe") != null) {
                box.findViewWithTag("qqmax_more_probe").setTag(-0x7f0e0004, feed);
                return;
            }
            android.view.View more = findMoreButton(box);
            if (more == null) return;
            more.setTag("qqmax_more_probe");
            more.setTag(-0x7f0e0004, feed);
            /* 用 OnTouchListener 记录，不消费事件，保证宿主原逻辑不受影响 */
            more.setOnTouchListener(new android.view.View.OnTouchListener() {
                @Override
                public boolean onTouch(android.view.View v, android.view.MotionEvent e) {
                    try {
                        if (e.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                            Object f = v.getTag(-0x7f0e0004);
                            if (f != null) {
                                sLastClickedFeed = f;
                            }
                        }
                    } catch (Throwable ignored) {}
                    return false;   /* 不消费 */
                }
            });
        } catch (Throwable ignored) {}
    }

    /** 找卡片里的"三点/更多"按钮：ImageView 且无文字、位于右上角，或 id 名含 more */
    private static android.view.View findMoreButton(android.view.ViewGroup box) {
        try {
            java.util.ArrayList<android.view.View> all = new java.util.ArrayList<>();
            collectAll(box, all, 0);
            android.view.View best = null;
            for (android.view.View v : all) {
                if (!(v instanceof android.widget.ImageView)) continue;
                int id = v.getId();
                if (id != android.view.View.NO_ID) {
                    try {
                        String nm = v.getContext().getResources().getResourceEntryName(id);
                        if (nm != null && (nm.contains("more") || nm.contains("menu"))) {
                            return v;
                        }
                    } catch (Throwable ignored) {}
                }
            }
            /* 兜底：最靠右上的 ImageView */
            int bestScore = Integer.MIN_VALUE;
            for (android.view.View v : all) {
                if (!(v instanceof android.widget.ImageView)) continue;
                if (v.getWidth() <= 0) continue;
                int[] loc = new int[2];
                v.getLocationOnScreen(loc);
                int score = loc[0] - loc[1];
                if (score > bestScore) {
                    bestScore = score;
                    best = v;
                }
            }
            return best;
        } catch (Throwable ignored) {
            return null;
        }
    }

    /** 最近一次 onUiFeedList 的 feed 列表（按顺序） */
    private static volatile java.util.List<?> sLastFeedList = null;
    /** 最近一次点击"更多操作"的那张卡片对应的 feed */
    private static volatile Object sLastClickedFeed = null;

    /** 记录最近一次 onUiFeedList（由 QqDynamicScheduler 调用） */
    public static void rememberFeedList(java.util.List<?> list) {
        sLastFeedList = list;
    }

    /** 从最近的 feed 列表里挑一个有赞的作为目标 */
    private static Object findFeedForMenu(android.content.Context ctx) {
        try {
            java.util.List<?> list = sLastFeedList;
            if (list == null || list.isEmpty()) {
                return null;
            }
            for (Object one : list) {
                if (one == null) continue;
                Object cl = null;
                try {
                    cl = de.robv.android.xposed.XposedHelpers.getObjectField(one, "cellLikeInfo");
                } catch (Throwable ignored) {}
                if (cl == null) continue;
                Object mans = null;
                try {
                    mans = de.robv.android.xposed.XposedHelpers.getObjectField(cl, "likeMans");
                } catch (Throwable ignored) {}
                if (mans instanceof java.util.List && !((java.util.List) mans).isEmpty()) {
                    return one;
                }
            }
            return list.get(0);
        } catch (Throwable ignored) {
            return null;
        }
    }

    /** 记录当前弹层对应的 feed（每次弹层独立，避免串到别的动态） */
    private static final java.util.Map<Object, Object> sDialogFeed =
            new java.util.WeakHashMap<>();

    /** 在弹层里找"竖排按钮列表"，追加"查看点赞用户"按钮 */
    private static void injectIntoDialog(android.app.Dialog d) {
        try {
            android.view.View decor = d.getWindow().getDecorView();
            if (decor == null) {
                return;
            }
            java.util.ArrayList<android.view.View> all = new java.util.ArrayList<>();
            collectAll(decor, all, 0);
            /* 找标题为"更多操作"的 TextView，确认是目标弹层 */
            boolean isTarget = false;
            for (android.view.View v : all) {
                if (v instanceof android.widget.TextView) {
                    String t = String.valueOf(((android.widget.TextView) v).getText()).trim();
                    if (t.contains("更多操作")) {
                        isTarget = true;
                        break;
                    }
                }
            }
            if (!isTarget) {
                return;
            }
            if (decor.findViewWithTag("qqmax_zan_menu_item") != null) {
                return;
            }
            /* 确定本弹层作用于哪条动态：优先用最近一次被"更多操作"点击的卡片 */
            Object feed = sLastClickedFeed;
            if (feed == null) {
                feed = findFeedForMenu(decor.getContext());
            }
            sDialogFeed.put(d, feed);
            if (feed == null) {
                com.tencent.qqlitepro.util.Log.w("动态点赞: 弹层未关联到动态，跳过注入");
                return;
            }
            /* 找最深的竖排容器（按钮列表的父级） */
            android.view.ViewGroup best = null;
            int bestDepth = -1;
            for (android.view.View v : all) {
                if (!(v instanceof android.view.ViewGroup)) continue;
                android.view.ViewGroup g = (android.view.ViewGroup) v;
                if (g.getChildCount() < 2) continue;
                boolean vert = !(g instanceof android.widget.LinearLayout)
                        || ((android.widget.LinearLayout) g).getOrientation()
                        == android.widget.LinearLayout.VERTICAL;
                if (!vert) continue;
                int dep = depthOf(decor, v);
                if (dep > bestDepth) {
                    bestDepth = dep;
                    best = g;
                }
            }
            if (best == null) {
                return;
            }
            android.content.Context ctx = decor.getContext();
            android.widget.TextView btn = new android.widget.TextView(ctx);
            btn.setTag("qqmax_zan_menu_item");
            btn.setText("查看点赞用户");
            btn.setTextSize(15f);
            btn.setTextColor(-16737793);
            btn.setGravity(17);
            int p = (int) (ctx.getResources().getDisplayMetrics().density * 14);
            btn.setPadding(p, p, p, p);
            android.graphics.drawable.GradientDrawable bg =
                    new android.graphics.drawable.GradientDrawable();
            bg.setColor(-13948117);
            bg.setCornerRadius(p);
            btn.setBackground(bg);
            btn.setClickable(true);
            btn.setOnClickListener(new android.view.View.OnClickListener() {
                @Override
                public void onClick(android.view.View v) {
                    Object feed = sDialogFeed.get(d);
                    try { d.dismiss(); } catch (Throwable ignored) {}
                    if (feed == null) {
                        com.tencent.qqlitepro.util.Log.w("动态点赞: 该弹层未绑定动态数据");
                        return;
                    }
                    showZanUsersDialog(v.getContext(), feed, null);
                }
            });
            android.view.ViewGroup.LayoutParams lp = null;
            for (android.view.View c : new android.view.View[]{best.getChildAt(0)}) {
                if (c != null && c.getLayoutParams() != null) {
                    try {
                        lp = c.getLayoutParams().getClass()
                                .getConstructor(Integer.TYPE, Integer.TYPE)
                                .newInstance(Integer.valueOf(c.getLayoutParams().width),
                                        Integer.valueOf(c.getLayoutParams().height));
                    } catch (Throwable ignored) {}
                    break;
                }
            }
            if (lp == null) {
                lp = new android.widget.LinearLayout.LayoutParams(-1, -2);
            }
            /* 避免与上方按钮粘连：加外边距 */
            try {
                if (lp instanceof android.view.ViewGroup.MarginLayoutParams) {
                    android.view.ViewGroup.MarginLayoutParams mlp =
                            (android.view.ViewGroup.MarginLayoutParams) lp;
                    int gap = (int) (ctx.getResources().getDisplayMetrics().density * 10);
                    mlp.topMargin = gap;
                    mlp.leftMargin = gap / 2;
                    mlp.rightMargin = gap / 2;
                    mlp.bottomMargin = gap / 2;
                }
            } catch (Throwable ignored) {}
            best.addView(btn, lp);
            com.tencent.qqlitepro.util.Log.i("动态点赞: 已在更多操作弹层追加查看点赞用户");
        } catch (Throwable t) {
            com.tencent.qqlitepro.util.Log.w("动态点赞: 追加按钮失败 " + t);
        }
    }

    private static int depthOf(android.view.View root, android.view.View target) {
        int d = 0;
        android.view.ViewParent p = target.getParent();
        while (p != null && p != root) {
            d++;
            p = p.getParent();
        }
        return d;
    }

    /** 把"查看点赞用户"项注入宿主的 SelectDialogFragment 列表 */
    private static void injectOurItem(Object frag) {
        try {
            if (frag == null) return;
            if (sInjectProbe++ < 6) {
                com.tencent.qqlitepro.util.Log.i("动态点赞: injectOurItem 进入 frag="
                        + frag.getClass().getSimpleName());
            }
            Object items = null;
            for (String fld : new String[]{"b", "items"}) {
                try {
                    Object v = de.robv.android.xposed.XposedHelpers.getObjectField(frag, fld);
                    if (v instanceof java.util.List) {
                        items = v;
                        break;
                    }
                } catch (Throwable ignored) {}
            }
            if (!(items instanceof java.util.List)) {
                if (sInjectProbe < 6) {
                    com.tencent.qqlitepro.util.Log.w("动态点赞: b 字段不是 List，实际="
                            + (items == null ? "null" : items.getClass().getSimpleName()));
                }
                return;
            }
            java.util.List list = (java.util.List) items;
            Class<?> itemCls = null;
            for (Object it : list) {
                if (it != null) { itemCls = it.getClass(); break; }
            }
            if (itemCls == null) return;
            for (Object it : list) {
                try {
                    Object c = de.robv.android.xposed.XposedHelpers.getObjectField(it, "c");
                    if (c != null && ((Integer) c).intValue() == OUR_ACTION_ID) {
                        return;
                    }
                } catch (Throwable ignored) {}
            }
            Object newItem = null;
            for (java.lang.reflect.Constructor<?> ct : itemCls.getDeclaredConstructors()) {
                Class<?>[] ps = ct.getParameterTypes();
                if (ps.length == 5 && ps[0] == Integer.TYPE && ps[1] == Integer.TYPE
                        && ps[2] == Integer.TYPE && ps[3] == android.os.Bundle.class
                        && ps[4] == String.class) {
                    ct.setAccessible(true);
                    newItem = ct.newInstance(Integer.valueOf(0),
                            Integer.valueOf(OUR_ACTION_ID), Integer.valueOf(0),
                            new android.os.Bundle(), "em_watch_report");
                    break;
                }
            }
            if (newItem != null) {
                list.add(newItem);
                com.tencent.qqlitepro.util.Log.i("动态点赞: 已注入查看点赞用户 size=" + list.size());
            }
        } catch (Throwable t) {
            com.tencent.qqlitepro.util.Log.w("动态点赞: 注入异常 " + t);
        }
    }

    /** 记录菜单弹出时携带的 feed，供后续点击使用 */
    private static volatile Object sPendingFeed = null;
    private static final java.util.Map<String, Object> sFeedByRequestKey = new java.util.HashMap<>();
    /** 最近一次触发"更多操作"的 feed（由 renderZanNamesFromHost 记录） */
    private static volatile Object sLastFeedForMenu = null;

    private static void installMenuResultHook(java.lang.ClassLoader cl) {
        try {
            Class<?> fragCls = null;
            for (String cn : new String[]{
                    "com.tencent.qqnt.watch.ui.componet.select.SelectDialogFragment",
                    "com.tencent.qqnt.watch.ui.componet.select.SelectFragment"}) {
                try {
                    fragCls = de.robv.android.xposed.XposedHelpers.findClass(cn, cl);
                    break;
                } catch (Throwable ignored) {}
            }
            if (fragCls == null) {
                com.tencent.qqlitepro.util.Log.w("动态点赞: SelectFragment 类未找到");
                return;
            }
            com.tencent.qqlitepro.util.Log.i("动态点赞: SelectDialogFragment 注入 hook 已安装");

            /* 探测：hook Fragment.onCreateView 全局，打印实际打开的对话框类名 */
            try {
                Class<?> fragBase = de.robv.android.xposed.XposedHelpers.findClass(
                        "androidx.fragment.app.Fragment", cl);
                de.robv.android.xposed.XposedBridge.hookAllMethods(fragBase, "onCreateView",
                        new de.robv.android.xposed.XC_MethodHook() {
                            @Override
                            protected void afterHookedMethod(MethodHookParam param) {
                                try {
                                    Object self = param.thisObject;
                                    if (self == null) return;
                                    String cn = self.getClass().getName();
                                    if (cn.contains("Select") || cn.contains("Dialog")) {
                                        if (sInjectProbe++ < 10) {
                                            com.tencent.qqlitepro.util.Log.i(
                                                    "动态点赞: 检测到对话框 Fragment = " + cn);
                                        }
                                        injectOurItem(self);
                                    }
                                } catch (Throwable ignored) {}
                            }
                        });
                com.tencent.qqlitepro.util.Log.i("动态点赞: Fragment.onCreateView 探测已安装");
            } catch (Throwable t) {
                com.tencent.qqlitepro.util.Log.w("动态点赞: Fragment 探测安装失败 " + t);
            }
            /* 兜底：onCreateView 时再尝试一次注入（此时 b 一定已就绪） */
            de.robv.android.xposed.XposedBridge.hookAllMethods(fragCls, "onCreateView",
                    new de.robv.android.xposed.XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) {
                            try {
                                com.tencent.qqlitepro.util.Log.i(
                                        "动态点赞: onCreateView 命中，准备注入");
                                injectOurItem(param.thisObject);
                            } catch (Throwable t) {
                                com.tencent.qqlitepro.util.Log.w("动态点赞: onCreateView 注入失败 " + t);
                            }
                        }
                    });
            de.robv.android.xposed.XposedBridge.hookAllMethods(fragCls, "onResume",
                    new de.robv.android.xposed.XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) {
                            try {
                                injectOurItem(param.thisObject);
                            } catch (Throwable ignored) {}
                        }
                    });
            de.robv.android.xposed.XposedBridge.hookAllConstructors(fragCls,
                    new de.robv.android.xposed.XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) {
                            try {
                                injectOurItem(param.thisObject);
                            } catch (Throwable ignored) {}
                        }
                    });

            /*
             * 点击我们的菜单项时：宿主 AbsItem.onClick 会调用 SelectDialogFragment.f() 关闭。
             * 因此 hook AbsItem.onClick，若 thisObject 是我们注入的项（actionId 匹配），
             * 则打开自建点赞用户页并吞掉原逻辑。
             */
            try {
                Class<?> absCls = de.robv.android.xposed.XposedHelpers.findClass(
                        "com.tencent.qqnt.watch.ui.componet.AbsItem", cl);
                de.robv.android.xposed.XposedBridge.hookAllMethods(absCls, "onClick",
                        new de.robv.android.xposed.XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) {
                                try {
                                    Object self = param.thisObject;
                                    if (self == null) {
                                        return;
                                    }
                                    Object c = de.robv.android.xposed.XposedHelpers
                                            .getObjectField(self, "c");
                                    if (c == null || ((Integer) c).intValue() != OUR_ACTION_ID) {
                                        return;
                                    }
                                    Object feed = sPendingFeed;
                                    android.content.Context ctx = null;
                                    if (param.args != null && param.args.length > 0
                                            && param.args[0] instanceof android.view.View) {
                                        ctx = ((android.view.View) param.args[0]).getContext();
                                    }
                                    if (ctx == null) {
                                        android.app.Activity a =
                                                com.tencent.qqlitepro.hook.ChatCardInjector
                                                        .anyActivityPublic();
                                        if (a != null) {
                                            ctx = a;
                                        }
                                    }
                                    showZanUsersDialog(ctx, feed, null);
                                    /* 关闭菜单 */
                                    try {
                                        de.robv.android.xposed.XposedHelpers
                                                .callMethod(self, "f", new Object[0]);
                                    } catch (Throwable ignored) {}
                                    param.setResult(null);
                                    com.tencent.qqlitepro.util.Log.i("动态点赞: 打开自建点赞用户页");
                                } catch (Throwable t) {
                                    com.tencent.qqlitepro.util.Log.w("动态点赞: 菜单点击处理失败 " + t);
                                }
                            }
                        });
                com.tencent.qqlitepro.util.Log.i("动态点赞: AbsItem.onClick hook 已安装");
            } catch (Throwable t) {
                com.tencent.qqlitepro.util.Log.w("动态点赞: AbsItem hook 失败 " + t);
            }

            com.tencent.qqlitepro.util.Log.i("动态点赞: SelectFragment hook 已安装");
        } catch (Throwable t) {
            com.tencent.qqlitepro.util.Log.w("动态点赞: SelectFragment hook 失败 " + t);
        }
    }

    /* ================= Overlay 方案：点赞列表独立一行 =================
     * 卡片内部容器高度被约束，addView 会被裁掉；替换 RecyclerView child 又会 NPE。
     * 因此在 RecyclerView 的父容器上盖一层透明 FrameLayout，自己按每个 child 的位置
     * 绘制"赞：昵称(QQ号)、…"文本，并监听滚动实时重绘。
     */
    private static final java.util.WeakHashMap<Object, OverlayHolder> sOverlays =
            new java.util.WeakHashMap<>();

    private static final class OverlayHolder {
        android.view.ViewOverlay overlay;
        Object rv;
        /* 每条：[赞数(int), 赞列表文本] —— 用赞数做配对键，避免顺序错位 */
        java.util.List<Object[]> entries = new java.util.ArrayList<>();
        boolean scrollHooked;
    }

    /**
     * 用 RecyclerView 自带的 ViewOverlay 绘制赞列表。
     * 优点：overlay 坐标系与 RecyclerView 内容一致，随滚动自动跟随，
     *      不需要额外子 View（不会再被 SmartRefreshLayout 重排成 0 高）。
     */
    private static OverlayHolder ensureOverlay(android.view.View rv) {
        try {
            OverlayHolder oh = sOverlays.get(rv);
            if (oh != null) {
                return oh;
            }
            android.view.ViewOverlay ov = rv.getOverlay();
            if (ov == null) {
                return null;
            }
            oh = new OverlayHolder();
            oh.rv = rv;
            oh.overlay = ov;
            sOverlays.put(rv, oh);
            final OverlayHolder fOh = oh;
            final android.view.View fRv = rv;
            try {
                rv.getViewTreeObserver().addOnScrollChangedListener(
                        new android.view.ViewTreeObserver.OnScrollChangedListener() {
                            @Override
                            public void onScrollChanged() {
                                redrawOverlay(fOh, fRv);
                            }
                        });
                rv.getViewTreeObserver().addOnGlobalLayoutListener(
                        new android.view.ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                redrawOverlay(fOh, fRv);
                            }
                        });
            } catch (Throwable ignored) {}
            try {
                int[] loc = new int[2];
                rv.getLocationOnScreen(loc);
                com.tencent.qqlitepro.util.Log.i("动态点赞: ViewOverlay 已就绪 rv="
                        + rv.getClass().getSimpleName() + " w=" + rv.getWidth()
                        + " h=" + rv.getHeight() + " screenXY=" + loc[0] + "," + loc[1]
                        + " childCount=" + (rv instanceof android.view.ViewGroup
                        ? ((android.view.ViewGroup) rv).getChildCount() : -1));
            } catch (Throwable ignored) {}
            return oh;
        } catch (Throwable t) {
            com.tencent.qqlitepro.util.Log.w("动态点赞: ViewOverlay 失败 " + t);
            return null;
        }
    }

    /** 重绘：用 drawable 画到 RecyclerView 的 ViewOverlay 上 */
    private static void redrawOverlay(OverlayHolder oh, android.view.View rv) {
        if (oh == null || oh.overlay == null || rv == null) {
            return;
        }
        try {
            oh.overlay.clear();
            if (!(rv instanceof android.view.ViewGroup)) {
                return;
            }
            android.view.ViewGroup vg = (android.view.ViewGroup) rv;
            int added = 0;
            /* 遍历整棵视图树里的所有"点赞行"，每行下方画一条赞列表 */
            java.util.ArrayList<android.view.View> all = new java.util.ArrayList<>();
            collectAll(vg, all, 0);
            int rvScreenY = 0;
            int[] locRv = new int[2];
            rv.getLocationOnScreen(locRv);
            rvScreenY = locRv[1];
            /* 收集所有赞行（按屏幕 Y 从上到下排序，保证与文本顺序一致） */
            java.util.List<android.view.View> rows = new java.util.ArrayList<>();
            for (android.view.View v : all) {
                if (isZanRow(v)) rows.add(v);
            }
            /* 不过滤：保持 rows 与 entries 顺序一一对应（宿主的 num 字段常常为 0） */
            java.util.Collections.sort(rows, new java.util.Comparator<android.view.View>() {
                @Override
                public int compare(android.view.View a, android.view.View b) {
                    int[] la = new int[2];
                    int[] lb = new int[2];
                    a.getLocationOnScreen(la);
                    b.getLocationOnScreen(lb);
                    return Integer.compare(la[1], lb[1]);
                }
            });
            if (sZanProbe < 14) {
                StringBuilder dbg = new StringBuilder("动态点赞overlay: 配对 rows=" + rows.size()
                        + " entries=" + oh.entries.size() + " rvH=" + rv.getHeight() + " [");
                for (int k = 0; k < oh.entries.size() && k < 8; k++) {
                    dbg.append(oh.entries.get(k)[0]).append(",");
                }
                dbg.append("] rowsCnt=[");
                for (int k = 0; k < rows.size() && k < 8; k++) {
                    android.view.View t = findDigitTv(rows.get(k));
                    dbg.append(t == null ? "?" :
                            String.valueOf(((android.widget.TextView) t).getText())).append(",");
                }
                dbg.append("]");
                com.tencent.qqlitepro.util.Log.i(dbg.toString());
            }
            /* 顺序配对：rows（按屏幕 Y 排序）与 entries（按 onUiFeedList 顺序）一一对应 */
            int n2 = Math.min(rows.size(), oh.entries.size());
            for (int i = 0; i < n2; i++) {
                android.view.View zanRow = rows.get(i);
                String text = (String) oh.entries.get(i)[1];
                if (text == null || text.isEmpty()) continue;
                android.view.View numTv = findDigitTv(zanRow);
                android.view.View baseV = (numTv != null) ? numTv : zanRow;
                int[] locAn = new int[2];
                baseV.getLocationOnScreen(locAn);
                /* ViewOverlay 坐标原点 = RecyclerView 自身左上角 */
                int top = (locAn[1] - rvScreenY) + baseV.getHeight() + 6;
                if (top < -50 || top > rv.getHeight() + 50) continue;
                ZanDrawable d = new ZanDrawable(text, rv.getWidth());
                d.setBounds(0, top, rv.getWidth(), top + d.getHeight());
                oh.overlay.add(d);
                added++;
            }
        } catch (Throwable ignored) {}
    }

    /** 把赞列表画成 Drawable（支持自动换行） */
    private static final class ZanDrawable extends android.graphics.drawable.Drawable {
        private final android.text.TextPaint paint = new android.text.TextPaint();
        private final java.util.List<String> lines = new java.util.ArrayList<>();
        private final android.graphics.Paint bgPaint = new android.graphics.Paint();
        private final android.graphics.RectF bgRect = new android.graphics.RectF();
        private final float padH;
        private final float padV;
        private int w = 0;
        private int h = 0;

        ZanDrawable(String text, int width) {
            paint.setAntiAlias(true);
            paint.setColor(-6643546);
            paint.setTextSize(26f);
            /* 半透明底板，避免与卡片内其它文字视觉重叠 */
            bgPaint.setAntiAlias(true);
            bgPaint.setColor(android.graphics.Color.argb(230, 32, 32, 34));
            bgPaint.setStyle(android.graphics.Paint.Style.FILL);
            padH = 20f;
            padV = 8f;
            w = width;
            /* 简单按宽度折行 */
            StringBuilder cur = new StringBuilder();
            float maxW = w - padH * 2 - 20;
            for (int i = 0; i < text.length(); i++) {
                char c = text.charAt(i);
                cur.append(c);
                if (paint.measureText(cur.toString()) > maxW) {
                    lines.add(cur.toString());
                    cur.setLength(0);
                }
            }
            if (cur.length() > 0) {
                lines.add(cur.toString());
            }
            if (lines.isEmpty()) {
                lines.add(text);
            }
            h = (int) ((paint.descent() - paint.ascent() + 4) * lines.size()
                    + padV * 2) + 4;
        }

        int getHeight() {
            return h;
        }

        @Override
        public void draw(android.graphics.Canvas canvas) {
            android.graphics.Rect b = getBounds();
            bgRect.set(b.left + padH / 2, b.top, b.right - padH / 2, b.bottom);
            canvas.drawRoundRect(bgRect, 16f, 16f, bgPaint);
            float y = b.top + padV - paint.ascent() + 2;
            for (String line : lines) {
                canvas.drawText(line, b.left + padH, y, paint);
                y += paint.descent() - paint.ascent() + 4;
            }
        }

        @Override
        public void setAlpha(int alpha) {
            paint.setAlpha(alpha);
        }

        @Override
        public void setColorFilter(android.graphics.ColorFilter cf) {
            paint.setColorFilter(cf);
        }

        @Override
        public int getOpacity() {
            return android.graphics.PixelFormat.TRANSLUCENT;
        }
    }

    /** 重绘：遍历 RecyclerView 可见 child，在每张卡片底部画赞列表 */

    /** 清空所有 overlay 文本（新一轮 onUiFeedList 前调用） */
    public static void clearAllOverlayTexts() {
        try {
            for (java.util.Map.Entry<Object, OverlayHolder> e : sOverlays.entrySet()) {
                OverlayHolder oh = e.getValue();
                if (oh != null && oh.entries != null) {
                    oh.entries.clear();
                }
            }
        } catch (Throwable ignored) {}
    }

    /** 把某张卡片的赞列表文本登记到 overlay 并重绘 */
    private static void putOverlayText(android.view.View rv, android.view.View card, String text) {
        putOverlayText(rv, card, text, -1);
    }

    /** 记录赞列表文本，同时记录该动态的赞数（用于与视图里的赞行精确配对） */
    private static void putOverlayText(android.view.View rv, android.view.View card, String text,
                                       int likeCount) {
        try {
            OverlayHolder oh = ensureOverlay(rv);
            if (oh == null) {
                return;
            }
            oh.entries.add(new Object[]{Integer.valueOf(likeCount), text});
            redrawOverlay(oh, rv);
        } catch (Throwable ignored) {}
    }

    /**
     * 从 host（adapter 或宿主对象）反查 RecyclerView，按 feed 找到对应 itemView 再渲染。
     * 用于 onUiFeedList 这条不带 holder 的路径。
     */
    public static void renderZanNamesFromHost(Object host, Object data) {
        try {
            if (host == null || data == null) {
                return;
            }
            sLastFeedForMenu = data;
            android.view.View itemView = findItemViewForFeed(host, data);
            if (itemView == null) {
                return;
            }
            Object fakeHolder = new Object() {
                public android.view.View itemView;
            };
            /* 直接用 itemView 渲染（不走 holder 反射） */
            renderZanNamesOnView(itemView, data);
        } catch (Throwable ignored) {
        }
    }

    /** 在给定 itemView 上渲染点赞列表 */
    public static void renderZanNamesOnView(android.view.View itemView, Object data) {
        try {
            if (itemView == null || data == null) {
                return;
            }
            Object cellLike = null;
            try {
                cellLike = de.robv.android.xposed.XposedHelpers.getObjectField(data, "cellLikeInfo");
            } catch (Throwable ignored) {}
            if (cellLike == null) {
                try {
                    cellLike = de.robv.android.xposed.XposedHelpers.callMethod(data, "getCellLikeInfo", new Object[0]);
                } catch (Throwable ignored) {}
            }
            String zanText = buildZanText(cellLike);
            if (sZanProbe++ < 10) {
                com.tencent.qqlitepro.util.Log.i("动态点赞诊断(view): cellLike="
                        + (cellLike == null ? "null" : "ok") + " zanText=[" + zanText + "]");
            }
            /*
             * 已按需求彻底移除"点赞按钮下方显示列表"。
             * 这里只保留数据缓存（供更多操作菜单里的"查看点赞用户"使用），
             * 不再向 RecyclerView 的 Overlay 绘制任何内容。
             */
            sLastFeedForMenu = data;
            if (false) {
            android.view.View rv = findRecyclerView(itemView);
            if (rv == null) {
                /* 从 itemView 向上找 RecyclerView */
                android.view.ViewParent vp = itemView.getParent();
                while (vp instanceof android.view.View && rv == null) {
                    if (isRv(vp)) {
                        rv = (android.view.View) vp;
                        break;
                    }
                    vp = vp.getParent();
                }
            }
            /* 给所有可见卡片的 holder 追加"查看点赞"入口 */
            try {
                android.view.View rv0 = findRecyclerView(itemView);
                if (rv0 == null) {
                    android.view.ViewParent vpp = itemView.getParent();
                    while (vpp instanceof android.view.View && rv0 == null) {
                        if (isRv(vpp)) { rv0 = (android.view.View) vpp; break; }
                        vpp = vpp.getParent();
                    }
                }
                if (rv0 instanceof android.view.ViewGroup) {
                    Object rvp = rv0.getParent();
                    while (rvp != null && !isRv(rvp)) {
                        rvp = (rvp instanceof android.view.View) ? ((android.view.View) rvp).getParent() : null;
                    }
                    Object adapter = null;
                    try {
                        adapter = de.robv.android.xposed.XposedHelpers
                                .callMethod(rv0, "getAdapter", new Object[0]);
                    } catch (Throwable ignored) {}
                    if (adapter != null) {
                        android.view.View vie = rv0;
                        java.lang.reflect.Method gvh = null;
                        for (java.lang.reflect.Method mm : vie.getClass().getMethods()) {
                            if (mm.getName().equals("getChildViewHolder")
                                    && mm.getParameterTypes().length == 1
                                    && mm.getParameterTypes()[0] == android.view.View.class) {
                                gvh = mm;
                                break;
                            }
                        }
                        if (gvh != null) {
                            android.view.ViewGroup vg0 = (android.view.ViewGroup) rv0;
                            for (int ci = 0; ci < vg0.getChildCount(); ci++) {
                                android.view.View ch = vg0.getChildAt(ci);
                                if (ch == null) continue;
                                try {
                                    Object vh = gvh.invoke(vie, ch);
                                    hookMoreButtonOfHolder(vh);
                                } catch (Throwable ignored) {}
                            }
                        }
                    }
                }
            } catch (Throwable ignored) {}

            if (rv != null && zanText != null && !zanText.isEmpty()) {
                int likeCnt = 0;
                try {
                    Object num = de.robv.android.xposed.XposedHelpers.getObjectField(cellLike, "num");
                    likeCnt = num == null ? 0 : ((Number) num).intValue();
                } catch (Throwable ignored) {}
                putOverlayText(rv, itemView, "赞：" + zanText, likeCnt);
                if (sZanProbe < 20) {
                    com.tencent.qqlitepro.util.Log.i("动态点赞诊断: overlay 登记成功 len="
                            + zanText.length());
                }
            }
            }
        } catch (Throwable t) {
            if (sZanErr++ < 5) {
                com.tencent.qqlitepro.util.Log.w("动态点赞异常(view): " + t);
            }
        }
    }

    private static String buildZanText(Object cellLike) {
        StringBuilder sb = new StringBuilder();
        if (cellLike == null) {
            return "";
        }
        Object mans = null;
        try {
            mans = de.robv.android.xposed.XposedHelpers.getObjectField(cellLike, "likeMans");
        } catch (Throwable ignored) {}
        if (!(mans instanceof java.util.List)) {
            return "";
        }
        for (Object man : (java.util.List) mans) {
            if (man == null) continue;
            try {
                Object user = de.robv.android.xposed.XposedHelpers.getObjectField(man, "user");
                if (user == null) continue;
                String nick = null;
                String uin = "";
                for (String nf : new String[]{"nickName", "nameSeperate", "nick"}) {
                    try {
                        Object n = de.robv.android.xposed.XposedHelpers.getObjectField(user, nf);
                        if (n != null && !String.valueOf(n).trim().isEmpty()) {
                            nick = String.valueOf(n).trim();
                            break;
                        }
                    } catch (Throwable ignored) {}
                }
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
        return sb.toString();
    }

    private static void applyZanText(android.view.View itemView, String zanText) {
        /*
         * 独立一行，插在"点赞按钮"下方。
         *
         * 关键约束（踩过的坑）：
         *  1. 不能替换 RecyclerView 的直接 child —— 会让 ViewHolder 与 child 映射失效，
         *     直接抛 NPE（findMinMaxChildLayoutPositions on null ViewHolder）。
         *  2. 卡片内部有高度被约束的容器，随便 addView 会被裁掉。
         * 因此：在卡片内部的"纵向容器"里，紧跟点赞行之后插入，并强制 requestLayout。
         */
        try {
            if (itemView == null) {
                return;
            }
            /* 1) 找点赞行（含纯数字赞数的横向容器） */
            android.view.View zanRow = findZanRow(itemView);
            android.view.ViewGroup parent = null;
            int insertIndex = -1;
            if (zanRow != null && zanRow.getParent() instanceof android.view.ViewGroup) {
                parent = (android.view.ViewGroup) zanRow.getParent();
                insertIndex = parent.indexOfChild(zanRow) + 1;
            }
            if (parent == null) {
                android.view.ViewGroup vc = (itemView instanceof android.view.ViewGroup)
                        ? findVerticalContainerIn((android.view.ViewGroup) itemView) : null;
                if (vc != null) {
                    parent = vc;
                } else if (itemView instanceof android.view.ViewGroup) {
                    parent = (android.view.ViewGroup) itemView;
                }
                insertIndex = parent == null ? -1 : parent.getChildCount();
            }
            if (parent == null || insertIndex < 0) {
                return;
            }

            /* 2) 复用或新建 */
            android.widget.TextView line = null;
            for (int i = 0; i < parent.getChildCount(); i++) {
                android.view.View cv = parent.getChildAt(i);
                if ("qqlite_zan_names".equals(cv.getTag())) {
                    line = (android.widget.TextView) cv;
                    break;
                }
            }
            if (zanText == null || zanText.isEmpty()) {
                if (line != null) {
                    line.setVisibility(android.view.View.GONE);
                }
                return;
            }
            if (line == null) {
                line = new android.widget.TextView(itemView.getContext());
                line.setTag("qqlite_zan_names");
                line.setTextSize(11.5f);
                line.setTextColor(-6643546);
                line.setSingleLine(false);
                line.setMaxLines(30);
                line.setEllipsize(null);
                line.setLineSpacing(0f, 1.2f);
                int pad = (int) (itemView.getContext().getResources()
                        .getDisplayMetrics().density * 10);
                line.setPadding(pad, 2, pad, 8);
                /* 用父容器自己的 LayoutParams 类型，避免类型不匹配被忽略 */
                android.view.ViewGroup.LayoutParams lp = null;
                try {
                    java.lang.reflect.Method gm = android.view.ViewGroup.class
                            .getDeclaredMethod("generateDefaultLayoutParams");
                    gm.setAccessible(true);
                    lp = (android.view.ViewGroup.LayoutParams) gm.invoke(parent);
                } catch (Throwable ignored) {}
                if (lp == null) {
                    lp = new android.view.ViewGroup.LayoutParams(-1, -2);
                }
                lp.width = -1;
                lp.height = -2;
                parent.addView(line, Math.min(insertIndex, parent.getChildCount()), lp);
            } else {
                line.setVisibility(android.view.View.VISIBLE);
            }
            line.setText("赞：" + zanText);

            if (sZanProbe < 20) {
                com.tencent.qqlitepro.util.Log.i("动态点赞诊断: 已插入独立一行 parent="
                        + parent.getClass().getSimpleName()
                        + " idx=" + insertIndex + "/" + parent.getChildCount()
                        + " textLen=" + zanText.length());
            }
            try {
                parent.requestLayout();
                itemView.requestLayout();
            } catch (Throwable ignored) {}
        } catch (Throwable t) {
            if (sZanErr++ < 5) {
                com.tencent.qqlitepro.util.Log.w("动态点赞异常: " + t);
            }
        }
    }

    /** 取容器内显示赞数的 TextView */
    private static android.view.View findDigitTv(android.view.View group) {
        try {
            if (!(group instanceof android.view.ViewGroup)) {
                return null;
            }
            android.view.ViewGroup g = (android.view.ViewGroup) group;
            for (int i = 0; i < g.getChildCount(); i++) {
                android.view.View c = g.getChildAt(i);
                if (!(c instanceof android.widget.TextView)) continue;
                String txt = String.valueOf(((android.widget.TextView) c).getText()).trim();
                if (txt.isEmpty() || txt.length() > 4) continue;
                boolean allDigit = true;
                for (int k = 0; k < txt.length(); k++) {
                    if (!Character.isDigit(txt.charAt(k))) { allDigit = false; break; }
                }
                if (allDigit) {
                    return c;
                }
            }
        } catch (Throwable ignored) {}
        return null;
    }

    /** 判断某 View 是否为"点赞行"（含纯数字赞数 TextView 的容器） */
    private static boolean isZanRow(android.view.View row) {
        try {
            if (!(row instanceof android.view.ViewGroup)) {
                return false;
            }
            android.view.ViewGroup g = (android.view.ViewGroup) row;
            if (g.getChildCount() == 0 || g.getChildCount() > 6) {
                return false;
            }
            for (int i = 0; i < g.getChildCount(); i++) {
                android.view.View c = g.getChildAt(i);
                if (!(c instanceof android.widget.TextView)) continue;
                String txt = String.valueOf(((android.widget.TextView) c).getText()).trim();
                if (txt.isEmpty() || txt.length() > 4) continue;
                boolean allDigit = true;
                for (int k = 0; k < txt.length(); k++) {
                    if (!Character.isDigit(txt.charAt(k))) { allDigit = false; break; }
                }
                if (allDigit) {
                    return true;
                }
            }
        } catch (Throwable ignored) {}
        return false;
    }

    /** 在 card 内部找"点赞行"（含纯数字赞数），找不到返回 null */
    private static android.view.View findZanRowInCard(android.view.View card) {
        try {
            java.util.ArrayList<android.view.View> all = new java.util.ArrayList<>();
            collectAll(card, all, 0);
            android.view.View best = null;
            for (android.view.View v : all) {
                if (!(v instanceof android.widget.TextView)) continue;
                android.widget.TextView tv = (android.widget.TextView) v;
                String txt = String.valueOf(tv.getText()).trim();
                if (txt.isEmpty() || txt.length() > 4) continue;
                boolean allDigit = true;
                for (int i = 0; i < txt.length(); i++) {
                    if (!Character.isDigit(txt.charAt(i))) { allDigit = false; break; }
                }
                if (!allDigit) continue;
                if (tv.getParent() instanceof android.view.ViewGroup) {
                    best = (android.view.View) tv.getParent();
                }
            }
            return best;
        } catch (Throwable ignored) {
            return null;
        }
    }

    /** 找"点赞行"：包含纯数字赞数 TextView 的最内层横向容器 */
    private static android.view.View findZanRow(android.view.View root) {
        java.util.ArrayList<android.view.View> all = new java.util.ArrayList<>();
        collectAll(root, all, 0);
        android.view.View best = null;
        for (android.view.View v : all) {
            if (!(v instanceof android.widget.TextView)) continue;
            android.widget.TextView tv = (android.widget.TextView) v;
            String txt = String.valueOf(tv.getText()).trim();
            if (txt.isEmpty() || txt.length() > 4) continue;
            boolean allDigit = true;
            for (int i = 0; i < txt.length(); i++) {
                if (!Character.isDigit(txt.charAt(i))) { allDigit = false; break; }
            }
            if (!allDigit) continue;
            android.view.ViewParent p = tv.getParent();
            if (p instanceof android.view.ViewGroup) {
                best = (android.view.View) p;
            }
        }
        return best;
    }

    /** 从 host 反查 RecyclerView，按可见位置粗略匹配 feed（匹配不到则返回第一个） */
    private static int sFeedIdx = 0;

    /** 每次 onUiFeedList 前重置索引 */
    public static void resetFeedIndex() {
        sFeedIdx = 0;
    }

    private static android.view.View findItemViewForFeed(Object host, Object data) {
        try {
            android.view.View rv = findRecyclerView(host);
            if (rv == null || !(rv instanceof android.view.ViewGroup)) {
                return null;
            }
            android.view.ViewGroup vg = (android.view.ViewGroup) rv;
            for (int i = 0; i < vg.getChildCount(); i++) {
                android.view.View child = vg.getChildAt(i);
                if (child != null && child.getTag() == data) {
                    return child;
                }
            }
            /* 按索引对应：第 N 条 feed -> 第 N 个可见卡片 */
            int idx = sFeedIdx++;
            java.util.ArrayList<android.view.View> cards = new java.util.ArrayList<>();
            for (int i = 0; i < vg.getChildCount(); i++) {
                android.view.View child = vg.getChildAt(i);
                if (child != null && child.getHeight() > 0) {
                    cards.add(child);
                }
            }
            if (!cards.isEmpty()) {
                return cards.get(idx % cards.size());
            }
        } catch (Throwable ignored) {}
        return null;
    }

    /** 通过类名反射判定的 RecyclerView */
    static boolean isRv(Object v) {
        return v != null && v.getClass().getName().endsWith("RecyclerView");
    }

    private static android.view.View findRecyclerView(Object host) {
        try {
            if (isRv(host)) {
                return (android.view.View) host;
            }
            if (host instanceof android.view.View) {
                return findRvIn((android.view.View) host, 0);
            }
            /* Fragment：通过 getView() / getRecyclerView() 拿到视图树 */
            try {
                Object v0 = de.robv.android.xposed.XposedHelpers.callMethod(host, "getView", new Object[0]);
                if (v0 instanceof android.view.View) {
                    android.view.View r0 = findRvIn((android.view.View) v0, 0);
                    if (r0 != null) return r0;
                }
            } catch (Throwable ignored) {}
            for (String f : new String[]{"b", "host", "recyclerView", "rv", "mRecyclerView"}) {
                try {
                    Object v = de.robv.android.xposed.XposedHelpers.getObjectField(host, f);
                    if (isRv(v)) {
                        return (android.view.View) v;
                    }
                    if (v instanceof android.view.View) {
                        android.view.View r = findRvIn((android.view.View) v, 0);
                        if (r != null) return r;
                    }
                } catch (Throwable ignored) {}
            }
            /* 最后手段：遍历对象自身字段（含父类）找 RecyclerView 引用 */
            for (Class<?> c = host.getClass(); c != null && c != Object.class; c = c.getSuperclass()) {
                for (java.lang.reflect.Field f : c.getDeclaredFields()) {
                    if (java.lang.reflect.Modifier.isStatic(f.getModifiers())) continue;
                    try {
                        f.setAccessible(true);
                        Object v = f.get(host);
                        if (isRv(v)) {
                            return (android.view.View) v;
                        }
                        if (v instanceof android.view.View) {
                            android.view.View r = findRvIn((android.view.View) v, 0);
                            if (r != null) return r;
                        }
                    } catch (Throwable ignored) {}
                }
            }
        } catch (Throwable ignored) {}
        return null;
    }

    private static android.view.View findRvIn(android.view.View v, int depth) {
        if (v == null || depth > 12) {
            return null;
        }
        if (isRv(v)) {
            return v;
        }
        if (v instanceof android.view.ViewGroup) {
            android.view.ViewGroup vg = (android.view.ViewGroup) v;
            for (int i = 0; i < vg.getChildCount(); i++) {
                android.view.View r = findRvIn(vg.getChildAt(i), depth + 1);
                if (r != null) return r;
            }
        }
        return null;
    }

    private static volatile int sZanProbe = 0;
    private static volatile int sBindProbe = 0;
    private static volatile int sZanErr = 0;

    /** 找能承载"点赞昵称"的纵向容器：优先卡片里最靠下的纵向容器 */
    /** 找"点赞数"TextView：纯数字、字号小、深度浅 */
    private static android.widget.TextView findZanCountTv(android.view.View root) {
        java.util.ArrayList<android.view.View> all = new java.util.ArrayList<>();
        collectAll(root, all, 0);
        android.widget.TextView best = null;
        for (android.view.View v : all) {
            if (!(v instanceof android.widget.TextView)) continue;
            android.widget.TextView tv = (android.widget.TextView) v;
            String txt = String.valueOf(tv.getText()).trim();
            if (txt.isEmpty() || txt.length() > 8) continue;
            boolean allDigit = true;
            for (int i = 0; i < txt.length(); i++) {
                if (!Character.isDigit(txt.charAt(i))) { allDigit = false; break; }
            }
            if (!allDigit) continue;
            /* 排除很大的数字（如时间/QQ号） */
            if (txt.length() > 4) continue;
            best = tv;
        }
        return best;
    }

    private static android.view.ViewGroup findVerticalContainerIn(android.view.ViewGroup root) {
        java.util.ArrayList<android.view.View> all = new java.util.ArrayList<>();
        collectAll(root, all, 0);
        for (android.view.View v : all) {
            if (v instanceof android.widget.LinearLayout
                    && ((android.widget.LinearLayout) v).getOrientation()
                    == android.widget.LinearLayout.VERTICAL) {
                return (android.view.ViewGroup) v;
            }
        }
        return null;
    }

    private static android.view.ViewGroup findZanParent(android.view.View root) {
        try {
            java.util.ArrayList<android.view.View> all = new java.util.ArrayList<>();
            collectAll(root, all, 0);
            android.view.ViewGroup best = null;
            int bestBottom = -1;
            for (android.view.View v : all) {
                if (!(v instanceof android.widget.LinearLayout)) continue;
                android.widget.LinearLayout ll = (android.widget.LinearLayout) v;
                if (ll.getOrientation() != android.widget.LinearLayout.VERTICAL) continue;
                int b = v.getBottom();
                if (b >= bestBottom) {
                    bestBottom = b;
                    best = ll;
                }
            }
            if (best != null) {
                return best;
            }
            if (root instanceof android.view.ViewGroup) {
                return (android.view.ViewGroup) root;
            }
        } catch (Throwable ignored) {}
        return null;
    }

    private static void collectAll(android.view.View root, java.util.ArrayList<android.view.View> out, int depth) {
        if (root == null || depth > 20 || out.size() > 300) {
            return;
        }
        out.add(root);
        if (root instanceof android.view.ViewGroup) {
            android.view.ViewGroup vg = (android.view.ViewGroup) root;
            for (int i = 0; i < vg.getChildCount(); i++) {
                collectAll(vg.getChildAt(i), out, depth + 1);
            }
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.QzoneDynamicHook$3, reason: invalid class name */
    static class AnonymousClass3 extends de.robv.android.xposed.XC_MethodHook {
        AnonymousClass3() {
        }

        protected void afterHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            if (sBindProbe++ < 5) {
                com.tencent.qqlitepro.util.Log.i("动态点赞诊断: onBindViewHolder(3) 命中 holder="
                        + (methodHookParam.args[0] == null ? "null"
                        : methodHookParam.args[0].getClass().getSimpleName()));
            }
            int iIntValue = methodHookParam.args[1] instanceof java.lang.Integer ? ((java.lang.Integer) methodHookParam.args[1]).intValue() : -1;
            Object data = com.tencent.qqlitepro.hook.QzoneDynamicHook.extractFeed(methodHookParam.thisObject, iIntValue);
            /* 先渲染点赞用户列表（复用这里已确认可用的 hook 通道） */
            try {
                com.tencent.qqlitepro.hook.QzoneDynamicHook.renderZanNames(
                        methodHookParam.args[0], data);
            } catch (Throwable ignored) {}
            com.tencent.qqlitepro.hook.QzoneDynamicHook.dispatch(methodHookParam.thisObject, data, iIntValue);
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.QzoneDynamicHook$4, reason: invalid class name */
    static class AnonymousClass4 extends de.robv.android.xposed.XC_MethodHook {
        AnonymousClass4() {
        }

        protected void afterHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            if (sBindProbe++ < 5) {
                com.tencent.qqlitepro.util.Log.i("动态点赞诊断: onBindViewHolder(4) 命中");
            }
            java.lang.Object obj = methodHookParam.thisObject;
            int iPos0 = com.tencent.qqlitepro.hook.QzoneDynamicHook.pos0(methodHookParam);
            java.lang.Object data4 = com.tencent.qqlitepro.hook.QzoneDynamicHook.extractFeed(obj, iPos0);
            try {
                com.tencent.qqlitepro.hook.QzoneDynamicHook.renderZanNames(methodHookParam.args[0], data4);
            } catch (Throwable ignored) {}
            com.tencent.qqlitepro.hook.QzoneDynamicHook.dispatch(obj, data4, iPos0);
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
            /* 所有 dispatch 路径都会到这里：顺便渲染点赞用户列表 */
            try {
                com.tencent.qqlitepro.hook.QzoneDynamicHook.renderZanNames(safeField(obj, "b"), obj2);
            } catch (Throwable ignored) {}
            com.tencent.qqlitepro.auto.QqDynamicScheduler.onFeedCaptured(safeField(obj, "b"), obj2);
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("QQ动态: 捕获动态失败 " + th);
        }
    }
}
