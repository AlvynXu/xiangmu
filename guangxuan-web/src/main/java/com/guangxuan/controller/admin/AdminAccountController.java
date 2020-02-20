package com.guangxuan.controller.admin;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.guangxuan.config.ApiVersion;
import com.guangxuan.config.ApiVersionConstant;
import com.guangxuan.constant.AdminRole;
import com.guangxuan.model.Admin;
import com.guangxuan.service.AdminService;
import com.guangxuan.util.PasswordUtils;
import com.guangxuan.vo.BaseResponse;
import com.guangxuan.vo.ErrorResponse;
import com.guangxuan.vo.SuccessResponse;
import com.guangxuan.vo.admin.form.AdminForm;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author deofly
 * @since 2019-06-11
 */
@RestController("AdminAccountController")
@RequestMapping("/admin/accounts")
@Api(value = "账号管理", tags = "账号管理")
public class AdminAccountController extends BaseController {

    @Autowired
    private AdminService adminService;

    @GetMapping("")
    @ApiOperation("获取账号")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "page", defaultValue = "1", dataType = "int", required = true, value = "页码"),
//            @ApiImplicitParam(name = "size", defaultValue = "10", dataType = "size", required = true, value = "数据数")
//    })
    @ApiVersion(group = ApiVersionConstant.FAP_APP102)
    public BaseResponse<List<Admin>> getAccounts() {
        Admin i = getAdmin();
        if (i.getRoleId() != AdminRole.SUPER_ADMIN) {
            return new ErrorResponse("无权操作");
        }

        List<Admin> admins = adminService.list();
        if (CollectionUtils.isEmpty(admins)) {
            return new SuccessResponse();
        }

        return new SuccessResponse(admins
                .stream()
                .filter(t -> t.getDeleteTime() == null)
                .collect(Collectors.toList()));
    }

    @PostMapping("")
    @ApiOperation("新增账号")
    @ApiVersion(group = ApiVersionConstant.FAP_APP102)
    public BaseResponse createAccount(@Valid @RequestBody AdminForm form,
                                      BindingResult result) {
        if (result.hasErrors()) {
            return new ErrorResponse(result.getAllErrors().get(0).getDefaultMessage());
        }

        String username = form.getUsername();
        String password = form.getPassword();
        int roleId = form.getRoleId();

        if (StringUtils.isEmpty(password)) {
            return new ErrorResponse("密码不能为空");
        }

        if (password.length() < 6) {
            return new ErrorResponse("密码长度至少6位");
        }

        if (!AdminRole.isRoleValid(roleId)) {
            return new ErrorResponse("非法操作");
        }

        Admin i = getAdmin();
        if (i.getRoleId() != AdminRole.SUPER_ADMIN) {
            return new ErrorResponse("无权操作");
        }

        Admin admin = adminService.getOne(
                new LambdaQueryWrapper<Admin>().eq(Admin::getUsername, username));
        if (admin != null) {
            return new ErrorResponse("用户名已存在");
        }

        admin = new Admin();
        admin.setUsername(username);
        String salt = UUID.randomUUID().toString().replaceAll("-", "");
        admin.setPassword(PasswordUtils.createPassword(form.getPassword(), salt));
        admin.setSalt(salt);
        admin.setRoleId(roleId);

        adminService.save(admin);

        return new SuccessResponse();
    }

    @PutMapping("")
    @ApiOperation("修改账号")
    @ApiVersion(group = ApiVersionConstant.FAP_APP102)
    public BaseResponse updateAccount(@Valid @RequestBody AdminForm form,
                                      BindingResult result) {
        if (result.hasErrors()) {
            return new ErrorResponse(result.getAllErrors().get(0).getDefaultMessage());
        }

        String username = form.getUsername();
        String password = form.getPassword();
        int roleId = form.getRoleId();

        if (password != null && password.length() < 6) {
            return new ErrorResponse("密码长度至少6位");
        }

        if (!AdminRole.isRoleValid(roleId)) {
            return new ErrorResponse("非法操作");
        }

        Admin i = getAdmin();
        if (i.getRoleId() != AdminRole.SUPER_ADMIN) {
            return new ErrorResponse("无权操作");
        }

        Admin admin = adminService.getOne(
                new LambdaQueryWrapper<Admin>().eq(Admin::getUsername, username));

        if (admin == null) {
            return new ErrorResponse("用户不存在");
        }

        String salt = UUID.randomUUID().toString().replaceAll("-", "");
        admin.setPassword(PasswordUtils.createPassword(form.getPassword(), salt));
        admin.setSalt(salt);
        admin.setRoleId(roleId);

        adminService.updateById(admin);

        return new SuccessResponse();
    }

    @DeleteMapping("{id}")
    @ApiOperation("删除账号")
    @ApiVersion(group = ApiVersionConstant.FAP_APP102)
    public BaseResponse deleteAccount(@PathVariable(required = true) int id) {
        adminService.removeById(id);
        return new SuccessResponse();
    }
}
