package com.itcz.czword.file.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "file.upload")
@Component
public class FileUploadProperty {

    private String bucketName;

    private String url;

    private String accessKey;

    private String secretKey;
}
