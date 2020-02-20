package com.guangxuan.vo.admin.filter;

import lombok.Data;

import java.io.Serializable;

/**
 * @author deofly
 * @since 2019-05-29
 */
@Data
public class AddressApplyFilter implements Serializable {

    private static final long serialVersionUID = -148783339590288948L;

    private Integer status;

    private Integer type;
}
