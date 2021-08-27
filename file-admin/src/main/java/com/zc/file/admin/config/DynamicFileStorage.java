package com.zc.file.admin.config;

import com.zc.file.FileStorageService;
import com.zc.file.admin.model.ZFileConfig;
import com.zc.file.admin.service.ZFileConfigService;
import com.zc.file.platform.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 动态存储平台设置
 */
@Slf4j
@Component
public class DynamicFileStorage {
    @Autowired
    private ZFileConfigService zFileConfigService;
    @Autowired
    private FileStorageService fileStorageService;

    public void add(){

    }

    public FileStorageService init() {
        log.info("DynamicFileStorage init ()=============");
        final List<ZFileConfig> fileSourceConfs = zFileConfigService.findFileSourceConfs();
        if (fileSourceConfs == null) {
            throw new RuntimeException("请先添加文件源配置！");
        }
        CopyOnWriteArrayList<FileStorage> list = fileStorageService.getFileStorageList();

        fileSourceConfs.forEach(config ->{
            //如果未启用
            if (config.getStatus() == 0) {
                return;
            }
            switch (config.getClientType()) {
                case ZcFileCons.SourceType.SOURCE_TYPE_LOCAL:
                    log.info("动态加载文件源  {}--{}",config.getClientType(),config.getClientName());
                    LocalFileStorage localApiClient = new LocalFileStorage();
                    localApiClient.setClient(config.getClientType());
                    localApiClient.setBasePath(config.getBasePath());
                    localApiClient.setDomain(config.getDomain());
                    list.add(localApiClient);
                    break;
                case ZcFileCons.SourceType.SOURCE_TYPE_MINIO:
                    log.info("动态加载文件源  {}--{}",config.getClientType(),config.getClientName());
                    MinIOFileStorage storage = new MinIOFileStorage();
                    storage.setClient(config.getClientType());
                    storage.setAccessKey(config.getAccessKey());
                    storage.setSecretKey(config.getSecretKey());
                    storage.setBasePath(config.getBasePath());
                    storage.setBucketName(config.getBucket());
                    storage.setDomain(config.getDomain());
                    storage.setEndPoint(config.getEndpoint());
                    list.add(storage);
                    break;
                case ZcFileCons.SourceType.SOURCE_TYPE_ALIYUN:
                    log.info("动态加载文件源  {}--{}",config.getClientType(),config.getClientName());
                    AliyunOssFileStorage aliyunStorage = new AliyunOssFileStorage();
                    aliyunStorage.setClient(config.getClientType());
                    aliyunStorage.setAccessKey(config.getAccessKey());
                    aliyunStorage.setSecretKey(config.getSecretKey());
                    aliyunStorage.setBasePath(config.getBasePath());
                    aliyunStorage.setBucketName(config.getBucket());
                    aliyunStorage.setDomain(config.getDomain());
                    aliyunStorage.setEndPoint(config.getEndpoint());
                    list.add(aliyunStorage);
                    break;
                case ZcFileCons.SourceType.SOURCE_TYPE_BAIDUYUN:
                    log.info("动态加载文件源  {}--{}",config.getClientType(),config.getClientName());
                    BaiduBosFileStorage baiduStorage = new BaiduBosFileStorage();
                    baiduStorage.setClient(config.getClientType());
                    baiduStorage.setAccessKey(config.getAccessKey());
                    baiduStorage.setSecretKey(config.getSecretKey());
                    baiduStorage.setBasePath(config.getBasePath());
                    baiduStorage.setBucketName(config.getBucket());
                    baiduStorage.setDomain(config.getDomain());
                    baiduStorage.setEndPoint(config.getEndpoint());
                    list.add(baiduStorage);
                    break;
                case ZcFileCons.SourceType.SOURCE_TYPE_AWSS3:
                    log.info("动态加载文件源  {}--{}",config.getClientType(),config.getClientName());
                    AwsS3FileStorage awsS3FileStorage = new AwsS3FileStorage();
                    awsS3FileStorage.setClient(config.getClientType());
                    awsS3FileStorage.setAccessKey(config.getAccessKey());
                    awsS3FileStorage.setSecretKey(config.getSecretKey());
                    awsS3FileStorage.setBasePath(config.getBasePath());
                    awsS3FileStorage.setBucketName(config.getBucket());
                    awsS3FileStorage.setDomain(config.getDomain());
                    awsS3FileStorage.setEndPoint(config.getEndpoint());
                    awsS3FileStorage.setRegion(config.getRegion());
                    list.add(awsS3FileStorage);
                    break;
                case ZcFileCons.SourceType.SOURCE_TYPE_HUAWEIYUN:
                    log.info("动态加载文件源  {}--{}",config.getClientType(),config.getClientName());
                    HuaweiObsFileStorage huaweiObsFileStorage = new HuaweiObsFileStorage();
                    huaweiObsFileStorage.setClient(config.getClientType());
                    huaweiObsFileStorage.setAccessKey(config.getAccessKey());
                    huaweiObsFileStorage.setSecretKey(config.getSecretKey());
                    huaweiObsFileStorage.setBasePath(config.getBasePath());
                    huaweiObsFileStorage.setBucketName(config.getBucket());
                    huaweiObsFileStorage.setDomain(config.getDomain());
                    huaweiObsFileStorage.setEndPoint(config.getEndpoint());
                    list.add(huaweiObsFileStorage);
                    break;
                case ZcFileCons.SourceType.SOURCE_TYPE_QINIUYUN:
                    log.info("动态加载文件源  {}--{}",config.getClientType(),config.getClientName());
                    QiniuKodoFileStorage qiniuKodoFileStorage = new QiniuKodoFileStorage();
                    qiniuKodoFileStorage.setClient(config.getClientType());
                    qiniuKodoFileStorage.setAccessKey(config.getAccessKey());
                    qiniuKodoFileStorage.setSecretKey(config.getSecretKey());
                    qiniuKodoFileStorage.setBasePath(config.getBasePath());
                    qiniuKodoFileStorage.setBucketName(config.getBucket());
                    qiniuKodoFileStorage.setDomain(config.getDomain());
                    list.add(qiniuKodoFileStorage);
                    break;
                case ZcFileCons.SourceType.SOURCE_TYPE_TENGXUNYUN:
                    log.info("动态加载文件源  {}--{}",config.getClientType(),config.getClientName());
                    TencentCosFileStorage tencentCosFileStorage = new TencentCosFileStorage();
                    tencentCosFileStorage.setClient(config.getClientType());
                    tencentCosFileStorage.setSecretId(config.getAccessKey());
                    tencentCosFileStorage.setSecretKey(config.getSecretKey());
                    tencentCosFileStorage.setBasePath(config.getBasePath());
                    tencentCosFileStorage.setBucketName(config.getBucket());
                    tencentCosFileStorage.setDomain(config.getDomain());
                    tencentCosFileStorage.setRegion(config.getRegion());
                    list.add(tencentCosFileStorage);
                    break;
                case ZcFileCons.SourceType.SOURCE_TYPE_YOUPAIYUN:
                    log.info("动态加载文件源  {}--{}",config.getClientType(),config.getClientName());
                    UpyunUssFileStorage upyunUssFileStorage = new UpyunUssFileStorage();
                    upyunUssFileStorage.setClient(config.getClientType());
                    upyunUssFileStorage.setUsername(config.getAccessKey());
                    upyunUssFileStorage.setPassword(config.getSecretKey());
                    upyunUssFileStorage.setBasePath(config.getBasePath());
                    upyunUssFileStorage.setBucketName(config.getBucket());
                    upyunUssFileStorage.setDomain(config.getDomain());
                    list.add(upyunUssFileStorage);
                    break;
                default:
                    break;
            }

        });
        return fileStorageService;
    }
    public void remove(String platform){
//        for (LocalFileStorage localFileStorage : list) {
//
//        }
    }
}
