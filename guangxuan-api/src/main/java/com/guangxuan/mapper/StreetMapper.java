package com.guangxuan.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guangxuan.dto.SoldInfoDTO;
import com.guangxuan.dto.UserStreetDTO;
import com.guangxuan.dto.domain.StreetDTO;
import com.guangxuan.model.Booth;
import com.guangxuan.model.Street;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 街道 Mapper 接口
 * </p>
 *
 * @author zhuolin
 * @since 2019-11-27
 */
public interface StreetMapper extends BaseMapper<Street> {

    /**
     * 分页信息
     *
     * @param page
     * @param userId
     * @param status
     * @return
     */
    IPage<SoldInfoDTO> getPage(Page page, @Param("userId") Long userId, @Param("status") Integer status);

    /**
     * 获取市下面的展位
     *
     * @param streetPage
     * @param areaCode
     * @param type
     * @return
     */
    IPage<Street> getCityPage(Page<Booth> streetPage, @Param("areaCode") String areaCode, @Param("type") Integer type);

    /**
     * 获取区县下面的展位
     *
     * @param streetPage
     * @param areaCode
     * @param type
     * @return
     */
    IPage<Street> getDistrictPage(Page<Booth> streetPage, @Param("areaCode") String areaCode, @Param("type") Integer type);

    /**
     * 获取区县下面的展位
     *
     * @param streetPage
     * @param areaCode
     * @param type
     * @return
     */
    IPage<Street> getStreetPage(Page<Booth> streetPage, @Param("areaCode") String areaCode, @Param("type") Integer type);

    /**
     * 获取分页
     *
     * @param page
     * @param name
     * @param areaCode
     * @return
     */
    IPage<StreetDTO> adminPageList(Page page, @Param("name") String name, @Param("areaCode") String areaCode);

    /**
     * 获取租赁订单街道
     *
     * @param orderId
     * @return
     */
    List<UserStreetDTO> getMarketStreet(Long orderId);

    /**
     * 获取市下街道
     *
     * @param cityCode
     * @return
     */
    List<Street> getCityStreet(@Param("cityCode") String cityCode);
}
