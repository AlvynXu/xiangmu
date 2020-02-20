package com.guangxuan.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhuolin
 * @since 2019-11-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Booth对象", description="")
public class Booth extends Model<Booth> {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "展位编码")
    private String boothCode;

    @ApiModelProperty(value = "展位名称")
    private String boothName;

    @ApiModelProperty(value = "售卖状态 0 付款中 1有效 2过期")
    private Integer status;

    private Long streetId;

    private BigDecimal price;

    @TableField("is_saved")
    private Boolean saved;

    @ApiModelProperty(value = "使用状态 1闲置 2待租 3挂展 4出租")
    private Integer useStatus;



    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
