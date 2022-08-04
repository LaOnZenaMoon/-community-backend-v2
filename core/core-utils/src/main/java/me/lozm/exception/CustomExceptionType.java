package me.lozm.exception;

import lombok.Getter;

@Getter
public enum CustomExceptionType {

    // Common
    INTERNAL_SERVER_ERROR("SERVER_001", "요청을 처리하는 중에 오류가 있습니다."),
    INVALID_REQUEST_PARAMETERS("PARAMS_001", "요청값이 잘못되었습니다."),
    USER_NOT_FOUND("USER_001", "존재하지 않는 사용자입니다."),

    // Authorize & Authenticate
    INVALID_USER_LOGIN_ID("SECURITY_001", "유효하지 않은 사용자 아이디입니다."),
    INVALID_USER_PASSWORD("SECURITY_002", "아이디 또는 비밀번호를 잘못 입력하셨습니다. 다시 확인해주세요."),
    ACCESS_TOKEN_EXPIRED("SECURITY_003", "인증 토큰이 만료되었습니다."),
    INVALID_ACCESS_TOKEN("SECURITY_004", "유효하지 않은 인증 토큰입니다."),
    AUTH_INTERNAL_SERVER_ERROR("SECURITY_005", "인증 진행 시 오류가 발생되었습니다."),
    INVALID_GRANT_TYPE("SECURITY_006", "유효하지 않은 Grant 타입입니다."),
    INVALID_SCOPE("SECURITY_006", "유효하지 않은 Scope 입니다."),

    // Boards and comments
    INVALID_HIERARCHY_TYPE("HIERARCHY_001", "계층 유형이 잘못되었습니다."),

    // Files
    INTERNAL_SERVER_ERROR_FILES("SERVER_002", "요청을 처리하는 중에 오류가 있습니다."),
    ;

    private final String code;
    private final String message;


    CustomExceptionType(String code, String message) {
        this.code = code;
        this.message = message;
    }


    public static CustomExceptionType getOAuth2Exception(String message) {
        if (message.contains("Unsupported grant type") || message.contains("Unauthorized grant type")) {
            return CustomExceptionType.INVALID_GRANT_TYPE;
        } else if (message.contains("유효하지 않은 사용자입니다.")) {
            return CustomExceptionType.INVALID_USER_LOGIN_ID;
        } else if (message.contains("Bad credentials")) {
            return CustomExceptionType.INVALID_USER_PASSWORD;
        } else if (message.contains("Invalid scope")) {
            return CustomExceptionType.INVALID_SCOPE;
        } else if (message.contains("Access token expired") || message.contains("Token has expired")) {
            return CustomExceptionType.ACCESS_TOKEN_EXPIRED;
        } else if (message.contains("Cannot convert access token to JSON")) {
            return CustomExceptionType.INVALID_ACCESS_TOKEN;
        } else {
            return CustomExceptionType.AUTH_INTERNAL_SERVER_ERROR;
        }
    }

}
