package com.trasher.thread;


import com.trasher.algorithm.AOVNetwork;
import com.trasher.database.DBModel;
import com.trasher.util.LoadContext;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 整个App初始运行类
 * Created by RoyChan on 2018/2/9.
 */
public class AppStart extends BaseTask {

    private static final String tag = "AppStart";

    private ThreadPoolExecutor threadPoolExecutor;

    long startTime;
    long endTime;

    public AppStart() {
        super(tag);
        logger = LogFactory.getLog(this.getClass());
    }

    public void run(){
        try{
            printLog("-----------App start-----------");

            startTime = System.currentTimeMillis();

            LoadContext loadContext = LoadContext.getInstance();
            ApplicationContext context = loadContext.getContext();

            Integer[][] tableModel = DBModel.getTableModel();
            System.out.println(Arrays.deepToString(tableModel));

            AOVNetwork aovNetwork = adjTableToAOVNetwork(tableModel);

            List<Integer> sortList = aovNetwork.topologicalSorting();

            System.out.println(sortList);

            Collections.reverse(sortList);

            System.out.println("sortList:" + sortList);

            threadPoolExecutor = (ThreadPoolExecutor) context.getBean("executorService");

            int taskCount = 10;

            CountDownLatch countDownLatch = new CountDownLatch(taskCount);

            for (int i = 0; i < taskCount; i++) {
                threadPoolExecutor.submit(new Task("task-" + i, context, countDownLatch, sortList));
            }

            while (threadPoolExecutor.getCompletedTaskCount() < taskCount){
                long timeOfUsing = calcTime(startTime, System.currentTimeMillis());
                printDebugLog(String.format("task complete: %s", threadPoolExecutor.getCompletedTaskCount()));
                Thread.sleep(1000);
            }

            countDownLatch.await();

            endTime = System.currentTimeMillis();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            threadPoolExecutor.shutdown();
            printLog("-----------App end-----------");

            String timeStr = String.valueOf(calcTime(startTime, endTime));

            printLog(String.format("time - %s", timeStr));
        }

    }

    /**
     * 将tableModel转化成AOV网
     * @param tableModel
     * @return
     */
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

    /**
     *
     * @param startTime
     * @param endTime
     * @return
     */
    private long calcTime(long startTime, long endTime){
       return TimeUnit.MILLISECONDS.toSeconds(endTime - startTime);
    }
}
