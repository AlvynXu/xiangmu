package com.guangxuan.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guangxuan.dto.LeaveMessageDTO;
import com.guangxuan.model.ItemLeaveMessage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 项目留言 服务类
 * </p>
 *
 * @author zhuolin
 * @since 2020-01-13
 */
public interface ItemLeaveMessageService extends IService<ItemLeaveMessage> {
    /**
     * 获取项目留言
     * @param page
     * @param size
     * @param itemId
     * @return
     */
    IPage<LeaveMessageDTO> getLeaveMessagePage(Integer page, Integer size, Long itemId);
}
