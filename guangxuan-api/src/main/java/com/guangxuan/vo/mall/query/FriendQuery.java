//package com.guangxuan.vo.mall.query;
//
//import com.easecoding.app.gugu.vo.PageRequestParam;
//import com.fasterxml.jackson.annotation.JsonFormat;
//import lombok.Data;
//import org.springframework.format.annotation.DateTimeFormat;
//
//import java.io.Serializable;
//import java.time.LocalDateTime;
//
///**
// * @author deofly
// * @since 2019-05-30
// */
//@Data
//public class FriendQuery implements Serializable {
//
//    private static final long serialVersionUID = 7511084015616162616L;
//
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    private LocalDateTime time;
//
//    private Integer size;
//
//    private Integer threshold; // 好友的好友数阈值
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
