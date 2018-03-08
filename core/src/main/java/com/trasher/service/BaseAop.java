package com.trasher.service;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Created by RoyChan on 2018/2/13.
 */
@Aspect
public class BaseAop {

    @Pointcut("within(zyj.report.service.BaseDataService)")
    public void logBefore() {}	//通过执行不同的Pointcut是执行不同的切面方法


}
