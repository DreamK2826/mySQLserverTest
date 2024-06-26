package com.dreamk.mysqlservertest.utils;

import android.os.NetworkOnMainThreadException;

//import org.mariadb.jdbc.Connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * function： 数据库工具类，连接数据库用
 */
public class JDBCUtils {
    private static final String TAG = "JDBCUtils";
//    private static final String driver = "org.mariadb.jdbc.Driver";// Sql驱动
    private static final String driver = "com.mysql.jdbc.Driver";// Sql驱动


    /**
     * 获取 Connection
     *
     * @param user     SQL user
     * @param password SQL user password
     * @param dbName   数据库名称
     * @param ip       地址
     * @param port     端口
     * @return 获取到的 connection
     */
    public static Connection getConnection(String user, String password, String dbName, String ip, int port) {
        Connection connection = null;
        try {
            Class.forName(driver);// 动态加载类
            // 尝试建立到给定数据库URL的连接
//            connection = (Connection) DriverManager.getConnection("jdbc:mariadb://"
//                    + ip + ":" + port + "/" + dbName, user, password);
            connection = (Connection) DriverManager.getConnection("jdbc:mysql://"
                    + ip + ":" + port + "/" + dbName, user, password);
//            + "?characterEncoding=utf-8"

        } catch (SQLException | ClassNotFoundException | NetworkOnMainThreadException e) {
            e.printStackTrace();
        }

        return connection;
    }
}
