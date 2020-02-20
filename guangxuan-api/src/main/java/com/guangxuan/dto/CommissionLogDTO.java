package com.guangxuan.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author zhuolin
 * @Date 2019/12/3
 */
@Data
@Builder
public class CommissionLogDTO implements Serializable {

    private String name;

    private Long id;

    private String time;

    private BigDecimal amount;

    private String type;

}
