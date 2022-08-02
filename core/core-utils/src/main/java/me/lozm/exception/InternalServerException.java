package me.lozm.exception;

import lombok.Getter;

@Getter
public class InternalServerException extends RuntimeException {

    private CustomExceptionType type;

    public InternalServerException(CustomExceptionType type) {
        super(type.getMessage());
        this.type = type;
    }

    public InternalServerException(CustomExceptionType type, String message) {
        super(message);
        this.type = type;
    }

}
