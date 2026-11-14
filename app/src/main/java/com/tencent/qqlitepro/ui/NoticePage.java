package com.tencent.qqlitepro.ui;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.tencent.qqlitepro.http.QqKernel;
import com.tencent.qqlitepro.net.FeedbackApi;
import com.tencent.qqlitepro.util.Log;

/**
 * 通知中心全屏页。
 *
 * 支持两类通知（服务端 category 区分）：
 *  - feedback：反馈处理结果（refId = 反馈编号）
 *  - system / admin：系统公告、管理员下发
 *
 * 行为：
 *  - 进入即拉取列表（默认全部，可切"只看未读"）
 *  - 未读项高亮 + 左侧竖条
 *  - 点击某条：标记已读并刷新
 *  - 顶部「全部已读」
 */
public final class NoticePage {

    private static final int COLOR_BG = -15657960;
    private static final int COLOR_CARD = -14013129;
    private static final int COLOR_CARD_UNREAD = -14079200;
    private static final int COLOR_TITLE = -1;
    private static final int COLOR_SUB = -6709078;
    private static final int COLOR_ACCENT = -16737793;
    private static final int COLOR_HINT = -8355712;

    private NoticePage() {
    }

    public static void show(final Context ctx) {
        try {
            final android.app.Dialog dialog = new android.app.Dialog(ctx);
            dialog.requestWindowFeature(1);
            dialog.setCanceledOnTouchOutside(true);

            final LinearLayout root = new LinearLayout(ctx);
            root.setOrientation(1);
            root.setBackgroundColor(COLOR_BG);
            int pad = dp(ctx, 14);
            root.setPadding(pad, dp(ctx, 10), pad, dp(ctx, 12));

            /* 标题栏 */
            LinearLayout header = new LinearLayout(ctx);
            header.setOrientation(0);
            header.setGravity(16);
            TextView title = new TextView(ctx);
            title.setText("通知");
            title.setTextSize(18f);
            title.setTextColor(COLOR_TITLE);
            header.addView(title, new LinearLayout.LayoutParams(0, -2, 1f));

            TextView markAll = new TextView(ctx);
            markAll.setText("全部已读");
            markAll.setTextSize(12.5f);
            markAll.setTextColor(COLOR_ACCENT);
            markAll.setPadding(dp(ctx, 8), dp(ctx, 4), dp(ctx, 8), dp(ctx, 4));
            header.addView(markAll);

            TextView close = new TextView(ctx);
            close.setText("关闭");
            close.setTextSize(12.5f);
            close.setTextColor(COLOR_ACCENT);
            close.setPadding(dp(ctx, 8), dp(ctx, 4), 0, dp(ctx, 4));
            close.setOnClickListener(new android.view.View.OnClickListener() {
                @Override
                public void onClick(android.view.View v) {
                    try { dialog.dismiss(); } catch (Throwable ignored) {}
                }
            });
            header.addView(close);
            root.addView(header);

            /* 过滤切换 */
            final boolean[] unreadOnly = new boolean[]{false};
            final TextView filterTv = new TextView(ctx);
            filterTv.setText("显示：全部");
            filterTv.setTextSize(12f);
            filterTv.setTextColor(COLOR_ACCENT);
            filterTv.setPadding(0, dp(ctx, 6), 0, dp(ctx, 8));
            root.addView(filterTv);

            final LinearLayout listBox = new LinearLayout(ctx);
            listBox.setOrientation(1);
            ScrollView sv = new ScrollView(ctx);
            sv.addView(listBox);
            root.addView(sv, new LinearLayout.LayoutParams(-1, 0, 1f));

            dialog.setContentView(root);
            if (dialog.getWindow() != null) {
                android.view.WindowManager.LayoutParams attrs = dialog.getWindow().getAttributes();
                attrs.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
                attrs.height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
                dialog.getWindow().setAttributes(attrs);
                dialog.getWindow().setBackgroundDrawable(
                        new android.graphics.drawable.ColorDrawable(COLOR_BG));
            }
            dialog.show();

            final android.os.Handler h = new android.os.Handler(android.os.Looper.getMainLooper());
            final Runnable reload = new Runnable() {
                @Override
                public void run() {
                    final String qq = safe(QqKernel.selfUin());
                    if (qq.isEmpty()) {
                        showEmpty(ctx, listBox, "未登录，无法获取通知");
                        return;
                    }
                    listBox.removeAllViews();
                    TextView loading = new TextView(ctx);
                    loading.setText("加载中…");
                    loading.setTextSize(13f);
                    loading.setTextColor(COLOR_HINT);
                    loading.setPadding(0, dp(ctx, 16), 0, 0);
                    listBox.addView(loading);

                    final boolean onlyUnread = unreadOnly[0];
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            final java.util.List<FeedbackApi.Notice> list =
                                    FeedbackApi.fetchList(qq, onlyUnread, 1);
                            h.post(new Runnable() {
                                @Override
                                public void run() {
                                    listBox.removeAllViews();
                                    if (list == null) {
                                        showEmpty(ctx, listBox, "加载失败，请检查网络后重试");
                                        return;
                                    }
                                    if (list.isEmpty()) {
                                        showEmpty(ctx, listBox, onlyUnread
                                                ? "没有未读通知" : "暂无通知");
                                        return;
                                    }
                                    for (final FeedbackApi.Notice n : list) {
                                        listBox.addView(row(ctx, dialog, n, h, this, qq));
                                    }
                                }
                            });
                        }
                    }, "notice-load").start();
                }
            };

            filterTv.setOnClickListener(new android.view.View.OnClickListener() {
                @Override
                public void onClick(android.view.View v) {
                    unreadOnly[0] = !unreadOnly[0];
                    filterTv.setText(unreadOnly[0] ? "显示：仅未读" : "显示：全部");
                    reload.run();
                }
            });

            markAll.setOnClickListener(new android.view.View.OnClickListener() {
                @Override
                public void onClick(android.view.View v) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            final String qq = safe(QqKernel.selfUin());
                            final boolean ok = FeedbackApi.markAllRead(qq);
                            if (ok) {
                                /* 全部已读：红点直接清零，无需再等网络 */
                                com.tencent.qqlitepro.hook.SettingsInjector.setUnreadBadge(0);
                            }
                            h.post(new Runnable() {
                                @Override
                                public void run() {
                                    toast(ctx, ok ? "已全部标记为已读" : "操作失败");
                                    reload.run();
                                }
                            });
                        }
                    }, "notice-readall").start();
                }
            });

            reload.run();
        } catch (Throwable t) {
            Log.w("通知页打开失败: " + t);
        }
    }

    /** 单条通知行 */
    private static android.view.View row(final Context ctx, final android.app.Dialog dialog,
                                         final FeedbackApi.Notice n,
                                         final android.os.Handler h,
                                         final Runnable reload,
                                         final String qq) {
        LinearLayout card = new LinearLayout(ctx);
        card.setOrientation(0);
        GradientDrawable bg = new GradientDrawable();
        bg.setColor(n.read ? COLOR_CARD : COLOR_CARD_UNREAD);
        bg.setCornerRadius(dp(ctx, 10));
        card.setBackground(bg);
        int p = dp(ctx, 11);
        card.setPadding(p, p, p, p);
        LinearLayout.LayoutParams clp = new LinearLayout.LayoutParams(-1, -2);
        clp.topMargin = dp(ctx, 8);
        card.setLayoutParams(clp);

        /* 未读左侧竖条 */
        if (!n.read) {
            android.view.View bar = new android.view.View(ctx);
            GradientDrawable bbg = new GradientDrawable();
            bbg.setColor(COLOR_ACCENT);
            bbg.setCornerRadius(dp(ctx, 2));
            bar.setBackground(bbg);
            LinearLayout.LayoutParams blp =
                    new LinearLayout.LayoutParams(dp(ctx, 3), -1);
            blp.rightMargin = dp(ctx, 9);
            card.addView(bar, blp);
        }

        LinearLayout mid = new LinearLayout(ctx);
        mid.setOrientation(1);
        card.addView(mid, new LinearLayout.LayoutParams(0, -2, 1f));

        /* 标签 + 时间 */
        LinearLayout topRow = new LinearLayout(ctx);
        topRow.setOrientation(0);
        topRow.setGravity(16);
        TextView tag = new TextView(ctx);
        String label = n.categoryLabel == null || n.categoryLabel.isEmpty()
                ? (n.category == null ? "通知" : n.category) : n.categoryLabel;
        tag.setText(label);
        tag.setTextSize(10.5f);
        tag.setTextColor(COLOR_ACCENT);
        GradientDrawable tbg = new GradientDrawable();
        tbg.setColor(-14935012);
        tbg.setCornerRadius(dp(ctx, 4));
        tag.setBackground(tbg);
        tag.setPadding(dp(ctx, 5), dp(ctx, 1), dp(ctx, 5), dp(ctx, 1));
        topRow.addView(tag);
        TextView time = new TextView(ctx);
        time.setText(relTime(n.createdAt));
        time.setTextSize(10.5f);
        time.setTextColor(COLOR_HINT);
        LinearLayout.LayoutParams tlp = new LinearLayout.LayoutParams(0, -2, 1f);
        tlp.leftMargin = dp(ctx, 8);
        topRow.addView(time, tlp);
        mid.addView(topRow);

        TextView title = new TextView(ctx);
        title.setText(n.title == null ? "" : n.title);
        title.setTextSize(13.5f);
        title.setTextColor(n.read ? COLOR_SUB : COLOR_TITLE);
        title.setTypeface(null, n.read
                ? android.graphics.Typeface.NORMAL : android.graphics.Typeface.BOLD);
        title.setPadding(0, dp(ctx, 5), 0, 0);
        mid.addView(title);

        if (n.content != null && !n.content.isEmpty()) {
            TextView content = new TextView(ctx);
            content.setText(n.content);
            content.setTextSize(12f);
            content.setTextColor(COLOR_SUB);
            content.setPadding(0, dp(ctx, 4), 0, 0);
            mid.addView(content);
        }

        card.setClickable(true);
        card.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                if (n.read) {
                    return;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        FeedbackApi.markRead(n.id, qq);
                        /* 立即刷新设置页小红点（不等下一轮轮询） */
                        com.tencent.qqlitepro.hook.SettingsInjector.refreshUnreadBadge();
                        h.post(new Runnable() {
                            @Override
                            public void run() {
                                reload.run();
                            }
                        });
                    }
                }, "notice-read").start();
            }
        });
        return card;
    }

    private static void showEmpty(Context ctx, LinearLayout box, String msg) {
        TextView t = new TextView(ctx);
        t.setText(msg);
        t.setTextSize(13f);
        t.setTextColor(COLOR_HINT);
        t.setPadding(0, dp(ctx, 24), 0, 0);
        t.setGravity(17);
        box.addView(t);
    }

    /** 相对时间：刚刚 / N 分钟前 / N 小时前 / N 天前 */
    private static String relTime(long ms) {
        try {
            if (ms <= 0) {
                return "";
            }
            long d = System.currentTimeMillis() - ms;
            if (d < 60_000L) {
                return "刚刚";
            }
            if (d < 3600_000L) {
                return (d / 60_000L) + " 分钟前";
            }
            if (d < 86400_000L) {
                return (d / 3600_000L) + " 小时前";
            }
            return (d / 86400_000L) + " 天前";
        } catch (Throwable ignored) {
            return "";
        }
    }

    private static String safe(String s) {
        return s == null ? "" : s.trim();
    }

    private static int dp(Context c, float v) {
        return (int) (c.getResources().getDisplayMetrics().density * v + 0.5f);
    }

    private static void toast(Context c, String s) {
        try {
            android.widget.Toast.makeText(c, s, android.widget.Toast.LENGTH_SHORT).show();
        } catch (Throwable ignored) {}
    }
}
