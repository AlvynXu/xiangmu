package com.guangxuan.controller.admin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageInfo;
import com.guangxuan.config.ApiVersion;
import com.guangxuan.config.ApiVersionConstant;
import com.guangxuan.dto.Result;
import com.guangxuan.dto.domain.UserDTO;
import com.guangxuan.enumration.BusinessFailEnum;
import com.guangxuan.enumration.LevelEnum;
import com.guangxuan.model.Users;
import com.guangxuan.service.UsersService;
import com.guangxuan.shiro.ThreadLocalCurrentUser;
import com.guangxuan.shiro.util.PageInfoUtils;
import com.guangxuan.vo.BaseResponse;
import com.guangxuan.vo.ErrorResponse;
import com.guangxuan.vo.SuccessResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @author deofly
 * @since 2019-06-03
 */
@RestController("AdminMemberController")
@RequestMapping("/admin/members")
@Api(value = "用户管理", tags = "用户管理")
public class MemberController extends BaseController {

    @Autowired
    private UsersService usersService;

    @GetMapping("")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "int"),
            @ApiImplicitParam(name = "size", value = "数据条数", required = true, dataType = "int"),
            @ApiImplicitParam(name = "phone", value = "手机号", dataType = "String"),
            @ApiImplicitParam(name = "level", value = "用户等级", dataType = "int"),
    })
    @ApiOperation("分页获取用户信息")
    @ApiVersion(group = ApiVersionConstant.FAP_APP102)
    public BaseResponse<PageInfo<UserDTO>> getMemberList(Integer page, Integer size, String phone, Integer level) {
        if (page == null) {
            return new ErrorResponse(BusinessFailEnum.PAGE_NOT_NULL.getCode(), BusinessFailEnum.PAGE_NOT_NULL.getMessage());
        }
        if (size == null) {
            return new ErrorResponse(BusinessFailEnum.SIZE_NOT_NULL.getCode(), BusinessFailEnum.SIZE_NOT_NULL.getMessage());
        }
        IPage<UserDTO> page1 = usersService.getPage(page, size, phone, level);
        return new SuccessResponse(PageInfoUtils.getPageInfo(page1));
    }

//    @GetMapping("getLevel")
//    @ApiOperation("获取用户等级")
//    @ApiVersion(group = ApiVersionConstant.FAP_APP102)
//    public BaseResponse<PageInfo<UserDTO>> getLevel(Integer page, Integer size, String phone, Integer level) {
//        IPage<UserDTO> page1 = usersService.getPage(page, size, phone, level);
//        return new SuccessResponse(PageInfoUtils.getPageInfo(page1));
//    }

    @PostMapping("{id}/ban")
    @ApiOperation("禁用")
    @ApiVersion(group = ApiVersionConstant.FAP_APP102)
    public BaseResponse ban(@PathVariable Integer id) {
        if (id == null) {
            return new ErrorResponse("用户不存在");
        }

        Users member = usersService.getById(id);
        if (member == null) {
            return new ErrorResponse("用户不存在");
        }
        member.setDeleted(true);
        usersService.updateById(member);
        return new SuccessResponse();
    }

    @PostMapping("{id}/activate")
    @ApiOperation("启用")
    @ApiVersion(group = ApiVersionConstant.FAP_APP102)
    public BaseResponse activate(@PathVariable Integer id) {
        if (id == null) {
            return new ErrorResponse("用户不存在");
        }
        Users member = usersService.getById(id);
        if (member == null) {
            return new ErrorResponse("用户不存在");
        }
        member.setDeleted(false);
        usersService.updateById(member);
        return new SuccessResponse();
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    @GetMapping("/getLevelList")
    @ApiOperation(value = "获取用户等级")
    @ApiVersion(group = ApiVersionConstant.FAP_APP102)
    public BaseResponse getLevel() {
        JSONArray jsonArray = new JSONArray();
        for(LevelEnum levelEnum :LevelEnum.values()){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id",levelEnum.getLevel());
            jsonObject.put("name",levelEnum.getLevelName());
            jsonArray.add(jsonObject);
        }
        return new SuccessResponse(jsonArray);
    }
}
