package com.guangxuan.service;

import com.guangxuan.dto.ProvinceStreetCountDTO;
import com.guangxuan.model.Area;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guangxuan.model.Street;

import java.util.List;

/**
 * <p>
 * 地区 服务类
 * </p>
 *
 * @author zhuolin
 * @since 2019-11-27
 */
public interface AreaService extends IService<Area> {
    /**
     * 获取省信息
     * @return
     */
    List<Area> getAllProvinces();

    /**
     * 获取城市信息
     * @param code
     * @return
     */
    List<Area> getCities(String code);

    /**
     * 获取区县
     * @param code
     * @return
     */
    List<Area> getDistricts(String code);

    /**
     * 获取街道
     * @param code
     * @return
     */
    List<Street> getStreet(String code);

    /**
     * 获取全部城市
     * @return
     */
    List<Area> getAllCities();

    /**
     * 获取身份和城市数量
     * @return
     */
    List<ProvinceStreetCountDTO> getProviceInfos();


}
