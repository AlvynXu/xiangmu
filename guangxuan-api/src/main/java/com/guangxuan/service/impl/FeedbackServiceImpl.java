package com.guangxuan.service.impl;

import com.guangxuan.model.Feedback;
import com.guangxuan.mapper.FeedbackMapper;
import com.guangxuan.service.FeedbackService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 意见反馈 服务实现类
 * </p>
 *
 * @author zhuolin
 * @since 2019-12-03
 */
@Service
public class FeedbackServiceImpl extends ServiceImpl<FeedbackMapper, Feedback> implements FeedbackService {

    @Override
    public Object saveFeedBack(Feedback feedback, Long userId) {
        feedback.setStatus(false);
        feedback.setTime(new Date());
        feedback.setUserId(userId);
        this.save(feedback);
        return feedback;
    }
}
