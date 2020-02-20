package com.guangxuan.service;

import com.guangxuan.model.PayLog;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 支付日志 服务类
 * </p>
 *
 * @author zhuolin
 * @since 2019-11-27
 */
public interface PayLogService extends IService<PayLog> {
    /**
     * 取消支付
     *
     * @param userId
     * @param type
     * @param id
     * @return
     */
    Object cancelPay(Long userId, Integer type, Long id);

}
