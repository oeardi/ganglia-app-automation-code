package com.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.testng.Reporter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static com.utils.JdbcUtils.getConnection;
import static com.utils.JdbcUtils.getStatement;

/**
 * 操作数据库
 * （用于自动化脚本执行之前，初始化一些数据。）
 *
 * @author 冷枫红舞
 */
@Slf4j
public class BaseActionDBOperateUtils {

    private static Connection connection = null;
    private static Statement statement = null;

    static {
        connection = getConnection();
        statement = getStatement(connection);
    }

    /**
     * 根据传入的 sql 语句，执行数据库操作。
     *
     * @param sql
     */
    public static void dbOperate(String sql) {
        log.info("[调试信息] [dbOperate]");
        Reporter.log("【调试信息】 [dbOperate]");

        if (StringUtils.isEmpty(sql)) {
            log.info("[调试信息] [dbOperate] 传入的 SQL 语句为 NULL，dbOperate() 方法终止执行。[return;]");
            Reporter.log("【调试信息】 [dbOperate] 传入的 SQL 语句为 NULL，dbOperate() 方法终止执行。[return;]");
            return;
        }
        log.info("[调试信息] [dbOperate] 传入的 SQL 语句为：[{}]", sql);
        Reporter.log("【调试信息】 [dbOperate] 传入的 SQL 语句为：[" + sql + "]");

        if (sql.startsWith("insert") || sql.startsWith("INSERT")) {
            insert(sql);
//            log.info("[调试信息] [dbOperate][insert] SQL 执行完毕。[{}]", sql);
        } else if (sql.startsWith("delete") || sql.startsWith("DELETE")) {
            delete(sql);
//            log.info("[调试信息] [dbOperate][delete] SQL 执行完毕。[{}]", sql);
        } else if (sql.startsWith("update") || sql.startsWith("UPDATE")) {
            update(sql);
//            log.info("[调试信息] [dbOperate][update] SQL 执行完毕。[{}]", sql);
        } else if (sql.startsWith("select") || sql.startsWith("SELECT")) {
            select(sql);
//            log.info("[调试信息] [dbOperate][select] SQL 执行完毕。[{}]", sql);
        } else {
            log.info("[调试信息] [dbOperate] 传入的 SQL 语句不是 [增、删、改、查] 功能，dbOperate() 方法终止执行。[return;]");
            Reporter.log("【调试信息】 [dbOperate] 传入的 SQL 语句不是 [增、删、改、查] 功能，dbOperate() 方法终止执行。[return;]");
            return;
        }

        log.info("[调试信息] [dbOperate] 方法执行完毕。");
        Reporter.log("【调试信息】 [dbOperate] 方法执行完毕。");
    }

    /**
     * @param sql
     */
    private static void insert(String sql) {
        if (StringUtils.isEmpty(sql)) {
            log.info("[调试信息] [insert] 传入参数 [sql == null]，insert() 方法终止执行。[return null;]");
            return;
        }

        int result = 0;
        try {
            result = statement.executeUpdate(sql);
            if (result > 0) {
                log.info("[调试信息] [insert] INSERT 执行成功。");
                Reporter.log("【调试信息】 [insert] INSERT 执行成功。");
            } else {
                log.info("[调试信息] [insert] INSERT 语句执行结果 [result <= 0]，数据 [INSERT] 失败。");
                Reporter.log("【调试信息】 [insert] INSERT 语句执行结果 [result <= 0]，数据 [INSERT] 失败。");
            }
            log.info("[调试信息] [insert] 输出 SQL 执行结果 result：[{}]", result);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param sql
     */
    private static void delete(String sql) {
        if (StringUtils.isEmpty(sql)) {
            log.info("[调试信息] [delete] 传入参数 [sql == null]，delete() 方法终止执行。[return null;]");
            return;
        }

        int result = 0;
        try {
            result = statement.executeUpdate(sql);
            if (result > 0) {
                log.info("[调试信息] [delete] DELETE 执行成功。");
                Reporter.log("【调试信息】 [delete] DELETE 执行成功。");
            } else {
                log.info("[调试信息] [delete] DELETE 执行结果 [result <= 0]，数据 [DELETE] 失败。");
                Reporter.log("【调试信息】 [delete] DELETE 执行结果 [result <= 0]，数据 [DELETE] 失败。");
            }
            log.info("[调试信息] [delete] 输出 SQL 执行结果 result：[{}]", result);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param sql
     */
    private static void update(String sql) {
        if (StringUtils.isEmpty(sql)) {
            log.info("[调试信息] [update] 传入参数 [sql == null]，update() 方法终止执行。[return null;]");
            return;
        }

        int result = 0;
        try {
            result = statement.executeUpdate(sql);
            if (result > 0) {
                log.info("[调试信息] [update] UPDATE 执行成功。");
                Reporter.log("【调试信息】 [update] UPDATE 执行成功。");
            } else {
                log.info("[调试信息] [update] UPDATE 执行结果 [result <= 0]，数据 [UPDATE] 失败。");
                Reporter.log("【调试信息】 [update] UPDATE 执行结果 [result <= 0]，数据 [UPDATE] 失败。");
            }
            log.info("[调试信息] [update] 输出 SQL 执行结果 result：[{}]", result);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param sql
     */
    private static void select(String sql) {
        if (StringUtils.isEmpty(sql)) {
            log.info("[调试信息] [select] 传入参数 [sql == null]，select() 方法终止执行。[return null;]");
            return;
        }

        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                String id = (String) resultSet.getString("id");
                log.info("[调试信息] [select] SELECT 执行成功。");
                Reporter.log("【调试信息】 [select] SELECT 执行成功。");
                log.info("[调试信息] [select] 输出查询结果的 id：[{}]", id);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
