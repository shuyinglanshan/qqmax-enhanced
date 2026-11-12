#!/usr/bin/env python3
"""对比两个 jadx 源码树的方法体，输出差异明细"""
import os, re, json, sys, difflib

KEYWORDS = {'if','while','for','switch','catch','synchronized','try','else','do','return','new'}

def norm(s):
    s = re.sub(r'/\*.*?\*/', '', s, flags=re.S)
    s = re.sub(r'//[^\n]*', '', s)
    s = re.sub(r'\s+', ' ', s)
    return s.strip()

SIG = re.compile(r'\n[ \t]*((?:(?:public|private|protected|static|final|synchronized|native|abstract)\s+)+)'
                 r'([\w.$<>\[\],\s]+?)\s+(\w+)\s*\(([^;{)]*(?:\([^)]*\)[^;{)]*)*)\)\s*'
                 r'(?:throws\s+[\w.,\s]+)?\{')

def bodies(path):
    try:
        src = open(path, encoding='utf-8', errors='replace').read()
    except OSError:
        return {}
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
        key = (name, re.sub(r'\s+', '', m.group(4)))
        out.setdefault(key, []).append(norm(src[i:j+1]))
    return out

def index(root):
    d = {}
    for r, _, fs in os.walk(root):
        for f in fs:
            if f.endswith('.java'):
                rel = os.path.join(r, f)[len(root)+1:-5]
                rel = '/'.join(re.sub(r'\$AnonymousClass(\d+)', r'$\1', x) for x in rel.split('/'))
                d[rel] = os.path.join(r, f)
    return d

def compare(A, B, verbose=False):
    IA, IB = index(A), index(B)
    common = sorted(set(IA) & set(IB))
    tot = same = 0
    diff = []
    for rel in common:
        a = bodies(IA[rel]); b = bodies(IB[rel])
        for k in set(a) & set(b):
            tot += 1
            if a[k] == b[k]: same += 1
            else: diff.append((rel, k[0], len(a[k][0]), len(b[k][0])))
    return common, tot, same, diff, IA, IB

def show(A, B, rel, name, limit=60):
    a = bodies(os.path.join(A, rel + '.java'))
    b = bodies(os.path.join(B, rel + '.java'))
    ka = [k for k in a if k[0] == name]
    kb = [k for k in b if k[0] == name]
    if not ka or not kb:
        print('  未找到:', rel, name); return
    ta = re.sub(r'([{};])', r'\1\n', a[ka[0]][0]).split('\n')
    tb = re.sub(r'([{};])', r'\1\n', b[kb[0]][0]).split('\n')
    n = 0
    for l in difflib.unified_diff(ta, tb, 'orig', 'new', lineterm='', n=1):
        print(l); n += 1
        if n >= limit: break

if __name__ == '__main__':
    A, B = sys.argv[1], sys.argv[2]
    common, tot, same, diff, _, _ = compare(A, B)
    print('共同源文件 %d | 可配对方法 %d | 完全一致 %d | 差异 %d' % (len(common), tot, same, len(diff)))
    if len(sys.argv) > 3:
        pat = sys.argv[3]
        for rel, name, x, y in diff:
            if pat in rel:
                print('\n===== %s :: %s (%d vs %d) =====' % (rel, name, x, y))
                show(A, B, rel, name)
