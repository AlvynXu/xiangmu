package com.guangxuan.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.guangxuan.dto.domain.CategoryDTO;
import com.guangxuan.model.MallItemCategory;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 商城商品类目 服务类
 * </p>
 *
 * @author zhuolin
 * @since 2019-11-27
 */
public interface MallItemCategoryService extends IService<MallItemCategory> {
    /**
     * 获取街道
     * @return
     */
    Object getList();

    /**
     * 分页获取列表
     * @param page
     * @param size
     * @return
     */
    IPage<CategoryDTO> getAllCategories(Integer page, Integer size);
}
