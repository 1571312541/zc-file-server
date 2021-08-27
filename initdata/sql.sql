/*
SQLyog Ultimate v12.08 (32 bit)
MySQL - 8.0.19 : Database - zc-file-server
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`zc-file-server` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `zc-file-server`;

/*Table structure for table `z_file_config` */

DROP TABLE IF EXISTS `z_file_config`;

CREATE TABLE `z_file_config` (
  `id` int NOT NULL AUTO_INCREMENT,
  `client_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '储存类型',
  `client_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '储存名称',
  `access_key` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户',
  `secret_key` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '密码',
  `endpoint` varchar(128) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '例如 http://192.168.1.112:9000',
  `region` varchar(128) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '存仓库所在地域',
  `domain` varchar(128) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '访问域名',
  `bucket` varchar(128) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'bucket名称',
  `base_path` varchar(128) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '基础路径',
  `status` int DEFAULT '1' COMMENT '1启用 0禁用',
  `create_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/*Data for the table `z_file_config` */

insert  into `z_file_config`(`id`,`client_type`,`client_name`,`access_key`,`secret_key`,`endpoint`,`region`,`domain`,`bucket`,`base_path`,`status`,`create_time`,`update_time`) values (1,'local','local-1',NULL,NULL,NULL,NULL,NULL,NULL,'F:/code/owner/spring-file-storage-main/upload/',1,'2021-08-26 18:59:37',NULL),(2,'local','local-2',NULL,NULL,NULL,NULL,NULL,NULL,'F:/code/owner/spring-file-storage-main/upload2/',1,'2021-08-26 19:00:04',NULL),(3,'minio','minio-1','minio','minio@123','http://192.168.1.112:9000',NULL,'http://192.168.1.112:9000/test/','test',NULL,1,'2021-08-26 19:00:49',NULL);

/*Table structure for table `z_file_info` */

DROP TABLE IF EXISTS `z_file_info`;

CREATE TABLE `z_file_info` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '文件id',
  `size` bigint DEFAULT NULL COMMENT '文件大小，单位字节',
  `filename` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '文件名称',
  `original_filename` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '原始文件名',
  `base_path` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '基础存储路径',
  `path` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '存储路径',
  `url` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文件访问地址',
  `suffix` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '文件扩展名',
  `file_type` varchar(64) DEFAULT NULL COMMENT '文件类型',
  `client` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '存储平台',
  `th_url` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '缩略图访问路径',
  `th_filename` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '缩略图名称',
  `th_size` bigint DEFAULT NULL COMMENT '缩略图大小，单位字节',
  `object_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '文件所属对象id',
  `object_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '文件所属对象类型，例如用户头像，评价图片',
  `upload_start_time` datetime DEFAULT NULL COMMENT '上传开始时间',
  `upload_end_time` datetime DEFAULT NULL COMMENT '上传结束时间',
  `use_time` longblob COMMENT '使用时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='文件记录表';

/*Data for the table `z_file_info` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
