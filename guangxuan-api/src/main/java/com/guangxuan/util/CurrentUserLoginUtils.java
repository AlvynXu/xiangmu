package com.guangxuan.util;

import com.guangxuan.enumration.BusinessFailEnum;
import com.guangxuan.exception.BusinessException;
import com.guangxuan.model.Users;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Component
public class CurrentUserLoginUtils {

    @Resource
    private HttpSession httpSession;

    private static HttpSession session;

    @PostConstruct
    public void init() {
        session = this.httpSession;
    }

    public static Users getCurrentUsers() {
        System.out.println(session.getId());
        Users users = (Users) session.getAttribute("users");
        if (users == null) {
            throw new BusinessException(BusinessFailEnum.NOT_GET_CURRENT_USER);
        }
        return users;
    }

}
