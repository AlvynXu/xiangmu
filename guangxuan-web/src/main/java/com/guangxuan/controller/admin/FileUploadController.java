package com.guangxuan.controller.admin;

import com.guangxuan.config.ApiVersion;
import com.guangxuan.config.ApiVersionConstant;
import com.guangxuan.config.FileStorageProperties;
import com.guangxuan.controller.pay.utils.StringUtils;
import com.guangxuan.service.FilesService;
import com.guangxuan.service.IStorageService;
import com.guangxuan.vo.BaseResponse;
import com.guangxuan.vo.ErrorResponse;
import com.guangxuan.vo.SuccessResponse;
import com.mysql.cj.x.protobuf.Mysqlx;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * @author deofly
 * @since 2019-03-30
 */
@RestController
@RequestMapping("/admin")
@Api(value = "文件上传controller", tags = {"文件上传接口"})
public class FileUploadController {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    @Autowired
    private IStorageService storageService;

    @javax.annotation.Resource
    private FileStorageProperties fileStorageProperties;

    @Autowired
    private HttpServletRequest request;

    @Resource
    private FilesService filesService;

    @ApiOperation("上传文件")
    @ApiVersion(group = ApiVersionConstant.FAP_APP102)
    @PostMapping("/upload")
    public BaseResponse upload(@RequestParam(value = "file", required = true) MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        InputStream is = file.getInputStream();
        if (originalFilename == null || !originalFilename.contains(".")) {
            return new ErrorResponse("文件后缀不存在");
        }
        String fileName = UUID.randomUUID().toString()
                + originalFilename.substring(originalFilename.lastIndexOf("."));
        String url = filesService.putObject(fileName, is);
        return new SuccessResponse(url);
    }

    @ApiOperation("上传文件并设置名称")
    @ApiVersion(group = ApiVersionConstant.FAP_APP102)
    @PostMapping("/uploadWithName")
    public BaseResponse upload(@RequestParam(value = "file", required = true) MultipartFile file, String name) throws IOException {
        String originalFilename = file.getOriginalFilename();
        InputStream is = file.getInputStream();
        if (StringUtils.isBlank(originalFilename) || !originalFilename.contains(".")) {
            return new ErrorResponse("文件后缀不存在");
        }
        if (StringUtils.isBlank(name) || !name.contains(".")) {
            return new ErrorResponse("文件后缀不存在");
        }
        if(!originalFilename.substring(originalFilename.lastIndexOf(".")).equals(name.substring(name.lastIndexOf(".")))){
            return new ErrorResponse("制定文件和源文件后缀名不一致");
        }
        String url = filesService.putObject(name, is);
        return new SuccessResponse(url);
    }


}
