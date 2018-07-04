package com.trasher.database;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * DB操作类
 * Created by RoyChan on 2018/2/5.
 */
@Component(value = "dbOperator")
public class DBOperator {

    private final static Log log = LogFactory.getLog(DBOperator.class);

    @Resource(name = "dbConnectPool")
    DBConnectPool dbConnectPool;

    private static final Lock lock = new ReentrantLock();

    /**
     * 执行参数完整的insert sql
     * @param sql
     * @throws SQLException
     */
   public void executeInsert(String sql) throws SQLException, InterruptedException {
       Connection connection = dbConnectPool.getConnection();
       Statement statement = connection.createStatement();
       statement.execute(sql);
       dbConnectPool.releaseConnection(connection);
       log.info("dbConnectPool : " + dbConnectPool.getRemainConnections());
   }

    /**
     * 执行increment sql
     * @throws SQLException
     */
    public int executeQueryCount(String tableName) throws SQLException, InterruptedException {
        String sql = String.format("SELECT TABLE_ROWS\n" +
                "FROM information_schema.tables \n" +
                "WHERE table_name='%s'\n" +
                "  AND table_schema = DATABASE();", tableName);
        Connection connection = dbConnectPool.getConnection();
        Statement statement = connection.createStatement();
        statement.execute(sql);
        ResultSet resultSet = statement.getResultSet();
        dbConnectPool.releaseConnection(connection);
        log.info("dbConnectPool : " + dbConnectPool.getRemainConnections());
        if (resultSet.next()){
            return resultSet.getInt("TABLE_ROWS");
        }
        return 0;
    }

}
