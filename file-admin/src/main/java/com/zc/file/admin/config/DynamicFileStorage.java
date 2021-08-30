package com.zc.file.admin.config;

import com.zc.file.FileService;
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
    private FileService fileService;

    public void init() {
        log.info("开始加载动态文件源 begin");
        //数据库获取动态文件源配置
        final List<ZFileConfig> fileSourceConfs = zFileConfigService.findFileSourceConfs();
        //获取配置文件中文件源配置
        CopyOnWriteArrayList<FileStorage> list = fileService.getFileStorageList();
        if (fileSourceConfs == null && list.size() == 0) {
            throw new RuntimeException("加载动态文件源失败，请在配置文件或数据库添加文件源配置！");
        }
        //标识是否有默认配置
        boolean defaultConfig = list != null && list.size() > 0;
        if (fileSourceConfs != null && fileSourceConfs.size() > 0 && list != null) {
            fileSourceConfs.forEach(config ->{
                //如果配置文件中和动态源冲突,使用动态源
                if (defaultConfig) {
                    boolean conflict = list.stream().allMatch(a -> a.getClient().equals(config.getClientName()));
                    if (conflict) {
                        log.warn("检测到配置文件中文件源{}和动态源冲突,使用动态源",config.getClientName());
                        list.removeIf(a -> a.getClient().equals(config.getClientName()));
                    }
                }
                //如果未启用
                if (config.getStatus() == 0) {
                    return;
                }
                switch (config.getClientType()) {
                    case ZcFileCons.SourceType.SOURCE_TYPE_LOCAL:
                        log.info("动态加载文件源  {}--{}",config.getClientType(),config.getClientName());
                        LocalFileStorage localApiClient = new LocalFileStorage();
                        localApiClient.setClient(config.getClientName());
                        localApiClient.setBasePath(config.getBasePath());
                        localApiClient.setDomain(config.getDomain());
                        list.add(localApiClient);
                        break;
                    case ZcFileCons.SourceType.SOURCE_TYPE_MINIO:
                        log.info("动态加载文件源  {}--{}",config.getClientType(),config.getClientName());
                        MinIOFileStorage storage = new MinIOFileStorage();
                        storage.setClient(config.getClientName());
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
                        aliyunStorage.setClient(config.getClientName());
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
                        baiduStorage.setClient(config.getClientName());
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
                        awsS3FileStorage.setClient(config.getClientName());
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
                        huaweiObsFileStorage.setClient(config.getClientName());
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
                        qiniuKodoFileStorage.setClient(config.getClientName());
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
                        tencentCosFileStorage.setClient(config.getClientName());
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
                        upyunUssFileStorage.setClient(config.getClientName());
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
        }
        log.info("加载动态文件源结束 end");
    }
    public void remove(String platform){
//        for (LocalFileStorage localFileStorage : list) {
//
//        }
    }
}
