package com.trasher.thread;

import com.trasher.service.ExecSercvice;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 数据生成任务
 * Created by RoyChan on 2018/2/9.
 */
public class Task extends BaseTask {

    private ApplicationContext context;
    private CountDownLatch countDownLatch;
    private List<Integer> sortList;

    public Task(String name, ApplicationContext context, CountDownLatch countDownLatch, List<Integer> sortList) {
        super(name);
        logger = LogFactory.getLog(this.getClass());
        this.context = context;
        this.countDownLatch = countDownLatch;
        this.sortList = sortList;
    }

    @Override
    public void run() {
        printLog("-----------task start-----------");

        try {
            ExecSercvice execSercvice = (ExecSercvice) context.getBean("execService");

            execSercvice.exec(sortList);

            countDownLatch.countDown();
        } catch (SQLException e) {
            e.printStackTrace();
            printLog(e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }

        printLog("-----------task end-----------");
    }


}
