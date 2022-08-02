package me.lozm.app.file.service;

import me.lozm.domain.file.vo.FileUploadVo;

public interface FileService {
    FileUploadVo.Response uploadFile(FileUploadVo.Request requestVo);
}
