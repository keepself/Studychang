package com.changseon.StudyWithChang.dto;

//생략

import com.changseon.StudyWithChang.domain.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MemberRegisterRequest {

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9_]{5,20}",
            message = "영문, 숫자, _만을 사용하세요")
    private String loginId;
    @NotBlank
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@$%^&*])[a-zA-Z0-9!@$%^&*]{8,20}",
            message = "영문, 숫자, 특수문자를 포함한 10~20자리로 입력해주세요")
    private String password;
    @NotBlank
    @Email(message = "이메일 형식을 확인하세요")
    private String email;
    @NotBlank
    @Pattern(regexp = "^[ㄱ-힣]{1,10}",
            message = "올바른 이름을 입력하세요")
    private String name;

    public Member toEntity() {
        return Member.builder()
                .loginId(this.getLoginId())
                .password(this.getPassword())
                .name(this.getName())
                .email(this.getEmail())
                .build();
    }
}
