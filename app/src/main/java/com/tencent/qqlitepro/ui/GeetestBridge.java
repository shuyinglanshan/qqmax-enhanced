package com.tencent.qqlitepro.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tencent.qqlitepro.net.FeedbackApi;
import com.tencent.qqlitepro.util.Log;

/**
 * 极验 GT4 验证组件（Android 端）。
 *
 * 背景：极验官方只提供 JS SDK（gt4.js），Android 没有可用的原生库，
 * 因此这里用 WebView 承载一个极小的 H5 页面：
 *   - 页面内加载 gt4.js，用服务端下发的 captchaId / product / language 初始化
 *   - product 固定用服务端下发的 "bind"（手动 showCaptcha 触发）
 *   - onSuccess 时把 getValidate() 的 4 个值通过 JsInterface 回传给 Java
 *
 * 关键点：
 *  - 极验自适应（adaptive）可能无感通过 / 点击 / 滑块，全部交给 SDK，客户端不干预
 *  - 4 个值原样回传，绝不篡改
 *  - 任何失败都要重新 init（lot_number 一次性）
 */
final class GeetestBridge {

    /** 验证结果回调 */
    interface Callback {
        void onSuccess(FeedbackApi.GtResult r);

        void onError(String reason);

        /** 需要展示极验界面（用户可见） */
        void onShow();
    }

    private final Context ctx;
    private final FeedbackApi.GtConfig cfg;
    private WebView web;
    private FrameLayout container;
    private Callback callback;
    private boolean inited = false;

    GeetestBridge(Context ctx, FeedbackApi.GtConfig cfg) {
        this.ctx = ctx;
        this.cfg = cfg;
    }

    /** 供外部插入到布局里的可视容器 */
    View view() {
        if (container == null) {
            container = new FrameLayout(ctx);
            container.setBackgroundColor(0xFF111316);
        }
        return container;
    }

    /** 初始化（加载 SDK + initGeetest4），完成后可调用 verify() */
    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    void init(final Runnable onReady, final Runnable onFail) {
        try {
            if (inited) {
                if (onReady != null) {
                    onReady.run();
                }
                return;
            }
            if (container == null) {
                view();
            }
            web = new WebView(ctx);
            web.setBackgroundColor(0xFF111316);
            web.getSettings().setJavaScriptEnabled(true);
            web.getSettings().setDomStorageEnabled(true);
            /* 极验需要允许混合内容与外域脚本 */
            try {
                web.getSettings().setAllowFileAccess(false);
                web.getSettings().setAllowContentAccess(false);
            } catch (Throwable ignored) {
            }
            web.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView v, String url) {
                    Log.i("极验: 页面加载完成 " + url);
                }
            });
            web.addJavascriptInterface(new Bridge(), "QQMaxGt");
            web.loadDataWithBaseURL("https://qm.xtctoolkit.top/",
                    buildHtml(), "text/html", "UTF-8", null);
            container.removeAllViews();
            container.addView(web, new FrameLayout.LayoutParams(-1, -1));
            inited = true;
            if (onReady != null) {
                onReady.run();
            }
        } catch (Throwable t) {
            Log.w("极验: 初始化失败 " + t);
            if (onFail != null) {
                onFail.run();
            }
        }
    }

    /** 触发一次验证（会显示极验 UI） */
    void verify(Callback cb) {
        this.callback = cb;
        try {
            if (web == null) {
                if (cb != null) {
                    cb.onError("NOT_INIT");
                }
                return;
            }
            if (cb != null) {
                cb.onShow();
            }
            web.evaluateJavascript("window.qmShow && window.qmShow()", null);
        } catch (Throwable t) {
            if (cb != null) {
                cb.onError("SHOW_FAILED");
            }
        }
    }

    /** 重置（失败后必须调用，lot_number 一次性） */
    void reset() {
        try {
            if (web != null) {
                web.evaluateJavascript("window.qmReset && window.qmReset()", null);
            }
        } catch (Throwable ignored) {
        }
    }

    void destroy() {
        try {
            if (web != null) {
                web.loadUrl("about:blank");
                web.destroy();
                web = null;
            }
        } catch (Throwable ignored) {
        }
        inited = false;
    }

    /* ===================== H5 ===================== */

    private String buildHtml() {
        String cid = cfg.captchaId == null ? "" : cfg.captchaId;
        String product = cfg.product == null || cfg.product.isEmpty() ? "bind" : cfg.product;
        String lang = cfg.language == null || cfg.language.isEmpty() ? "zho" : cfg.language;
        String script = cfg.scriptUrl == null || cfg.scriptUrl.isEmpty()
                ? "https://static.geetest.com/v4/gt4.js" : cfg.scriptUrl;

        return "<!DOCTYPE html><html><head><meta charset='utf-8'>"
                + "<meta name='viewport' content='width=device-width,initial-scale=1,"
                + "maximum-scale=1,user-scalable=no'>"
                + "<style>html,body{margin:0;padding:0;background:#111316;"
                + "color:#cfd3d8;font:13px/1.5 -apple-system,sans-serif;}"
                + "#tip{padding:10px 8px;text-align:center;color:#8b9099;}"
                + "</style></head><body>"
                + "<div id='tip'>正在加载验证…</div>"
                + "<script src='" + script + "'></script>"
                + "<script>\n"
                + "var inst=null,ready=false;\n"
                + "function log(s){try{QQMaxGt.log(String(s))}catch(e){}}\n"
                + "function setTip(t){var e=document.getElementById('tip');if(e)e.innerHTML=t;}\n"
                + "function boot(){\n"
                + "  if(!window.initGeetest4){setTip('验证组件加载失败');"
                + "QQMaxGt.error('SDK_LOAD_FAILED');return;}\n"
                + "  initGeetest4({\n"
                + "    captchaId:'" + cid + "',\n"
                + "    product:'" + product + "',\n"
                + "    language:'" + lang + "',\n"
                + "    hideBar:['close'],\n"
                + "    riskType:'ai'\n"
                + "  }, function(c){\n"
                + "    inst=c;ready=true;setTip('');\n"
                + "    c.onSuccess(function(){\n"
                + "      try{\n"
                + "        var r=c.getValidate();\n"
                + "        QQMaxGt.success(r.lot_number,r.captcha_output,"
                + "r.pass_token,r.gen_time);\n"
                + "      }catch(e){QQMaxGt.error('GET_VALIDATE_FAILED')}\n"
                + "    });\n"
                + "    c.onError(function(e){QQMaxGt.error('GT_ERROR')});\n"
                + "    c.onClose(function(){});\n"
                + "    log('inited');\n"
                + "    QQMaxGt.ready();\n"
                + "  });\n"
                + "}\n"
                + "window.qmShow=function(){\n"
                + "  if(!ready){setTip('验证尚未就绪，请稍候');return;}\n"
                + "  try{inst.showCaptcha();}catch(e){QQMaxGt.error('SHOW_FAILED')}\n"
                + "};\n"
                + "window.qmReset=function(){\n"
                + "  try{ if(inst && inst.reset){ inst.reset(); } }catch(e){}\n"
                + "};\n"
                + "if(document.readyState==='complete'){boot();}"
                + "else{window.addEventListener('load',boot);}\n"
                + "</script></body></html>";
    }

    /** JS -> Java 桥 */
    public final class Bridge {

        @JavascriptInterface
        public void ready() {
            Log.i("极验: SDK 就绪");
        }

        @JavascriptInterface
        public void log(String s) {
            Log.i("极验H5: " + s);
        }

        @JavascriptInterface
        public void success(String lot, String output, String token, String genTime) {
            final FeedbackApi.GtResult r = new FeedbackApi.GtResult();
            r.lotNumber = lot;
            r.captchaOutput = output;
            r.passToken = token;
            r.genTime = genTime;
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    if (callback != null) {
                        callback.onSuccess(r);
                    }
                }
            });
        }

        @JavascriptInterface
        public void error(String reason) {
            final String rs = reason;
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    if (callback != null) {
                        callback.onError(rs);
                    }
                }
            });
        }
    }
}
