//package com.guangxuan.vo.mall;
//
//import com.easecoding.app.gugu.domain.MallMpUserDO;
//import com.easecoding.app.gugu.domain.MemberDO;
//import com.easecoding.app.gugu.utils.StringUtils;
//import com.fasterxml.jackson.annotation.JsonFormat;
//import lombok.Data;
//import org.springframework.util.CollectionUtils;
//
//import java.io.Serializable;
//import java.time.LocalDateTime;
//import java.util.Comparator;
//import java.util.List;
//import java.util.stream.Collectors;
//
///**
// * @author deofly
// * @since 2019-05-30
// */
//@Data
//public class FriendVO implements Serializable {
//
//    private static final long serialVersionUID = 6588648368639504866L;
//
//    private String avatarUrl;
//
//    private String nickname;
//
//    private Integer friendCount;
//
//    private Integer teamCount;
//
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    private LocalDateTime createTime;
//
//    public FriendVO() {}
//
//    public FriendVO(MemberDO member) {
//        if (member == null) {
//            return;
//        }
//
//        List<MallMpUserDO> users = member.getMallMpUsers();
//        MallMpUserDO mallMpUser = CollectionUtils.isEmpty(users) ? null : users
//                .stream()
//                .sorted(Comparator.comparing(MallMpUserDO::getCreateTime))
//                .collect(Collectors.toList())
//                .get(users.size() - 1);
//
//        if (StringUtils.isNotEmpty(member.getAvatarUrl())) {
//            this.avatarUrl = member.getAvatarUrl();
//        } else if (mallMpUser != null
//                && StringUtils.isNotEmpty(mallMpUser.getAvatar())) {
//            this.avatarUrl = mallMpUser.getAvatar();
//        }
//
//        if (StringUtils.isNotEmpty(member.getNickname())) {
//            this.nickname = member.getNickname();
//        } else if (mallMpUser != null
//                && StringUtils.isNotEmpty(mallMpUser.getNick())) {
//            this.nickname = mallMpUser.getNick();
//        }
//
//        this.friendCount = member.getFriendCount();
//        this.teamCount = member.getTeamCount();
//
//        this.createTime = member.getCreateTime();
//    }
//}
