package com.dingqinliu.servlet;

import com.dingqinliu.model.User;
import com.dingqinliu.service.AlbumService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.UUID;

@MultipartConfig
@WebServlet("/new-album")
public class NewAlbumServlet extends HttpServlet {
    private final AlbumService albumService=new AlbumService();
    private ServletContext servletContext;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext servletContext = config.getServletContext();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //拿到当前用户（专辑的作者）
        HttpSession session=req.getSession();
        User currentUser=(User)session.getAttribute("currentUser");
        if (currentUser==null){
            resp.sendError(401,"此操作需先登录");
            return;
        }


        req.setCharacterEncoding("utf-8");
        String name = req.getParameter("name");
        String brief = req.getParameter("brief");
        Part coverPart = req.getPart("cover");
        Part headerPart = req.getPart("header");

        //拿到图片路径信息
        String coverPath=savaImage(coverPart);
        String headerPath=savaImage(headerPart);

        try {
            int aid=albumService.save(currentUser.uid, name, brief, coverPath, headerPath);

            resp.sendRedirect("/album-editor.html?aid="+aid);
        }catch (SQLException exc){
            throw new ServletException(exc);
        }
    }

    private String savaImage(Part part) throws IOException{
        //把图片保存为文件
        String submittedFileName = part.getSubmittedFileName();
        int i = submittedFileName.lastIndexOf('.');
        String extension = submittedFileName.substring(i);
        String uuid = UUID.randomUUID().toString();
        String filename="/img/"+uuid+extension;

        String realPath=servletContext.getRealPath(filename);
        try(OutputStream os=new FileOutputStream(realPath)){
            byte[] buf=new byte[1024];
            int len;
            InputStream is=part.getInputStream();
            while (true){
                len=is.read(buf);
                if (len==1){
                    break;
                }
                os.write(buf,0,len);
            }
            os.flush();
        }
        return "/img/"+filename;
    }
}
