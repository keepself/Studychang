package com.changseon.StudyWithChang.repository;

import com.changseon.StudyWithChang.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
