package com.guangxuan.enumration;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * @author zhuolin
 * @Date 2019/12/4
 */
@Getter
@AllArgsConstructor
public enum LevelEnum implements Serializable {

    LEVEL_0(0, "游客"),
    LEVEL_1(1, "会员"),
    LEVEL_2(2, "展主"),
    LEVEL_3(3, "地主"),
    LEVEL_4(4, "庄家"),
    ;

    Integer level;
    String levelName;


    public static LevelEnum valueOfLevel(Integer level) {
        if (level == null) {
            return null;
        }
        for (LevelEnum levelEnum : LevelEnum.values()) {
            if (levelEnum.getLevel().equals(level)) {
                return levelEnum;
            }
        }
        return null;
    }
}
