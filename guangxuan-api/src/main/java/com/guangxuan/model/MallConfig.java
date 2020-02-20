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
 * 商城基本配置
 * </p>
 *
 * @author zhuolin
 * @since 2019-11-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "MallConfig对象", description = "商城基本配置")
public class MallConfig extends Model<MallConfig> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "id", required = true)
    private Integer id;

    @ApiModelProperty(value = "单位鸡蛋可兑换的价格")
    private BigDecimal eggUnitPrice;

    @ApiModelProperty(value = "普通合伙人充值门槛")
    private BigDecimal threshold0;

    @ApiModelProperty(value = "vip价格", required = true)
    private BigDecimal threshold1;

    @ApiModelProperty(value = "展位价格", required = true)
    private BigDecimal threshold2;

    @ApiModelProperty(value = "地主价格", required = true)
    private BigDecimal threshold3;

    @ApiModelProperty(value = "城区合伙人充值门槛", required = true)
    private BigDecimal threshold4;

    @ApiModelProperty(value = "城市合伙人充值门槛", required = true)
    private BigDecimal threshold5;

    @ApiModelProperty(value = "游客直推返佣费率", required = true)
    private BigDecimal directRate0;

    @ApiModelProperty(value = "VIP直推返佣费率", required = true)
    private BigDecimal directRate1;

    @ApiModelProperty(value = "展商直推返佣费率", required = true)
    private BigDecimal directRate2;

    @ApiModelProperty(value = "地主直推返佣费率", required = true)
    private BigDecimal directRate3;

    @ApiModelProperty(value = "BOSS直推返佣费率", required = true)
    private BigDecimal directRate4;

    private BigDecimal directRate5;

    @ApiModelProperty(value = "vip街道返佣费率", required = true)
    private BigDecimal globalRate1;

    @ApiModelProperty(value = "展商街道返佣费率", required = true)
    private BigDecimal globalRate2;

    @ApiModelProperty(value = "地主街道全网返佣费率", required = true)
    private BigDecimal globalRate3;

    @ApiModelProperty(value = "BOSS全网返佣费率", required = true)
    private BigDecimal globalRate4;

    private BigDecimal globalRate5;

    private BigDecimal villagePrice;

    private BigDecimal streetPrice;

    private BigDecimal cityPrice;

    private BigDecimal purchaseStreetThreshold;

    private Integer purchaseStreetMaxNumber;

    private BigDecimal purchaseVillageThreshold;

    private Integer purchaseVillageMaxNumber;

    @ApiModelProperty(value = "游客全网返佣费率", required = true)
    private BigDecimal indirectRate0;

    @ApiModelProperty(value = "vip全网返佣费率", required = true)
    private BigDecimal indirectRate1;

    @ApiModelProperty(value = "展主全网返佣费率", required = true)
    private BigDecimal indirectRate2;

    @ApiModelProperty(value = "地主全网返佣费率", required = true)
    private BigDecimal indirectRate3;

    @ApiModelProperty(value = "BOSS全网返佣费率", required = true)
    private BigDecimal indirectRate4;

    @ApiModelProperty(value = "城市合伙人全网返佣费率", required = true)
    private BigDecimal indirectRate5;

    private BigDecimal platformRate;

    private BigDecimal platformDivideRate;

    private BigDecimal platformIndirectRate;

    private BigDecimal platformAreaRate;

    private String purchaseStreetConfigs;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
