package com.guangxuan.vo.admin.filter;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author deofly
 * @since 2019-04-27
 */
@Data
public class MallItemFilter implements Serializable {

    private static final long serialVersionUID = -5620638025738747525L;

    private Integer status;

    private Integer category;

    private BigDecimal minPrice;

    private BigDecimal maxPrice;
}
