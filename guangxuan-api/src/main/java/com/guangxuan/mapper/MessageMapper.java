package com.guangxuan.mapper;

import com.guangxuan.model.Message;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhuolin
 * @since 2019-11-27
 */
public interface MessageMapper extends BaseMapper<Message> {
    /**
     * 删除消息
     * @param maxDate
     */
    void updateMessageDelete(@Param("minDate") Date minDate,@Param("maxDate") Date maxDate);
}
