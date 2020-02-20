package com.guangxuan.controller;


import com.guangxuan.dto.Result;
import com.guangxuan.service.StreetService;
import com.guangxuan.shiro.ThreadLocalCurrentUser;
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
 * 街道 前端控制器
 * </p>
 *
 * @author zhuolin
 * @since 2019-11-27
 */
@RestController
@RequestMapping("/street")
@Api(value = "街道信息controller", tags = {"街道信息接口"})
public class StreetController {

    @Resource
    private StreetService streetService;

    @ApiOperation("获取购买街道区域数量信息")
    @GetMapping("/getCountMessage")
    public Result<?> getCountMessage() {
        return Result.success(streetService.getCountMessage(), null);
    }


    @ApiOperation("获取地主分页")
    @GetMapping("/getStreetPage")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页", required = true, dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页数据数", required = true, dataType = "int"),
            @ApiImplicitParam(name = "type", value = "类型", required = true, dataType = "int"),
            @ApiImplicitParam(name = "areaCode", value = "地区编码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "areaType", value = "地区类型", required = true, dataType = "int")
    })
    public Result<?> getStreetPage(Integer page, Integer size, Integer type, String areaCode, Integer areaType) {
        return Result.success(streetService.getStreetPage(page, size, type, areaCode, areaType), null);
    }

    @ApiOperation("获取市级编码")
    @GetMapping("/getCityCode")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cityName", value = "城市名称", required = true, dataType = "int")
    })
    public Result<?> getCityCode(String cityName) {
        return Result.success(streetService.getCityCode(cityName), null);
    }

    @ApiOperation("获取地主详细信息")
    @GetMapping("/getDetail")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "int"),
            @ApiImplicitParam(name = "type", value = "类型 1展位 2 街道", required = true, dataType = "int")
    })
    public Result<?> getDetail(Long id, Integer type) {
        return Result.success(streetService.getDetail(id, type), null);
    }

    @ApiOperation("获取当前用户已购街道")
    @GetMapping("/getCurrentUserStreet")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页", required = true, dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页数据数", required = true, dataType = "int")
    })
    public Result<?> getCurrentUserStreet(Integer page, Integer size, Integer status) {
        return Result.success(streetService.getCurrentUserStreet(ThreadLocalCurrentUser.getUsers().getId(), page, size, status), null);
    }


}

