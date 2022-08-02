package me.lozm.domain.file.vo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.lozm.domain.file.code.FileUploadType;
import org.springframework.util.Assert;

import java.io.File;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileUploadVo {

    @Getter
    public static class Request {
        private final File uploadFile;
        private final FileUploadType uploadType;
        private final Long targetId;

        public Request(File uploadFile, FileUploadType uploadType, Long targetId) {
            Assert.notNull(uploadFile, "업로드 파일은 null 일 수 없습니다.");
            Assert.notNull(uploadType, "업로드 유형은 null 일 수 없습니다.");
            Assert.notNull(targetId, "대상 ID 는 null 일 수 없습니다.");

            this.uploadFile = uploadFile;
            this.uploadType = uploadType;
            this.targetId = targetId;
        }
    }

    @Getter
    @AllArgsConstructor
    public static class Response {
        private final Long fileId;
        private final FileUploadType uploadType;
        private final Long targetId;
    }

}
