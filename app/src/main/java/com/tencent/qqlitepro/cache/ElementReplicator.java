package com.tencent.qqlitepro.cache;

/* JADX INFO: loaded from: classes.dex */
public final class ElementReplicator {
    private static final java.lang.String BYTES_MARK = "@b";
    private static final java.lang.String ENUM_MARK = "@e";
    private static final java.lang.String MAP_MARK = "@map";
    private static final java.lang.String MODEL_MARK = "@m";
    private static final com.google.gson.Gson GSON = new com.google.gson.GsonBuilder().disableHtmlEscaping().create();
    private static volatile java.lang.ClassLoader APP_CL = null;

    static void rememberLoader(java.lang.ClassLoader classLoader) {
        if (APP_CL != null || classLoader == null) {
            return;
        }
        APP_CL = classLoader;
    }

    private ElementReplicator() {
    }

    private static java.lang.ClassLoader cl() {
        if (APP_CL != null) {
            return APP_CL;
        }
        try {
            return java.lang.Thread.currentThread().getContextClassLoader();
        } catch (java.lang.Throwable unused) {
            return com.tencent.qqlitepro.cache.ElementReplicator.class.getClassLoader();
        }
    }

    public static java.util.List<?> deepCopy(java.util.List<?> list) {
        if (list == null) {
            return null;
        }
        try {
            java.util.ArrayList arrayList = new java.util.ArrayList();
            java.util.Iterator<?> it = list.iterator();
            while (it.hasNext()) {
                java.lang.Object objDeepCopyValue = deepCopyValue(it.next(), 0, new java.util.IdentityHashMap());
                if (objDeepCopyValue != null) {
                    arrayList.add(objDeepCopyValue);
                }
            }
            return arrayList;
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("ElementReplicator.deepCopy 失败: " + th);
            return null;
        }
    }

    private static java.lang.Object deepCopyValue(java.lang.Object obj, int i, java.util.IdentityHashMap<java.lang.Object, java.lang.Boolean> identityHashMap) {
        if (obj == null || i > 8) {
            return null;
        }
        if (obj instanceof java.lang.CharSequence) {
            return obj.toString();
        }
        if (!(obj instanceof java.lang.Number) && !(obj instanceof java.lang.Boolean)) {
            if (obj instanceof java.lang.Character) {
                return (java.lang.Character) obj;
            }
            if (obj instanceof byte[]) {
                return ((byte[]) obj).clone();
            }
            if (obj.getClass().isEnum()) {
                return obj;
            }
            if (obj instanceof java.util.List) {
                java.util.ArrayList arrayList = new java.util.ArrayList();
                java.util.Iterator it = ((java.util.List) obj).iterator();
                while (it.hasNext()) {
                    java.lang.Object objDeepCopyValue = deepCopyValue(it.next(), i + 1, identityHashMap);
                    if (objDeepCopyValue != null) {
                        arrayList.add(objDeepCopyValue);
                    }
                }
                return arrayList;
            }
            if (obj.getClass().isArray()) {
                int length = java.lang.reflect.Array.getLength(obj);
                java.util.ArrayList arrayList2 = new java.util.ArrayList();
                for (int i2 = 0; i2 < length; i2++) {
                    java.lang.Object objDeepCopyValue2 = deepCopyValue(java.lang.reflect.Array.get(obj, i2), i + 1, identityHashMap);
                    if (objDeepCopyValue2 != null) {
                        arrayList2.add(objDeepCopyValue2);
                    }
                }
                return arrayList2;
            }
            if (obj instanceof java.util.Map) {
                java.util.LinkedHashMap linkedHashMap = new java.util.LinkedHashMap();
                for (java.util.Map.Entry<?, ?> entry : ((java.util.Map<?, ?>) obj).entrySet()) {
                    int i3 = i + 1;
                    java.lang.Object objDeepCopyValue3 = deepCopyValue(entry.getKey(), i3, identityHashMap);
                    java.lang.Object objDeepCopyValue4 = deepCopyValue(entry.getValue(), i3, identityHashMap);
                    if (objDeepCopyValue3 == null) {
                        objDeepCopyValue3 = entry.getKey();
                    }
                    if (objDeepCopyValue4 == null) {
                        objDeepCopyValue4 = entry.getValue();
                    }
                    linkedHashMap.put(objDeepCopyValue3, objDeepCopyValue4);
                }
                return linkedHashMap;
            }
            if (!isModelLike(obj.getClass())) {
                return obj;
            }
            if (identityHashMap.containsKey(obj)) {
                return null;
            }
            identityHashMap.put(obj, java.lang.Boolean.TRUE);
            try {
                java.lang.Object objNewInstance = newInstance(obj.getClass());
                if (objNewInstance == null) {
                    identityHashMap.remove(obj);
                    return obj;
                }
                copyFields(obj, objNewInstance, i, identityHashMap);
                identityHashMap.remove(obj);
                return objNewInstance;
            } catch (java.lang.Throwable unused) {
                identityHashMap.remove(obj);
            }
        }
        return obj;
    }

    private static void copyFields(java.lang.Object obj, java.lang.Object obj2, int i, java.util.IdentityHashMap<java.lang.Object, java.lang.Boolean> identityHashMap) {
        java.lang.Object objDeepCopyValue;
        for (java.lang.reflect.Field field : allFields(obj.getClass())) {
            try {
                if (!java.lang.reflect.Modifier.isStatic(field.getModifiers()) && !field.isSynthetic()) {
                    field.setAccessible(true);
                    java.lang.Object obj3 = field.get(obj);
                    if (obj3 != null && (objDeepCopyValue = deepCopyValue(obj3, 1 + i, identityHashMap)) != null) {
                        field.set(obj2, objDeepCopyValue);
                    }
                }
            } catch (java.lang.Throwable unused) {
            }
        }
    }

    public static java.lang.Object toTree(java.util.List<?> list) {
        java.util.ArrayList arrayList = new java.util.ArrayList();
        if (list != null) {
            java.util.Iterator<?> it = list.iterator();
            while (it.hasNext()) {
                java.lang.Object treeValue = toTreeValue(it.next(), 0, new java.util.IdentityHashMap());
                if (treeValue != null) {
                    arrayList.add(treeValue);
                }
            }
        }
        return arrayList;
    }

    public static java.lang.String toTreeJson(java.util.List<?> list) {
        try {
            return GSON.toJson(toTree(list));
        } catch (java.lang.Throwable th) {
            com.tencent.qqlitepro.util.Log.w("toTreeJson 失败: " + th);
            return null;
        }
    }

    public static java.util.List<?> fromTreeJson(java.lang.String str) {
        if (str != null && !str.isEmpty()) {
            try {
                return fromTree(GSON.fromJson(str, java.lang.Object.class));
            } catch (java.lang.Throwable th) {
                com.tencent.qqlitepro.util.Log.w("fromTreeJson 失败: " + th);
            }
        }
        return null;
    }

    private static java.lang.Object toTreeValue(java.lang.Object obj, int i, java.util.IdentityHashMap<java.lang.Object, java.lang.Boolean> identityHashMap) {
        java.lang.Object treeValue;
        if (obj == null || i > 8) {
            return null;
        }
        if (obj instanceof java.lang.CharSequence) {
            return obj.toString();
        }
        if ((obj instanceof java.lang.Number) || (obj instanceof java.lang.Boolean)) {
            return obj;
        }
        if (obj instanceof java.lang.Character) {
            return obj.toString();
        }
        if (obj instanceof byte[]) {
            byte[] bArr = (byte[]) obj;
            if (bArr.length > 1024) {
                return null;
            }
            java.util.LinkedHashMap linkedHashMap = new java.util.LinkedHashMap();
            linkedHashMap.put(BYTES_MARK, android.util.Base64.encodeToString(bArr, 2));
            return linkedHashMap;
        }
        if (obj.getClass().isEnum()) {
            java.util.LinkedHashMap linkedHashMap2 = new java.util.LinkedHashMap();
            linkedHashMap2.put(ENUM_MARK, obj.getClass().getName());
            linkedHashMap2.put("v", ((java.lang.Enum) obj).name());
            return linkedHashMap2;
        }
        if (obj instanceof java.util.List) {
            java.util.ArrayList arrayList = new java.util.ArrayList();
            java.util.Iterator it = ((java.util.List) obj).iterator();
            while (it.hasNext()) {
                java.lang.Object treeValue2 = toTreeValue(it.next(), i + 1, identityHashMap);
                if (treeValue2 != null) {
                    arrayList.add(treeValue2);
                }
            }
            return arrayList;
        }
        if (obj.getClass().isArray()) {
            java.util.ArrayList arrayList2 = new java.util.ArrayList();
            int length = java.lang.reflect.Array.getLength(obj);
            for (int i2 = 0; i2 < length; i2++) {
                java.lang.Object treeValue3 = toTreeValue(java.lang.reflect.Array.get(obj, i2), i + 1, identityHashMap);
                if (treeValue3 != null) {
                    arrayList2.add(treeValue3);
                }
            }
            return arrayList2;
        }
        if (obj instanceof java.util.Map) {
            java.util.LinkedHashMap linkedHashMap3 = new java.util.LinkedHashMap();
            linkedHashMap3.put(MAP_MARK, java.lang.Boolean.TRUE);
            for (java.util.Map.Entry<?, ?> entry : ((java.util.Map<?, ?>) obj).entrySet()) {
                int i3 = i + 1;
                java.lang.Object treeValue4 = toTreeValue(java.lang.String.valueOf(entry.getKey()), i3, identityHashMap);
                java.lang.Object treeValue5 = toTreeValue(entry.getValue(), i3, identityHashMap);
                java.lang.String strValueOf = java.lang.String.valueOf(treeValue4);
                if (treeValue5 == null) {
                    treeValue5 = null;
                }
                linkedHashMap3.put(strValueOf, treeValue5);
            }
            return linkedHashMap3;
        }
        if (!isModelLike(obj.getClass())) {
            if (!(obj instanceof java.io.Serializable) || (obj instanceof android.graphics.drawable.Drawable)) {
                return null;
            }
            return obj;
        }
        if (identityHashMap.containsKey(obj)) {
            return null;
        }
        identityHashMap.put(obj, java.lang.Boolean.TRUE);
        try {
            java.util.LinkedHashMap linkedHashMap4 = new java.util.LinkedHashMap();
            linkedHashMap4.put(MODEL_MARK, obj.getClass().getName());
            for (java.lang.reflect.Field field : allFields(obj.getClass())) {
                try {
                    if (!java.lang.reflect.Modifier.isStatic(field.getModifiers()) && !field.isSynthetic()) {
                        field.setAccessible(true);
                        java.lang.Object obj2 = field.get(obj);
                        if (obj2 != null && (treeValue = toTreeValue(obj2, i + 1, identityHashMap)) != null) {
                            linkedHashMap4.put(field.getName(), treeValue);
                        }
                    }
                } catch (java.lang.Throwable unused) {
                }
            }
            identityHashMap.remove(obj);
            return linkedHashMap4;
        } catch (java.lang.Throwable unused2) {
            identityHashMap.remove(obj);
            return null;
        }
    }

    private static java.util.List<?> fromTree(java.lang.Object obj) {
        java.util.ArrayList arrayList = new java.util.ArrayList();
        if (obj instanceof java.util.List) {
            java.util.Iterator it = ((java.util.List) obj).iterator();
            while (it.hasNext()) {
                java.lang.Object objFromTreeValue = fromTreeValue(it.next(), 0, new java.util.IdentityHashMap());
                if (objFromTreeValue != null) {
                    arrayList.add(objFromTreeValue);
                }
            }
        }
        return arrayList;
    }

    private static java.lang.Object fromTreeValue(java.lang.Object obj, int i, java.util.IdentityHashMap<java.lang.Object, java.lang.Boolean> identityHashMap) {
        java.lang.reflect.Field fieldFindField;
        if (obj == null || i > 8) {
            return null;
        }
        if (obj instanceof java.util.Map) {
            java.util.Map map = (java.util.Map) obj;
            if (map.containsKey(BYTES_MARK)) {
                try {
                    return android.util.Base64.decode(java.lang.String.valueOf(map.get(BYTES_MARK)), 2);
                } catch (java.lang.Throwable unused) {
                    return null;
                }
            }
            if (map.containsKey(ENUM_MARK)) {
                try {
                    java.lang.Class<?> enumCls = java.lang.Class.forName(java.lang.String.valueOf(map.get(ENUM_MARK)), false, cl());
                    for (java.lang.Object enumConst : enumCls.getEnumConstants()) {
                        if (((java.lang.Enum) enumConst).name().equals(java.lang.String.valueOf(map.get("v")))) {
                            return enumConst;
                        }
                    }
                    return null;
                } catch (java.lang.Throwable unused2) {
                    return null;
                }
            }
            if (map.containsKey(MAP_MARK)) {
                java.util.LinkedHashMap linkedHashMap = new java.util.LinkedHashMap();
                for (java.util.Map.Entry<?, ?> entry : ((java.util.Map<?, ?>) map).entrySet()) {
                    if (!MAP_MARK.equals(java.lang.String.valueOf(entry.getKey()))) {
                        java.lang.Object objFromTreeValue = fromTreeValue(entry.getValue(), i + 1, identityHashMap);
                        java.lang.Object key = entry.getKey();
                        if (objFromTreeValue == null) {
                            objFromTreeValue = entry.getValue();
                        }
                        linkedHashMap.put(key, objFromTreeValue);
                    }
                }
                return linkedHashMap;
            }
            if (map.containsKey(MODEL_MARK)) {
                try {
                    java.lang.Class<?> cls = java.lang.Class.forName(java.lang.String.valueOf(map.get(MODEL_MARK)), false, cl());
                    if (identityHashMap.containsKey(cls)) {
                        return null;
                    }
                    identityHashMap.put(cls, java.lang.Boolean.TRUE);
                    java.lang.Object objNewInstance = newInstance(cls);
                    if (objNewInstance == null) {
                        return null;
                    }
                    for (java.util.Map.Entry<?, ?> entry2 : ((java.util.Map<?, ?>) map).entrySet()) {
                        java.lang.String strValueOf = java.lang.String.valueOf(entry2.getKey());
                        if (!MODEL_MARK.equals(strValueOf) && (fieldFindField = findField(cls, strValueOf)) != null) {
                            try {
                                java.lang.Object objFromTreeValue2 = fromTreeValue(entry2.getValue(), i + 1, identityHashMap);
                                if (objFromTreeValue2 != null) {
                                    setWithType(fieldFindField, objNewInstance, objFromTreeValue2);
                                }
                            } catch (java.lang.Throwable unused3) {
                            }
                        }
                    }
                    identityHashMap.remove(cls);
                    return objNewInstance;
                } catch (java.lang.Throwable unused4) {
                    return null;
                }
            }
            java.util.LinkedHashMap linkedHashMap2 = new java.util.LinkedHashMap();
            for (java.util.Map.Entry<?, ?> entry3 : ((java.util.Map<?, ?>) map).entrySet()) {
                java.lang.Object objFromTreeValue3 = fromTreeValue(entry3.getValue(), i + 1, identityHashMap);
                java.lang.Object key2 = entry3.getKey();
                if (objFromTreeValue3 == null) {
                    objFromTreeValue3 = entry3.getValue();
                }
                linkedHashMap2.put(key2, objFromTreeValue3);
            }
            return linkedHashMap2;
        }
        if (!(obj instanceof java.util.List)) {
            return obj;
        }
        java.util.ArrayList arrayList = new java.util.ArrayList();
        java.util.Iterator it = ((java.util.List) obj).iterator();
        while (it.hasNext()) {
            java.lang.Object objFromTreeValue4 = fromTreeValue(it.next(), i + 1, identityHashMap);
            if (objFromTreeValue4 != null) {
                arrayList.add(objFromTreeValue4);
            }
        }
        return arrayList;
    }

    private static void setWithType(java.lang.reflect.Field field, java.lang.Object obj, java.lang.Object obj2) throws java.lang.IllegalAccessException {
        java.lang.Class<?> type = field.getType();
        field.setAccessible(true);
        if (obj2 == null) {
            return;
        }
        if (type == java.lang.String.class) {
            field.set(obj, java.lang.String.valueOf(obj2));
            return;
        }
        if (type == java.lang.Integer.TYPE || type == java.lang.Integer.class) {
            field.set(obj, java.lang.Integer.valueOf(((java.lang.Number) obj2).intValue()));
            return;
        }
        if (type == java.lang.Long.TYPE || type == java.lang.Long.class) {
            field.set(obj, java.lang.Long.valueOf(((java.lang.Number) obj2).longValue()));
            return;
        }
        if (type == java.lang.Short.TYPE || type == java.lang.Short.class) {
            field.set(obj, java.lang.Short.valueOf(((java.lang.Number) obj2).shortValue()));
            return;
        }
        if (type == java.lang.Byte.TYPE || type == java.lang.Byte.class) {
            field.set(obj, java.lang.Byte.valueOf(((java.lang.Number) obj2).byteValue()));
            return;
        }
        if (type == java.lang.Float.TYPE || type == java.lang.Float.class) {
            field.set(obj, java.lang.Float.valueOf(((java.lang.Number) obj2).floatValue()));
            return;
        }
        if (type == java.lang.Double.TYPE || type == java.lang.Double.class) {
            field.set(obj, java.lang.Double.valueOf(((java.lang.Number) obj2).doubleValue()));
            return;
        }
        if (type == java.lang.Boolean.TYPE || type == java.lang.Boolean.class) {
            field.set(obj, obj2);
            return;
        }
        if (type == byte[].class) {
            field.set(obj, obj2);
            return;
        }
        if (type.isInstance(obj2)) {
            field.set(obj, obj2);
        } else if (java.lang.Number.class.isAssignableFrom(type)) {
            field.set(obj, obj2);
        } else {
            field.set(obj, obj2);
        }
    }

    private static boolean isModelLike(java.lang.Class<?> cls) {
        java.lang.String name = cls.getName();
        return name.startsWith("com.tencent.qqnt.kernel.nativeinterface.") || name.startsWith("com.tencent.qqnt.") || name.startsWith("com.tencent.");
    }

    private static java.lang.Object newInstance(java.lang.Class<?> cls) {
        try {
            return cls.getDeclaredConstructor(new java.lang.Class[0]).newInstance(new java.lang.Object[0]);
        } catch (java.lang.Throwable unused) {
            return null;
        }
    }

    private static java.util.List<java.lang.reflect.Field> allFields(java.lang.Class<?> cls) {
        java.util.ArrayList arrayList = new java.util.ArrayList();
        while (cls != null && cls != java.lang.Object.class) {
            try {
                java.util.Collections.addAll(arrayList, cls.getDeclaredFields());
            } catch (java.lang.Throwable unused) {
            }
            cls = cls.getSuperclass();
        }
        return arrayList;
    }

    private static java.lang.reflect.Field findField(java.lang.Class<?> cls, java.lang.String str) {
        while (cls != null && cls != java.lang.Object.class) {
            try {
                return cls.getDeclaredField(str);
            } catch (java.lang.Throwable unused) {
                cls = cls.getSuperclass();
            }
        }
        return null;
    }
}
