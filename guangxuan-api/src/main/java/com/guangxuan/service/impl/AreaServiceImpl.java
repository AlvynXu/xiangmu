package com.guangxuan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.guangxuan.dto.ProvinceStreetCountDTO;
import com.guangxuan.enumration.AreaLevel;
import com.guangxuan.model.Area;
import com.guangxuan.mapper.AreaMapper;
import com.guangxuan.model.Street;
import com.guangxuan.service.AreaService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guangxuan.service.StreetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 地区 服务实现类
 * </p>
 *
 * @author zhuolin
 * @since 2019-11-27
 */
@Service
public class AreaServiceImpl extends ServiceImpl<AreaMapper, Area> implements AreaService {

    @Resource
    private StreetService streetService;

    @Override
    public List<Area> getAllProvinces() {
        return this.list(new LambdaQueryWrapper<Area>().eq(Area::getLevel, AreaLevel.PROVINCE.getId()));
    }

    @Override
    public List<Area> getCities(String code) {
        return this.list(new LambdaQueryWrapper<Area>().eq(Area::getLevel, AreaLevel.CITY.getId()).eq(Area::getParentCode, code));

    }

    @Override
    public List<Area> getDistricts(String code) {
        return this.list(new LambdaQueryWrapper<Area>().eq(Area::getLevel, AreaLevel.DISTRICT.getId()).eq(Area::getParentCode, code));

    }

    @Override
    public List<Street> getStreet(String code) {
        return streetService.list(new LambdaQueryWrapper<Street>().eq(Street::getAreaCode, code));
    }

    @Override
    public List<Area> getAllCities() {
        return this.list(new LambdaQueryWrapper<Area>().eq(Area::getLevel, AreaLevel.CITY.getId()));
    }

    @Override
    public List<ProvinceStreetCountDTO> getProviceInfos() {
        List<ProvinceStreetCountDTO> countDTOS = this.getBaseMapper().getProvinceStreetCount();
        return countDTOS;
    }



}
