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
 * 街道合伙人
 * </p>
 *
 * @author zhuolin
 * @since 2019-11-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="StreetPartnerOrder对象", description="街道合伙人")
public class StreetPartnerOrder extends Model<StreetPartnerOrder> {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long streetId;

    private Long userId;

    private Integer status;

    private Integer payType;

    private BigDecimal amount;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    private Date completeTime;

    private String orderNo;

    private Integer orderStatus;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
