package com.changseon.StudyWithChang.global.error;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DataHeader {
    private int successCode;
    private String resultCode;
    private String resultMessage;

    public DataHeader(int successCode, String resultCode, String resultMessage) {
        this.successCode = successCode;
        this.resultCode = resultCode;
        this.resultMessage = resultMessage;
    }

    public static DataHeader ok() {
        return new DataHeader(ResultCode.SUCCESS.getResult(),
                HttpStatus.OK.value() + " " + HttpStatus.OK.name(),
                ResultCode.SUCCESS.getMsg());
    }

    public static DataHeader error(HttpStatus status) {
        return new DataHeader(ResultCode.ERROR.getResult(),
                status.value() + " " + status.name(),
                ResultCode.ERROR.getMsg());
    }
}