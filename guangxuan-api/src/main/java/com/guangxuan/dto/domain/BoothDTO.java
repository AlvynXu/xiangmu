package com.guangxuan.dto.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zhuolin
 * @Date 2019/12/19
 */
@Data
public class BoothDTO implements Serializable {

    private Long id;

    private String code;

    private String city;

    private String district;

    private String street;

    private String phone;

    private String rentUserPhone;

    private Long leftDate;

    private Long leftRentDate;

    private Integer rentTotalDate;

    private Integer useStatus;

    private Integer rentType;

    private String marketUserPhone;

    private String marketRentUserPhone;

    private Date marketEndTime;

    private Date endTime;
}
