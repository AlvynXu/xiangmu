//package com.guangxuan.vo.admin;
//
//import com.easecoding.app.gugu.domain.MallMpUserDO;
//import com.easecoding.app.gugu.domain.MemberDO;
//import com.easecoding.app.gugu.utils.StringUtils;
//import com.fasterxml.jackson.annotation.JsonFormat;
//import com.fasterxml.jackson.annotation.JsonInclude;
//import lombok.Data;
//import org.springframework.util.CollectionUtils;
//
//import java.io.Serializable;
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.Comparator;
//import java.util.List;
//import java.util.stream.Collectors;
//
///**
// * @author deofly
// * @since 2019-06-03
// */
//@Data
//@JsonInclude(JsonInclude.Include.NON_NULL)
//public class MemberVO implements Serializable {
//
//    private static final long serialVersionUID = 4816816147868052235L;
//
//    private Integer id;
//
//    private String nickname;
//
//    private String avatarUrl;
//
//    private String phone;
//
//    // 邀请人手机号
//    private String inviterPhone;
//
//    // 邀请码
//    private String inviteCode;
//
//    // 余额
//    private BigDecimal balance;
//
//    // 直推好友数
//    private Integer friendCount;
//
//    // 全网好友数
//    private Integer teamCount;
//
//    // 鸡蛋数
//    private BigDecimal eggCount;
//
//    // 购买鸡蛋数
//    private Integer buyEggCount;
//
//    // 是否违规
//    private boolean isViolation;
//
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    private LocalDateTime createTime;
//
//    public MemberVO() {}
//
//    public MemberVO(MemberDO member) {
//        if (member == null) {
//            return;
//        }
//
//        this.id = member.getId();
//
//        List<MallMpUserDO> users = member.getMallMpUsers();
//        MallMpUserDO user =  CollectionUtils.isEmpty(users) ? null : users
//                .stream()
//                .sorted(Comparator.comparing(MallMpUserDO::getCreateTime))
//                .collect(Collectors.toList())
//                .get(users.size() - 1);
//
//        if (StringUtils.isNotEmpty(member.getAvatarUrl())) {
//            this.avatarUrl = member.getAvatarUrl();
//        } else if (user != null && StringUtils.isNotEmpty(user.getAvatar())) {
//            this.avatarUrl = user.getAvatar();
//        }
//
//        if (StringUtils.isNotEmpty(member.getNickname())) {
//            this.nickname = member.getNickname();
//        } else if (user != null && StringUtils.isNotEmpty(user.getNick())) {
//            this.nickname = user.getNick();
//        }
//
//        this.balance = member.getMoney();
//        this.eggCount = member.getEggCount();
//        this.buyEggCount = member.getBuyEggCount();
//
//        this.friendCount = member.getFriendCount();
//        this.teamCount = member.getTeamCount();
//
//        this.inviteCode = member.getInviteCode();
//        if (member.getInviter() != null) {
//            this.inviterPhone = member.getInviter().getPhoneNumber();
//        }
//
//        this.phone = member.getPhoneNumber();
//        this.isViolation = member.getIsViolation();
//
//        this.createTime = member.getCreateTime();
//    }
//}
