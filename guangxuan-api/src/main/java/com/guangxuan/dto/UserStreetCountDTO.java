package com.guangxuan.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author zhuolin
 * @Date 2020/1/3
 */
@Data
public class UserStreetCountDTO {

    private Integer count;

    private BigDecimal rentRate;

    private BigDecimal price;

    private List<UserStreetDTO> userStreetDTOList;
}
