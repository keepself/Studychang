package com.changseon.StudyWithChang.controller;


import com.changseon.StudyWithChang.dto.MemberRegisterRequest;
import com.changseon.StudyWithChang.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public String signup(@RequestBody MemberRegisterRequest request) {
        return memberService.signUp(request);
    }
}