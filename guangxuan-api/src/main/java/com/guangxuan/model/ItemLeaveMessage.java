package com.guangxuan.model;

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
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * <p>
 * 项目留言
 * </p>
 *
 * @author zhuolin
 * @since 2020-01-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "ItemLeaveMessage对象", description = "项目留言")
public class ItemLeaveMessage extends Model<ItemLeaveMessage> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "留言信息", required = true)
    @NotEmpty(message = "留言信息不能为空")
    @Length(max = 100,message = "请输如100字以内")
    private String message;

    @ApiModelProperty(value = "时间")
    private Date time;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "项目id", required = true)
    @NotNull(message = "未获取到该项目")
    private Long itemId;

    @ApiModelProperty(value = "是否删除")
    @TableField(fill = FieldFill.INSERT, value = "is_deleted")
    private Boolean deleted;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
