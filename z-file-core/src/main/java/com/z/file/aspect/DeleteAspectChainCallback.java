package com.z.file.aspect;

import com.z.file.entity.FileInfo;
import com.z.file.platform.FileStorage;
import com.z.file.recorder.FileRecorder;

/**
 * 删除切面调用链结束回调
 */
public interface DeleteAspectChainCallback {
    boolean run(FileInfo fileInfo, FileStorage fileStorage, FileRecorder fileRecorder);
}
