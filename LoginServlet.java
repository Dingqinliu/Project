package com.dingqinliu.servlet;

import com.dingqinliu.model.User;
import com.dingqinliu.util.DButil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1、获取用户输入
        req.setCharacterEncoding("utf-8");
        String username=req.getParameter("username");
        String password = req.getParameter("password");

        //2、给明文密码hash
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
            byte[] digest = messageDigest.digest(password.getBytes());
            StringBuilder stringBuilder = new StringBuilder();
            for (byte i : digest) {
                stringBuilder.append(String.format("%02x", i));
            }
            password = stringBuilder.toString();

            //数据库中查询 username & password
            //username是唯一键 若username & password均正确 查到一条正确信息 否则 无信息
            User user = new User();
            user.username = username;

            String sql = "SELECT uid, nickname FROM user WHERE username=? AND password=?";
            try (Connection a = DButil.getConnection()) {
                try (PreparedStatement s = a.prepareStatement(sql)) {
                    s.setString(1, username);
                    s.setString(2, password);

                    try (ResultSet rs = s.executeQuery()) {
                        if (!rs.next()) { //没查到信息 登录失败
                            //失败回到当前页
                            resp.sendRedirect("/login.html");
                            return;
                        }
                        user.uid = rs.getInt("uid");
                        user.nickname = rs.getString("nickname");
                    }
                }
            }

            //登录成功
            HttpSession session = req.getSession();
            session.setAttribute("currentUser", user);

            //执行重定向
            resp.sendRedirect("/");
        }catch (SQLException exc){
            throw new ServletException(exc);
        } catch (NoSuchAlgorithmException exc){
            throw new ServletException(exc);
        }
    }
}
