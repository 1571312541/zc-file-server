package com.z.file.admin.vo;

import lombok.Data;

/**
 * 〈〉
 *
 * @author zchao
 * @create 2021/10/28
 */
@Data
public class FilePretreatment {

    /**
     * 要上传到的平台
     */
    private String platform;
    /**
     * 原始文件名
     */
    private String originalFilename;
    /**
     * 文件所属对象id
     */
    private String objectId;
    /**
     * 文件所属对象类型
     */
    private String objectType;
    /**
     * 备注
     */
    private String remark;
    /**
     * 文件存储路径
     */
    private String path = "";

    /**
     * 保存文件名，如果不设置则自动生成，注意此文件名不含后缀
     */
    private String saveFilename;

    /**
     * 缩略图的保存文件名，注意此文件名不含后缀
     */
    private String saveThFilename;
    /**
     * 是否产生缩略图
     */
    private Boolean thumbnail = true;
    /**
     * 缩略图宽度
     */
    private Integer thumWidth = 200;
    /**
     * 缩略图高度
     */
    private Integer thumHeight = 200;
}
