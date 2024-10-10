package com.changseon.StudyWithChang.global.common.authority;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

@Data
public class TokenInfo {
    private String grantType;
    private String accessToken;

    public TokenInfo(String grantType, String accessToken) {
        this.grantType = grantType;
        this.accessToken = accessToken;
    }
}