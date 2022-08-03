package me.lozm.app.file.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.lozm.domain.board.service.BoardHelperService;
import me.lozm.domain.board.service.CommentHelperService;
import me.lozm.domain.file.code.FileUploadType;
import me.lozm.domain.file.entity.File;
import me.lozm.domain.file.repository.FileRepository;
import me.lozm.domain.file.vo.FileUploadVo;
import me.lozm.exception.BadRequestException;
import me.lozm.exception.CustomExceptionType;
import me.lozm.exception.InternalServerException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final BoardHelperService boardHelperService;
    private final CommentHelperService commentHelperService;
    private final FileRepository fileRepository;
    private final Environment environment;


    @Override
    @Transactional
    public FileUploadVo.Response uploadFile(FileUploadVo.Request requestVo) {
        final String originalFileName = requestVo.getUploadFile().getOriginalFilename();
        final String fileExtension = FilenameUtils.getExtension(originalFileName);

        final String randomFileName = UUID.randomUUID().toString();
        uploadFileToServer(requestVo, randomFileName + FilenameUtils.EXTENSION_SEPARATOR + fileExtension);

        final FileUploadType uploadType = requestVo.getUploadType();
        final Long targetId = requestVo.getTargetId();

        if (uploadType == FileUploadType.BOARD) {
            boardHelperService.getBoard(targetId);
        } else if (uploadType == FileUploadType.COMMENT) {
            commentHelperService.getComment(targetId);
        } else {
            throw new BadRequestException(CustomExceptionType.INVALID_REQUEST_PARAMETERS);
        }

        File file = fileRepository.save(File.of(
                randomFileName, FilenameUtils.removeExtension(originalFileName), fileExtension, uploadType, targetId));

        return FileUploadVo.Response.builder()
                .fileId(file.getId())
                .fileName(originalFileName)
                .uploadType(uploadType)
                .targetId(targetId)
                .build();
    }

    @Override
    public java.io.File uploadFileToServer(String fileName, InputStream inputStream) {
        if (isBlank(fileName)) {
            throw new IllegalArgumentException("업로드할 파일명은 비어있을 수 없습니다.");
        }

        final String filePath = environment.getProperty("file.upload-path") + java.io.File.separator + fileName;

        try {
            Path createDirectories = Files.createDirectories(Paths.get(filePath)
                    .toAbsolutePath()
                    .normalize());
            Files.copy(inputStream, createDirectories, StandardCopyOption.REPLACE_EXISTING);
            return new java.io.File(filePath);

        } catch (FileAlreadyExistsException e) {
            log.info(e.getMessage());
            return new java.io.File(filePath);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new InternalServerException(CustomExceptionType.INTERNAL_SERVER_ERROR_FILES, e);
        }
    }

    private java.io.File uploadFileToServer(FileUploadVo.Request requestVo, String randomFileName) {
        java.io.File uploadedFileToServer;
        try {
            uploadedFileToServer = uploadFileToServer(randomFileName, requestVo.getUploadFile().getInputStream());
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new InternalServerException(CustomExceptionType.INTERNAL_SERVER_ERROR_FILES, e);
        }
        return uploadedFileToServer;
    }

}
