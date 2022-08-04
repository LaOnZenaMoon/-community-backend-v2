package me.lozm.domain.file.vo;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.lozm.domain.file.code.FileUploadType;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileUploadVo {

    @Getter
    public static class Request {
        private final MultipartFile uploadFile;
        private final FileUploadType uploadType;
        private final Long targetId;

        public Request(MultipartFile uploadFile, FileUploadType uploadType, Long targetId) {
            Assert.notNull(uploadFile, "업로드 파일은 null 일 수 없습니다.");
            Assert.notNull(uploadType, "업로드 유형은 null 일 수 없습니다.");
            Assert.notNull(targetId, "대상 ID 는 null 일 수 없습니다.");

            this.uploadFile = uploadFile;
            this.uploadType = uploadType;
            this.targetId = targetId;
        }
    }

    @Getter
    public static class Response {
        private final String fileId;
        private final String fileName;
        private final FileUploadType uploadType;
        private final Long targetId;

        @Builder
        public Response(String fileId, String fileName, FileUploadType uploadType, Long targetId) {
            this.fileId = fileId;
            this.fileName = fileName;
            this.uploadType = uploadType;
            this.targetId = targetId;
        }
    }

}
