package com.guangxuan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guangxuan.enumration.BusinessFailEnum;
import com.guangxuan.exception.BusinessException;
import com.guangxuan.mapper.ItemCollectionMapper;
import com.guangxuan.model.ItemCollection;
import com.guangxuan.model.MallItemInfo;
import com.guangxuan.service.ItemCollectionService;
import com.guangxuan.service.MallItemInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zhuolin
 * @since 2019-12-18
 */
@Service
public class ItemCollectionServiceImpl extends ServiceImpl<ItemCollectionMapper, ItemCollection> implements ItemCollectionService {


    @Resource
    private MallItemInfoService mallItemInfoService;

    @Override
    public Object collection(ItemCollection itemCollection, Long id) {
        MallItemInfo mallItemInfo = mallItemInfoService.getById(itemCollection.getItemId());
        if (mallItemInfo == null) {
            throw new BusinessException(BusinessFailEnum.NOT_GET_DATA);
        }
        Integer count = this.count(new LambdaQueryWrapper<ItemCollection>().eq(ItemCollection::getItemId, itemCollection.getItemId()).eq(ItemCollection::getUserId, id));
        if(count > 0){
            throw  new BusinessException(BusinessFailEnum.COLLECTED);
        }
        itemCollection.setUserId(id);
        itemCollection.setCreateTime(new Date());
        this.save(itemCollection);
        return null;
    }

    @Override
    public Object cancelCollection(Long itemId, Long id) {
        this.remove(new LambdaQueryWrapper<ItemCollection>().eq(ItemCollection::getItemId, itemId).eq(ItemCollection::getUserId, id));
        return null;
    }


}
