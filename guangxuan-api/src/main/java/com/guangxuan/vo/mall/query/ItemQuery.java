//package com.guangxuan.vo.mall.query;
//
//import com.easecoding.app.gugu.vo.PageRequestParam;
//import lombok.Data;
//
//import java.io.Serializable;
//
///**
// * @author deofly
// * @since 2019-05-03
// */
//@Data
//public class ItemQuery implements Serializable {
//
//    private static final long serialVersionUID = -2063175090831309288L;
//
//    private Integer last;
//
//    private Integer size;
//
//    private Integer category;
//
//    /**
//     * 0: sort ASC, id DESC
//     * 1: price ASC
//     * 2: price DESC
//     * 3: sales ASC
//     * 4: sales DESC
//     */
//    private Integer sortType;
//
//    private String search;
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
