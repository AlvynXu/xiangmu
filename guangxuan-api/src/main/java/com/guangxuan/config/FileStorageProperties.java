package com.guangxuan.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author deofly
 * @since 2019-03-29
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {

    private String resourcePath;

    private String serverUrl;

}
