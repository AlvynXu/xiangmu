package com.guangxuan.controller.admin;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guangxuan.config.ApiVersion;
import com.guangxuan.config.ApiVersionConstant;
import com.guangxuan.controller.pay.utils.StringUtils;
import com.guangxuan.dto.Result;
import com.guangxuan.model.AppVersion;
import com.guangxuan.service.AppVersionService;
import com.guangxuan.service.FilesService;
import com.guangxuan.vo.BaseResponse;
import com.guangxuan.vo.ErrorResponse;
import com.guangxuan.vo.SuccessResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zhuolin
 * @since 2019-12-04
 */
@RestController
@RequestMapping("/admin/appVersion")
@Api(value = "app版本controller", tags = {"app版本接口"})
public class AdminAppVersionController {

    @Resource
    private AppVersionService appVersionService;

    @Resource
    private FilesService filesService;


    @GetMapping("getUseAppVersion")
    @ApiVersion(group = ApiVersionConstant.FAP_APP102)
    public BaseResponse getEnableAppVersion() {
        return new SuccessResponse(appVersionService.list(new LambdaQueryWrapper<AppVersion>().eq(AppVersion::getEnable, true)));
    }

    @PostMapping("upLoadVersion")
    @ApiVersion(group = ApiVersionConstant.FAP_APP102)
    public BaseResponse upLoadVersion(Integer type, String name, MultipartFile file, String version, String description) throws IOException {
        int count = appVersionService.count(new LambdaQueryWrapper<AppVersion>().eq(AppVersion::getType, type).eq(AppVersion::getAppVersion, version));
        if (count > 0) {
            return new ErrorResponse("已存在一个相同的版本");
        }
        String originalFilename = file.getOriginalFilename();
        InputStream is = file.getInputStream();
        if (StringUtils.isBlank(originalFilename) || !originalFilename.contains(".")) {
            return new ErrorResponse("文件后缀不存在");
        }
        if (StringUtils.isBlank(name) || !name.contains(".")) {
            return new ErrorResponse("文件后缀不存在");
        }
        if (!originalFilename.substring(originalFilename.lastIndexOf(".")).equals(name.substring(name.lastIndexOf(".")))) {
            return new ErrorResponse("制定文件和源文件后缀名不一致");
        }
        String url = filesService.putObject(name, is);

        AppVersion appVersion = new AppVersion();
        appVersion.setForceUpdate(false);
        appVersion.setAppVersion(version);
        appVersion.setExt1(name);
        appVersion.setDescription(description);
        appVersion.setDownloadPth(url);
        appVersion.setTime(new Date());
        appVersion.setType(type);
        appVersion.setEnable(false);
        appVersionService.save(appVersion);
        return new SuccessResponse(appVersionService.list(
                new LambdaQueryWrapper<AppVersion>().eq(AppVersion::getEnable, true)));
    }


    @PostMapping("useVersion")
    @ApiVersion(group = ApiVersionConstant.FAP_APP102)
    public BaseResponse useVersion(Long id) {
        AppVersion appVersion = appVersionService.getById(id);
        if (appVersion == null) {
            return new ErrorResponse("未获取制定版本");
        }
        if(appVersion.getEnable()){
            return new ErrorResponse("正在使用该版本，关闭之后无开启版本");
        }
        appVersion.setEnable(true);

        AppVersion appVersion1 = appVersionService.getOne(new LambdaQueryWrapper<AppVersion>()
                .eq(AppVersion::getType, appVersion.getType()).eq(AppVersion::getEnable, true));
        appVersion1.setEnable(false);

        appVersionService.updateById(appVersion1);
        appVersionService.updateById(appVersion);
        return new SuccessResponse();
    }

    @GetMapping("listVersion")
    @ApiVersion(group = ApiVersionConstant.FAP_APP102)
    public BaseResponse listVersion(Integer type, Integer page, Integer size) {
        return new SuccessResponse(appVersionService.page(new Page(page, size), new LambdaQueryWrapper<AppVersion>()
                .eq(AppVersion::getType, type).orderByDesc(AppVersion::getTime)));
    }


}

