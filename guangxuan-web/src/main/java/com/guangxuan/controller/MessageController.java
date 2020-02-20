package com.guangxuan.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guangxuan.dto.Result;
import com.guangxuan.model.Message;
import com.guangxuan.service.MessageService;
import com.guangxuan.shiro.ThreadLocalCurrentUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhuolin
 * @since 2019-11-25
 */
@RestController
@RequestMapping("/message")
@Api(value = "用户消息controller", tags = {"用户消息接口"})
public class MessageController {

    @Resource
    private MessageService messageService;

    @ApiOperation(value = "获取未读消息数量")
    @GetMapping("/getUnreadMessageCount")
    public Result<?> getUnreadMessageCount() {
        return Result.success(messageService.getUnreadMessageCount(ThreadLocalCurrentUser.getUsers().getId()), null);
    }

    @ApiOperation(value = "获取消息内容并置为已读")
    @GetMapping("/getMessageInfo")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "信息id", required = true, dataType = "int", defaultValue = "1")
    })
    public Result<?> getMessageInfo(Long id) {
        return Result.success(messageService.getMessageInfo(id, ThreadLocalCurrentUser.getUsers().getId()), null);
    }

    @ApiOperation(value = "分页获取消息信息")
    @GetMapping("/getMessageList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "size", value = "每页数据数", required = true, dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "status", value = "状态", required = true, dataType = "int")
    })
    public Result<?> getMessageInfo(Integer page, Integer size, Boolean status) {
        return Result.success(messageService.page(new Page<>(page, size),
                new LambdaQueryWrapper<Message>()
                        .eq(Message::getToUserId, ThreadLocalCurrentUser.getUsers().getId())
                        .eq(status != null, Message::getStatus, status).orderByDesc(Message::getCreateTime)), null);
    }

}

