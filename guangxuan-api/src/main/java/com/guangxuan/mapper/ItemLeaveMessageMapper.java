package com.guangxuan.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guangxuan.dto.LeaveMessageDTO;
import com.guangxuan.model.ItemLeaveMessage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 项目留言 Mapper 接口
 * </p>
 *
 * @author zhuolin
 * @since 2020-01-13
 */
public interface ItemLeaveMessageMapper extends BaseMapper<ItemLeaveMessage> {
    /**
     * 获取留言
     * @param page
     * @param itemId
     * @return
     */
    IPage<LeaveMessageDTO> getLeaveMessagePage(Page page,@Param("itemId") Long itemId);
}
