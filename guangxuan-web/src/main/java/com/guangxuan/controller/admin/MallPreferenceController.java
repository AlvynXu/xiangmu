package com.guangxuan.controller.admin;

import com.guangxuan.config.ApiVersion;
import com.guangxuan.config.ApiVersionConstant;
import com.guangxuan.model.MallConfig;
import com.guangxuan.service.MallConfigService;
import com.guangxuan.vo.BaseResponse;
import com.guangxuan.vo.SuccessResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author deofly
 * @since 2019-04-26
 */
@RestController
@RequestMapping("/admin/mall/preferences")
@Api(value = "返佣配置", tags = "返佣配置")
public class MallPreferenceController extends BaseController {

    @Autowired
    private MallConfigService mallConfigService;

    @GetMapping("basic")
    @ApiVersion(group = ApiVersionConstant.FAP_APP102)
    @ApiOperation("获取返佣配置")
    public BaseResponse<MallConfig> getBasic() {
        MallConfig configDO = mallConfigService.getById(1L);
        return new SuccessResponse(configDO);
    }

    @PutMapping("basic")
    @ApiOperation("修改返佣配置")
    @ApiVersion(group = ApiVersionConstant.FAP_APP102)
    public BaseResponse updateBasic(@RequestBody MallConfig configDO) {
        mallConfigService.updateById(configDO);
        return new SuccessResponse();
    }
}
