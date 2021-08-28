package com.zc.file.platform;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.zc.file.FileInfo;
import com.zc.file.UploadPretreatment;
import com.zc.file.exception.FileStorageRuntimeException;
import com.upyun.RestManager;
import com.upyun.UpException;
import lombok.Getter;
import lombok.Setter;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Consumer;

/**
 * 又拍云 USS 存储
 */
@Getter
@Setter
public class UpyunUssFileStorage implements FileStorage {

    /* 存储平台 */
    private String client;
    private String username;
    private String password;
    private String bucketName;
    private String domain;
    private String basePath;

    public RestManager getManager() {
        return new RestManager(bucketName,username,password);
    }

    @Override
    public boolean save(FileInfo fileInfo,UploadPretreatment pre) {


        String newFileKey = basePath + fileInfo.getPath() + fileInfo.getFilename();
        fileInfo.setBasePath(basePath);
        fileInfo.setUrl(domain + newFileKey);

        RestManager manager = getManager();
        try {
            try (Response result = manager.writeFile(newFileKey,pre.getFileWrapper().getInputStream(),null)) {
                if (!result.isSuccessful()) {
                    throw new UpException(result.toString());
                }
            }

            byte[] thumbnailBytes = pre.getThumbnailBytes();
            if (thumbnailBytes != null) { //上传缩略图
                String newThFileKey = basePath + fileInfo.getPath() + fileInfo.getThFilename();
                fileInfo.setThUrl(domain + newThFileKey);

                Response thResult = manager.writeFile(newThFileKey,new ByteArrayInputStream(thumbnailBytes),null);
                if (!thResult.isSuccessful()) {
                    throw new UpException(thResult.toString());
                }
            }
            fileInfo.setUploadEndTime(new Date());
            return true;
        } catch (IOException | UpException e) {
            try {
                manager.deleteFile(newFileKey,null).close();
            } catch (IOException | UpException ignored) {
            }
            throw new FileStorageRuntimeException("文件上传失败！platform：" + client + "，filename：" + fileInfo.getOriginalFilename(),e);
        }
    }

    @Override
    public boolean delete(FileInfo fileInfo) {
        RestManager manager = getManager();
        String file = fileInfo.getBasePath() + fileInfo.getPath() + fileInfo.getFilename();
        String thFile = fileInfo.getBasePath() + fileInfo.getPath() + fileInfo.getThFilename();

        try (Response ignored = fileInfo.getThFilename() != null ? manager.deleteFile(thFile,null) : null;
             Response response = manager.deleteFile(file,null)) {
            return response.isSuccessful();
        } catch (IOException | UpException e) {
            throw new FileStorageRuntimeException("文件删除失败！fileInfo：" + fileInfo,e);
        }
    }

    @Override
    public boolean exists(FileInfo fileInfo) {
        try (Response response = getManager().getFileInfo(fileInfo.getBasePath() + fileInfo.getPath() + fileInfo.getFilename())) {
            return StrUtil.isNotBlank(response.header("x-upyun-file-size"));
        } catch (IOException | UpException e) {
            throw new FileStorageRuntimeException("判断文件是否存在失败！fileInfo：" + fileInfo,e);
        }
    }

    @Override
    public void download(FileInfo fileInfo,Consumer<InputStream> consumer) {
        try (Response response = getManager().readFile(fileInfo.getBasePath() + fileInfo.getPath() + fileInfo.getFilename());
             ResponseBody body = response.body();
             InputStream in = body == null ? null : body.byteStream()) {
            if (body == null) {
                throw new FileStorageRuntimeException("文件下载失败，结果为 null ！fileInfo：" + fileInfo);
            }
            if (!response.isSuccessful()) {
                throw new UpException(IoUtil.read(in,StandardCharsets.UTF_8));
            }
            consumer.accept(in);
        } catch (IOException | UpException e) {
            throw new FileStorageRuntimeException("文件下载失败！fileInfo：" + fileInfo,e);
        }
    }

    @Override
    public void downloadTh(FileInfo fileInfo,Consumer<InputStream> consumer) {
        if (StrUtil.isBlank(fileInfo.getThFilename())) {
            throw new FileStorageRuntimeException("缩略图文件下载失败，文件不存在！fileInfo：" + fileInfo);
        }
        try (Response response = getManager().readFile(fileInfo.getBasePath() + fileInfo.getPath() + fileInfo.getThFilename());
             ResponseBody body = response.body();
             InputStream in = body == null ? null : body.byteStream()) {
            if (body == null) {
                throw new FileStorageRuntimeException("缩略图文件下载失败，结果为 null ！fileInfo：" + fileInfo);
            }
            if (!response.isSuccessful()) {
                throw new UpException(IoUtil.read(in,StandardCharsets.UTF_8));
            }
            consumer.accept(in);
        } catch (IOException | UpException e) {
            throw new FileStorageRuntimeException("缩略图文件下载失败！fileInfo：" + fileInfo,e);
        }
    }
}
