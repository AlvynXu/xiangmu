//package com.guangxuan.controller.admin;
//
//import com.easecoding.app.gugu.domain.WithdrawRecordDO;
//import com.easecoding.app.gugu.service.IWithdrawRecordService;
//import com.easecoding.app.gugu.vo.BaseResponse;
//import com.easecoding.app.gugu.vo.ErrorResponse;
//import com.easecoding.app.gugu.vo.PageRequestParam;
//import com.easecoding.app.gugu.vo.SuccessResponse;
//import com.easecoding.app.gugu.vo.admin.filter.WithdrawRecordFilter;
//import com.github.pagehelper.PageInfo;
//import com.guangxuan.service.WithdrawRecordService;
//import com.guangxuan.vo.PageRequestParam;
//import com.guangxuan.vo.admin.filter.WithdrawRecordFilter;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.time.LocalDateTime;
//
///**
// * @author deofly
// * @since 2019-06-20
// */
//@RestController("AdminWithdrawController")
//@RequestMapping("/api/admin/withdraw")
//public class WithdrawController extends BaseController {
//
//    @Autowired
//    private WithdrawRecordService withdrawRecordService;
//
//    @GetMapping("records")
//    public BaseResponse getWithdrawRecordList(PageRequestParam pageParam,
//                                              WithdrawRecordFilter filter) {
//        PageInfo pageInfo;
//        if (filter == null) {
//            pageInfo = withdrawRecordService.getRecordList(
//                    pageParam.getOffset(), pageParam.getLimit());
//        } else {
//            pageInfo = withdrawRecordService.getRecordList(
//                    filter, pageParam.getOffset(), pageParam.getLimit());
//        }
//
//        return new SuccessResponse(pageInfo);
//    }
//
//    @PostMapping("{id}")
//    public BaseResponse withdraw(@PathVariable int id) {
//        WithdrawRecordDO record = withdrawRecordService.get(id);
//        if (record == null) {
//            return new ErrorResponse("该提现记录不存在");
//        }
//
//        if (record.getCompleteTime() != null) {
//            return new ErrorResponse("无法重复提现");
//        }
//
//        record.setCompleteTime(LocalDateTime.now());
//        withdrawRecordService.update(record);
//
//        return new SuccessResponse();
//    }
//}
