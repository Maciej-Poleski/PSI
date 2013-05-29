#!/usr/bin/python2

class Matrix(object):
    def __init__(self,size,data):
        self.__size=size
        if data:
            self.__data=[]
            for i in xrange(size):
                self.__data.append([])
                for j in xrange(size):
                    self.__data[i].append(data[i][j])
        else:
            self.__data=[]
            for i in xrange(size):
                self.__data.append([])
                for j in xrange(size):
                    self.__data[i].append(0)

    def __getitem__(self,key):
        return self.__data[key]

    def __add__(self,other):
        result=Matrix(self.__size,self.__data)
        for i in xrange(self.__size):
            for j in range(self.__size):
                result.__data[i][j]+=other.__data[i][j]
        return result

    def __str__(self):
        result=str(self.__size)
        for i in xrange(self.__size):
            result+="\n"
            for j in xrange(self.__size):
                result+=str(self.__data[i][j])+" "
        return result

    def __mul__(self,other):
        if type(other)==Matrix:
            result=Matrix(self.__size,None)
            for i in xrange(self.__size):
                for j in xrange(self.__size):
                    for k in xrange(self.__size):
                        result.__data[i][k]+=self.__data[i][j]*other.__data[j][k]
            return result
        else:
            result=Matrix(self.__size,None)
            for i in xrange(self.__size):
                for j in xrange(self.__size):
                        result.__data[i][j]=self.__data[i][j]*other
            return result

    def __rmul__(self,other):
        result=Matrix(self.__size,None)
        for i in xrange(self.__size):
            for j in xrange(self.__size):
                result.__data[i][j]=self.__data[i][j]*other
        return result

    def __mod__(self,other):
        result=Matrix(self.__size,None)
        for i in xrange(self.__size):
            for j in xrange(self.__size):
                result.__data[i][j]=self.__data[i][j]%other
        return result

    def __pow__(self,other,modulo=None):
        result=Matrix(self.__size,None)
        for i in xrange(self.__size):
            result.__data[i][i]=1
        for i in xrange(other):
            result=result*self
        if modulo != None:
            result=result%modulo
        return result

    def __iter__(self):
        for i in xrange(self.__size):
            for j in xrange(self.__size):
                yield self.__data[i][j]
