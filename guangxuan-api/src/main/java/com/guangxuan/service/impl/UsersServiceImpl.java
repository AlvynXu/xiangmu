package com.guangxuan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guangxuan.config.SystemProperties;
import com.guangxuan.constant.CommissionLogConstant;
import com.guangxuan.constant.PayConstant;
import com.guangxuan.constant.PayResourceConstant;
import com.guangxuan.constant.RedisConstant;
import com.guangxuan.dto.*;
import com.guangxuan.dto.domain.UserDTO;
import com.guangxuan.enumration.BusinessFailEnum;
import com.guangxuan.exception.BusinessException;
import com.guangxuan.mapper.UsersMapper;
import com.guangxuan.model.*;
import com.guangxuan.service.*;
import com.guangxuan.util.JwtUtil;
import com.guangxuan.util.RandomCodeUtils;
import com.guangxuan.util.SmsUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zhuolin
 * @since 2019-11-27
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements UsersService {

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private BoothUsersService boothUsersService;

    @Resource
    private StreetPartnerOrderService streetPartnerOrderService;

    @Resource
    private SystemProperties systemProperties;

    @Resource
    private MessageService messageService;

    @Resource
    private MallConfigService mallConfigService;

    @Resource
    private CommissionLogService commissionLogService;

    @Resource
    private PayLogService payLogService;

    @Resource
    private MallItemInfoService mallItemInfoService;

    @Resource
    private MarketService marketService;

    @Resource
    private AppVersionService appVersionService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object setPassword(String phone, String password, Long userId) {
//        Users loginUsers = CurrentUserLoginUtils.getCurrentUsers();
        Users loginUsers = this.getById(userId);
        if (loginUsers == null) {
            throw new BusinessException(BusinessFailEnum.NOT_GET_CURRENT_USER);
        }
        if (!loginUsers.getPhone().equals(phone)) {
            throw new BusinessException(BusinessFailEnum.NOT_EQUAL_CURRENT_USER_PHONE);
        }
        Users users = this.getOne(new LambdaQueryWrapper<Users>().eq(Users::getPhone, phone));
        if (users == null) {
            throw new BusinessException(BusinessFailEnum.NOT_GET_CURRENT_USER);
        }
        users.setDeleted(false);
        if (StringUtils.isNotBlank(password)) {

            users.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
        }
        users.setPhone(phone);
        users.setUpdateTime(new Date());
        this.updateById(users);
        String token = JwtUtil.sign(users.getPhone(), users.getPassword());
        LoginUser loginUser = new LoginUser();
        loginUser.setToken(token);
        loginUser.setUser(users);
        return loginUser;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object validRandomCode(String phone, String randomCode, Long promoterId) {
        if (!systemProperties.getDebugOff()) {
            if (!redisTemplate.hasKey(RedisConstant.REGISTER_RANDOM_CODE + phone)) {
                throw new BusinessException(BusinessFailEnum.NOT_GET_RANDOM);
            }
            String saveRandomCode = (String) redisTemplate.opsForValue().get(RedisConstant.REGISTER_RANDOM_CODE + phone);
            if (!randomCode.equals(saveRandomCode)) {
                throw new BusinessException(BusinessFailEnum.VALID_RANDOM_FAIL);
            }
        }
        Boolean flag = false;
        Users users = this.getOne(new LambdaQueryWrapper<Users>().eq(Users::getPhone, phone));
        if (users == null) {
            users = new Users();
            users.setCreateTime(new Date());
            if (promoterId != null) {
                Users promoterUsers = this.getOne(new LambdaQueryWrapper<Users>().eq(Users::getRegCode, promoterId));
                if (promoterUsers != null) {
                    users.setPromoterId(promoterUsers.getId());
                } else {
                    users.setPromoterId(1L);
                }
            } else {
                users.setPromoterId(1L);
            }
            users.setRegCode(createInviteCode());
            flag = true;
        }
        users.setDeleted(false);
        users.setPhone(phone);
        users.setUpdateTime(new Date());
        this.saveOrUpdate(users);
        Map<String, Object> data = new HashMap<>();
        data.put("userId", users.getId());
        data.put("flag", flag);
        data.put("regCode", users.getRegCode());
        if (flag) {
            PromoterDTO promoterDTO = PromoterDTO.builder().userId(users.getId()).promoterDTOS(new ArrayList<>()).build();
            if (!redisTemplate.hasKey(RedisConstant.USER_PROMOTE_COUNT)) {

                redisTemplate.opsForValue().set(RedisConstant.USER_PROMOTE_COUNT, promoterDTO);
            } else {
                PromoterDTO savePromoterDTO = (PromoterDTO) redisTemplate.opsForValue().get(RedisConstant.USER_PROMOTE_COUNT);
                PromoterDTO targetPromoterDto =
                        getTargetPromoter(users.getPromoterId(), savePromoterDTO);
                targetPromoterDto.getPromoterDTOS().add(promoterDTO);
                redisTemplate.opsForValue().set(RedisConstant.USER_PROMOTE_COUNT, savePromoterDTO);
            }
        }
        return data;
    }

    @Override
    public Object sendRandomCode(String phone) {
        if (!Pattern.matches("^[1][\\d]{10}$", phone)) {
            throw new BusinessException(BusinessFailEnum.PHONE_VALID_FAIL);
        }
        if (!systemProperties.getSmsOff()) {
            String randomCode = RandomCodeUtils.generateRandomCode(4);
            redisTemplate.opsForValue().set(RedisConstant.REGISTER_RANDOM_CODE + phone, randomCode, 5, TimeUnit.MINUTES);
            SmsUtils.sendSms(phone, randomCode);
        }
        return true;
    }

    /**
     * 用户登录
     *
     * @param user
     * @return
     */
    @Override
    public LoginUser login(LoginDTO user) {
        Users user1 = this.getOne(new LambdaQueryWrapper<Users>().eq(Users::getPhone, user.getPhone()));
        if (user1 == null) {
            throw new BusinessException(BusinessFailEnum.USER_NAME_OR_PASSWORD_ERROR);
        }
        if (!DigestUtils.md5DigestAsHex(user.getPassword().getBytes()).equals(user1.getPassword())) {
            throw new BusinessException(BusinessFailEnum.USER_NAME_OR_PASSWORD_ERROR);
        }
        LoginUser loginUser = new LoginUser();
        loginUser.setUser(user1);
        //根据电话号码和密码加密生成token
        String token = JwtUtil.sign(user1.getPhone(), user1.getPassword());
        loginUser.setToken(token);
        return loginUser;
    }

    @Override
    public Object getLoginRandomCode(String phone) {
        if (!Pattern.matches("^[1][\\d]{10}$", phone)) {
            throw new BusinessException(BusinessFailEnum.PHONE_VALID_FAIL);
        }
//        Users users = this.getOne(new LambdaQueryWrapper<Users>().eq(Users::getPhone, phone));
//        if (users == null) {
//            throw new BusinessException(BusinessFailEnum.NOT_REGISTER_PHONE);
//        }
        if (!systemProperties.getSmsOff()) {
            String randomCode = RandomCodeUtils.generateRandomCode(4);
            redisTemplate.opsForValue().set(RedisConstant.LOGIN_RANDOM_CODE + phone, randomCode, 5, TimeUnit.MINUTES);
            SmsUtils.sendSms(phone, randomCode);
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoginUser phoneLogin(LoginDTO user) {
        Users users = this.getOne(new LambdaQueryWrapper<Users>().eq(Users::getPhone, user.getPhone()));
        boolean flag = false;
        if (users == null) {
            users = new Users();
            users.setCreateTime(new Date());
            users.setRegCode(createInviteCode());
            flag = true;
            users.setDeleted(false);
            users.setPhone(user.getPhone());
            users.setUpdateTime(new Date());
            users.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
            this.saveOrUpdate(users);
        }
        if (!systemProperties.getDebugOff()) {
            if (!redisTemplate.hasKey(RedisConstant.LOGIN_RANDOM_CODE + user.getPhone())) {
                throw new BusinessException(BusinessFailEnum.NOT_GET_RANDOM);
            }
            if (!redisTemplate.opsForValue().get(RedisConstant.LOGIN_RANDOM_CODE + user.getPhone()).equals(user.getPassword())) {
                throw new BusinessException(BusinessFailEnum.VALID_RANDOM_FAIL);
            }
        }
        LoginUser loginUser = new LoginUser();
        loginUser.setUser(users);
        //根据电话号码和密码加密生成token
        String token = JwtUtil.sign(users.getPhone(), users.getPassword());
        loginUser.setToken(token);
        loginUser.setFlag(flag);
        return loginUser;
    }

    @Override
    // @Cacheable(cacheNames = "GUANGXUAN:USER:TEAM", key = "id")
    public PromoterCountDTO getTeam(Long id) {
        PromoterDTO promoterDTO = (PromoterDTO) redisTemplate.opsForValue().get(RedisConstant.USER_PROMOTE_COUNT);
        PromoterDTO targetPromoterDTO =
                getTargetPromoter(id, promoterDTO);
        Integer totalCount = countTotal(targetPromoterDTO);
        Integer directCount = targetPromoterDTO.getPromoterDTOS().size();
        PromoterCountDTO result = new PromoterCountDTO();
        result.setDirectCount(directCount);
        result.setTotalCount(totalCount);
        return result;
    }

    @Override
    public Object setPromoter(String promoterId, Long id) {
        Users users = this.getById(id);
        if (users == null) {
            throw new BusinessException(BusinessFailEnum.NOT_GET_CURRENT_USER);
        }
        if (StringUtils.isBlank(users.getPassword())) {
            throw new BusinessException(BusinessFailEnum.NOT_SET_PASSWORD);
        }
        if (users.getPromoterId() == null && promoterId != null) {
            Users promoterUsers = this.getOne(new LambdaQueryWrapper<Users>().eq(Users::getRegCode, promoterId));
            if (promoterUsers == null) {
                throw new BusinessException(BusinessFailEnum.NOT_GET_PROMOTER_USER);
            } else {
                users.setPromoterId(promoterUsers.getId());
            }
        } else {
            return null;
        }
        this.updateById(users);
        PromoterDTO promoterDTO = PromoterDTO.builder().userId(users.getId()).promoterDTOS(new ArrayList<>()).build();
        if (!redisTemplate.hasKey(RedisConstant.USER_PROMOTE_COUNT)) {

            redisTemplate.opsForValue().set(RedisConstant.USER_PROMOTE_COUNT, promoterDTO);
        } else {
            PromoterDTO savePromoterDTO = (PromoterDTO) redisTemplate.opsForValue().get(RedisConstant.USER_PROMOTE_COUNT);
            PromoterDTO targetPromoterDto =
                    getTargetPromoter(users.getPromoterId(), savePromoterDTO);
            targetPromoterDto.getPromoterDTOS().add(promoterDTO);
            redisTemplate.opsForValue().set(RedisConstant.USER_PROMOTE_COUNT, savePromoterDTO);
        }
        return null;
    }

    @Override
    // @Cacheable(cacheNames = "GUANGXUAN:USER:MESSAGEBALANCE", key = "id")
    public Object getMessage(Long id) {
        PromoterDTO promoterDTO = (PromoterDTO) redisTemplate.opsForValue().get(RedisConstant.USER_PROMOTE_COUNT);
        PromoterDTO targetPromoterDTO =
                getTargetPromoter(id, promoterDTO);
        Integer totalCount = countTotal(targetPromoterDTO);
        Users users = this.getById(id);
        Integer count = messageService.getUnreadMessageCount(id);

        MallConfig mallConfig = mallConfigService.getById(1L);
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setTotalMessage(messageService.count(new LambdaQueryWrapper<Message>().eq(Message::getToUserId, id)));
        userInfoDTO.setMessage(count);
        userInfoDTO.setProfit(users.getProfit());
        userInfoDTO.setTeam(totalCount);
        userInfoDTO.setVipPrice(mallConfig.getThreshold1());
        userInfoDTO.setBalance(users.getBalance());
        return userInfoDTO;

    }

    @Override
    public Object getProfit(Long id, Integer page, Integer size) {
        if (page == null || size == null) {
            throw new BusinessException(BusinessFailEnum.NOT_GET_PAGE_INFO);
        }
        IPage<CommissionLog> commissionLogPage = commissionLogService.page(new Page<>(page, size),
                new LambdaQueryWrapper<CommissionLog>().eq(CommissionLog::getUserId, id)
                        .orderByDesc(CommissionLog::getCreateTime));
        List<Long> fromUserIds = commissionLogPage.getRecords().stream().map(CommissionLog::getFromUserId).collect(Collectors.toList());
        Collection<Users> users = new ArrayList<>();
        if (fromUserIds.size() > 0) {
            users = this.listByIds(fromUserIds);
        }
        Map<Long, Users> map = users.stream().collect(Collectors.toMap(Users::getId, a -> a));
        List<CommissionLogDTO> commissionLogDTOS = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        for (CommissionLog log : commissionLogPage.getRecords()) {
            Users user = map.get(log.getFromUserId());
            CommissionLogDTO commissionLogDTO = CommissionLogDTO.builder()
                    .amount(log.getAmount())
                    .time(simpleDateFormat.format(log.getCreateTime()))
                    .id(log.getId())
                    .type(log.getType() == CommissionLogConstant.AREA ? "区域"
                            : (log.getType() == CommissionLogConstant.DIRECT ? "直推"
                            : (log.getType() == CommissionLogConstant.RECHARGE ? "充值"
                            : (log.getType() == CommissionLogConstant.WITHDRAWAL ? "提现" : ""))))
                    .name(user.getPhone().replaceAll("(\\d{7})(\\d{4})", "*******$2"))
                    .build();
            commissionLogDTOS.add(commissionLogDTO);
        }
        IPage page1 = new Page();
        page1.setTotal(commissionLogPage.getTotal());
        page1.setCurrent(commissionLogPage.getCurrent());
        page1.setPages(commissionLogPage.getPages());
        page1.setSize(commissionLogPage.getSize());
        page1.setRecords(commissionLogDTOS);
        return page1;
    }

    @Override
    // @Cacheable(cacheNames = "GUANGXUAN:USER:TEAMPAGE", key = "id+'_'+page+'_'+size")
    public Object getTeamPage(Long id, Integer page, Integer size) {
        if (page == null || size == null) {
            throw new BusinessException(BusinessFailEnum.NOT_GET_PAGE_INFO);
        }
        PromoterDTO promoterDTO = (PromoterDTO) redisTemplate.opsForValue().get(RedisConstant.USER_PROMOTE_COUNT);
        PromoterDTO targetPromoterDTO =
                getTargetPromoter(id, promoterDTO);
        List<PromoterCountDTO> promoterCountDTOS = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        List<Long> userIds = targetPromoterDTO.getPromoterDTOS().stream()
                .map(PromoterDTO::getUserId).collect(Collectors.toList());
        Map<Long, PromoterDTO> map = targetPromoterDTO.getPromoterDTOS()
                .stream().collect(Collectors.toMap(PromoterDTO::getUserId, a -> a));
        IPage<Users> usersPage = new Page<>();
        if (userIds.size() > 0) {
            usersPage = this.page(new Page<>(page, size),
                    new LambdaQueryWrapper<Users>().in(Users::getId, userIds).orderByDesc(Users::getCreateTime));
        }
//        Map<Long, Users> usersMap = usersPage.getRecords().stream().collect(Collectors.toMap(Users::getId, a -> a));
        for (Users user : usersPage.getRecords()) {
//            if (!usersMap.containsKey(promoterDTO1.getUserId())) {
//                continue;
//            }
//            Users user = usersMap.get(promoterDTO1.getUserId());
            PromoterCountDTO countDTO = new PromoterCountDTO();
            countDTO.setTotalCount(countTotal(map.get(user.getId())));
            countDTO.setDirectCount(map.get(user.getId()).getPromoterDTOS().size());
            countDTO.setTime(simpleDateFormat.format(user.getCreateTime()));
            countDTO.setName(user.getPhone().replaceAll("(\\d{7})(\\d{4})", "*******$2"));
            promoterCountDTOS.add(countDTO);
        }
        IPage page1 = new Page();
        page1.setTotal(usersPage.getTotal());
        page1.setCurrent(usersPage.getCurrent());
        page1.setPages(usersPage.getPages());
        page1.setRecords(promoterCountDTOS);
        page1.setSize(usersPage.getSize());
        return page1;
    }

    @Override
    // @Cacheable(cacheNames = "GUANGXUAN:USER:PAYLOG", key = "userId+'+'+page+'_'+size")
    public Object getPayLog(Long userId, Integer page, Integer size) {
        IPage<PayLog> payLogIPage = payLogService.page(new Page<PayLog>(page, size),
                new LambdaQueryWrapper<PayLog>().eq(PayLog::getUserId, userId)
                        .eq(PayLog::getStatus, PayConstant.SUCCESS_PAY)
                        .orderByDesc(PayLog::getCreateTime));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        for (PayLog payLog : payLogIPage.getRecords()) {
            payLog.setTypeString(PayResourceConstant.valueOfType(payLog.getSource()).getTypeName());
            payLog.setTime(simpleDateFormat.format(payLog.getCreateTime()));
        }
        return payLogIPage;
    }

    @Override
    public Object getMyBaseInfo(Long id) {
        Integer boothCount = boothUsersService.count(new LambdaQueryWrapper<BoothUsers>().eq(BoothUsers::getUserId, id)
                .eq(BoothUsers::getStatus, PayConstant.SUCCESS_PAY));
        Integer marketCount = marketService.count(new LambdaQueryWrapper<Market>().eq(Market::getUserId, id)
                .isNotNull(Market::getBoothId));
        Integer streetCount = streetPartnerOrderService.count(new LambdaQueryWrapper<StreetPartnerOrder>()
                .eq(StreetPartnerOrder::getUserId, id).eq(StreetPartnerOrder::getStatus, PayConstant.SUCCESS_PAY));
        Map<String, Integer> map = new HashMap<>();
        map.put("boothCount", boothCount + marketCount);
        map.put("streetCount", streetCount);
        return map;
    }

    @Override
    public IPage<UserDTO> getPage(Integer page, Integer size, String phone, Integer level) {
        IPage<UserDTO> userDTOIPage = this.getBaseMapper().getPage(new Page<>(page, size), phone, level);
        for (UserDTO userDTO : userDTOIPage.getRecords()) {
            userDTO.setBoothCount(boothUsersService.count(new LambdaQueryWrapper<BoothUsers>()
                    .eq(BoothUsers::getUserId, userDTO.getId()).eq(BoothUsers::getStatus, PayConstant.SUCCESS_PAY)));
            userDTO.setBoothCount(streetPartnerOrderService.count(new LambdaQueryWrapper<StreetPartnerOrder>()
                    .eq(StreetPartnerOrder::getUserId, userDTO.getId()).eq(StreetPartnerOrder::getStatus, PayConstant.SUCCESS_PAY)));
            userDTO.setItemCount(mallItemInfoService.count(new LambdaQueryWrapper<MallItemInfo>().eq(MallItemInfo::getUserId, userDTO.getId())));
            PromoterCountDTO promoterCountDTO = getTeam(userDTO.getId());
            userDTO.setDirect(promoterCountDTO.getDirectCount());
            userDTO.setIndirect(promoterCountDTO.getIndirectCount());
            userDTO.setEnabled(!userDTO.getDeleted());
        }
        return userDTOIPage;
    }

    @Override
    public DownloadPathDTO getDownloadPath() {
        DownloadPathDTO downloadPathDTO = DownloadPathDTO.builder()
                .androidPath(appVersionService.getOne(new LambdaQueryWrapper<AppVersion>()
                        .eq(AppVersion::getEnable, true).eq(AppVersion::getType, 0)).getDownloadPth())
                .iosPath("http://wlgx.hiyanjiao.com/register/index.html").build();
        return downloadPathDTO;
    }


    private String createInviteCode() {
        String code = String.valueOf(RandomUtils.nextInt(100000, 999999))
                .replaceAll("4", "0");
        Users user = this.getOne(new LambdaQueryWrapper<Users>().eq(Users::getRegCode, code));
        if (user != null) {
            return createInviteCode();
        }
        return code;
    }


    private Integer countTotal(PromoterDTO promoterDTO) {
        Integer count = 0;
        count += promoterDTO.getPromoterDTOS().size();
        for (PromoterDTO promoterInfo : promoterDTO.getPromoterDTOS()) {
            count += countTotal(promoterInfo);
        }
        return count;
    }

    private PromoterDTO getTargetPromoter(Long userId, PromoterDTO promoterDTO) {
        PromoterDTO targetPromoterDTO;
        if (promoterDTO.getUserId().equals(userId)) {
            return promoterDTO;
        }
        for (PromoterDTO childPromoterDTO : promoterDTO.getPromoterDTOS()) {
            targetPromoterDTO = getTargetPromoter(userId, childPromoterDTO);
            if (targetPromoterDTO != null && targetPromoterDTO.getUserId() != null) {
                return targetPromoterDTO;
            }
        }
        return null;
    }

}
