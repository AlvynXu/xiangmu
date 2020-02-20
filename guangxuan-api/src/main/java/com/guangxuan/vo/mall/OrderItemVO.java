package com.guangxuan.vo.mall;

import lombok.Data;

import java.io.Serializable;

/**
 * @author deofly
 * @since 2019-05-15
 */
@Data
public class OrderItemVO implements Serializable {

    private static final long serialVersionUID = 8764403545483047520L;

    private Integer itemId;

    private Integer quantity;
}
