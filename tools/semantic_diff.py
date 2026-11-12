#!/usr/bin/env python3
"""DEX 方法体语义比对：归一化寄存器/标签/lambda/nest 桥，消除编译器形式差异"""
import re, os, sys, json

KW = {'public','private','protected','static','final','synthetic','bridge','abstract',
      'synchronized','declared-synchronized','native','varargs','volatile','transient',
      'enum','constructor'}

def norm_sig(sig):
    parts = sig.split()
    return ' '.join(p for p in parts if p not in KW)

def read_methods(path):
    out = {}; cur = None; body = []
    for raw in open(path, encoding='utf-8', errors='replace'):
        st = raw.strip()
        if st.startswith('.method '):
            cur = norm_sig(st[8:]); body = []
        elif st.startswith('.end method'):
            if cur is not None: out[cur] = body
            cur = None
        elif cur is not None:
            if not st: continue
            if st.startswith(('.line','.local','.end local','.restart local','.param',
                              '.prologue','.source','.annotation','.end annotation','#',
                              '.end param')):
                continue
            body.append(st)
    if cur is not None: out[cur] = body
    return out

REG = re.compile(r'\b([vps])(\d+)\b')

def split_args(s):
    """切分 DEX 类型描述符序列（无分隔符，靠描述符自身边界）"""
    out, i, n = [], 0, len(s)
    while i < n:
        start = i
        while i < n and s[i] == '[':      # 数组维度
            i += 1
        if i < n and s[i] == 'L':          # 引用类型，读到 ;
            j = s.find(';', i)
            if j < 0: j = n - 1
            i = j + 1
        else:                              # 基本类型单字符
            i += 1
        if i > start:
            out.append(s[start:i])
    return out

# R8 把 instance lambda 提升成 static，并加一个 receiver 参数
R8LAMBDA = re.compile(
    r'invoke-(static|virtual|direct)(/range)? \{([^}]*)\}, ((?:L[^;]+;)+)->\$r8\$lambda\$[^\(]+\(([^)]*)\)')
# D8 的 nestmate 访问桥
NEST = re.compile(
    r'invoke-static(/range)? \{([^}]*)\}, ((?:L[^;]+;)+|\[[^\s]+)->-\$\$Nest\$[mf](?:get|put|sm|s)?(\w+)\(([^)]*)\)')

def canon_body(lines):
    regmap = {}
    def reg_sub(m):
        k = (m.group(1), m.group(2))
        if k not in regmap:
            regmap[k] = '%s%d' % (m.group(1), len(regmap))
        return regmap[k]

    out = []
    for l in lines:
        def _r8(m):
            owner = m.group(4)
            parts = split_args(m.group(5))
            if parts and parts[0].startswith('L'):
                parts = parts[1:]          # 去掉 R8 追加的 receiver
            return 'invoke-virtual {%s}, %s->LAMBDA(%s)' % (m.group(3), owner, ''.join(parts))
        l = R8LAMBDA.sub(_r8, l)

        def _nest(m):
            owner = m.group(3)
            parts = split_args(m.group(5))
            if parts and parts[0].startswith('L'):
                parts = parts[1:]          # nest 桥同样把 receiver 放第一位
            return 'invoke-virtual {%s}, %s->%s(%s)' % (m.group(2), owner, m.group(4), ''.join(parts))
        l = NEST.sub(_nest, l)

        l = re.sub(r'\$r8\$lambda\$[^\s(]+', 'LAMBDA', l)
        l = re.sub(r'lambda\$\w+\$\d+', 'LAMBDA', l)
        s = REG.sub(reg_sub, l)
        s = re.sub(r':(cond|goto|try_start|try_end|catch|catchall|array|switch_data|pswitch_data)_[0-9a-fA-F]+',
                   r':\1_X', s)
        s = re.sub(r'\.catchall? \{[^}]*\}', '.catch', s)
        out.append(s)
    return out

def index(root):
    d = {}
    for r, _, fs in os.walk(root):
        for f in fs:
            if f.endswith('.smali'):
                p = os.path.join(r, f)
                rel = p[len(root)+1:-6]
                rel = '/'.join(re.sub(r'\$AnonymousClass(\d+)', r'$\1', x) for x in rel.split('/'))
                d[rel] = p
    return d

def is_synth(sig):
    nm = sig.split('(')[0].split()[-1] if sig.split('(') else ''
    return (nm.startswith('lambda$') or '$r8$lambda$' in nm or '-$$Nest$' in nm
            or nm.startswith('access$') or nm.startswith('m$'))

def biz(methods):
    return {s: canon_body(v) for s, v in methods.items() if not is_synth(s)}

def main(a, b, pkg='com/tencent/qqlitepro'):
    A, B = index(a), index(b)
    common = [k for k in A if k in B and k.startswith(pkg)]
    ident, diff = [], []
    for k in common:
        ba, bb = biz(read_methods(A[k])), biz(read_methods(B[k]))
        (ident if ba == bb else diff).append(k)
    print('自有类 %d | 业务方法体语义一致 %d | 有差异 %d' % (len(common), len(ident), len(diff)))
    json.dump(diff, open('/tmp/semdiff.json', 'w'))
    return diff

if __name__ == '__main__':
    main(sys.argv[1], sys.argv[2])
