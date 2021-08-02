package com.dingqinliu.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.dingqinliu.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/api/current-user.json")
public class CurrentUserAPI extends HttpServlet {
    private final ObjectMapper objectMapper=new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        {
//            //测试一下：http://localhost:8080/api/current-user.json
//            User one=new User();
//            one.username="zgz";
//            one.nickname="找工作";
//            one.uid=110;
//
//            req.getSession().setAttribute("currentUser",one);
//        }


        //输出JSON格式 Content-Type:application/json
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("application/json");
        PrintWriter writer=resp.getWriter();

        //判断用户是否已经登录，通过session获取当前已经登录的用户信息
        //如果无法取到 就是未登录
        HttpSession session = req.getSession();
        User currentUser = (User) session.getAttribute("currentUser");

        Object result;
        if (currentUser==null){
            //无法从session中获取到当前用户，代表未登录
            //这个对象要被序列化成JSON的对象
            //使用匿名类的好处，可以省略类的定义 减少类的定义
            result=new Object(){
                public final boolean logged=false;
            };
        }else {
            //已经登录 当前的登录用户就是currentUser
            result=new Object(){
                public final boolean logged=true;
                public final User user=currentUser;
            };
        }
        String json=objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(result);
        writer.println(json);
    }
}
