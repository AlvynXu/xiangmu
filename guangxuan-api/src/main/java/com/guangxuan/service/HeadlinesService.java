package com.guangxuan.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guangxuan.dto.domain.HeadlineDTO;
import com.guangxuan.model.HeadlineItem;
import com.guangxuan.model.Headlines;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhuolin
 * @since 2019-12-15
 */
public interface HeadlinesService extends IService<Headlines> {
    /**
     * 分页获取头条
     * @param areaType
     * @param areaCode
     * @param page
     * @param size
     * @return
     */
    Object pageList(Integer areaType, String areaCode, Integer page, Integer size);

    /**
     * 获取头条
     * @param areaCode
     * @param page
     * @param size
     * @return
     */
    IPage<HeadlineDTO> getAdminPage(String areaCode, Integer page, Integer size);

    List<HeadlineItem> getAdminHeadlinesItem(Integer sort, List<String> areaCodes,Integer status);
}
