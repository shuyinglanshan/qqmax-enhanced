#!/usr/bin/env python3
"""比对方法体的"操作码+常量"序列，忽略寄存器编号与标签编号"""
import sys, os, re, json
sys.path.insert(0, '/workspace/tools')
from semantic_diff import read_methods, index

OPARG = re.compile(r'^(?:\S+\s+)?([a-z0-9/\-]+)')   # 取操作码

def norm_line(l):
    # 去掉寄存器列表 {v0, v1} 里的具体编号
    l = re.sub(r'\{[^}]*\}', '{}', l)
    # 去掉 :label 编号
    l = re.sub(r':[a-z_]+_[0-9a-fA-F]+', ':L', l)
    # 去掉 .catchall 的偏移
    l = re.sub(r'\.catch.*', '.catch', l)
    # 去掉 move-result / const 的立即数以外内容保持不变（立即数有意义）
    # 寄存器 vN/pN -> vX
    l = re.sub(r'\b[vp]\d+\b', 'vX', l)
    # 匿名类名归一化：$AnonymousClassN -> $N
    l = re.sub(r'\$AnonymousClass(\d+)', r'$\1', l)
    # 字符串常量保留（业务逻辑）
    return re.sub(r'\s+', ' ', l).strip()

def opsig(body):
    out = []
    for l in body:
        s = l.strip()
        if not s: continue
        if s.startswith('.registers'):
            continue
        out.append(norm_line(s))
    return out

def compare(a_dir, b_dir, only_pkg='com/tencent/qqlitepro', verbose=False):
    IA, IB = index(a_dir), index(b_dir)
    common = [k for k in IA if k in IB and k.startswith(only_pkg)]
    tot = same = 0
    diffs = []
    for k in common:
        ma, mb = read_methods(IA[k]), read_methods(IB[k])
        for sig in set(ma) & set(mb):
            nm = sig.split('(')[0].split()[-1] if sig.split('(') else ''
            if (nm.startswith('lambda$') or '$r8$lambda$' in nm or '-$$Nest$' in nm
                    or nm.startswith('access$') or nm.startswith('m$')):
                continue
            tot += 1
            if opsig(ma[sig]) == opsig(mb[sig]):
                same += 1
            else:
                diffs.append((k, sig))
    return tot, same, diffs

if __name__ == '__main__':
    tot, same, diffs = compare(sys.argv[1], sys.argv[2])
    print('业务方法 %d | 操作码序列一致 %d (%.1f%%) | 差异 %d'
          % (tot, same, 100.0*same/tot if tot else 0, len(diffs)))
    json.dump(diffs, open('/tmp/opseq.json','w'))
    for k, s in diffs[:20]:
        print('   ~', k.split('/')[-1], '::', s.split('(')[0])
