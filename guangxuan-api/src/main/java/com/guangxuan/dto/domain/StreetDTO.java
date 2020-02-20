package com.guangxuan.dto.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zhuolin
 * @Date 2019/12/23
 */
@Data
public class StreetDTO implements Serializable {

    private Long id;

    private Long userId;

    private String phone;

    private String city;

    private String district;

    private String name;

    private String code;

    private Integer count;


}
