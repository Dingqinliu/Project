package com.dingqinliu.api;

import com.dingqinliu.model.Album;
import com.dingqinliu.model.User;
import com.dingqinliu.service.AlbumService;
import com.dingqinliu.service.StoryService;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.sql.SQLException;

@MultipartConfig
@WebServlet("/api/new-story.json")
public class NewStoryApi extends AbsApiServlet{
    @Override
    protected Object doPostInternal(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ApiException, IOException, ServletException {
        final AlbumService albumService = new AlbumService();
        final StoryService storyService = new StoryService();

        User currentUser=(User)req.getSession().getAttribute("currentUser");
        if (currentUser==null){
            throw new ApiException(401,"未登录");
        }
        int aid = Integer.parseInt(req.getParameter("aid"));
        String name = req.getParameter("name");
        Part audio=req.getPart("audio");

        Album album = albumService.detail(aid);
        if (album == null) {
            throw new ApiException(404, "对应的专辑不存在");
        }

        if (!album.uid.equals(currentUser.uid)) {
            throw new ApiException(403, "只有专辑作者，才有添加故事的权限");
        }

        storyService.save(aid, name, audio.getInputStream());

        return null;
    }
}
