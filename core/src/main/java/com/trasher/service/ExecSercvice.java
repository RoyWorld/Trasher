package com.trasher.service;

import com.dolpins.domains.Table;
import com.trasher.data.DataFactory;
import com.trasher.data.TableIdMap;
import com.trasher.database.DBOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 执行数据生成
 * Created by RoyChan on 2018/2/13.
 */
@Service(value = "execService")
public class ExecSercvice {

    public static final Logger logger = LoggerFactory.getLogger(ExecSercvice.class);

    @Autowired
    DataFactory dataFactory;

    @Autowired
    DBOperator dbOperator;

    @Autowired
    List<Table> tables;

    public void exec(List<Integer> sortList, TableIdMap tableIdMap) throws SQLException, InterruptedException {
        String sql = null;
        try {
            for (int j = 0; j < sortList.size(); j++) {
                Table table = tables.get(sortList.get(j));

                Map<String, Object> data = dataFactory.generateData(table.getSqlName(), tableIdMap);
                String columnName = "";
                String columnData = "";

                Iterator<String> iterator = data.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
//                    if (key.equals("id")){
//                        continue;
//                    }
                    columnName = columnName + key;
                    columnData = columnData + data.get(key);
                    if (iterator.hasNext()) {
                        columnName = columnName + ", ";
                        columnData = columnData + ", ";
                    }
                }

                sql = "insert into `" + table.getSqlName() + "` (" + columnName + ") values (" + columnData + ");";

                dbOperator.executeInsert(sql);

            }
        } catch (SQLException e){
            logger.error(sql);
            throw new SQLException(e);
        }

    }
}
