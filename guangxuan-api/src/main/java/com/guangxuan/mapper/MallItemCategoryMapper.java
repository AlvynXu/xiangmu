package com.guangxuan.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guangxuan.dto.domain.CategoryDTO;
import com.guangxuan.model.MallItemCategory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 商城商品类目 Mapper 接口
 * </p>
 *
 * @author zhuolin
 * @since 2019-11-27
 */
public interface MallItemCategoryMapper extends BaseMapper<MallItemCategory> {
    /**
     * 获取全部商品类目
     * @param page
     * @return
     */
    IPage<CategoryDTO> getAllCategories(Page page);
}
