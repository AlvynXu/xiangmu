package com.guangxuan.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * <p>
 *
 * </p>
 *
 * @author zhuolin
 * @since 2019-12-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "MallItemInfoDetail对象", description = "")
public class MallItemInfoDetail extends Model<MallItemInfoDetail> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long mallItemInfoId;

    @NotNull(message = "文件路径不能为空")
    @ApiModelProperty(value = "文件路径")
    private String filePath;

    @NotBlank(message = "描述不能为空")
    @ApiModelProperty(value = "描述")
    private String description;

    @NotBlank(message = "序号不能为空")
    @ApiModelProperty(value = "序号")
    private Integer seq;

    @ApiModelProperty(value = "类型 1字符串 2图片 3视频")
    private Integer type;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
