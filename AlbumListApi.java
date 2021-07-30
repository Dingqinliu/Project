package com.dingqinliu.servlet;

import com.dingqinliu.model.Album;
import com.dingqinliu.util.DButil;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/api/album-list.json")
public class AlbumListApi extends HttpServlet { //作品列表接口
    //转json
    private final ObjectMapper objectMapper=new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        //判断有无传keywords
        String keyword = req.getParameter("keyword");
        if (keyword != null && keyword.trim().isEmpty()) { //传上来或者传上来为空 一律视为null
            keyword = null;
        }

        resp.setCharacterEncoding("utf-8");
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();

        //查询 根据有无keyword分成两种sql
        String sql;
        if (keyword != null) { //传了
            sql = "SELECT aid, name, cover, count FROM album WHERE name LIKE ? ORDER BY aid DESC LIMIT 20";
            //只要它的名称中含有我们要查找的某个关键字 就保留下来
            //倒序(DESC) 最新的在最上面 LIMIT 只往出查20个
        } else { //没传
            sql = "SELECT aid, name, cover, count FROM album ORDER BY aid DESC LIMIT 20";
        }
        //查询
        List<Album> albumList = new ArrayList<>();
        try (Connection b = DButil.getConnection()) {
            try (PreparedStatement s = b.prepareStatement(sql)) {
                if (keyword != null) {
                    //SQL :WHERE name LIKE %xxx% 模糊匹配 只要含有xxx的就返回 类似于根据关键字查找
                    s.setString(1, "%" + keyword + "%");
                }
                //执行查询
                try (ResultSet rs = s.executeQuery()) {
                    while (rs.next()) {
                        Album album = new Album();
                        album.aid = rs.getInt("aid");
                        album.name = rs.getString("name");
                        album.cover = rs.getString("cover");
                        album.count = rs.getInt("count");

                        albumList.add(album); //放到顺序表里 顺序表里是查出来的所有列表信息
                    }
                }
            }
            Object result = new Object() {
                public final boolean success = true;
                public final Object data = albumList;
            };
            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(result);
            writer.println(json);
        } catch (SQLException exc) { //报错
            exc.printStackTrace();//打印错误

            Object result = new Object() {
                public final boolean success = false;
                public final String reason = exc.getMessage();
            };

            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(result);
            writer.println(json);
        }
    }
    }
