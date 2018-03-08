package com.trasher;

import com.dolpins.domains.Table;
import com.dolpins.tools.XMLHelper;
import com.trasher.algorithm.AOVNetwork;
import com.trasher.data.DataFactory;
import com.trasher.database.DBModel;
import com.trasher.database.DBOperator;
import com.trasher.util.ReflectUtil;
import org.junit.Test;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by RoyChan on 2018/1/5.
 */
public class TestCode {

    @Test
    public void splitTest() {
        AOVNetwork aovNetwork = new AOVNetwork(14);

        aovNetwork.addEdges(0, 11, 5, 4);
        aovNetwork.addEdges(1, 8, 4, 2);
        aovNetwork.addEdges(2, 9, 5, 6);
        aovNetwork.addEdges(3, 13, 2);
        aovNetwork.addEdges(4, 7);
        aovNetwork.addEdges(5, 12, 8);
        aovNetwork.addEdges(6, 5);
        aovNetwork.addEdges(8, 7);
        aovNetwork.addEdges(9, 11, 10);
        aovNetwork.addEdges(10, 13);
        aovNetwork.addEdges(12, 9);

        System.out.println(aovNetwork.toString());

        System.out.println(aovNetwork.topologicalSorting());
    }

    private AOVNetwork adjTableToAOVNetwork(Integer[][] tableModel) {

        AOVNetwork aovNetwork = new AOVNetwork(tableModel.length);

        for (int i = 0; i < tableModel.length; i++) {
            for (int j = 0; j < tableModel[i].length; j++) {
                if (tableModel[i][j] == 1) {
                    aovNetwork.addEdge(i, j);
                }
            }
        }

        return aovNetwork;
    }

    @Test
    public void testDB() {
        Integer[][] tableModel = DBModel.getTableModel();
        System.out.println(Arrays.deepToString(tableModel));

        AOVNetwork aovNetwork = adjTableToAOVNetwork(tableModel);

        List<Integer> sortList = aovNetwork.topologicalSorting();

        System.out.println(sortList);

        Collections.reverse(sortList);

        System.out.println(sortList);
    }

    @Test
    public void testDB2() throws SQLException, InterruptedException {

        URL contextURL = this.getClass().getClassLoader().getResource("component.xml");
        FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext(contextURL.getFile());

        Integer[][] tableModel = DBModel.getTableModel();
        System.out.println(Arrays.deepToString(tableModel));

        AOVNetwork aovNetwork = adjTableToAOVNetwork(tableModel);

        List<Integer> sortList = aovNetwork.topologicalSorting();

        System.out.println(sortList);

        Collections.reverse(sortList);

        System.out.println("sortList:" + sortList);

        List<Table> tables = (List<Table>) context.getBean("tableList");

        DataFactory dataFactory = (DataFactory) context.getBean("dataFactory");

        for (int i = 0; i < sortList.size(); i++) {
            Table table = tables.get(sortList.get(i));
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

            String sql = "insert into " + table.getSqlName() + " (" + columnName + ") values (" + columnData + ");";

            System.out.println(sql);

            DBOperator dbOperator = (DBOperator) context.getBean("dbOperator");
            dbOperator.executeInsert(sql);
        }

    }

    @Test
    public void testClass() {
        ClassLoader cl = ClassLoader.getSystemClassLoader();

        URL[] urls = ((URLClassLoader) cl).getURLs();

        for (URL url : urls) {
            String filePath = url.getFile();
            if (filePath.contains("Trasher") && filePath.contains("core")) {
                System.out.println(url.getFile());
                File file = new File(filePath + "/com/trasher/algorithm");
                File[] files = file.listFiles();
                if (files != null) {
                    for (int i = 0; i < files.length; i++) {
                        System.out.println(files[i].getName());
                    }
                }
            }
        }

    }

    @Test
    public void testXML1() throws ParserConfigurationException, IOException, SAXException, TransformerException {
        InputStream inputFile = this.getClass().getClassLoader().getResourceAsStream("table_data.xml");
        XMLHelper.NodeData nd = new XMLHelper().parseXML(inputFile);

        Map table = nd.attributes;
        List columns = nd.childs;
        System.out.println(table);
        System.out.println(columns);
//        System.out.println(nd);
    }

    @Test
    public void testReflect(){
        Class randomTableDataClass = ReflectUtil.reflectClass("com.trasher.data.table.RandomTableData");

        Table table = DBModel.getTable("tb_a");

        Object[] objects = new Object[]{table};
        Class[] classes = new Class[]{Table.class};
        Object randomTableDataObject = ReflectUtil.newInstance(randomTableDataClass, classes, objects);
        Map<String, Object> result = (Map<String, Object>) ReflectUtil.invokeMethod(randomTableDataObject, randomTableDataClass, "produceData");

        result.entrySet().stream().forEach(key->{
            System.out.println(String.format("key: %s, value: %s", key, result.get(key)));
        });
    }

    @Test
    public void testReflect1() throws ClassNotFoundException {
        List<Table> tables = DBModel.getTables();

        Class randomTableDataClass = Class.forName("com.trasher.data.table.RandomTableData");

        Class[] classes = new Class[]{Table.class};
        Object[] args = new Object[]{tables.get(0)};
        Object randomTableDataObject = ReflectUtils.newInstance(randomTableDataClass, classes, args);

        Map<String, Object> result = (Map<String, Object>) ReflectUtil.invokeMethod(randomTableDataObject, randomTableDataClass, "produceData");

        result.keySet().stream().forEach(key->{
            System.out.println(String.format("key: %s, value: %s", key, result.get(key)));
        });
    }


    @Test
    public void testSpring(){
        URL context = this.getClass().getClassLoader().getResource("table_data.xml");
        FileSystemXmlApplicationContext fileSystemXmlApplicationContext = new FileSystemXmlApplicationContext(context.getFile());

        List<Table> tables = (List<Table>) fileSystemXmlApplicationContext.getBean("tableList");

        System.out.println(tables.get(0).getName());

        DataFactory dataFactory = (DataFactory) fileSystemXmlApplicationContext.getBean("dataFactory");

        Map<String, Object> result = dataFactory.generateData("tb_a");

        result.keySet().stream().forEach(key->{
            System.out.println(String.format("key: %s, value: %s", key, result.get(key)));
        });

    }

    @Test
    public void tests(){
        int[] p = new int[]{1, 2, 5, 5, 5, 9, 0};

        int temp1 = p[0], temp2 = p[1];
        for (int i = 2; i < p.length; i++){
            if (p[i] > temp1){
                temp2 = temp1;
                temp1 = p[i];
            }else if (p[i] > temp2){
                temp2 = p[i];
            }
        }

        System.out.println(temp2);
    }

}
