package me.lozm.global.code;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum UseYn {

    USE("Y", "사용"),
    NOT_USE("N", "미사용");

    private String code;
    private String description;

    UseYn(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static UseYn findCode(String code) {
        return Arrays.stream(UseYn.values())
                .filter(v -> v.getCode().equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException());
    }

}
