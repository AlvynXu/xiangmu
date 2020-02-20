package com.guangxuan.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.guangxuan.config.ApiVersion;
import com.guangxuan.config.ApiVersionConstant;
import com.guangxuan.constant.OrderStatus;
import com.guangxuan.model.Area;
import com.guangxuan.model.Street;
import com.guangxuan.model.StreetPartnerOrder;
import com.guangxuan.service.AreaService;
import com.guangxuan.service.StreetPartnerOrderService;
import com.guangxuan.service.StreetService;
import com.guangxuan.vo.BaseResponse;
import com.guangxuan.vo.ErrorResponse;
import com.guangxuan.vo.SuccessResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 地区 前端控制器
 * </p>
 *
 * @author zhuolin
 * @since 2019-11-27
 */
@RestController
@RequestMapping("/areas")
@Api(value = "用户controller", tags = {"地区信息接口"})
public class AreaController {

    @Resource
    private AreaService areaService;

    @Resource
    private StreetService streetService;

    @Resource
    private StreetPartnerOrderService streetPartnerService;

    //    @Autowired
//    private IVillagePartnerService villagePartnerService;
    @ApiOperation(value = "获取省份")
    @GetMapping("provinces")
    public BaseResponse getAllProvinces() {
        List<Area> provinces = areaService.getAllProvinces();
        return new SuccessResponse(provinces);
    }

    @ApiOperation(value = "获取城市")
    @GetMapping("cities")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "城市编码", required = true, dataType = "String")
    })
    public BaseResponse getCities(String code) {
        if (StringUtils.isEmpty(code)) {
            return new SuccessResponse();
        }
        List<Area> cities = areaService.getCities(code);
        return new SuccessResponse(cities);
    }

    @ApiOperation(value = "获取区县")
    @GetMapping("districts")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "城市编码", required = true, dataType = "String")
    })
    public BaseResponse getDistricts(String code) {
        if (StringUtils.isEmpty(code)) {
            return new SuccessResponse();
        }
       return new SuccessResponse(areaService.getDistricts(code));
    }

    @ApiOperation(value = "获取街道")
    @GetMapping("streets")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "区县", required = true, dataType = "String")
    })
    public BaseResponse getStreets(String code) {
        if (StringUtils.isEmpty(code)) {
            return new SuccessResponse();
        }
        return new SuccessResponse(areaService.getStreet(code));
    }

    @ApiOperation(value = "获取全部城市")
    @GetMapping("getCities")
    @ApiVersion(group = ApiVersionConstant.FAP_APP101)
    public BaseResponse<List<Area>> getCities() {
        List<Area> cities = areaService.getAllCities();
        return new SuccessResponse(cities);
    }

}

