package com.trasher.data;

import com.trasher.database.DBOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

/**
 * Created by RoyChan on 2018/3/19.
 */
@Component("unitDataUtil")
public class UnitDataUtil {

    @Autowired
    DBOperator dbOperator;

    public String password(){
        return wrapString("123456");
    }

    public Integer status(){
        return new Integer(1);
    }

    public String date(){
        return "now()";
    }

    public Integer incrId(String tableName){
        Integer count = 0;
        try {
            count =  dbOperator.executeQueryCount(tableName) + 1;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return count;
    }

    public String wrapString(String s){
        return "'" + s +"'";
    }
}
