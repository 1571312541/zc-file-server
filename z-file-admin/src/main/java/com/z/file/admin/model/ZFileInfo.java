package com.z.file.admin.model;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "z_file_info")
public class ZFileInfo implements Serializable {

    /**
     * 文件id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 文件访问地址
     */
    @TableField(value = "url")
    private String url;

    /**
     * 文件大小，单位字节
     */
    @TableField(value = "size")
    private Long size;

    /**
     * 文件名称
     */
    @TableField(value = "filename")
    private String filename;

    /**
     * 原始文件名
     */
    @TableField(value = "original_filename")
    private String originalFilename;

    /**
     * 基础存储路径
     */
    @TableField(value = "base_path")
    private String basePath;

    /**
     * 存储路径
     */
    @TableField(value = "path")
    private String path;

    /**
     * 文件扩展名
     */
    @TableField(value = "suffix")
    private String suffix;

    /**
     * 文件类型
     */
    @TableField(value = "file_type")
    private String fileType;

    /**
     * 存储平台
     */
    @TableField(value = "client")
    private String client;

    /**
     * 缩略图访问路径
     */
    @TableField(value = "th_url")
    private String thUrl;

    /**
     * 缩略图名称
     */
    @TableField(value = "th_filename")
    private String thFilename;

    /**
     * 缩略图大小，单位字节
     */
    @TableField(value = "th_size")
    private Long thSize;

    /**
     * 文件所属对象id
     */
    @TableField(value = "object_id")
    private String objectId;

    /**
     * 文件所属对象类型，例如用户头像，评价图片
     */
    @TableField(value = "object_type")
    private String objectType;
    /**
     * 上传开始时间
     */
    @TableField(value = "upload_start_time")
    private Date uploadStartTime;

    /**
     * 上传结束时间
     */
    @TableField(value = "upload_end_time")
    private Date uploadEndTime;
    /**
     * 上传使用时间
     */
    @TableField(value = "use_time")
    private Long useTime;
    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;
    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;
    /**
     * 修改时间
     */
    @TableField(value = "update_time")
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
