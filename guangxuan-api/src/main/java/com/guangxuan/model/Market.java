package com.guangxuan.model;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
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
 * @since 2019-12-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Market对象", description="")
public class Market extends Model<Market> {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "价格")
    private BigDecimal price = BigDecimal.ZERO;

    @ApiModelProperty(value = "天数")
    private Integer days;

    @ApiModelProperty(value = "租赁结束时间")
    private Date rentEndTime;

    @ApiModelProperty(value = "求租人")
    private Long userId;

    @ApiModelProperty(value = "展位id")
    private Long boothId;

    @ApiModelProperty(value = "平台费率")
    private BigDecimal rate;

    @ApiModelProperty(value = "数量")
    private Integer number;

    @ApiModelProperty(value = "街道编码")
    private String areaCode;

    @ApiModelProperty(value = "出租人")
    private Long rentUserId;

    @ApiModelProperty(value = "市场结束时间")
    private Date markertEndTime;

    @ApiModelProperty(value = "2 求租 1出租 ")
    private Integer type;
    /**
     * 0 待租 1未使用 2 使用中
     */
    private Integer status;

    private Long orderId;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
