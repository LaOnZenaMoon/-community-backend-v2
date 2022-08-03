package me.lozm.app.file.service;

import me.lozm.domain.file.vo.FileUploadVo;

import java.io.InputStream;

public interface FileService {
    FileUploadVo.Response uploadFile(FileUploadVo.Request requestVo);

    java.io.File uploadFileToServer(String fileName, InputStream inputStream);
}
