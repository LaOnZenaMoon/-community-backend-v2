package me.lozm.global.code;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum SearchType {

    TITLE("title", "제목"),
    ;

    private String code;
    private String description;

    SearchType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static SearchType findCode(String code) {
        return Arrays.stream(SearchType.values())
                .filter(v -> v.getCode().equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException());
    }

}
