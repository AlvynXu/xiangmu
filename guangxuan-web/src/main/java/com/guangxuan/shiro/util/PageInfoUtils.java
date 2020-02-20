package com.guangxuan.shiro.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author zhuolin
 * @Date 2019/12/5
 */
public class PageInfoUtils {

    public static <T> PageInfo<T> getPageInfo(IPage<T> page) {
        PageInfo pageInfo = new PageInfo();
        pageInfo.setEndRow(Long.valueOf(page.getCurrent()*page.getSize()).intValue());
        pageInfo.setHasNextPage(page.getCurrent() < page.getPages());
        pageInfo.setHasPreviousPage(page.getCurrent() != 1);
        pageInfo.setIsFirstPage(page.getCurrent() <= 1);
        pageInfo.setIsLastPage(page.getCurrent() >= page.getPages());
        pageInfo.setNextPage(Long.valueOf(Math.min(page.getCurrent()+1,page.getPages())).intValue());
        pageInfo.setPageNum(Long.valueOf(page.getCurrent()).intValue());
        pageInfo.setPages(Long.valueOf(page.getPages()).intValue());
        pageInfo.setPageSize(Long.valueOf(page.getSize()).intValue());
        pageInfo.setPrePage(Long.valueOf(Math.max(page.getCurrent()-1,1)).intValue());
        pageInfo.setSize(Long.valueOf(page.getSize()).intValue());
        pageInfo.setStartRow(Long.valueOf((page.getCurrent()-1)*page.getSize()).intValue());
        pageInfo.setList(page.getRecords());
        pageInfo.setTotal(page.getTotal());
        return pageInfo;
    }

    public static <T> PageInfo<T> getPageInfo(IPage page, List<T> data) {
        PageInfo pageInfo = new PageInfo();
        pageInfo.setEndRow(Long.valueOf(page.getCurrent()*page.getSize()).intValue());
        pageInfo.setHasNextPage(page.getCurrent() < page.getPages());
        pageInfo.setHasPreviousPage(page.getCurrent() != 1);
        pageInfo.setIsFirstPage(page.getCurrent() <= 1);
        pageInfo.setIsLastPage(page.getCurrent() >= page.getPages());
        pageInfo.setNextPage(Long.valueOf(Math.min(page.getCurrent()+1,page.getPages())).intValue());
        pageInfo.setPageNum(Long.valueOf(page.getCurrent()).intValue());
        pageInfo.setPages(Long.valueOf(page.getPages()).intValue());
        pageInfo.setPageSize(Long.valueOf(page.getSize()).intValue());
        pageInfo.setPrePage(Long.valueOf(Math.max(page.getCurrent()-1,1)).intValue());
        pageInfo.setSize(Long.valueOf(page.getSize()).intValue());
        pageInfo.setStartRow(Long.valueOf((page.getCurrent()-1)*page.getSize()).intValue());
        pageInfo.setList(data);
        pageInfo.setTotal(page.getTotal());
        return pageInfo;
    }
}
