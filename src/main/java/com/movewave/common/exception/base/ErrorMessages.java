package com.movewave.common.exception.base;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorMessages {
    /**
     * @see IllegalArgumentException
     */
    BAD_REQUEST("E400-001", "Invalid Request"),
    /**
     * @see EntityNotFoundException
     */
    NOT_FOUND_ENTITY("E404-001", "%s Not Found with ID: %s");

    private final String code;
    private final String message;

    public String format(Object... args) {
        return String.format(this.message, args);
    }

}