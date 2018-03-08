package com.trasher.thread;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**所有task都继承这个basetask
 * Created by RoyChan on 2018/2/8.
 */
public abstract class BaseTask implements Runnable{
    protected static Log logger;

    private String taskName;

    public BaseTask(String name){
        this.taskName = name;
        logger = LogFactory.getLog(this.getClass());
    }

    /**
     * print log message
     * @param message
     */
    protected void printLog(String message){
        logger.info(this.taskName + ": " + message);
    }

    /**
     * print log message
     * @param message
     */
    protected void printDebugLog(String message){
        logger.debug(this.taskName + ": " + message);
    }

}
