#!/usr/bin/env python3
"""源码方法体语义比对：把局部变量名归一化为占位符后比较"""
import os, re, json, sys, difflib

KEYWORDS = {'if','while','for','switch','catch','synchronized','try','else','do','return','new'}
JAVA_KW = {'abstract','assert','boolean','break','byte','case','catch','char','class','const',
           'continue','default','do','double','else','enum','extends','final','finally','float',
           'for','goto','if','implements','import','instanceof','int','interface','long','native',
           'new','package','private','protected','public','return','short','static','strictfp',
           'super','switch','synchronized','this','throw','throws','transient','try','void',
           'volatile','while','true','false','null'}

def strip_comments(s):
    s = re.sub(r'/\*.*?\*/', '', s, flags=re.S)
    s = re.sub(r'//[^\n]*', '', s)
    return s

SIG = re.compile(r'\n[ \t]*((?:(?:public|private|protected|static|final|synchronized|native|abstract)\s+)+)'
                 r'([\w.$<>\[\],\s]+?)\s+(\w+)\s*\(([^;{)]*(?:\([^)]*\)[^;{)]*)*)\)\s*'
                 r'(?:throws\s+[\w.,\s]+)?\{')

def raw_bodies(path):
    """-> {(name,args): [原始方法体文本]}"""
    try:
        src = open(path, encoding='utf-8', errors='replace').read()
    except OSError:
        return {}
    src = strip_comments(src)
    out = {}
    for m in SIG.finditer(src):
        name = m.group(3)
        if name in KEYWORDS:
            continue
        i = src.find('{', m.end() - 1); depth = 0; j = i
        while j < len(src):
            if src[j] == '{': depth += 1
            elif src[j] == '}':
                depth -= 1
                if depth == 0: break
            j += 1
        out.setdefault((name, re.sub(r'\s+', '', m.group(4))), []).append(src[i:j+1])
    return out

LOCAL_DECL = re.compile(r'(?<![\w$.])(?:[a-zA-Z_$][\w$]*\.)*[A-Z][\w$]*'
                        r'(?:<[^;{}()]*?>)?(?:\[\])*\s+([a-zA-Z_$]\w*)\s*(?=[=;,)])')

def canon(body, params=()):
    """归一化局部变量名 + 空白"""
    names = {}
    def sub(m):
        n = m.group(1)
        if n in JAVA_KW: return m.group(0)
        if n not in names:
            names[n] = 'V%d' % len(names)
        return m.group(0)[:m.start(1)-m.start(0)] + names[n]
    s = LOCAL_DECL.sub(sub, body)
    # 参数 & this 引用
    for i, p in enumerate(params):
        s = re.sub(r'\b' + re.escape(p) + r'\b', 'P%d' % i, s)
    # 残留的局部名（已声明的再做一次全局替换）
    for n, v in sorted(names.items(), key=lambda x: -len(x[0])):
        # 只替换"非 . 之后、非 ( 之前紧跟的方法名"的独立标识符
        s = re.sub(r'(?<![.\w$])' + re.escape(n) + r'(?![\w$])', v, s)
    s = re.sub(r'\s+', ' ', s)
    s = re.sub(r'this\.(?!f\$)', 'this.', s)
    return s.strip()

def split_params(p):
    out, depth, cur = [], 0, ''
    for ch in p:
        if ch in '<([': depth += 1
        elif ch in '>)]': depth -= 1
        if ch == ',' and depth == 0:
            out.append(cur); cur = ''
        else:
            cur += ch
    if cur.strip(): out.append(cur)
    res = []
    for x in out:
        x = x.strip()
        m = re.search(r'(\w+)\s*$', x)
        res.append(m.group(1) if m else x)
    return res

def index(root):
    d = {}
    for r, _, fs in os.walk(root):
        for f in fs:
            if f.endswith('.java'):
                rel = os.path.join(r, f)[len(root)+1:-5]
                rel = '/'.join(re.sub(r'\$AnonymousClass(\d+)', r'$\1', x) for x in rel.split('/'))
                d[rel] = os.path.join(r, f)
    return d

def run(A, B, only=None):
    IA, IB = index(A), index(B)
    common = sorted(set(IA) & set(IB))
    tot = same = 0; diffs = []
    for rel in common:
        if only and only not in rel:
            continue
        a = raw_bodies(IA[rel]); b = raw_bodies(IB[rel])
        for k in set(a) & set(b):
            tot += 1
            ca = canon(a[k][0], split_params(k[1]))
            cb = canon(b[k][0], split_params(k[1]))
            if ca == cb: same += 1
            else: diffs.append((rel, k[0], ca, cb))
    return common, tot, same, diffs

if __name__ == '__main__':
    A, B = sys.argv[1], sys.argv[2]
    only = sys.argv[3] if len(sys.argv) > 3 else None
    common, tot, same, diffs = run(A, B, only)
    print('共同源文件 %d | 可配对方法 %d | 语义一致 %d | 差异 %d'
          % (len(common), tot, same, len(diffs)))
    json.dump([(d[0], d[1]) for d in diffs], open('/tmp/srcdiff2.json', 'w'))
    for rel, n, _, _ in diffs[:25]:
        print('   ~ %-32s %s' % (rel, n))
