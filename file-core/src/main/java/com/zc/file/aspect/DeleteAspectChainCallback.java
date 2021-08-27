package com.zc.file.aspect;

import com.zc.file.FileInfo;
import com.zc.file.platform.FileStorage;
import com.zc.file.recorder.FileRecorder;

/**
 * 删除切面调用链结束回调
 */
public interface DeleteAspectChainCallback {
    boolean run(FileInfo fileInfo, FileStorage fileStorage, FileRecorder fileRecorder);
}
