package com.guangxuan.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guangxuan.model.MallBanner;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 商城banner配置 Mapper 接口
 * </p>
 *
 * @author zhuolin
 * @since 2019-11-27
 */
public interface MallBannerMapper extends BaseMapper<MallBanner> {
    /**
     * 获取banner
     *
     * @param areaCodes
     * @param categoryId
     * @return
     */
    List<MallBanner> getBannerList(@Param("areaCodes") List<String> areaCodes, @Param("categoryId") Long categoryId);

    /**
     * 获取banner
     *
     * @param page
     * @return
     */
    IPage<MallBanner> getAdminBannerPageList(Page page, @Param("areaCodes") List<String> areaCodes);

    /**
     * 获取banner
     *
     * @param areaCodes
     * @param itemId
     * @return
     */
    MallBanner listInfo(@Param("areaCodes") List<String> areaCodes, @Param("itemId") String itemId);
}
