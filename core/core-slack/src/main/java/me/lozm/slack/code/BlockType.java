package me.lozm.slack.code;

import lombok.Getter;

@Getter
public enum BlockType {

    SECTION("section", "섹션"),
    ACTION("action", "액션"),
    DIVIDER("divider", "구분자"),
    CONTEXT("context", "문맥"),
    ;

    private final String code;
    private final String description;

    BlockType(String code, String description) {
        this.code = code;
        this.description = description;
    }

}
