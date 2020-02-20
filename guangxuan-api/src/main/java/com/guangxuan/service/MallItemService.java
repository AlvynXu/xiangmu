package com.guangxuan.service;

import com.guangxuan.model.MallItem;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 项目信息 服务类
 * </p>
 *
 * @author zhuolin
 * @since 2019-11-27
 */
public interface MallItemService extends IService<MallItem> {

    /**
     * 获取订单下的物品
     *
     * @param orderId
     * @return
     */
    List<MallItem> getItems(Long orderId);

}
