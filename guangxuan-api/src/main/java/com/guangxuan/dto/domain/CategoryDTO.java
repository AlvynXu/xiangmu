package com.guangxuan.dto.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.guangxuan.model.MallItemCategory;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zhuolin
 * @Date 2019/12/19
 */
@Data
@ApiModel(value = "MallItemCategory对象", description = "商城商品类目")
public class CategoryDTO implements Serializable {


    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String title;

    @ApiModelProperty(value = "权重")
    private Integer sort;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "编码")
    private String code;

    @ApiModelProperty(value = "浏览量")
    private Integer visibleCount;

    @ApiModelProperty(value = "广告量")
    private Integer itemCount;

    @ApiModelProperty(value = "投放中")
    private Integer usingCount;

    @ApiModelProperty(value = "未投放")
    private Integer unUseCount;



}
