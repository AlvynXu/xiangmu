package com.guangxuan.model;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
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
 * @since 2019-12-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="BannerArea对象", description="")
public class BannerArea extends Model<BannerArea> {

    private static final long serialVersionUID=1L;

    private Long id;

    private Long bannerId;

    @ApiModelProperty(value = "区域编码 全国为-1 首页为0")
    private String areaCode;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
