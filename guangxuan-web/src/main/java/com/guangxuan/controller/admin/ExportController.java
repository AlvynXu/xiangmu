//package com.guangxuan.controller.admin;
//
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//import com.guangxuan.constant.OrderStatus;
//import com.guangxuan.dto.domain.MallOrderDO;
//import com.guangxuan.model.MallAddress;
//import com.guangxuan.model.MallItem;
//import com.guangxuan.model.MallOrder;
//import com.guangxuan.service.MallItemService;
//import com.guangxuan.service.MallOrderService;
//import com.guangxuan.vo.admin.filter.MallOrderFilter;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.poi.ss.usermodel.HorizontalAlignment;
//import org.apache.poi.xssf.usermodel.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.util.CollectionUtils;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.annotation.Resource;
//import java.io.BufferedOutputStream;
//import java.io.IOException;
//import java.io.OutputStream;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.List;
//
///**
// * 数据导出
// *
// * @author deofly
// * @since 2019-06-13
// */
//@RestController("AdminExportController")
//@RequestMapping("/api/admin/export")
//public class AdminExportController extends BaseController {
//
//    @Autowired
//    private MallOrderService mallOrderService;
//
//    @Resource
//    private MallItemService mallItemService;
//
//    @PostMapping("mall/orders")
//    public void exportMallOrders(MallOrderFilter filter) throws IOException {
//        XSSFWorkbook workbook = new XSSFWorkbook();
//        // 创建一个Excel表单,参数为sheet的名字
//        XSSFSheet sheet = workbook.createSheet("商品订单");
//        // 创建表头
//        setTitle(workbook, sheet);
//
//        List<MallOrder> orders = mallOrderService.list(
//                new LambdaQueryWrapper<MallOrder>().eq(filter.getStatus() != null && filter.getStatus() > 0,MallOrder::getStatus,filter.getStatus())
//                        .eq(StringUtils.isNoneBlank(filter.getOrderId()),MallOrder::getOrderId,filter.getOrderId())
//                        .ge(filter.getStartTime()!=null,MallOrder::getCreateTime,filter.getStartTime())
//                        .le(filter.getEndTime()!=null,MallOrder::getCreateTime,filter.getEndTime())
//        );
//        int rowId = 1;
//        for (MallOrder order : orders) {
//            XSSFRow row = sheet.createRow(rowId);
//            row.createCell(0).setCellValue(order.getOrderId());
//
//            List<MallItem> items = mallItemService.getItems(order.getId());
//            if (!CollectionUtils.isEmpty(items)) {
//                StringBuilder itemBuilder = new StringBuilder();
//                for (int i = 0; i < items.size(); ++i) {
//                    MallItem item = items.get(i);
//                    itemBuilder.append(String.format(
//                            "ID: %d, %s, 数量: %d, 单价(元): %s",
//                            item.getId(),
//                            item.getTitle(),
//                            items.get(i).getQuantity(),
//                            item.getPrice().stripTrailingZeros().toPlainString()));
//                    itemBuilder.append("\r\n");
//                }
//                row.createCell(1).setCellValue(itemBuilder.toString());
//            }
//            row.createCell(2).setCellValue(order.getAmount().stripTrailingZeros().toPlainString());
//
//            String statusStr = "";
//            if (order.getStatus() == OrderStatus.CLOSE) {
//                statusStr = "已关闭";
//            } else if (order.getStatus() == OrderStatus.NOT_PAY) {
//                statusStr = "待支付";
//            } else if (order.getStatus() == OrderStatus.NOT_DELIVER) {
//                statusStr = "待发货";
//            } else if (order.getStatus() == OrderStatus.NOT_RECEIVE) {
//                statusStr = "待收货";
//            } else if (order.getStatus() == OrderStatus.NOT_RATE) {
//                statusStr = "待评价";
//            } else if (order.getStatus() == OrderStatus.AFTER_SALE) {
//                statusStr = "售后";
//            }
//            row.createCell(3).setCellValue(statusStr);
//
//            row.createCell(4).setCellValue(order.getCreateTime().toString());
//
//            MallAddress address = order.getAddress();
//            if (address != null) {
//                row.createCell(5).setCellValue(order.getAddress().getName());
//                row.createCell(6).setCellValue(order.getAddress().getPhone());
//                row.createCell(7).setCellValue(order.getAddress().getFullAddress());
//            } else {
//                row.createCell(7).setCellValue("自提");
//            }
//
//            String payTypeStr = "";
//            if (order.getPayType() == PayType.WX) {
//                payTypeStr = "微信";
//            } else if (order.getPayType() == PayType.ALIPAY) {
//                payTypeStr = "支付宝";
//            } else if (order.getPayType() == PayType.BALANCE) {
//                payTypeStr = "余额";
//            }
//            row.createCell(8).setCellValue(payTypeStr);
//            if (order.getPaidTime() != null) {
//                row.createCell(9).setCellValue(order.getPaidTime().toString());
//            }
//
//            rowId++;
//        }
//
//        String fileName = "mall-orders-"
//                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
//                + ".xlsx";
//        //清空response
//        response.reset();
//        //设置response的Header
//        response.addHeader("Content-Disposition", "attachment;filename="+ fileName);
//        OutputStream os = new BufferedOutputStream(response.getOutputStream());
//        response.setContentType("application/vnd.ms-excel;charset=utf-8");
////        response.setContentType("application/octet-stream");
//        //将excel写入到输出流中
//        workbook.write(os);
//        os.flush();
//        os.close();
//    }
//
//    /**
//     * 设置表头
//     *
//     * @param workbook
//     * @param sheet
//     */
//    private void setTitle(XSSFWorkbook workbook, XSSFSheet sheet) {
//        XSSFRow row = sheet.createRow(0);
//        // 设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
//        sheet.setColumnWidth(0, 32 * 256);
//        sheet.setColumnWidth(1, 128 * 256);
//        sheet.setColumnWidth(2, 16 * 256);
//        sheet.setColumnWidth(3, 16 * 256);
//        sheet.setColumnWidth(4, 32 * 256);
//        sheet.setColumnWidth(5, 16 * 256);
//        sheet.setColumnWidth(6, 16 * 256);
//        sheet.setColumnWidth(7, 32 * 256);
//        sheet.setColumnWidth(8, 16 * 256);
//        sheet.setColumnWidth(9, 32 * 256);
//
//        //设置为居中加粗
//        XSSFCellStyle style = workbook.createCellStyle();
//        XSSFFont font = workbook.createFont();
//        font.setBold(true);
//        style.setFont(font);
//        style.setAlignment(HorizontalAlignment.CENTER);
//
//        XSSFCell cell;
//        cell = row.createCell(0);
//        cell.setCellValue("订单号");
//        cell.setCellStyle(style);
//
//        cell = row.createCell(1);
//        cell.setCellValue("商品");
//        cell.setCellStyle(style);
//
//        cell = row.createCell(2);
//        cell.setCellValue("总金额(元)");
//        cell.setCellStyle(style);
//
//        cell = row.createCell(3);
//        cell.setCellValue("状态");
//        cell.setCellStyle(style);
//
//        cell = row.createCell(4);
//        cell.setCellValue("创建时间");
//        cell.setCellStyle(style);
//
//        cell = row.createCell(5);
//        cell.setCellValue("收货人姓名");
//        cell.setCellStyle(style);
//
//        cell = row.createCell(6);
//        cell.setCellValue("收货人电话");
//        cell.setCellStyle(style);
//
//        cell = row.createCell(7);
//        cell.setCellValue("收货地址");
//        cell.setCellStyle(style);
//
//        cell = row.createCell(8);
//        cell.setCellValue("支付方式");
//        cell.setCellStyle(style);
//
//        cell = row.createCell(9);
//        cell.setCellValue("支付时间");
//        cell.setCellStyle(style);
//    }
//}
