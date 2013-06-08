#!/usr/bin/python2
# -*- coding: UTF-8 -*-

from proxy import Proxy

class A(object):
    test0 = 'test0'
    def __init__(self):
        self.test1 = 'test1'
    def test2(self):
        return 'test2'

class B(A):
    pass

b = B()
p = Proxy(b)
print p.TeSt0
print p.TEST1
print p.tESt2()

p.tEsT3 = 'test3'
print b.test3
try:
    print b.TEST3
except:
    print 'b.TEST3 not defined'

p.obj = 'obj'
print b.obj
print id(b) == id(p.obj)
print p.obj.test2()
#taki odczyt nie będzie testowany:
print p.obj