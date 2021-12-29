package com.z.file.platform;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.z.file.entity.FileInfo;
import com.z.file.entity.UploadPretreatment;
import com.z.file.exception.FileException;
import io.minio.ErrorCode;
import io.minio.MinioClient;
import io.minio.ObjectStat;
import io.minio.PutObjectOptions;
import io.minio.errors.*;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.function.Consumer;

/**
 * MinIO 存储
 */
@Getter
@Setter
public class MinIOFileStorage implements FileStorage {

    /* 存储平台 */
    private String client;
    /* 存储平台类型 */
    private String clientType;
    private String accessKey;
    private String secretKey;
    private String endPoint;
    private String bucketName;
    private String domain;
    private String basePath;

    public MinioClient getMiClient() {
        try {
            return new MinioClient(endPoint,accessKey,secretKey);
        } catch (InvalidEndpointException | InvalidPortException e) {
            throw new FileException("文件上传失败！platform：" + client,e);
        }
    }

    @Override
    public boolean save(FileInfo fileInfo, UploadPretreatment pre) {
        String newFileKey = basePath + fileInfo.getPath() + fileInfo.getFilename();
        fileInfo.setBasePath(basePath);
        fileInfo.setUrl(domain + newFileKey);

        MinioClient client = getMiClient();
        try {
            PutObjectOptions options = new PutObjectOptions(pre.getFileWrapper().getSize(), -1);
            if (ObjectUtil.isNotNull(pre.getFileWrapper().getContentType())) {
                options.setContentType(pre.getFileWrapper().getContentType());
            }
            client.putObject(bucketName,newFileKey,pre.getFileWrapper().getInputStream(), options);

            byte[] thumbnailBytes = pre.getThumbnailBytes();
            if (thumbnailBytes != null) { //上传缩略图
                String newThFileKey = basePath + fileInfo.getPath() + fileInfo.getThFilename();
                fileInfo.setThUrl(domain + newThFileKey);
                PutObjectOptions thumOptions = new PutObjectOptions(thumbnailBytes.length, -1);
                if (ObjectUtil.isNotNull(pre.getFileWrapper().getContentType())) {
                    thumOptions.setContentType(pre.getFileWrapper().getContentType());
                }
                client.putObject(bucketName,newThFileKey,new ByteArrayInputStream(thumbnailBytes), thumOptions);
            }
            fileInfo.setUploadEndTime(new Date());
            return true;
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidBucketNameException | InvalidKeyException | InvalidResponseException | IOException | NoSuchAlgorithmException | XmlParserException e) {
            try {
                client.removeObject(bucketName,newFileKey);
            } catch (Exception ignored) {
            }
            throw new FileException("文件上传失败！platform：" + client + "，filename：" + fileInfo.getOriginalFilename(),e);
        }
    }

    @Override
    public boolean delete(FileInfo fileInfo) {
        MinioClient client = getMiClient();
        String path = fileInfo.getPath();
        if (StringUtils.isBlank(fileInfo.getPath())) {
            path = "";
        }
        try {
            if (fileInfo.getThFilename() != null) {   //删除缩略图
                client.removeObject(bucketName,fileInfo.getBasePath() + path + fileInfo.getThFilename());
            }
            client.removeObject(bucketName,fileInfo.getBasePath() + path + fileInfo.getFilename());
            return true;
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidBucketNameException | InvalidKeyException | InvalidResponseException | IOException | NoSuchAlgorithmException | XmlParserException e) {
            throw new FileException("文件删除失败！fileInfo：" + fileInfo,e);
        }
    }


    @Override
    public boolean exists(FileInfo fileInfo) {
        MinioClient client = getMiClient();
        try {
            ObjectStat stat = client.statObject(bucketName,fileInfo.getBasePath() + fileInfo.getPath() + fileInfo.getFilename());
            return stat != null && stat.createdTime() != null;
        } catch (ErrorResponseException e) {
            String code = e.errorResponse().errorCode().code();
            if (ErrorCode.RESOURCE_NOT_FOUND.code().equals(code) || ErrorCode.NO_SUCH_OBJECT.code().equals(code)) {
                return false;
            }
            throw new FileException("查询文件是否存在失败！",e);
        } catch (InsufficientDataException | InternalException | InvalidBucketNameException | InvalidKeyException | InvalidResponseException | IOException | NoSuchAlgorithmException | XmlParserException e) {
            throw new FileException("查询文件是否存在失败！",e);
        }
    }

    @Override
    public void download(FileInfo fileInfo,Consumer<InputStream> consumer) {
        MinioClient client = getMiClient();
        try (InputStream in = client.getObject(bucketName,fileInfo.getBasePath() + fileInfo.getPath() + fileInfo.getFilename())) {
            consumer.accept(in);
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidBucketNameException | InvalidKeyException | InvalidResponseException | IOException | NoSuchAlgorithmException | XmlParserException e) {
            throw new FileException("文件下载失败！platform：" + fileInfo,e);
        }
    }

    @Override
    public void downloadTh(FileInfo fileInfo,Consumer<InputStream> consumer) {
        if (StrUtil.isBlank(fileInfo.getThFilename())) {
            throw new FileException("缩略图文件下载失败，文件不存在！fileInfo：" + fileInfo);
        }
        MinioClient client = getMiClient();
        try (InputStream in = client.getObject(bucketName,fileInfo.getBasePath() + fileInfo.getPath() + fileInfo.getThFilename())) {
            consumer.accept(in);
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidBucketNameException | InvalidKeyException | InvalidResponseException | IOException | NoSuchAlgorithmException | XmlParserException e) {
            throw new FileException("缩略图文件下载失败！fileInfo：" + fileInfo,e);
        }

    }
}
