#!/usr/bin/env python3
"""反编译还原常见缺陷静态扫描"""
import os, re, sys

ROOT = sys.argv[1] if len(sys.argv) > 1 else '.'

def files():
    for r, _, fs in os.walk(ROOT):
        for f in fs:
            if f.endswith('.java'):
                yield os.path.join(r, f)

def block(lines, start):
    """返回 start 行开始的花括号块 (start,end) 索引"""
    depth = 0
    for j in range(start, len(lines)):
        depth += lines[j].count('{') - lines[j].count('}')
        if j > start and depth <= 0:
            return start, j
    return start, len(lines) - 1

def scan():
    problems = []
    for p in files():
        lines = open(p, encoding='utf-8', errors='replace').read().split('\n')

        # 1) while(true) 无退出路径
        for i, l in enumerate(lines):
            if re.match(r'\s*while \(true\)\s*\{\s*$', l):
                s, e = block(lines, i)
                body = '\n'.join(lines[s:e+1])
                if not re.search(r'\b(break|return|throw)\b', body):
                    problems.append((p, i+1, 'while(true) 无退出', l.strip()))

        # 2) while(X < Y) 体内无自增
        for i, l in enumerate(lines):
            m = re.match(r'\s*while \((\w+) < ([\w.]+)\)\s*\{\s*$', l)
            if m:
                var = m.group(1)
                s, e = block(lines, i)
                body = '\n'.join(lines[s+1:e])
                if not re.search(r'\b' + var + r'\s*(\+\+|\+=|=\s*' + var + r'\s*\+)', body) \
                   and 'break' not in body and 'return' not in body:
                    problems.append((p, i+1, 'while 无自增（可能死循环）', l.strip()))

        # 3) for 循环体里变量被赋值成自身（死循环）
        for i, l in enumerate(lines):
            m = re.match(r'\s*for \(int (\w+) = 0; ', l)
            if m:
                var = m.group(1)
                s, e = block(lines, i)
                body = '\n'.join(lines[s+1:e])
                if re.search(r'\b' + var + r'\s*=\s*' + var + r'\s*;', body):
                    problems.append((p, i+1, 'for 自赋值', l.strip()))

        # 4) 方法体内 return 类型不匹配的明显迹象：return; in non-void（由编译保证，跳过）

        # 5) 未使用的 catch 变量导致吞异常（仅提示，不算错）

        # 6) 空的 if/else 分支（可能是 jadx 丢逻辑）
        for i, l in enumerate(lines):
            if re.match(r'\s*if \(.*\)\s*\{\s*$', l):
                s, e = block(lines, i)
                inner = [x.strip() for x in lines[s+1:e] if x.strip()]
                if not inner:
                    problems.append((p, i+1, '空 if 分支', l.strip()[:70]))

        # 7) 变量与自身比较（通常表示逻辑被破坏，但不一定是错）
        for i, l in enumerate(lines):
            m = re.search(r'\b(\w+) (==|!=) \1\b', l)
            if m and m.group(1) not in ('this',):
                problems.append((p, i+1, '变量自比较', l.strip()[:70]))
    return problems

if __name__ == '__main__':
    ps = scan()
    from collections import Counter
    c = Counter(k for _, _, k, _ in ps)
    print('扫描到问题 %d 处' % len(ps))
    for k, v in c.most_common():
        print('  %-28s %d' % (k, v))
    print()
    for p, ln, k, txt in ps:
        if 'while' in k or 'for 自赋值' in k:
            print('%-58s %5d  %-22s %s' % (p[len(ROOT)+1:], ln, k, txt))
