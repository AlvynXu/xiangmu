package com.guangxuan.service;

import com.guangxuan.dto.RentDTO;
import com.guangxuan.dto.RentOtherDTO;
import com.guangxuan.dto.RentToOtherDTO;
import com.guangxuan.model.Market;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhuolin
 * @since 2019-12-17
 */
public interface MarketService extends IService<Market> {
    /**
     * 获取我的租赁信息
     * @param type
     * @param areaCode
     * @param userId
     * @return
     */
    Object getMyRent(Integer type, String areaCode, Long userId);

    /**
     * 获取租赁市场信息
     * @param type
     * @param areaCode
     * @param page
     * @param size
     * @return
     */
    Object getRent(Integer type, String areaCode, Integer page, Integer size,Long userId);

    /**
     * 出租
     * @param rentDTO
     * @param id
     * @return
     */
    Object rent(RentDTO rentDTO, Long id);


    /**
     * 获取可以出租的街道
     * @param userId
     * @param orderId
     * @return
     */
    Object getCanRentStreet(Long userId, Long orderId);

    /**
     * 租给他
     * @param rentOtherDTO
     * @param userId
     * @return
     */
    Object rentToOther(RentToOtherDTO rentOtherDTO, Long userId);

    /**
     * 租用
     * @param rentOtherDTO
     * @param userId
     * @return
     */
    Object rentOther(RentOtherDTO rentOtherDTO, Long userId);

    /**
     * 获取租赁费率
     * @return
     */
    Object getRentRate();
    /**
     * 根据省份发布求租
     * @param provinceCode
     * @param days
     * @param price
     * @param userId
     * @param majorId
     */
    void seeKProvince(String provinceCode, Integer days, BigDecimal price, Long userId, Long majorId);
}
