/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.app.Dialog
 *  android.content.Context
 *  android.content.ContextWrapper
 *  android.graphics.Bitmap
 *  android.graphics.Bitmap$CompressFormat
 *  android.graphics.Bitmap$Config
 *  android.graphics.BitmapFactory
 *  android.graphics.Canvas
 *  android.graphics.Paint
 *  android.graphics.PorterDuff$Mode
 *  android.graphics.PorterDuffXfermode
 *  android.graphics.Rect
 *  android.graphics.RectF
 *  android.graphics.Typeface
 *  android.graphics.Xfermode
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.GradientDrawable
 *  android.os.Bundle
 *  android.os.Handler
 *  android.os.Looper
 *  android.text.TextUtils$TruncateAt
 *  android.util.Log
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.view.View$OnLongClickListener
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.widget.CompoundButton
 *  android.widget.FrameLayout
 *  android.widget.FrameLayout$LayoutParams
 *  android.widget.ImageView
 *  android.widget.LinearLayout
 *  android.widget.LinearLayout$LayoutParams
 *  android.widget.ScrollView
 *  android.widget.TextView
 *  com.tencent.qqlitepro.hook.ChatCardInjector$$ExternalSyntheticLambda1
 *  com.tencent.qqlitepro.hook.SettingsInjector
 *  com.tencent.qqlitepro.http.QqKernel
 *  com.tencent.qqlitepro.mail.QqMailManager
 *  com.tencent.qqlitepro.util.Config
 *  com.tencent.qqlitepro.util.Log
 *  com.tencent.qqlitepro.util.Log$$ExternalSyntheticBackport0
 *  de.robv.android.xposed.XC_MethodHook
 *  de.robv.android.xposed.XC_MethodHook$MethodHookParam
 *  de.robv.android.xposed.XposedBridge
 *  de.robv.android.xposed.XposedHelpers
 */
package com.tencent.qqlitepro.hook;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.tencent.qqlitepro.hook.SettingsInjector;
import com.tencent.qqlitepro.http.QqKernel;
import com.tencent.qqlitepro.mail.QqMailManager;
import com.tencent.qqlitepro.util.Config;
import com.tencent.qqlitepro.util.Log;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.function.Consumer;

public class ChatCardInjector {
    private static final String ABS_ITEM = "com.tencent.qqnt.watch.ui.componet.AbsItem";
    private static final String ADAPTER_CLASS = "androidx.recyclerview.widget.RecyclerView$Adapter";
    private static final String AVATAR_URL = "https://img2.baidu.com/it/u=3554844775,4056477822&fm=253&fmt=auto&app=120&f=JPEG?w=247&h=247";
    private static final int CARD_TYPE = -10;
    /** 宿主原生会话卡 viewType（0 = ItemRecentContact） */
    private static final int HOST_ITEM_TYPE = 0;
    private static final int TEMP_CHAT_CARD_TYPE = -11;
    private static final String CLEAR_ITEM = "momoi.mod.qqpro.hook.ClearMsgListItem";
    private static final String DELETE_ITEM = "com.tencent.qqnt.watch.chat.list.longclick.DeleteItem";
    private static final String DISTURB_ITEM = "com.tencent.qqnt.watch.chat.list.longclick.SetDisturbItem";
    private static final String HEADER_HOLDER = "com.tencent.qqnt.chats.core.adapter.holder.HeaderItemHolder";
    private static final String HOST_LAYOUT = "com.tencent.qqnt.chats.kit.x2k.ChatRecentContactItemLayout";
    private static final int KEY_BINDING = 1296124236;
    private static final String RECENT_ITEM = "com.tencent.qqnt.chats.core.adapter.itemdata.RecentContactChatItem";
    private static final String RV_CLASS = "androidx.recyclerview.widget.RecyclerView";
    private static final String SELECT_DIALOG = "com.tencent.qqnt.watch.ui.componet.select.SelectDialogFragment";
    private static final String TAG = "ChatCard";
    private static final String TOP_ITEM = "com.tencent.qqnt.watch.chat.list.longclick.SetTopItem";
    private static volatile Object sActiveMenu;
    private static volatile Context sAppCtx;
    private static final Map<Object, Boolean> sInjected;
    private static final Map<Object, View> sCards;
    private static final Map<Object, View> sTempCards;
    private static volatile boolean sNativeMenuHooked;
    private static volatile boolean sPatched;
    private static volatile Object sListAdapter;
    private static volatile Object sBindOverride;
    private static final Map<Object, SlotVal> sSlotCache;
    private static final Map<Object, Integer> sBaseUnread;
    private static final Map<Object, Integer> sLastSlots;
    private static volatile long sLastSlotRelayout;
    private static volatile Boolean sMailEnabledCache;
    private static Runnable fPendingFill;
    private static Handler fPendingHandler;
    private static final Set<Object> sCardMenuItems;
    /* 当前菜单归属：mail=邮箱卡片 temp=临时会话 normal=宿主原生 */
    private static volatile String sMenuKind = "normal";
    /* 主列表 RecyclerView（injectAdapter 时保存）——导航/Activity 获取用 */
    private static volatile ViewGroup sMainRecyclerView;

    public static int cardSlot(Object obj) {
        return 0;
    }

    public static int tempChatCardSlot(Object obj) {
        /* 汇总卡已停用：临时会话改为直接插入主会话列表 */
        return -1;
    }

    private static boolean tempChatFeatureOn() {
        try {
            Context ctx = sAppCtx;
            if (ctx == null) {
                return true;
            }
            return Config.tempChatEnabled((Context)ctx);
        }
        catch (Throwable ignored) {
            return true;
        }
    }

    /** 当前 adapter 的额外卡片数（必须与 getItemViewType/getItem 的实际产出严格一致） */
    private static int extraCardCountFor(Object adapter) {
        int n = 1;   /* 邮箱卡 */
        if (ChatCardInjector.tempChatFeatureOn()) {
            try {
                n += insertPlan(adapter).size();
            } catch (Throwable ignored) {}
        }
        return n;
    }

    private static int extraCardCount() {
        int n = 1;
        if (ChatCardInjector.tempChatFeatureOn()) {
            n += Math.min(50, com.tencent.qqlitepro.http.QqKernel.tempChatCount());
        }
        return n;
    }

    /** 临时会话卡在列表中的起始下标（邮箱卡之后） */
    private static int tempChatBaseSlot() {
        return 1;
    }

    /**
     * 逐位时间穿插：临时会话按 msgTime 与宿主会话混排。
     * 返回 List<int[]>：{临时序号, 宿主会话下标(0..hostCount)}
     */
    private static java.util.List<int[]> insertPlan(Object adapter) {
        java.util.List<int[]> plan = new java.util.ArrayList<int[]>();
        try {
            java.util.List<Object[]> snap = com.tencent.qqlitepro.http.QqKernel.tempChatSnapshot();
            if (snap == null || snap.isEmpty()) {
                return plan;
            }
            long[] hostTimes = hostItemTimes(adapter);
            int hostCount = hostTimes.length;
            int limit = Math.min(50, snap.size());
            for (int i = 0; i < limit; i++) {
                Object[] one = snap.get(i);
                long t = com.tencent.qqlitepro.http.QqKernel.longOf(one[4]);
                int at = hostCount;
                for (int k = 0; k < hostCount; k++) {
                    if (hostTimes[k] <= t) {
                        at = k;
                        break;
                    }
                }
                plan.add(new int[]{i, at});
            }
        } catch (Throwable th) {
            Log.w("insertPlan 失败: " + th);
        }
        return plan;
    }

    /** 宿主当前列表每条会话的 lastMsgTime（秒->毫秒归一化） */
    private static long[] hostItemTimes(Object adapter) {
        try {
            Object cur = XposedHelpers.callMethod(adapter, "getCurrentList", new Object[0]);
            if (!(cur instanceof List)) {
                return new long[0];
            }
            List list = (List) cur;
            java.util.ArrayList<Long> times = new java.util.ArrayList<Long>();
            for (int i = 0; i < list.size(); i++) {
                Object it = list.get(i);
                if (it == null) continue;
                String cn = it.getClass().getName();
                /* 只统计真实会话卡（跳过 BannerHeaderItem 等头部项） */
                if (!cn.endsWith("RecentContactChatItem")) continue;
                long t = 0;
                try {
                    Object info = XposedHelpers.getObjectField(it, "a");
                    t = com.tencent.qqlitepro.http.QqKernel.longOf(
                            XposedHelpers.getObjectField(info, "msgTime"));
                    if (t > 0 && t < 100000000000L) t *= 1000L;
                } catch (Throwable ignored) {}
                times.add(Long.valueOf(t));
            }
            long[] out = new long[times.size()];
            for (int i = 0; i < out.length; i++) {
                out[i] = times.get(i).longValue();
            }
            return out;
        } catch (Throwable ignored) {
            return new long[0];
        }
    }

    /**
     * 统一位置解析：显示位置 pos 属于哪一段。
     * 布局：[0]=邮箱卡，[1..N]=临时会话卡（按时间），[N+1..]=宿主会话（按时间穿插）。
     * 返回 int[]{类型, 索引}：类型 0=邮箱卡，1=临时卡，2=宿主卡
     */
    private static int[] resolvePos(Object adapter, int pos) {
        try {
            java.util.List<int[]> plan = insertPlan(adapter);
            int hostCount = hostItemTimes(adapter).length;
            int n = plan.size();
            /* 展开显示序列 */
            int cursor = 0;
            if (pos == cursor) {
                return new int[]{0, 0};
            }
            cursor++;
            int planIdx = 0;
            int hostIdx = 0;
            while (planIdx < n || hostIdx < hostCount) {
                /* 该宿主位置前要插入的临时卡 */
                while (planIdx < n && plan.get(planIdx)[1] <= hostIdx) {
                    if (pos == cursor) {
                        return new int[]{1, plan.get(planIdx)[0]};
                    }
                    cursor++;
                    planIdx++;
                }
                if (hostIdx < hostCount) {
                    if (pos == cursor) {
                        return new int[]{2, hostIdx};
                    }
                    cursor++;
                    hostIdx++;
                }
            }
        } catch (Throwable ignored) {}
        return new int[]{-1, -1};
    }

    /**
     * 显示位置 pos -> 宿主真实 list 下标（时间穿插的逆映射）。
     * 临时卡返回 0（占位，getItem 结果会被 onBindViewHolder 覆盖渲染）。
     */
    private static int hostIndexForPos(Object adapter, int pos) {
        try {
            int[] r = resolvePos(adapter, pos);
            if (r[0] == 2) {
                return r[1];
            }
            return 0;
        } catch (Throwable ignored) {
            return Math.max(0, pos);
        }
    }


    /** 位置 pos 是否为临时会话卡；是则返回其序号，否则 -1（时间穿插版） */
    private static int tempChatIndexAtPos(Object adapter, int pos) {
        if (!ChatCardInjector.tempChatFeatureOn()) return -1;
        try {
            Object mailPos = Integer.valueOf(cardSlot(adapter));
            int mp = mailPos instanceof Integer ? (Integer) mailPos : 0;
            java.util.List<int[]> plan = insertPlan(adapter);
            int cursor = 1;   /* 邮箱卡占 0 */
            int planIdx = 0;
            int hostIdx = 0;
            int hostCount = hostItemTimes(adapter).length;
            while (true) {
                boolean mailSlot = (mp == cursor);
                if (mailSlot) {
                    if (pos == cursor) return -1;
                    cursor++;
                    continue;
                }
                while (planIdx < plan.size() && plan.get(planIdx)[1] <= hostIdx) {
                    if (pos == cursor) {
                        return plan.get(planIdx)[0];
                    }
                    cursor++;
                    planIdx++;
                }
                if (hostIdx >= hostCount && planIdx >= plan.size()) {
                    return -1;
                }
                if (pos == cursor) {
                    return -1;   /* 宿主普通项 */
                }
                cursor++;
                hostIdx++;
            }
        } catch (Throwable ignored) {
            return -1;
        }
    }

    /** 该位置对应的临时会话序号（0..N-1），非临时卡返回 -1 */
    private static int tempChatIndexAt(int pos) {
        if (!ChatCardInjector.tempChatFeatureOn()) return -1;
        int idx = pos - tempChatBaseSlot();
        int n = Math.min(50, com.tencent.qqlitepro.http.QqKernel.tempChatCount());
        return (idx >= 0 && idx < n) ? idx : -1;
    }

    private static boolean isTempChatCardPos(Object adapter, int pos) {
        return ChatCardInjector.tempChatFeatureOn() && pos == ChatCardInjector.tempChatCardSlot(adapter);
    }

    public static void patch(ClassLoader classLoader) {
        if (sPatched) {
            return;
        }
        sPatched = true;
        try {
            XposedHelpers.findAndHookMethod(Class.forName(RV_CLASS, false, classLoader), (String)"setAdapter", (Object[])new Object[]{Class.forName(ADAPTER_CLASS, false, classLoader), new AnonymousClass1(classLoader)});
            Log.i((String)"ChatCard \u5df2 hook RecyclerView.setAdapter \u6ce8\u5165\u90ae\u7bb1\u4f1a\u8bdd\u5361\u7247");
            ChatCardInjector.attachExistingLists(classLoader);
        }
        catch (Throwable th) {
            Log.w((String)("ChatCard hook \u6ce8\u518c\u5931\u8d25: " + th));
        }
    }

    public static boolean isChatListAdapter(Object obj, ClassLoader classLoader) {
        String name = obj.getClass().getName();
        if (name.contains("Recent") || name.contains("WatchRecent") || name.contains("Chats")) {
            return true;
        }
        try {
            List list;
            Class<?> cls = Class.forName(RECENT_ITEM, false, classLoader);
            Object objCallMethod = XposedHelpers.callMethod((Object)obj, (String)"getCurrentList", (Object[])new Object[0]);
            if (objCallMethod instanceof List && !(list = (List)objCallMethod).isEmpty() && cls.isInstance(list.get(0))) {
                return true;
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        return false;
    }

    public static void injectAdapter(Context context, Object obj, ClassLoader classLoader, ViewGroup viewGroup) {
        Class<?> cls = obj.getClass();
        AnonymousClass2 anonymousClass2 = new AnonymousClass2();
        AnonymousClass3 anonymousClass3 = new AnonymousClass3();
        AnonymousClass4 anonymousClass4 = new AnonymousClass4();
        AnonymousClass5 anonymousClass5 = new AnonymousClass5();
        AnonymousClass6 anonymousClass6 = new AnonymousClass6(classLoader);
        AnonymousClass7 anonymousClass7 = new AnonymousClass7();
        AnonymousClass8 anonymousClass8 = new AnonymousClass8();
        AnonymousClass9 anonymousClass9 = new AnonymousClass9();
        try {
            if (sAppCtx == null) {
                sAppCtx = context != null ? context.getApplicationContext() : null;
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        Log.i((String)("ChatCard \u6ce8\u5165 " + cls.getName() + "\uff08CARD_TYPE=-10\uff09"));
        int iHookInHierarchy = ChatCardInjector.hookInHierarchy(cls, "getItemCount", anonymousClass2);
        int iHookInHierarchy2 = ChatCardInjector.hookInHierarchy(cls, "getItemViewType", anonymousClass3);
        int iHookItemBySignature = ChatCardInjector.hookItemBySignature(cls, anonymousClass4);
        int iHookInHierarchy3 = ChatCardInjector.hookInHierarchy(cls, "onBindViewHolder", anonymousClass5);
        ChatCardInjector.hookInHierarchy(cls, "onBindViewHolder", new AnonymousClass5B());
        int iHookInHierarchy4 = ChatCardInjector.hookInHierarchy(cls, "onCreateViewHolder", anonymousClass6);
        ChatCardInjector.hookInHierarchy(cls, "hasStableIds", new AnonymousClass10());
        ChatCardInjector.hookInHierarchy(cls, "getItem", new GetItemOverride());
        int iHookAll = 0;
        try {
            Class<?> cls2 = Class.forName(ADAPTER_CLASS, false, classLoader);
            iHookAll = ChatCardInjector.hookAll(cls2, "notifyItemChanged", anonymousClass7) + ChatCardInjector.hookAll(cls2, "notifyItemInserted", anonymousClass7) + ChatCardInjector.hookAll(cls2, "notifyItemRemoved", anonymousClass7) + ChatCardInjector.hookAll(cls2, "notifyItemRangeChanged", anonymousClass8) + ChatCardInjector.hookAll(cls2, "notifyItemRangeInserted", anonymousClass8) + ChatCardInjector.hookAll(cls2, "notifyItemRangeRemoved", anonymousClass8);
            iHookAll += ChatCardInjector.hookAll(cls2, "notifyItemMoved", anonymousClass9);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        sInjected.put(obj, Boolean.TRUE);
        sListAdapter = obj;
        /* 保存主列表 RecyclerView（后续导航用它的 Context/根视图） */
        try {
            if (viewGroup != null) {
                sMainRecyclerView = viewGroup;
            }
        } catch (Throwable ignored) {}
        Log.i((String)("ChatCard \u5df2\u6ce8\u5165\u4f1a\u8bdd\u5217\u8868\u9002\u914d\u5668: " + cls.getName() + " count=" + iHookInHierarchy + " type=" + iHookInHierarchy2 + " item=" + iHookItemBySignature + " bind=" + iHookInHierarchy3 + " create=" + iHookInHierarchy4 + " notify=" + iHookAll));
    }

    public static Object newHeaderHolder(Object obj, View view) {
        try {
            Constructor<?> constructor = Class.forName(HEADER_HOLDER, false, obj.getClass().getClassLoader()).getConstructor(View.class);
            constructor.setAccessible(true);
            return constructor.newInstance(view);
        }
        catch (Throwable th) {
            Log.w((String)("ChatCard HeaderItemHolder \u6784\u5efa\u5931\u8d25: " + th));
            return null;
        }
    }

    private static int countUnreadSessions(Object obj) {
        long jCurrentTimeMillis = System.currentTimeMillis();
        SlotVal slotVal = sSlotCache.get(obj);
        if (slotVal != null && jCurrentTimeMillis - slotVal.ts < 250L) {
            return slotVal.unread;
        }
        int i = 0;
        try {
            Object objCallMethod = XposedHelpers.callMethod((Object)obj, (String)"getCurrentList", (Object[])new Object[0]);
            if (objCallMethod instanceof List) {
                for (Object obj2 : (List)objCallMethod) {
                    if (obj2 == null || ChatCardInjector.unreadCount(obj2) <= 0L) continue;
                    ++i;
                }
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        sSlotCache.put(obj, new SlotVal(jCurrentTimeMillis, i));
        return i;
    }

    public static void ensureCardSlot(Object obj) {
        if (obj == null || !ChatCardInjector.isTarget(obj)) {
            return;
        }
        try {
            sSlotCache.remove(obj);
            int iCardSlot = ChatCardInjector.cardSlot(obj);
            Map<Object, Integer> map = sLastSlots;
            Integer num = map.get(obj);
            if (num == null || num != iCardSlot) {
                map.put(obj, iCardSlot);
                long jCurrentTimeMillis = System.currentTimeMillis();
                if (jCurrentTimeMillis - sLastSlotRelayout < 400L) {
                    return;
                }
                sLastSlotRelayout = jCurrentTimeMillis;
                new Handler(Looper.getMainLooper()).post((Runnable)new ChatCardInjector$$ExternalSyntheticLambda1(obj));
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    private static long unreadCount(Object obj) {
        Object objObjectField = ChatCardInjector.objectField(obj, "unreadInfo", "i");
        if (objObjectField == null) {
            return 0L;
        }
        Object objObjectField2 = ChatCardInjector.objectField(objObjectField, "count", "b");
        if (objObjectField2 instanceof Number) {
            return ((Number)objObjectField2).longValue();
        }
        return 0L;
    }

    private static Object objectField(Object obj, String ... strArr) {
        if (obj == null) {
            return null;
        }
        for (String str : strArr) {
            try {
                return XposedHelpers.getObjectField((Object)obj, (String)str);
            }
            catch (Throwable throwable) {
            }
        }
        return null;
    }

    public static int currentListSize(Object obj) {
        try {
            Object objCallMethod = XposedHelpers.callMethod((Object)obj, (String)"getCurrentList", (Object[])new Object[0]);
            if (objCallMethod instanceof List) {
                return ((List)objCallMethod).size();
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        return 0;
    }

    public static View buildCard(Context context, Object obj, ClassLoader classLoader, ViewGroup viewGroup) {
        View viewBuildCardFromHost = ChatCardInjector.buildCardFromHost(obj, viewGroup);
        if (viewBuildCardFromHost != null) {
            return viewBuildCardFromHost;
        }
        ClassLoader classLoaderFindLayoutLoader = ChatCardInjector.findLayoutLoader(context, obj, classLoader);
        if (classLoaderFindLayoutLoader == null) {
            Log.w((String)"ChatCard \u672a\u627e\u5230\u53ef\u89e3\u6790 SwipeMenuLayout \u7684 loader\uff0c\u56de\u9000\u81ea\u5b9a\u4e49\u5361\u7247");
            return ChatCardInjector.buildCardFallback(context);
        }
        try {
            Object objBuildHostBinding = ChatCardInjector.buildHostBinding(context, classLoaderFindLayoutLoader);
            if (objBuildHostBinding == null) {
                Log.w((String)"ChatCard \u5bbf\u4e3b\u5361\u7247\u6784\u5efa\u5931\u8d25\uff0c\u56de\u9000\u81ea\u5b9a\u4e49\u5361\u7247");
                return ChatCardInjector.buildCardFallback(context);
            }
            Method method = objBuildHostBinding.getClass().getMethod("b", new Class[0]);
            method.setAccessible(true);
            View view = (View)method.invoke(objBuildHostBinding, new Object[0]);
            ChatCardInjector.setupCard(view, objBuildHostBinding);
            return view;
        }
        catch (Throwable th) {
            Throwable thRethrow = th;
            if (thRethrow instanceof InvocationTargetException && thRethrow.getCause() != null) {
                thRethrow = thRethrow.getCause();
            }
            Log.w((String)("ChatCard \u5bbf\u4e3b\u4f1a\u8bdd\u5361\u7247\u6784\u5efa\u5931\u8d25\uff0c\u56de\u9000\u81ea\u5b9a\u4e49\u5361\u7247: " + thRethrow));
            Log.w((String)("ChatCard \u5bbf\u4e3b\u5361\u7247\u6784\u5efa\u5931\u8d25\u8be6\u60c5: " + android.util.Log.getStackTraceString((Throwable)thRethrow)));
            return ChatCardInjector.buildCardFallback(context);
        }
    }

    private static View buildCardFromHost(Object obj, ViewGroup viewGroup) {
        if (obj != null && viewGroup != null) {
            try {
                Object objCallMethod = XposedHelpers.callMethod((Object)obj, (String)"onCreateViewHolder", (Object[])new Object[]{viewGroup, 0});
                View view = (View)XposedHelpers.getObjectField((Object)objCallMethod, (String)"itemView");
                if (view == null) {
                    return null;
                }
                Log.i((String)("ChatCard \u5bbf\u4e3b\u751f\u6210\u5361\u7247 holder=" + objCallMethod.getClass().getName() + " itemView=" + view.getClass().getName()));
                ChatCardInjector.dumpViewTree(view);
                Object fieldAny = ChatCardInjector.readFieldAny(objCallMethod, objCallMethod.getClass(), "e");
                if (fieldAny == null) {
                    fieldAny = view;
                }
                ChatCardInjector.setupCard(view, fieldAny);
                ChatCardInjector.applyAvatarImage(view);
                return view;
            }
            catch (Throwable thRethrow) {
                Log.w((String)("ChatCard \u5bbf\u4e3b onCreateViewHolder \u751f\u6210\u5361\u7247\u5931\u8d25: " + thRethrow));
                Log.w((String)("ChatCard \u8be6\u60c5: " + android.util.Log.getStackTraceString((Throwable)thRethrow)));
            }
        }
        return null;
    }

    private static void setupCard(View view, Object obj) {
        view.setClickable(true);
        view.setOnClickListener((View.OnClickListener)new AnonymousClass11());
        view.setOnLongClickListener((View.OnLongClickListener)new AnonymousClass12());
        view.setTag(1296124236, obj);
        ChatCardInjector.bindCard(view);
    }

    private static Object buildHostBinding(Context context, ClassLoader classLoader) {
        try {
            Class<?> cls = Class.forName("com.tencent.qqnt.chats.core.utils.ChatViewBuildUtil", false, classLoader);
            Object staticObjectField = XposedHelpers.getStaticObjectField(cls, (String)"f14158a");
            Method method = cls.getMethod("a", LayoutInflater.class);
            method.setAccessible(true);
            return method.invoke(staticObjectField, LayoutInflater.from((Context)context));
        }
        catch (Throwable thRethrow) {
            Log.w((String)("ChatCard ChatViewBuildUtil \u8def\u5f84\u5931\u8d25: " + thRethrow));
            try {
                Constructor<?> constructor = Class.forName(HOST_LAYOUT, false, classLoader).getConstructor(Context.class, Integer.class, Integer.TYPE);
                constructor.setAccessible(true);
                return constructor.newInstance(context, null, 0);
            }
            catch (Throwable th2) {
                Log.w((String)("ChatCard \u76f4\u63a5\u6784\u9020\u8def\u5f84\u5931\u8d25: " + th2));
                return null;
            }
        }
    }

    private static ClassLoader findLayoutLoader(Context context, Object obj, ClassLoader classLoader) {
        LinkedHashSet<ClassLoader> linkedHashSet;
        block6: {
            linkedHashSet = new LinkedHashSet<ClassLoader>();
            ChatCardInjector.addLoaderChain(linkedHashSet, obj.getClass().getClassLoader());
            ChatCardInjector.addLoaderChain(linkedHashSet, context.getClassLoader());
            ChatCardInjector.addLoaderChain(linkedHashSet, classLoader);
            try {
                Object objCallMethod = XposedHelpers.callMethod((Object)obj, (String)"getCurrentList", (Object[])new Object[0]);
                if (!(objCallMethod instanceof List)) break block6;
                for (Object obj2 : (List)objCallMethod) {
                    if (obj2 == null) continue;
                    ChatCardInjector.addLoaderChain(linkedHashSet, obj2.getClass().getClassLoader());
                    break;
                }
            }
            catch (Throwable throwable) {
                // empty catch block
            }
        }
        for (ClassLoader classLoader2 : linkedHashSet) {
            try {
                Class.forName("com.tencent.qqnt.widget.SwipeMenuLayout", false, classLoader2);
                return classLoader2;
            }
            catch (Throwable throwable) {
            }
        }
        return null;
    }

    private static void addLoaderChain(LinkedHashSet<ClassLoader> linkedHashSet, ClassLoader classLoader) {
        while (classLoader != null) {
            linkedHashSet.add(classLoader);
            classLoader = classLoader.getParent();
        }
    }

    private static View buildCardFallback(Context context) {
        FrameLayout frameLayout = new FrameLayout(context);
        frameLayout.setClickable(true);
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(-14996934);
        gradientDrawable.setCornerRadius((float)ChatCardInjector.dp(context, 16));
        frameLayout.setBackground((Drawable)gradientDrawable);
        int iDp = ChatCardInjector.dp(context, 14);
        frameLayout.setPadding(iDp, iDp, iDp, iDp);
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(1);
        TextView textView = new TextView(context);
        textView.setText((CharSequence)"QQ \u90ae\u7bb1");
        textView.setTextColor(-1);
        textView.setTextSize(16.0f);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        linearLayout.addView((View)textView);
        TextView textView2 = new TextView(context);
        textView2.setTag((Object)"mail_sub");
        textView2.setText((CharSequence)"\u63a5\u6536 / \u53d1\u9001\u90ae\u4ef6");
        textView2.setTextColor(-6642255);
        textView2.setTextSize(13.0f);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
        layoutParams.topMargin = ChatCardInjector.dp(context, 4);
        linearLayout.addView((View)textView2, (ViewGroup.LayoutParams)layoutParams);
        frameLayout.addView((View)linearLayout, (ViewGroup.LayoutParams)new FrameLayout.LayoutParams(-2, -2));
        TextView textView3 = new TextView(context);
        textView3.setTag((Object)"mail_badge");
        textView3.setGravity(17);
        textView3.setTextColor(-1);
        textView3.setTextSize(11.0f);
        textView3.setVisibility(8);
        textView3.setMinWidth(ChatCardInjector.dp(context, 18));
        textView3.setPadding(ChatCardInjector.dp(context, 5), ChatCardInjector.dp(context, 1), ChatCardInjector.dp(context, 5), ChatCardInjector.dp(context, 1));
        GradientDrawable gradientDrawable2 = new GradientDrawable();
        gradientDrawable2.setColor(-1754827);
        gradientDrawable2.setCornerRadius((float)ChatCardInjector.dp(context, 18));
        textView3.setBackground((Drawable)gradientDrawable2);
        FrameLayout.LayoutParams layoutParams2 = new FrameLayout.LayoutParams(-2, -2);
        layoutParams2.gravity = 8388661;
        frameLayout.addView((View)textView3, (ViewGroup.LayoutParams)layoutParams2);
        frameLayout.setOnClickListener((View.OnClickListener)new AnonymousClass13());
        try {
            frameLayout.setLayoutParams((ViewGroup.LayoutParams)Class.forName("androidx.recyclerview.widget.RecyclerView$LayoutParams", false, context.getClassLoader()).getConstructor(Integer.TYPE, Integer.TYPE).newInstance(-1, -2));
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        return frameLayout;
    }

    public static void openTempChatFullScreen(final Context context) {
        if (context == null) {
            return;
        }
        try {
            final Dialog dialog = new Dialog(context, 16974121);
            dialog.requestWindowFeature(1);
            FrameLayout shell = new FrameLayout(context);
            shell.setBackgroundColor(-15657960);
            LinearLayout root = new LinearLayout(context);
            root.setOrientation(1);
            LinearLayout bar = new LinearLayout(context);
            bar.setOrientation(0);
            bar.setGravity(16);
            bar.setBackgroundColor(-14276822);
            int bp = ChatCardInjector.dp(context, 12);
            bar.setPadding(bp, ChatCardInjector.dp(context, 10), bp, ChatCardInjector.dp(context, 10));
            TextView back = new TextView(context);
            back.setText((CharSequence)"\u2039");
            back.setTextSize(22.0f);
            back.setTextColor(-1);
            back.setPadding(0, 0, ChatCardInjector.dp(context, 10), 0);
            back.setClickable(true);
            back.setOnClickListener(new View.OnClickListener(){

                public void onClick(View v) {
                    try {
                        dialog.dismiss();
                    }
                    catch (Throwable throwable) {
                        // empty catch block
                    }
                }
            });
            bar.addView((View)back);
            TextView title = new TextView(context);
            title.setText((CharSequence)"\u4e34\u65f6\u4f1a\u8bdd");
            title.setTextSize(16.0f);
            title.setTextColor(-1);
            bar.addView((View)title, (ViewGroup.LayoutParams)new LinearLayout.LayoutParams(0, -2, 1.0f));
            TextView refresh = new TextView(context);
            refresh.setText((CharSequence)"\u5237\u65b0");
            refresh.setTextSize(13.0f);
            refresh.setTextColor(-11751600);
            refresh.setPadding(ChatCardInjector.dp(context, 8), ChatCardInjector.dp(context, 4), 0, ChatCardInjector.dp(context, 4));
            refresh.setClickable(true);
            refresh.setOnClickListener(new View.OnClickListener(){

                public void onClick(View v) {
                    try {
                        QqKernel.refreshTempChatList();
                        dialog.dismiss();
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable(){

                            @Override
                            public void run() {
                                ChatCardInjector.openTempChatFullScreen(context);
                            }
                        }, 300L);
                    }
                    catch (Throwable throwable) {
                        // empty catch block
                    }
                }
            });
            bar.addView((View)refresh);
            root.addView((View)bar, (ViewGroup.LayoutParams)new LinearLayout.LayoutParams(-1, -2));
            ScrollView scroll = new ScrollView(context);
            LinearLayout list = new LinearLayout(context);
            list.setOrientation(1);
            scroll.addView((View)list);
            List items = QqKernel.tempChatSnapshot();
            if (items.isEmpty()) {
                TextView empty = new TextView(context);
                empty.setText((CharSequence)"\u6682\u65e0\u4e34\u65f6\u4f1a\u8bdd");
                empty.setTextSize(14.0f);
                empty.setTextColor(-6643546);
                empty.setGravity(17);
                empty.setPadding(0, ChatCardInjector.dp(context, 60), 0, 0);
                list.addView((View)empty, (ViewGroup.LayoutParams)new LinearLayout.LayoutParams(-1, -2));
            } else {
                Runnable[] step = new Runnable[1];
                final List fItems = items;
                final LinearLayout fList = list;
                final Dialog fDialog = dialog;
                final Context fCtx = context;
                final Handler h = new Handler(Looper.getMainLooper());
                final int[] cursor = new int[]{0};
                final int[] builtCount = new int[]{0};
                step[0] = new Runnable() {

                    @Override
                    public void run() {
                        try {
                            java.util.ArrayList<String> uids = new java.util.ArrayList<String>();
                            for (int i = 0; i < fItems.size(); ++i) {
                                uids.add((String) ((Object[]) fItems.get(i))[0]);
                            }
                            List chatItems = ChatCardInjector.submitTempChatList(fCtx, uids);
                            if (chatItems == null || chatItems.isEmpty()) {
                                for (int i = 0; i < fItems.size(); ++i) {
                                    fList.addView(ChatCardInjector.fallbackRow(fCtx, fDialog, (Object[]) fItems.get(i)),
                                            new LinearLayout.LayoutParams(-1, -2));
                                }
                                Log.i("全屏临时会话页: 宿主渲染失败，已全部退化 共 " + fItems.size() + " 条");
                                return;
                            }
                            for (int i = 0; i < chatItems.size() && i < fItems.size(); ++i) {
                                final String uid = (String) ((Object[]) fItems.get(i))[0];
                                View card = ChatCardInjector.buildHostChatRowAt(fCtx, (ViewGroup) fList, chatItems.get(i), i);
                                if (card == null) {
                                    /* 单卡失败补 fallback，保证每 uid 恰好一张卡（防粘连/缺卡） */
                                    fList.addView(ChatCardInjector.fallbackRow(fCtx, fDialog, (Object[]) fItems.get(i)),
                                            new LinearLayout.LayoutParams(-1, -2));
                                } else {
                                    builtCount[0] = builtCount[0] + 1;
                                    /* 防溢出/防粘连：卡片外包裁切容器，上下各留 2dp */
                                    android.widget.FrameLayout clipWrap = new android.widget.FrameLayout(fCtx);
                                    clipWrap.setClipChildren(true);
                                    clipWrap.setClipToPadding(true);
                                    clipWrap.addView(card, new android.widget.FrameLayout.LayoutParams(-1, -2));
                                    float dens = fCtx.getResources().getDisplayMetrics().density;
                                    LinearLayout.LayoutParams cwLp = new LinearLayout.LayoutParams(-1, -2);
                                    cwLp.topMargin = (int) (dens * 2);
                                    cwLp.bottomMargin = (int) (dens * 2);
                                    fList.addView(clipWrap, cwLp);
                                    card.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            try {
                                                fDialog.dismiss();
                                            } catch (Throwable ignored) {}
                                            QqKernel.setTempChatUnread((String) uid, 0);
                                            QqKernel.openTempChatSession((Context) fCtx, (String) uid);
                                        }
                                    });
                                    card.setOnLongClickListener(new View.OnLongClickListener() {
                                        @Override
                                        public boolean onLongClick(View v) {
                                            QqKernel.showTempChatMenu((Context) fCtx, (String) uid);
                                            return true;
                                        }
                                    });
                                }
                            }
                            Log.i("全屏临时会话页: 共 " + fItems.size() + " 条，宿主卡片 " + builtCount[0] + " 条");
                        } catch (Throwable th) {
                            Log.w("填充临时会话列表失败: " + th);
                        }
                    }
                };
                fPendingFill = step[0];
                fPendingHandler = h;
            }
            root.addView((View)scroll, (ViewGroup.LayoutParams)new LinearLayout.LayoutParams(-1, 0, 1.0f));
            shell.addView((View)root);
            dialog.setContentView((View)shell);
            if (dialog.getWindow() != null) {
                dialog.getWindow().setLayout(-1, -1);
            }
            dialog.show();
            if (fPendingFill != null && fPendingHandler != null) {
                fPendingHandler.post(fPendingFill);
                fPendingFill = null;
                fPendingHandler = null;
            }
        }
        catch (Throwable th) {
            Log.w((String)("\u6253\u5f00\u5168\u5c4f\u4e34\u65f6\u4f1a\u8bdd\u9875\u5931\u8d25: " + th));
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    /**
     * 用宿主 ChatsListAdapter 的独立实例渲染真实会话卡片。
     * 不复用主列表的 adapter（避免污染数据），而是 new 一个干净的实例，
     * submitList 我们自己的条目，onCreateViewHolder/onBindViewHolder 全走宿主逻辑。
     */
    /**
     * 手表版 binding 直填（WatchRecentContactHolder -> ItemRecentContactBinding）：
     *   b=WatchAvatarView 头像, c=摘要, d=时间, e=标题, g=QUIBadge 未读
     */
    private static void fillWatchBinding(Object adapter, Object holder, Object chatItem, String peerUid) {
        try {
            Object entry = chatItem;
            Object info = XposedHelpers.getObjectField(entry, "a");
            String nick = (String) XposedHelpers.getObjectField(info, "peerName");
            if (nick == null || nick.trim().isEmpty()) {
                nick = peerUid;
            }
            /* 去掉登记时附加的"（临时会话）"后缀，与宿主卡片展示一致 */
            nick = nick.replace("（临时会话）", "").replace("(临时会话)", "").trim();
            Object[] known = com.tencent.qqlitepro.http.QqKernel.tempChatKnownEntry(peerUid);
            String preview = known != null ? com.tencent.qqlitepro.http.QqKernel.previewOf(known) : "";
            if (preview == null) preview = "";
            long msgTime = known != null ? com.tencent.qqlitepro.http.QqKernel.longOf(known[2]) : 0L;
            int unreadCnt = com.tencent.qqlitepro.http.QqKernel.tempChatUnread(peerUid);
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault());
            String timeStr = msgTime > 0 ? sdf.format(new java.util.Date(msgTime)) : "";

            Object binding = null;
            for (java.lang.reflect.Field fld : holder.getClass().getDeclaredFields()) {
                if (fld.getType().getName().endsWith("ItemRecentContactBinding")) {
                    fld.setAccessible(true);
                    binding = fld.get(holder);
                    break;
                }
            }
            if (binding == null) {
                Log.w("直填失败: holder 内未找到 ItemRecentContactBinding, holder=" + holder.getClass().getName());
                return;
            }
            Class<?> bcls = binding.getClass();
            /* binding 构造参数顺序（逆向 WatchRecentItemBuilder.a）：
             *   (root, 2114519368头像, 2114519698标题, 2114521588时间, 2114521611摘要, 2114521638置顶, 2114521741未读)
             * 对应字段 a..g：a=root, b=头像, c=标题(SingleLineTextView),
             * d=时间, e=摘要(SingleLineTextView), f=置顶Icon, g=未读QUIBadge。
             * 宿主 e(item) 里 r4.b.c.setText(item.h.a=标题) 也证明 c 是标题。 */
            /* 视觉顺序（真机确认）：e(SingleLineTextView) 在上=标题位，c(SingleLineTextView) 在下=摘要位。
             * 宿主 e() 把 SummaryInfo 填 e？不——宿主 e() 填的是 c。真机显示预览在上昵称在下，
             * 因此这里按视觉修正：e ← 昵称（标题位），c ← 消息预览（摘要位）。 */
            try {
                Object tv = bcls.getField("e").get(binding);
                XposedHelpers.callMethod(tv, "setText", new Object[]{nick});
            } catch (Throwable th2) {
                Log.w("直填标题失败: " + th2);
            }
            try {
                Object tv = bcls.getField("d").get(binding);
                XposedHelpers.callMethod(tv, "setText", new Object[]{timeStr});
            } catch (Throwable ignored) {}
            try {
                Object tv = bcls.getField("c").get(binding);
                XposedHelpers.callMethod(tv, "setText", new Object[]{preview});
            } catch (Throwable ignored) {}
            try {
                Object badge = bcls.getField("g").get(binding);
                if (badge != null) {
                    if (unreadCnt > 0) {
                        XposedHelpers.callMethod(badge, "setText",
                                new Object[]{unreadCnt > 99 ? "99+" : String.valueOf(unreadCnt)});
                        XposedHelpers.callMethod(badge, "setVisibility", new Object[]{Integer.valueOf(View.VISIBLE)});
                    } else {
                        XposedHelpers.callMethod(badge, "setVisibility", new Object[]{Integer.valueOf(View.GONE)});
                    }
                }
            } catch (Throwable th3) {
                Log.w("直填未读失败: " + th3);
            }
            /* 置顶箭头标识：按该会话自己的置顶标志独立显示/隐藏 */
            try {
                Object topIcon = bcls.getField("f").get(binding);
                if (topIcon instanceof android.view.View) {
                    boolean top = com.tencent.qqlitepro.http.QqKernel.isTempChatTop(peerUid);
                    ((android.view.View) topIcon).setVisibility(top
                            ? android.view.View.VISIBLE : android.view.View.GONE);
                }
            } catch (Throwable ignored) {}

            /*
             * 卡片底色：不要自己设色！
             * 宿主 ItemRecentContactBinding.a 根容器本身带主题背景（与正常会话卡一致）。
             * 旧实现强制 setBackgroundColor(-14013129) 会让临时卡比正常卡更深。
             * 这里只清除可能的"高亮/选中"态残留，还原宿主默认。
             */
            try {
                Object rootC = bcls.getField("a").get(binding);
                if (rootC instanceof android.view.View) {
                    View rv0 = (View) rootC;
                    if (rv0.isSelected() || rv0.isPressed() || rv0.isActivated()) {
                        rv0.setSelected(false);
                        rv0.setPressed(false);
                        rv0.setActivated(false);
                    }
                    /* 不设自定义背景；仅当被我们早期代码污染过才重置 */
                    android.graphics.drawable.Drawable bgd = rv0.getBackground();
                    if (bgd instanceof android.graphics.drawable.ColorDrawable) {
                        rv0.setBackground(null);
                    }
                }
            } catch (Throwable ignored) {}

            /* 卡片分隔：外层包装 LinearLayout 已有 4/2dp margin（buildHostChatRowAt 添加处），
             * 不再对宿主 itemView 调 cardMargin——它自带 RecyclerView MarginParams，
             * 重复设置会导致独立容器里 measure 异常（首卡内容溢出）。 */

            /* 头像：qlogo URL 按 uin 拉取 Bitmap 写入 WatchAvatarView。
             * （AvatarFacade 链会在 dex 类解析时抛 NoClassDefFoundError：SingleLineTextView，
             *   该类只存在于特定 dex，跨 classloader 解析不稳定，所以用最可靠的 Bitmap 直写。） */
            try {
                Object avObj = bcls.getField("b").get(binding);
                long auin = known != null ? com.tencent.qqlitepro.http.QqKernel.longOf(known[0]) : 0L;
                if (avObj instanceof android.view.View && auin > 0) {
                    final android.view.View avV = (android.view.View) avObj;
                    final long fUin = auin;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                java.net.URL url = new java.net.URL(
                                        "https://q1.qlogo.cn/g?b=qq&nk=" + fUin + "&s=100");
                                java.net.HttpURLConnection conn =
                                        (java.net.HttpURLConnection) url.openConnection();
                                conn.setConnectTimeout(8000);
                                conn.setReadTimeout(8000);
                                conn.setDoInput(true);
                                android.graphics.Bitmap bm =
                                        android.graphics.BitmapFactory.decodeStream(conn.getInputStream());
                                if (bm != null) {
                                    final android.graphics.Bitmap fbm = bm;
                                    new android.os.Handler(android.os.Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                ChatCardInjector.setBitmapIntoAvatar(avV, fbm);
                                            } catch (Throwable ignored) {}
                                        }
                                    });
                                }
                            } catch (Throwable ignored) {}
                        }
                    }, "temp-avatar-" + fUin).start();
                }
            } catch (Throwable thAv) {
                Log.w("直填头像失败: " + thAv);
            }
        } catch (Throwable th) {
            Log.w("直填卡片数据失败: " + th);
        }
    }

    /**
     * 按索引渲染一行宿主卡片（数据已由 submitTempChatList 批量提交，单次 diff）。
     */
    private static View buildHostChatRowAt(Context context, ViewGroup parent, Object chatItem, int index) {
        try {
            Object adapter = tempChatAdapter(context);
            if (adapter == null) {
                return null;
            }
            Object holder = null;
            try {
                holder = XposedHelpers.callMethod(adapter, "onCreateViewHolder",
                        new Object[]{parent, Integer.valueOf(0)});
            } catch (Throwable th) {
                Log.w("构建宿主卡片: onCreateViewHolder 失败 " + th);
            }
            View itemView = null;
            if (holder != null) {
                try {
                    itemView = (View) XposedHelpers.getObjectField(holder, "itemView");
                } catch (Throwable ignored) {
                }
            }
            if (itemView == null) {
                return null;
            }
            try {
                XposedHelpers.callMethod(adapter, "onBindViewHolder",
                        new Object[]{holder, Integer.valueOf(index)});
            } catch (Throwable th) {
                Log.w("构建宿主卡片: onBindViewHolder 失败 " + th);
            }
            /* 置顶标识：写 item.k 后调宿主 builder.o(item, holder)（宿主原生显隐逻辑） */
            try {
                String uidT = peerUidOf(chatItem);
                boolean top = com.tencent.qqlitepro.http.QqKernel.isTempChatTop(uidT);
                XposedHelpers.setBooleanField(chatItem, "k", top);
                Object builderT = watchRecentItemBuilder();
                if (builderT != null) {
                    for (java.lang.reflect.Method m : builderT.getClass().getDeclaredMethods()) {
                        if (m.getName().equals("o") && m.getParameterTypes().length == 2
                                && m.getParameterTypes()[0].isInstance(chatItem)
                                && m.getParameterTypes()[1].isInstance(holder)) {
                            m.setAccessible(true);
                            m.invoke(builderT, chatItem, holder);
                            break;
                        }
                    }
                }
            } catch (Throwable ignored) {
            }
            /* 点击/长按：调宿主 holder.e(item)——宿主为每张卡挂独立的
             * 点击(进聊天)/长按(原生菜单, items 带 item 对象, 天然逐卡独立) */
            try {
                for (java.lang.reflect.Method m : holder.getClass().getDeclaredMethods()) {
                    if (m.getName().equals("e") && m.getParameterTypes().length == 1
                            && m.getParameterTypes()[0].isInstance(chatItem)) {
                        m.setAccessible(true);
                        m.invoke(holder, chatItem);
                        break;
                    }
                }
            } catch (Throwable thE) {
                Log.w("构建宿主卡片: holder.e 失败 " + thE);
            }
            /* 直填数据放最后：holder.e 会用 item.h.a 重写昵称（带后缀），
             * 我们在它之后剥后缀/填摘要/时间/未读/头像，保证最终显示正确 */
            final Object fAdapter2 = adapter;
            final Object fHolder2 = holder;
            final Object fItem2 = chatItem;
            final String fUid2 = peerUidOf(chatItem);
            /* 直填延后一拍：确保覆盖宿主 onBindViewHolder 的异步渲染结果 */
            new android.os.Handler(android.os.Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    try {
                        fillWatchBinding(fAdapter2, fHolder2, fItem2, fUid2);
                    } catch (Throwable ignored) {}
                }
            });
            /* 独立容器（LinearLayout）里的 RecyclerView.LayoutParams 会让
             * measure 走 RecyclerView 的子 View 语义，导致 ConstraintLayout
             * 高度塌陷/内容溢出。用通用 MarginLayoutParams（保留原 margin）修复。 */
            try {
                android.view.ViewGroup.LayoutParams lp0 = itemView.getLayoutParams();
                android.view.ViewGroup.MarginLayoutParams nlp;
                if (lp0 instanceof android.view.ViewGroup.MarginLayoutParams) {
                    nlp = (android.view.ViewGroup.MarginLayoutParams) lp0;
                } else {
                    nlp = new android.widget.LinearLayout.LayoutParams(-1, -2);
                }
                nlp.width = -1;
                nlp.height = -2;
                itemView.setLayoutParams(nlp);
            } catch (Throwable ignored) {
            }
            return itemView;
        } catch (Throwable th) {
            Log.w("构建宿主卡片异常: " + th);
            return null;
        }
    }

    /**
     * 取宿主 WatchRecentItemBuilder 实例：
     * RecentBuilderFactoryProvider.a（单例）的 a/b/d/e（factory map）里，
     * factory.a 是 BaseRecentItemBuilder[]，遍历找 WatchRecentItemBuilder 类型。
     */
    public static Object watchRecentItemBuilder() {
        try {
            Context ctx = sAppCtx;
            ClassLoader cl = ctx != null ? ctx.getClassLoader() : null;
            if (cl == null) {
                return null;
            }
            Class<?> providerCls = Class.forName(
                    "com.tencent.qqnt.chats.core.adapter.factory.RecentBuilderFactoryProvider",
                    false, cl);
            Object provider = providerCls.getField("a").get(null);
            if (provider == null) {
                return null;
            }
            Class<?> watchBuilderCls = Class.forName(
                    "com.tencent.qqnt.watch.chat.list.WatchRecentItemBuilder", false, cl);
            // 遍历 provider 的 HashMap 字段（c/d/e）→ factory → factory.a 数组
            for (java.lang.reflect.Field f : providerCls.getDeclaredFields()) {
                if (!java.lang.reflect.Modifier.isStatic(f.getModifiers())) continue;
                Object v;
                try {
                    f.setAccessible(true);
                    v = f.get(null);
                } catch (Throwable ignored) { continue; }
                if (!(v instanceof java.util.Map)) continue;
                for (Object fv : ((java.util.Map) v).values()) {
                    if (fv == null) continue;
                    try {
                        Object arr = XposedHelpers.getObjectField(fv, "a");
                        if (arr instanceof Object[]) {
                            for (Object b : (Object[]) arr) {
                                if (b != null && watchBuilderCls.isInstance(b)) {
                                    /* 只有静态 d（WatchFragment）就绪的实例才可用于导航 */
                                    try {
                                        Object wf = XposedHelpers.getStaticObjectField(watchBuilderCls, "d");
                                        if (wf != null) {
                                            return b;
                                        }
                                    } catch (Throwable ignored) {}
                                }
                            }
                        }
                    } catch (Throwable ignored) {}
                }
            }
            /* 没找到就绪实例时也返回类型实例（调用方自行判断 d） */
            return null;
        } catch (Throwable th) {
            Log.w("watchRecentItemBuilder 失败: " + th);
            return null;
        }
    }

    private static String peerUidOf(Object chatItem) {
        try {
            Object info = XposedHelpers.getObjectField(chatItem, "a");
            return (String) XposedHelpers.getObjectField(info, "peerUid");
        } catch (Throwable t) {
            return "";
        }
    }

    /** 一次性提交全部临时会话（单次 diff），返回 chatItem 列表 */
    private static List submitTempChatList(Context context, List<String> peerUids) {
        Object adapter = tempChatAdapter(context);
        if (adapter == null) {
            return null;
        }
        List chatItems = new ArrayList();
        for (String uid : peerUids) {
            try {
                Object info = com.tencent.qqlitepro.http.QqKernel.buildTempChatEntryForUi(uid);
                if (info == null) continue;
                Object item = ChatCardInjector.wrapAsChatItem(info);
                if (item != null) chatItems.add(item);
            } catch (Throwable ignored) {}
        }
        try {
            XposedHelpers.callMethod(adapter, "submitList", new Object[]{chatItems});
        } catch (Throwable th) {
            try {
                XposedHelpers.callMethod(adapter, "submitList", new Object[]{chatItems, null});
            } catch (Throwable ignored) {}
        }
        /* diff 提交等待：高频短轮询（10ms×最多 100 次=1s），尽快进入逐卡渲染 */
        for (int wait = 0; wait < 100; ++wait) {
            try {
                Object cur = XposedHelpers.callMethod(adapter, "getCurrentList", new Object[0]);
                if (cur instanceof List && ((List)cur).size() == chatItems.size()) {
                    break;
                }
            } catch (Throwable ignored) {}
            try { Thread.sleep(10L); } catch (InterruptedException ie) { break; }
        }
        return chatItems;
    }

    /**
     * 取宿主主列表当前使用的 builder（WatchRecentItemBuilder）。
     * 逆向：RecentBuilderFactoryProvider.a（Companion 单例）.a(styleId) 返回 builder；
     * builder.d 字段是 WatchFragment（进聊天导航要用它的 NavController）。
     */
    public static Object currentRecentBuilder() {
        try {
            Context ctx = sAppCtx;
            ClassLoader cl = ctx != null ? ctx.getClassLoader() : null;
            if (cl == null) {
                return null;
            }
            /* 优先从主列表 adapter 的 builder 字段（c）拿 —— 它就是主列表正在用的 WatchRecentItemBuilder */
            Object adapter0 = sListAdapter;
            if (adapter0 != null) {
                try {
                    Object b0 = XposedHelpers.getObjectField(adapter0, "c");
                    if (b0 != null) {
                        Log.i("currentRecentBuilder: 从主列表 adapter 拿到 builder " + b0.getClass().getName());
                        return b0;
                    }
                } catch (Throwable ignored) {}
            }
            Class<?> providerCls = Class.forName(
                    "com.tencent.qqnt.chats.core.adapter.factory.RecentBuilderFactoryProvider",
                    false, cl);
            Object provider = null;
            try {
                provider = providerCls.getField("a").get(null);
            } catch (Throwable ignored) {}
            if (provider == null) {
                for (java.lang.reflect.Field f : providerCls.getDeclaredFields()) {
                    if (java.lang.reflect.Modifier.isStatic(f.getModifiers())) {
                        f.setAccessible(true);
                        Object v = f.get(null);
                        if (v != null) { provider = v; break; }
                    }
                }
            }
            if (provider == null) {
                return null;
            }
            // provider.a(styleId) — 遍历单参 int 方法
            for (java.lang.reflect.Method m : provider.getClass().getDeclaredMethods()) {
                if (m.getParameterTypes().length == 1
                        && m.getParameterTypes()[0] == Integer.TYPE
                        && !m.isSynthetic()) {
                    m.setAccessible(true);
                    Object b = m.invoke(provider, Integer.valueOf(0));
                    if (b != null) {
                        return b;
                    }
                }
            }
            // 兜底：sListAdapter（主列表）里的 c 字段是 ChatsListBuilder
            Object adapter = sListAdapter;
            if (adapter != null) {
                try {
                    return XposedHelpers.getObjectField(adapter, "c");
                } catch (Throwable ignored) {}
            }
            return null;
        } catch (Throwable th) {
            Log.w("获取 RecentBuilder 失败: " + th);
            return null;
        }
    }

    public static Object mainRecyclerView() {
        return sMainRecyclerView;
    }

    /** 递归清空子 View 的点击/长按监听（depth 限 6 层，防深度树） */
    private static void clearChildClickListeners(View v, int depth) {
        if (v == null || depth > 6) {
            return;
        }
        v.setOnClickListener(null);
        v.setOnLongClickListener(null);
        v.setClickable(false);
        v.setLongClickable(false);
        if (v instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) v;
            for (int i = 0; i < vg.getChildCount(); ++i) {
                clearChildClickListeners(vg.getChildAt(i), depth + 1);
            }
        }
    }

    /** 独立的宿主 ChatsListAdapter（缓存复用） */
    private static volatile Object sTempChatAdapter;
    private static volatile ClassLoader sTempChatAdapterCl;

    private static Object tempChatAdapter(Context context) {
        ClassLoader cl = context != null ? context.getClassLoader() : null;
        if (sTempChatAdapter != null && sTempChatAdapterCl == cl) {
            return sTempChatAdapter;
        }
        try {
            /* 主列表真实使用的 builder 是根包下的 MainChatsListBuilder（逆向确认） */
            Class<?> builderCls = Class.forName("MainChatsListBuilder", false, cl);
            Object builder = null;
            try {
                builder = builderCls.getDeclaredConstructor().newInstance();
            } catch (Throwable ignored) {
            }
            if (builder == null) {
                for (Constructor<?> c : builderCls.getDeclaredConstructors()) {
                    Class<?>[] ps = c.getParameterTypes();
                    if (ps.length == 1 && context != null && ps[0].isInstance(context)) {
                        c.setAccessible(true);
                        builder = c.newInstance(context);
                        break;
                    }
                }
            }
            if (builder == null) {
                Log.w("独立 adapter: ChatsListBuilder 构造失败");
                return null;
            }
            Class<?> adapterCls = Class.forName(
                    "com.tencent.qqnt.chats.core.adapter.ChatsListAdapter", false, cl);
            /* 构造器参数是抽象基类 ChatsListBuilder，必须用 isAssignableFrom 匹配 */
            Object adapter = null;
            for (Constructor<?> c : adapterCls.getDeclaredConstructors()) {
                Class<?>[] ps = c.getParameterTypes();
                if (ps.length == 1 && ps[0].isAssignableFrom(builderCls)) {
                    c.setAccessible(true);
                    adapter = c.newInstance(builder);
                    break;
                }
            }
            if (adapter == null) {
                throw new NoSuchMethodException("ChatsListAdapter(builder) 未匹配");
            }
            sTempChatAdapter = adapter;
            sTempChatAdapterCl = cl;
            Log.i("全屏临时会话: 独立 ChatsListAdapter 已创建");
            return adapter;
        } catch (Throwable th) {
            Log.w("独立 adapter 创建失败: " + th);
            return null;
        }
    }


    private static void bindHolder(Object adapter, Object holder) {
        try {
            for (Method m : adapter.getClass().getMethods()) {
                String n = m.getName();
                if (!n.equals("onBindViewHolder") && !n.equals("k") || m.getParameterTypes().length != 2 || m.getParameterTypes()[1].isAssignableFrom(List.class)) continue;
                m.setAccessible(true);
                m.invoke(adapter, holder, 0);
                return;
            }
        }
        catch (Throwable th) {
            Log.w((String)("bindHolder \u5931\u8d25: " + th));
        }
    }

    private static Object getAdapterItemList(Object adapter) {
        try {
            Method m = adapter.getClass().getMethod("getCurrentList", new Class[0]);
            m.setAccessible(true);
            return m.invoke(adapter, new Object[0]);
        }
        catch (Throwable throwable) {
            return null;
        }
    }

    private static boolean setAdapterItemList(Object adapter, List<?> list) {
        if (list == null) {
            return false;
        }
        try {
            XposedHelpers.callMethod((Object)adapter, (String)"submitList", (Object[])new Object[]{list});
            return true;
        }
        catch (Throwable throwable) {
            return false;
        }
    }

    private static Object findBindingInHolder(Object holder, Class<?> want) {
        if (holder == null) {
            return null;
        }
        try {
            Object v;
            Class<?> c;
            for (c = holder.getClass(); c != null && c != Object.class; c = c.getSuperclass()) {
                for (Field f : c.getDeclaredFields()) {
                    if (Modifier.isStatic(f.getModifiers()) || !want.isAssignableFrom(f.getType())) continue;
                    f.setAccessible(true);
                    v = f.get(holder);
                    if (v == null) continue;
                    return v;
                }
            }
            for (c = holder.getClass(); c != null && c != Object.class; c = c.getSuperclass()) {
                for (Field f : c.getDeclaredFields()) {
                    if (Modifier.isStatic(f.getModifiers()) || !f.getType().getName().endsWith("Binding")) continue;
                    f.setAccessible(true);
                    v = f.get(holder);
                    if (v == null) continue;
                    return v;
                }
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        return null;
    }

    /**
     * 把内核的 RecentContactInfo 转成宿主渲染需要的 RecentContactChatItem。
     * 宿主构造器需要 TitleInfo/SummaryInfo/UnreadInfo/AvatarInfo 等子对象，
     * 这里按内核字段逐个填充。
     */
    /**
     * 把内核 RecentContactInfo 转成宿主渲染对象 RecentContactChatItem。
     *
     * 逆向自宿主（QQ Max M2.5）：
     *   TitleInfo(String) / TitleInfo(String,int)
     *   SummaryInfo(CharSequence[,int])
     *   UnreadInfo(int,long) / UnreadInfo()
     *   AvatarInfo(int showType, String path, Integer resId, Integer rightBottomIconResId)
     *   VasInfo()                          ← 无参构造，内部自动 new VASPersonalNamePlate()
     *   RecentContactChatItem(RecentContactInfo, J, I, String, TitleInfo, AvatarInfo, String,
     *                         SummaryInfo, UnreadInfo, VasInfo, Z, Z, I, ArrayList, ArrayList, Z, J, B, J, Z, ArrayList, I)
     */
    public static Object wrapAsChatItem(Object info) {
        if (info == null) {
            return null;
        }
        try {
            ClassLoader cl = info.getClass().getClassLoader();
            String pkg = "com.tencent.qqnt.chats.core.adapter.itemdata.";
            Class<?> itemCls = Class.forName(RECENT_ITEM, false, cl);
            Class<?> titleCls = Class.forName(pkg + "RecentContactChatItem$TitleInfo", false, cl);
            Class<?> summaryCls = Class.forName(pkg + "RecentContactChatItem$SummaryInfo", false, cl);
            Class<?> unreadCls = Class.forName(pkg + "RecentContactChatItem$UnreadInfo", false, cl);
            Class<?> avatarCls = Class.forName(pkg + "RecentContactChatItem$AvatarInfo", false, cl);
            Class<?> vasCls;
            try {
                vasCls = Class.forName(pkg + "RecentContactChatItem$VasInfo", false, cl);
            } catch (Throwable ignored) {
                vasCls = null;
            }

            String name = QqKernel.strOf(QqKernel.fieldValue(info, "peerName"));
            if (name == null || name.trim().isEmpty()) {
                name = QqKernel.strOf(QqKernel.fieldValue(info, "peerUid"));
            }
            /* 临时会话（chatType=1 且来源为临时登记）显示「（临时会话）」标识 */
            try {
                String uidT = QqKernel.strOf(QqKernel.fieldValue(info, "peerUid"));
                if (uidT != null && com.tencent.qqlitepro.http.QqKernel.tempChatKnownEntry(uidT) != null) {
                    name = name.replace("（临时会话）", "").replace("(临时会话)", "").trim() + "（临时会话）";
                }
            } catch (Throwable ignored) {}
            /*
             * 逆向宿主渲染语义（WatchRecentItemBuilder）：
             *   e(item): binding.c(标题)  = item.h.a  -> SummaryInfo.a 应装【昵称】
             *   s(item): binding.e(摘要)  = item.f.a  -> TitleInfo.a   应装【消息预览】
             *   r(item): binding.d(时间)  = item.g    -> item.g 直接是时间串
             * 这里按宿主语义构造（此前把昵称装进 TitleInfo 导致标题/摘要颠倒）。
             */
            String previewText0 = QqKernel.previewTextOfAbstract(QqKernel.fieldValue(info, "abstractContent"));
            if (previewText0 == null) {
                previewText0 = "";
            }
            Object title;
            try {
                title = titleCls.getConstructor(String.class).newInstance(previewText0);
            } catch (Throwable ignored) {
                title = titleCls.newInstance();
                try {
                    java.lang.reflect.Method st = titleCls.getMethod("a", String.class);
                    st.invoke(title, previewText0);
                } catch (Throwable ignored2) {
                }
            }

            Object summary;
            try {
                summary = summaryCls.getConstructor(CharSequence.class).newInstance(name);
            } catch (Throwable ignored) {
                summary = summaryCls.newInstance();
            }

            int unreadCnt = QqKernel.intOf(QqKernel.fieldValue(info, "unreadCnt"));
            Object unread;
            try {
                unread = unreadCls.getConstructor(Integer.TYPE, Long.TYPE)
                        .newInstance(Integer.valueOf(unreadCnt), Long.valueOf(0L));
            } catch (Throwable ignored) {
                unread = unreadCls.newInstance();
            }

            /* 头像：showType=0, path 用 peerUid 让宿主自行解析头像缓存 */
            Object avatar;
            try {
                avatar = avatarCls.getConstructor(Integer.TYPE, String.class,
                        Integer.class, Integer.class)
                        .newInstance(Integer.valueOf(0),
                                QqKernel.strOf(QqKernel.fieldValue(info, "peerUid")),
                                null, null);
            } catch (Throwable ignored) {
                avatar = null;
            }

            /* VasInfo：必须用无参构造（内部自动创建 VASPersonalNamePlate），不能传 null */
            Object vas = null;
            if (vasCls != null) {
                try {
                    vas = vasCls.newInstance();
                } catch (Throwable ignored) {
                }
            }

            /* 主构造器：按参数类型精确填充 */
            Constructor<?> best = null;
            for (Constructor<?> c : itemCls.getDeclaredConstructors()) {
                Class<?>[] ps = c.getParameterTypes();
                if (ps.length >= 4 && ps.length <= 32
                        && ps[0] == info.getClass()
                        && containsType(ps, titleCls)
                        && containsType(ps, summaryCls)
                        && containsType(ps, unreadCls)) {
                    if (best == null || c.getParameterTypes().length < best.getParameterTypes().length) {
                        best = c;
                    }
                }
            }
            if (best == null) {
                Log.w("包装会话条目: 未找到主构造器");
                return null;
            }
            best.setAccessible(true);
            Class<?>[] ps = best.getParameterTypes();
            Object[] argv = new Object[ps.length];
            boolean infoSet = false;
            for (int i = 0; i < ps.length; ++i) {
                String pn = ps[i].getName();
                if (ps[i] == info.getClass() && !infoSet) {
                    argv[i] = info;
                    infoSet = true;
                } else if (ps[i] == titleCls) {
                    argv[i] = title;
                } else if (ps[i] == summaryCls) {
                    argv[i] = summary;
                } else if (ps[i] == unreadCls) {
                    argv[i] = unread;
                } else if (ps[i] == avatarCls) {
                    argv[i] = avatar;
                } else if (vasCls != null && ps[i] == vasCls) {
                    argv[i] = vas;
                } else if (ps[i] == Integer.TYPE) {
                    argv[i] = Integer.valueOf(0);
                } else if (ps[i] == Long.TYPE) {
                    argv[i] = Long.valueOf(0L);
                } else if (ps[i] == Boolean.TYPE) {
                    argv[i] = Boolean.FALSE;
                } else if (ps[i] == Byte.TYPE) {
                    argv[i] = Byte.valueOf((byte) 0);
                } else if (ps[i] == Short.TYPE) {
                    argv[i] = Short.valueOf((short) 0);
                } else if (ps[i] == Character.TYPE) {
                    argv[i] = Character.valueOf('\u0000');
                } else if (ps[i] == Float.TYPE) {
                    argv[i] = Float.valueOf(0f);
                } else if (ps[i] == Double.TYPE) {
                    argv[i] = Double.valueOf(0d);
                } else if (ps[i] == String.class) {
                    argv[i] = "";
                } else if (ps[i] == List.class || ps[i] == ArrayList.class) {
                    argv[i] = new ArrayList();
                } else {
                    argv[i] = null;
                }
            }
            return best.newInstance(argv);
        } catch (Throwable th) {
            Throwable cause = (th instanceof InvocationTargetException && th.getCause() != null)
                    ? th.getCause() : th;
            Log.w("包装会话条目失败: " + cause);
        }
        return null;
    }

    private static boolean containsType(Class<?>[] ps, Class<?> want) {
        for (Class<?> p : ps) {
            if (p == want) {
                return true;
            }
        }
        return false;
    }

    /** 把内核条目的关键字段反射设置到宿主渲染对象上 */
    private static void fillChatItemFields(Object item, Object info) {
        try {
            Object title = null;
            try {
                Class<?> titleCls = Class.forName(
                        "com.tencent.qqnt.chats.core.adapter.itemdata.RecentContactChatItem$TitleInfo",
                        false, item.getClass().getClassLoader());
                /* 宿主语义：TitleInfo.a=消息预览（s()填摘要），SummaryInfo.a=昵称（e()填标题） */
                String prev0 = QqKernel.previewTextOfAbstract(QqKernel.fieldValue(info, "abstractContent"));
                if (prev0 == null) prev0 = "";
                title = buildSimple(titleCls,
                        new Class<?>[]{String.class, Integer.class, Integer.class, String.class},
                        new Object[]{prev0, Integer.valueOf(0), Integer.valueOf(0), ""});
                if (title == null) {
                    title = buildFirst(titleCls, prev0);
                }
            } catch (Throwable ignored) {
            }
            Object summary = null;
            try {
                Class<?> sumCls = Class.forName(
                        "com.tencent.qqnt.chats.core.adapter.itemdata.RecentContactChatItem$SummaryInfo",
                        false, item.getClass().getClassLoader());
                String nick0 = QqKernel.strOf(QqKernel.fieldValue(info, "peerName"));
                if (nick0 != null) {
                    nick0 = nick0.replace("（临时会话）", "").replace("(临时会话)", "").trim();
                }
                try {
                    summary = sumCls.getConstructor(CharSequence.class, Integer.TYPE)
                            .newInstance(nick0, Integer.valueOf(0));
                } catch (Throwable ignored2) {
                }
                if (summary == null) {
                    summary = buildFirst(sumCls, nick0);
                }
            } catch (Throwable ignored) {
            }
            Object unread = null;
            try {
                Class<?> unCls = Class.forName(
                        "com.tencent.qqnt.chats.core.adapter.itemdata.RecentContactChatItem$UnreadInfo",
                        false, item.getClass().getClassLoader());
                int n = QqKernel.intOf(QqKernel.fieldValue(info, "unreadCnt"));
                try {
                    unread = unCls.getConstructor(Integer.TYPE, Long.TYPE)
                            .newInstance(Integer.valueOf(n), Long.valueOf(0L));
                } catch (Throwable ignored2) {
                }
                if (unread == null) {
                    unread = buildFirst(unCls, Integer.valueOf(n));
                }
            } catch (Throwable ignored) {
            }
            if (title != null) {
                for (Field f : item.getClass().getDeclaredFields()) {
                    if (f.getName().equals("title") || f.getType().getSimpleName().equals("TitleInfo")) {
                        f.setAccessible(true);
                        f.set(item, title);
                        break;
                    }
                }
            }
            if (summary != null) {
                for (Field f : item.getClass().getDeclaredFields()) {
                    if (f.getName().equals("summary") || f.getType().getSimpleName().equals("SummaryInfo")) {
                        f.setAccessible(true);
                        f.set(item, summary);
                        break;
                    }
                }
            }
            if (unread != null) {
                for (Field f : item.getClass().getDeclaredFields()) {
                    if (f.getName().equals("unread") || f.getType().getSimpleName().equals("UnreadInfo")) {
                        f.setAccessible(true);
                        f.set(item, unread);
                        break;
                    }
                }
            }
        } catch (Throwable th) {
            Log.w("填充渲染字段失败: " + th);
        }
    }

    private static boolean paramHas(Class<?>[] ps, Class<?> want) {
        for (Class<?> p : ps) {
            if (p == want) {
                return true;
            }
        }
        return false;
    }

    private static Object buildSimple(Class<?> cls, Class<?>[] types, Object[] vals) {
        try {
            Constructor<?> c = cls.getConstructor(types);
            c.setAccessible(true);
            return c.newInstance(vals);
        } catch (Throwable ignored) {
        }
        return null;
    }

    private static Object buildFirst(Class<?> cls, Object first) {
        try {
            for (Constructor<?> c : cls.getDeclaredConstructors()) {
                Class<?>[] ps = c.getParameterTypes();
                if (ps.length == 0) {
                    continue;
                }
                c.setAccessible(true);
                Object[] argv = new Object[ps.length];
                boolean firstSet = false;
                for (int i = 0; i < ps.length; ++i) {
                    if (!firstSet && (ps[i] == String.class || ps[i] == CharSequence.class
                            || ps[i] == Integer.class || ps[i] == Integer.TYPE)) {
                        argv[i] = first;
                        firstSet = true;
                    } else if (ps[i] == Integer.TYPE || ps[i] == Integer.class) {
                        argv[i] = Integer.valueOf(0);
                    } else if (ps[i] == Long.TYPE || ps[i] == Long.class) {
                        argv[i] = Long.valueOf(0L);
                    } else if (ps[i] == Boolean.TYPE || ps[i] == Boolean.class) {
                        argv[i] = Boolean.FALSE;
                    } else if (ps[i] == String.class || ps[i] == CharSequence.class) {
                        argv[i] = "";
                    } else {
                        argv[i] = null;
                    }
                }
                if (firstSet) {
                    return c.newInstance(argv);
                }
            }
        } catch (Throwable ignored) {
        }
        return null;
    }

    private static Object defaultValue(Class<?> t) {
        if (!t.isPrimitive()) {
            return null;
        }
        if (t == Boolean.TYPE) {
            return Boolean.FALSE;
        }
        if (t == Integer.TYPE) {
            return 0;
        }
        if (t == Long.TYPE) {
            return 0L;
        }
        if (t == Float.TYPE) {
            return Float.valueOf(0.0f);
        }
        if (t == Double.TYPE) {
            return 0.0;
        }
        if (t == Character.TYPE) {
            return Character.valueOf('\u0000');
        }
        if (t == Byte.TYPE) {
            return (byte)0;
        }
        if (t == Short.TYPE) {
            return (short)0;
        }
        return null;
    }

    private static Method findBindMethodUncached(Class<?> cls) {
        for (Class<?> c = cls; c != null && c != Object.class; c = c.getSuperclass()) {
            for (Method m : c.getDeclaredMethods()) {
                Class<?>[] ps = m.getParameterTypes();
                if (ps.length < 2) continue;
                boolean hasItem = false;
                for (Class<?> p : ps) {
                    if (!p.getName().endsWith("RecentContactChatItem")) continue;
                    hasItem = true;
                    break;
                }
                if (!hasItem || m.getName().equals("getItem") || m.getName().startsWith("set")) continue;
                return m;
            }
        }
        return null;
    }

    private static View fallbackRow(final Context context, final Dialog dialog, Object[] item) {
        /* 与宿主卡片一致的外观：卡片色、圆角、4dp 间距防粘连 */
        LinearLayout outer = new LinearLayout(context);
        outer.setOrientation(1);
        LinearLayout.LayoutParams outerLp = new LinearLayout.LayoutParams(-1, -2);
        outerLp.setMargins(dp(context, 4), dp(context, 2), dp(context, 4), dp(context, 2));
        outer.setLayoutParams(outerLp);
        LinearLayout row = new LinearLayout(context);
        row.setOrientation(1);
        GradientDrawable bgCard = new GradientDrawable();
        bgCard.setColor(-14013129);
        bgCard.setCornerRadius(dp(context, 10));
        row.setBackground(bgCard);
        int p = dp(context, 14);
        row.setPadding(p, dp(context, 12), p, dp(context, 12));
        TextView t = new TextView(context);
        String name = (String)item[1];
        t.setText((CharSequence)(name == null || name.trim().isEmpty() ? (String)item[0] : name.trim()));
        t.setTextSize(15.0f);
        t.setTextColor(-1);
        row.addView((View)t);
        TextView pv = new TextView(context);
        pv.setText((CharSequence)item[2]);
        pv.setTextSize(12.5f);
        pv.setTextColor(-6643546);
        pv.setSingleLine(true);
        row.addView((View)pv);
        final String uid = (String)item[0];
        row.setClickable(true);
        row.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                try {
                    dialog.dismiss();
                }
                catch (Throwable ignored) {}
                QqKernel.setTempChatUnread((String)uid, 0);
                QqKernel.openTempChatSession((Context)context, (String)uid);
            }
        });
        row.setOnLongClickListener(new View.OnLongClickListener(){
            public boolean onLongClick(View v) {
                QqKernel.showTempChatMenu((Context)context, (String)uid);
                return true;
            }
        });
        outer.addView((View)row);
        return outer;
    }

    /**
     * 渲染第 idx 个临时会话卡（与宿主会话卡一致：昵称/头像/未读/时间/预览 + 原生长按菜单）。
     * 数据来自 QqKernel.tempChatSnapshot()（按最后消息时间倒序）。
     */
    /**
     * 取宿主真实会话 item 作为原型（克隆法：所有非空字段天然满足 Kotlin 非空校验）。
     * 优先从 adapter 的 getCurrentList() 里拿第一个 RecentContactChatItem。
     */
    /** 供 QqKernel 调用：取宿主真实 ChatItem 原型 */
    /** 取 adapter 当前列表中第 index 项（不经我们的 getItem hook） */
    public static Object currentListItemAt(Object adapter, int index) {
        try {
            Object cur = XposedHelpers.callMethod(adapter, "getCurrentList", new Object[0]);
            if (cur instanceof List) {
                List list = (List) cur;
                if (index >= 0 && index < list.size()) {
                    return list.get(index);
                }
            }
        } catch (Throwable ignored) {}
        return null;
    }

    public static Object hostChatItemPrototypePublic() {
        if (sHostItemProto != null) {
            return sHostItemProto;
        }
        try {
            Object adapter = sTempChatAdapter;
            if (adapter == null && sAppCtx != null) {
                adapter = tempChatAdapter(sAppCtx);
            }
            if (adapter == null && sListAdapter != null) {
                adapter = sListAdapter;
            }
            if (adapter == null) {
                return null;
            }
            /* 1) 先试 adapter 自己的 getItem(int) */
            try {
                Object one = XposedHelpers.callMethod(adapter, "getItem", Integer.valueOf(0));
                if (one != null && one.getClass().getName().endsWith("RecentContactChatItem")) {
                    return one;
                }
            } catch (Throwable ignored) {}
            /* 2) 再试 getCurrentList() */
            Object cur = XposedHelpers.callMethod(adapter, "getCurrentList", new Object[0]);
            if (cur instanceof List) {
                for (Object it : (List) cur) {
                    if (it != null && it.getClass().getName().endsWith("RecentContactChatItem")) {
                        return it;
                    }
                }
            }
        } catch (Throwable ignored) {}
        return null;
    }

    private static Object hostChatItemPrototype(android.content.Context context) {
        try {
            Object adapter = tempChatAdapter(context);
            if (adapter == null) return null;
            Object cur = XposedHelpers.callMethod(adapter, "getCurrentList", new Object[0]);
            if (!(cur instanceof List)) return null;
            for (Object it : (List) cur) {
                if (it == null) continue;
                if (it.getClass().getName().endsWith("RecentContactChatItem")) {
                    return it;
                }
            }
        } catch (Throwable ignored) {}
        return null;
    }

    /** 按字段名取 binding 里任意类型字段 */
    private static Object readAnyField(Object target, String name) {
        if (target == null) return null;
        Class<?> c = target.getClass();
        while (c != null && c != Object.class) {
            try {
                java.lang.reflect.Field f = c.getDeclaredField(name);
                f.setAccessible(true);
                return f.get(target);
            } catch (Throwable ignored) {}
            c = c.getSuperclass();
        }
        return null;
    }

    /** 给任意文本控件（TextView / SingleLineTextView）设文本 */
    private static void setAnyText(Object v, String text) {
        if (v == null) return;
        try {
            if (v instanceof android.widget.TextView) {
                ((android.widget.TextView) v).setText(text);
                return;
            }
            java.lang.reflect.Method m = v.getClass().getMethod("setText", CharSequence.class);
            m.setAccessible(true);
            m.invoke(v, text);
        } catch (Throwable ignored) {
            try {
                java.lang.reflect.Method m2 = v.getClass().getMethod("setText", String.class);
                m2.setAccessible(true);
                m2.invoke(v, text);
            } catch (Throwable ignored2) {}
        }
    }

    /**
     * 用宿主 WatchRecentContactHolder 的 binding 直接填充临时会话（样式与宿主完全一致）。
     * 逆向：WatchRecentContactHolder.b = ItemRecentContactBinding
     *   b.c -> 昵称  b.e -> 摘要  b.d -> 时间  b.f -> 置顶图标
     */
    public static void bindTempChatByHostBinding(Object holder, int idx) {
        try {
            java.util.List<Object[]> list = com.tencent.qqlitepro.http.QqKernel.tempChatSnapshot();
            if (list == null || idx < 0 || idx >= list.size()) {
                return;
            }
            Object[] item = list.get(idx);
            String uid = (String) item[0];
            String nick = item[1] == null ? "" : String.valueOf(item[1]);
            nick = nick.replace("（临时会话）", "").replace("(临时会话)", "").trim();
            if (nick.isEmpty()) {
                nick = uid;
            }
            String preview = item[2] == null ? "" : String.valueOf(item[2]);
            int unread = item[3] instanceof Integer ? (Integer) item[3] : 0;
            long msgTime = com.tencent.qqlitepro.http.QqKernel.longOf(item[4]);
            long uinCalc = com.tencent.qqlitepro.http.QqKernel.uidToUinPublic(uid);
            if (uinCalc <= 0) {
                uinCalc = com.tencent.qqlitepro.http.QqKernel.tempChatUinOf(uid);
            }
            final long uin = uinCalc;
            boolean top = com.tencent.qqlitepro.http.QqKernel.isTempChatTop(uid);

            Object binding = null;
            try {
                binding = XposedHelpers.getObjectField(holder, "b");
            } catch (Throwable ignored) {}
            if (binding == null && holder != null) {
                /* 兜底：holder 自身可能是 binding */
                binding = holder;
            }
            View itemView = null;
            try {
                itemView = (View) XposedHelpers.getObjectField(holder, "itemView");
            } catch (Throwable ignored) {}

            /*
             * 关键：itemView 会被 RecyclerView 复用。
             * 旧版自绘卡曾给它设过深色圆角背景 / 12dp padding / 6dp 外边距，
             * 换成宿主 binding 渲染后这些残留会让临时卡底色与正常会话不一致。
             * 这里还原为宿主原生外观（宿主正常卡没有自定义背景，padding/外边距由布局自身决定）。
             */
            /*
             * 底色：保留宿主 ItemRecentContactBinding 自带的卡片背景（normal 会话就是这个）。
             * 旧实现在这里把 background 置 null，导致临时卡变"透明"，与正常卡不一致。
             * 这里只清掉我们早期自绘留下的"纯色"污染（ColorDrawable），
             * 宿主自带的 StateListDrawable / 圆角 Drawable 一律保留。
             */
            try {
                Object[] targets = new Object[]{itemView, readAnyField(binding, "a")};
                for (Object t : targets) {
                    if (!(t instanceof View)) continue;
                    View v0 = (View) t;
                    android.graphics.drawable.Drawable bgd = v0.getBackground();
                    if (bgd instanceof android.graphics.drawable.ColorDrawable) {
                        /* 自绘残留纯色（如 -14013129）才重置 */
                        v0.setBackground(null);
                    }
                    v0.setSelected(false);
                    v0.setPressed(false);
                    v0.setActivated(false);
                }
            } catch (Throwable ignored) {}

            /* 字段：a=ConstraintLayout b=AvatarView c=昵称 d=时间 e=摘要 f=置顶 g=未读 */
            Object oNick = readAnyField(binding, "c");
            Object oSummary = readAnyField(binding, "e");
            Object oTime = readAnyField(binding, "d");
            View vTop = (View) readAnyField(binding, "f");
            Object oUnread = readAnyField(binding, "g");
            View vAvatar = (View) readAnyField(binding, "b");
            View vUnread = (oUnread instanceof View) ? (View) oUnread : null;

            /* 字段 d 是时间、c 是"上排"、e 是"下排"。
             * 逆向确认：宿主把 item.h.a(SummaryInfo) 放 c，item.f.a(TitleInfo) 放 e。
             * 这里按视觉语义：c=昵称，e=预览。 */
            /* 实测：binding.e 为上排(昵称)，binding.c 为下排(摘要) */
            setAnyText(oSummary, nick + "（临时会话）");
            setAnyText(oNick, preview);
            setAnyText(oTime, com.tencent.qqlitepro.http.QqKernel.formatChatTime(msgTime));
            if (vTop != null) {
                vTop.setVisibility(top ? View.VISIBLE : View.GONE);
            }
            if (vUnread != null) {
                try {
                    XposedHelpers.callMethod(vUnread, "setRedNum", Integer.valueOf(Math.max(0, unread)));
                    XposedHelpers.callMethod(vUnread, "setVisibility",
                            Integer.valueOf(unread > 0 ? View.VISIBLE : View.GONE));
                } catch (Throwable ignored) {}
            }
            if (vAvatar != null && uin > 0) {
                final View avV = vAvatar;
                final long fUin = uin;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            java.net.URL u = new java.net.URL(
                                    "https://q1.qlogo.cn/g?b=qq&nk=" + fUin + "&s=140");
                            java.net.HttpURLConnection c = (java.net.HttpURLConnection) u.openConnection();
                            c.setConnectTimeout(8000);
                            c.setReadTimeout(8000);
                            c.setDoInput(true);
                            c.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android)");
                            final android.graphics.Bitmap bm =
                                    android.graphics.BitmapFactory.decodeStream(c.getInputStream());
                            if (bm != null) {
                                new android.os.Handler(android.os.Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        /* WatchAvatarView 只认 setBitmap(Bitmap)，ImageView#setImageBitmap 无效 */
                                        try {
                                            ChatCardInjector.setBitmapIntoAvatar(avV, bm);
                                        } catch (Throwable ignored) {}
                                    }
                                });
                            }
                        } catch (Throwable ignored) {}
                    }
                }, "temp-av-" + fUin).start();
            }

            /* 点击进会话 / 长按原生菜单 */
            final Object bindingF = binding;
            if (itemView != null) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            com.tencent.qqlitepro.http.QqKernel.setTempChatUnread(uid, 0);
                            /* 立即隐藏当前卡片上的未读徽章，避免进会话后仍显示 */
                            try {
                                Object unreadV = readAnyField(bindingF, "g");
                                if (unreadV != null) {
                                    XposedHelpers.callMethod(unreadV, "setRedNum", Integer.valueOf(0));
                                    XposedHelpers.callMethod(unreadV, "setVisibility",
                                            Integer.valueOf(View.GONE));
                                }
                            } catch (Throwable ignored) {}
                            com.tencent.qqlitepro.http.QqKernel.openTempChatSession(v.getContext(), uid);
                        } catch (Throwable ignored) {}
                    }
                });
                /* 需求：临时会话卡片长按不再弹出任何菜单（直接消费事件） */
                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        return true;
                    }
                });
            }
        } catch (Throwable th) {
            Log.w("bindTempChatByHostBinding 失败: " + th);
        }
    }

    /** 从 Context 找到宿主栈顶 androidx Fragment（菜单 show 需要） */
    private static Object topAndroidXFragmentOf(android.content.Context context) {
        try {
            android.app.Activity act = null;
            android.content.Context c = context;
            while (c instanceof android.content.ContextWrapper) {
                if (c instanceof android.app.Activity) {
                    act = (android.app.Activity) c;
                    break;
                }
                c = ((android.content.ContextWrapper) c).getBaseContext();
            }
            if (act == null) {
                java.util.Iterator<android.app.Activity> it = allActivities().iterator();
                if (it.hasNext()) act = it.next();
            }
            if (act == null) return null;
            Class<?> supCls = Class.forName("androidx.fragment.app.FragmentActivity");
            if (!supCls.isInstance(act)) return null;
            Object fm = supCls.getMethod("getSupportFragmentManager").invoke(act);
            java.util.List<?> frags = (java.util.List<?>) fm.getClass().getMethod("getFragments").invoke(fm);
            if (frags == null || frags.isEmpty()) return null;
            for (int i = frags.size() - 1; i >= 0; i--) {
                if (frags.get(i) != null) return frags.get(i);
            }
        } catch (Throwable ignored) {}
        return null;
    }



    public static void bindTempChatCard(View view) {
        if (view == null) {
            return;
        }
        try {
            Context context = view.getContext();
            int total = QqKernel.tempChatCount();
            int unread = QqKernel.tempChatTotalUnread();
            Object tag = view.getTag(1296124236);
            View titleView = null;
            View summaryView = null;
            View badgeView = null;
            try {
                if (tag instanceof View) {
                    View inner = (View)tag;
                    titleView = ChatCardInjector.findTextViews(inner)[0];
                    summaryView = ChatCardInjector.findTextViews(inner)[1];
                    badgeView = ChatCardInjector.findBadgeView(inner);
                } else if (tag != null) {
                    Class<?> cls = tag.getClass();
                    titleView = ChatCardInjector.readViewField(tag, cls, View.class, "title", "f", "b");
                    summaryView = ChatCardInjector.readViewField(tag, cls, View.class, "summary", "h", "d");
                    badgeView = ChatCardInjector.readViewField(tag, cls, View.class, "unread", "g", "a");
                }
            }
            catch (Throwable throwable) {
                // empty catch block
            }
            if (titleView == null) {
                titleView = view.findViewWithTag((Object)"temp_chat_title");
            }
            if (summaryView == null) {
                summaryView = view.findViewWithTag((Object)"temp_chat_sub");
            }
            if (badgeView == null) {
                badgeView = view.findViewWithTag((Object)"temp_chat_badge");
            }
            if (titleView != null) {
                ChatCardInjector.setText(titleView, "\u4e34\u65f6\u4f1a\u8bdd");
            }
            if (summaryView != null) {
                try {
                    XposedHelpers.callMethod((Object)summaryView, (String)"setSingleLine", (Object[])new Object[]{true});
                    XposedHelpers.callMethod((Object)summaryView, (String)"setEllipsize", (Object[])new Object[]{TextUtils.TruncateAt.END});
                }
                catch (Throwable throwable) {
                    // empty catch block
                }
                ChatCardInjector.setText(summaryView, ChatCardInjector.tempChatSummary(total, unread));
            }
            if (badgeView != null) {
                ChatCardInjector.applyUnread(badgeView, unread);
            }
            view.setClickable(true);
            view.setOnClickListener(new View.OnClickListener(){

                public void onClick(View v) {
                    try {
                        ChatCardInjector.openTempChatFullScreen(v.getContext());
                    }
                    catch (Throwable th) {
                        Log.w((String)("\u6253\u5f00\u4e34\u65f6\u4f1a\u8bdd\u5217\u8868\u5931\u8d25: " + th));
                    }
                }
            });
        }
        catch (Throwable th) {
            Log.w((String)("ChatCard \u7ed1\u5b9a\u4e34\u65f6\u4f1a\u8bdd\u5361\u7247\u5931\u8d25: " + th));
        }
    }

    private static String tempChatSummary(int total, int unread) {
        if (total <= 0) {
            return "\u6682\u65e0\u4e34\u65f6\u4f1a\u8bdd";
        }
        if (unread > 0) {
            return total + " \u4e2a\u4e34\u65f6\u4f1a\u8bdd\uff0c" + unread + " \u6761\u672a\u8bfb";
        }
        return total + " \u4e2a\u4e34\u65f6\u4f1a\u8bdd";
    }

    public static void bindCard(View view) {
        try {
            View viewFindBadgeView;
            View view3;
            View view2;
            Object tag = view.getTag(1296124236);
            if (tag == null) {
                TextView textView2;
                Context context = view.getContext();
                int iMailUnreadCount = Config.mailUnreadCount((Context)context);
                TextView textView = (TextView)view.findViewWithTag((Object)"mail_badge");
                if (textView != null) {
                    ChatCardInjector.applyBadgeTextView(textView, iMailUnreadCount);
                }
                if ((textView2 = (TextView)view.findViewWithTag((Object)"mail_sub")) != null) {
                    textView2.setSingleLine(true);
                    textView2.setEllipsize(TextUtils.TruncateAt.END);
                    textView2.setText((CharSequence)ChatCardInjector.summaryText(context, iMailUnreadCount));
                    return;
                }
                return;
            }
            int iMailUnreadCount2 = Config.mailUnreadCount((Context)view.getContext());
            int i = 0;
            if (tag instanceof View) {
                View view4 = (View)tag;
                view2 = ChatCardInjector.findTextViews(view4)[0];
                view3 = ChatCardInjector.findTextViews(view4)[1];
                viewFindBadgeView = ChatCardInjector.findBadgeView(view4);
                View viewFindTimeView = ChatCardInjector.findTimeView(view4);
                if (viewFindTimeView != null) {
                    ChatCardInjector.setText(viewFindTimeView, ChatCardInjector.formatMailTime(view4.getContext()));
                }
            } else {
                Class<?> cls = tag.getClass();
                view2 = ChatCardInjector.readViewField(tag, cls, View.class, "title", "f", "b");
                view3 = ChatCardInjector.readViewField(tag, cls, View.class, "summary", "h", "d");
                viewFindBadgeView = ChatCardInjector.readViewField(tag, cls, View.class, "unread", "g", "a");
            }
            if (view2 != null) {
                ChatCardInjector.setText(view2, "QQ \u90ae\u7bb1");
            }
            if (view3 != null) {
                try {
                    XposedHelpers.callMethod((Object)view3, (String)"setSingleLine", (Object[])new Object[]{true});
                    XposedHelpers.callMethod((Object)view3, (String)"setEllipsize", (Object[])new Object[]{TextUtils.TruncateAt.END});
                }
                catch (Throwable cls) {
                    // empty catch block
                }
                ChatCardInjector.setText(view3, ChatCardInjector.summaryText(view.getContext(), iMailUnreadCount2));
            }
            if (viewFindBadgeView != null) {
                ChatCardInjector.applyUnread(viewFindBadgeView, iMailUnreadCount2);
            }
            try {
                View viewFindViewById;
                Context context2 = view.getContext();
                int identifier = context2.getResources().getIdentifier("top_icon", "id", context2.getPackageName());
                if (identifier != 0 && (viewFindViewById = view.findViewById(identifier)) != null) {
                    if (!Config.mailPinned((Context)context2)) {
                        i = 8;
                    }
                    viewFindViewById.setVisibility(i);
                }
            }
            catch (Throwable context2) {
                // empty catch block
            }
            if (tag instanceof View) {
                StringBuilder sb = new StringBuilder("ChatCard bindCard: title=");
                String simpleName = "null";
                sb.append(view2 == null ? "null" : view2.getClass().getSimpleName());
                sb.append(" summary=");
                sb.append(view3 == null ? "null" : view3.getClass().getSimpleName());
                sb.append(" unread=");
                if (viewFindBadgeView != null) {
                    simpleName = viewFindBadgeView.getClass().getSimpleName();
                }
                sb.append(simpleName);
                sb.append(" count=");
                sb.append(iMailUnreadCount2);
                Log.i((String)sb.toString());
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    private static void setText(View view, String str) {
        try {
            XposedHelpers.callMethod((Object)view, (String)"setText", (Object[])new Object[]{str});
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    private static String summaryText(Context context, int i) {
        if (Config.mailAddr((Context)context).isEmpty()) {
            return "\u672a\u914d\u7f6eQQ\u90ae\u7bb1\u4fe1\u606f";
        }
        /* 已配置：尽量展示更多邮箱信息（未读数 + 发件人 + 主题摘要） */
        String from = "";
        String subject = "";
        try { from = Config.mailLatestFrom(context); } catch (Throwable ignored) {}
        try { subject = Config.mailLatestSubject(context); } catch (Throwable ignored) {}
        String latest = Config.mailLatestSummary((Context)context);
        StringBuilder sb = new StringBuilder();
        if (i > 0) {
            sb.append("未读 ").append(i).append(" 封");
        } else {
            sb.append("暂无新邮件");
        }
        if (from != null && !from.isEmpty()) {
            sb.append(" · ").append(from);
        }
        if (subject != null && !subject.isEmpty()) {
            sb.append("：").append(subject);
        } else if (latest != null && !latest.isEmpty()) {
            sb.append("：").append(latest);
        }
        return truncateText(sb.toString(), 60);
    }

    private static String truncateText(String str, int i) {
        if (str == null) {
            return "";
        }
        String strTrim = str.replaceAll("[\\r\\n\\t]+", " ").trim();
        if (strTrim.length() <= i) {
            return strTrim;
        }
        return strTrim.substring(0, i) + "\u2026";
    }

    private static void applyUnread(View view, int i) {
        if (view == null) {
            return;
        }
        String name = view.getClass().getName();
        if (name.contains("QUIBadge")) {
            ChatCardInjector.applyQuiBadge(view, i);
            return;
        }
        if (name.contains("RollingTextView")) {
            try {
                Method methodFindMethod = ChatCardInjector.findMethod(view.getClass(), "e", Integer.class, Drawable.class);
                if (methodFindMethod != null) {
                    methodFindMethod.setAccessible(true);
                    Object[] objArr = new Object[]{i > 0 ? Integer.valueOf(i) : null, null};
                    methodFindMethod.invoke((Object)view, objArr);
                }
            }
            catch (Throwable throwable) {
                // empty catch block
            }
            view.setVisibility(i <= 0 ? 8 : 0);
            return;
        }
        if (view instanceof TextView) {
            ChatCardInjector.applyBadgeTextView((TextView)view, i);
        }
    }

    private static void applyQuiBadge(View view, int i) {
        try {
            if (i > 0) {
                XposedHelpers.setIntField((Object)view, (String)"i", (int)(i <= 9 ? 1 : 2));
                XposedHelpers.setIntField((Object)view, (String)"j", (int)i);
                XposedHelpers.setObjectField((Object)view, (String)"k", (Object)(i > 99 ? "99+" : String.valueOf(i)));
                view.setVisibility(0);
                return;
            }
            view.setVisibility(8);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    private static void applyBadgeTextView(TextView textView, int i) {
        try {
            Context context = textView.getContext();
            boolean zMailDnd = Config.mailDnd((Context)context);
            if (i > 0) {
                textView.setText((CharSequence)(i > 99 ? "99+" : String.valueOf(i)));
                textView.setTextColor(zMailDnd ? -1754827 : -1);
                Drawable background = textView.getBackground();
                GradientDrawable gradientDrawable = background instanceof GradientDrawable ? (GradientDrawable)background : new GradientDrawable();
                gradientDrawable.setColor(zMailDnd ? -1 : -1754827);
                gradientDrawable.setCornerRadius((float)ChatCardInjector.dp(context, 18));
                if (!(background instanceof GradientDrawable)) {
                    textView.setBackground((Drawable)gradientDrawable);
                }
                textView.setVisibility(0);
                return;
            }
            textView.setVisibility(8);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    /** 临时会话专用菜单：sMenuKind="temp"，菜单项点击按登记 uid 独立生效 */
    /** 当前菜单是否为"模块为临时会话弹出"且目标确实是临时会话 */
    private static boolean isTempMenuActive() {
        if (!"temp".equals(sMenuKind)) {
            return false;
        }
        String uid = com.tencent.qqlitepro.http.QqKernel.menuTargetUid();
        return uid != null && !uid.isEmpty()
                && com.tencent.qqlitepro.http.QqKernel.isTempChat(uid);
    }

    public static void showTempKindMenu(Context context) {
        sMenuKind = "temp";
        showNativeMenu(context, "temp");
    }

    public static void showNativeMailMenu(Context context) {
        ChatCardInjector.showNativeMenu(context, "mail");
    }

    /** 临时会话菜单（不覆盖 sMenuKind 为 mail）：showTempChatMenu 的回退必须走这里 */
    public static void showNativeTempMenu(Context context) {
        ChatCardInjector.showNativeMenu(context, "temp");
    }

    /** kind: mail=邮箱卡片 temp=临时会话 */
    /**
     * 向宿主会话长按菜单（SelectDialogFragment）注入"消息数据"按钮。
     * 仅当 sMenuTargetUin 是群（>0）且来自群聊菜单时显示。
     * 实现：找到菜单根容器（setting_container），在条目列表末尾追加一个 TextView 按钮，
     * 样式与宿主条目一致（icon+text 行），点击后打开消息数据界面。
     */
    private static void injectStatMenuButton(Object dialogFragment) {
        try {
            /*
             * 目标解析：优先从宿主菜单的 items（SelectDialogFragment.b）里
             * 找 RecentContactChatItem（DeleteItem/SetTopItem/DisturbItem 都带 item 字段），
             * 这样群聊/好友长按（不经我们 hook）也能拿到正确目标。
             */
            /* 只对群聊显示"消息数据"入口 */
            if (!menuTargetIsGroup()) {
                return;
            }
            long uin = com.tencent.qqlitepro.http.QqKernel.menuTargetUinLong();
            String name = com.tencent.qqlitepro.http.QqKernel.menuTargetName();
            try {
                Object items = XposedHelpers.getObjectField(dialogFragment, "b");
                if (items instanceof List) {
                    for (Object it : (List) items) {
                        if (it == null) continue;
                        try {
                            Object item = XposedHelpers.getObjectField(it, "item");
                            if (item != null) {
                                Object info = XposedHelpers.getObjectField(item, "a");
                                if (info != null) {
                                    String peerUid = (String) XposedHelpers.getObjectField(info, "peerUid");
                                    long peerUin = XposedHelpers.getLongField(info, "peerUin");
                                    String peerName = (String) XposedHelpers.getObjectField(info, "peerName");
                                    if (peerUin > 0) { uin = peerUin; }
                                    if (peerUin <= 0 && peerUid != null && peerUid.matches("\\d+")) {
                                        uin = Long.parseLong(peerUid);
                                    }
                                    if (peerName != null && !peerName.isEmpty()) { name = peerName; }
                                    break;
                                }
                            }
                        } catch (Throwable ignored) {}
                    }
                }
            } catch (Throwable ignored) {}
            if (uin <= 0) {
                return;
            }
            if (dialogFragment == null) {
                return;
            }
            // 延迟到视图创建完成后注入
            final Object fDialog = dialogFragment;
            final long fUin = uin;
            final String fName = name;
            new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        Object frag = fDialog;
                        View root = null;
                        try {
                            root = (View) XposedHelpers.callMethod(frag, "getView", new Object[0]);
                        } catch (Throwable ignored) {}
                        if (root == null) {
                            try {
                                root = (View) XposedHelpers.callMethod(frag, "requireView", new Object[0]);
                            } catch (Throwable ignored) {}
                        }
                        if (!(root instanceof ViewGroup)) {
                            return;
                        }
                        // 找一个可容纳按钮的竖向容器（最深的 ViewGroup 且 childCount>0）
                        ViewGroup container = findVerticalContainer((ViewGroup) root, 0);
                        if (container == null) {
                            return;
                        }
                        Context ctx = container.getContext();
                        TextView btn = new TextView(ctx);
                        btn.setText("消息数据");
                        btn.setTextSize(14.0f);
                        btn.setTextColor(0xFF4CAF50);
                        btn.setGravity(17);
                        int pad = (int) (ctx.getResources().getDisplayMetrics().density * 12);
                        btn.setPadding(pad, pad, pad, pad);
                        btn.setClickable(true);
                        final long uu = fUin;
                        final String nn = fName == null ? String.valueOf(fUin) : fName;
                        btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    ChatCardInjector.dismissActiveMenu();
                                } catch (Throwable ignored) {}
                                com.tencent.qqlitepro.hook.SettingsInjector.openGroupMsgStats(v.getContext(), uu, nn);
                            }
                        });
                        container.addView(btn);
                    } catch (Throwable th) {
                        Log.w("注入消息数据按钮失败: " + th);
                    }
                }
            }, 220L);
        } catch (Throwable th) {
            Log.w("injectStatMenuButton 异常: " + th);
        }
    }

    /** 找菜单里最深的竖向 LinearLayoutCompat/LinearLayout 容器 */
    private static ViewGroup findVerticalContainer(ViewGroup root, int depth) {
        if (root == null || depth > 5) {
            return null;
        }
        ViewGroup best = null;
        for (int i = 0; i < root.getChildCount(); ++i) {
            View c = root.getChildAt(i);
            if (c instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) c;
                if ((vg instanceof LinearLayout) && ((LinearLayout) vg).getOrientation() == LinearLayout.VERTICAL) {
                    best = vg;
                }
                ViewGroup deeper = findVerticalContainer(vg, depth + 1);
                if (deeper != null) {
                    best = deeper;
                }
            }
        }
        return best;
    }

    /**
     * 临时会话卡长按：完全复用宿主会话卡片的长按设置链路（逆向 listener$1.a_0）：
     *   new SelectDialogFragment(listOf(new DeleteItem(item), new SetTopItem(item),
     *        new SetDisturbItem(item)), null, null, 6)
     *       .show(fragment.getParentFragmentManager(), "SelectFragment")
     * item 用真实的构造函数传入（不是 Unsafe），故菜单项能按该会话生效、互不影响。
     */
    public static boolean showHostSessionMenu(Context context, Object chatItem, Object fragment) {
        try {
            ClassLoader cl = chatItem.getClass().getClassLoader();
            Class<?> deleteCls = Class.forName("com.tencent.qqnt.watch.chat.list.longclick.DeleteItem", false, cl);
            Class<?> topCls = Class.forName("com.tencent.qqnt.watch.chat.list.longclick.SetTopItem", false, cl);
            Class<?> disturbCls = Class.forName("com.tencent.qqnt.watch.chat.list.longclick.SetDisturbItem", false, cl);
            java.util.ArrayList<Object> items = new java.util.ArrayList<Object>();
            /* 宿主构造器签名就是 <init>(RecentContactChatItem)，必须传具体类而不是接口 */
            Class<?> itemType = chatItem.getClass();
            items.add(deleteCls.getConstructor(itemType).newInstance(chatItem));
            items.add(topCls.getConstructor(itemType).newInstance(chatItem));
            items.add(disturbCls.getConstructor(itemType).newInstance(chatItem));
            /* 模块自带的"清空消息" */
            try {
                Class<?> clearCls = Class.forName("momoi.mod.qqpro.hook.ClearMsgListItem", false, cl);
                items.add(clearCls.getConstructor(itemType).newInstance(chatItem));
            } catch (Throwable ignored) {}
            Class<?> selectCls = Class.forName("com.tencent.qqnt.watch.ui.componet.select.SelectDialogFragment", false, cl);
            Class<?> fn0 = Class.forName("kotlin.jvm.functions.Function0", false, cl);
            Object dialog = selectCls.getConstructor(java.util.List.class, fn0, String.class, Integer.TYPE)
                    .newInstance(items, null, null, Integer.valueOf(6));
            /* 用宿主 Fragment 的 parentFragmentManager show（与原生一致） */
            Object fm = null;
            if (fragment != null) {
                try {
                    fm = fragment.getClass().getMethod("getParentFragmentManager").invoke(fragment);
                } catch (Throwable ignored) {}
            }
            if (fm == null) {
                fm = findSupportFragmentManager(context);
            }
            if (fm == null) {
                return false;
            }
            /* 让菜单 hook 立刻拿到目标 uid（双保险：items 里也有 item） */
            try {
                Object info = XposedHelpers.getObjectField(chatItem, "a");
                String uid = (String) XposedHelpers.getObjectField(info, "peerUid");
                String nick = (String) XposedHelpers.getObjectField(info, "peerName");
                int ct = XposedHelpers.getIntField(info, "chatType");
                long uin = XposedHelpers.getLongField(info, "peerUin");
                com.tencent.qqlitepro.http.QqKernel.registerMenuTarget(uid, nick, ct, uin);
            } catch (Throwable ignored) {}
            markCardMenuItems(items);
            selectCls.getMethod("show", Class.forName("androidx.fragment.app.FragmentManager", false, cl), String.class)
                    .invoke(dialog, fm, "SelectFragment");
            Log.i("临时会话: 已用宿主原生链路弹出长按菜单（item=" + chatItem.hashCode() + "）");
            return true;
        } catch (Throwable th) {
            Log.w("showHostSessionMenu 失败: " + th);
            return false;
        }
    }

    public static void showNativeMenu(Context context, String kind) {
        try {
            Class<?> cls;
            ChatCardInjector.sMenuKind = kind;
            ChatCardInjector.ensureNativeMenuHooks(context);
            ClassLoader classLoaderMenuLoader = ChatCardInjector.menuLoader(context);
            if (classLoaderMenuLoader == null) {
                throw new IllegalStateException("\u65e0\u6cd5\u89e3\u6790\u5bbf\u4e3b\u83dc\u5355\u7c7b loader");
            }
            ArrayList<Object> arrayList = new ArrayList<Object>();
            arrayList.add(ChatCardInjector.allocateNoCtor(Class.forName(DELETE_ITEM, false, classLoaderMenuLoader)));
            arrayList.add(ChatCardInjector.allocateNoCtor(Class.forName(TOP_ITEM, false, classLoaderMenuLoader)));
            arrayList.add(ChatCardInjector.allocateNoCtor(Class.forName(DISTURB_ITEM, false, classLoaderMenuLoader)));
            try {
                arrayList.add(ChatCardInjector.allocateNoCtor(Class.forName(CLEAR_ITEM, false, classLoaderMenuLoader)));
            }
            catch (Throwable throwable) {
                // empty catch block
            }
            Class<?> cls2 = Class.forName(SELECT_DIALOG, false, classLoaderMenuLoader);
            try {
                cls = Class.forName("kotlin.jvm.functions.Function0", false, classLoaderMenuLoader);
            }
            catch (Throwable unused2) {
                cls = null;
            }
            if (cls == null) {
                throw new IllegalStateException("\u65e0\u6cd5\u89e3\u6790 kotlin Function0");
            }
            Object objNewInstance = cls2.getConstructor(List.class, cls, String.class, Integer.TYPE).newInstance(arrayList, null, null, 6);
            ChatCardInjector.markCardMenuItems(arrayList);
            Object objFindSupportFragmentManager = ChatCardInjector.findSupportFragmentManager(context);
            if (objFindSupportFragmentManager == null) {
                throw new IllegalStateException("\u627e\u4e0d\u5230\u53ef\u7528\u7684 FragmentManager");
            }
            String menuTag = "QQMaxCardMenu_" + System.currentTimeMillis();
            cls2.getMethod("show", Class.forName("androidx.fragment.app.FragmentManager", false, classLoaderMenuLoader), String.class).invoke(objNewInstance, objFindSupportFragmentManager, menuTag);
            Log.i((String)"ChatCard \u5df2\u5f39\u51fa QQ \u539f\u751f\u4f1a\u8bdd\u83dc\u5355\uff08\u90ae\u7bb1\u5361\u7247\uff09");
        }
        catch (Throwable thRethrow) {
            Log.w((String)("ChatCard \u539f\u751f\u83dc\u5355\u5f39\u51fa\u5931\u8d25: " + thRethrow));
        }
    }

    private static Object allocateNoCtor(Class<?> cls) throws Throwable {
        Class<?> cls2 = Class.forName("sun.misc.Unsafe");
        Field declaredField = cls2.getDeclaredField("theUnsafe");
        declaredField.setAccessible(true);
        return cls2.getMethod("allocateInstance", Class.class).invoke(declaredField.get(null), cls);
    }

    private static Object findSupportFragmentManager(Context context) {
        while (context instanceof ContextWrapper) {
            try {
                Object objCallMethod = XposedHelpers.callMethod((Object)context, (String)"getSupportFragmentManager", (Object[])new Object[0]);
                if (objCallMethod != null) {
                    return objCallMethod;
                }
                context = ((ContextWrapper)context).getBaseContext();
            }
            catch (Throwable objCallMethod) {}
        }
        Iterator<Activity> it = ChatCardInjector.allActivities().iterator();
        while (it.hasNext()) {
            try {
                Object objCallMethod2 = XposedHelpers.callMethod((Object)it.next(), (String)"getSupportFragmentManager", (Object[])new Object[0]);
                if (objCallMethod2 == null) continue;
                return objCallMethod2;
            }
            catch (Throwable throwable) {
            }
        }
        return null;
    }

    private static ClassLoader menuLoader(Context context) {
        LinkedHashSet<ClassLoader> linkedHashSet = new LinkedHashSet<ClassLoader>();
        try {
            linkedHashSet.add(context.getClassLoader());
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        try {
            linkedHashSet.add(Thread.currentThread().getContextClassLoader());
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        Iterator<Activity> it = ChatCardInjector.allActivities().iterator();
        while (it.hasNext()) {
            try {
                linkedHashSet.add(it.next().getClassLoader());
            }
            catch (Throwable throwable) {}
        }
        for (ClassLoader classLoader : linkedHashSet) {
            try {
                Class.forName(SELECT_DIALOG, false, classLoader);
                return classLoader;
            }
            catch (Throwable throwable) {
            }
        }
        return null;
    }

    private static void markCardMenuItems(List<?> items) {
        if (items == null) {
            return;
        }
        for (int i = 0; i < items.size(); ++i) {
            Object it = items.get(i);
            if (it == null) continue;
            sCardMenuItems.add(it);
        }
    }

    /**
     * 从当前活动的宿主菜单（sActiveMenu=SelectDialogFragment）的 items 里
     * 解析出 RecentContactChatItem -> RecentContactInfo（字段 a）。
     * 原生列表长按不经我们的 showNativeMenu，sCardMenuItems 不含这些 item，
     * 所以 mailMenuItem 为 false —— 必须用这个方法拿目标。
     */
    private static Object menuTargetInfo() {
        try {
            Object menu = sActiveMenu;
            if (menu == null) {
                return null;
            }
            Object items = XposedHelpers.getObjectField(menu, "b");
            if (!(items instanceof List)) {
                return null;
            }
            for (Object it : (List) items) {
                if (it == null) continue;
                try {
                    Object item = XposedHelpers.getObjectField(it, "item");
                    if (item != null) {
                        Object info = XposedHelpers.getObjectField(item, "a");
                        if (info != null) {
                            return info;
                        }
                    }
                } catch (Throwable ignored) {}
            }
        } catch (Throwable ignored) {}
        return null;
    }

    /** 当前菜单目标是否为临时会话：显式登记的 uid 优先（逐卡独立），否则从菜单 items 解析 */
    private static boolean menuTargetIsTempChat() {
        String registered = com.tencent.qqlitepro.http.QqKernel.menuTargetUid();
        if (registered != null && !registered.isEmpty()
                && com.tencent.qqlitepro.http.QqKernel.isTempChat(registered)) {
            return true;
        }
        Object info = menuTargetInfo();
        if (info == null) {
            return false;
        }
        try {
            String uid = (String) XposedHelpers.getObjectField(info, "peerUid");
            return uid != null && com.tencent.qqlitepro.http.QqKernel.isTempChat(uid);
        } catch (Throwable t) {
            return false;
        }
    }

    /** 当前菜单目标的 peerUid：登记 uid 优先 */
    private static String menuTargetPeerUid() {
        String registered = com.tencent.qqlitepro.http.QqKernel.menuTargetUid();
        if (registered != null && !registered.isEmpty()
                && com.tencent.qqlitepro.http.QqKernel.isTempChat(registered)) {
            return registered;
        }
        Object info = menuTargetInfo();
        if (info == null) {
            return null;
        }
        try {
            return (String) XposedHelpers.getObjectField(info, "peerUid");
        } catch (Throwable t) {
            return null;
        }
    }

    /** 当前菜单目标是否为群聊（chatType=2） */
    private static boolean menuTargetIsGroup() {
        Object info = menuTargetInfo();
        if (info == null) {
            return false;
        }
        try {
            return XposedHelpers.getIntField(info, "chatType") == 2;
        } catch (Throwable t) {
            return false;
        }
    }

    /** 当前菜单目标的 uin */
    private static long menuTargetUinLong() {
        Object info = menuTargetInfo();
        if (info == null) {
            return 0L;
        }
        try {
            return XposedHelpers.getLongField(info, "peerUin");
        } catch (Throwable t) {
            try {
                String uid = (String) XposedHelpers.getObjectField(info, "peerUid");
                if (uid != null && uid.matches("\\d+")) return Long.parseLong(uid);
            } catch (Throwable ignored) {}
        }
        return 0L;
    }

    /** 当前菜单目标的昵称 */
    private static String menuTargetNick() {
        Object info = menuTargetInfo();
        if (info == null) {
            return null;
        }
        try {
            return (String) XposedHelpers.getObjectField(info, "peerName");
        } catch (Throwable t) {
            return null;
        }
    }

    public static boolean mailMenuItem(XC_MethodHook.MethodHookParam methodHookParam) {
        Object self;
        Object object = self = methodHookParam == null ? null : methodHookParam.thisObject;
        if (self == null) {
            return false;
        }
        return sCardMenuItems.contains(self);
    }

    /**
     * 关闭当前弹出的菜单。
     * 除了 sActiveMenu，还要遍历所有 Activity 的 FragmentManager：
     * 右滑返回时 onDestroy 可能先于本方法执行，导致 sActiveMenu 已为 null 而菜单残留在界面上。
     */
    private static void dismissActiveMenu() {
        /* 1) 已知实例 */
        Object obj = sActiveMenu;
        if (obj != null) {
            try {
                XposedHelpers.callMethod(obj, "dismissAllowingStateLoss", new Object[0]);
            } catch (Throwable ignored) {
                try {
                    XposedHelpers.callMethod(obj, "dismiss", new Object[0]);
                } catch (Throwable ignored2) {}
            }
        }
        /* 2) 兜底：扫描所有 Fragment，找 SelectDialogFragment 直接关掉 */
        try {
            for (java.lang.Object act : allActivities()) {
                try {
                    Object fm = act.getClass().getMethod("getSupportFragmentManager").invoke(act);
                    java.util.List<?> frags =
                            (java.util.List<?>) fm.getClass().getMethod("getFragments").invoke(fm);
                    if (frags == null) continue;
                    for (Object f : frags) {
                        if (f == null) continue;
                        String cn = f.getClass().getName();
                        if (!cn.contains("SelectDialogFragment")) continue;
                        try {
                            XposedHelpers.callMethod(f, "dismissAllowingStateLoss", new Object[0]);
                        } catch (Throwable ignored) {
                            try {
                                XposedHelpers.callMethod(f, "dismiss", new Object[0]);
                            } catch (Throwable ignored2) {}
                        }
                        Log.i("菜单: 已兜底关闭 " + cn);
                    }
                } catch (Throwable ignored) {}
            }
        } catch (Throwable ignored) {}
        sActiveMenu = null;
    }

    /** 界面切走（右滑返回/Home）时，强制关闭残留菜单 */
    public static void installActivityPauseMenuDismiss(java.lang.ClassLoader cl) {
        if (sPauseDismissHooked) {
            return;
        }
        sPauseDismissHooked = true;
        try {
            de.robv.android.xposed.XposedBridge.hookAllMethods(
                    android.app.Activity.class, "onPause", new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) {
                            try {
                                if (sActiveMenu != null || sMenuKind != null && !"normal".equals(sMenuKind)) {
                                    dismissActiveMenu();
                                }
                            } catch (Throwable ignored) {}
                        }
                    });
            Log.i("菜单: Activity.onPause 关闭钩子已注册");
        } catch (Throwable t) {
            Log.w("菜单: onPause 钩子注册失败 " + t);
        }
    }

    private static synchronized void ensureNativeMenuHooks(Context context) {
        if (sNativeMenuHooked) {
            return;
        }
        try {
            ClassLoader classLoaderMenuLoader = ChatCardInjector.menuLoader(context);
            if (classLoaderMenuLoader == null) {
                return;
            }
            sNativeMenuHooked = true;
            try {
                Class<?> cls;
                Class<?> cls2 = Class.forName(DELETE_ITEM, false, classLoaderMenuLoader);
                Class<?> cls3 = Class.forName(TOP_ITEM, false, classLoaderMenuLoader);
                Class<?> cls4 = Class.forName(DISTURB_ITEM, false, classLoaderMenuLoader);
                Class<?> cls5 = null;
                try {
                    cls = Class.forName(CLEAR_ITEM, false, classLoaderMenuLoader);
                }
                catch (Throwable unused) {
                    cls = null;
                }
                try {
                    cls5 = Class.forName("com.tencent.widget.Switch", false, classLoaderMenuLoader);
                }
                catch (Throwable unused) {
                    // empty catch block
                }
                Class<?> cls6 = Class.forName(ABS_ITEM, false, classLoaderMenuLoader);
                Class<?> cls7 = Class.forName(SELECT_DIALOG, false, classLoaderMenuLoader);
                XposedHelpers.findAndHookMethod(cls7, (String)"onCreate", (Object[])new Object[]{Bundle.class, new AnonymousClass14()});
                XposedHelpers.findAndHookMethod(cls7, (String)"onDestroy", (Object[])new Object[]{new AnonymousClass15()});
                XposedHelpers.findAndHookMethod(cls2, (String)"onClick", (Object[])new Object[]{View.class, new AnonymousClass16(context)});
                AnonymousClass17 anonymousClass17 = new AnonymousClass17(context);
                XposedHelpers.findAndHookMethod(cls3, (String)"onClick", (Object[])new Object[]{View.class, new AnonymousClass18()});
                XposedHelpers.findAndHookMethod(cls3, (String)"getText", (Object[])new Object[]{anonymousClass17});
                AnonymousClass19 anonymousClass19 = new AnonymousClass19(context);
                XposedHelpers.findAndHookMethod(cls4, (String)"onClick", (Object[])new Object[]{View.class, new AnonymousClass20()});
                XposedHelpers.findAndHookMethod(cls4, (String)"getText", (Object[])new Object[]{anonymousClass19});
                XposedHelpers.findAndHookMethod(cls6, (String)"needSwitch", (Object[])new Object[]{new AnonymousClass21(cls3, cls4)});
                /* updateSwitchStatus：只对"模块邮箱卡"生效（mailMenuItem 判定），
                 * 宿主原生会话（含临时会话）完全放行，由宿主写入各自真实状态。 */
                if (cls5 != null) {
                    XposedHelpers.findAndHookMethod(cls6, (String)"updateSwitchStatus",
                            (Object[]) new Object[]{cls5, new AnonymousClass22(cls3, cls4, context)});
                }
                /* onCheckedChanged：只对"模块邮箱卡"生效（mailMenuItem 判定）。
                 * 逆向：宿主 AbsItem.onCheckedChanged 是空实现，SetTopItem 未重写；
                 * 真正置顶逻辑在 SetTopItem.onClick -> setTop() -> TipsUtils。
                 * 因此原生会话必须放行，绝不能 setResult。 */
                XposedHelpers.findAndHookMethod(cls6, (String)"onCheckedChanged",
                        (Object[]) new Object[]{CompoundButton.class, Boolean.TYPE, new AnonymousClass23(cls3, cls4, context)});
                if (cls != null) {
                    XposedHelpers.findAndHookMethod(cls, (String)"onClick", (Object[])new Object[]{View.class, new AnonymousClass24(context)});
                }
                Log.i((String)"ChatCard \u539f\u751f\u83dc\u5355\u90ae\u7bb1\u62e6\u622a hook \u5df2\u6ce8\u518c");
            }
            catch (Throwable thRethrow) {
                Log.w((String)("ChatCard \u539f\u751f\u83dc\u5355 hook \u6ce8\u518c\u5931\u8d25: " + thRethrow));
                sNativeMenuHooked = false;
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    public static void attachExistingLists(ClassLoader classLoader) {
        Handler handler = new Handler(Looper.getMainLooper());
        long[] jArr = new long[]{0L, 600L, 1500L, 3000L};
        for (int i = 0; i < 4; ++i) {
            handler.postDelayed((Runnable)new AnonymousClass25(classLoader), jArr[i]);
        }
    }

    public static void attachExistingListsNow(ClassLoader classLoader) {
        try {
            ArrayList<Object> arrayList = new ArrayList<Object>();
            for (Activity activity : ChatCardInjector.allActivities()) {
                if (activity == null || activity.isFinishing()) continue;
                try {
                    View view = activity.getWindow() == null ? null : activity.getWindow().getDecorView();
                    if (view == null) continue;
                    ChatCardInjector.collectRecyclerViews(view, arrayList);
                }
                catch (Throwable throwable) {}
            }
            boolean z = false;
            for (Object e : arrayList) {
                try {
                    Object objCallMethod = XposedHelpers.callMethod(e, (String)"getAdapter", (Object[])new Object[0]);
                    if (objCallMethod == null || sInjected.containsKey(objCallMethod) || !ChatCardInjector.isChatListAdapter(objCallMethod, classLoader)) continue;
                    ChatCardInjector.injectAdapter(((View)e).getContext(), objCallMethod, classLoader, (ViewGroup)e);
                    z = true;
                }
                catch (Throwable thRethrow) {
                    Log.w((String)("ChatCard \u8865\u9f50\u73b0\u5b58\u4f1a\u8bdd\u5217\u8868\u5931\u8d25: " + thRethrow));
                }
            }
            if (z) {
                Log.i((String)"ChatCard \u665a\u6ce8\u5165\u8865\u9f50\uff1a\u5df2\u8865\u6302\u73b0\u5b58\u4f1a\u8bdd\u5217\u8868\u9002\u914d\u5668\u5e76\u5237\u65b0");
                ChatCardInjector.refreshChatList();
            }
        }
        catch (Throwable th2) {
            Log.w((String)("ChatCard \u665a\u6ce8\u5165\u8865\u9f50\u5f02\u5e38: " + th2));
        }
    }

    public static void refreshChatList() {
        ChatCardInjector.refreshMainList();
    }

    private static void refreshMainList() {
        try {
            ArrayList<Object> arrayList = new ArrayList<Object>();
            LinkedHashSet<Activity> linkedHashSet = new LinkedHashSet<Activity>();
            for (Activity activity : ChatCardInjector.allActivities()) {
                if (activity == null || activity.isFinishing()) continue;
                linkedHashSet.add(activity);
            }
            for (Activity activity2 : linkedHashSet) {
                try {
                    View decorView = activity2.getWindow() == null ? null : activity2.getWindow().getDecorView();
                    if (decorView == null) continue;
                    ChatCardInjector.collectRecyclerViews(decorView, arrayList);
                }
                catch (Throwable throwable) {}
            }
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                ChatCardInjector.notifyAdapter(it.next());
            }
            if (arrayList.isEmpty()) {
                return;
            }
            new Handler(Looper.getMainLooper()).postDelayed((Runnable)new AnonymousClass26(new ArrayList<Object>(arrayList)), 300L);
        }
        catch (Throwable thRethrow) {
            Log.w((String)("ChatCard \u5237\u65b0\u5217\u8868\u5931\u8d25: " + thRethrow));
        }
    }

    public static void notifyAdapter(Object obj) {
        try {
            Object objCallMethod = XposedHelpers.callMethod((Object)obj, (String)"getAdapter", (Object[])new Object[0]);
            if (objCallMethod == null) {
                return;
            }
            if (ChatCardInjector.isTarget(objCallMethod) || ChatCardInjector.isChatListAdapter(objCallMethod, objCallMethod.getClass().getClassLoader())) {
                XposedHelpers.callMethod((Object)objCallMethod, (String)"notifyDataSetChanged", (Object[])new Object[0]);
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    /** 供其它 hook 取任意一个存活 Activity */
    public static android.app.Activity anyActivityPublic() {
        try {
            java.util.List<android.app.Activity> list = allActivities();
            if (list != null) {
                for (android.app.Activity a : list) {
                    if (a != null && !a.isFinishing()) {
                        return a;
                    }
                }
            }
        } catch (Throwable ignored) {}
        return null;
    }

    private static List<Activity> allActivities() {
        ArrayList<Activity> arrayList = new ArrayList<Activity>();
        try {
            Method method = Class.forName("android.app.ActivityThread").getMethod("currentActivityThread", new Class[0]);
            method.setAccessible(true);
            Object objInvoke = method.invoke(null, new Object[0]);
            if (objInvoke == null) {
                return arrayList;
            }
            Object objectField = XposedHelpers.getObjectField((Object)objInvoke, (String)"mActivities");
            if (objectField instanceof Map) {
                Iterator it = ((Map)objectField).values().iterator();
                while (it.hasNext()) {
                    try {
                        Object objectField2 = XposedHelpers.getObjectField(it.next(), (String)"activity");
                        if (!(objectField2 instanceof Activity)) continue;
                        arrayList.add((Activity)objectField2);
                    }
                    catch (Throwable throwable) {}
                }
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        return arrayList;
    }

    private static boolean isRecyclerView(View view) {
        try {
            Class<?> cls = Class.forName(RV_CLASS, false, view.getContext().getClassLoader());
            if (cls != null) {
                return cls.isAssignableFrom(view.getClass());
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        return RV_CLASS.equals(view.getClass().getName());
    }

    private static void collectRecyclerViews(View view, List<Object> list) {
        if (view == null) {
            return;
        }
        if (ChatCardInjector.isRecyclerView(view)) {
            list.add(view);
        }
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup)view;
            for (int i = 0; i < viewGroup.getChildCount(); ++i) {
                ChatCardInjector.collectRecyclerViews(viewGroup.getChildAt(i), list);
            }
        }
    }

    private static View[] findTextViews(View view) {
        View[] viewArr = new View[]{null, null};
        ChatCardInjector.walkViews(view, new AnonymousClass27(viewArr));
        return viewArr;
    }

    private static View findTimeView(View view) {
        View[] viewArr = new View[]{null};
        ChatCardInjector.walkViews(view, new AnonymousClass28(viewArr));
        return viewArr[0];
    }

    private static String formatMailTime(Context context) {
        long jMailLastTime = Config.mailLastTime((Context)context);
        if (jMailLastTime <= 0L) {
            jMailLastTime = System.currentTimeMillis();
        }
        try {
            return new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date(jMailLastTime));
        }
        catch (Throwable unused) {
            return "";
        }
    }

    private static View findBadgeView(View view) {
        View[] viewArr = new View[]{null};
        ChatCardInjector.walkViews(view, new AnonymousClass29(viewArr));
        View view2 = viewArr[0];
        if (view2 != null) {
            return view2;
        }
        View[] viewArr2 = new View[]{null};
        ChatCardInjector.walkViews(view, new AnonymousClass30(viewArr2));
        return viewArr2[0];
    }

    private static void dumpViewTree(View view) {
        ChatCardInjector.dumpTree(view, 0, 4);
    }

    private static void dumpTree(View view, int i, int i2) {
        if (view == null || i > i2) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (int i3 = 0; i3 < i; ++i3) {
            sb.append("  ");
        }
        sb.append(view.getClass().getSimpleName());
        sb.append(" vis=");
        sb.append(view.getVisibility());
        if (view instanceof TextView) {
            sb.append(" text=");
            sb.append(((TextView)view).getText());
        }
        Log.i((String)("ChatCard tree " + sb));
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup)view;
            for (int i4 = 0; i4 < viewGroup.getChildCount(); ++i4) {
                ChatCardInjector.dumpTree(viewGroup.getChildAt(i4), i + 1, i2);
            }
        }
    }

    private static <T> T readViewField(Object obj, Class<?> cls, Class<T> cls2, String ... strArr) {
        while (cls != null && cls != Object.class) {
            for (String str : strArr) {
                try {
                    Field declaredField = cls.getDeclaredField(str);
                    declaredField.setAccessible(true);
                    Object obj2 = declaredField.get(obj);
                    if (obj2 == null || !cls2.isInstance(obj2)) continue;
                    return cls2.cast(obj2);
                }
                catch (Throwable throwable) {
                    // empty catch block
                }
            }
            cls = cls.getSuperclass();
        }
        return null;
    }

    private static Object readFieldAny(Object obj, Class<?> cls, String ... strArr) {
        while (cls != null && cls != Object.class) {
            for (String str : strArr) {
                try {
                    Field declaredField = cls.getDeclaredField(str);
                    declaredField.setAccessible(true);
                    Object obj2 = declaredField.get(obj);
                    if (obj2 == null) continue;
                    return obj2;
                }
                catch (Throwable throwable) {
                    // empty catch block
                }
            }
            cls = cls.getSuperclass();
        }
        return null;
    }

    private static void walkViews(View view, Consumer<View> consumer) {
        consumer.accept(view);
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup)view;
            for (int i = 0; i < viewGroup.getChildCount(); ++i) {
                ChatCardInjector.walkViews(viewGroup.getChildAt(i), consumer);
            }
        }
    }

    private static Method findMethod(Class<?> cls, String str, Class<?> ... clsArr) {
        while (cls != null && cls != Object.class) {
            try {
                return cls.getDeclaredMethod(str, clsArr);
            }
            catch (Throwable unused) {
                cls = cls.getSuperclass();
            }
        }
        return null;
    }

    public static boolean isTarget(Object obj) {
        return obj != null && sInjected.containsKey(obj) && ChatCardInjector.mailEnabled();
    }

    private static boolean mailEnabled() {
        Boolean bool = sMailEnabledCache;
        if (bool != null) {
            return bool;
        }
        Context context = sAppCtx;
        boolean z = context == null || Config.mailEnabled((Context)context);
        sMailEnabledCache = z;
        return z;
    }

    public static void setMailEnabled(boolean z) {
        sMailEnabledCache = z;
        ChatCardInjector.refreshChatList();
    }

    private static int hookInHierarchy(Class<?> cls, String str, XC_MethodHook xC_MethodHook) {
        int i = 0;
        while (cls != null && cls != Object.class) {
            try {
                int size = XposedBridge.hookAllMethods(cls, (String)str, (XC_MethodHook)xC_MethodHook).size();
                i += size;
                if (size > 0) break;
                cls = cls.getSuperclass();
            }
            catch (Throwable throwable) {}
        }
        return i;
    }

    private static int hookAll(Class<?> cls, String str, XC_MethodHook xC_MethodHook) {
        try {
            return XposedBridge.hookAllMethods(cls, (String)str, (XC_MethodHook)xC_MethodHook).size();
        }
        catch (Throwable unused) {
            return 0;
        }
    }

    private static int hookItemBySignature(Class<?> cls, XC_MethodHook xC_MethodHook) {
        try {
            Class<?> cls2 = Class.forName("com.tencent.qqnt.chats.core.adapter.itemdata.BaseChatItem", false, cls.getClassLoader());
            while (cls != null && cls != Object.class) {
                int i = 0;
                for (Method method : cls.getDeclaredMethods()) {
                    if (com.tencent.qqlitepro.util.Log$$ExternalSyntheticBackport0.m((Method)method) != 1 || method.getParameterTypes()[0] != Integer.TYPE || !cls2.isAssignableFrom(method.getReturnType())) continue;
                    try {
                        XposedBridge.hookMethod((Member)method, (XC_MethodHook)xC_MethodHook);
                        ++i;
                    }
                    catch (Throwable throwable) {
                        // empty catch block
                    }
                }
                if (i > 0) {
                    return i;
                }
                cls = cls.getSuperclass();
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        return 0;
    }

    public static Integer lastIntArg(XC_MethodHook.MethodHookParam methodHookParam) {
        for (int length = methodHookParam.args.length - 1; length >= 0; --length) {
            long jLongValue;
            Object obj = methodHookParam.args[length];
            if (obj instanceof Integer) {
                return (Integer)obj;
            }
            if (!(obj instanceof Long) || (jLongValue = ((Long)obj).longValue()) < Integer.MIN_VALUE || jLongValue > Integer.MAX_VALUE) continue;
            return (int)jLongValue;
        }
        return null;
    }

    private static int dp(Context context, int i) {
        return Math.round(context.getResources().getDisplayMetrics().density * (float)i);
    }

    private static void applyAvatarImage(View view) {
        View viewFindAvatarView = ChatCardInjector.findAvatarView(view);
        if (viewFindAvatarView == null) {
            Log.w((String)"ChatCard \u672a\u627e\u5230\u5934\u50cf\u63a7\u4ef6");
        } else {
            new Thread((Runnable)new AnonymousClass31(view.getContext(), viewFindAvatarView), "AntiRecall-MailAvatar").start();
        }
    }

    private static View findAvatarView(View view) {
        View[] viewArr = new View[]{null};
        ChatCardInjector.walkViews(view, new AnonymousClass32(viewArr));
        return viewArr[0];
    }

    public static void setBitmapIntoAvatar(View view, Bitmap bitmap) {
        try {
            Method methodFindMethod = ChatCardInjector.findMethod(view.getClass(), "setBitmap", Bitmap.class);
            methodFindMethod.setAccessible(true);
            methodFindMethod.invoke((Object)view, bitmap);
            Log.i((String)"ChatCard \u5934\u50cf\u5df2\u5199\u5165 setBitmap");
        }
        catch (Throwable unused) {
            if (view instanceof ImageView) {
                ((ImageView)view).setImageBitmap(bitmap);
                Log.i((String)"ChatCard \u5934\u50cf\u5df2\u5199\u5165 ImageView");
                return;
            }
            ImageView[] imageViewArr = new ImageView[]{null};
            ChatCardInjector.walkViews(view, new AnonymousClass33(imageViewArr));
            ImageView imageView = imageViewArr[0];
            if (imageView != null) {
                imageView.setImageBitmap(bitmap);
                Log.i((String)("ChatCard \u5934\u50cf\u5df2\u5199\u5165\u5185\u90e8 ImageView: " + imageViewArr[0].getClass().getName()));
                return;
            }
            Log.w((String)"ChatCard \u5934\u50cf\u63a7\u4ef6\u5185\u90e8\u65e0 ImageView\uff0c\u65e0\u6cd5\u5199\u5165\u56fe\u7247");
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static Bitmap loadAvatarBitmap(Context context) {
        File file = new File(context.getFilesDir(), "mail_avatar.png");
        try {
            Bitmap bitmapDecodeFile;
            if (file.exists() && (bitmapDecodeFile = BitmapFactory.decodeFile((String)file.getAbsolutePath())) != null) {
                return bitmapDecodeFile;
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        try {
        HttpURLConnection httpURLConnection = (HttpURLConnection)new URL(AVATAR_URL).openConnection();
        httpURLConnection.setConnectTimeout(10000);
        httpURLConnection.setReadTimeout(15000);
        httpURLConnection.setInstanceFollowRedirects(true);
        httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Android)");
        httpURLConnection.setRequestProperty("Referer", "https://www.baidu.com/");
        httpURLConnection.connect();
        InputStream inputStream = httpURLConnection.getInputStream();
        try {
            Bitmap bitmapDecodeStream = BitmapFactory.decodeStream((InputStream)inputStream);
            if (bitmapDecodeStream != null) {
                Log.i((String)("ChatCard \u5934\u50cf\u4e0b\u8f7d\u6210\u529f: " + bitmapDecodeStream.getWidth() + "x" + bitmapDecodeStream.getHeight()));
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    bitmapDecodeStream.compress(Bitmap.CompressFormat.PNG, 100, (OutputStream)fileOutputStream);
                    fileOutputStream.close();
                }
                catch (Throwable throwable) {}
            } else {
                Log.w((String)"ChatCard \u5934\u50cf\u4e0b\u8f7d\u6210\u529f\u4f46\u89e3\u7801\u4e3a\u7a7a");
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                }
                catch (Throwable throwable) {
                    // empty catch block
                }
            }
            return bitmapDecodeStream;
        }
        catch (Throwable thRethrow) {
            Bitmap bitmap;
            block22: {
                try {
                    Log.w((String)("ChatCard \u5934\u50cf\u4e0b\u8f7d\u5931\u8d25: " + thRethrow));
                    bitmap = null;
                    if (inputStream == null) break block22;
                }
                catch (Throwable throwable) {
                    try {
                        if (inputStream != null) {
                            try {
                                inputStream.close();
                            }
                            catch (Throwable throwable2) {
                                // empty catch block
                            }
                        }
                        throw throwable;
                    }
                    catch (Throwable th2) {
                        Log.w((String)("ChatCard \u5934\u50cf\u4e0b\u8f7d\u5931\u8d25: " + th2));
                        return null;
                    }
                }
                try {
                    inputStream.close();
                }
                catch (Throwable throwable) {
                    // empty catch block
                }
            }
            return bitmap;
        }
        } catch (Throwable throwable2) {
            // empty catch block
        }
        return null;
    }

    public static Bitmap toCircle(Bitmap bitmap) {
        try {
            int iMin = Math.min(bitmap.getWidth(), bitmap.getHeight());
            Bitmap bitmapCreateBitmap = Bitmap.createBitmap((int)iMin, (int)iMin, (Bitmap.Config)Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmapCreateBitmap);
            Paint paint = new Paint(1);
            float f = iMin;
            float f2 = f / 2.0f;
            canvas.drawCircle(f2, f2, f2, paint);
            paint.setXfermode((Xfermode)new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, new Rect((bitmap.getWidth() - iMin) / 2, (bitmap.getHeight() - iMin) / 2, (bitmap.getWidth() + iMin) / 2, (bitmap.getHeight() + iMin) / 2), new RectF(0.0f, 0.0f, f, f), paint);
            return bitmapCreateBitmap;
        }
        catch (Throwable unused) {
            return bitmap;
        }
    }

    static {
        sInjected = Collections.synchronizedMap(new IdentityHashMap());
        sCards = new IdentityHashMap<Object, View>();
        sTempCards = new IdentityHashMap<Object, View>();
        sNativeMenuHooked = false;
        sPatched = false;
        sSlotCache = new IdentityHashMap<Object, SlotVal>();
        sBaseUnread = new IdentityHashMap<Object, Integer>();
        sLastSlots = new IdentityHashMap<Object, Integer>();
        sLastSlotRelayout = 0L;
        sMailEnabledCache = null;
        sCardMenuItems = Collections.newSetFromMap(new WeakHashMap());
    }

    static class AnonymousClass1
    extends XC_MethodHook {
        final ClassLoader val$cl;

        AnonymousClass1(ClassLoader classLoader) {
            this.val$cl = classLoader;
        }

        protected void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
            try {
                Object obj = methodHookParam.args[0];
                if (obj == null || sInjected.containsKey(obj) || !ChatCardInjector.isChatListAdapter(obj, this.val$cl)) {
                    return;
                }
                ChatCardInjector.injectAdapter(methodHookParam.thisObject instanceof ViewGroup ? ((ViewGroup)methodHookParam.thisObject).getContext() : null, obj, this.val$cl, (ViewGroup)methodHookParam.thisObject);
            }
            catch (Throwable th) {
                Log.w((String)("ChatCard setAdapter hook \u5f02\u5e38: " + th));
            }
        }
    }

    static class AnonymousClass2
    extends XC_MethodHook {
        AnonymousClass2() {
        }

        protected void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
            if (ChatCardInjector.isTarget(methodHookParam.thisObject)) {
                Object result = methodHookParam.getResult();
                int iLongValue = result instanceof Integer ? (Integer)result : (result instanceof Long ? (int)((Long)result).longValue() : 0);
                methodHookParam.setResult((Object)(iLongValue
                        + ChatCardInjector.extraCardCountFor(methodHookParam.thisObject)));
            }
        }
    }

    static class AnonymousClass3
    extends XC_MethodHook {
        AnonymousClass3() {
        }

        protected void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
            Integer numLastIntArg;
            if (ChatCardInjector.isTarget(methodHookParam.thisObject) && (numLastIntArg = ChatCardInjector.lastIntArg(methodHookParam)) != null) {
                int pos = numLastIntArg;
                if (pos == ChatCardInjector.cardSlot(methodHookParam.thisObject)) {
                    methodHookParam.setResult(Integer.valueOf(-10));
                } else if (ChatCardInjector.tempChatIndexAtPos(methodHookParam.thisObject, pos) >= 0) {
                    /* 临时卡：用宿主原生 viewType，让宿主 inflate 自己的 ItemRecentContact 布局 */
                    methodHookParam.setResult(Integer.valueOf(ChatCardInjector.HOST_ITEM_TYPE));
                }
            }
        }
    }

    static class AnonymousClass4
    extends XC_MethodHook {
        AnonymousClass4() {
        }

        protected void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
            Integer numLastIntArg;
            if (ChatCardInjector.isTarget(methodHookParam.thisObject) && (numLastIntArg = ChatCardInjector.lastIntArg(methodHookParam)) != null && methodHookParam.args.length != 0 && methodHookParam.args[0] instanceof Integer) {
                /* 临时卡：直接返回宿主 RecentContactChatItem，由宿主原生渲染（样式 100% 一致） */
                int tIdx = ChatCardInjector.tempChatIndexAtPos(methodHookParam.thisObject, numLastIntArg);
                if (tIdx >= 0) {
                    java.util.List<Object[]> snap = com.tencent.qqlitepro.http.QqKernel.tempChatSnapshot();
                    if (snap != null && tIdx < snap.size()) {
                        String uidT = (String) snap.get(tIdx)[0];
                        Object okItem = com.tencent.qqlitepro.http.QqKernel.tempChatItemFor(uidT);
                        if (okItem != null) {
                            methodHookParam.setResult(okItem);
                            return;
                        }
                    }
                }
                /* 时间穿插版索引映射：pos(显示位) -> 宿主真实 list 下标 */
                int mapped = ChatCardInjector.hostIndexForPos(methodHookParam.thisObject, numLastIntArg);
                int iCurrentListSize = ChatCardInjector.currentListSize(methodHookParam.thisObject);
                if (iCurrentListSize > 0) {
                    mapped = Math.min(mapped, iCurrentListSize - 1);
                }
                methodHookParam.args[0] = Math.max(0, mapped);
            }
        }
    }

    static class AnonymousClass5
    extends XC_MethodHook {
        AnonymousClass5() {
        }

        protected void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
            Integer numLastIntArg;
            if (ChatCardInjector.isTarget(methodHookParam.thisObject) && (numLastIntArg = ChatCardInjector.lastIntArg(methodHookParam)) != null) {
                boolean isTemp;
                int pos = numLastIntArg;
                boolean isMail = pos == ChatCardInjector.cardSlot(methodHookParam.thisObject);
                int tempIdx = ChatCardInjector.tempChatIndexAtPos(methodHookParam.thisObject, pos);
                boolean isTempCard = tempIdx >= 0;
                if (!isMail && !isTempCard) {
                    return;
                }
                try {
                    View itemView = (View)XposedHelpers.getObjectField((Object)methodHookParam.args[0], (String)"itemView");
                    if (isTempCard) {
                        ChatCardInjector.bindTempChatByHostBinding(methodHookParam.args[0], tempIdx);
                    } else {
                        /* 顺手缓存一个宿主真实 item 作为克隆原型（供临时卡构建用） */
                        try {
                            Object it0 = ChatCardInjector.currentListItemAt(
                                    methodHookParam.thisObject, numLastIntArg.intValue());
                            if (it0 != null
                                    && it0.getClass().getName().endsWith("RecentContactChatItem")) {
                                sHostItemProto = it0;
                            }
                        } catch (Throwable ignored) {}
                        ChatCardInjector.bindCard(itemView);
                    }
                }
                catch (Throwable throwable) {
                    // empty catch block
                }
                methodHookParam.setResult(null);
            }
        }
    }

    static class AnonymousClass6
    extends XC_MethodHook {
        final ClassLoader val$cl;

        AnonymousClass6(ClassLoader classLoader) {
            this.val$cl = classLoader;
        }

        protected void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
            Integer typeArg = ChatCardInjector.lastIntArg(methodHookParam);
            if (ChatCardInjector.isTarget(methodHookParam.thisObject) && typeArg != null) {
                boolean isTempCard;
                int vtype = typeArg;
                boolean isMailCard = vtype == -10;
                boolean bl = isTempCard = vtype == -11;
                if (!isMailCard && !isTempCard) {
                    return;
                }
                ViewGroup viewGroup = methodHookParam.args.length <= 0 || !(methodHookParam.args[0] instanceof ViewGroup) ? null : (ViewGroup)methodHookParam.args[0];
                try {
                    Object objNewHeaderHolder;
                    Map<Object, View> cache = isTempCard ? sTempCards : sCards;
                    View viewBuildCard = cache.get(methodHookParam.thisObject);
                    if (viewBuildCard == null || viewBuildCard.getParent() != null) {
                        Context context;
                        Context context2 = context = viewGroup != null ? viewGroup.getContext() : sAppCtx;
                        if (context == null) {
                            return;
                        }
                        viewBuildCard = ChatCardInjector.buildCard(context, methodHookParam.thisObject, this.val$cl, viewGroup);
                        cache.put(methodHookParam.thisObject, viewBuildCard);
                    }
                    if ((objNewHeaderHolder = ChatCardInjector.newHeaderHolder(methodHookParam.thisObject, viewBuildCard)) != null) {
                        methodHookParam.setResult(objNewHeaderHolder);
                    }
                }
                catch (Throwable th) {
                    Log.w((String)("ChatCard \u521b\u5efa\u5361\u7247 holder \u5931\u8d25: " + th));
                }
            }
        }
    }

    static class AnonymousClass7
    extends XC_MethodHook {
        AnonymousClass7() {
        }

        protected void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
            if (ChatCardInjector.isTarget(methodHookParam.thisObject)) {
                int iCardSlot = ChatCardInjector.cardSlot(methodHookParam.thisObject);
                if (methodHookParam.args.length <= 0 || !(methodHookParam.args[0] instanceof Integer) || (Integer)methodHookParam.args[0] < iCardSlot) {
                    return;
                }
                methodHookParam.args[0] = (Integer)methodHookParam.args[0] + 1;
            }
        }

        protected void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
            ChatCardInjector.ensureCardSlot(methodHookParam.thisObject);
        }
    }

    static class AnonymousClass8
    extends XC_MethodHook {
        AnonymousClass8() {
        }

        protected void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
            if (ChatCardInjector.isTarget(methodHookParam.thisObject)) {
                int iCardSlot = ChatCardInjector.cardSlot(methodHookParam.thisObject);
                if (methodHookParam.args.length <= 0 || !(methodHookParam.args[0] instanceof Integer) || (Integer)methodHookParam.args[0] < iCardSlot) {
                    return;
                }
                methodHookParam.args[0] = (Integer)methodHookParam.args[0] + 1;
            }
        }

        protected void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
            ChatCardInjector.ensureCardSlot(methodHookParam.thisObject);
        }
    }

    static class AnonymousClass9
    extends XC_MethodHook {
        AnonymousClass9() {
        }

        protected void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
            if (ChatCardInjector.isTarget(methodHookParam.thisObject)) {
                int iCardSlot = ChatCardInjector.cardSlot(methodHookParam.thisObject);
                for (int i = 0; i < methodHookParam.args.length && i < 2; ++i) {
                    if (!(methodHookParam.args[i] instanceof Integer) || (Integer)methodHookParam.args[i] < iCardSlot) continue;
                    methodHookParam.args[i] = (Integer)methodHookParam.args[i] + 1;
                }
            }
        }

        protected void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
            ChatCardInjector.ensureCardSlot(methodHookParam.thisObject);
        }
    }

    static class AnonymousClass5B
    extends XC_MethodHook {
        AnonymousClass5B() {
        }

        protected void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
            try {
                if (methodHookParam.args == null || methodHookParam.args.length < 2) {
                    return;
                }
                Object holder = methodHookParam.args[0];
                Object item = methodHookParam.args[1];
                if (holder == null || item == null) {
                    return;
                }
                View itemView = (View)XposedHelpers.getObjectField((Object)holder, (String)"itemView");
                if (itemView == null) {
                    return;
                }
                String peerUid = QqKernel.tempChatPeerUidOfItem((Object)item);
                if (peerUid == null || peerUid.isEmpty()) {
                    return;
                }
                QqKernel.attachTempChatLongPress((View)itemView, (String)peerUid);
            }
            catch (Throwable throwable) {
                // empty catch block
            }
        }
    }

    static class AnonymousClass10
    extends XC_MethodHook {
        AnonymousClass10() {
        }

        protected void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
            if (ChatCardInjector.isTarget(methodHookParam.thisObject)) {
                methodHookParam.setResult((Object)false);
            }
        }
    }

    private static volatile int sGetItemProbe = 0;
    private static volatile int sVTProbe = 0;
    private static volatile int sBindProbe = 0;
    private static volatile int sTimeProbe = 0;
    private static volatile int sBgProbe = 0;
    /** 开关转调原生 onClick 的防抖时间戳 */
    private static volatile long sLastSwitchInvoke = 0L;
    /** 宿主真实会话 item 原型（用于克隆） */
    private static volatile Object sHostItemProto = null;
    private static volatile boolean sPauseDismissHooked = false;

    static class GetItemOverride
    extends XC_MethodHook {
        GetItemOverride() {
        }

        protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) {
            Object override = sBindOverride;
            if (override != null) {
                param.setResult(override);
                return;
            }
            /* 临时会话卡：返回宿主 RecentContactChatItem，由宿主原生渲染（样式 100% 一致） */
            try {
                if (!ChatCardInjector.isTarget(param.thisObject)) {
                    return;
                }
                Integer pos = ChatCardInjector.lastIntArg(param);
                if (pos == null) {
                    return;
                }
                int tIdx = ChatCardInjector.tempChatIndexAtPos(param.thisObject, pos.intValue());
                if (tIdx < 0) {
                    return;
                }
                java.util.List<Object[]> snap = com.tencent.qqlitepro.http.QqKernel.tempChatSnapshot();
                if (snap == null || tIdx >= snap.size()) {
                    return;
                }
                /* 占位：返回"宿主真实列表首项"（一定合法），实际渲染由 onBindViewHolder 覆盖 */
                Object cur = XposedHelpers.callMethod(param.thisObject, "getCurrentList", new Object[0]);
                if (cur instanceof List && !((List) cur).isEmpty()) {
                    param.setResult(((List) cur).get(0));
                }
            } catch (Throwable ignored) {}
        }
    }

    private static final class SlotVal {
        final long ts;
        final int unread;

        SlotVal(long j, int i) {
            this.ts = j;
            this.unread = i;
        }
    }

    static class AnonymousClass11
    implements View.OnClickListener {
        AnonymousClass11() {
        }

        public void onClick(View view) {
            try {
                SettingsInjector.openMailDialog((Context)view.getContext());
            }
            catch (Throwable throwable) {
                // empty catch block
            }
        }
    }

    static class AnonymousClass12
    implements View.OnLongClickListener {
        AnonymousClass12() {
        }

        public boolean onLongClick(View view) {
            try {
                ChatCardInjector.showNativeMailMenu(view.getContext());
                return true;
            }
            catch (Throwable thRethrow) {
                Log.w((String)("ChatCard \u957f\u6309\u83dc\u5355\u5f39\u51fa\u5931\u8d25: " + thRethrow));
                return false;
            }
        }
    }

    static class AnonymousClass13
    implements View.OnClickListener {
        AnonymousClass13() {
        }

        public void onClick(View view) {
            try {
                SettingsInjector.openMailDialog((Context)view.getContext());
            }
            catch (Throwable throwable) {
                // empty catch block
            }
        }
    }

    static class AnonymousClass14
    extends XC_MethodHook {
        AnonymousClass14() {
        }

        protected void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
            sActiveMenu = methodHookParam.thisObject;
            /* 菜单打开即登记目标 uid：从 items 解析 RecentContactChatItem（字段 a）。
             * 这样后续 onClick/onCheckedChanged 的 menuTargetUid() 立即可用、逐菜单独立。 */
            try {
                Object info = ChatCardInjector.menuTargetInfo();
                if (info != null) {
                    String uid = (String) XposedHelpers.getObjectField(info, "peerUid");
                    String nick = (String) XposedHelpers.getObjectField(info, "peerName");
                    int chatType = XposedHelpers.getIntField(info, "chatType");
                    long uin = XposedHelpers.getLongField(info, "peerUin");
                    if (uid != null && !uid.isEmpty()) {
                        com.tencent.qqlitepro.http.QqKernel.registerMenuTarget(uid, nick, chatType, uin);
                        Log.i("菜单打开登记目标 uid=" + uid);
                    }
                }
            } catch (Throwable th) {
                Log.w("菜单登记失败: " + th);
            }
        }
    }

    static class AnonymousClass15
    extends XC_MethodHook {
        AnonymousClass15() {
        }

        protected void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
            if (sActiveMenu == methodHookParam.thisObject) {
                sActiveMenu = null;
            }
            /* 菜单关闭即清空登记目标 + 复位菜单类型，
             * 避免下一次正常会话/邮箱卡菜单沿用 "temp" 标记（这是"设置牵连其它会话"的根因） */
            com.tencent.qqlitepro.http.QqKernel.clearMenuTarget();
            ChatCardInjector.sMenuKind = "normal";
            ChatCardInjector.sCardMenuItems.clear();
        }
    }

    static class AnonymousClass16
    extends XC_MethodHook {
        final Context val$ctx;

        AnonymousClass16(Context context) {
            this.val$ctx = context;
        }

        protected void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
            /* 只拦截模块自己的邮箱卡菜单项；临时卡走宿主原生逻辑（item 已在菜单里） */
            if (ChatCardInjector.mailMenuItem(methodHookParam)) {
                if (ChatCardInjector.isTempMenuActive()) {
                    /* 临时会话删除：走 forgetTempChat */
                    try {
                        String uid = com.tencent.qqlitepro.http.QqKernel.menuTargetUid();
                        if (uid != null && !uid.isEmpty()) {
                            com.tencent.qqlitepro.http.QqKernel.forgetTempChat(uid);
                        }
                    }
                    catch (Throwable throwable) {
                        // empty catch block
                    }
                } else {
                    try {
                        Config.resetMail((Context)(sAppCtx != null ? sAppCtx : this.val$ctx));
                        QqMailManager.stopPolling();
                        ChatCardInjector.refreshChatList();
                    }
                    catch (Throwable throwable) {
                        // empty catch block
                    }
                }
                ChatCardInjector.dismissActiveMenu();
                methodHookParam.setResult(null);
            }
        }
    }

    static class AnonymousClass17
    extends XC_MethodHook {
        final Context val$ctx;

        AnonymousClass17(Context context) {
            this.val$ctx = context;
        }

        protected void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
            Context context;
            int identifier;
            if (ChatCardInjector.mailMenuItem(methodHookParam) && (identifier = (context = sAppCtx != null ? sAppCtx : this.val$ctx).getResources().getIdentifier(Config.mailPinned((Context)context) ? "chat_cancel_top" : "chat_set_top", "string", context.getPackageName())) != 0) {
                methodHookParam.setResult((Object)identifier);
            }
        }
    }

    static class AnonymousClass18
    extends XC_MethodHook {
        AnonymousClass18() {
        }

        protected void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
            if (ChatCardInjector.mailMenuItem(methodHookParam)) {
                try {
                    Object objCallMethod = XposedHelpers.callMethod((Object)methodHookParam.thisObject, (String)"getSwitch", (Object[])new Object[0]);
                    if (objCallMethod != null) {
                        XposedHelpers.callMethod((Object)objCallMethod, (String)"toggle", (Object[])new Object[0]);
                    }
                }
                catch (Throwable throwable) {
                    // empty catch block
                }
                methodHookParam.setResult(null);
            }
        }
    }

    static class AnonymousClass19
    extends XC_MethodHook {
        final Context val$ctx;

        AnonymousClass19(Context context) {
            this.val$ctx = context;
        }

        protected void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
            Context context;
            int identifier;
            if (ChatCardInjector.mailMenuItem(methodHookParam) && (identifier = (context = sAppCtx != null ? sAppCtx : this.val$ctx).getResources().getIdentifier(Config.mailDnd((Context)context) ? "disable_disturb" : "enable_disturb", "string", context.getPackageName())) != 0) {
                methodHookParam.setResult((Object)identifier);
            }
        }
    }

    static class AnonymousClass20
    extends XC_MethodHook {
        AnonymousClass20() {
        }

        protected void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
            if (ChatCardInjector.mailMenuItem(methodHookParam)) {
                try {
                    Object objCallMethod = XposedHelpers.callMethod((Object)methodHookParam.thisObject, (String)"getSwitch", (Object[])new Object[0]);
                    if (objCallMethod != null) {
                        XposedHelpers.callMethod((Object)objCallMethod, (String)"toggle", (Object[])new Object[0]);
                    }
                }
                catch (Throwable throwable) {
                    // empty catch block
                }
                methodHookParam.setResult(null);
                return;
            }
            /* 临时会话：按登记 uid 独立免打扰（同样双校验） */
            if (ChatCardInjector.isTempMenuActive()) {
                try {
                    String uidD = com.tencent.qqlitepro.http.QqKernel.menuTargetUid();
                    if (uidD != null && !uidD.isEmpty()
                            && com.tencent.qqlitepro.http.QqKernel.isTempChat(uidD)) {
                        boolean nowD = !com.tencent.qqlitepro.http.QqKernel.isTempChatDnd(uidD);
                        com.tencent.qqlitepro.http.QqKernel.setTempChatDnd(uidD, nowD);
                        android.widget.Toast.makeText(sAppCtx, nowD ? "已开启免打扰" : "已关闭免打扰",
                                android.widget.Toast.LENGTH_SHORT).show();
                        ChatCardInjector.refreshChatList();
                    }
                } catch (Throwable t) {
                    Log.w("临时会话: 免打扰失败 " + t);
                }
                ChatCardInjector.dismissActiveMenu();
                methodHookParam.setResult(null);
            }
        }
    }

    static class AnonymousClass21
    extends XC_MethodHook {
        final Class val$disturbCls;
        final Class val$topCls;

        AnonymousClass21(Class cls, Class cls2) {
            this.val$topCls = cls;
            this.val$disturbCls = cls2;
        }

        protected void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
            Object obj;
            /* 仅模块邮箱卡需要强制显示开关；宿主原生会话（含临时会话）走原生判定 */
            if (ChatCardInjector.mailMenuItem(methodHookParam) && (obj = methodHookParam.thisObject) != null
                    && (this.val$topCls.isInstance(obj) || this.val$disturbCls.isInstance(obj))) {
                methodHookParam.setResult((Object) true);
            }
        }
    }

    static class AnonymousClass22
    extends XC_MethodHook {
        final Context val$ctx;
        final Class val$disturbCls;
        final Class val$topCls;

        AnonymousClass22(Class cls, Class cls2, Context context) {
            this.val$topCls = cls;
            this.val$disturbCls = cls2;
            this.val$ctx = context;
        }

        protected void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
            Object obj;
            /* 仅模块邮箱卡：用模块配置驱动开关初值；宿主原生会话（含临时会话）完全放行 */
            if (ChatCardInjector.mailMenuItem(methodHookParam) && (obj = methodHookParam.thisObject) != null && (this.val$topCls.isInstance(obj) || this.val$disturbCls.isInstance(obj))) {
                Context context = sAppCtx != null ? sAppCtx : this.val$ctx;
                boolean zMailPinned = this.val$topCls.isInstance(obj) ? Config.mailPinned((Context)context) : Config.mailDnd((Context)context);
                Object obj2 = methodHookParam.args[0];
                if (obj2 != null) {
                    try {
                        XposedHelpers.callMethod((Object)obj2, (String)"setOnCheckedChangeListener", (Object[])new Object[]{null});
                        XposedHelpers.callMethod((Object)obj2, (String)"setChecked", (Object[])new Object[]{zMailPinned});
                        XposedHelpers.callMethod((Object)obj2, (String)"setOnCheckedChangeListener", (Object[])new Object[]{obj});
                    }
                    catch (Throwable throwable) {
                        // empty catch block
                    }
                }
                methodHookParam.setResult(null);
            }
        }
    }

    static class AnonymousClass23
    extends XC_MethodHook {
        final Context val$ctx;
        final Class val$disturbCls;
        final Class val$topCls;

        AnonymousClass23(Class cls, Class cls2, Context context) {
            this.val$topCls = cls;
            this.val$disturbCls = cls2;
            this.val$ctx = context;
        }

        protected void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
            Object obj;
            Object self = methodHookParam.thisObject;
            if (self == null) {
                return;
            }
            boolean isTop = this.val$topCls.isInstance(self);
            boolean isDisturb = this.val$disturbCls.isInstance(self);
            if (!isTop && !isDisturb) {
                return;
            }
            /* 模块邮箱卡：走模块自己的全局配置 */
            if (ChatCardInjector.mailMenuItem(methodHookParam)) {
                boolean z = methodHookParam.args.length > 1 && Boolean.TRUE.equals(methodHookParam.args[1]);
                Context context = sAppCtx != null ? sAppCtx : this.val$ctx;
                if (isTop) {
                    Config.setMailPinned(context, z);
                } else {
                    Config.setMailDnd(context, z);
                }
                ChatCardInjector.refreshChatList();
                methodHookParam.setResult(null);
                return;
            }
            /*
             * 宿主原生会话 / 临时会话：
             * 逆向 AbsItem.createView -> switch.setOnCheckedChangeListener(this)，
             * 而 AbsItem.onCheckedChanged 是空实现（SetTopItem/SetDisturbItem 也未重写）。
             * 真正生效逻辑在 onClick(View)：NetworkUtils -> setTop()/setDisturb() -> TipsUtils 提示。
             * 所以这里主动转调 onClick，让"设置中/设置成功/设置失败"提示与真实设置全部走宿主原生链路，
             * 同时保证每个会话用自己菜单里的 item（互不影响）。
             */
            /* 防抖 + 只在用户真实点击后触发（避免 updateSwitchStatus 程序化设置误触发） */
            long now = java.lang.System.currentTimeMillis();
            if (now - sLastSwitchInvoke < 600L) {
                return;
            }
            sLastSwitchInvoke = now;

            /*
             * 临时会话：模块自建的 SelectDialogFragment 里，点击开关只会走 onCheckedChanged
             * （宿主 AbsItem.onCheckedChanged 是空实现，不会触发 onClick），
             * 所以这里直接按登记的 peerUid 写入该会话自己的置顶/免打扰标志，并给出 Toast + 关闭菜单。
             */
            com.tencent.qqlitepro.util.Log.i("开关回调: kind=" + ChatCardInjector.sMenuKind
                    + " tempActive=" + ChatCardInjector.isTempMenuActive()
                    + " uid=" + com.tencent.qqlitepro.http.QqKernel.menuTargetUid()
                    + " isTop=" + isTop);
            if (ChatCardInjector.isTempMenuActive()) {
                String uidT = com.tencent.qqlitepro.http.QqKernel.menuTargetUid();
                if (uidT == null || uidT.isEmpty()
                        || !com.tencent.qqlitepro.http.QqKernel.isTempChat(uidT)) {
                    return;
                }
                boolean checked = methodHookParam.args.length > 1
                        && Boolean.TRUE.equals(methodHookParam.args[1]);
                Context ctxT = sAppCtx != null ? sAppCtx : this.val$ctx;
                try {
                    if (isTop) {
                        com.tencent.qqlitepro.http.QqKernel.setTempChatTop(uidT, checked);
                        android.widget.Toast.makeText(ctxT,
                                checked ? "已置顶" : "已取消置顶",
                                android.widget.Toast.LENGTH_SHORT).show();
                        com.tencent.qqlitepro.util.Log.i("临时会话: 菜单置顶 " + uidT + " -> " + checked);
                    } else {
                        com.tencent.qqlitepro.http.QqKernel.setTempChatDnd(uidT, checked);
                        android.widget.Toast.makeText(ctxT,
                                checked ? "已开启免打扰" : "已关闭免打扰",
                                android.widget.Toast.LENGTH_SHORT).show();
                        com.tencent.qqlitepro.util.Log.i("临时会话: 菜单免打扰 " + uidT + " -> " + checked);
                    }
                } catch (Throwable t) {
                    com.tencent.qqlitepro.util.Log.w("临时会话: 开关写入失败 " + t);
                }
                ChatCardInjector.refreshChatList();
                ChatCardInjector.dismissActiveMenu();
                methodHookParam.setResult(null);
                return;
            }

            final Object selfF = self;
            final View rowView = (View) readAnyField(self, "curButton");
            new android.os.Handler(android.os.Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    try {
                        java.lang.reflect.Method onClick = selfF.getClass()
                                .getMethod("onClick", View.class);
                        onClick.setAccessible(true);
                        onClick.invoke(selfF, rowView);
                        com.tencent.qqlitepro.util.Log.i("菜单: 已转调宿主原生 onClick（"
                                + selfF.getClass().getSimpleName() + "）");
                    } catch (Throwable t) {
                        com.tencent.qqlitepro.util.Log.w("菜单: 转调 onClick 失败 " + t);
                    }
                }
            });
            /* 不 setResult：保留开关自身的视觉切换 */
            
        }
    }

    static class AnonymousClass24
    extends XC_MethodHook {
        final Context val$ctx;

        AnonymousClass24(Context context) {
            this.val$ctx = context;
        }

        protected void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
            if (ChatCardInjector.mailMenuItem(methodHookParam)) {
                try {
                    Config.setMailInbox((Context)(sAppCtx != null ? sAppCtx : this.val$ctx), (String)"", (int)0, (String)"");
                    ChatCardInjector.refreshChatList();
                }
                catch (Throwable throwable) {
                    // empty catch block
                }
                ChatCardInjector.dismissActiveMenu();
                methodHookParam.setResult(null);
            }
        }
    }

    static class AnonymousClass25
    implements Runnable {
        final ClassLoader val$cl;

        AnonymousClass25(ClassLoader classLoader) {
            this.val$cl = classLoader;
        }

        @Override
        public void run() {
            ChatCardInjector.attachExistingListsNow(this.val$cl);
        }
    }

    static class AnonymousClass26
    implements Runnable {
        final List val$snapshot;

        AnonymousClass26(List list) {
            this.val$snapshot = list;
        }

        @Override
        public void run() {
            Iterator it = this.val$snapshot.iterator();
            while (it.hasNext()) {
                ChatCardInjector.notifyAdapter(it.next());
            }
        }
    }

    static class AnonymousClass27
    implements Consumer<View> {
        final View[] val$found;

        AnonymousClass27(View[] viewArr) {
            this.val$found = viewArr;
        }

        @Override
        public void accept(View view) {
            if (this.val$found[1] == null && view.getVisibility() == 0 && view.getClass().getName().contains("SingleLineTextView")) {
                View[] viewArr = this.val$found;
                if (viewArr[0] == null) {
                    viewArr[0] = view;
                } else if (viewArr[1] == null) {
                    viewArr[1] = view;
                }
            }
        }
    }

    static class AnonymousClass28
    implements Consumer<View> {
        final View[] val$found;

        AnonymousClass28(View[] viewArr) {
            this.val$found = viewArr;
        }

        @Override
        public void accept(View view) {
            if (this.val$found[0] == null && view.getVisibility() == 0 && !view.getClass().getName().contains("SingleLineTextView") && view instanceof TextView) {
                this.val$found[0] = view;
            }
        }
    }

    static class AnonymousClass29
    implements Consumer<View> {
        final View[] val$found;

        AnonymousClass29(View[] viewArr) {
            this.val$found = viewArr;
        }

        @Override
        public void accept(View view) {
            if (this.val$found[0] != null) {
                return;
            }
            String name = view.getClass().getName();
            if (name.contains("Badge") || name.contains("QUIBadge") || name.contains("RollingTextView") || name.contains("Unread") || name.contains("RedDot")) {
                this.val$found[0] = view;
            }
        }
    }

    static class AnonymousClass30
    implements Consumer<View> {
        final View[] val$fallback;

        AnonymousClass30(View[] viewArr) {
            this.val$fallback = viewArr;
        }

        @Override
        public void accept(View view) {
            if (this.val$fallback[0] == null && view instanceof TextView && view.getVisibility() == 8) {
                this.val$fallback[0] = view;
            }
        }
    }

    static class AnonymousClass31
    implements Runnable {
        final View val$avatar;
        final Context val$ctx;

        AnonymousClass31(Context context, View view) {
            this.val$ctx = context;
            this.val$avatar = view;
        }

        @Override
        public void run() {
            Bitmap bitmapLoadAvatarBitmap = ChatCardInjector.loadAvatarBitmap(this.val$ctx);
            if (bitmapLoadAvatarBitmap == null) {
                return;
            }
            new Handler(Looper.getMainLooper()).post((Runnable)new AnonymousClass1(ChatCardInjector.toCircle(bitmapLoadAvatarBitmap)));
        }

        class AnonymousClass1
        implements Runnable {
            final Bitmap val$circle;

            AnonymousClass1(Bitmap bitmap) {
                this.val$circle = bitmap;
            }

            @Override
            public void run() {
                try {
                    ChatCardInjector.setBitmapIntoAvatar(AnonymousClass31.this.val$avatar, this.val$circle);
                }
                catch (Throwable throwable) {
                    // empty catch block
                }
            }
        }
    }

    static class AnonymousClass32
    implements Consumer<View> {
        final View[] val$found;

        AnonymousClass32(View[] viewArr) {
            this.val$found = viewArr;
        }

        @Override
        public void accept(View view) {
            if (this.val$found[0] == null && view.getClass().getName().contains("Avatar")) {
                this.val$found[0] = view;
            }
        }
    }

    static class AnonymousClass33
    implements Consumer<View> {
        final ImageView[] val$inner;

        AnonymousClass33(ImageView[] imageViewArr) {
            this.val$inner = imageViewArr;
        }

        @Override
        public void accept(View view) {
            ImageView[] imageViewArr = this.val$inner;
            if (imageViewArr[0] == null && view instanceof ImageView) {
                imageViewArr[0] = (ImageView)view;
            }
        }
    }
}

