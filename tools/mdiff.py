#!/usr/bin/env python3
"""逐方法指令级比对（含字符串/调用/常量），输出可读差异"""
import sys, os, re, difflib
sys.path.insert(0, '/workspace/tools')
from semantic_diff import read_methods, canon_body, index

A = '/workspace/cmp/smali_o'
B = '/workspace/cmp/smali_n'

def show(cls, meth, limit=120):
    IA, IB = index(A), index(B)
    key = 'com/tencent/qqlitepro/' + cls
    if key not in IA or key not in IB:
        print('类缺失:', cls); return
    ma, mb = read_methods(IA[key]), read_methods(IB[key])
    ka = [k for k in ma if meth + '(' in k]
    kb = [k for k in mb if meth + '(' in k]
    if not ka or not kb:
        print('方法缺失:', cls, meth, bool(ka), bool(kb)); return
    a = canon_body(ma[ka[0]]); b = canon_body(mb[kb[0]])
    print('=== %s :: %s  (%d vs %d 指令)' % (cls, meth, len(a), len(b)))
    if a == b:
        print('   完全一致'); return
    n = 0
    for l in difflib.unified_diff(a, b, '原包', '还原', lineterm=''):
        print(l); n += 1
        if n >= limit:
            print('   ... (截断)'); break

if __name__ == '__main__':
    show(sys.argv[1], sys.argv[2])
