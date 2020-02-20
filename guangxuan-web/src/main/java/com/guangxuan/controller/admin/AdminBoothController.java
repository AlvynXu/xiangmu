package com.guangxuan.controller.admin;

import com.github.pagehelper.PageInfo;
import com.guangxuan.config.ApiVersion;
import com.guangxuan.config.ApiVersionConstant;
import com.guangxuan.dto.Result;
import com.guangxuan.dto.domain.BoothDTO;
import com.guangxuan.service.BoothService;
import com.guangxuan.shiro.util.PageInfoUtils;
import com.guangxuan.vo.BaseResponse;
import com.guangxuan.vo.SuccessResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author zhuolin
 * @Date 2019/12/19
 */
@RestController("AdminBoothController")
@RequestMapping("/admin/booth")
@Api(value = "展位管理", tags = "展位管理")
public class AdminBoothController extends BaseController {

    @Resource
    private BoothService boothService;

    @GetMapping(value = "/getBoothPage")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "int"),
            @ApiImplicitParam(name = "size", value = "数据条数", required = true, dataType = "int"),
            @ApiImplicitParam(name = "streetCode", value = "街道编码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "boothCode", value = "编号", dataType = "int"),
    })
    @ApiOperation("分页获取展位信息")
    @ApiVersion(group = ApiVersionConstant.FAP_APP102)
    public BaseResponse<PageInfo<BoothDTO>> getBooths(Integer page, Integer size, String streetCode, String boothCode) {
         return new SuccessResponse(PageInfoUtils.getPageInfo(boothService.getAdminBoothPage(page, size, streetCode, boothCode)));
    }


}
