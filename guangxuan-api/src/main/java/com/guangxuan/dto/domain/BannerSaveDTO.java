package com.guangxuan.dto.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author zhuolin
 * @Date 2019/12/19
 */
@Data
public class BannerSaveDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @NotNull(message = "跳转商品id或者是链接不能为空")
    @ApiModelProperty(value = "跳转商品id或者是链接")
    private String itemId;

    @NotBlank(message = "图片地址不能为空")
    @ApiModelProperty(value = "图片地址")
    private String imageUrl;

    @NotNull(message = "是否可见不能为空")
    @ApiModelProperty(value = "可见")
    private Boolean visible;

    @NotNull(message = "权重不能为空")
    @ApiModelProperty(value = "权重")
    private Integer sort;


    /**
     * 天数
     */
    @NotNull(message = "天数不能为空")
    @ApiModelProperty(value = "天数")
    private Integer days;


    private Integer categoryId;

    @NotNull(message = "总价不能为空")
    private BigDecimal price;

    /**
     * 全国可直接传入-1 首页传入0
     */
    @ApiModelProperty(value = "区域编码 -1为首页 其余为各自地方的编码")
    @NotEmpty(message = "请选择区域")
    private List<String> areaCodes;

}
