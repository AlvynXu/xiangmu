package com.guangxuan.service;

import com.guangxuan.model.Message;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author zhuolin
 * @since 2019-11-27
 */
public interface MessageService extends IService<Message> {
    /**
     * 获取未读消息
     *
     * @param id
     * @return
     */
    Integer getUnreadMessageCount(Long id);

    /**
     * 获取用户消息
     *
     * @param id
     * @param userId
     * @return
     */
    Object getMessageInfo(Long id, Long userId);

    /**
     * 新建消息
     * @return
     */
    Message createMessage(Message message);

    /**
     * 设置消息位已删除
     * @param maxDate
     */
    void deleteMessage(Date minDate,Date maxDate);
}
