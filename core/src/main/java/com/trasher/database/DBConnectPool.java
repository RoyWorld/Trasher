package com.trasher.database;

import com.dolpins.util.DataSourceProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 数据库连接池
 * Created by RoyChan on 2018/2/8.
 */
@Component(value = "dbConnectPool")
public class DBConnectPool {

    //connection pool
    private LinkedBlockingQueue<Connection> connections;

    //connection count
    @Value("${database.connection.count}")
    private int count;

    /**
     * init() after injection is done
     */
    @PostConstruct
    private void init(){
        connections = new LinkedBlockingQueue<>();
        for (int i = 0; i < count; i++) {
            connections.add(DataSourceProvider.getNewConnection());
        }
    }

    /**
     * get a db connection
     * @return
     */
    public Connection getConnection() throws InterruptedException {
        return connections.take();
    }

    /**
     * release a db connection
     * @param connection
     */
    public void releaseConnection(Connection connection){
        connections.add(connection);
    }

    /**
     * return the remained number of connections
     * @return
     */
    public int getRemainConnections(){
        return connections.size();
    }
}
