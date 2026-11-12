# Xposed 模块入口与全部 hook 类均通过反射/类名加载，禁止混淆与裁剪
-keep class com.tencent.qqlitepro.** { *; }
-keep class de.robv.android.xposed.** { *; }
-dontwarn de.robv.android.xposed.**

# Rhino / JavaMail / Gson 内部大量使用反射
-keep class org.mozilla.javascript.** { *; }
-dontwarn org.mozilla.javascript.**
-keep class javax.mail.** { *; }
-keep class com.sun.mail.** { *; }
-keep class javax.activation.** { *; }
-dontwarn javax.mail.**
-dontwarn com.sun.mail.**
-dontwarn javax.activation.**
-keep class com.google.gson.** { *; }
