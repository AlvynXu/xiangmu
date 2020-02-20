//package com.guangxuan.controller.admin;
//
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//import com.baomidou.mybatisplus.core.metadata.IPage;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.guangxuan.constant.AddressApplyStatus;
//import com.guangxuan.constant.AddressType;
//import com.guangxuan.model.AddressApplyRecord;
//import com.guangxuan.model.Area;
//import com.guangxuan.model.Booth;
//import com.guangxuan.model.Street;
//import com.guangxuan.service.AddressApplyRecordService;
//import com.guangxuan.service.AreaService;
//import com.guangxuan.service.BoothService;
//import com.guangxuan.service.StreetService;
//import com.guangxuan.shiro.util.PageInfoUtils;
//import com.guangxuan.vo.BaseResponse;
//import com.guangxuan.vo.ErrorResponse;
//import com.guangxuan.vo.PageRequestParam;
//import com.guangxuan.vo.SuccessResponse;
//import com.guangxuan.vo.admin.filter.AddressApplyFilter;
//import com.guangxuan.vo.admin.form.AddressCheckForm;
//import io.swagger.annotations.Api;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.*;
//
//import javax.annotation.Resource;
//import javax.validation.Valid;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import static com.guangxuan.shiro.util.PinYinUtils.getPinYinHeadChar;
//
///**
// * @author deofly
// * @since 2019-05-29
// */
//@RestController("AdminAddressController")
//@RequestMapping("/admin/address")
//public class AdminAddressController {
//
//    @Autowired
//    private AreaService areaService;
//
//    @Autowired
//    private AddressApplyRecordService addressApplyService;
//
//    @Resource
//    private StreetService streetService;
//
//    @Resource
//    private BoothService boothService;
//
//    /**
//     * 地址申请列表
//     *
//     * @param pageParam
//     * @param filter
//     * @return
//     */
//    @GetMapping("applications")
//    public BaseResponse getApplicationList(PageRequestParam pageParam, AddressApplyFilter filter) {
//        IPage<AddressApplyRecord> page =
//                addressApplyService.page(new Page<AddressApplyRecord>(pageParam.getOffset(), pageParam.getLimit()),
//                        new LambdaQueryWrapper<AddressApplyRecord>().eq(filter.getStatus() != null, AddressApplyRecord::getStatus, filter.getStatus())
//                                .eq(filter.getType() != null, AddressApplyRecord::getType, filter.getType()));
//        return new SuccessResponse(PageInfoUtils.getPageInfo(page));
//    }
//
//    @PostMapping("check")
//    @Transactional(rollbackFor = Exception.class)
//    public BaseResponse updateApplyStatus2Success(@Valid @RequestBody AddressCheckForm form,
//                                                  BindingResult result) {
//        if (result.hasErrors()) {
//            return new ErrorResponse(result.getAllErrors().get(0).getDefaultMessage());
//        }
//
//        int id = form.getId();
//        AddressApplyRecord record = addressApplyService.getById(id);
//        if (record == null) {
//            return new ErrorResponse("该地址申请记录不存在");
//        }
//
//        if (record.getStatus() == AddressApplyStatus.SUCCESS ||
//                record.getStatus() == AddressApplyStatus.FAIL) {
//            return new ErrorResponse("该地址已审核");
//        }
//
//        boolean isSuccess = form.getSuccess();
//        if (isSuccess) {
//            // 添加新地址
//            int type = record.getType();
//            int parentId = record.getParentId();
//            if (type == AddressType.STREET) {
//                Area district = areaService.getById(parentId);
//                if (district == null) {
//                    record.setCheckTime(new Date());
//                    record.setStatus(AddressApplyStatus.FAIL);
//                    addressApplyService.updateById(record);
//                    return new ErrorResponse("省市区不存在");
//                }
//                Area city = areaService.getOne(new LambdaQueryWrapper<Area>().eq(Area::getCode, district.getParentCode()));
//                String cityHeader = getPinYinHeadChar(city.getName());
//                Street street = new Street();
//                street.setAreaCode(district.getCode());
//                street.setCode(cityHeader + getPinYinHeadChar(street.getName()));
//                street.setName(record.getContent());
//                streetService.save(street);
//                List<Booth> booths = new ArrayList<>();
//                for (int i = 0; i < 30; i++) {
//                    Booth booth = new Booth();
//                    booth.setStreetId(street.getId());
//                    booth.setBoothCode(cityHeader.substring(0,2) + getPinYinHeadChar(street.getName()).substring(0,2) + (10000+i));
//                    booth.setStatus(0);
//                    booths.add(booth);
//                }
//                boothService.saveBatch(booths);
//            }
//            record.setCheckTime(new Date());
//            record.setStatus(AddressApplyStatus.SUCCESS);
//            addressApplyService.updateById(record);
//        } else {
//            record.setCheckTime(new Date());
//            record.setStatus(AddressApplyStatus.FAIL);
//            addressApplyService.updateById(record);
//        }
//        return new SuccessResponse();
//    }
//}
