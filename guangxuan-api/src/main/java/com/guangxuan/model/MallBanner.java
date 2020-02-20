package com.guangxuan.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.google.gson.internal.$Gson$Preconditions;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 商城banner配置
 * </p>
 *
 * @author zhuolin
 * @since 2019-11-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "MallBanner对象", description = "商城banner配置")
public class MallBanner extends Model<MallBanner> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "跳转商品id或者是链接",required = true)
    @NotBlank(message = "跳转商品id或者是链接不能为空")
    private String itemId;

    @ApiModelProperty(value = "图片地址",required = true)
    @NotNull(message = "图片地址不能为空")
    private String imageUrl;

    @ApiModelProperty(value = "是否可见",required = true)
    @NotNull(message = "是否可见不能为空")
    private Boolean visible;

    @ApiModelProperty(value = "权重",required = true)
    @NotNull(message = "权重不能为空")
    private Integer sort;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    /**
     * 地区编码
     */
    private String areaCode;
    /**
     * 类型
     */
    @ApiModelProperty(value = "跳转类型 1地主 2展位 3项目 4h5")
    private Integer type;
    /**
     * 天数
     */
    @ApiModelProperty(value = "天数",required = true)
    @NotNull(message = "天数不能为空")
    private Integer days;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    private Integer categoryId;

    @ApiModelProperty(value = "总价")
    private BigDecimal price;
    /**
     * 全国可直接传入-1 首页传入0
     */
    @ApiModelProperty(value = "区域编码 -1为首页 其余为各自地方的编码",required = true)
    @NotEmpty(message = "请选择区域")
    @TableField(exist = false)
    private List<String> areaCodes;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
