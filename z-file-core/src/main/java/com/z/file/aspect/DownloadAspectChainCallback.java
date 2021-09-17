package com.z.file.aspect;

import com.z.file.FileInfo;
import com.z.file.platform.FileStorage;

import java.io.InputStream;
import java.util.function.Consumer;

/**
 * 下载切面调用链结束回调
 */
public interface DownloadAspectChainCallback {
    void run(FileInfo fileInfo, FileStorage fileStorage, Consumer<InputStream> consumer);
}
