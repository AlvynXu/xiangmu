package com.guangxuan.mapper;

import com.guangxuan.model.BoothUsers;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhuolin
 * @since 2019-11-27
 */
public interface BoothUsersMapper extends BaseMapper<BoothUsers> {
    /**
     * 设置过期
     * @param boothIds
     */
    void updateExpire(@Param("boothIds") Set<Long> boothIds);
}
