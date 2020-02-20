package com.guangxuan.vo.mall;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author deofly
 * @since 2019-06-04
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StreetVO implements Serializable {

    private static final long serialVersionUID = -4503796824608486766L;

    private Integer id;

    private String name;

    private BigDecimal price;

    private List<String> fullName;
}
