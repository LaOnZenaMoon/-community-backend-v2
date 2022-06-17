package me.lozm.global.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import me.lozm.utils.exception.CustomExceptionType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isEmpty;

@Slf4j
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonResponseDto<T> {

    private static final String TIMESTAMP_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";

    private String code;
    private String message;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = TIMESTAMP_PATTERN)
    private LocalDateTime timestamp;
    private T data;

    @Builder
    public CommonResponseDto(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    public static CommonResponseDto success() {
        return CommonResponseDto.builder()
                .build();
    }

    public static CommonResponseDto success(Object object) {
        return CommonResponseDto.builder()
                .data(object)
                .build();
    }

    public static ResponseEntity<Object> badRequest(String message) {
        CommonResponseDto<Object> response = CommonResponseDto.builder()
                .code(CustomExceptionType.INVALID_PARAM.getCode())
                .message(isEmpty(message) ? CustomExceptionType.INVALID_PARAM.getMessage() : message)
                .build();
        return getObjectResponseEntity(response);
    }
    public static ResponseEntity<Object> badRequest(CustomExceptionType type, String additionalMessage) {
        CommonResponseDto<Object> response = CommonResponseDto.builder()
                .code(type.getCode())
                .message(isEmpty(additionalMessage) ? type.getMessage() : format("%s %s", type.getMessage(), additionalMessage))
                .build();
        return getObjectResponseEntity(response);
    }

    public static ResponseEntity<Object> badRequest(CustomExceptionType type, String message, Object data) {
        CommonResponseDto<Object> response = CommonResponseDto.builder()
                .code(type.getCode())
                .message(message)
                .data(data)
                .build();
        return getObjectResponseEntity(response);
    }

    public static ResponseEntity<Object> badRequest(CustomExceptionType type, BindingResult bindingResult) {
        CommonResponseDto<Object> response = CommonResponseDto.builder()
                .code(type.getCode())
                .message(type.getMessage())
                .data(FieldError.of(bindingResult))
                .build();
        return getObjectResponseEntity(response);
    }

    private static ResponseEntity<Object> getObjectResponseEntity(CommonResponseDto<Object> response) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class FieldError {
        private String field;
        private String value;
        private String reason;

        private FieldError(final String field, final String value, final String reason) {
            this.field = field;
            this.value = value;
            this.reason = reason;
        }

        public static List<FieldError> of(final String field, final String value, final String reason) {
            List<FieldError> fieldErrors = new ArrayList<>();
            fieldErrors.add(new FieldError(field, value, reason));
            return fieldErrors;
        }

        private static List<FieldError> of(final BindingResult bindingResult) {
            final List<org.springframework.validation.FieldError> fieldErrors = bindingResult.getFieldErrors();
            return fieldErrors.stream()
                    .map(error -> new FieldError(
                            error.getField(),
                            error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
                            error.getDefaultMessage()))
                    .collect(Collectors.toList());
        }
    }
}