package com.zc.file;

import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.zc.file.aspect.DeleteAspectChain;
import com.zc.file.aspect.ExistsAspectChain;
import com.zc.file.aspect.FileStorageAspect;
import com.zc.file.aspect.UploadAspectChain;
import com.zc.file.exception.FileStorageRuntimeException;
import com.zc.file.platform.FileStorage;
import com.zc.file.recorder.FileRecorder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;


/**
 * 用来处理文件存储，对接多个平台
 */
@Getter
@Setter
public class FileService {

    private FileService self;
    private FileRecorder fileRecorder;
    private CopyOnWriteArrayList<FileStorage> fileStorageList;
    private FileStorageProperties properties;
    private CopyOnWriteArrayList<FileStorageAspect> aspectList;


    /**
     * 获取默认的存储平台
     */
    public FileStorage getFileStorage() {
        return getFileStorage(properties.getDefaultPlatform());
    }

    /**
     * 获取对应的存储平台
     */
    public FileStorage getFileStorage(String platform) {
        for (FileStorage fileStorage : fileStorageList) {
            if (fileStorage.getClient().equals(platform)) {
                return fileStorage;
            }
        }
        return null;
    }

    /**
     * 获取对应的存储平台，如果存储平台不存在则抛出异常
     */
    public FileStorage getFileStorageVerify(FileInfo fileInfo) {
        FileStorage fileStorage = getFileStorage(fileInfo.getClient());
        if (fileStorage == null) {
            throw new FileStorageRuntimeException("没有找到对应的存储平台！");
        }
        return fileStorage;
    }

    /**
     * 上传文件，成功返回文件信息，失败返回 null
     */
    public FileInfo upload(UploadPretreatment pre) {
        MultipartFile file = pre.getFileWrapper();
        if (file == null) {
            throw new FileStorageRuntimeException("文件不允许为 null ！");
        }
        if (pre.getPlatform() == null) {
            throw new FileStorageRuntimeException("platform 不允许为 null ！");
        }

        FileInfo fileInfo = new FileInfo();
        fileInfo.setCreateTime(new Date());
        fileInfo.setUploadStartTime(new Date());
        fileInfo.setSize(file.getSize());
        fileInfo.setOriginalFilename(file.getOriginalFilename());
        fileInfo.setSuffix(FileNameUtil.getSuffix(file.getOriginalFilename()));
        fileInfo.setObjectId(pre.getObjectId());
        fileInfo.setObjectType(pre.getObjectType());
        fileInfo.setPath(pre.getPath());
        fileInfo.setClient(pre.getPlatform());
        if (StrUtil.isNotBlank(pre.getSaveFilename())) {
            fileInfo.setFilename(pre.getSaveFilename());
        } else {
            fileInfo.setFilename(IdUtil.objectId() + (StrUtil.isEmpty(fileInfo.getSuffix()) ? StrUtil.EMPTY : "." + fileInfo.getSuffix()));
        }

        byte[] thumbnailBytes = pre.getThumbnailBytes();
        if (thumbnailBytes != null) {
            fileInfo.setThSize((long) thumbnailBytes.length);
            if (StrUtil.isNotBlank(pre.getSaveThFilename())) {
                fileInfo.setThFilename(pre.getSaveThFilename() + pre.getThumbnailSuffix());
            } else {
                fileInfo.setThFilename(fileInfo.getFilename() + pre.getThumbnailSuffix());
            }
        }

        FileStorage fileStorage = getFileStorage(pre.getPlatform());
        if (fileStorage == null){ throw new FileStorageRuntimeException("没有找到对应的存储平台！");}


        //处理切面
        return new UploadAspectChain(aspectList,(_fileInfo,_pre,_fileStorage,_fileRecorder) -> {
            //真正开始保存
            if (_fileStorage.save(_fileInfo,_pre)) {
                if (_fileRecorder.record(_fileInfo)) {
                    return _fileInfo;
                }
            }
            return null;
        }).next(fileInfo,pre,fileStorage,fileRecorder);
    }

    /**
     * 根据 url 获取 FileInfo
     */
    public FileInfo getFileInfoByUrl(String url) {
        return fileRecorder.getByUrl(url);
    }

    /**
     * 根据 url 删除文件
     */
    public boolean delete(String url) {
        return delete(getFileInfoByUrl(url));
    }

    /**
     * 根据 url 删除文件
     */
    public boolean delete(String url,Predicate<FileInfo> predicate) {
        return delete(getFileInfoByUrl(url),predicate);
    }

    /**
     * 根据条件
     */
    public boolean delete(FileInfo fileInfo) {
        return delete(fileInfo,null);
    }

    /**
     * 根据条件删除文件
     */
    public boolean delete(FileInfo fileInfo,Predicate<FileInfo> predicate) {
        if (fileInfo == null) {
            return true;
        }
        if (predicate != null && !predicate.test(fileInfo)) {
            return false;
        }
        FileStorage fileStorage = getFileStorage(fileInfo.getClient());
        if (fileStorage == null) {
            throw new FileStorageRuntimeException("没有找到对应的存储平台！");
        }

        return new DeleteAspectChain(aspectList,(_fileInfo,_fileStorage,_fileRecorder) -> {
            if (_fileStorage.delete(_fileInfo)) {   //删除文件
                return _fileRecorder.delete(_fileInfo.getUrl());  //删除文件记录
            }
            return false;
        }).next(fileInfo,fileStorage,fileRecorder);
    }

    /**
     * 文件是否存在
     */
    public boolean exists(String url) {
        return exists(getFileInfoByUrl(url));
    }

    /**
     * 文件是否存在
     */
    public boolean exists(FileInfo fileInfo) {
        if (fileInfo == null) {
            return false;
        }
        return new ExistsAspectChain(aspectList,(_fileInfo,_fileStorage) ->
                _fileStorage.exists(_fileInfo)
        ).next(fileInfo,getFileStorageVerify(fileInfo));
    }


    /**
     * 获取文件下载器
     */
    public Downloader download(FileInfo fileInfo) {
        return new Downloader(fileInfo,aspectList,getFileStorageVerify(fileInfo),Downloader.TARGET_FILE);
    }

    /**
     * 获取文件下载器
     */
    public Downloader download(String url) {
        return download(getFileInfoByUrl(url));
    }

    /**
     * 获取缩略图文件下载器
     */
    public Downloader downloadTh(FileInfo fileInfo) {
        return new Downloader(fileInfo,aspectList,getFileStorageVerify(fileInfo),Downloader.TARGET_TH_FILE);
    }

    /**
     * 获取缩略图文件下载器
     */
    public Downloader downloadTh(String url) {
        return downloadTh(getFileInfoByUrl(url));
    }


    /**
     * 创建上传预处理器
     */
    public UploadPretreatment build() {
        UploadPretreatment pre = new UploadPretreatment();
        pre.setFileService(self);
        pre.setPlatform(properties.getDefaultPlatform());
        pre.setThumbnailSuffix(properties.getThumbnailSuffix());
        return pre;
    }

    /**
     * 根据 MultipartFile 创建上传预处理器
     */
    public UploadPretreatment build(MultipartFile file) {
        UploadPretreatment pre = build();
        pre.setFileWrapper(new MultipartFileWrapper(file));
        return pre;
    }

    /**
     * 根据 byte[] 创建上传预处理器，name 为空字符串
     */
    public UploadPretreatment build(byte[] bytes) {
        UploadPretreatment pre = build();
        pre.setFileWrapper(new MultipartFileWrapper(new MockMultipartFile("",bytes)));
        return pre;
    }

    /**
     * 根据 InputStream 创建上传预处理器，originalFilename 为空字符串
     */
    public UploadPretreatment build(InputStream in) {
        try {
            UploadPretreatment pre = build();
            pre.setFileWrapper(new MultipartFileWrapper(new MockMultipartFile("",in)));
            return pre;
        } catch (Exception e) {
            throw new FileStorageRuntimeException("根据 InputStream 创建上传预处理器失败！",e);
        }
    }

    /**
     * 根据 File 创建上传预处理器，originalFilename 为 file 的 name
     */
    public UploadPretreatment build(File file) {
        try {
            UploadPretreatment pre = build();
            pre.setFileWrapper(new MultipartFileWrapper(new MockMultipartFile(file.getName(),file.getName(),null,new FileInputStream(file))));
            return pre;
        } catch (Exception e) {
            throw new FileStorageRuntimeException("根据 File 创建上传预处理器失败！",e);
        }
    }

    /**
     * 根据 URL 创建上传预处理器，originalFilename 为空字符串
     */
    public UploadPretreatment build(URL url) {
        try {
            UploadPretreatment pre = build();
            pre.setFileWrapper(new MultipartFileWrapper(new MockMultipartFile("",url.openStream())));
            return pre;
        } catch (Exception e) {
            throw new FileStorageRuntimeException("根据 URL 创建上传预处理器失败！",e);
        }
    }

    /**
     * 根据 URI 创建上传预处理器，originalFilename 为空字符串
     */
    public UploadPretreatment build(URI uri) {
        try {
            return build(uri.toURL());
        } catch (Exception e) {
            throw new FileStorageRuntimeException("根据 URI 创建上传预处理器失败！",e);
        }
    }

    /**
     * 根据 url 字符串创建上传预处理器，兼容Spring的ClassPath路径、文件路径、HTTP路径等，originalFilename 为空字符串
     */
    public UploadPretreatment build(String url) {
        try {
            return build(URLUtil.url(url));
        } catch (Exception e) {
            throw new FileStorageRuntimeException("根据 url：" + url + " 创建上传预处理器失败！",e);
        }
    }

}
