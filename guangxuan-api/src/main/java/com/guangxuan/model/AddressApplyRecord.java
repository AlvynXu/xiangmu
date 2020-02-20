package com.guangxuan.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * 地址申请记录
 * </p>
 *
 * @author zhuolin
 * @since 2019-11-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="AddressApplyRecord对象", description="地址申请记录")
public class AddressApplyRecord extends Model<AddressApplyRecord> {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    @ApiModelProperty(value = "地址类型(1:省 2:市 3:区 4:街道 5:小区)")
    private Integer type;

    private Integer parentId;

    @ApiModelProperty(value = "提交内容")
    private String content;

    @ApiModelProperty(value = "申请状态(0:审核中 1:审核成功 2:审核失败)")
    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    private Date checkTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
