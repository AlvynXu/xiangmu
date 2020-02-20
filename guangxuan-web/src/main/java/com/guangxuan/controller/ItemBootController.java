package com.guangxuan.controller;


import com.guangxuan.config.ApiVersion;
import com.guangxuan.config.ApiVersionConstant;
import com.guangxuan.dto.Result;
import com.guangxuan.model.ItemBoot;
import com.guangxuan.model.MallItemInfo;
import com.guangxuan.service.ItemBootService;
import com.guangxuan.shiro.ThreadLocalCurrentUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zhuolin
 * @since 2019-11-28
 */
@RestController
@RequestMapping("/itemBoot")
@Api(value = "商品展位controller", tags = {"商品展位接口"})
public class ItemBootController {

    @Resource
    private ItemBootService itemBootService;

    @PostMapping("/saveItemBooth")
    @ApiOperation("商品挂上展位")
    @ApiVersion(group = ApiVersionConstant.FAP_APP101)
    public Result<?> saveItemBooth(@RequestBody ItemBoot itemBoot) {
        return Result.success(itemBootService.saveItemBooth(itemBoot), null);
    }

    @PostMapping("/removeItem")
    @ApiOperation("下架")
    @ApiVersion(group = ApiVersionConstant.FAP_APP101)
    public Result<?> removeItem(@RequestBody MallItemInfo itemInfo) {
        return Result.success(itemBootService.removeItem(itemInfo), null);
    }

    @GetMapping("/getCanUseBooth")
    @ApiOperation("获取可以使用的展位")
    @ApiVersion(group = ApiVersionConstant.FAP_APP101)
    public Result<?> getCanUseBooth(Integer page, Integer size, Long itemId) {
        return Result.success(itemBootService.getCanUseBooth(ThreadLocalCurrentUser.getUsers().getId(), page, size, itemId), null);
    }

}

