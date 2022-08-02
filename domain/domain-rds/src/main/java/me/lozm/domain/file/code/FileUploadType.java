package me.lozm.domain.file.code;

import lombok.Getter;
import me.lozm.code.EnumModel;

import java.util.Arrays;

@Getter
public enum FileUploadType implements EnumModel {

    BOARD("BOARD", "게시글 파일"),
    COMMENT("COMMENT", "댓글 파일"),
    ;

    private final String code;
    private final String description;


    FileUploadType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static FileUploadType findCode(String code) {
        return Arrays.stream(FileUploadType.values())
                .filter(v -> v.getCode().equals(code))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public String getKey() {
        return null;
    }

    @Override
    public String getValue() {
        return null;
    }
}
