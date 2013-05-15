var createEmitter = function(){
    var result= {
        mapping : {},        
        emit : function(name)
        {
            if(name in this.mapping)
            for(var i=0;i<this.mapping.name.length;++i)
            {
                if(this.mapping.name[i]!==undefined)
                this.mapping.name[i]();
            }
        },
        
        on : function(name,callback)
        {
            if(this.mapping.name===undefined)
            {
                this.mapping.name=[callback];
            }
            else
            {
                this.mapping.name.push(callback);
            }
        },
        once : function(name,callback)
        {},
        off:function(name)
        {
            if(arguments>1)
            {
                var callback=arguments[0];
                if(name in this.mapping)
                for(var i=0;i<this.mapping.name.length;++i)
                {
                    if(this.mapping.name[i]===callback)
                        delete this.mapping.name[i];
                }
            }
            else
            {
                if(name in this.mapping)
                    delete this.mapping.name;
            }
        }
    };
    return result;
};