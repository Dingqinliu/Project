package com.dingqinliu.api;

import com.dingqinliu.service.AlbumService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;


@WebServlet("/api/album-list.json")
public class AlbumListApi extends AbsApiServlet { //作品列表接口
    private final AlbumService albumService=new AlbumService();

    @Override
    protected Object doGetInternal(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ApiException {

        //判断有无传keywords
        String keyword = req.getParameter("keyword");
        if (keyword != null && keyword.trim().isEmpty()) { //传上来或者传上来为空 一律视为null
            keyword = null;
        }
        return albumService.latestList(keyword);
    }
}
