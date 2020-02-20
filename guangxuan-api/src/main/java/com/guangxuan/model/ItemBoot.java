package com.guangxuan.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhuolin
 * @since 2019-11-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ItemBoot对象", description="")
public class ItemBoot extends Model<ItemBoot> {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @NotNull(message = "未获取项目")
    private Long itemId;

    private Long boothId;

    @NotEmpty(message = "未获取展位信息")
    @TableField(exist = false)
    private ArrayList<Long> boothIds;

    private Boolean enable;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
