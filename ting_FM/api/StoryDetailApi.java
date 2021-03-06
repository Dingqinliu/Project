package com.dingqinliu.api;

import com.dingqinliu.model.Story;
import com.dingqinliu.service.StoryService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

@WebServlet("/api/story-detail.json")
public class StoryDetailApi extends AbsApiServlet {
    private final StoryService storyService=new StoryService();

    @Override
    protected Object doGetInternal(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ApiException {
        String sid=req.getParameter("sid");
        if (sid==null||sid.trim().isEmpty()){
            throw new ApiException(400,"参数sid是必须的");
        }

        int sidInt=Integer.parseInt(sid);
        Story story=storyService.detail(sidInt);
        if (story==null){
            throw new ApiException(404,"sid对应的故事不存在");
        }

        return story;
    }
}
