package com.guangxuan.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.guangxuan.config.ApiVersion;
import com.guangxuan.config.ApiVersionConstant;
import com.guangxuan.constant.ResponseConstant;
import com.guangxuan.model.Admin;
import com.guangxuan.service.AdminService;
import com.guangxuan.util.PasswordUtils;
import com.guangxuan.vo.BaseResponse;
import com.guangxuan.vo.ErrorResponse;
import com.guangxuan.vo.SuccessResponse;
import com.guangxuan.vo.admin.form.LoginForm;
import com.guangxuan.vo.admin.form.UpdatePasswordForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Objects;
import java.util.UUID;

/**
 * @author deofly
 * @since 2019-04-08
 */
@RestController("AdminPassportController")
@RequestMapping("/admin/passport")
@Api(value = "管理员登陆",tags = {"管理员登陆"})
public class AdminPassportController extends BaseController {

    @Autowired
    private AdminService adminService;

    @PostMapping("login")
    @ApiOperation("管理员登陆")
    @ApiVersion(group = ApiVersionConstant.FAP_APP102)
    public BaseResponse loginByUsername(@RequestBody @Valid LoginForm form,
                                        BindingResult result) {
        if (result.hasErrors()) {
            return new ErrorResponse(result.getAllErrors().get(0).getDefaultMessage());
        }

        String username = form.getUsername();
        String password = form.getPassword();

        Admin admin = adminService.getOne(new LambdaQueryWrapper<Admin>().eq(Admin::getUsername,username));
        if (admin == null || admin.getDeleteTime() != null) {
            return new ErrorResponse("用户名或密码错误");
        }

        String salt = admin.getSalt();
        // 验证密码
        if (!Objects.equals(admin.getPassword(), PasswordUtils.createPassword(password, salt))) {
            return new ErrorResponse("用户名或密码错误");
        }

        HttpSession session = request.getSession();
        session.setAttribute("adminId", admin.getId());

        return new SuccessResponse("登录成功");
    }

    @PostMapping("logout")
    @ApiOperation("管理员退出")
    @ApiVersion(group = ApiVersionConstant.FAP_APP102)
    public BaseResponse logout() {
        HttpSession session = request.getSession();
        session.invalidate();

        return new SuccessResponse();
    }

    @GetMapping("profile")
    @ApiOperation("管理员信息")
    @ApiVersion(group = ApiVersionConstant.FAP_APP102)
    public BaseResponse getProfile() {
        Admin admin = getAdmin();
        if (admin == null) {
            return ResponseConstant.SESSION_EXPIRED;
        }

        return new SuccessResponse(admin);
    }

    @PutMapping("password")
    @ApiOperation("管理员修改密码")
    @ApiVersion(group = ApiVersionConstant.FAP_APP102)
    public BaseResponse updatePassword(@RequestBody UpdatePasswordForm form,
                                       BindingResult result) {
        if (result.hasErrors()) {
            return new ErrorResponse(result.getAllErrors().get(0).getDefaultMessage());
        }

        Admin admin = getAdmin();
        if (admin == null) {
            return ResponseConstant.SESSION_EXPIRED;
        }

        String oldPassword = form.getOldPassword();
        String newPassword = form.getNewPassword();
        if (!Objects.equals(PasswordUtils.createPassword(
                oldPassword, admin.getSalt()), admin.getPassword())) {
            return new ErrorResponse("原密码不正确");
        }

        String salt = UUID.randomUUID().toString().replaceAll("-", "");
        admin.setPassword(PasswordUtils.createPassword(form.getNewPassword(), salt));
        admin.setSalt(salt);
        adminService.updateById(admin);

        return new SuccessResponse();
    }
}
