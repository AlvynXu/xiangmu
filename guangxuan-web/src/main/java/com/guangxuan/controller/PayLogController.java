package com.guangxuan.controller;


import com.guangxuan.config.ApiVersion;
import com.guangxuan.config.ApiVersionConstant;
import com.guangxuan.dto.Result;
import com.guangxuan.service.PayLogService;
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
 * 支付日志 前端控制器
 * </p>
 *
 * @author zhuolin
 * @since 2019-11-27
 */
@RestController
@RequestMapping("/payLog")
@Api(tags = "支付操作")
public class PayLogController {

    @Resource
    private PayLogService payLogService;

    @GetMapping("/cancelPay")
    @ApiOperation("取消支付")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "类型 0vip 1购买展位 2购买街道 ", required = true, dataType = "int"),
            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "int")
    })
    @ApiVersion(group = ApiVersionConstant.FAP_APP101)
    public Result<?> cancelPay(Integer type, Long id) {
        return Result.success(payLogService.cancelPay(ThreadLocalCurrentUser.getUsers().getId(), type, id), null);
    }
}

