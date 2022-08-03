package me.lozm.domain.file.mapper;

import me.lozm.domain.file.dto.FileUploadDto;
import me.lozm.domain.file.vo.FileUploadVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.web.multipart.MultipartFile;

@Mapper(componentModel = "spring")
public interface FileMapper {

    @Mapping(source = "uploadFile", target = "uploadFile")
    @Mapping(source = "requestDto.uploadType", target = "uploadType")
    @Mapping(source = "requestDto.targetId", target = "targetId")
    FileUploadVo.Request toUploadVo(MultipartFile uploadFile, FileUploadDto.Request requestDto);

    FileUploadDto.Response toUploadDto(FileUploadVo.Response responseVo);
}
