package me.lozm.app.file.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.lozm.app.file.service.FileService;
import me.lozm.domain.file.dto.FileUploadDto;
import me.lozm.domain.file.mapper.FileMapper;
import me.lozm.domain.file.vo.FileUploadVo;
import me.lozm.exception.CustomExceptionType;
import me.lozm.exception.InternalServerException;
import me.lozm.global.model.CommonResponseDto;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

@Slf4j
@RequestMapping("files")
@RestController
@RequiredArgsConstructor
public class FileController {

    private final Environment environment;
    private final FileService fileService;
    private final FileMapper fileMapper;


    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CommonResponseDto<FileUploadDto.Response>> uploadFile(
            @RequestPart("requestDto") @Validated FileUploadDto.Request requestDto,
            @RequestPart("uploadFile") MultipartFile uploadFile) {

        File uploadedFile = uploadFileToServer(uploadFile);
        FileUploadVo.Request requestVo = fileMapper.toUploadVo(uploadedFile, requestDto);
        FileUploadVo.Response responseVo = fileService.uploadFile(requestVo);
        FileUploadDto.Response responseDto = fileMapper.toUploadDto(responseVo);
        return CommonResponseDto.created(responseDto);
    }

    private File uploadFileToServer(MultipartFile multipartFile) {
        final String filePath = environment.getProperty("file.upload-path") + File.separator + multipartFile.getOriginalFilename();

        try {
            Path createDirectories = Files.createDirectories(Paths.get(filePath)
                    .toAbsolutePath()
                    .normalize());
            Files.copy(multipartFile.getInputStream(), createDirectories, StandardCopyOption.REPLACE_EXISTING);
            return new File(filePath);

        } catch (FileAlreadyExistsException e) {
            log.info(e.getMessage());
            return new File(filePath);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new InternalServerException(CustomExceptionType.INTERNAL_SERVER_ERROR, e);
        }
    }

}
