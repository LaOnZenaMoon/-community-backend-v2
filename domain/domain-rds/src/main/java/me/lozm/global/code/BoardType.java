package me.lozm.global.code;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum BoardType {

    ALL("ALL", "전체"),
    NEWS("NEWS", "뉴스"),
    MAGAZINE("MAGAZINE", "잡지"),
    FREE_CONTENTS("FREE_CONTENTS", "자유게시판"),
    MULTIMEDIA("MULTIMEDIA", "멀티미디어"),
    MARKET("MARKET", "마켓");

    private String code;
    private String description;

    BoardType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static BoardType findCode(String code) {
        return Arrays.stream(BoardType.values())
                .filter(v -> v.getCode().equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException());
    }

}
