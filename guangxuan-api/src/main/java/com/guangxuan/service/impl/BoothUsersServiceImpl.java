package com.guangxuan.service.impl;

import com.guangxuan.model.BoothUsers;
import com.guangxuan.mapper.BoothUsersMapper;
import com.guangxuan.service.BoothUsersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zhuolin
 * @since 2019-11-27
 */
@Service
public class BoothUsersServiceImpl extends ServiceImpl<BoothUsersMapper, BoothUsers> implements BoothUsersService {

    @Override
    public void expire(Set<Long> boothIds) {
        this.getBaseMapper().updateExpire(boothIds);
    }
}
