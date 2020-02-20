package com.guangxuan.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.guangxuan.config.ApiVersion;
import com.guangxuan.config.ApiVersionConstant;
import com.guangxuan.dto.Result;
import com.guangxuan.model.MallItemCategory;
import com.guangxuan.service.MallItemCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 商城商品类目 前端控制器
 * </p>
 *
 * @author zhuolin
 * @since 2019-11-27
 */
@RestController
@RequestMapping("/mallItemCategory")
@Api(value = "商品类目controller", tags = {"商品类目接口"})
public class MallItemCategoryController {

    @Resource
    private MallItemCategoryService mallItemCategoryService;

    @ApiOperation("获取全部可见商品类目")
    @ApiVersion(group = ApiVersionConstant.FAP_APP101)
    @GetMapping("getList")
    public Result<?> getList() {
        return Result.success(mallItemCategoryService.getList(), null);
    }


}

