package com.guangxuan.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guangxuan.model.MarketOrder;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhuolin
 * @since 2019-12-30
 */
public interface MarketOrderService extends IService<MarketOrder> {
    /**
     * 获取我的求租
     * @param areaCode
     * @param type
     * @param userId
     * @return
     */
    List<MarketOrder> getMyCityMarket(String areaCode, Integer type, Long userId);

    /**
     *
     * @param page
     * @param areaCode
     * @param type
     * @param userId
     * @return
     */
    Object getMyCityMarketPage(Page page, String areaCode, Integer type, Long userId);


}
