package com.guangxuan.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author zhuolin
 * @Date 2019/12/30
 */
@Data
public class UserStreetDTO {
    private String streetName;
    private String districtName;
    private String cityName;
    private String streetCode;
    private Boolean hasBooth;
    private Integer days;
    private BigDecimal price;
}
