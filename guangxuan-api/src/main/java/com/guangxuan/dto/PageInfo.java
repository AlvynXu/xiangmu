package com.guangxuan.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

/**
 * @author zhuolin
 * @Date 2019/11/30
 */
@Data
public class PageInfo<T> extends Page<T> {

    private Integer totalCount;

    private Long soldCount;

    private Long notSoldCount;
}
