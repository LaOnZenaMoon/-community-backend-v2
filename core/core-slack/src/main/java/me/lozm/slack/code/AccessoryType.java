package me.lozm.slack.code;

import lombok.Getter;

@Getter
public enum AccessoryType {

    IMAGE("image", "이미지"),
    BUTTON("button", "버튼"),
    ;

    private final String code;
    private final String description;

    AccessoryType(String code, String description) {
        this.code = code;
        this.description = description;
    }

}
