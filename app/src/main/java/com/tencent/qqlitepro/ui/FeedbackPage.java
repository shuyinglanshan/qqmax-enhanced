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

            final LinearLayout root = new LinearLayout(ctx);
            root.setOrientation(1);
            root.setBackgroundColor(COLOR_BG);
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

            /* ---- 崩溃日志勾选 ---- */
            LinearLayout logCard = card(ctx);
            logCard.addView(label(ctx, "崩溃日志"), lp());
            final java.util.List<java.io.File> crashFiles = CrashLog.listFiles();
            final boolean hasCrash = crashFiles != null && !crashFiles.isEmpty();
            final boolean[] uploadLog = new boolean[]{hasCrash && !sLogUploadedOnce};

            final TextView logRow = new TextView(ctx);
            logRow.setTextSize(13f);
            logRow.setPadding(dp(ctx, 10), dp(ctx, 9), dp(ctx, 10), dp(ctx, 9));
            GradientDrawable lbg = new GradientDrawable();
            lbg.setColor(-14935012);
            lbg.setCornerRadius(dp(ctx, 8));
            logRow.setBackground(lbg);
            logCard.addView(logRow, lp());
            body.addView(logCard, top(ctx, 10));

            final Runnable refreshLogRow = new Runnable() {
                @Override
                public void run() {
                    try {
                        if (!hasCrash) {
                            logRow.setText("（未检测到崩溃日志）");
                            logRow.setTextColor(COLOR_HINT);
                            uploadLog[0] = false;
                            return;
                        }
                        if (sLogUploadedOnce) {
                            logRow.setText("log.txt 已上传过，本次不再重复上传");
                            logRow.setTextColor(COLOR_HINT);
                            uploadLog[0] = false;
                            return;
                        }
                        long size = crashFiles.get(0).length();
                        String mark = uploadLog[0] ? "[√] " : "[ ] ";
                        logRow.setText(mark + "上传最近崩溃日志（"
                                + crashFiles.get(0).getName() + "，" + humanSize(size) + "）");
                        logRow.setTextColor(uploadLog[0] ? COLOR_ACCENT : COLOR_TITLE);
                    } catch (Throwable ignored) {}
                }
            };
            refreshLogRow.run();

            logRow.setOnClickListener(new android.view.View.OnClickListener() {
                @Override
                public void onClick(android.view.View v) {
                    if (!hasCrash) {
                        toast(ctx, "未检测到崩溃日志");
                        return;
                    }
                    if (sLogUploadedOnce) {
                        toast(ctx, "本次已上传过日志，请勿重复提交");
                        return;
                    }
                    uploadLog[0] = !uploadLog[0];
                    refreshLogRow.run();
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
                android.view.WindowManager.LayoutParams attrs = dialog.getWindow().getAttributes();
                attrs.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
                attrs.height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
                dialog.getWindow().setAttributes(attrs);
                dialog.getWindow().setBackgroundDrawable(
                        new android.graphics.drawable.ColorDrawable(COLOR_BG));
            }
            dialog.show();

            /* ---- 异步加载类型 ---- */
            final java.util.List<FeedbackApi.FbType>[] typesRef = new java.util.List[]{null};
            final int[] selIdx = {0};
            final String[] selValue = {null};
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
                    String[] labels = new String[types.size()];
                    for (int i = 0; i < types.size(); i++) {
                        labels[i] = types.get(i).label;
                    }
                    new android.app.AlertDialog.Builder(ctx)
                            .setTitle("选择反馈类型")
                            .setSingleChoiceItems(labels, selIdx[0],
                                    new android.content.DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(android.content.DialogInterface d, int which) {
                                            selIdx[0] = which;
                                            selValue[0] = types.get(which).value;
                                            typeTv.setText(types.get(which).label + "  ▾");
                                            try { d.dismiss(); } catch (Throwable ignored) {}
                                        }
                                    })
                            .show();
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
                    final boolean wantLog = uploadLog[0];

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
                                logBytes = readLogBytes(crashFiles);
                            }
                            final FeedbackApi.SubmitResult r = FeedbackApi.submit(
                                    safe(QqKernel.selfUin()),
                                    nick,
                                    type,
                                    content,
                                    contact,
                                    safe(com.tencent.qqlitepro.BuildConfig.VERSION_NAME),
                                    logBytes);
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
                                                uploadLog[0] = false;
                                                refreshLogRow.run();
                                                toast(ctx, "反馈已提交，感谢反馈");
                                            } else {
                                                resultTv.setTextColor(-1739917);
                                                resultTv.setText(r.error == null
                                                        ? "提交失败" : r.error);
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
