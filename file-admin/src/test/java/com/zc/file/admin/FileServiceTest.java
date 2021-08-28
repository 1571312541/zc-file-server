package com.zc.file.admin;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import com.zc.file.FileInfo;
import com.zc.file.FileService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;


@Slf4j
@SpringBootTest
class FileServiceTest {

    @Autowired
    private FileService fileService;

    /**
     * 单独对文件上传进行测试
     */
    @Test
    public void upload() {

        String filename = "image.jpg";
        InputStream in = this.getClass().getClassLoader().getResourceAsStream(filename);
        FileInfo fileInfo = fileService.build(in).setPath("aab/")
                .setPlatform("local-2")
                .setName("file")
                .setOriginalFilename(filename)
                .thumbnail().upload();
        Assert.notNull(fileInfo,"文件上传失败！");
        log.info("文件上传成功：{}",fileInfo.toString());
    }

    /**
     * 测试根据 url 上传文件
     */
    @Test
    public void uploadByURL() throws MalformedURLException {

        URL url = new URL("https://www.xuyanwu.cn/file/upload/1566046282790-1.png");

        FileInfo fileInfo = fileService.build(url).thumbnail().setOriginalFilename("1566046282790-1.png").setPath("test/").setObjectId("0").setObjectType("0").upload();
        Assert.notNull(fileInfo,"文件上传失败！");
        log.info("文件上传成功：{}",fileInfo.toString());
    }

    /**
     * 测试上传并删除文件
     */
    @Test
    public void delete() {
        String filename = "image.jpg";
        InputStream in = this.getClass().getClassLoader().getResourceAsStream(filename);

        FileInfo fileInfo = fileService.build(in).setOriginalFilename(filename).setPath("test/").setObjectId("0").setObjectType("0").thumbnail(200,200).upload();
        Assert.notNull(fileInfo,"文件上传失败！");
        boolean delete = fileService.delete(fileInfo.getUrl());
        Assert.isTrue(delete,"文件删除失败！" + fileInfo.getUrl());
        log.info("文件删除成功：{}",fileInfo);
    }

    /**
     * 测试上传并验证文件是否存在
     */
    @Test
    public void exists() {
        String filename = "image.jpg";
        InputStream in = this.getClass().getClassLoader().getResourceAsStream(filename);
        FileInfo fileInfo = fileService.build(in).setOriginalFilename(filename).setPath("test/").setObjectId("0").setObjectType("0").upload();
        Assert.notNull(fileInfo,"文件上传失败！");
        boolean exists = fileService.exists(fileInfo);
        log.info("文件是否存在，应该存在，实际为：{}，文件：{}",exists,fileInfo);
        Assert.isTrue(exists,"文件是否存在，应该存在，实际为：{}，文件：{}",exists,fileInfo);

        fileInfo = BeanUtil.copyProperties(fileInfo,FileInfo.class);
        fileInfo.setFilename(fileInfo.getFilename() + "111.cc");
        fileInfo.setUrl(fileInfo.getUrl() + "111.cc");
        exists = fileService.exists(fileInfo);
        log.info("文件是否存在，不该存在，实际为：{}，文件：{}",exists,fileInfo);
        Assert.isFalse(exists,"文件是否存在，不该存在，实际为：{}，文件：{}",exists,fileInfo);
    }


    /**
     * 测试上传并下载文件
     */
    @Test
    public void download() {
        String filename = "image.jpg";
        InputStream in = this.getClass().getClassLoader().getResourceAsStream(filename);

        FileInfo fileInfo = fileService.build(in).setOriginalFilename(filename).setPath("test/").setObjectId("0").setObjectType("0").setSaveFilename("aaa.jpg").setSaveThFilename("bbb").thumbnail(200,200).upload();
        Assert.notNull(fileInfo,"文件上传失败！");

        byte[] bytes = fileService.download(fileInfo).setProgressMonitor((progressSize, allSize) ->
                log.info("文件下载进度：{} {}%",progressSize,progressSize * 100 / allSize)
        ).bytes();
        Assert.notNull(bytes,"文件下载失败！");
        log.info("文件下载成功，文件大小：{}",bytes.length);

        byte[] thBytes = fileService.downloadTh(fileInfo).setProgressMonitor((progressSize, allSize) ->
                log.info("缩略图文件下载进度：{} {}%",progressSize,progressSize * 100 / allSize)
        ).bytes();
        Assert.notNull(thBytes,"缩略图文件下载失败！");
        log.info("缩略图文件下载成功，文件大小：{}",thBytes.length);


    }

}
