package com.z.file.admin.config;

/**
 * 〈〉
 *
 * @author zc
 * @date 2021/8/27 0027
 */
public class ZcFileCons {

    private ZcFileCons() {

    }
    public static class SystemType{
        /**
         * win 系统
         */
        public static final String WIN = "win";

        /**
         * mac 系统
         */
        public static final String MAC = "mac";
    }
    public static class SourceType{
        public static final String SOURCE_TYPE_LOCAL = "local";
        public static final String SOURCE_TYPE_MINIO = "minio";
        public static final String SOURCE_TYPE_ALIYUN = "aliyun";
        public static final String SOURCE_TYPE_BAIDUYUN = "baiduyun";
        public static final String SOURCE_TYPE_AWSS3 = "awss3";
        public static final String SOURCE_TYPE_HUAWEIYUN = "huaweiyun";
        public static final String SOURCE_TYPE_QINIUYUN = "qiniuyun";
        public static final String SOURCE_TYPE_TENGXUNYUN = "tengxunyun";
        public static final String SOURCE_TYPE_YOUPAIYUN = "youpaiyun";
    }
}
