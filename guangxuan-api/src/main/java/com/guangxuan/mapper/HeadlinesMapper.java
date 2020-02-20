package com.guangxuan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guangxuan.dto.domain.HeadlineDTO;
import com.guangxuan.model.HeadlineItem;
import com.guangxuan.model.Headlines;
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
public interface HeadlinesMapper extends BaseMapper<Headlines> {
    /**
     * 获取后台管理的展位信息
     *
     * @param objectPage
     * @param areaCode
     * @return
     */
    IPage<HeadlineDTO> getAdminPage(Page objectPage, @Param("areaCode") String areaCode);

    /**
     * 后台获取头条
     *
     * @param sort
     * @param areaCodes
     */
    List<HeadlineItem> getAdminList(@Param("sort") Integer sort, @Param("areaCodes") List<String> areaCodes, @Param("status") Integer status);
}
