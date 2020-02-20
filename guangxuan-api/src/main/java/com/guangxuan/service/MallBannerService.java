package com.guangxuan.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.guangxuan.model.MallBanner;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 商城banner配置 服务类
 * </p>
 *
 * @author zhuolin
 * @since 2019-11-27
 */
public interface MallBannerService extends IService<MallBanner> {
    /**
     * 获取区域banner
     *
     * @param areaCode
     * @param categoryId
     * @return
     */
    Object getBanner(String areaCode, Long categoryId);

    /**
     * 获取区域banner
     *
     * @param areaCode
     * @return
     */
    IPage<MallBanner> getAdminBanner(Integer page, Integer size, String areaCode);

    /**
     * 获取banner
     * @param areaCodes
     * @param itemId
     * @return
     */
    MallBanner listInfo(List<String> areaCodes, String itemId);
}
