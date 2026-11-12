package com.tencent.qqlitepro.http;

/* JADX INFO: loaded from: classes.dex */
public final class QqDynamic {
    public static final int CODE_BUSINESS_ERR = -4;
    public static final int CODE_HTTP_ERR = -2;
    public static final int CODE_NO_TICKET = -1;
    public static final int CODE_OK = 0;
    public static final int CODE_PARSE_ERR = -3;

    private QqDynamic() {
    }

    public static com.google.gson.JsonArray fetchTodayFeeds(java.util.List<java.lang.String> list) {
        java.lang.String strValueOf;
        java.lang.String strQzoneHttpGet;
        java.util.List<com.google.gson.JsonObject> jsonFeeds;
        java.lang.String strQzoneSelfUin = com.tencent.qqlitepro.http.QqKernel.qzoneSelfUin();
        java.lang.String strQzoneSkey = com.tencent.qqlitepro.http.QqKernel.qzoneSkey();
        int i = 0;
        if (strQzoneSelfUin == null || strQzoneSelfUin.isEmpty() || strQzoneSkey == null || strQzoneSkey.isEmpty()) {
            java.lang.StringBuilder sb = new java.lang.StringBuilder("QQ动态: 无登录票据 uin=");
            sb.append(strQzoneSelfUin);
            sb.append(" skey空=");
            sb.append(strQzoneSkey == null);
            com.tencent.qqlitepro.util.Log.w(sb.toString());
            return null;
        }
        java.lang.String strBuildCookie = buildCookie(strQzoneSelfUin);
        int iQzoneBkn = com.tencent.qqlitepro.http.QqKernel.qzoneBkn(strQzoneSkey);
        long j = todayStartSeconds();
        java.lang.String[] strArr = {"https://h5.qzone.qq.com/proxy/domain/taotao.qq.com/cgi-bin/emotion_cgi_msglist_v6?uin=" + strQzoneSelfUin + "&hostuin=" + strQzoneSelfUin + "&suin=" + strQzoneSelfUin + "&ftype=0&sort=0&pos=0&num=30&replynum=0&g_tk=" + iQzoneBkn, "https://user.qzone.qq.com/proxy/domain/taotao.qq.com/cgi-bin/emotion_cgi_msglist_v6?uin=" + strQzoneSelfUin + "&hostuin=" + strQzoneSelfUin + "&suin=" + strQzoneSelfUin + "&ftype=0&sort=0&pos=0&num=30&replynum=0&g_tk=" + iQzoneBkn, "https://h5.qzone.qq.com/proxy/domain/taotao.qq.com/cgi-bin/emotion_cgi_feedlist_v6?uin=" + strQzoneSelfUin + "&hostuin=" + strQzoneSelfUin + "&ftype=0&sort=0&pos=0&num=30&replynum=0&g_tk=" + iQzoneBkn, "https://h5.qzone.qq.com/proxy/domain/feeds.qzone.qq.com/cgi-bin/feeds/feeds3_html_more?uin=" + strQzoneSelfUin + "&loginuin=" + strQzoneSelfUin + "&scope=0&type=0&format=1&json=1&start=0&num=30&update=0&via=QZ5&gf=fd_com&qid=" + strQzoneSelfUin + "&pre=hot&list_type=1&tic=1&fuin=" + strQzoneSelfUin + "&g_tk=" + iQzoneBkn};
        while (true) {
            if (i >= 4) {
                strValueOf = "-";
                strQzoneHttpGet = null;
                break;
            }
            strQzoneHttpGet = com.tencent.qqlitepro.http.QqKernel.qzoneHttpGet(strArr[i], strBuildCookie);
            if (strQzoneHttpGet == null) {
                com.tencent.qqlitepro.util.Log.w("QQ动态: 候选[" + i + "] 无响应");
            } else {
                com.tencent.qqlitepro.util.Log.i("QQ动态: 候选[" + i + "] len=" + strQzoneHttpGet.length() + " head=" + truncate(strQzoneHttpGet));
                if (strQzoneHttpGet.contains("\"code\":0") || strQzoneHttpGet.contains("\"code\" :0")) {
                    strValueOf = java.lang.String.valueOf(i);
                    break;
                }
            }
            i++;
        }
        if (strQzoneHttpGet == null) {
            com.tencent.qqlitepro.util.Log.w("QQ动态: 所有候选接口均未返回可识别数据（核算据/票据）");
            return null;
        }
        if ("0".equals(strValueOf) || "1".equals(strValueOf) || "2".equals(strValueOf)) {
            jsonFeeds = parseJsonFeeds(strQzoneHttpGet, list, j);
        } else {
            if (!"3".equals(strValueOf)) {
                return null;
            }
            com.tencent.qqlitepro.util.Log.w("QQ动态: 走经典 HTML 分页接口（起点固定，仅一页）");
            jsonFeeds = parseFeedsHtml(strQzoneHttpGet, list, j).feeds;
        }
        if (jsonFeeds == null || jsonFeeds.isEmpty()) {
            com.tencent.qqlitepro.util.Log.w("QQ动态: 解析到 0 条动态，可能今天无新动态");
        }
        com.google.gson.JsonArray jsonArray = new com.google.gson.JsonArray();
        java.util.Iterator<com.google.gson.JsonObject> it = jsonFeeds.iterator();
        while (it.hasNext()) {
            jsonArray.add(it.next());
        }
        if (jsonArray.size() > 0) {
            return jsonArray;
        }
        return null;
    }

    private static java.util.List<com.google.gson.JsonObject> parseJsonFeeds(java.lang.String str, java.util.List<java.lang.String> list, long j) {
        com.google.gson.JsonObject asJsonObject;
        com.google.gson.JsonElement jsonElement;
        long j2;
        java.util.ArrayList arrayList = new java.util.ArrayList();
        try {
            com.google.gson.JsonElement string = com.google.gson.JsonParser.parseString(str);
            if (string != null && string.isJsonObject() && (jsonElement = (asJsonObject = string.getAsJsonObject()).get("data")) != null && jsonElement.isJsonObject()) {
                com.google.gson.JsonElement jsonElement2 = jsonElement.getAsJsonObject().get("msglist");
                if (jsonElement2 == null || !jsonElement2.isJsonArray()) {
                    com.google.gson.JsonElement jsonElement3 = asJsonObject.get("msglist");
                    if (jsonElement3 == null || !jsonElement3.isJsonArray()) {
                        jsonElement3 = null;
                    }
                    jsonElement2 = jsonElement3;
                }
                if (jsonElement2 == null) {
                    return arrayList;
                }
                for (com.google.gson.JsonElement jsonElement4 : jsonElement2.getAsJsonArray()) {
                    if (jsonElement4 instanceof com.google.gson.JsonObject) {
                        com.google.gson.JsonObject asJsonObject2 = jsonElement4.getAsJsonObject();
                        java.lang.String strStrOf = strOf(asJsonObject2, "tid", "fid");
                        java.lang.String strStrOf2 = strOf(asJsonObject2, "unikey");
                        if (strStrOf2.isEmpty()) {
                            strStrOf2 = strOf(asJsonObject2, "abi_report_traceid", "keystamp");
                        }
                        java.lang.String strStrOf3 = strOf(asJsonObject2, "appid", "appId");
                        java.lang.String strStrOf4 = strOf(asJsonObject2, "uin", "authoruin", "hostuin");
                        java.lang.String strStrOf5 = strOf(asJsonObject2, "name_masked", "nickname", com.sun.mail.imap.IMAPStore.ID_NAME);
                        java.lang.String strStrOf6 = strOf(asJsonObject2, "createTime", "timestamp", "create_time");
                        if (strStrOf6.isEmpty()) {
                            j2 = 0;
                        } else {
                            try {
                                j2 = java.lang.Long.parseLong(strStrOf6);
                            } catch (java.lang.Throwable unused) {
                                j2 = 0;
                            }
                        }
                        if (!strStrOf.isEmpty() || !strStrOf2.isEmpty()) {
                            if (j2 <= 0 || j2 >= j) {
                                if (list == null || list.isEmpty() || list.contains(strStrOf4)) {
                                    com.google.gson.JsonObject jsonObject = new com.google.gson.JsonObject();
                                    if (strStrOf3.isEmpty()) {
                                        strStrOf3 = "311";
                                    }
                                    jsonObject.addProperty("appid", strStrOf3);
                                    jsonObject.addProperty("fid", strStrOf);
                                    jsonObject.addProperty("curkey", strStrOf2);
                                    jsonObject.addProperty("unikey", strStrOf2);
                                    jsonObject.addProperty("fromuin", strStrOf4);
                                    jsonObject.addProperty("nickname", strStrOf5);
                                    jsonObject.addProperty("timestamp", java.lang.Long.valueOf(j2));
                                    arrayList.add(jsonObject);
                                }
                            }
                        }
                    }
                }
                return arrayList;
            }
            return arrayList;
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("QQ动态: 解析 json 动态失败 " + th);
        }
        return new java.util.ArrayList();
    }

    private static long todayStartSeconds() {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        return calendar.getTimeInMillis() / 1000;
    }

    private static final class ParsedPage {
        boolean done;
        final java.util.List<com.google.gson.JsonObject> feeds;
        boolean found;
        int nextStart;



        private ParsedPage() {
            this.found = false;
            this.done = true;
            this.nextStart = 0;
            this.feeds = new java.util.ArrayList();
        }
    }

    private static com.tencent.qqlitepro.http.QqDynamic.ParsedPage parseFeedsHtml(java.lang.String str, java.util.List<java.lang.String> list, long j) {
        java.lang.String asString;
        com.google.gson.JsonObject feedBlock;
        com.tencent.qqlitepro.http.QqDynamic.ParsedPage parsedPage = new com.tencent.qqlitepro.http.QqDynamic.ParsedPage();
        try {
            com.google.gson.JsonElement string = com.google.gson.JsonParser.parseString(str);
            if (string != null && string.isJsonObject()) {
                com.google.gson.JsonObject asJsonObject = string.getAsJsonObject();
                int asInt = (asJsonObject.has("code") && asJsonObject.get("code").isJsonPrimitive()) ? asJsonObject.get("code").getAsInt() : -1;
                if (asInt != 0) {
                    com.tencent.qqlitepro.util.Log.w("QQ动态: 拉取返回 code=" + asInt + " raw=" + truncate(str));
                    return parsedPage;
                }
                com.google.gson.JsonElement jsonElement = asJsonObject.get("data");
                if (jsonElement != null && (jsonElement instanceof com.google.gson.JsonObject)) {
                    com.google.gson.JsonObject asJsonObject2 = jsonElement.getAsJsonObject();
                    if (asJsonObject2.has("start") && asJsonObject2.get("start").isJsonPrimitive()) {
                        parsedPage.nextStart = asJsonObject2.get("start").getAsInt();
                    }
                    int asInt2 = (asJsonObject2.has(org.mozilla.javascript.ES6Iterator.NEXT_METHOD) && asJsonObject2.get(org.mozilla.javascript.ES6Iterator.NEXT_METHOD).isJsonPrimitive()) ? asJsonObject2.get(org.mozilla.javascript.ES6Iterator.NEXT_METHOD).getAsInt() - parsedPage.nextStart : 30;
                    parsedPage.done = asInt2 <= 0 || asInt2 < 30;
                    if (asJsonObject2.has("data") && !asJsonObject2.get("data").isJsonNull() && (asString = asJsonObject2.get("data").getAsString()) != null && !asString.trim().isEmpty()) {
                        parsedPage.found = true;
                        java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("<div class=\"[^\"]*f_item[^\"]*\".*?(?=<div class=\"[^\"]*f_item|$)").matcher(asString);
                        while (matcher.find()) {
                            java.lang.String strGroup = matcher.group();
                            if (strGroup != null && !strGroup.isEmpty() && (feedBlock = parseFeedBlock(strGroup)) != null) {
                                long asLong = feedBlock.has("timestamp") ? feedBlock.get("timestamp").getAsLong() : 0L;
                                if (asLong <= 0 || asLong >= j) {
                                    if (list != null && !list.isEmpty()) {
                                        if (!list.contains(feedBlock.has("fromuin") ? feedBlock.get("fromuin").getAsString() : "")) {
                                        }
                                    }
                                    feedBlock.addProperty("_page", java.lang.Integer.valueOf(parsedPage.nextStart));
                                    parsedPage.feeds.add(feedBlock);
                                }
                            }
                        }
                        return parsedPage;
                    }
                }
            }
            return parsedPage;
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("QQ动态: 解析动态失败 " + th);
        }
        return parsedPage;
    }

    private static com.google.gson.JsonObject parseFeedBlock(java.lang.String str) {
        long j;
        com.google.gson.JsonObject jsonObject = new com.google.gson.JsonObject();
        java.lang.String strGroupOf = groupOf(str, "data-appid=\"(\\d{1,6})\"");
        if (strGroupOf.isEmpty()) {
            strGroupOf = groupOf(str, "id=\"appid\" value=\"(\\d+)\"");
        }
        java.lang.String strGroupOf2 = groupOf(str, "class=\"item\"[^>]*data-contentid=\"([^\"]+)\"");
        if (strGroupOf2.isEmpty()) {
            strGroupOf2 = groupOf(str, "data-fid=\"([^\"]+)\"");
        }
        if (strGroupOf2.isEmpty()) {
            strGroupOf2 = groupOf(str, "name=\"fid\" value=\"([^\"]+)\"");
        }
        java.lang.String strGroupOf3 = groupOf(str, "class=\"[^\"]*curkey[^\"]*\"[^>]*value=\"([^\"]+)\"");
        if (strGroupOf3.isEmpty()) {
            strGroupOf3 = groupOf(str, "unikey[^\"]*\"[^>]*value=\"([^\"]+)\"");
        }
        java.lang.String strGroupOf4 = groupOf(str, "data-uin=\"(\\d{5,})\"");
        if (strGroupOf4.isEmpty()) {
            strGroupOf4 = groupOf(str, "opuin\"?[>:]+(\\d{5,})");
        }
        java.lang.String strGroupOf5 = groupOf(str, "title=\"([^\"]{1,30})\"");
        java.lang.String strGroupOf6 = groupOf(str, "data-time=\"(\\d{1,20})\"");
        if (strGroupOf6.isEmpty()) {
            strGroupOf6 = groupOf(str, "pubtime=\"(\\d{1,20})\"");
        }
        if (strGroupOf6.isEmpty()) {
            j = 0;
        } else {
            try {
                j = java.lang.Long.parseLong(strGroupOf6);
            } catch (java.lang.Throwable unused) {
                j = 0;
            }
        }
        if (strGroupOf.isEmpty() && strGroupOf2.isEmpty() && strGroupOf3.isEmpty()) {
            return null;
        }
        if (strGroupOf.isEmpty()) {
            strGroupOf = "311";
        }
        jsonObject.addProperty("appid", strGroupOf);
        jsonObject.addProperty("fid", strGroupOf2);
        jsonObject.addProperty("curkey", strGroupOf3);
        jsonObject.addProperty("unikey", strGroupOf3);
        if (strGroupOf4.isEmpty()) {
            strGroupOf4 = "";
        }
        jsonObject.addProperty("fromuin", strGroupOf4);
        jsonObject.addProperty("nickname", strGroupOf5);
        jsonObject.addProperty("timestamp", java.lang.Long.valueOf(j));
        return jsonObject;
    }

    private static java.lang.String groupOf(java.lang.String str, java.lang.String str2) {
        try {
            java.util.regex.Matcher matcher = java.util.regex.Pattern.compile(str2).matcher(str);
            return matcher.find() ? matcher.group(1) : "";
        } catch (java.lang.Throwable unused) {
            return "";
        }
    }

    public static int likeFeed(com.google.gson.JsonObject jsonObject) {
        if (jsonObject == null) {
            return -3;
        }
        java.lang.String strQzoneSelfUin = com.tencent.qqlitepro.http.QqKernel.qzoneSelfUin();
        java.lang.String strQzoneSkey = com.tencent.qqlitepro.http.QqKernel.qzoneSkey();
        if (strQzoneSelfUin == null || strQzoneSelfUin.isEmpty() || strQzoneSkey == null || strQzoneSkey.isEmpty()) {
            return -1;
        }
        java.lang.String strBuildCookie = buildCookie(strQzoneSelfUin);
        int iQzoneBkn = com.tencent.qqlitepro.http.QqKernel.qzoneBkn(strQzoneSkey);
        java.lang.String strStrOf = strOf(jsonObject, "fid");
        java.lang.String strStrOf2 = strOf(jsonObject, "curkey", "unikey");
        java.lang.String strStrOf3 = strOf(jsonObject, "appid");
        java.lang.String strStrOf4 = strOf(jsonObject, "fromuin");
        if (strStrOf.isEmpty() || strStrOf2.isEmpty()) {
            return -3;
        }
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        addFrm(sb, "appid", strStrOf3);
        addFrm(sb, "fid", strStrOf);
        addFrm(sb, "curkey", strStrOf2);
        addFrm(sb, "unikey", strStrOf2);
        addFrm(sb, "fromUin", strStrOf4);
        addFrm(sb, "uin", strQzoneSelfUin);
        addFrm(sb, "reason", "2");
        addFrm(sb, "fopen_vipOr", "1");
        addFrm(sb, "isToAll", "0");
        addFrm(sb, "num", "1");
        addFrm(sb, "is_redirect", "1");
        addFrm(sb, "format", "json");
        return judgeWriteResp(com.tencent.qqlitepro.http.QqKernel.qzoneHttpPost("https://h5.qzone.qq.com/proxy/domain/taotao.qq.com/cgi-bin/taotao/qzone_like?g_tk=" + iQzoneBkn, strBuildCookie, sb.toString()), "点赞");
    }

    public static int commentFeed(com.google.gson.JsonObject jsonObject, java.lang.String str) {
        if (jsonObject == null) {
            return -3;
        }
        java.lang.String strQzoneSelfUin = com.tencent.qqlitepro.http.QqKernel.qzoneSelfUin();
        java.lang.String strQzoneSkey = com.tencent.qqlitepro.http.QqKernel.qzoneSkey();
        if (strQzoneSelfUin == null || strQzoneSelfUin.isEmpty() || strQzoneSkey == null || strQzoneSkey.isEmpty()) {
            return -1;
        }
        if (str != null && !str.trim().isEmpty()) {
            java.lang.String strBuildCookie = buildCookie(strQzoneSelfUin);
            int iQzoneBkn = com.tencent.qqlitepro.http.QqKernel.qzoneBkn(strQzoneSkey);
            java.lang.String strStrOf = strOf(jsonObject, "fid");
            java.lang.String strStrOf2 = strOf(jsonObject, "curkey", "unikey");
            java.lang.String strStrOf3 = strOf(jsonObject, "appid");
            java.lang.String strStrOf4 = strOf(jsonObject, "fromuin");
            if (!strStrOf.isEmpty() && !strStrOf2.isEmpty()) {
                java.lang.StringBuilder sb = new java.lang.StringBuilder();
                addFrm(sb, "fid", strStrOf);
                addFrm(sb, "content", str);
                addFrm(sb, "froot", strStrOf);
                addFrm(sb, "appid", strStrOf3);
                addFrm(sb, "unikey", strStrOf2);
                addFrm(sb, "hostuin", strStrOf4);
                addFrm(sb, "opuin", strQzoneSelfUin);
                addFrm(sb, "uin", strQzoneSelfUin);
                addFrm(sb, "reqfrom", "1");
                addFrm(sb, "reverence", "");
                addFrm(sb, "actions", "0");
                addFrm(sb, "format", "json");
                return judgeWriteResp(com.tencent.qqlitepro.http.QqKernel.qzoneHttpPost("https://h5.qzone.qq.com/proxy/domain/taotao.qq.com/cgi-bin/taotao/add_for_comment?g_tk=" + iQzoneBkn, strBuildCookie, sb.toString()), "评论");
            }
        }
        return -3;
    }

    private static int judgeWriteResp(java.lang.String str, java.lang.String str2) {
        if (str == null || str.trim().isEmpty()) {
            com.tencent.qqlitepro.util.Log.w("QQ动态: " + str2 + " 无响应");
            return -2;
        }
        try {
            com.google.gson.JsonElement string = com.google.gson.JsonParser.parseString(str);
            if (string != null && string.isJsonObject()) {
                com.google.gson.JsonObject asJsonObject = string.getAsJsonObject();
                int asInt = (asJsonObject.has("code") && asJsonObject.get("code").isJsonPrimitive()) ? asJsonObject.get("code").getAsInt() : -1;
                java.lang.String asString = (asJsonObject.has("message") && asJsonObject.get("message").isJsonPrimitive()) ? asJsonObject.get("message").getAsString() : "";
                if (asInt == 0) {
                    return 0;
                }
                com.tencent.qqlitepro.util.Log.w("QQ动态: " + str2 + " 业务失败 code=" + asInt + " msg=" + asString + " raw=" + truncate(str));
                return -4;
            }
        } catch (java.lang.Throwable unused) {
        }
        com.tencent.qqlitepro.util.Log.w("QQ动态: " + str2 + " 响应解析失败 raw=" + truncate(str));
        return -3;
    }

    private static java.lang.String buildCookie(java.lang.String str) {
        java.lang.StringBuilder sb = new java.lang.StringBuilder("uin=o");
        sb.append(str);
        sb.append("; skey=");
        java.lang.String strQzoneSkey = com.tencent.qqlitepro.http.QqKernel.qzoneSkey();
        sb.append(strQzoneSkey == null ? "" : strQzoneSkey);
        java.lang.String strQzonePskey = com.tencent.qqlitepro.http.QqKernel.qzonePskey("qzone.qq.com");
        if (strQzonePskey != null && !strQzonePskey.isEmpty()) {
            sb.append("; p_skey=");
            sb.append(strQzonePskey);
        }
        java.lang.String strQzonePt4Token = com.tencent.qqlitepro.http.QqKernel.qzonePt4Token("qzone.qq.com");
        if (strQzonePt4Token != null && !strQzonePt4Token.isEmpty()) {
            sb.append("; pt4_token=");
            sb.append(strQzonePt4Token);
        }
        java.lang.StringBuilder sb2 = new java.lang.StringBuilder("QQ动态: cookie 构成 skey=");
        sb2.append(strQzoneSkey == null ? 0 : strQzoneSkey.length());
        sb2.append(" p_skey=");
        sb2.append(strQzonePskey == null ? 0 : strQzonePskey.length());
        sb2.append(" pt4=");
        sb2.append(strQzonePt4Token != null ? strQzonePt4Token.length() : 0);
        com.tencent.qqlitepro.util.Log.i(sb2.toString());
        return sb.toString();
    }

    private static void addFrm(java.lang.StringBuilder sb, java.lang.String str, java.lang.String str2) {
        if (sb.length() > 0) {
            sb.append('&');
        }
        sb.append(str);
        sb.append('=');
        if (str2 == null) {
            str2 = "";
        }
        sb.append(encode(str2));
    }

    private static java.lang.String encode(java.lang.String str) {
        try {
            return java.net.URLEncoder.encode(str, "UTF-8");
        } catch (java.io.UnsupportedEncodingException unused) {
            return str;
        }
    }

    private static java.lang.String strOf(com.google.gson.JsonObject jsonObject, java.lang.String... strArr) {
        java.lang.String asString;
        if (jsonObject == null) {
            return "";
        }
        for (java.lang.String str : strArr) {
            com.google.gson.JsonElement jsonElement = jsonObject.get(str);
            if (jsonElement != null && !jsonElement.isJsonNull() && jsonElement.isJsonPrimitive() && (asString = jsonElement.getAsString()) != null && !asString.isEmpty()) {
                return asString;
            }
        }
        return "";
    }

    private static java.lang.String truncate(java.lang.String str) {
        if (str == null) {
            return "null";
        }
        return str.length() > 300 ? str.substring(0, 300) : str;
    }

    public static java.util.Set<java.lang.String> parseBlacklist(java.lang.String str) {
        java.util.HashSet hashSet = new java.util.HashSet();
        if (str == null) {
            return hashSet;
        }
        for (java.lang.String str2 : str.split("[\\r\\n,;，；]")) {
            java.lang.String strTrim = str2.trim();
            if (!strTrim.isEmpty() && strTrim.matches("\\d{5,}")) {
                hashSet.add(strTrim);
            }
        }
        return hashSet;
    }

}
