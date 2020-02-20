package com.guangxuan.controller;


import com.guangxuan.config.ApiVersion;
import com.guangxuan.config.ApiVersionConstant;
import com.guangxuan.dto.*;
import com.guangxuan.service.MarketService;
import com.guangxuan.shiro.ThreadLocalCurrentUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.redisson.MapWriterTask;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zhuolin
 * @since 2019-12-17
 */
@RestController
@RequestMapping("/market")
@Api(value = "集市controller", tags = {"集市接口"})
public class MarketController {

    @Resource
    private MarketService marketService;

    @GetMapping("/getMyRent")
    @ApiOperation("获取我的租赁信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "areaCode", value = "地区编码", required = true, dataType = "String")
    })
    @ApiVersion(group = ApiVersionConstant.FAP_APP101)
    public Result<?> getMyRent(Integer type, String areaCode) {
        return Result.success(marketService.getMyRent(type, areaCode, ThreadLocalCurrentUser.getUsers().getId()), null);
    }

    @GetMapping("/getRentRate")
    @ApiOperation("获取租赁手续费")
    @ApiVersion(group = ApiVersionConstant.FAP_APP101)
    public Result<?> getRentRate() {
        return Result.success(marketService.getRentRate(), null);
    }


    @GetMapping("/getRent")
    @ApiOperation("获取租赁信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "size", value = "每页数据条数", required = true, dataType = "int", defaultValue = "10"),
            @ApiImplicitParam(name = "areaCode", value = "地区编码", required = true, dataType = "String")
    })
    @ApiVersion(group = ApiVersionConstant.FAP_APP101)
    public Result<?> getRent(Integer type, String areaCode, Integer page, Integer size) {
        return Result.success(marketService.getRent(type, areaCode, page, size, ThreadLocalCurrentUser.getUsers().getId()), null);
    }

//    @PostMapping("/rent")
//    @ApiOperation("出租")
//    @ApiVersion(group = ApiVersionConstant.FAP_APP101)
//    public Result<?> rent(@RequestBody @Valid RentDTO rentDTO) {
//        return Result.success(marketService.rent(rentDTO, ThreadLocalCurrentUser.getUsers().getId()), null);
//    }

    @PostMapping("/getCanRentStreet")
    @ApiOperation("获取可以出租的街道")
    @ApiVersion(group = ApiVersionConstant.FAP_APP101)
    public Result<?> getCanRentStreet(@RequestBody @Valid RemoveDTO removeDTO) {
        return Result.success(marketService.getCanRentStreet(ThreadLocalCurrentUser.getUsers().getId(),removeDTO.getId()), null);
    }

    @PostMapping("/rentToOther")
    @ApiOperation("租给ta")
    @ApiVersion(group = ApiVersionConstant.FAP_APP101)
    public Result<?> rentToOther(@RequestBody @Valid RentToOtherDTO rentOtherDTO) {
        return Result.success(marketService.rentToOther(rentOtherDTO, ThreadLocalCurrentUser.getUsers().getId()), null);
    }
//
//    @PostMapping("/rentOther")
//    @ApiOperation("租用")
//    @ApiVersion(group = ApiVersionConstant.FAP_APP101)
//    public Result<?> rentOther(@RequestBody @Valid RentOtherDTO rentOtherDTO) {
//        return Result.success(marketService.rentOther(rentOtherDTO, ThreadLocalCurrentUser.getUsers().getId()), null);
//    }
}

