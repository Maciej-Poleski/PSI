#!/usr/bin/python2

source = raw_input()
part1 = raw_input()
part2 = raw_input()

p1t=False
p2t=False

a1=source.find(part1)
if a1 !=-1:
    if source.find(part2,a1+len(part1)) !=-1:
        p1t=True

source=source[::-1]

a2=source.find(part1)
if a2 !=-1:
    if source.find(part2,a2+len(part1)) !=-1:
        p2t=True
        
if p1t:
    if p2t:
        print 'both'
    else:
        print 'forward'
else:
    if p2t:
        print 'backward'
    else:
        print 'fantasy'
