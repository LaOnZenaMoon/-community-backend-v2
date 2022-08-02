package me.lozm.domain.board.code;

import lombok.Getter;
import me.lozm.code.EnumModel;

import java.util.Arrays;

@Getter
public enum ContentType implements EnumModel {

    GENERAL("GENERAL", "일반"),
    NOTICE("NOTICE", "공지"),
    EVENT("EVENT", "이벤트");

    private final String code;
    private final String description;

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

    @Override
    public String getKey() {
        return code;
    }

    @Override
    public String getValue() {
        return description;
    }

}
