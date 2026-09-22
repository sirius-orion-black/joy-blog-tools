package com.joy.config.file;

import com.joy.mapper.infra.FilesRecordMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.recorder.FileRecorder;
import org.dromara.x.file.storage.core.upload.FilePartInfo;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class XFileStorageRecorder implements FileRecorder {
    //预留，以后要下载也好，用oss也好，会用的上

    private final FilesRecordMapper filesMapper;


    @Override
    public boolean save(FileInfo fileInfo) {
        return false;
    }

    @Override
    public void update(FileInfo fileInfo) {

    }

    @Override
    public FileInfo getByUrl(String s) {
        return null;
    }

    @Override
    public boolean delete(String s) {
        return false;
    }

    @Override
    public void saveFilePart(FilePartInfo filePartInfo) {

    }

    @Override
    public void deleteFilePartByUploadId(String s) {

    }
}