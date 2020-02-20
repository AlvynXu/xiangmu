package com.guangxuan.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.guangxuan.config.ApiVersion;
import com.guangxuan.config.ApiVersionConstant;
import com.guangxuan.constant.OrderStatus;
import com.guangxuan.enumration.AreaLevel;
import com.guangxuan.model.Area;
import com.guangxuan.model.Street;
import com.guangxuan.model.StreetPartnerOrder;
import com.guangxuan.service.AreaService;
import com.guangxuan.service.StreetPartnerOrderService;
import com.guangxuan.service.StreetService;
import com.guangxuan.vo.BaseResponse;
import com.guangxuan.vo.ErrorResponse;
import com.guangxuan.vo.SuccessResponse;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author deofly
 * @since 2019-05-01
 */
@RestController("AdminAreaController")
@RequestMapping("/admin/areas")
@Api(value = "地区管理", tags = "地区管理")
public class AdminAreaController {

    @Resource
    private AreaService areaService;

    @Resource
    private StreetService streetService;

    @Resource
    private StreetPartnerOrderService streetPartnerService;

    //    @Autowired
//    private IVillagePartnerService villagePartnerService;
    @ApiOperation(value = "获取省份")
    @GetMapping("getAllProvinces")
    @ApiVersion(group = ApiVersionConstant.FAP_APP102)
    public BaseResponse<List<Area>> getAllProvinces() {
        List<Area> provinces = areaService.getAllProvinces();
        return new SuccessResponse(provinces);
    }

    @ApiOperation(value = "获取城市")
    @GetMapping("cities")
    @ApiVersion(group = ApiVersionConstant.FAP_APP102)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "省份编码", required = true, dataType = "String")
    })
    public BaseResponse<List<Area>> getCities(String code) {
        if (StringUtils.isEmpty(code)) {
            return new SuccessResponse();
        }
        List<Area> cities = areaService.getCities(code);
        return new SuccessResponse(cities);
    }


    @ApiOperation(value = "获取全部城市")
    @GetMapping("getCities")
    @ApiVersion(group = ApiVersionConstant.FAP_APP102)
    public BaseResponse<List<Area>> getCities() {
        List<Area> cities = areaService.getAllCities();
        return new SuccessResponse(cities);
    }

    @ApiOperation(value = "获取区县")
    @ApiVersion(group = ApiVersionConstant.FAP_APP102)
    @GetMapping("districts")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "城市编码", required = true, dataType = "String")
    })
    public BaseResponse getDistricts(String code) {
        if (StringUtils.isEmpty(code)) {
            return new SuccessResponse();
        }
        List<Area> districts = areaService.getDistricts(code);
        return new SuccessResponse(districts);
    }

    @ApiOperation(value = "获取街道")
    @GetMapping("streets")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "区县编码", required = true, dataType = "String")
    })
    @ApiVersion(group = ApiVersionConstant.FAP_APP102)
    public BaseResponse<List<Street>> getStreets(String code) {
        if (StringUtils.isEmpty(code)) {
            return new SuccessResponse();
        }
        List<Street> streets = areaService.getStreet(code);
//                streetService.getStreets();
        return new SuccessResponse(streets);
    }

    @ApiOperation(value = "添加街道")
    @PostMapping("streets")
    @ApiVersion(group = ApiVersionConstant.FAP_APP102)
    public BaseResponse createStreet(@Valid @RequestBody Street streetDO,
                                     BindingResult result) {
        if (result.hasErrors()) {
            return new ErrorResponse(result.getAllErrors().get(0).getDefaultMessage());
        }
        streetService.saveStreet(streetDO);
        return new SuccessResponse();
    }

    @ApiOperation(value = "修改街道")
    @PutMapping("streets")
    @ApiVersion(group = ApiVersionConstant.FAP_APP102)
    public BaseResponse updateStreet(@Valid @RequestBody Street streetDO,
                                     BindingResult result) {
        if (result.hasErrors()) {
            return new ErrorResponse(result.getAllErrors().get(0).getDefaultMessage());
        }
        streetService.updateInfo(streetDO);
        return new SuccessResponse();
    }

    @ApiOperation(value = "删除街道")
    @DeleteMapping("streets/{id}")
    @ApiVersion(group = ApiVersionConstant.FAP_APP102)
    public BaseResponse deleteStreet(@PathVariable Integer id) {
        if (id == null || id <= 0) {
            return new ErrorResponse("街道不存在");
        }
        List<StreetPartnerOrder> streetPartnerOrders;
        streetPartnerOrders = streetPartnerService.list(new LambdaQueryWrapper<StreetPartnerOrder>()
                .eq(StreetPartnerOrder::getStreetId, id)
                .ne(StreetPartnerOrder::getOrderStatus, OrderStatus.CLOSE));
        if (streetPartnerOrders.size() > 0) {
            return new ErrorResponse("已售卖或正在售卖中");
        }
        streetService.removeById(id);
        return new SuccessResponse();
    }


}
