package com.guangxuan.controller;


import com.guangxuan.service.MallItemService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 项目信息 前端控制器
 * </p>
 *
 * @author zhuolin
 * @since 2019-11-25
 */
@RestController
@RequestMapping("/mallItem")
public class MallItemController {

    @Resource
    private MallItemService mallItemService;


}

