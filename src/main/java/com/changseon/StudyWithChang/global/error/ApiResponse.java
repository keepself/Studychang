package com.changseon.StudyWithChang.global.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Map;


@Getter
public class ApiResponse<T> {

    private final DataHeader dataHeader;
    private final T data;
    private final T error;

    public ApiResponse(DataHeader dataHeader, T data, T error) {
        this.dataHeader = dataHeader;
        this.data = data;
        this.error = error;
    }

    public static <T> ApiResponse<?> ok(T data) {
        return new ApiResponse<>(DataHeader.ok(), data, null);
    }

    public static ApiResponse<?> error(HttpStatus status, Map<String, String> errors) {
        return new ApiResponse<>(DataHeader.error(status), null, errors);
    }
}