package me.lozm.app.file.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.lozm.app.file.service.FileService;
import me.lozm.domain.file.dto.FileUploadDto;
import me.lozm.domain.file.mapper.FileMapper;
import me.lozm.domain.file.vo.FileUploadVo;
import me.lozm.global.model.CommonResponseDto;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequestMapping("files")
@RestController
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;
    private final FileMapper fileMapper;


    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CommonResponseDto<FileUploadDto.Response>> uploadFile(
            @RequestPart("requestDto") @Validated FileUploadDto.Request requestDto,
            @RequestPart("uploadFile") MultipartFile uploadFile) {

        FileUploadVo.Request requestVo = fileMapper.toUploadVo(uploadFile, requestDto);
        FileUploadVo.Response responseVo = fileService.uploadFile(requestVo);
        FileUploadDto.Response responseDto = fileMapper.toUploadDto(responseVo);
        return CommonResponseDto.created(responseDto);
    }

}
