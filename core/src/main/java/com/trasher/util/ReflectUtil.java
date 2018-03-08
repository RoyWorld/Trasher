package com.trasher.util;

import org.springframework.cglib.core.ReflectUtils;

import java.lang.reflect.Method;

/**
 * 反射公共类
 * Created by RoyChan on 2018/2/6.
 */
public class ReflectUtil {

    public static Class reflectClass(String className) {
        Class clazz = null;
        try {
            clazz = Class.forName(className);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clazz;
    }

    /**
     * 构造一个clazz类型的实例
     * @param clazz
     * @param classes
     * @param args
     * @return
     */
    public static Object newInstance(Class clazz, Class[] classes, Object[] args) {
        Object object = null;
        try {
            object = ReflectUtils.newInstance(clazz, classes, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }

    /**
     * 调用clazz的无参函数
     * @param object
     * @param clazz
     * @param methodName
     * @return
     */
    public static Object invokeMethod(Object object, Class clazz, String methodName) {
        Object result = null;
        try {
            Method method = clazz.getMethod(methodName);
            result = method.invoke(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
