package com.tencent.qqlitepro.plugin;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class PluginEngine$$ExternalSyntheticLambda3 implements java.io.FilenameFilter {
    @Override // java.io.FilenameFilter
    public final boolean accept(java.io.File file, java.lang.String str) {
        return str.toLowerCase().endsWith(".js");
    }
}
