package com.zc.file.admin.controller;

import com.zc.file.FileInfo;
import com.zc.file.FileService;
import com.zc.file.UploadPretreatment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;


@RestController
public class FileDetailController {

    @Autowired
    private FileService fileService;

    /**
     * 上传文件，成功返回文件 url
     */
    @PostMapping("/upload")
    public String upload(MultipartFile file, UploadPretreatment pr) {
        FileInfo fileInfo = fileService.build(file)
                .setPlatform(pr.getPlatform())
                .setName(pr.getName())
                .setPath(pr.getPath())
                .setOriginalFilename(pr.getOriginalFilename())
                .setSaveFilename(pr.getSaveFilename())
                .setObjectId(pr.getObjectId())
                .setObjectType(pr.getObjectType())
                .setRemark(pr.getRemark())
                .upload();
        return fileInfo == null ? "上传失败！" : fileInfo.getUrl();
    }
    /**
     * 上传文件，成功返回文件 url
     * 默认originalFilename 为空字符串
     */
    @PostMapping("/uploadStream")
    public String uploadStream(InputStream inputStream, UploadPretreatment pr) {
        FileInfo fileInfo = fileService.build(inputStream)
                .setPlatform(pr.getPlatform())
                .setName(pr.getName())
                .setPath(pr.getPath())
                .setOriginalFilename(pr.getOriginalFilename())
                .setSaveFilename(pr.getSaveFilename())
                .setObjectId(pr.getObjectId())
                .setObjectType(pr.getObjectType())
                .setRemark(pr.getRemark())
                .upload();
        return fileInfo == null ? "上传失败！" : fileInfo.getUrl();
    }
    /**
     * 上传文件，成功返回文件 url
     * 默认originalFilename 为空字符串
     */
    @PostMapping("/uploadFile")
    public String uploadFile(File file, UploadPretreatment pr) {
        FileInfo fileInfo = fileService.build(file)
                .setPlatform(pr.getPlatform())
                .setName(pr.getName())
                .setPath(pr.getPath())
                .setOriginalFilename(pr.getOriginalFilename())
                .setSaveFilename(pr.getSaveFilename())
                .setObjectId(pr.getObjectId())
                .setObjectType(pr.getObjectType())
                .setRemark(pr.getRemark())
                .upload();
        return fileInfo == null ? "上传失败！" : fileInfo.getUrl();
    }

    /**
     * 上传文件，成功返回文件 url
     * 默认originalFilename 为空字符串
     */
    @PostMapping("/uploadUrl")
    public String uploadUrl(String url, UploadPretreatment pr) {
        FileInfo fileInfo = fileService.build(url)
                .setPlatform(pr.getPlatform())
                .setName(pr.getName())
                .setPath(pr.getPath())
                .setOriginalFilename(pr.getOriginalFilename())
                .setSaveFilename(pr.getSaveFilename())
                .setObjectId(pr.getObjectId())
                .setObjectType(pr.getObjectType())
                .setRemark(pr.getRemark())
                .upload();
        return fileInfo == null ? "上传失败！" : fileInfo.getUrl();
    }


    /**
     * 上传图片，成功返回文件信息
     * 图片处理使用的是 https://github.com/coobird/thumbnailator
     */
    @PostMapping("/uploadImg")
    public FileInfo uploadImg(MultipartFile file, UploadPretreatment pr) {
        return fileService.build(file)
                .setPlatform(pr.getPlatform())
                .setName(pr.getName())
                .setPath(pr.getPath())
                .setOriginalFilename(pr.getOriginalFilename())
                .setSaveThFilename(pr.getSaveThFilename())
                .setObjectId(pr.getObjectId())
                .setObjectType(pr.getObjectType())
                .setRemark(pr.getRemark())
                .image(img -> img.size(pr.getImgSize().getImgWidth(), pr.getImgSize().getImgheight()))  //将图片大小调整到 1000*1000
                .thumbnail(th -> th.size(pr.getImgSize().getThImgWidth(),pr.getImgSize().getThImgheight()))  //再生成一张 200*200 的缩略图
                .upload();
    }


    /**
     * 上传图片，成功返回文件信息
     * 图片处理使用的是 https://github.com/coobird/thumbnailator
     */
    @PostMapping("/uploadImgFile")
    public FileInfo uploadImgFile(File file, UploadPretreatment pr) {
        return fileService.build(file)
                .setPlatform(pr.getPlatform())
                .setName(pr.getName())
                .setPath(pr.getPath())
                .setOriginalFilename(pr.getOriginalFilename())
                .setSaveThFilename(pr.getSaveThFilename())
                .setObjectId(pr.getObjectId())
                .setObjectType(pr.getObjectType())
                .setRemark(pr.getRemark())
                .image(img -> img.size(pr.getImgSize().getImgWidth(), pr.getImgSize().getImgheight()))  //将图片大小调整到 1000*1000
                .thumbnail(th -> th.size(pr.getImgSize().getThImgWidth(),pr.getImgSize().getThImgheight()))  //再生成一张 200*200 的缩略图
                .upload();
    }


    /**
     * 上传图片，成功返回文件信息
     * 图片处理使用的是 https://github.com/coobird/thumbnailator
     */
    @PostMapping("/uploadImgStream")
    public FileInfo uploadImgStream(InputStream inputStream, UploadPretreatment pr) {
        return fileService.build(inputStream)
                .setPlatform(pr.getPlatform())
                .setName(pr.getName())
                .setPath(pr.getPath())
                .setOriginalFilename(pr.getOriginalFilename())
                .setSaveThFilename(pr.getSaveThFilename())
                .setObjectId(pr.getObjectId())
                .setObjectType(pr.getObjectType())
                .setRemark(pr.getRemark())
                .image(img -> img.size(pr.getImgSize().getImgWidth(), pr.getImgSize().getImgheight()))  //将图片大小调整到 1000*1000
                .thumbnail(th -> th.size(pr.getImgSize().getThImgWidth(),pr.getImgSize().getThImgheight()))  //再生成一张 200*200 的缩略图
                .upload();
    }


    /**
     * 上传图片，成功返回文件信息
     * 图片处理使用的是 https://github.com/coobird/thumbnailator
     */
    @PostMapping("/uploadImgUrl")
    public FileInfo uploadImgUrl(String url, UploadPretreatment pr) {
        return fileService.build(url)
                .setPlatform(pr.getPlatform())
                .setName(pr.getName())
                .setPath(pr.getPath())
                .setOriginalFilename(pr.getOriginalFilename())
                .setSaveThFilename(pr.getSaveThFilename())
                .setObjectId(pr.getObjectId())
                .setObjectType(pr.getObjectType())
                .setRemark(pr.getRemark())
                .image(img -> img.size(pr.getImgSize().getImgWidth(), pr.getImgSize().getImgheight()))  //将图片大小调整到 1000*1000
                .thumbnail(th -> th.size(pr.getImgSize().getThImgWidth(),pr.getImgSize().getThImgheight()))  //再生成一张 200*200 的缩略图
                .upload();
    }


}
