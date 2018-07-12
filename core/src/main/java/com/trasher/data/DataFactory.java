package com.trasher.data;

import com.dolpins.domains.Table;
import com.trasher.data.table.RandomTableData;
import com.trasher.util.LoadContext;
import com.trasher.util.ReflectUtil;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 数据工厂
 * 根据class来生成相应的数据
 * Created by RoyChan on 2018/2/6.
 */
public class DataFactory {

    //tableMap, key: tableName, key: table
    @Resource(name = "tableMap")
    private Map<String, Table> tableMap;

//    @Resource(name = "tableIdMap")
    private TableIdMap tableIdMap;

    public static final Lock lock = new ReentrantLock();

    /**
     * factory method
     * 通过clazz和tableName用反射找到对应table的数据生成方法
     * @param clazz
     * @param tableName
     * @return
     * @throws ClassNotFoundException
     */
    public Map<String, Object> generate(String clazz, String tableName) throws ClassNotFoundException {
        Table table = tableMap.get(tableName);

        Class tableDataClass = Class.forName(clazz);

        Class[] classes = new Class[]{Table.class, TableIdMap.class};
        Object[] args = new Object[]{table, this.tableIdMap};
        Object tableDataObject = ReflectUtil.newInstance(tableDataClass, classes, args);

        return (Map<String, Object>) ReflectUtil.invokeMethod(tableDataObject, tableDataClass, "produceData");

    }

    public void setTableMap(Map tableMap) {
        this.tableMap = tableMap;
    }

    public Map getTableMap() {
        return tableMap;
    }

    /**
     * 根据tableName生成数据
     * @param tableName
     * @return
     */
    public Map<String, Object> generateData(String tableName, TableIdMap tableIdMap){
        Map<String, Object> data;
        lock.lock();
        try {
            this.tableIdMap = tableIdMap;
            data = LoadContext.getBean(tableName);
        } catch (Exception e){
            RandomTableData randomTableData = new RandomTableData(tableMap.get(tableName), tableIdMap);
            data = randomTableData.produceData();
        }
        lock.unlock();
        return data;
    }
}
