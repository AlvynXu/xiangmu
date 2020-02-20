package com.guangxuan.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author zhuolin
 * @Date 2020/1/8
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DownloadPathDTO implements Serializable {

    @ApiModelProperty("安卓下载地址")
    private String androidPath;

    @ApiModelProperty("ios下载地址")
    private String iosPath;
}
