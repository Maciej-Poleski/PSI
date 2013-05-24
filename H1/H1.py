#!/usr/bin/python2

available = raw_input()
needed=raw_input()

data=[]

for i in xrange(256):
    data.append(0)
    
for c in available:
    if c!=' ':
        data[ord(c)]+=1

for c in needed:
    if c!=' ':
        data[ord(c)]-=1
        
for i in data:
    if i<0:
        print 'NO'
        break
else:
    print 'YES'
    