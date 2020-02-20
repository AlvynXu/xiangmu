package com.guangxuan.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author deofly
 * @since 2019-04-26
 */
public interface IStorageService {

    String storeFile(MultipartFile file, String dir);

    Resource loadFileAsResource(String fileName);
}
