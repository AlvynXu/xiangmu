package com.guangxuan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.guangxuan.constant.PayConstant;
import com.guangxuan.constant.PayResourceConstant;
import com.guangxuan.constant.RedisConstant;
import com.guangxuan.enumration.BusinessFailEnum;
import com.guangxuan.exception.BusinessException;
import com.guangxuan.model.PayLog;
import com.guangxuan.mapper.PayLogMapper;
import com.guangxuan.service.PayLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 支付日志 服务实现类
 * </p>
 *
 * @author zhuolin
 * @since 2019-11-27
 */
@Service
public class PayLogServiceImpl extends ServiceImpl<PayLogMapper, PayLog> implements PayLogService {


    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public Object cancelPay(Long userId, Integer type, Long id) {
        List<PayLog> payLogs = this.list(new LambdaQueryWrapper<PayLog>()
                .eq(PayLog::getUserId, userId)
                .eq(PayLog::getStatus, PayConstant.WAIT_PAY)
                .eq(PayLog::getGoodsId, id)
                .eq(PayLog::getSource, type));
        if (payLogs.size()>0) {
            payLogs.forEach(a->a.setStatus(PayConstant.CANCEL_PAY));
            this.updateBatchById(payLogs);
        }
        if (type == 0) {
            if (redisTemplate.hasKey(RedisConstant.BUY_VIP + userId)) {
                redisTemplate.delete(RedisConstant.BUY_VIP + userId);
            }
        }
        if (type == 1) {
            if (redisTemplate.hasKey(RedisConstant.BUY_BOOTH + id)) {
                redisTemplate.delete(RedisConstant.BUY_BOOTH + id);
            }
        }
        if (type == 2) {
            if (redisTemplate.hasKey(RedisConstant.BUY_STREET + id)) {
                redisTemplate.delete(RedisConstant.BUY_STREET + id);
            }
        }
        return null;
    }
}
