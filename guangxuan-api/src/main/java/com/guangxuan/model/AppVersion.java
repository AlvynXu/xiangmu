package com.guangxuan.model;

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
 * @since 2019-12-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "AppVersion对象", description = "")
public class AppVersion extends Model<AppVersion> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "安卓版本")
    private String appVersion;

    @ApiModelProperty(value = "下载路径")
    private String downloadPth;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "0 安卓 1ios")
    private Integer type;

    @ApiModelProperty(value = "是否有效（只允许存在一个有效）")
    private Boolean enable;

    @ApiModelProperty(value = "时间")
    private Date time;

    @ApiModelProperty(value = "冗余字段1")
    private String ext1;

    @ApiModelProperty(value = "冗余字段2")
    private String ext2;

    @ApiModelProperty(value = "冗余字段3")
    private String ext3;

    private Boolean forceUpdate;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
