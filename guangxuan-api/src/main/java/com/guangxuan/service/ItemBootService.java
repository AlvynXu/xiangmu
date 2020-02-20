package com.guangxuan.service;

import com.guangxuan.model.ItemBoot;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guangxuan.model.MallItemInfo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhuolin
 * @since 2019-11-28
 */
public interface ItemBootService extends IService<ItemBoot> {
    /**
     * 物品挂到展位上
     * @param itemBoot
     * @return
     */
    Object saveItemBooth(ItemBoot itemBoot);
    /**
     * 物品解除展位
     * @param itemBoot
     * @return
     */
    Object cancelItemBooth(ItemBoot itemBoot);

    /**
     * 获取可以使用展位
     * @param userId
     * @param page
     * @param size
     * @param itemId
     * @return
     */
    Object getCanUseBooth(Long userId, Integer page, Integer size, Long itemId);

    /**
     * 移除展位
     * @param itemBoot
     * @return
     */
    Object removeItem(MallItemInfo itemBoot);
}
