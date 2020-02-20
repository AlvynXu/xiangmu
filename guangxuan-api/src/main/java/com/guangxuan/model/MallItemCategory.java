package com.guangxuan.model;

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

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 商城商品类目
 * </p>
 *
 * @author zhuolin
 * @since 2019-11-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "MallItemCategory对象", description = "商城商品类目")
public class MallItemCategory extends Model<MallItemCategory> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "id", required = true)
    private Long id;
//
//    @ApiModelProperty(value = "上级类目id")
//    private Integer parentId;

    private String title;

    private String icon;

    @ApiModelProperty(value = "类目级别", required = true)
    @NotNull(message = "类目级别不能为空")
    private Integer level;

    @ApiModelProperty(value = "是否可见", required = true)
    @NotNull(message = "是否可见不能为空")
    private Boolean visible;

    @ApiModelProperty(value = "权重", required = true)
    @NotNull(message = "权重不能为空")
    private Integer sort;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "编码")
    private String code;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
