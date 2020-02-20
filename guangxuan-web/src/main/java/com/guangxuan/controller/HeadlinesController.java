package com.guangxuan.controller;


import com.guangxuan.config.ApiVersion;
import com.guangxuan.config.ApiVersionConstant;
import com.guangxuan.dto.Result;
import com.guangxuan.service.HeadlinesService;
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
 *  前端控制器
 * </p>
 *
 * @author zhuolin
 * @since 2019-12-15
 */
@RestController
@RequestMapping("/headlines")
@Api(value = "头条controller", tags = {"头条接口"})
public class HeadlinesController {

    @Resource
    private HeadlinesService headlinesService;

    @GetMapping("/pageList")
    @ApiOperation("分页获取头条")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页数据条数", required = true, dataType = "int"),
            @ApiImplicitParam(name = "areaType", value = "地区类型 1街道 2区县 3城市 4全国", required = true, dataType = "int"),
            @ApiImplicitParam(name = "areaCode", value = "地区类型 当为全国时可以不传", required = false, dataType = "String")
    })
    @ApiVersion(group = ApiVersionConstant.FAP_APP101)
    public Result<?> pageList(Integer areaType, String areaCode,  Integer page, Integer size) {
        return Result.success(headlinesService.pageList(areaType, areaCode, page, size), null);
    }


}

