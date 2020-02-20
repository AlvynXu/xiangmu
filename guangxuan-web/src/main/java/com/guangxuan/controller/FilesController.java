package com.guangxuan.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.guangxuan.dto.Result;
import com.guangxuan.model.AppVersion;
import com.guangxuan.service.AppVersionService;
import com.guangxuan.service.FilesService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zhuolin
 * @since 2019-11-27
 */
@RestController
@RequestMapping("/files")
public class FilesController {

    @Resource
    private FilesService filesService;

    @Resource
    private AppVersionService appVersionService;

    @GetMapping("/downloadIos")
    public Result<?> downloadIos(Integer type) {
        if (type == 0) {
            return Result.success(appVersionService.getOne(new LambdaQueryWrapper<AppVersion>()
                    .eq(AppVersion::getEnable, true).eq(AppVersion::getType, 0)).getDownloadPth(), null);
        } else {
            return Result.success(appVersionService.getOne(new LambdaQueryWrapper<AppVersion>()
                    .eq(AppVersion::getEnable, true).eq(AppVersion::getType, 1)).getDownloadPth(), null);
        }
    }


}

