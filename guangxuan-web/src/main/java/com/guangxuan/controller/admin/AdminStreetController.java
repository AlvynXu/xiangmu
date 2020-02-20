package com.guangxuan.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.guangxuan.config.ApiVersion;
import com.guangxuan.config.ApiVersionConstant;
import com.guangxuan.controller.pay.utils.StringUtils;
import com.guangxuan.dto.PageInfo;
import com.guangxuan.dto.domain.StreetDTO;
import com.guangxuan.enumration.BusinessFailEnum;
import com.guangxuan.service.StreetService;
import com.guangxuan.vo.BaseResponse;
import com.guangxuan.vo.ErrorResponse;
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
 * @Date 2019/12/23
 */
@RestController("AdminStreetController")
@RequestMapping("/admin/street")
@Api(value = "街道管理", tags = "街道管理")
public class AdminStreetController extends BaseController {

    @Resource
    private StreetService streetService;

    @GetMapping("pageList")
    @ApiOperation("获取街道信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页数据条数", required = true, dataType = "int"),
            @ApiImplicitParam(name = "areaCode", value = "地区编码", required = true, dataType = "String")
    })
    @ApiVersion(group = ApiVersionConstant.FAP_APP102)
    public BaseResponse<PageInfo<StreetDTO>> pageList(Integer page, Integer size, String name, String areaCode) {
        if (page == null) {
            return new ErrorResponse(BusinessFailEnum.PAGE_NOT_NULL.getCode(), BusinessFailEnum.PAGE_NOT_NULL.getMessage());
        }
        if (size == null) {
            return new ErrorResponse(BusinessFailEnum.SIZE_NOT_NULL.getCode(), BusinessFailEnum.SIZE_NOT_NULL.getMessage());
        }
        if (StringUtils.isBlank(areaCode)) {
            return new ErrorResponse(BusinessFailEnum.AREA_CODE_NOT_NULL.getCode(), BusinessFailEnum.AREA_CODE_NOT_NULL.getMessage());
        }

        IPage<StreetDTO> streetIPage = streetService.adminPageList(page, size, name, areaCode);
        return new SuccessResponse(streetIPage);
    }
}
