#!/usr/bin/env python3
"""把 jadx 输出整理成可编译的 Java 源码树（幂等，可重复运行）"""
import os, re, sys, shutil

ROOT = sys.argv[1] if len(sys.argv) > 1 else '.'
REG = []
STEPS = []

def walk():
    for root, _, fs in os.walk(ROOT):
        for f in fs:
            if f.endswith('.java'):
                yield os.path.join(root, f)

def edit(fn):
    def deco(func):
        STEPS.append((fn, func))
        return func
    return deco

# ---------------------------------------------------------------- 1 匿名类
@edit('01 anonymous-static')
def _(s):
    # 原始 DEX 中这些匿名类都是静态的（无 this$0），补 static 修饰符
    return re.sub(r'(?m)^(    )class (AnonymousClass[0-9]+)(\s+extends|\s+implements|\s*\{)',
                  r'\1static class \2\3', s)

# ------------------------------------------------- 2 合成占位构造器 (*IA)
@edit('02 drop-IA-ctors')
def _(s):
    s = re.sub(r'\n[ \t]*/\* synthetic \*/[ \t]*([A-Za-z0-9_]+)\([^)]*?\.([A-Za-z0-9_]+IA) [A-Za-z0-9_]+\) \{\n[ \t]*this\(\);\n[ \t]*\}\n', '\n', s)
    names = ['Att','MailContent','ParsedPage','BulletinWaiter','ApplyWaiter','Request','Pending','FileInfoUI','LinkClickMovementMethod']
    s = re.sub(r'new (com\.tencent\.qqlitepro\.[A-Za-z0-9_.$]*?\.(' + '|'.join(names) + r'))\(null\)', r'new \1()', s)
    return s

# ------------------------------------------------------- 3 资源常量名
@edit('03 R-style-names')
def _(s):
    s = s.replace('android.R.style.Theme.DeviceDefault.Dialog.NoActionBar', 'android.R.style.Theme_DeviceDefault_Dialog_NoActionBar')
    s = s.replace('android.R.style.Theme.Black.NoTitleBar.Fullscreen', 'android.R.style.Theme_Black_NoTitleBar_Fullscreen')
    return s

# ------------------------------------- 4 死代码：自抑制/自抛出 finally 块
@edit('04 drop-self-suppress')
def _(s):
    s = re.sub(
        r'[ \t]*try \{\n'
        r'[ \t]*([A-Za-z0-9_.]+)\.close\(\);\n'
        r'[ \t]*\} catch \(java\.lang\.Throwable (\w+)\) \{\n'
        r'[ \t]*\2\.addSuppressed\(\2\);\n'
        r'[ \t]*\}\n'
        r'[ \t]*throw (?:new java\.lang\.RuntimeException\([^)]*\)|\2);\n', '', s)
    s = re.sub(r'[ \t]*throw new java\.lang\.RuntimeException\(m\.group\(2\)\);\n', '', s)
    return s

# ---------------------------------------------------- 5 集合泛型 / 枚举
@edit('05 generics-enum')
def _(s):
    s = s.replace('for (java.util.Map.Entry entry : ((java.util.Map) obj).entrySet())',
                  'for (java.util.Map.Entry<?, ?> entry : ((java.util.Map<?, ?>) obj).entrySet())')
    s = re.sub(r'\(java\.util\.Map<\?, \?>\) \(\(java\.util\.Map\) ([\w.]+)\)\.entrySet\(\)',
               r'((java.util.Map<?, ?>) \1).entrySet()', s)
    for v in ('entry', 'entry2', 'entry3'):
        s = s.replace('for (java.util.Map.Entry %s : map.entrySet())' % v,
                      'for (java.util.Map.Entry<?, ?> %s : ((java.util.Map<?, ?>) map).entrySet())' % v)
    s = s.replace('private static boolean insertAt(java.util.List<?> list',
                  'private static boolean insertAt(java.util.List list')
    s = s.replace('private static boolean replaceAt(java.util.List<?> list',
                  'private static boolean replaceAt(java.util.List list')
    s = s.replace(
        'return java.lang.Enum.valueOf(java.lang.Class.forName(java.lang.String.valueOf(map.get(ENUM_MARK)), false, cl()), java.lang.String.valueOf(map.get("v")));',
        'java.lang.Class<?> enumCls = java.lang.Class.forName(java.lang.String.valueOf(map.get(ENUM_MARK)), false, cl());\n'
        '                    for (java.lang.Object enumConst : enumCls.getEnumConstants()) {\n'
        '                        if (((java.lang.Enum) enumConst).name().equals(java.lang.String.valueOf(map.get("v")))) {\n'
        '                            return enumConst;\n'
        '                        }\n'
        '                    }\n'
        '                    return null;')
    return s

# ------------------------------------------------ 6 jadx 异常变量提升
SIG = re.compile(r'^    (?:public |private |protected |static |final |synchronized )*[A-Za-z0-9_.<>\[\], ]+\s+[A-Za-z0-9_]+\s*\([^;{]*\)\s*(?:throws [^{]+)?\{\s*$')
PROMOTE = re.compile(r'^(\s*)th = (th\d*);\s*$')

@edit('06 th-hoisting')
def _(s):
    lines = s.split('\n')
    tot = 0
    for i in range(len(lines) - 1, -1, -1):
        m = PROMOTE.match(lines[i])
        if not m:
            continue
        ind, src = m.group(1), m.group(2)
        start = None
        for j in range(i, -1, -1):
            if re.match(r'^    \S', lines[j]) and SIG.match(lines[j]):
                start = j; break
        if start is None:
            continue
        end = len(lines)
        for j in range(start + 1, len(lines)):
            if lines[j] == '}':
                end = j; break
        for j in range(i + 1, end):
            lines[j] = re.sub(r'(?<![A-Za-z0-9_.$"\'\\])th(?![A-Za-z0-9_$])', 'thRethrow', lines[j])
        lines[i] = ind + 'java.lang.Throwable thRethrow = ' + src + ';'
        tot += 1
    return '\n'.join(lines)

# ------------------------- 7 同名提升冲突：重建为独立 catch 变量
@edit('07 th-unique')
def _(s):
    lines = s.split('\n')
    changed = False
    decl = re.compile(r'^(\s*)java\.lang\.Throwable thRethrow = (th\d*);\s*$')
    i = 0
    while i < len(lines):
        if re.match(r'^    \S', lines[i]) and SIG.match(lines[i]):
            end = len(lines)
            for j in range(i + 1, len(lines)):
                if lines[j] == '}':
                    end = j; break
            cnt = 0
            for j in range(i + 1, end):
                m = decl.match(lines[j])
                if not m:
                    continue
                cnt += 1
                if cnt > 1:
                    new = 'thRethrow%d' % cnt
                    lines[j] = '%sjava.lang.Throwable %s = %s;' % (m.group(1), new, m.group(2))
                    for k in range(j + 1, end):
                        if decl.match(lines[k]):
                            break
                        lines[k] = re.sub(r'(?<![A-Za-z0-9_.$"\'\\])thRethrow(?![0-9A-Za-z_$])', new, lines[k])
                    changed = True
            i = end
        i += 1
    return '\n'.join(lines) if changed else s

# 8 还原被上一轮误改名的 catch 形参
@edit('08 catch-param-fix')
def _(s):
    return re.sub(r'(catch \(java\.lang\.Throwable (thRethrow\d*)\) \{\n)((?:[^\n]*\n)*?)[ \t]*java\.lang\.Throwable \2 = \2;\n',
                  r'\1\3', s)

# ------------------------------------------- 9 提升源变量本身缺失的情况
@edit('09 th-from-thRethrow')
def _(s):
    lines = s.split('\n')
    out = []
    changed = False
    for i, l in enumerate(lines):
        m = re.match(r'^(\s*)java\.lang\.Throwable (\w+) = (thRethrow\d*);\s*$', l)
        if m and m.group(3) != m.group(2):
            # 引用一个从未定义的 catch 变量 → 退化为通用异常对象
            out.append('%sjava.lang.Throwable %s = new java.lang.RuntimeException();' % (m.group(1), m.group(2)))
            changed = True
        else:
            out.append(l)
    return '\n'.join(out) if changed else s

# --------------------------------------------------- 10 访问可见性放宽
@edit('10 visibility')
def _(s):
    s = s.replace('private static final java.util.concurrent.ExecutorService SHARD_COLLECT_EXEC;',
                  'static final java.util.concurrent.ExecutorService SHARD_COLLECT_EXEC;')
    for name in ('REQ_MAIL_ATTACH', 'REQ_MAIL_BG', 'REQ_MAIL_IMAGE'):
        s = re.sub(r'private static final int %s =' % name, 'static final int %s =' % name, s)
    for name in ('sFileLog', 'sMailDialog', 'sPluginLog', 'sQqDynamicLog', 'sVoiceLog'):
        s = re.sub(r'private static (volatile )?([A-Za-z0-9_.<>]+) %s;' % name,
                   r'static \1\2 %s;' % name, s)
    s = re.sub(r'private static int (noticeNodeId|noticeId|nodeId)\(com\.google\.gson\.JsonObject',
               r'static int \1(com.google.gson.JsonObject', s)
    s = s.replace('private static java.lang.String httpTranslate(java.lang.String str) {',
                  'static java.lang.String httpTranslate(java.lang.String str) {')
    return s

# ----------------------------------------- 11 Runnable/TimerTask 的 throws
@edit('11 run-throws')
def _(s):
    s = re.sub(r'(public final void run\(\)) throws [\w.]+', r'\1', s)
    s = re.sub(r'(public void run\(\)) throws [\w.]+', r'\1', s)
    return s



# ------------------------- 12 jadx 1.5.6 在 QqMailManager 丢失的局部变量类型
@edit('12 qqmail-??-vars')
def _(s):
    s = s.replace("""    private static int pollInboxDirect(android.content.Context context) {
        ?? r1;""", """    private static int pollInboxDirect(android.content.Context context) {
        javax.mail.Folder r1;""")
    s = s.replace("""    public static boolean upgradeEntry(android.content.Context context, long j) {
        ?? r0;""", """    public static boolean upgradeEntry(android.content.Context context, long j) {
        javax.mail.Folder r0;""")
    s = s.replace("        ?? folder = 0;", "        javax.mail.Folder folder = null;")
    s = re.sub(r'\bfolder != 0\b', 'folder != null', s)
    s = re.sub(r'\br1 != 0\b', 'r1 != null', s)
    s = re.sub(r'\br0 != 0\b', 'r0 != null', s)
    s = re.sub(r'\br1 = 0;', 'r1 = null;', s)
    s = re.sub(r'\br0 = 0;', 'r0 = null;', s)
    return s

for _name, _fn in STEPS:
    _n = 0
    for _p in walk():
        _s = open(_p, encoding='utf-8').read()
        _s2 = _fn(_s)
        if _s2 != _s:
            open(_p, 'w', encoding='utf-8').write(_s2); _n += 1
    print('  %-22s %d files' % (_name, _n))
print('done')
