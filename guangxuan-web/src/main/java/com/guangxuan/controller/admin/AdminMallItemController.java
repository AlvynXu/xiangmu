package com.guangxuan.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageInfo;
import com.guangxuan.config.ApiVersion;
import com.guangxuan.config.ApiVersionConstant;
import com.guangxuan.constant.ItemStatus;
import com.guangxuan.constant.MessageStatus;
import com.guangxuan.dto.domain.CategoryDTO;
import com.guangxuan.dto.domain.CategorySaveDTO;
import com.guangxuan.enumration.BusinessFailEnum;
import com.guangxuan.model.*;
import com.guangxuan.service.*;
import com.guangxuan.shiro.util.PageInfoUtils;
import com.guangxuan.util.ItemUtils;
import com.guangxuan.vo.BaseResponse;
import com.guangxuan.vo.ErrorResponse;
import com.guangxuan.vo.PageRequestParam;
import com.guangxuan.vo.SuccessResponse;
import com.guangxuan.vo.mall.form.MallItemStatusForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author deofly
 * @since 2019-04-27
 */
@RestController("AdminMallItemController")
@RequestMapping("/admin/mall/items")
@Api(value = "项目管理", tags = "项目管理")
public class AdminMallItemController extends BaseController {

    @Autowired
    private MallItemInfoService mallItemService;

    @Autowired
    private MallItemInfoDetailService mallItemInfoDetailService;

    @Resource
    private MallItemCategoryService mallItemCategoryService;

    @Resource
    private UsersService usersService;

    @Resource
    private ItemBootService itemBootService;

    @Resource
    private MessageService messageService;


    @GetMapping("categories")
    @ApiOperation("获取类别")
    @ApiVersion(group = ApiVersionConstant.FAP_APP102)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页数据条数", required = true, dataType = "int")
    })
    public BaseResponse<PageInfo<MallItemCategory>> getAllCategories(Integer page, Integer size) {
        IPage<CategoryDTO> categories = mallItemCategoryService.getAllCategories(page, size);
        return new SuccessResponse(PageInfoUtils.getPageInfo(categories));
    }

    @GetMapping("categories/{id}")
    @ApiOperation("获取类别详细信息")
    @ApiVersion(group = ApiVersionConstant.FAP_APP102)
    public BaseResponse getCategory(@PathVariable(required = true) int id) {
        MallItemCategory categoryDO = mallItemCategoryService.getById(id);
        if (categoryDO == null) {
            return new ErrorResponse("该商品类目不存在");
        }

        return new SuccessResponse(categoryDO);
    }

    @PostMapping("categories")
    @ApiOperation("添加类别")
    @ApiVersion(group = ApiVersionConstant.FAP_APP102)
    public BaseResponse createCategory(@RequestBody @Valid CategorySaveDTO categoryDO,
                                       BindingResult result) {
        if (result.hasErrors()) {
            return new ErrorResponse(result.getAllErrors().get(0).getDefaultMessage());
        }

        MallItemCategory category = new MallItemCategory();
        category.setCreateTime(new Date());
        category.setSort(categoryDO.getSeq());
        category.setTitle(categoryDO.getName());
        category.setVisible(true);
        category.setCode(generateCode());
        mallItemCategoryService.save(category);
        return new SuccessResponse();
    }

    private String generateCode() {
        String code = String.format("%04d", new Random().nextInt(9999));
        int count = mallItemCategoryService.count(new LambdaQueryWrapper<MallItemCategory>().eq(MallItemCategory::getCode, code));
        if (count > 0) {
            code = generateCode();
        }
        return code;

    }

    @PutMapping("categories")
    public BaseResponse updateCategory(@RequestBody @Valid MallItemCategory categoryDO,
                                       BindingResult result) {
        if (result.hasErrors()) {
            return new ErrorResponse(result.getAllErrors().get(0).getDefaultMessage());
        }

        mallItemCategoryService.updateById(categoryDO);

        return new SuccessResponse();
    }

    @ApiOperation("删除类别")
    @ApiVersion(group = ApiVersionConstant.FAP_APP102)
    @DeleteMapping("categories/{id}")
    public BaseResponse deleteCategory(@PathVariable int id) {

        int count = mallItemService.count(new LambdaQueryWrapper<MallItemInfo>()
                .eq(MallItemInfo::getCategoryId, id));
        if (count > 0) {
            return new ErrorResponse("该类目下存在商品，无法删除");
        }

        mallItemService.removeById(id);

        return new SuccessResponse();
    }

    @GetMapping("")
    @ApiOperation("获取项目")
    @ApiVersion(group = ApiVersionConstant.FAP_APP102)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", dataType = "String"),
            @ApiImplicitParam(name = "categoryId", value = "类别id", dataType = "int"),
    })
    public BaseResponse<PageInfo<MallItemInfo>> getItemList(PageRequestParam pageParam, String phone, Long categoryId) {
        List<Users> users = null;
        List<Long> ids = new ArrayList<>();
        if (StringUtils.isNotBlank(phone)) {
            users = usersService.list(new QueryWrapper<Users>().like("phone", "%" + phone + "%"));
            if (users == null) {
                return new SuccessResponse(new PageInfo<>());
            }
            ids = users.stream().map(Users::getId).collect(Collectors.toList());
        }
        IPage<MallItemInfo> page = mallItemService.page(
                new Page<MallItemInfo>(pageParam.getOffset(), pageParam.getLimit()),
                new LambdaQueryWrapper<MallItemInfo>()
                        .in(StringUtils.isNotBlank(phone), MallItemInfo::getUserId, ids)
                        .eq(categoryId != null, MallItemInfo::getCategoryId, categoryId));
        List<Long> categoryIds = page.getRecords().stream().map(MallItemInfo::getCategoryId).collect(Collectors.toList());
        List<Long> userIds = page.getRecords().stream().map(MallItemInfo::getUserId).collect(Collectors.toList());
        if (categoryIds.size() > 0) {
            Collection<MallItemCategory> categories = mallItemCategoryService.listByIds(categoryIds);
            Collection<Users> users1 = usersService.listByIds(userIds);
            Map<Long, MallItemCategory> mallItemCategoryMap = categories.stream()
                    .collect(Collectors.toMap(MallItemCategory::getId, a -> a));
            Map<Long, Users> userMap = users1.stream().collect(Collectors.toMap(Users::getId, a -> a));

            for (MallItemInfo mallItemInfo : page.getRecords()) {
                mallItemInfo.setCategory(mallItemCategoryMap.get(mallItemInfo.getCategoryId()));
                mallItemInfo.setUser(userMap.get(mallItemInfo.getUserId()));
            }
        }
        return new SuccessResponse(PageInfoUtils.getPageInfo(page));
    }

    @PutMapping("audit")
    @ApiOperation("审核项目")
    @ApiVersion(group = ApiVersionConstant.FAP_APP102)
    public BaseResponse audit(@RequestBody @Valid MallItemInfo itemDO) {
        MallItemInfo mallItemInfo = mallItemService.getById(itemDO.getId());
        if (mallItemInfo == null) {
            return new ErrorResponse(BusinessFailEnum.NOT_GET_DATA.getCode(), BusinessFailEnum.NOT_GET_DATA.getMessage());
        }
        if (mallItemInfo.getStatus().equals(ItemStatus.NOT_AUDIT)) {
            if (itemDO.getStatus().equals(ItemStatus.AUDIT_FAIL)) {
                if (StringUtils.isBlank(itemDO.getAuditDescription())) {
                    return new ErrorResponse("请输入拒绝原因");
                }
                Message message = Message.builder()
                        .content("您的广告原稿未通过审核！\n原因："
                                + itemDO.getAuditDescription()
                                + "，请修改后重新提交")
                        .toUserId(itemDO.getUserId())
                        .deleted(false)
                        .sender("审核通知")
                        .status(MessageStatus.UNREAD)
                        .createTime(new Date())
                        .build();
                messageService.createMessage(message);
            } else if (itemDO.getStatus().equals(ItemStatus.OFF_SHELVE)) {
                Message message = Message.builder()
                        .content("您的广告原稿已通过审核，快去发布吧~")
                        .toUserId(itemDO.getUserId())
                        .deleted(false)
                        .sender("审核通知")
                        .status(MessageStatus.UNREAD)
                        .createTime(new Date())
                        .build();
                messageService.createMessage(message);
            } else {
                return new ErrorResponse("审核状态今可选择未审核和未发布");
            }
        } else {
            return new ErrorResponse("该项目非待审核状态");
        }
        mallItemInfo.setStatus(itemDO.getStatus());
        mallItemInfo.setAuditTime(new Date());
        mallItemService.updateById(mallItemInfo);
        return new SuccessResponse();
    }

    @PostMapping("/removeItem")
    @ApiOperation("下架")
    @ApiVersion(group = ApiVersionConstant.FAP_APP102)
    public BaseResponse removeItem(@RequestBody MallItemInfo itemInfo) {
        itemBootService.removeItem(itemInfo);
        return new SuccessResponse();
    }

    @GetMapping("/getDetail")
    @ApiOperation("获取详细信息")
    @ApiVersion(group = ApiVersionConstant.FAP_APP102)
    public BaseResponse<MallItemInfo> getDetail(@RequestParam(required = true) Long id) {
        MallItemInfo mallItemInfo = mallItemService.getById(id);
        List<MallItemInfoDetail> mallItemInfoDetails = mallItemInfoDetailService.list(
                new LambdaQueryWrapper<MallItemInfoDetail>().eq(MallItemInfoDetail::getMallItemInfoId, id));
        mallItemInfo.setDetailList(mallItemInfoDetails);

        return new SuccessResponse(mallItemInfo);
    }

//    @PostMapping("")
////    public BaseResponse createItem(@RequestBody @Valid MallItemInfo itemDO,
////                                   BindingResult result) {
////        if (result.hasErrors()) {
////            return new ErrorResponse(result.getAllErrors().get(0).getDefaultMessage());
////        }
////        mallItemService.save(itemDO);
////        return new SuccessResponse();
////    }
////
////    @PutMapping("")
////    public BaseResponse updateItem(@RequestBody @Valid MallItemInfo itemDO,
////                                   BindingResult result) {
////        if (result.hasErrors()) {
////            return new ErrorResponse(result.getAllErrors().get(0).getDefaultMessage());
////        }
////
////        mallItemService.updateById(itemDO);
////
////        return new SuccessResponse();
////    }

    @PutMapping("status")
    public BaseResponse updateItemStatus(@RequestBody @Valid MallItemStatusForm form,
                                         BindingResult result) {
        if (result.hasErrors()) {
            return new ErrorResponse(result.getAllErrors().get(0).getDefaultMessage());
        }

        int itemId = form.getId();
        MallItemInfo item = mallItemService.getById(itemId);
        if (item == null) {
            return new ErrorResponse("商品不存在");
        }

        int status = form.getStatus();
        if (!ItemUtils.isStatusValid(status)) {
            return new ErrorResponse("商品状态不正确");
        }

        // 商品状态无法修改为未上架
        if (status == ItemStatus.NOT_AUDIT) {
            return new ErrorResponse("商品状态不正确");
        }

        if (item.getStatus() == status) {
            return new SuccessResponse();
        }

        item.setStatus(status);
        mallItemService.updateById(item);

        return new SuccessResponse();
    }

//    @PostMapping("off-shelf")
//    public BaseResponse batchOffShelf(@RequestBody List<Integer> ids) {
//        if (CollectionUtils.isEmpty(ids)) {
//            return new SuccessResponse();
//        }
//        Collection<MallItemInfo> mallItems = mallItemService.listByIds(ids);
//        for (MallItemInfo mallItem : mallItems) {
//            mallItem.setStatus(ItemStatus.OFF_SHELVE);
//        }
//        mallItemService.updateBatchById(mallItems);
//        return new SuccessResponse();
//    }
//
//    @PostMapping("on-shelf")
//    public BaseResponse batchOnShelf(@RequestBody List<Integer> ids) {
//        if (CollectionUtils.isEmpty(ids)) {
//            return new SuccessResponse();
//        }
//        Collection<MallItemInfo> mallItems = mallItemService.listByIds(ids);
//        for (MallItemInfo mallItem : mallItems) {
//            mallItem.setStatus(ItemStatus.ON_SALE);
//        }
//        mallItemService.updateBatchById(mallItems);
//        return new SuccessResponse();
//    }
}
