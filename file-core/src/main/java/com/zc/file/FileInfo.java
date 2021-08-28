package com.zc.file;


import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class FileInfo implements Serializable {

    /**
     * 文件id
     */
    private Integer id;

    /**
     * 文件访问地址
     */
    private String url;

    /**
     * 文件大小，单位字节
     */
    private Long size;

    /**
     * 文件名称
     */
    private String filename;

    /**
     * 原始文件名
     */
    private String originalFilename;

    /**
     * 基础存储路径
     */
    private String basePath;

    /**
     * 存储路径
     */
    private String path;

    /**
     * 文件扩展名
     */
    private String suffix;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 存储平台
     */
    private String client;

    /**
     * 存储平台
     */
    private String clientType;

    /**
     * 缩略图访问路径
     */
    private String thUrl;

    /**
     * 缩略图名称
     */
    private String thFilename;

    /**
     * 缩略图大小，单位字节
     */
    private Long thSize;

    /**
     * 文件所属对象id
     */
    private String objectId;

    /**
     * 文件所属对象类型，例如用户头像，评价图片
     */
    private String objectType;
    /**
     * 上传开始时间
     */
    private Date uploadStartTime;

    /**
     * 上传结束时间
     */
    private Date uploadEndTime;
    /**
     * 上传使用时间
     */
    private Long useTime;
    /**
     * 备注
     */
    private String remark;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
    public long getUseTime() {
        Date uploadEndTime = this.getUploadEndTime();
        Date uploadStartTime = this.getUploadStartTime();
        if (null == uploadStartTime || null == uploadEndTime) {
            return -1;
        }
        return uploadEndTime.getTime() - uploadStartTime.getTime();
    }
}
