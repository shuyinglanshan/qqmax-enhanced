package com.tencent.qqlitepro.util;

/* JADX INFO: loaded from: classes.dex */
public final class Config {
    /* 复用一个 Gson 实例：每个 Getter 都 new Gson() 在 hook 高频调用下会显著拖慢主线程 */
    private static final com.google.gson.Gson GSON = new com.google.gson.Gson();

    private static final java.lang.String CONFIG_FILE = "qqlite_settings.json";
    private static final boolean DEFAULT_AUTO_TASK = false;
    private static final java.lang.String DEFAULT_CATGIRL_SUFFIX = "喵~";
    public static final java.lang.String DEFAULT_DOWNLOAD_DIR = "/sdcard/QQMax增强/downloads";
    private static final boolean DEFAULT_ENABLED = true;
    private static final int DEFAULT_MODE = 1;
    private static final int DEFAULT_PORT = 8081;
    private static final java.lang.String KEY_AUTO_CHAT_TYPE = "auto_task_chat_type";
    private static final java.lang.String KEY_AUTO_CONTENT = "auto_task_content";
    private static final java.lang.String KEY_AUTO_LAST_SENT_DAY = "auto_task_last_sent_day";
    private static final java.lang.String KEY_AUTO_PEERS = "auto_task_peers";
    private static final java.lang.String KEY_AUTO_PEER_UID = "auto_task_peer_uid";
    private static final java.lang.String KEY_AUTO_REPLY_AT = "auto_reply_at";
    private static final java.lang.String KEY_AUTO_REPLY_CONTENT = "auto_reply_content";
    private static final java.lang.String KEY_AUTO_REPLY_ENABLED = "auto_reply_enabled";
    private static final java.lang.String KEY_AUTO_REPLY_PEERS = "auto_reply_peers";
    private static final java.lang.String KEY_AUTO_TASK = "auto_task_enabled";
    private static final java.lang.String KEY_AUTO_TASK_RULES = "auto_task_rules";
    private static final java.lang.String KEY_AUTO_TASK_SENT = "auto_task_sent";
    private static final java.lang.String KEY_AUTO_TIME = "auto_task_time";
    private static final java.lang.String KEY_BULLETIN_ALL = "bulletin_all_enabled";
    private static final java.lang.String KEY_CATGIRL_ENABLED = "catgirl_enabled";
    private static final java.lang.String KEY_CATGIRL_SUFFIX = "catgirl_suffix";
    private static final java.lang.String KEY_DOWNLOAD_DIR = "download_dir";
    private static final java.lang.String KEY_ENABLED = "enabled";
    private static final java.lang.String KEY_FORCE_NOTICE_READ = "force_notice_read";
    private static final java.lang.String KEY_KEYWORD_REPLY_CONTENT = "keyword_reply_content";
    private static final java.lang.String KEY_KEYWORD_REPLY_ENABLED = "keyword_reply_enabled";
    private static final java.lang.String KEY_KEYWORD_REPLY_KEYWORDS = "keyword_reply_keywords";
    private static final java.lang.String KEY_KEYWORD_REPLY_PEERS = "keyword_reply_peers";
    private static final java.lang.String KEY_KEYWORD_REPLY_RULES = "keyword_reply_rules";
    private static final java.lang.String KEY_KW_AI_API_KEY = "keyword_ai_api_key";
    private static final java.lang.String KEY_KW_AI_MODEL = "keyword_ai_model";
    private static final java.lang.String KEY_KW_AI_PERSONA = "keyword_ai_persona";
    private static final java.lang.String KEY_KW_AI_URL = "keyword_ai_url";
    private static final java.lang.String KEY_MAIL_ADDR = "mail_addr";
    private static final java.lang.String KEY_MAIL_AUTH_CODE = "mail_auth_code";
    private static final java.lang.String KEY_MAIL_DND = "mail_dnd";
    private static final java.lang.String KEY_MAIL_ENABLED = "mail_enabled";
    /* 官方群加入提示开关（加入后可关掉启动时的 toast） */
    private static final java.lang.String KEY_GROUP_TOAST = "group_toast_enabled";
    /* 临时会话功能总开关（默认开启，关闭后首页不显示卡片） */
    private static final java.lang.String KEY_TEMP_CHAT_ENABLED = "temp_chat_enabled";
    private static final java.lang.String KEY_MAIL_INBOX_CACHE = "mail_inbox_cache";
    private static final java.lang.String KEY_MAIL_LAST_UID = "mail_last_uid";
    private static final java.lang.String KEY_MAIL_NAME = "mail_name";
    private static final java.lang.String KEY_MAIL_PINNED = "mail_pinned";
    private static final java.lang.String KEY_MAIL_POLL_MS = "mail_poll_ms";
    private static final java.lang.String KEY_MAIL_UNREAD_COUNT = "mail_unread_count";
    private static final java.lang.String KEY_MAIL_VERIFIED = "mail_verified";
    private static final java.lang.String KEY_MAIL_WEB_BG = "mail_web_bg";
    private static final java.lang.String KEY_MODE = "mode";
    private static final java.lang.String KEY_NATIVE_PICKER = "native_picker";
    private static final java.lang.String KEY_PLUGINS_ENABLED = "plugins_enabled";
    private static final java.lang.String KEY_PORT = "http_port";
    private static final java.lang.String KEY_QQ_DYNAMIC_COMMENT = "qq_dynamic_comment";
    private static final java.lang.String KEY_QQ_DYNAMIC_COMMENT_COUNT = "qq_dynamic_comment_count";
    private static final java.lang.String KEY_QQ_DYNAMIC_COMMENT_TEXT = "qq_dynamic_comment_text";
    private static final java.lang.String KEY_QQ_DYNAMIC_ENABLED = "qq_dynamic_enabled";
    private static final java.lang.String KEY_QQ_DYNAMIC_INTERVAL = "qq_dynamic_interval";
    private static final java.lang.String KEY_QQ_DYNAMIC_LIKE = "qq_dynamic_like";
    private static final java.lang.String KEY_QQ_DYNAMIC_LIKE_COUNT = "qq_dynamic_like_count";
    private static final java.lang.String KEY_QQ_DYNAMIC_TIME = "qq_dynamic_time";
    private static final java.lang.String KEY_QQ_DYNAMIC_WHITELIST = "qq_dynamic_whitelist";
    private static final java.lang.String KEY_SEND_MODE = "send_mode";
    private static final java.lang.String KEY_SHARD_ENABLED = "shard_enabled";
    private static final java.lang.String KEY_SUMMARY_API_KEY = "summary_api_key";
    private static final java.lang.String KEY_SUMMARY_MODEL = "summary_model";
    private static final java.lang.String KEY_SUMMARY_URL = "summary_url";
    private static final java.lang.String KEY_SYSTEM_MEDIA_PICKER = "system_media_picker";
    private static final java.lang.String KEY_TRANSLATE_ENABLED = "translate_enabled";
    private static final java.lang.String LEGACY_DEFAULT_DOWNLOAD_DIR = "/sdcard/QQMax增强/下载";
    public static final int MODE_BLOCK = 1;
    public static final int MODE_RESTORE = 2;
    private static final java.lang.String PREFS = "antirecall_settings";
    private static final long RELOAD_MS = 800;
    private static volatile int sAutoChatType = 2;
    private static volatile java.lang.String sAutoContent = "";
    private static volatile java.lang.String sAutoLastSentDay = "";
    private static volatile java.lang.String sAutoPeerUid = "";
    private static volatile java.lang.String sAutoPeers = "";
    private static volatile boolean sAutoReplyAt = false;
    private static volatile java.lang.String sAutoReplyContent = "";
    private static volatile boolean sAutoReplyEnabled = false;
    private static volatile java.lang.String sAutoReplyPeers = "";
    private static volatile boolean sAutoTask = false;
    private static volatile java.lang.String sAutoTaskRules = "";
    private static volatile java.lang.String sAutoTaskSent = "";
    private static volatile java.lang.String sAutoTime = "08:00";
    private static volatile boolean sBulletinAll = true;
    private static volatile boolean sCatgirlEnabled = false;
    private static volatile java.lang.String sCatgirlSuffix = "";
    private static volatile java.lang.String sDownloadDir = "";
    private static volatile boolean sEnabled = true;
    private static volatile java.lang.String sForceNoticeRead = "";
    private static volatile java.lang.String sKeywordReplyContent = "";
    private static volatile boolean sKeywordReplyEnabled = false;
    private static volatile java.lang.String sKeywordReplyKeywords = "";
    private static volatile java.lang.String sKeywordReplyPeers = "";
    private static volatile java.lang.String sKeywordReplyRules = "";
    private static volatile java.lang.String sKwAiApiKey = "";
    private static volatile java.lang.String sKwAiModel = "";
    private static volatile java.lang.String sKwAiPersona = "";
    private static volatile java.lang.String sKwAiUrl = "";
    private static long sLastRead = 0;
    /* 配置文件内容指纹：用于跳过无变化的重读 */
    private static long sCfgLength = -1L;
    private static long sCfgMtime = 0L;
    private static volatile boolean sLoaded = false;
    private static volatile java.lang.String sMailAddr = "";
    private static volatile java.lang.String sMailAuthCode = "";
    private static volatile boolean sMailDnd = false;
    private static volatile boolean sMailEnabled = true;
    private static volatile boolean sGroupToastEnabled = true;
    private static volatile boolean sTempChatEnabled = true;
    private static volatile java.lang.String sMailInboxCache = "";
    private static volatile java.lang.String sMailLastUid = "";
    private static volatile java.lang.String sMailName = "";
    private static volatile boolean sMailPinned = false;
    private static volatile long sMailPollMs = 10000;
    private static volatile int sMailUnreadCount = 0;
    private static volatile boolean sMailVerified = false;
    private static volatile java.lang.String sMailWebBg = "";
    private static volatile int sMode = 1;
    private static volatile boolean sNativePicker = true;
    private static volatile int sPort = 8081;
    private static volatile boolean sQqDynamicComment = false;
    private static volatile int sQqDynamicCommentCount = 0;
    private static volatile java.lang.String sQqDynamicCommentText = "不错哦~";
    private static volatile boolean sQqDynamicEnabled = false;
    private static volatile int sQqDynamicInterval = 30;
    private static volatile boolean sQqDynamicLike = true;
    private static volatile int sQqDynamicLikeCount = 0;
    private static volatile java.lang.String sQqDynamicTime = "09:00";
    private static volatile java.lang.String sQqDynamicWhitelist = "";
    private static volatile int sSendMode = 0;
    private static volatile boolean sShardEnabled = false;
    private static volatile java.lang.String sSummaryApiKey = "";
    private static volatile java.lang.String sSummaryModel = "";
    private static volatile java.lang.String sSummaryUrl = "";
    private static volatile boolean sSystemMediaPicker = true;
    private static volatile boolean sTranslateEnabled = true;
    private static volatile long sVersion = java.lang.System.currentTimeMillis();
    private static volatile java.lang.String sPluginsEnabled = "";

    public static long settingsVersion() {
        return sVersion;
    }

    private Config() {
    }

    public static boolean enabled(android.content.Context context) {
        if (!fastHit()) {
            reload(context);
        }
        return sEnabled;
    }

    public static int mode(android.content.Context context) {
        if (!fastHit()) {
            reload(context);
        }
        return sMode;
    }

    public static int port(android.content.Context context) {
        if (!fastHit()) {
            reload(context);
        }
        return sPort;
    }

    public static int sendMode(android.content.Context context) {
        if (!fastHit()) {
            reload(context);
        }
        return sSendMode;
    }

    public static boolean translateEnabled(android.content.Context context) {
        if (!fastHit()) {
            reload(context);
        }
        return sTranslateEnabled;
    }

    public static void setTranslateEnabled(android.content.Context context, boolean z) {
        sTranslateEnabled = z;
        markDirtyAndPersist(context);
    }

    public static boolean bulletinAllEnabled(android.content.Context context) {
        if (!fastHit()) {
            reload(context);
        }
        return sBulletinAll;
    }

    public static void setBulletinAllEnabled(android.content.Context context, boolean z) {
        sBulletinAll = z;
        markDirtyAndPersist(context);
    }

    public static boolean systemMediaPickerEnabled(android.content.Context context) {
        if (!fastHit()) {
            reload(context);
        }
        return sSystemMediaPicker;
    }

    public static void setSystemMediaPickerEnabled(android.content.Context context, boolean z) {
        sSystemMediaPicker = z;
        markDirtyAndPersist(context);
    }

    public static boolean catgirlEnabled(android.content.Context context) {
        if (!fastHit()) {
            reload(context);
        }
        return sCatgirlEnabled;
    }

    public static void setCatgirlEnabled(android.content.Context context, boolean z) {
        sCatgirlEnabled = z;
        markDirtyAndPersist(context);
    }

    public static java.lang.String catgirlSuffixRaw(android.content.Context context) {
        if (!fastHit()) {
            reload(context);
        }
        return sCatgirlSuffix == null ? "" : sCatgirlSuffix;
    }

    public static java.lang.String catgirlSuffix(android.content.Context context) {
        java.lang.String strCatgirlSuffixRaw = catgirlSuffixRaw(context);
        return strCatgirlSuffixRaw.isEmpty() ? DEFAULT_CATGIRL_SUFFIX : strCatgirlSuffixRaw;
    }

    public static void setCatgirlSuffix(android.content.Context context, java.lang.String str) {
        sCatgirlSuffix = str == null ? "" : str.trim();
        markDirtyAndPersist(context);
    }

    public static boolean nativePickerEnabled(android.content.Context context) {
        if (!fastHit()) {
            reload(context);
        }
        return sNativePicker;
    }

    public static void setNativePickerEnabled(android.content.Context context, boolean z) {
        sNativePicker = z;
        markDirtyAndPersist(context);
    }

    public static void setSendMode(android.content.Context context, int i) {
        if (i != 0 && i != 1) {
            i = 0;
        }
        sSendMode = i;
        if (!fastHit()) {
            reload(context);
        }
        sSendMode = i;
        persist(context);
    }

    public static boolean shardEnabled(android.content.Context context) {
        if (!fastHit()) {
            reload(context);
        }
        return sShardEnabled;
    }

    public static void setShardEnabled(android.content.Context context, boolean z) {
        sShardEnabled = z;
        markDirtyAndPersist(context);
    }

    public static synchronized java.lang.String downloadDir(android.content.Context context) {
        if (!fastHit()) {
            reload(context);
        }
        if (sDownloadDir != null && !sDownloadDir.trim().isEmpty() && !LEGACY_DEFAULT_DOWNLOAD_DIR.equals(sDownloadDir.trim())) {
            java.lang.String custom = sDownloadDir.trim();
            /*
             * 路径修复：
             * 用户/旧版本写入的多为 /sdcard/QQMax增强/downloads 这类绝对路径。
             * Android 10+ 分区存储下该路径不可写，继续返回会让下载直接 ENOENT。
             * 这里把不可写的目录映射到应用自己的可写目录（同名子目录），保证下载可用。
             */
            java.io.File f = new java.io.File(custom);
            if (com.tencent.qqlitepro.util.Storage.ensureWritable(f)) {
                return custom;
            }
            return com.tencent.qqlitepro.util.Storage.dir(context, "downloads").getAbsolutePath();
        }
        return com.tencent.qqlitepro.util.Storage.dir(context, "downloads").getAbsolutePath();
    }

    public static synchronized void setDownloadDir(android.content.Context context, java.lang.String str) {
        sDownloadDir = str == null ? "" : str.trim();
        sDownloadDir = sDownloadDir.endsWith("/") ? sDownloadDir.substring(0, sDownloadDir.length() - 1) : sDownloadDir;
        markDirtyAndPersist(context);
    }

    public static boolean qqDynamicEnabled(android.content.Context context) {
        if (!fastHit()) {
            reload(context);
        }
        return sQqDynamicEnabled;
    }

    public static void setQqDynamicEnabled(android.content.Context context, boolean z) {
        sQqDynamicEnabled = z;
        markDirtyAndPersist(context);
    }

    public static int qqDynamicInterval(android.content.Context context) {
        if (!fastHit()) {
            reload(context);
        }
        if (sQqDynamicInterval > 0) {
            return sQqDynamicInterval;
        }
        return 30;
    }

    public static void setQqDynamicInterval(android.content.Context context, int i) {
        if (i < 1) {
            i = 30;
        }
        if (i > 1440) {
            i = 1440;
        }
        sQqDynamicInterval = i;
        markDirtyAndPersist(context);
    }

    public static boolean qqDynamicLike(android.content.Context context) {
        if (!fastHit()) {
            reload(context);
        }
        return sQqDynamicLike;
    }

    public static void setQqDynamicLike(android.content.Context context, boolean z) {
        sQqDynamicLike = z;
        markDirtyAndPersist(context);
    }

    public static boolean qqDynamicComment(android.content.Context context) {
        if (!fastHit()) {
            reload(context);
        }
        return sQqDynamicComment;
    }

    public static void setQqDynamicComment(android.content.Context context, boolean z) {
        sQqDynamicComment = z;
        markDirtyAndPersist(context);
    }

    public static java.lang.String qqDynamicWhitelist(android.content.Context context) {
        if (!fastHit()) {
            reload(context);
        }
        return sQqDynamicWhitelist;
    }

    public static void setQqDynamicWhitelist(android.content.Context context, java.lang.String str) {
        if (str == null) {
            str = "";
        }
        sQqDynamicWhitelist = str;
        markDirtyAndPersist(context);
    }

    public static java.lang.String qqDynamicCommentText(android.content.Context context) {
        if (!fastHit()) {
            reload(context);
        }
        return sQqDynamicCommentText.trim().isEmpty() ? "不错哦~" : sQqDynamicCommentText;
    }

    public static void setQqDynamicCommentText(android.content.Context context, java.lang.String str) {
        if (str == null) {
            str = "";
        }
        sQqDynamicCommentText = str;
        markDirtyAndPersist(context);
    }

    public static int qqDynamicLikeCount(android.content.Context context) {
        if (!fastHit()) {
            reload(context);
        }
        return java.lang.Math.max(0, sQqDynamicLikeCount);
    }

    public static void setQqDynamicLikeCount(android.content.Context context, int i) {
        sQqDynamicLikeCount = java.lang.Math.max(0, i);
        markDirtyAndPersist(context);
    }

    public static int qqDynamicCommentCount(android.content.Context context) {
        if (!fastHit()) {
            reload(context);
        }
        return java.lang.Math.max(0, sQqDynamicCommentCount);
    }

    public static void setQqDynamicCommentCount(android.content.Context context, int i) {
        sQqDynamicCommentCount = java.lang.Math.max(0, i);
        markDirtyAndPersist(context);
    }

    public static void setQqDynamicAll(android.content.Context context, boolean z, java.lang.String str, boolean z2, boolean z3, java.lang.String str2, java.lang.String str3) {
        sQqDynamicEnabled = z;
        sQqDynamicTime = normalizeTime(str);
        sQqDynamicLike = z2;
        sQqDynamicComment = z3;
        if (str2 == null) {
            str2 = "";
        }
        sQqDynamicWhitelist = str2;
        if (str3 == null) {
            str3 = "";
        }
        sQqDynamicCommentText = str3;
        markDirtyAndPersist(context);
    }

    public static void setQqDynamicAll(android.content.Context context, boolean z, java.lang.String str, boolean z2, boolean z3, java.lang.String str2, java.lang.String str3, int i, int i2) {
        setQqDynamicAll(context, z, str, z2, z3, str2, str3);
        sQqDynamicLikeCount = java.lang.Math.max(0, i);
        sQqDynamicCommentCount = java.lang.Math.max(0, i2);
        markDirtyAndPersist(context);
    }

    public static java.lang.String qqDynamicTime(android.content.Context context) {
        if (!fastHit()) {
            reload(context);
        }
        return sQqDynamicTime;
    }

    public static void setQqDynamicTime(android.content.Context context, java.lang.String str) {
        sQqDynamicTime = normalizeTime(str);
        markDirtyAndPersist(context);
    }

    public static java.lang.String normalizeTime(java.lang.String str) {
        if (str == null) {
            return "09:00";
        }
        java.lang.String strTrim = str.trim();
        if (strTrim.matches("\\d{1,2}:\\d{2}")) {
            java.lang.String[] strArrSplit = strTrim.split(":");
            int i = java.lang.Integer.parseInt(strArrSplit[0]);
            int i2 = java.lang.Integer.parseInt(strArrSplit[1]);
            if (i >= 0 && i <= 23 && i2 >= 0 && i2 <= 59) {
                return java.lang.String.format(java.util.Locale.US, "%02d:%02d", java.lang.Integer.valueOf(i), java.lang.Integer.valueOf(i2));
            }
        }
        return "09:00";
    }

    public static boolean autoTaskEnabled(android.content.Context context) {
        if (!fastHit()) {
            reload(context);
        }
        return sAutoTask;
    }

    public static java.lang.String autoContent(android.content.Context context) {
        if (!fastHit()) {
            reload(context);
        }
        return sAutoContent;
    }

    public static java.lang.String autoTime(android.content.Context context) {
        if (!fastHit()) {
            reload(context);
        }
        return sAutoTime;
    }

    public static int autoChatType(android.content.Context context) {
        if (!fastHit()) {
            reload(context);
        }
        return sAutoChatType;
    }

    public static java.lang.String autoPeerUid(android.content.Context context) {
        if (!fastHit()) {
            reload(context);
        }
        return sAutoPeerUid;
    }

    public static java.lang.String autoPeers(android.content.Context context) {
        if (!fastHit()) {
            reload(context);
        }
        return sAutoPeers;
    }

    public static java.lang.String autoLastSentDay(android.content.Context context) {
        if (!fastHit()) {
            reload(context);
        }
        return sAutoLastSentDay;
    }

    public static boolean keywordReplyEnabled(android.content.Context context) {
        if (!fastHit()) {
            reload(context);
        }
        return sKeywordReplyEnabled;
    }

    public static java.lang.String keywordReplyKeywords(android.content.Context context) {
        if (!fastHit()) {
            reload(context);
        }
        return sKeywordReplyKeywords;
    }

    public static java.lang.String keywordReplyContent(android.content.Context context) {
        if (!fastHit()) {
            reload(context);
        }
        return sKeywordReplyContent;
    }

    public static java.lang.String keywordReplyPeers(android.content.Context context) {
        if (!fastHit()) {
            reload(context);
        }
        return sKeywordReplyPeers;
    }

    public static void setKeywordReply(android.content.Context context, boolean z, java.lang.String str, java.lang.String str2, java.lang.String str3) {
        sKeywordReplyEnabled = z;
        if (str == null) {
            str = "";
        }
        sKeywordReplyKeywords = str;
        if (str2 == null) {
            str2 = "";
        }
        sKeywordReplyContent = str2;
        if (str3 == null) {
            str3 = "";
        }
        sKeywordReplyPeers = str3;
        markDirtyAndPersist(context);
    }

    public static java.lang.String keywordRules(android.content.Context context) {
        if (!fastHit()) {
            reload(context);
        }
        return sKeywordReplyRules;
    }

    public static void setKeywordRules(android.content.Context context, boolean z, java.lang.String str, java.lang.String str2) {
        sKeywordReplyEnabled = z;
        if (str == null) {
            str = "";
        }
        sKeywordReplyRules = str;
        if (str2 == null) {
            str2 = "";
        }
        sKeywordReplyPeers = str2;
        markDirtyAndPersist(context);
    }

    public static boolean autoReplyEnabled(android.content.Context context) {
        if (!fastHit()) {
            reload(context);
        }
        return sAutoReplyEnabled;
    }

    public static java.lang.String autoReplyContent(android.content.Context context) {
        if (!fastHit()) {
            reload(context);
        }
        return sAutoReplyContent;
    }

    public static java.lang.String autoReplyPeers(android.content.Context context) {
        if (!fastHit()) {
            reload(context);
        }
        return sAutoReplyPeers;
    }

    public static boolean autoReplyAt(android.content.Context context) {
        if (!fastHit()) {
            reload(context);
        }
        return sAutoReplyAt;
    }

    public static void setAutoReply(android.content.Context context, boolean z, java.lang.String str, java.lang.String str2) {
        setAutoReply(context, z, str, str2, false);
    }

    public static void setAutoReply(android.content.Context context, boolean z, java.lang.String str, java.lang.String str2, boolean z2) {
        sAutoReplyEnabled = z;
        if (str == null) {
            str = "";
        }
        sAutoReplyContent = str;
        if (str2 == null) {
            str2 = "";
        }
        sAutoReplyPeers = str2;
        sAutoReplyAt = z2;
        markDirtyAndPersist(context);
    }

    public static java.lang.String summaryUrl(android.content.Context context) {
        if (!fastHit()) {
            reload(context);
        }
        return sSummaryUrl;
    }

    public static java.lang.String summaryApiKey(android.content.Context context) {
        if (!fastHit()) {
            reload(context);
        }
        return sSummaryApiKey;
    }

    public static java.lang.String summaryModel(android.content.Context context) {
        if (!fastHit()) {
            reload(context);
        }
        return sSummaryModel;
    }

    public static void setSummaryConfig(android.content.Context context, java.lang.String str, java.lang.String str2, java.lang.String str3) {
        sSummaryUrl = str == null ? "" : str.trim();
        sSummaryApiKey = str2 == null ? "" : str2.trim();
        sSummaryModel = str3 != null ? str3.trim() : "";
        markDirtyAndPersist(context);
    }

    public static java.lang.String keywordAiUrl(android.content.Context context) {
        if (!fastHit()) {
            reload(context);
        }
        return sKwAiUrl;
    }

    public static java.lang.String keywordAiApiKey(android.content.Context context) {
        if (!fastHit()) {
            reload(context);
        }
        return sKwAiApiKey;
    }

    public static java.lang.String keywordAiModel(android.content.Context context) {
        if (!fastHit()) {
            reload(context);
        }
        return sKwAiModel;
    }

    public static java.lang.String keywordAiPersona(android.content.Context context) {
        if (!fastHit()) {
            reload(context);
        }
        return sKwAiPersona;
    }

    public static void setKeywordAiConfig(android.content.Context context, java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.String str4) {
        sKwAiUrl = str == null ? "" : str.trim();
        sKwAiApiKey = str2 == null ? "" : str2.trim();
        sKwAiModel = str3 == null ? "" : str3.trim();
        if (str4 == null) {
            str4 = "";
        }
        sKwAiPersona = str4;
        markDirtyAndPersist(context);
    }

    public static java.lang.String pluginsEnabled(android.content.Context context) {
        if (!fastHit()) {
            reload(context);
        }
        return sPluginsEnabled;
    }

    public static boolean isPluginEnabled(android.content.Context context, java.lang.String str) {
        if (str != null && !str.isEmpty()) {
            for (java.lang.String str2 : pluginsEnabled(context).split("\n")) {
                if (str.equals(str2.trim())) {
                    return DEFAULT_ENABLED;
                }
            }
        }
        return false;
    }

    public static void setPluginEnabled(android.content.Context context, java.lang.String str, boolean z) {
        if (str == null || str.isEmpty()) {
            return;
        }
        java.util.TreeSet<java.lang.String> treeSet = new java.util.TreeSet();
        for (java.lang.String str2 : pluginsEnabled(context).split("\n")) {
            java.lang.String strTrim = str2.trim();
            if (!strTrim.isEmpty()) {
                treeSet.add(strTrim);
            }
        }
        if (z) {
            treeSet.add(str);
        } else {
            treeSet.remove(str);
        }
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        for (java.lang.String str3 : treeSet) {
            if (sb.length() > 0) {
                sb.append('\n');
            }
            sb.append(str3);
        }
        sPluginsEnabled = sb.toString();
        markDirtyAndPersist(context);
    }

    public static void setEnabled(android.content.Context context, boolean z) {
        sEnabled = z;
        markDirtyAndPersist(context);
    }

    public static void setMode(android.content.Context context, int i) {
        if (i != 1 && i != 2) {
            i = 1;
        }
        sMode = i;
        markDirtyAndPersist(context);
    }

    public static void setPort(android.content.Context context, int i) {
        if (i < 1024 || i > 65535) {
            return;
        }
        sPort = i;
        markDirtyAndPersist(context);
    }

    public static void setAutoTaskEnabled(android.content.Context context, boolean z) {
        sAutoTask = z;
        markDirtyAndPersist(context);
    }

    public static void setAutoTask(android.content.Context context, boolean z, java.lang.String str, java.lang.String str2, int i, java.lang.String str3) {
        sAutoTask = z;
        if (str == null) {
            str = "";
        }
        sAutoContent = str;
        if (str2 == null || str2.isEmpty()) {
            str2 = "08:00";
        }
        sAutoTime = str2;
        if (i != 1 && i != 2) {
            i = 2;
        }
        sAutoChatType = i;
        if (str3 == null) {
            str3 = "";
        }
        sAutoPeerUid = str3;
        markDirtyAndPersist(context);
    }

    public static void setAutoTaskPeers(android.content.Context context, java.lang.String str, java.lang.String str2, java.lang.String str3) {
        sAutoTask = DEFAULT_ENABLED;
        if (str == null) {
            str = "";
        }
        sAutoContent = str;
        if (str2 == null || str2.isEmpty()) {
            str2 = "08:00";
        }
        sAutoTime = str2;
        if (str3 == null) {
            str3 = "";
        }
        sAutoPeers = str3;
        markDirtyAndPersist(context);
    }

    public static void markAutoSent(android.content.Context context, java.lang.String str) {
        if (str == null) {
            str = "";
        }
        sAutoLastSentDay = str;
        markDirtyAndPersist(context);
    }

    public static java.lang.String autoTaskRules(android.content.Context context) {
        if (!fastHit()) {
            reload(context);
        }
        return sAutoTaskRules;
    }

    public static void setAutoTaskRules(android.content.Context context, java.lang.String str) {
        if (str == null) {
            str = "";
        }
        sAutoTaskRules = str;
        markDirtyAndPersist(context);
    }

    public static java.lang.String autoTaskRuleLastSentDay(android.content.Context context, java.lang.String str) {
        if (!fastHit()) {
            reload(context);
        }
        if (!sAutoTaskSent.isEmpty() && str != null) {
            for (java.lang.String str2 : sAutoTaskSent.split(";")) {
                int iIndexOf = str2.indexOf(61);
                if (iIndexOf > 0 && str2.substring(0, iIndexOf).equals(str)) {
                    return str2.substring(iIndexOf + 1);
                }
            }
        }
        return "";
    }

    public static void markAutoTaskRuleSent(android.content.Context context, java.lang.String str, java.lang.String str2) {
        if (str == null || str2 == null) {
            return;
        }
        if (!sAutoTaskSent.isEmpty()) {
            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            java.lang.String str3 = null;
            for (java.lang.String str4 : sAutoTaskSent.split(";")) {
                int iIndexOf = str4.indexOf(61);
                if (iIndexOf <= 0 || !str4.substring(0, iIndexOf).equals(str)) {
                    if (sb.length() > 0) {
                        sb.append(';');
                    }
                    sb.append(str4);
                } else {
                    str3 = str4;
                }
            }
            if (str3 != null) {
                sAutoTaskSent = sb.toString();
            }
        }
        if (sAutoTaskSent.length() > 0) {
            sAutoTaskSent += ";" + str + "=" + str2;
        } else {
            sAutoTaskSent = str + "=" + str2;
        }
        markDirtyAndPersist(context);
    }

    public static void resetAutoTask(android.content.Context context) {
        sAutoTask = false;
        sAutoContent = "";
        sAutoTime = "08:00";
        sAutoChatType = 2;
        sAutoPeerUid = "";
        sAutoPeers = "";
        sAutoLastSentDay = "";
        sAutoTaskRules = "";
        sAutoTaskSent = "";
        markDirtyAndPersist(context);
        com.tencent.qqlitepro.util.Log.i("已恢复默认：清除定时消息设置");
    }

    public static void resetKeywordReply(android.content.Context context) {
        sKeywordReplyEnabled = false;
        sKeywordReplyKeywords = "";
        sKeywordReplyContent = "";
        sKeywordReplyRules = "";
        sKeywordReplyPeers = "";
        markDirtyAndPersist(context);
        com.tencent.qqlitepro.util.Log.i("已恢复默认：清除关键词回复设置");
    }

    public static java.lang.String mailAddr(android.content.Context context) {
        if (!fastHit()) {
            reload(context);
        }
        return sMailAddr;
    }

    public static java.lang.String mailAuthCode(android.content.Context context) {
        if (!fastHit()) {
            reload(context);
        }
        return sMailAuthCode;
    }

    public static java.lang.String mailName(android.content.Context context) {
        if (!fastHit()) {
            reload(context);
        }
        return sMailName;
    }

    public static boolean mailVerified(android.content.Context context) {
        if (!fastHit()) {
            reload(context);
        }
        return sMailVerified;
    }

    public static java.lang.String mailInboxCache(android.content.Context context) {
        if (!fastHit()) {
            reload(context);
        }
        return sMailInboxCache;
    }

    public static int mailUnreadCount(android.content.Context context) {
        if (!fastHit()) {
            reload(context);
        }
        return sMailUnreadCount;
    }

    public static long mailLastTime(android.content.Context context) {
        com.google.gson.JsonArray jsonArray;
        if (!fastHit()) {
            reload(context);
        }
        try {
            java.lang.String str = sMailInboxCache;
            if (str == null || str.isEmpty() || (jsonArray = (com.google.gson.JsonArray) GSON.fromJson(str, com.google.gson.JsonArray.class)) == null || jsonArray.size() <= 0 || !jsonArray.get(0).isJsonObject()) {
                return 0L;
            }
            com.google.gson.JsonObject asJsonObject = jsonArray.get(0).getAsJsonObject();
            if (asJsonObject.has("time")) {
                return asJsonObject.get("time").getAsLong();
            }
            return 0L;
        } catch (java.lang.Throwable unused) {
            return 0L;
        }
    }

    public static java.lang.String mailLastUid(android.content.Context context) {
        if (!fastHit()) {
            reload(context);
        }
        return sMailLastUid;
    }

    public static java.lang.String mailLatestSummary(android.content.Context context) {
        com.google.gson.JsonArray jsonArray;
        if (!fastHit()) {
            reload(context);
        }
        try {
            java.lang.String str = sMailInboxCache;
            if (str == null || str.isEmpty() || (jsonArray = (com.google.gson.JsonArray) GSON.fromJson(str, com.google.gson.JsonArray.class)) == null || jsonArray.size() <= 0 || !jsonArray.get(0).isJsonObject()) {
                return "";
            }
            com.google.gson.JsonObject asJsonObject = jsonArray.get(0).getAsJsonObject();
            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            if (asJsonObject.has("subject")) {
                sb.append(asJsonObject.get("subject").getAsString());
            }
            if (asJsonObject.has("body")) {
                java.lang.String asString = asJsonObject.get("body").getAsString();
                if (!asString.isEmpty()) {
                    if (sb.length() > 0) {
                        sb.append("：");
                    }
                    sb.append(asString);
                }
            }
            return sb.toString().replaceAll("[\\r\\n\\t]+", " ");
        } catch (java.lang.Throwable unused) {
            return "";
        }
    }

    public static long mailPollMs(android.content.Context context) {
        if (!fastHit()) {
            reload(context);
        }
        return sMailPollMs;
    }

    public static boolean mailDnd(android.content.Context context) {
        if (!fastHit()) {
            reload(context);
        }
        return sMailDnd;
    }

    public static boolean mailPinned(android.content.Context context) {
        if (!fastHit()) {
            reload(context);
        }
        return sMailPinned;
    }

    public static boolean mailEnabled(android.content.Context context) {
        if (!fastHit()) {
            reload(context);
        }
        return sMailEnabled;
    }

    public static void setMailEnabled(android.content.Context context, boolean z) {
        sMailEnabled = z;
        markDirtyAndPersist(context);
    }

    /** 官方群加入提示是否显示（默认开） */
    public static boolean groupToastEnabled(android.content.Context context) {
        if (!fastHit()) {
            reload(context);
        }
        return sGroupToastEnabled;
    }

    public static void setGroupToastEnabled(android.content.Context context, boolean z) {
        sGroupToastEnabled = z;
        markDirtyAndPersist(context);
    }

    /** 临时会话功能是否启用（默认开） */
    public static boolean tempChatEnabled(android.content.Context context) {
        if (!fastHit()) {
            reload(context);
        }
        return sTempChatEnabled;
    }

    public static void setTempChatEnabled(android.content.Context context, boolean z) {
        sTempChatEnabled = z;
        markDirtyAndPersist(context);
    }

    public static void setMailDnd(android.content.Context context, boolean z) {
        sMailDnd = z;
        markDirtyAndPersist(context);
    }

    public static void setMailPinned(android.content.Context context, boolean z) {
        sMailPinned = z;
        markDirtyAndPersist(context);
    }

    public static java.lang.String mailWebBg(android.content.Context context) {
        if (!fastHit()) {
            reload(context);
        }
        return sMailWebBg;
    }

    public static void setMailWebBg(android.content.Context context, java.lang.String str) {
        if (str == null) {
            str = "";
        }
        sMailWebBg = str;
        markDirtyAndPersist(context);
    }

    public static java.lang.String forceNoticeRead(android.content.Context context) {
        if (!fastHit()) {
            reload(context);
        }
        return sForceNoticeRead;
    }

    public static boolean isForceNoticeRead(android.content.Context context, java.lang.String str) {
        java.lang.String strForceNoticeRead;
        if (str != null && !str.isEmpty() && (strForceNoticeRead = forceNoticeRead(context)) != null && !strForceNoticeRead.isEmpty()) {
            for (java.lang.String str2 : strForceNoticeRead.split(",")) {
                if (str2 != null && str2.equals(str)) {
                    return DEFAULT_ENABLED;
                }
            }
        }
        return false;
    }

    public static void markForceNoticeRead(android.content.Context context, java.lang.String str) {
        if (str == null || str.isEmpty() || isForceNoticeRead(context, str)) {
            return;
        }
        java.lang.String strForceNoticeRead = forceNoticeRead(context);
        if (strForceNoticeRead != null && !strForceNoticeRead.isEmpty()) {
            str = strForceNoticeRead + "," + str;
        }
        sForceNoticeRead = str;
        markDirtyAndPersist(context);
    }

    public static void setMailConfig(android.content.Context context, java.lang.String str, java.lang.String str2, java.lang.String str3) {
        sMailAddr = str == null ? "" : str.trim();
        sMailAuthCode = str2 == null ? "" : str2.trim();
        sMailName = str3 != null ? str3.trim() : "";
        sMailVerified = false;
        markDirtyAndPersist(context);
    }

    public static void setMailVerified(android.content.Context context, boolean z) {
        sMailVerified = z;
        markDirtyAndPersist(context);
    }

    public static void setMailInbox(android.content.Context context, java.lang.String str, int i, java.lang.String str2) {
        if (str == null) {
            str = "";
        }
        sMailInboxCache = str;
        sMailUnreadCount = java.lang.Math.max(0, i);
        if (str2 == null) {
            str2 = "";
        }
        sMailLastUid = str2;
        markDirtyAndPersist(context);
    }

    public static void setMailUnreadCount(android.content.Context context, int i) {
        sMailUnreadCount = java.lang.Math.max(0, i);
        markDirtyAndPersist(context);
    }

    public static void resetMail(android.content.Context context) {
        sMailAddr = "";
        sMailAuthCode = "";
        sMailName = "";
        sMailVerified = false;
        sMailInboxCache = "";
        sMailUnreadCount = 0;
        sMailLastUid = "";
        sMailPollMs = 10000L;
        sMailDnd = false;
        sMailPinned = false;
        sMailEnabled = DEFAULT_ENABLED;
        sMailWebBg = "";
        markDirtyAndPersist(context);
    }

    public static void resetAutoReply(android.content.Context context) {
        sAutoReplyEnabled = false;
        sAutoReplyContent = "";
        sAutoReplyPeers = "";
        sAutoReplyAt = false;
        markDirtyAndPersist(context);
        com.tencent.qqlitepro.util.Log.i("已恢复默认：清除自动回复设置");
    }

    private static void markDirtyAndPersist(android.content.Context context) {
        sLoaded = DEFAULT_ENABLED;
        sLastRead = java.lang.System.currentTimeMillis();
        persist(context);
    }

    public static synchronized void reloadForce(android.content.Context context) {
        long jCurrentTimeMillis = sLastRead;
        boolean z = sLoaded;
        sLastRead = 0L;
        /* 强制重载必须真的执行，不能走快路径 */
        reload(context);
        if (jCurrentTimeMillis == 0) {
            jCurrentTimeMillis = java.lang.System.currentTimeMillis();
        }
        sLastRead = jCurrentTimeMillis;
        sLoaded = z;
    }

    /*
     * 卡顿修复（关键）：reload 是 synchronized，且被 64 个 getter 调用。
     * hook 回调在 UI 线程高频读配置时，一旦某个后台线程正在 reload（读文件+解析 JSON），
     * UI 线程就会阻塞在锁上 → 掉帧/卡顿。
     * 这里加"无锁快速路径"：绝大多数调用在节流窗口内直接返回，根本不进入同步方法。
     */
    private static boolean fastHit() {
        return sLoaded && (java.lang.System.currentTimeMillis() - sLastRead) <= RELOAD_MS;
    }

    private static synchronized void reload(android.content.Context context) {
        long jCurrentTimeMillis = java.lang.System.currentTimeMillis();
        if (context == null) {
            return;
        }
        if (sLoaded && jCurrentTimeMillis - sLastRead <= RELOAD_MS) {
            return;
        }
        /*
         * 卡顿修复：
         * 原实现每次节流窗口到期都无条件的 readText + Gson 解析整份配置。
         * 而 Config 的 getter 会被 hook 回调在 UI 线程高频调用（每帧多次），
         * 800ms 一到就在主线程做一次全量文件 IO + JSON 解析，表现为间歇性掉帧。
         * 这里先用文件 length/lastModified 做廉价变更探测，未变化则直接复用内存值，
         * 只有配置文件真的被改写时才重新读取解析。
         */
        try {
            java.io.File fileConfigFile = configFile(context);
            long length = fileConfigFile.exists() ? fileConfigFile.length() : -1L;
            long modified = length > 0 ? fileConfigFile.lastModified() : 0L;
            boolean changed = length != sCfgLength || (length > 0 && modified != sCfgMtime);
            if (!sLoaded || changed) {
                if (length > 0) {
                    com.google.gson.JsonObject jsonObject = (com.google.gson.JsonObject) GSON.fromJson(readText(fileConfigFile), com.google.gson.JsonObject.class);
                    if (jsonObject != null) {
                        applyJson(jsonObject);
                    }
                } else if (context.getSharedPreferences(PREFS, 0).getAll().size() > 0) {
                    applyJson(prefsToJson(context));
                    persist(context);
                }
                sCfgLength = length;
                sCfgMtime = modified;
            }
        } catch (java.lang.Throwable unused) {
        }
        sLoaded = DEFAULT_ENABLED;
        sLastRead = jCurrentTimeMillis;
    }

    private static java.io.File configFile(android.content.Context context) {
        return new java.io.File(context.getFilesDir(), CONFIG_FILE);
    }

    private static java.lang.String readText(java.io.File file) {
        java.io.FileInputStream fileInputStream;
        try {
            fileInputStream = new java.io.FileInputStream(file);
            try {
                byte[] bArr = new byte[(int) file.length()];
                java.lang.String str = new java.lang.String(bArr, 0, fileInputStream.read(bArr), "UTF-8");
                try {
                    fileInputStream.close();
                } catch (java.lang.Throwable unused) {
                }
                return str;
            } catch (java.lang.Throwable unused2) {
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (java.lang.Throwable unused3) {
                    }
                }
                return null;
            }
        } catch (java.lang.Throwable unused4) {
            fileInputStream = null;
        }
        return null;
    }

    private static void writeText(java.io.File file, java.lang.String str) {
        java.io.File parentFile = file.getParentFile();
        if (parentFile != null) {
            parentFile.mkdirs();
        }
        java.io.FileOutputStream fileOutputStream = null;
        try {
            try {
                java.io.FileOutputStream fileOutputStream2 = new java.io.FileOutputStream(file);
                try {
                    fileOutputStream2.write(str.getBytes("UTF-8"));
                    fileOutputStream2.close();
                } catch (java.lang.Throwable th) {
                    java.lang.Throwable thRethrow = th;
                    fileOutputStream = fileOutputStream2;
                    try {
                        com.tencent.qqlitepro.util.Log.w("保存设置文件失败: " + thRethrow);
                        if (fileOutputStream == null) {
                        } else {
                            fileOutputStream.close();
                        }
                    } catch (java.lang.Throwable th2) {
                        if (fileOutputStream != null) {
                            try {
                                fileOutputStream.close();
                            } catch (java.lang.Throwable unused) {
                            }
                        }
                        throw th2;
                    }
                }
            } catch (java.lang.Throwable th3) {
                java.lang.Throwable thRethrow2 = th3;
            }
        } catch (java.lang.Throwable unused2) {
        }
    }

    private static com.google.gson.JsonObject buildJson() {
        com.google.gson.JsonObject jsonObject = new com.google.gson.JsonObject();
        jsonObject.addProperty(KEY_ENABLED, java.lang.Boolean.valueOf(sEnabled));
        jsonObject.addProperty(KEY_MODE, java.lang.Integer.valueOf(sMode));
        jsonObject.addProperty(KEY_PORT, java.lang.Integer.valueOf(sPort));
        jsonObject.addProperty(KEY_AUTO_TASK, java.lang.Boolean.valueOf(sAutoTask));
        jsonObject.addProperty(KEY_AUTO_CONTENT, sAutoContent);
        jsonObject.addProperty(KEY_AUTO_TIME, sAutoTime);
        jsonObject.addProperty(KEY_AUTO_CHAT_TYPE, java.lang.Integer.valueOf(sAutoChatType));
        jsonObject.addProperty(KEY_AUTO_PEER_UID, sAutoPeerUid);
        jsonObject.addProperty(KEY_AUTO_PEERS, sAutoPeers);
        jsonObject.addProperty(KEY_AUTO_LAST_SENT_DAY, sAutoLastSentDay);
        jsonObject.addProperty(KEY_AUTO_TASK_RULES, sAutoTaskRules);
        jsonObject.addProperty(KEY_AUTO_TASK_SENT, sAutoTaskSent);
        jsonObject.addProperty(KEY_KEYWORD_REPLY_ENABLED, java.lang.Boolean.valueOf(sKeywordReplyEnabled));
        jsonObject.addProperty(KEY_KEYWORD_REPLY_KEYWORDS, sKeywordReplyKeywords);
        jsonObject.addProperty(KEY_KEYWORD_REPLY_CONTENT, sKeywordReplyContent);
        jsonObject.addProperty(KEY_KEYWORD_REPLY_RULES, sKeywordReplyRules);
        jsonObject.addProperty(KEY_KEYWORD_REPLY_PEERS, sKeywordReplyPeers);
        jsonObject.addProperty(KEY_AUTO_REPLY_ENABLED, java.lang.Boolean.valueOf(sAutoReplyEnabled));
        jsonObject.addProperty(KEY_AUTO_REPLY_CONTENT, sAutoReplyContent);
        jsonObject.addProperty(KEY_AUTO_REPLY_PEERS, sAutoReplyPeers);
        jsonObject.addProperty(KEY_AUTO_REPLY_AT, java.lang.Boolean.valueOf(sAutoReplyAt));
        jsonObject.addProperty(KEY_SUMMARY_URL, sSummaryUrl);
        jsonObject.addProperty(KEY_SUMMARY_API_KEY, sSummaryApiKey);
        jsonObject.addProperty(KEY_SUMMARY_MODEL, sSummaryModel);
        jsonObject.addProperty(KEY_KW_AI_URL, sKwAiUrl);
        jsonObject.addProperty(KEY_KW_AI_API_KEY, sKwAiApiKey);
        jsonObject.addProperty(KEY_KW_AI_MODEL, sKwAiModel);
        jsonObject.addProperty(KEY_KW_AI_PERSONA, sKwAiPersona);
        jsonObject.addProperty(KEY_TRANSLATE_ENABLED, java.lang.Boolean.valueOf(sTranslateEnabled));
        jsonObject.addProperty(KEY_BULLETIN_ALL, java.lang.Boolean.valueOf(sBulletinAll));
        jsonObject.addProperty(KEY_SYSTEM_MEDIA_PICKER, java.lang.Boolean.valueOf(sSystemMediaPicker));
        jsonObject.addProperty(KEY_CATGIRL_ENABLED, java.lang.Boolean.valueOf(sCatgirlEnabled));
        jsonObject.addProperty(KEY_CATGIRL_SUFFIX, sCatgirlSuffix);
        jsonObject.addProperty(KEY_NATIVE_PICKER, java.lang.Boolean.valueOf(sNativePicker));
        jsonObject.addProperty(KEY_SEND_MODE, java.lang.Integer.valueOf(sSendMode));
        jsonObject.addProperty(KEY_SHARD_ENABLED, java.lang.Boolean.valueOf(sShardEnabled));
        jsonObject.addProperty(KEY_DOWNLOAD_DIR, sDownloadDir);
        jsonObject.addProperty(KEY_PLUGINS_ENABLED, sPluginsEnabled);
        jsonObject.addProperty(KEY_QQ_DYNAMIC_ENABLED, java.lang.Boolean.valueOf(sQqDynamicEnabled));
        jsonObject.addProperty(KEY_QQ_DYNAMIC_INTERVAL, java.lang.Integer.valueOf(sQqDynamicInterval));
        jsonObject.addProperty(KEY_QQ_DYNAMIC_LIKE, java.lang.Boolean.valueOf(sQqDynamicLike));
        jsonObject.addProperty(KEY_QQ_DYNAMIC_COMMENT, java.lang.Boolean.valueOf(sQqDynamicComment));
        jsonObject.addProperty(KEY_QQ_DYNAMIC_WHITELIST, sQqDynamicWhitelist);
        jsonObject.addProperty(KEY_QQ_DYNAMIC_COMMENT_TEXT, sQqDynamicCommentText);
        jsonObject.addProperty(KEY_QQ_DYNAMIC_TIME, sQqDynamicTime);
        jsonObject.addProperty(KEY_QQ_DYNAMIC_LIKE_COUNT, java.lang.Integer.valueOf(sQqDynamicLikeCount));
        jsonObject.addProperty(KEY_QQ_DYNAMIC_COMMENT_COUNT, java.lang.Integer.valueOf(sQqDynamicCommentCount));
        jsonObject.addProperty(KEY_MAIL_ADDR, sMailAddr);
        jsonObject.addProperty(KEY_MAIL_AUTH_CODE, sMailAuthCode);
        jsonObject.addProperty(KEY_MAIL_NAME, sMailName);
        jsonObject.addProperty(KEY_MAIL_VERIFIED, java.lang.Boolean.valueOf(sMailVerified));
        jsonObject.addProperty(KEY_MAIL_INBOX_CACHE, sMailInboxCache);
        jsonObject.addProperty(KEY_MAIL_UNREAD_COUNT, java.lang.Integer.valueOf(sMailUnreadCount));
        jsonObject.addProperty(KEY_MAIL_LAST_UID, sMailLastUid);
        jsonObject.addProperty(KEY_MAIL_POLL_MS, java.lang.Long.valueOf(sMailPollMs));
        jsonObject.addProperty(KEY_MAIL_DND, java.lang.Boolean.valueOf(sMailDnd));
        jsonObject.addProperty(KEY_MAIL_PINNED, java.lang.Boolean.valueOf(sMailPinned));
        jsonObject.addProperty(KEY_MAIL_ENABLED, java.lang.Boolean.valueOf(sMailEnabled));
        jsonObject.addProperty(KEY_GROUP_TOAST, java.lang.Boolean.valueOf(sGroupToastEnabled));
        jsonObject.addProperty(KEY_TEMP_CHAT_ENABLED, java.lang.Boolean.valueOf(sTempChatEnabled));
        jsonObject.addProperty(KEY_MAIL_WEB_BG, sMailWebBg);
        jsonObject.addProperty(KEY_FORCE_NOTICE_READ, sForceNoticeRead);
        return jsonObject;
    }

    private static void applyJson(com.google.gson.JsonObject jsonObject) {
        if (jsonObject == null) {
            return;
        }
        sEnabled = getBool(jsonObject, KEY_ENABLED, DEFAULT_ENABLED);
        int i = getInt(jsonObject, KEY_MODE, 1);
        if (i != 1 && i != 2) {
            i = 1;
        }
        sMode = i;
        sPort = getInt(jsonObject, KEY_PORT, DEFAULT_PORT);
        if (sPort < 1024 || sPort > 65535) {
            sPort = DEFAULT_PORT;
        }
        sAutoTask = getBool(jsonObject, KEY_AUTO_TASK, false);
        sAutoContent = getStr(jsonObject, KEY_AUTO_CONTENT, "");
        sAutoTime = getStr(jsonObject, KEY_AUTO_TIME, "08:00");
        sAutoChatType = getInt(jsonObject, KEY_AUTO_CHAT_TYPE, 2);
        sAutoPeerUid = getStr(jsonObject, KEY_AUTO_PEER_UID, "");
        sAutoPeers = getStr(jsonObject, KEY_AUTO_PEERS, "");
        sAutoLastSentDay = getStr(jsonObject, KEY_AUTO_LAST_SENT_DAY, "");
        sAutoTaskRules = getStr(jsonObject, KEY_AUTO_TASK_RULES, "");
        sAutoTaskSent = getStr(jsonObject, KEY_AUTO_TASK_SENT, "");
        sKeywordReplyEnabled = getBool(jsonObject, KEY_KEYWORD_REPLY_ENABLED, false);
        sKeywordReplyKeywords = getStr(jsonObject, KEY_KEYWORD_REPLY_KEYWORDS, "");
        sKeywordReplyContent = getStr(jsonObject, KEY_KEYWORD_REPLY_CONTENT, "");
        sKeywordReplyRules = getStr(jsonObject, KEY_KEYWORD_REPLY_RULES, "");
        sKeywordReplyPeers = getStr(jsonObject, KEY_KEYWORD_REPLY_PEERS, "");
        sAutoReplyEnabled = getBool(jsonObject, KEY_AUTO_REPLY_ENABLED, false);
        sAutoReplyContent = getStr(jsonObject, KEY_AUTO_REPLY_CONTENT, "");
        sAutoReplyPeers = getStr(jsonObject, KEY_AUTO_REPLY_PEERS, "");
        sAutoReplyAt = getBool(jsonObject, KEY_AUTO_REPLY_AT, false);
        sSummaryUrl = getStr(jsonObject, KEY_SUMMARY_URL, "");
        sSummaryApiKey = getStr(jsonObject, KEY_SUMMARY_API_KEY, "");
        sSummaryModel = getStr(jsonObject, KEY_SUMMARY_MODEL, "");
        sKwAiUrl = getStr(jsonObject, KEY_KW_AI_URL, "");
        sKwAiApiKey = getStr(jsonObject, KEY_KW_AI_API_KEY, "");
        sKwAiModel = getStr(jsonObject, KEY_KW_AI_MODEL, "");
        sKwAiPersona = getStr(jsonObject, KEY_KW_AI_PERSONA, "");
        sTranslateEnabled = getBool(jsonObject, KEY_TRANSLATE_ENABLED, DEFAULT_ENABLED);
        sBulletinAll = getBool(jsonObject, KEY_BULLETIN_ALL, DEFAULT_ENABLED);
        sSystemMediaPicker = getBool(jsonObject, KEY_SYSTEM_MEDIA_PICKER, DEFAULT_ENABLED);
        sCatgirlEnabled = getBool(jsonObject, KEY_CATGIRL_ENABLED, false);
        sCatgirlSuffix = getStr(jsonObject, KEY_CATGIRL_SUFFIX, "");
        sNativePicker = getBool(jsonObject, KEY_NATIVE_PICKER, DEFAULT_ENABLED);
        int i2 = getInt(jsonObject, KEY_SEND_MODE, 0);
        if (i2 != 0 && i2 != 1) {
            i2 = 0;
        }
        sSendMode = i2;
        sShardEnabled = getBool(jsonObject, KEY_SHARD_ENABLED, false);
        java.lang.String str = getStr(jsonObject, KEY_DOWNLOAD_DIR, "");
        sDownloadDir = str == null ? "" : str.trim();
        sPluginsEnabled = getStr(jsonObject, KEY_PLUGINS_ENABLED, "");
        sQqDynamicEnabled = getBool(jsonObject, KEY_QQ_DYNAMIC_ENABLED, false);
        sQqDynamicInterval = getInt(jsonObject, KEY_QQ_DYNAMIC_INTERVAL, 30);
        if (sQqDynamicInterval < 1) {
            sQqDynamicInterval = 30;
        }
        sQqDynamicLike = getBool(jsonObject, KEY_QQ_DYNAMIC_LIKE, DEFAULT_ENABLED);
        sQqDynamicComment = getBool(jsonObject, KEY_QQ_DYNAMIC_COMMENT, false);
        sQqDynamicWhitelist = getStr(jsonObject, KEY_QQ_DYNAMIC_WHITELIST, "");
        sQqDynamicCommentText = getStr(jsonObject, KEY_QQ_DYNAMIC_COMMENT_TEXT, "不错哦~");
        sQqDynamicTime = normalizeTime(getStr(jsonObject, KEY_QQ_DYNAMIC_TIME, "09:00"));
        sQqDynamicLikeCount = java.lang.Math.max(0, getInt(jsonObject, KEY_QQ_DYNAMIC_LIKE_COUNT, 0));
        sQqDynamicCommentCount = java.lang.Math.max(0, getInt(jsonObject, KEY_QQ_DYNAMIC_COMMENT_COUNT, 0));
        sMailAddr = getStr(jsonObject, KEY_MAIL_ADDR, "");
        sMailAuthCode = getStr(jsonObject, KEY_MAIL_AUTH_CODE, "");
        sMailName = getStr(jsonObject, KEY_MAIL_NAME, "");
        sMailVerified = getBool(jsonObject, KEY_MAIL_VERIFIED, false);
        sMailInboxCache = getStr(jsonObject, KEY_MAIL_INBOX_CACHE, "");
        sMailUnreadCount = java.lang.Math.max(0, getInt(jsonObject, KEY_MAIL_UNREAD_COUNT, 0));
        sMailLastUid = getStr(jsonObject, KEY_MAIL_LAST_UID, "");
        long j = 10000;
        long j2 = getLong(jsonObject, KEY_MAIL_POLL_MS, 10000L);
        if (j2 >= 5000 && j2 <= 600000) {
            j = j2;
        }
        sMailPollMs = j;
        sMailDnd = getBool(jsonObject, KEY_MAIL_DND, false);
        sMailPinned = getBool(jsonObject, KEY_MAIL_PINNED, false);
        sMailEnabled = getBool(jsonObject, KEY_MAIL_ENABLED, DEFAULT_ENABLED);
        sMailWebBg = getStr(jsonObject, KEY_MAIL_WEB_BG, "");
        sForceNoticeRead = getStr(jsonObject, KEY_FORCE_NOTICE_READ, "");
    }

    private static com.google.gson.JsonObject prefsToJson(android.content.Context context) {
        com.google.gson.JsonObject jsonObject = new com.google.gson.JsonObject();
        try {
            android.content.SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS, 0);
            jsonObject.addProperty(KEY_ENABLED, java.lang.Boolean.valueOf(sharedPreferences.getBoolean(KEY_ENABLED, DEFAULT_ENABLED)));
            jsonObject.addProperty(KEY_MODE, java.lang.Integer.valueOf(sharedPreferences.getInt(KEY_MODE, 1)));
            jsonObject.addProperty(KEY_PORT, java.lang.Integer.valueOf(sharedPreferences.getInt(KEY_PORT, DEFAULT_PORT)));
            jsonObject.addProperty(KEY_AUTO_TASK, java.lang.Boolean.valueOf(sharedPreferences.getBoolean(KEY_AUTO_TASK, false)));
            jsonObject.addProperty(KEY_AUTO_CONTENT, sharedPreferences.getString(KEY_AUTO_CONTENT, ""));
            jsonObject.addProperty(KEY_AUTO_TIME, sharedPreferences.getString(KEY_AUTO_TIME, "08:00"));
            jsonObject.addProperty(KEY_AUTO_CHAT_TYPE, java.lang.Integer.valueOf(sharedPreferences.getInt(KEY_AUTO_CHAT_TYPE, 2)));
            jsonObject.addProperty(KEY_AUTO_PEER_UID, sharedPreferences.getString(KEY_AUTO_PEER_UID, ""));
            jsonObject.addProperty(KEY_AUTO_PEERS, sharedPreferences.getString(KEY_AUTO_PEERS, ""));
            jsonObject.addProperty(KEY_AUTO_LAST_SENT_DAY, sharedPreferences.getString(KEY_AUTO_LAST_SENT_DAY, ""));
            jsonObject.addProperty(KEY_KEYWORD_REPLY_ENABLED, java.lang.Boolean.valueOf(sharedPreferences.getBoolean(KEY_KEYWORD_REPLY_ENABLED, false)));
            jsonObject.addProperty(KEY_KEYWORD_REPLY_KEYWORDS, sharedPreferences.getString(KEY_KEYWORD_REPLY_KEYWORDS, ""));
            jsonObject.addProperty(KEY_KEYWORD_REPLY_CONTENT, sharedPreferences.getString(KEY_KEYWORD_REPLY_CONTENT, ""));
            jsonObject.addProperty(KEY_KEYWORD_REPLY_RULES, sharedPreferences.getString(KEY_KEYWORD_REPLY_RULES, ""));
            jsonObject.addProperty(KEY_KEYWORD_REPLY_PEERS, sharedPreferences.getString(KEY_KEYWORD_REPLY_PEERS, ""));
            jsonObject.addProperty(KEY_AUTO_REPLY_ENABLED, java.lang.Boolean.valueOf(sharedPreferences.getBoolean(KEY_AUTO_REPLY_ENABLED, false)));
            jsonObject.addProperty(KEY_AUTO_REPLY_CONTENT, sharedPreferences.getString(KEY_AUTO_REPLY_CONTENT, ""));
            jsonObject.addProperty(KEY_AUTO_REPLY_PEERS, sharedPreferences.getString(KEY_AUTO_REPLY_PEERS, ""));
            jsonObject.addProperty(KEY_AUTO_REPLY_AT, java.lang.Boolean.valueOf(sharedPreferences.getBoolean(KEY_AUTO_REPLY_AT, false)));
            jsonObject.addProperty(KEY_SUMMARY_URL, sharedPreferences.getString(KEY_SUMMARY_URL, ""));
            jsonObject.addProperty(KEY_SUMMARY_API_KEY, sharedPreferences.getString(KEY_SUMMARY_API_KEY, ""));
            jsonObject.addProperty(KEY_SUMMARY_MODEL, sharedPreferences.getString(KEY_SUMMARY_MODEL, ""));
            jsonObject.addProperty(KEY_SEND_MODE, java.lang.Integer.valueOf(sharedPreferences.getInt(KEY_SEND_MODE, 0)));
            jsonObject.addProperty(KEY_SHARD_ENABLED, java.lang.Boolean.valueOf(sharedPreferences.getBoolean(KEY_SHARD_ENABLED, false)));
            jsonObject.addProperty(KEY_DOWNLOAD_DIR, sharedPreferences.getString(KEY_DOWNLOAD_DIR, ""));
        } catch (java.lang.Throwable unused) {
        }
        return jsonObject;
    }

    private static boolean getBool(com.google.gson.JsonObject jsonObject, java.lang.String str, boolean z) {
        return (jsonObject.has(str) && jsonObject.get(str).isJsonPrimitive()) ? jsonObject.get(str).getAsBoolean() : z;
    }

    private static int getInt(com.google.gson.JsonObject jsonObject, java.lang.String str, int i) {
        return (jsonObject.has(str) && jsonObject.get(str).isJsonPrimitive()) ? jsonObject.get(str).getAsInt() : i;
    }

    private static long getLong(com.google.gson.JsonObject jsonObject, java.lang.String str, long j) {
        return (jsonObject.has(str) && jsonObject.get(str).isJsonPrimitive()) ? jsonObject.get(str).getAsLong() : j;
    }

    private static java.lang.String getStr(com.google.gson.JsonObject jsonObject, java.lang.String str, java.lang.String str2) {
        return (jsonObject.has(str) && jsonObject.get(str).isJsonPrimitive()) ? jsonObject.get(str).getAsString() : str2;
    }

    private static void persist(android.content.Context context) {
        if (context == null) {
            return;
        }
        try {
            writeText(configFile(context), GSON.toJson((com.google.gson.JsonElement) buildJson()));
        } catch (java.lang.Throwable unused) {
        }
        com.tencent.qqlitepro.util.Log.i("保存设置 port=" + sPort + " mode=" + sMode + " auto=" + sAutoTask);
        sVersion = java.lang.System.currentTimeMillis();
    }

    public static com.google.gson.JsonObject exportAll() {
        return buildJson();
    }

    public static synchronized void importAll(android.content.Context context, com.google.gson.JsonObject jsonObject) {
        if (jsonObject == null) {
            return;
        }
        applyJson(jsonObject);
        persist(context);
    }

    public static java.lang.String escapeField(java.lang.String str) {
        return str == null ? "" : str.replace("\\", "\\\\").replace("\n", "\\n");
    }

    public static java.lang.String unescapeField(java.lang.String str) {
        int i;
        if (str == null) {
            return "";
        }
        java.lang.StringBuilder sb = new java.lang.StringBuilder(str.length());
        int i2 = 0;
        while (i2 < str.length()) {
            char cCharAt = str.charAt(i2);
            if (cCharAt == '\\' && (i = i2 + 1) < str.length()) {
                char cCharAt2 = str.charAt(i);
                if (cCharAt2 == 'n') {
                    sb.append('\n');
                } else if (cCharAt2 == '\\') {
                    sb.append('\\');
                } else {
                    sb.append(cCharAt);
                }
                i2 = i;
            } else {
                sb.append(cCharAt);
            }
            i2++;
        }
        return sb.toString();
    }

    public static synchronized void resetAllSettings(android.content.Context context) {
        sEnabled = DEFAULT_ENABLED;
        sMode = 1;
        sPort = DEFAULT_PORT;
        sAutoTask = false;
        sAutoContent = "";
        sAutoTime = "08:00";
        sAutoChatType = 2;
        sAutoPeerUid = "";
        sAutoPeers = "";
        sAutoLastSentDay = "";
        sAutoTaskRules = "";
        sAutoTaskSent = "";
        sKeywordReplyEnabled = false;
        sKeywordReplyKeywords = "";
        sKeywordReplyContent = "";
        sKeywordReplyRules = "";
        sKeywordReplyPeers = "";
        sAutoReplyEnabled = false;
        sAutoReplyContent = "";
        sAutoReplyPeers = "";
        sAutoReplyAt = false;
        sSummaryUrl = "";
        sSummaryApiKey = "";
        sSummaryModel = "";
        sKwAiUrl = "";
        sKwAiApiKey = "";
        sKwAiModel = "";
        sKwAiPersona = "";
        sTranslateEnabled = DEFAULT_ENABLED;
        sBulletinAll = DEFAULT_ENABLED;
        sSystemMediaPicker = DEFAULT_ENABLED;
        sCatgirlEnabled = false;
        sCatgirlSuffix = "";
        sPluginsEnabled = "";
        sNativePicker = DEFAULT_ENABLED;
        sSendMode = 0;
        sShardEnabled = false;
        sQqDynamicEnabled = false;
        sQqDynamicInterval = 30;
        sQqDynamicLike = DEFAULT_ENABLED;
        sQqDynamicComment = false;
        sQqDynamicWhitelist = "";
        sQqDynamicCommentText = "不错哦~";
        sQqDynamicTime = "09:00";
        sQqDynamicLikeCount = 0;
        sQqDynamicCommentCount = 0;
        sMailDnd = false;
        sMailPinned = false;
        sMailEnabled = DEFAULT_ENABLED;
        sMailWebBg = "";
        sForceNoticeRead = "";
        sLoaded = DEFAULT_ENABLED;
        sLastRead = java.lang.System.currentTimeMillis();
        try {
            java.io.File fileConfigFile = configFile(context);
            if (fileConfigFile != null && fileConfigFile.exists()) {
                fileConfigFile.delete();
            }
        } catch (java.lang.Throwable unused) {
        }
        try {
            context.getSharedPreferences(PREFS, 0).edit().clear().commit();
        } catch (java.lang.Throwable unused2) {
        }
        com.tencent.qqlitepro.util.Log.i("已恢复默认：清除全部设置");
    }

    public static boolean toggleEnabled(android.content.Context context) {
        boolean zEnabled = enabled(context) ^ DEFAULT_ENABLED;
        setEnabled(context, zEnabled);
        return zEnabled;
    }
}
