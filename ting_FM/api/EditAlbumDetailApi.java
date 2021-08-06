package com.dingqinliu.api;

import com.dingqinliu.model.Album;
import com.dingqinliu.model.User;
import com.dingqinliu.service.AlbumService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

@WebServlet("/api/edit-album-detail.json")
public class EditAlbumDetailApi extends AbsApiServlet{
    private final AlbumService albumService=new AlbumService();

    @Override
    protected Object doGetInternal(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ApiException {
        //1、判断用户是否已经登录
        User currentUser=(User)req.getSession().getAttribute("currentUser");
        if (currentUser==null){
            throw new ApiException(401,"未登录");
        }

        //2、判断调用的时候是否提供了aid
        String aid=req.getParameter("aid");
        if (aid==null||aid.trim().isEmpty()){
            throw new ApiException(400,"aid参数是必须的");
        }

        int aidInt = Integer.parseInt(aid);
        // 3. 根据 aid 获取对应的专辑
        Album album = albumService.detail(aidInt);

        // 4. 判断专辑的作者是不是当前登录用户
        if (!album.uid.equals(currentUser.uid)) {
            throw new ApiException(403, "只有专辑的作者有权限使用");
        }

        // 5. 返回专辑信息
        return album;
    }
}
