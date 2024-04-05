package com.itcz.czword.file.service.impl;

import cn.hutool.core.date.DateUtil;
import com.itcz.czword.common.service.exception.BusinessException;
import com.itcz.czword.file.property.FileUploadProperty;
import com.itcz.czword.file.service.FileService;
import com.itcz.czword.model.enums.ErrorCode;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    @Resource
    private FileUploadProperty fileUploadProperty;
    @Override
    public String upload(MultipartFile multipartFile) {
        try {
            //获取配置文件参数信息
            String bucketName = fileUploadProperty.getBucketName();
            String url = fileUploadProperty.getUrl();
            String accessKey = fileUploadProperty.getAccessKey();
            String secretKey = fileUploadProperty.getSecretKey();
            //获取文件输入流
            InputStream inputStream = multipartFile.getInputStream();
            //获取文件大小
            long size = multipartFile.getSize();
            //获取文件名
            String originalFilename = multipartFile.getOriginalFilename();
            // 设置存储对象名称
            String uuid = UUID.randomUUID().toString().replace("-", "");
            String dateDir = DateUtil.format(new Date(), "yyyyMMdd");
            //20230801/443e1e772bef482c95be28704bec58a901.jpg
            //加个"/" 就会有文件夹的效果
            originalFilename = dateDir + "/"+uuid+originalFilename;
            //创建一个Minio客户端
            MinioClient minioClient =
                    MinioClient.builder()
                            .endpoint(url)
                            .credentials(accessKey, secretKey)
                            .build();

            // 判断桶是否存在
            boolean found =
                    minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) {
                // 如果不存在，那么此时就创建一个新的桶
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            } else {
                // 如果存在打印信息
                System.out.println("Bucket '"+bucketName+"' 已存在.");
            }
            minioClient.putObject(
                    PutObjectArgs.builder().bucket(bucketName).object(originalFilename).stream(
                                    inputStream, size, -1)
                            .build());

            return  url + "/" + bucketName + "/" + originalFilename;
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.FILE_UPLOAD_ERROR);
        }
    }
}
