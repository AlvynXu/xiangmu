package com.guangxuan.model;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 街道
 * </p>
 *
 * @author zhuolin
 * @since 2019-11-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Street对象", description="街道")
public class Street extends Model<Street> {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(required = true)
    private String name;

    @ApiModelProperty(required = true)
    private String areaCode;

    @ApiModelProperty(value = "售卖价格")
    private BigDecimal price;

    @ApiModelProperty(required = true)
    private String code;

    @ApiModelProperty(value = "售卖状态 0 付款中 1有效 2过期")
    private Integer status;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
