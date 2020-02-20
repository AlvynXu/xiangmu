package com.guangxuan.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author zhuolin
 * @Date 2020/1/2
 */
@Data
public class RemoveDTO implements Serializable {

    @NotNull(message = "获取信息失败")
    private Long id;

}
