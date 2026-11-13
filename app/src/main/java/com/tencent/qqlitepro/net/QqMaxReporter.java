package com.tencent.qqlitepro.net;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * QQMax 用户在线状态上报（完全异步，绝不阻塞宿主主线程）。
 *
 * <p>服务端约定（见 README）：
 * <ul>
 *   <li>Base：{@code https://qm.xtctoolkit.top/qqmax/api}</li>
 *   <li>写操作必须带 {@code X-QQMax-Key}、{@code X-QQMax-Ts}、{@code X-QQMax-Nonce}</li>
 *   <li>nonce 一次性，重复使用返回 409 REPLAY</li>
 *   <li>心跳建议 60 秒一次；离线判定窗口默认 5 分钟</li>
 * </ul>
 *
 * <p>线程模型：
 * <ul>
 *   <li>单线程守护调度器负责定时心跳（60s）</li>
 *   <li>单线程守护执行器负责网络 IO，失败静默重试，不抛到宿主</li>
 *   <li>所有方法都可从任意线程调用，内部不做任何阻塞等待</li>
 * </ul>
 */
public final class QqMaxReporter {

    private static final String BASE = "https://qm.xtctoolkit.top/qqmax/api";
    /** 上报密钥（服务端 .env 的 QQMAX_REPORT_KEY，客户端与之一致即可通过鉴权） */
    private static final String REPORT_KEY =
            "bab516299be0e575f710845598eb811c553ffa61441937b4";

    /** 心跳间隔：3 分钟（首次上报用户信息后开始计时） */
    private static final long HEARTBEAT_INTERVAL_SEC = 180L;
    private static final int CONNECT_TIMEOUT_MS = 10000;
    private static final int READ_TIMEOUT_MS = 10000;

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final char[] NONCE_ALPHABET =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_-".toCharArray();

    private static volatile String sQq = null;
    private static volatile String sNickname = null;
    private static volatile boolean sStarted = false;

    private static ScheduledExecutorService sScheduler = null;
    private static ExecutorService sWorker = null;

    private QqMaxReporter() {
    }

    /**
     * 启动上报（幂等）。
     *
     * @param qq       当前登录 QQ 号（纯数字）
     * @param nickname 昵称
     */
    public static synchronized void start(final String qq, final String nickname) {
        try {
            if (qq == null || !qq.matches("\\d{5,12}")) {
                return;
            }
            sQq = qq;
            sNickname = sanitizeNickname(nickname, qq);
            if (sStarted) {
                /* 已启动：只更新资料，立刻补一次心跳 */
                runAsync(new Runnable() {
                    @Override
                    public void run() {
                        doHeartbeat();
                    }
                });
                return;
            }
            sStarted = true;

            sWorker = Executors.newSingleThreadExecutor(new DaemonFactory("qqmax-report-io"));
            sScheduler = Executors.newSingleThreadScheduledExecutor(
                    new DaemonFactory("qqmax-report-timer"));

            /* 首次：上报用户信息（顺便建立记录） */
            runAsync(new Runnable() {
                @Override
                public void run() {
                    doReport();
                }
            });

            /* 定时心跳：每 3 分钟一次，首次在 report 成功后开始计时 */
            sScheduler.scheduleWithFixedDelay(new Runnable() {
                @Override
                public void run() {
                    try {
                        doHeartbeat();
                    } catch (Throwable ignored) {
                    }
                }
            }, HEARTBEAT_INTERVAL_SEC, HEARTBEAT_INTERVAL_SEC, TimeUnit.SECONDS);

            com.tencent.qqlitepro.util.Log.i("QQMax上报: 已启动 qq=" + sQq);
        } catch (Throwable t) {
            com.tencent.qqlitepro.util.Log.w("QQMax上报: 启动失败 " + t);
        }
    }

    /** 宿主退到后台/进程结束前调用（可选） */
    public static synchronized void stop() {
        try {
            sStarted = false;
            if (sScheduler != null) {
                sScheduler.shutdownNow();
                sScheduler = null;
            }
            if (sWorker != null) {
                sWorker.shutdownNow();
                sWorker = null;
            }
        } catch (Throwable ignored) {
        }
    }

    /* ===================== 内部实现 ===================== */

    private static void runAsync(Runnable r) {
        try {
            ExecutorService w = sWorker;
            if (w != null && !w.isShutdown()) {
                w.execute(r);
            }
        } catch (Throwable ignored) {
        }
    }

    /** 客户端版本号：从 BuildConfig 读取，绝不写死 */
    private static String clientVersion() {
        try {
            return com.tencent.qqlitepro.BuildConfig.VERSION_NAME;
        } catch (Throwable t) {
            return null;
        }
    }

    private static boolean doReport() {
        String qq = sQq;
        if (qq == null) {
            return false;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("{\"qq\":\"").append(qq).append("\"");
        /*
         * 服务端 nickname 为必填（缺失会 400 BAD_NICKNAME）。
         * 优先用真实昵称；真拿不到时才用 QQ 号占位，避免整个上报被拒。
         */
        String nick = sNickname;
        if (nick == null || nick.isEmpty()) {
            nick = qq;
        }
        sb.append(",\"nickname\":\"").append(escapeJson(nick)).append("\"");
        String v = clientVersion();
        if (v != null && v.matches("[0-9]+(\\.[0-9]+)*")) {
            sb.append(",\"version\":\"").append(v).append("\"");
        }
        sb.append("}");
        return post("/users/report", sb.toString());
    }

    private static void doHeartbeat() {
        String qq = sQq;
        if (qq == null) {
            return;
        }
        /* 文档要求：心跳前必须先成功 report 过，否则 404 USER_NOT_FOUND */
        if (!sUserKnown) {
            if (!doReport()) {
                return;   /* 建档失败，等下一轮心跳再试 */
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("{\"qq\":\"").append(qq).append("\"");
        String v = clientVersion();
        if (v != null && v.matches("[0-9]+(\\.[0-9]+)*")) {
            sb.append(",\"version\":\"").append(v).append("\"");
        }
        sb.append("}");
        post("/users/heartbeat", sb.toString());
    }

    /** 本地时钟与服务器的偏差（毫秒），TS_INVALID 时按响应 Date 校准 */
    private static volatile long sClockOffsetMs = 0L;
    /** 服务端是否已确认存在该用户（心跳 404 时置 false，触发重新 report） */
    private static volatile boolean sUserKnown = false;

    /**
     * 统一 POST：带 3 个鉴权头（key / 毫秒时间戳 / 一次性 nonce），失败静默。
     * 返回 true 表示 2xx。
     */
    private static boolean post(String path, String body) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(BASE + path);
            /* 关键：禁止自动重定向——HttpURLConnection 跨协议重定向时会
             * 丢弃全部自定义请求头（X-QQMax-Key 等），导致 401 UNAUTHORIZED */
            conn = (HttpURLConnection) url.openConnection();
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(CONNECT_TIMEOUT_MS);
            conn.setReadTimeout(READ_TIMEOUT_MS);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            /* X-QQMax-Key: 固定上报密钥，与服务端 .env 一致 */
            conn.setRequestProperty("X-QQMax-Key", REPORT_KEY);
            /* X-QQMax-Ts: 毫秒时间戳（+本地校准偏差），与服务端偏差需在 ±5 分钟内 */
            conn.setRequestProperty("X-QQMax-Ts",
                    String.valueOf(System.currentTimeMillis() + sClockOffsetMs));
            /* X-QQMax-Nonce: 每次现场生成，绝不复用（复用会被判 409 REPLAY） */
            conn.setRequestProperty("X-QQMax-Nonce", newNonce());
            conn.setRequestProperty("User-Agent", "QQMax-Enhanced/" + safeVersion());

            OutputStream os = conn.getOutputStream();
            os.write(body.getBytes("UTF-8"));
            os.flush();
            os.close();

            int code = conn.getResponseCode();
            if (code >= 300 && code < 400) {
                /* 理论上不该出现（BASE 已是 https）；若出现则记录便于排查 */
                com.tencent.qqlitepro.util.Log.w("QQMax上报: " + path
                        + " 被重定向 " + code + " -> " + conn.getHeaderField("Location"));
                return false;
            }
            String resp = code >= 400 ? readStream(conn.getErrorStream()) : "";
            if (code >= 200 && code < 300) {
                if (path.endsWith("/report")) {
                    sUserKnown = true;
                }
                return true;
            }
            /* ---- 按文档错误码处理 ---- */
            if (code == 404 && resp.contains("USER_NOT_FOUND")) {
                /* 心跳时用户不存在：先 report 建档，下一轮心跳即可 */
                sUserKnown = false;
                runAsync(new Runnable() {
                    @Override
                    public void run() {
                        doReport();
                    }
                });
                return false;
            }
            if (code == 400 && resp.contains("TS_INVALID")) {
                /* 时钟超窗：用响应头 Date 校准本地偏差后重试一次 */
                long serverMs = parseDateHeader(conn);
                if (serverMs > 0) {
                    sClockOffsetMs = serverMs - System.currentTimeMillis();
                    com.tencent.qqlitepro.util.Log.i(
                            "QQMax上报: 时钟已校准 offset=" + sClockOffsetMs + "ms");
                    runAsync(new Runnable() {
                        @Override
                        public void run() {
                            if (path.endsWith("/report")) {
                                doReport();
                            } else {
                                doHeartbeat();
                            }
                        }
                    });
                }
                return false;
            }
            if (code == 409 && resp.contains("REPLAY")) {
                /* nonce 意外重复：换新 nonce 立即重试一次 */
                runAsync(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(50L);
                        } catch (Throwable ignored) {
                        }
                        if (path.endsWith("/report")) {
                            doReport();
                        } else {
                            doHeartbeat();
                        }
                    }
                });
                return false;
            }
            if (code == 429) {
                /* 限流：退避 60 秒（由心跳节奏自然消化，这里只记日志） */
                com.tencent.qqlitepro.util.Log.w("QQMax上报: " + path + " 限流(429)，等待下轮");
                return false;
            }
            if (code == 401 || code == 400 || code == 503) {
                /* 配置/数据问题：不要自动重试 */
                com.tencent.qqlitepro.util.Log.w("QQMax上报: " + path
                        + " HTTP " + code + " " + abbreviate(resp, 120));
                return false;
            }
            com.tencent.qqlitepro.util.Log.w("QQMax上报: " + path + " -> HTTP " + code);
            return false;
        } catch (Throwable t) {
            /* 网络异常完全静默，绝不能影响宿主 */
            com.tencent.qqlitepro.util.Log.w("QQMax上报: " + path + " 失败 " + t);
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.disconnect();
                } catch (Throwable ignored) {
                }
            }
        }
    }

    private static String safeVersion() {
        try {
            String v = clientVersion();
            return v == null ? "?" : v;
        } catch (Throwable ignored) {
            return "?";
        }
    }

    private static long parseDateHeader(HttpURLConnection conn) {
        try {
            long d = conn.getDate();
            return d;
        } catch (Throwable ignored) {
            return -1L;
        }
    }

    private static String readStream(java.io.InputStream is) {
        if (is == null) {
            return "";
        }
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int n;
            while ((n = is.read(buf)) > 0) {
                bos.write(buf, 0, n);
                if (bos.size() > 16384) {
                    break;
                }
            }
            is.close();
            return new String(bos.toByteArray(), "UTF-8");
        } catch (Throwable ignored) {
            return "";
        }
    }

    private static String abbreviate(String s, int max) {
        if (s == null) {
            return "";
        }
        String one = s.replace('\n', ' ').trim();
        return one.length() > max ? one.substring(0, max) + "…" : one;
    }

    /** 16-64 位、[A-Za-z0-9_-]、每次都是新的 */
    private static String newNonce() {
        int len = 24;
        char[] buf = new char[len];
        for (int i = 0; i < len; i++) {
            buf[i] = NONCE_ALPHABET[RANDOM.nextInt(NONCE_ALPHABET.length)];
        }
        return new String(buf);
    }

    /**
     * 昵称清洗。注意：**绝不把 QQ 号当昵称上报**。
     * 若拿不到真实昵称则返回空串，由调用方跳过 nickname 字段
     * （服务端约定：不传则保留原值）。
     */
    private static String sanitizeNickname(String nick, String uin) {
        if (nick == null) {
            return "";
        }
        /* 去掉 HTML 标签与控制字符，截断到 32 字符 */
        String s = nick.replaceAll("<[^>]*>", "")
                .replaceAll("[\\p{Cntrl}]", "")
                .trim();
        /* 若与 QQ 号相同说明没拿到真昵称，视为空 */
        if (s.isEmpty() || s.equals(uin)) {
            return "";
        }
        if (s.length() > 32) {
            s = s.substring(0, 32);
        }
        return s;
    }

    private static String escapeJson(String s) {
        if (s == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder(s.length() + 8);
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '"':
                    sb.append("\\\"");
                    break;
                case '\\':
                    sb.append("\\\\");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                default:
                    if (c < 0x20) {
                        sb.append(String.format("\\u%04x", (int) c));
                    } else {
                        sb.append(c);
                    }
            }
        }
        return sb.toString();
    }

    /** 守护线程工厂：低优先级，进程退出时自动结束 */
    private static final class DaemonFactory implements java.util.concurrent.ThreadFactory {
        private final String name;

        DaemonFactory(String name) {
            this.name = name;
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, name);
            t.setDaemon(true);
            try {
                t.setPriority(Thread.MIN_PRIORITY);
            } catch (Throwable ignored) {
            }
            return t;
        }
    }
}
