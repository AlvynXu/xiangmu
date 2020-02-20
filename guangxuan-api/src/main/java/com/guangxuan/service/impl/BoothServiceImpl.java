package com.guangxuan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guangxuan.constant.GoodStatusConstant;
import com.guangxuan.constant.MarketType;
import com.guangxuan.constant.OrderStatus;
import com.guangxuan.constant.PayConstant;
import com.guangxuan.dto.BoothDetailDTO;
import com.guangxuan.dto.SoldCountDTO;
import com.guangxuan.dto.SoldInfoDTO;
import com.guangxuan.dto.domain.BoothDTO;
import com.guangxuan.enumration.BusinessFailEnum;
import com.guangxuan.exception.BusinessException;
import com.guangxuan.mapper.BoothMapper;
import com.guangxuan.model.Booth;
import com.guangxuan.model.BoothUsers;
import com.guangxuan.model.MallConfig;
import com.guangxuan.model.Users;
import com.guangxuan.service.*;
import jodd.util.StringUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.*;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.guangxuan.constant.RedisConstant.FAKE_SOLD_BOOTH_COUNT;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zhuolin
 * @since 2019-11-27
 */
@Service
public class BoothServiceImpl extends ServiceImpl<BoothMapper, Booth> implements BoothService {
    @Resource
    MallConfigService mallConfigService;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private BoothUsersService boothUsersService;

    @Resource
    private UsersService usersService;

    @Resource
    private MarketService marketService;

    @Override
    // @Cacheable(cacheNames = "GUANGXUAN:BOOTH:COUNT")
    public Object getBootCountMessage() {
        Integer count = this.count();
        Integer soldCount = this.count(new LambdaQueryWrapper<Booth>().eq(Booth::getStatus, 1));
        if (redisTemplate.hasKey(FAKE_SOLD_BOOTH_COUNT)) {
            soldCount += (Integer) redisTemplate.opsForValue().get(FAKE_SOLD_BOOTH_COUNT);
        }
        SoldCountDTO soldCountDTO = SoldCountDTO.builder().soldCount(count < soldCount ? count : soldCount).totalCount(count).build();
        return soldCountDTO;
    }

    @Override
    // @Cacheable(cacheNames = "GUAGNXUAN:BOOTH:PAGE", key = "areaType+'_'+areaCode+'_'+isSaved+'_'+type+'_'+page+'_'+size")
    public IPage<Booth> getBoothPage(Integer page, Integer size, Integer type, String areaCode, Integer areaType, Boolean isSaved) {
        if (page == null) {
            throw new BusinessException(BusinessFailEnum.PAGE_NOT_NULL);
        }
        if (size == null) {
            throw new BusinessException(BusinessFailEnum.SIZE_NOT_NULL);
        }
        if (areaType == null) {
            throw new BusinessException(BusinessFailEnum.TYPE_NOT_NULL);
        }
        if (StringUtil.isBlank(areaCode)) {
            throw new BusinessException(BusinessFailEnum.AREA_CODE_NOT_NULL);
        }
        if (type == null) {
            throw new BusinessException(BusinessFailEnum.TYPE_NOT_NULL);
        }
        IPage<Booth> pages;
        if (areaType == 3) {
            pages = this.getBaseMapper().getCityPage(new Page<Booth>(page, size), areaCode, type, isSaved);
        } else if (areaType == 2) {
            pages = this.getBaseMapper().getDistrictPage(new Page<Booth>(page, size), areaCode, type, isSaved);
        } else if (areaType == 1) {
            pages = this.getBaseMapper().getStreetPage(new Page<Booth>(page, size), areaCode, type, isSaved);
        } else {
            pages = new Page<>();
        }
        MallConfig mallConfig = mallConfigService.getById(1L);
        for (Booth booth : pages.getRecords()) {
            booth.setPrice(mallConfig.getThreshold2());
        }
        return pages;
    }

    @Override
    public IPage<SoldInfoDTO> getCurrentUserBooth(Long userId, Integer page, Integer size, Integer status) {
        IPage<SoldInfoDTO> soldInfoDTOIPage = this.baseMapper.getPage(new Page<>(page, size), userId, status);
        for (SoldInfoDTO soldInfoDTO : soldInfoDTOIPage.getRecords()) {
            LocalDateTime expireTime = LocalDateTime.of(LocalDateTime.ofInstant(soldInfoDTO.getExpireDate().toInstant(),
                    ZoneId.systemDefault()).toLocalDate(), LocalTime.MAX);
            LocalDateTime end = LocalDateTime.now();
            Duration duration = Duration.between(end, expireTime);
            soldInfoDTO.setDays(duration.toDays());
            if (soldInfoDTO.getRentEndDate() != null) {
                LocalDateTime rentTime = LocalDateTime.of(LocalDateTime.ofInstant(soldInfoDTO.getRentEndDate().toInstant(),
                        ZoneId.systemDefault()).toLocalDate(), LocalTime.MAX);
                Duration rentDuration = Duration.between(end, rentTime);
                soldInfoDTO.setRentHours(rentDuration.toHours());
            }
        }
        return soldInfoDTOIPage;
    }

    @Override
    public void expire(Set<Long> boothIds) {
        if(boothIds.size()>0){
            this.getBaseMapper().expire(boothIds);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object choose(Long id, Long boothId) {
        Users user = usersService.getById(id);
        if (user.getChooseBoothCount() <= 0) {
            throw new BusinessException(BusinessFailEnum.NO_CHOOSE_COUNT);
        }
        Booth booth = this.getById(boothId);
        if (!booth.getSaved() || booth.getStatus() != 0) {
            throw new BusinessException(BusinessFailEnum.BOOTH_NOT_SALE_STATUS);
        }
        user.setChooseBoothCount(user.getChooseBoothCount() - 1);
        usersService.updateById(user);
        BoothUsers boothUsers = new BoothUsers();
        boothUsers.setBoothId(booth.getId());
        boothUsers.setCreateTime(new Date());
        boothUsers.setOrderNo(UUID.randomUUID().toString());
        boothUsers.setOrderStatus(OrderStatus.PAID);
        boothUsers.setStatus(PayConstant.SUCCESS_PAY);
        boothUsers.setUserId(id);
        boothUsers.setExpireTime(Date.from(LocalDate.now().plusYears(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
        boothUsersService.save(boothUsers);
        booth.setStatus(GoodStatusConstant.SOLD);
        this.updateById(booth);
        return null;
    }

    @Override
    public IPage<BoothDTO> getAdminBoothPage(Integer page, Integer size, String streetCode, String boothCode) {
        IPage<BoothDTO> boothDTOIPage = this.getBaseMapper().getAdminBoothPage(new Page<>(page, size), streetCode, boothCode);
        for (BoothDTO boothDTO : boothDTOIPage.getRecords()) {
            LocalDateTime end = LocalDateTime.now();
            if (boothDTO.getEndTime() != null) {
                LocalDateTime localDateTime = LocalDateTime.ofInstant(boothDTO.getEndTime().toInstant(), ZoneId.systemDefault());
                Duration duration = Duration.between(end, localDateTime);
                boothDTO.setLeftDate(duration.toDays());
            }
            if (boothDTO.getMarketEndTime() != null) {
                LocalDateTime rentLocalDateTime = LocalDateTime.ofInstant(boothDTO.getMarketEndTime().toInstant(), ZoneId.systemDefault());
                Duration rentDuration = Duration.between(end, rentLocalDateTime);
                boothDTO.setLeftRentDate(rentDuration.toDays());
            }
            if (boothDTO.getRentType() != null) {
                if (boothDTO.getRentType().equals(MarketType.RENT_SEEK)) {
                    boothDTO.setRentUserPhone(boothDTO.getMarketUserPhone());
                } else {
                    boothDTO.setRentUserPhone(boothDTO.getMarketRentUserPhone());
                }
            }
        }
        return boothDTOIPage;
    }

    @Override
    public List<SoldInfoDTO> getCanRentBooth(Long userId, Set<String> streetCodes) {
        return this.getBaseMapper().getCanRentBooth(userId, streetCodes);
    }

    @Override
    public List<BoothDetailDTO> getBoothDetail(Set<Long> boothIds) {
        return this.getBaseMapper().getBoothDetail(boothIds);
    }


}
