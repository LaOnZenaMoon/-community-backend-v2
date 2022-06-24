package me.lozm.global.code;

import lombok.Getter;
import me.lozm.utils.code.EnumModel;

import java.util.Arrays;

@Getter
public enum BoardType implements EnumModel {

    ALL("ALL", "전체"),
    NEWS("NEWS", "뉴스"),
    MAGAZINE("MAGAZINE", "잡지"),
    FREE_CONTENTS("FREE_CONTENTS", "자유게시판"),
    MULTIMEDIA("MULTIMEDIA", "멀티미디어"),
    MARKET("MARKET", "마켓");

    private final String code;
    private final String description;

    BoardType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static BoardType findCode(String code) {
        return Arrays.stream(BoardType.values())
                .filter(v -> v.getCode().equals(code))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
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
