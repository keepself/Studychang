package com.changseon.StudyWithChang.repository;

import com.changseon.StudyWithChang.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findAllByDelYNOrderByCreatedAtDesc(Boolean delYN, Pageable pageable);

    Optional<Post> findByPostId(long postId);
}



