package com.guangxuan.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.guangxuan.dto.UserStreetDTO;
import com.guangxuan.dto.domain.StreetDTO;
import com.guangxuan.model.Street;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 街道 服务类
 * </p>
 *
 * @author zhuolin
 * @since 2019-11-25
 */
public interface StreetService extends IService<Street> {
    /**
     * 获取街道售卖情况
     * @return
     */
    Object getCountMessage();

    /**
     * 获取地主分页
     * @param page
     * @param size
     * @param type
     * @param areaCode
     * @param areaType
     * @return
     */
    Object getStreetPage(Integer page, Integer size, Integer type, String areaCode, Integer areaType);

    /**
     * 获取市名
     * @param cityName
     * @return
     */
    Object getCityCode(String cityName);

    /**
     * 购买结果详情
     * @param id
     * @param type
     * @return
     */
    Object getDetail(Long id, Integer type);

    /**
     * 获取当前用户已购地主
     * @param userId
     * @param page
     * @param size
     * @param status
     * @return
     */
    Object getCurrentUserStreet(Long userId, Integer page, Integer size, Integer status);

    /**
     *
     * @param page
     * @param size
     * @param name
     * @param areaCode
     * @return
     */
    IPage<StreetDTO> adminPageList(Integer page, Integer size, String name, String areaCode);

    /**
     * 保存街道
     * @param streetDO
     */
    Object saveStreet(Street streetDO);

    /**
     * 修改
     * @param streetDO
     */
    Object updateInfo(Street streetDO);

    /**
     * 根据订单获取街道信息
     * @param orderId
     * @return
     */
    List<UserStreetDTO> getMarketStreet(Long orderId);

    /**
     * 获取城市下街道
     * @param cityCode
     * @return
     */
    List<Street> getCityStreet(String cityCode);
}
