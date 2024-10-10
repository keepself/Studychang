package com.changseon.StudyWithChang.service;

import com.changseon.StudyWithChang.domain.Member;
import com.changseon.StudyWithChang.dto.MemberRegisterRequest;
import com.changseon.StudyWithChang.global.error.InvalidInputException;
import com.changseon.StudyWithChang.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    //회원가입
    public String signUp(MemberRegisterRequest request) {
        // 이미 존재하는 loginId가 있을 경우 예외 처리
        Optional<Member> existingMember = memberRepository.findByLoginId(request.getLoginId());
        if (existingMember.isPresent()) {
            throw new InvalidInputException("loginId", "중복된 아이디입니다.");
        }

        Member member = request.toEntity();
        memberRepository.save(member);
        return "가입되었습니다.";
    }
}
