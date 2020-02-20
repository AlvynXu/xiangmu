package com.guangxuan.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.guangxuan.model.Users;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @author zhuolin
 * @Date 2019/11/23
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Component
@ApiModel("用户的登陆")
public class LoginUser  implements Serializable {

    /**
     * 验证码
     */
//    private String phoneCode;
    /**
     * 用户
     */
    private Users user;

    /**
     * 用户token验证(header的键名)
     */
    @ApiModelProperty("用户token")
    private String token;

    @ApiModelProperty("是否为新用户")
    private Boolean flag;
}

