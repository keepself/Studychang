package com.changseon.StudyWithChang.global.error;


import lombok.Getter;

@Getter
public class InvalidInputException extends RuntimeException {
    private final String fieldName;
    private final String message;

    public InvalidInputException(String fieldName, String message) {
        super(message);
        this.fieldName = fieldName;
        this.message = message;
    }
}