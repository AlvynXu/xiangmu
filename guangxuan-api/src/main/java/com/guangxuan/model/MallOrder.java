package com.guangxuan.model;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 商城订单
 * </p>
 *
 * @author zhuolin
 * @since 2019-11-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="MallOrder对象", description="商城订单")
public class MallOrder extends Model<MallOrder> {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String orderId;

    private Integer userId;

    @ApiModelProperty(value = "订单状态(1:待付款 10:待发货 11:待收货 20:待评价 30:售后 -1:关闭)")
    private Integer status;

    private BigDecimal amount;

    @ApiModelProperty(value = "收货地址")
    private Integer addressId;

    @ApiModelProperty(value = "支付方式(1:微信 2:支付宝 3:余额)")
    private Integer payType;

    @ApiModelProperty(value = "支付状态(0:提交支付 1:支付成功 -1:支付失败)")
    private Integer payStatus;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "提交支付时间")
    private LocalDateTime payTime;

    @ApiModelProperty(value = "完成支付时间")
    private LocalDateTime paidTime;


    @Override
    protected Serializable pkVal() {
        return this.orderId;
    }

}
