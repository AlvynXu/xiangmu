//package com.guangxuan.controller.admin;
//
//import com.baomidou.mybatisplus.core.metadata.IPage;
//import com.github.pagehelper.PageInfo;
//import com.guangxuan.service.MallOrderService;
//import com.guangxuan.vo.BaseResponse;
//import com.guangxuan.vo.PageRequestParam;
//import com.guangxuan.vo.SuccessResponse;
//import com.guangxuan.vo.admin.filter.MallOrderFilter;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.*;
//
//import javax.validation.Valid;
//
///**
// * @author deofly
// * @since 2019-05-24
// */
//@RestController
//@RequestMapping("/api/admin/mall/orders")
//public class MallOrderController extends BaseController {
//
//    @Autowired
//    private MallOrderService mallOrderService;
//
//    @GetMapping("")
//    public BaseResponse getOrderList(PageRequestParam pageParam, MallOrderFilter filter) {
//        PageInfo pageInfo;
//        IPage page = mallOrderService
//        if (filter == null) {
//            pageInfo = mallOrderService.getOrderList(pageParam.getOffset(), pageParam.getLimit());
//        } else {
//            pageInfo = mallOrderService.getOrderList(filter,
//                    pageParam.getOffset(), pageParam.getLimit());
//        }
//
//        return new SuccessResponse(pageInfo);
//    }
//
//    /**
//     * 发货
//     */
//    @PostMapping("deliver")
//    public BaseResponse deliver(@Valid @RequestBody MallOrderStatusForm form,
//                                BindingResult result) {
//        if (result.hasErrors()) {
//            return new ErrorResponse(result.getAllErrors().get(0).getDefaultMessage());
//        }
//
//        String orderId = form.getOrderId();
//        MallOrderDO order = mallOrderService.getOrderByOrderId(orderId);
//        if (order == null) {
//            return new ErrorResponse("订单不存在");
//        }
//
//        int status = order.getStatus();
//        if (status > OrderStatus.NOT_DELIVER) {
//            return new SuccessResponse("商家已发货");
//        }
//
//        if (status != OrderStatus.NOT_DELIVER) {
//            return new ErrorResponse("无法发货");
//        }
//
//        mallOrderService.updateOrderStatus(order, OrderStatus.NOT_RECEIVE);
//
//        return new SuccessResponse();
//    }
//}
