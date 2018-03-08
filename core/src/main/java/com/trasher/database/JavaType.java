package com.trasher.database;

import java.util.Arrays;

/**
 * java类型枚举类
 * Created by RoyChan on 2018/2/2.
 */
public enum JavaType {

    Date("java.util.Date"),
    String("java.lang.String"),
    Integer("java.lang.Integer"),
    Double("java.lang.Double");

    private String name;

    JavaType(String name) {
        this.name = name;
    }

    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public static JavaType fromName(String name) {
        return Arrays.stream(values())
                .filter(v -> v.name.equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

}
