package com.guangxuan.service;

import com.guangxuan.model.Feedback;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 意见反馈 服务类
 * </p>
 *
 * @author zhuolin
 * @since 2019-12-03
 */
public interface FeedbackService extends IService<Feedback> {
    /**
     * 保存意见反馈数据
     * @param feedback
     * @param userId
     * @return
     */
    Object saveFeedBack(Feedback feedback, Long userId);
}
