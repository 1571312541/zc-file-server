package com.z.file.platform;

import cn.hutool.core.util.StrUtil;
import com.z.file.entity.FileInfo;
import com.z.file.entity.UploadPretreatment;
import com.z.file.exception.FileException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.function.Consumer;

/**
 * AWS S3 存储
 */
@Getter
@Setter
public class AwsS3FileStorage implements FileStorage {

    /* 存储平台 */
    private String client;
    /* 存储平台类型 */
    private String clientType;
    private String accessKey;
    private String secretKey;
    private String region;
    private String endPoint;
    private String bucketName;
    private String domain;
    private String basePath;

    public AmazonS3 getAmazonS3() {
        AmazonS3ClientBuilder builder = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey,secretKey)));
        if (StrUtil.isNotBlank(endPoint)) {
            builder.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint,region));
        } else if (StrUtil.isNotBlank(region)) {
            builder.withRegion(region);
        }
        return builder.build();
    }

    /**
     * 关闭
     */
    public void shutdown(AmazonS3 s3) {
        if (s3 != null) {
            s3.shutdown();
        }
    }

    @Override
    public boolean save(FileInfo fileInfo, UploadPretreatment pre) {
        String newFileKey = basePath + fileInfo.getPath() + fileInfo.getFilename();
        fileInfo.setBasePath(basePath);
        fileInfo.setUrl(domain + newFileKey);

        AmazonS3 s3 = getAmazonS3();
        try {
            s3.putObject(bucketName,newFileKey,pre.getFileWrapper().getInputStream(),null);

            byte[] thumbnailBytes = pre.getThumbnailBytes();
            if (thumbnailBytes != null) { //上传缩略图
                String newThFileKey = basePath + fileInfo.getPath() + fileInfo.getThFilename();
                fileInfo.setThUrl(domain + newThFileKey);
                s3.putObject(bucketName,newThFileKey,new ByteArrayInputStream(thumbnailBytes),null);
            }
            fileInfo.setUploadEndTime(new Date());
            return true;
        } catch (IOException e) {
            s3.deleteObject(bucketName,newFileKey);
            throw new FileException("文件上传失败！platform：" + client + "，filename：" + fileInfo.getOriginalFilename(),e);
        } finally {
            shutdown(s3);
        }
    }

    @Override
    public boolean delete(FileInfo fileInfo) {
        AmazonS3 oss = getAmazonS3();
        String path = fileInfo.getPath();
        if (StringUtils.isBlank(fileInfo.getPath())) {
            path = "";
        }
        try {
            if (fileInfo.getThFilename() != null) {   //删除缩略图
                oss.deleteObject(bucketName,fileInfo.getBasePath() + path + fileInfo.getThFilename());
            }
            oss.deleteObject(bucketName,fileInfo.getBasePath() + path + fileInfo.getFilename());
            return true;
        } finally {
            shutdown(oss);
        }
    }


    @Override
    public boolean exists(FileInfo fileInfo) {
        AmazonS3 s3 = getAmazonS3();
        try {
            return s3.doesObjectExist(bucketName,fileInfo.getBasePath() + fileInfo.getPath() + fileInfo.getFilename());
        } finally {
            shutdown(s3);
        }
    }

    @Override
    public void download(FileInfo fileInfo,Consumer<InputStream> consumer) {
        AmazonS3 s3 = getAmazonS3();
        try {
            S3Object object = s3.getObject(bucketName,fileInfo.getBasePath() + fileInfo.getPath() + fileInfo.getFilename());
            try (InputStream in = object.getObjectContent()) {
                consumer.accept(in);
            } catch (IOException e) {
                throw new FileException("文件下载失败！platform：" + fileInfo,e);
            }
        } finally {
            shutdown(s3);
        }
    }

    @Override
    public void downloadTh(FileInfo fileInfo,Consumer<InputStream> consumer) {
        if (StrUtil.isBlank(fileInfo.getThFilename())) {
            throw new FileException("缩略图文件下载失败，文件不存在！fileInfo：" + fileInfo);
        }
        AmazonS3 s3 = getAmazonS3();
        try {
            S3Object object = s3.getObject(bucketName,fileInfo.getBasePath() + fileInfo.getPath() + fileInfo.getThFilename());
            try (InputStream in = object.getObjectContent()) {
                consumer.accept(in);
            } catch (IOException e) {
                throw new FileException("缩略图文件下载失败！fileInfo：" + fileInfo,e);
            }
        } finally {
            shutdown(s3);
        }
    }
}
