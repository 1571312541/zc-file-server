package com.z.file.aspect;

import com.z.file.entity.FileInfo;
import com.z.file.entity.UploadPretreatment;
import com.z.file.platform.FileStorage;
import com.z.file.recorder.FileRecorder;
import lombok.Getter;
import lombok.Setter;

import java.util.Iterator;

/**
 * 上传的切面调用链
 */
@Getter
@Setter
public class UploadAspectChain {

    private UploadAspectChainCallback callback;
    private Iterator<FileStorageAspect> aspectIterator;

    public UploadAspectChain(Iterable<FileStorageAspect> aspects,UploadAspectChainCallback callback) {
        this.aspectIterator = aspects.iterator();
        this.callback = callback;
    }

    /**
     * 调用下一个切面
     */
    public FileInfo next(FileInfo fileInfo, UploadPretreatment pre, FileStorage fileStorage, FileRecorder fileRecorder) {
        if (aspectIterator.hasNext()) {//还有下一个
            return aspectIterator.next().uploadAround(this,fileInfo,pre,fileStorage,fileRecorder);
        } else {
            return callback.run(fileInfo,pre,fileStorage,fileRecorder);
        }
    }
}
