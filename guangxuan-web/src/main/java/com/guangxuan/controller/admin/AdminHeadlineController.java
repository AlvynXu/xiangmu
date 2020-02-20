package com.guangxuan.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.pagehelper.PageInfo;
import com.guangxuan.config.ApiVersion;
import com.guangxuan.config.ApiVersionConstant;
import com.guangxuan.controller.pay.utils.StringUtils;
import com.guangxuan.dto.domain.HeadlineDTO;
import com.guangxuan.dto.domain.HeadlineSaveDTO;
import com.guangxuan.enumration.BusinessFailEnum;
import com.guangxuan.exception.BusinessException;
import com.guangxuan.model.HeadlineItem;
import com.guangxuan.model.HeadlineItemArea;
import com.guangxuan.model.Headlines;
import com.guangxuan.model.MallItemInfo;
import com.guangxuan.service.*;
import com.guangxuan.shiro.util.PageInfoUtils;
import com.guangxuan.vo.BaseResponse;
import com.guangxuan.vo.ErrorResponse;
import com.guangxuan.vo.SuccessResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author zhuolin
 * @Date 2019/12/19
 */
@RestController("AdminHeadlineController")
@RequestMapping("/admin/headline")
@Api(value = "头条", tags = {"头条接口"})
public class AdminHeadlineController extends BaseController {

    @Resource
    private HeadlinesService headlinesService;

    @Resource
    private HeadlineItemAreaService headlineItemAreaService;

    @Resource
    private HeadlineItemService headlineItemService;

    @Resource
    private MallItemInfoService mallItemInfoService;

    @Resource
    private AreaService areaService;

    @GetMapping("getList")
    @ApiOperation("分页获取头条信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "areaCode", defaultValue = "1", dataType = "String", required = true,
                    value = "=地区编码 -1为首页 其余均为城市其地址"),
            @ApiImplicitParam(name = "page", defaultValue = "1", dataType = "int", required = true, value = "页码"),
            @ApiImplicitParam(name = "size", defaultValue = "10", dataType = "size", required = true, value = "数据数")
    })
    @ApiVersion(group = ApiVersionConstant.FAP_APP102)
    public BaseResponse<PageInfo<HeadlineDTO>> getList(String areaCode, Integer page, Integer size) {
        if (StringUtils.isBlank(areaCode)) {
            return new ErrorResponse(BusinessFailEnum.AREA_CODE_NOT_NULL.getCode(), BusinessFailEnum.AREA_CODE_NOT_NULL.getMessage());
        }
        if (page == null) {
            return new ErrorResponse(BusinessFailEnum.PAGE_NOT_NULL.getCode(), BusinessFailEnum.PAGE_NOT_NULL.getMessage());
        }
        if (size == null) {
            return new ErrorResponse(BusinessFailEnum.SIZE_NOT_NULL.getCode(), BusinessFailEnum.SIZE_NOT_NULL.getMessage());
        }
        IPage<HeadlineDTO> pages = headlinesService.getAdminPage(areaCode, page, size);
        return new SuccessResponse(PageInfoUtils.getPageInfo(pages));
    }

    @PostMapping("")
    @ApiOperation("添加头条")
    @ApiVersion(group = ApiVersionConstant.FAP_APP102)
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse create(@RequestBody @Valid HeadlineSaveDTO headlineDTO) {
        if (headlineDTO.getStatus() != 0 && headlineDTO.getTotalDays() == 0) {
            return new ErrorResponse(BusinessFailEnum.DAYS_NOT_NULL.getCode(), BusinessFailEnum.DAYS_NOT_NULL.getMessage());
        }
        MallItemInfo mallItemInfo = mallItemInfoService.getById(headlineDTO.getItemId());
        if(mallItemInfo == null){
            return new ErrorResponse(BusinessFailEnum.NOT_GET_ITM.getCode(), BusinessFailEnum.NOT_GET_ITM.getMessage());
        }
        List<HeadlineItem> headlineItems = headlinesService.getAdminHeadlinesItem(headlineDTO.getSort(), headlineDTO.getAreaCodes(), 1);
        if (headlineItems.size() > 0) {
            return new ErrorResponse(BusinessFailEnum.HEADLINE_AREA_USED.getCode(), BusinessFailEnum.HEADLINE_AREA_USED.getMessage());
        }
        if (headlineDTO.getStatus() == 0) {
            List<HeadlineItem> items = headlinesService.getAdminHeadlinesItem(headlineDTO.getSort(), headlineDTO.getAreaCodes(), null);
            if (items.size() > 0) {
                return new ErrorResponse(BusinessFailEnum.HEADLINE_AREA_USED.getCode(), BusinessFailEnum.HEADLINE_AREA_USED.getMessage());
            }
        }
        Headlines headlines;
        if (headlineDTO.getAreaCodes().contains("-1")) {
            headlines = headlinesService.getOne(new LambdaQueryWrapper<Headlines>().eq(Headlines::getSort, headlineDTO.getSort()).eq(Headlines::getType, 0));
        } else {
            headlines = headlinesService.getOne(new LambdaQueryWrapper<Headlines>().eq(Headlines::getSort, headlineDTO.getSort()).eq(Headlines::getType, 1));
        }
        if (headlines == null) {
            return new ErrorResponse(BusinessFailEnum.NOT_GET_DATA.getCode(), BusinessFailEnum.NOT_GET_DATA.getMessage());
        }
        HeadlineItem headlineItem = new HeadlineItem();
        headlineItem.setFee(headlines.getPrice().multiply(new BigDecimal(headlineDTO.getTotalDays())));
        headlineItem.setHeadlineId(headlines.getId());
        headlineItem.setEndTime(Date.from(LocalDateTime.now().plusDays(headlineDTO.getTotalDays()).atZone(ZoneId.systemDefault()).toInstant()));
        headlineItem.setStatus(headlineDTO.getStatus());
        headlineItem.setDays(headlineDTO.getTotalDays());
        headlineItem.setCreateTime(new Date());
        headlineItem.setItemId(headlineDTO.getItemId());
        headlineItemService.save(headlineItem);
        List<HeadlineItemArea> headlineItemAreas = new ArrayList<>();
        for (String areaCode : headlineDTO.getAreaCodes()) {
            HeadlineItemArea headlineItemArea = new HeadlineItemArea();
            headlineItemArea.setAreaCode(areaCode);
            headlineItemArea.setHeandlineItemId(headlineItem.getId());
            headlineItemAreas.add(headlineItemArea);
        }
        headlineItemAreaService.saveBatch(headlineItemAreas);
        return new SuccessResponse();
    }

    @PutMapping("")
    @ApiOperation("修改头条")
    @ApiVersion(group = ApiVersionConstant.FAP_APP102)
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse update(@RequestBody @Valid HeadlineSaveDTO headlineDTO) {
        if (headlineDTO.getStatus() != 0 && headlineDTO.getTotalDays() == 0) {
            return new ErrorResponse(BusinessFailEnum.DAYS_NOT_NULL.getCode(), BusinessFailEnum.DAYS_NOT_NULL.getMessage());
        }
        MallItemInfo mallItemInfo = mallItemInfoService.getById(headlineDTO.getItemId());
        if(mallItemInfo == null){
            return new ErrorResponse(BusinessFailEnum.NOT_GET_ITM.getCode(), BusinessFailEnum.NOT_GET_ITM.getMessage());
        }
        List<HeadlineItem> headlineItems = headlinesService.getAdminHeadlinesItem(headlineDTO.getSort(), headlineDTO.getAreaCodes(), 1);
        if (headlineItems.size() > 1 || (headlineItems.size() == 1 && !headlineItems.get(0).getId().equals(headlineDTO.getId()))) {
            return new ErrorResponse(BusinessFailEnum.HEADLINE_AREA_USED.getCode(), BusinessFailEnum.HEADLINE_AREA_USED.getMessage());
        }
        if (headlineDTO.getStatus() == 0) {
            List<HeadlineItem> items = headlinesService.getAdminHeadlinesItem(headlineDTO.getSort(), headlineDTO.getAreaCodes(), null);
            if (items.size() > 0) {
                return new ErrorResponse(BusinessFailEnum.HEADLINE_AREA_USED.getCode(), BusinessFailEnum.HEADLINE_AREA_USED.getMessage());
            }
        }
        Headlines headlines;
        if (headlineDTO.getAreaCodes().contains("-1")) {
            headlines = headlinesService.getOne(new LambdaQueryWrapper<Headlines>().eq(Headlines::getSort, headlineDTO.getSort()).eq(Headlines::getType, 0));
        } else {
            headlines = headlinesService.getOne(new LambdaQueryWrapper<Headlines>().eq(Headlines::getSort, headlineDTO.getSort()).eq(Headlines::getType, 1));
        }
        if (headlines == null) {
            return new ErrorResponse(BusinessFailEnum.NOT_GET_DATA.getCode(), BusinessFailEnum.NOT_GET_DATA.getMessage());
        }
        HeadlineItem headlineItem = headlineItemService.getById(headlineDTO.getId());
        if (headlineItem == null) {
            return new ErrorResponse(BusinessFailEnum.NOT_GET_DATA.getCode(), BusinessFailEnum.NOT_GET_DATA.getMessage());
        }
        headlineItem.setFee(headlines.getPrice().multiply(new BigDecimal(headlineDTO.getTotalDays())));
        headlineItem.setHeadlineId(headlines.getId());
        headlineItem.setEndTime(Date.from(LocalDateTime.now().plusDays(headlineItem.getDays()).atZone(ZoneId.systemDefault()).toInstant()));
        headlineItem.setStatus(headlineDTO.getStatus());
        headlineItem.setDays(headlineDTO.getTotalDays());
        headlineItem.setItemId(headlineDTO.getItemId());
        headlineItemService.updateById(headlineItem);
        List<HeadlineItemArea> headlineItemAreas = new ArrayList<>();
        for (String areaCode : headlineDTO.getAreaCodes()) {
            HeadlineItemArea headlineItemArea = new HeadlineItemArea();
            headlineItemArea.setAreaCode(areaCode);
            headlineItemArea.setHeandlineItemId(headlineItem.getId());
            headlineItemAreas.add(headlineItemArea);
        }
        headlineItemAreaService.remove(new LambdaQueryWrapper<HeadlineItemArea>().eq(HeadlineItemArea::getHeandlineItemId, headlineItem.getId()));
        headlineItemAreaService.saveBatch(headlineItemAreas);
        return new SuccessResponse();
    }


    @DeleteMapping("")
    @ApiOperation("删除头条")
    @ApiVersion(group = ApiVersionConstant.FAP_APP102)
    @Transactional(rollbackFor = Exception.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", defaultValue = "1", required = true, value = "头条id")
    })
    public BaseResponse delete(Long id) {
        HeadlineItem headlineItem = headlineItemService.getById(id);
        if (headlineItem.getStatus() == 1) {
            return new ErrorResponse(BusinessFailEnum.HEADLINE_USED.getCode(), BusinessFailEnum.HEADLINE_USED.getMessage());
        }
        headlineItemService.removeById(id);
        headlineItemAreaService.remove(new LambdaQueryWrapper<HeadlineItemArea>()
                .eq(HeadlineItemArea::getHeandlineItemId, id));

        return new SuccessResponse();
    }


}
