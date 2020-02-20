package com.guangxuan.controller;


import com.guangxuan.config.ApiVersion;
import com.guangxuan.config.ApiVersionConstant;
import com.guangxuan.dto.Result;
import com.guangxuan.model.ItemCollection;
import com.guangxuan.service.ItemCollectionService;
import com.guangxuan.shiro.ThreadLocalCurrentUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zhuolin
 * @since 2019-12-18
 */
@RestController
@RequestMapping("/itemCollection")
@Api(value = "收藏controller", tags = {"收藏接口"})
public class ItemCollectionController {

    @Resource
    private ItemCollectionService itemCollectionService;

    @PostMapping("collection")
    @ApiOperation("收藏")
    @ApiVersion(group = ApiVersionConstant.FAP_APP101)
    public Result<?> collection(@RequestBody @Valid ItemCollection itemCollection) {
        return Result.success(itemCollectionService.collection(itemCollection, ThreadLocalCurrentUser.getUsers().getId()), null);
    }

    @PostMapping("cancelCollection")
    @ApiOperation("取消收藏")
    @ApiVersion(group = ApiVersionConstant.FAP_APP101)
    public Result<?> cancelCollection(@RequestBody @Valid ItemCollection itemCollection) {
        return Result.success(itemCollectionService.cancelCollection(itemCollection.getItemId(), ThreadLocalCurrentUser.getUsers().getId()), null);
    }


}

