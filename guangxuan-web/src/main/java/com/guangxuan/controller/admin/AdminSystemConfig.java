package com.guangxuan.controller.admin;

import com.guangxuan.config.ApiVersion;
import com.guangxuan.config.ApiVersionConstant;
import com.guangxuan.model.SystemConfig;
import com.guangxuan.service.SystemConfigService;
import com.guangxuan.vo.BaseResponse;
import com.guangxuan.vo.SuccessResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author zhuolin
 * @Date 2019/12/20
 */
@RestController
@RequestMapping("/admin/system")
@Api( value = "抽点配置",tags = {"抽点配置"})
public class AdminSystemConfig extends BaseController {

    @Resource
    private SystemConfigService systemConfigService;


    @GetMapping("")
    @ApiOperation("获取抽点信息")
    @ApiVersion(group = ApiVersionConstant.FAP_APP102)
    public BaseResponse<SystemConfig> getSystemConfig() {
        return new SuccessResponse(systemConfigService.getById(1L));
    }

    @PutMapping("")
    @ApiOperation("修改抽点信息")
    @ApiVersion(group = ApiVersionConstant.FAP_APP102)
    public BaseResponse update(@RequestBody @Valid SystemConfig systemConfig) {
        systemConfigService.updateById(systemConfig);
        return new SuccessResponse();
    }

}
