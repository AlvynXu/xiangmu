package com.guangxuan.service.impl;

import com.guangxuan.config.FileStorageProperties;
import com.guangxuan.enumration.BusinessFailEnum;
import com.guangxuan.exception.BusinessException;
import com.guangxuan.service.IStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * @author deofly
 * @since 2019-03-30
 */
@Service
public class StorageServiceImpl implements IStorageService {

    private final Path fileStorageLocation;

    private static final String uploadDir = "static/";

    @Autowired
    public StorageServiceImpl(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getResourcePath())
                .resolve(uploadDir).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(BusinessFailEnum.NOT_GET_DATA);
        }
    }

    @Override
    public String storeFile(MultipartFile file, String dir) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        int pos = fileName.lastIndexOf(".");
        String suffix = "";
        if (pos != -1) {
            suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        fileName = UUID.randomUUID().toString() + "." + suffix;

        try {
            Path targetDir = this.fileStorageLocation.resolve(dir);
            if (!Files.exists(targetDir)) {
                Files.createDirectories(targetDir);
            }
            Path targetLocation = targetDir.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(BusinessFailEnum.NOT_GET_DATA);
        }
    }

    @Override
    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                return null;
            }
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
