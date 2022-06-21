package me.lozm.global.code;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ContentType {

    GENERAL("GENERAL", "일반"),
    NOTICE("NOTICE", "공지"),
    EVENT("EVENT", "이벤트");

    private String code;
    private String description;

    ContentType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static ContentType findCode(String code) {
        return Arrays.stream(ContentType.values())
                .filter(v -> v.getCode().equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException());
    }

}
