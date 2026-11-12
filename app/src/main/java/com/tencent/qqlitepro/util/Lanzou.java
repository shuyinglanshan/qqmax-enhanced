package com.tencent.qqlitepro.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 蓝奏云链接识别与解析。
 *
 * <p>支持：
 * <ul>
 *   <li>标准分享链接 {@code https://xxx.lanzou*.com/xxxxxx}（含 lanzou/lanzoux/lanzoui/lanzouy/lanzoun/lanzoub 等全部后缀）</li>
 *   <li>带密码的形式 {@code 链接: https://... 密码:abcd}</li>
 *   <li>独立的提取码（4 位）</li>
 * </ul>
 *
 * <p>解析流程：GET 分享页 → 提取 JS 参数 → POST 到 `/ajaxm.php` 取真实直链。
 */
public final class Lanzou {

    /** 匹配任意 lanzou* 域名的分享链接 */
    private static final Pattern HOST = Pattern.compile(
            "(?i)^(?:https?://)?([a-z0-9\\-]+\\.)?lanzou[a-z]*\\.(?:com|cn|net|org)(/.*)?$");

    /** 从一段文本里找蓝奏云链接 */
    private static final Pattern LINK_IN_TEXT = Pattern.compile(
            "(?i)https?://(?:[a-z0-9\\-]+\\.)?lanzou[a-z]*\\.(?:com|cn|net|org)/[A-Za-z0-9_\\-]+");

    /** 提取码：支持 "密码:abcd" / "pwd=abcd" / "提取码 abcd" */
    private static final Pattern PWD_IN_TEXT = Pattern.compile(
            "(?i)(?:密码|提取码|pwd|code)\\s*[:：=]?\\s*([A-Za-z0-9]{4})");

    private Lanzou() {
    }

    /** 文本中是否含蓝奏云链接 */
    public static boolean contains(String text) {
        return text != null && LINK_IN_TEXT.matcher(text).find();
    }

    /** 取出文本中的第一个蓝奏云链接（没有则 null） */
    public static String firstLink(String text) {
        if (text == null) {
            return null;
        }
        Matcher m = LINK_IN_TEXT.matcher(text);
        return m.find() ? m.group() : null;
    }

    /** 是否为蓝奏云链接 */
    public static boolean isLanzou(String url) {
        return url != null && HOST.matcher(url.trim()).matches();
    }

    /** 从文本中猜提取码（没有则空串） */
    public static String guessPassword(String text) {
        if (text == null) {
            return "";
        }
        Matcher m = PWD_IN_TEXT.matcher(text);
        return m.find() ? m.group(1) : "";
    }

    /** 解析结果 */
    public static final class Info {
        public String name = "";
        public String size = "";
        public String time = "";
        public String directUrl = "";
        public String password = "";
        public String shareUrl = "";
        public String error = "";

        public boolean ok() {
            return directUrl != null && !directUrl.isEmpty();
        }
    }

    /**
     * 解析蓝奏云分享链接，拿到文件信息与直链。
     * 注意：会发起网络请求，必须在子线程调用。
     */
    public static Info resolve(String shareUrl, String password) {
        Info info = new Info();
        info.shareUrl = shareUrl == null ? "" : shareUrl.trim();
        info.password = password == null ? "" : password.trim();
        if (info.shareUrl.isEmpty()) {
            info.error = "链接为空";
            return info;
        }
        if (!info.shareUrl.startsWith("http")) {
            info.shareUrl = "https://" + info.shareUrl;
        }
        try {
            String page = httpGet(info.shareUrl);
            if (page == null || page.isEmpty()) {
                info.error = "无法访问页面";
                return info;
            }
            // 页面标题里的文件名
            Matcher t = Pattern.compile("<title>(.*?)</title>", Pattern.DOTALL).matcher(page);
            if (t.find()) {
                info.name = clean(t.group(1));
            }
            // 文件大小 / 时间
            Matcher sz = Pattern.compile("文件大小[:：]\\s*</?[^>]*>?\\s*([^<\\s]+)").matcher(page);
            if (sz.find()) {
                info.size = clean(sz.group(1));
            }
            Matcher tm = Pattern.compile("上传时间[:：]\\s*</?[^>]*>?\\s*([^<\\s]+)").matcher(page);
            if (tm.find()) {
                info.time = clean(tm.group(1));
            }

            // 收集 JS 变量
            String sign = findVar(page, "sign");
            String ajaxdata = findVar(page, "ajaxdata");
            String pwd = info.password;
            if (pwd.isEmpty()) {
                pwd = findVar(page, "pwd");
            }

            // 有密码的页面需要先 POST 校验，拿到新的 sign/参数
            String ajaxBase = baseOf(info.shareUrl) + "/ajaxm.php";
            if (!sign.isEmpty() && !ajaxdata.isEmpty()) {
                String body = "action=downprocess&sign=" + urlEncode(sign)
                        + (pwd.isEmpty() ? "" : "&pwd=" + urlEncode(pwd))
                        + "&ves=1";
                String resp = httpPost(ajaxBase, body, referer(info.shareUrl));
                String direct = pickDirect(resp);
                if (!direct.isEmpty()) {
                    info.directUrl = direct;
                    return info;
                }
            }
            // 兜底：从页面里直接找直链
            Matcher d = Pattern.compile("(?i)(https?://[^\"'\\s]*(?:lanzou[a-z]*|down)[^\"'\\s]*\\.(?:zip|apk|rar|7z|js|txt|pdf|jpg|png|doc[x]?|mp4|mp3)[^\"'\\s]*)").matcher(page);
            if (d.find()) {
                info.directUrl = d.group(1).replace("\\/", "/");
                return info;
            }
            if (info.error.isEmpty()) {
                info.error = "未解析到下载直链";
            }
        } catch (Throwable th) {
            info.error = String.valueOf(th.getMessage());
        }
        return info;
    }

    private static String pickDirect(String resp) {
        if (resp == null || resp.isEmpty()) {
            return "";
        }
        // ajaxm.php 返回 JSON: {"dom":"...","url":"xxxx","inf":0}
        String dom = jsonValue(resp, "dom");
        String url = jsonValue(resp, "url");
        if (!url.isEmpty()) {
            if (url.startsWith("http")) {
                return url;
            }
            if (!dom.isEmpty()) {
                if (!dom.endsWith("/")) {
                    dom = dom + "/";
                }
                return dom + url;
            }
        }
        return "";
    }

    private static String jsonValue(String json, String key) {
        try {
            Matcher m = Pattern.compile("\"" + key + "\"\\s*:\\s*\"([^\"]*)\"").matcher(json);
            if (m.find()) {
                return m.group(1).replace("\\/", "/");
            }
        } catch (Throwable ignored) {
        }
        return "";
    }

    private static String findVar(String page, String name) {
        try {
            Matcher m = Pattern.compile("(?:var\\s+|['\"]?)" + name + "['\"]?\\s*[=:]\\s*['\"]([^'\"]+)['\"]").matcher(page);
            if (m.find()) {
                return m.group(1);
            }
        } catch (Throwable ignored) {
        }
        return "";
    }

    private static String baseOf(String url) {
        try {
            java.net.URL u = new java.net.URL(url);
            return u.getProtocol() + "://" + u.getHost();
        } catch (Throwable ignored) {
        }
        return "";
    }

    private static String referer(String url) {
        return url;
    }

    private static String clean(String s) {
        if (s == null) {
            return "";
        }
        return s.replace("&nbsp;", " ").replace("&amp;", "&").trim();
    }

    private static String urlEncode(String s) {
        try {
            return java.net.URLEncoder.encode(s, "UTF-8");
        } catch (Throwable ignored) {
            return s;
        }
    }

    private static String httpGet(String url) throws Throwable {
        java.net.HttpURLConnection c = (java.net.HttpURLConnection) new java.net.URL(url).openConnection();
        try {
            c.setConnectTimeout(15000);
            c.setReadTimeout(20000);
            c.setInstanceFollowRedirects(true);
            c.setRequestProperty("User-Agent", UA);
            return readAll(c);
        } finally {
            try {
                c.disconnect();
            } catch (Throwable ignored) {
            }
        }
    }

    private static String httpPost(String url, String body, String referer) throws Throwable {
        java.net.HttpURLConnection c = (java.net.HttpURLConnection) new java.net.URL(url).openConnection();
        try {
            c.setConnectTimeout(15000);
            c.setReadTimeout(20000);
            c.setRequestMethod("POST");
            c.setDoOutput(true);
            c.setRequestProperty("User-Agent", UA);
            c.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            if (referer != null) {
                c.setRequestProperty("Referer", referer);
            }
            c.getOutputStream().write(body.getBytes("UTF-8"));
            return readAll(c);
        } finally {
            try {
                c.disconnect();
            } catch (Throwable ignored) {
            }
        }
    }

    private static String readAll(java.net.HttpURLConnection c) throws Throwable {
        java.io.InputStream in = c.getInputStream();
        java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream();
        byte[] buf = new byte[8192];
        int n;
        while ((n = in.read(buf)) > 0) {
            bos.write(buf, 0, n);
        }
        in.close();
        return new String(bos.toByteArray(), "UTF-8");
    }

    private static final String UA = "Mozilla/5.0 (Linux; Android 13) AppleWebKit/537.36 "
            + "(KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36";
}
