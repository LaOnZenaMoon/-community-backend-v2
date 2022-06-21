package me.lozm.global.code;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum UsersType {

    ALL(-0L, "전체"),
    ADMIN(-5L, "관리자"),
    MANAGER(-10L, "매니저"),
    USER(-15L, "사용자"),
    GUEST(-20L, "방문자"),
    SYSTEM(-25L, "시스템");

    private Long code;
    private String description;

    UsersType(Long code, String description) {
        this.code = code;
        this.description = description;
    }

    public static UsersType findCode(Long code) {
        return Arrays.stream(UsersType.values())
                .filter(v -> v.getCode().equals(code))
                .findAny()
                .orElse(USER);
    }

}
