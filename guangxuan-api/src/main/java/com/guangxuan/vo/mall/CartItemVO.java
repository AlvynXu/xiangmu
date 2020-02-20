//package com.guangxuan.vo.mall;
//
//import com.easecoding.app.gugu.domain.MallCartDO;
//import com.easecoding.app.gugu.domain.MallItemDO;
//import com.fasterxml.jackson.annotation.JsonInclude;
//import lombok.Data;
//import org.springframework.util.CollectionUtils;
//
//import java.io.Serializable;
//import java.math.BigDecimal;
//
///**
// * @author deofly
// * @since 2019-05-14
// */
//@Data
//@JsonInclude(JsonInclude.Include.NON_NULL)
//public class CartItemVO implements Serializable {
//
//    private static final long serialVersionUID = 1399213800289452931L;
//
//    private int itemId;
//
//    private int quantity;
//
//    private String headPic;
//
//    private String title;
//
//    private BigDecimal tagPrice;
//
//    private BigDecimal price;
//
//    private BigDecimal extraPrice;
//
//    private String spec;
//
//    private int inventory;
//
//    public CartItemVO() {}
//
//    public CartItemVO(MallCartDO cartDO) {
//        if (cartDO == null || cartDO.getItem() == null
//                || CollectionUtils.isEmpty(cartDO.getItem().getHeadPicList())) {
//            return;
//        }
//
//        MallItemDO item = cartDO.getItem();
//        this.itemId = cartDO.getItemId();
//        this.quantity = cartDO.getQuantity();
//        this.title = item.getTitle();
//        this.price = item.getPrice();
//        this.tagPrice = item.getTagPrice();
//        this.extraPrice = item.getExtraPrice();
//        this.spec = item.getSpec();
//        this.inventory = item.getInventory();
//        this.headPic = item.getHeadPicList().get(0);
//    }
//}
