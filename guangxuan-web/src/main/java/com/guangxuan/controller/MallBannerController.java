package com.guangxuan.controller;


import com.guangxuan.config.ApiVersion;
import com.guangxuan.config.ApiVersionConstant;
import com.guangxuan.dto.Result;
import com.guangxuan.service.MallBannerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 商城banner配置 前端控制器
 * </p>
 *
 * @author zhuolin
 * @since 2019-11-27
 */
@RestController
@RequestMapping("/mallBanner")
@Api(value = "bannercontroller", tags = {"banner接口"})
public class MallBannerController {

    @Resource
    private MallBannerService mallBannerService;

    @GetMapping("getBanner")
    @ApiOperation("获取banner")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "areaCode", value = "地区编码", required = true, dataType = "-1时表示全国")
    })
    @ApiVersion(group = ApiVersionConstant.FAP_APP101)
    public Result<?> getBanners(String areaCode, Long categoryId) {
        return Result.success(mallBannerService.getBanner(areaCode, categoryId), null);
    }

}

