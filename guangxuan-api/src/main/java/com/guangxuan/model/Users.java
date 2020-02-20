package com.guangxuan.model;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
 * @since 2019-11-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("guangxuan_users")
@ApiModel(value = "Users对象", description = "")
public class Users extends Model<Users> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "手机号")
    private String phone;


    @ApiModelProperty(value = "密码")
    @JsonIgnore
    private String password;

    @JsonIgnore
    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @JsonIgnore
    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @JsonIgnore
    @ApiModelProperty(value = "是否删除")
    @TableField(fill = FieldFill.INSERT, value = "is_deleted")
    private Boolean deleted;

    @ApiModelProperty(value = "推广人id")
    private Long promoterId;

    @ApiModelProperty(value = "个人专属6位邀请码")
    private String regCode;

    @ApiModelProperty(value = "用户等级")
    private Integer level;

    @ApiModelProperty(value = "累计收益")
    private BigDecimal profit;

    @ApiModelProperty(value = "余额")
    private BigDecimal balance;

    @ApiModelProperty(value = "是否是会员")
    private Boolean isVip;

    @ApiModelProperty(value = "团队收益")
    private BigDecimal teamProfit;

    @JsonIgnore
    private String wxOpenId;

    private Integer chooseBoothCount;

    private Integer totalChooseBoothCount;

    @TableField(value = "is_major")
    private Boolean major;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
