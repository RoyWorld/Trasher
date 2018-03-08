package com.trasher.data.table;

import com.dolpins.domains.Table;
import com.trasher.data.AbstractTableData;
import com.trasher.data.TableIdMap;
import com.trasher.database.JavaType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by RoyChan on 2018/2/5.
 */
public class RandomTableData extends AbstractTableData {

    public RandomTableData(Table table, TableIdMap tableIdMap) {
        super(table, tableIdMap);
    }

    private String createStringData(){
        return "hehee";
    }

    private Integer createIntegerData(){
        return new Integer(1000);
    }

    private Double createDoubleData(){
        return new Double(100.90);
    }

    @Override
    public Map<String, Object> produceData() {
        Map<String, Object> data = new HashMap<>();
        table.getColumns().stream().forEach(column -> {
            if (column.getSqlName().equals("id")){
                String uuid = String.valueOf(UUID.randomUUID());
                data.put(column.getSqlName(), "'" + uuid + "'");
                tableIdMap.put(table.getSqlName(), uuid);
            } else if (column.getRemarks().split("\\|").length != 1){
                String spilt[] = column.getRemarks().split("\\|");
                data.put(column.getSqlName(), "'" + tableIdMap.get(spilt[1]) + "'");
            }else {
                JavaType type = JavaType.fromName(column.getJavaType());
                if (type == JavaType.Date){
                    data.put(column.getSqlName(), createDateData());
                }
                if (type == JavaType.String){
                    data.put(column.getSqlName(), "'" + createStringData() + "'");
                }
                if (type == JavaType.Integer){
                    data.put(column.getSqlName(), createIntegerData());
                }
                if (type == JavaType.Double){
                    data.put(column.getSqlName(), createDoubleData());
                }
            }
        });
        return data;
    }
}
