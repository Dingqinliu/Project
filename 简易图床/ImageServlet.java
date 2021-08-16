package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.Image;
import dao.ImageDao;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ImageServlet extends HttpServlet {
    /**
     * 查找图片信息
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //考虑两方面 查看唯一图片属性 & 查看指定图片属性
        // 通过URL是否带imageId参数进行区分
        //存在imageId,则查看指定图片属性，否则就查看所有。
        String imageId=req.getParameter("imageId");

        //URL中不存在imageId，则返回null
        if (imageId==null||imageId.equals("")){
            //查看所有图片属性
            selectAll(req,resp);
        }else {
            //查看指定
            selectOne(imageId,resp);
        }

    }

    /**
     * 查找指定id图片信息
     * @param imageId
     * @param resp
     * @throws IOException
     */
    private void selectOne(String imageId, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json; charset=utf-8");
        //1、创建ImageDao对象
        ImageDao imageDao = new ImageDao();
        Image image=imageDao.selectOne(Integer.parseInt(imageId));
        //2、使用gson把查到的数据转为json格式，并写回给响应对象
        Gson gson=new GsonBuilder().create();
        String jsonData=gson.toJson(image);
        resp.getWriter().write(jsonData);

    }

    /**
     * 查找所有图片信息
     * @param req
     * @param resp
     * @throws IOException
     */

    private void selectAll(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json; charset=utf-8");
        // 1、创建 ImageDao 对象并从数据库查找数据
        ImageDao imageDao = new ImageDao();
        List<Image> images = imageDao.selectAll(); //字段为key，值为value
        // 2. 将查找到的数据转换成 JSON 格式的字符串，并写回给resp对象，按前面设计的数组格式
        Gson gson=new GsonBuilder().create();
        String jsonData=gson.toJson(images);
        resp.getWriter().write(jsonData);
    }

    /**
     * 上传图片
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1、获取图片的属性信息，并且存入数据库
        //a)需要创建一个factory对象和upload对象,为获取图片属性所做的准备工作，是固定的逻辑
        FileItemFactory factory=new DiskFileItemFactory();
        ServletFileUpload upload=new ServletFileUpload(factory);
        //b)通过upload对象进一步解析请求req
        //FileItem代表一个上传的文件对象，理论上，HTTP支持一个请求中同时上传多个文件
        //所以多个文件就用一个List表示
        List<FileItem> items=null;
        try {
             items=upload.parseRequest(req);
        } catch (FileUploadException e) {
            //出现异常说明解析出错
            e.printStackTrace();

            //出错应该告知客户端，出现了什么类型的错误
            resp.setContentType("application/json; charset=utf-8");
            resp.getWriter().write("{\"OK\": false, \"reason\": \"请求解析失败\"}");//按照之前设计好的JSON格式 注意添加转义字符
            return;
        }
        //c)提取FileItem中的属性，转化为Image对象，才能存到数据库中
        //当前只考虑一张图片的情况 转化为Image对象
        FileItem fileItem=items.get(0);//取第一张图片
        Image image=new Image();
        image.setImageName(fileItem.getName());
        image.setSize((int)fileItem.getSize());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd");//获取当前时间
        image.setUploadTime(simpleDateFormat.format(new Date()));
        image.setContentType(fileItem.getContentType());
        image.setPath("./image"+System.currentTimeMillis()+"_"+image.getImageName());
        //文件在服务器上要存储的路径，自行构造(原本这里构造为image路径加文件名作为新路径)
        //但发现当不同图片名字相同时，路径会被覆盖，所以在路径中加上毫秒级时间戳
        image.setMd5("333");

        //存到数据库中
        ImageDao imageDao=new ImageDao();
        imageDao.insert(image);

        //2、获取图片的内容信息，并且写入磁盘文件
        File file=new File(image.getPath());
        try {
            fileItem.write(file);
        }catch (Exception e) {
            e.printStackTrace();
            resp.setContentType("application/json; charset=utf-8");
            resp.getWriter().write("{\"OK\":false, \"reason\": \"写磁盘失败\"}");
            return;
        }

        //3、上传完成，给客户端返回一个结果数据
        resp.setContentType("application/json; charset=utf-8");
        resp.getWriter().write("{\"OK\": true}");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       resp.setContentType("application/json; charset=utf-8");
        //1、指定id删除，获取请求中的imageId
        String imageId=req.getParameter("imageId");
        if (imageId==null||imageId.equals("")){
            resp.setStatus(200);
            resp.getWriter().write("{\"OK\": false, \"reason\": \"解析请求失败\"}");
            return;
        }
        //2、创建ImageDao对象，查看到该图片对象对应的相关属性
        ImageDao imageDao=new ImageDao();
        Image image=imageDao.selectOne(Integer.parseInt(imageId));
        if (image==null){
            //此时请求中传入的id在数据库中不存在
            resp.setStatus(200);
            resp.getWriter().write("{\"OK\": false, \"reason\": \"数据库中不存在此imageId\"}");
            return;
        }
        //3、删除数据库中的记录（因为前面存的时候是分为两部分存的 那么删除同理）
        imageDao.delete(Integer.parseInt(imageId));
        //4、删除本地磁盘文件
        File file=new File(image.getPath());
        file.delete();
        resp.setStatus(200);
        resp.getWriter().write("{\"OK\": true}");
    }
}
