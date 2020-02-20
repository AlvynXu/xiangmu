package com.guangxuan.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author zhuolin
 * @Date 2019/12/3
 */
@Data
public class ProfitDTO implements Serializable {

    private BigDecimal profit;
}
