package com.guangxuan.controller;


import com.guangxuan.dto.Result;
import com.guangxuan.model.Feedback;
import com.guangxuan.service.FeedbackService;
import com.guangxuan.shiro.ThreadLocalCurrentUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 意见反馈 前端控制器
 * </p>
 *
 * @author zhuolin
 * @since 2019-12-03
 */
@RestController
@RequestMapping("/feedback")
@Api(value = "意见反馈controller", tags = {"意见反馈接口"})
public class FeedbackController {

    @Resource
    private FeedbackService feedbackService;

    @PostMapping("/saveFeedBack")
    @ApiOperation(value = "保存意见反馈接口")
    private Result<?> saveFeedBack(@RequestBody Feedback feedback){
        return Result.success(feedbackService.saveFeedBack(feedback, ThreadLocalCurrentUser.getUsers().getId()),null);
    }

}

