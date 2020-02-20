package com.guangxuan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.guangxuan.constant.MessageStatus;
import com.guangxuan.dto.SoldCountDTO;
import com.guangxuan.enumration.BusinessFailEnum;
import com.guangxuan.exception.BusinessException;
import com.guangxuan.model.Message;
import com.guangxuan.mapper.MessageMapper;
import com.guangxuan.service.MessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zhuolin
 * @since 2019-11-27
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    @Override
    public Integer getUnreadMessageCount(Long id) {
        Integer count = this.count(new LambdaQueryWrapper<Message>()
                .eq(Message::getStatus, MessageStatus.UNREAD)
                .eq(Message::getToUserId, id)
                .eq(Message::getDeleted, false));
        return count;
    }

    @Override
    public Object getMessageInfo(Long id, Long userId) {
        Message message = this.getById(id);
        if (message == null) {
            throw new BusinessException(BusinessFailEnum.NOT_GET_CURRENT_MESSAGE);
        }
        message.setStatus(MessageStatus.READED);
        this.updateById(message);
        return message;
    }

    @Override
    public Message createMessage(Message message) {
        this.save(message);
        return message;
    }

    @Override
    public void deleteMessage(Date minDate, Date maxDate) {
        this.getBaseMapper().updateMessageDelete(minDate, maxDate);
    }
}
