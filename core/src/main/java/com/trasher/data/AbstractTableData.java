package com.trasher.data;

import com.dolpins.domains.Table;

import java.util.Date;
import java.util.Map;

/**
 * Created by RoyChan on 2018/2/5.
 */
public abstract class AbstractTableData {
    private String id;
    private Date createDate;
    private Date lastModify;

    protected Table table;
    protected TableIdMap tableIdMap;

    public AbstractTableData(Table table, TableIdMap tableIdMap){
        this.table = table;
        this.tableIdMap = tableIdMap;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastModify() {
        return lastModify;
    }

    public void setLastModify(Date lastModify) {
        this.lastModify = lastModify;
    }

    public abstract Map<String, Object> produceData();

    protected String createDateData(){
        return "now()";
    }


}
