package com.tencent.qqlitepro.plugin;

/* JADX INFO: loaded from: classes.dex */
public final class PluginEngine {
    public static final int LOG_MAX = 4000;
    private static volatile java.lang.ClassLoader sCl = null;
    private static volatile android.content.Context sCtx = null;
    private static volatile android.app.Activity sCurrentActivity = null;
    private static volatile boolean sHookApplied = false;
    private static volatile boolean sLoaded = false;
    private static volatile android.widget.TextView sLogView;
    private static final java.util.Map<java.lang.String, com.tencent.qqlitepro.plugin.PluginEngine.Plugin> sPlugins = new java.util.concurrent.ConcurrentHashMap();
    private static final java.util.concurrent.CopyOnWriteArrayList<com.tencent.qqlitepro.plugin.PluginEngine.Feature> sFeatures = new java.util.concurrent.CopyOnWriteArrayList<>();
    private static final java.util.concurrent.atomic.AtomicBoolean sApplyingHooks = new java.util.concurrent.atomic.AtomicBoolean(false);
    private static final java.util.Map<com.tencent.qqlitepro.plugin.PluginEngine.Plugin, java.util.concurrent.CopyOnWriteArrayList<java.lang.String>> sMsgCallbacks = new java.util.concurrent.ConcurrentHashMap();
    private static volatile java.lang.String sLogText = "";

    public static android.app.Activity currentActivity() {
        return sCurrentActivity;
    }

    public static android.content.Context getContext() {
        return sCtx;
    }

    public static java.lang.String getLog() {
        return sLogText;
    }

    public static void setCurrentActivity(android.app.Activity activity) {
        sCurrentActivity = activity;
    }

    private PluginEngine() {
    }

    public static void setLogView(android.widget.TextView textView) {
        sLogView = textView;
        refreshLogView();
    }

    public static void clearLog() {
        synchronized (com.tencent.qqlitepro.plugin.PluginEngine.class) {
            sLogText = "";
        }
        refreshLogView();
    }

    public static void appendLog(java.lang.String str) {
        java.lang.String str2;
        java.lang.String str3;
        try {
            str2 = new java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale.US).format(new java.util.Date());
        } catch (java.lang.Throwable unused) {
            str2 = "--";
        }
        java.lang.String str4 = "[" + str2 + "] " + str;
        synchronized (com.tencent.qqlitepro.plugin.PluginEngine.class) {
            if (sLogText.length() + str4.length() > 4000) {
                sLogText = sLogText.substring(java.lang.Math.max(0, sLogText.length() - (LOG_MAX - str4.length())));
            }
            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            if (sLogText.isEmpty()) {
                str3 = "";
            } else {
                str3 = sLogText + "\n";
            }
            sb.append(str3);
            sb.append(str4);
            sLogText = sb.toString();
        }
        refreshLogView();
    }

    private static void refreshLogView() {
        java.lang.String str = sLogText;
        if (sLogView == null) {
            return;
        }
        new android.os.Handler(android.os.Looper.getMainLooper()).post(new com.tencent.qqlitepro.plugin.PluginEngine$$ExternalSyntheticLambda7(str));
    }

    static /* synthetic */ void lambda$refreshLogView$0(java.lang.String str) {
        if (sLogView != null) {
            sLogView.setText(str);
        }
    }

    public static synchronized void ensureLoaded(android.content.Context context) {
        if (sLoaded && sCtx == context) {
            return;
        }
        sCtx = context;
        sCl = context != null ? context.getClassLoader() : null;
        sLoaded = false;
        reload();
    }

    public static synchronized void reload() {
        for (com.tencent.qqlitepro.plugin.PluginEngine.Feature feature : sFeatures) {
            if (feature.type == 1) {
                detachHook(feature);
            }
        }
        sPlugins.clear();
        sFeatures.clear();
        sMsgCallbacks.clear();
        sHookApplied = false;
        sApplyingHooks.set(false);
        if (sCtx == null) {
            sLoaded = true;
            return;
        }
        loadAll();
        sLoaded = true;
        appendLog("插件引擎初始化完成");
    }

    static java.io.File pluginDir(android.content.Context context) {
        java.io.File file = new java.io.File(context.getFilesDir(), "qqlite_plugins");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    private static void loadAll() {
        java.io.File[] fileArrListFiles = pluginDir(sCtx).listFiles(new com.tencent.qqlitepro.plugin.PluginEngine$$ExternalSyntheticLambda3());
        if (fileArrListFiles == null) {
            return;
        }
        java.util.Arrays.sort(fileArrListFiles, new com.tencent.qqlitepro.plugin.PluginEngine$$ExternalSyntheticLambda4());
        for (java.io.File file : fileArrListFiles) {
            if (com.tencent.qqlitepro.util.Config.isPluginEnabled(sCtx, file.getName())) {
                try {
                    java.lang.StringBuilder sb = new java.lang.StringBuilder();
                    com.tencent.qqlitepro.plugin.PluginEngine.Plugin pluginLoadOne = loadOne(file, sb);
                    if (pluginLoadOne != null) {
                        sPlugins.put(file.getName(), pluginLoadOne);
                        appendLog("[插件] 已加载 " + pluginLoadOne.name + " v" + pluginLoadOne.version);
                    } else {
                        java.lang.StringBuilder sb2 = new java.lang.StringBuilder();
                        sb2.append("[插件] 加载失败 ");
                        sb2.append(file.getName());
                        sb2.append(sb.length() > 0 ? " -> " + ((java.lang.Object) sb) : "");
                        appendLog(sb2.toString());
                    }
                } catch (java.lang.Throwable th) {
                    appendLog("[插件] 加载异常 " + file.getName() + " -> " + th);
                }
            }
        }
        applyHooks();
    }

    private static com.tencent.qqlitepro.plugin.PluginEngine.Plugin loadOne(java.io.File file, java.lang.StringBuilder sb) {
        org.mozilla.javascript.Context contextEnter = null;
        com.tencent.qqlitepro.plugin.PluginEngine.Meta meta = parseMeta(readFile(file));
        if (meta == null || meta.name == null || meta.name.isEmpty()) {
            if (sb != null && sb.length() == 0) {
                sb.append("缺少 @name");
            }
            return null;
        }
        com.tencent.qqlitepro.plugin.PluginEngine.Plugin plugin = new com.tencent.qqlitepro.plugin.PluginEngine.Plugin(file, file.getName(), meta.name.trim(), meta.version, meta.desc, meta.hasSettings, meta.code);
        try {
            contextEnter = org.mozilla.javascript.Context.enter();
            try {
                contextEnter.setOptimizationLevel(-1);
                java.lang.ClassLoader classLoader = org.mozilla.javascript.Context.class.getClassLoader();
                if (classLoader == null) {
                    classLoader = sCl;
                }
                if (classLoader != null) {
                    contextEnter.setApplicationClassLoader(classLoader);
                }
                org.mozilla.javascript.ScriptableObject scriptableObjectInitStandardObjects = contextEnter.initStandardObjects();
                org.mozilla.javascript.ScriptableObject.putProperty(scriptableObjectInitStandardObjects, "PLUGIN", org.mozilla.javascript.Context.javaToJS(new com.tencent.qqlitepro.plugin.PluginAPI(plugin), scriptableObjectInitStandardObjects));
                org.mozilla.javascript.ScriptableObject.putProperty(scriptableObjectInitStandardObjects, "console", org.mozilla.javascript.Context.javaToJS(new com.tencent.qqlitepro.plugin.ConsoleBridge(), scriptableObjectInitStandardObjects));
                contextEnter.evaluateString(scriptableObjectInitStandardObjects, plugin.mainCode, plugin.base, 1, null);
                plugin.scope = scriptableObjectInitStandardObjects;
                return plugin;
            } catch (java.lang.Throwable th) {
                if (sb != null && sb.length() == 0) {
                    sb.append(th.toString());
                }
                return null;
            }
        } catch (java.lang.Throwable th2) {
            if (sb != null && sb.length() == 0) {
                sb.append(th2.toString());
            }
            return null;
        } finally {
            if (contextEnter != null && org.mozilla.javascript.Context.getCurrentContext() != null) {
                org.mozilla.javascript.Context.exit();
            }
        }
    }

    static void invokeFunction(com.tencent.qqlitepro.plugin.PluginEngine.Plugin plugin, java.lang.String str) {
        callFn(plugin, str, null);
    }

    static void callFn(com.tencent.qqlitepro.plugin.PluginEngine.Plugin plugin, java.lang.String str, java.lang.String str2) {
        org.mozilla.javascript.Context contextEnter = null;
        if (plugin == null || plugin.scope == null) {
            return;
        }
        try {
            contextEnter = org.mozilla.javascript.Context.enter();
            try {
                contextEnter.setOptimizationLevel(-1);
                java.lang.Object obj = plugin.scope.get(str, plugin.scope);
                if (obj instanceof org.mozilla.javascript.Callable) {
                    ((org.mozilla.javascript.Callable) obj).call(contextEnter, plugin.scope, plugin.scope, str2 == null ? new java.lang.Object[0] : new java.lang.Object[]{str2});
                } else {
                    appendLog("[插件] 未找到函数 " + str + " (" + plugin.name + ")");
                }
                if (contextEnter == null) {
                }
            } catch (java.lang.Throwable thRethrow2) {
                try {
                    appendLog("[插件] " + plugin.name + " 调用 " + str + " 异常: " + thRethrow2);
                } finally {
                    if (contextEnter != null) {
                        org.mozilla.javascript.Context.exit();
                    }
                }
            }
        } catch (java.lang.Throwable th2) {
            java.lang.Throwable thRethrow3 = th2;
            contextEnter = null;
        }
    }

    private static void applyHooks() {
        if (sHookApplied || sCl == null) {
            sHookApplied = true;
        } else if (sApplyingHooks.compareAndSet(false, true)) {
            new java.lang.Thread(new com.tencent.qqlitepro.plugin.PluginEngine$$ExternalSyntheticLambda1(), "plugin-hook").start();
        }
    }

    static /* synthetic */ void lambda$applyHooks$3() {
        try {
            for (com.tencent.qqlitepro.plugin.PluginEngine.Feature feature : sFeatures) {
                if (feature.type == 1 && isFeatureEnabled(sCtx, feature)) {
                    attachHook(feature);
                }
            }
            sApplyingHooks.set(false);
            sHookApplied = true;
        } catch (java.lang.Throwable thRethrow3) {
            sApplyingHooks.set(false);
            sHookApplied = true;
            throw thRethrow3;
        }
    }

    private static void attachHook(com.tencent.qqlitepro.plugin.PluginEngine.Feature feature) {
        com.tencent.qqlitepro.plugin.PluginEngine.Plugin plugin = feature.plugin;
        if (feature.type != 1 || feature.hookHandle != null || plugin.scope == null || sCl == null) {
            return;
        }
        try {
            feature.hookHandle = de.robv.android.xposed.XposedBridge.hookAllMethods(java.lang.Class.forName(feature.hookCls, false, sCl), feature.hookMethod, new com.tencent.qqlitepro.plugin.PluginEngine.AnonymousClass1(plugin, feature));
            appendLog("[插件] 已挂载 hook: " + feature.hookCls + "." + feature.hookMethod + " (" + plugin.name + ")");
        } catch (java.lang.Throwable thRethrow3) {
            appendLog("[插件] hook 失败 " + feature.hookCls + "." + feature.hookMethod + " -> " + thRethrow3);
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.plugin.PluginEngine$1, reason: invalid class name */
    static class AnonymousClass1 extends de.robv.android.xposed.XC_MethodHook {
        final /* synthetic */ com.tencent.qqlitepro.plugin.PluginEngine.Feature val$f;
        final /* synthetic */ com.tencent.qqlitepro.plugin.PluginEngine.Plugin val$p;

        AnonymousClass1(com.tencent.qqlitepro.plugin.PluginEngine.Plugin plugin, com.tencent.qqlitepro.plugin.PluginEngine.Feature feature) {
            this.val$p = plugin;
            this.val$f = feature;
        }

        protected void beforeHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            com.tencent.qqlitepro.plugin.PluginEngine.callFn(this.val$p, this.val$f.hookFn, com.tencent.qqlitepro.plugin.PluginEngine.paramToJson(methodHookParam));
        }
    }

    private static void detachHook(com.tencent.qqlitepro.plugin.PluginEngine.Feature feature) {
        if (feature.hookHandle == null || feature.hookHandle.isEmpty()) {
            return;
        }
        java.util.Iterator<de.robv.android.xposed.XC_MethodHook.Unhook> it = feature.hookHandle.iterator();
        while (it.hasNext()) {
            try {
                it.next().unhook();
            } catch (java.lang.Throwable unused) {
            }
        }
        feature.hookHandle = null;
        appendLog("[插件] 已卸载 hook: " + feature.hookCls + "." + feature.hookMethod + " (" + feature.plugin.name + ")");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.lang.String paramToJson(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
        try {
            java.lang.Object[] objArr = (methodHookParam.thisObject == null || methodHookParam.args == null || methodHookParam.args.length <= 0) ? null : methodHookParam.args;
            if (objArr == null) {
                return "{}";
            }
            com.google.gson.JsonObject jsonObject = new com.google.gson.JsonObject();
            jsonObject.addProperty("this", java.lang.String.valueOf(methodHookParam.thisObject));
            jsonObject.add("args", new com.google.gson.Gson().toJsonTree(objArr));
            return jsonObject.toString();
        } catch (java.lang.Throwable unused) {
            return "{}";
        }
    }

    public static java.util.List<com.tencent.qqlitepro.plugin.PluginEngine.Feature> features() {
        return new java.util.ArrayList(sFeatures);
    }

    public static boolean isFeatureEnabled(android.content.Context context, com.tencent.qqlitepro.plugin.PluginEngine.Feature feature) {
        return featSet(context, feature.plugin.base).contains(feature.name);
    }

    public static void setFeatureEnabled(android.content.Context context, com.tencent.qqlitepro.plugin.PluginEngine.Feature feature, boolean z) {
        java.util.TreeSet<java.lang.String> treeSetFeatSet = featSet(context, feature.plugin.base);
        if (z) {
            treeSetFeatSet.add(feature.name);
        } else {
            treeSetFeatSet.remove(feature.name);
        }
        writeFeatFile(context, feature.plugin.base, treeSetFeatSet);
        java.lang.StringBuilder sb = new java.lang.StringBuilder("[功能] ");
        sb.append(feature.name);
        sb.append(" 已");
        sb.append(z ? "启用" : "停用");
        appendLog(sb.toString());
        if (feature.type == 1) {
            if (z) {
                attachHook(feature);
            } else {
                detachHook(feature);
            }
        }
    }

    public static void runFeature(com.tencent.qqlitepro.plugin.PluginEngine.Feature feature, java.lang.Runnable runnable) {
        java.lang.Thread thread = new java.lang.Thread(new com.tencent.qqlitepro.plugin.PluginEngine$$ExternalSyntheticLambda0(feature, feature.plugin, runnable), "plugin-feature");
        thread.setDaemon(true);
        thread.start();
    }

    static /* synthetic */ void lambda$runFeature$4(com.tencent.qqlitepro.plugin.PluginEngine.Feature feature, com.tencent.qqlitepro.plugin.PluginEngine.Plugin plugin, java.lang.Runnable runnable) {
        appendLog("[功能] 正在运行 " + feature.name);
        try {
            callFn(plugin, feature.runFn, null);
            appendLog("[功能] " + feature.name + " 执行完成");
        } catch (java.lang.Throwable thRethrow3) {
            appendLog("[功能] " + feature.name + " 异常: " + thRethrow3);
        }
        if (runnable != null) {
            try {
                runnable.run();
            } catch (java.lang.Throwable unused) {
            }
        }
    }

    private static java.util.TreeSet<java.lang.String> featSet(android.content.Context context, java.lang.String str) {
        java.util.TreeSet<java.lang.String> treeSet = new java.util.TreeSet<>();
        java.lang.String file = readFile(featFile(context, str));
        if (file != null) {
            for (java.lang.String str2 : file.split("\n")) {
                java.lang.String strTrim = str2.trim();
                if (!strTrim.isEmpty()) {
                    treeSet.add(strTrim);
                }
            }
        }
        return treeSet;
    }

    private static void writeFeatFile(android.content.Context context, java.lang.String str, java.util.TreeSet<java.lang.String> treeSet) {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        java.util.Iterator<java.lang.String> it = treeSet.iterator();
        while (it.hasNext()) {
            sb.append(it.next());
            sb.append('\n');
        }
        try {
            java.io.FileOutputStream fileOutputStream = new java.io.FileOutputStream(featFile(context, str));
            try {
                fileOutputStream.write(sb.toString().getBytes("UTF-8"));
            } finally {
                fileOutputStream.close();
            }
        } catch (java.lang.Throwable thRethrow3) {
            appendLog("[功能] 保存失败: " + thRethrow3);
        }
    }

    private static java.io.File featFile(android.content.Context context, java.lang.String str) {
        if (str.toLowerCase().endsWith(".js")) {
            str = str.substring(0, str.length() - 3);
        }
        return new java.io.File(pluginDir(context), str + ".features");
    }

    static final class Meta {
        java.lang.String code;
        java.lang.String desc;
        boolean hasSettings;
        java.lang.String name;
        java.lang.String version;

        Meta() {
        }
    }

    static com.tencent.qqlitepro.plugin.PluginEngine.Meta parseMeta(java.lang.String str) {
        int i;
        if (str == null || str.isEmpty()) {
            return null;
        }
        com.tencent.qqlitepro.plugin.PluginEngine.Meta meta = new com.tencent.qqlitepro.plugin.PluginEngine.Meta();
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        java.lang.String[] strArrSplit = str.split("\n", -1);
        int i2 = 0;
        boolean z = false;
        while (true) {
            if (i2 >= strArrSplit.length) {
                i = -1;
                break;
            }
            java.lang.String str2 = strArrSplit[i2];
            java.lang.String strTrim = str2.trim();
            if (z) {
                if (strTrim.startsWith("*")) {
                    strTrim = strTrim.substring(1).trim();
                }
                if (strTrim.startsWith("@name")) {
                    meta.name = tagVal(str2, "@name");
                } else if (strTrim.startsWith("@description") || strTrim.startsWith("@desc ")) {
                    appendDesc(sb, tagVal(str2, strTrim.startsWith("@desc") ? "@desc" : "@description"));
                } else if (strTrim.startsWith("@version")) {
                    meta.version = tagVal(str2, "@version");
                } else if (strTrim.startsWith("@hasSettings")) {
                    meta.hasSettings = "true".equalsIgnoreCase(tagVal(str2, "@hasSettings"));
                }
                if (str2.contains("*/")) {
                    i = i2 + 1;
                    break;
                }
                i2++;
            } else {
                if (!strTrim.startsWith("/*")) {
                    if (!strTrim.startsWith("//")) {
                        i = i2;
                        break;
                    }
                    java.lang.String strTrim2 = strTrim.substring(2).trim();
                    if (strTrim2.startsWith("@name")) {
                        meta.name = tagVal(strTrim2, "@name");
                    } else if (strTrim2.startsWith("@description") || strTrim2.startsWith("@desc ")) {
                        appendDesc(sb, tagVal(strTrim2, strTrim2.startsWith("@desc") ? "@desc" : "@description"));
                    } else if (strTrim2.startsWith("@version")) {
                        meta.version = tagVal(strTrim2, "@version");
                    } else if (strTrim2.startsWith("@hasSettings")) {
                        meta.hasSettings = "true".equalsIgnoreCase(tagVal(strTrim2, "@hasSettings"));
                    }
                } else {
                    z = true;
                }
                i2++;
            }
        }
        if (meta.name == null || meta.name.isEmpty()) {
            return null;
        }
        java.lang.StringBuilder sb2 = new java.lang.StringBuilder();
        for (int iMax = java.lang.Math.max(i, 0); iMax < strArrSplit.length; iMax++) {
            if (sb2.length() > 0) {
                sb2.append('\n');
            }
            sb2.append(strArrSplit[iMax]);
        }
        meta.code = sb2.toString();
        meta.desc = sb.toString().trim();
        meta.version = (meta.version == null || meta.version.isEmpty()) ? "1.0.0" : meta.version;
        return meta;
    }

    private static void appendDesc(java.lang.StringBuilder sb, java.lang.String str) {
        if (str == null || str.isEmpty()) {
            return;
        }
        if (sb.length() > 0) {
            sb.append('\n');
        }
        sb.append(str);
    }

    private static java.lang.String tagVal(java.lang.String str, java.lang.String str2) {
        int iIndexOf = str.indexOf(str2);
        if (iIndexOf < 0) {
            return "";
        }
        java.lang.String strSubstring = str.substring(iIndexOf + str2.length());
        if (strSubstring.startsWith(":")) {
            strSubstring = strSubstring.substring(1);
        }
        java.lang.String strTrim = strSubstring.trim();
        return strTrim.endsWith("*/") ? strTrim.substring(0, strTrim.length() - 2).trim() : strTrim;
    }

    private static java.lang.String readFile(java.io.File file) {
        if (file != null) {
            try {
                if (file.exists()) {
                    byte[] bArr = new byte[(int) java.lang.Math.min(file.length(), 4194304L)];
                    java.io.FileInputStream fileInputStream = new java.io.FileInputStream(file);
                    try {
                        return new java.lang.String(bArr, 0, java.lang.Math.max(fileInputStream.read(bArr), 0), "UTF-8");
                    } finally {
                        fileInputStream.close();
                    }
                }
            } catch (java.lang.Throwable unused) {
            }
        }
        return "";
    }

    public static final class Plugin {
        final java.lang.String base;
        final java.lang.String desc;
        final java.io.File file;
        final boolean hasSettings;
        final java.lang.String mainCode;
        final java.lang.String name;
        volatile org.mozilla.javascript.Scriptable scope;
        final java.lang.String version;

        Plugin(java.io.File file, java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.String str4, boolean z, java.lang.String str5) {
            this.file = file;
            this.base = str;
            this.name = str2;
            this.version = str3;
            this.desc = str4;
            this.hasSettings = z;
            this.mainCode = str5;
        }
    }

    public static final class Feature {
        public static final int TYPE_HOOK = 1;
        public static final int TYPE_RUN = 0;
        volatile java.lang.String desc;
        volatile java.lang.String hookCls;
        volatile java.lang.String hookFn;
        volatile java.util.Set<de.robv.android.xposed.XC_MethodHook.Unhook> hookHandle;
        volatile java.lang.String hookMethod;
        public final java.lang.String name;
        final com.tencent.qqlitepro.plugin.PluginEngine.Plugin plugin;
        volatile java.lang.String runFn;
        final int type;

        public java.lang.String getDesc() {
            return this.desc;
        }

        public java.lang.String hookMethodName() {
            return this.hookMethod == null ? "" : this.hookMethod;
        }

        public boolean isHook() {
            return this.type == 1;
        }

        Feature(com.tencent.qqlitepro.plugin.PluginEngine.Plugin plugin, java.lang.String str, java.lang.String str2, java.lang.String str3) {
            this.plugin = plugin;
            this.name = str;
            this.type = 0;
            this.desc = str2;
            this.runFn = str3;
        }

        Feature(com.tencent.qqlitepro.plugin.PluginEngine.Plugin plugin, java.lang.String str, int i) {
            this.plugin = plugin;
            this.name = str;
            this.type = i;
            this.desc = "";
        }

        public java.lang.String pluginName() {
            return this.plugin.name;
        }

        public java.lang.String pluginVersion() {
            return this.plugin.version;
        }

        public boolean pluginHasSettings() {
            return this.plugin.hasSettings;
        }

        public java.lang.String pluginBase() {
            return this.plugin.base;
        }
    }

    static void registerFeature0(com.tencent.qqlitepro.plugin.PluginEngine.Plugin plugin, java.lang.String str, java.lang.String str2, java.lang.String str3) {
        for (com.tencent.qqlitepro.plugin.PluginEngine.Feature feature : sFeatures) {
            if (feature.type == 0 && feature.plugin == plugin && feature.name.equals(str)) {
                feature.desc = str2;
                feature.runFn = str3;
                return;
            }
        }
        sFeatures.add(new com.tencent.qqlitepro.plugin.PluginEngine.Feature(plugin, str, str2, str3));
    }

    static void registerFeatureHook(com.tencent.qqlitepro.plugin.PluginEngine.Plugin plugin, java.lang.String str, java.lang.String str2, java.lang.String str3) {
        java.lang.String str4 = str + "." + str2;
        for (com.tencent.qqlitepro.plugin.PluginEngine.Feature feature : sFeatures) {
            if (feature.type == 1 && feature.plugin == plugin && feature.name.equals(str4)) {
                feature.hookFn = str3;
                return;
            }
        }
        com.tencent.qqlitepro.plugin.PluginEngine.Feature feature2 = new com.tencent.qqlitepro.plugin.PluginEngine.Feature(plugin, str4, 1);
        feature2.hookCls = str;
        feature2.hookMethod = str2;
        feature2.hookFn = str3;
        sFeatures.add(feature2);
    }

    static java.io.File pluginDirFor(com.tencent.qqlitepro.plugin.PluginEngine.Plugin plugin) {
        android.content.Context context = sCtx;
        if (context == null) {
            return null;
        }
        return pluginDir(context);
    }

    static void toastOnMain(java.lang.String str) {
        android.content.Context context = sCtx;
        if (context == null) {
            return;
        }
        new android.os.Handler(android.os.Looper.getMainLooper()).post(new com.tencent.qqlitepro.plugin.PluginEngine$$ExternalSyntheticLambda6(context, str));
    }

    static void registerMessageCb(com.tencent.qqlitepro.plugin.PluginEngine.Plugin plugin, java.lang.String str) {
        if (plugin == null || str == null || str.isEmpty()) {
            return;
        }
        java.util.concurrent.CopyOnWriteArrayList copyOnWriteArrayList = (java.util.concurrent.CopyOnWriteArrayList) sMsgCallbacks.computeIfAbsent(plugin, new com.tencent.qqlitepro.plugin.PluginEngine$$ExternalSyntheticLambda2());
        if (copyOnWriteArrayList.contains(str)) {
            return;
        }
        copyOnWriteArrayList.add(str);
    }

    static /* synthetic */ java.util.concurrent.CopyOnWriteArrayList lambda$registerMessageCb$6(com.tencent.qqlitepro.plugin.PluginEngine.Plugin plugin) {
        return new java.util.concurrent.CopyOnWriteArrayList();
    }

    public static boolean hasMessageCallbacks() {
        return !sMsgCallbacks.isEmpty();
    }

    public static void dispatchIncomingMessage(java.lang.String str) {
        if (sMsgCallbacks.isEmpty() || str == null) {
            return;
        }
        java.lang.Thread thread = new java.lang.Thread(new com.tencent.qqlitepro.plugin.PluginEngine$$ExternalSyntheticLambda5(str), "plugin-msg-dispatch");
        thread.setDaemon(true);
        thread.start();
    }

    static /* synthetic */ void lambda$dispatchIncomingMessage$7(java.lang.String str) {
        for (java.util.Map.Entry<com.tencent.qqlitepro.plugin.PluginEngine.Plugin, java.util.concurrent.CopyOnWriteArrayList<java.lang.String>> entry : sMsgCallbacks.entrySet()) {
            com.tencent.qqlitepro.plugin.PluginEngine.Plugin key = entry.getKey();
            for (java.lang.String str2 : entry.getValue()) {
                try {
                    callFn(key, str2, str);
                } catch (java.lang.Throwable thRethrow3) {
                    appendLog("[" + key.name + "] 消息回调 " + str2 + " 异常: " + thRethrow3);
                }
            }
        }
    }
}
