package com.guangxuan.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author zhuolin
 * @Date 2019/12/17
 */
@Data
public class MarketDTO implements Serializable {

    private Long totalCount;

    private Long soldCount;

    private BigDecimal price;

    private Integer status;

    private Integer days;
}
