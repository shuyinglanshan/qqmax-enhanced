package com.tencent.qqlitepro.ui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

import com.tencent.qqlitepro.util.Log;

/**
 * 极简 SVG 渲染器（仅覆盖人机验证图用到的子集）。
 *
 * 支持元素：
 *  - rect（含 rx 圆角）
 *  - circle
 *  - polygon / polyline
 *  - path（M/L/H/V/Z 绝对指令）
 *  - text（忽略，验证图不含文字）
 *
 * 支持属性：fill / stroke / stroke-width / fill-opacity
 *
 * 设计取舍：不引入第三方 SVG 库（体积大、依赖多），
 * 验证图结构固定且简单，自己解析足够稳。
 */
final class CaptchaSvg {

    private CaptchaSvg() {
    }

    /** 把 SVG 文本渲染为指定宽高的 Bitmap（保持 viewBox 比例居中） */
    static Bitmap render(String svg, int targetW, int targetH) {
        if (svg == null || svg.isEmpty() || targetW <= 0 || targetH <= 0) {
            return null;
        }
        try {
            float[] vb = parseViewBox(svg);
            float vbW = vb[0] > 0 ? vb[0] : targetW;
            float vbH = vb[1] > 0 ? vb[1] : targetH;

            Bitmap bm = Bitmap.createBitmap(targetW, targetH, Bitmap.Config.ARGB_8888);
            Canvas cv = new Canvas(bm);
            /* 背景：验证图通常自带底色 rect，这里先铺一层深色兜底 */
            cv.drawColor(0xFF1A1C1F);

            float sx = targetW / vbW;
            float sy = targetH / vbH;
            cv.save();
            cv.scale(sx, sy);
            drawElements(svg, cv, vbW, vbH);
            cv.restore();
            return bm;
        } catch (Throwable t) {
            Log.w("CaptchaSvg.render 失败: " + t);
            return null;
        }
    }

    /** 解析 viewBox="0 0 320 240"；返回 {width, height} */
    private static float[] parseViewBox(String svg) {
        float[] out = new float[]{0f, 0f};
        try {
            String vb = attr(svg, "viewBox");
            if (vb != null) {
                String[] p = vb.trim().split("[\\s,]+");
                if (p.length >= 4) {
                    float w = Float.parseFloat(p[2]);
                    float h = Float.parseFloat(p[3]);
                    if (w > 0 && h > 0) {
                        out[0] = w;
                        out[1] = h;
                        return out;
                    }
                }
            }
            String w = attr(svg, "width");
            String h = attr(svg, "height");
            if (w != null && h != null) {
                out[0] = num(w);
                out[1] = num(h);
            }
        } catch (Throwable ignored) {
        }
        return out;
    }

    /** 逐个元素解析并绘制 */
    private static void drawElements(String svg, Canvas cv, float vbW, float vbH) {
        int i = 0;
        int n = svg.length();
        while (i < n) {
            int lt = svg.indexOf('<', i);
            if (lt < 0) {
                break;
            }
            int gt = findTagEnd(svg, lt);
            if (gt < 0) {
                break;
            }
            String tag = svg.substring(lt + 1, gt).trim();
            i = gt + 1;
            /* 跳过结束标签、注释、处理指令 */
            if (tag.startsWith("/") || tag.startsWith("?") || tag.startsWith("!")) {
                continue;
            }
            /* 取标签名 */
            int sp = indexOfAny(tag, " \t\r\n/");
            String name = sp < 0 ? tag : tag.substring(0, sp);
            try {
                if ("rect".equals(name)) {
                    drawRect(cv, tag);
                } else if ("circle".equals(name)) {
                    drawCircle(cv, tag);
                } else if ("polygon".equals(name)) {
                    drawPoly(cv, tag, true);
                } else if ("polyline".equals(name)) {
                    drawPoly(cv, tag, false);
                } else if ("path".equals(name)) {
                    drawPath(cv, tag);
                } else if ("g".equals(name)) {
                    /* g 只承载样式继承，这里逐元素自带样式，可直接忽略 */
                }
            } catch (Throwable ignored) {
            }
        }
    }

    /** 找到标签真正的结束 > （跳过属性值里的 >） */
    private static int findTagEnd(String s, int from) {
        boolean inQuote = false;
        char q = 0;
        for (int i = from + 1; i < s.length(); i++) {
            char c = s.charAt(i);
            if (inQuote) {
                if (c == q) {
                    inQuote = false;
                }
            } else if (c == '"' || c == '\'') {
                inQuote = true;
                q = c;
            } else if (c == '>') {
                return i;
            }
        }
        return -1;
    }

    /* ---------- 元素 ---------- */

    private static void drawRect(Canvas cv, String tag) {
        float x = num(attr(tag, "x"));
        float y = num(attr(tag, "y"));
        float w = num(attr(tag, "width"));
        float h = num(attr(tag, "height"));
        float rx = num(attr(tag, "rx"));
        if (w <= 0 || h <= 0) {
            return;
        }
        Paint fill = fillPaint(tag);
        RectF r = new RectF(x, y, x + w, y + h);
        if (fill != null) {
            if (rx > 0) {
                cv.drawRoundRect(r, rx, rx, fill);
            } else {
                cv.drawRect(r, fill);
            }
        }
        Paint stroke = strokePaint(tag);
        if (stroke != null) {
            if (rx > 0) {
                cv.drawRoundRect(r, rx, rx, stroke);
            } else {
                cv.drawRect(r, stroke);
            }
        }
    }

    private static void drawCircle(Canvas cv, String tag) {
        float cx = num(attr(tag, "cx"));
        float cy = num(attr(tag, "cy"));
        float r = num(attr(tag, "r"));
        if (r <= 0) {
            return;
        }
        Paint fill = fillPaint(tag);
        if (fill != null) {
            cv.drawCircle(cx, cy, r, fill);
        }
        Paint stroke = strokePaint(tag);
        if (stroke != null) {
            cv.drawCircle(cx, cy, r, stroke);
        }
    }

    private static void drawPoly(Canvas cv, String tag, boolean close) {
        String pts = attr(tag, "points");
        if (pts == null) {
            return;
        }
        String[] arr = pts.trim().split("[\\s,]+");
        if (arr.length < 4) {
            return;
        }
        Path p = new Path();
        boolean first = true;
        for (int i = 0; i + 1 < arr.length; i += 2) {
            try {
                float x = Float.parseFloat(arr[i]);
                float y = Float.parseFloat(arr[i + 1]);
                if (first) {
                    p.moveTo(x, y);
                    first = false;
                } else {
                    p.lineTo(x, y);
                }
            } catch (Throwable ignored) {
            }
        }
        if (close) {
            p.close();
        }
        Paint fill = fillPaint(tag);
        if (fill != null && close) {
            cv.drawPath(p, fill);
        }
        Paint stroke = strokePaint(tag);
        if (stroke != null) {
            cv.drawPath(p, stroke);
        } else if (fill != null && !close) {
            cv.drawPath(p, fill);
        }
    }

    /** 仅支持 M/L/H/V/Z 绝对指令（验证图足够） */
    private static void drawPath(Canvas cv, String tag) {
        String d = attr(tag, "d");
        if (d == null) {
            return;
        }
        Path p = new Path();
        String[] toks = d.trim().split("[\\s,]+");
        float cx = 0, cy = 0;
        float startX = 0, startY = 0;
        boolean moved = false;
        char cmd = 0;
        int i = 0;
        while (i < toks.length) {
            String t = toks[i];
            if (t.isEmpty()) {
                i++;
                continue;
            }
            char c0 = t.charAt(0);
            if (Character.isLetter(c0)) {
                cmd = c0;
                if (t.length() > 1) {
                    /* 命令与数值粘连，如 M10 */
                    toks[i] = t.substring(1);
                    continue;
                }
                i++;
                if (cmd == 'Z' || cmd == 'z') {
                    p.close();
                }
                continue;
            }
            try {
                switch (cmd) {
                    case 'M':
                    case 'm': {
                        float x = Float.parseFloat(t);
                        float y = Float.parseFloat(toks[i + 1]);
                        p.moveTo(x, y);
                        cx = x;
                        cy = y;
                        startX = x;
                        startY = y;
                        moved = true;
                        cmd = (cmd == 'M') ? 'L' : 'l';
                        i += 2;
                        break;
                    }
                    case 'L':
                    case 'l': {
                        float x = Float.parseFloat(t);
                        float y = Float.parseFloat(toks[i + 1]);
                        p.lineTo(x, y);
                        cx = x;
                        cy = y;
                        i += 2;
                        break;
                    }
                    case 'H':
                    case 'h': {
                        float x = Float.parseFloat(t);
                        p.lineTo(x, cy);
                        cx = x;
                        i++;
                        break;
                    }
                    case 'V':
                    case 'v': {
                        float y = Float.parseFloat(t);
                        p.lineTo(cx, y);
                        cy = y;
                        i++;
                        break;
                    }
                    default:
                        i++;
                        break;
                }
            } catch (Throwable ignored) {
                i++;
            }
        }
        if (moved) {
            Paint fill = fillPaint(tag);
            if (fill != null) {
                cv.drawPath(p, fill);
            }
            Paint stroke = strokePaint(tag);
            if (stroke != null) {
                cv.drawPath(p, stroke);
            }
        }
    }

    /* ---------- 样式 ---------- */

    private static Paint fillPaint(String tag) {
        String f = attr(tag, "fill");
        if (f == null) {
            f = "#000000";
        }
        f = f.trim();
        if ("none".equalsIgnoreCase(f)) {
            return null;
        }
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setStyle(Paint.Style.FILL);
        p.setColor(parseColor(f));
        String op = attr(tag, "fill-opacity");
        if (op != null) {
            try {
                float o = Float.parseFloat(op);
                p.setAlpha((int) (255 * Math.max(0f, Math.min(1f, o))));
            } catch (Throwable ignored) {
            }
        }
        return p;
    }

    private static Paint strokePaint(String tag) {
        String s = attr(tag, "stroke");
        if (s == null || "none".equalsIgnoreCase(s.trim())) {
            return null;
        }
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setStyle(Paint.Style.STROKE);
        p.setColor(parseColor(s));
        p.setStrokeWidth(num(attr(tag, "stroke-width")) > 0
                ? num(attr(tag, "stroke-width")) : 1f);
        return p;
    }

    /** #RGB / #RRGGBB / rgb(r,g,b) */
    private static int parseColor(String s) {
        try {
            s = s.trim();
            if (s.startsWith("#")) {
                if (s.length() == 4) {
                    char r = s.charAt(1), g = s.charAt(2), b = s.charAt(3);
                    return Color.rgb(
                            Integer.parseInt("" + r + r, 16),
                            Integer.parseInt("" + g + g, 16),
                            Integer.parseInt("" + b + b, 16));
                }
                if (s.length() == 7) {
                    return 0xFF000000 | Integer.parseInt(s.substring(1), 16);
                }
                if (s.length() == 9) {
                    return (int) Long.parseLong(s.substring(1), 16);
                }
            }
            if (s.startsWith("rgb")) {
                int a = s.indexOf('(');
                int b = s.indexOf(')');
                if (a > 0 && b > a) {
                    String[] p = s.substring(a + 1, b).split(",");
                    if (p.length >= 3) {
                        return Color.rgb((int) num(p[0]), (int) num(p[1]), (int) num(p[2]));
                    }
                }
            }
        } catch (Throwable ignored) {
        }
        return 0xFFFFFFFF;
    }

    /* ---------- 解析小工具 ---------- */

    /** 取属性值（支持双引号/单引号） */
    private static String attr(String tag, String name) {
        try {
            String key = name + "=";
            int i = tag.indexOf(key);
            if (i < 0) {
                return null;
            }
            /* 确保是完整属性名（前面是空白） */
            if (i > 0) {
                char prev = tag.charAt(i - 1);
                if (prev != ' ' && prev != '\t' && prev != '\n' && prev != '\r' && prev != '<') {
                    /* 可能是别的属性名的后缀，继续找 */
                    int j = tag.indexOf(" " + key);
                    if (j < 0) {
                        return null;
                    }
                    i = j + 1;
                }
            }
            int v = i + key.length();
            while (v < tag.length() && Character.isWhitespace(tag.charAt(v))) {
                v++;
            }
            if (v >= tag.length()) {
                return null;
            }
            char q = tag.charAt(v);
            if (q == '"' || q == '\'') {
                int end = tag.indexOf(q, v + 1);
                if (end < 0) {
                    return null;
                }
                return tag.substring(v + 1, end);
            }
            int end = v;
            while (end < tag.length() && !Character.isWhitespace(tag.charAt(end))
                    && tag.charAt(end) != '/' && tag.charAt(end) != '>') {
                end++;
            }
            return tag.substring(v, end);
        } catch (Throwable ignored) {
            return null;
        }
    }

    private static int indexOfAny(String s, String chars) {
        for (int i = 0; i < s.length(); i++) {
            if (chars.indexOf(s.charAt(i)) >= 0) {
                return i;
            }
        }
        return -1;
    }

    /** 解析数值，去掉 px / % 等单位 */
    private static float num(String s) {
        if (s == null) {
            return 0f;
        }
        try {
            String t = s.trim();
            int i = 0;
            while (i < t.length()
                    && (Character.isDigit(t.charAt(i)) || t.charAt(i) == '.'
                    || t.charAt(i) == '-' || t.charAt(i) == '+')) {
                i++;
            }
            if (i == 0) {
                return 0f;
            }
            return Float.parseFloat(t.substring(0, i));
        } catch (Throwable ignored) {
            return 0f;
        }
    }
}
