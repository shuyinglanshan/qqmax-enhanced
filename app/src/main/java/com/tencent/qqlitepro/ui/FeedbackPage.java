package com.tencent.qqlitepro.ui;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.widget.EditText;
import android.widget.FrameLayout;
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
            /*
             * 与 ui/NoticePage 保持完全一致的实现方式（已验证可用）：
             * 标准 Dialog + 铺满 + 不透明背景，不做任何窗口 Flag 折腾。
             * 之前加过 FLAG_LAYOUT_NO_LIMITS / 缩窗 / decorView 覆盖层，
             * 都会在这类定制 ROM 上留下整屏灰层，已全部移除。
             */
            final android.app.Dialog dialog = new android.app.Dialog(ctx);
            dialog.requestWindowFeature(1);
            dialog.setCanceledOnTouchOutside(true);

            final LinearLayout root = new LinearLayout(ctx);
            root.setOrientation(1);
            root.setBackgroundColor(0xFF111316);
            int pad = dp(ctx, 14);
            root.setPadding(pad, dp(ctx, 10), pad, dp(ctx, 14));

            /* 返回键直接关闭（不再做特殊处理） */
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

            /* ---- 极验人机验证（插在崩溃日志与提交按钮之间） ---- */
            final LinearLayout capCard = card(ctx);
            capCard.addView(label(ctx, "人机验证"), lp());

            final TextView capHint = new TextView(ctx);
            capHint.setText("点击下方按钮完成验证");
            capHint.setTextSize(12f);
            capHint.setTextColor(COLOR_SUB);
            capHint.setSingleLine(true);
            capHint.setEllipsize(android.text.TextUtils.TruncateAt.END);
            capHint.setPadding(0, 0, 0, dp(ctx, 6));
            capCard.addView(capHint, lp());

            /* 极验 WebView 容器（自适应：无感/点击/滑块都由 SDK 决定） */
            final android.widget.FrameLayout gtBox = new android.widget.FrameLayout(ctx);
            GradientDrawable gbg = new GradientDrawable();
            gbg.setColor(-14935012);
            gbg.setCornerRadius(dp(ctx, 8));
            gtBox.setBackground(gbg);
            gtBox.setVisibility(android.view.View.GONE);
            LinearLayout.LayoutParams glp = new LinearLayout.LayoutParams(-1, 0);
            glp.height = dp(ctx, 260);
            capCard.addView(gtBox, glp);

            final TextView capStatus = new TextView(ctx);
            capStatus.setTextSize(11.5f);
            capStatus.setTextColor(COLOR_HINT);
            capStatus.setSingleLine(false);
            capStatus.setMaxLines(2);
            capStatus.setPadding(0, dp(ctx, 5), 0, 0);
            capCard.addView(capStatus, lp());

            final TextView capVerifyBtn = new TextView(ctx);
            capVerifyBtn.setText("开始验证");
            capVerifyBtn.setTextSize(13f);
            capVerifyBtn.setTextColor(-1);
            capVerifyBtn.setGravity(17);
            capVerifyBtn.setPadding(0, dp(ctx, 11), 0, dp(ctx, 11));
            GradientDrawable vbg = new GradientDrawable();
            vbg.setColor(COLOR_ACCENT);
            vbg.setCornerRadius(dp(ctx, 9));
            capVerifyBtn.setBackground(vbg);
            LinearLayout.LayoutParams vrlp = lp();
            vrlp.topMargin = dp(ctx, 8);
            capCard.addView(capVerifyBtn, vrlp);

            body.addView(capCard, top(ctx, 10));

            /* ---- 极验状态 ---- */
            final FeedbackApi.GtConfig[] gtCfg = {null};
            final GeetestBridge[] gtBridge = {null};
            final FeedbackApi.GtResult[] gtResult = {null};
            final boolean[] gtInited = {false};

            /* 异步拉配置 + 初始化 WebView */
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final FeedbackApi.GtConfig c = FeedbackApi.fetchGtConfig();
                    new android.os.Handler(android.os.Looper.getMainLooper())
                            .post(new Runnable() {
                                @Override
                                public void run() {
                                    if (c == null) {
                                        capStatus.setText("验证服务不可用，请稍后重试");
                                        capStatus.setTextColor(-1739917);
                                        capVerifyBtn.setEnabled(false);
                                        capVerifyBtn.setAlpha(0.5f);
                                        return;
                                    }
                                    gtCfg[0] = c;
                                    gtBridge[0] = new GeetestBridge(ctx, c);
                                    gtBridge[0].init(new Runnable() {
                                        @Override
                                        public void run() {
                                            gtInited[0] = true;
                                            capStatus.setText("");
                                            /* 把 WebView 挂进容器 */
                                            android.view.View v = gtBridge[0].view();
                                            FrameLayout.LayoutParams wlp =
                                                    new FrameLayout.LayoutParams(-1, -1);
                                            android.view.ViewParent vp = v.getParent();
                                            if (vp instanceof android.view.ViewGroup) {
                                                ((android.view.ViewGroup) vp).removeView(v);
                                            }
                                            gtBox.removeAllViews();
                                            gtBox.addView(v, wlp);
                                        }
                                    }, new Runnable() {
                                        @Override
                                        public void run() {
                                            capStatus.setText("验证组件加载失败");
                                            capStatus.setTextColor(-1739917);
                                        }
                                    });
                                }
                            });
                }
            }, "fb-gt-config").start();

            capVerifyBtn.setOnClickListener(new android.view.View.OnClickListener() {
                @Override
                public void onClick(android.view.View v) {
                    if (gtBridge[0] == null || !gtInited[0]) {
                        toast(ctx, "验证组件尚未就绪");
                        return;
                    }
                    gtResult[0] = null;
                    gtBox.setVisibility(android.view.View.VISIBLE);
                    capStatus.setText("请完成验证…");
                    capStatus.setTextColor(COLOR_HINT);
                    gtBridge[0].verify(new GeetestBridge.Callback() {
                        @Override
                        public void onSuccess(FeedbackApi.GtResult r) {
                            gtResult[0] = r;
                            capStatus.setText("验证已通过");
                            capStatus.setTextColor(-11751600);
                            capHint.setText("已完成验证");
                        }

                        @Override
                        public void onError(String reason) {
                            gtResult[0] = null;
                            capStatus.setText("验证未通过，请重新验证");
                            capStatus.setTextColor(-1739917);
                            /* lot_number 一次性：必须重置 SDK */
                            try {
                                gtBridge[0].reset();
                            } catch (Throwable ignored) {
                            }
                        }

                        @Override
                        public void onShow() {
                            gtBox.setVisibility(android.view.View.VISIBLE);
                        }
                    });
                }
            });
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
            if (dialog.getWindow() != null) {
                android.view.WindowManager.LayoutParams attrs =
                        dialog.getWindow().getAttributes();
                attrs.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
                attrs.height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
                dialog.getWindow().setAttributes(attrs);
                dialog.getWindow().setBackgroundDrawable(
                        new android.graphics.drawable.ColorDrawable(0xFF111316));
            }
            dialog.show();

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
                    final FeedbackApi.GtResult cap = gtResult[0];
                    if (cap == null) {
                        resultTv.setTextColor(-1739917);
                        resultTv.setText("请先拖动滑块完成验证");
                        return;
                    }
                    if (!cap.valid()) {
                        resultTv.setTextColor(-1739917);
                        resultTv.setText("请先完成人机验证");
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
                                                 * 提交成功：lot_number 一次性，
                                                 * 下一次提交必须重新完成极验。
                                                 */
                                                gtResult[0] = null;
                                                capHint.setText("点击下方按钮完成验证");
                                                capStatus.setText("");
                                                try {
                                                    if (gtBridge[0] != null) {
                                                        gtBridge[0].reset();
                                                    }
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
                                                        || r.error.contains("极验")
                                                        || r.error.contains("失效")
                                                        || r.error.contains("过期"));
                                                if (captchaIssue) {
                                                    capStatus.setText("验证未通过，请重新验证");
                                                    capStatus.setTextColor(-1739917);
                                                }
                                                /* 任何失败都清空结果 + 重置 SDK（lot_number 一次性） */
                                                gtResult[0] = null;
                                                try {
                                                    if (gtBridge[0] != null) {
                                                        gtBridge[0].reset();
                                                    }
                                                } catch (Throwable ignored) {}
                                                if (captchaIssue) {
                                                    toast(ctx, "验证未通过，请重新验证");
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

    /**
     * 把服务端下发的 SVG 渲染到 FrameLayout 并监听点击。
     *
     * 服务端返回的是 data:image/svg+xml;base64,... 的 SVG 文本，
     * Android 原生不支持 SVG，这里用一个轻量解析器把它画成 Bitmap：
     * 只处理本验证图用到的子集（rect / circle / polygon / path / 纯色填充）。
     */


    /** 最近一次验证图显示时刻（用于计算 captchaMs） */


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
