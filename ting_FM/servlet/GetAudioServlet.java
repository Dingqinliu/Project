package com.dingqinliu.servlet;

import com.dingqinliu.service.StoryService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/get-audio")
public class GetAudioServlet extends HttpServlet {
    private final StoryService storyService=new StoryService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sid=req.getParameter("sid");
        if (sid==null||sid.trim().isEmpty()){
            resp.sendError(400,"参数sid是必须的");
            return;
        }

        int sidInt=Integer.parseInt(sid);
        storyService.getAudio(sidInt);
    }
}
