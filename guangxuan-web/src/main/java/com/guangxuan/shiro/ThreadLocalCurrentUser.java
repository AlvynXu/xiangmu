package com.guangxuan.shiro;

import com.guangxuan.model.Users;

/**
 * @author zhuolin
 * @Date 2019/11/25
 */
public class ThreadLocalCurrentUser {
    /**
     * 当前用户
     */
    private static ThreadLocal<Users> connThreadLocal = new ThreadLocal<Users>();

    public static Users getUsers() {
        return connThreadLocal.get();
    }

    public static void setUsers(Users users) {
        connThreadLocal.set(users);
    }
}
