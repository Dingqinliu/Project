package dao;

import common.JavaImageServerException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ImageDao {
    //1、把image对象插入到数据库中
   public boolean insert(Image image){
       //1、获取数据库连接 用封装好的DBUtil
       Connection connection = DBUtil.getConnection();

       //2、创建并拼装SQL语句
       PreparedStatement statement = null;
       String sql = "insert into image_table values(null, ?, ?, ?, ?, ?, ?)";//7个字段
       try {
           // 2. 拼装 PreparedStatement
           statement = connection.prepareStatement(sql);
           statement.setString(1, image.getImageName());
           statement.setInt(2, image.getSize());
           statement.setString(3, image.getUploadTime());
           statement.setString(4, image.getMd5());
           statement.setString(5, image.getContentType());
           statement.setString(6, image.getPath());
           System.out.println(statement);

           // 3. 执行SQL语句, 返回值表示影响了几行表格
           int ret = statement.executeUpdate();
//           return ret > 0;
           if (ret!=1){
               // 程序出现问题 抛出异常
               throw new JavaImageServerException("插入数据出错！");
           }
       } catch (SQLException | JavaImageServerException e) {
           e.printStackTrace();
       } finally {
           //4、关闭连接和statement对象
           DBUtil.close(connection, statement, null);
       }
       return false;
   }

   //2、查找所有 如果数据库中存储数据上亿
    //此时这个查询不合理 科学做法是增设查找条件 限制条数 或者分页显示
    public List<Image> selectAll(){
        // 1. 获取数据库链接
        Connection connection = DBUtil.getConnection();
        PreparedStatement statement = null;
        String sql = "select * from image_table";
        ArrayList<Image> result = new ArrayList<>();
        ResultSet resultSet = null;
        try {
            // 2. 执行 sql, 并获取结果集合
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();
            // 3. 遍历结果集合
            while (resultSet.next()) {
                Image image = new Image();
                image.setImageId(resultSet.getInt("image_id"));
                image.setImageName(resultSet.getString("image_name"));
                image.setSize(resultSet.getInt("size"));
                image.setUploadTime(resultSet.getString("upload_time"));
                image.setMd5(resultSet.getString("md5"));
                image.setContentType(resultSet.getString("content_type"));
                image.setPath(resultSet.getString("path"));
                result.add(image);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(connection, statement, resultSet);
        }
        return result;
    }

    //3、指定imageId查找
    public Image selectOne(int imageId) {
        // 1. 获取数据库连接
        Connection connection = DBUtil.getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String sql = "select * from image_table where image_id = ?";
        try {
            // 2. 执行 SQL 语句
            statement = connection.prepareStatement(sql);
            statement.setInt(1, imageId);
            resultSet = statement.executeQuery();

            // 3. 遍历结果集合(这个结果中应该只有一个)
            if (resultSet.next()) {
                Image image = new Image();
                image.setImageId(resultSet.getInt("image_id"));
                image.setImageName(resultSet.getString("image_name"));
                image.setSize(resultSet.getInt("size"));
                image.setUploadTime(resultSet.getString("upload_time"));
                image.setMd5(resultSet.getString("md5"));
                image.setContentType(resultSet.getString("content_type"));
                image.setPath(resultSet.getString("path"));
                return image;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(connection, statement, resultSet);
        }
        return null;
    }

    //4、指定imageId 删除
    public boolean delete(int imageId) {
        // 1. 创建数据库连接
        Connection connection = DBUtil.getConnection();
        PreparedStatement statement = null;
        String sql = "delete from image_table where image_id = ?";
        try {
            // 2. 执行 SQL 语句
            statement = connection.prepareStatement(sql);
            statement.setInt(1, imageId);
            int ret = statement.executeUpdate();
            return ret > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(connection, statement, null);
        }
        return false;
    }
    public Image selectByMD5(String md5) {
        Connection connection = DBUtil.getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String sql = "select * from image_table where md5 = ?";
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, md5);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Image image = new Image();
                image.setImageId(resultSet.getInt("image_id"));
                image.setImageName(resultSet.getString("image_name"));
                image.setSize(resultSet.getInt("size"));
                image.setUploadTime(resultSet.getString("upload_time"));
                image.setMd5(resultSet.getString("md5"));
                image.setContentType(resultSet.getString("content_type"));
                image.setPath(resultSet.getString("path"));
                return image;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(connection, statement, resultSet);
        }
        return null;
    }

    //测试
    public static void main(String[] args) {
       Image image=new Image();
       image.setImageName("1.jpg");
       image.setSize(50);
       image.setUploadTime("20210814");
       image.setContentType("image/jpg");
       image.setPath("./data/1.jpg");
       image.setMd5("1111222");

       ImageDao imageDao=new ImageDao();
       imageDao.insert(image);

    }
}
