package com.zc.file.aspect;

import com.zc.file.FileInfo;
import com.zc.file.UploadPretreatment;
import com.zc.file.platform.FileStorage;
import com.zc.file.recorder.FileRecorder;

/**
 * 上传切面调用链结束回调
 */
public interface UploadAspectChainCallback {
    FileInfo run(FileInfo fileInfo, UploadPretreatment pre, FileStorage fileStorage, FileRecorder fileRecorder);
}
