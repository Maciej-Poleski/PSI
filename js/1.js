var createEmitter = function(){
    var result= {
        mapping : [],
        on : function(name,callback)
        {
            this.mapping.push({name : name,callback : callback,once : false});
        },
        once : function(name,callback)
        {
            this.mapping.push({name : name,callback : callback,once : true});
        },
        off : function(name,callback)
        {
            if(arguments.length==1)
            {
                this.mapping=this.mapping.filter(function(e){return e.name!==name;});
            }
            else
            {
                this.mapping=this.mapping.filter(function(e){return e.name!==name || e.callback!=callback;});
            }
        },
        emit : function(name)
        {
            for(var i=0;i<this.mapping.length;++i)
            {
                if(this.mapping[i].name===name)
                {
                    this.mapping[i].callback();
                }
            }
            this.mapping=this.mapping.filter(function(e){return e.name!=name || !e.once;});
        }
    };
    return result;
};