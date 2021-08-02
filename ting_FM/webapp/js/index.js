//把要执行的代码放在window.load事件发生后执行
//以此保证代码的执行发生在所有资源都已经加载完成后

function checkLogged() {
    var userElement = document.querySelector(".user");

    //GET /api/current-user.json接口
    var xhr=new XMLHttpRequest();
    xhr.open("GET","/api/current-user.json");
    xhr.onload=function () { //绑个onload 当响应返回时会执行这个function()
        //因为返回时是返回数据
        //so,1、数据转换为json格式(数据反序列)
        var result=JSON.parse(xhr.responseText);//得到由接口返回的数据
        if (result.logged===true){ //表示用户已经登录 去往新建作品的页面
            var currentUser=result.user;

            //替换掉index.html里注册-登录(class="user")部分
            var html=`<a href="/new-album.html">新建作品</a>
                      <a href="/my-album-list.html">我的作品</a>
                      <span>${currentUser.nickname}</span>`;
            userElement.innerHTML=html;
        }
    };
    xhr.send();
}

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

function fetchAlbumList() {
    var olElement=document.querySelector("ol.album-list");

    var url="/api/album-list.json";
    var keyword=getParameter("keyword");
    if (keyword){
        url+=`?keyword=${encodeURIComponent(keyword)}`;
    }

    var xhr=new XMLHttpRequest();
    xhr.open("GET",url);
    xhr.onload=function () {
        var result=JSON.parse(xhr.responseText);
        if (result.success===true){ //拉取成功
            var albumList=result.data;
            for (var album of albumList){ //建立作品列表下的每一个作品
                //html拼接
                var html=`<li class="album-item">
                    <a href="/album.html?aid=${album.aid}">
                    <img src="${album.cover}">
                    <span class="name">${album.name}</span>
                    <span class="count">播放量 ${album.count}</span>
                    </a>
                    </li>`

                    olElement.innerHTML+=html;//每有一个作品就往里填一行
            }
        }
    };
    xhr.send();
}


//不用onload是因为需要多个地方绑定load，用onload有覆盖效果 这个没有覆盖效果
window.addEventListener("load",function () {
    //1、检查用户是否已经登录
    checkLogged();
    //2、拉取作品列表并显示
    fetchAlbumList();
});

