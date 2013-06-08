#!/usr/bin/python2
# -*- coding: UTF-8 -*-


class Proxy(object):
    def __init__(self,o):
        self.__dict__['obj']=o

    def __getattr__(self,name):
        for n in self.obj.__dict__:
            if n.lower()==name.lower():
                return self.obj.__dict__[n]

        for c in self.obj.__class__.__mro__:
            for n in c.__dict__:
                if n.lower()==name.lower():
                    return getattr(self.obj,n)
        else:
            return 42

    def __setattr__(self,name,value):
        for n in self.obj.__dict__:
            if n.lower()==name.lower():
                self.obj.__class__.__setattr__(self.obj,n,value)
                return

        for c in self.obj.__class__.__mro__:
            for n in c.__dict__:
                if n.lower()==name.lower():
                    self.obj.__class__.__setattr__(self.obj,n,value)
                    return
        else:
            self.obj.__class__.__setattr__(self.obj,name.lower(),value)
