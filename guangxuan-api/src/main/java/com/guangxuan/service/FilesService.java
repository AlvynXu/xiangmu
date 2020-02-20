package com.guangxuan.service;

import com.guangxuan.model.Files;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.File;
import java.io.InputStream;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author zhuolin
 * @since 2019-11-27
 */
public interface FilesService extends IService<Files> {

    String putObject(String objName, InputStream is);

    String putObject(String objName, File file);
}
