package com.guangxuan.mapper;

import com.guangxuan.model.MallItem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 项目信息 Mapper 接口
 * </p>
 *
 * @author zhuolin
 * @since 2019-11-27
 */
public interface MallItemMapper extends BaseMapper<MallItem> {
    /**
     * 获取订单下的物品
     * @param orderId
     * @return
     */
    List<MallItem> getItems(Long orderId);
}
