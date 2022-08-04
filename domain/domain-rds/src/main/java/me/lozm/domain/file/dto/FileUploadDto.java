package me.lozm.domain.file.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.lozm.domain.file.code.FileUploadType;

import javax.validation.constraints.NotNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileUploadDto {

    @Getter
    @AllArgsConstructor
    public static class Request {
        @NotNull
        private FileUploadType uploadType;
        @NotNull
        private Long targetId;
    }

    @Getter
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Response {
        private final String fileId;
        private final String fileName;
        private final FileUploadType uploadType;
        private final Long targetId;
    }

}
