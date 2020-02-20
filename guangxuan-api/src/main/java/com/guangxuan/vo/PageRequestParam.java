package com.guangxuan.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author deofly
 * @since 2019-04-28
 */
@Data
public class PageRequestParam implements Serializable {

    private static final long serialVersionUID = 1069636638547236960L;

    public static final int MIN_PAGE_LIMIT = 10;
    public static final int MAX_PAGE_LIMIT = 50;

    @ApiModelProperty(required = true)
    @NotNull(message = "页码不能为空")
    private Integer offset;
    @ApiModelProperty(required = true)
    @NotNull(message = "数据条数不能为空")
    private Integer limit;

    public int getOffset() {
        if (offset == null || offset <= 0) {
            return 1;
        }

        return offset;
    }

    public int getLimit() {
        if (limit == null || limit < MIN_PAGE_LIMIT) {
            return MIN_PAGE_LIMIT;
        }

        if (limit > MAX_PAGE_LIMIT) {
            return MAX_PAGE_LIMIT;
        }

        return limit;
    }
}
