package com.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * 工具类
 *
 * @author 冷枫红舞
 */
@Slf4j
public class JdbcUtils {

    private JdbcUtils() {
    }

    public static void myClose(Statement statement, Connection connection) {
        log.info("[调试信息] [myClose]");

        try {
            statement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        log.info("[调试信息] [myClose] 释放 statement.close() 和 connection.close() 执行完毕。");
    }

    /**
     * 获取 Connection
     *
     * @return
     */
    public static Connection getConnection() {
//        log.info("[调试信息] [getConnection()]");

        InputStream inputStream = JdbcUtils.class.getClassLoader().getResourceAsStream("db.properties");

        Properties properties = new Properties();
        try {
            properties.load(inputStream);
            inputStream.close();

            String driver = properties.getProperty("jdbc.driver");
            String url = properties.getProperty("jdbc.url");
            String username = properties.getProperty("jdbc.username");
            String password = properties.getProperty("jdbc.password");

            if (StringUtils.isEmpty(driver) || StringUtils.isEmpty(url) || StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
                log.info("[调试信息] [getConnection] 获取数据库连接信息 [dirver | url | username | password] 为 null，getConnection() 方法终止执行。[return null;]");
                return null;
            }

//            log.info("[调试信息] [getConnection] 输出数据库连接信息：");
//            log.info("[调试信息] [getConnection] [{}]", driver);
//            log.info("[调试信息] [getConnection] [{}]", url);
//            log.info("[调试信息] [getConnection] [{}]", username);
//            log.info("[调试信息] [getConnection] [{}]", password);

            Class.forName(driver);

            Connection connection = DriverManager.getConnection(url, username, password);
            log.info("[调试信息] [getConnection()] 获取 Connection 对象，[return connection;]");
            return connection;


        } catch (IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

//        log.info("[调试信息] [getConnection()] 没有获取到 Connection 对象，程序返回 null。");
        return null;
    }

    /**
     * @return Statement
     */
    public static Statement getStatement(Connection connection) {
//        log.info("[调试信息] [getStatement()]");

        if (null == connection) {
            log.info("[调试信息] [getStatement()] 获取到 [connection == null]，getStatement() 方法终止执行。[return null;]");
            return null;
        }

        Statement statement = null;
        try {
            statement = connection.createStatement();
            if (null == statement) {
                log.info("[调试信息] [getStatement()] 获取到 [statement == null]，getStatement() 方法终止执行。[return null;]");
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

//        log.info("[调试信息] [getStatement()] 获取 Statement 对象，[return statement;]");
        return statement;
    }

    @Test
    public void test() {
        String sql = "select * from";
        Connection connection = null;
        Statement statement = null;

        try {
            connection = getConnection();
            statement = getStatement(connection);

            ResultSet resultSet = null;
            resultSet = statement.executeQuery(sql);
            System.out.println("[Test Case] resultSet = " + resultSet);

            myClose(statement, connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
