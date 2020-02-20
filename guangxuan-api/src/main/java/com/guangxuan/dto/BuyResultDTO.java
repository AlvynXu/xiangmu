package com.guangxuan.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zhuolin
 * @Date 2019/12/4
 */
@Data
public class BuyResultDTO implements Serializable {

    private Integer type;

    private String code;

    private String city;

    private String province;

    private String district;

    private String street;

    private String time;

}
