package com.movewave.common.exception.base;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorMessages {
    BAD_REQUEST("E400-001", "Invalid Request"); // IllegalArgumentException

    private final String code;
    private final String message;

    public String format(Object... args) {
        return String.format(this.message, args);
    }

}