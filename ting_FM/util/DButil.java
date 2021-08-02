package com.dingqinliu.util;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DButil {
    private static DataSource dataSource=null;

    private static void InitDataSource(){
        MysqlDataSource mysqlDataSource = new MysqlDataSource();
        mysqlDataSource.setServerName("127.0.0.1");
        mysqlDataSource.setPort(3306);
        mysqlDataSource.setUser("root");
        mysqlDataSource.setPassword("L12345678");//自己数据库密码密码
        mysqlDataSource.setDatabaseName("ting_fm");
        mysqlDataSource.setCharacterEncoding("utf8");
        mysqlDataSource.setUseSSL(false);
        mysqlDataSource.setServerTimezone("Asia/Shanghai");

        dataSource=mysqlDataSource;
    }

    public static Connection getConnection() throws SQLException {
        if (dataSource==null){
            //初始化
            //为使其在多线程情况下线程安全 因为servlet本质上是多线程环境 所以应该用二次加锁判断 所以先synchron...
            synchronized (DButil.class){
                if (dataSource==null){
                    InitDataSource();
                }
            }
        }
        //不为空时
        return dataSource.getConnection();
    }
}
