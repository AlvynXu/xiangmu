package com.guangxuan.vo.admin.filter;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author deofly
 * @since 2019-06-03
 */
@Data
public class MemberFilter implements Serializable {

    private static final long serialVersionUID = -8260540490302424857L;

    private String phone;

    private String nickname;

    // 最小直推好友数
    private Integer minFriendCount;

    // 最小全网好友数
    private Integer minTeamFriendCount;

    private BigDecimal minBalance;

    // 被邀请码
    private String invitedCode;

    /**
     * 排序类型
     * 0: 默认排序
     * 1: 按直推好友数排序
     * 2: 按全网好友数排序
     * 3: 按余额排序
     */
    private Integer sortType;
}
