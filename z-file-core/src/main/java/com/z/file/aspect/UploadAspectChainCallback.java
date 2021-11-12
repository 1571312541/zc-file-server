package com.z.file.aspect;

import com.z.file.entity.FileInfo;
import com.z.file.entity.UploadPretreatment;
import com.z.file.platform.FileStorage;
import com.z.file.recorder.FileRecorder;

/**
 * 上传切面调用链结束回调
 */
public interface UploadAspectChainCallback {
    FileInfo run(FileInfo fileInfo, UploadPretreatment pre, FileStorage fileStorage, FileRecorder fileRecorder);
}
