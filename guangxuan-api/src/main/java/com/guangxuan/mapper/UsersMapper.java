package com.guangxuan.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guangxuan.dto.domain.UserDTO;
import com.guangxuan.model.Users;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhuolin
 * @since 2019-11-27
 */
public interface UsersMapper extends BaseMapper<Users> {
    /**
     * 后台用户列表
     * @param objectPage
     * @param phone
     * @param level
     * @return
     */
    IPage<UserDTO> getPage(Page<Object> objectPage, @Param("phone") String phone,@Param("level") Integer level);
}
