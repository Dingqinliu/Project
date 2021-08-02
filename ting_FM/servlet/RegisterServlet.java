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
import java.sql.*;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1、读取用户填写的信息
        req.setCharacterEncoding("utf-8");
        String username = req.getParameter("username");
        String nickname = req.getParameter("nickname");
        String password = req.getParameter("password");

        //密码做哈希，目前的方法不是最好的 还可以再改进
        try{
                MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
            byte[] digest = messageDigest.digest(password.getBytes());//直接做摘要，得到的结果是纯二进制结果
            StringBuilder stringBuilder = new StringBuilder();//转成十六进制显示
            for (byte i:digest){
                stringBuilder.append(String.format("%02x",i));
            }
            password=stringBuilder.toString();


            //2、用户注册，插入数据库表(数据库表的创建见MySQL.txt)
            User user=new User();
            user.username=username;
            user.nickname=nickname;


                 String sql="INSERT INTO user (username, nickname, password) VALUES (?, ?, ?)";
                try(Connection c= DButil.getConnection()) {
                     try (PreparedStatement s = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) { //执行一次插入过程 Statement.RE..执行插入之后拿到id
                     s.setString(1, username);
                     s.setString(2, nickname);
                     s.setString(3, password); //数据库里不保存明文密码 应该先做哈希 再存到数据库里去
                     s.executeUpdate();

                     //获取插入后的自增id, 即uid
                         try(ResultSet rs=s.getGeneratedKeys()){
                             rs.next();
                             user.uid=rs.getInt(1);//
                         }
                    }
                }

                //3、用户登录 把当前登录的用户信息写入session中
            HttpSession session=req.getSession();
                session.setAttribute("currentUser",user);

                //4、重定向 登录成功后再回到首页
            resp.sendRedirect("/");

            }catch (SQLException exc){
                throw new ServletException(exc);
                } catch (NoSuchAlgorithmException exc){
                throw new ServletException(exc);
            }
    }
}
