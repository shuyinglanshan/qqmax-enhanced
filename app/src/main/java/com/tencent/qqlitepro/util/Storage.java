package com.tencent.qqlitepro.util;

/**
 * 统一目录解析与可写性探测。
 *
 * 背景：原模块把下载/更新/分片目录硬编码为 /sdcard/QQMax增强/...。
 * 在 Android 10+ 分区存储下，未申请 MANAGE_EXTERNAL_STORAGE 的应用对该路径没有写权限，
 * File.mkdirs() 会静默失败，随后 new FileOutputStream(...) 抛 ENOENT
 * (No such file or directory) —— 这就是"未找到路径"的根因。
 *
 * 本类按优先级返回确定可写的目录：
 *   1) 原硬编码路径（老系统 / 已有全盘存储权限时保持原行为）
 *   2) 应用外部私有目录 Android/data/<pkg>/files/...（无需权限）
 *   3) 应用内部私有目录 files/...（永远可写）
 */
public final class Storage {

    /** 模块在存储上的根目录名 */
    public static final String ROOT_NAME = "QQMax增强";

    private static volatile String sResolvedRoot;

    private Storage() {
    }

    /** 首次调用后缓存，避免反复探测 */
    public static synchronized String root(android.content.Context context) {
        String cached = sResolvedRoot;
        if (cached != null) {
            return cached;
        }
        String resolved = probeRoot(context);
        sResolvedRoot = resolved;
        return resolved;
    }

    private static String probeRoot(android.content.Context context) {
        // 1) 原路径优先，保持与老版本一致
        String legacy = "/sdcard/" + ROOT_NAME;
        if (ensureWritable(new java.io.File(legacy))) {
            return legacy;
        }
        // 2) 应用外部私有目录
        try {
            if (context != null) {
                java.io.File ext = context.getExternalFilesDir(null);
                if (ext != null) {
                    java.io.File f = new java.io.File(ext, ROOT_NAME);
                    if (ensureWritable(f)) {
                        return f.getAbsolutePath();
                    }
                }
            }
        } catch (java.lang.Throwable ignored) {
        }
        // 3) 应用内部私有目录
        try {
            if (context != null) {
                java.io.File f = new java.io.File(context.getFilesDir(), ROOT_NAME);
                if (ensureWritable(f)) {
                    return f.getAbsolutePath();
                }
            }
        } catch (java.lang.Throwable ignored) {
        }
        // 4) 兜底
        try {
            if (context != null && context.getExternalFilesDir(null) != null) {
                return context.getExternalFilesDir(null).getAbsolutePath();
            }
        } catch (java.lang.Throwable ignored) {
        }
        return legacy;
    }

    /** 取 root 下的子目录，自动创建并验证可写 */
    public static java.io.File dir(android.content.Context context, String relative) {
        java.io.File base = new java.io.File(root(context));
        if (relative == null || relative.isEmpty()) {
            return base;
        }
        java.io.File f = new java.io.File(base, relative);
        ensureWritable(f);
        return f;
    }

    /** 把 /sdcard/QQMax增强/xxx 映射到实际可写根目录下的同名子路径 */
    public static java.io.File mapLegacy(android.content.Context context, String legacyPath) {
        String sub = subPathOf(legacyPath);
        if (sub == null) {
            return new java.io.File(legacyPath);
        }
        return dir(context, sub);
    }

    /** 提取 /sdcard/QQMax增强 之后的相对部分；不匹配返回 null */
    public static String subPathOf(String path) {
        if (path == null) {
            return null;
        }
        String[] prefixes = {
                "/sdcard/" + ROOT_NAME,
                "/storage/emulated/0/" + ROOT_NAME,
                "/mnt/sdcard/" + ROOT_NAME
        };
        for (int i = 0; i < prefixes.length; i++) {
            String p = prefixes[i];
            if (path.equals(p)) {
                return "";
            }
            if (path.startsWith(p + "/")) {
                return path.substring(p.length() + 1);
            }
        }
        return null;
    }

    /** 确保目录存在且真的可写 */
    public static boolean ensureWritable(java.io.File dir) {
        if (dir == null) {
            return false;
        }
        try {
            if (!dir.isDirectory() && !dir.mkdirs() && !dir.isDirectory()) {
                return false;
            }
            // mkdirs 成功不代表可写，用探针文件实测
            java.io.File probe = new java.io.File(dir, ".qqmax_write_probe_" + android.os.Process.myPid());
            if (probe.exists() && !probe.delete()) {
                return false;
            }
            // 并发下 createNewFile 可能因同名已存在返回 false，这里放宽为"能读能删即视为可写"
            if (!probe.createNewFile() && !probe.exists()) {
                return false;
            }
            probe.delete();
            return true;
        } catch (java.lang.Throwable ignored) {
            return false;
        }
    }

    /** 供界面展示的目录串 */
    public static String display(android.content.Context context) {
        return root(context);
    }
}
