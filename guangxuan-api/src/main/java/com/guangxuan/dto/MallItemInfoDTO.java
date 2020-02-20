package com.guangxuan.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * @author zhuolin
 * @Date 2019/12/16
 */
@Data
public class MallItemInfoDTO implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "商品类目id")
    @NotNull(message = "商品类目不能为空")
    private Long categoryId;

    @ApiModelProperty(value = "展示文字")
    @NotBlank(message = "展示文字不能为空")
    @Size(min = 1, max = 40, message = "标题长度为1-40位")
    private String description;

    private String provinceCode;

    private String districtCode;

    private String streetCode;

    @ApiModelProperty(value = "地址")
    @NotBlank(message = "地址不能为空")
    private String addressDetail;

    @ApiModelProperty(value = "联系方式")
    @NotBlank(message = "联系方式不能为空")
    @Size( max = 20, message = "请输入20位以内为数字或字符")
    private String phone;

    private String companyName;


//    @TableField(exist=false)
//    private List<MallItemInfoDetailDTO> detailList;


}
