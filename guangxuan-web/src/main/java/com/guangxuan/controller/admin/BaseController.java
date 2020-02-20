package com.guangxuan.controller.admin;

import com.guangxuan.model.Admin;
import com.guangxuan.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author deofly
 * @since 2019-04-25
 */
public abstract class BaseController {

    @Autowired
    protected HttpServletRequest request;

    @Autowired
    protected HttpServletResponse response;

    @Autowired
    protected AdminService adminService;

    protected Admin getAdmin() {
        Integer adminId = (Integer) request.getSession().getAttribute("adminId");
        if (adminId == null || adminId == 0) {
            return null;
        }

        return adminService.getById(adminId);
    }
}
