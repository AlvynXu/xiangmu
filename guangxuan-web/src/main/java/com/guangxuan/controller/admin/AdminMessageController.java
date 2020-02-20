package com.guangxuan.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guangxuan.config.ApiVersion;
import com.guangxuan.config.ApiVersionConstant;
import com.guangxuan.constant.MessageStatus;
import com.guangxuan.constant.MessageStatusConstant;
import com.guangxuan.controller.pay.utils.StringUtils;
import com.guangxuan.enumration.BusinessFailEnum;
import com.guangxuan.enumration.MessageTypeEnum;
import com.guangxuan.model.Message;
import com.guangxuan.model.PublishMessageInfo;
import com.guangxuan.model.Users;
import com.guangxuan.service.MessageService;
import com.guangxuan.service.PublishMessageInfoService;
import com.guangxuan.service.UsersService;
import com.guangxuan.shiro.util.PageInfoUtils;
import com.guangxuan.vo.BaseResponse;
import com.guangxuan.vo.ErrorResponse;
import com.guangxuan.vo.SuccessResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import jdk.nashorn.internal.objects.annotations.Getter;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author zhuolin
 * @Date 2019/12/23
 */
@RestController("AdminMessageController")
@RequestMapping("/admin/message")
@Api(value = "消息管理", tags = "消息管理")
public class AdminMessageController {

    @Resource
    private PublishMessageInfoService publishMessageInfoService;

    @Resource
    private MessageService messageService;

    @Resource
    private UsersService usersService;

    @GetMapping("")
    @ApiOperation("获取消息")
    @ApiVersion(group = ApiVersionConstant.FAP_APP102)
    @Transactional(rollbackFor = Exception.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "int"),
            @ApiImplicitParam(name = "size", value = "当前页", required = true, dataType = "int"),
            @ApiImplicitParam(name = "type", value = "类型", required = true, dataType = "int"),
    })
    public BaseResponse publishMessage(Integer page, Integer size, Integer type) {
        if (page == null) {
            return new ErrorResponse(BusinessFailEnum.PAGE_NOT_NULL.getCode(), BusinessFailEnum.PAGE_NOT_NULL.getMessage());
        }
        if (size == null) {
            return new ErrorResponse(BusinessFailEnum.SIZE_NOT_NULL.getCode(), BusinessFailEnum.SIZE_NOT_NULL.getMessage());
        }
        IPage<PublishMessageInfo> publishMessageInfoIPage = publishMessageInfoService.page(new Page<>(page, size),
                new LambdaQueryWrapper<PublishMessageInfo>().eq(type != null, PublishMessageInfo::getType, type));
        return new SuccessResponse(PageInfoUtils.getPageInfo(publishMessageInfoIPage));
    }

    @PostMapping("")
    @ApiOperation("发布消息")
    @ApiVersion(group = ApiVersionConstant.FAP_APP102)
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse publishMessage(@RequestBody @Valid PublishMessageInfo messageInfo) {
        messageInfo.setPublishTime(new Date());
        messageInfo.setStatus(MessageStatusConstant.PUBLISHED);
        publishMessageInfoService.save(messageInfo);
        List<Message> messages = new ArrayList<>();
        List<Users> users = usersService.list();
        MessageTypeEnum messageTypeEnum = MessageTypeEnum.valueOfType(messageInfo.getType());
        for (Users user : users) {
            Message message = new Message();
            message.setStatus(MessageStatus.UNREAD);
            message.setContent(messageInfo.getContent());
            message.setTheme(messageInfo.getTitle());
            message.setSender(messageTypeEnum.getTypeName());
            message.setCreateTime(new Date());
            message.setToUserId(user.getId());
            message.setDeleted(false);
            messages.add(message);
        }
        messageService.saveBatch(messages);
        return new SuccessResponse();
    }

    @GetMapping("getMessageType")
    @ApiOperation("获取消息类型")
    @ApiVersion(group = ApiVersionConstant.FAP_APP102)
    public BaseResponse<List> getMessageType() {
        MessageTypeEnum[] typeEnums = MessageTypeEnum.values();
        List<JSONObject> jsonObjects = new ArrayList<>();
        for (MessageTypeEnum typeEnum : typeEnums) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", typeEnum.getType());
            jsonObject.put("typeName", typeEnum.getTypeName());
            jsonObjects.add(jsonObject);
        }
        return new SuccessResponse(jsonObjects);
    }
}
