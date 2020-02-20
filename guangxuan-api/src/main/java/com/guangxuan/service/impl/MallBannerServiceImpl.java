package com.guangxuan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.beust.jcommander.internal.Lists;
import com.guangxuan.model.BannerArea;
import com.guangxuan.model.MallBanner;
import com.guangxuan.mapper.MallBannerMapper;
import com.guangxuan.service.BannerAreaService;
import com.guangxuan.service.MallBannerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 商城banner配置 服务实现类
 * </p>
 *
 * @author zhuolin
 * @since 2019-11-27
 */
@Service
public class MallBannerServiceImpl extends ServiceImpl<MallBannerMapper, MallBanner> implements MallBannerService {

    @Resource
    private BannerAreaService bannerAreaService;

    @Override
//   // @Cacheable(cacheNames = "GUANGXUAN:BANNER:LIST", key = "areaCode+'_'+categoryId")
    public Object getBanner(String areaCode, Long categoryId) {

        return this.getBaseMapper().getBannerList(Lists.newArrayList(areaCode), categoryId);
    }

    @Override
    public IPage<MallBanner> getAdminBanner(Integer page, Integer size, String areaCode) {
        // 首页banner
        List<String> areaCodes = new ArrayList<>();
        areaCodes.add(areaCode);
        IPage<MallBanner> pages = this.getBaseMapper().getAdminBannerPageList(new Page(page, size), areaCodes);
        for (MallBanner mallBanner : pages.getRecords()) {
            List<BannerArea> bannerAreas = bannerAreaService.list(new LambdaQueryWrapper<BannerArea>().eq(BannerArea::getBannerId, mallBanner.getId()));
            mallBanner.setAreaCodes(bannerAreas.stream().map(BannerArea::getAreaCode).collect(Collectors.toList()));
        }
        return pages;
    }

    @Override
    public MallBanner listInfo(List<String> areaCodes, String itemId) {
        return this.getBaseMapper().listInfo(areaCodes, itemId);
    }


}
