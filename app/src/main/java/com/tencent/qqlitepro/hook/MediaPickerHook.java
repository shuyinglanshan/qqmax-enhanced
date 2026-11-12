package com.tencent.qqlitepro.hook;

/* JADX INFO: loaded from: classes.dex */
public final class MediaPickerHook {
    private static final java.lang.String CHOOSER_TITLE = "选择应用";
    private static final java.lang.String CLS_AIO_FRAGMENT = "com.tencent.watch.aio_impl.ui.WatchAIOFragment";
    private static final java.lang.String CLS_CURRENT_CONTACT = "momoi.mod.qqpro.hook.action.CurrentContactKt";
    private static final java.lang.String CLS_IMAGE_TAG = "momoi.mod.qqpro.lib.ImageTag";
    private static final java.lang.String CLS_IME_OPERATION = "moye.wearqq.IMEOperation";
    private static final java.lang.String CLS_INLINE_INPUT = "momoi.mod.qqpro.hook.InlineInput";
    private static final java.lang.String CLS_MENU_FRAME = "com.tencent.watch.aio_impl.ui.frames.MenuFrame";
    private static final java.lang.String CLS_MSG_ELEMENT = "com.tencent.qqnt.kernel.nativeinterface.MsgElement";
    private static final java.lang.String CLS_SETTING_FRAME = "com.tencent.watch.aio_impl.ui.frames.SettingFrame";
    private static final java.lang.String KEY_CHAT_TYPE = "key_bundle_chat_type";
    private static final java.lang.String KEY_PEER_UID = "key_bundle_peer_id";
    private static final java.lang.String LABEL_ADD_FRIEND = "添加好友";
    private static final java.lang.String LABEL_GALLERY = "相册";
    private static final int REQ_MEDIA_PICK = 24369;
    private static final java.lang.String TAG_SETTINGS_ENTRY = "qqlitepro_panel_settings_entry";
    private static volatile boolean sBindHooked = false;
    private static volatile int sChatType = -1;
    private static volatile java.lang.ClassLoader sClassLoader = null;
    private static volatile boolean sHandlingResult = false;
    private static volatile java.lang.Object sMenuInstance = null;
    private static volatile boolean sPatched = false;
    private static volatile java.lang.String sPeerUid = "";
    private static volatile java.lang.Class<?> sRecyclerViewCls;
    private static volatile long sRequestAt;

    public static boolean isInstalled() {
        return sPatched;
    }

    private MediaPickerHook() {
    }

    public static void patch(java.lang.ClassLoader classLoader) {
        if (sPatched) {
            return;
        }
        sClassLoader = classLoader;
        try {
            java.lang.Class<?> cls = java.lang.Class.forName(CLS_MENU_FRAME, false, classLoader);
            de.robv.android.xposed.XposedHelpers.findAndHookMethod(cls, "onViewCreated", new java.lang.Object[]{android.view.View.class, android.os.Bundle.class, new com.tencent.qqlitepro.hook.MediaPickerHook.AnonymousClass1()});
            hookResult(cls);
            try {
                hookResult(java.lang.Class.forName(CLS_AIO_FRAGMENT, false, classLoader));
            } catch (java.lang.Throwable th) {
                com.tencent.qqlitepro.util.Log.w("会话页片段不可用，回传仅依赖「+」面板: " + th);
            }
            try {
                de.robv.android.xposed.XposedHelpers.findAndHookMethod(java.lang.Class.forName(CLS_SETTING_FRAME, false, classLoader), "onViewCreated", new java.lang.Object[]{android.view.View.class, android.os.Bundle.class, new com.tencent.qqlitepro.hook.MediaPickerHook.AnonymousClass2()});
            } catch (java.lang.Throwable th2) {
                com.tencent.qqlitepro.util.Log.w("聊天设置页 hook 安装失败: " + th2);
            }
            sPatched = true;
            com.tencent.qqlitepro.util.Log.i("已接管聊天「相册」单元格：系统媒体选择器 hook 就绪");
        } catch (java.lang.Throwable th3) {
            com.tencent.qqlitepro.util.Log.w("媒体选择器 hook 安装失败（稍后重试）: " + th3);
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.MediaPickerHook$1, reason: invalid class name */
    static class AnonymousClass1 extends de.robv.android.xposed.XC_MethodHook {
        AnonymousClass1() {
        }

        protected void afterHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            try {
                java.lang.Object obj = methodHookParam.thisObject;
                java.lang.Object obj2 = (methodHookParam.args == null || methodHookParam.args.length <= 0) ? null : methodHookParam.args[0];
                if (obj2 instanceof android.view.View) {
                    com.tencent.qqlitepro.hook.MediaPickerHook.attachCells(obj, (android.view.View) obj2);
                }
            } catch (java.lang.Throwable th) {
                com.tencent.qqlitepro.util.Log.w("接管相册单元格失败: " + th);
            }
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.MediaPickerHook$2, reason: invalid class name */
    static class AnonymousClass2 extends de.robv.android.xposed.XC_MethodHook {
        AnonymousClass2() {
        }

        protected void afterHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            try {
                com.tencent.qqlitepro.hook.MediaPickerHook.injectAddFriendEntry(methodHookParam.thisObject);
            } catch (java.lang.Throwable th) {
                com.tencent.qqlitepro.util.Log.w("注入「添加好友」入口失败: " + th);
            }
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.MediaPickerHook$3, reason: invalid class name */
    static class AnonymousClass3 extends de.robv.android.xposed.XC_MethodHook {
        AnonymousClass3() {
        }

        protected void afterHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            try {
                com.tencent.qqlitepro.hook.MediaPickerHook.handleResult(methodHookParam.thisObject, methodHookParam.args);
            } catch (java.lang.Throwable th) {
                com.tencent.qqlitepro.util.Log.w("处理系统选择器回传失败: " + th);
            }
        }
    }

    private static void hookResult(java.lang.Class<?> cls) {
        try {
            de.robv.android.xposed.XposedHelpers.findAndHookMethod(cls, "onActivityResult", new java.lang.Object[]{java.lang.Integer.TYPE, java.lang.Integer.TYPE, android.content.Intent.class, new com.tencent.qqlitepro.hook.MediaPickerHook.AnonymousClass3()});
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("挂接回传失败(" + cls.getName() + "): " + th);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void handleResult(java.lang.Object obj, java.lang.Object[] objArr) {
        if (objArr == null || objArr.length <= 0) {
            return;
        }
        if (((java.lang.Integer) objArr[0]).intValue() != REQ_MEDIA_PICK) {
            return;
        }
        int iIntValue = objArr.length > 1 ? ((java.lang.Integer) objArr[1]).intValue() : 0;
        java.lang.Object obj2 = objArr.length > 2 ? objArr[2] : null;
        java.lang.StringBuilder sb = new java.lang.StringBuilder("收到系统选择器回传：result=");
        sb.append(iIntValue);
        sb.append("，投递片段=");
        sb.append(obj == null ? "?" : obj.getClass().getSimpleName());
        com.tencent.qqlitepro.util.Log.i(sb.toString());
        if (iIntValue == -1 && (obj2 instanceof android.content.Intent)) {
            long jCurrentTimeMillis = java.lang.System.currentTimeMillis();
            if (sRequestAt <= 0 || jCurrentTimeMillis - sRequestAt > 600000 || sHandlingResult) {
                return;
            }
            sHandlingResult = true;
            onPicked(sMenuInstance, (android.content.Intent) obj2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void attachCells(java.lang.Object obj, android.view.View view) {
        android.view.View viewFindRecyclerView = findRecyclerView(view);
        if (viewFindRecyclerView == null) {
            return;
        }
        injectSettingsEntry(obj, viewFindRecyclerView);
        hookAdapterBind(obj, viewFindRecyclerView);
        if (viewFindRecyclerView instanceof android.view.ViewGroup) {
            android.view.ViewGroup viewGroup = (android.view.ViewGroup) viewFindRecyclerView;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                try {
                    restyleCell(obj, viewGroup.getChildAt(i));
                } catch (java.lang.Throwable unused) {
                }
            }
        }
        try {
            java.lang.Class<?> cls = java.lang.Class.forName("androidx.recyclerview.widget.RecyclerView$OnChildAttachStateChangeListener", false, viewFindRecyclerView.getClass().getClassLoader());
            java.lang.Object objNewProxyInstance = java.lang.reflect.Proxy.newProxyInstance(cls.getClassLoader(), new java.lang.Class[]{cls}, new com.tencent.qqlitepro.hook.MediaPickerHook$$ExternalSyntheticLambda1(obj));
            java.lang.reflect.Method method = viewFindRecyclerView.getClass().getMethod("addOnChildAttachStateChangeListener", cls);
            method.setAccessible(true);
            method.invoke(viewFindRecyclerView, objNewProxyInstance);
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("注册 RecyclerView 子项回调失败: " + th);
        }
    }

    static /* synthetic */ java.lang.Object lambda$attachCells$0(java.lang.Object obj, java.lang.Object obj2, java.lang.reflect.Method method, java.lang.Object[] objArr) throws java.lang.Throwable {
        if (!"onChildViewAttachedToWindow".equals(method.getName()) || objArr == null || objArr.length != 1) {
            return null;
        }
        java.lang.Object obj3 = objArr[0];
        if (!(obj3 instanceof android.view.View)) {
            return null;
        }
        try {
            restyleCell(obj, (android.view.View) obj3);
            return null;
        } catch (java.lang.Throwable unused) {
            return null;
        }
    }

    private static void hookAdapterBind(java.lang.Object obj, android.view.View view) {
        java.lang.reflect.Method method;
        if (sBindHooked) {
            return;
        }
        try {
            java.lang.Object objCallMethod = de.robv.android.xposed.XposedHelpers.callMethod(view, "getAdapter", new java.lang.Object[0]);
            if (objCallMethod == null) {
                return;
            }
            java.lang.Class<?> cls = objCallMethod.getClass();
            java.lang.reflect.Method[] methods = cls.getMethods();
            int length = methods.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    method = null;
                    break;
                }
                method = methods[i];
                if ("onBindViewHolder".equals(method.getName()) && method.getParameterTypes().length == 2) {
                    break;
                } else {
                    i++;
                }
            }
            if (method == null) {
                return;
            }
            de.robv.android.xposed.XposedHelpers.findAndHookMethod(cls, "onBindViewHolder", new java.lang.Object[]{method.getParameterTypes()[0], java.lang.Integer.TYPE, new com.tencent.qqlitepro.hook.MediaPickerHook.AnonymousClass4(obj)});
            sBindHooked = true;
            com.tencent.qqlitepro.util.Log.i("已 hook 面板适配器 onBindViewHolder（重绑后重新接管「相册」按钮）");
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("hook 面板 onBindViewHolder 失败: " + th);
        }
    }

    /* JADX INFO: renamed from: com.tencent.qqlitepro.hook.MediaPickerHook$4, reason: invalid class name */
    static class AnonymousClass4 extends de.robv.android.xposed.XC_MethodHook {
        final /* synthetic */ java.lang.Object val$menuFrame;

        AnonymousClass4(java.lang.Object obj) {
            this.val$menuFrame = obj;
        }

        protected void afterHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {
            try {
                java.lang.Object objectField = null;
                java.lang.Object obj = (methodHookParam.args == null || methodHookParam.args.length <= 0) ? null : methodHookParam.args[0];
                if (obj != null) {
                    objectField = de.robv.android.xposed.XposedHelpers.getObjectField(obj, "itemView");
                }
                if (objectField instanceof android.view.View) {
                    com.tencent.qqlitepro.hook.MediaPickerHook.restyleCell(this.val$menuFrame, (android.view.View) objectField);
                }
            } catch (java.lang.Throwable unused) {
            }
        }
    }

    private static void injectSettingsEntry(java.lang.Object obj, android.view.View view) {
        android.content.Context context;
        int iDp;
        int iDp2;
        android.view.ViewParent parent = view.getParent();
        if (parent instanceof android.widget.FrameLayout) {
            android.widget.FrameLayout frameLayout = (android.widget.FrameLayout) parent;
            if (frameLayout.getTag() == TAG_SETTINGS_ENTRY || (context = view.getContext()) == null) {
                return;
            }
            android.view.ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            int i = layoutParams != null ? layoutParams.height : -1;
            if (layoutParams instanceof android.view.ViewGroup.MarginLayoutParams) {
                android.view.ViewGroup.MarginLayoutParams marginLayoutParams = (android.view.ViewGroup.MarginLayoutParams) layoutParams;
                iDp2 = marginLayoutParams.leftMargin;
                iDp = marginLayoutParams.rightMargin;
            } else {
                iDp = 0;
                iDp2 = 0;
            }
            boolean z = i == -1;
            android.widget.LinearLayout linearLayout = new android.widget.LinearLayout(context);
            linearLayout.setOrientation(1);
            linearLayout.setLayoutParams(new android.widget.FrameLayout.LayoutParams(-1, -2, 17));
            frameLayout.removeView(view);
            android.widget.LinearLayout.LayoutParams layoutParams2 = new android.widget.LinearLayout.LayoutParams(-1, z ? 0 : -2);
            if (z) {
                layoutParams2.weight = 1.0f;
            }
            layoutParams2.leftMargin = iDp2;
            layoutParams2.rightMargin = iDp;
            linearLayout.addView(view, layoutParams2);
            android.view.View viewChatPanelSettingsEntry = com.tencent.qqlitepro.hook.SettingsInjector.chatPanelSettingsEntry(context);
            android.widget.LinearLayout.LayoutParams layoutParams3 = new android.widget.LinearLayout.LayoutParams(-1, -2);
            if (iDp2 <= 0) {
                iDp2 = dp(context, 14.0f);
            }
            layoutParams3.leftMargin = iDp2;
            if (iDp <= 0) {
                iDp = dp(context, 14.0f);
            }
            layoutParams3.rightMargin = iDp;
            layoutParams3.topMargin = dp(context, 6.0f);
            layoutParams3.bottomMargin = dp(context, 10.0f);
            linearLayout.addView(viewChatPanelSettingsEntry, layoutParams3);
            frameLayout.addView(linearLayout);
            frameLayout.setTag(TAG_SETTINGS_ENTRY);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void injectAddFriendEntry(java.lang.Object obj) {
        android.content.Context context = null;
        android.os.Bundle bundle = null;
        java.lang.String string;
        android.widget.TextView textView;
        android.widget.ImageView imageView;
        if (obj == null) {
            return;
        }
        try {
            java.lang.Object objInvoke = invoke(obj, "requireContext", new java.lang.Class[0], new java.lang.Object[0]);
            context = objInvoke instanceof android.content.Context ? (android.content.Context) objInvoke : null;
        } catch (java.lang.Throwable unused) {
        }
        if (context == null) {
            return;
        }
        try {
            java.lang.Object objInvoke2 = invoke(obj, "getArguments", new java.lang.Class[0], new java.lang.Object[0]);
            bundle = objInvoke2 instanceof android.os.Bundle ? (android.os.Bundle) objInvoke2 : null;
        } catch (java.lang.Throwable unused2) {
        }
        if (bundle == null || (string = bundle.getString(KEY_PEER_UID)) == null || string.trim().isEmpty() || !com.tencent.qqlitepro.http.QqKernel.isKnownTempChatPeer(string)) {
            return;
        }
        java.lang.Object objFieldValue = fieldValue(obj, "contentView");
        if (!(objFieldValue instanceof android.widget.ScrollView)) {
            objFieldValue = fieldValue(obj, "i");
        }
        if (objFieldValue instanceof android.widget.ScrollView) {
            android.view.View childAt = ((android.widget.ScrollView) objFieldValue).getChildAt(0);
            if (childAt instanceof android.widget.LinearLayout) {
                android.widget.LinearLayout linearLayout = (android.widget.LinearLayout) childAt;
                android.content.res.Resources resources = context.getResources();
                java.lang.String packageName = context.getPackageName();
                int identifier = resources.getIdentifier("desc", "id", packageName);
                int identifier2 = resources.getIdentifier("icon", "id", packageName);
                for (int i = 0; i < linearLayout.getChildCount(); i++) {
                    android.view.View childAt2 = linearLayout.getChildAt(i);
                    android.widget.TextView textView2 = (childAt2 == null || identifier == 0) ? null : (android.widget.TextView) childAt2.findViewById(identifier);
                    if (textView2 != null && LABEL_ADD_FRIEND.contentEquals(textView2.getText())) {
                        return;
                    }
                }
                int identifier3 = resources.getIdentifier("setting_item", "layout", packageName);
                if (identifier3 == 0) {
                    return;
                }
                android.view.View viewInflate = android.view.LayoutInflater.from(context).inflate(identifier3, (android.view.ViewGroup) linearLayout, false);
                int identifier4 = resources.getIdentifier("icon_add_friend", "drawable", packageName);
                if (identifier4 != 0 && identifier2 != 0 && (imageView = (android.widget.ImageView) viewInflate.findViewById(identifier2)) != null) {
                    imageView.setImageResource(identifier4);
                }
                if (identifier != 0 && (textView = (android.widget.TextView) viewInflate.findViewById(identifier)) != null) {
                    textView.setText(LABEL_ADD_FRIEND);
                }
                viewInflate.setOnClickListener(new com.tencent.qqlitepro.hook.MediaPickerHook$$ExternalSyntheticLambda2(obj));
                linearLayout.addView(viewInflate);
                com.tencent.qqlitepro.util.Log.i("添加好友: 已在聊天设置页注入入口 uid=" + string);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void requestAddFriend(java.lang.Object obj) {
        android.content.Context contextAppContext = com.tencent.qqlitepro.cache.MsgCache.appContext();
        java.lang.String string = null;
        try {
            java.lang.Object objInvoke = invoke(obj, "requireArguments", new java.lang.Class[0], new java.lang.Object[0]);
            if (objInvoke instanceof android.os.Bundle) {
                string = ((android.os.Bundle) objInvoke).getString(KEY_PEER_UID);
            }
        } catch (java.lang.Throwable unused) {
        }
        if (string == null || string.trim().isEmpty()) {
            toast(contextAppContext, "无法获取对方账号");
            return;
        }
        java.lang.String strTrim = string.trim();
        java.lang.String strValueOf = com.tencent.qqlitepro.http.QqKernel.tempChatUinOf(strTrim) > 0 ? java.lang.String.valueOf(com.tencent.qqlitepro.http.QqKernel.tempChatUinOf(strTrim)) : "";
        com.tencent.qqlitepro.util.Log.i("添加好友: 发起申请 uid=" + strTrim + ", uin=" + strValueOf);
        toast(contextAppContext, "正在发送好友申请…");
        new java.lang.Thread(new com.tencent.qqlitepro.hook.MediaPickerHook$$ExternalSyntheticLambda0(strValueOf, strTrim, contextAppContext), "qqlitepro-addfriend").start();
    }

    static /* synthetic */ void lambda$requestAddFriend$2(java.lang.String str, java.lang.String str2, android.content.Context context) {
        java.lang.String string;
        com.tencent.qqlitepro.http.QqKernel.ApplyResult applyResultSendFriendRequest = com.tencent.qqlitepro.http.QqKernel.sendFriendRequest(str, str2, "", -1, "", "", 0, 0, 8000L);
        if (applyResultSendFriendRequest == null || applyResultSendFriendRequest.code != 0) {
            java.lang.StringBuilder sb = new java.lang.StringBuilder("添加好友失败：");
            sb.append(applyResultSendFriendRequest == null ? "未知错误" : applyResultSendFriendRequest.msg);
            string = sb.toString();
        } else {
            string = "好友申请已发送";
        }
        toast(context, string);
        java.lang.StringBuilder sb2 = new java.lang.StringBuilder("添加好友: 结果 code=");
        sb2.append(applyResultSendFriendRequest == null ? "null" : java.lang.Integer.valueOf(applyResultSendFriendRequest.code));
        sb2.append(", msg=");
        sb2.append(applyResultSendFriendRequest == null ? "" : applyResultSendFriendRequest.msg);
        com.tencent.qqlitepro.util.Log.i(sb2.toString());
    }

    private static int dp(android.content.Context context, float f) {
        return java.lang.Math.round(f * context.getResources().getDisplayMetrics().density);
    }

    private static android.view.View findRecyclerView(android.view.View view) {
        if (view == null) {
            return null;
        }
        if (isRecyclerView(view)) {
            return view;
        }
        if (view instanceof android.view.ViewGroup) {
            android.view.ViewGroup viewGroup = (android.view.ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                android.view.View viewFindRecyclerView = findRecyclerView(viewGroup.getChildAt(i));
                if (viewFindRecyclerView != null) {
                    return viewFindRecyclerView;
                }
            }
        }
        return null;
    }

    private static boolean isRecyclerView(android.view.View view) {
        try {
            java.lang.Class<?> cls = sRecyclerViewCls;
            if (cls == null) {
                cls = java.lang.Class.forName("androidx.recyclerview.widget.RecyclerView", false, view.getClass().getClassLoader());
                sRecyclerViewCls = cls;
            }
            return cls.isInstance(view);
        } catch (java.lang.Throwable unused) {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void restyleCell(java.lang.Object obj, android.view.View view) {
        java.lang.CharSequence text;
        android.content.Context context;
        android.widget.ImageView imageView = null;
        android.widget.LinearLayout linearLayout = view instanceof android.widget.LinearLayout ? (android.widget.LinearLayout) view : null;
        if (linearLayout == null) {
            return;
        }
        android.widget.TextView textView = null;
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            android.view.View childAt = linearLayout.getChildAt(i);
            if (imageView == null && (childAt instanceof android.widget.ImageView)) {
                imageView = (android.widget.ImageView) childAt;
            }
            if (textView == null && (childAt instanceof android.widget.TextView)) {
                textView = (android.widget.TextView) childAt;
            }
        }
        if (imageView == null || textView == null || (text = textView.getText()) == null || !text.toString().contains(LABEL_GALLERY) || (context = linearLayout.getContext()) == null || !com.tencent.qqlitepro.util.Config.systemMediaPickerEnabled(context)) {
            return;
        }
        takeover(obj, linearLayout, imageView);
    }

    private static void takeover(java.lang.Object obj, android.widget.LinearLayout linearLayout, android.widget.ImageView imageView) {
        try {
            com.tencent.qqlitepro.hook.MediaPickerHook$$ExternalSyntheticLambda4 mediaPickerHook$$ExternalSyntheticLambda4 = new com.tencent.qqlitepro.hook.MediaPickerHook$$ExternalSyntheticLambda4(obj);
            imageView.setOnClickListener(mediaPickerHook$$ExternalSyntheticLambda4);
            linearLayout.setClickable(true);
            linearLayout.setOnClickListener(mediaPickerHook$$ExternalSyntheticLambda4);
        } catch (java.lang.Throwable unused) {
        }
    }

    private static android.content.Intent buildPickIntent() {
        android.content.Intent intent = new android.content.Intent("android.intent.action.GET_CONTENT");
        intent.setType("*/*");
        intent.addCategory("android.intent.category.OPENABLE");
        intent.putExtra("android.intent.extra.MIME_TYPES", new java.lang.String[]{"image/*", "video/*"});
        intent.putExtra("android.intent.extra.ALLOW_MULTIPLE", true);
        return intent;
    }

    private static android.content.Intent pickVariant(java.lang.String str, java.lang.String str2, boolean z) {
        android.content.Intent intent = new android.content.Intent(str);
        intent.setType(str2);
        if (z) {
            intent.addCategory("android.intent.category.OPENABLE");
        }
        intent.putExtra("android.intent.extra.ALLOW_MULTIPLE", true);
        return intent;
    }

    private static android.content.Intent buildChooser(android.content.Context context) {
        android.content.Intent intentBuildPickIntent = buildPickIntent();
        java.util.ArrayList arrayList = new java.util.ArrayList();
        try {
            android.content.pm.PackageManager packageManager = context.getPackageManager();
            java.util.HashSet hashSet = new java.util.HashSet();
            try {
                java.util.List<android.content.pm.ResolveInfo> listQueryIntentActivities = packageManager.queryIntentActivities(intentBuildPickIntent, 0);
                if (listQueryIntentActivities != null) {
                    java.util.Iterator<android.content.pm.ResolveInfo> it = listQueryIntentActivities.iterator();
                    while (it.hasNext()) {
                        java.lang.String strComponentKey = componentKey(it.next());
                        if (strComponentKey != null) {
                            hashSet.add(strComponentKey);
                        }
                    }
                }
            } catch (java.lang.Throwable unused) {
            }
            java.util.ArrayList<android.content.Intent> arrayList2 = new java.util.ArrayList();
            arrayList2.add(pickVariant("android.intent.action.GET_CONTENT", "image/*", false));
            arrayList2.add(pickVariant("android.intent.action.GET_CONTENT", "video/*", false));
            arrayList2.add(pickVariant("android.intent.action.PICK", "image/*", false));
            arrayList2.add(pickVariant("android.intent.action.PICK", "video/*", false));
            arrayList2.add(pickVariant("android.intent.action.OPEN_DOCUMENT", "image/*", true));
            arrayList2.add(pickVariant("android.intent.action.OPEN_DOCUMENT", "video/*", true));
            for (android.content.Intent intent : arrayList2) {
                java.util.List<android.content.pm.ResolveInfo> listQueryIntentActivities2 = packageManager.queryIntentActivities(intent, 0);
                if (listQueryIntentActivities2 == null) {
                    continue;
                } else {
                    for (android.content.pm.ResolveInfo resolveInfo : listQueryIntentActivities2) {
                        java.lang.String strComponentKey2 = componentKey(resolveInfo);
                        if (strComponentKey2 != null && hashSet.add(strComponentKey2)) {
                            android.content.Intent intent2 = new android.content.Intent(intent);
                            intent2.setComponent(new android.content.ComponentName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name));
                            intent2.addFlags(1);
                            arrayList.add(intent2);
                        }
                    }
                }
            }
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("枚举可用媒体选择器应用失败: " + th);
        }
        android.content.Intent intentCreateChooser = android.content.Intent.createChooser(intentBuildPickIntent, CHOOSER_TITLE);
        intentCreateChooser.putExtra("android.intent.extra.ALLOW_MULTIPLE", true);
        if (!arrayList.isEmpty()) {
            intentCreateChooser.putExtra("android.intent.extra.INITIAL_INTENTS", (android.os.Parcelable[]) arrayList.toArray(new android.content.Intent[0]));
        }
        com.tencent.qqlitepro.util.Log.i("系统媒体选择器候选应用数（不含主意图）=" + arrayList.size());
        return intentCreateChooser;
    }

    private static java.lang.String componentKey(android.content.pm.ResolveInfo resolveInfo) {
        if (resolveInfo == null || resolveInfo.activityInfo == null) {
            return null;
        }
        return resolveInfo.activityInfo.packageName + "/" + resolveInfo.activityInfo.name;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void launchSystemPicker(java.lang.Object obj) {
        try {
            java.lang.Object objInvoke = invoke(obj, "getContext", new java.lang.Class[0], new java.lang.Object[0]);
            android.content.Context contextAppContext = objInvoke instanceof android.content.Context ? (android.content.Context) objInvoke : com.tencent.qqlitepro.cache.MsgCache.appContext();
            if (contextAppContext != null && com.tencent.qqlitepro.util.Config.systemMediaPickerEnabled(contextAppContext)) {
                rememberChat(obj);
                android.content.Intent intentBuildChooser = buildChooser(contextAppContext);
                java.lang.Object objResultHost = resultHost(obj);
                if (objResultHost == null) {
                    objResultHost = obj;
                }
                sMenuInstance = obj;
                sRequestAt = java.lang.System.currentTimeMillis();
                sHandlingResult = false;
                invoke(objResultHost, "startActivityForResult", new java.lang.Class[]{android.content.Intent.class, java.lang.Integer.TYPE}, new java.lang.Object[]{intentBuildChooser, java.lang.Integer.valueOf(REQ_MEDIA_PICK)});
                com.tencent.qqlitepro.util.Log.i("系统媒体选择器已调起：回传宿主=" + objResultHost.getClass().getSimpleName());
            }
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("调起系统媒体选择器失败: " + th);
            toast(com.tencent.qqlitepro.cache.MsgCache.appContext(), "无法调起选择器：" + th);
        }
    }

    private static java.lang.Object resultHost(java.lang.Object obj) {
        for (int i = 0; obj != null && i < 8; i++) {
            try {
                if (CLS_AIO_FRAGMENT.equals(obj.getClass().getName())) {
                    return obj;
                }
                obj = invoke(obj, "getParentFragment", new java.lang.Class[0], new java.lang.Object[0]);
            } catch (java.lang.Throwable unused) {
                return null;
            }
        }
        return null;
    }

    private static void rememberChat(java.lang.Object obj) {
        try {
            java.lang.Object objInvoke = invoke(obj, "getArguments", new java.lang.Class[0], new java.lang.Object[0]);
            if (objInvoke instanceof android.os.Bundle) {
                android.os.Bundle bundle = (android.os.Bundle) objInvoke;
                sChatType = bundle.getInt(KEY_CHAT_TYPE, -1);
                java.lang.String string = bundle.getString(KEY_PEER_UID);
                if (string == null) {
                    string = "";
                }
                sPeerUid = string;
                com.tencent.qqlitepro.util.Log.i("记录当前会话：chatType=" + sChatType + " peerUid=" + sPeerUid);
            }
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("读取当前会话参数失败: " + th);
        }
    }

    private static boolean readCurrentContact(int[] iArr, java.lang.String[] strArr) {
        java.lang.Object objInvoke;
        try {
            java.lang.ClassLoader classLoader = sClassLoader;
            if (classLoader == null || (objInvoke = java.lang.Class.forName(CLS_CURRENT_CONTACT, false, classLoader).getMethod("getCurrentContact", new java.lang.Class[0]).invoke(null, new java.lang.Object[0])) == null) {
                return false;
            }
            java.lang.Object objectField = de.robv.android.xposed.XposedHelpers.getObjectField(objInvoke, "chatType");
            java.lang.Object objectField2 = de.robv.android.xposed.XposedHelpers.getObjectField(objInvoke, "peerUid");
            int iIntValue = objectField instanceof java.lang.Integer ? ((java.lang.Integer) objectField).intValue() : 0;
            java.lang.String strValueOf = objectField2 == null ? "" : java.lang.String.valueOf(objectField2);
            if (iIntValue > 0 && !strValueOf.isEmpty()) {
                iArr[0] = iIntValue;
                strArr[0] = strValueOf;
                return true;
            }
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("读取当前会话兜底失败: " + th);
        }
        return false;
    }

    private static void closeMenuPanel(java.lang.Object obj) {
        if (obj == null) {
            return;
        }
        try {
            java.lang.Class<?> superclass = obj.getClass();
            java.lang.Object obj2 = null;
            while (true) {
                if (superclass == null || obj2 != null) {
                    break;
                }
                for (java.lang.reflect.Field field : superclass.getDeclaredFields()) {
                    if ("kotlin.jvm.functions.Function1".equals(field.getType().getName())) {
                        field.setAccessible(true);
                        java.lang.Object obj3 = field.get(obj);
                        if (obj3 != null) {
                            obj2 = obj3;
                            break;
                        }
                    }
                }
                superclass = superclass.getSuperclass();
            }
            if (obj2 == null) {
                return;
            }
            for (java.lang.reflect.Method method : obj2.getClass().getMethods()) {
                if ("invoke".equals(method.getName()) && method.getParameterTypes().length == 1) {
                    method.setAccessible(true);
                    method.invoke(obj2, 0);
                    com.tencent.qqlitepro.util.Log.i("已收起聊天「+」面板（等价宿主 selector.invoke(0)）");
                    return;
                }
            }
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("收起聊天「+」面板失败: " + th);
        }
    }

    private static java.util.List<java.lang.Object> readExtraMsg(java.lang.Class<?> cls) {
        try {
            java.lang.Object obj = cls.getField("extraMsg").get(null);
            if (obj instanceof java.util.List) {
                return (java.util.List) obj;
            }
            return null;
        } catch (java.lang.Throwable unused) {
            return null;
        }
    }

    private static java.lang.Object fieldValue(java.lang.Object obj, java.lang.String str) {
        if (obj == null) {
            return null;
        }
        for (java.lang.Class<?> superclass = obj.getClass(); superclass != null; superclass = superclass.getSuperclass()) {
            try {
                java.lang.reflect.Field declaredField = superclass.getDeclaredField(str);
                declaredField.setAccessible(true);
                return declaredField.get(obj);
            } catch (java.lang.NoSuchFieldException unused) {
            } catch (java.lang.Throwable unused2) {
                return null;
            }
        }
        return null;
    }

    private static java.lang.Object invoke(java.lang.Object obj, java.lang.String str, java.lang.Class<?>[] clsArr, java.lang.Object[] objArr) throws java.lang.Exception {
        for (java.lang.Class<?> superclass = obj.getClass(); superclass != null; superclass = superclass.getSuperclass()) {
            try {
                java.lang.reflect.Method declaredMethod = superclass.getDeclaredMethod(str, clsArr);
                declaredMethod.setAccessible(true);
                return declaredMethod.invoke(obj, objArr);
            } catch (java.lang.NoSuchMethodException unused) {
            }
        }
        throw new java.lang.NoSuchMethodException(str);
    }

    private static void onPicked(java.lang.Object obj, android.content.Intent intent) {
        android.content.Context contextAppContext = com.tencent.qqlitepro.cache.MsgCache.appContext();
        if (contextAppContext == null) {
            com.tencent.qqlitepro.util.Log.w("系统媒体选择器：应用上下文为空，放弃处理");
            return;
        }
        java.util.List<android.net.Uri> listPickedUris = pickedUris(intent);
        com.tencent.qqlitepro.util.Log.i("系统媒体选择器：回传 " + listPickedUris.size() + " 个 Uri");
        if (listPickedUris.isEmpty()) {
            sHandlingResult = false;
            toast(contextAppContext, "未读取到所选文件");
            return;
        }
        toast(contextAppContext, "已选择 " + listPickedUris.size() + " 个文件，正在处理...");
        sHandlingResult = false;
        new java.lang.Thread(new com.tencent.qqlitepro.hook.MediaPickerHook$$ExternalSyntheticLambda7(listPickedUris, contextAppContext, obj), "antirecall-media-pick").start();
    }

    static /* synthetic */ void lambda$onPicked$6(java.util.List list, android.content.Context context, java.lang.Object obj) {
        java.lang.Object objCreatePicElement;
        java.util.ArrayList arrayList = new java.util.ArrayList();
        java.util.Iterator it = list.iterator();
        while (it.hasNext()) {
            android.net.Uri uri = (android.net.Uri) it.next();
            try {
                java.io.File fileCopyToPrivate = copyToPrivate(context, uri);
                if (fileCopyToPrivate == null) {
                    com.tencent.qqlitepro.util.Log.w("复制所选媒体失败（返回空）: " + uri);
                } else {
                    if (isVideo(context, uri, fileCopyToPrivate)) {
                        objCreatePicElement = com.tencent.qqlitepro.http.QqKernel.createVideoElement(fileCopyToPrivate.getAbsolutePath());
                    } else {
                        objCreatePicElement = com.tencent.qqlitepro.http.QqKernel.createPicElement(fileCopyToPrivate.getAbsolutePath());
                    }
                    if (objCreatePicElement != null) {
                        arrayList.add(objCreatePicElement);
                    }
                }
            } catch (java.lang.Throwable th) {
                com.tencent.qqlitepro.util.Log.w("构建所选媒体失败: " + th);
            }
        }
        if (arrayList.isEmpty()) {
            com.tencent.qqlitepro.util.Log.w("系统媒体选择器：未读取到任何所选文件");
            toast(context, "未读取到所选文件");
        } else {
            new android.os.Handler(android.os.Looper.getMainLooper()).post(new com.tencent.qqlitepro.hook.MediaPickerHook$$ExternalSyntheticLambda3(new java.util.ArrayList(arrayList), obj, context));
        }
    }

    static /* synthetic */ void lambda$onPicked$5(java.util.List list, java.lang.Object obj, android.content.Context context) {
        java.util.ArrayList arrayList = new java.util.ArrayList();
        java.util.ArrayList arrayList2 = new java.util.ArrayList();
        for (java.lang.Object obj2 : list) {
            if (isVideoElement(obj2)) {
                arrayList.add(obj2);
            } else {
                arrayList2.add(obj2);
            }
        }
        java.util.ArrayList arrayList3 = new java.util.ArrayList();
        if (!arrayList2.isEmpty() && !previewImages(obj, arrayList2)) {
            arrayList3.addAll(arrayList2);
        }
        arrayList3.addAll(arrayList);
        if (arrayList3.isEmpty()) {
            return;
        }
        new java.lang.Thread(new com.tencent.qqlitepro.hook.MediaPickerHook$$ExternalSyntheticLambda6(context, sChatType, sPeerUid, arrayList3), "antirecall-media-send").start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void resolveAndSend(android.content.Context context, int i, java.lang.String str, java.util.List<java.lang.Object> list) {
        if (str == null) {
            str = "";
        }
        if (i <= 0 || str.isEmpty()) {
            int[] iArr = {0};
            java.lang.String[] strArr = {""};
            if (readCurrentContact(iArr, strArr)) {
                i = iArr[0];
                str = strArr[0];
                com.tencent.qqlitepro.util.Log.i("会话参数缺失，已用当前会话兜底：chatType=" + i + " peerUid=" + str);
            }
        }
        if (i <= 0 || str.isEmpty()) {
            com.tencent.qqlitepro.util.Log.w("系统媒体选择器：无法确定当前会话，取消发送");
            toast(context, "无法确定当前会话，发送取消");
        } else {
            sendElements(context, i, str, list);
        }
    }

    private static boolean isVideoElement(java.lang.Object obj) {
        try {
            return de.robv.android.xposed.XposedHelpers.getObjectField(obj, "videoElement") != null;
        } catch (java.lang.Throwable unused) {
            return false;
        }
    }

    /* JADX WARN: Code duplicated, block: B:24:0x00ae  */
    /* JADX WARN: Code duplicated, block: B:25:0x00c2  */
    /* JADX WARN: Instruction removed from duplicated block: B:24:0x00ae, please report this as an issue */
    /* JADX WARN: Instruction removed from duplicated block: B:25:0x00c2, please report this as an issue */
    private static void sendElements(android.content.Context context, int i, java.lang.String str, java.util.List<java.lang.Object> list) {
        int i2;
        java.lang.String str2;
        toast(context, "正在发送 " + list.size() + " 个文件...");
        int i3 = 0;
        try {
            java.lang.Object objCreateContact = com.tencent.qqlitepro.http.QqKernel.createContact(i, str);
            i2 = 0;
            for (java.lang.Object obj : list) {
                try {
                    try {
                        java.util.ArrayList arrayList = new java.util.ArrayList();
                        arrayList.add(obj);
                        com.tencent.qqlitepro.http.QqKernel.SendResult sendResultSendMsg = com.tencent.qqlitepro.http.QqKernel.sendMsg(objCreateContact, arrayList, 30000L);
                        if (sendResultSendMsg.code == 0) {
                            i3++;
                        } else {
                            i2++;
                            com.tencent.qqlitepro.util.Log.w("发送失败 code=" + sendResultSendMsg.code + " " + sendResultSendMsg.msg);
                        }
                    } catch (java.lang.Throwable th) {
                        i2++;
                        com.tencent.qqlitepro.util.Log.w("发送所选媒体失败: " + th);
                    }
                } catch (java.lang.Throwable th2) {
                    java.lang.Throwable thRethrow = th2;
                    com.tencent.qqlitepro.util.Log.w("系统媒体选择器发送异常: " + thRethrow);
                    com.tencent.qqlitepro.util.Log.i("系统媒体选择器发送完成：成功 " + i3 + " 失败 " + i2);
                    if (i2 == 0) {
                        str2 = "已发送 " + i3 + " 个文件";
                    } else {
                        str2 = "发送完成：成功 " + i3 + "，失败 " + i2;
                    }
                    toast(context, str2);
                }
            }
        } catch (java.lang.Throwable th3) {
            java.lang.Throwable thRethrow2 = th3;
            i2 = 0;
        }
        com.tencent.qqlitepro.util.Log.i("系统媒体选择器发送完成：成功 " + i3 + " 失败 " + i2);
        if (i2 == 0) {
            str2 = "已发送 " + i3 + " 个文件";
        } else {
            str2 = "发送完成：成功 " + i3 + "，失败 " + i2;
        }
        toast(context, str2);
    }

    private static boolean previewImages(java.lang.Object obj, java.util.List<java.lang.Object> list) {
        java.lang.ClassLoader classLoader = sClassLoader;
        if (classLoader != null && !list.isEmpty()) {
            closeMenuPanel(obj);
            if (insertInlineTokens(classLoader, list)) {
                com.tencent.qqlitepro.util.Log.i("已插入内联输入框预览标签 " + list.size() + " 个");
                return true;
            }
            try {
                java.lang.Class<?> cls = java.lang.Class.forName(CLS_IME_OPERATION, false, classLoader);
                java.lang.Object obj2 = cls.getField("INSTANCE").get(null);
                if (cls.getField("openIME").get(null) == null) {
                    com.tencent.qqlitepro.util.Log.w("输入框尚未创建（openIME 为空），图片改为直接发送");
                    return false;
                }
                java.util.List<java.lang.Object> extraMsg = readExtraMsg(cls);
                if (extraMsg == null) {
                    com.tencent.qqlitepro.util.Log.w("找不到 IMEOperation.extraMsg 字段，图片改为直接发送");
                    return false;
                }
                extraMsg.addAll(list);
                cls.getMethod("openIME", new java.lang.Class[0]).invoke(obj2, new java.lang.Object[0]);
                com.tencent.qqlitepro.util.Log.i("已把 " + list.size() + " 张图片写入宿主输入框（extraMsg + openIME）");
                return true;
            } catch (java.lang.Throwable thRethrow2) {
                com.tencent.qqlitepro.util.Log.w("写入宿主输入框失败，图片改为直接发送: " + thRethrow2);
            }
        }
        return false;
    }

    private static boolean insertInlineTokens(java.lang.ClassLoader classLoader, java.util.List<java.lang.Object> list) {
        if (classLoader != null && !list.isEmpty()) {
            try {
                java.lang.Class<?> cls = java.lang.Class.forName(CLS_INLINE_INPUT, false, classLoader);
                java.lang.Object obj = cls.getField("INSTANCE").get(null);
                if (!java.lang.Boolean.TRUE.equals(cls.getMethod("isReady", new java.lang.Class[0]).invoke(obj, new java.lang.Object[0]))) {
                    com.tencent.qqlitepro.util.Log.w("内联输入框尚未就绪（isReady=false），改用宿主原生预览通道");
                    return false;
                }
                java.lang.reflect.Method declaredMethod = cls.getDeclaredMethod("editText", new java.lang.Class[0]);
                declaredMethod.setAccessible(true);
                java.lang.Object objInvoke = declaredMethod.invoke(obj, new java.lang.Object[0]);
                if (objInvoke == null) {
                    com.tencent.qqlitepro.util.Log.w("内联输入框 editText 为空，改用宿主原生预览通道");
                    return false;
                }
                com.tencent.qqlitepro.util.Log.i("内联输入框就绪：editText=" + java.lang.System.identityHashCode(objInvoke));
                int iIntValue = -11048043;
                try {
                    java.lang.reflect.Method declaredMethod2 = cls.getDeclaredMethod("getTokenColor", new java.lang.Class[0]);
                    declaredMethod2.setAccessible(true);
                    java.lang.Object objInvoke2 = declaredMethod2.invoke(obj, new java.lang.Object[0]);
                    if (objInvoke2 instanceof java.lang.Integer) {
                        iIntValue = ((java.lang.Integer) objInvoke2).intValue();
                    }
                } catch (java.lang.Throwable unused) {
                }
                java.lang.Object objCallMethod = de.robv.android.xposed.XposedHelpers.callMethod(objInvoke, "getText", new java.lang.Object[0]);
                if (!(objCallMethod instanceof android.text.Editable)) {
                    com.tencent.qqlitepro.util.Log.w("内联输入框文本不可编辑，改用宿主原生预览通道");
                    return false;
                }
                android.text.Editable editable = (android.text.Editable) objCallMethod;
                java.lang.Object objCallMethod2 = de.robv.android.xposed.XposedHelpers.callMethod(objInvoke, "getSelectionStart", new java.lang.Object[0]);
                java.lang.Object objCallMethod3 = de.robv.android.xposed.XposedHelpers.callMethod(objInvoke, "getSelectionEnd", new java.lang.Object[0]);
                int iIntValue2 = objCallMethod2 instanceof java.lang.Integer ? ((java.lang.Integer) objCallMethod2).intValue() : 0;
                int iIntValue3 = objCallMethod3 instanceof java.lang.Integer ? ((java.lang.Integer) objCallMethod3).intValue() : iIntValue2;
                int iMax = java.lang.Math.max(0, java.lang.Math.min(iIntValue2, editable.length()));
                int iMax2 = java.lang.Math.max(iMax, java.lang.Math.min(iIntValue3, editable.length()));
                java.lang.reflect.Constructor<?> constructor = java.lang.Class.forName(CLS_IMAGE_TAG, false, classLoader).getConstructor(java.lang.Class.forName(CLS_MSG_ELEMENT, false, classLoader));
                for (java.lang.Object obj2 : list) {
                    android.text.SpannableString spannableString = new android.text.SpannableString(isVideoElement(obj2) ? "[视频]" : "[图片]");
                    java.lang.Object objNewInstance = constructor.newInstance(obj2);
                    spannableString.setSpan(objNewInstance, 0, spannableString.length(), 33);
                    spannableString.setSpan(new android.text.style.ForegroundColorSpan(iIntValue), 0, spannableString.length(), 33);
                    editable.replace(iMax, iMax2, spannableString);
                    if (editable.getSpanStart(objNewInstance) < 0) {
                        com.tencent.qqlitepro.util.Log.w("内联输入框标签写入失败，改用宿主原生预览通道");
                        return false;
                    }
                    iMax += spannableString.length();
                    iMax2 = iMax;
                }
                com.tencent.qqlitepro.util.Log.i("内联输入框标签写入成功，共 " + list.size() + " 个");
                try {
                    java.lang.reflect.Method declaredMethod3 = cls.getDeclaredMethod("focusAndShow$default", cls, java.lang.Boolean.TYPE, java.lang.Integer.TYPE, java.lang.Object.class);
                    declaredMethod3.setAccessible(true);
                    declaredMethod3.invoke(null, obj, false, 1, null);
                } catch (java.lang.Throwable thRethrow2) {
                    com.tencent.qqlitepro.util.Log.w("内联输入框聚焦失败: " + thRethrow2);
                }
                return true;
            } catch (java.lang.Throwable th2) {
                com.tencent.qqlitepro.util.Log.w("插入内联输入框标签失败: " + th2);
            }
        }
        return false;
    }

    private static java.util.List<android.net.Uri> pickedUris(android.content.Intent intent) {
        java.util.ArrayList arrayList = new java.util.ArrayList();
        android.content.ClipData clipData = intent.getClipData();
        if (clipData != null) {
            for (int i = 0; i < clipData.getItemCount(); i++) {
                android.net.Uri uri = clipData.getItemAt(i).getUri();
                if (uri != null) {
                    arrayList.add(uri);
                }
            }
        }
        if (arrayList.isEmpty() && intent.getData() != null) {
            arrayList.add(intent.getData());
        }
        return arrayList;
    }

    private static java.io.File copyToPrivate(android.content.Context context, android.net.Uri uri) {
        java.io.InputStream fileInputStream = null;
        java.io.FileOutputStream fileOutputStream = null;
        try {
            fileInputStream = "file".equals(uri.getScheme())
                    ? new java.io.FileInputStream(new java.io.File(uri.getPath()))
                    : context.getContentResolver().openInputStream(uri);
            if (fileInputStream == null) {
                return null;
            }
            java.io.File dir = new java.io.File(context.getFilesDir(), "media_pick");
            if (!dir.exists() && !dir.mkdirs()) {
                return null;
            }
            java.io.File target = new java.io.File(dir, "pick_" + java.lang.System.currentTimeMillis()
                    + "_" + java.lang.Math.abs(uri.hashCode()) + "." + guessExtension(context, uri));
            fileOutputStream = new java.io.FileOutputStream(target);
            byte[] buffer = new byte[8192];
            int read;
            while ((read = fileInputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, read);
            }
            fileOutputStream.flush();
            return target;
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("复制所选媒体失败: " + th);
            return null;
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (java.lang.Throwable unused) {
                }
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (java.lang.Throwable unused2) {
                }
            }
        }
    }

    private static boolean isVideo(android.content.Context context, android.net.Uri uri, java.io.File file) {
        try {
            java.lang.String type = context.getContentResolver().getType(uri);
            if (type != null && !type.isEmpty()) {
                return type.startsWith("video/");
            }
        } catch (java.lang.Throwable unused) {
        }
        java.lang.String lowerCase = file.getName().toLowerCase();
        return lowerCase.endsWith(".mp4") || lowerCase.endsWith(".3gp") || lowerCase.endsWith(".webm") || lowerCase.endsWith(".mkv") || lowerCase.endsWith(".mov") || lowerCase.endsWith(".avi");
    }

    private static java.lang.String guessExtension(android.content.Context context, android.net.Uri uri) {
        int iLastIndexOf;
        int iIndexOf;
        try {
            java.lang.String type = context.getContentResolver().getType(uri);
            if (type != null && (iIndexOf = type.indexOf(47)) > 0 && iIndexOf < type.length() - 1) {
                java.lang.String lowerCase = type.substring(iIndexOf + 1).toLowerCase();
                int iIndexOf2 = lowerCase.indexOf(59);
                if (iIndexOf2 > 0) {
                    lowerCase = lowerCase.substring(0, iIndexOf2);
                }
                if (!lowerCase.isEmpty() && lowerCase.matches("[a-z0-9]{1,5}")) {
                    return "jpeg".equals(lowerCase) ? "jpg" : lowerCase;
                }
            }
        } catch (java.lang.Throwable unused) {
        }
        try {
            java.lang.String lastPathSegment = uri.getLastPathSegment();
            if (lastPathSegment != null && (iLastIndexOf = lastPathSegment.lastIndexOf(46)) > 0 && iLastIndexOf < lastPathSegment.length() - 1) {
                java.lang.String lowerCase2 = lastPathSegment.substring(iLastIndexOf + 1).toLowerCase();
                if (lowerCase2.matches("[a-z0-9]{1,5}")) {
                    return lowerCase2;
                }
            }
        } catch (java.lang.Throwable unused2) {
        }
        return "jpg";
    }

    private static void toast(android.content.Context context, java.lang.String str) {
        if (context == null) {
            return;
        }
        new android.os.Handler(android.os.Looper.getMainLooper()).post(new com.tencent.qqlitepro.hook.MediaPickerHook$$ExternalSyntheticLambda5(context, str));
}
}
