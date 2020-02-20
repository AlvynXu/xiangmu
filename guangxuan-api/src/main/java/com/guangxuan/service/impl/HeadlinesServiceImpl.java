package com.guangxuan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guangxuan.dto.domain.HeadlineDTO;
import com.guangxuan.enumration.BusinessFailEnum;
import com.guangxuan.exception.BusinessException;
import com.guangxuan.mapper.HeadlinesMapper;
import com.guangxuan.mapper.MallItemInfoMapper;
import com.guangxuan.model.HeadlineItem;
import com.guangxuan.model.HeadlineItemArea;
import com.guangxuan.model.Headlines;
import com.guangxuan.model.MallItemInfo;
import com.guangxuan.service.HeadlineItemAreaService;
import com.guangxuan.service.HeadlinesService;
import com.guangxuan.service.MallItemInfoService;
import jodd.util.StringUtil;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zhuolin
 * @since 2019-12-15
 */
@Service
public class HeadlinesServiceImpl extends ServiceImpl<HeadlinesMapper, Headlines> implements HeadlinesService {

    @Resource
    private MallItemInfoMapper mallItemInfoMapper;

    @Resource
    private MallItemInfoService mallItemInfoService;

    @Resource
    private HeadlineItemAreaService headlineItemAreaService;

    @Override
    // @Cacheable(cacheNames = "GUANGXUAN:HEADLINE:PAGE",key = "areaType+'_'+areaCode+'_'+page+'_'+size")
    public Object pageList(Integer areaType, String areaCode, Integer page, Integer size) {
        if (page == null) {
            throw new BusinessException(BusinessFailEnum.PAGE_NOT_NULL);
        }
        if (size == null) {
            throw new BusinessException(BusinessFailEnum.SIZE_NOT_NULL);
        }
        if (areaType == null) {
            throw new BusinessException(BusinessFailEnum.TYPE_NOT_NULL);
        }
        if (areaType != 4 && StringUtil.isBlank(areaCode)) {
            throw new BusinessException(BusinessFailEnum.AREA_CODE_NOT_NULL);
        }
        return mallItemInfoMapper.getHeadlinesPage(new Page<MallItemInfo>(page, size), areaType == 4 ? "-1" : areaCode);
    }

    @Override
    public IPage<HeadlineDTO> getAdminPage(String areaCode, Integer page, Integer size) {
        IPage<HeadlineDTO> headlineDTOIPage = this.getBaseMapper().getAdminPage(new Page<>(page, size), areaCode);
        List<Long> itemIds = headlineDTOIPage.getRecords().stream().map(HeadlineDTO::getItemId).collect(Collectors.toList());
        Map<Long, MallItemInfo> map = new HashMap<>();
        if (itemIds.size() > 0) {
            Collection<MallItemInfo> mallItemInfos = mallItemInfoService.listByIds(itemIds);
            map.putAll(mallItemInfos.stream().collect(Collectors.toMap(MallItemInfo::getId, a -> a)));
        }
        for (HeadlineDTO headlineDTO : headlineDTOIPage.getRecords()) {
            if (map.containsKey(headlineDTO.getItemId())) {
                headlineDTO.setDescription(map.get(headlineDTO.getItemId()).getDescription());
            }
            LocalDateTime localDateTime = LocalDateTime.of(LocalDateTime.ofInstant(headlineDTO.getEndTime().toInstant(),
                    ZoneId.systemDefault()).toLocalDate(), LocalTime.MAX);
            LocalDateTime end = LocalDateTime.now();
            Duration duration = Duration.between(end, localDateTime);
            headlineDTO.setLeftDays(duration.toDays());
            List<HeadlineItemArea> headlineItemAreas = headlineItemAreaService.list(new LambdaQueryWrapper<HeadlineItemArea>().eq(HeadlineItemArea::getHeandlineItemId, headlineDTO.getId()));
            headlineDTO.setAreaCodes(headlineItemAreas.stream().map(HeadlineItemArea::getAreaCode).collect(Collectors.toList()));
        }
        return headlineDTOIPage;
    }

    @Override
    public List<HeadlineItem> getAdminHeadlinesItem(Integer sort, List<String> areaCodes, Integer status) {
        return this.getBaseMapper().getAdminList(sort, areaCodes, status);
    }
}
