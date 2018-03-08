package com.trasher.util;

import org.springframework.beans.BeansException;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.net.URL;

/**
 * 加载xml配置文件
 * Created by RoyChan on 2018/2/8.
 */
public class LoadContext {
    private static final String xml = "app_config.xml";
    private static final LoadContext loadContext = new LoadContext(xml);

    private static FileSystemXmlApplicationContext context;

    private LoadContext(String xml) {
        URL contextURL = this.getClass().getClassLoader().getResource(xml);
        context = new FileSystemXmlApplicationContext(contextURL.getFile());
    }

    public static LoadContext getInstance(){
        return loadContext;
    }

    public FileSystemXmlApplicationContext getContext() {
        return context;
    }

    public static <T> T getBean(Class<T> clazz) throws BeansException {
        return context.getBean(clazz);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) throws BeansException {
        return (T) context.getBean(name);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name, Class<T> clazz) throws BeansException {
        return (T) context.getBean(name);
    }
}
