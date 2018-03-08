package com.trasher.service;

import com.dolpins.domains.Table;
import com.trasher.data.DataFactory;
import com.trasher.database.DBOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by RoyChan on 2018/2/13.
 */
@Service(value = "execService")
public class ExecSercvice {

    @Autowired
    DataFactory dataFactory;

    @Autowired
    DBOperator dbOperator;

    @Autowired
    List<Table> tables;

    public void exec(List<Integer> sortList) throws SQLException, InterruptedException {
        String sql = null;
        try {
            for (int j = 0; j < sortList.size(); j++) {
                Table table = tables.get(sortList.get(j));

                Map<String, Object> data = dataFactory.generateData(table.getSqlName());
                String columnName = "";
                String columnData = "";

                Iterator<String> iterator = data.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    columnName = columnName + key;
                    columnData = columnData + data.get(key);
                    if (iterator.hasNext()) {
                        columnName = columnName + ", ";
                        columnData = columnData + ", ";
                    }
                }

                sql = "insert into " + table.getSqlName() + " (" + columnName + ") values (" + columnData + ");";

                dbOperator.executeInsert(sql);

            }
        } catch (SQLException e){
            throw new SQLException(sql);
        }

    }
}
