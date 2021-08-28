package com.zc.file.admin.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

import static com.zc.file.admin.config.ZcFileCons.SystemType.MAC;
import static com.zc.file.admin.config.ZcFileCons.SystemType.WIN;

@Data
@TableName(value = "z_file_config")
public class ZFileConfig {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
    * 储存类型
    */
    @TableField(value = "client_type")
    private String clientType;

    /**
    * 储存名称
    */
    @TableField(value = "client_name")
    private String clientName;

    /**
    * 用户
    */
    @TableField(value = "access_key")
    private String accessKey;

    /**
    * 密码
    */

    @TableField(value = "secret_key")
    private String secretKey;

    /**
    * 例如 http://192.168.1.112:9000
    */
    @TableField(value = "endpoint")
    private String endpoint;

    /**
    * 存仓库所在地域
    */
    @TableField(value = "region")
    private String region;
    /**
    * 访问域名
    */
    @TableField(value = "domain")
    private String domain;

    /**
    * bucket名称
    */
    @TableField(value = "bucket")
    private String bucket;
    /**
    * bucket名称
    */
    @TableField(value = "path_patterns")
    private String pathPatterns;

    /**
    * 基础路径
    */
    @TableField(value = "base_path")
    private String basePath;
    /**
    * MAC下基础路径，只在local下起作用
    */
    @TableField(value = "mac_base_path")
    private String macBasePath;

    /**
    * LINUX下基础路径，只在local下起作用
    */
    @TableField(value = "linux_base_path")
    private String linuxBasePath;

    /**
    * 1启用 0禁用
    */
    @TableField(value = "status")
    private Integer status;

    @TableField(value = "create_time")
    private Date createTime;

    @TableField(value = "update_time")
    private Date updateTime;

    public String getBasePath(){
        String os = System.getProperty("os.name");
        if(os.toLowerCase().startsWith(WIN)) {
            return basePath;
        } else if(os.toLowerCase().startsWith(MAC)){
            return macBasePath;
        }
        return linuxBasePath;
    }
}
