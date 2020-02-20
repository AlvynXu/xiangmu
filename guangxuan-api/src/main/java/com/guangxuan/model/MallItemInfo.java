package com.guangxuan.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
@ApiModel(value = "MallItemInfo对象", description = "")
public class MallItemInfo extends Model<MallItemInfo> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "id", required = true)
    private Long id;

    @ApiModelProperty(value = "商品类目id", required = true)
    @NotNull(message = "商品类目不能为空")
    private Long categoryId;

    /**
     * @see com.guangxuan.constant.ItemStatus
     */
    @ApiModelProperty(value = "-1未审核(审核失败) 0 审核中 1发布中 2未发布(审核成功) ", required = true)
    private Integer status;

    @ApiModelProperty(value = "编码")
    private String itemCode;

    @ApiModelProperty(value = "浏览量")
    private Integer views;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "审核时间")
    private Date auditTime;

    @ApiModelProperty(value = "发布时间")
    private Date publishTime;

    @ApiModelProperty(value = "是否被举报")
    private Boolean hasReport;

    @ApiModelProperty(value = "举报处理状态 -1无举报 0未处理 1举报无效 2举报通过")
    private Integer reportStatus;
    /**
     * 发布人
     */
    private Long userId;

    @ApiModelProperty(value = "展示图路径", required = true)
    private String bannerPath;

    @ApiModelProperty(value = "展示文字", required = true)
    @NotBlank(message = "展示文字不能为空")
    private String description;

    private String provinceCode;
    private String districtCode;
    private String streetCode;
    private String addressDetail;

    private String phone;

    private Integer shareCount;

    @TableField(exist = false)
    private String cityName;

    @TableField(exist = false)
    private String districtName;

    @TableField(exist = false)
    private String provinceName;

    @TableField(exist = false)
    private String streetName;

    private Integer sort;

    @ApiModelProperty(value = "=审核结果", required = true)
    private String auditDescription;

    @TableField(exist = false)
    private List<MallItemInfoDetail> detailList;

    @TableField(exist = false)
    private MallItemCategory category;

    @TableField(exist = false)
    private Users user;

    @TableField(exist = false)
    private Boolean collect;

    @TableField(exist = false)
    private String regCode;

    @TableField(exist = false)
    private String downloadPath;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
