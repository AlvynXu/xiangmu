package com.guangxuan.model;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;
import java.util.Date;

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
 * @since 2019-12-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "头条对象", description = "")
public class Headlines extends Model<Headlines> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long itemId;

    @ApiModelProperty(value = "权重")
    private Integer sort;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "价格")
    private BigDecimal price;
    /**
     * 编码 -1为全国 0为首页
     */
    private String areaCode;

    private Integer status;

    private Integer type;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
