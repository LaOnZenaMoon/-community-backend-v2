package me.lozm.global.code;

import me.lozm.utils.code.EnumModel;

public enum HierarchyType implements EnumModel {

    ORIGIN("ORIGIN", "원본"),
    REPLY_FOR_ORIGIN("REPLY_FOR_ORIGIN", "원본에 대한 답변"),
    REPLY_FOR_REPLY("REPLY_FOR_REPLY", "답변에 대한 답변")
    ;

    private final String code;
    private final String description;

    HierarchyType(String code, String description) {
        this.code = code;
        this.description = description;
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
