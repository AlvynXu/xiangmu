package com.guangxuan.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 收货地址
 * </p>
 *
 * @author zhuolin
 * @since 2019-11-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="MallAddress对象", description="收货地址")
public class MallAddress extends Model<MallAddress> {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;

    private String phone;

    private Integer userId;

    private Integer areaId;

    private Integer streetId;

    private Integer villageId;

    @ApiModelProperty(value = "地址补充说明")
    private String ext;

    @ApiModelProperty(value = "是否默认地址")
    private Boolean isDefault;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
