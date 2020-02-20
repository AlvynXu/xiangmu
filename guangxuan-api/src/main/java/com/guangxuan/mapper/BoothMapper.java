package com.guangxuan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guangxuan.dto.BoothDetailDTO;
import com.guangxuan.dto.ItemCanUseBoothDTO;
import com.guangxuan.dto.SoldInfoDTO;
import com.guangxuan.dto.domain.BoothDTO;
import com.guangxuan.model.Booth;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author zhuolin
 * @since 2019-11-27
 */
public interface BoothMapper extends BaseMapper<Booth> {
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
     * 设置过期
     *
     * @param boothIds
     */
    void expire(@Param("list") Set<Long> boothIds);

    /**
     * 获取市下面的展位
     *
     * @param boothPage
     * @param areaCode
     * @param type
     * @param isSaved
     * @return
     */
    IPage<Booth> getCityPage(Page<Booth> boothPage, @Param("areaCode") String areaCode, @Param("type") Integer type, @Param("isSaved") Boolean isSaved);

    /**
     * 获取区县下面的展位
     *
     * @param boothPage
     * @param areaCode
     * @param type
     * @param isSaved
     * @return
     */
    IPage<Booth> getDistrictPage(Page<Booth> boothPage, @Param("areaCode") String areaCode, @Param("type") Integer type, @Param("isSaved") Boolean isSaved);

    /**
     * 获取区县下面的展位
     *
     * @param boothPage
     * @param areaCode
     * @param type
     * @param isSaved
     * @return
     */
    IPage<Booth> getStreetPage(Page<Booth> boothPage, @Param("areaCode") String areaCode, @Param("type") Integer type, @Param("isSaved") Boolean isSaved);

    /**
     * 获取展位
     *
     * @param objectPage
     * @param streetCode
     * @param boothCode
     * @return
     */
    IPage<BoothDTO> getAdminBoothPage(Page objectPage, @Param("streetCode") String streetCode, @Param("boothCode") String boothCode);

    /**
     * 获取项目可以使用的展位
     *
     * @param objectPage
     * @param boothIds
     * @param status
     * @param saveStreetIds
     * @param showSameStreetBooth
     * @return
     */
    IPage<ItemCanUseBoothDTO> getItemCanUsePage(Page<Object> objectPage, @Param("boothIds") List<Long> boothIds, @Param("status") List<Integer> status,
                                                @Param("saveStreetIds") List<Long> saveStreetIds, @Param("showSameStreetBooth")  Boolean showSameStreetBooth);

    /**
     * @param userId
     * @param streetCodes
     * @return
     */
    List<SoldInfoDTO> getCanRentBooth(@Param("userId") Long userId, @Param("streetCodes") Set<String> streetCodes);

    /**
     * @param boothIds
     * @return
     */
    List<BoothDetailDTO> getBoothDetail(@Param("boothIds") Set<Long> boothIds);
}
