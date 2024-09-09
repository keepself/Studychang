package com.changseon.StudyWithChang.controller;

import com.changseon.StudyWithChang.domain.Member;
import com.changseon.StudyWithChang.repository.MemberRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MemberController {

    private final MemberRepository memberRepository;

    public MemberController(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @GetMapping("/study")
    public List<Member> Study() {

        return memberRepository.findAll();


    }
}
