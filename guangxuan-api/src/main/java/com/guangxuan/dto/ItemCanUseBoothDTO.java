package com.guangxuan.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zhuolin
 * @Date 2019/12/19
 */
@Data
public class ItemCanUseBoothDTO implements Serializable {

    private Long id;

    private String code;

    private String province;

    private String city;

    private String district;

    private String street;

    private Long leftDate;

    private Date expireEndTime;

    private Date rentEndTime;
}
