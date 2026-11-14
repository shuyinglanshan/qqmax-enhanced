package com.tencent.qqlitepro.net;

import com.tencent.qqlitepro.util.Log;

/**
 * QQMax 反馈与通知接口客户端。
 *
 * 协议要点（见《PROMPT_通知与反馈.md》）：
 *  - Base: https://qm.xtctoolkit.top/qqmax
 *  - 每个请求必须带 3 个头：X-QQMax-Key / X-QQMax-Ts（毫秒）/ X-QQMax-Nonce（一次性）
 *  - nonce 复用会 409 REPLAY；时间戳偏差 > 5 分钟会 400 TS_INVALID
 *  - 超时 10 秒；失败静默，不打扰用户
 */
public final class FeedbackApi {

    private static final String BASE = "https://qm.xtctoolkit.top/qqmax";
    /** 上报密钥（与服务端 .env 的 QQMAX_REPORT_KEY 一致） */
    private static final String KEY =
            "bab516299be0e575f710845598eb811c553ffa61441937b4";

    private static final int TIMEOUT_MS = 10000;
    /** 日志文件大小上限：2MB（服务端硬限制） */
    public static final int MAX_LOG_BYTES = 2 * 1024 * 1024;

    private static final java.security.SecureRandom RANDOM = new java.security.SecureRandom();
    private static final char[] NONCE_CHARS =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_-".toCharArray();

    /** 时钟偏差（毫秒），TS_INVALID 时用响应 Date 校准 */
    private static volatile long sClockOffsetMs = 0L;

    private FeedbackApi() {
    }

    private static String newNonce() {
        char[] buf = new char[24];
        for (int i = 0; i < buf.length; i++) {
            buf[i] = NONCE_CHARS[RANDOM.nextInt(NONCE_CHARS.length)];
        }
        return new String(buf);
    }

    private static void applyAuth(java.net.HttpURLConnection conn) throws Exception {
        conn.setRequestProperty("X-QQMax-Key", KEY);
        conn.setRequestProperty("X-QQMax-Ts",
                String.valueOf(System.currentTimeMillis() + sClockOffsetMs));
        conn.setRequestProperty("X-QQMax-Nonce", newNonce());
    }

    private static String get(String path) {
        java.net.HttpURLConnection conn = null;
        try {
            java.net.URL url = new java.net.URL(BASE + path);
            conn = (java.net.HttpURLConnection) url.openConnection();
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(TIMEOUT_MS);
            conn.setReadTimeout(TIMEOUT_MS);
            applyAuth(conn);
            int code = conn.getResponseCode();
            java.io.InputStream is = (code >= 200 && code < 300)
                    ? conn.getInputStream() : conn.getErrorStream();
            String body = readAll(is);
            if (code >= 400) {
                handleClockSkew(code, body, conn);
                Log.w("反馈API: GET " + path + " -> HTTP " + code + " " + brief(body));
                return null;
            }
            return body;
        } catch (Throwable t) {
            Log.w("反馈API: GET " + path + " 失败 " + t);
            return null;
        } finally {
            if (conn != null) {
                try { conn.disconnect(); } catch (Throwable ignored) {}
            }
        }
    }

    private static String postJson(String path, String json) {
        java.net.HttpURLConnection conn = null;
        try {
            java.net.URL url = new java.net.URL(BASE + path);
            conn = (java.net.HttpURLConnection) url.openConnection();
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(TIMEOUT_MS);
            conn.setReadTimeout(TIMEOUT_MS);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            applyAuth(conn);
            byte[] data = json.getBytes("UTF-8");
            conn.setFixedLengthStreamingMode(data.length);
            java.io.OutputStream os = conn.getOutputStream();
            os.write(data); os.flush(); os.close();
            int code = conn.getResponseCode();
            java.io.InputStream is = (code >= 200 && code < 300)
                    ? conn.getInputStream() : conn.getErrorStream();
            String body = readAll(is);
            if (code >= 400) {
                handleClockSkew(code, body, conn);
                Log.w("反馈API: POST " + path + " -> HTTP " + code + " " + brief(body));
                return null;
            }
            return body;
        } catch (Throwable t) {
            Log.w("反馈API: POST " + path + " 失败 " + t);
            return null;
        } finally {
            if (conn != null) {
                try { conn.disconnect(); } catch (Throwable ignored) {}
            }
        }
    }

    private static void handleClockSkew(int code, String body, java.net.HttpURLConnection conn) {
        try {
            if (code == 400 && body != null && body.contains("TS_INVALID")) {
                long server = conn.getDate();
                if (server > 0) {
                    sClockOffsetMs = server - System.currentTimeMillis();
                    Log.i("反馈API: 时钟已校准 offset=" + sClockOffsetMs + "ms");
                }
            }
        } catch (Throwable ignored) {}
    }

    /* ===================== 通知 ===================== */

    public static int fetchUnread(String qq) {
        try {
            String r = get("/api/notifications/unread?qq=" + enc(qq));
            if (r == null) return -1;
            return new org.json.JSONObject(r).optInt("unread", 0);
        } catch (Throwable t) {
            return -1;
        }
    }

    public static class Notice {
        public String id;
        public String category;
        public String categoryLabel;
        public String title;
        public String content;
        public String refId;
        public boolean read;
        public long createdAt;
        @Override public String toString() { return title == null ? "" : title; }
    }

    public static java.util.List<Notice> fetchList(String qq, boolean unreadOnly, int page) {
        try {
            StringBuilder path = new StringBuilder("/api/notifications?qq=")
                    .append(enc(qq)).append("&page=").append(page).append("&pageSize=20");
            if (unreadOnly) path.append("&unread=1");
            String r = get(path.toString());
            if (r == null) return null;
            org.json.JSONArray arr = new org.json.JSONObject(r).optJSONArray("items");
            java.util.ArrayList<Notice> out = new java.util.ArrayList<>();
            if (arr != null) {
                for (int i = 0; i < arr.length(); i++) {
                    org.json.JSONObject it = arr.optJSONObject(i);
                    if (it == null) continue;
                    Notice n = new Notice();
                    n.id = it.optString("id");
                    n.category = it.optString("category");
                    n.categoryLabel = it.optString("categoryLabel");
                    n.title = it.optString("title");
                    n.content = it.optString("content");
                    n.refId = it.optString("refId");
                    n.read = it.optBoolean("read", false);
                    n.createdAt = it.optLong("createdAt", 0L);
                    out.add(n);
                }
            }
            return out;
        } catch (Throwable t) {
            return null;
        }
    }

    public static boolean markRead(String id, String qq) {
        try {
            org.json.JSONObject body = new org.json.JSONObject();
            body.put("qq", qq);
            return postJson("/api/notifications/" + enc(id) + "/read", body.toString()) != null;
        } catch (Throwable t) {
            return false;
        }
    }

    public static boolean markAllRead(String qq) {
        try {
            org.json.JSONObject body = new org.json.JSONObject();
            body.put("qq", qq);
            return postJson("/api/notifications/read-all", body.toString()) != null;
        } catch (Throwable t) {
            return false;
        }
    }

    /* ===================== 人机验证 ===================== */

    /** 验证挑战 */
    public static class Captcha {
        public String captchaId;
        /** data:image/svg+xml;base64,... 可直接解码为 SVG 文本 */
        public String image;
        public int cols;
        public int rows;
        public long expiresAt;
        /** 服务端 HMAC 签名，必须原样回传 */
        public String signature;
        public String hint;
        public long ttlMs = 180000L;
        public long minMs = 300L;
        public int maxFails = 5;
    }

    /**
     * 获取人机验证挑战。
     * 注意：每次提交失败后都必须重新调用（挑战一次性）。
     */
    public static Captcha fetchCaptcha() {
        try {
            String r = get("/api/captcha");
            if (r == null) {
                return null;
            }
            org.json.JSONObject o = new org.json.JSONObject(r);
            if (!o.optBoolean("ok", false)) {
                return null;
            }
            Captcha c = new Captcha();
            c.captchaId = o.optString("captchaId");
            c.image = o.optString("image");
            c.cols = o.optInt("cols", 4);
            c.rows = o.optInt("rows", 3);
            c.expiresAt = o.optLong("expiresAt", 0L);
            c.signature = o.optString("signature");
            c.hint = o.optString("hint", "请点击图中的星形图案");
            org.json.JSONObject meta = o.optJSONObject("meta");
            if (meta != null) {
                c.ttlMs = meta.optLong("ttlMs", 180000L);
                c.minMs = meta.optLong("minMs", 300L);
                c.maxFails = meta.optInt("maxFails", 5);
            }
            if (c.captchaId == null || c.captchaId.isEmpty()) {
                return null;
            }
            return c;
        } catch (Throwable t) {
            Log.w("反馈API: 获取验证挑战失败 " + t);
            return null;
        }
    }

    /** 把 data:image/svg+xml;base64,xxx 解码成 SVG 文本 */
    public static String decodeSvg(String dataUri) {
        try {
            if (dataUri == null) {
                return null;
            }
            int comma = dataUri.indexOf(',');
            if (comma < 0) {
                return null;
            }
            String b64 = dataUri.substring(comma + 1);
            byte[] raw = android.util.Base64.decode(b64, android.util.Base64.DEFAULT);
            return new String(raw, "UTF-8");
        } catch (Throwable t) {
            return null;
        }
    }

    /** 验证参数 */
    public static class CaptchaPayload {
        public String captchaId;
        public String captchaSig;
        public double captchaX;
        public double captchaY;
        public long captchaMs;
    }

    /* ===================== 反馈 ===================== */

    public static class FbType {
        public String value;
        public String label;
        @Override public String toString() { return label == null ? value : label; }
    }

    public static java.util.List<FbType> getTypes() {
        try {
            String r = get("/api/feedback/types");
            if (r == null) return null;
            org.json.JSONArray arr = new org.json.JSONObject(r).optJSONArray("types");
            java.util.ArrayList<FbType> out = new java.util.ArrayList<>();
            if (arr != null) {
                for (int i = 0; i < arr.length(); i++) {
                    org.json.JSONObject it = arr.optJSONObject(i);
                    if (it == null) continue;
                    FbType t = new FbType();
                    t.value = it.optString("value");
                    t.label = it.optString("label");
                    out.add(t);
                }
            }
            return out.isEmpty() ? null : out;
        } catch (Throwable t) {
            return null;
        }
    }

    public static class SubmitResult {
        public boolean ok;
        public String id;
        public String error;
    }

    public static SubmitResult submit(String qq, String nickname, String type,
                                      String content, String contact, String version,
                                      byte[] logBytes, CaptchaPayload cap) {
        SubmitResult res = new SubmitResult();
        java.net.HttpURLConnection conn = null;
        try {
            java.net.URL url = new java.net.URL(BASE + "/api/feedback");
            conn = (java.net.HttpURLConnection) url.openConnection();
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(TIMEOUT_MS);
            conn.setReadTimeout(TIMEOUT_MS);
            conn.setDoOutput(true);
            applyAuth(conn);

            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream();
            final String boundary = "----QQMax" + System.currentTimeMillis();

            if (logBytes != null && logBytes.length > 0) {
                conn.setRequestProperty("Content-Type",
                        "multipart/form-data; boundary=" + boundary);
                writeField(bos, boundary, "qq", qq);
                writeField(bos, boundary, "nickname", nickname);
                writeField(bos, boundary, "type", type);
                writeField(bos, boundary, "content", content);
                writeField(bos, boundary, "contact", contact);
                writeField(bos, boundary, "version", version);
                /* 人机验证（一次性挑战，服务端会重算 HMAC 比对） */
                if (cap != null) {
                    writeField(bos, boundary, "captchaId", cap.captchaId);
                    writeField(bos, boundary, "captchaX", String.valueOf(cap.captchaX));
                    writeField(bos, boundary, "captchaY", String.valueOf(cap.captchaY));
                    writeField(bos, boundary, "captchaMs", String.valueOf(cap.captchaMs));
                    writeField(bos, boundary, "captchaSig", cap.captchaSig);
                }
                bos.write(("--" + boundary + "\r\n").getBytes("UTF-8"));
                bos.write(("Content-Disposition: form-data; name=\"log\"; "
                        + "filename=\"qqmax_log.txt\"\r\n").getBytes("UTF-8"));
                bos.write("Content-Type: text/plain; charset=utf-8\r\n\r\n".getBytes("UTF-8"));
                bos.write(logBytes);
                bos.write("\r\n".getBytes("UTF-8"));
                bos.write(("--" + boundary + "--\r\n").getBytes("UTF-8"));
            } else {
                conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                org.json.JSONObject o = new org.json.JSONObject();
                o.put("qq", qq == null ? "" : qq);
                o.put("nickname", nickname == null ? "" : nickname);
                o.put("type", type);
                o.put("content", content == null ? "" : content);
                if (contact != null && !contact.isEmpty()) o.put("contact", contact);
                if (version != null && !version.isEmpty()) o.put("version", version);
                /* 人机验证字段 */
                if (cap != null) {
                    o.put("captchaId", cap.captchaId);
                    o.put("captchaX", cap.captchaX);
                    o.put("captchaY", cap.captchaY);
                    o.put("captchaMs", cap.captchaMs);
                    o.put("captchaSig", cap.captchaSig);
                }
                bos.write(o.toString().getBytes("UTF-8"));
            }

            byte[] data = bos.toByteArray();
            conn.setFixedLengthStreamingMode(data.length);
            java.io.OutputStream os = conn.getOutputStream();
            os.write(data); os.flush(); os.close();

            int code = conn.getResponseCode();
            java.io.InputStream is = (code >= 200 && code < 300)
                    ? conn.getInputStream() : conn.getErrorStream();
            String body = readAll(is);
            if (code >= 200 && code < 300) {
                res.ok = true;
                try { res.id = new org.json.JSONObject(body).optString("id"); }
                catch (Throwable ignored) {}
                return res;
            }
            handleClockSkew(code, body, conn);
            res.error = friendlyError(code, body);
            Log.w("反馈API: 提交失败 HTTP " + code + " " + brief(body));
            return res;
        } catch (Throwable t) {
            Log.w("反馈API: 提交异常 " + t);
            res.error = "网络异常，请稍后重试";
            return res;
        } finally {
            if (conn != null) {
                try { conn.disconnect(); } catch (Throwable ignored) {}
            }
        }
    }

    private static void writeField(java.io.ByteArrayOutputStream bos, String boundary,
                                   String name, String value) throws Exception {
        bos.write(("--" + boundary + "\r\n").getBytes("UTF-8"));
        bos.write(("Content-Disposition: form-data; name=\"" + name + "\"\r\n\r\n")
                .getBytes("UTF-8"));
        bos.write((value == null ? "" : value).getBytes("UTF-8"));
        bos.write("\r\n".getBytes("UTF-8"));
    }

    private static String friendlyError(int code, String body) {
        try {
            if (body != null) {
                if (body.contains("BAD_EXT")) return "日志文件必须是 .txt";
                if (body.contains("BAD_CONTENT")) return "日志不是纯文本，请换 UTF-8 编码的 .txt";
                if (body.contains("FILE_TOO_LARGE")) return "日志超过 2MB，请截取后再传";
                if (body.contains("BAD_TYPE")) return "反馈类型无效，请重新选择";
                if (body.contains("BAD_QQ")) return "QQ 号格式不正确";
                if (body.contains("RATE_LIMITED")) return "提交太频繁，请 1 分钟后再试";
                if (body.contains("REPLAY")) return "请求重复，请重试";
                if (body.contains("TS_INVALID")) return "设备时间不准，请校准后重试";
                if (body.contains("CAPTCHA_LOCKED")) return "验证失败次数过多，请 10 分钟后再试";
                if (body.contains("CAPTCHA_FAILED")) {
                    if (body.contains("TOO_FAST")) return "点击太快，请重新验证";
                    if (body.contains("TOO_MANY_FAILS")) return "验证失败次数过多，请 10 分钟后再试";
                    if (body.contains("WRONG_CELL")) return "没点中星形，请重新验证";
                    if (body.contains("EXPIRED")) return "验证已过期，请重新验证";
                    return "人机验证未通过，请重新验证";
                }
            }
            if (code == 401) return "鉴权失败，请联系开发者";
            if (code == 413) return "文件太大";
        } catch (Throwable ignored) {}
        return "提交失败（HTTP " + code + "）";
    }

    static String enc(String s) {
        try {
            return java.net.URLEncoder.encode(s == null ? "" : s, "UTF-8");
        } catch (Throwable ignored) {
            return s == null ? "" : s;
        }
    }

    private static String readAll(java.io.InputStream is) {
        if (is == null) return "";
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream();
            byte[] buf = new byte[4096];
            int n;
            while ((n = is.read(buf)) > 0) {
                bos.write(buf, 0, n);
                if (bos.size() > 262144) break;
            }
            is.close();
            return new String(bos.toByteArray(), "UTF-8");
        } catch (Throwable ignored) {
            return "";
        }
    }

    private static String brief(String s) {
        if (s == null) return "null";
        String one = s.replace('\n', ' ').trim();
        return one.length() > 160 ? one.substring(0, 160) + "…" : one;
    }
}
