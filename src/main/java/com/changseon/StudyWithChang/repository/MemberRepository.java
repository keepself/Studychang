    package com.changseon.StudyWithChang.repository;


    import com.changseon.StudyWithChang.domain.Member;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.stereotype.Repository;

    import java.util.Optional;

    @Repository
    public interface MemberRepository extends JpaRepository<Member, Long> {
        Optional<Member> findByLoginId(String loginId);
    }