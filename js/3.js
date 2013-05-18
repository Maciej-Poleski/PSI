var my$ = function(name){
    var setOfElements = [];
    
    setOfElements=document.querySelectorAll(name);
    
    return {
        elements : setOfElements,
        on : function(event,callback){
            for(var i=0;i<this.elements.length;++i)
            {
                this.elements[i].addEventListener(event,callback);
            }
            return this;
        }
    };
};