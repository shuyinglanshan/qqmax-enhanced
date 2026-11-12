#!/usr/bin/env python3
"""按方法比对"调用序列+字符串常量"，这是最能反映业务逻辑的指纹"""
import os, re, sys, json
from collections import Counter

KW = set()

def strip_c(s):
    s = re.sub(r'/\*.*?\*/', '', s, flags=re.S)
    s = re.sub(r'//[^\n]*', '', s)
    return s

SIG = re.compile(r'\n[ \t]*((?:(?:public|private|protected|static|final|synchronized|native|abstract)\s+)+)'
                 r'([\w.$<>\[\],\s]+?)\s+(\w+)\s*\(([^;{)]*(?:\([^)]*\)[^;{)]*)*)\)\s*'
                 r'(?:throws\s+[\w.,\s]+)?\{')

def methods(path):
    try: src = open(path, encoding='utf-8', errors='replace').read()
    except OSError: return {}
    src = strip_c(src)
    out = {}
    for m in SIG.finditer(src):
        name = m.group(3)
        if name in ('if','while','for','switch','catch','synchronized','try'):
            continue
        i = src.find('{', m.end()-1); d = 0; j = i
        while j < len(src):
            if src[j] == '{': d += 1
            elif src[j] == '}':
                d -= 1
                if d == 0: break
            j += 1
        out.setdefault(name, []).append(src[i:j+1])
    return out

CALL = re.compile(r'(?<![\w$])([A-Za-z_$][\w$]*(?:\.[A-Za-z_$][\w$]*)*)\s*\(')
STR = re.compile(r'"((?:[^"\\]|\\.)*)"')

def fingerprint(body):
    """调用名序列 + 字符串常量集合"""
    calls = [c for c in CALL.findall(body)
             if c not in ('if','while','for','switch','catch','synchronized','try','return','new')]
    strs = STR.findall(body)
    return tuple(calls), tuple(sorted(set(strs)))

def index(root):
    d = {}
    for r, _, fs in os.walk(root):
        for f in fs:
            if f.endswith('.java'):
                rel = os.path.join(r, f)[len(root)+1:-5]
                rel = '/'.join(re.sub(r'\$AnonymousClass(\d+)', r'$\1', x) for x in rel.split('/'))
                d[rel] = os.path.join(r, f)
    return d

def run(A, B):
    IA, IB = index(A), index(B)
    common = sorted(set(IA) & set(IB))
    tot = same_call = same_str = same_both = 0
    details = []
    for rel in common:
        a = methods(IA[rel]); b = methods(IB[rel])
        for name in set(a) & set(b):
            tot += 1
            ca, sa = fingerprint(a[name][0])
            cb, sb = fingerprint(b[name][0])
            okc, oks = ca == cb, sa == sb
            same_call += okc; same_str += oks; same_both += (okc and oks)
            if not (okc and oks):
                details.append({
                    'file': rel, 'method': name,
                    'call_only_a': [x for x in ca if x not in cb][:6],
                    'call_only_b': [x for x in cb if x not in ca][:6],
                    'str_only_a': [x for x in sa if x not in sb][:4],
                    'str_only_b': [x for x in sb if x not in sa][:4],
                })
    return common, tot, same_call, same_str, same_both, details

if __name__ == '__main__':
    A, B = sys.argv[1], sys.argv[2]
    common, tot, sc, ss, sb, det = run(A, B)
    print('共同文件 %d | 配对方法 %d' % (len(common), tot))
    print('  调用序列一致 : %d (%.1f%%)' % (sc, 100.0*sc/tot))
    print('  字符串集一致 : %d (%.1f%%)' % (ss, 100.0*ss/tot))
    print('  两者都一致   : %d (%.1f%%)' % (sb, 100.0*sb/tot))
    json.dump(det, open('/tmp/callseq.json','w'), ensure_ascii=False, indent=1)
