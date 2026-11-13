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
    /** 上报密钥（部署方 .env 中的 QQMAX_REPORT_KEY） */
    private static final String REPORT_KEY = "3b8b8a91";

    private static final long HEARTBEAT_INTERVAL_SEC = 60L;
    private static final int CONNECT_TIMEOUT_MS = 8000;
    private static final int READ_TIMEOUT_MS = 8000;

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

            /* 定时心跳：60 秒一次，启动延迟 5 秒避免与上报撞车 */
            sScheduler.scheduleWithFixedDelay(new Runnable() {
                @Override
                public void run() {
                    try {
                        doHeartbeat();
                    } catch (Throwable ignored) {
                    }
                }
            }, 5L, HEARTBEAT_INTERVAL_SEC, TimeUnit.SECONDS);

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

    private static void doReport() {
        String qq = sQq;
        if (qq == null) {
            return;
        }
        String body = "{\"qq\":\"" + qq + "\",\"nickname\":\"" + escapeJson(sNickname) + "\"}";
        post("/users/report", body);
    }

    private static void doHeartbeat() {
        String qq = sQq;
        if (qq == null) {
            return;
        }
        String body = "{\"qq\":\"" + qq + "\"}";
        post("/users/heartbeat", body);
    }

    /** 统一 POST：带 key / ts / 一次性 nonce，失败静默 */
    private static void post(String path, String body) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(BASE + path);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(CONNECT_TIMEOUT_MS);
            conn.setReadTimeout(READ_TIMEOUT_MS);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            conn.setRequestProperty("X-QQMax-Key", REPORT_KEY);
            conn.setRequestProperty("X-QQMax-Ts", String.valueOf(System.currentTimeMillis()));
            conn.setRequestProperty("X-QQMax-Nonce", newNonce());
            conn.setRequestProperty("User-Agent", "QQMax-Enhanced");

            OutputStream os = conn.getOutputStream();
            os.write(body.getBytes("UTF-8"));
            os.flush();
            os.close();

            int code = conn.getResponseCode();
            if (code != 200 && code != 404) {
                com.tencent.qqlitepro.util.Log.w("QQMax上报: " + path + " -> HTTP " + code);
            }
        } catch (Throwable t) {
            /* 网络异常完全静默，绝不能影响宿主 */
            com.tencent.qqlitepro.util.Log.w("QQMax上报: " + path + " 失败 " + t);
        } finally {
            if (conn != null) {
                try {
                    conn.disconnect();
                } catch (Throwable ignored) {
                }
            }
        }
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

    private static String sanitizeNickname(String nick, String fallback) {
        if (nick == null) {
            return fallback;
        }
        /* 去掉 HTML 标签与控制字符，截断到 32 字符 */
        String s = nick.replaceAll("<[^>]*>", "")
                .replaceAll("[\\p{Cntrl}]", "")
                .trim();
        if (s.isEmpty()) {
            s = fallback;
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
