#!/usr/bin/python2

import re

data=[]

z=int(raw_input())

for i in xrange(z):
    line=raw_input()
    m=re.search('(\d\d)\.(\d\d)\.(\d\d\d\d) (\d\d):(\d\d) (.*)',line)
    data.append((int(m.group(3)),int(m.group(2)),int(m.group(1)),int(m.group(4)),int(m.group(5)),m.group(6)))

data.sort()

for i in data:
    print '{0:0=2d}.{1:0=2d}.{2:0=4d} {3:0=2d}:{4:0=2d} {5}'.format(i[2],i[1],i[0],i[3],i[4],i[5])
