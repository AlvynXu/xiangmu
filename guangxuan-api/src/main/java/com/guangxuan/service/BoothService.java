package com.guangxuan.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guangxuan.dto.BoothDetailDTO;
import com.guangxuan.dto.SoldInfoDTO;
import com.guangxuan.dto.domain.BoothDTO;
import com.guangxuan.model.Booth;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhuolin
 * @since 2019-11-27
 */
public interface BoothService extends IService<Booth> {
    /**
     * 获取展位购买数量信息
     * @return
     */
    Object getBootCountMessage();

    /**
     * 分页获取展位信息
     * @param page
     * @param size
     * @param type
     * @param areaCode
     * @param areaType
     * @param isSaved
     * @return
     */
    IPage<Booth> getBoothPage(Integer page, Integer size, Integer type, String areaCode, Integer areaType, Boolean isSaved);

    /**
     * 获取当前用户购买的展位泪飙
     * @param userId
     * @param page
     * @param size
     * @param status
     * @return
     */
    IPage<SoldInfoDTO> getCurrentUserBooth(Long userId, Integer page, Integer size, Integer status);

    /**
     * 设置过期
     * @param boothIds
     */
    void expire(Set<Long> boothIds);

    /**
     * 选择展位
     * @param id
     * @param boothId
     * @return
     */
    Object choose(Long id, Long boothId);

    /**
     * 获取展位管理
     * @param page
     * @param size
     * @param streetCode
     * @param boothCode
     * @return
     */
    IPage<BoothDTO> getAdminBoothPage(Integer page, Integer size, String streetCode, String boothCode);

    /**
     * 获取可出租展位
     * @param userId
     * @param streetCodes
     * @return
     */
    List<SoldInfoDTO> getCanRentBooth(Long userId, Set<String> streetCodes);

    /**
     * 获取booth详情
     * @param boothIds
     * @return
     */
    List<BoothDetailDTO> getBoothDetail(Set<Long> boothIds);
}
