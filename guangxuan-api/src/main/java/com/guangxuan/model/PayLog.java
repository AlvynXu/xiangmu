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
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 支付日志
 * </p>
 *
 * @author zhuolin
 * @since 2019-11-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "PayLog对象", description = "支付日志")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayLog extends Model<PayLog> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long userId;

    @ApiModelProperty(value = "支付来源")
    private Integer source;

    @ApiModelProperty(value = "支付方式(1:微信 2:支付宝 3:余额)")
    private Integer payType;

    @ApiModelProperty(value = "应付金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "后续费")
    private BigDecimal fee;

    @ApiModelProperty(value = "实际支付金额")
    private BigDecimal realAmount;

    @ApiModelProperty(value = "扩展字段1(订单编号等)")
    private String extend1;

    @ApiModelProperty(value = "扩展字段2")
    private String extend2;

    @ApiModelProperty(value = "扩展字段3")
    private String extend3;

    private String product;
    /**
     * @see com.guangxuan.constant.PayConstant
     */
    private Integer status;

    private Long goodsId;

    private Integer num;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(exist = false)
    private String typeString;

    @TableField(exist = false)
    private String time;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
