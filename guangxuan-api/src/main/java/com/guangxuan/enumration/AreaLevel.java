package com.guangxuan.enumration;

import lombok.Getter;

/**
 * @author zhuolin
 * @Date 2019/12/18
 */
public enum AreaLevel {

    COUNTRY(0, "国家"),
    PROVINCE(1, "省"),
    CITY(2, "市"),
    DISTRICT(3, "县/区");

    @Getter
    private int id;

    @Getter
    private String name;

    AreaLevel(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
