package com.zc.file.aspect;

import com.zc.file.FileInfo;
import com.zc.file.platform.FileStorage;

/**
 * 文件是否存在切面调用链结束回调
 */
public interface ExistsAspectChainCallback {
    boolean run(FileInfo fileInfo, FileStorage fileStorage);
}
