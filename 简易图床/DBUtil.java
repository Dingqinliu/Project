package dao;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUtil {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/image_server?characterEncoding=utf8&useSSL=true";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    //获取连接
    private static volatile DataSource dataSource = null;
    private static Object PreparedStatement;
    private static Object ResultSet;

    //通过此方法创建DataSource的实例
    public static DataSource getDataSource() throws SQLException {
        if (dataSource == null) {  // (2) 双重判定 降低锁冲突
            //如果直接写dataSource=new MysqlDataSource()
            // 多线程环境下调用是线程不安全的
            //因为dataSource是唯一对象
            // 线程1执行dataSource=new MysqlDataSource() 时，有可能线程2抢占CPU，也调用getDataSource()
            //所以 改成线程安全（1）加锁 （2）双重判定 （3）volatile
            synchronized (DBUtil.class) {
                if (dataSource == null) {
                    dataSource = new MysqlDataSource();
                    ((MysqlDataSource) dataSource).setUrl(URL);
                    ((MysqlDataSource) dataSource).setUser(USERNAME);
                    ((MysqlDataSource) dataSource).setPassword(PASSWORD);
                }
            }
        }
        return dataSource;
    }

    public static Connection getConnection(){
        try {
            return getDataSource().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void close(Connection connection, PreparedStatement statement, ResultSet resultSet) {
        // 关闭顺序必须是先关 resultSet, 再关 statement, 最后关 connectin ，先创建的后关，例子：进门出门
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
