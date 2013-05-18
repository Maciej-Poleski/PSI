function clock(server)
{
    var d=new Date();
    var ref=function()
    {
        var month = d.getMonth()+1;
        var day = d.getDate();
        var t=d.getFullYear() + '-' + (month<10 ? '0' : '') + month + '-' + (day<10 ? '0' : '') + day;
        $('div#clock').text(t);
    };
    server.on('update', function(date){
        d=date;
        ref();
    });
    return ref;
}