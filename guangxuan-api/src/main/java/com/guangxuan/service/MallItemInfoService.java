package com.guangxuan.service;

import com.guangxuan.dto.MallItemInfoDTO;
import com.guangxuan.dto.MallItemInfoDetailDTO;
import com.guangxuan.model.ItemLeaveMessage;
import com.guangxuan.model.MallItemInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author zhuolin
 * @since 2019-12-15
 */
public interface MallItemInfoService extends IService<MallItemInfo> {
    /**
     * 保存数据
     *
     * @param itemInfo
     * @param multipartFile
     * @param userId
     * @return
     */
    Object saveInfo(MallItemInfoDTO itemInfo, MultipartFile multipartFile, Long userId) throws IOException;

    /**
     * 举报
     *
     * @param itemId
     * @return
     */
    Object report(Long itemId);

    /**
     * 分页获取商品信息
     *
     * @param areaType
     * @param areaCode
     * @param categoryId
     * @param page
     * @param size
     * @return
     */
    Object pageList(Integer areaType, String areaCode, Integer categoryId, Integer page, Integer size);

    /**
     * 获取商品详细信息
     *
     * @param userId
     * @param itemId
     * @return
     */
    Object getDetail(Long itemId, Long userId);
    /**
     * 获取商品详细信息
     *
     * @param regCode
     * @param itemId
     * @return
     */
    MallItemInfo getShareDetail(Long itemId, String regCode);

    /**
     * 我的项目
     *
     * @param userId
     * @param page
     * @param size
     * @return
     */
    Object getMyItemPageList(Long userId, Integer page, Integer size);

    /**
     * 保存详细信息
     *
     * @param itemInfo
     * @param multipartFile
     * @param id
     * @return
     */
    Object saveInfoDetail(MallItemInfoDetailDTO itemInfo, MultipartFile multipartFile, Long id) throws IOException;

    /**
     * 获取分享链接
     *
     * @param id
     * @param itemId
     * @return
     */
    Object getShareUrl(Long id, Long itemId);

    /**
     * 获取收藏列表
     *
     * @param page
     * @param size
     * @param id
     * @return
     */
    Object getCollectionPageList(Integer page, Integer size, Long id);

    /**
     * 删除项目
     * @param itemId
     * @param userId
     * @return
     */
    Object removeItem(Long itemId, Long userId);

    /**
     * 保存商品留言
     * @param leaveMessage
     * @param userId
     * @return
     */
    Object saveLeaveMessage(ItemLeaveMessage leaveMessage, Long userId);

    /**
     * 分页加载留言
     * @param itemId
     * @param page
     * @param size
     * @return
     */
    Object getLeaveMessagePage(Long itemId, Integer page, Integer size);
//
//    /**
//     * 提交审核
//     * @param userId
//     * @param itemId
//     * @return
//     */
//    Object confirmAudit(Long userId, Long itemId);
}
