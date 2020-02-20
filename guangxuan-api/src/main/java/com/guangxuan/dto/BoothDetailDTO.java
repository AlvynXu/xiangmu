package com.guangxuan.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author zhuolin
 * @Date 2020/1/2
 */
@Data
public class BoothDetailDTO {
    private Long id;

    private String code;

    private String province;

    private String city;

    private String district;

    private String street;

    private Long userId;

    private Date expireTime;

}
