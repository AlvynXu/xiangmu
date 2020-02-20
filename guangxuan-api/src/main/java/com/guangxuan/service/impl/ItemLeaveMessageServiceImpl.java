package com.guangxuan.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guangxuan.dto.LeaveMessageDTO;
import com.guangxuan.model.ItemLeaveMessage;
import com.guangxuan.mapper.ItemLeaveMessageMapper;
import com.guangxuan.service.ItemLeaveMessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 项目留言 服务实现类
 * </p>
 *
 * @author zhuolin
 * @since 2020-01-13
 */
@Service
public class ItemLeaveMessageServiceImpl extends ServiceImpl<ItemLeaveMessageMapper, ItemLeaveMessage> implements ItemLeaveMessageService {

    @Override
    public IPage<LeaveMessageDTO> getLeaveMessagePage(Integer page, Integer size, Long itemId) {
        return this.getBaseMapper().getLeaveMessagePage(new Page(page, size), itemId);
    }
}
