package com.guangxuan.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.pagehelper.PageInfo;
import com.guangxuan.config.ApiVersion;
import com.guangxuan.config.ApiVersionConstant;
import com.guangxuan.enumration.BusinessFailEnum;
import com.guangxuan.exception.BusinessException;
import com.guangxuan.model.BannerArea;
import com.guangxuan.model.MallBanner;
import com.guangxuan.model.MallItemInfo;
import com.guangxuan.service.AreaService;
import com.guangxuan.service.BannerAreaService;
import com.guangxuan.service.MallBannerService;
import com.guangxuan.service.MallItemInfoService;
import com.guangxuan.shiro.util.PageInfoUtils;
import com.guangxuan.vo.BaseResponse;
import com.guangxuan.vo.ErrorResponse;
import com.guangxuan.vo.SuccessResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author deofly
 * @since 2019-04-28
 */
@RestController("AdminMallBannerController")
@RequestMapping("/admin/mall/banners")
@Api(value = "banner管理", tags = "banner管理")
public class AdminMallBannerController {

    @Autowired
    private MallBannerService mallBannerService;

    @Resource
    private MallItemInfoService mallItemInfoService;

    @Resource
    private BannerAreaService bannerAreaService;

    @Resource
    private AreaService areaService;

    @GetMapping("")
    @ApiOperation("分页获取banner")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页数据条数", required = true, dataType = "int"),
            @ApiImplicitParam(name = "areaCode", value = "地区编码 -1首页 区域为各市编码", required = true, dataType = "String")}
    )
    @ApiVersion(group = ApiVersionConstant.FAP_APP102)
    public BaseResponse<PageInfo<MallBanner>> getAllBanners(String areaCode, Integer page, Integer size) {
        if (page == null) {
            return new ErrorResponse(BusinessFailEnum.PAGE_NOT_NULL.getCode(), BusinessFailEnum.PAGE_NOT_NULL.getMessage());
        }
        if (size == null) {
            return new ErrorResponse(BusinessFailEnum.SIZE_NOT_NULL.getCode(), BusinessFailEnum.SIZE_NOT_NULL.getMessage());
        }
        if (areaCode == null) {
            return new ErrorResponse(BusinessFailEnum.AREA_CODE_NOT_NULL.getCode(), BusinessFailEnum.AREA_CODE_NOT_NULL.getMessage());
        }
        IPage<MallBanner> banners = mallBannerService.getAdminBanner(page, size, areaCode);
        if (CollectionUtils.isEmpty(banners.getRecords())) {
            return new SuccessResponse(new PageInfo<>());
        }
        return new SuccessResponse(PageInfoUtils.getPageInfo(banners));
    }

    @GetMapping("{id}")
    @ApiOperation("获取banner详情")
    @ApiVersion(group = ApiVersionConstant.FAP_APP102)
    public BaseResponse<MallBanner> getBanner(@PathVariable(required = true) int id) {
        MallBanner bannerDO = mallBannerService.getById(id);
        if (bannerDO == null) {
            return new ErrorResponse("该Banner不存在");
        }
        return new SuccessResponse(bannerDO);
    }

    @PostMapping("")
    @ApiOperation("添加banner")
    @ApiVersion(group = ApiVersionConstant.FAP_APP102)
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse createBanner(@RequestBody @Valid MallBanner bannerDO,
                                     BindingResult result) {
        if (result.hasErrors()) {
            return new ErrorResponse(result.getAllErrors().get(0).getDefaultMessage());
        }
        MallItemInfo mallItemInfo = mallItemInfoService.getById(bannerDO.getItemId());
        if(mallItemInfo == null){
            return new ErrorResponse(BusinessFailEnum.NOT_GET_ITM.getCode(), BusinessFailEnum.NOT_GET_ITM.getMessage());
        }
        MallBanner mallBanner = mallBannerService.listInfo(bannerDO.getAreaCodes(), bannerDO.getItemId());
        if (mallBanner!= null) {
            return new ErrorResponse(BusinessFailEnum.BANNER_AREA_USED.getCode(), BusinessFailEnum.BANNER_AREA_USED.getMessage());
        }
        bannerDO.setType(mallItemInfo == null ? 4 : 3);
        bannerDO.setEndTime(Date.from(LocalDateTime.now().plusDays(bannerDO.getDays()).atZone(ZoneId.systemDefault()).toInstant()));
        mallBannerService.save(bannerDO);
        List<BannerArea> bannerAreas = new ArrayList<>();
        for (String areaCode : bannerDO.getAreaCodes()) {
            BannerArea bannerArea = new BannerArea();
            bannerArea.setAreaCode(areaCode);
            bannerArea.setBannerId(bannerDO.getId());
            bannerAreas.add(bannerArea);
        }
        bannerAreaService.saveBatch(bannerAreas);
        return new SuccessResponse();
    }

    @PutMapping("")
    @ApiOperation("修改banner")
    @ApiVersion(group = ApiVersionConstant.FAP_APP102)
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse updateBanner(@RequestBody @Valid MallBanner bannerDO,
                                     BindingResult result) {
        if (result.hasErrors()) {
            return new ErrorResponse(result.getAllErrors().get(0).getDefaultMessage());
        }
        MallBanner mallBanner = mallBannerService.listInfo(bannerDO.getAreaCodes(), bannerDO.getItemId());
        if (mallBanner!= null && !mallBanner.getId().equals(bannerDO.getId())) {
            return new ErrorResponse(BusinessFailEnum.BANNER_AREA_USED.getCode(), BusinessFailEnum.BANNER_AREA_USED.getMessage());
        }
        MallItemInfo mallItemInfo = mallItemInfoService.getById(bannerDO.getItemId());
        if(mallItemInfo == null){
            return new ErrorResponse(BusinessFailEnum.NOT_GET_ITM.getCode(), BusinessFailEnum.NOT_GET_ITM.getMessage());
        }
        bannerDO.setType(mallItemInfo == null ? 4 : 3);
        bannerDO.setEndTime(Date.from(LocalDateTime.now().plusDays(bannerDO.getDays()).atZone(ZoneId.systemDefault()).toInstant()));
        mallBannerService.updateById(bannerDO);
        List<BannerArea> bannerAreas = new ArrayList<>();
        for (String areaCode : bannerDO.getAreaCodes()) {
            BannerArea bannerArea = new BannerArea();
            bannerArea.setAreaCode(areaCode);
            bannerArea.setBannerId(bannerDO.getId());
            bannerAreas.add(bannerArea);
        }
        bannerAreaService.remove(new LambdaQueryWrapper<BannerArea>().eq(BannerArea::getBannerId, bannerDO.getId()));
        bannerAreaService.saveBatch(bannerAreas);
        return new SuccessResponse();
    }

    @DeleteMapping("{id}")
    @ApiOperation("删除banner")
    @ApiVersion(group = ApiVersionConstant.FAP_APP102)
    public BaseResponse deleteBanner(@PathVariable(required = true) int id) {
        mallBannerService.removeById(id);
        bannerAreaService.remove(new LambdaQueryWrapper<BannerArea>().eq(BannerArea::getBannerId, id));
        return new SuccessResponse();
    }
}
