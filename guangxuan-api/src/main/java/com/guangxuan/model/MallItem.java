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
 * 项目信息
 * </p>
 *
 * @author zhuolin
 * @since 2019-11-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="MallItem对象", description="项目信息")
public class MallItem extends Model<MallItem> {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long categoryId;

    @ApiModelProperty(value = "商品状态(0:待审核 1:进行中 2:已结束)")
    private Integer status;

    @ApiModelProperty(value = "最终资金")
    private BigDecimal totalMoney;

    @ApiModelProperty(value = "天数")
    private Integer days;

    @ApiModelProperty(value = "目标资金")
    private BigDecimal targetMoney;

    @ApiModelProperty(value = "商品头图")
    private String headPics;

    @ApiModelProperty(value = "城市编码")
    private String areaCode;

    @ApiModelProperty(value = "区域编码")
    private String districtCode;

    @ApiModelProperty(value = "详细地址")
    private String detailAddress;

    private String title;

    @ApiModelProperty(value = "吊牌价")
    private BigDecimal tagPrice;

    @ApiModelProperty(value = "售卖价")
    private BigDecimal price;

    @ApiModelProperty(value = "补充价格")
    private BigDecimal extraPrice;

    @ApiModelProperty(value = "库存")
    private Integer inventory;

    @ApiModelProperty(value = "销量")
    private Integer sales;

    @ApiModelProperty(value = "浏览量")
    private Integer views;

    @ApiModelProperty(value = "图文详情富文本")
    private String descInfo;

    private String descPics;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    private BigDecimal platformRate;

    private BigDecimal platformDivideRate;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
