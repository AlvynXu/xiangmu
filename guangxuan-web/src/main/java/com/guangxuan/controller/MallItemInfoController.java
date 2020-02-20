package com.guangxuan.controller;


import com.guangxuan.config.ApiVersion;
import com.guangxuan.config.ApiVersionConstant;
import com.guangxuan.dto.MallItemInfoDTO;
import com.guangxuan.dto.MallItemInfoDetailDTO;
import com.guangxuan.dto.RemoveDTO;
import com.guangxuan.dto.Result;
import com.guangxuan.model.ItemLeaveMessage;
import com.guangxuan.model.MallItemInfo;
import com.guangxuan.model.Users;
import com.guangxuan.service.MallItemInfoService;
import com.guangxuan.shiro.ThreadLocalCurrentUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FileUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zhuolin
 * @since 2019-12-15
 */
@RestController
@RequestMapping("/mallItemInfo")
@Api(value = "商品controller", tags = {"商品接口"})
public class MallItemInfoController {

    @Resource
    private MallItemInfoService mallItemInfoService;

    @PostMapping("/saveInfo")
    @ApiOperation("保存商品信息")
    @ApiVersion(group = ApiVersionConstant.FAP_APP101)
    public Result<?> saveInfo(@Valid MallItemInfoDTO itemInfo, MultipartFile multipartFile, HttpServletRequest request) throws IOException {
        return Result.success(mallItemInfoService.saveInfo(itemInfo, multipartFile, ThreadLocalCurrentUser.getUsers().getId()), null);
    }

    @PostMapping("/saveInfoDetail")
    @ApiOperation("保存商品详细信息")
    @ApiVersion(group = ApiVersionConstant.FAP_APP101)
    public Result<?> saveInfoDetail(@Valid MallItemInfoDetailDTO itemInfo, MultipartFile multipartFile) throws IOException {
        return Result.success(mallItemInfoService.saveInfoDetail(itemInfo, multipartFile, ThreadLocalCurrentUser.getUsers().getId()), null);
    }

    @PostMapping("/saveLeaveMessage")
    @ApiOperation("保存商品留言")
    @ApiVersion(group = ApiVersionConstant.FAP_APP103)
    public Result<?> saveLeaveMessage(@RequestBody @Valid ItemLeaveMessage leaveMessage) throws IOException {
        return Result.success(mallItemInfoService.saveLeaveMessage(leaveMessage, ThreadLocalCurrentUser.getUsers().getId()), null);
    }

    @GetMapping("/getLeaveMessagePage")
    @ApiOperation("获取商品留言")
    @ApiVersion(group = ApiVersionConstant.FAP_APP103)
    public Result<?> getLeaveMessagePage(Long itemId, Integer page, Integer size) throws IOException {
        return Result.success(mallItemInfoService.getLeaveMessagePage(itemId, page, size), null);
    }

    @GetMapping("/report")
    @ApiOperation("举报商品信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "itemId", value = "被举报物品id", required = true, dataType = "long")})
    @ApiVersion(group = ApiVersionConstant.FAP_APP101)
    public Result<?> report(Long itemId) {
        return Result.success(mallItemInfoService.report(itemId), null);
    }

    @GetMapping("/pageList")
    @ApiOperation("分页商品信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "size", value = "每页数据条数", required = true, dataType = "int", defaultValue = "10"),
            @ApiImplicitParam(name = "areaCode", value = "地区编码", required = true, dataType = "String", defaultValue = "HZ0098343"),
            @ApiImplicitParam(name = "areaType", value = "地区类型 1街道 2区县 3城市 4省份", required = true, dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "categoryId", value = "类别id", required = true, dataType = "int", defaultValue = "1")
    })
    @ApiVersion(group = ApiVersionConstant.FAP_APP101)
    public Result<?> pageList(Integer areaType, String areaCode, Integer categoryId, Integer page, Integer size) {
        return Result.success(mallItemInfoService.pageList(areaType, areaCode, categoryId, page, size), null);
    }

    @GetMapping("/getDetail")
    @ApiOperation("获取商品详细信息")
    @ApiVersion(group = ApiVersionConstant.FAP_APP101)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "itemId", value = "物品id", required = true, dataType = "int", defaultValue = "1")
    })
    public Result<?> getDetail(Long itemId) {
        return Result.success(mallItemInfoService.getDetail(itemId, ThreadLocalCurrentUser.getUsers().getId()), null);
    }

    @GetMapping("/getShareDetail")
    @ApiOperation("获取分享商品详细信息")
    @ApiVersion(group = ApiVersionConstant.FAP_APP101)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "itemId", value = "物品id", required = true, dataType = "int", defaultValue = "1")
    })
    public Result<MallItemInfo> getShareDetail(Long itemId, String regCode) {
        return Result.success(mallItemInfoService.getShareDetail(itemId, regCode), null);
    }

    @GetMapping("/getMyItemPageList")
    @ApiOperation("获取我的项目")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "size", value = "每页数据条数", required = true, dataType = "int", defaultValue = "10"),
    })
    @ApiVersion(group = ApiVersionConstant.FAP_APP101)
    public Result<?> getMyItemPageList(Integer page, Integer size) {
        return Result.success(mallItemInfoService.getMyItemPageList(ThreadLocalCurrentUser.getUsers().getId(), page, size), null);
    }

//    @GetMapping("/confirmAudit")
//    @ApiOperation("提交审核")
//    @ApiVersion(group = ApiVersionConstant.FAP_APP103)
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "itemId", value = "物品id", required = true, dataType = "int", defaultValue = "1")
//    })
//    public Result<?> confirmAudit(Long itemId) {
//        return Result.success(mallItemInfoService.confirmAudit(ThreadLocalCurrentUser.getUsers().getId(), itemId), null);
//    }

    @GetMapping("/getShareUrl")
    @ApiOperation("分享")
    @ApiVersion(group = ApiVersionConstant.FAP_APP101)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "itemId", value = "物品id", required = true, dataType = "int", defaultValue = "1")
    })
    public Result<?> getShareUrl(Long itemId) {
        return Result.success(mallItemInfoService.getShareUrl(ThreadLocalCurrentUser.getUsers().getId(), itemId), null);
    }


    @ApiOperation("获取收藏列表")
    @ApiVersion(group = ApiVersionConstant.FAP_APP101)
    @GetMapping("getCollectionPageList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "size", value = "每页数据条数", required = true, dataType = "int", defaultValue = "10"),
    })
    public Result<?> getCollectionPageList(Integer page, Integer size) {
        return Result.success(mallItemInfoService.getCollectionPageList(page, size, ThreadLocalCurrentUser.getUsers().getId()), null);
    }

    @ApiOperation("删除项目")
    @ApiVersion(group = ApiVersionConstant.FAP_APP101)
    @PostMapping("remove")
    public Result<?> remove(@RequestBody @Valid RemoveDTO removeDTO) {
        return Result.success(mallItemInfoService.removeItem(removeDTO.getId(), ThreadLocalCurrentUser.getUsers().getId()), null);
    }


}

