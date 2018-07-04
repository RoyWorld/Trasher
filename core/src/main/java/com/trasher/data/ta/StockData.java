package com.trasher.data.ta;

import com.dolpins.domains.Column;
import com.dolpins.domains.Table;
import com.trasher.data.AbstractTableData;
import com.trasher.data.TableIdMap;
import com.trasher.data.UnitDataUtil;
import com.trasher.util.LoadContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by RoyChan on 2018/3/19.
 */
public class StockData extends AbstractTableData {

    UnitDataUtil unitDataUtil;

    public StockData(Table table, TableIdMap tableIdMap) {
        super(table, tableIdMap);
        unitDataUtil = LoadContext.getBean("unitDataUtil");
    }

    @Override
    public Map<String, Object> produceData() {
        Map<String, Object> data = new HashMap<>();
        table.getColumns().stream().forEach(column -> {
            if (column.getSqlName().equals("id")){
                tableIdMap.put(table.getSqlName(), String.valueOf(unitDataUtil.incrId(table.getSqlName())));
            } else if (column.getRemarks().split("\\|").length != 1){
                String spilt[] = column.getRemarks().split("\\|");
                data.put(column.getSqlName(), "'" + tableIdMap.get(spilt[1]) + "'");
            } else if (column.getSqlName().equals("product_name")){
                data.put(column.getSqlName(), unitDataUtil.wrapString(getName()));
            } else if (column.getSqlName().equals("amount")){
                data.put(column.getSqlName(), getCount());
            } else if (column.getSqlName().equals("price")){
                data.put(column.getSqlName(), getPrice());
            } else if (column.getSqlName().equals("cost")){
                data.put(column.getSqlName(), getPrice() * 0.2);
            } else {
                setData(column, data);
            }
        });
        return data;
    }

    private void setData(Column column, Map<String, Object> data){
        if (column.getSqlName().equals("status")){
            data.put(column.getSqlName(), unitDataUtil.status());
        }else if (column.getSqlName().equals("create_time") || column.getSqlName().equals("modified_time")){
            data.put(column.getSqlName(), unitDataUtil.date());
        }else if (column.getSqlName().equals("creator") || column.getSqlName().equals("modified")){
            data.put(column.getSqlName(), unitDataUtil.wrapString("robot"));
        }else {
            data.put(column.getSqlName(), unitDataUtil.wrapString(""));
        }
    }

    private Integer getPrice(){
        Random random = new Random();
        int i = random.nextInt(1000);
        return i;
    }

    private Integer getCount(){
        Random random = new Random();
        int i = random.nextInt(50);
        return i;
    }

    private final String[] charArray = new String[]{
            "banana",
            "apple",
            "watermelon",
            "grape",
            "plum",
            "orange",
            "cantaloupe",
            "cherry",
            "mulberry"
    };

    private String getName(){
        Random random = new Random();
        int i = random.nextInt(1000) % charArray.length;
        return charArray[i];
    }
}

