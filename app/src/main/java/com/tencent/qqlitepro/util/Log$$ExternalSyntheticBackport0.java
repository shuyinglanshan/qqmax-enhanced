package com.tencent.qqlitepro.util;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class Log$$ExternalSyntheticBackport0 {
    private Log$$ExternalSyntheticBackport0() {
    }

    public static /* synthetic */ int m(java.lang.reflect.Method method) {
        return method.getParameterTypes().length;
    }

    public static /* synthetic */ java.lang.String m(java.lang.CharSequence charSequence, java.lang.CharSequence[] charSequenceArr) {
        if (charSequence == null) {
            throw new java.lang.NullPointerException("delimiter");
        }
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        if (charSequenceArr.length > 0) {
            sb.append(charSequenceArr[0]);
            for (int i = 1; i < charSequenceArr.length; i++) {
                sb.append(charSequence);
                sb.append(charSequenceArr[i]);
            }
        }
        return sb.toString();
    }
}
