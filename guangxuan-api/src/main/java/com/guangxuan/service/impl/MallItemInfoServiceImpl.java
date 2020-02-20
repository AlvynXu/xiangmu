package com.guangxuan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guangxuan.config.SystemProperties;
import com.guangxuan.constant.ItemStatus;
import com.guangxuan.constant.ReportStatus;
import com.guangxuan.dto.LeaveMessageDTO;
import com.guangxuan.dto.MallItemInfoDTO;
import com.guangxuan.dto.MallItemInfoDetailDTO;
import com.guangxuan.enumration.BusinessFailEnum;
import com.guangxuan.exception.BusinessException;
import com.guangxuan.mapper.MallItemInfoMapper;
import com.guangxuan.model.*;
import com.guangxuan.service.*;
import jodd.util.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zhuolin
 * @since 2019-12-15
 */
@Service
public class MallItemInfoServiceImpl extends ServiceImpl<MallItemInfoMapper, MallItemInfo> implements MallItemInfoService {

    @Resource
    private MallItemInfoDetailService mallItemInfoDetailService;

    @Resource
    private ItemLeaveMessageService itemLeaveMessageService;

    @Resource
    private FilesService filesService;

    @Resource
    private SystemProperties systemProperties;

    @Resource
    private UsersService usersService;

    @Resource
    private ItemCollectionService itemCollectionService;

    @Resource
    private MallBannerService mallBannerService;

    @Resource
    private HeadlineItemService headlineItemService;

    @Resource
    private AppVersionService appVersionService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object saveInfo(MallItemInfoDTO itemInfo, MultipartFile multipartFile, Long userId) throws IOException {
        MallItemInfo saveInfo = null;
        if (itemInfo.getId() == null && (multipartFile == null || multipartFile.getSize() == 0)) {
            throw new BusinessException(BusinessFailEnum.FILE_NOT_NULL);
        }
        Boolean isNew = false;
        if (itemInfo.getId() != null) {
            saveInfo = this.getById(itemInfo.getId());
            if (saveInfo == null) {
                throw new BusinessException(BusinessFailEnum.NOT_GET_DATA);
            }
            if (saveInfo.getStatus().equals(ItemStatus.ON_SALE) || saveInfo.getStatus().equals(ItemStatus.NOT_AUDIT)) {
                throw new BusinessException(BusinessFailEnum.CURRENT_STATUS_CANNOT_OPERATE);
            }
        } else {
            isNew = true;
            saveInfo = new MallItemInfo();
        }
        BeanUtils.copyProperties(itemInfo, saveInfo);
        if (isNew) {
            saveInfo.setCreateTime(new Date());
            saveInfo.setHasReport(false);
            saveInfo.setItemCode(UUID.randomUUID().toString());

            saveInfo.setReportStatus(ReportStatus.NO_REPORT);
            saveInfo.setUserId(userId);
            saveInfo.setShareCount(0);
        }
        if (multipartFile != null) {
            saveInfo.setBannerPath(saveFile(multipartFile));
        }
        if (systemProperties.getAuditOff()) {
            saveInfo.setStatus(ItemStatus.OFF_SHELVE);
        } else {
            saveInfo.setStatus(ItemStatus.NOT_AUDIT);
        }
        this.saveOrUpdate(saveInfo);
        mallItemInfoDetailService.remove(new LambdaQueryWrapper<MallItemInfoDetail>()
                .eq(MallItemInfoDetail::getMallItemInfoId, saveInfo.getId()));
        return saveInfo;
    }

    private String saveFile(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        InputStream is = file.getInputStream();
        if (originalFilename == null || !originalFilename.contains(".")) {
            throw new BusinessException(BusinessFailEnum.FILE_SUFFIX_IS_NULL);
        }
        String fileName = UUID.randomUUID().toString()
                + originalFilename.substring(originalFilename.lastIndexOf("."));
        return filesService.putObject(fileName, is);
    }

    @Override
    public Object report(Long itemId) {
        MallItemInfo saveInfo = this.getById(itemId);
        if (saveInfo == null) {
            throw new BusinessException(BusinessFailEnum.NOT_GET_DATA);
        }
        if (!saveInfo.getHasReport()) {
            saveInfo.setHasReport(true);
            this.updateById(saveInfo);
        }
        return saveInfo;
    }

    @Override
    // @Cacheable(cacheNames = "GUANGXUAN:ITEM:PAGE",key = "#p0+'_'+#p1+'_'+#p2+'_'+#p3+'_'+#p4")
    public Object pageList(Integer areaType, String areaCode, Integer categoryId, Integer page, Integer size) {
        if (page == null) {
            throw new BusinessException(BusinessFailEnum.PAGE_NOT_NULL);
        }
        if (size == null) {
            throw new BusinessException(BusinessFailEnum.SIZE_NOT_NULL);
        }
        if (areaType == null) {
            throw new BusinessException(BusinessFailEnum.TYPE_NOT_NULL);
        }
        if (categoryId == null) {
            throw new BusinessException(BusinessFailEnum.CATEGORY_TYPE_NOT_NULL);
        }
        if (StringUtil.isBlank(areaCode)) {
            throw new BusinessException(BusinessFailEnum.AREA_CODE_NOT_NULL);
        }
        IPage<MallItemInfo> pages;
        if (areaType == 4) {
            pages = this.getBaseMapper().getProvincePage(new Page<MallItemInfo>(page, size), areaCode, categoryId, ItemStatus.ON_SALE);
        } else if (areaType == 3) {
            pages = this.getBaseMapper().getCityPage(new Page<MallItemInfo>(page, size), areaCode, categoryId, ItemStatus.ON_SALE);
        } else if (areaType == 2) {
            pages = this.getBaseMapper().getDistrictPage(new Page<MallItemInfo>(page, size), areaCode, categoryId, ItemStatus.ON_SALE);
        } else if (areaType == 1) {
            pages = this.getBaseMapper().getStreetPage(new Page<MallItemInfo>(page, size), areaCode, categoryId, ItemStatus.ON_SALE);
        } else {
            pages = new Page<>();
        }
        return pages;
    }

    @Override
    public Object getDetail(Long itemId, Long userId) {
        MallItemInfo mallItemInfo = this.getById(itemId);
        if (mallItemInfo == null) {
            throw new BusinessException(BusinessFailEnum.NOT_GET_DATA);
        }
        mallItemInfo.setViews(mallItemInfo.getViews() == null ? 0 : mallItemInfo.getViews() + 1);
        this.updateById(mallItemInfo);
        List<MallItemInfoDetail> mallItemInfoDetails = mallItemInfoDetailService.list(
                new LambdaQueryWrapper<MallItemInfoDetail>().eq(MallItemInfoDetail::getMallItemInfoId, itemId)
                        .orderByAsc(MallItemInfoDetail::getSeq));
        mallItemInfo.setDetailList(mallItemInfoDetails);
        Integer count = itemCollectionService.count(new LambdaQueryWrapper<ItemCollection>().eq(ItemCollection::getItemId, itemId).eq(ItemCollection::getUserId, userId));
        mallItemInfo.setCollect(count > 0);
        mallItemInfo.setRegCode(usersService.getById(userId).getRegCode());
        return mallItemInfo;
    }

    @Override
    public MallItemInfo getShareDetail(Long itemId, String regCode) {
        Users users = usersService.getOne(new LambdaQueryWrapper<Users>().eq(Users::getRegCode, regCode));
        MallItemInfo mallItemInfo = this.getById(itemId);
        if (mallItemInfo == null) {
            throw new BusinessException(BusinessFailEnum.NOT_GET_DATA);
        }
        mallItemInfo.setViews(mallItemInfo.getViews() == null ? 0 : mallItemInfo.getViews() + 1);
        this.updateById(mallItemInfo);
        List<MallItemInfoDetail> mallItemInfoDetails = mallItemInfoDetailService.list(
                new LambdaQueryWrapper<MallItemInfoDetail>().eq(MallItemInfoDetail::getMallItemInfoId, itemId)
                        .orderByAsc(MallItemInfoDetail::getSeq));
        mallItemInfo.setDetailList(mallItemInfoDetails);
        mallItemInfo.setRegCode(users.getRegCode());
        String path = appVersionService.getOne(new LambdaQueryWrapper<AppVersion>()
                .eq(AppVersion::getEnable, true).eq(AppVersion::getType, 0)).getDownloadPth();
        mallItemInfo.setDownloadPath(path);
        return mallItemInfo;
    }

    @Override
    // @Cacheable(cacheNames = "GUANGXUAN:ITEM:MYITEM",key = "#p0+'_'+#p1+'_'+#p2")
    public Object getMyItemPageList(Long userId, Integer page, Integer size) {
        if (page == null) {
            throw new BusinessException(BusinessFailEnum.PAGE_NOT_NULL);
        }
        if (size == null) {
            throw new BusinessException(BusinessFailEnum.SIZE_NOT_NULL);
        }
        return this.page(new Page<>(page, size), new LambdaQueryWrapper<MallItemInfo>()
                .eq(MallItemInfo::getUserId, userId).orderByDesc(MallItemInfo::getStatus));
    }

    @Override
    public Object saveInfoDetail(MallItemInfoDetailDTO itemInfo, MultipartFile multipartFile, Long id) throws IOException {
        if (itemInfo == null || itemInfo.getItemId() == null) {
            throw new BusinessException(BusinessFailEnum.NOT_GET_DATA);
        }
        if (itemInfo.getType() == 1 && itemInfo.getDescription() == null) {
            throw new BusinessException(BusinessFailEnum.DATA_VALID_FAIL);
        }
        if (itemInfo.getId() == null && (itemInfo.getType() == 2 || itemInfo.getItemId() == 3)
                && (multipartFile == null || multipartFile.getSize() == 0)) {
            throw new BusinessException(BusinessFailEnum.FILE_NOT_NULL);
        }
        if (itemInfo.getType() == 1 && itemInfo.getDescription().length() > 500) {
            throw new BusinessException(BusinessFailEnum.GRAPH_TOO_LONG);
        }
        MallItemInfoDetail mallItemInfoDetail = new MallItemInfoDetail();
        mallItemInfoDetail.setFilePath(multipartFile != null ? saveFile(multipartFile) : null);
        mallItemInfoDetail.setDescription(itemInfo.getDescription());
        mallItemInfoDetail.setMallItemInfoId(itemInfo.getItemId());
        mallItemInfoDetail.setSeq(itemInfo.getSeq());
        mallItemInfoDetail.setType(itemInfo.getType());
        mallItemInfoDetailService.save(mallItemInfoDetail);
        return mallItemInfoDetail;
    }

    @Override
    // @Cacheable(cacheNames = "GUANGXUAN:ITEM:SHARE",key = "id+'_'+itemId")
    public Object getShareUrl(Long id, Long itemId) {
        MallItemInfo mallItemInfo = this.getById(itemId);
        if (mallItemInfo == null) {
            throw new BusinessException(BusinessFailEnum.NOT_GET_DATA);
        }
        if (!mallItemInfo.getStatus().equals(ItemStatus.ON_SALE)) {
            throw new BusinessException(BusinessFailEnum.NOT_ON_SALE);
        }
        mallItemInfo.setShareCount(mallItemInfo.getShareCount() + 1);
        this.save(mallItemInfo);
        Users users = usersService.getById(id);
        String sharePath = systemProperties.getItemShare() + "?itemId=" + itemId + "&regCode=" + users.getRegCode();
        Map<String, String> map = new HashMap<>();
        map.put("sharePath", sharePath);
        return map;
    }

    @Override
    public Object getCollectionPageList(Integer page, Integer size, Long id) {
        if (page == null) {
            throw new BusinessException(BusinessFailEnum.PAGE_NOT_NULL);
        }
        if (size == null) {
            throw new BusinessException(BusinessFailEnum.SIZE_NOT_NULL);
        }
        return this.getBaseMapper().getCollectionPageList(new Page<>(page, size), id);
    }

    @Override
    public Object removeItem(Long itemId, Long userId) {
        int bannerCount = mallBannerService.count(new LambdaQueryWrapper<MallBanner>()
                .eq(MallBanner::getItemId, itemId).eq(MallBanner::getVisible, true));
        if (bannerCount > 0) {
            throw new BusinessException(BusinessFailEnum.BANNER_USED);
        }
        int headlineCount = headlineItemService.count(new LambdaQueryWrapper<HeadlineItem>()
                .eq(HeadlineItem::getItemId, itemId));
        if (headlineCount > 0) {
            throw new BusinessException(BusinessFailEnum.HEADLINE_USED_ITEM);
        }
        MallItemInfo mallItemInfo = this.getOne(new LambdaQueryWrapper<MallItemInfo>()
                .eq(MallItemInfo::getId, itemId)
                .eq(MallItemInfo::getUserId, userId));
        if (mallItemInfo == null) {
            throw new BusinessException(BusinessFailEnum.NOT_GET_DATA);
        }
        if (mallItemInfo.getStatus().equals(ItemStatus.NOT_AUDIT)
                || mallItemInfo.getStatus().equals(ItemStatus.ON_SALE)) {
            throw new BusinessException(BusinessFailEnum.CURRENT_STATUS_CANNOT_OPERATE);
        }
        this.removeById(itemId);
        return null;
    }

    @Override
    public Object saveLeaveMessage(ItemLeaveMessage leaveMessage, Long userId) {
        MallItemInfo mallItemInfo = this.getById(leaveMessage.getItemId());
        if (mallItemInfo == null) {
            throw new BusinessException(BusinessFailEnum.NOT_GET_DATA);
        }
        leaveMessage.setTime(new Date());
        leaveMessage.setDeleted(false);
        leaveMessage.setUserId(userId);
        itemLeaveMessageService.save(leaveMessage);
        return leaveMessage;
    }

    @Override
    public Object getLeaveMessagePage(Long itemId, Integer page, Integer size) {
        if (page == null) {
            throw new BusinessException(BusinessFailEnum.PAGE_NOT_NULL);
        }
        if (size == null) {
            throw new BusinessException(BusinessFailEnum.SIZE_NOT_NULL);
        }
        if (itemId == null) {
            throw new BusinessException(BusinessFailEnum.NOT_GET_ITM);
        }
        IPage<LeaveMessageDTO> leaveMessageDTOIPage = itemLeaveMessageService.getLeaveMessagePage(page, size, itemId);
        for (LeaveMessageDTO leaveMessageDTO : leaveMessageDTOIPage.getRecords()) {
            leaveMessageDTO.setPhone(leaveMessageDTO.getPhone().replaceAll("(\\d{7})(\\d{4})", "*******$2"));
        }
        return leaveMessageDTOIPage;
    }

//    @Override
//    public Object confirmAudit(Long userId, Long itemId) {
//        MallItemInfo mallItemInfo = this.getOne(new LambdaQueryWrapper<MallItemInfo>()
//                .eq(MallItemInfo::getId, itemId)
//                .eq(MallItemInfo::getUserId, userId));
//        if (mallItemInfo == null) {
//            throw new BusinessException(BusinessFailEnum.NOT_GET_DATA);
//        }
//        if (mallItemInfo.getStatus().equals(ItemStatus.NOT_AUDIT)
//                || mallItemInfo.getStatus().equals(ItemStatus.ON_SALE)
//                || mallItemInfo.getStatus().equals(ItemStatus.OFF_SHELVE)) {
//            throw new BusinessException(BusinessFailEnum.CURRENT_STATUS_CANNOT_OPERATE);
//        }
//        mallItemInfo.setStatus(0);
//        this.save(mallItemInfo);
//        return null;
//    }

}
