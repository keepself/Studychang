package com.changseon.StudyWithChang.global.error;

import lombok.Getter;

@Getter
public enum ResultCode {
    SUCCESS(1, "정상 처리 되었습니다."),
    ERROR(0, "에러가 발생했습니다.");

    private final int result;
    private final String msg;

    ResultCode(int result, String msg) {
        this.result = result;
        this.msg = msg;
    }
}
