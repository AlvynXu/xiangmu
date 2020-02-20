package com.guangxuan.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guangxuan.dto.DownloadPathDTO;
import com.guangxuan.dto.LoginDTO;
import com.guangxuan.dto.LoginUser;
import com.guangxuan.dto.domain.UserDTO;
import com.guangxuan.model.Users;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.management.Query;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author zhuolin
 * @since 2019-11-20
 */
public interface UsersService extends IService<Users> {
    /**
     * 注册
     *
     * @param phone
     * @param password
     * @param userId
     * @return
     */
    Object setPassword(String phone, String password, Long userId);

    /**
     * 验证2
     *
     * @param phone
     * @param randomCode
     * @param promoterId
     * @return
     */
    Object validRandomCode(String phone, String randomCode, Long promoterId);

    /**
     * 发送验证码
     *
     * @param phone
     * @return
     */
    Object sendRandomCode(String phone);

    /**
     * 登陆
     *
     * @param user
     * @return
     */
    LoginUser login(LoginDTO user);

    /**
     * 获取手机验证码 登陆
     *
     * @param phone
     * @return
     */
    Object getLoginRandomCode(String phone);

    /**
     * 手机验证码登陆
     *
     * @param user
     * @return
     */
    LoginUser phoneLogin(LoginDTO user);

    /**
     * 获取团队人数信息
     *
     * @param id
     * @return
     */
    Object getTeam(Long id);

    /**
     * 设置用户邀请人
     *
     * @param promoterId
     * @param id
     * @return
     */
    Object setPromoter(String promoterId, Long id);

    /**
     * 获取用户收益
     *
     * @param id
     * @return
     */
    Object getMessage(Long id);

    /**
     * 获取收益
     *
     * @param id
     * @param page
     * @param size
     * @return
     */
    Object getProfit(Long id, Integer page, Integer size);

    /**
     * 分页获取团队
     *
     * @param id
     * @param page
     * @param size
     * @return
     */
    Object getTeamPage(Long id, Integer page, Integer size);

    /**
     * 获取收支记录
     *
     * @param userId
     * @param page
     * @param size
     * @return
     */
    Object getPayLog(Long userId, Integer page, Integer size);

    /**
     * 获取用户购买展位及街道数量
     * @param id
     * @return
     */
    Object getMyBaseInfo(Long id);

    /**
     * 获取用户信息
     * @param page
     * @param size
     * @param phone
     * @param level
     * @return
     */
    IPage<UserDTO> getPage(Integer page, Integer size, String phone, Integer level);

    /**
     * 获取下载地址
     * @return
     */
    DownloadPathDTO getDownloadPath();
}
