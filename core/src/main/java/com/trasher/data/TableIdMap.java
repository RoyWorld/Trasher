package com.trasher.data;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局table id表
 * 保存各个表的id值
 * Created by RoyChan on 2018/2/5.
 */
//@Component("tableIdMap")
//@Scope(value = "prototype")
public class TableIdMap {
    protected static Log logger = LogFactory.getLog(TableIdMap.class);

    private Map<String, String> tableIdMap = new HashMap<>();

    public void put(String tableName, String id){
        tableIdMap.put(tableName, id);
    }

    public String get(String tableName){
       return tableIdMap.get(tableName);
    }

    public void clean(){
        tableIdMap.clear();
    }

    public void printId(){
        logger.debug(tableIdMap.toString());
    }
}
