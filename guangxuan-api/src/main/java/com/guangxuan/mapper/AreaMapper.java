package com.guangxuan.mapper;

import com.guangxuan.dto.ProvinceStreetCountDTO;
import com.guangxuan.model.Area;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 地区 Mapper 接口
 * </p>
 *
 * @author zhuolin
 * @since 2019-11-27
 */
public interface AreaMapper extends BaseMapper<Area> {
    /**
     * 获取省份名称
     * @return
     */
    List<ProvinceStreetCountDTO> getProvinceStreetCount();


}
