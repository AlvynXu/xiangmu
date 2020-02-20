package com.guangxuan.constant;

/**
 * @author deofly
 * @since 2019-06-12
 */
public class AdminRole {

    public static final int SUPER_ADMIN = 1;

    public static final int NORMAL_ADMIN = 10;

    public static boolean isRoleValid(int roleId) {
        return roleId == 1
                || roleId == 10;
    }
}
