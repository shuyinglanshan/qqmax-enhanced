package com.tencent.qqlitepro.plugin;

/* JADX INFO: loaded from: classes.dex */
public class ConsoleBridge {
    public void log(java.lang.String str) {
        if (str == null) {
            str = "";
        }
        com.tencent.qqlitepro.plugin.PluginEngine.appendLog(str);
    }

    public void info(java.lang.String str) {
        if (str == null) {
            str = "";
        }
        com.tencent.qqlitepro.plugin.PluginEngine.appendLog(str);
    }

    public void error(java.lang.String str) {
        java.lang.StringBuilder sb = new java.lang.StringBuilder("[错误] ");
        if (str == null) {
            str = "";
        }
        sb.append(str);
        com.tencent.qqlitepro.plugin.PluginEngine.appendLog(sb.toString());
    }

    public void warn(java.lang.String str) {
        java.lang.StringBuilder sb = new java.lang.StringBuilder("[警告] ");
        if (str == null) {
            str = "";
        }
        sb.append(str);
        com.tencent.qqlitepro.plugin.PluginEngine.appendLog(sb.toString());
    }
}
