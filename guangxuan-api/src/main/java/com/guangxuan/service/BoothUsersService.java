package com.guangxuan.service;

import com.guangxuan.model.BoothUsers;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Set;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhuolin
 * @since 2019-11-27
 */
public interface BoothUsersService extends IService<BoothUsers> {
    /**
     * 设置购买过期
     * @param boothIds
     */
    void expire(Set<Long> boothIds);
}
