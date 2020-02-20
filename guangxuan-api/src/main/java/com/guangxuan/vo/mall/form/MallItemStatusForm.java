package com.guangxuan.vo.mall.form;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author deofly
 * @since 2019-05-22
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MallItemStatusForm implements Serializable {

    private static final long serialVersionUID = 2188697310971080121L;

    @NotNull(message = "商品不存在")
    private Integer id;

    @NotNull(message = "商品状态不能为空")
    private Integer status;
}
