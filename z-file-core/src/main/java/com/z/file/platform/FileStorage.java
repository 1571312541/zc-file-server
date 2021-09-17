package com.z.file.platform;

import com.z.file.FileInfo;
import com.z.file.UploadPretreatment;

import java.io.InputStream;
import java.util.function.Consumer;

/**
 * 文件存储接口，对应各个平台
 */
public interface FileStorage {

    /**
     * 获取平台
     */
    String getClient();

    /**
     * 设置平台
     */
    void setClient(String client);

    /**
     * 保存文件
     */
    boolean save(FileInfo fileInfo, UploadPretreatment pre);


    /**
     * 删除文件
     */
    boolean delete(FileInfo fileInfo);

    /**
     * 文件是否存在
     */
    boolean exists(FileInfo fileInfo);

    /**
     * 下载文件
     */
    void download(FileInfo fileInfo,Consumer<InputStream> consumer);

    /**
     * 下载缩略图文件
     */
    void downloadTh(FileInfo fileInfo,Consumer<InputStream> consumer);
}
