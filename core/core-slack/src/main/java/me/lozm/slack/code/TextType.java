package me.lozm.slack.code;

import lombok.Getter;

@Getter
public enum TextType {

    PLAIN_TEXT("plain_text", "평문"),
    IMAGE("image", "이미지"),
    MARK_DOWN("mrkdwn", "마크 다운")
    ;

    private final String code;
    private final String description;

    TextType(String code, String description) {
        this.code = code;
        this.description = description;
    }

}
