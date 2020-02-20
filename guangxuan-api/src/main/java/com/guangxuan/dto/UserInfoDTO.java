package com.guangxuan.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author zhuolin
 * @Date 2019/11/28
 */
@Data
public class UserInfoDTO implements Serializable {

    private BigDecimal profit;

    private Integer team;

    private Integer message;

    private Integer totalMessage;

    private BigDecimal vipPrice;

    private BigDecimal balance;
}
