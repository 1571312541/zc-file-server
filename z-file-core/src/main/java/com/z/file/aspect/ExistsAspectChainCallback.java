package com.z.file.aspect;

import com.z.file.entity.FileInfo;
import com.z.file.platform.FileStorage;

/**
 * 文件是否存在切面调用链结束回调
 */
public interface ExistsAspectChainCallback {
    boolean run(FileInfo fileInfo, FileStorage fileStorage);
}
