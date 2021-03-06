package com.z.file.platform;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.z.file.entity.FileInfo;
import com.z.file.entity.UploadPretreatment;
import com.z.file.exception.FileException;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.function.Consumer;

/**
 * 本地文件存储
 */
@Getter
@Setter
public class LocalFileStorage implements FileStorage {

    /* 本地存储路径*/
    private String basePath;
    /* 存储平台 */
    private String client;
    /* 存储平台类型 */
    private String clientType;
    /* 访问域名 */
    private String domain;

    @Override
    public boolean save(FileInfo fileInfo, UploadPretreatment pre) {
        String path = fileInfo.getPath();

        File newFile = FileUtil.touch(basePath + path,fileInfo.getFilename());
        fileInfo.setBasePath(basePath);
        if (StringUtils.isBlank(domain)) {
            domain = "";
        }
        fileInfo.setUrl(domain + path + fileInfo.getFilename());

        try {
            pre.getFileWrapper().transferTo(newFile);

            byte[] thumbnailBytes = pre.getThumbnailBytes();
            if (thumbnailBytes != null) { //上传缩略图
                fileInfo.setThUrl(domain + path + fileInfo.getThFilename());
                FileUtil.writeBytes(thumbnailBytes,basePath + path + fileInfo.getThFilename());
            }
            fileInfo.setUploadEndTime(new Date());
            return true;
        } catch (IOException e) {
            FileUtil.del(newFile);
            throw new FileException("文件上传失败！platform：" + client + "，filename：" + fileInfo.getOriginalFilename(),e);
        }
    }

    @Override
    public boolean delete(FileInfo fileInfo) {
        String path = fileInfo.getPath();
        if (StringUtils.isBlank(fileInfo.getPath())) {
            path = "";
        }
        //删除缩略图
        if (fileInfo.getThFilename() != null) {
            FileUtil.del(new File(fileInfo.getBasePath() + path,fileInfo.getThFilename()));
        }
        // 删除
        return FileUtil.del(new File(fileInfo.getBasePath() + path,fileInfo.getFilename()));
    }


    @Override
    public boolean exists(FileInfo fileInfo) {
        return new File(fileInfo.getBasePath() + fileInfo.getPath(),fileInfo.getFilename()).exists();
    }

    @Override
    public void download(FileInfo fileInfo,Consumer<InputStream> consumer) {
        try (InputStream in = FileUtil.getInputStream(fileInfo.getBasePath() + fileInfo.getPath() + fileInfo.getFilename())) {
            consumer.accept(in);
        } catch (IOException e) {
            throw new FileException("文件下载失败！platform：" + fileInfo,e);
        }
    }

    @Override
    public void downloadTh(FileInfo fileInfo,Consumer<InputStream> consumer) {
        if (StrUtil.isBlank(fileInfo.getThFilename())) {
            throw new FileException("缩略图文件下载失败，文件不存在！fileInfo：" + fileInfo);
        }
        try (InputStream in = FileUtil.getInputStream(fileInfo.getBasePath() + fileInfo.getPath() + fileInfo.getThFilename())) {
            consumer.accept(in);
        } catch (IOException e) {
            throw new FileException("缩略图文件下载失败！fileInfo：" + fileInfo,e);
        }
    }
}
