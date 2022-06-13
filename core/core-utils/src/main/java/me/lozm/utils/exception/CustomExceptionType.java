package me.lozm.utils.exception;

import lombok.Getter;

@Getter
public enum CustomExceptionType {

    SERVICE_ERROR("COMMON_001", "요청을 처리하는 중에 오류가 있습니다."),
    INVALID_PARAM("COMMON_002", "유효하지 않은 데이터입니다."),
    ;

    private final String code;
    private final String message;

    CustomExceptionType(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
