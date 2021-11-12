#### 发开日志

2021/11/13 删除文件时，会出现拼接 fileInfo.getBasePath() + path，
    而path可能为null，字符串+null 变成 “null”，LocalFileStorage已改，其他平台未改