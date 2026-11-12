package com.tencent.qqlitepro.util;

/**
 * 可靠的 Context 获取器。
 *
 * <p>原有实现只用 {@code ActivityThread.currentApplication()} 反射取值，
 * 但在 Xposed 早期回调（handleLoadPackage 阶段）以及部分 ROM 上，该方法返回 null，
 * 导致所有依赖 Context 的功能静默失效：
 * <ul>
 *   <li>临时会话置顶/免打扰写不进 SharedPreferences，读回来永远是空 —— 表现为"设置无效"</li>
 *   <li>因为降级路径返回空集合，所有会话看起来都"未置顶"，</li>
 * </ul>
 *
 * <p>这里按可靠性从高到低依次尝试，并把第一个成功的结果缓存起来。
 */
public final class Ctx {

    private static volatile android.content.Context sApp;

    private Ctx() {
    }

    /** 主动登记 Context（hook Activity / Application 时调用，最可靠） */
    public static void set(android.content.Context context) {
        if (context == null) {
            return;
        }
        try {
            android.content.Context app = context.getApplicationContext();
            sApp = app != null ? app : context;
        } catch (java.lang.Throwable ignored) {
            sApp = context;
        }
    }

    /** 取应用级 Context，可能返回 null */
    public static android.content.Context get() {
        android.content.Context c = sApp;
        if (c != null) {
            return c;
        }
        c = fromActivityThread();
        if (c == null) {
            c = fromMsgCache();
        }
        if (c != null) {
            sApp = c;
        }
        return c;
    }

    private static android.content.Context fromActivityThread() {
        // Android 8+ 是 currentApplication
        try {
            java.lang.reflect.Method m = java.lang.Class.forName("android.app.ActivityThread")
                    .getMethod("currentApplication", new java.lang.Class[0]);
            m.setAccessible(true);
            android.content.Context c = (android.content.Context) m.invoke(null, new java.lang.Object[0]);
            if (c != null) {
                return c;
            }
        } catch (java.lang.Throwable ignored) {
        }
        // 退回 currentActivityThread + getApplication
        try {
            java.lang.Class<?> at = java.lang.Class.forName("android.app.ActivityThread");
            java.lang.reflect.Method cur = at.getMethod("currentActivityThread", new java.lang.Class[0]);
            cur.setAccessible(true);
            java.lang.Object thread = cur.invoke(null, new java.lang.Object[0]);
            if (thread != null) {
                java.lang.reflect.Method getApp = at.getMethod("getApplication", new java.lang.Class[0]);
                getApp.setAccessible(true);
                android.content.Context c = (android.content.Context) getApp.invoke(thread, new java.lang.Object[0]);
                if (c != null) {
                    return c;
                }
            }
        } catch (java.lang.Throwable ignored) {
        }
        return null;
    }

    private static android.content.Context fromMsgCache() {
        try {
            return com.tencent.qqlitepro.cache.MsgCache.appContext();
        } catch (java.lang.Throwable ignored) {
            return null;
        }
    }

    /** 取 Context；拿不到时抛异常，便于调用方区分"真的没有"和"值为空" */
    public static android.content.Context require() {
        android.content.Context c = get();
        if (c == null) {
            throw new java.lang.IllegalStateException("应用 Context 尚未就绪");
        }
        return c;
    }
}
