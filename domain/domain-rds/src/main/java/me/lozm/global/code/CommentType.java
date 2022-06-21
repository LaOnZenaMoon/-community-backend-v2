package me.lozm.global.code;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum CommentType {

    GENERAL("GENERAL", "일반"),
    NOTICE("NOTICE", "공지"),
    EVENT("EVENT", "이벤트");

    private String code;
    private String description;

    CommentType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static CommentType findCode(String code) {
        return Arrays.stream(CommentType.values())
                .filter(v -> v.getCode().equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException());
    }

}
