package com.trasher.data.table;

import com.dolpins.domains.Table;
import com.trasher.data.AbstractTableData;
import com.trasher.data.TableIdMap;
import com.trasher.database.JavaType;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * Created by RoyChan on 2018/2/5.
 */
public class IATableData extends AbstractTableData {

    private final String[] charArray = new String[]{
            "Jakc",
            "Nayc",
            "Cidny",
            "Ryo",
            "Ijure"
    };

    public IATableData(Table table, TableIdMap tableIdMap) {
        super(table, tableIdMap);
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
                    data.put(column.getSqlName(), "'" + getName() + "'");
                }
            }
        });
        return data;
    }

    private String getName(){
        Random random = new Random();
        int i = random.nextInt(1000) % 5;
        return charArray[i];
    }
}
