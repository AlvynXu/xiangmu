package com.guangxuan.model;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalTime;
import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * <p>
 * 意见反馈
 * </p>
 *
 * @author zhuolin
 * @since 2019-12-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Feedback对象", description="意见反馈")
public class Feedback extends Model<Feedback> {

    private static final long serialVersionUID=1L;

    private Long id;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "时间")
    private Date time;

    @NotBlank(message = "内容不能为空")
    @Size(min=10,max = 100,message = "允许输入长度为10-100")
    @ApiModelProperty(value = "内容",required = true)
    private String content;

    @ApiModelProperty(value = "是否已查看")
    private Boolean status;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
