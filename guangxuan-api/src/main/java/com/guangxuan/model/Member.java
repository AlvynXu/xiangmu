package com.guangxuan.model;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * 用户
 * </p>
 *
 * @author zhuolin
 * @since 2019-11-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Member对象", description="用户")
public class Member extends Model<Member> {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String nickname;

    private String avatarUrl;

    private String phoneNumber;

    private Integer fromUserId;

    private String inviteCode;

    private String openid;

    private String payOpenid;

    private String unionid;

    private BigDecimal money;

    @ApiModelProperty(value = "支付金额")
    private BigDecimal payMoney;

    @ApiModelProperty(value = "虚拟财产")
    private BigDecimal virtualMoney;

    private Integer friendCount;

    private Integer teamCount;

    @ApiModelProperty(value = "小区数")
    private Integer villageCount;

    @ApiModelProperty(value = "街道数")
    private Integer streetCount;

    private BigDecimal eggCount;

    private Integer buyEggCount;

    private BigDecimal speed;

    private BigDecimal allSpeed;

    private BigDecimal oneSecondEggCount;

    private BigDecimal speedCardCount;

    private Integer chickenCount;

    private Integer level;

    private String realName;

    private String alipayAccount;

    private Integer isViolation;

    private Integer isTester;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    private Integer lastLoginTime;

    private Integer notLoginStatus;

    private Integer version;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
