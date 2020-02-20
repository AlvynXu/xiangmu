package com.guangxuan.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guangxuan.dto.MarketDTO;
import com.guangxuan.dto.UserStreetDTO;
import com.guangxuan.model.Market;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author zhuolin
 * @since 2019-12-17
 */
public interface MarketMapper extends BaseMapper<Market> {
    /**
     * 获取城市下的我的租赁情况
     *
     * @param areaCode
     * @param type
     * @param userId
     * @return
     */
    List<MarketDTO> getMyCityMarket(@Param("areaCode") String areaCode,
                                    @Param("type") Integer type, @Param("userId") Long userId, @Param("status") List<Integer> status);

    /**
     * 获取区县下的我的租赁情况
     *
     * @param areaCode
     * @param type
     * @param userId
     * @return
     */
    List<MarketDTO> getMyDistrictMarket(@Param("areaCode") String areaCode,
                                        @Param("type") Integer type, @Param("userId") Long userId, @Param("status") List<Integer> status);

    /**
     * 获取区县下的我的租赁情况
     *
     * @param areaCode
     * @param type
     * @param userId
     * @return
     */
    List<MarketDTO> getMyStreetMarket(@Param("areaCode") String areaCode,
                                      @Param("type") Integer type, @Param("userId") Long userId, @Param("status") List<Integer> status);

    /**
     * 获取城市下的我的租赁情况
     *
     * @param areaCode
     * @param type
     * @return
     */
    IPage<MarketDTO> getMyCityMarketPage(Page page, @Param("areaCode") String areaCode, @Param("type") Integer type,
                                         @Param("status") List<Integer> status, @Param("soldStatus") List<Integer> soldStatus);

    /**
     * 获取区县下的我的租赁情况
     *
     * @param areaCode
     * @param type
     * @return
     */
    IPage<MarketDTO> getMyDistrictMarketPage(Page page, @Param("areaCode") String areaCode, @Param("type") Integer type,
                                             @Param("status") List<Integer> status, @Param("soldStatus") List<Integer> soldStatus);

    /**
     * 获取区县下的我的租赁情况
     *
     * @param areaCode
     * @param type
     * @return
     */
    IPage<MarketDTO> getMyStreetMarketPage(Page page, @Param("areaCode") String areaCode, @Param("type") Integer type,
                                           @Param("status") List<Integer> status, @Param("soldStatus") List<Integer> soldStatus);

    /**
     * 按调价查找租赁信息
     *
     * @param areaCode
     * @param days
     * @param price
     * @param type
     * @return
     */
    List<Market> getCityMarket(@Param("areaCode") String areaCode, @Param("days") Integer days, @Param("status") Integer status,
                               @Param("price") BigDecimal price, @Param("type") Integer type);
    /**
     * 按调价查找租赁信息
     *
     * @param areaCode
     * @param days
     * @param price
     * @param type
     * @return
     */
    List<Market> getDistrictMarket(@Param("areaCode") String areaCode, @Param("days") Integer days, @Param("status") Integer status,
                               @Param("price") BigDecimal price, @Param("type") Integer type);
    /**
     * 按调价查找租赁信息
     *
     * @param areaCode
     * @param days
     * @param price
     * @param type
     * @return
     */
    List<Market> getStreetMarket(@Param("areaCode") String areaCode, @Param("days") Integer days, @Param("status") Integer status,
                                   @Param("price") BigDecimal price, @Param("type") Integer type);

    /**
     * 匹配
     * @param userId
     * @param orderId
     * @return
     */
    List<UserStreetDTO> getUserStreetDTO(@Param("userId")Long userId,@Param("orderId") Long orderId);
}
