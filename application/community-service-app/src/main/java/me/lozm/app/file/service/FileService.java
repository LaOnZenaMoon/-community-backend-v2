package me.lozm.app.file.service;

import me.lozm.domain.file.vo.FileUploadVo;
import org.springframework.core.io.Resource;

import java.io.InputStream;

public interface FileService {
    FileUploadVo.Response uploadFile(FileUploadVo.Request requestVo);

    Resource downloadFile(String fileId);

    java.io.File uploadFileToServer(String fileName, InputStream inputStream);
}
