package com.trasher.database;

import com.dolpins.TableFactory;
import com.dolpins.domains.Column;
import com.dolpins.domains.Table;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 根据DB中的table生成各个表的关系模型
 * Created by RoyChan on 2018/1/5.
 */
public class DBModel {

    private static List<Table> tableList;
    private static Map<String, Table> tableMap;

    /**
     * the list of all tables
     * @return
     */
    public static List<Table> getTables(){
        if (tableList == null) {
            tableMap = new HashMap<>();
            tableList = TableFactory.getInstance().getAllTables();
            tableList.stream().forEach(table -> {
                tableMap.put(table.getSqlName(), table);
            });
        }
        return tableList;
    }

    /**
     * tableMap, key: tableName, value: table
     * @return
     */
    public static Map<String, Table> getTableMap(){
        return tableMap;
    }

    public static Table getTable(String tableName){
        return tableMap.get(tableName);
    }

    /**
     * 返回数据库的关系模型
     * @return
     */
    public static Integer[][] getTableModel(){

        //list of all table
        List<Table> tableList = getTables();
        //table count
        int tableNum = tableList.size();

        //model inital
        Integer[][] relationModel = new Integer[tableNum][tableNum];
        for (int i = 0; i < relationModel.length; i++) {
            Arrays.fill(relationModel[i], 0);
        }

        //tableMap key: tableName, value: table
        Map<String, Integer> tableListMap = new HashMap<>();
        for (int i = 0; i < tableList.size(); i++){
            tableListMap.put(tableList.get(i).getSqlName(), i);
        }

        //create DBModel
        tableList.stream().forEach(table -> {
            for (Column column : table.getColumns()){
                String[] splitArray = column.getRemarks().split("\\|");
                if (splitArray.length != 1){
                    String foreignTable = splitArray[1];
                    int rowNum = tableListMap.get(table.getSqlName());
                    int columnNum = tableListMap.get(foreignTable);
                    relationModel[rowNum][columnNum] = 1;
                }
            }
        });

        System.out.println(Arrays.deepToString(relationModel));

        return relationModel;
    }

}
