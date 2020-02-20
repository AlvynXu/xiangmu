package com.guangxuan.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.guangxuan.dto.Result;
import com.guangxuan.model.AppVersion;
import com.guangxuan.service.AppVersionService;
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
 * 前端控制器
 * </p>
 *
 * @author zhuolin
 * @since 2019-12-04
 */
@RestController
@RequestMapping("/appVersion")
@Api(value = "app版本controller", tags = {"app版本接口"})
public class AppVersionController {

    @Resource
    private AppVersionService appVersionService;

    @GetMapping(value = "/getAppVersion")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "类型", required = true, dataType = "int")
    })
    @ApiOperation(value = "获取App版本")
    public Result<?> getAppVersion(Integer type) {
        return Result.success(appVersionService.getOne(new LambdaQueryWrapper<AppVersion>().eq(AppVersion::getType, type).eq(AppVersion::getEnable, true)),null);
    }

}

