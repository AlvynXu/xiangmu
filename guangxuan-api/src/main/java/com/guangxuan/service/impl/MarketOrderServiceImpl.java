package com.guangxuan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guangxuan.constant.PayConstant;
import com.guangxuan.model.MarketOrder;
import com.guangxuan.mapper.MarketOrderMapper;
import com.guangxuan.service.MarketOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zhuolin
 * @since 2019-12-30
 */
@Service
public class MarketOrderServiceImpl extends ServiceImpl<MarketOrderMapper, MarketOrder> implements MarketOrderService {


    @Override
    public List<MarketOrder> getMyCityMarket(String areaCode, Integer type, Long userId) {
        return this.list(new LambdaQueryWrapper<MarketOrder>()
                .eq(MarketOrder::getCityCode, areaCode)
                .eq(MarketOrder::getUserId, userId)
                .eq(MarketOrder::getPayStatus, PayConstant.SUCCESS_PAY)
                .orderByDesc(MarketOrder::getPrice));
    }

    @Override
    public Object getMyCityMarketPage(Page page, String areaCode, Integer type, Long userId) {
        return this.page(page, new LambdaQueryWrapper<MarketOrder>()
                .eq(MarketOrder::getCityCode, areaCode)
                .ne(MarketOrder::getUserId, userId)
                .eq(MarketOrder::getPayStatus, PayConstant.SUCCESS_PAY)
                .orderByDesc(MarketOrder::getPrice));
    }

}
