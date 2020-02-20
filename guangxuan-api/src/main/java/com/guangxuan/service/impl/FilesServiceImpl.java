package com.guangxuan.service.impl;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.guangxuan.config.OssProperties;
import com.guangxuan.model.Files;
import com.guangxuan.mapper.FilesMapper;
import com.guangxuan.service.FilesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zhuolin
 * @since 2019-11-27
 */
@Service
@Slf4j
public class FilesServiceImpl extends ServiceImpl<FilesMapper, Files> implements FilesService {


    @Resource
    private OssProperties ossProperties;

    @Override
    public String putObject(String objName, InputStream is) {
        String urlStr = ossProperties.getEndpoint().replaceAll("https?://", "https://" + ossProperties.getBucketName() + ".");
        OSSClient ossClient = null;
        try {
            ossClient = new OSSClient(ossProperties.getEndpoint(), ossProperties.getAccessKeyId(), ossProperties.getAccessKeySecret());
            ossClient.putObject(ossProperties.getBucketName(), objName, is);
        } catch (OSSException oe) {
            log.error("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.", oe);
            return null;
        } catch (ClientException ce) {
            log.error("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.", ce);
            return null;
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }

        return urlStr + "/" + objName;
    }

    @Override
    public String putObject(String objName, File file) {
        String urlStr = ossProperties.getEndpoint().replaceAll("https?://", "https://" + ossProperties.getBucketName() + ".");
        OSSClient ossClient = null;
        try {
            ossClient = new OSSClient(ossProperties.getEndpoint(), ossProperties.getAccessKeyId(), ossProperties.getAccessKeySecret());
            ossClient.putObject(ossProperties.getBucketName(), objName, file);
        } catch (OSSException oe) {
            log.error("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.", oe);
            return null;
        } catch (ClientException ce) {
            log.error("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.", ce);
            return null;
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }

        return urlStr + "/" + objName;
    }
}
