package com.guangxuan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guangxuan.dto.domain.CategoryDTO;
import com.guangxuan.model.MallItemCategory;
import com.guangxuan.mapper.MallItemCategoryMapper;
import com.guangxuan.service.MallItemCategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商城商品类目 服务实现类
 * </p>
 *
 * @author zhuolin
 * @since 2019-11-27
 */
@Service
public class MallItemCategoryServiceImpl extends ServiceImpl<MallItemCategoryMapper, MallItemCategory> implements MallItemCategoryService {

    @Override
//   // @Cacheable(cacheNames = "GUANGXUAN:CATEGORY:LIST")
    public Object getList() {
        return this.list(new LambdaQueryWrapper<MallItemCategory>()
                .eq(MallItemCategory::getVisible, true).orderByDesc(MallItemCategory::getSort));
    }

    @Override
    public IPage<CategoryDTO> getAllCategories(Integer page, Integer size) {
        return this.getBaseMapper().getAllCategories(new Page(page, size));
    }
}
