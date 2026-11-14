package com.tencent.qqlitepro.ui;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.tencent.qqlitepro.http.QqKernel;
import com.tencent.qqlitepro.net.FeedbackApi;
import com.tencent.qqlitepro.util.CrashLog;
import com.tencent.qqlitepro.util.Log;

/**
 * 反馈提交全屏页。
 *
 * 功能：
 *  - 类型下拉（从 /api/feedback/types 拉取，不硬编码）
 *  - 内容输入（≤2000 字）
 *  - 联系方式（可选）
 *  - 「上传最近崩溃日志」勾选（自动检测是否已上传 / 防止连点）
 *  - 提交（有日志走 multipart，无日志走 JSON）
 *  - 说明「填写 QQ 可收到处理结果通知」
 */
public final class FeedbackPage {

    private static final int COLOR_BG = -15657960;
    private static final int COLOR_CARD = -14013129;
    private static final int COLOR_TITLE = -1;
    private static final int COLOR_SUB = -6709078;
    private static final int COLOR_ACCENT = -16737793;
    private static final int COLOR_HINT = -8355712;

    /** 本次进程内是否已成功上传过日志（避免重复上传同一个崩溃日志） */
    private static volatile boolean sLogUploadedOnce = false;
    /** 防止连点 */
    private static volatile boolean sSubmitting = false;

    private FeedbackPage() {
    }

    public static void show(final Context ctx) {
        try {
            final android.app.Dialog dialog = new android.app.Dialog(ctx);
            dialog.requestWindowFeature(1);
            dialog.setCanceledOnTouchOutside(true);
            /*
             * 修复"按返回键变灰屏"：
             * 返回键关闭 Dialog 后，若窗口背景是半透明/未铺满，会露出灰色底层。
             * 这里禁止返回键直接关闭（改为走我们自己的 close，先还原背景），
             * 同时把窗口背景设为不透明深色，避免任何间隙露出灰底。
             */
            dialog.setOnKeyListener(new android.content.DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(android.content.DialogInterface d, int keyCode,
                                     android.view.KeyEvent event) {
                    if (keyCode == android.view.KeyEvent.KEYCODE_BACK
                            && event.getAction() == android.view.KeyEvent.ACTION_UP) {
                        try {
                            d.dismiss();
                        } catch (Throwable ignored) {
                        }
                        return true;
                    }
                    return false;
                }
            });

            final LinearLayout root = new LinearLayout(ctx);
            root.setOrientation(1);
            root.setBackgroundColor(0xFF111316);
            int pad = dp(ctx, 14);
            root.setPadding(pad, dp(ctx, 10), pad, dp(ctx, 14));

            /* ---- 标题栏 ---- */
            LinearLayout header = new LinearLayout(ctx);
            header.setOrientation(0);
            header.setGravity(16);
            TextView title = new TextView(ctx);
            title.setText("问题反馈");
            title.setTextSize(18f);
            title.setTextColor(COLOR_TITLE);
            header.addView(title, new LinearLayout.LayoutParams(0, -2, 1f));
            TextView close = new TextView(ctx);
            close.setText("关闭");
            close.setTextSize(13f);
            close.setTextColor(COLOR_ACCENT);
            close.setPadding(dp(ctx, 10), dp(ctx, 4), 0, dp(ctx, 4));
            close.setOnClickListener(new android.view.View.OnClickListener() {
                @Override
                public void onClick(android.view.View v) {
                    try { dialog.dismiss(); } catch (Throwable ignored) {}
                }
            });
            header.addView(close);
            root.addView(header);

            ScrollView sv = new ScrollView(ctx);
            LinearLayout body = new LinearLayout(ctx);
            body.setOrientation(1);
            sv.addView(body);
            root.addView(sv, new LinearLayout.LayoutParams(-1, 0, 1f));

            final java.util.List<FeedbackApi.FbType>[] typesRef = new java.util.List[]{null};
            final int[] selIdx = {0};
            final String[] selValue = {null};

            /* ---- 提示 ---- */
            TextView tip = new TextView(ctx);
            tip.setText("填写 QQ 号可收到处理结果通知");
            tip.setTextSize(12f);
            tip.setTextColor(COLOR_HINT);
            tip.setPadding(0, dp(ctx, 4), 0, dp(ctx, 10));
            body.addView(tip);

            /* ---- QQ / 昵称（只读展示） ---- */
            final String qq = safe(QqKernel.selfUin());
            String nickTmp = safe(QqKernel.capturedNick());
            if (nickTmp.isEmpty()) nickTmp = safe(QqKernel.selfNick());
            final String nick = nickTmp;
            LinearLayout whoCard = card(ctx);
            whoCard.addView(label(ctx, "反馈者"), lp());
            TextView whoTv = new TextView(ctx);
            whoTv.setText((nick.isEmpty() ? "未知" : nick) + "（" + (qq.isEmpty() ? "未登录" : qq) + "）");
            whoTv.setTextSize(13.5f);
            whoTv.setTextColor(COLOR_TITLE);
            whoCard.addView(whoTv, lp());
            body.addView(whoCard, top(ctx, 0));

            /* ---- 类型下拉 ---- */
            final LinearLayout typeCard = card(ctx);
            typeCard.addView(label(ctx, "反馈类型"), lp());
            final TextView typeTv = new TextView(ctx);
            typeTv.setText("加载中…");
            typeTv.setTextSize(13.5f);
            typeTv.setTextColor(COLOR_ACCENT);
            typeTv.setPadding(dp(ctx, 10), dp(ctx, 9), dp(ctx, 10), dp(ctx, 9));
            GradientDrawable tbg = new GradientDrawable();
            tbg.setColor(-14935012);
            tbg.setCornerRadius(dp(ctx, 8));
            typeTv.setBackground(tbg);
            typeCard.addView(typeTv, lp());
            body.addView(typeCard, top(ctx, 10));

            /* ---- 内容 ---- */
            LinearLayout contentCard = card(ctx);
            contentCard.addView(label(ctx, "问题描述（最多 2000 字）"), lp());
            final EditText etContent = new EditText(ctx);
            etContent.setHint("请描述遇到的问题，越详细越好");
            etContent.setTextSize(13.5f);
            etContent.setTextColor(COLOR_TITLE);
            etContent.setHintTextColor(COLOR_HINT);
            etContent.setBackgroundColor(-14935012);
            etContent.setPadding(dp(ctx, 10), dp(ctx, 9), dp(ctx, 10), dp(ctx, 9));
            etContent.setMinLines(4);
            etContent.setGravity(48);
            contentCard.addView(etContent, lp());
            body.addView(contentCard, top(ctx, 10));

            /* ---- 联系方式 ---- */
            LinearLayout contactCard = card(ctx);
            contactCard.addView(label(ctx, "联系方式（可选）"), lp());
            final EditText etContact = new EditText(ctx);
            etContact.setHint("邮箱 / 其他 QQ 等");
            etContact.setTextSize(13.5f);
            etContact.setTextColor(COLOR_TITLE);
            etContact.setHintTextColor(COLOR_HINT);
            etContact.setBackgroundColor(-14935012);
            etContact.setPadding(dp(ctx, 10), dp(ctx, 9), dp(ctx, 10), dp(ctx, 9));
            etContact.setSingleLine(true);
            contactCard.addView(etContact, lp());
            body.addView(contactCard, top(ctx, 10));

            /* ---- 崩溃日志：仅「功能异常」显示，单选，默认最近的 ---- */
            final LinearLayout logCard = card(ctx);
            logCard.addView(label(ctx, "崩溃日志（仅功能异常可附）"), lp());
            final java.util.List<java.io.File> crashFiles = CrashLog.listFiles();
            final int[] selLog = {0};   /* 选中的日志下标；始终单选 */
            final TextView[] logRows = new TextView[0];
            final java.util.List<TextView> rowList = new java.util.ArrayList<>();

            if (crashFiles == null || crashFiles.isEmpty()) {
                TextView none = new TextView(ctx);
                none.setText("（未检测到崩溃日志）");
                none.setTextSize(12.5f);
                none.setTextColor(COLOR_HINT);
                none.setPadding(dp(ctx, 8), dp(ctx, 6), 0, dp(ctx, 6));
                logCard.addView(none, lp());
            } else {
                for (int i = 0; i < crashFiles.size(); i++) {
                    final int idx = i;
                    java.io.File f = crashFiles.get(i);
                    TextView row = new TextView(ctx);
                    row.setTextSize(12.5f);
                    row.setPadding(dp(ctx, 10), dp(ctx, 8), dp(ctx, 10), dp(ctx, 8));
                    row.setTag("qqmax_log_row_" + i);
                    row.setOnClickListener(new android.view.View.OnClickListener() {
                        @Override
                        public void onClick(android.view.View v) {
                            selLog[0] = idx;
                            for (int k = 0; k < rowList.size(); k++) {
                                styleLogRow(ctx, rowList.get(k),
                                        String.valueOf(crashFiles.get(k).getName()),
                                        crashFiles.get(k).length(), k == idx);
                            }
                        }
                    });
                    rowList.add(row);
                    LinearLayout.LayoutParams rlp = new LinearLayout.LayoutParams(-1, -2);
                    if (i > 0) {
                        rlp.topMargin = dp(ctx, 5);
                    }
                    logCard.addView(row, rlp);
                }
                /* 默认勾选最近的（listFiles 已按时间倒序） */
                selLog[0] = 0;
                for (int k = 0; k < rowList.size(); k++) {
                    styleLogRow(ctx, rowList.get(k),
                            String.valueOf(crashFiles.get(k).getName()),
                            crashFiles.get(k).length(), k == 0);
                }
            }
            body.addView(logCard, top(ctx, 10));

            /* 类型变化时显示/隐藏日志卡片 */
            final Runnable syncLogVisibility = new Runnable() {
                @Override
                public void run() {
                    boolean isBug = "bug".equals(selValue[0]);
                    logCard.setVisibility(isBug
                            ? android.view.View.VISIBLE : android.view.View.GONE);
                }
            };

            /* ---- 人机验证（插在崩溃日志与提交按钮之间） ---- */
            final LinearLayout capCard = card(ctx);
            capCard.addView(label(ctx, "人机验证"), lp());

            final TextView capHint = new TextView(ctx);
            capHint.setTextSize(12.5f);
            capHint.setTextColor(COLOR_SUB);
            capHint.setPadding(0, 0, 0, dp(ctx, 6));
            capHint.setSingleLine(false);
            capHint.setMaxLines(2);
            capCard.addView(capHint, lp());

            /*
             * 用于承载 SVG 的容器。
             * 注意：宽必须 MATCH_PARENT、高固定，否则在部分 ROM 上
             * LinearLayout 会把子 View 压成 0 宽（文字被挤成竖排、溢出到左侧）。
             * 同时 clipChildren 保证内部任何绘制都不越界。
             */
            final android.widget.FrameLayout capBox = new android.widget.FrameLayout(ctx);
            GradientDrawable cabg = new GradientDrawable();
            cabg.setColor(-14935012);
            cabg.setCornerRadius(dp(ctx, 8));
            capBox.setBackground(cabg);
            capBox.setClipChildren(true);
            capBox.setClipToPadding(true);
            LinearLayout.LayoutParams capLp = new LinearLayout.LayoutParams(
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT, dp(ctx, 150));
            capCard.addView(capBox, capLp);

            final TextView capStatus = new TextView(ctx);
            capStatus.setTextSize(11.5f);
            capStatus.setTextColor(COLOR_HINT);
            capStatus.setPadding(0, dp(ctx, 5), 0, 0);
            capStatus.setSingleLine(false);
            /* 允许换行并按宽度折行，避免被挤成竖排 */
            capStatus.setMaxLines(3);
            capCard.addView(capStatus, lp());

            final TextView capRefresh = new TextView(ctx);
            capRefresh.setText("刷新验证");
            capRefresh.setTextSize(12.5f);
            capRefresh.setTextColor(COLOR_ACCENT);
            capRefresh.setGravity(17);
            capRefresh.setPadding(0, dp(ctx, 9), 0, dp(ctx, 9));
            GradientDrawable crbg = new GradientDrawable();
            crbg.setColor(-14935012);
            crbg.setCornerRadius(dp(ctx, 8));
            capRefresh.setBackground(crbg);
            LinearLayout.LayoutParams crlp = lp();
            crlp.topMargin = dp(ctx, 8);
            capCard.addView(capRefresh, crlp);

            body.addView(capCard, top(ctx, 10));

            /* 验证状态 */
            final FeedbackApi.Captcha[] capCur = {null};
            final FeedbackApi.CaptchaPayload[] capPayload = {null};
            final long[] capShownAt = {0L};
            final android.view.View[] capSvgView = {null};

            final Runnable loadCaptcha = new Runnable() {
                @Override
                public void run() {
                    capCur[0] = null;
                    capPayload[0] = null;
                    capShownAt[0] = 0L;
                    capStatus.setText("加载验证中…");
                    capStatus.setTextColor(COLOR_HINT);
                    capBox.removeAllViews();
                    /* 重置图框边框（取消已选中样式） */
                    GradientDrawable idle = new GradientDrawable();
                    idle.setColor(-14935012);
                    idle.setCornerRadius(dp(ctx, 8));
                    capBox.setBackground(idle);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            final FeedbackApi.Captcha c = FeedbackApi.fetchCaptcha();
                            new android.os.Handler(android.os.Looper.getMainLooper())
                                    .post(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (c == null) {
                                                capStatus.setText("验证加载失败，点击「刷新验证」重试");
                                                capStatus.setTextColor(-1739917);
                                                return;
                                            }
                                            capCur[0] = c;
                                            capHint.setText(c.hint == null || c.hint.isEmpty()
                                                    ? "请点击图中的星形图案" : c.hint);
                                            capStatus.setText("请点击上图中的星形");
                                            capStatus.setTextColor(COLOR_HINT);
                                            capShownAt[0] = 0L;   /* 重置，等图片真正显示后再记 */
                                            captchaShownCallback = new CaptchaShownCallback() {
                                                @Override
                                                public void onShown(long atMs) {
                                                    capShownAt[0] = atMs;
                                                }
                                            };
                                            renderCaptchaSvg(ctx, capBox, c, capSvgView,
                                                    new CaptchaTapListener() {
                                                        @Override
                                                        public void onTap(float px, float py,
                                                                          float w, float h) {
                                                            if (capCur[0] == null || w <= 0 || h <= 0) {
                                                                return;
                                                            }
                                                            /* 换算成百分比（必须用显示尺寸） */
                                                            double x = px * 100.0 / w;
                                                            double y = py * 100.0 / h;
                                                            /* 计时起点必须是"图片显示完成"时刻 */
                                                            long shown = capShownAt[0] > 0
                                                                    ? capShownAt[0] : sLastShownAt;
                                                            long ms = shown <= 0 ? 0
                                                                    : System.currentTimeMillis() - shown;
                                                            FeedbackApi.CaptchaPayload p =
                                                                    new FeedbackApi.CaptchaPayload();
                                                            p.captchaId = capCur[0].captchaId;
                                                            p.captchaSig = capCur[0].signature;
                                                            p.captchaX = Math.round(x * 100) / 100.0;
                                                            p.captchaY = Math.round(y * 100) / 100.0;
                                                            p.captchaMs = ms;
                                                            capPayload[0] = p;
                                                            /*
                                                             * 视觉反馈：改为图框描边高亮（不再画圆点）。
                                                             * 之前的小圆点会溢出到容器边界外，看起来像
                                                             * 左上角有一块"没显示全的蓝色东西"。
                                                             */
                                                            GradientDrawable picked = new GradientDrawable();
                                                            picked.setColor(-14935012);
                                                            picked.setCornerRadius(dp(ctx, 8));
                                                            picked.setStroke(dp(ctx, 1.5f),
                                                                    ms < 300L ? -1739917 : COLOR_ACCENT);
                                                            capBox.setBackground(picked);
                                                            if (ms < 300L) {
                                                                capStatus.setText("点击太快，请稍候再点（需 ≥ 0.3 秒）");
                                                                capStatus.setTextColor(-1739917);
                                                            } else {
                                                                capStatus.setText("已选择位置，可提交");
                                                                capStatus.setTextColor(-11751600);
                                                            }
                                                        }
                                                    });
                                        }
                                    });
                        }
                    }, "fb-captcha").start();
                }
            };

            capRefresh.setOnClickListener(new android.view.View.OnClickListener() {
                @Override
                public void onClick(android.view.View v) {
                    loadCaptcha.run();
                }
            });
            loadCaptcha.run();

            /* ---- 提交按钮 ---- */
            final TextView submit = new TextView(ctx);
            submit.setText("提交反馈");
            submit.setTextSize(15f);
            submit.setTextColor(-1);
            submit.setGravity(17);
            submit.setPadding(0, dp(ctx, 13), 0, dp(ctx, 13));
            GradientDrawable sbg = new GradientDrawable();
            sbg.setColor(-15620000);
            sbg.setCornerRadius(dp(ctx, 23));
            submit.setBackground(sbg);
            body.addView(submit, top(ctx, 16));

            /* 结果提示 */
            final TextView resultTv = new TextView(ctx);
            resultTv.setTextSize(12.5f);
            resultTv.setTextColor(COLOR_HINT);
            resultTv.setPadding(0, dp(ctx, 10), 0, 0);
            body.addView(resultTv, lp());

            dialog.setContentView(root);
            /*
             * 窗口背景必须"先设再 show"，但如果只设背景不设 FLAG，
             * 部分 ROM 会在 dismiss 动画期间露出窗口底色（=灰屏）。
             * 这里同时：① 不透明背景 ② 去掉变暗 ③ 关闭窗口动画
             * ④ show 之后再强制重设一次尺寸（防止首次布局用了默认 wrap）。
             */
            applyFullscreenWindow(dialog);
            dialog.show();
            /* show 之后再校正一次（首次 getWindow 布局尚未完成） */
            applyFullscreenWindow(dialog);

            /* ---- 异步加载类型 ---- */
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final java.util.List<FeedbackApi.FbType> types = FeedbackApi.getTypes();
                    new android.os.Handler(android.os.Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            typesRef[0] = types;
                            if (types == null || types.isEmpty()) {
                                typeTv.setText("类型加载失败，点击重试");
                                typeTv.setTextColor(-1739917);
                                return;
                            }
                            selValue[0] = types.get(0).value;
                            typeTv.setText(types.get(0).label + "  ▾");
                            syncLogVisibility.run();
                        }
                    });
                }
            }, "fb-types").start();

            typeTv.setOnClickListener(new android.view.View.OnClickListener() {
                @Override
                public void onClick(android.view.View v) {
                    final java.util.List<FeedbackApi.FbType> types = typesRef[0];
                    if (types == null || types.isEmpty()) {
                        toast(ctx, "类型未加载");
                        return;
                    }
                    /* 自建卡片选择器（不用系统 AlertDialog，与模块风格一致） */
                    final android.app.Dialog pick = new android.app.Dialog(ctx);
                    pick.requestWindowFeature(1);
                    LinearLayout box = new LinearLayout(ctx);
                    box.setOrientation(1);
                    GradientDrawable pbg = new GradientDrawable();
                    pbg.setColor(COLOR_CARD);
                    pbg.setCornerRadius(dp(ctx, 14));
                    box.setBackground(pbg);
                    int pp = dp(ctx, 14);
                    box.setPadding(pp, dp(ctx, 12), pp, dp(ctx, 12));

                    TextView head = new TextView(ctx);
                    head.setText("选择反馈类型");
                    head.setTextSize(15f);
                    head.setTextColor(COLOR_TITLE);
                    head.setGravity(17);
                    head.setPadding(0, 0, 0, dp(ctx, 10));
                    box.addView(head);

                    for (int i = 0; i < types.size(); i++) {
                        final int which = i;
                        final FeedbackApi.FbType t = types.get(i);
                        TextView opt = new TextView(ctx);
                        opt.setTextSize(13.5f);
                        opt.setGravity(17);
                        opt.setPadding(dp(ctx, 10), dp(ctx, 11), dp(ctx, 10), dp(ctx, 11));
                        boolean on = (which == selIdx[0]);
                        GradientDrawable obg = new GradientDrawable();
                        obg.setColor(on ? -15000558 : -14935012);
                        obg.setCornerRadius(dp(ctx, 9));
                        if (on) {
                            obg.setStroke(dp(ctx, 1.2f), COLOR_ACCENT);
                        }
                        opt.setBackground(obg);
                        opt.setTextColor(on ? COLOR_ACCENT : COLOR_TITLE);
                        opt.setText((on ? "● " : "○ ") + t.label);
                        opt.setClickable(true);
                        opt.setOnClickListener(new android.view.View.OnClickListener() {
                            @Override
                            public void onClick(android.view.View v) {
                                selIdx[0] = which;
                                selValue[0] = t.value;
                                typeTv.setText(t.label + "  ▾");
                                syncLogVisibility.run();
                                try { pick.dismiss(); } catch (Throwable ignored) {}
                            }
                        });
                        LinearLayout.LayoutParams olp =
                                new LinearLayout.LayoutParams(-1, -2);
                        if (i > 0) {
                            olp.topMargin = dp(ctx, 6);
                        }
                        box.addView(opt, olp);
                    }

                    TextView cancel = new TextView(ctx);
                    cancel.setText("取消");
                    cancel.setTextSize(13f);
                    cancel.setTextColor(COLOR_SUB);
                    cancel.setGravity(17);
                    cancel.setPadding(0, dp(ctx, 11), 0, dp(ctx, 6));
                    cancel.setOnClickListener(new android.view.View.OnClickListener() {
                        @Override
                        public void onClick(android.view.View v) {
                            try { pick.dismiss(); } catch (Throwable ignored) {}
                        }
                    });
                    box.addView(cancel);

                    /* 返回键关闭选择器（不穿透到下层） */
                    pick.setOnKeyListener(new android.content.DialogInterface.OnKeyListener() {
                        @Override
                        public boolean onKey(android.content.DialogInterface d, int keyCode,
                                             android.view.KeyEvent event) {
                            if (keyCode == android.view.KeyEvent.KEYCODE_BACK
                                    && event.getAction() == android.view.KeyEvent.ACTION_UP) {
                                try { d.dismiss(); } catch (Throwable ignored) {}
                                return true;
                            }
                            return false;
                        }
                    });
                    pick.setCanceledOnTouchOutside(true);
                    pick.setContentView(box);
                    if (pick.getWindow() != null) {
                        pick.getWindow().setBackgroundDrawable(
                                new android.graphics.drawable.ColorDrawable(0x00000000));
                        pick.getWindow().setDimAmount(0.6f);
                    }
                    pick.show();
                }
            });

            /* ---- 提交 ---- */
            submit.setOnClickListener(new android.view.View.OnClickListener() {
                @Override
                public void onClick(android.view.View v) {
                    if (sSubmitting) {
                        toast(ctx, "正在提交，请稍候");
                        return;
                    }
                    final String content = etContent.getText().toString().trim();
                    final String contact = etContact.getText().toString().trim();
                    final String type = selValue[0];
                    /* 仅「功能异常」且已选日志时才带文件 */
                    final boolean wantLog = "bug".equals(type)
                            && crashFiles != null && !crashFiles.isEmpty()
                            && selLog[0] >= 0 && selLog[0] < crashFiles.size();

                    if (type == null || type.isEmpty()) {
                        resultTv.setTextColor(-1739917);
                        resultTv.setText("请先选择反馈类型");
                        return;
                    }
                    if (content.isEmpty() && !wantLog) {
                        resultTv.setTextColor(-1739917);
                        resultTv.setText("请填写问题描述，或勾选上传崩溃日志");
                        return;
                    }
                    if (content.length() > 2000) {
                        resultTv.setTextColor(-1739917);
                        resultTv.setText("描述超过 2000 字，请精简");
                        return;
                    }
                    /* 必须完成人机验证才能提交 */
                    final FeedbackApi.CaptchaPayload cap = capPayload[0];
                    if (cap == null) {
                        resultTv.setTextColor(-1739917);
                        resultTv.setText("请先点击上方验证图中的星形");
                        return;
                    }
                    if (cap.captchaMs < 300L) {
                        resultTv.setTextColor(-1739917);
                        resultTv.setText("点击太快，请稍候重新点击验证图");
                        return;
                    }

                    sSubmitting = true;
                    submit.setText("提交中…");
                    submit.setAlpha(0.6f);
                    resultTv.setTextColor(COLOR_HINT);
                    resultTv.setText("正在提交…");

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            byte[] logBytes = null;
                            if (wantLog) {
                                final java.io.File pick =
                                        crashFiles.get(selLog[0]);
                                java.util.ArrayList<java.io.File> one =
                                        new java.util.ArrayList<>();
                                one.add(pick);
                                logBytes = readLogBytes(one);
                            }
                            final FeedbackApi.SubmitResult r = FeedbackApi.submit(
                                    safe(QqKernel.selfUin()),
                                    nick,
                                    type,
                                    content,
                                    contact,
                                    safe(com.tencent.qqlitepro.BuildConfig.VERSION_NAME),
                                    logBytes,
                                    cap);
                            final boolean usedLog = logBytes != null && logBytes.length > 0;
                            new android.os.Handler(android.os.Looper.getMainLooper())
                                    .post(new Runnable() {
                                        @Override
                                        public void run() {
                                            sSubmitting = false;
                                            submit.setText("提交反馈");
                                            submit.setAlpha(1f);
                                            if (r.ok) {
                                                if (usedLog) {
                                                    sLogUploadedOnce = true;
                                                }
                                                resultTv.setTextColor(-11751600);
                                                resultTv.setText("反馈已提交，编号 "
                                                        + (r.id == null ? "-" : r.id));
                                                etContent.setText("");
                                                /* 重置为默认选中最近一个 */
                                                selLog[0] = 0;
                                                for (int k = 0; k < rowList.size(); k++) {
                                                    styleLogRow(ctx, rowList.get(k),
                                                            String.valueOf(crashFiles.get(k).getName()),
                                                            crashFiles.get(k).length(), k == 0);
                                                }
                                                toast(ctx, "反馈已提交，感谢反馈");
                                                /*
                                                 * 提交成功后也必须换新挑战：
                                                 * 挑战是一次性的，旧 challenge 已被服务端消费。
                                                 */
                                                try {
                                                    loadCaptcha.run();
                                                } catch (Throwable ignored) {}
                                            } else {
                                                resultTv.setTextColor(-1739917);
                                                resultTv.setText(r.error == null
                                                        ? "提交失败" : r.error);
                                                /*
                                                 * 人机验证失败 → 立即自动刷新验证图，并给出明确提示。
                                                 * 服务端挑战为一次性，任何失败都不能重试同一个。
                                                 */
                                                boolean captchaIssue = r.error != null
                                                        && (r.error.contains("验证")
                                                        || r.error.contains("星形")
                                                        || r.error.contains("点击太快")
                                                        || r.error.contains("过期"));
                                                if (captchaIssue) {
                                                    capStatus.setText("验证已失效，正在刷新…");
                                                    capStatus.setTextColor(-1739917);
                                                }
                                                try {
                                                    loadCaptcha.run();
                                                } catch (Throwable ignored) {}
                                                if (captchaIssue) {
                                                    toast(ctx, "验证未通过，已刷新验证图，请重新点击星形");
                                                }
                                            }
                                        }
                                    });
                        }
                    }, "fb-submit").start();
                }
            });
        } catch (Throwable t) {
            Log.w("反馈页打开失败: " + t);
        }
    }

    /* ===================== 工具 ===================== */

    /** 读取崩溃日志为 UTF-8 纯文本字节（服务端要求） */
    private static byte[] readLogBytes(java.util.List<java.io.File> files) {
        try {
            if (files == null || files.isEmpty()) {
                return null;
            }
            java.io.File f = files.get(0);
            if (f == null || !f.exists()) {
                return null;
            }
            long len = f.length();
            if (len <= 0) {
                return null;
            }
            /* 超过 2MB 只取尾部（保留最近的崩溃，通常最有价值） */
            int max = FeedbackApi.MAX_LOG_BYTES;
            java.io.RandomAccessFile raf = new java.io.RandomAccessFile(f, "r");
            long start = len > max ? len - max : 0;
            raf.seek(start);
            byte[] buf = new byte[(int) Math.min(len - start, max)];
            raf.readFully(buf);
            raf.close();
            /* 确保是合法 UTF-8 纯文本：含 NUL 或解码失败则放弃 */
            for (byte b : buf) {
                if (b == 0) {
                    Log.w("反馈: 日志含 NUL 字节，放弃上传");
                    return null;
                }
            }
            try {
                new String(buf, "UTF-8");
            } catch (Throwable t) {
                Log.w("反馈: 日志不是合法 UTF-8，放弃上传");
                return null;
            }
            return buf;
        } catch (Throwable t) {
            Log.w("反馈: 读取日志失败 " + t);
            return null;
        }
    }

    /** 验证图点击回调 */
    private interface CaptchaTapListener {
        void onTap(float px, float py, float w, float h);
    }

    /**
     * 把服务端下发的 SVG 渲染到 FrameLayout 并监听点击。
     *
     * 服务端返回的是 data:image/svg+xml;base64,... 的 SVG 文本，
     * Android 原生不支持 SVG，这里用一个轻量解析器把它画成 Bitmap：
     * 只处理本验证图用到的子集（rect / circle / polygon / path / 纯色填充）。
     */
    private static void renderCaptchaSvg(final Context ctx, final android.widget.FrameLayout box,
                                         final FeedbackApi.Captcha cap,
                                         final android.view.View[] holder,
                                         final CaptchaTapListener listener) {
        renderCaptchaSvg(ctx, box, cap, holder, listener, captchaShownCallback);
    }

    private static void renderCaptchaSvg(final Context ctx, final android.widget.FrameLayout box,
                                         final FeedbackApi.Captcha cap,
                                         final android.view.View[] holder,
                                         final CaptchaTapListener listener,
                                         final CaptchaShownCallback shownCb) {
        try {
            final android.graphics.Bitmap bm = CaptchaSvg.render(
                    FeedbackApi.decodeSvg(cap.image),
                    dp(ctx, 420), dp(ctx, 315));
            if (bm == null) {
                TextView err = new TextView(ctx);
                err.setText("验证图渲染失败，请点「刷新验证」");
                err.setTextSize(12f);
                err.setTextColor(-1739917);
                err.setGravity(17);
                box.addView(err, new android.widget.FrameLayout.LayoutParams(-1, -1));
                return;
            }
            final android.widget.ImageView iv = new android.widget.ImageView(ctx);
            iv.setScaleType(android.widget.ImageView.ScaleType.FIT_CENTER);
            iv.setImageBitmap(bm);
            box.addView(iv, new android.widget.FrameLayout.LayoutParams(-1, -1));
            holder[0] = iv;
            /* 图片就绪后开始计时（必须从显示时刻算，不是请求发出时） */
            /*
             * 计时起点：用 OnPreDrawListener 捕捉"图片即将上屏"的那一刻，
             * 比 post() 更准（post 可能在首帧绘制前就执行，导致 captchaMs 偏大
             * 或用户看到图时时间已被消耗掉）。
             */
            iv.getViewTreeObserver().addOnPreDrawListener(
                    new android.view.ViewTreeObserver.OnPreDrawListener() {
                        @Override
                        public boolean onPreDraw() {
                            try {
                                iv.getViewTreeObserver().removeOnPreDrawListener(this);
                            } catch (Throwable ignored) {
                            }
                            try {
                                sLastShownAt = System.currentTimeMillis();
                                if (shownCb != null) {
                                    shownCb.onShown(sLastShownAt);
                                }
                            } catch (Throwable ignored) {
                            }
                            return true;
                        }
                    });
            iv.setOnClickListener(new android.view.View.OnClickListener() {
                @Override
                public void onClick(android.view.View v) {
                    /* 用显示尺寸换算，不能用 SVG 原始尺寸 */
                    float w = iv.getWidth();
                    float h = iv.getHeight();
                    if (w <= 0 || h <= 0) {
                        return;
                    }
                    listener.onTap(v.getX(), v.getY(), w, h);
                }
            });
            box.setOnClickListener(new android.view.View.OnClickListener() {
                @Override
                public void onClick(android.view.View v) {
                    float w = box.getWidth();
                    float h = box.getHeight();
                    if (w <= 0 || h <= 0) {
                        return;
                    }
                    listener.onTap(v.getX(), v.getY(), w, h);
                }
            });
        } catch (Throwable t) {
            Log.w("验证图渲染异常: " + t);
        }
    }

    /** 最近一次验证图显示时刻（用于计算 captchaMs） */
    private static volatile long sLastShownAt = 0L;

    /** 图片显示完成回调（用于精确开始计时） */
    interface CaptchaShownCallback {
        void onShown(long atMs);
    }

    private static volatile CaptchaShownCallback captchaShownCallback = null;

    /** 渲染单条日志行：选中=蓝框+蓝字，未选=灰底 */
    private static void styleLogRow(Context ctx, TextView row, String name,
                                    long size, boolean selected) {
        try {
            GradientDrawable bg = new GradientDrawable();
            bg.setColor(selected ? -15000558 : -14935012);
            bg.setCornerRadius(dp(ctx, 8));
            if (selected) {
                bg.setStroke(dp(ctx, 1.2f), COLOR_ACCENT);
            }
            row.setBackground(bg);
            row.setTextColor(selected ? COLOR_ACCENT : COLOR_TITLE);
            row.setText((selected ? "● " : "○ ") + name + "（" + humanSize(size) + "）");
        } catch (Throwable ignored) {}
    }

    private static String humanSize(long b) {
        if (b < 1024) return b + "B";
        if (b < 1024 * 1024) return (b / 1024) + "KB";
        return String.format(java.util.Locale.US, "%.1fMB", b / 1048576.0);
    }

    /**
     * 全屏不透明窗口设置。
     * 关键点：
     *  - 背景用不透明色，避免 dismiss 动画期间露出系统灰底
     *  - dimAmount=0 去掉变暗遮罩
     *  - 关闭窗口进出动画（动画期间窗口背景会被系统临时替换成灰）
     */
    private static void applyFullscreenWindow(android.app.Dialog dialog) {
        try {
            android.view.Window w = dialog.getWindow();
            if (w == null) {
                return;
            }
            android.view.WindowManager.LayoutParams attrs = w.getAttributes();
            attrs.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
            attrs.height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
            attrs.dimAmount = 0f;
            w.setAttributes(attrs);
            w.setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(0xFF111316));
            /* 关闭窗口进出动画：动画期间系统会临时用灰底，这是灰屏的真正来源 */
            w.setWindowAnimations(0);
            try {
                w.addFlags(android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            } catch (Throwable ignored) {
            }
        } catch (Throwable ignored) {
        }
    }

    private static String safe(String s) {
        return s == null ? "" : s.trim();
    }

    private static int dp(Context c, float v) {
        return (int) (c.getResources().getDisplayMetrics().density * v + 0.5f);
    }

    private static LinearLayout card(Context c) {
        LinearLayout l = new LinearLayout(c);
        l.setOrientation(1);
        GradientDrawable bg = new GradientDrawable();
        bg.setColor(COLOR_CARD);
        bg.setCornerRadius(dp(c, 12));
        l.setBackground(bg);
        int p = dp(c, 12);
        l.setPadding(p, p, p, p);
        return l;
    }

    private static TextView label(Context c, String s) {
        TextView t = new TextView(c);
        t.setText(s);
        t.setTextSize(12.5f);
        t.setTextColor(COLOR_SUB);
        t.setPadding(0, 0, 0, dp(c, 6));
        return t;
    }

    private static LinearLayout.LayoutParams lp() {
        return new LinearLayout.LayoutParams(-1, -2);
    }

    private static LinearLayout.LayoutParams top(Context c, float v) {
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1, -2);
        p.topMargin = dp(c, v);
        return p;
    }

    private static void toast(Context c, String s) {
        try {
            android.widget.Toast.makeText(c, s, android.widget.Toast.LENGTH_SHORT).show();
        } catch (Throwable ignored) {}
    }
}
