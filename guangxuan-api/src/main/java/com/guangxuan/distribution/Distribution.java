package com.guangxuan.distribution;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.guangxuan.constant.*;
import com.guangxuan.model.*;
import com.guangxuan.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

/**
 * @author zhuolin
 * @Date 2019/11/27
 */
@Component
public class Distribution {
    private Logger logger = LoggerFactory.getLogger("distributionLogger");

    @Resource
    private UsersService usersService;

    @Resource
    private MallConfigService mallConfigService;

    @Resource
    private StreetService streetService;

    @Resource
    private AreaService areaService;

    @Resource
    private CommissionLogService commissionLogService;

    @Resource
    private MallItemService mallItemService;

    @Resource
    private StreetPartnerOrderService streetPartnerOrderService;

    @Resource
    private AreaPartnerOrderService areaPartnerOrderService;

    @Resource
    private PayLogService payLogService;

    /**
     * @param userId   购买人id
     * @param money
     * @param areaType -1 街道
     * @param areaCode
     * @param itemId
     */
    public void distribution(Long userId, BigDecimal money, Integer areaType, String areaCode, Long itemId)
            throws NoSuchFieldException, IllegalAccessException {
        MallItem mallItem = mallItemService.getById(itemId);
        logger.info(JSON.toJSONString(mallItem));
        logger.info(JSON.toJSONString("========================="));
        // 平台收益情况
        MallConfig mallConfig = mallConfigService.getById(1L);
        logger.info(JSON.toJSONString(mallConfig));
        logger.info("订单金额{}", money);

        // 平台抽取总金额
        BigDecimal totalPlatform = (mallItem == null ? mallConfig.getPlatformRate() : mallItem.getPlatformRate()).multiply(money);
        logger.info("平台金额{}", totalPlatform);
        // 平台分给用户金额
        BigDecimal dividePlatform = mallConfig.getPlatformDivideRate().multiply(totalPlatform);
        logger.info("波比{}金额{}", mallConfig.getPlatformDivideRate(), dividePlatform);
        // 全网
        BigDecimal platformIndirect = mallConfig.getPlatformIndirectRate().multiply(totalPlatform);

        // 区域总金额
        BigDecimal platformArea = mallConfig.getPlatformAreaRate().multiply(totalPlatform);


        Users user = usersService.getById(userId);
        // 直推金额
        direct(mallConfig, user.getPromoterId(), user.getId(), money, dividePlatform);
        // 区域
        if (areaCode != null) {
            logger.info("区域{}金额{}剩余{}", mallConfig.getPlatformAreaRate(), platformArea, platformArea);
            global(areaType, areaCode, mallConfig, dividePlatform, user.getId(), money, platformArea);
        }
//        // 全网
//        logger.info("全网{}金额{}", mallConfig.getPlatformIndirectRate(), platformIndirect);
//        indirect(mallConfig, user.getPromoterId(), user.getId(), money, dividePlatform, platformIndirect);
    }

    /**
     * 全网
     *
     * @param mallConfig
     * @param promoterId
     * @param fromUserId
     * @param totalMount
     * @param divideMount
     * @param leftMoney
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private void indirect(MallConfig mallConfig, Long promoterId, Long fromUserId,
                          BigDecimal totalMount, BigDecimal divideMount, BigDecimal leftMoney)
            throws NoSuchFieldException, IllegalAccessException {
        if (divideMount.equals(BigDecimal.ZERO)) {
            return;
        }
        Users directUser = usersService.getById(promoterId);
        if (directUser == null) {
            return;
        }
        Field field = mallConfig.getClass().getDeclaredField("indirectRate" + directUser.getLevel());
        //得到所有属性
        field.setAccessible(true);
        //获取属性
        BigDecimal value = (BigDecimal) field.get(mallConfig);
        // 本次收益
        BigDecimal profit = divideMount.multiply(value);
        profit = profit.compareTo(leftMoney) > 0 ? leftMoney : profit;
        CommissionLog commissionLog = CommissionLog.builder()
                .amount(profit)
                .createTime(new Date())
                .fromUserId(fromUserId)
                .rate(value)
                .totalAmount(totalMount)
                .userId(directUser.getId())
                .type(1)
                .build();
        PayLog payLog = PayLog.builder()
                .realAmount(profit).payType(PayTypeConstant.BALANCE_PAY)
                .amount(profit).userId(directUser.getId())
                .extend1("INDIR"+ UUID.randomUUID().toString()).createTime(new Date()).status(PayConstant.SUCCESS_PAY)
                .source(PayResourceConstant.INDIRECT.getType()).build();
        payLogService.save(payLog);
        directUser.setTeamProfit(directUser.getTeamProfit().add(profit));
        directUser.setProfit(directUser.getProfit().add(profit));
        directUser.setBalance(directUser.getBalance().add(profit));
        usersService.updateById(directUser);
        commissionLogService.save(commissionLog);
        if (directUser.getPromoterId() == null || directUser.getId().equals(directUser.getPromoterId())) {
            return;
        }
        logger.info("全网{}用户{}比例{}金额{}剩余{}", mallConfig.getPlatformAreaRate(), directUser.getLevel(), value, profit, leftMoney.subtract(profit));

        indirect(mallConfig, directUser.getPromoterId(), fromUserId, totalMount, divideMount, leftMoney.subtract(profit));
    }

    /**
     * 地区
     *
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private void global(Integer areaType, String areaCode, MallConfig mallConfig, BigDecimal divideMount,
                        Long fromUserId, BigDecimal totalMount, BigDecimal leftMoney)
            throws NoSuchFieldException, IllegalAccessException {
        if (leftMoney.equals(BigDecimal.ZERO)) {
            return;
        }
        if (areaType == 2) {
            return;
        }
        Field field = mallConfig.getClass().getDeclaredField("globalRate" + (areaType + 4));
        //得到所有属性
        field.setAccessible(true);
        //获取属性
        BigDecimal value = (BigDecimal) field.get(mallConfig);
        // 本次收益
        BigDecimal profit = divideMount.multiply(value);
        profit = profit.compareTo(leftMoney) > 0 ? leftMoney : profit;
        Long userId = null;
        String parentAreaCode = null;
        if (areaType == -1) {
            Street street = streetService.getOne(new LambdaQueryWrapper<Street>().eq(Street::getCode, areaCode));
            if (street == null) {
                return;
            }
            parentAreaCode = street.getAreaCode();
            StreetPartnerOrder streetPartnerOrder = streetPartnerOrderService.getOne(
                    new LambdaQueryWrapper<StreetPartnerOrder>()
                            .eq(StreetPartnerOrder::getOrderStatus, OrderStatus.PAID)
                            .eq(StreetPartnerOrder::getStreetId, street.getId()));
            if (streetPartnerOrder == null) {
                userId = null;
            } else {
                userId = streetPartnerOrder.getUserId();
            }

        } else {
            Area area = areaService.getOne(new LambdaQueryWrapper<Area>().eq(Area::getCode, areaCode));
            if (area == null) {
                return;
            }
            parentAreaCode = area.getParentCode();
            AreaPartnerOrder areaPartnerOrder = areaPartnerOrderService.getOne(new LambdaQueryWrapper<AreaPartnerOrder>()
                    .eq(AreaPartnerOrder::getStatus, OrderStatus.PAID)
                    .eq(AreaPartnerOrder::getAreaId, area.getId()));
            if (areaPartnerOrder == null) {
                userId = null;
            } else {
                userId = areaPartnerOrder.getUserId();
            }
        }
        if (userId == null) {
            global(areaType + 1, parentAreaCode, mallConfig, divideMount, fromUserId, totalMount, leftMoney);
        }
        Users users = usersService.getById(userId);
        if (users == null) {
            return;
        }
        CommissionLog commissionLog = CommissionLog.builder()
                .amount(profit)
                .createTime(new Date())
                .fromUserId(fromUserId)
                .rate(value)
                .totalAmount(totalMount)
                .userId(userId)
                .type(CommissionLogConstant.AREA)
                .build();
        commissionLogService.save(commissionLog);
        PayLog payLog = PayLog.builder()
                .realAmount(profit).payType(PayTypeConstant.BALANCE_PAY)
                .amount(profit).userId(users.getId())
                .extend1("GLO"+ UUID.randomUUID().toString()).createTime(new Date()).status(PayConstant.SUCCESS_PAY)
                .source(PayResourceConstant.AREA.getType()).build();
        payLogService.save(payLog);
        users.setTeamProfit(users.getTeamProfit().add(profit));
        users.setProfit(users.getProfit().add(profit));
        users.setBalance(users.getBalance().add(profit));
        usersService.updateById(users);
        logger.info("区域{}type{}比例{}金额{}剩余{}", mallConfig.getPlatformAreaRate(), areaType, value, profit, leftMoney.subtract(profit));
        global(areaType + 1, parentAreaCode, mallConfig, divideMount, fromUserId, totalMount, leftMoney.subtract(profit));
    }

    /**
     * 直推
     *
     * @param mallConfig
     * @param promoterId
     * @param fromUserId
     * @param totalMount
     * @param divideMount
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private void direct(MallConfig mallConfig, Long promoterId, Long fromUserId, BigDecimal totalMount, BigDecimal divideMount)
            throws NoSuchFieldException, IllegalAccessException {
        Users directUser = usersService.getById(promoterId);
        if (directUser == null) {
            return;
        }
        Field field = mallConfig.getClass().getDeclaredField("directRate" + directUser.getLevel());
        //得到所有属性
        field.setAccessible(true);
        //获取属性
        BigDecimal value = (BigDecimal) field.get(mallConfig);
        // 本次收益
        BigDecimal profit = divideMount.multiply(value);
        CommissionLog commissionLog = CommissionLog.builder()
                .amount(profit)
                .createTime(new Date())
                .fromUserId(fromUserId)
                .rate(value)
                .totalAmount(totalMount)
                .userId(directUser.getId())
                .type(CommissionLogConstant.DIRECT)
                .build();
        logger.info("用户{}直推{}金额{}", directUser.getLevel(), value, profit);
        directUser.setTeamProfit(directUser.getTeamProfit().add(profit));
        directUser.setProfit(directUser.getProfit().add(profit));
        directUser.setBalance(directUser.getBalance().add(profit));
        PayLog payLog = PayLog.builder()
                .realAmount(profit).payType(PayTypeConstant.BALANCE_PAY)
                .amount(profit).userId(directUser.getId())
                .extend1("DIR"+ UUID.randomUUID().toString()).createTime(new Date()).status(PayConstant.SUCCESS_PAY)
                .source(PayResourceConstant.DIRECT.getType()).build();
        payLogService.save(payLog);
        usersService.updateById(directUser);
        commissionLogService.save(commissionLog);
    }


}
