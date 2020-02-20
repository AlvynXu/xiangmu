package com.guangxuan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guangxuan.constant.GoodStatusConstant;
import com.guangxuan.constant.PayConstant;
import com.guangxuan.dto.BuyResultDTO;
import com.guangxuan.dto.PageInfo;
import com.guangxuan.dto.SoldCountDTO;
import com.guangxuan.dto.UserStreetDTO;
import com.guangxuan.dto.domain.StreetDTO;
import com.guangxuan.enumration.BusinessFailEnum;
import com.guangxuan.exception.BusinessException;
import com.guangxuan.mapper.StreetMapper;
import com.guangxuan.model.Area;
import com.guangxuan.model.Booth;
import com.guangxuan.model.MallConfig;
import com.guangxuan.model.Street;
import com.guangxuan.service.*;
import jodd.util.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.cache.annotation.CacheRemoveAll;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.guangxuan.constant.RedisConstant.FAKE_STREET_SOLD_COUNT;

/**
 * <p>
 * 街道 服务实现类
 * </p>
 *
 * @author zhuolin
 * @since 2019-11-27
 */
@Service
public class StreetServiceImpl extends ServiceImpl<StreetMapper, Street> implements StreetService {

    @Resource
    private MallConfigService mallConfigService;

    @Resource
    private StreetPartnerOrderService streetPartnerOrderService;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private AreaService areaService;

    @Resource
    private BoothService boothService;

    @Override
   // @Cacheable(cacheNames = "GUANGXUAN:STREET:COUNT")
    public Object getCountMessage() {
        Integer count = this.count();
        Integer soldCount = this.count(
                new LambdaQueryWrapper<Street>().eq(Street::getStatus, 1));
        if (redisTemplate.hasKey(FAKE_STREET_SOLD_COUNT)) {
            soldCount += (Integer) redisTemplate.opsForValue().get(FAKE_STREET_SOLD_COUNT);
        }
        SoldCountDTO soldCountDTO = SoldCountDTO.builder().soldCount(count < soldCount ? count : soldCount).totalCount(count).build();
        return soldCountDTO;
    }

    @Override
   // @Cacheable(cacheNames = "GUANGXUAN:STREET:PAGE", key = "areaType+'_'+areaCode+'_'+type+'_'+page+'_'+size")
    public Object getStreetPage(Integer page, Integer size, Integer type, String areaCode, Integer areaType) {
        if (page == null) {
            throw new BusinessException(BusinessFailEnum.PAGE_NOT_NULL);
        }
        if (size == null) {
            throw new BusinessException(BusinessFailEnum.SIZE_NOT_NULL);
        }
        if (areaType == null) {
            throw new BusinessException(BusinessFailEnum.TYPE_NOT_NULL);
        }
        if (StringUtil.isBlank(areaCode)) {
            throw new BusinessException(BusinessFailEnum.AREA_CODE_NOT_NULL);
        }
        if (type == null) {
            throw new BusinessException(BusinessFailEnum.TYPE_NOT_NULL);
        }
        IPage<Street> pages;
        if (areaType == 3) {
            pages = this.getBaseMapper().getCityPage(new Page<Booth>(page, size), areaCode, type);
        } else if (areaType == 2) {
            pages = this.getBaseMapper().getDistrictPage(new Page<Booth>(page, size), areaCode, type);
        } else if (areaType == 1) {
            pages = this.getBaseMapper().getStreetPage(new Page<Booth>(page, size), areaCode, type);
        } else {
            pages = new Page<>();
        }
        MallConfig mallConfig = mallConfigService.getById(1L);
        for (Street street : pages.getRecords()) {
            street.setPrice(mallConfig.getThreshold3());
        }
        return pages;
    }

    @Override
   // @Cacheable(cacheNames = "GUANGXUAN:AREA:CITYCODE", key = "#p0")
    public Object getCityCode(String cityName) {
        Area area = areaService.getOne(new LambdaQueryWrapper<Area>().eq(Area::getName, cityName).eq(Area::getLevel, 2));
        if (area == null) {
            throw new BusinessException(BusinessFailEnum.NOT_GET_DATA);
        }
        return area;
    }

    @Override
   // @Cacheable(cacheNames = "GUANGXUAN:STREET:DETAIL", key = "#p0+'_'+#p1")
    public Object getDetail(Long id, Integer type) {
        BuyResultDTO resultDTO = new BuyResultDTO();
        // 展位
        if (type == 1) {
            Booth booth = boothService.getById(id);
            Street street = this.getById(booth.getStreetId());
            if (street == null) {
                throw new BusinessException(BusinessFailEnum.NOT_GET_DATA);
            }
            Area district = areaService.getOne(new LambdaQueryWrapper<Area>().eq(Area::getCode, street.getAreaCode()));
            Area city = areaService.getOne(new LambdaQueryWrapper<Area>().eq(Area::getCode, district.getParentCode()));
            Area province = areaService.getOne(new LambdaQueryWrapper<Area>().eq(Area::getCode, city.getParentCode()));
            resultDTO.setProvince(province.getName());
            resultDTO.setCity(city.getName());
            resultDTO.setDistrict(district.getName());
            resultDTO.setStreet(street.getName());
            resultDTO.setCode(booth.getBoothCode());
            resultDTO.setTime("一年");
            resultDTO.setType(2);

        }
        // 街道
        else if (type == 2) {
            Street street = this.getById(id);
            if (street == null) {
                throw new BusinessException(BusinessFailEnum.NOT_GET_DATA);
            }
            Area district = areaService.getOne(new LambdaQueryWrapper<Area>().eq(Area::getCode, street.getAreaCode()));
            Area city = areaService.getOne(new LambdaQueryWrapper<Area>().eq(Area::getCode, district.getParentCode()));
            Area province = areaService.getOne(new LambdaQueryWrapper<Area>().eq(Area::getCode, city.getParentCode()));
            resultDTO.setProvince(province.getName());
            resultDTO.setCity(city.getName());
            resultDTO.setDistrict(district.getName());
            resultDTO.setStreet(street.getName());
            resultDTO.setCode(street.getCode());
            resultDTO.setTime("永久");
            resultDTO.setType(2);
        }
        return resultDTO;
    }

    @Override
   // @Cacheable(cacheNames = "GUANGXUAN:STREET:MYSTREET", key = "userId+'_'+page+'_'+size+'_'+status")
    public Object getCurrentUserStreet(Long userId, Integer page, Integer size, Integer status) {
        return this.baseMapper.getPage(new Page<>(page, size), userId, status);
    }

    @Override
    public IPage<StreetDTO> adminPageList(Integer page, Integer size, String name, String areaCode) {
        IPage<StreetDTO> page1 = this.getBaseMapper().adminPageList(new Page(page, size), name, areaCode);
        for (StreetDTO streetDTO : page1.getRecords()) {
            int count = boothService.count(new LambdaQueryWrapper<Booth>().eq(Booth::getStatus, GoodStatusConstant.NOT_SOLD)
                    .eq(Booth::getSaved, true).eq(Booth::getStreetId, streetDTO.getId()));
            streetDTO.setCount(count);
        }
        return page1;
    }

    @Override
    public Object saveStreet(Street streetDO) {
        this.save(streetDO);
        return null;
    }

    @Override
    public Object updateInfo(Street streetDO) {
        this.updateById(streetDO);
        return null;
    }

    @Override
    public List<UserStreetDTO> getMarketStreet(Long orderId) {
        return this.getBaseMapper().getMarketStreet(orderId) ;
    }

    @Override
    public List<Street> getCityStreet(String cityCode) {
        return this.getBaseMapper().getCityStreet(cityCode) ;
    }
}
