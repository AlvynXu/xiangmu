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
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Transient;

/**
 * <p>
 * 返佣日志
 * </p>
 *
 * @author zhuolin
 * @since 2019-11-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@ApiModel(value = "CommissionLog对象", description = "返佣日志")
public class CommissionLog extends Model<CommissionLog> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long userId;

    @ApiModelProperty(value = "佣金来源用户id")
    private Long fromUserId;

    @ApiModelProperty(value = "返佣金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "支付总金额")
    private BigDecimal totalAmount;

    @ApiModelProperty(value = "返佣费率")
    private BigDecimal rate;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT)
    private Integer type;



    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
