package me.lozm.global.code;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ResourceType {

    URL("URL", "HTTP 요청 URL"),
    METHOD("METHOD", "HTTP Method");

    private String code;
    private String description;

    ResourceType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static ResourceType findCode(String code) {
        return Arrays.stream(ResourceType.values())
                .filter(v -> v.getCode().equals(code))
                .findAny()
                .orElse(URL);
    }

}
