function getParameter(name) {
    var query=window.location.search.substring(1);//跳过”？“
    var params=query.split("&");//按照&进行分割
    for (var param of params){
        var pair=param.split("="); //遍历分割后的每一段 按照=再分开
        if (pair[0]===name){
            return decodeURIComponent(pair[1]);
        }
    }
    return undefined;
}

function ajax(method,url,onloadFn) {
    var xhr=new XMLHttpRequest();
    xhr.open(method,url);
    xhr.onload=function(){
        var result=JSON.parse(xhr.responseText);
        onloadFn(result);
    }
    xhr.send();

}