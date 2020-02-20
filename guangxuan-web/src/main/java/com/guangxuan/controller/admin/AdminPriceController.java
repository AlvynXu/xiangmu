package com.guangxuan.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.guangxuan.config.ApiVersion;
import com.guangxuan.config.ApiVersionConstant;
import com.guangxuan.dto.domain.HeadlinesPriceDTO;
import com.guangxuan.dto.domain.ProductPriceDTO;
import com.guangxuan.model.Headlines;
import com.guangxuan.model.MallConfig;
import com.guangxuan.model.SystemConfig;
import com.guangxuan.service.HeadlinesService;
import com.guangxuan.service.MallConfigService;
import com.guangxuan.service.SystemConfigService;
import com.guangxuan.vo.BaseResponse;
import com.guangxuan.vo.SuccessResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhuolin
 * @Date 2019/12/20
 */
@RestController
@RequestMapping("/admin/price")
@Api(value = "价值管理",tags = {"价值管理"})
public class AdminPriceController {

    @Resource
    private MallConfigService mallConfigService;

    @Resource
    private SystemConfigService systemConfigService;

    @Resource
    private HeadlinesService headlinesService;

    @GetMapping("productPrice")
    @ApiOperation("获取价值")
    @ApiVersion(group = ApiVersionConstant.FAP_APP102)
    public BaseResponse<ProductPriceDTO> getProductPrice() {
        MallConfig mallConfig = mallConfigService.getById(1L);
        SystemConfig systemConfig = systemConfigService.getById(1L);
        ProductPriceDTO productPriceDTO = new ProductPriceDTO();
        productPriceDTO.setBoothPrice(mallConfig.getThreshold2());
        productPriceDTO.setStreetPrice(mallConfig.getThreshold3());
        productPriceDTO.setVipPrice(mallConfig.getThreshold1());
        productPriceDTO.setCityBannerPrice(systemConfig.getCityBannerPrice());
        productPriceDTO.setBannerPrice(systemConfig.getBannerPrice());
        return new SuccessResponse(productPriceDTO);
    }


    @PutMapping("productPrice")
    @ApiOperation("修改价值")
    @ApiVersion(group = ApiVersionConstant.FAP_APP102)
    public BaseResponse updateProductPrice(@RequestBody @Valid ProductPriceDTO productPriceDTO) {
        MallConfig mallConfig = mallConfigService.getById(1L);
        SystemConfig systemConfig = systemConfigService.getById(1L);
        mallConfig.setThreshold1(productPriceDTO.getVipPrice());
        mallConfig.setThreshold2(productPriceDTO.getBoothPrice());
        mallConfig.setThreshold3(productPriceDTO.getStreetPrice());
        systemConfig.setBannerPrice(productPriceDTO.getBannerPrice());
        systemConfig.setCityBannerPrice(productPriceDTO.getCityBannerPrice());
        mallConfigService.updateById(mallConfig);
        systemConfigService.updateById(systemConfig);
        return new SuccessResponse();
    }

    @GetMapping("headlinesPrice")
    @ApiOperation("获取头条/推荐价值")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type",defaultValue = "0",dataType = "int", required = true, value = "类型0 首页 1市区")
    })
    @ApiVersion(group = ApiVersionConstant.FAP_APP102)
    public BaseResponse<List<HeadlinesPriceDTO>> getHeadlinesPrice(Integer type) {
        List<Headlines> headlines = headlinesService.list(new LambdaQueryWrapper<Headlines>().eq(Headlines::getType, type));
        List<HeadlinesPriceDTO> result = new ArrayList<>();
        for (Headlines headline : headlines) {
            HeadlinesPriceDTO headlinesPriceDTO = new HeadlinesPriceDTO();
            headlinesPriceDTO.setId(headline.getId());
            headlinesPriceDTO.setPrice(headline.getPrice());
            headlinesPriceDTO.setSort(headline.getSort());
            headlinesPriceDTO.setType(headline.getType());
            result.add(headlinesPriceDTO);
        }
        return new SuccessResponse(result);
    }

    @PutMapping("headlinesPrice")
    @ApiOperation("修改头条/推荐价值")
    @ApiVersion(group = ApiVersionConstant.FAP_APP102)
    public BaseResponse getHeadlinesPrice(@RequestBody @Valid HeadlinesPriceDTO headlinesPriceDTO) {
        Headlines headlines = new Headlines();
        headlines.setId(headlinesPriceDTO.getId());
        headlines.setPrice(headlinesPriceDTO.getPrice());
        headlines.setSort(headlinesPriceDTO.getSort());
        headlines.setType(headlinesPriceDTO.getType());
        headlinesService.updateById(headlines);
        return new SuccessResponse();
    }

}
