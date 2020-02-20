//package com.guangxuan.vo.mall.query;
//
//import com.easecoding.app.gugu.vo.PageRequestParam;
//import lombok.Data;
//
//import java.io.Serializable;
//
///**
// * @author deofly
// * @since 2019-06-04
// */
//@Data
//public class StreetQuery implements Serializable {
//
//    private static final long serialVersionUID = 7367538230574492922L;
//
//    private Integer parentCode;
//
//    private Integer id;
//
//    private Integer last;
//
//    private Integer size;
//
//    public int getSize() {
//        if (size == null) {
//            return PageRequestParam.MIN_PAGE_LIMIT;
//        }
//
//        return Math.max(PageRequestParam.MIN_PAGE_LIMIT,
//                Math.min(this.size, PageRequestParam.MAX_PAGE_LIMIT));
//    }
//}
