package com.guangxuan.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guangxuan.model.MallBanner;
import com.guangxuan.model.MallItemInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author zhuolin
 * @since 2019-12-15
 */
public interface MallItemInfoMapper extends BaseMapper<MallItemInfo> {
    /**
     * 获取省份下的商品
     * @param boothPage
     * @param areaCode
     * @param categoryId
     * @param status
     * @return
     */
    IPage<MallItemInfo> getProvincePage(Page<MallItemInfo> boothPage, @Param("areaCode") String areaCode,
                                        @Param("categoryId") Integer categoryId, @Param("status") Integer status);
    /**
     * 获取市下商品
     *
     * @param boothPage
     * @param areaCode
     * @param categoryId
     * @return
     */
    IPage<MallItemInfo> getCityPage(Page<MallItemInfo> boothPage, @Param("areaCode") String areaCode,
                                    @Param("categoryId") Integer categoryId, @Param("status") Integer status);

    /**
     * 获取区县下商品
     *
     * @param boothPage
     * @param areaCode
     * @param categoryId
     * @return
     */
    IPage<MallItemInfo> getDistrictPage(Page<MallItemInfo> boothPage, @Param("areaCode") String areaCode,
                                        @Param("categoryId") Integer categoryId, @Param("status") Integer status);

    /**
     * 获取街道下商品
     *
     * @param boothPage
     * @param areaCode
     * @param categoryId
     * @return
     */
    IPage<MallItemInfo> getStreetPage(Page<MallItemInfo> boothPage, @Param("areaCode") String areaCode,
                                      @Param("categoryId") Integer categoryId, @Param("status") Integer status);

    /**
     * 获取全国头条分页
     *
     * @param mallItemInfoPage
     * @param areaCode
     * @return
     */
    IPage<MallItemInfo> getHeadlinesPage(Page<MallItemInfo> mallItemInfoPage, @Param("areaCode") String areaCode);

    /**
     * 收藏列表
     *
     * @param objectPage
     * @param id
     * @return
     */
    IPage<MallItemInfo> getCollectionPageList(Page objectPage, @Param("userId") Long id);

}
