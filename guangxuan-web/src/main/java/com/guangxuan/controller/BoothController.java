package com.guangxuan.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.guangxuan.config.ApiVersion;
import com.guangxuan.config.ApiVersionConstant;
import com.guangxuan.dto.Result;
import com.guangxuan.dto.SoldInfoDTO;
import com.guangxuan.locker.annotation.Lock;
import com.guangxuan.service.BoothService;
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
 * 前端控制器
 * </p>
 *
 * @author zhuolin
 * @since 2019-11-27
 */
@RestController
@RequestMapping("/booth")
@Api(value = "展位controller", tags = {"展位信息接口"})
public class BoothController {

    @Resource
    private BoothService boothService;

    @ApiOperation("获取购买展位数量信息")
    @GetMapping("/getBootCountMessage")
    public Result<?> getBootCountMessage() {
        return Result.success(boothService.getBootCountMessage(), null);
    }

    /**
     * @param page
     * @param size
     * @param type
     * @param areaCode
     * @param areaType
     * @param isSaved  是否为保留
     * @return
     */
    @ApiOperation("分页获取展位信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页数据条数", required = true, dataType = "int"),
            @ApiImplicitParam(name = "type", value = "类型", required = true, dataType = "int"),
            @ApiImplicitParam(name = "areaCode", value = "地区编码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "areaType", value = "地区类型", required = true, dataType = "int")
    })
    @ApiVersion(group = ApiVersionConstant.FAP_APP101)
    @GetMapping("/getBoothPage")
    public Result<?> getBoothPage(Integer page, Integer size, Integer type, String areaCode, Integer areaType, Boolean isSaved) {
        return Result.success(boothService.getBoothPage(page, size, type, areaCode, areaType, isSaved), null);
    }

    @ApiOperation("获取展位详细信息")
    @GetMapping("/getDetail")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "物品id", required = true, dataType = "int")
    })
    public Result<?> getDetail(Long id) {
        return Result.success(boothService.getById(id), null);
    }

    @ApiOperation("获取当前用户已购展位")
    @GetMapping("/getCurrentUserBooth")
    @ApiVersion(group = ApiVersionConstant.FAP_APP101)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "int"),
            @ApiImplicitParam(name = "size", value = "数据数", required = true, dataType = "int"),
            @ApiImplicitParam(name = "status", value = "数据数", required = true, dataType = "int")
    })
    public Result<IPage<SoldInfoDTO>> getCurrentUserBooth(Integer page, Integer size, Integer status) {
        return Result.success(boothService.getCurrentUserBooth(ThreadLocalCurrentUser.getUsers().getId(), page, size, status), null);
    }

    @ApiOperation("选择展位")
    @GetMapping("/choose")
    @Lock(value = "GUANGXUAN:BUY_BOOTH")
    @ApiVersion(group = ApiVersionConstant.FAP_APP101)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "boothId", value = "展位id", required = true, dataType = "int")
    })
    public Result<?> choose(Long boothId) {
        return Result.success(boothService.choose(ThreadLocalCurrentUser.getUsers().getId(), boothId), null);
    }

}

