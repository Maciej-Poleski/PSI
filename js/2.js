function makeChain(funArray,callback)
{
    if(funArray.length===0)
        return callback;
    return function(){
        var fun=funArray.pop();
        return fun(makeChain(funArray,callback));
    };
}

var done = function(fun, callback){
    return makeChain(fun,callback)();
};