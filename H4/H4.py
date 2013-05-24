#!/usr/bin/python2

import re

G={}
V={}

z=int(raw_input())

for i in xrange(z):
    line=raw_input()
    m=re.search('dodaj ([^ ]+) zanim dodasz ([^ ]+)',line)
    if m.group(1) not in G:
        G[m.group(1)]=[]
    G[m.group(1)].append(m.group(2))
    if m.group(2) not in G:
        G[m.group(2)]=[]

def dfs(v):
    if v in V:
        return V[v]==1
    V[v]=1
    for n in G[v]:
        if dfs(n):
            return True
    else:
        V[v]=2
        return False

for v in G:
    if dfs(v):
        print 'NIE'
        break
else:
    print 'TAK'
