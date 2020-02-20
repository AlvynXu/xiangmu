//package com.guangxuan.vo.mall;
//
//import com.easecoding.app.gugu.domain.MallMpUserDO;
//import com.easecoding.app.gugu.domain.MemberDO;
//import com.fasterxml.jackson.annotation.JsonInclude;
//import lombok.Data;
//
//import java.io.Serializable;
//import java.math.BigDecimal;
//
///**
// * @author deofly
// * @since 2019-05-05
// */
//@Data
//@JsonInclude(JsonInclude.Include.NON_NULL)
//public class UserInfo implements Serializable {
//
//    private static final long serialVersionUID = -5405287007437288539L;
//
//    private String token;
//
//    private String nickname;
//
//    private String avatarUrl;
//
//    private String phone;
//
//    private String inviteCode;
//
//    private BigDecimal balance;
//
//    private BigDecimal eggs;
//
//    private Integer villages;
//
//    private Integer streets;
//
//    private Integer friendCount;
//
//    private Integer teamCount;
//
//    private BigDecimal totalPayMoney;
//
//    private Integer level;
//
//    private Integer remainVillagesToBuy;
//
//    private Integer remainStreetsToBuy;
//
//    public UserInfo() {}
//
//    public UserInfo(MallMpUserDO user) {
//        if (user == null || user.getUser() == null) {
//            return;
//        }
//
//        MemberDO member = user.getUser();
//
//        this.token = user.getToken();
//        this.avatarUrl = user.getAvatar();
//        this.nickname = user.getNick();
//        this.phone = member.getPhoneNumber();
//        this.balance = member.getMoney();
//        this.eggs = member.getEggCount();
//        this.villages = member.getVillageCount();
//        this.streets = member.getStreetCount();
//        this.inviteCode = member.getInviteCode();
//        this.friendCount = member.getFriendCount();
//        this.teamCount = member.getTeamCount();
//
//        this.totalPayMoney = member.getTotalPayMoney();
//        this.level = member.getLevel();
//
//        this.remainVillagesToBuy = member.getRemainVillagesToBuy();
//        this.remainStreetsToBuy = 0;
//    }
//}
