package com.guangxuan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guangxuan.model.ItemCollection;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhuolin
 * @since 2019-12-18
 */
public interface ItemCollectionService extends IService<ItemCollection> {
    /**
     * 收藏
     * @param itemCollection
     * @param id
     * @return
     */
    Object collection(ItemCollection itemCollection, Long id);

    /**
     * 取消收藏
     * @param itemId
     * @param id
     * @return
     */
    Object cancelCollection(Long itemId, Long id);


}
