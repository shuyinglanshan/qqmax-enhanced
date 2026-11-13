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

    /**
     * 从 host（adapter 或宿主对象）反查 RecyclerView，按 feed 找到对应 itemView 再渲染。
     * 用于 onUiFeedList 这条不带 holder 的路径。
     */
    public static void renderZanNamesFromHost(Object host, Object data) {
        try {
            if (host == null || data == null) {
                return;
            }
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
            applyZanText(itemView, zanText);
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
    private static boolean isRv(Object v) {
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
